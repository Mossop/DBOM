package com.blueprintit.dbom;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import javax.servlet.ServletRequest;

import com.blueprintit.dbom.query.BaseTableReference;
import com.blueprintit.dbom.query.Query;

/**
 * @author Dave
 * 
 * A Table represents the known information about the table in the database. The table also maintains
 * a cache of previously retrieved records from the database. This means that when the same record is
 * returned from two different queries, the same TableRecord will be returned.
 */
public class Table
{
	/**
	 * The database that this table is a part of.
	 */
	private Database database;
	/**
	 * The fields that make up the primary key for this table.
	 */
	private Set primaryKey;
	/**
	 * The fields that exist on this table. The key is the field name.
	 */
	private Map fields;
	/**
	 * The name of this table.
	 */
	private String name;
	/**
	 * A cache of the records retrieved from this table. The key is the primary key of the record.
	 */
	private Map recordCache;
	
	/**
	 * Initialiss the table.
	 * 
	 * @param db The database.
	 * @param prototype The prototype for this table.
	 */
	public Table(Database db, TablePrototype prototype)
	{
		recordCache = new HashMap();
		this.name=prototype.getName();
		database=db;
		fields = new HashMap(prototype.getFields());
		primaryKey = new HashSet(prototype.getPrimaryKeyFields());
	}
	
	/**
	 * Returns the request associated with this table.
	 * 
	 * @return The request.
	 */
	ServletRequest getRequest()
	{
		return database.getRequest();
	}
	
	/**
	 * @return The primary key fields for this table.
	 */
	public Set getPrimaryKeyFields()
	{
		return primaryKey;
	}
	
	/**
	 * Returns a field on this table.
	 * 
	 * @param name the field name.
	 * @return The field or null if the field does not exist.
	 */
	public Field getField(String name)
	{
		return (Field)fields.get(name);
	}
	
	/**
	 * Retrieves the reference to the given record as it appears in the cache.
	 * If the cache does not contain a record with the given primary key then the given record is added
	 * to the cache and returned, otherwise the record in the cache is told the new field values and then returned.
	 * 
	 * @param base The record to find a reference for.
	 * @return The reference from the cache.
	 */
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
	
	/**
	 * Returns a RecordSet representing all records in this table.
	 * 
	 * @return The RecordSet.
	 */
	public RecordSet getRecords()
	{
		return (new Query(new BaseTableReference(this))).results();
	}
	
	/**
	 * @return The name of this table.
	 */
	public String getName()
	{
		return name;
	}
}
