package mrdev023.opengl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.awt.*;
import java.nio.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import mrdev023.exception.*;

public class Display {

	private static DisplayMode displayMode;
	private static String TITLE = "";
	private static long window;
	private static boolean hasResized = false;
	
	public static void create(String title,int width,int height){
		if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");
		
		TITLE = title;
		displayMode = new DisplayMode(width,height);
		window = glfwCreateWindow(displayMode.getWidth(),displayMode.getHeight(), TITLE, NULL, NULL);
		
	}
	
	public static void create(String title,int width,int height,int major,int minor){
		if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, major); // Nous voulons OpenGL 3.3 
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, minor); 
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		TITLE = title;
		displayMode = new DisplayMode(width,height);
		window = glfwCreateWindow(displayMode.getWidth(),displayMode.getHeight(), TITLE, NULL, NULL);
	}
	
	public static void setMouseGrabbed(boolean a){
		if(a){
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		}else{
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}
	}
	
	public static void setVSync(boolean a) throws DisplayException{
		if(a)glfwSwapInterval(1);
		else glfwSwapInterval(0);
	}
	
	public static void create(String title,int width,int height,int major,int minor,int sample){
		if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwWindowHint(GLFW_SAMPLES, sample); // antialiasing 4x 
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, major); // Nous voulons OpenGL 3.3 
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, minor); 
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		TITLE = title;
		displayMode = new DisplayMode(width,height);
		window = glfwCreateWindow(displayMode.getWidth(),displayMode.getHeight(), TITLE, NULL, NULL);
	}
	
	public static void setSample(int sample){
		glfwWindowHint(GLFW_SAMPLES, sample);
	}
	
	public static void setResizable(boolean a){
		if(a)glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		else glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
	}
	
	public static void setTitle(String title){
		TITLE = title;
		glfwSetWindowTitle(window, TITLE);
	}
	
	public static String getTitle(){
		return TITLE;
	}
	
	public static boolean wasResized(){
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window, w, h);
		int width = w.get(0);
		int height = h.get(0);
		
		if(Display.getDisplayMode().getWidth() != width || Display.getDisplayMode().getHeight() != height || hasResized){
			setDisplayMode(new DisplayMode(width, height));
			hasResized = false;
			return true;
		}else{
			return false;
		}
	}
	
	public static void printMonitorsInfo(){
		PointerBuffer monitors = glfwGetMonitors();
		GLFWVidMode m;
		if(monitors == null){
			System.out.println("No monitor detected !");
			return;
		}
		for(int i = 0;i < monitors.capacity();i++){
			m = glfwGetVideoMode(monitors.get(i));
			System.out.println(glfwGetMonitorName(monitors.get(i)) + "(" + i + ") : " + m.width() + "x" + m.height() + ":" + m.refreshRate() + "Hz");
		}
		
	}
	
	public static boolean isCloseRequested(){
		return glfwWindowShouldClose(window);
	}
	
	public static void createContext(){
		glfwMakeContextCurrent(window);
        GL.createCapabilities();
	}
	
	public static void updateEvent(){
		glfwPollEvents();
	}
	
	public static void updateFrame(){
		glfwSwapBuffers(window);
	}
	
	public static DisplayMode getDisplayMode() {
		return displayMode;
	}

	public static void setDisplayMode(DisplayMode displayMode) {
		if(Display.displayMode == null || displayMode == null)return;
		Display.displayMode.setDisplayMode(displayMode);
		hasResized = true;
	}
	
	public static void destroy(){
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public static long getWindow() {
		return window;
	}
	
	
}
