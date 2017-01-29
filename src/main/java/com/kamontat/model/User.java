package model;

import constant.RequestStatus;
import exception.RequestException;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author kamontat
 * @version 2.4
 * @since 1/25/2017 AD - 10:42 PM
 */
public class User {
	public GHUser githubUser;
	
	public int id;
	public String loginName;
	public String fullname;
	public String name;
	public String surname;
	public String company;
	public String email;
	public String location;
	public URL url;
	public Date createAt;
	public Date updateAt;
	
	public User(GHUser user) throws RequestException {
		this.githubUser = user;
		
		try {
			id = githubUser.getId();
			loginName = githubUser.getLogin();
			
			fullname = githubUser.getName();
			if (fullname != null) {
				String[] n = fullname.split(" ");
				name = n[0];
				if (n.length == 2) surname = n[1];
			} else fullname = loginName;
			
			company = githubUser.getCompany();
			email = githubUser.getEmail();
			location = githubUser.getLocation();
			url = githubUser.getHtmlUrl();
			createAt = githubUser.getCreatedAt();
			updateAt = githubUser.getUpdatedAt();
		} catch (IOException e) {
			throw new RequestException(RequestStatus.USER_ERROR);
		}
	}
	
	/**
	 * should have both name&surname
	 *
	 * @return true if full name
	 */
	public boolean isFullName() {
		return name != null && surname != null;
	}
	
	@Override
	public String toString() {
		return String.format("id=%s loginName=%s, Name: %s, email: %s\ncompany=%s \'%s\'\nlink: %s\ncreate=\'%s\' update=\'%s\'", id, loginName, fullname, email, company, location, url, createAt, updateAt);
	}
}
