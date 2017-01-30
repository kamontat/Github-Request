package com.kamontat.exception;

import com.kamontat.constant.RequestStatus;

/**
 * @author kamontat
 * @version 2.1
 * @since 1/26/2017 AD - 3:49 PM
 */
public class RequestException extends Exception {
	private RequestStatus status;
	private String username;
	private String repoName;
	
	public RequestException(Exception e, String user, String repository) {
		this(Error.get(e).getRequestCode(), user, repository);
	}
	
	public RequestException(RequestStatus status, String fullName, String repoName) {
		super(status.toString());
		this.status = status;
		this.username = fullName;
		this.repoName = repoName;
	}
	
	public RequestException(Exception e, String fullName) {
		this(Error.get(e).getRequestCode(), fullName);
	}
	
	public RequestException(RequestStatus status, String fullName) {
		super(status.toString());
		this.status = status;
		this.username = fullName;
	}
	
	public RequestException(Exception e) {
		this(Error.get(e).getRequestCode());
	}
	
	public RequestException(RequestStatus status) {
		super(status.toString());
		this.status = status;
	}
	
	public RequestStatus getRequestCode() {
		return status;
	}
	
	public String getDescription() {
		return status.toString();
	}
	
	public void printStackTrace() {
		System.err.println(status.getFullDescription(username, repoName));
		//		super.printStackTrace();
	}
}
