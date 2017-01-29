package exception;

import constant.RequestStatus;

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
	
	public RequestException(RequestStatus status, String user, String repoName) {
		super(status.toString());
		this.status = status;
		this.username = user;
		this.repoName = repoName;
	}
	
	public RequestException(Exception e, String user) {
		this(Error.get(e).getRequestCode(), user);
	}
	
	public RequestException(RequestStatus status, String user) {
		super(status.toString());
		this.status = status;
		this.username = user;
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
