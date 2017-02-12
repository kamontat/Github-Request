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
	
	public static ArrayList<GHAccount> convertAll(ArrayList<User> users) {
		ArrayList<GHAccount> accounts = new ArrayList<GHAccount>();
		for (User u : users) {
			accounts.add(new GHAccount(u));
		}
		return accounts;
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
		try {
			// convert array of repo to text separate by ","
			StringBuilder repoText = new StringBuilder();
			ArrayList<Repositories.Repository> repositories = this.repositories.getAllRepositories();
			for (Repositories.Repository repo : repositories) {
				repoText.append(repo.name).append(", ");
			}
			repoText.delete(repoText.capacity() - 2, repoText.capacity());
			
			vector.add(repoText);
		} catch (RequestException e) {
			e.printStackTrace();
		}
		return vector;
	}
	
	@Override
	public Vector<String> getStringTitleVector() {
		Vector<String> vector = user.getStringTitleVector();
		vector.add("Repository name (eg. a, b, c)");
		
		return vector;
	}
}
