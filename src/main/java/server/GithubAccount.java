package server;

import model.User;

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
	
}
