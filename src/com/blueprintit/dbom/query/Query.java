package com.blueprintit.dbom.query;

import java.util.Set;

import com.blueprintit.dbom.Database;
import com.blueprintit.dbom.RecordSet;

/**
 * @author Dave
 */
public class Query
{
	private SelectOptions options;
	private TableReference tables;
	private Restriction restrictions;
	
	public Query(TableReference ref)
	{
		this(ref,null);
	}
	
	public Query(TableReference ref, Restriction rest)
	{
		this(null,ref,rest);
	}
	
	Query(SelectOptions options, TableReference ref, Restriction rest)
	{
		this.options=options;
		tables=ref;
		restrictions=rest;
	}
	
	public Set getPrimaryKeyFields()
	{
		return tables.getPrimaryKeyFields();
	}
	
	public Query getSubset(Restriction newr)
	{
		if ((restrictions==null)||(restrictions.isEmpty()))
		{
			return new Query(options,tables,newr);
		}
		else
		{
			return new Query(options,tables,new AndRestrictionSet(restrictions,newr));
		}
	}
	
	public Database getDatabase()
	{
		return tables.getDatabase();
	}
	
	public RecordSet results()
	{
		return new RecordSet(this);
	}
	
	public String getSQL()
	{
		StringBuffer result = new StringBuffer("SELECT");
		if (options!=null)
		{
			result.append(" ");
			result.append(options.getSQL());
		}
		result.append(" *");
		if (tables!=null)
		{
			result.append(" FROM ");
			result.append(tables.getSQL());
		}
		if ((restrictions!=null)&&(!restrictions.isEmpty()))
		{
			result.append(" WHERE ");
			result.append(restrictions.getSQL());
		}
		result.append(";");
		//System.out.println("Generated query: "+result);
		return result.toString();
	}
	
	public String toString()
	{
		return getSQL();
	}
}
