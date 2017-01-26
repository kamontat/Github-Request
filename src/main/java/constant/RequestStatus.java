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
	REPO_NOT_FOUND("Repositories not found."),
	USER_ERROR("Cannot get information from User"),
	
	INTERNET_ERROR("4xx/5xx http-status-code"),
	LIMIT_EXCEED("Rate limit exceed."),
	USER_NOT_FOUND("Username not found."),
	GITHUB_ERROR("Cannot connect git"),
	ERROR("An Exception has occurred!");
	
	public String description;
	
	private RequestStatus(String description) {
		this.description = description;
	}
	
	public boolean isError() {
		return this != COMPLETE && this != EMPTY;
	}
	
	public boolean haveUser() {
		return !isError() && this != USER_NOT_FOUND;
	}
	
	public boolean haveRepo() {
		return haveUser() && this != REPO_NOT_FOUND;
	}
	
	@Override
	public String toString() {
		return description;
	}
}
