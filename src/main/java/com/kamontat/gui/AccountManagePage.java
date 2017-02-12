package com.kamontat.gui;

import com.kamontat.model.github.GHAccount;
import com.kamontat.model.management.Location;
import com.kamontat.model.management.Size;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/12/2017 AD - 7:42 PM
 */
public class AccountManagePage extends JFrame {
	private ArrayList<GHAccount> accounts;
	
	public AccountManagePage(ArrayList<GHAccount> accounts) {
		this.accounts = accounts;
		
	}
	
	private void compile(Window oldPage) {
		setSize(Size.getDefaultPageSize());
		setLocation(Location.getCenterPage(oldPage, this));
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void run(final Window old, final ArrayList<GHAccount> accounts) {
		invokeLater(new Runnable() {
			@Override
			public void run() {
				new AccountManagePage(accounts).compile(old);
			}
		});
	}
}
