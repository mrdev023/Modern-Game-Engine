package mrdev023.utils;

import java.io.*;

import mrdev023.utils.compile.*;

public class ClassCompiler {

	public static Class compile(String path,String className,String packageName){
		String f = "";
		try {
			f = IO.loadFile(path + className + ".java");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		f = "package " + packageName + ";\n\n" + f;
		System.out.println(f);
		Class cl = null;
		try {
			cl = InMemoryJavaCompiler.compile(packageName + "." + className, f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cl;
	}
	
}
