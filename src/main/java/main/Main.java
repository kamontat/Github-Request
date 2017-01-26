package main;

import constant.RequestStatus;
import exception.RequestException;
import file.File;
import model.GHAccount;
import org.kohsuke.github.GHIssueState;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/25/2017 AD - 8:25 PM
 */
public class Main {
	public static void main(String[] args) {
		String repository_name = "GuessingGame";
		
		for (String name : File.getGithubName()) {
			final GHAccount account = new GHAccount(name);
			try {
				System.out.println(account.repositories.getIssuesCSV(repository_name, GHIssueState.OPEN));
				System.out.println(account.requestCode.getFullDescription(account.user.fullname, repository_name));
			} catch (final RequestException e) {
				e.printStackTrace();
				if (e.getRequestCode() == RequestStatus.REPO_NOT_FOUND) System.out.println(account.repoList());
			}
		}
	}
}
