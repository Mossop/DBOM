package com.blueprintit.dbom.query;

import com.blueprintit.dbom.Field;

/**
 * @author Dave
 */
public class FieldValue implements Value
{
	private Field field;
	
	public FieldValue(Field f)
	{
		field=f;
	}
	
	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.query.Value#getSQL()
	 */
	public String getSQL()
	{
		return field.getTableName()+"."+field.getFieldName();
	}

}
