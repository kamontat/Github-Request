package com.kamontat.controller.menu;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/8/2017 AD - 10:42 PM
 */
public abstract class MenuUpdateListener implements MenuListener {
	
	public abstract void updateTitle(MenuEvent e);
	
	@Override
	public void menuSelected(MenuEvent e) {
		updateTitle(e);
	}
	
	@Override
	public void menuDeselected(MenuEvent e) {
	}
	
	@Override
	public void menuCanceled(MenuEvent e) {
	}
}
