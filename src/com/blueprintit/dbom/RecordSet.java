package com.blueprintit.dbom;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	private Set primarykey;
	
	/**
	 * Initialises the RecordSet.
	 */
	RecordSet()
	{
		records = new HashMap();
		retrieved=false;
	}
	
	/**
	 * Converts the object into a compatible index for this RecordSet.
	 * 
	 * @param obj The object used to attempt to index the RecordSet.
	 * @return The obj as converted to a primary key, or null if no conversion was possible.
	 */
	private Map makeKey(Object obj)
	{
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
	 * First checks the cache then attempts to retrieve it if nothing was found.
	 * 
	 * @param okey The index of the record.
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object okey)
	{
		Map key = makeKey(okey);
		if (key==null)
			return false;
		if (records.containsKey(key))
		{
			if (records.get(key)!=null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		if (get(key)!=null)
			return true;
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
