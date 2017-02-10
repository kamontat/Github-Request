package com.kamontat.gui;

import com.kamontat.model.github.User;
import com.kamontat.model.management.Location;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * @author kamontat
 * @version 1.0
 */
public class RepositoryPage extends JFrame {
	private ArrayList<User> all;
	private JPanel pane;
	
	private RepositoryPage(ArrayList<User> all) {
		setContentPane(pane);
		this.all = all;
	}
	
	private void compile(Window old) {
		pack();
		setMinimumSize(new Dimension(getWidth(), getHeight()));
		setLocation(Location.getCenterPage(old, this));
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void run(final Window oldPage, final ArrayList<User> all) {
		invokeLater(new Runnable() {
			@Override
			public void run() {
				new RepositoryPage(all).compile(oldPage);
			}
		});
	}
}
