package model;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.Serializable;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/28/2017 AD - 2:34 AM
 */
public class Encryption implements Serializable {
	static final long serialVersionUID = 1L;
	
	private StandardPBEStringEncryptor stringEncryptor;
	
	public static Encryption get(String pass) {
		return new Encryption(pass);
	}
	
	private Encryption(String password) {
		String pass = getPassword(password);
		configEncryption(pass);
	}
	
	public String decode(String password, String code) {
		stringEncryptor.setPassword(getPassword(password));
		return stringEncryptor.decrypt(code);
	}
	
	public String encode(String password, String text) {
		stringEncryptor.setPassword(getPassword(password));
		return stringEncryptor.encrypt(text);
	}
	
	private String getPassword(String pass) {
		StandardStringDigester p = new StandardStringDigester();
		p.setSaltGenerator(NoSaltGenerator.get());
		//		p.setAlgorithm("SHA-512");
		return pass;
		//		return p.digest(pass);
	}
	
	private void configEncryption(String pass) {
		stringEncryptor = new StandardPBEStringEncryptor();
		stringEncryptor.setSaltGenerator(NoSaltGenerator.get());
		stringEncryptor.setStringOutputType("HEX");
		stringEncryptor.setPassword(pass);
	}
	
}
