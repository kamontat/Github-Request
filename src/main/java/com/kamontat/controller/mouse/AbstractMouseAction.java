package com.kamontat.controller.mouse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/5/2017 AD - 7:25 PM
 */
public abstract class AbstractMouseAction extends MouseAdapter {
	
	public void doubleClick(MouseEvent e) {
	}
	
	public void tripleClick(MouseEvent e) {
	}
	
	public void rightClick(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getClickCount() == 2) doubleClick(e);
		if (e.getClickCount() == 3) tripleClick(e);
		if (e.getButton() == MouseEvent.BUTTON3) rightClick(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) rightClick(e);
	}
}
