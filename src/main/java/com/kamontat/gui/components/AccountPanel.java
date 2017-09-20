package com.kamontat.gui.components;

import com.kamontat.model.github.GHAccount;

import java.awt.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/13/2017 AD - 9:21 PM
 */
public class AccountPanel extends Panel {
	public AccountPanel(GHAccount account) {
		setLayout(new BorderLayout());
		add(new Label(account.user.getName()), BorderLayout.NORTH);
	}
}
