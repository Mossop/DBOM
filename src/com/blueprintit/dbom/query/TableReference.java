package com.blueprintit.dbom.query;

import java.util.Set;

/**
 * @author Dave
 */
public interface TableReference
{
	public Set getPrimaryKeyFields();
	
	public String getSQL();
}
