package com.kamontat.constant;

import com.kamontat.model.management.FileUtils;

import java.io.File;

/**
 * All file extension that might use in this program. <br>
 * contain: txt, pdf, xls, xlsx, csv <br>
 * and if don't have extension in this enum this will create extension other and return by itself
 * you can easiest found extension by use {@link FileExtension#getExtension(String)} and you can get string extension by using  {@link FileUtils#getStringExtension(File) FileUtils.getStringExtension(File)} <br>
 * <p>
 * BUT For more easy than that you can use {@link FileUtils#getExtension(File) FileUtils.getExtension(File)} and it will return extension that you want</p>
 *
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
	OTHER("", "Other");
	
	/**
	 * extension like txt, pdf, js, java
	 */
	public String text;
	/**
	 * description of extension like <b>excel(97-2003) file</b> for <b>xls</b>
	 */
	public String description;
	
	private FileExtension(String s, String explanation) {
		text = s;
		description = explanation;
	}
	
	/**
	 * searching extension by string extension that can have or don't have dot <br>
	 * Example
	 * both <i>txt</i> and <i>.txt</i> are valid
	 *
	 * @param t
	 * 		extension string
	 * @return enum extension
	 */
	public static FileExtension getExtension(String t) {
		for (FileExtension fe : FileExtension.values()) {
			if (t.equalsIgnoreCase(fe.text)) return fe;
		}
		OTHER.setExtensionText(t);
		return OTHER;
	}
	
	/**
	 * no use any way except in {@link #getExtension(String)}
	 *
	 * @param t
	 * 		some string
	 */
	private void setExtensionText(String t) {
		text = t;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
