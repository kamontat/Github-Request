package com.kamontat.gui;

import com.kamontat.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/26/2017 AD - 2:34 PM
 */
public class MainPage extends JFrame {
	private JComboBox comboBox;
	private JButton allButton;
	private JButton selectButton;
	private JPanel pane;
	private JTextField textField;
	
	public MainPage(ArrayList<User> users) {
		setContentPane(pane);
	}
	
	private void setComboBox(ArrayList<User> users) {
		for (User user : users) {
			comboBox.addItem(user);
		}
	}
	
	public void run(Point point) {
		pack();
		setMinimumSize(new Dimension(getWidth(), getHeight()));
		setLocation(point);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
//		MainPage main = new MainPage(FileUtil.getGHUser());
//		main.run(new Point(0, 0));
	}
}
