package com.blueprintit.dbom.query;

import java.util.Iterator;
import java.util.List;

/**
 * @author Dave
 */
public class AndRestrictionSet extends RestrictionSet
{
	public AndRestrictionSet()
	{
		super();
	}
	
	public AndRestrictionSet(Restriction r1)
	{
		this();
		addRestriction(r1);
	}
	
	public AndRestrictionSet(Restriction r1, Restriction r2)
	{
		this();
		addRestriction(r1);
		addRestriction(r2);
	}
	
	public AndRestrictionSet(Restriction r1, Restriction r2, Restriction r3)
	{
		this();
		addRestriction(r1);
		addRestriction(r2);
		addRestriction(r3);
	}
	
	public AndRestrictionSet(Restriction r1, Restriction r2, Restriction r3, Restriction r4)
	{
		this();
		addRestriction(r1);
		addRestriction(r2);
		addRestriction(r3);
		addRestriction(r4);
	}
	
	public String getSQL(List restrictions)
	{
		StringBuffer result = new StringBuffer();
		Iterator loop = restrictions.iterator();
		while (loop.hasNext())
		{
			Restriction r = (Restriction)loop.next();
			result.append(r.getSQL());
			if (loop.hasNext())
			{
				result.append(" AND ");
			}
		}
		return result.toString();
	}
}
