package com.blueprintit.dbom;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.blueprintit.dbom.query.Query;

/**
 * @author Dave
 * 
 * A RecordSet constitutes the result of a query on the database. It is made up of a number of
 * Records mapped to a primary key for the record.
 */
public class RecordSet implements Map
{
	/**
	 * The records held. The key is the primary key of the record. If the value is null then the record is
	 * known not exist in the database.
	 */
	private Map records;
	/**
	 * Indicates whether a full retrieve has been done on the RecordSet.
	 */
	private boolean retrieved;
	/**
	 * The query that is used to retrieve records from the database.
	 */
	private Query query;
	
	/**
	 * Creates a RecordSet with the given database query as its base.
	 * 
	 * @param query
	 */
	public RecordSet(Query query)
	{
		records = new HashMap();
		retrieved=false;
		this.query=query;
	}

	/**
	 * Converts the object into a compatible index for this RecordSet.
	 * 
	 * @param obj The object used to attempt to index the RecordSet.
	 * @return The obj as converted to a primary key, or null if no conversion was possible.
	 */
	private Map makeKey(Object obj)
	{
		Set primarykey = query.getPrimaryKeyFields();
		if (obj instanceof Map)
		{
			if (((Map)obj).keySet().equals(primarykey))
			{
				return (Map)obj;
			}
			else
			{
				return null;
			}
		}
		else
		{
			if (primarykey.size()==1)
			{
				Map key = new HashMap();
				key.put(primarykey.iterator().next(),obj);
				return key;
			}
			else
			{
				return null;
			}
		}
	}
	
	/**
	 * Attempts to retrieve all the remaining records from the database.
	 */
	private void retrieveRecords()
	{
		if (!retrieved)
		{
			Iterator loop = records.entrySet().iterator();
			while (loop.hasNext())
			{
				Map.Entry entry = (Map.Entry)loop.next();
				if (entry.getValue()==null)
				{
					loop.remove();
				}
			}
			try
			{
				Statement stmt = query.getDatabase().getConnection().createStatement();
				ResultSet results = stmt.executeQuery(query.getSQL());
				ResultSetMetaData metadata = results.getMetaData();
				while (results.next())
				{
					Record record = new Record(query.getDatabase(),results,metadata);
					records.put(record.getPrimaryKey(),record);
				}
				results.close();
				stmt.close();
			}
			catch (SQLException e)
			{
			}
			retrieved=true;
		}
	}
	
	/**
	 * Attempts to clear the RecordsSet. Will fail with an UnsupportedOperationException.
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Attempts to determine if a particular record exists in the RecordSet.
	 * 
	 * @param okey The index of the record.
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object okey)
	{
		Map key = makeKey(okey);
		if (key==null)
			return false;
		retrieveRecords();
		if (records.containsKey(key))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object arg0)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet()
	{
		retrieveRecords();
		return Collections.unmodifiableSet(records.entrySet());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object okey)
	{
		Map key = makeKey(okey);
		if (key==null)
			return null;
		retrieveRecords();
		return records.get(key);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		retrieveRecords();
		return records.isEmpty();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	public Set keySet()
	{
		retrieveRecords();
		return Collections.unmodifiableSet(records.keySet());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object arg0, Object arg1)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map arg0)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object arg0)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		retrieveRecords();
		return records.size();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	public Collection values()
	{
		retrieveRecords();
		return Collections.unmodifiableCollection(records.values());
	}
}
