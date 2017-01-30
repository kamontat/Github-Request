package com.kamontat.controller;

import javax.swing.*;
import java.awt.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 1:51 AM
 */
public class PopupLog {
	private static PopupLog popup;
	private Component parent;
	
	public static PopupLog getLog(Component parent) {
		if (popup == null) popup = new PopupLog(parent);
		return popup;
	}
	
	private PopupLog(Component parent) {
		this.parent = parent;
	}
	
	public void normalMessage(String title, String message) {
		getMessage(JOptionPane.PLAIN_MESSAGE, title, message);
	}
	
	public void errorMessage(String title, String message) {
		getMessage(JOptionPane.ERROR_MESSAGE, title, message);
	}
	
	public void warningMessage(String title, String message) {
		getMessage(JOptionPane.WARNING_MESSAGE, title, message);
	}
	
	public void infoMessage(String title, String message) {
		getMessage(JOptionPane.INFORMATION_MESSAGE, title, message);
	}
	
	public void questionMessage(String title, String message) {
		getMessage(JOptionPane.QUESTION_MESSAGE, title, message);
	}
	
	private void getMessage(int type, String title, String message) {
		JOptionPane.showMessageDialog(parent, message, title, type);
	}
}
