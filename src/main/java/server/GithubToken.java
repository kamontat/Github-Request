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
	
	private static GithubToken githubToken;
	
	static GithubToken getGT() {
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
	
	boolean isTokenValid() {
		try {
			GitHub.connectUsingOAuth(token);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getHelp() {
		return "you can create your account token in link: https://github.com/settings/tokens/new/";
	}
}
