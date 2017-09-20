package com.kamontat.cache;

import com.kamontat.model.management.Cache;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by bubblebitoey on 1/28/2017 AD.
 */
public class CacheTest {
	private static final String FILE_NAME = "test";
	
	@Test
	public void testString() {
		String text = "Some String";
		
		Cache.loadCache(FILE_NAME).saveToFile(text);
		
		String loadString = Cache.loadCache(FILE_NAME).loadFromFile(text.getClass());
		
		assertEquals(text, loadString);
	}
	
	@Test
	public void testXString() {
		String text = "S&P";
		
		Cache.loadCache(FILE_NAME).saveToFile(text);
		
		String loadString = Cache.loadCache(FILE_NAME).loadFromFile(text.getClass());
		
		assertEquals(text, loadString);
	}
	
	@Test
	public void testChar() {
		Character c = 'y';
		Cache.loadCache(FILE_NAME).saveToFile(c);
		
		Character loadChar = Cache.loadCache(FILE_NAME).loadFromFile(c.getClass());
		
		assertEquals(c, loadChar);
	}
	
	@Test
	public void testInteger() {
		Integer integer = 120000;
		
		Cache.loadCache(FILE_NAME).saveToFile(integer);
		
		Integer loadInt = Cache.loadCache(FILE_NAME).loadFromFile(integer.getClass());
		
		assertEquals(integer, loadInt);
	}
	
	@Test
	public void testArray() {
		String[] arr = new String[]{"I", "Hate", "This", "App"};
		
		Cache.loadCache(FILE_NAME).saveToFile(arr);
		
		String[] loadArr = Cache.loadCache(FILE_NAME).loadFromFile(arr.getClass());
		
		assertArrayEquals(arr, loadArr);
	}
	
	@AfterClass
	public static void removeTestFile() {
		System.out.println(Cache.loadCache(FILE_NAME).delete() ? "remove file complete": "fail");
	}
}