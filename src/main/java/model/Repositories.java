package model;

import constant.RequestStatus;
import exception.RequestException;
import org.kohsuke.github.*;
import server.GithubLoader;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static constant.RequestStatus.EMPTY;
import static constant.RequestStatus.INTERNET_ERROR;
import static constant.RequestStatus.LIMIT_EXCEED;
import static server.GithubLoader.getGithub;
import static server.GithubLoader.getRateLimit;

/**
 * @author kamontat
 * @version 1.0
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
					repositories = GithubLoader.getRepositories(user);
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
	 * get all issue in repository <code>name</code> and state <code>issueState</code>
	 *
	 * @param repoName
	 * 		name of repository
	 * @param issueState
	 * 		state of issue (OPEN, CLOSE, ALL)
	 * @return String in csv format
	 * @throws RequestException
	 * 		if cannot get issue
	 */
	public StringBuilder getIssuesCSV(String repoName, GHIssueState issueState) throws RequestException {
		StringBuilder sb = new StringBuilder();
		sb.append("repo_name, username, Id, Title, Creator, Assignee, Label, Milestone, State, Body Text\n");
		
		try {
			List<GHIssue> issueList = getIssue(repoName, issueState);
			
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
			throw new RequestException(e);
		}
	}
	
	public Repository getRepository(String name) throws RequestException {
		Repository repo = new Repository(repositories.get(name));
		return repo;
	}
	
	public List<GHIssue> getIssue(String repoName, GHIssueState issueState) throws RequestException {
		GitHub github = getGithub();
		if (isOutLimit()) throw new RequestException(LIMIT_EXCEED);
		
		Repository repository = getRepository(repoName);
		if (repository.getIssues(issueState).isEmpty()) throw new RequestException(EMPTY);
		
		return repository.getIssues(issueState);
	}
	
	private boolean isOutLimit() throws RequestException {
		return getRateLimit().remaining == 0;
	}
	
	@Override
	public String toString() {
		String output = "";
		for (GHRepository repository : repositories.values()) {
			output += String.format("");
		}
		return output;
	}
	
	class Repository {
		private GHRepository repository;
		
		public int id;
		public String name;
		public String description;
		public User owner;
		public URL url;
		public String readme;
		public Date createAt;
		public Date updateAt;
		
		Repository(GHRepository repository) throws RequestException {
			if (repository == null) throw new RequestException(RequestStatus.REPO_NOT_FOUND);
			
			this.repository = repository;
			
			id = repository.getId();
			name = repository.getName();
			description = repository.getDescription();
			url = repository.getUrl();
			
			try {
				owner = new User(repository.getOwner());
				GHContent ghc = repository.getReadme();
				System.out.println(ghc.getName());
				System.out.println(ghc.getOwner());
				
				System.out.println(ghc.getUrl());
				System.out.println(ghc.getGitUrl());
				System.out.println(ghc.getDownloadUrl());
				System.out.println(ghc.getHtmlUrl());
				System.out.println(ghc.getPath());
				System.out.println(ghc.getSize());
				System.out.println(ghc.getType());
				
			} catch (IOException e) {
				throw new RequestException(INTERNET_ERROR);
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
				throw new RequestException(e);
			}
		}
	}
}
