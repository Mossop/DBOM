package com.blueprintit.dbom.query;

/**
 * @author Dave
 */
public interface SelectOptions
{
	public SelectOptions OPTIONS_DISTINCT = new SelectOptions() {
		public String getSQL()
		{
			return "DISTINCT";
		}
	};
	
	public String getSQL();
}
