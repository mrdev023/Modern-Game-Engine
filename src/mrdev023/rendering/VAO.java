package mrdev023.rendering;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.*;
import java.util.*;

import org.lwjgl.*;

public class VAO {

	private int vbo,vao,size = 0,textureid = 0;
	
	public static final int SIZE_OF_FLOAT = 4;
	
	public ArrayList<Float> data = new ArrayList<Float>();
	
	public VAO(){
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
	}
	
	public void setTexture(int id){
		this.textureid = id;
	}
	
	public void addData(float... d){
		for(float f : d){
			data.add(f);
		}
	}
	
	public void addData(double... d){
		for(double f : d){
			data.add((float)f);
		}
	}
	
	public void clearBuffer(){
		data.clear();
	}
	
	public void bindBuffer(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.size());
		for(float f : data){
			buffer.put(f);
		}
		buffer.flip();
		glBindVertexArray(vao);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, (2 + 3 + 4 + 2) * SIZE_OF_FLOAT, 0);
		
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, (2 + 3 + 4 + 2) * SIZE_OF_FLOAT, 2 * SIZE_OF_FLOAT);
		
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, (2 + 3 + 4 + 2) * SIZE_OF_FLOAT, (2 + 3) * SIZE_OF_FLOAT);
		
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, (2 + 3 + 4 + 2) * SIZE_OF_FLOAT, (2 + 3 + 4) * SIZE_OF_FLOAT);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		
		glBindVertexArray(0);
		size = data.size();
	}
	
	public void render2D(){
		glBindTexture(GL_TEXTURE_2D, textureid);
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glDrawArrays(GL_QUADS, 0, size/(2 + 3 + 4 + 2));
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void render3D(){
		glBindTexture(GL_TEXTURE_2D, textureid);
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glVertexAttribPointer(0, 3, GL_FLOAT, false, (3 + 3 + 4 + 2) * SIZE_OF_FLOAT, 0);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, (3 + 3 + 4 + 2) * SIZE_OF_FLOAT, 3 * SIZE_OF_FLOAT);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, (3 + 3 + 4 + 2) * SIZE_OF_FLOAT, (3 + 3) * SIZE_OF_FLOAT);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, (3 + 3 + 4 + 2) * SIZE_OF_FLOAT, (3 + 3 + 4) * SIZE_OF_FLOAT);
		
		glDrawArrays(GL_QUADS, 0, size/(3 + 3 + 4 + 2));
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void destroy(){
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
	}
	
}
