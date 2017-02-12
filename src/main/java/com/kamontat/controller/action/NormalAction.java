package com.kamontat.controller.action;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/12/2017 AD - 7:12 PM
 */
public class NormalAction {
	public static class ExitAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public static class RateLimitAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 2/12/2017 AD 7:15 PM do something
		}
	}
}
