package com.blueprintit.dbom;

import java.util.Map.Entry;

/**
 * An implementation of Map.Entry.
 * 
 * @author Dave
 */
public class MapEntry implements Entry
{
	/**
	 * The key of the entry.
	 */
	private Object key;
	/**
	 * The value of the entry.
	 */
	private Object value;
	
	/**
	 * Initialises the entry.
	 * 
	 * @param key The key.
	 * @param value The value.
	 */
	public MapEntry(Object key, Object value)
	{
		this.key=key;
		this.value=value;
	}

	/**
	 * Returns the key.
	 * 
	 * @see java.util.Map.Entry#getKey()
	 */
	public Object getKey()
	{
		return key;
	}

	/**
	 * Returns the value.
	 * 
	 * @see java.util.Map.Entry#getValue()
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * Attempts to set the value in the underlying map. This will throw an UnsupportedOperationException.
	 * 
	 * @see java.util.Map.Entry#setValue(java.lang.Object)
	 */
	public Object setValue(Object arg0)
	{
		throw new UnsupportedOperationException();
	}

}
