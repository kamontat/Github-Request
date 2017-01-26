package main;

import exception.RequestException;
import model.GHAccount;

import java.io.IOException;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/25/2017 AD - 8:25 PM
 */
public class Main {
	public static void main(String[] args) throws IOException {
		//		GithubAccount.setRepositoryName("GuessingGame");
		//		for (String name : File.getGithubName()) {
		//			RequestStatus status = GithubAccount.get(name).saveIssues(GHIssueState.OPEN);
		//			if (status.isError()) System.err.println(GithubAccount.getStatus(status) + "\n");
		//			else System.out.println(GithubAccount.getStatus(status) + "\n");
		//		}
		
		GHAccount kamontat = new GHAccount("kamontat");
		try {
			System.out.println(kamontat.repository.getRepository("CheckIDNumber"));
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}
}
