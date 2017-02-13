package com.kamontat.controller.action;

import com.kamontat.controller.table.AutoFitTable;
import com.kamontat.gui.containers.UserInfoPage;
import com.kamontat.model.github.User;

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
public abstract class TableAction extends AbstractAction {
	private static AutoFitTable table;
	public String name;
	
	private TableAction(String name, AutoFitTable table) {
		TableAction.table = table;
		this.name = name;
		putValue(NAME, name);
	}
	
	public int getSelectR() {
		return table.getSelectedRow();
	}
	
	public int getSelectC() {
		return table.getSelectedColumn();
	}
	
	public String getName() {
		return name;
	}
	
	public Object getSelectValue() {
		return table.getValueAt(getSelectR(), getSelectC());
	}
	
	public static class InfoAction extends TableAction {
		private Window owner;
		
		public InfoAction(Window owner, AutoFitTable table) {
			super("Information", table);
			this.owner = owner;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = table.getSelectedObject();
			UserInfoPage.run(User.class.cast(obj), owner);
		}
	}
	
	public static class CopyAction extends TableAction {
		public CopyAction(AutoFitTable table) {
			super("Copy", table);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			cb.setContents(new StringSelection(table.getValueAt(getSelectR(), getSelectC()).toString()), null);
		}
	}
	
	public static class DeleteAction extends TableAction {
		public DeleteAction(AutoFitTable table) {
			super("Delete", table);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			table.model.deleteRow(getSelectR());
		}
	}
	
	public static class DeleteAllAction extends TableAction {
		public DeleteAllAction(AutoFitTable table) {
			super("Delete All", table);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			table.model.deleteAll();
		}
	}
	
	public static abstract class CustomAction extends TableAction {
		public CustomAction(String name, AutoFitTable table) {
			super(name, table);
		}
	}
}
