package server;

import constant.RequestStatus;
import model.User;
import org.kohsuke.github.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import static constant.RequestStatus.*;
import static server.GHAccount.getGithub;
import static server.GHAccount.getUser;

/**
 * some of code from <a href="http://stackoverflow.com/questions/21827958/downloading-github-issues-to-csv-file">link</a> <br>
 * use framework <a href="http://github-api.kohsuke.org">Github by kohsuke</a> you can see in <a href="http://github-api.kohsuke.org/apidocs/index.html">java doc</a>
 *
 * @author kamontat
 * @version 1.0
 * @since 1/25/2017 AD - 8:09 PM
 */
public class GithubAccount {
	
	private static String repo_name;
	private String username;
	
	private String fileNameFormat = "%s_%s.csv";
	
	private static final String path = "src/main/resources/issues/";
	
	private static GithubAccount github_account;
	
	private static User user;
	private static int issueCount = 0;
	
	public static GithubAccount get(String username) {
		if (github_account == null) github_account = new GithubAccount();
		github_account.username = username;
		return github_account;
	}
	
	public static String getUsername() {
		return github_account.username;
	}
	
	public static String getRepo_name() {
		return repo_name;
	}
	
	public static void setRepositoryName(String repo_name) {
		GithubAccount.repo_name = repo_name;
	}
	
	public RequestStatus saveIssues(GHIssueState issueState) {
		try {
			GitHub github = getGithub();
			
			if (isOutLimit()) return RATE_EXCEED;
			
			// get user
			user = getUser(github_account.username);
			
			// get repository
			GHRepository repository = user.githubUser.getRepository(repo_name);
			if (repository == null) return REPO_NOT_FOUND;
			if (repository.getIssues(issueState).isEmpty()) return EMPTY;
			issueCount = repository.getIssues(issueState).size();
			
			// start loading...
			File file = new File(path + String.format(fileNameFormat, user.name, user.surname));
			
			if (file.exists() || file.createNewFile()) {
				FileWriter writer = new FileWriter(file);
				writer.append("repo_name, username, Id, Title, Creator, Assignee, Label, Milestone, State, Body Text");
				writer.append("\n");
				
				for (GHIssue issue : repository.getIssues(issueState)) {
					// repo name
					writer.append(repo_name).append(", ");
					
					// username
					writer.append(username).append(", ");
					
					// id
					writer.append(String.valueOf(issue.getNumber())).append(", ");
					
					// title
					writer.append(issue.getTitle()).append(", ");
					
					// creator
					writer.append(issue.getUser().getLogin()).append(", ");
					
					// assignee
					if (issue.getAssignee() != null) writer.append(issue.getAssignee().getName());
					else writer.append("-");
					writer.append(", ");
					
					// label
					if (!issue.getLabels().isEmpty()) {
						for (GHLabel ghLabel : issue.getLabels()) {
							writer.append(String.format("(%s)", ghLabel.getName()));
						}
					} else writer.append("-");
					writer.append(", ");
					
					// milestone
					if (issue.getMilestone() != null) writer.append(issue.getMilestone().getTitle());
					else writer.append("-");
					writer.append(", ");
					
					// state
					writer.append(String.valueOf(issue.getState())).append(", ");
					
					// body
					if (!issue.getBody().equals(""))
						writer.append(issue.getBody().trim().replaceAll(",", " ").replaceAll("\\r|\\n", " "));
					else writer.append("-");
					writer.append("\n");
				}
				writer.flush();
				writer.close();
			} else {
				return FILE_ERROR;
			}
			
			return COMPLETE;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			if (e.getMessage().contains("Not Found")) {
				return USER_NOT_FOUND;
			}
		} catch (HttpException e) {
			e.printStackTrace();
			if (e.getResponseCode() == -1) {
				return INTERNET_LOST;
			}
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}
	
	private static GHRateLimit getRateLimit() {
		GHRateLimit rateLimit = null;
		try {
			rateLimit = getGithub().rateLimit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rateLimit;
	}
	
	private static boolean isOutLimit() {
		return getRateLimit().remaining == 0;
	}
	
	public static String getStatus(RequestStatus code) {
		if (code.haveUser()) System.out.println(user.toString());
		
		// list all repo if repo not found
		try {
			if (!code.haveRepo()) {
				System.out.print("(");
				for (GHRepository repository : user.githubUser.getRepositories().values()) {
					System.out.print(repository.getName() + ", ");
				}
				System.out.println(")");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return String.format("%s (%s) %s (%d)\nLimit Information: %s", github_account.username, repo_name, code.description, issueCount, getRateLimit());
	}
}
