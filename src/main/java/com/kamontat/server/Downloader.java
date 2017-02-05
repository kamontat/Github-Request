package com.kamontat.server;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/5/2017 AD - 6:40 PM
 */
public class Downloader {
	private URL link;
	
	private static Downloader il;
	
	public static Downloader from(String url) {
		return new Downloader(url);
	}
	
	public static Downloader from(URL url) {
		return new Downloader(url);
	}
	
	public ImageIcon downloadImage() {
		if (canDownload()) {
			try {
				return new ImageIcon(ImageIO.read(link));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public ImageIcon downloadImage(int w, int h) {
		if (canDownload()) {
			try {
				return new ImageIcon(ImageIO.read(link).getScaledInstance(w, h, Image.SCALE_SMOOTH));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private Downloader(String url) {
		try {
			this.link = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private Downloader(URL url) {
		this.link = url;
	}
	
	private boolean canDownload() {
		return link != null;
	}
}
