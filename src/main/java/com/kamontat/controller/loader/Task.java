package com.kamontat.controller.loader;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * update new value by method <code>setProgress()</code>
 *
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 8:07 PM
 */
public abstract class Task extends SwingWorker<Void, Void> implements PropertyChangeListener {
	public static final String PROGRESS = "progress";
	private String label;
	
	public Task() {
		addPropertyChangeListener(this);
	}
	
	public String getLabel() {
		return label == null ? "": label;
	}
	
	public void update(String label, int increaseNumber) {
		this.label = label;
		increaseBy(increaseNumber);
	}
	
	public void increaseBy(int number) {
		setProgress(getProgress() + number);
	}
	
	public boolean isComplete() {
		return isDone();
	}
	
	/**
	 * doing when have some value update <br>
	 * from old value to new value
	 *
	 * @param oldValue
	 * 		old one
	 * @param newValue
	 * 		new old
	 */
	public abstract void whenUpdate(Object oldValue, Object newValue);
	
	/**
	 * very long progress, don't foget to use method <br>
	 * - <code>increaseBy(number)</code> - to increase progress bar by <code>number</code> <br>
	 * - <code>setProgress(number)</code> - to replace current progress to <code>number</code> <br>
	 * - <code>update(label, number)</code> - to assign label or note in progress monitor and <b>increase</b> progress bar by <code>number</code> <br>
	 */
	public abstract void progress();
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(PROGRESS)) whenUpdate(evt.getOldValue(), evt.getNewValue());
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		progress();
		return null;
	}
}
