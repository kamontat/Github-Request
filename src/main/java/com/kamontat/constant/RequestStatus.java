package com.kamontat.constant;

import com.kamontat.model.github.Repositories;
import com.kamontat.model.github.User;

/**
 * when this program need to request some thing in the internet <br>
 * this might return some exception or code that program can't handle by itself so this class will have develop and user to know that which exception be happen and what best result that we should handle <br>
 * <b>mostly</b> this enum class will use with {@link com.kamontat.exception.RequestException} to handle exception easier.
 *
 * @author kamontat
 * @version 5
 * @see com.kamontat.exception.RequestException
 * @since 1/26/2017 AD - 2:11 PM
 */
public enum RequestStatus {
	COMPLETE("Download complete!"), EMPTY("Empty issue."), README_NOT_FOUND("Readme not found."),
	
	USER_NOT_FOUND("Username not found."), REPO_NOT_FOUND("Repositories not found."),
	
	FILE_ERROR("Cannot create csv file!"), USER_GET_ERROR("Cannot get information from User"), USER_EMAIL_ERROR("Cannot access to email by your token"), REPO_ERROR("Cannot get Repository"), ISSUE_ERROR("Cannot get issue information"), MYSELF_ERROR("Cannot get information from Myself"), INTERNET_ERROR("4xx/5xx http-status-code"), TOKEN_ERROR("Invalid token"), GITHUB_ERROR("Cannot connect git"),
	
	LIMIT_EXCEED("Rate limit exceed."), ERROR("An Exception has occurred!");
	
	private String description;
	
	RequestStatus(String description) {
		this.description = description;
	}
	
	/**
	 * change every parameter to string and call {@link #getFullDescription(String, String)}
	 *
	 * @param user
	 * 		user, this method will get only user name
	 * @param repository
	 * 		this method wll get only repository name
	 * @return full description
	 */
	public String getFullDescription(User user, Repositories.Repository repository) {
		if (repository == null) return getFullDescription(user, "-");
		return String.format("%s (%s) %s", user.getName(), repository.name, description);
	}
	
	/**
	 * change every parameter to string and call {@link #getFullDescription(String, String)}
	 *
	 * @param user
	 * 		user, this method will get only user name
	 * @param repoName
	 * 		repository name
	 * @return full description
	 * @see #getFullDescription(String, String)
	 */
	public String getFullDescription(User user, String repoName) {
		if (user == null) return getFullDescription("-", repoName);
		return getFullDescription(user.getName(), repoName);
	}
	
	/**
	 * get full description (include user and repository) that occurred exception or something don't have if error occurred faster that get user and repository
	 *
	 * @param username
	 * 		name of user
	 * @param repoName
	 * 		name of repository
	 * @return string in one of this format <br>
	 * <ol>
	 * <li><code>username (repo_name) exception_description</code></li>
	 * <li><code>username exception_description</code></li>
	 * <li><code>exception_description</code></li>
	 * </ol>
	 */
	public String getFullDescription(String username, String repoName) {
		if (username == null) {
			return String.format("%s", description);
		} else if (repoName == null) {
			return String.format("%s %s", username, description);
		}
		return String.format("%s (%s) %s", username, repoName, description);
	}
	
	@Override
	public String toString() {
		return description;
	}
}
