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
	public TablePrototype(String name)
	{
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
	
	/**
	 * @return The primary key for this table.
	 */
	public Set getPrimaryKeyFields()
	{
		return Collections.unmodifiableSet(primarykey);
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
