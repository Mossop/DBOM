package com.blueprintit.dbom;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Dave
 * 
 * A Record is a single result from a query. It is made up of a number of TableRecords, one for each Table
 * that included results in the query. The TableRecords are generally not used by the end user however.
 */
public class Record implements Map
{
	/**
	 * This field holds a map to the TableRecords, with a Table as the key.
	 */
	private Map tableRecords;
	/**
	 * This holds the primary key for the current record.
	 */
	private Map primaryKey;
	
	/**
	 * Initialises the record from the given ResultSet.
	 * 
	 * @param db The database this result is from.
	 * @param results The ResultSet holding values for the record.
	 * @throws SQLException
	 */
	Record(Database db, ResultSet results) throws SQLException
	{
		tableRecords = new HashMap();
		ResultSetMetaData metadata = results.getMetaData();
		for (int loop=0; loop<metadata.getColumnCount(); loop++)
		{
			Table tb = db.getTable(metadata.getTableName(loop));
			if ((tb!=null)&&(!tableRecords.containsKey(tb)))
			{
				tableRecords.put(tb,(new TableRecord(tb,results,metadata)).intern());
			}
		}
		primaryKey = new HashMap();
		Iterator tableLoop = tableRecords.values().iterator();
		while (tableLoop.hasNext())
		{
			primaryKey.putAll(((TableRecord)tableLoop.next()).getPrimaryKey());
		}
	}
	
	/**
	 * Returns an unmodifiable version of the primary key.
	 * 
	 * @return The primary key.
	 */
	public Map getPrimaryKey()
	{
		return Collections.unmodifiableMap(primaryKey);
	}
	
	/**
	 * Attempts to clear the RecordSet. This will fail with an UnsupportedOperationException.
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	public Set keySet()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object arg0, Object arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map arg0)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	public Collection values()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
