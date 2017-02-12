package com.kamontat.model.encryption;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.Serializable;

/**
 * This class use library from <b>jasypt</b> to encryption
 *
 * @author kamontat
 * @version 1.3
 * @see <a href="www.jasypt.com">jasypt</a>
 * @since 1/28/2017 AD - 2:34 AM
 */
public class Encryption implements Serializable {
	static final long serialVersionUID = 3L;
	
	private StandardPBEStringEncryptor encryption;
	
	/**
	 * decrypt the code by using real password <br>
	 * and this method will change your real password to some unknown password later <br>
	 * so you can't decrypt the code by yourself <br>
	 *
	 * @param rawPass
	 * 		real password to decrypt
	 * @param code
	 * 		text code
	 * @return normal text
	 * @throws Exception
	 * 		mostly throws when password was wrong, but there have another exception else
	 */
	public static String decode(String rawPass, String code) throws Exception {
		return new Encryption(getAdvancedEncryption(rawPass)).decode(code);
	}
	
	/**
	 * encrypt the text by using real password <br>
	 * and this method will change your real password to some unknown password later <br>
	 * so if you direct encode the password, you shouldn't use <code>decode</code> method in this class
	 *
	 * @param rawPass
	 * 		real password to encrypt
	 * @param text
	 * 		normal text
	 * @return encrypt code
	 * @throws Exception
	 * 		every exception that occurred
	 */
	public static String encode(String rawPass, String text) throws Exception {
		return new Encryption(getAdvancedEncryption(rawPass)).encode(text);
	}
	
	/**
	 * save password into encryption to decode and encode later
	 *
	 * @param password
	 * 		real password
	 */
	private Encryption(String password) {
		configEncryption(password);
	}
	
	/**
	 * decrypt the text by using password
	 *
	 * @param code
	 * 		encrypt text
	 * @return normal text
	 */
	private String decode(String code) {
		return encryption.decrypt(code);
	}
	
	/**
	 * encrypt the text by using password
	 *
	 * @param text
	 * 		normal text
	 * @return encrypt text
	 */
	private String encode(String text) {
		return encryption.encrypt(text);
	}
	
	/**
	 * encryption pass by using advanced encryption
	 *
	 * @param pass
	 * 		text or password to encrypt
	 * @return advanced encrypt string
	 */
	private static String getAdvancedEncryption(String pass) {
		StandardStringDigester p = new StandardStringDigester();
		p.setSaltGenerator(NoSaltGenerator.get());
		p.setAlgorithm("SHA-512");
		p.setStringOutputType("HEX");
		return p.digest(pass);
	}
	
	/**
	 * config encryption
	 * current config is
	 * <ul>
	 * <li>output as HEX string</li>
	 * </ul>
	 *
	 * @param pass
	 * 		encryption must have password to encrypt or decrypt
	 */
	private void configEncryption(String pass) {
		encryption = new StandardPBEStringEncryptor();
		encryption.setStringOutputType("HEX");
		encryption.setPassword(pass);
	}
	
}
