package model;

import constant.RequestStatus;
import exception.RequestException;
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
	
	public RequestStatus requestCode = RequestStatus.COMPLETE;
	
	public GHAccount(String username) {
		try {
			GitHub gh = getGithub();
			user = new User(GithubLoader.getUser(username));
			repositories = new Repositories(user);
		} catch (RequestException e) {
			requestCode = e.getRequestCode();
		}
	}
	
	public Repositories.Repository getRepository(String repoName) {
		if (isError()) return null;
		try {
			return repositories.getRepository(repoName);
		} catch (RequestException e) {
			requestCode = e.getRequestCode();
		}
		return null;
	}
	
	public synchronized String repoList() {
		String output = "(";
		for (GHRepository re : repositories.getAll()) {
			output += re.getName() + ", ";
		}
		output = output.substring(0, output.length() - 2);
		return output + ")";
	}
	
	public boolean isError() {
		return requestCode != RequestStatus.COMPLETE;
	}
}
