package com.blueprintit.dbom.query;

import com.blueprintit.dbom.Field;

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
		else if (value instanceof Field)
		{
			Field field = (Field)value;
			return field.getTableName()+"."+field.getFieldName();
		}
		else
		{
			return value.toString();
		}
	}

}
