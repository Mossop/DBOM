package com.blueprintit.dbom;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

/**
 * @author Dave
 * 
 * The DatabasePrototype contains the base information about the database.
 */
public class DatabasePrototype
{
	private Map tables;
	
	public DatabasePrototype()
	{
		tables = new HashMap();
	}
	
	public Database getDatabaseInstance(ServletRequest request)
	{
		return new Database(this,request);
	}
	
	public void addTablePrototype(TablePrototype table)
	{
		tables.put(table.getName(),table);
	}
	
	public TablePrototype getTablePrototype(String name)
	{
		return (TablePrototype)tables.get(name);
	}
	
	public Map getTablePrototypes()
	{
		return Collections.unmodifiableMap(tables);
	}
}
