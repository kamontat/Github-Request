package com.kamontat.controller.table;

import com.kamontat.controller.popup.PopupController;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/5/2017 AD - 7:38 PM
 */
public class AutoFitTable extends JTable {
	private PopupController popup;
	
	public AutoFitTable() {
	}
	
	public void addPopup(PopupController p) {
		popup = p;
	}
	
	public void addPopupAction(Action a) {
		popup.addAction(a);
	}
	
	public void showPopup(MouseEvent e) {
		popup.show(e);
	}
	
	@Override
	public void setAutoResizeMode(int mode) {
		super.setAutoResizeMode(AUTO_RESIZE_OFF);
	}
	
	@Override
	public void setSelectionMode(int mode) {
		super.setSelectionMode(SINGLE_SELECTION);
	}
	
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component component = super.prepareRenderer(renderer, row, column);
		int rendererWidth = component.getPreferredSize().width;
		TableColumn tableColumn = getColumnModel().getColumn(column);
		tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width + 20, tableColumn.getPreferredWidth()));
		return component;
	}
}
