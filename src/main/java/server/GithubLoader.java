package server;

import constant.RequestStatus;
import exception.RequestException;
import model.User;
import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 6:11 PM
 */
public class GithubLoader {
	// you can get token in this link `https://github.com/settings/tokens`
	private static final String TOKEN = "925cc49f2798daae39b0e594896fdea9388e528f"; // if have token rate_limit will be `5000`, otherwise rate_limit will be `60`
	
	public static GitHub getGithub() throws RequestException {
		try {
			if (TOKEN == null || TOKEN.equals("")) {
				return GitHub.connectAnonymously();
			} else {
				return GitHub.connectUsingOAuth(TOKEN);
			}
		} catch (IOException e) {
			throw new RequestException(RequestStatus.GITHUB_ERROR);
		}
	}
	
	public static GHRateLimit getRateLimit() throws RequestException {
		GHRateLimit rateLimit = null;
		try {
			rateLimit = getGithub().rateLimit();
		} catch (IOException e) {
			throw new RequestException(RequestStatus.INTERNET_ERROR);
		}
		return rateLimit;
	}
	
	public static GHUser getUser(String name) throws RequestException {
		try {
			return getGithub().getUser(name);
		} catch (IOException e) {
			throw new RequestException(RequestStatus.USER_NOT_FOUND);
		}
	}
	
	public static Map<String, GHRepository> getRepositories(User user) throws RequestException {
		try {
			return user.githubUser.getRepositories();
		} catch (IOException e) {
			throw new RequestException(e);
		}
	}
}
