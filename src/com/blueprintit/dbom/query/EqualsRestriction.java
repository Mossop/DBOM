package com.blueprintit.dbom.query;

/**
 * @author Dave
 */
public class EqualsRestriction extends ComparisonRestriction
{
	public EqualsRestriction(Value val1, Value val2)
	{
		super("=",val1,val2);
	}
}
