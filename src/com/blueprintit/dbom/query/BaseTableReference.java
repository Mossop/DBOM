package com.blueprintit.dbom.query;

import com.blueprintit.dbom.TablePrototype;

/**
 * @author Dave
 */
public class BaseTableReference implements TableReference
{
	private TablePrototype table;

	public BaseTableReference(TablePrototype table)
	{
		this.table=table;
	}
	
	public String getSQL()
	{
		return table.getName();
	}

}
