package com.blueprintit.dbom.query;

/**
 * @author Dave
 */
public class IntValue implements Value
{
	private int value;
	
	public IntValue(int val)
	{
		value=val;
	}
	
	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.query.Value#getSQL()
	 */
	public String getSQL()
	{
		return String.valueOf(value);
	}

}
