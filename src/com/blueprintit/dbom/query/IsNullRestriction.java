package com.blueprintit.dbom.query;

/**
 * @author Dave
 */
public class IsNullRestriction implements Restriction
{
	private Value value;
	
	public IsNullRestriction(Value val)
	{
		value=val;
	}
	
	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.query.Restriction#getSQL()
	 */
	public String getSQL()
	{
		return "ISNULL("+value.getSQL()+")";
	}

	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.query.Restriction#isEmpty()
	 */
	public boolean isEmpty()
	{
		return false;
	}
}
