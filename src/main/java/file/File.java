package file;

import model.User;
import server.GHAccount;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/25/2017 AD - 8:44 PM
 */
public class File {
	private static final String FILE_NAME = "src/main/resources/file_name/github_name.txt";
	
	public static ArrayList<String> getGithubName() {
		ArrayList<String> allName = new ArrayList<String>();
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
			while ((line = br.readLine()) != null) {
				allName.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return allName;
	}
	
	public static ArrayList<User> getGHUser() {
		ArrayList<User> users = new ArrayList<>();
		try {
			for (String name : getGithubName()) {
				users.add(GHAccount.getUser(name));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return users;
	}
}
