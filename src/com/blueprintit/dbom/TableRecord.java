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
	/**
	 * The Table that this is a record from.
	 */
	private Table table;
	/**
	 * The internal values from the database.
	 */
	private Map fieldValues;
	/**
	 * The primary key for this record. Generated from the internal values
	 */
	private Map primaryKey;
	
	/**
	 * Initialises the record from the given ResultSet.
	 * 
	 * @param table The table that this is a record of.
	 * @param values A ResultSet containing values for this record.
	 * @param metadata The metadata for the resultset.
	 * @throws SQLException
	 */
	TableRecord(Table table, ResultSet values, ResultSetMetaData metadata) throws SQLException
	{
		fieldValues = new HashMap();
		this.table=table;
		for (int loop=1; loop<=metadata.getColumnCount(); loop++)
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
	
	/**
	 * Initialises the record from the given ResultSet. Generates the metadata and calls the other
	 * constructor.
	 * 
	 * @param table The table that this is a record of.
	 * @param values A ResultSet containing values for this record.
	 * @throws SQLException
	 */
	TableRecord(Table table, ResultSet values) throws SQLException
	{
		this(table,values,values.getMetaData());
	}
	
	/**
	 * Returns an unmodifiable map holding the values retrieved from the database.
	 * 
	 * @return An unmodifiable map of the database values.
	 */
	Map getInternalValues()
	{
		return Collections.unmodifiableMap(fieldValues);
	}
	
	/**
	 * Updates the internal map of values. Used to notify the object that new information
	 * has been pulled out of the database.
	 * 
	 * @param values The values just taken from the database.
	 */
	void setInternalValues(Map values)
	{
		fieldValues.clear();
		fieldValues.putAll(values);
	}
	
	/**
	 * Returns the table that this is a record from.
	 * 
	 * @return A table.
	 */
	public Table getTable()
	{
		return table;
	}
	
	/**
	 * Retrieve this record from the table cache.
	 * 
	 * @return A copy of this record that exists in the table cache.
	 */
	public TableRecord intern()
	{
		return table.getInternedRecord(this);
	}
	
	/**
	 * Returns the primary key for this record.
	 * 
	 * @return The primary key.
	 */
	public Map getPrimaryKey()
	{
		return Collections.unmodifiableMap(primaryKey);
	}
	
	/**
	 * Returns the internal value for the given field name.
	 * 
	 * @param field The field to retrieve the value for.
	 * @return The object as it was retrieved from the database.
	 */
	public Object getInternalValue(String field)
	{
		return fieldValues.get(field);
	}
	
	/**
	 * Gets a value for the given field. Calls the field to retrieve the value from this record.
	 * 
	 * @param field The field to retrieve tha value of.
	 * @return The returned value.
	 */
	public Object getFieldValue(Field field)
	{
		return field.getValue(this,table.getRequest());
	}
}
