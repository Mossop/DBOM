package com.blueprintit.dbom.parser;

import java.sql.Connection;

import org.w3c.dom.Element;

import com.blueprintit.dbom.DatabasePrototype;
import com.blueprintit.dbom.TablePrototype;

/**
 * @author Dave
 */
public interface ElementParser
{
	public String[] getParseableElements();
	
	public boolean parseDatabaseChildElementPass1(Element element, Connection db, DatabasePrototype dbPrototype);
	
	public boolean parseDatabaseChildElementPass2(Element element, Connection db, DatabasePrototype dbPrototype);
	
	public boolean parseTableChildElementPass1(Element element, Connection db, TablePrototype tablePrototype);
	
	public boolean parseTableChildElementPass2(Element element, Connection db, TablePrototype tablePrototype);
}
