package com.kamontat.model.github;

import com.kamontat.constant.RequestStatus;
import com.kamontat.exception.RequestException;
import com.kamontat.server.Downloader;
import com.kamontat.server.GithubLoader;
import org.kohsuke.github.GHEmail;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

import javax.swing.*;
import java.awt.*;
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
	public GHMyself githubMy;
	
	private final int id;
	public final String loginName;
	private String fullname;
	public String name; // can be null
	public String surname; // can be null
	private String email;
	private final String company;
	public final String location;
	public final URL api_url;
	public final URL url;
	public final URL image_url;
	private final ImageIcon image;
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
			image_url = new URL(githubUser.getAvatarUrl());
			image = Downloader.from(image_url).downloadImage();
			createAt = githubUser.getCreatedAt();
			updateAt = githubUser.getUpdatedAt();
		} catch (IOException e) {
			throw new RequestException(e, RequestStatus.USER_GET_ERROR);
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
			new RequestException(e, RequestStatus.USER_EMAIL_ERROR, fullname).printStackTrace();
			email = null;
		}
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
	
	public String getID() {
		return String.valueOf(id);
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	public String getCompany() {
		return company == null ? "": company;
	}
	
	public String getEmail() {
		return email == null ? "": email;
	}
	
	public ImageIcon getImage(int w, int h) {
		return new ImageIcon(image.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	public GHRepository getRepository(String name) throws RequestException {
		return GithubLoader.getGithubLoader().getRepository(this, name);
	}
	
	public Map<String, GHRepository> getRepositories() throws RequestException {
		return GithubLoader.getGithubLoader().getRepositories(this);
	}
	
	@Override
	public User getRawData() {
		return this;
	}
	
	@Override
	public String getName() {
		return fullname;
	}
	
	@Override
	public Vector<Object> getStringInformationVector() {
		Vector<Object> vector = new Vector<Object>(4, 1);
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
		Vector<String> vector = new Vector<String>(4, 1);
		vector.add("ID");
		vector.add("Login Name");
		vector.add("Full Name");
		vector.add("E-mail");
		vector.add("Github Link");
		
		return vector;
	}
}
