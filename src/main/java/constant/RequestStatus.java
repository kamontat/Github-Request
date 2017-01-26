package constant;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 2:11 PM
 */
public enum RequestStatus {
	COMPLETE("Download complete!"),
	EMPTY("Empty issue."),
	
	FILE_ERROR("Cannot create csv file!"),
	REPO_NOT_FOUND("Repository not found."),
	
	INTERNET_ERROR("4xx/5xx http-status-code"),
	RATE_EXCEED("Rate limit exceed."),
	USER_NOT_FOUND("Username not found."), ERROR("An Exception has occurred!");
	
	public String description;
	
	private RequestStatus(String description) {
		this.description = description;
	}
	
	public boolean isError() {
		return this != COMPLETE && this != EMPTY;
	}
	
	public boolean haveUser() {
		return !isError() && this != INTERNET_ERROR && this != RATE_EXCEED && this != USER_NOT_FOUND && this != ERROR;
	}
	
	public boolean haveRepo() {
		return !isError() && haveUser() && this != REPO_NOT_FOUND;
	}
}
