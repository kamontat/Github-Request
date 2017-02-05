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
	
	public RequestException(String user, String repository) {
		this(RequestStatus.ERROR, user, repository);
	}
	
	public RequestException(RequestStatus status, String fullName, String repoName) {
		super(status.toString());
		this.status = status;
		this.username = fullName;
		this.repoName = repoName;
	}
	
	public RequestException(String fullName) {
		this(RequestStatus.ERROR, fullName);
	}
	
	public RequestException(RequestStatus status, String fullName) {
		super(status.toString());
		this.status = status;
		this.username = fullName;
	}
	
	public RequestException() {
		this(RequestStatus.ERROR);
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
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RequestException && ((RequestException) obj).status == status;
	}
	
	public void printStackTrace() {
		System.err.println(status.getFullDescription(username, repoName));
		super.printStackTrace();
	}
}
