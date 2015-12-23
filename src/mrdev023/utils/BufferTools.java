package mrdev023.utils;

import java.nio.*;

import org.lwjgl.*;

public class BufferTools {

	public static FloatBuffer asFlippedFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
}
