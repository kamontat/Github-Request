package com.kamontat.server;

import com.kamontat.constant.RequestStatus;
import com.kamontat.exception.NonException;
import com.kamontat.exception.RequestException;
import com.kamontat.model.github.User;
import org.kohsuke.github.*;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static com.kamontat.constant.RequestStatus.REPO_NOT_FOUND;

/**
 * @author kamontat
 * @version 2.2
 * @since 1/26/2017 AD - 6:11 PM
 */
public class GithubLoader {
	public enum Type {
		ANONYMOUS, AUTH;
	}
	
	private Type type;
	private GithubToken token;
	
	private static GithubLoader githubLoader;
	
	public static GithubLoader setAuth(GithubToken token) {
		if (githubLoader == null) githubLoader = new GithubLoader();
		githubLoader.type = Type.AUTH;
		githubLoader.token = token;
		return githubLoader;
	}
	
	public static GithubLoader setAnonymous() {
		if (githubLoader == null) githubLoader = new GithubLoader();
		githubLoader.type = Type.ANONYMOUS;
		return githubLoader;
	}
	
	public static void wait(Component c) {
		c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	public static void done(Component c) {
		c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	/**
	 * getLog github loader by type that you set but if don't call method <code>set</code> before, github loader will load as anonymous
	 *
	 * @return github loader
	 */
	public static GithubLoader getGithubLoader() {
		if (githubLoader == null) setAnonymous();
		return githubLoader;
	}
	
	public GitHub getGithub() throws RequestException {
		try {
			if (type == Type.AUTH) return GitHub.connectUsingOAuth(token.getToken());
			else return GitHub.connectAnonymously();
		} catch (IOException e) {
			throw new RequestException(e, RequestStatus.TOKEN_ERROR);
		}
	}
	
	public User getMyself() throws RequestException {
		if (type == Type.AUTH) {
			try {
				return new User(getGithub().getMyself());
			} catch (IOException e) {
				throw new RequestException(e, RequestStatus.MYSELF_ERROR);
			}
		}
		return null;
	}
	
	public GHRateLimit getRateLimit() throws RequestException {
		GHRateLimit rateLimit;
		try {
			rateLimit = getGithub().rateLimit();
		} catch (IOException e) {
			throw new RequestException(e, RequestStatus.INTERNET_ERROR);
		}
		return rateLimit;
	}
	
	public boolean isOutLimit() throws RequestException {
		return GithubLoader.getGithubLoader().getRateLimit().remaining == 0;
	}
	
	public User[] getOrganization(String name) throws RequestException {
		try {
			PagedIterable<GHUser> allUser = getGithub().getOrganization(name).listMembers();
			
			List<GHUser> ghusers = allUser.asList();
			User[] users = new User[ghusers.size()];
			
			int i = 0;
			for (GHUser ghu : ghusers) {
				users[i++] = new User(ghu);
			}
			
			return users;
		} catch (IOException e) {
			throw new RequestException(e, RequestStatus.ERROR);
		}
	}
	
	public User getUser(String name) throws RequestException {
		try {
			return new User(getGithub().getUser(name));
		} catch (IOException e) {
			throw new RequestException(e, RequestStatus.USER_NOT_FOUND);
		}
	}
	
	public Map<String, GHRepository> getRepositories(User user) throws RequestException {
		if (user.isMine()) {
			try {
				return user.githubMy.getAllRepositories();
			} catch (IOException e) {
				throw new RequestException(e, user.getName());
			}
		} else {
			try {
				return user.githubUser.getRepositories();
			} catch (IOException e) {
				throw new RequestException(e, user.getName());
			}
		}
	}
	
	public GHRepository getRepository(User user, String repoName) throws RequestException {
		try {
			GHRepository repository = null;
			
			if (user.isMine()) repository = user.githubMy.getRepository(repoName);
			else repository = user.githubUser.getRepository(repoName);
			
			if (repository == null)
				throw new RequestException(NonException.get(), REPO_NOT_FOUND, user.getName(), repoName);
			
			return repository;
		} catch (IOException e) {
			throw new RequestException(e, user.getName(), repoName);
		}
	}
	
	public int getMaximumRateLimit() {
		if (type == Type.ANONYMOUS) return 60;
		else if (type == Type.AUTH) return 5000;
		else return 0;
	}
	
	public Type getType() {
		return type;
	}
}
