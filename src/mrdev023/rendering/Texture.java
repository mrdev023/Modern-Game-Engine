package mrdev023.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.awt.image.*;
import java.io.*;
import java.nio.*;

import javax.imageio.*;

import org.lwjgl.*;

public class Texture {
	
	public static Texture FLOOR,WOOD;

	int width, height;
	int id;
	
	public static void init() {
		FLOOR = Texture.loadTexture("res/textures/floor.jpg");
		WOOD = Texture.loadTexture("res/textures/wood.jpg");
	}
	
	public Texture(int width,int height,int id){
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public static Texture loadTexture(String path){
		try {
			BufferedImage image = ImageIO.read(new File(path));
			int width = image.getWidth();
			int height = image.getHeight();
			int[] pixels = new int[width * height];
			
			image.getRGB(0, 0, width, height, pixels, 0,width);
			
			ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
			for(int y = 0; y < width; y++){
				for(int x = 0; x < height; x++){
					int i = pixels[x + y * width];//0xAARRGGBB
					buffer.put((byte) ((i >> 16) & 0xFF));//r
					buffer.put((byte) ((i >> 8) & 0xFF));//g
					buffer.put((byte) ((i) & 0xFF));//b
					buffer.put((byte) ((i >> 24) & 0xFF));//a
				}
			}
			buffer.flip();
			
			int id = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, id);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			glBindTexture(GL_TEXTURE_2D, 0);
			
			System.out.println("Texture loaded ! " + width + "x" + height + " id:" + id);
			
			return new Texture(width, height, id);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getID(){
		return id;
	}
	
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void unbind(){
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	
}
