package com.kamontat.controller.table;

import com.kamontat.controller.popup.Popup;
import com.kamontat.model.github.TableInformation;

import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 1:21 AM
 */
public class TableInformationModel<T> extends DefaultTableModel {
	private Class data;
	public ArrayList<T> rawData = new ArrayList<T>();
	
	public TableInformationModel(Vector columnNames) {
		super(columnNames, 0);
	}
	
	public TableInformationModel(TableInformation<T> info) {
		super(getVVString(info.getStringInformationVector()), info.getStringTitleVector());
		rawData.add(info.getRawData());
	}
	
	private static Vector<Vector<Object>> getVVString(Vector<Object> vs) {
		Vector<Vector<Object>> v = new Vector<Vector<Object>>(1);
		v.add(vs);
		return v;
	}
	
	public void addRow(TableInformation<T> info) {
		if (!rawData.contains(info.getRawData())) {
			super.addRow(info.getStringInformationVector());
			rawData.add(info.getRawData());
		} else {
			Popup.getLog(null).warning("Duplicate Data", info.getName() + " already add to table");
		}
	}
	
	public void deleteRow(int row) {
		super.removeRow(row);
		rawData.remove(row);
	}
	
	public void deleteAll() {
		setRowCount(0);
		
		rawData.removeAll(rawData);
	}
	
	public T getData(int row) {
		return rawData.get(row);
	}
	
	public ArrayList<T> getAllData() {
		return new ArrayList<T>(rawData);
	}
	
	public int size() {
		return rawData.size();
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
