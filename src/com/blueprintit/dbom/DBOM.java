package com.blueprintit.dbom;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dave
 */
public class DBOM
{
	private Map databases;
	
	public DBOM()
	{
		databases = new HashMap();
	}
	
	public void addDatabase(DatabasePrototype db)
	{
		databases.put(db.getId(),db);
	}
	
	public Map getDatabases()
	{
		return Collections.unmodifiableMap(databases);
	}
}
