package mrdev023.audio;


import org.lwjgl.BufferUtils;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.stb.STBVorbisInfo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.ALUtil.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.*;


import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.BufferUtils.*;

public class AudioTest {

	public static void main(String[] args) {
		ALDevice device = ALDevice.create(null);
		if ( device == null )
			throw new IllegalStateException("Failed to open the default device.");

		ALCCapabilities caps = device.getCapabilities();

		System.out.println("OpenALC10: " + caps.OpenALC10);
		System.out.println("OpenALC11: " + caps.OpenALC11);
		System.out.println("caps.ALC_EXT_EFX = " + caps.ALC_EXT_EFX);


		String defaultDeviceSpecifier = alcGetString(0L, ALC_DEFAULT_DEVICE_SPECIFIER);
		System.out.println("Default device: " + defaultDeviceSpecifier);

		ALContext context = ALContext.create(device);

		System.out.println("ALC_FREQUENCY: " + alcGetInteger(device.address(), ALC_FREQUENCY) + "Hz");
		System.out.println("ALC_REFRESH: " + alcGetInteger(device.address(), ALC_REFRESH) + "Hz");
		System.out.println("ALC_SYNC: " + (alcGetInteger(device.address(), ALC_SYNC) == ALC_TRUE));
		System.out.println("ALC_MONO_SOURCES: " + alcGetInteger(device.address(), ALC_MONO_SOURCES));
		System.out.println("ALC_STEREO_SOURCES: " + alcGetInteger(device.address(), ALC_STEREO_SOURCES));

		try {
			testPlayback();
		} finally {
			context.destroy();
			device.destroy();
		}
	}

	private static void testPlayback() {
		STBVorbisInfo info = STBVorbisInfo.malloc();
		ByteBuffer pcm = readVorbis("res/audio/test.ogg", 32 * 1024, info);

		// generate buffers and sources
		int buffer = alGenBuffers();
		checkALError();

		int source = alGenSources();
		checkALError();

		//copy to buffer
		alBufferData(buffer, AL_FORMAT_STEREO16, pcm, info.sample_rate());
		checkALError();

		info.free();

		//set up source input
		alSourcei(source, AL_BUFFER, buffer);
		checkALError();

		//lets loop the sound
		alSourcei(source, AL_LOOPING, AL_TRUE);
		checkALError();

		//play source 0
		alSourcePlay(source);
		checkALError();

//		//wait 5 secs
//		try {
//			System.out.println("Waiting 10 seconds for sound to complete");
//			Thread.sleep(1);
//		} catch (InterruptedException inte) {
//		}
		
		while(alGetSourcei(source, AL_SOURCE_STATE) != 4116);

		//stop source 0
		alSourceStop(source);
		checkALError();

		//delete buffers and sources
		alDeleteSources(source);
		checkALError();

		alDeleteBuffers(buffer);
		checkALError();
	}

	static ByteBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info) {
		ByteBuffer vorbis;
		try {
			vorbis = ioResourceToByteBuffer(resource, bufferSize);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(vorbis, error, null);
		if ( decoder == NULL )
			throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));

		stb_vorbis_get_info(decoder, info);

		int channels = info.channels();

		int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

		ByteBuffer pcm = BufferUtils.createByteBuffer(lengthSamples * 2);

		stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm, lengthSamples);
		stb_vorbis_close(decoder);
		

		return pcm;
	}
	
	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}

	/**
	 * Reads the specified resource and returns the raw data as a ByteBuffer.
	 *
	 * @param resource   the resource to read
	 * @param bufferSize the initial buffer size
	 *
	 * @return the resource data
	 *
	 * @throws IOException if an IO error occurs
	 */
	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;

		File file = new File(resource);
		if ( file.isFile() ) {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();
			
			buffer = BufferUtils.createByteBuffer((int)fc.size() + 1);

			while ( fc.read(buffer) != -1 ) ;
			
			fis.close();
			fc.close();
		} else {
			buffer = createByteBuffer(bufferSize);

			InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if ( source == null )
				throw new FileNotFoundException(resource);

			try {
				ReadableByteChannel rbc = Channels.newChannel(source);
				try {
					while ( true ) {
						int bytes = rbc.read(buffer);
						if ( bytes == -1 )
							break;
						if ( buffer.remaining() == 0 )
							buffer = resizeBuffer(buffer, buffer.capacity() * 2);
					}
				} finally {
					rbc.close();
				}
			} finally {
				source.close();
			}
		}

		buffer.flip();
		return buffer;
	}
	
}
