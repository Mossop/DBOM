package com.blueprintit.dbom;

import java.util.Set;

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
	private Set primarykey;
	private Set fields;
	private String name;
	
	public Table(Database db, String name)
	{
		this.name=name;
		database=db;
	}
	
	public Set getPrimaryKey()
	{
		return primarykey;
	}
	
	public Set getFields()
	{
		return fields;
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
