package exception;

import constant.RequestStatus;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 3:49 PM
 */
public class ConvertException extends Exception {
	private RequestStatus status;
	
	public ConvertException(Exception e) {
		this(Error.get(e).getRequestCode());
	}
	
	public ConvertException(RequestStatus status) {
		super(status.description);
		this.status = status;
	}
	
	public RequestStatus getRequestCode() {
		return status;
	}
	
	public String getRequestString() {
		return status.description;
	}
}
