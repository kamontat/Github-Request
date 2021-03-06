package com.kamontat.model.management;

import java.awt.*;

/**
 * with 2 method make the class became screen location api
 * <p>
 * this class contain some of method but all of them is <code>static method</code> so you don't need to assign everything to use it
 * <ol>
 * <li>{@link #getCenterLocation(Dimension)}</li>
 * <li>{@link #getCenterPage(Window, Window)}</li>
 * </ol>
 *
 * @author kamontat
 * @version 4.1
 * @since 17/8/59 - 23:54
 */
public class Location {
	
	/**
	 * get default point at x=0, y=0
	 */
	public static Point getDefaultPoint = new Point(0, 0);
	
	/**
	 * get point that stay in the center of the screen
	 *
	 * @param pageSize
	 * 		size of page that want to show in the center
	 * @return point of center screen (you can set in <code>setLocation()</code> directly)
	 */
	public static Point getCenterLocation(Dimension pageSize) {
		return new Point((int) ((Size.getScreenSize().getWidth() / 2) - (pageSize.getWidth() / 2)), (int) ((Size.getScreenSize().getHeight() / 2) - (pageSize.getHeight() / 2)));
	}
	
	/**
	 * get point to show page at center of the old page <br>
	 * if don't have old page (oldPage is <code>null</code>) this method will return center of the screen
	 *
	 * @param oldPage
	 * 		current page
	 * @param newPage
	 * 		new page that want to show
	 * @return point (you can set in <code>setLocation()</code> directly)
	 */
	public static Point getCenterPage(Window oldPage, Window newPage) {
		if (oldPage == null) {
			return getCenterLocation(newPage.getSize());
		}
		Point currPoint = oldPage.getLocation();
		
		int newX = (currPoint.x + (oldPage.getWidth() / 2)) - (newPage.getWidth() / 2);
		int newY = (currPoint.y + (oldPage.getHeight() / 2)) - (newPage.getHeight() / 2);
		
		return new Point(newX, newY);
	}
}
