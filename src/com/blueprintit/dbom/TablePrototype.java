package com.blueprintit.dbom;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dave
 * 
 * The TablePrototype holds the known information about a table.
 */
public class TablePrototype
{
	/**
	 * The name of the table.
	 */
	private String name;
	/**
	 * The database that this table belongs to.
	 */
	private DatabasePrototype database;
	/**
	 * A list of the fields that are available on this table. The key is the field name.
	 */
	private Map fields;
	/**
	 * Holds the fields that mark the primary key for this table.
	 */
	private Set primarykey;
	
	/**
	 * Initialises the prototype.
	 * 
	 * @param name The name of the table that this object describes.
	 */
	public TablePrototype(DatabasePrototype db, String name)
	{
		database=db;
		this.name=name;
		fields = new HashMap();
		primarykey = new HashSet();
	}
	
	/**
	 * @return The name of the table that this object describes.
	 */
	public String getName()
	{
		return name;
	}
	
	public DatabasePrototype getDatabasePrototype()
	{
		return database;
	}
	
	/**
	 * @return The primary key for this table.
	 */
	public Set getPrimaryKeyFields()
	{
		return Collections.unmodifiableSet(primarykey);
	}
	
	/**
	 * Marks the given field name as being part of the primary key.
	 * 
	 * @param name The name of the field that already exists on the table.
	 */
	public void markAsKeyField(String name)
	{
		Object field = fields.get(name);
		if (field!=null)
		{
			primarykey.add(field);
		}
	}
	
	/**
	 * Adds a field to this table.
	 * 
	 * @param field the Field
	 */
	public void addField(Field field)
	{
		fields.put(field.getFieldName(),field);
	}
	
	/**
	 * Returns an unmodifiable map of the fields that this table has.
	 * 
	 * @return The map of fields.
	 */
	public Map getFields()
	{
		return Collections.unmodifiableMap(fields);
	}
}
