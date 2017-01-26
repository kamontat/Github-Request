package model;

import constant.RequestStatus;
import exception.RequestException;
import org.kohsuke.github.GitHub;
import server.GithubLoader;

import static server.GithubLoader.getGithub;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 2:59 PM
 */
public class GHAccount {
	private GitHub gh;
	
	public User user;
	public Repositories repository;
	
	public RequestStatus requestCode;
	
	public GHAccount(String username) {
		try {
			gh = getGithub();
			user = new User(GithubLoader.getUser(username));
			repository = new Repositories(user);
		} catch (RequestException e) {
			requestCode = e.getRequestCode();
		}
	}
}
