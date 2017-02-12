package com.kamontat.model.github;

import com.kamontat.exception.RequestException;

import java.util.*;

/**
 * @author kamontat
 * @version 5.2
 * @since 1/26/2017 AD - 2:59 PM
 */
public class GHAccount implements TableInformation<GHAccount> {
	public User user;
	private Repositories repositories;
	
	public GHAccount(User user) {
		this.user = user;
		repositories = new Repositories(user);
	}
	
	public Repositories.Repository getRepository(String repoName) throws RequestException {
		return repositories.getRepository(repoName);
	}
	
	@Override
	public GHAccount getRawData() {
		return this;
	}
	
	@Override
	public String getName() {
		return user.getName();
	}
	
	@Override
	public Vector<Object> getStringInformationVector() {
		Vector<Object> vector = this.user.getStringInformationVector();
		// TODO 2/12/2017 AD 12:31 PM add repository name
		return vector;
	}
	
	@Override
	public Vector<String> getStringTitleVector() {
		Vector<String> vector = user.getStringTitleVector();
		vector.add("Repository name");
		
		return vector;
	}
}
