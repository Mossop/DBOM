package com.blueprintit.dbom;

import java.util.Map.Entry;

/**
 * @author Dave
 */
public class MapEntry implements Entry
{
	private Object key;
	private Object value;
	
	public MapEntry(Object key, Object value)
	{
		this.key=key;
		this.value=value;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#getKey()
	 */
	public Object getKey()
	{
		return key;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#getValue()
	 */
	public Object getValue()
	{
		return value;
	}

	/* (non-Javadoc)
	 * @see java.util.Map.Entry#setValue(java.lang.Object)
	 */
	public Object setValue(Object arg0)
	{
		throw new UnsupportedOperationException();
	}

}
