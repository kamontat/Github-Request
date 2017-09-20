package com.kamontat.server;

import com.kamontat.model.encryption.Encryption;
import com.kamontat.model.management.Cache;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.io.Serializable;

// FIXME: 2/17/2017 AD I think saving and loading object from caches file can't loading in other day!

/**
 * this class very importance in <code>GithubLoader</code> class <br>
 * this will use to sign in into github by using Token
 * Normally when you login, you need to enter username and p but to authentication, you can use token as the same way as username and p.
 * you can getLog easy your token by login your github and go to this link
 * <ul>
 * <li><code>https://github.com/settings/tokens/new</code> - To generate new token</li>
 * <li><code>https://github.com/settings/tokens</code> - To manage your exist token</li>
 * </ul>
 * and this class also use com.kamontat.cache to keep your encrypt token pass using <code>Encryption</code> class
 *
 * @author kamontat
 * @version 2.3
 * @see GithubLoader
 * @see java.io.Serializable
 * @see Cache
 * @see Encryption
 * @since 1/28/2017 AD - 12:51 AM
 */
public class GithubToken implements Serializable {
	// stand for version
	static final long serialVersionUID = 3L;
	// default name of com.kamontat.cache file
	private static final String FILE_NAME = "Token";
	// token
	private String token;
	// check that user set password or not
	private boolean p;
	
	/**
	 * return empty token
	 *
	 * @return empty token
	 */
	private static GithubToken getEmptyToken() {
		return new GithubToken(null);
	}
	
	/**
	 * constructor that will create token by parameter
	 *
	 * @param token
	 * 		github token
	 */
	public GithubToken(String token) {
		this.token = token;
	}
	
	/**
	 * reset token to empty
	 */
	public void resetToken() {
		token = null;
	}
	
	/**
	 * getLog real token or <b>empty string</b> ("") if it's <b>empty token</b>
	 *
	 * @return token
	 */
	public String getToken() {
		return token == null ? "": token;
	}
	
	/**
	 * check is current token valid or not <b>(using internet)</b> <br>
	 * this method may be slow
	 *
	 * @return true if token valid; false otherwise
	 */
	public boolean isTokenValid() {
		try {
			GitHub.connectUsingOAuth(token);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * encrypt current github token by using p
	 *
	 * @param password
	 * 		encrypt p
	 * @return Encrypt GithubToken
	 * @throws Exception
	 * 		every exception that occurred
	 */
	private GithubToken encryptGT(String password) throws Exception {
		return new GithubToken(Encryption.encode(password, token));
	}
	
	/**
	 * decrypt current github token by using p
	 *
	 * @param password
	 * 		decrypt p
	 * @return Normal GithubToken
	 * @throws Exception
	 * 		mostly will throw if p wrong
	 */
	private GithubToken decryptGT(String password) throws Exception {
		this.token = Encryption.decode(password, token);
		return this;
	}
	
	/**
	 * save current github token into com.kamontat.cache file
	 *
	 * @param password
	 * 		p that need to decryption later
	 * @return true if save successfully; false otherwise
	 */
	public boolean saveCache(String password) {
		if (password.equals("")) {
			this.p = false;
		}
		this.p = true;
		try {
			Cache.loadCache(FILE_NAME).saveToFile(this.encryptGT(password));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * save current github token into com.kamontat.cache file with option <code>not use password</code>
	 *
	 * @return true if save successfully; false otherwise
	 */
	public boolean saveCache() {
		return saveCache("");
	}
	
	
	/**
	 * load github token from com.kamontat.cache file by using p that create when save com.kamontat.cache
	 *
	 * @param password
	 * 		decryption p
	 * @return real token if p correctly; false otherwise
	 */
	public static GithubToken loadCache(String password) {
		try {
			if (Cache.loadCache(FILE_NAME).isExist()) {
				GithubToken ght = Cache.loadCache(FILE_NAME).loadFromFile(GithubToken.class).decryptGT(password);
				if (ght.isTokenValid()) return ght;
			}
		} catch (Exception ignored) {
		}
		return GithubToken.getEmptyToken();
	}
	
	/**
	 * load github token from com.kamontat.cache file by that must save with option <code>not use password</code>
	 *
	 * @return real token if and only if com.kamontat.cache save with option <code>not use password</code>, otherwise return empty token
	 */
	public static GithubToken loadCache() {
		return loadCache("");
	}
	
	/**
	 * delete token com.kamontat.cache if it exist
	 */
	public static void removeCache() {
		if (haveCache()) Cache.loadCache(FILE_NAME).delete();
	}
	
	/**
	 * check is current token is empty or not
	 *
	 * @return true if empty; otherwise return false
	 */
	public boolean isEmptyToken() {
		return token == null;
	}
	
	/**
	 * check is have token com.kamontat.cache or not
	 *
	 * @return true if have com.kamontat.cache, false in otherwise
	 */
	public static boolean haveCache() {
		return Cache.loadCache(FILE_NAME).isExist();
	}
	
	/**
	 * return string how to create new token
	 *
	 * @return helping create token string
	 */
	public static String getHelp() {
		return "create new one at https://github.com/settings/tokens/new/";
	}
	
	@Override
	public String toString() {
		return "GithubToken{" + "token='" + token + '\'' + '}';
	}
}
