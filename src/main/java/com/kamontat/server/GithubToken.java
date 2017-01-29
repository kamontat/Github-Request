package com.kamontat.server;

import com.kamontat.cache.Cache;
import com.kamontat.model.Encryption;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author kamontat
 * @version 2.2
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
	
	public static GithubToken getEmptyToken() {
		return new GithubToken(null);
	}
	
	public void resetToken() {
		token = null;
	}
	
	public String getToken() {
		return token == null ? "": token;
	}
	
	public boolean isTokenValid() {
		try {
			GitHub.connectUsingOAuth(token);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	private GithubToken encryptGT(String password) throws Exception {
		return new GithubToken(Encryption.encode(password, token));
	}
	
	private GithubToken decryptGT(String password) throws Exception {
		this.token = Encryption.decode(password, token);
		return this;
	}
	
	public boolean saveCache(String password) {
		try {
			Cache.loadCache(FILE_NAME).saveToFile(this.encryptGT(password));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static GithubToken loadCache(String password) {
		try {
			if (Cache.loadCache(FILE_NAME).isExist())
				return Cache.loadCache(FILE_NAME).loadFromFile(GithubToken.class).decryptGT(password);
			else return GithubToken.getEmptyToken();
		} catch (Exception e) {
			return GithubToken.getEmptyToken();
		}
	}
	
	public static void removeCache() {
		if (Cache.loadCache(FILE_NAME).isExist()) Cache.loadCache(FILE_NAME).delete();
	}
	
	public boolean isEmptyToken() {
		return token == null;
	}
	
	public static boolean haveCache() {
		return Cache.loadCache(FILE_NAME).isExist();
	}
	
	public static String getHelp() {
		return "you can create your account token in link: https://github.com/settings/tokens/new/";
	}
	
	@Override
	public String toString() {
		return "GithubToken{" + "token='" + token + '\'' + '}';
	}
}
