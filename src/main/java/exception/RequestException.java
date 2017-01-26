package exception;

import constant.RequestStatus;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 3:49 PM
 */
public class RequestException extends Exception {
	private RequestStatus status;
	
	public RequestException(Exception e) {
		this(Error.get(e).getRequestCode());
	}
	
	public RequestException(RequestStatus status) {
		super(status.description);
		this.status = status;
	}
	
	public RequestStatus getRequestCode() {
		return status;
	}
	
	public String getRequestString() {
		return status.description;
	}
	
	@Override
	public void printStackTrace() {
		System.err.println(status);
		super.printStackTrace();
	}
}
