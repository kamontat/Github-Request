package com.kamontat.controller.loader;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoadProgress implements PropertyChangeListener {
	private ProgressMonitor progressMonitor;
	private Task task;
	
	private boolean done, cancel;
	
	public LoadProgress(Component c, String title, int maximum) {
		progressMonitor = new ProgressMonitor(c, title, "", 0, maximum);
		progressMonitor.setProgress(0);
	}
	
	public void set(String note, int value) {
		progressMonitor.setProgress(value);
		progressMonitor.setNote(note);
	}
	
	public void setTask(final Task task) {
		progressMonitor.setProgress(0);
		this.task = task;
		task.addPropertyChangeListener(this);
		task.execute();
	}
	
	public Task getTask() {
		return task;
	}
	
	public int getMax() {
		return progressMonitor.getMaximum();
	}
	
	public boolean isCancel() {
		return cancel;
	}
	
	public boolean isDone() {
		return done;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		cancel = progressMonitor.isCanceled();
		done = task.isComplete();
	}
}
