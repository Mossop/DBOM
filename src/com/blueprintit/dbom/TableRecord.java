package com.blueprintit.dbom;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
	private Map fieldValues;
	private Map primaryKey;
	
	TableRecord(Table table, ResultSet values, ResultSetMetaData metadata) throws SQLException
	{
		fieldValues = new HashMap();
		this.table=table;
		for (int loop=0; loop<metadata.getColumnCount(); loop++)
		{
			if (table.getName().equals(metadata.getTableName(loop)))
			{
				fieldValues.put(metadata.getColumnName(loop),values.getObject(loop));
			}
		}
		primaryKey = new HashMap();
		Iterator keyloop = table.getPrimaryKeyFields().iterator();
		while (keyloop.hasNext())
		{
			Field field = (Field)keyloop.next();
			if (fieldValues.containsKey(field.getFieldName()))
			{
				primaryKey.put(field,fieldValues.get(field.getFieldName()));
			}
		}
	}
	
	TableRecord(Table table, ResultSet values) throws SQLException
	{
		this(table,values,values.getMetaData());
	}
	
	Map getInternalValues()
	{
		return Collections.unmodifiableMap(fieldValues);
	}
	
	void setInternalValues(Map values)
	{
		fieldValues.clear();
		fieldValues.putAll(values);
	}
	
	public TableRecord intern()
	{
		return table.getInternedRecord(this);
	}
	
	public Map getPrimaryKey()
	{
		return Collections.unmodifiableMap(primaryKey);
	}
	
	public Object getInternalValue(String field)
	{
		return fieldValues.get(field);
	}
	
	public Object getFieldValue(Field field)
	{
		return field.getValue(this,table.getRequest());
	}
}
