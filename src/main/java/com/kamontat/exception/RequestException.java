package com.kamontat.exception;

import com.kamontat.constant.RequestStatus;

/**
 * this class is exception class and using with {@link RequestStatus} class <br>
 * because github-api framework doesn't provide any exception that i will use in program, so i need to do it by myself
 *
 * @author kamontat
 * @version 2.1
 * @since 1/26/2017 AD - 3:49 PM
 */
public class RequestException extends Exception {
	private Exception e;
	private RequestStatus status;
	private String username;
	private String repoName;
	
	/**
	 * set request status as <code>Error</code>
	 *
	 * @param user
	 * 		user that make request
	 * @param repository
	 * 		repository that make request
	 */
	public RequestException(Exception e, String user, String repository) {
		this(e, RequestStatus.ERROR, user, repository);
	}
	
	/**
	 * custom request status error by using enum {@link RequestStatus}
	 *
	 * @param status
	 * 		some status that will tell user to know which error had occurred
	 * @param fullName
	 * 		name of user that make error
	 * @param repoName
	 * 		name of repository that make error
	 */
	public RequestException(Exception e, RequestStatus status, String fullName, String repoName) {
		super(status.toString());
		this.e = e;
		this.status = status;
		this.username = fullName;
		this.repoName = repoName;
	}
	
	/**
	 * set request status as <code>Error</code>
	 *
	 * @param fullName
	 * 		name of user that make request
	 */
	public RequestException(Exception e, String fullName) {
		this(e, RequestStatus.ERROR, fullName);
	}
	
	/**
	 * custom request status error by using enum {@link RequestStatus}
	 *
	 * @param status
	 * 		some status that will tell user to know which error had occurred
	 * @param fullName
	 * 		name of user that make error
	 */
	public RequestException(Exception e, RequestStatus status, String fullName) {
		super(status.toString());
		this.e = e;
		this.status = status;
		this.username = fullName;
	}
	
	/**
	 * set request status as <code>Error</code>
	 */
	public RequestException(Exception e) {
		this(e, RequestStatus.ERROR);
	}
	
	/**
	 * custom request status error by using enum {@link RequestStatus}
	 *
	 * @param status
	 * 		some status that will tell user to know which error had occurred
	 */
	public RequestException(Exception e, RequestStatus status) {
		super(status.toString());
		this.e = e;
		this.status = status;
	}
	
	/**
	 * get error status
	 *
	 * @return {@link RequestStatus} something that tell user/develop to know which error had occurred
	 */
	public RequestStatus getRequestCode() {
		return status;
	}
	
	/**
	 * get {@link RequestStatus#toString()}
	 *
	 * @return description
	 */
	public String getDescription() {
		return status.toString();
	}
	
	/**
	 * is same status with obj?
	 *
	 * @param obj
	 * 		requestException
	 * @return true if same status, otherwise return false
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof RequestException && ((RequestException) obj).status == status;
	}
	
	public void printStackTrace() {
		System.err.println(status.getFullDescription(username, repoName));
		e.printStackTrace();
	}
}
