package com.kamontat.gui;

import com.kamontat.constant.FileExtension;
import com.kamontat.constant.HotKey;
import com.kamontat.controller.loader.LoadProgress;
import com.kamontat.controller.loader.LoadingFile;
import com.kamontat.controller.loader.Task;
import com.kamontat.controller.menu.MenuBarController;
import com.kamontat.controller.menu.MenuUpdateListener;
import com.kamontat.controller.mouse.AbstractMouseAction;
import com.kamontat.controller.popup.TableAction;
import com.kamontat.controller.popup.PopupController;
import com.kamontat.controller.popup.Popup;
import com.kamontat.controller.table.TableInformationModel;
import com.kamontat.exception.RequestException;
import com.kamontat.model.github.User;
import com.kamontat.model.management.Device;
import com.kamontat.model.management.FileUtil;
import com.kamontat.model.management.Location;
import com.kamontat.model.management.Size;
import com.kamontat.server.GithubLoader;

import javax.swing.*;
import javax.swing.event.MenuEvent;
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
	private final static String name1 = "With Text File";
	private final static String name2 = "With Organizations";
	
	enum SelectedWay {
		TEXT, ORG
	}
	
	private JTextField searchingField;
	private JButton selectBtn;
	private JButton myselfBtn;
	private JButton nextBtn;
	private com.kamontat.controller.table.AutoFitTable<User> table;
	private JPanel pane;
	
	private TableInformationModel<User> model = new TableInformationModel<User>(User.getStringTitleVectorStatic());
	
	private UserPage() {
		super("User Page");
		setContentPane(pane);
		
		setMenuBar();
		settingTable();
		
		textFieldEvent();
		buttonEvent();
	}
	
	private void setMenuBar() {
		// left
		MenuBarController.Menu manageMenu = new MenuBarController.Menu("Management");
		// delete
		TableAction deleteAct = new TableAction.DeleteAction(table);
		manageMenu.addItem(new MenuBarController.Menu.Item(new HotKey(deleteAct.getName() + " selected"), deleteAct));
		// delete all
		TableAction deleteAllAct = new TableAction.DeleteAllAction(table);
		manageMenu.addItem(new MenuBarController.Menu.Item(new HotKey(deleteAllAct.getName()), deleteAllAct));
		// exit
		manageMenu.addItem(MenuBarController.Menu.Item.getExitMenu());
		
		MenuBarController.Menu multiSMenu = new MenuBarController.Menu("Multi-Selection");
		multiSMenu.addItem(new MenuBarController.Menu.Item(new HotKey(name1), new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				multiSelection(SelectedWay.TEXT);
			}
		}));
		
		multiSMenu.addItem(new MenuBarController.Menu.Item(new HotKey(name2), new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				multiSelection(SelectedWay.ORG);
			}
		}));
		
		// right
		final MenuBarController.Menu infoMenu = new MenuBarController.Menu("Information");
		final MenuBarController.Menu.Item sizeItem = new MenuBarController.Menu.Item("have: " + model.size() + " user(s)");
		infoMenu.addItem(sizeItem);
		infoMenu.addMenuUpdateListener(new MenuUpdateListener() {
			@Override
			public void updateTitle(MenuEvent e) {
				sizeItem.setText("have: " + model.size() + " user(s)");
			}
		});
		
		final MenuBarController.Menu settingMenu = new MenuBarController.Menu("Setting");
		
		MenuBarController.Menu[] lefts = MenuBarController.Menu.toArray(manageMenu, multiSMenu);
		MenuBarController.Menu[] rights = MenuBarController.Menu.toArray(infoMenu, settingMenu);
		
		setJMenuBar(new MenuBarController(lefts, rights));
	}
	
	private void buttonEvent() {
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
					Popup.getLog(myselfBtn).error("Myself Not Found", "please check sign-in token or internet connecting clearly");
				}
				GithubLoader.done(myselfBtn);
			}
		});
		
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<User> allUser = model.getAllData();
			}
		});
	}
	
	private void multiSelection(SelectedWay way) {
		if (way == SelectedWay.TEXT) multiSByText();
		else if (way == SelectedWay.ORG) multiSByOrg();
	}
	
	private void multiSByText() {
		final File file = LoadingFile.type(FileExtension.TXT).getFile(this);
		if (file != null) {
			ArrayList<String> arr = FileUtil.getContentByLine(file);
			loadingProgress(arr, "Loading... username (Text File)");
		}
	}
	
	private void multiSByOrg() {
		try {
			String name = Popup.getInput(this).question("Organization Name", "Enter the Organization Name?");
			GithubLoader.wait(this);
			User[] users = GithubLoader.getGithubLoader().getOrganization(name);
			GithubLoader.done(this);
			ArrayList<String> arr = new ArrayList<String>();
			for (User u : users) {
				arr.add(u.loginName);
			}
			
			loadingProgress(arr, "Loading... username (Organizations)");
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}
	
	private void loadingProgress(final ArrayList<String> arr, String title) {
		final Component self = this;
		final LoadProgress loadPage = new LoadProgress(this, title, arr.size());
		loadPage.setTask(new Task() {
			@Override
			public void whenUpdate(Object oldValue, Object newValue) {
				int maximum = loadPage.getMax();
				int progress = (Integer) newValue;
				int percent = (progress * 100) / maximum;
				String message = String.format("loading: %s (%d%%).\n", getLabel(), percent);
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
			
			@Override
			public void ifDone() {
				Popup.getLog(self).info("Loaded User", "there are " + arr.size() + " user(s)");
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
			Popup.getLog(selectBtn).error("User Not Found", "please check user name clearly");
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
		table.addTableAction(new TableAction.InfoAction(this, table));
		// copy action
		table.addTableAction(new TableAction.CopyAction(table));
		// delete action
		table.addTableAction(new TableAction.DeleteAction(table));
		// delete all action
		table.addTableAction(new TableAction.DeleteAllAction(table));
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
