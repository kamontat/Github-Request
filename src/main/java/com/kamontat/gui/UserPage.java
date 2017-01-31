package com.kamontat.gui;

import com.kamontat.constant.FileExtension;
import com.kamontat.controller.*;
import com.kamontat.exception.RequestException;
import com.kamontat.file.FileUtil;
import com.kamontat.file.LoadingFile;
import com.kamontat.model.TableInformation;
import com.kamontat.model.User;
import com.kamontat.server.GithubLoader;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

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
	private JTable table;
	private JPanel pane;
	
	private TableInformationModel<User> model = new TableInformationModel<User>(User.getStringTitleVectorStatic());
	
	public UserPage() {
		super("User Page");
		setContentPane(pane);
		
		settingTable();
		settingPopup();
		
		textFieldEvent();
		buttonEvent();
	}
	
	private void buttonEvent() {
		final Component self = this;
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
				User user = getMyself();
				if (user == null) {
					PopupLog.getLog(myselfBtn).errorMessage("Myself Not Found", "please check sign-in token or internet connecting clearly");
				} else {
					save(user);
				}
				GithubLoader.done(myselfBtn);
			}
		});
		multiSBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = LoadingFile.type(FileExtension.TXT).getFile(self);
				if (file != null) {
					ArrayList<String> arr = FileUtil.getContentByLine(file);
					for (String name : arr) {
						addUser(name);
					}
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
		User user = isUserFound(name);
		if (user == null) {
			PopupLog.getLog(selectBtn).errorMessage("User Not Found", "please check user name clearly");
		} else {
			save(user);
		}
		GithubLoader.done(this);
	}
	
	private User isUserFound(String username) {
		try {
			return GithubLoader.getGithubLoader().getUser(username);
		} catch (RequestException e) {
			return null;
		}
	}
	
	private void save(TableInformation<User> u) {
		model.addRow(u);
	}
	
	private void delete(int row) {
		model.deleteRow(row);
	}
	
	private void settingTable() {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				if (e.getClickCount() == 3) {
					// triple click event
					for (int i = 0; i < table.getColumnCount(); i++) {
						try {
							URL url = (URL) table.getValueAt(row, i);
							Device.openWeb(url);
						} catch (ClassCastException ignore) {
							
						}
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					if (e.isPopupTrigger()) {
						TablePopupAction.getInstance().show(e);
					}
				}
			}
		});
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				// auto fit the content
				autoFit();
			}
		});
		
		// auto fit the content
		autoFit();
	}
	
	private void settingPopup() {
		// copy action
		TablePopupAction.getInstance().addAction(new PopupController.CopyAction(table));
		// delete action
		TablePopupAction.getInstance().addAction(new PopupController.DeleteAction(table));
	}
	
	private void autoFit() {
		for (int column = 0; column < table.getColumnCount(); column++) {
			TableColumn tableColumn = table.getColumnModel().getColumn(column);
			int preferredWidth = tableColumn.getMinWidth();
			int maxWidth = tableColumn.getMaxWidth();
			
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
				Component c = table.prepareRenderer(cellRenderer, row, column);
				int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
				preferredWidth = Math.max(preferredWidth, width);
				
				//  We've exceeded the maximum width, no need to check other rows
				if (preferredWidth >= maxWidth) {
					preferredWidth = maxWidth;
					break;
				}
			}
			
			tableColumn.setPreferredWidth(preferredWidth + 50);
		}
	}
	
	private User getMyself() {
		try {
			return GithubLoader.getGithubLoader().getMyself();
		} catch (RequestException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void run(Window oldPage) {
		setSize(Size.getDefaultPageSize());
		setLocation(Location.getCenterPage(oldPage, this));
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
