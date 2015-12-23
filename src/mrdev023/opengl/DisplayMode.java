package mrdev023.opengl;

import static org.lwjgl.glfw.GLFW.*;

public class DisplayMode {

	private int width = 0,height = 0;
	private boolean fullscreen = false;
	
	public DisplayMode(int width,int height){
		this.width = width;
		this.height = height;
	}
	
	public void setDisplayMode(DisplayMode displayMode){
		this.width = displayMode.getWidth();
		this.height = displayMode.getHeight();
		this.fullscreen = displayMode.isFullscreen();
		setDisplayMode();
	}
	
	public void setDisplayMode(){
		glfwSetWindowSize(Display.getWindow(), width,height);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
		setDisplayMode();
	}
	
	
}
