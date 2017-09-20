package com.kamontat.model.management;

import com.kamontat.controller.popup.Popup;

import java.io.*;

/**
 * This class for save the object into file and can restore every time <br>
 * Easy to use and manage <br>
 * 1) call method loadCache(file name) to getLog name of the file com.kamontat.cache <br>
 * 2) and than there have 4 method that you can use it
 * <ol>
 * <li><code>saveToFile(object)</code> - must be sure that <i>object</i> implements <code>Serializable</code>.</li>
 * <li><code>loadFromFile(class)</code> - you can specify return class by pass in parameter.</li>
 * <li><code>isExist()</code> - to check that com.kamontat.cache file is exist or not.</li>
 * <li><code>delete()</code> - delete com.kamontat.cache file.</li>
 * </ol>
 *
 * @author kamontat
 * @version 2.1
 * @since 1/28/2017 AD - 1:04 AM
 */
public class Cache {
	// default com.kamontat.cache folder
	private String path = "caches/";
	// default com.kamontat.cache name
	private String name = "com.kamontat.cache";
	// com.kamontat.cache file
	private File file = new File(path + name);
	
	/**
	 * only this method than can return <code>Cache</code>
	 * this method will create new com.kamontat.cache and name it by parameter
	 *
	 * @param fileName
	 * 		com.kamontat.cache file name
	 * @return <code>Cache</code> that you can use later
	 */
	public static Cache loadCache(String fileName) {
		return new Cache(fileName);
	}
	
	/**
	 * private constructor
	 * set file name and update location
	 *
	 * @param name
	 * 		file name
	 */
	private Cache(String name) {
		this.name = name;
		updateLocation();
	}
	
	/**
	 * save the object in default folder which is <b>caches</b> with file name that you naming before
	 *
	 * @param obj
	 * 		object that want to save
	 * @param <E>
	 * 		object must implements <code>Serializable</code>
	 */
	public <E extends Serializable> void saveToFile(E obj) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obj);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * load object from file that you naming before. <br>
	 * the return object class will same as the parameter so pass clearing parameter <br>
	 * or you don't know the object class you can pass <code>Object.class</code> <br>
	 * Example:
	 * <pre><code>
	 * // return String
	 * String name = Cache.loadCache("name").loadFromFile(String.class);
	 * </code></pre>
	 * <pre><code>
	 * // return Object
	 * Object object = Cache.loadCache("obj").loadFromFile(Object.class);
	 * </code></pre>
	 *
	 * @param eClass
	 * 		you can getLog class by using <code>.class</code>
	 * @param <E>
	 * 		every class in java.
	 * @return saving object in com.kamontat.cache file by from of <code>eClass</code>. <br>
	 * or <code>null</code> if
	 * <ul>
	 * <li>Class not found.</li>
	 * <li>Cannot cast result object to <code>eClass</code>.</li>
	 * <li>Have problem when load file.</li>
	 * </ul>
	 */
	public <E> E loadFromFile(Class<E> eClass) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			return (E) ois.readObject();
		} catch (InvalidClassException e) {
			Popup.getLog(null).error("Invalid Cache Version", "Your saving Cache version is too old");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * delete com.kamontat.cache file.
	 *
	 * @return true if delete completely, otherwise return false.
	 */
	public boolean delete() {
		return file.delete();
	}
	
	/**
	 * check is com.kamontat.cache file exist or not.
	 *
	 * @return true if exist, otherwise return false.
	 */
	public boolean isExist() {
		return file.exists();
	}
	
	/**
	 * update file to current location
	 */
	private void updateLocation() {
		file = new File(path + name);
		pathSetup();
	}
	
	/**
	 * create folder <b>caches</b> id not exist
	 * or delete and recreate if that file not a folder
	 */
	private void pathSetup() {
		File f = new File(path);
		// if don't have folder
		if (!f.exists()) {
			if (f.mkdir()) {
				System.out.println("create folder com.kamontat.cache");
			}
		}
		// if not folder
		if (!f.isDirectory()) {
			if (f.delete()) {
				if (f.mkdir()) {
					System.out.println("create folder com.kamontat.cache");
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("Cache{ \n\tfile    = %s \n\tisExist = %s \n}", file.getAbsolutePath(), isExist());
	}
}
