package mrdev023.gamestate;

import java.util.ArrayList;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import mrdev023.entity.*;
import mrdev023.gameengine.*;
import mrdev023.gamestate.main.*;
import mrdev023.math.*;
import mrdev023.model.*;
import mrdev023.opengl.*;
import mrdev023.rendering.*;
import mrdev023.rendering.lights.*;
import mrdev023.utils.*;

public class MainMenu extends Game implements IGameState{

	public int time = 0;
	public static final float speed = 1.0f;
	public ArrayList<Light> lights;
	public ArrayList<Mesh> meshs;
	public TextFont text;
	
	public void update() {
		time+=Timer.getDeltaTime();
	}
	
	public void updateKeyboard() {
		if(Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE))GameEngine.setIsRunning(false);
		Camera camera = GameEngine.getCamera();
		Vector3f pos = camera.getPos();
		Vector3f rot = camera.getRot();
		rot.x += -Input.getDMouse().getY() * Settings.sens;
		rot.y += -Input.getDMouse().getX() * Settings.sens;
		if(rot.x > 90)rot.x = 90;
		if(rot.x < -90)rot.x = -90;
		if(Input.isKey(GLFW.GLFW_KEY_W)){
			pos.x += Mathf.cos(Mathf.toRadians(rot.y + 90)) * speed;
			pos.z += Mathf.sin(Mathf.toRadians(rot.y + 90)) * speed;
		}
		if(Input.isKey(GLFW.GLFW_KEY_S)){
			pos.x += -Mathf.cos(Mathf.toRadians(rot.y + 90)) * speed;
			pos.z += -Mathf.sin(Mathf.toRadians(rot.y + 90)) * speed;
		}
		if(Input.isKey(GLFW.GLFW_KEY_A)){
			pos.x += -Mathf.cos(Mathf.toRadians(rot.y)) * speed;
			pos.z += -Mathf.sin(Mathf.toRadians(rot.y)) * speed;
		}
		if(Input.isKey(GLFW.GLFW_KEY_D)){
			pos.x += Mathf.cos(Mathf.toRadians(rot.y)) * speed;
			pos.z += Mathf.sin(Mathf.toRadians(rot.y)) * speed;
		}
		if(Input.isKey(GLFW.GLFW_KEY_LEFT_SHIFT)){
			pos.y -= speed;
		}
		if(Input.isKey(GLFW.GLFW_KEY_SPACE)){
			pos.y += speed;
		}
	}

	public void updateMouse() {
		if(Input.isButtonDown(0))Display.setMouseGrabbed(true);
		if(Input.isButtonDown(1))Display.setMouseGrabbed(false);
	}

	public void init() {
		//Ajout de la lumiere sur la scene
		lights = new ArrayList<Light>();
		lights.add(new AmbientLight(new Vector3f(100,100,100),Color4f.WHITE,Color4f.WHITE));
//		lights.add(new AmbientLight(new Vector3f(100,100,100),Color4f.WHITE,Color4f.WHITE));
		//Ajout des Objets sur la scene
		meshs = new ArrayList<Mesh>();
		meshs.add(new Mesh(MeshBuilder.createFloor(400, Color4f.WHITE,Texture.FLOOR),(new Matrix4f()).loadIdentity()));
		meshs.add(new Mesh(MeshBuilder.createCube(100, Color4f.WHITE,Texture.WOOD),(new Matrix4f()).loadIdentity().tranlate(0, 200, 0)));
		text = new TextFont("Test", 10, 10, 16, Color4f.WHITE);
	}

	public void render2D() {
		
	}

	public void render3D() {
		for(Light light : lights){
			for(Mesh mesh : meshs){
				light.drawLight();
				Shader.MAIN.uniform("transform", mesh.getTransform());
				mesh.getVao().render3D();
			}
		}
	}

	public void renderGUI() {
		text.drawText();
	}

	public void destroy() {
		for(Mesh mesh : meshs)mesh.getVao().destroy();
	}

}
