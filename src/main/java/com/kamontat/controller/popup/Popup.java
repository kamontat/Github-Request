package com.kamontat.controller.popup;

import javax.swing.*;
import java.awt.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 1:51 AM
 */
public class Popup {
	private Component parent;
	
	public static Log getLog(Component parent) {
		return new Log(parent);
	}
	
	public static Input getInput(Component parent) {
		return new Input(parent);
	}
	
	private Popup(Component parent) {
		this.parent = parent;
	}
	
	public static class Log extends Popup {
		private Log(Component parent) {
			super(parent);
		}
		
		public void normal(String title, String message) {
			getMessage(JOptionPane.PLAIN_MESSAGE, title, message);
		}
		
		public void error(String title, String message) {
			getMessage(JOptionPane.ERROR_MESSAGE, title, message);
		}
		
		public void warning(String title, String message) {
			getMessage(JOptionPane.WARNING_MESSAGE, title, message);
		}
		
		public void info(String title, String message) {
			getMessage(JOptionPane.INFORMATION_MESSAGE, title, message);
		}
		
		public void question(String title, String message) {
			getMessage(JOptionPane.QUESTION_MESSAGE, title, message);
		}
		
		private void getMessage(int type, String title, String message) {
			JOptionPane.showMessageDialog(super.parent, message, title, type);
		}
	}
	
	public static class Input extends Popup {
		private Input(Component parent) {
			super(parent);
		}
		
		public String normal(String title, String message) {
			return getMessage(JOptionPane.PLAIN_MESSAGE, title, message);
		}
		
		public String error(String title, String message) {
			return getMessage(JOptionPane.ERROR_MESSAGE, title, message);
		}
		
		public String warning(String title, String message) {
			return getMessage(JOptionPane.WARNING_MESSAGE, title, message);
		}
		
		public String info(String title, String message) {
			return getMessage(JOptionPane.INFORMATION_MESSAGE, title, message);
		}
		
		public String question(String title, String message) {
			return getMessage(JOptionPane.QUESTION_MESSAGE, title, message);
		}
		
		private String getMessage(int type, String title, String message) {
			return JOptionPane.showInputDialog(super.parent, message, title, type);
		}
	}
}
