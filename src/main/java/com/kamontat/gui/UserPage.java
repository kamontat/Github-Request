package com.kamontat.gui;

import com.kamontat.constant.FileExtension;
import com.kamontat.controller.loader.LoadProgress;
import com.kamontat.controller.loader.Task;
import com.kamontat.controller.management.Device;
import com.kamontat.controller.management.Location;
import com.kamontat.controller.management.Size;
import com.kamontat.controller.popup.PopupController;
import com.kamontat.controller.popup.PopupLog;
import com.kamontat.controller.popup.TablePopupAction;
import com.kamontat.controller.table.TableInformationModel;
import com.kamontat.exception.RequestException;
import com.kamontat.file.FileUtil;
import com.kamontat.file.LoadingFile;
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
	private JTable table;
	private JPanel pane;
	
	private TableInformationModel<User> model = new TableInformationModel<User>(User.getStringTitleVectorStatic());
	
	private UserPage() {
		super("User Page");
		setContentPane(pane);
		
		settingTable();
		settingPopup();
		
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
				User user = getMyself();
				if (user == null) {
					PopupLog.getLog(myselfBtn).errorMessage("Myself Not Found", "please check sign-in token or internet connecting clearly");
				} else {
					model.addRow(user);
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
		// delete all action
		TablePopupAction.getInstance().addAction(new PopupController.DeleteAllAction(table));
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
