package com.kamontat.gui;

import com.kamontat.server.GithubLoader;
import com.kamontat.server.GithubToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bubblebitoey
 *
 * @version 1.4
 * @since 1/28/2017 AD
 */
public class LoginPage extends JFrame {
	enum Pass {
		SET, GET;
	}
	
	private JPanel pane;
	private JButton loginBtn;
	private JTextField textField;
	private JLabel tLb;
	
	public LoginPage() {
		setContentPane(pane);
		
		// set help
		tLb.setToolTipText(GithubToken.getHelp());
		
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GithubToken token = new GithubToken(textField.getText());
				int ans = -99;
				
				loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				boolean isValid = token.isTokenValid();
				loginBtn.setCursor(Cursor.getDefaultCursor());
				
				
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
		String title = "";
		String output = "";
		
		if (pt == Pass.SET) {
			title = "Set Password";
			output = "For use next time without enter token again";
		} else {
			title = "Enter Password";
			output = "Enter your password for access token";
		}
		
		return JOptionPane.showInputDialog(loginBtn, output, title, JOptionPane.QUESTION_MESSAGE);
	}
	
	private void loginSuccess() {
		// login success
		JOptionPane.showMessageDialog(loginBtn, "login can use " + GithubLoader.getMaximumRateLimit(), "Token valid", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}
	
	public void run(Point p) {
		pack();
		setLocation(p);
		setVisible(true);
		
		if (GithubToken.haveCache()) {
			GithubToken t = GithubToken.loadCache(password(Pass.GET));
			// success
			if (!t.isEmptyToken()) {
				GithubLoader.setAuth(t);
				loginSuccess();
				// remove cache
			} else {
				GithubToken.removeCache();
			}
		}
	}
	
	public static void main(String[] args) {
		new LoginPage().run(new Point(0, 0));
	}
}
