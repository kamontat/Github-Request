package server;

import model.User;
import org.kohsuke.github.GitHub;

import java.io.IOException;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 2:59 PM
 */
public class GHAccount {
	// you can get token in this link `https://github.com/settings/tokens`
	private static final String TOKEN = "925cc49f2798daae39b0e594896fdea9388e528f"; // if have token rate_limit will be `5000`, otherwise rate_limit will be `60`
	
	private GitHub gh;
	private User user;
	
	public GHAccount(String user) {
		try {
			gh = getGithub();
			this.user = getUser(user);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GHAccount(User user) {
		try {
			gh = getGithub();
			this.user = user;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static GitHub getGithub() throws IOException {
		if (TOKEN == null || TOKEN.equals("")) {
			return GitHub.connectAnonymously();
		} else {
			return GitHub.connectUsingOAuth(TOKEN);
		}
	}
	
	public static User getUser(String username) throws IOException {
		return new User(getGithub().getUser(username));
	}
}
