package com.blueprintit.dbom.query;

import java.util.Set;

import com.blueprintit.dbom.Database;

/**
 * @author Dave
 */
public interface TableReference
{
	public Set getPrimaryKeyFields();
	
	public Database getDatabase();
	
	public String getSQL();
}
