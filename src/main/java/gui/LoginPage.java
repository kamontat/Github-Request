package gui;

import server.GithubLoader;
import server.GithubToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bubblebitoey on 1/28/2017 AD.
 */
public class LoginPage extends JFrame {
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
					String password = JOptionPane.showInputDialog(loginBtn, "For use next time without enter token again", "Set Password", JOptionPane.QUESTION_MESSAGE);
					GithubLoader.setAuth(token);
					token.saveCache(password);
				}
				
				if (ans == JOptionPane.OK_OPTION || ans == -99) {
					// login success
					JOptionPane.showMessageDialog(loginBtn, "login can use " + GithubLoader.getMaximumRateLimit(), "Token valid", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}
	
	public void run(Point p) {
		pack();
		
		setLocation(p);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new LoginPage().run(new Point(0, 0));
	}
}
