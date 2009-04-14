package com.maciejgryka.fyproject;

public abstract class XmlDataObject {

	public abstract String getAttribute(String attribute);
	public abstract boolean setAttribute(String attribute, String value);
	public abstract void clear();
	
}
