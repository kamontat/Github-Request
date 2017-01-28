package gui;

import server.GithubLoader;
import server.GithubToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

/**
 * Created by bubblebitoey on 1/28/2017 AD.
 */
public class LoginPage extends JFrame {
	private JPanel pane;
	private JButton loginBtn;
	private JTextField textField1;
	private JLabel tLb;
	
	public LoginPage() {
		setContentPane(pane);
		
		// set help
		tLb.setToolTipText(GithubToken.getHelp());
		
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GithubToken.getGT().setToken(textField1.getText());
				int ans = -99;
				
				loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				boolean isValid = GithubToken.getGT().isTokenValid();
				loginBtn.setCursor(Cursor.getDefaultCursor());
				
				
				if (!isValid) {
					ans = JOptionPane.showConfirmDialog(loginBtn, "do you want to continues login?", "Token Invalid", JOptionPane.YES_NO_OPTION);
					if (ans == JOptionPane.OK_OPTION) GithubLoader.setGHType(GithubLoader.Type.ANONYMOUS);
				} else {
					GithubLoader.setGHType(GithubLoader.Type.AUTH);
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
