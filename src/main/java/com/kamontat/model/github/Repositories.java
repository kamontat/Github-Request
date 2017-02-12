package com.kamontat.model.github;

import com.kamontat.exception.RequestException;
import com.kamontat.server.GithubLoader;
import org.kohsuke.github.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.kamontat.constant.RequestStatus.*;

/**
 * @author kamontat
 * @version 3.2
 * @since 1/26/2017 AD - 3:27 PM
 */
public class Repositories {
	private User owner;
	private Map<String, GHRepository> repositories;
	
	public Repositories(User user) {
		this.owner = user;
	}
	
	public void addRepository(String repoName) {
		try {
			GHRepository ghRepo = owner.getRepository(repoName);
			repositories.put(repoName, ghRepo);
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}
	
	public void addAllRepositories() throws RequestException {
		if (GithubLoader.getGithubLoader().isOutLimit()) throw new RequestException(LIMIT_EXCEED);
		
		Thread run = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					repositories = GithubLoader.getGithubLoader().getRepositories(owner);
				} catch (RequestException e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			run.start();
			run.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Repository getRepository(String name) {
		if (repositories.get(name) == null) addRepository(name);
		
		GHRepository repo = repositories.get(name);
		try {
			if (repo != null) return new Repository(owner, repo);
		} catch (RequestException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Repository> getAllRepositories() throws RequestException {
		Collection<GHRepository> collection = repositories.values();
		ArrayList<Repository> repositories = new ArrayList<Repository>(collection.size());
		for (GHRepository repo : collection) {
			repositories.add(new Repository(owner, repo));
		}
		return repositories;
	}
	
	/**
	 * getRepository all issue in repositories <code>name</code> and state <code>issueState</code>
	 *
	 * @param repoName
	 * 		name of repositories
	 * @param issueState
	 * 		state of issue (OPEN, CLOSE, ALL)
	 * @return String in csv format
	 * @throws RequestException
	 * 		if cannot getRepository issue
	 */
	public StringBuilder getIssuesCSV(String repoName, GHIssueState issueState) throws RequestException {
		StringBuilder sb = new StringBuilder();
		sb.append("repo_name, username, Id, Title, Creator, Assignee, Label, Milestone, State, Body Text\n");
		Repository repository;
		try {
			repository = getRepository(repoName);
			
			List<GHIssue> issueList = getIssue(repository, issueState);
			
			for (GHIssue issue : issueList) {
				// repo name
				sb.append(repoName).append(", ");
				
				// full name
				sb.append(owner.getName()).append(", ");
				
				// id
				sb.append(String.valueOf(issue.getNumber())).append(", ");
				
				// title
				sb.append(issue.getTitle()).append(", ");
				
				// creator
				sb.append(issue.getUser().getLogin()).append(", ");
				
				// assignee
				if (issue.getAssignee() != null) sb.append(issue.getAssignee().getName());
				else sb.append("-");
				sb.append(", ");
				
				// label
				if (!issue.getLabels().isEmpty()) {
					for (GHLabel ghLabel : issue.getLabels()) {
						sb.append(String.format("(%s)", ghLabel.getName()));
					}
				} else sb.append("-");
				sb.append(", ");
				
				// milestone
				if (issue.getMilestone() != null) sb.append(issue.getMilestone().getTitle());
				else sb.append("-");
				sb.append(", ");
				
				// state
				sb.append(String.valueOf(issue.getState())).append(", ");
				
				// body
				if (!issue.getBody().equals(""))
					sb.append(issue.getBody().trim().replaceAll(",", " ").replaceAll("\\r|\\n", " "));
				else sb.append("-");
				sb.append("\n");
			}
			return sb;
		} catch (IOException e) {
			throw new RequestException(ISSUE_ERROR, owner.getName(), repoName);
		}
	}
	
	private List<GHIssue> getIssue(Repository repository, GHIssueState issueState) throws RequestException {
		if (GithubLoader.getGithubLoader().isOutLimit()) throw new RequestException(LIMIT_EXCEED);
		
		// empty issue
		if (repository.getIssues(issueState).isEmpty())
			System.err.println(EMPTY.getFullDescription(repository.owner, repository));
		
		return repository.getIssues(issueState);
	}
	
	@Override
	public String toString() {
		String output = "";
		for (GHRepository repository : repositories.values()) {
			try {
				output += String.format("%s\n", new Repository(owner, repository));
			} catch (RequestException e) {
				e.printStackTrace();
			}
		}
		return output;
	}
	
	public class Repository {
		private GHRepository repository;
		
		public int id;
		public String name;
		public String description;
		public User owner;
		public URL api_url;
		public String url;
		public Date createAt;
		public Date updateAt;
		
		GHContent readme;
		public String readme_link;
		public String readme_download_link;
		public String readme_api_url;
		
		/**
		 * get all information from repository
		 *
		 * @param owner
		 * 		repository owner
		 * @param repository
		 * 		some repo
		 * @throws RequestException
		 * 		occurred {@link com.kamontat.constant.RequestStatus#INTERNET_ERROR} when internet error
		 */
		Repository(User owner, GHRepository repository) throws RequestException {
			this.repository = repository;
			
			id = repository.getId();
			name = repository.getName();
			description = repository.getDescription();
			api_url = repository.getUrl();
			url = repository.getSvnUrl();
			this.owner = owner;
			
			try {
				readme = repository.getReadme();
				
				readme_api_url = readme.getUrl();
				readme_download_link = readme.getDownloadUrl();
				readme_link = readme.getHtmlUrl();
			} catch (IOException e) {
				System.err.println(README_NOT_FOUND.getFullDescription(owner, name));
			}
			
			try {
				createAt = repository.getCreatedAt();
				updateAt = repository.getUpdatedAt();
			} catch (IOException e) {
				throw new RequestException(INTERNET_ERROR);
			}
		}
		
		/**
		 * get list of issue that match of parameter
		 *
		 * @param issueState
		 * 		status of issue {@link GHIssueState}
		 * @return list of issue
		 * @throws RequestException
		 * 		occurred {@link com.kamontat.constant.RequestStatus#ISSUE_ERROR} when have some error in issue getting
		 */
		private List<GHIssue> getIssues(GHIssueState issueState) throws RequestException {
			try {
				return repository.getIssues(issueState);
			} catch (IOException e) {
				throw new RequestException(ISSUE_ERROR, owner.getName(), name);
			}
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
}
