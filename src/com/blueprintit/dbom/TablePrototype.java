package com.blueprintit.dbom;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dave
 * 
 * The TablePrototype holds the known information about a table.
 */
public class TablePrototype
{
	private String name;
	private Set fields;
	private Set primarykey;
	
	public TablePrototype(String name)
	{
		this.name=name;
		fields = new HashSet();
		primarykey = new HashSet();
	}
	
	public String getName()
	{
		return name;
	}
	
	public Set getPrimaryKeyFields()
	{
		return Collections.unmodifiableSet(primarykey);
	}
	
	public Set getFields()
	{
		return Collections.unmodifiableSet(fields);
	}
}
