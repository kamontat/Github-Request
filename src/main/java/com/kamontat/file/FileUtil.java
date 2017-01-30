package com.kamontat.file;

import com.kamontat.constant.FileExtension;

import java.io.File;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/25/2017 AD - 8:44 PM
 */
public class FileUtil {
	static FileExtension getExtension(File f) {
		return FileExtension.getExtension(getStringExtension(f));
	}
	
	private static String getStringExtension(File f) {
		return f.getName().substring(f.getName().indexOf(".") + 1, f.getName().length()).toLowerCase();
	}
}
