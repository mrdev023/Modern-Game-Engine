package mrdev023.entity;

import mrdev023.math.*;

public class Camera extends Player{

	public Camera(float x,float y,float z){
		super(x,y,z);
	}
	
	public Camera(){
		super();
	}
	
	public void transform(){
		transformMatrix.loadIdentity();
		transformMatrix.rotate(new Quaternion(new Vector3f(1,0,0),rot.x));
		transformMatrix.rotate(new Quaternion(new Vector3f(0,1,0),rot.y));
		transformMatrix.rotate(new Quaternion(new Vector3f(0,0,1),rot.z));
		transformMatrix.tranlate(-pos.x, -pos.y, -pos.z);
	}
	
}
