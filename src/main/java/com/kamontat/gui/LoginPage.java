package com.kamontat.gui;

import com.kamontat.constant.HotKey;
import com.kamontat.controller.menu.MenuBarController;
import com.kamontat.controller.popup.PopupLog;
import com.kamontat.model.management.Location;
import com.kamontat.model.management.Size;
import com.kamontat.server.GithubLoader;
import com.kamontat.server.GithubToken;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * Created by bubblebitoey
 *
 * @version 2.0
 * @since 1/28/2017 AD
 */
public class LoginPage extends JFrame {
	enum Pass {
		SET, GET
	}
	
	private JPanel pane;
	private JButton loginBtn;
	private JTextField textField;
	
	private LoginPage() {
		super("Login Page");
		setContentPane(pane);
		setMenuBar();
		
		textField.setToolTipText(GithubToken.getHelp());
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
	}
	
	private void setMenuBar() {
		// left
		MenuBarController.Menu manageMenu = new MenuBarController.Menu("Management");
		
		
		// right
		MenuBarController.Menu settingMenu = new MenuBarController.Menu("Setting");
		settingMenu.addItem(MenuBarController.Menu.MenuItem.getExitMenu());
		// custom setting
		settingMenu.addItem(new MenuBarController.Menu.MenuItem(new HotKey("test"), new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		}));
		
		MenuBarController.Menu[] lefts = MenuBarController.Menu.toArray(manageMenu);
		MenuBarController.Menu[] rights = MenuBarController.Menu.toArray(settingMenu);
		
		setJMenuBar(new MenuBarController(lefts, rights));
	}
	
	private void login() {
		GithubToken token = new GithubToken(textField.getText());
		int ans = -99;
		
		GithubLoader.wait(loginBtn);
		boolean isValid = token.isTokenValid();
		GithubLoader.done(loginBtn);
		
		
		if (!isValid) {
			ans = JOptionPane.showConfirmDialog(loginBtn, "do you want to sign in as anonymous?", "Token Invalid", JOptionPane.YES_NO_OPTION);
			if (ans == JOptionPane.OK_OPTION) GithubLoader.setAnonymous();
		} else {
			String password = password(Pass.SET);
			GithubLoader.setAuth(token);
			token.saveCache(password);
		}
		
		if (ans == JOptionPane.OK_OPTION || ans == -99) {
			loginSuccess();
		}
	}
	
	private String password(Pass pt) {
		String title, output;
		
		switch (pt) {
			case SET:
				title = "Set Password";
				output = "For use next time without enter token again";
				break;
			case GET:
				title = "Enter Password";
				output = "Enter your password for access token";
				break;
			default:
				title = "Password";
				output = "";
				break;
		}
		
		return JOptionPane.showInputDialog(loginBtn, output, title, JOptionPane.QUESTION_MESSAGE);
	}
	
	private void loadCache() {
		GithubLoader.wait(this);
		String password = password(Pass.GET);
		GithubToken t = GithubToken.loadCache(password);
		
		// success
		if (!t.isEmptyToken()) {
			GithubLoader.setAuth(t);
			loginSuccess();
			// remove cache
		} else {
			GithubToken.removeCache();
			
			GithubLoader.done(this);
			PopupLog.getLog(this).errorMessage("Have Problem", "The saved token will be lost!");
		}
	}
	
	private void loginSuccess() {
		// login success
		UserPage.run(this);
		dispose();
		GithubLoader.done(this);
	}
	
	private void compile() {
		setSize(Size.getDefaultPageSize());
		setLocation(Location.getCenterLocation(this.getSize()));
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// load cache
		if (GithubToken.haveCache()) {
			loadCache();
		}
	}
	
	public static void run() {
		invokeLater(new Runnable() {
			@Override
			public void run() {
				new LoginPage().compile();
			}
		});
	}
}