package mrdev023.gamestate.main;

import mrdev023.gamestate.*;

public enum GameState {

	MAIN_MENU(new MainMenu());
	
	private IGameState state;
	
	GameState(IGameState state){
		this.state = state;
	}
	
	public void update(){
		state.update();
	}
	
	public void updateKeyboard(){
		state.updateKeyboard();
	}
	
	public void updateMouse(){
		state.updateMouse();
	}
	
	public void init(){
		state.init();
	}
	
	public void render2D(){
		state.render2D();
	}
	
	public void render3D(){
		state.render3D();
	}
	
	public void renderGUI(){
		state.renderGUI();
	}
	
	public void destroy(){
		state.destroy();
	}
	
	
}
