package com.kamontat.server;

import com.kamontat.constant.RequestStatus;
import com.kamontat.exception.RequestException;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/16/2017 AD - 11:25 PM
 */
public class Update {
	private static final String DEVELOPER_NAME = "kamontat";
	private static final String PROJECT_NAME = "Github-Request";
	
	private static final Update update = new Update();
	private static GHRepository project;
	private static List<GHRelease> releases;
	
	private Update() {
		try {
			project = GithubLoader.getGithubLoader().getUser(DEVELOPER_NAME).getRepository(PROJECT_NAME);
			releases = project.listReleases().asList();
		} catch (RequestException e) {
			e.printStackTrace();
		} catch (IOException e) {
			new RequestException(e, RequestStatus.UPDATE_ERROR, DEVELOPER_NAME, PROJECT_NAME).printStackTrace();
		}
	}
	
	public boolean isLatestVersion() {
		for (GHRelease release : releases) {
			// do some job
		}
		return false;
	}
	
	private GHRelease getLatestRelease() {
		return null;
	}
}
