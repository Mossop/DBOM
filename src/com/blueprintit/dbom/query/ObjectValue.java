package com.blueprintit.dbom.query;

/**
 * @author Dave
 */
public class ObjectValue implements Value
{
	private Object value;
	
	public ObjectValue(Object val)
	{
		value=val;
	}
	
	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.query.Value#getSQL()
	 */
	public String getSQL()
	{
		if (value instanceof String)
		{
			return StringValue.escape(value.toString());
		}
		else
		{
			return value.toString();
		}
	}

}
