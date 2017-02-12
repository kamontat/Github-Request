package com.kamontat.model.management;

import java.awt.*;
import java.net.URL;

/**
 * contain {@link #openWeb(URL)} to open web in browser
 *
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 4:33 AM
 */
public class Device {
	private static Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop(): null;
	
	/**
	 * open website link by using url parameter
	 *
	 * @param url
	 * 		url link to open in browser
	 */
	public static void openWeb(URL url) {
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(url.toURI());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
