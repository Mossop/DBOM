package com.blueprintit.dbom.query;

/**
 * @author Dave
 */
public interface Restriction
{
	public String getSQL();
	
	public boolean isEmpty();
}
