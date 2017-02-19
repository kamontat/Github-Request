package com.kamontat.exception;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/19/2017 AD - 10:32 PM
 */
public class NonException extends Exception {
	public static NonException get() {
		return new NonException();
	}
	
	private NonException() {
		super();
	}
	
	@Override
	public void printStackTrace() {
		// do nothing
	}
}
