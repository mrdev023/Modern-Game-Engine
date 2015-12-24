package mrdev023.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.*;

import mrdev023.math.*;
import mrdev023.utils.*;

public class Shader {

	public int program;
	public int includeV = 0,includeF = 0;
	public static Shader MAIN,LIGHT,LIGHT_AMBIENT,FONT;
	
	public static void init() {
		System.out.println("---------------------------- Load Shader ---------------------------------------------");
		MAIN = new Shader("res/shaders/main");
		FONT = new Shader("res/shaders/font");
		LIGHT = new Shader("res/shaders/light");
		LIGHT_AMBIENT = new Shader("res/shaders/light_ambient");
		System.out.println("--------------------------------------------------------------------------------------");
	}

	public Shader(String file){
		String vertexShader = "";
		try {
			vertexShader = IO.loadFile(file + ".vert");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		vertexShader = loadShader(vertexShader,0,file + ".vert");
		String fragmentShader = "";
		try {
			fragmentShader = IO.loadFile(file + ".frag");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		fragmentShader = loadShader(fragmentShader,1,file + ".vert");
		program = glCreateProgram();
		int vert = glCreateShader(GL_VERTEX_SHADER);
		int frag = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vert, vertexShader);
		glShaderSource(frag, fragmentShader);
		glCompileShader(vert);
		if (glGetShaderi(vert, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(vert, 2048));
			System.exit(1);
		}
		glCompileShader(frag);
		if (glGetShaderi(frag, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(frag, 2048));
			System.exit(1);
		}
		glAttachShader(program, vert);
		glAttachShader(program, frag);
		glLinkProgram(program);
		glValidateProgram(program);
		System.out.println("Shader " + file + ".vert loaded with " + includeV + " include !");
		System.out.println("Shader " + file + ".frag loaded with " + includeF + " include !");
	}
	
	public String loadShader(String shader,int a,String name){
		String st = "";
		BufferedReader reader = new BufferedReader(new StringReader(shader));
		String buffer = "";
		int line = 0;
		try {
			while ((buffer = reader.readLine()) != null) {
				line++;
				if(buffer.startsWith("#include")){
					if(a == 0){
						includeV++;
					}else{
						includeF++;
					}
					String path = buffer.substring(10, buffer.length() - 1);
					String sh = IO.loadFile("res/shaders/" + path);
					buffer += sh;
				}else{
					st += buffer + "\n";
				}
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("include file not found in " + name + " #" + line + " !");
			System.exit(-1);
		}
		return st;
	}
	
	public void bind(){
		glUseProgram(program);
	}
	
	public void unbind(){
		glUseProgram(0);
	}
	
	public void uniform(String name,float v){
		glUniform1f(glGetUniformLocation(program, name), v);
	}
	
	public void uniform(String name,Vector3f vec){
		glUniform3f(glGetUniformLocation(program, name), vec.x,vec.y,vec.z);
	}
	
	public void uniform(String name,Matrix4f mat){
		glUniformMatrix4fv(glGetUniformLocation(program, name),true, mat.getBuffer());
	}

	public void uniform(String name, Color4f v) {
		glUniform4f(glGetUniformLocation(program, name), v.getR(),v.getG(),v.getB(),v.getA());
	}


}
