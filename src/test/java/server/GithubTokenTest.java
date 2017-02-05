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
	private static String token = "token";
	
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
		final GithubToken[] newOne = new GithubToken[1];
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				newOne[0] = GithubToken.loadCache(password);
			}
		});
		t.start();
		
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(token, newOne[0].getToken());
		
	}
	
	@Test
	public void checkTokenLoadFail() {
		String otherPass = "haha";
		
		GithubToken newOne = GithubToken.loadCache(otherPass);
		
		assertEquals("", newOne.getToken());
	}
}