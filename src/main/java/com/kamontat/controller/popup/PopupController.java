package com.kamontat.controller.popup;

import com.kamontat.controller.table.TableInformationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 1:35 PM
 */
public abstract class PopupController extends AbstractAction {
	private static JTable table;
	
	PopupController(String name, JTable table) {
		PopupController.table = table;
		putValue(NAME, name);
	}
	
	public int getSelectR() {
		return table.getSelectedRow();
	}
	
	public int getSelectC() {
		return table.getSelectedColumn();
	}
	
	public Object getSelectValue() {
		return table.getValueAt(getSelectR(), getSelectC());
	}
	
	public static class CopyAction extends PopupController {
		public CopyAction(JTable table) {
			super("Copy", table);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			cb.setContents(new StringSelection(table.getValueAt(getSelectR(), getSelectC()).toString()), null);
		}
	}
	
	public static class DeleteAction extends PopupController {
		public DeleteAction(JTable table) {
			super("Delete", table);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			TableInformationModel model = (TableInformationModel) table.getModel();
			model.deleteRow(getSelectR());
		}
	}
	
	public static class DeleteAllAction extends PopupController {
		public DeleteAllAction(JTable table) {
			super("Delete All", table);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			TableInformationModel model = (TableInformationModel) table.getModel();
			model.deleteAll();
		}
	}
	
	public static abstract class CustomAction extends PopupController {
		public CustomAction(String name, JTable table) {
			super(name, table);
		}
	}
}
