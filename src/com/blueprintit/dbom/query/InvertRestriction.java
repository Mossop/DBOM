package com.blueprintit.dbom.query;

/**
 * @author Dave
 */
public class InvertRestriction implements Restriction
{
	private Restriction restriction;
	
	public InvertRestriction(Restriction r)
	{
		restriction=r;
	}
	
	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.query.Restriction#getSQL()
	 */
	public String getSQL()
	{
		if (!restriction.isEmpty())
		{
			return "NOT "+restriction.getSQL();
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.query.Restriction#isEmpty()
	 */
	public boolean isEmpty()
	{
		return restriction.isEmpty();
	}

}
