package com.blueprintit.dbom.parser;

import java.sql.Connection;

import org.w3c.dom.Element;

import com.blueprintit.dbom.TablePrototype;

/**
 * The table parser is used to do some custom parsing on table elements in the DBOM settings.
 * 
 * Databases are parsed in 2 passes. The first is to establish the existence of all the tables
 * and fields. The second allows references between fields to be created.
 * 
 * @author Dave
 */
public interface TableParser extends Parser
{
	/**
	 * This method is the first pass through the table element. It should create any fields as
	 * necessary.
	 * 
	 * @param settings The table element to be parsed
	 * @param db A connection to the database
	 * @param tablePrototype The TablePrototype for the relevant element
	 */
	public void parseTableElementPass1(Element settings, Connection db, TablePrototype tablePrototype);
	
	/**
	 * This method is the second pass through the table element. It should establish any links
	 * as necessary.
	 * 
	 * @param settings The table element to be parsed
	 * @param db A connection to the database
	 * @param tablePrototype The TablePrototype for the relevant element
	 */
	public void parseTableElementPass2(Element settings, Connection db, TablePrototype tablePrototype);
}
