package com.blueprintit.dbom.parser;

import java.sql.Connection;

import org.w3c.dom.Element;

import com.blueprintit.dbom.DatabasePrototype;

/**
 * The database parser is used to do some custom parsing on database elements in the DBOM settings.
 * 
 * Databases are parsed in 2 passes. The first is to establish the existence of all the tables
 * and fields. The second allows references between fields to be created.
 * 
 * @author Dave
 */
public interface DatabaseParser
{
	/**
	 * This method is the first pass through the database element. It should create any tables as
	 * necessary.
	 * 
	 * @param settings The database element to be parsed
	 * @param db A connection to the database
	 * @param dbPrototype The DatabasePrototype for the relevant element
	 */
	public void parseDatabaseElementPass1(Element settings, Connection db, DatabasePrototype dbPrototype);
	
	/**
	 * This method is the second pass through the database element. It should establish any links as
	 * necessary.
	 * 
	 * @param settings The database element to be parsed
	 * @param db A connection to the database
	 * @param dbPrototype The DatabasePrototype for the relevant element
	 */
	public void parseDatabaseElementPass2(Element settings, Connection db, DatabasePrototype dbPrototype);
}
