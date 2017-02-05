package com.kamontat.server;

import com.kamontat.model.management.Cache;
import com.kamontat.model.encryption.Encryption;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.io.Serializable;

/**
 * this class very importance in <code>GithubLoader</code> class <br>
 * this will use to sign in into github by using Token
 * Normally when you login, you need to enter username and password but to authentication, you can use token as the same way as username and password.
 * you can get easy your token by login your github and go to this link
 * <ul>
 * <li><code>https://github.com/settings/tokens/new</code> - To generate new token</li>
 * <li><code>https://github.com/settings/tokens</code> - To manage your exist token</li>
 * </ul>
 * and this class also use cache to keep your encrypt token pass using <code>Encryption</code> class
 *
 * @author kamontat
 * @version 2.2
 * @see GithubLoader
 * @see java.io.Serializable
 * @see Cache
 * @see Encryption
 * @since 1/28/2017 AD - 12:51 AM
 */
public class GithubToken implements Serializable {
	// stand for version
	static final long serialVersionUID = 2L;
	// default name of cache file
	private static final String FILE_NAME = "Token";
	// token
	private String token;
	
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
	 * get real token or <b>empty string</b> ("") if it's <b>empty token</b>
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
	 * encrypt current github token by using password
	 *
	 * @param password
	 * 		encrypt password
	 * @return Encrypt GithubToken
	 * @throws Exception
	 * 		every exception that occurred
	 */
	private GithubToken encryptGT(String password) throws Exception {
		return new GithubToken(Encryption.encode(password, token));
	}
	
	/**
	 * decrypt current github token by using password
	 *
	 * @param password
	 * 		decrypt password
	 * @return Normal GithubToken
	 * @throws Exception
	 * 		mostly will throw if password wrong
	 */
	private GithubToken decryptGT(String password) throws Exception {
		this.token = Encryption.decode(password, token);
		return this;
	}
	
	/**
	 * save current github token into cache file
	 *
	 * @param password
	 * 		password that need to decryption later
	 * @return true if save successfully; false otherwise
	 */
	public boolean saveCache(String password) {
		try {
			Cache.loadCache(FILE_NAME).saveToFile(this.encryptGT(password));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * load github token from cache file by using password that create when save cache
	 *
	 * @param password
	 * 		decryption password
	 * @return real token if password correctly; false otherwise
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
	 * delete token cache if it exist
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
	 * check is have token cache or not
	 *
	 * @return true if have cache, false in otherwise
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
