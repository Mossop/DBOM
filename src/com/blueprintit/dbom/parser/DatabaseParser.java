package com.blueprintit.dbom.parser;

import java.sql.Connection;

import org.w3c.dom.Element;

import com.blueprintit.dbom.DatabasePrototype;

/**
 * @author Dave
 */
public interface DatabaseParser
{
	public void parseDatabaseElementPass1(Element settings, Connection db, DatabasePrototype dbPrototype);
	
	public void parseDatabaseElementPass2(Element settings, Connection db, DatabasePrototype dbPrototype);
}
