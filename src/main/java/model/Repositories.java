package model;

import exception.RequestException;
import org.kohsuke.github.*;
import server.GithubLoader;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static constant.RequestStatus.*;
import static server.GithubLoader.getRateLimit;

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
		
		Thread run = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					repositories = GithubLoader.getRepositories(owner);
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
				sb.append(owner.fullname).append(", ");
				
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
		} catch (Exception e) {
			throw new RequestException(e, owner.fullname, repoName);
		}
	}
	
	public Repository getRepository(String repoName) throws RequestException {
		GHRepository repository = repositories.get(repoName);
		if (repository == null) throw new RequestException(REPO_NOT_FOUND, owner.fullname, repoName);
		return new Repository(owner, repository);
	}
	
	public List<GHRepository> getAll() {
		return new ArrayList<>(repositories.values());
	}
	
	private List<GHIssue> getIssue(Repository repository, GHIssueState issueState) throws RequestException {
		if (isOutLimit()) throw new RequestException(LIMIT_EXCEED);
		
		if (repository.getIssues(issueState).isEmpty())
			System.err.println(EMPTY.getFullDescription(repository.owner, repository));
		
		return repository.getIssues(issueState);
	}
	
	private boolean isOutLimit() throws RequestException {
		return getRateLimit().remaining == 0;
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
		
		public String readme_link;
		public String readme_download_link;
		public String readme_api_url;
		
		Repository(User owner, GHRepository repository) throws RequestException {
			this.repository = repository;
			
			id = repository.getId();
			name = repository.getName();
			description = repository.getDescription();
			api_url = repository.getUrl();
			url = repository.getSvnUrl();
			this.owner = owner;
			
			try {
				GHContent ghc = repository.getReadme();
				readme_api_url = ghc.getUrl();
				readme_download_link = ghc.getDownloadUrl();
				readme_link = ghc.getHtmlUrl();
			} catch (IOException e) {
				System.err.println(README_NOT_FOUND.getFullDescription(owner, name));
			}
			
			try {
				createAt = repository.getCreatedAt();
				updateAt = repository.getUpdatedAt();
			} catch (IOException e) {
				throw new RequestException(e);
			}
		}
		
		private List<GHIssue> getIssues(GHIssueState issueState) throws RequestException {
			try {
				return repository.getIssues(issueState);
			} catch (IOException e) {
				throw new RequestException(e, owner.fullname, name);
			}
		}
		
		@Override
		public String toString() {
			return String.format("id=%s name=%s \n\"%s\"\nurl=%s\napi_url=%s\nReadme:\n\tlink=%s\n\tdownload link=%s\n\tapi link=%s\ncreate=\'%s\' update=\'%s\'", id, name, description, url, api_url, readme_link == null ? "none": readme_link, readme_download_link == null ? "none": readme_download_link, readme_api_url == null ? "none": readme_api_url, createAt, updateAt);
		}
	}
}
