package main;

import file.File;
import org.kohsuke.github.GHIssueState;
import server.GithubAccount;

import java.io.IOException;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/25/2017 AD - 8:25 PM
 */
public class Main {
	public static void main(String[] args) throws IOException {
		GithubAccount.setRepositoryName("GuessingGame");
		for (String name : File.getGithubName()) {
			int status = GithubAccount.get(name).saveIssues(GHIssueState.OPEN);
			if (status < 0) System.err.println(GithubAccount.getStatus(status) + "\n");
			else System.out.println(GithubAccount.getStatus(status) + "\n");
		}
	}
}
