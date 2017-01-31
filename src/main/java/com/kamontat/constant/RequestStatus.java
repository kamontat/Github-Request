package com.kamontat.constant;

import com.kamontat.model.Repositories;
import com.kamontat.model.User;

/**
 * @author kamontat
 * @version 5
 * @since 1/26/2017 AD - 2:11 PM
 */
public enum RequestStatus {
	COMPLETE("Download complete!"),
	EMPTY("Empty issue."),
	README_NOT_FOUND("Readme not found."),
	
	USER_NOT_FOUND("Username not found."),
	REPO_NOT_FOUND("Repositories not found."),
	
	FILE_ERROR("Cannot create csv file!"),
	USER_GET_ERROR("Cannot get information from User"),
	USER_EMAIL_ERROR("Cannot access to email by your token"),
	REPO_ERROR("Cannot get Repository"),
	ISSUE_ERROR("Cannot get issue information"),
	MYSELF_ERROR("Cannot get information from Myself"),
	INTERNET_ERROR("4xx/5xx http-status-code"),
	GITHUB_ERROR("Cannot connect git"),
	
	LIMIT_EXCEED("Rate limit exceed."),
	ERROR("An Exception has occurred!");
	
	private String description;
	
	RequestStatus(String description) {
		this.description = description;
	}
	
	public String getFullDescription(User user, Repositories.Repository repository) {
		if (repository == null) return getFullDescription(user, "-");
		return String.format("%s (%s) %s", user.fullname, repository.name, description);
	}
	
	public String getFullDescription(User user, String repoName) {
		if (user == null) return getFullDescription("-", repoName);
		return getFullDescription(user.fullname, repoName);
	}
	
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
