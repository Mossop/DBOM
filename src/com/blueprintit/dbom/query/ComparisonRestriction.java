package com.blueprintit.dbom.query;

/**
 * @author Dave
 */
public class ComparisonRestriction implements Restriction
{
	private Value value1;
	private Value value2;
	private String type;
	
	public ComparisonRestriction(String type, Value val1, Value val2)
	{
		this.type=type;
		value1=val1;
		value2=val2;
	}
	
	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.query.Restriction#getSQL()
	 */
	public String getSQL()
	{
		return value1.getSQL()+" "+type+" "+value2.getSQL();
	}

	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.query.Restriction#isEmpty()
	 */
	public boolean isEmpty()
	{
		return false;
	}

}
