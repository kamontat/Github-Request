package com.kamontat.server;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/21/2017 AD - 3:54 PM
 */
public class UpdateTest {
	@Test
	public void getLatestRelease() throws Exception {
		String version = Update.getLatestRelease().type(Update.ReleaseTitle.TAG_NAME, String.class);
		String id = Update.getLatestRelease().type(Update.ReleaseTitle.ID, String.class);
		
		Assert.assertEquals("Latest Release: not found", version);
		Assert.assertEquals("Latest Release: not found", id);
	}
}