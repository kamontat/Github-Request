package com.kamontat.gui;

import com.kamontat.constant.HotKey;
import com.kamontat.controller.menu.MenuBarController;
import com.kamontat.controller.menu.MenuUpdateListener;
import com.kamontat.controller.popup.Popup;
import com.kamontat.model.management.Location;
import com.kamontat.model.management.Size;
import com.kamontat.server.GithubLoader;
import com.kamontat.server.GithubToken;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.kamontat.gui.LoginPage.Pass.GET;
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
	private JPasswordField textField;
	
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
		manageMenu.addItem(MenuBarController.Menu.Item.getExitMenu());
		
		// right
		final MenuBarController.Menu settingMenu = new MenuBarController.Menu("Setting");
		settingMenu.addMenuUpdateListener(new MenuUpdateListener() {
			@Override
			public void updateTitle(MenuEvent e) {
				// load cache
				if (GithubToken.haveCache() && !settingMenu.isContain(LoadCache.name)) {
					// loading cache
					settingMenu.addItem(new LoadCache());
				}
			}
		});
		
		MenuBarController.Menu[] lefts = MenuBarController.Menu.toArray(manageMenu);
		MenuBarController.Menu[] rights = MenuBarController.Menu.toArray(settingMenu);
		
		setJMenuBar(new MenuBarController(lefts, rights));
	}
	
	private void login() {
		GithubToken token = new GithubToken(new String(textField.getPassword()));
		int ans = -99;
		
		GithubLoader.wait(loginBtn);
		boolean isValid = token.isTokenValid();
		GithubLoader.done(loginBtn);
		
		
		if (!isValid) {
			ans = JOptionPane.showConfirmDialog(loginBtn, "do you want to sign in as anonymous?", "Token Invalid", JOptionPane.YES_NO_OPTION);
			if (ans == JOptionPane.OK_OPTION) GithubLoader.setAnonymous();
		} else {
			String password = password(Pass.SET);
			System.out.println(password);
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
				title = "Setting Password";
				output = "For (Personal Computer) you can blank input field to set as no password \n\t\tSo next time you will login automatically\n" + "For (Public Computer) you should enter password to encrypt your token";
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
		return Popup.getInput(loginBtn).question(title, output);
	}
	
	private void loadCache() {
		GithubLoader.wait(this);
		if (!preLoadCache()) {
			GithubToken t = GithubToken.loadCache(password(GET));
			// success
			if (!t.isEmptyToken()) {
				GithubLoader.setAuth(t);
				loginSuccess();
				// remove cache
			} else {
				GithubToken.removeCache();
				
				Popup.getLog(this).error("Have Problem", "The saved token will be lost!");
			}
		}
		GithubLoader.done(this);
	}
	
	/**
	 * this method will try to loading cache with no password.
	 *
	 * @return true if loading successfully, false in otherwise
	 */
	private boolean preLoadCache() {
		GithubLoader.wait(this);
		GithubToken t = GithubToken.loadCache();
		if (!t.isEmptyToken()) {
			GithubLoader.setAuth(t);
			loginSuccess();
			GithubLoader.done(this);
			return true;
		}
		GithubLoader.done(this);
		return false;
	}
	
	private void loginSuccess() {
		// login success
		UserPage.run(this);
		dispose();
	}
	
	private void compile() {
		if (!preLoadCache()) {
			setSize(Size.getDefaultPageSize());
			setLocation(Location.getCenterLocation(this.getSize()));
			setVisible(true);
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
	
	private class LoadCache extends MenuBarController.Menu.Item {
		private final static String name = "Load Cache";
		
		private LoadCache() {
			super(new HotKey(name), new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					loadCache();
				}
			});
		}
	}
}