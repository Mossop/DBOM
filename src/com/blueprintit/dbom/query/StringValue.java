package com.blueprintit.dbom.query;

/**
 * @author Dave
 */
public class StringValue implements Value
{
	private String value;
	
	public StringValue(String val)
	{
		value=val;
	}
	
	public static String escape(String value)
	{
		value=value.replaceAll("\\","\\\\");
		value=value.replaceAll("'","\\'");
		value=value.replaceAll("\"","\\\"");
		value=value.replaceAll("\0","\\\0");
		return "'"+value+"'";
	}
	
	public static String unEscape(String value)
	{
		value=value.substring(1,value.length()-1);
		value=value.replaceAll("\\\\","\\");
		value=value.replaceAll("\\'","'");
		value=value.replaceAll("\\\"","\"");
		value=value.replaceAll("\\\0","\0");
		return value;
	}
	
	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.query.Value#getSQL()
	 */
	public String getSQL()
	{
		return escape(value);
	}

}
