package com.kamontat.gui;

import com.kamontat.constant.FileExtension;
import com.kamontat.controller.loader.LoadProgress;
import com.kamontat.controller.loader.LoadingFile;
import com.kamontat.controller.loader.Task;
import com.kamontat.controller.mouse.AbstractMouseAction;
import com.kamontat.controller.popup.PopupAction;
import com.kamontat.controller.popup.PopupController;
import com.kamontat.controller.popup.PopupLog;
import com.kamontat.controller.table.TableInformationModel;
import com.kamontat.exception.RequestException;
import com.kamontat.model.gihub.User;
import com.kamontat.model.management.Device;
import com.kamontat.model.management.FileUtil;
import com.kamontat.model.management.Location;
import com.kamontat.model.management.Size;
import com.kamontat.server.GithubLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

import static javax.swing.SwingUtilities.invokeLater;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/30/2017 AD - 5:57 PM
 */
public class UserPage extends JFrame {
	private JTextField searchingField;
	private JButton selectBtn;
	private JButton multiSBtn;
	private JButton myselfBtn;
	private JButton nextBtn;
	private com.kamontat.controller.table.AutoFitTable<User> table;
	private JPanel pane;
	
	private TableInformationModel<User> model = new TableInformationModel<User>(User.getStringTitleVectorStatic());
	
	private UserPage() {
		super("User Page");
		setContentPane(pane);
		
		settingTable();
		
		textFieldEvent();
		buttonEvent();
	}
	
	private void buttonEvent() {
		final Window self = this;
		selectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addUser(searchingField.getText());
			}
		});
		
		myselfBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GithubLoader.wait(myselfBtn);
				try {
					model.addRow(GithubLoader.getGithubLoader().getMyself());
				} catch (RequestException e1) {
					PopupLog.getLog(myselfBtn).errorMessage("Myself Not Found", "please check sign-in token or internet connecting clearly");
				}
				GithubLoader.done(myselfBtn);
			}
		});
		
		multiSBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final File file = LoadingFile.type(FileExtension.TXT).getFile(self);
				if (file != null) {
					final ArrayList<String> arr = FileUtil.getContentByLine(file);
					
					final LoadProgress loadPage = new LoadProgress(self, "Loading... username", arr.size());
					loadPage.setTask(new Task() {
						@Override
						public void whenUpdate(Object oldValue, Object newValue) {
							int maximum = loadPage.getMax();
							int progress = (Integer) newValue;
							String message = String.format("loading: %s (%d%%).\n", getLabel(), (progress * 100) / maximum);
							loadPage.set(message, progress);
						}
						
						@Override
						public void progress() {
							for (String name : arr) {
								update(name, 1);
								addUser(name);
								if (loadPage.isCancel()) break;
							}
						}
					});
				}
			}
		});
		
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// open nextEvent page
			}
		});
	}
	
	private void textFieldEvent() {
		searchingField.setToolTipText("Enter github username");
		searchingField.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addUser(searchingField.getText());
				searchingField.setText("");
			}
		});
	}
	
	private void addUser(String name) {
		GithubLoader.wait(this);
		User user = null;
		try {
			user = GithubLoader.getGithubLoader().getUser(name);
			model.addRow(user);
		} catch (RequestException e) {
			PopupLog.getLog(selectBtn).errorMessage("User Not Found", "please check user name clearly");
		}
		GithubLoader.done(this);
	}
	
	private void settingTable() {
		table.setModel(model);
		settingPopup();
		table.addMouseListener(new AbstractMouseAction() {
			
			@Override
			public void tripleClick(MouseEvent e) {
				table.updateSelected(e.getPoint());
				
				// triple click event
				for (int i = 0; i < table.getColumnCount(); i++) {
					try {
						URL url = (URL) table.getValueAt(row(), i);
						Device.openWeb(url);
					} catch (ClassCastException ignore) {
					}
				}
			}
			
			@Override
			public void rightClick(MouseEvent e) {
				table.updateSelected(e.getPoint());
				
				if (e.isPopupTrigger()) {
					table.showPopup(e);
				}
			}
			
			private int row() {
				return table.getSelectedRow();
			}
		});
	}
	
	private void settingPopup() {
		table.addPopup(PopupController.getInstance());
		// information action
		table.addPopupAction(new PopupAction.InfoAction(this, table));
		// copy action
		table.addPopupAction(new PopupAction.CopyAction(table));
		// delete action
		table.addPopupAction(new PopupAction.DeleteAction(table));
		// delete all action
		table.addPopupAction(new PopupAction.DeleteAllAction(table));
	}
	
	private void compile(Window oldPage) {
		setSize(Size.getDefaultPageSize());
		setLocation(Location.getCenterPage(oldPage, this));
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public static void run(final Window old) {
		invokeLater(new Runnable() {
			@Override
			public void run() {
				new UserPage().compile(old);
			}
		});
	}
	
}
