package com.blueprintit.dbom.query;

import java.util.HashSet;
import java.util.Set;

import com.blueprintit.dbom.Database;

/**
 * @author Dave
 */
public class Join implements TableReference
{
	private String type;
	private TableReference left;
	private TableReference right;
	private Restriction condition;
	
	public Join(String type, TableReference left, TableReference right)
	{
		this(type,left,right,null);
	}
	
	public Join(String type, TableReference left, TableReference right, Restriction condition)
	{
		if (left.getDatabase()==right.getDatabase())
		{
			this.type=type;
			this.left=left;
			this.right=right;
			this.condition=condition;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	
	public Database getDatabase()
	{
		return left.getDatabase();
	}
	
	public Set getPrimaryKeyFields()
	{
		Set key = new HashSet();
		key.addAll(left.getPrimaryKeyFields());
		key.addAll(right.getPrimaryKeyFields());
		return key;
	}
	
	public String getSQL()
	{
		StringBuffer result = new StringBuffer();
		result.append("(");
		result.append(left.getSQL());
		result.append(" ");
		result.append(right.getSQL());
		if ((condition!=null)&&(!condition.isEmpty()))
		{
			result.append(" ON ");
			result.append(condition.getSQL());
		}
		result.append(")");
		return result.toString();
	}

}
