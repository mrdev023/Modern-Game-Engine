package mrdev023.utils;

import java.lang.reflect.*;

import mrdev023.*;
import sun.misc.*;

public class Pointer{
	
	private static Unsafe unsafe = null;
	
	static{
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (sun.misc.Unsafe) field.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
	}
	
    private long address;
    
	public Pointer(){
    	address = unsafe.allocateMemory(unsafe.addressSize());
    }
    
    public Pointer(long address){
    	this.address = address;
    }
    
    public Pointer(Object obj){
    	this.address = unsafe.allocateMemory(unsafe.addressSize());
    	setValue(obj);
    }
    
    public void setValue(Object value){
    	unsafe.putObject(0, address, value);
    }
    
    public Object getValue(){
    	return unsafe.getObject(0, address);
    }
    
    public void delete(){
    	unsafe.freeMemory(address);
    }

	public static Unsafe getUnsafe() {
		return unsafe;
	}

	public static void setUnsafe(Unsafe unsafe) {
		Pointer.unsafe = unsafe;
	}

	public String toString(){
		return Long.toHexString(address).toUpperCase();
	}

	public long getAddress() {
		return address;
	}

	public void setAddress(long address) {
		this.address = address;
	}
	
}