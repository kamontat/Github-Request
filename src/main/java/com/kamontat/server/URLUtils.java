package com.kamontat.server;

import com.kamontat.constant.RequestStatus;
import com.kamontat.exception.NonException;
import com.kamontat.exception.RequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/17/2017 AD - 1:58 PM
 */
public class URLUtils {
	/**
	 * Get a URL for a string name without throwing MalformedURLException.
	 */
	public static URLConnection getURL(String name) throws RequestException {
		if (name == null) throw new RequestException(NonException.get(), RequestStatus.LINK_NOT_FOUND);
		name = name.trim();
		name = settingURL(name);
		try {
			return new URL(name).openConnection();
		} catch (MalformedURLException e) {
			throw new RequestException(e, RequestStatus.LINK_NOT_FOUND);
		} catch (IOException e) {
			throw new RequestException(e, RequestStatus.INTERNET_ERROR);
		}
	}
	
	private static String settingURL(String name) {
		if (name.contains("http://") || name.contains("https://")) return name;
		return "http://" + name;
	}
	
	public static String getContent(URL url) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
		StringBuilder builder = new StringBuilder();
		String line = "";
		while ((line = bufferedReader.readLine()) != null) {
			builder.append(line).append("\n");
		}
		return builder.toString();
	}
	
	/**
	 * Get the size in bytes of the resource at the specified url.
	 */
	public static int getURLSize(URL url) {
		if (url == null) return 0;
		try {
			URLConnection connection = url.openConnection();
			return connection.getContentLength();
		} catch (java.io.IOException e) {
			System.out.println("\nget URL size caused an IOException: " + e.toString());
			return 0;
		}
	}
	
	
	/**
	 * Get filename portion of URL.  Result may be an empty string.
	 */
	public static String getURLFilename(URL url) {
		String filename = url.getPath();
		int k = filename.lastIndexOf("/");
		if (k == filename.length() - 1) return "";
		if (k >= 0) filename = filename.substring(k + 1);
		return filename;
	}
	
	public static void printHeader(String url) {
		try {
			URLConnection connection = URLUtils.getURL(url);
			Map header = connection.getHeaderFields();
			for (Object key : header.keySet()) {
				System.out.printf("%s: %s\n", key, header.get(key));
			}
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}
}
