package com.kamontat.file;

import java.io.File;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/25/2017 AD - 8:44 PM
 */
public class FileUtil {
	enum FileExtension {
		TXT("txt"), PDF("pdf"), XLS("xls"), XLSX("xlsx"), OTHER("");
		
		String text;
		
		private FileExtension(String s) {
			text = s;
		}
		
		private static FileExtension getExtension(String t) {
			for (FileExtension fe : FileExtension.values()) {
				if (t.equals(fe.text)) return fe;
			}
			OTHER.setText(t);
			return OTHER;
		}
		
		private void setText(String t) {
			text = t;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	
	static FileExtension getExtension(File f) {
		return FileExtension.getExtension(getStringExtension(f));
	}
	
	private static String getStringExtension(File f) {
		return f.getName().substring(f.getName().indexOf(".") + 1, f.getName().length()).toLowerCase();
	}
}
