package com.kamontat.gui;

import com.kamontat.controller.*;
import com.kamontat.exception.RequestException;
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
import java.net.URL;

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
	
	public UserPage() {
		super("User Page");
		setContentPane(pane);
		
		settingTable();
		
		textFieldEvent();
		buttonEvent();
	}
	
	private void buttonEvent() {
		selectBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getAndAddUser();
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
				getAndAddUser();
				searchingField.setText("");
			}
		});
	}
	
	private void getAndAddUser() {
		GithubLoader.wait(this);
		User user = isUserFound(searchingField.getText());
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
	
	private void save(User u) {
		UserTableModel model = (UserTableModel) table.getModel();
		model.addRow(u);
	}
	
	private void settingTable() {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new UserTableModel(User.getStringTitleVectorStatic()));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				if (e.getClickCount() == 2) {
					// double click event
					for (int i = 0; i < table.getColumnCount(); i++) {
						try {
							URL url = (URL) table.getValueAt(row, i);
							Device.openWeb(url);
						} catch (ClassCastException ignore) {
						}
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					// right click event
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
			
			tableColumn.setPreferredWidth(preferredWidth);
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
