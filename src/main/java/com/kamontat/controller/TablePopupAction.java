package com.kamontat.controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 1:12 PM
 */
public class TablePopupAction {
	private JPopupMenu popup;
	private static TablePopupAction tpa;
	
	public static TablePopupAction getInstance() {
		if (tpa == null) tpa = new TablePopupAction();
		return tpa;
	}
	
	private TablePopupAction() {
		popup = new JPopupMenu();
	}
	
	public JPopupMenu getPopup() {
		return popup;
	}
	
	public void addAction(Action action) {
		popup.add(action);
	}
	
	public void show(Component c, int x, int y) {
		popup.show(c, x, y);
	}
	
	public void show(MouseEvent e) {
		popup.show(e.getComponent(), e.getX(), e.getY());
	}
}
