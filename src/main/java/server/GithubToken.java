package server;

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
	
	public static GithubToken getGitToken(String password, String token) {
		if (githubToken == null) githubToken = new GithubToken(token);
		return githubToken;
	}
	
	private GithubToken(String token) {
		this.token = token;
	}
	
	public String getToken() {
		
		return token;
	}
	
	/**
	 * beware to use this method
	 *
	 * @param token
	 * 		the token
	 */
	public void forceSetToken(String token) {
		this.token = token;
	}
	
	public boolean have() {
		return token != null;
	}
	
	@Override
	public String toString() {
		return "GithubToken{" + "token='" + token + '\'' + '}';
	}
}
