package mrdev023.rendering.lights;

import mrdev023.math.*;

public abstract class Light {

	public Vector3f lightPos = new Vector3f();
	public Color4f lightColor = new Color4f();
	public Color4f ambientLight = new Color4f();
	
	public Light(Vector3f pos,Color4f color,Color4f ambientLight){
		this.lightPos = pos;
		this.lightColor = color;
		this.ambientLight = ambientLight;
	}
	
	public abstract void drawLight();
	
}
