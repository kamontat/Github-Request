package server;

import constant.RequestStatus;
import exception.ConvertException;
import model.User;
import org.kohsuke.github.GitHub;

import java.io.IOException;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 2:59 PM
 */
public class GHAccount {
	// you can get token in this link `https://github.com/settings/tokens`
	private static final String TOKEN = "925cc49f2798daae39b0e594896fdea9388e528f"; // if have token rate_limit will be `5000`, otherwise rate_limit will be `60`
	
	private GitHub gh;
	public User user;
	
	public RequestStatus requestCode;
	
	public GHAccount(String user) {
		try {
			gh = getGithub();
			this.user = getUser(user);
		} catch (ConvertException e) {
			requestCode = e.getRequestCode();
		}
	}
	
	public GHAccount(User user) {
		try {
			gh = getGithub();
			this.user = user;
		} catch (ConvertException e) {
			requestCode = e.getRequestCode();
		}
	}
	
	private static GitHub getGithub() throws ConvertException {
		try {
			if (TOKEN == null || TOKEN.equals("")) {
				return GitHub.connectAnonymously();
			} else {
				return GitHub.connectUsingOAuth(TOKEN);
			}
		} catch (IOException e) {
			throw new ConvertException(RequestStatus.GITHUB_ERROR);
		}
	}
	
	public static User getUser(String username) throws ConvertException {
		try {
			return new User(getGithub().getUser(username));
		} catch (IOException e) {
			throw new ConvertException(RequestStatus.USER_NOT_FOUND);
		}
	}
}
