package mrdev023.opengl;

import static org.lwjgl.opengl.GL11.*;

import mrdev023.gameengine.*;
import mrdev023.math.*;
import mrdev023.rendering.*;

public class DisplayManager {
	
	private static Matrix4f projection = new Matrix4f();

	public static void init(){
		
	}
	
	public static void clearScreen(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Shader.MAIN.bind();
	}
	
	public static void preRender2D(){
		projection.loadIdentity();
		projection.Ortho2D(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode().getHeight(), 1, -1);
		Shader.MAIN.uniform("projection", projection);
	}
	
	public static void preRender3D(){
		projection.loadIdentity();
		projection.perspective(45f, (float)Display.getDisplayMode().getWidth()/(float)Display.getDisplayMode().getHeight(), 0.01f, 1000f);
		Shader.MAIN.uniform("projection", projection);
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
	}
	
	public static void preRenderGUI(){
		projection.loadIdentity();
		projection.Ortho2D(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode().getHeight(), 1, -1);
		Shader.MAIN.uniform("projection", projection);
	}
	
	public static void render2D(){
		GameEngine.getGameState().render2D();
	}
	
	public static void render3D(){
		GameEngine.getCamera().transform();
		Shader.MAIN.uniform("camera", GameEngine.getCamera().getTransformMatrix());
		GameEngine.getGameState().render3D();
	}
	
	public static void renderGUI(){
		GameEngine.getGameState().renderGUI();
	}
		
}
