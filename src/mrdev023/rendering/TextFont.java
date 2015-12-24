package mrdev023.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.*;

import org.lwjgl.*;

import mrdev023.math.*;

public class TextFont {
	
	private int x,y,vbo,size,sizeVBO;
	private String text;
	private Color4f color;

	private static final String chars = "" + //
			  "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + //
			  "0123456789.,!?'\"-+=/\\%()<>:;     " + //
			  "";
	
	private static final int SIZE_OF_FLOAT = 4;

	public TextFont(String text,int x,int y,int size,Color4f color){
		this.size = size;
		this.x = x;
		this.y = y;
		this.text = text;
		this.color = color;
		String msg = text.toUpperCase();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(8*4*msg.length());
		for (int i = 0; i < msg.length(); i++) {
			int ix = chars.indexOf(msg.charAt(i));
			int iy = 0;
			if (ix >= 32) iy = 1;
			if (iy >= 0) {
				if (ix >= 0) {
					float xx = x + i * size;
					float yy = y;
					
					float yo = iy;
					float xo = ix % 32;
					float texSize = 32.0f;
					
					buffer.put(new float[]{
							xx + size, yy,			color.r,color.g,color.b,color.a,		(1 + xo) / texSize, (0 + yo) / 2.0f,
							xx , yy,				color.r,color.g,color.b,color.a,		(0 + xo) / texSize, (0 + yo) / 2.0f,
							xx, yy + size,			color.r,color.g,color.b,color.a,		(0 + xo) / texSize, (1 + yo) / 2.0f,
							xx + size, yy + size,	color.r,color.g,color.b,color.a,		(1 + xo) / texSize, (1 + yo) / 2.0f
					});
				}
			}
		}
		buffer.flip();
		sizeVBO = buffer.capacity();
		
		vbo = glGenBuffers();
		
		glBindBuffer(GL_ARRAY_BUFFER,vbo);
		glBufferData(GL_ARRAY_BUFFER,buffer,GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER,0);
		
	}
	

	public void drawText() {
		Shader.FONT.bind();
		glBindTexture(GL_TEXTURE_2D, Texture.FONT.getID());
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glVertexAttribPointer(0, 2, GL_FLOAT, false, (2 + 4 + 2) * SIZE_OF_FLOAT, 0);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, (2 + 4 + 2) * SIZE_OF_FLOAT, (2) * SIZE_OF_FLOAT);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, (2 + 4 + 2) * SIZE_OF_FLOAT, (2 + 4) * SIZE_OF_FLOAT);
		
		glDrawArrays(GL_QUADS, 0, sizeVBO/(2 + 4 + 2));
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
		Shader.MAIN.bind();
	}
	
	public void destroy(){
		glDeleteBuffers(vbo);
	}
}
