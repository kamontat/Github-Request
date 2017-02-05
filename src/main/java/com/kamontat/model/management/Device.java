package com.kamontat.model.management;

import java.awt.*;
import java.net.URL;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 4:33 AM
 */
public class Device {
	private static Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop(): null;
	
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
