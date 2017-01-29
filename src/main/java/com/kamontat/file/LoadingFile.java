package com.kamontat.file;

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
	private static JFileChooser fileChooser = new JFileChooser();
	
	private static void setFileChooser() {
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || FileUtil.getExtension(f) == FileUtil.FileExtension.TXT;
			}
			
			@Override
			public String getDescription() {
				return "Text File Only (.txt)";
			}
		});
	}
	
	public static File getFile(Component parent) {
		setFileChooser();
		
		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		File f = LoadingFile.getFile(frame);
		System.out.println(f);
	}
}
