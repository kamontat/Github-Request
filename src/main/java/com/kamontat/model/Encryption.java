package com.kamontat.model;

import org.jasypt.digest.StandardStringDigester;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.Serializable;

/**
 * @author kamontat
 * @version 1.2
 * @since 1/28/2017 AD - 2:34 AM
 */
public class Encryption implements Serializable {
	private static final String DEFAULT_PASSWORD = "Hello-World";
	static final long serialVersionUID = 1L;
	
	private StandardPBEStringEncryptor stringEncryptor;
	
	public static String decode(String rawPass, String code) throws Exception {
		return new Encryption(getPassword(rawPass)).decode(code);
	}
	
	public static String encode(String rawPass, String code) throws Exception {
		return new Encryption(getPassword(rawPass)).encode(code);
	}
	
	private static Encryption get(String rawPass) {
		return new Encryption(getPassword(rawPass));
	}
	
	private Encryption(String password) {
		configEncryption(password);
	}
	
	private String decode(String code) {
		return stringEncryptor.decrypt(code);
	}
	
	private String encode(String text) {
		return stringEncryptor.encrypt(text);
	}
	
	private static String getPassword(String pass) {
		StandardStringDigester p = new StandardStringDigester();
		p.setSaltGenerator(NoSaltGenerator.get());
		p.setAlgorithm("SHA-512");
		p.setStringOutputType("HEX");
		return p.digest(pass);
	}
	
	private void configEncryption(String pass) {
		stringEncryptor = new StandardPBEStringEncryptor();
		stringEncryptor.setStringOutputType("HEX");
		stringEncryptor.setPassword(pass);
	}
	
}
