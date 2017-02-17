package com.kamontat.controller.loader;

import com.kamontat.constant.FileExtension;
import com.kamontat.model.management.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/29/2017 AD - 2:36 PM
 */
public class LoadingFile {
	private JFileChooser fileChooser = new JFileChooser();
	
	public static LoadingFile type(final FileExtension extension) {
		return new LoadingFile(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || FileUtils.getExtension(f) == extension;
			}
			
			@Override
			public String getDescription() {
				return extension.description;
			}
		});
	}
	
	private LoadingFile(FileFilter fileFilter) {
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(fileFilter);
	}
	
	public File getFile(Component parent) {
		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
}
