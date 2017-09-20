package com.kamontat.constant;

import org.apache.commons.lang.SystemUtils;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @author kamontat
 * @version 1.0
 * @since 1/7/2017 AD - 12:25 AM
 */
public class HotKey {
	private static int CTRL = SystemUtils.IS_OS_MAC ? InputEvent.META_MASK: InputEvent.CTRL_MASK;
	private static int ALT = InputEvent.ALT_MASK;
	
	
	public static HotKey EXIT = new HotKey("Exit", "Exit the program", "Everywhere", "Every time", getKey(KeyEvent.VK_ESCAPE, CTRL));
	public static HotKey LOAD_CACHE = new HotKey("Load Cache", "Loading token com.kamontat.cache with password", "login page", "Every time", getKey(KeyEvent.VK_C, CTRL));
	
	private String name;
	private String description, position, when;
	private KeyStroke keyStroke;
	
	public HotKey(String name) {
		this.name = name;
	}
	
	public HotKey(String name, KeyStroke keyStroke) {
		this.name = name;
		this.keyStroke = keyStroke;
	}
	
	public HotKey(String name, String description, KeyStroke keyStroke) {
		this.name = name;
		this.description = description;
		this.keyStroke = keyStroke;
	}
	
	private HotKey(String name, String description, String position, String when, KeyStroke keyStroke) {
		this.name = name;
		this.description = description;
		this.position = position;
		this.when = when;
		this.keyStroke = keyStroke;
	}
	
	private static KeyStroke getKey(int code, int modify) {
		return KeyStroke.getKeyStroke(code, modify);
	}
	
	private static KeyStroke getKey(int code) {
		return KeyStroke.getKeyStroke(code, 0);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description == null ? "": description;
	}
	
	public String getPosition() {
		return position == null ? "": position;
	}
	
	public String getWhen() {
		return when == null ? "": when;
	}
	
	public KeyStroke getKeyStroke() {
		return keyStroke;
	}
	
	public String getKeyString() {
		return toString().replace(" pressed ", "+").replace("pressed ", "").replace("meta", "cmd").toUpperCase();
	}
	
	public String[] getAll() {
		return new String[]{name, description, position, when, getKeyString()};
	}
}