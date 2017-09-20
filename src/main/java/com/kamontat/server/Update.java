package com.kamontat.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamontat.Configuration;
import com.sun.istack.internal.Nullable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Precondition: <br>
 * <a href="https://github.com/FasterXML/jackson">jackson library</a>
 * <ul>
 * <li><a href="http://wiki.fasterxml.com/JacksonHome">WIKI</a></li>
 * <li><a href="https://github.com/FasterXML/jackson-docs">DOC</a></li>
 * </ul>
 * getting latest release from the github with anonymous auth (rate-limit: 60 times)
 *
 * @author kamontat
 * @version 1.0
 * @since 2/16/2017 AD - 11:25 PM
 */
public class Update {
	/**
	 * release title of the json
	 */
	public enum ReleaseTitle {
		URL, ASSETS_URL, UPLOAD_URL, HTML_URL, ID, TAG_NAME, NAME, CREATE_AT, UPDATE_AT, TARBALL_URL, ZIPBALL_URL, BODY;
	}
	
	/**
	 * asset title of the json
	 */
	public enum AssetTitle {
		URL, ID, NAME, CONTENT_TYPE, SIZE, DOWNLOAD_COUNT, CREATE_AT, UPDATE_AT, BROWSER_DOWNLOAD_URL;
	}
	
	private static final String GITHUB_API = "https://api.github.com/";
	private static final String REPOS_API = "repos/";
	
	private static final String MARKDOWN_CONVERTER_API = "markdown";
	private static final String RELEASE_API = "releases";
	private static final String LATEST_RELEASE_API = "releases/latest";
	
	// username
	private static final String DEVELOPER_NAME = Configuration.PROJECT_OWNER + "/"; // fill this
	// repository name
	private static final String PROJECT_NAME = Configuration.PROJECT_NAME + "/"; // fill this
	
	// header of remaining rate-limit
	private static final String RATE_REMAINING_HEADER = "X-RateLimit-Remaining";
	// json parse using jackson api
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private URL url;
	private int remaining;
	private Release release;
	
	/**
	 * get latest release by developer name(DEVELOPER_NAME) and project(PROJECT_NAME)
	 *
	 * @return get latest release if no error occurred; otherwise return <code>null</code>
	 */
	public static Release getLatestRelease() {
		try {
			Update update = new Update();
			if (update.remaining > 0) {
				return update.release;
			} else {
				throw new UpdateException(update.url, "OUT OF LIMIT");
			}
		} catch (UpdateException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * constructor of update, <br>
	 * <ol>
	 * <li>create url by {@link #DEVELOPER_NAME} and {@link #PROJECT_NAME}</li>
	 * <li>check remaining limit</li>
	 * <li>convert url respond body to json and add to release</li>
	 * <li>return the latest release</li>
	 * </ol>
	 *
	 * @throws UpdateException
	 * 		<ul>
	 * 		<li>json parsing error => can't parse respond body to json</li>
	 * 		<li>{@link #getUrl(String)}</li>
	 * 		<li>{@link #getConnection(URL)} </li>
	 * 		</ul>
	 */
	private Update() throws UpdateException {
		String link = GITHUB_API + REPOS_API + DEVELOPER_NAME + PROJECT_NAME + LATEST_RELEASE_API;
		url = getUrl(link);
		// get rate-limit
		remaining = Integer.parseInt(getConnection(url).getHeaderField(RATE_REMAINING_HEADER));
		
		// converting json to object
		try {
			JsonNode node = mapper.readTree(url);
			release = new Release(node);
		} catch (FileNotFoundException e) {
			release = Release.getEmptyRelease();
		} catch (IOException e) {
			throw new UpdateException(url, "CAN'T PARSE JSON");
		}
	}
	
	/**
	 * get url
	 *
	 * @param link
	 * 		link must content http:// or https://
	 * @return {@link URL} of the link
	 * @throws UpdateException
	 * 		url error => no protocol, string can't parse
	 */
	private static URL getUrl(String link) throws UpdateException {
		try {
			return new URL(link.toLowerCase(Locale.ENGLISH));
		} catch (MalformedURLException e) {
			throw new UpdateException(link, "URL ERROR");
		}
	}
	
	/**
	 * get connection of url
	 *
	 * @param url
	 * 		the url to connect
	 * @return https url connection
	 * @throws UpdateException
	 * 		if I/O occurred
	 */
	private static HttpURLConnection getConnection(URL url) throws UpdateException {
		try {
			return (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			throw new UpdateException(url, "IO ERROR OCCURRED");
		}
	}
	
	/**
	 * Latest Release <br>
	 * Usage: <br>
	 * &emsp; getting data inside that release by {@link #type(ReleaseTitle, Class)} <br>
	 * &emsp; also getting asset inside that release by {@link #getAsset(int)}
	 */
	public static class Release {
		/**
		 * node will be null if no latest release
		 */
		private JsonNode node;
		
		/**
		 * use this method if not latest release
		 */
		private static Release getEmptyRelease() {
			return new Release(null);
		}
		
		/**
		 * constructor to setting node
		 *
		 * @param node
		 * 		the json node
		 */
		private Release(JsonNode node) {
			this.node = node;
		}
		
		/**
		 * change text to json in form of "{ "text": "xxx" }"
		 *
		 * @param text
		 * 		some text
		 * @return String in form of json
		 */
		private String toJSON(String text) {
			return "{ \n\"text\": \"" + text.replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n") + "\"\n}";
		}
		
		/**
		 * convert markdown format to html format by using github api
		 *
		 * @param rawMarkdown
		 * 		markdown format
		 * @return {@link BufferedReader} of html result
		 * @throws UpdateException
		 * 		converting fail => can't convert markdown to html format
		 */
		private BufferedReader convertMarkdown(String rawMarkdown) throws UpdateException {
			String link = GITHUB_API + MARKDOWN_CONVERTER_API;
			String json = toJSON(rawMarkdown);
			HttpURLConnection connection = getConnection(getUrl(link));
			
			try {
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Content-Length", Integer.toString(json.length()));
				connection.setDoOutput(true);
				connection.getOutputStream().write(json.getBytes("UTF8"));
				return new BufferedReader(new InputStreamReader(connection.getInputStream()));
			} catch (IOException e) {
				throw new UpdateException(link, "CAN'T CONVERT MARKDOWN");
			}
		}
		
		/**
		 * convert {@link BufferedReader} to {@link String}
		 *
		 * @param reader
		 * 		buffer to converting
		 * @return String that content in buffer
		 */
		private String toString(BufferedReader reader) {
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} catch (IOException ignore) {
			}
			return sb.toString();
		}
		
		/**
		 * get data by title of the json and cast to className by parameter
		 *
		 * @param title
		 * 		getting the title of json
		 * @param className
		 * 		class that want to cast (simple pass by String.class, Object.class, etc.)
		 * @param <T>
		 * 		T class to cast
		 * @return object in form of class that pass in parameter, or try cast <code>empty string</code>("") to <code>className</code> parameter
		 */
		@SuppressWarnings("unchecked")
		@Nullable
		public <T> T type(ReleaseTitle title, Class<T> className) {
			if (node == null) return className.cast("Latest Release: not found");
			
			String output = node.get(title.name().toLowerCase(Locale.ENGLISH)).asText();
			
			// if body must convert markdown syntax to html syntax
			if (title == ReleaseTitle.BODY) {
				try {
					output = toString(convertMarkdown(output));
				} catch (UpdateException e) {
					e.printStackTrace();
				}
			}
			return className.cast(output);
		}
		
		/**
		 * since in github you can add asset more than 1 file, so you need to pass parameter file index that you want to use
		 *
		 * @param number
		 * 		index of asset
		 * @return {@link Asset} - getting asset with the index; <br>
		 * BUT if no latest release or no asset occurred it's will return empty asset;
		 */
		public Asset getAsset(int number) {
			if (this.node == null) return Asset.getEmptyAsset();
			
			JsonNode assetsNode = this.node.get("assets");
			if (assetsNode == null) return Asset.getEmptyAsset();
			return new Asset(assetsNode.get(number));
		}
		
		/**
		 * asset that content in {@link Release} <br>
		 * and same with {@link Release}, you can getting data inside json by method {@link #get(AssetTitle)}.
		 */
		public static class Asset {
			/**
			 * node will be null if no asset occurred
			 */
			private JsonNode node;
			
			/**
			 * use this method if not asset
			 */
			private static Asset getEmptyAsset() {
				return new Asset(null);
			}
			
			/**
			 * constructor to setting node.
			 *
			 * @param node
			 * 		the json node.
			 */
			private Asset(JsonNode node) {
				this.node = node;
			}
			
			/**
			 * convert size
			 *
			 * @param size
			 * 		in form of <code>B</code>
			 * @return String in format (#,###.##)
			 */
			private String convertSize(long size) {
				if (size <= 0) return "0";
				final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
				int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
				return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
			}
			
			/**
			 * get data by title of the json.
			 *
			 * @param title
			 * 		json title
			 * @return data String in title, or empty string("") if no asset occurred
			 */
			public String get(AssetTitle title) {
				if (node == null) return "";
				String output = node.get(title.name().toLowerCase(Locale.ENGLISH)).asText();
				if (title == AssetTitle.SIZE) output = convertSize(Long.parseLong(output));
				return output;
			}
		}
	}
	
	/**
	 * Exception that use inside {@link Update} class ONLY <br>
	 * using for exception or error that occurred when updating version
	 */
	public static class UpdateException extends Exception {
		UpdateException(URL url, String message) {
			super(String.format("error_url: %s, cause: %s", url == null ? "null": url.toString(), message));
		}
		
		UpdateException(String link, String message) {
			super(String.format("error_url: %s, cause: %s", link, message));
		}
	}
}
