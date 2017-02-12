package com.kamontat.controller.menu;

import com.kamontat.constant.HotKey;
import com.kamontat.controller.action.NormalAction;

import javax.swing.*;
import java.util.*;

/**
 * @author kamontat
 * @version 1.0
 * @since 2/8/2017 AD - 10:40 PM
 */
public class MenuBarController extends JMenuBar {
	public MenuBarController(Menu[] lefts, Menu[] rights) {
		if (lefts != null) for (Menu left : lefts) {
			add(left);
		}
		
		setToRight();
		if (rights != null) for (Menu right : rights) {
			add(right);
		}
	}
	
	private Menu add(Menu m) {
		return (Menu) super.add(m);
	}
	
	private void setToRight() {
		add(Box.createHorizontalGlue());
	}
	
	public static class Menu extends JMenu {
		HashMap<String, Item> items;
		
		public Menu(String title) {
			super(title);
			items = new HashMap<String, Item>();
		}
		
		public void addItem(Item menuItem) {
			items.put(menuItem.getText(), menuItem);
			add(menuItem);
		}
		
		public void addItem(Item[] menuItems) {
			for (Item i : menuItems) {
				items.put(i.getText(), i);
				add(i);
			}
		}
		
		public Item getItem(String text) {
			return items.get(text);
		}
		
		public boolean isContain(String text) {
			return items.containsKey(text);
		}
		
		@Override
		public void addSeparator() {
			super.addSeparator();
		}
		
		public static Menu[] toArray(Menu... menus) {
			return menus;
		}
		
		public void addMenuUpdateListener(MenuUpdateListener l) {
			super.addMenuListener(l);
		}
		
		/**
		 * how to use custom menu item
		 * <pre><code>
		 * Menu.addItem(new MenuBarController.Menu.Item(new HotKey("test"), new AbstractAction() {
		 *      public void actionPerformed(ActionEvent e) {
		 *          // some code
		 *      }
		 * }));
		 * </code></pre>
		 */
		public static class Item extends JMenuItem {
			public Item(HotKey key, Action a) {
				super(key.getName());
				addActionListener(a);
				setAccelerator(key.getKeyStroke());
				setToolTipText(key.getDescription());
			}
			
			public Item(String text, Action a) {
				super(text);
				addActionListener(a);
			}
			
			public Item(String text) {
				super(text);
			}
			
			public void setText(String text) {
				super.setText(text);
			}
			
			public static Item getExitMenu() {
				return new Item(HotKey.EXIT, new NormalAction.ExitAction());
			}
		}
	}
}
