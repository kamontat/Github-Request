package main;

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
			System.out.println(account.user.url);
			System.out.println(account.getIssueCSV(repository_name, GHIssueState.OPEN));
		}
		
		// cache test
//		Cache.loadCache().setFileName("hello-world").saveToFile(GithubToken.getGitToken("a", "asdffdsa"));
//		GithubToken x = Cache.loadCache().setFileName("hello-world").loadFromFile(GithubToken.class);
//		System.out.println(x);
		
		// encryption test
//		String pass = "kamontat";
//		String text = "I fuck you";
//		Encryption encryption = Encryption.get(pass);
//
//		String code = encryption.encode(pass, text);
//		System.out.println(text.equals(code));
//
//		String text2 = encryption.decode(pass, code);
//		System.out.println(text.equals(text2));
	}
}
