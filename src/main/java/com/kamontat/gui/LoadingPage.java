package com.kamontat.gui;

import javax.swing.*;
import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 15:10 PM
 */
public class LoadingPage extends JDialog implements Observer {
	private JPanel contentPane;
	private JProgressBar progressBar1;
	private JLabel textLb;
	
	public LoadingPage() {
		setContentPane(contentPane);
		setModal(true);
	}
	
	public static void showPage() {
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
	}
}
