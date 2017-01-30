package com.kamontat.controller;

import com.kamontat.model.TableInformation;

import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 1:21 AM
 */
public class UserTableModel extends DefaultTableModel {
	
	public UserTableModel(Vector columnNames) {
		super(columnNames, 0);
	}
	
	public UserTableModel(TableInformation info) {
		super(getVVString(info.getStringInformationVector()), info.getStringTitleVector());
	}
	
	private static Vector<Vector<String>> getVVString(Vector<String> vs) {
		Vector<Vector<String>> v = new Vector<>(1);
		v.add(vs);
		return v;
	}
	
	public void addRow(TableInformation info) {
		// TODO 1/31/2017 AD 4:22 AM check duplicate info
		super.addRow(info.getStringInformationVector());
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
