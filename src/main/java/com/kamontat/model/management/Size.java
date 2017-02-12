package com.kamontat.model.management;

import java.awt.*;

/**
 * Size api
 * <p>
 * this class contain some of method but all of them is <code>static method</code> so you don't need to assign everything to use it
 * <ul>
 * <li>{@link #getDefaultPageSize()}</li>
 * <li>{@link #getScreenSize()}</li>
 * <li>{@link #isBiggerThanScreen(Dimension)}</li>
 * <li>{@link #maximum(Dimension, Dimension)}</li>
 * </ul>
 *
 * @author kamontat
 * @version 1.2
 * @since 1/30/2017 AD - 1:47 PM
 */
public class Size {
	/**
	 * Screen information <br>
	 * - get size <code>screen.getWidth</code> and <code>screen.getHeight</code>
	 */
	private static DisplayMode screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	
	/**
	 * get Screen size
	 *
	 * @return screen size
	 */
	public static Dimension getScreenSize() {
		return new Dimension(screen.getWidth(), screen.getHeight());
	}
	
	/**
	 * get default/normally page size, (871, 557)
	 *
	 * @return default size
	 */
	public static Dimension getDefaultPageSize() {
		return new Dimension(871, 557);
	}
	
	/**
	 * test than pageSize is bigger than screen (computer monitor) or not
	 *
	 * @param pageSize
	 * 		page size (if you fixed size)
	 * @return true if bigger than, otherwise will return false
	 */
	public static boolean isBiggerThanScreen(Dimension pageSize) {
		return !(pageSize.getWidth() < screen.getWidth() && pageSize.getHeight() < screen.getHeight());
	}
	
	/**
	 * return the minimum width and height in a and b <br>
	 * example: a = (200, 400), b = (500, 100) <br>
	 * the result will be (500, 200)
	 *
	 * @param a
	 * 		first size
	 * @param b
	 * 		second size
	 * @return the minimum size
	 */
	public static Dimension maximum(Dimension a, Dimension b) {
		Dimension result = new Dimension();
		if (a.getWidth() > b.getWidth()) result.setSize(a.getWidth(), result.getHeight());
		else result.setSize(b.getWidth(), result.getHeight());
		if (a.getHeight() > b.getHeight()) result.setSize(result.getWidth(), a.getHeight());
		else result.setSize(result.getWidth(), b.getHeight());
		return result;
	}
}
