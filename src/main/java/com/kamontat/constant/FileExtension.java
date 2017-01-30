package com.kamontat.constant;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 12:56 AM
 */
public enum FileExtension {
	TXT("txt"), PDF("pdf"), XLS("xls"), XLSX("xlsx"), CSV("csv"), OTHER("");
	
	String text;
	
	private FileExtension(String s) {
		text = s;
	}
	
	public static FileExtension getExtension(String t) {
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
