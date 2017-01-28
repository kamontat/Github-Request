package cache;

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
	
	public static Cache loadCache(String path, String name) {
		return new Cache(path, name);
	}
	
	private Cache(String path, String name) {
		this.path = path;
		this.name = name;
		updateLocation();
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
		} catch (IOException | ClassNotFoundException e) {
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
	}
}
