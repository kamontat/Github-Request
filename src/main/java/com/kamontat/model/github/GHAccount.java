package com.kamontat.model.github;

import com.kamontat.constant.RequestStatus;
import com.kamontat.exception.RequestException;
import com.kamontat.server.GithubLoader;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * @author kamontat
 * @version 5.2
 * @since 1/26/2017 AD - 2:59 PM
 */
public class GHAccount {
	public User user;
	public Repositories repositories;
	public boolean error = false;
	
	/**
	 * before call this method you need to <b>set</b> <code>GithubLoader</code>
	 *
	 * @param username
	 * 		name of user
	 */
	public GHAccount(String username) {
		try {
			GitHub gh = GithubLoader.getGithubLoader().getGithub();
			user = GithubLoader.getGithubLoader().getUser(username);
			repositories = new Repositories(user);
		} catch (RequestException e) {
			error = true;
			e.printStackTrace();
		}
	}
	
	public StringBuilder getIssueCSV(String repoName, GHIssueState state) {
		if (isError()) return new StringBuilder();
		try {
			StringBuilder sb = repositories.getIssuesCSV(repoName, state);
			System.out.println(RequestStatus.COMPLETE.getFullDescription(user, repoName));
			return sb;
		} catch (RequestException e) {
			error = true;
			if (e.getRequestCode() == RequestStatus.REPO_NOT_FOUND) {
				System.out.println(repoList());
			}
			e.printStackTrace();
		}
		return new StringBuilder();
	}
	
	private String repoList() {
		String output = "(";
		for (GHRepository re : repositories.getAll()) {
			output += re.getName() + ", ";
		}
		output = output.substring(0, output.length() - 2);
		return output + ")";
	}
	
	public boolean isError() {
		return error;
	}
}
