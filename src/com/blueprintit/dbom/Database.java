package com.blueprintit.dbom;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

/**
 * @author Dave
 * 
 * The Database is made available automatically to webapps as a request object.
 */
public class Database implements Map
{
	/**
	 * The request used with this database.
	 */
	private ServletRequest request;
	/**
	 * An unmodifiable map of the tables. The key is the tables name.
	 */
	private Map publicTables;
	/**
	 * The database connection this Database should use.
	 */
	private Connection dbConnection;
	
	/**
	 * Initialises the database.
	 * 
	 * @param prototype The prototype for this database.
	 * @param conn A database connection to use.
	 * @param request The ServletRequest to use.
	 */
	Database(DatabasePrototype prototype, Connection conn, ServletRequest request)
	{
		this.request=request;
		dbConnection=conn;
		Map tables = new HashMap();
		publicTables = Collections.unmodifiableMap(tables);
		Iterator loop = prototype.getTablePrototypes().values().iterator();
		while (loop.hasNext())
		{
			Table newtable = new Table(this, (TablePrototype)loop.next());
			tables.put(newtable.getName(),newtable);
		}
	}
	
	/**
	 * Retrieves a table by name.
	 * 
	 * @param name The name of a table.
	 * @return A table or null if there is no table of that name.
	 */
	public Table getTable(String name)
	{
		return (Table)publicTables.get(name);
	}
	
	/**
	 * Retrieves the request for this database.
	 * 
	 * @return The request.
	 */
	ServletRequest getRequest()
	{
		return request;
	}
	
	/**
	 * Throws an UnsupportedOperationException.
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key)
	{
		if (key.equals("tables"))
		{
			return true;
		}
		return false;
	}

	/**
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value)
	{
		if (value==publicTables)
		{
			return true;
		}
		return false;
	}

	/**
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet()
	{
		Set set = new HashSet();
		set.add(new MapEntry("tables",publicTables));
		return set;
	}

	/**
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object key)
	{
		if (key.equals("tables"))
		{
			return publicTables;
		}
		return null;
	}

	/**
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		return false;
	}

	/**
	 * @see java.util.Map#keySet()
	 */
	public Set keySet()
	{
		Set set = new HashSet();
		set.add("tables");
		return set;
	}

	/**
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map map)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return 1;
	}

	/**
	 * @see java.util.Map#values()
	 */
	public Collection values()
	{
		ArrayList list = new ArrayList();
		list.add(publicTables);
		return list;
	}
}
