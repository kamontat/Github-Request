package model;

import constant.RequestStatus;
import exception.RequestException;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import server.GithubLoader;

import static server.GithubLoader.getGithub;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 2:59 PM
 */
public class GHAccount {
	public User user;
	public Repositories repositories;
	public boolean error = false;
	
	public GHAccount(String username) {
		try {
			GitHub gh = getGithub();
			user = new User(GithubLoader.getUser(username));
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
