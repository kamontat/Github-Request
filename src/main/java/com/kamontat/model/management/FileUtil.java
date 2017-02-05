package com.kamontat.model.management;

import com.kamontat.constant.FileExtension;

import java.io.*;
import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/25/2017 AD - 8:44 PM
 */
public class FileUtil {
	public static FileExtension getExtension(File f) {
		return FileExtension.getExtension(getStringExtension(f));
	}
	
	public static ArrayList<String> getContentByLine(File f) {
		ArrayList<String> output = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			while ((line = br.readLine()) != null) {
				output.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public static int countLine(File f) {
		return getContentByLine(f).size();
	}
	
	private static String getStringExtension(File f) {
		return f.getName().substring(f.getName().indexOf(".") + 1, f.getName().length()).toLowerCase();
	}
}
