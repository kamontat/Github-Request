package com.kamontat.exception;

import com.kamontat.constant.RequestStatus;
import org.kohsuke.github.HttpException;

import java.io.FileNotFoundException;

import static com.kamontat.constant.RequestStatus.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 3:44 PM
 */
public class Error {
	private Exception e;
	private static Error error;
	
	public static Error get(Exception e) {
		if (error == null) error = new Error();
		error.e = e;
		return error;
	}
	
	public RequestStatus getRequestCode() {
		if (e instanceof RequestException) {
			return ((RequestException) e).getRequestCode();
		} else if (e instanceof FileNotFoundException) {
			return USER_NOT_FOUND;
		} else if (e instanceof HttpException) {
			return INTERNET_ERROR;
		} else {
			return ERROR;
		}
	}
}
