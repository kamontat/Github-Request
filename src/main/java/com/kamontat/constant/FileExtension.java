package com.kamontat.constant;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 12:56 AM
 */
public enum FileExtension {
	ALL("", "all file"),
	TXT("txt", "plain text file"),
	PDF("pdf", "document file"),
	XLS("xls", "excel(97-2003) file"),
	XLSX("xlsx", "excel(2007-now) file"),
	CSV("csv", "tabular data in plain text file"),
	OTHER("", "");
	
	public String text;
	public String description;
	
	private FileExtension(String s, String explanation) {
		text = s;
		description = explanation;
	}
	
	public static FileExtension getExtension(String t) {
		for (FileExtension fe : FileExtension.values()) {
			if (t.equals(fe.text)) return fe;
		}
		OTHER.setText(t, "other");
		return OTHER;
	}
	
	/**
	 * can set only <code>OTHER</code>
	 *
	 * @param t
	 * 		text
	 * @param d
	 * 		description
	 */
	private void setText(String t, String d) {
		if (this == OTHER) {
			text = t;
			description = d;
		}
	}
	
	@Override
	public String toString() {
		return text;
	}
}
