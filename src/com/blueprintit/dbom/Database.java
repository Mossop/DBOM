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
 * The Database is the root object of the DBOM. It is made available
 * automatically to webapps as a request object.
 */
public class Database implements Map
{
	private ServletRequest request;
	private Map publicTables;
	private Connection dbConnection;
	
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
	
	public Table getTable(String name)
	{
		return (Table)publicTables.get(name);
	}
	
	ServletRequest getRequest()
	{
		return request;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet()
	{
		Set set = new HashSet();
		set.add(new MapEntry("tables",publicTables));
		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	public Set keySet()
	{
		Set set = new HashSet();
		set.add("tables");
		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map map)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key)
	{
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	public Collection values()
	{
		ArrayList list = new ArrayList();
		list.add(publicTables);
		return list;
	}
}
