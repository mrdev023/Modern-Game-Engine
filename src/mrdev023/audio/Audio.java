package mrdev023.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.ALUtil.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

import javax.sound.sampled.*;

import org.lwjgl.*;
import org.lwjgl.openal.*;


import org.lwjgl.stb.STBVorbisInfo;

public class Audio {
	
	//Variables global
	//------------------------------------------------------
	public static ALDevice device;
	public static ALCCapabilities caps;
	public static ALContext context;
	public static final int INITIAL_STATE = 4113,PAUSED_STATE = 4115,STOPPED_STATE = 4116,PLAYING_STATE = 4114;
	//------------------------------------------------------
	
	//Variable de l'objet audio ou du son a lire
	//------------------------------------------------------
	private int buffer,source;
	private String fileName;
	private String format;
	//------------------------------------------------------

	//Fonction global
	//------------------------------------------------------
	public static void create(){
		device = ALDevice.create(null);
		if ( device == null )
			throw new IllegalStateException("Failed to open the default device.");
		caps = device.getCapabilities();
		System.out.println("---------------------------- Create Audio Device -------------------------------------");
		System.out.println("OpenALC10: " + caps.OpenALC10);
		System.out.println("OpenALC11: " + caps.OpenALC11);
		System.out.println("caps.ALC_EXT_EFX = " + caps.ALC_EXT_EFX);
		
		String defaultDeviceSpecifier = alcGetString(0L, ALC_DEFAULT_DEVICE_SPECIFIER);
		System.out.println("Default device: " + defaultDeviceSpecifier);

		context = ALContext.create(device);

		System.out.println("ALC_FREQUENCY: " + alcGetInteger(device.address(), ALC_FREQUENCY) + "Hz");
		System.out.println("ALC_REFRESH: " + alcGetInteger(device.address(), ALC_REFRESH) + "Hz");
		System.out.println("ALC_SYNC: " + (alcGetInteger(device.address(), ALC_SYNC) == ALC_TRUE));
		System.out.println("ALC_MONO_SOURCES: " + alcGetInteger(device.address(), ALC_MONO_SOURCES));
		System.out.println("ALC_STEREO_SOURCES: " + alcGetInteger(device.address(), ALC_STEREO_SOURCES));
		System.out.println("---------------------------------------------------------------------------------------");
	}
	
	public static void destroy(){
		context.destroy();
		device.destroy();
	}
	//------------------------------------------------------
	
	//Fonction de l'objet audio ou du son a lire
	//------------------------------------------------------
	
	public Audio(String fileName) throws Exception{
		this.fileName = fileName;
		setSound();
	}
	
	private void setSound() throws Exception{
		if(fileName.endsWith(".ogg")){
			loadOGGFormat();
			format = "OGG";
		}else if(fileName.endsWith(".wav")){
			loadWavFormat();
			format = "WAV";
		}else{
			throw new Exception("Format not supported !");
		}
        alSourcei(source, AL_BUFFER, buffer);
        checkALError();
        int size = alGetBufferi(buffer,AL_SIZE);
		int bits = alGetBufferi(buffer, AL_BITS);
		int channels = alGetBufferi(buffer, AL_CHANNELS);
		int freq = alGetBufferi(buffer, AL_FREQUENCY);
        System.out.println(fileName + " loaded !" + " | TIME : " + (size/channels/(bits/8)/freq) + "s | BITS : " + bits + " | CHANNELS : " + channels + " | FREQUENCE : " + freq + " FORMAT : " + format);
	}
	
	public void loadWavFormat() throws Exception{
		AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(fileName)));
		AudioFormat audioformat = ais.getFormat();

		// get channels
		int channels = 0;
		if (audioformat.getChannels() == 1) {
			if (audioformat.getSampleSizeInBits() == 8) {
				channels = AL10.AL_FORMAT_MONO8;
			} else if (audioformat.getSampleSizeInBits() == 16) {
				channels = AL10.AL_FORMAT_MONO16;
			} else {
				assert false : "Illegal sample size";
			}
		} else if (audioformat.getChannels() == 2) {
			if (audioformat.getSampleSizeInBits() == 8) {
				channels = AL10.AL_FORMAT_STEREO8;
			} else if (audioformat.getSampleSizeInBits() == 16) {
				channels = AL10.AL_FORMAT_STEREO16;
			} else {
				assert false : "Illegal sample size";
			}
		} else {
			assert false : "Only mono or stereo is supported";
		}
		
		int available = ais.available();
			if(available <= 0) {
			available = ais.getFormat().getChannels() * (int) ais.getFrameLength() * ais.getFormat().getSampleSizeInBits() / 8;
		}
		byte[] buf = new byte[ais.available()];
		int read = 0, total = 0;
		while ((read = ais.read(buf, total, buf.length - total)) != -1
			&& total < buf.length) {
			total += read;
		}
		byte[] audio_bytes = buf;
		boolean two_bytes_data = audioformat.getSampleSizeInBits() == 16;
		ByteOrder order = audioformat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
		ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
		dest.order(ByteOrder.nativeOrder());
		ByteBuffer src = ByteBuffer.wrap(audio_bytes);
		src.order(order);
		if (two_bytes_data) {
			ShortBuffer dest_short = dest.asShortBuffer();
			ShortBuffer src_short = src.asShortBuffer();
			while (src_short.hasRemaining())
				dest_short.put(src_short.get());
		} else {
			while (src.hasRemaining())
				dest.put(src.get());
		}
		dest.rewind();
		
		this.buffer = alGenBuffers();
		this.source = alGenSources();
        alBufferData(this.buffer, channels, dest, (int)audioformat.getSampleRate());
        dest.clear();
	}
	
	public void loadOGGFormat(){
		STBVorbisInfo info = STBVorbisInfo.malloc();
		ByteBuffer buff = BufferUtils.createByteBuffer(0);
		//lecture du fichier
		//----------------------------------------------------------------------------------------------------------------
		try {
			File file = new File(fileName);
			if ( file.isFile() ) {
				FileInputStream fis = new FileInputStream(file);
				FileChannel fc = fis.getChannel();
				buff = BufferUtils.createByteBuffer((int)fc.size() + 1);
				
				while ( fc.read(buff) != -1 ) ;
				
				fis.close();
				fc.close();
			} else {
				System.err.println("File not found !");
				return;
			}

			buff.flip();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		//----------------------------------------------------------------------------------------------------------------

		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(buff, error, null);
		if ( decoder == NULL )
			throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));

		stb_vorbis_get_info(decoder, info);

		int channels = info.channels();

		stb_vorbis_seek_start(decoder);
		int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

		ByteBuffer pcm = BufferUtils.createByteBuffer(lengthSamples * 2 * channels);

		stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm, lengthSamples);
		stb_vorbis_close(decoder);
		
		buffer = alGenBuffers();
		checkALError();
		
		source = alGenSources();
		checkALError();
		
		if(channels == 1)alBufferData(buffer, AL_FORMAT_MONO16, pcm, info.sample_rate());
		else alBufferData(buffer, AL_FORMAT_STEREO16, pcm, info.sample_rate());
		checkALError();
	}
	
	public void playSound(){
		if(source == 0 || buffer == 0) return;
		alSourcePlay(source);
	}
	
	public int getPosition(){
		return alGetSourcei(source, AL_POSITION);
	}
	
	public int getDurationInSeconds(){
		if(source == 0 || buffer == 0) return 0;
		int size = alGetBufferi(buffer,AL_SIZE);
		int bits = alGetBufferi(buffer, AL_BITS);
		int channels = alGetBufferi(buffer, AL_CHANNELS);
		int freq = alGetBufferi(buffer, AL_FREQUENCY);
		return size/channels/(bits/8)/freq;
	}
	
	public int getStateSound(){
		if(source == 0 || buffer == 0) return 0;
		return alGetSourcei(source, AL_SOURCE_STATE);
	}
	
	public boolean isStopped(){
		if(source == 0 || buffer == 0) return false;
		if(alGetSourcei(source, AL_SOURCE_STATE) == STOPPED_STATE)return true;
		else return false;
	}
	
	public boolean isPaused(){
		if(source == 0 || buffer == 0) return false;
		if(alGetSourcei(source, AL_SOURCE_STATE) == PAUSED_STATE)return true;
		else return false;
	}
	
	public boolean isPlaying(){
		if(source == 0 || buffer == 0) return false;
		if(alGetSourcei(source, AL_SOURCE_STATE) == PLAYING_STATE)return true;
		else return false;
	}
	
	public boolean isInitial(){
		if(source == 0 || buffer == 0) return false;
		if(alGetSourcei(source, AL_SOURCE_STATE) == INITIAL_STATE)return true;
		else return false;
	}
	
	public void stopSound(){
		if(source == 0 || buffer == 0) return;
		alSourceStop(source);
	}

	public void pauseSound(){
		if(source == 0 || buffer == 0) return;
		alSourcePause(source);
	}
	
	public void rewindSound(){
		if(source == 0 || buffer == 0) return;
		alSourceRewind(source);
	}
	
	public void setGain(float gain){
		if(source == 0 || buffer == 0) return;
		if(gain > 1.0f)gain = 1.0f;
		if(gain < 0.0f)gain = 0.0f;
		alSourcef(source, AL_GAIN, gain);
	}
	
	public void setPitch(float pitch){
		if(source == 0 || buffer == 0) return;
		if(pitch < 0.0f)pitch = 0.0f;
		alSourcef(source, AL_PITCH, pitch);
	}
	
	
	public float getGain(){
		if(source == 0 || buffer == 0) return 0;
		return alGetSourcef(source, AL_GAIN);
	}
	
	public float getPitch(){
		if(source == 0 || buffer == 0) return 0;
		return alGetSourcef(source, AL_PITCH);
	}
	
	public void setLooping(boolean looping){
		if(source == 0 || buffer == 0) return;
		if(looping){
			alSourcef(source, AL_LOOPING, AL_TRUE);
		}else{
			alSourcef(source, AL_LOOPING, AL_FALSE);
		}
	}
	
	public void destroySound(){
		alDeleteSources(source);
		alDeleteBuffers(buffer);
		source = 0;
		buffer = 0;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) throws Exception {
		this.fileName = fileName;
		destroySound();
		setSound();
	}

	public int getBuffer() {
		return buffer;
	}

	public void setBuffer(int buffer) {
		this.buffer = buffer;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}
	//------------------------------------------------------
	
}
