package server;

import com.kamontat.server.GithubToken;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by bubblebitoey on 1/28/2017 AD.
 */
public class GithubTokenTest {
	private static String password = "pass";
	private static String token = "fef5a7179eff6366d8b7c5f11fc94479485681da";
	
	private static GithubToken gt = new GithubToken(token);
	
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
		final GithubToken newOne = GithubToken.loadCache(password);
		assertEquals(token, newOne.getToken());
		
	}
	
	@Test
	public void checkTokenLoadFail() {
		String otherPass = "haha";
		
		GithubToken newOne = GithubToken.loadCache(otherPass);
		
		assertEquals("", newOne.getToken());
	}
}