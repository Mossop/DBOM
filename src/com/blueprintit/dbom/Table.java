package com.blueprintit.dbom;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import javax.servlet.ServletRequest;

/**
 * @author Dave
 * 
 * A Table represents the known information about the table in the database. The table also maintains
 * a cache of previously retrieved records from the database. This means that when the same record is
 * returned from two different queries, the same TableRecord will be returned.
 */
public class Table
{
	private Database database;
	private Set primaryKey;
	private Map fields;
	private String name;
	private Map recordCache;
	
	public Table(Database db, TablePrototype prototype)
	{
		recordCache = new HashMap();
		this.name=prototype.getName();
		database=db;
		fields = new HashMap(prototype.getFields());
		primaryKey = new HashSet(prototype.getPrimaryKeyFields());
	}
	
	ServletRequest getRequest()
	{
		return database.getRequest();
	}
	
	public Set getPrimaryKeyFields()
	{
		return primaryKey;
	}
	
	public Field getField(String name)
	{
		return (Field)fields.get(name);
	}
	
	TableRecord getInternedRecord(TableRecord base)
	{
		if (recordCache.containsKey(base.getPrimaryKey()))
		{
			TableRecord record = (TableRecord)recordCache.get(base.getPrimaryKey());
			record.setInternalValues(base.getInternalValues());
			return record;
		}
		else
		{
			recordCache.put(base.getPrimaryKey(),base);
			return base;
		}
	}
	
	public RecordSet getRecords()
	{
		return null;
	}
	
	public String getName()
	{
		return name;
	}
}
