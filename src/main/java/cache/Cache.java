package cache;

import java.io.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/28/2017 AD - 1:04 AM
 */
public class Cache {
	// setting file
	private String path = "";
	private String name = "Caches";
	
	private File file = new File(path + name);
	// singleton
	private static Cache cache;
	
	public static Cache loadCache() {
		if (cache == null) cache = new Cache();
		return cache;
	}
	
	public Cache setFilePath(String path) {
		cache.path = path;
		updateLocation();
		return cache;
	}
	
	public Cache setFileName(String name) {
		cache.name = name;
		updateLocation();
		return cache;
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
	
	private void updateLocation() {
		file = new File(path + name);
	}
}
