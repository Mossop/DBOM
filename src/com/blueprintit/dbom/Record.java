package com.blueprintit.dbom;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
	 * This field holds a map to the TableRecords, with a table name as the key.
	 */
	private Map tableRecords;
	/**
	 * This maps from simple field name to a TableRecord
	 */
	private Map fields;
	
	/**
	 * Initialises the record from the given ResultSet.
	 * 
	 * @param db The database this result is from.
	 * @param results The ResultSet holding values for the record.
	 * @param metadata The ResultSetMetaData holding information about the results.
	 * @throws SQLException
	 */
	Record(Database db, ResultSet results) throws SQLException
	{
		this(db,results,results.getMetaData());
	}

	/**
	 * Initialises the record from the given ResultSet.
	 * 
	 * @param db The database this result is from.
	 * @param results The ResultSet holding values for the record.
	 * @param metadata The ResultSetMetaData holding information about the results.
	 * @throws SQLException
	 */
	Record(Database db, ResultSet results, ResultSetMetaData metadata) throws SQLException
	{
		fields = new HashMap();
		tableRecords = new HashMap();
		for (int loop=0; loop<metadata.getColumnCount(); loop++)
		{
			Table tb = db.getTable(metadata.getTableName(loop));
			if ((tb!=null)&&(!tableRecords.containsKey(tb)))
			{
				TableRecord rec = new TableRecord(tb,results,metadata);
				tableRecords.put(tb.getName(),(new TableRecord(tb,results,metadata)).intern());
				Iterator fieldloop = tb.getFields().iterator();
				while (fieldloop.hasNext())
				{
					Field f = (Field)fieldloop.next();
					if (fields.containsKey(f.getFieldName()))
					{
						fields.put(f.getFieldName(),null);
					}
					else
					{
						fields.put(f.getFieldName(),rec);
					}
				}
			}
		}
	}
	
	/**
	 * Returns the primary key.
	 * 
	 * @return The primary key.
	 */
	public Map getPrimaryKey()
	{
		Map primaryKey = new HashMap();
		Iterator tableLoop = tableRecords.values().iterator();
		while (tableLoop.hasNext())
		{
			primaryKey.putAll(((TableRecord)tableLoop.next()).getPrimaryKey());
		}
		return primaryKey;
	}
	
	/**
	 * Tries to convert the given field name to a simple one, removing the table name as necessary.
	 * 
	 * @param name The name to convert
	 * @return The simple field name.
	 */
	private String getFieldName(String name)
	{
		if (name.indexOf(".")>0)
		{
			return name.substring(name.indexOf(".")+1);
		}
		return name;
	}
	
	/**
	 * Retrieves a TableRecord for a given complex field name.
	 * 
	 * @param fieldname The field name
	 * @return A TableRecord or null if one wasnt found.
	 */
	private TableRecord getTableRecord(String name)
	{
		if (name.indexOf(".")>0)
		{
			name=name.substring(0,name.indexOf("."));
			return (TableRecord)tableRecords.get(name);
		}
		else
		{
			return (TableRecord)fields.get(name);
		}
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
		if (arg0 instanceof Field)
		{
			TableRecord table = (TableRecord)tableRecords.get(((Field)arg0).getTableName());
			if (table!=null)
			{
				return true;
			}
		}
		else if (arg0 instanceof String)
		{
			String field = getFieldName((String)arg0);
			TableRecord table = getTableRecord((String)arg0);
			if (table!=null)
			{
				return (table.getTable().getField(field)!=null);
			}
		}
		return false;
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
		Map map = new HashMap();
		Iterator loop = tableRecords.values().iterator();
		while (loop.hasNext())
		{
			TableRecord tr = (TableRecord)loop.next();
			Iterator fieldloop = tr.getTable().getFields().iterator();
			while (fieldloop.hasNext())
			{
				Field f = (Field)fieldloop.next();
				map.put(f.getTableName()+"."+f.getFieldName(),tr.getFieldValue(f));
			}
		}
		return Collections.unmodifiableMap(map).entrySet();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object arg0)
	{
		if (arg0 instanceof Field)
		{
			TableRecord table = (TableRecord)tableRecords.get(((Field)arg0).getTableName());
			if (table!=null)
			{
				return table.getFieldValue((Field)arg0);
			}
		}
		else if (arg0 instanceof String)
		{
			String field = getFieldName((String)arg0);
			TableRecord table = getTableRecord((String)arg0);
			if (table!=null)
			{
				Field f =  table.getTable().getField(field);
				if (f!=null)
				{
					return table.getFieldValue(f);
				}
			}
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#keySet()
	 */
	public Set keySet()
	{
		Set set = new HashSet();
		Iterator loop = tableRecords.values().iterator();
		while (loop.hasNext())
		{
			TableRecord tr = (TableRecord)loop.next();
			Iterator fieldloop = tr.getTable().getFields().iterator();
			while (fieldloop.hasNext())
			{
				Field f = (Field)fieldloop.next();
				set.add(f.getTableName()+"."+f.getFieldName());
			}
		}
		return set;
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
		int sum = 0;
		Iterator loop = tableRecords.values().iterator();
		while (loop.hasNext())
		{
			TableRecord tr = (TableRecord)loop.next();
			sum+=tr.getTable().getFields().size();
		}
		return sum;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Map#values()
	 */
	public Collection values()
	{
		List list = new LinkedList();
		Iterator loop = tableRecords.values().iterator();
		while (loop.hasNext())
		{
			TableRecord tr = (TableRecord)loop.next();
			Iterator fieldloop = tr.getTable().getFields().iterator();
			while (fieldloop.hasNext())
			{
				Field f = (Field)fieldloop.next();
				list.add(tr.getFieldValue(f));
			}
		}
		return Collections.unmodifiableCollection(list);
	}
}
