package com.blueprintit.dbom.query;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dave
 */
public abstract class RestrictionSet implements Restriction
{
	private List restrictions;
	
	protected RestrictionSet()
	{
		restrictions = new LinkedList();
	}
	
	public abstract String getSQL(List restrictions);
	
	public RestrictionSet addRestriction(Restriction r)
	{
		restrictions.add(r);
		return this;
	}

	public String getSQL()
	{
		List reallist = new LinkedList();
		Iterator loop = restrictions.iterator();
		while (loop.hasNext())
		{
			Restriction curr = (Restriction)loop.next();
			if (!curr.isEmpty())
			{
				reallist.add(curr);
			}
		}
		if (reallist.isEmpty())
			return "";
		StringBuffer result = new StringBuffer();
		if (reallist.size()>1)
			result.append("(");
		result.append(getSQL(reallist));
		if (reallist.size()>1)
			result.append(")");
		return result.toString();
	}

	public boolean isEmpty()
	{
		if (restrictions.size()==0)
			return true;
		Iterator loop = restrictions.iterator();
		while (loop.hasNext())
		{
			if (!((Restriction)loop.next()).isEmpty())
				return false;
		}
		return true;
	}
}
