package com.blueprintit.dbom;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Dave
 * 
 * A TableRecord represents a full record out of a single table. This class is generally not used
 * by the end user and may not be public in the final release.
 */
public class TableRecord
{
	private Table table;
	private Map fieldvalues;
	
	TableRecord(Table table, ResultSet values, ResultSetMetaData metadata) throws SQLException
	{
		this.table=table;
		for (int loop=0; loop<metadata.getColumnCount(); loop++)
		{
			if (table.getName().equals(metadata.getTableName(loop)))
			{
				fieldvalues.put(metadata.getColumnName(loop),values.getObject(loop));
			}
		}
	}
	
	TableRecord(Table table, ResultSet values) throws SQLException
	{
		this(table,values,values.getMetaData());
	}
	
	public Object getInternalValue(String field)
	{
		return fieldvalues.get(field);
	}
	
	public Object getFieldValue(Field field)
	{
		return field.getValue(this,table.getRequest());
	}
}
