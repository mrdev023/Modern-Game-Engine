package mrdev023.model;

import mrdev023.math.*;
import mrdev023.rendering.*;

public class MeshBuilder {

	public static VAO createCube(float size,Color4f color,Texture texture){
		float half_size = size/2.0f;
		VAO vao = new VAO();
		vao.addData(half_size,half_size,half_size, 		0,1,0,	color.getR(),color.getG(),color.getB(),color.getA(), 	0,0,
				half_size,half_size,-half_size,	 		0,1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	0,1,
					-half_size,half_size,-half_size,	0,1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	1,1,//top
					-half_size,half_size,half_size,	 	0,1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	1,0,
					
					half_size,-half_size,half_size, 	1,0,0,	color.getR(),color.getG(),color.getB(),color.getA(),	0,0,
					half_size,-half_size,-half_size,	1,0,0,	color.getR(),color.getG(),color.getB(),color.getA(),	0,1,
					half_size,half_size,-half_size,	 	1,0,0,	color.getR(),color.getG(),color.getB(),color.getA(),	1,1,//right
					half_size,half_size,half_size,	 	1,0,0,	color.getR(),color.getG(),color.getB(),color.getA(),	1,0,
					
					-half_size,-half_size,half_size, 	0,-1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	0,0,
					-half_size,-half_size,-half_size,	0,-1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	0,1,//bottom
					half_size,-half_size,-half_size,	0,-1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	1,1,
					half_size,-half_size,half_size,	 	0,-1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	1,0,
					
					-half_size,half_size,half_size, 	-1,0,0,	color.getR(),color.getG(),color.getB(),color.getA(), 	0,0,
					-half_size,half_size,-half_size,	-1,0,0,	color.getR(),color.getG(),color.getB(),color.getA(),	0,1,//left
					-half_size,-half_size,-half_size,	-1,0,0,	color.getR(),color.getG(),color.getB(),color.getA(),	1,1,
					-half_size,-half_size,half_size,	-1,0,0,	color.getR(),color.getG(),color.getB(),color.getA(),	1,0,
				                                                                                                    
					half_size,half_size,-half_size, 	0,0,-1,	color.getR(),color.getG(),color.getB(),color.getA(),	0,0,
					half_size,-half_size,-half_size,	0,0,-1,	color.getR(),color.getG(),color.getB(),color.getA(),	0,1,//back
					-half_size,-half_size,-half_size,	0,0,-1,	color.getR(),color.getG(),color.getB(),color.getA(),	1,1,
					-half_size,half_size,-half_size,	0,0,-1,	color.getR(),color.getG(),color.getB(),color.getA(),	1,0,
					                                                                                                
					half_size,-half_size,half_size, 	0,0,1,	color.getR(),color.getG(),color.getB(),color.getA(),	0,0,
					half_size,half_size,half_size,		0,0,1,	color.getR(),color.getG(),color.getB(),color.getA(),	0,1,//front
					-half_size,half_size,half_size,	 	0,0,1,	color.getR(),color.getG(),color.getB(),color.getA(),	1,1,
					-half_size,-half_size,half_size,	0,0,1,	color.getR(),color.getG(),color.getB(),color.getA(),	1,0);
		vao.bindBuffer();
		vao.setTexture(texture.getID());
		return vao;
	}
	
	public static VAO createFloor(float size,Color4f color,Texture texture){
		float half_size = size/2.0f;
		VAO vao = new VAO();
		vao.addData(half_size,0,half_size, 		0,1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	0,0,
					half_size,0,-half_size,	 	0,1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	0,1,
					-half_size,0,-half_size,	0,1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	1,1,//up
					-half_size,0,half_size,	 	0,1,0,	color.getR(),color.getG(),color.getB(),color.getA(),	1,0);
		vao.bindBuffer();
		vao.setTexture(texture.getID());
		return vao;
	}
	
}
