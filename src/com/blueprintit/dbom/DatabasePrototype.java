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
	/**
	 * The TablePrototypes for this database are stored in this map with their name as the key.
	 */
	private Map tables;
	/**
	 * The id of the database.
	 */
	private String id;
	/**
	 * The catalog name for the database.
	 */
	private String catalog;
	
	/**
	 * Initialises the prototype.
	 * 
	 * @param id The database id.
	 * @param catalog The database catalog name.
	 */
	public DatabasePrototype(String id, String catalog)
	{
		this.id=id;
		this.catalog=catalog;
		tables = new HashMap();
	}
	
	/**
	 * Return the id.
	 * 
	 * @return The id.
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * Returns the catalog name.
	 * 
	 * @return The catalog name.
	 */
	public String getCatalog()
	{
		return catalog;
	}
	
	/**
	 * Creates an Database instance with the given request and connection.
	 * 
	 * @param request The current ServletRequest.
	 * @param conn A connection to the database.
	 * @return The Database.
	 */
	public Database getDatabaseInstance(ServletRequest request, Connection conn)
	{
		return new Database(this,conn,request);
	}
	
	/**
	 * Adds a TablePrototype.
	 * 
	 * @param table The table to add.
	 */
	public void addTablePrototype(TablePrototype table)
	{
		tables.put(table.getName(),table);
	}
	
	/**
	 * Retrieves a TablePrototype by name.
	 * 
	 * @param name The table name.
	 * @return The TablePrototype or null if there is no table of that name.
	 */
	public TablePrototype getTablePrototype(String name)
	{
		return (TablePrototype)tables.get(name);
	}
	
	/**
	 * Returns an unmodifiable collection of the TablePrototypes.
	 * 
	 * @return The map.
	 */
	public Map getTablePrototypes()
	{
		return Collections.unmodifiableMap(tables);
	}
}
