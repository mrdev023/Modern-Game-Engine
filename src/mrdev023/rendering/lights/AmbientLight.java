package mrdev023.rendering.lights;

import mrdev023.math.*;
import mrdev023.rendering.*;

public class AmbientLight extends Light{

	public AmbientLight(Vector3f pos, Color4f color,Color4f ambientLight) {
		super(pos, color,ambientLight);
	}

	public void drawLight() {
		Shader.MAIN.uniform("light_position", lightPos);
		Shader.MAIN.uniform("diffuse_light_color", lightColor);
		Shader.MAIN.uniform("ambient_light", ambientLight);
	}
		
	
}
