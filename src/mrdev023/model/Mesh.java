package mrdev023.model;

import mrdev023.math.*;
import mrdev023.rendering.*;

public class Mesh {

	private VAO vao;
	private Matrix4f transform;
	
	public Mesh(VAO vao,Matrix4f mat){
		this.vao = vao;
		this.transform = mat;
	}

	public VAO getVao() {
		return vao;
	}

	public void setVao(VAO vao) {
		this.vao = vao;
	}

	public Matrix4f getTransform() {
		return transform;
	}

	public void setTransform(Matrix4f transform) {
		this.transform = transform;
	}
	
	
	
}
