package com.kamontat.model;

import com.kamontat.model.encryption.Encryption;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bubblebitoey on 1/28/2017 AD.
 */
public class EncryptionTest {
	private String pass;
	private String text;
	
	@Test
	public void encryptionNormal() {
		pass = "kamontat";
		text = "password";
		
		try {
			String code1 = Encryption.encode(pass, text);
			String code2 = Encryption.encode(pass, text);
			String code3 = Encryption.encode(pass, text);
			
			String text1 = Encryption.decode(pass, code1);
			String text2 = Encryption.decode(pass, code2);
			String text3 = Encryption.decode(pass, code3);
			
			assertEquals(text, text1);
			assertEquals(text, text2);
			assertEquals(text, text3);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void encryptionWithSpace() {
		pass = "password";
		text = "text with space";
		try {
			String code1 = Encryption.encode(pass, text);
			String code2 = Encryption.encode(pass, text);
			
			String text1 = Encryption.decode(pass, code1);
			String text2 = Encryption.decode(pass, code2);
			
			assertEquals(text, text1);
			assertEquals(text, text2);
			
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void encryptionWithLongLongText() {
		try {
			pass = "password";
			text = "asdfghjklqwertyuiozxcvbnmasdfghqwert";
			
			String text1 = Encryption.decode(pass, Encryption.encode(pass, text));
			
			assertEquals(text, text1);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void encryptionWithLongLongPassword() {
		pass = "asdfghjklqwertyuiozxcvbnmasdfghqwertasdfqewrzxvzxcv";
		text = "password";
		try {
			String text1 = Encryption.decode(pass, Encryption.encode(pass, text));
			String text2 = Encryption.decode(pass, Encryption.encode(pass, text));
			String text3 = Encryption.decode(pass, Encryption.encode(pass, text));
			
			assertEquals(text, text1);
			assertEquals(text, text2);
			assertEquals(text, text3);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
}