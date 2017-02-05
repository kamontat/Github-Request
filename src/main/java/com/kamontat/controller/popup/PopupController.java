package com.kamontat.controller.popup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * not Singleton
 *
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 1:12 PM
 */
public class PopupController {
	private JPopupMenu popup;
	
	public static PopupController getInstance() {
		return new PopupController();
	}
	
	private PopupController() {
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
