package com.kamontat.gui;

import com.kamontat.controller.Location;
import com.kamontat.controller.PopupLog;
import com.kamontat.controller.Size;
import com.kamontat.server.GithubLoader;
import com.kamontat.server.GithubToken;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	public LoginPage() {
		super("Login Page");
		setContentPane(pane);
		
		textField.setToolTipText(GithubToken.getHelp());
		
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GithubToken token = new GithubToken(textField.getText());
				int ans = -99;
				
				GithubLoader.wait(loginBtn);
				boolean isValid = token.isTokenValid();
				GithubLoader.done(loginBtn);
				
				
				if (!isValid) {
					ans = JOptionPane.showConfirmDialog(loginBtn, "do you want to continues login?", "Token Invalid", JOptionPane.YES_NO_OPTION);
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
		});
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
	
	private void loginSuccess() {
		// login success
		new UserPage().run(this);
		dispose();
		GithubLoader.done(this);
	}
	
	public void run() {
		setSize(Size.getDefaultPageSize());
		setLocation(Location.getCenterLocation(this.getSize()));
		setVisible(true);
		
		if (GithubToken.haveCache()) {
			GithubLoader.wait(this);
			GithubToken t = GithubToken.loadCache(password(Pass.GET));
			// success
			if (!t.isEmptyToken()) {
				GithubLoader.setAuth(t);
				loginSuccess();
				// remove cache
			} else {
				GithubToken.removeCache();
				
				GithubLoader.done(this);
				PopupLog.getLog(this).errorMessage("Wrong password", "The saved token will be lost!");
			}
		}
	}
	
	public static void main(String[] args) {
		new LoginPage().run();
	}
}
