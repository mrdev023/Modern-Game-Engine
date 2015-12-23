package mrdev023.gamestate.main;

public interface IGameState {

	public void update();
	public void updateKeyboard();
	public void updateMouse();
	public void init();
	public void render2D();
	public void render3D();
	public void renderGUI();
	public void destroy();
	
}
