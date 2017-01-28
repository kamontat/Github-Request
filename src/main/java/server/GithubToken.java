package server;

import cache.Cache;
import exception.RequestException;
import model.Encryption;
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
	private static final String FILE_NAME = "Token";
	private String token;
	
	// if have token rate_limit will be `5000`, otherwise rate_limit will be `60`
	// final String TOKEN = "925cc49f2798daae39b0e594896fdea9388e528f";
	
	public GithubToken(String token) {
		this.token = token;
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
	
	private GithubToken encryptGT(String password) {
		this.token = Encryption.encode(password, token);
		return this;
	}
	
	private GithubToken decryptGT(String password) {
		this.token = Encryption.decode(password, token);
		return this;
	}
	
	public void saveCache(String password) {
		Cache.loadCache().setFileName(FILE_NAME).saveToFile(this.encryptGT(password));
	}
	
	public static GithubToken loadCache(String password) {
		return Cache.loadCache().setFileName(FILE_NAME).loadFromFile(GithubToken.class).decryptGT(password);
	}
	
	public static String getHelp() {
		return "you can create your account token in link: https://github.com/settings/tokens/new/";
	}
}
