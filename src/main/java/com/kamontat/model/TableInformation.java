package com.kamontat.model;

import java.util.*;

/**
 * this interface use to assign information in table
 *
 * @author kamontat
 * @version 1.0
 * @since 1/31/2017 AD - 1:25 AM
 */
public interface TableInformation<T> {
	
//	@Deprecated
//	abstract Vector<T> getInformationVector();
	
//	@Deprecated
//	abstract Vector<T> getTitleVector();
	public T getRawData();
	
	public Vector<Object> getStringInformationVector();
	
	public Vector<String> getStringTitleVector();
}
