package server;

import exception.RequestException;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/28/2017 AD - 12:51 AM
 */
public class GithubToken implements Serializable {
	static final long serialVersionUID = 1L;
	private String token;
	
	// if have token rate_limit will be `5000`, otherwise rate_limit will be `60`
	// final String TOKEN = "925cc49f2798daae39b0e594896fdea9388e528f";
	
	private static GithubToken githubToken;
	
	public static GithubToken getGT() {
		if (githubToken == null) githubToken = new GithubToken();
		return githubToken;
	}
	
	public void setToken(String token) {
		if (this.token == null) this.token = token;
	}
	
	public void resetToken() {
		token = null;
	}
	
	String getToken() {
		return token;
	}
	
	public boolean isTokenValid() {
		try {
			GitHub.connectUsingOAuth(token);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static String getHelp() {
		return "you can create your account token in link: https://github.com/settings/tokens/new/";
	}
}
