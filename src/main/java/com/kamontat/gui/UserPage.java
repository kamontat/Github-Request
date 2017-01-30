package com.kamontat.gui;

import com.kamontat.gui.code.Location;

import javax.swing.*;
import java.awt.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/30/2017 AD - 5:57 PM
 */
public class UserPage extends JFrame {
	private JTextField textField1;
	private JButton selectButton;
	private JButton multiSelectButton;
	private JButton myselfButton;
	private JButton nextButton;
	private JTable table1;
	private JPanel pane;
	
	public UserPage() {
		super("User Page");
		setContentPane(pane);
	}
	
	public void run(Window oldPage) {
		pack();
		setLocation(Location.getCenterPage(oldPage, this));
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
