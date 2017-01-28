package model;

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
		
		String code1 = Encryption.encode(pass, text);
		String code2 = Encryption.encode(pass, text);
		String code3 = Encryption.encode(pass, text);
		
		String text1 = Encryption.decode(pass, code1);
		String text2 = Encryption.decode(pass, code2);
		String text3 = Encryption.decode(pass, code3);
		
		assertEquals(text, text1);
		assertEquals(text, text2);
		assertEquals(text, text3);
	}
	
	@Test
	public void encryptionWithSpace() {
		pass = "password";
		text = "text with space";
		
		String code1 = Encryption.encode(pass, text);
		String code2 = Encryption.encode(pass, text);
		
		String text1 = Encryption.decode(pass, code1);
		String text2 = Encryption.decode(pass, code2);
		
		assertEquals(text, text1);
		assertEquals(text, text2);
	}
	
	@Test
	public void encryptionWithLongLongText() {
		pass = "password";
		text = "asdfghjklqwertyuiozxcvbnmasdfghqwert";
		
		String text1 = Encryption.decode(pass, Encryption.encode(pass, text));
		
		assertEquals(text, text1);
	}
	
	@Test
	public void encryptionWithLongLongPassword() {
		pass = "asdfghjklqwertyuiozxcvbnmasdfghqwertasdfqewrzxvzxcv";
		text = "password";
		
		String text1 = Encryption.decode(pass, Encryption.encode(pass, text));
		String text2 = Encryption.decode(pass, Encryption.encode(pass, text));
		String text3 = Encryption.decode(pass, Encryption.encode(pass, text));
		
		assertEquals(text, text1);
		assertEquals(text, text2);
		assertEquals(text, text3);
	}
	
}