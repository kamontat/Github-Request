package com.kamontat.model;

import com.kamontat.constant.RequestStatus;
import com.kamontat.exception.RequestException;
import org.kohsuke.github.GHEmail;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author kamontat
 * @version 2.5
 * @since 1/25/2017 AD - 10:42 PM
 */
public class User implements TableInformation<User> {
	public GHUser githubUser;
	private GHMyself githubMy;
	
	public final int id;
	public final String loginName;
	public String fullname;
	public String name; // can be null
	public String surname; // can be null
	public String email;
	public final String company;
	public final String location;
	public final URL api_url;
	public final URL url;
	public final String image_url;
	public final Date createAt;
	public final Date updateAt;
	
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
			
			email = githubUser.getEmail();
			company = githubUser.getCompany();
			location = githubUser.getLocation();
			url = githubUser.getHtmlUrl();
			api_url = githubUser.getUrl();
			image_url = githubUser.getAvatarUrl();
			createAt = githubUser.getCreatedAt();
			updateAt = githubUser.getUpdatedAt();
		} catch (IOException e) {
			throw new RequestException(RequestStatus.USER_ERROR);
		}
	}
	
	public User(GHMyself my) throws RequestException {
		this((GHUser) my);
		this.githubMy = my;
		
		try {
			for (GHEmail ghm : my.getEmails2()) {
				if (ghm.isPrimary()) email = ghm.getEmail();
			}
			if (email == null) email = my.getEmails2().get(0).getEmail();
		} catch (Exception e) {
			new RequestException(RequestStatus.USER_EMAIL_NOT_FOUND, fullname).printStackTrace();
			email = null;
		}
	}
	
	public GHMyself getGithubMyself() {
		return githubMy;
	}
	
	/**
	 * should have both name and surname
	 *
	 * @return true if full name
	 */
	public boolean isFullName() {
		return name != null && surname != null;
	}
	
	/**
	 * check this user is a current sign in user or not
	 *
	 * @return true if yes, otherwise return false
	 */
	public boolean isMine() {
		return githubMy != null;
	}
	
	@Override
	public final String toString() {
		return String.format("id=%s loginName=%s, Name: %s, email: %s\ncompany=%s \'%s\'\nlink: %s api: %s\ncreate=\'%s\' update=\'%s\'", id, loginName, fullname, email, company, location, url, api_url, createAt, updateAt);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof User && ((User) obj).id == id && ((User) obj).loginName.equals(loginName);
	}
	
	@Override
	public User getRawData() {
		return this;
	}
	
	@Override
	public Vector<Object> getStringInformationVector() {
		Vector<Object> vector = new Vector<>(4, 1);
		vector.add(id);
		vector.add(loginName);
		vector.add(fullname);
		vector.add(email);
		vector.add(url);
		return vector;
	}
	
	@Override
	public Vector<String> getStringTitleVector() {
		return getStringTitleVectorStatic();
	}
	
	public static Vector<String> getStringTitleVectorStatic() {
		Vector<String> vector = new Vector<>(4, 1);
		vector.add("ID");
		vector.add("Login Name");
		vector.add("Full Name");
		vector.add("E-mail");
		vector.add("Github Link");
		
		return vector;
	}
}
