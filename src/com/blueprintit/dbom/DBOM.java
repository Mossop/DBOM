package com.blueprintit.dbom;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * DBOM is the root object on hte object model.
 * 
 * @author Dave
 */
public class DBOM
{
	/**
	 * The databases that the model is configured for. The key is the database id.
	 */
	private Map databases;
	
	/**
	 * Initialises the fields.
	 */
	public DBOM()
	{
		databases = new HashMap();
	}
	
	/**
	 * Adds a DatabasePrototype to the object model.
	 * 
	 * @param db The prototype.
	 */
	public void addDatabasePrototype(DatabasePrototype db)
	{
		databases.put(db.getId(),db);
	}
	
	/**
	 * Returns an unmodifiable map of the DatabasePrototypes.
	 * 
	 * @return The map.
	 */
	public Map getDatabasePrototypes()
	{
		return Collections.unmodifiableMap(databases);
	}
}
