package com.blueprintit.dbom.query;

import java.util.Set;

import com.blueprintit.dbom.Database;
import com.blueprintit.dbom.Table;

/**
 * @author Dave
 */
public class BaseTableReference implements TableReference
{
	private Table table;

	public BaseTableReference(Table table)
	{
		this.table=table;
	}

	public Database getDatabase()
	{
		return table.getDatabase();
	}
	
	public Set getPrimaryKeyFields()
	{
		return table.getPrimaryKeyFields();
	}
	
	public String getSQL()
	{
		return table.getName();
	}

}
