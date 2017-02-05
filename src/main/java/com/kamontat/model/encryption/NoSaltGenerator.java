package com.kamontat.model.encryption;

import org.jasypt.salt.FixedSaltGenerator;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/28/2017 AD - 3:18 AM
 */
public class NoSaltGenerator implements FixedSaltGenerator {
	private static NoSaltGenerator no;
	
	public static NoSaltGenerator get() {
		if (no == null) no = new NoSaltGenerator();
		return no;
	}
	
	@Override
	public byte[] generateSalt(int lengthBytes) {
		return new byte[0];
	}
	
	@Override
	public boolean includePlainSaltInEncryptionResults() {
		return false;
	}
}
