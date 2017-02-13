package com.kamontat.controller.table;

import com.kamontat.controller.popup.PopupController;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/5/2017 AD - 7:38 PM
 */
public class AutoFitTable<T> extends JTable {
	private PopupController popup;
	public TableInformationModel<T> model;
	
	public AutoFitTable() {
	}
	
	public AutoFitTable(TableInformationModel<T> model) {
		super(model);
	}
	
	public void addPopup(PopupController p) {
		popup = p;
	}
	
	public void addTableAction(Action a) {
		popup.addAction(a);
	}
	
	public void showPopup(MouseEvent e) {
		popup.show(e);
	}
	
	public T getSelectedObject() {
		try {
			return model.getData(getSelectedRow());
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void updateSelected(Point p) {
		setRowSelectionInterval(rowAtPoint(p), rowAtPoint(p));
		setColumnSelectionInterval(columnAtPoint(p), columnAtPoint(p));
	}
	
	@Override
	public void setModel(TableModel dataModel) {
		if (dataModel instanceof TableInformationModel) model = (TableInformationModel<T>) dataModel;
		
		super.setModel(dataModel);
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
