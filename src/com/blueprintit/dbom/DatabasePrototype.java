package com.blueprintit.dbom;

import java.sql.Connection;
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
	private String id;
	private String catalog;
	
	public DatabasePrototype(String id, String catalog)
	{
		this.id=id;
		this.catalog=catalog;
		tables = new HashMap();
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getCatalog()
	{
		return catalog;
	}
	
	public Database getDatabaseInstance(ServletRequest request, Connection conn)
	{
		return new Database(this,conn,request);
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
