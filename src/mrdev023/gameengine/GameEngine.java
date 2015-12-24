package mrdev023.gameengine;


import java.util.concurrent.*;

import org.lwjgl.opengl.*;

import mrdev023.audio.Audio;
import mrdev023.entity.*;
import mrdev023.gamestate.*;
import mrdev023.gamestate.main.*;
import mrdev023.opengl.*;
import mrdev023.rendering.*;
import mrdev023.utils.*;

public class GameEngine {
	
	public static String TITLE = "";
	
	private static GameState state = GameState.MAIN_MENU;
	private static boolean IsRunning = true;
	private static int FPS = 0,TICKS = 0;
	private static int FPS_LIMIT = 12000000;
	private static Camera camera = new Camera();
	private static Audio a;

	public static void start(String title,int width,int height){
		TITLE = title;
		System.out.println("---------------------------- Create OpenGL Context -----------------------------------");
		Display.create(title, width, height);
		Display.createContext();
		Display.printMonitorsInfo();
		System.out.println("Window : " + width + "x" + height);
		System.out.println("OpenGL " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println("--------------------------------------------------------------------------------------");
		init();
		loop();
	}
	
	public static void init(){
		Audio.create();
		Shader.init();
		Texture.init();
		state.init();
		Timer.init();
		Input.init();
		DisplayManager.init();
		Timer.addTimer("info");
		Timer.addTimer("ticks");
		Timer.addTimer("fps");
		Display.setMouseGrabbed(true);
		System.out.println("---------------------------- Load Audio File -----------------------------------------");
		try {
			a = new Audio("res/audio/test.wav");
			a.playSound();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("--------------------------------------------------------------------------------------");
	}
	
	public static void loop(){
		while(IsRunning){
			if(Display.isCloseRequested())IsRunning = false;
			if(Display.wasResized())GL11.glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
			update();
			if(Timer.getNanoTime("ticks") >= 1000000000/60){
				Timer.deltaUpdate();
				Display.updateEvent();
				Input.update();
				state.updateKeyboard();
				state.updateMouse();
				state.update();
				TICKS++;
				Timer.setValue("ticks", Timer.getNanoTime("ticks") - 1000000000/60);
			}else if(Timer.getNanoTime("fps") >= 1000000000/FPS_LIMIT){
				DisplayManager.clearScreen();
				DisplayManager.preRender3D();
				DisplayManager.render3D();
				DisplayManager.preRender2D();
				DisplayManager.render2D();
				DisplayManager.preRenderGUI();
				DisplayManager.renderGUI();
				FPS++;
				Display.updateFrame();
				Timer.setValue("fps", Timer.getNanoTime("fps") - 1000000000/FPS_LIMIT);
			}
			
			if(Timer.getMillisTime("info") >= 1000){
				Display.setTitle(TITLE + " | Fps:" + FPS + " Ticks:" + TICKS + " | " + camera.getPos() + " " + camera.getRot());
				FPS = 0;
				TICKS = 0;
				Timer.setValue("info", Timer.getNanoTime("info") - 1000000000);
			}
			
		}
		destroy();
	}
	
	public static void update(){
		Timer.udpate();
	}
	
	public static void destroy(){
		state.destroy();
		Input.destroy();
		Audio.destroy();
		Display.destroy();
	}
	
	public static void changeGameState(GameState gameState){
		state.destroy();
		state = gameState;
		state.init();
	}
	
	public static GameState getGameState(){
		return state;
	}

	public static String getTITLE() {
		return TITLE;
	}

	public static void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	public static GameState getState() {
		return state;
	}

	public static void setState(GameState state) {
		GameEngine.state = state;
	}

	public static boolean isIsRunning() {
		return IsRunning;
	}

	public static void setIsRunning(boolean isRunning) {
		IsRunning = isRunning;
	}

	public static int getFPS() {
		return FPS;
	}

	public static void setFPS(int fPS) {
		FPS = fPS;
	}

	public static int getTICKS() {
		return TICKS;
	}

	public static void setTICKS(int tICKS) {
		TICKS = tICKS;
	}

	public static int getFPS_LIMIT() {
		return FPS_LIMIT;
	}

	public static void setFPS_LIMIT(int fPS_LIMIT) {
		FPS_LIMIT = fPS_LIMIT;
	}

	public static Camera getCamera() {
		return camera;
	}

	public static void setCamera(Camera camera) {
		GameEngine.camera = camera;
	}
	
	
	
}
