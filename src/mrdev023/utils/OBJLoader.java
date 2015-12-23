package mrdev023.utils;

import java.io.*;
import java.util.*;

import mrdev023.math.*;

public class OBJLoader {

	public static ArrayList<Vector3f> loadOBJ(String path){
		ArrayList<Vector3f> vecList = new ArrayList<Vector3f>();	
		String file = "";
		try {
			file = IO.loadFile(path);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		BufferedReader reader = new BufferedReader(new StringReader(file));
		String buffer = "";
		try {
			while ((buffer = reader.readLine()) != null) {
				if(buffer.contains("v ")){
					String[] line = buffer.split(" ");
					if(line.length == 4){
						vecList.add(new Vector3f(Float.parseFloat(line[1]),Float.parseFloat(line[2]),Float.parseFloat(line[3])));
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(vecList.size());
		return vecList;
	}
	
}
