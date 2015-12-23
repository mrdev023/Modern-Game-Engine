package mrdev023.utils;

import java.io.*;

public class IO {

	public static String loadFile(String input) throws IOException {
		String r = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
		String buffer = "";
		while ((buffer = reader.readLine()) != null) {
				r += buffer + "\n";
		}
		reader.close();
		return r;
	}
	
	public static void writeFile(String path,String output) throws Exception{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
		writer.write(output);
		writer.close();
	}
	
}
