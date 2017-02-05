package com.kamontat.gui;

import com.kamontat.model.gihub.User;
import com.kamontat.model.management.Device;
import com.kamontat.model.management.Location;
import com.kamontat.model.management.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/5/2017 AD - 6:34 PM
 */
public class UserInfoPage extends JDialog {
	private static final Dimension imageSize = new Dimension(200, 200);
	
	private Window oldWindow;
	
	private JLabel image;
	private JPanel pane;
	private JLabel fullNameLb;
	private JLabel loginNameLb;
	private JLabel statusLb;
	private JLabel idLb;
	private JTextField emailLb;
	private JLabel locationLb;
	private JLabel companyLb;
	private JTextField urlLb;
	private JTextField apiLb;
	private JTextField imageLb;
	private JButton urlGoBtn;
	private JButton apiGoBtn;
	private JButton imageGoBtn;
	private JButton backBtn;
	
	private User user;
	
	private UserInfoPage(Window oldPage, User user) {
		super(oldPage, "Information User Page");
		oldWindow = oldPage;
		setContentPane(pane);
		setModal(true);
		
		this.user = user;
		setImage();
		setUsername();
		setPersonalInformation();
		setLink();
		
		addBtnEvent();
	}
	
	private void addBtnEvent() {
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	private void setImage() {
		image.setIcon(user.getImage(imageSize.width, imageSize.height));
	}
	
	private void setUsername() {
		idLb.setText(user.getID());
		fullNameLb.setText(user.fullname);
		loginNameLb.setText(String.format("(%s)", user.loginName));
		statusLb.setText(user.isFullName() ? (user.isMine() ? "Myself": "OK!"): "Not Full Name");
	}
	
	private void setPersonalInformation() {
		emailLb.setText(user.getEmail());
		locationLb.setText(user.location);
		companyLb.setText(user.getCompany());
	}
	
	private void setLink() {
		// user link
		urlLb.setText(String.valueOf(user.url));
		urlGoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Device.openWeb(user.url);
			}
		});
		// api link
		apiLb.setText(String.valueOf(user.api_url));
		apiGoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Device.openWeb(user.api_url);
			}
		});
		// image link
		imageLb.setText(String.valueOf(user.image_url));
		imageGoBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Device.openWeb(user.image_url);
			}
		});
	}
	
	private void compile() {
		setSize(Size.getDefaultPageSize());
		setLocation(Location.getCenterPage(oldWindow, this));
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public static void run(final User user, final Window oldPage) {
		invokeLater(new Runnable() {
			@Override
			public void run() {
				new UserInfoPage(oldPage, user).compile();
			}
		});
	}
}
