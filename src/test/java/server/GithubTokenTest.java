package server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bubblebitoey on 1/28/2017 AD.
 */
public class GithubTokenTest {
	private static String password = "pass";
	private static String token = "token";
	
	private static GithubToken gt;
	
	@BeforeClass
	public static void initGT() {
		gt = new GithubToken(token);
	}
	
	@Test
	public void checkToken() {
		gt.saveCache(password);
		
		assertEquals(token, gt.getToken());
	}
	
	@Test
	public void checkTokenLoadComplete() {
		GithubToken.loadCache(password);
		
		assertEquals(token, gt.getToken());
	}
	
	@Test
	public void checkTokenLoadFail() {
		String otherPass = "haha";
		
		GithubToken.loadCache(otherPass);
		
		assertEquals("", gt.getToken());
	}
	
	
	@AfterClass
	public static void remove() {
		gt.removeCache();
	}
}