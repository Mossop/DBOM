package com.blueprintit.dbom.parser;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.blueprintit.dbom.DBOM;
import com.blueprintit.dbom.DatabasePrototype;
import com.blueprintit.dbom.TablePrototype;

/**
 * This class is used to parse the settings file. It creates the database and table prototypes but does very little
 * else. Everything else should be handled by plugin parsers.
 * 
 * @author Dave
 */
public class DBOMSettingsParser
{
	/**
	 * Holds a collection of parsers that are called for each database. Each is a DatabaseParser.
	 */
	private List dbParsers;
	/**
	 * Holds a collection of parsers that are called for each table. Each is a TableParser.
	 */
	private List tableParsers;
	/**
	 * Holds a collection of parsers that may be called for custom elements. The key is the tag
	 * name of an element and the value is an List of ElementParsers.
	 */
	private Map elementParsers;
	
	/**
	 * Creates a default parser. Installs the default plugins.
	 */
	public DBOMSettingsParser()
	{
		elementParsers = new HashMap();
		dbParsers = new LinkedList();
		tableParsers = new LinkedList();
	}
	
	/**
	 * Used to register an ElementParser into the elementParsers map.
	 * 
	 * @param element The name of the element that can be parsed.
	 * @param plugin The parser.
	 */
	private void addElementParser(String element, ElementParser plugin)
	{
		List plugins;
		if (elementParsers.containsKey(element))
		{
			plugins = (List)elementParsers.get(element);
		}
		else
		{
			plugins = new LinkedList();
			elementParsers.put(element,plugins);
		}
		insertParser(plugin,plugins);
	}
	
	private class ParserComparator implements Comparator
	{
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object arg0, Object arg1)
		{
			Parser p1 = (Parser)arg0;
			Parser p2 = (Parser)arg1;
			return p1.getPriority()-p2.getPriority();
		}
	}
	
	/**
	 * This inserts a parser into a list using the priority level to sort it.
	 * 
	 * @param parser The parser
	 * @param list The list
	 */
	private void insertParser(Parser parser, List list)
	{
		int newpos = Collections.binarySearch(list,parser,new ParserComparator());
		if (newpos<0)
		{
			newpos=-(newpos+1);
		}
		list.add(newpos,parser);
	}
	
	/**
	 * Called to register a custom parser. Detects what type of parser it is and installs it in the relevant
	 * collections.
	 * 
	 * @param parser The parser.
	 */
	private void addParser(Object parser)
	{
		if (parser instanceof DatabaseParser)
		{
			insertParser((Parser)parser,dbParsers);
		}
		if (parser instanceof TableParser)
		{
			insertParser((Parser)parser,tableParsers);
		}
		if (parser instanceof ElementParser)
		{
			ElementParser elParser = (ElementParser)parser;
			String[] names = elParser.getParseableElements();
			for (int loop=0; loop<names.length; loop++)
			{
				addElementParser(names[loop],elParser);
			}
		}
	}

	/**
	 * Called to parse a database element. Creates the prototype and does 2 passes through the element.
	 * 
	 * @param settings The database element.
	 * @param db A connection to the database.
	 * @param dbom
	 * @throws Exception
	 */
	private void parseDatabaseElement(Element settings, Connection db, DBOM dbom) throws Exception
	{
		DatabasePrototype thisDb = new DatabasePrototype(settings.getAttribute("id"),settings.getAttribute("catalog"));
		db.setCatalog(thisDb.getCatalog());
		parseDatabaseElementPass1(settings,db,thisDb);
		parseDatabaseElementPass2(settings,db,thisDb);
		dbom.addDatabasePrototype(thisDb);
	}
	
	/**
	 * Completes the first pass to parse a database element. Calls parsers as necessary and does the first
	 * pass through all table elements.
	 * 
	 * @param settings The database element.
	 * @param db A connection to the database.
	 * @param dbPrototype The prototype for this database.
	 * @throws Exception
	 */
	private void parseDatabaseElementPass1(Element settings, Connection db, DatabasePrototype dbPrototype) throws Exception
	{
		Iterator parserLoop = dbParsers.iterator();
		while (parserLoop.hasNext())
		{
			((DatabaseParser)parserLoop.next()).parseDatabaseElementPass1(settings,db,dbPrototype);
		}
		
		NodeList nodes = settings.getChildNodes();
		for (int loop=0; loop<nodes.getLength(); loop++)
		{
			if (nodes.item(loop).getNodeType()==Node.ELEMENT_NODE)
			{
				Element element = (Element)nodes.item(loop);
				if (element.getTagName().equals("Table"))
				{
					TablePrototype thisTable = new TablePrototype(element.getAttribute("id"));
					parseTableElementPass1(element,db,thisTable);
					dbPrototype.addTablePrototype(thisTable);
				}
				else if (elementParsers.containsKey(element.getTagName()))
				{
					parserLoop = ((List)elementParsers.get(element.getTagName())).iterator();
					while (parserLoop.hasNext())
					{
						if (((ElementParser)parserLoop.next()).parseDatabaseChildElementPass1(element,db,dbPrototype))
						{
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Completes the second pass to parse a database element. Calls parsers as necessary and does the second
	 * pass through all table elements.
	 * 
	 * @param settings The database element.
	 * @param db A connection to the database.
	 * @param dbPrototype The prototype for this database.
	 * @throws Exception
	 */
	private void parseDatabaseElementPass2(Element settings, Connection db, DatabasePrototype dbPrototype) throws Exception
	{
		Iterator parserLoop = dbParsers.iterator();
		while (parserLoop.hasNext())
		{
			((DatabaseParser)parserLoop.next()).parseDatabaseElementPass2(settings,db,dbPrototype);
		}
		
		NodeList nodes = settings.getChildNodes();
		for (int loop=0; loop<nodes.getLength(); loop++)
		{
			if (nodes.item(loop).getNodeType()==Node.ELEMENT_NODE)
			{
				Element element = (Element)nodes.item(loop);
				if (element.getTagName().equals("Table"))
				{
					TablePrototype thisTable = dbPrototype.getTablePrototype(element.getAttribute("id"));
					parseTableElementPass2(element,db,thisTable);
				}
				else if (elementParsers.containsKey(element.getTagName()))
				{
					parserLoop = ((List)elementParsers.get(element.getTagName())).iterator();
					while (parserLoop.hasNext())
					{
						if (((ElementParser)parserLoop.next()).parseDatabaseChildElementPass2(element,db,dbPrototype))
						{
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Completes the first pass to parse a table element. Calls parsers as necessary.
	 * 
	 * @param settings The table element.
	 * @param db A connection to the database.
	 * @param tablePrototype The prototype for this table.
	 * @throws Exception
	 */
	private void parseTableElementPass1(Element settings, Connection db, TablePrototype tablePrototype) throws Exception
	{
		Iterator parserLoop = tableParsers.iterator();
		while (parserLoop.hasNext())
		{
			((TableParser)parserLoop.next()).parseTableElementPass1(settings,db,tablePrototype);
		}
		
		NodeList nodes = settings.getChildNodes();
		for (int loop=0; loop<nodes.getLength(); loop++)
		{
			if (nodes.item(loop).getNodeType()==Node.ELEMENT_NODE)
			{
				Element element = (Element)nodes.item(loop);
				if (elementParsers.containsKey(element.getTagName()))
				{
					parserLoop = ((List)elementParsers.get(element.getTagName())).iterator();
					while (parserLoop.hasNext())
					{
						if (((ElementParser)parserLoop.next()).parseTableChildElementPass1(element,db,tablePrototype))
						{
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Completes the second pass to parse a table element. Calls parsers as necessary.
	 * 
	 * @param settings The table element.
	 * @param db A connection to the database.
	 * @param tablePrototype The prototype for this table.
	 * @throws Exception
	 */
	private void parseTableElementPass2(Element settings, Connection db, TablePrototype tablePrototype) throws Exception
	{
		Iterator parserLoop = tableParsers.iterator();
		while (parserLoop.hasNext())
		{
			((TableParser)parserLoop.next()).parseTableElementPass2(settings,db,tablePrototype);
		}
		
		NodeList nodes = settings.getChildNodes();
		for (int loop=0; loop<nodes.getLength(); loop++)
		{
			if (nodes.item(loop).getNodeType()==Node.ELEMENT_NODE)
			{
				Element element = (Element)nodes.item(loop);
				if (elementParsers.containsKey(element.getTagName()))
				{
					parserLoop = ((List)elementParsers.get(element.getTagName())).iterator();
					while (parserLoop.hasNext())
					{
						if (((ElementParser)parserLoop.next()).parseTableChildElementPass2(element,db,tablePrototype))
						{
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * The base method for parsing the settings file.
	 * 
	 * @param settings The document that has been parsed out of an xml file.
	 * @param db A connection to the database server.
	 * @return The DBOM generated from the settings.
	 * @throws Exception
	 */
	private DBOM parse(Document settings, Connection db) throws Exception
	{
		DBOM dbom = new DBOM();
		NodeList nodes = settings.getDocumentElement().getChildNodes();
		for (int loop=0; loop<nodes.getLength(); loop++)
		{
			if (nodes.item(loop).getNodeType()==Node.ELEMENT_NODE)
			{
				Element element = (Element)nodes.item(loop);
				if (element.getTagName().equals("Database"))
				{
					parseDatabaseElement(element,db,dbom);
				}
				else if (element.getTagName().equals("Parser"))
				{
					Class parserClass = Class.forName(element.getAttribute("class"));
					addParser(parserClass.newInstance());
				}
			}
		}
		return dbom;
	}
	
	/**
	 * Parses an InputSource.
	 * 
	 * @param in The InputSource.
	 * @param db A connection to the database server.
	 * @return The generated DBOM.
	 * @throws Exception
	 */
	public DBOM parse(InputSource in, Connection db) throws Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb = dbf.newDocumentBuilder();
		Document settings = docb.parse(in);
		return parse(settings,db);
	}
	
	/**
	 * Parses a Reader.
	 * 
	 * @param reader The Reader.
	 * @param db A connection to the database server.
	 * @return The generated DBOM.
	 * @throws Exception
	 */
	public DBOM parse(Reader reader, Connection db) throws Exception
	{
		return parse(new InputSource(reader),db);
	}
	
	/**
	 * Parses an InputStream.
	 * 
	 * @param in The InputStream.
	 * @param db A connection to the database server.
	 * @return The generated DBOM.
	 * @throws Exception
	 */
	public DBOM parse(InputStream in, Connection db) throws Exception
	{
		return parse(new InputSource(in),db);
	}
	
	/**
	 * Parses a File.
	 * 
	 * @param file The File.
	 * @param db A connection to the database server.
	 * @return The generated DBOM.
	 * @throws Exception
	 */
	public DBOM parse(File file, Connection db) throws Exception
	{
		return parse(new FileReader(file),db);
	}
	
	/**
	 * Parses a File.
	 * 
	 * @param filename The filename.
	 * @param db A connection to the database server.
	 * @return The generated DBOM.
	 * @throws Exception
	 */
	public DBOM parse(String filename, Connection db) throws Exception
	{
		return parse(new FileReader(filename),db);
	}
}
