package com.kamontat.cache;

import java.io.*;

/**
 * @author kamontat
 * @version 2.0
 * @since 1/28/2017 AD - 1:04 AM
 */
public class Cache {
	// setting file
	private String path = "caches/";
	private String name = "Caches";
	
	private File file = new File(path + name);
	
	public static Cache loadCache(String name) {
		return new Cache(name);
	}
	
	private Cache(String name) {
		this.name = name;
		updateLocation();
	}
	
	public <E> void saveToFile(E obj) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obj);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public <E> E loadFromFile(Class<E> eClass) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			return (E) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isExist() {
		return file.exists();
	}
	
	public boolean delete() {
		return file.delete();
	}
	
	private void updateLocation() {
		file = new File(path + name);
		pathSetup();
	}
	
	private void pathSetup() {
		File f = new File(path);
		
		// if don't have folder
		if (!f.exists()) {
			f.mkdir();
		}
		// if not folder
		if (!f.isDirectory()) {
			if (f.delete()) {
				f.mkdir();
			}
		}
	}
}
