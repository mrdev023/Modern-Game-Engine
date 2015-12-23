package mrdev023.entity;

import mrdev023.math.*;
import mrdev023.rendering.*;

public class Entity {
	
	Matrix4f transformMatrix = new Matrix4f();
	
	public Vector3f pos = new Vector3f();
	public Vector3f rot = new Vector3f();
	
	public Entity(){
		pos = new Vector3f();
	}
	
	public Entity(float x,float y,float z){
		pos = new Vector3f(x,y,z);
	}
	
	public void transform(){
		transformMatrix.loadIdentity();
		transformMatrix.tranlate(pos.x, pos.y, pos.z);
		transformMatrix.rotate(new Quaternion(rot));
	}


	public Vector3f getPos() {
		return pos;
	}


	public void setPos(Vector3f pos) {
		this.pos = pos;
	}


	public Vector3f getRot() {
		return rot;
	}


	public void setRot(Vector3f rot) {
		this.rot = rot;
	}


	public Matrix4f getTransformMatrix() {
		return transformMatrix;
	}
	
	public Quaternion getRotation(){
		return new Quaternion(rot);
	}
	
	
}
