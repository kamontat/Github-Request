package model;

import constant.RequestStatus;
import exception.RequestException;
import org.kohsuke.github.*;
import server.GithubLoader;

import java.io.IOException;
import java.util.*;

import static constant.RequestStatus.EMPTY;
import static constant.RequestStatus.LIMIT_EXCEED;
import static server.GithubLoader.getGithub;
import static server.GithubLoader.getRateLimit;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 3:27 PM
 */
public class Repositorys {
	private User owner;
	private Map<String, GHRepository> repositories;
	
	public Repositorys(User user) {
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
	
	public GHRepository getRepository(String name) throws RequestException {
		GHRepository repo = repositories.get(name);
		if (repo == null) throw new RequestException(RequestStatus.REPO_NOT_FOUND);
		return repo;
	}
	
	public List<GHIssue> getIssue(String repoName, GHIssueState issueState) throws RequestException {
		GitHub github = getGithub();
		if (isOutLimit()) throw new RequestException(LIMIT_EXCEED);
		
		GHRepository repository = getRepository(repoName);
		try {
			if (repository.getIssues(issueState).isEmpty()) throw new RequestException(EMPTY);
			return repository.getIssues(issueState);
		} catch (IOException e) {
			throw new RequestException(e);
		}
	}
	
	private boolean isOutLimit() throws RequestException {
		return getRateLimit().remaining == 0;
	}
	
	@Override
	public String toString() {
		String output = "";
		Iterator<GHRepository> it = repositories.values().iterator();
		while (it.hasNext()) {
			GHRepository repository = it.next();
			output += String.format("");
		}
		return output;
	}
}
