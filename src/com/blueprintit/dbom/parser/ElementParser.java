package com.blueprintit.dbom.parser;

import java.sql.Connection;

import org.w3c.dom.Element;

import com.blueprintit.dbom.DatabasePrototype;
import com.blueprintit.dbom.TablePrototype;

/**
 * The ElementParser is designed to parse custom elements in the settings file. These can be as
 * children to the database or table elements.
 * 
 * @author Dave
 */
public interface ElementParser
{
	/**
	 * This method returns the tag names that this parser should be used to parse.
	 * 
	 * @return An array of tag names that this parser will parse
	 */
	public String[] getParseableElements();
	
	/**
	 * This method is called during the first pass to parse an element that is a child of the database element.
	 * 
	 * @param element The element to be processed.
	 * @param db A connection to the database.
	 * @param dbPrototype The database prototype to be updated.
	 * @return True if the element has been fully parsed or false if other plugins should be notified
	 * of this element.
	 */
	public boolean parseDatabaseChildElementPass1(Element element, Connection db, DatabasePrototype dbPrototype);
	
	/**
	 * This method is called during the second pass to parse an element that is a child of the database element.
	 * 
	 * @param element The element to be processed.
	 * @param db A connection to the database.
	 * @param dbPrototype The database prototype to be updated.
	 * @return True if the element has been fully parsed or false if other plugins should be notified
	 * of this element.
	 */
	public boolean parseDatabaseChildElementPass2(Element element, Connection db, DatabasePrototype dbPrototype);
	
	/**
	 * This method is called during the first pass to parse an element that is a child of a table element.
	 * 
	 * @param element The element to be processed.
	 * @param db A connection to the database.
	 * @param tablePrototype The table prototype to be updated.
	 * @return True if the element has been fully parsed or false if other plugins should be notified
	 * of this element.
	 */
	public boolean parseTableChildElementPass1(Element element, Connection db, TablePrototype tablePrototype);
	
	/**
	 * This method is called during the second pass to parse an element that is a child of a table element.
	 * 
	 * @param element The element to be processed.
	 * @param db A connection to the database.
	 * @param tablePrototype The table prototype to be updated.
	 * @return True if the element has been fully parsed or false if other plugins should be notified
	 * of this element.
	 */
	public boolean parseTableChildElementPass2(Element element, Connection db, TablePrototype tablePrototype);
}
