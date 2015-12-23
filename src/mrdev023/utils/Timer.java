package mrdev023.utils;

import java.util.*;
import java.util.Map.*;

public class Timer {

	public static HashMap<String,Long> timers = new HashMap<String,Long>();
	
	private static long current,previous,currentDelta,previousDelta,delta;
	
	
	public static void init(){
		current = System.nanoTime();
		previous = System.nanoTime();
		currentDelta = System.currentTimeMillis();
	}
	
	public static void udpate(){
		previous = current;
		current = System.nanoTime();
		for(Entry<String, Long> timer : timers.entrySet()){
			timer.setValue(timer.getValue() + (current - previous));
		}
	}
	
	public static void deltaUpdate(){
		previousDelta = currentDelta;
		currentDelta = System.currentTimeMillis();
		delta = currentDelta - previousDelta;
	}
	
	public static void addTimer(String name){
		timers.put(name, 0L);
	}
	
	public static Long getNanoTime(String name){
		return timers.get(name);
	}
	
	public static Long getMillisTime(String name){
		return timers.get(name)/1000000;
	}
	
	public static void setValue(String name,Long value){
		timers.replace(name, value);
	}
	
	public static int getDeltaTime(){
		return (int)delta;
	}
	
}
