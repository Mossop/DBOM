package com.blueprintit.dbom.parser;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
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
 * @author Dave
 */
public class DBOMSettingsParser
{
	private List dbParsers;
	private List tableParsers;
	private Map elementParsers;
	
	public DBOMSettingsParser()
	{
		elementParsers = new HashMap();
		dbParsers = new LinkedList();
		tableParsers = new LinkedList();
	}
	
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
		plugins.add(plugin);
	}
	
	private void addParser(Object parser)
	{
		if (parser instanceof DatabaseParser)
		{
			dbParsers.add(parser);
		}
		if (parser instanceof TableParser)
		{
			tableParsers.add(parser);
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

	private void parseDatabaseElement(Element settings, Connection db, DBOM dbom) throws Exception
	{
		DatabasePrototype thisDb = new DatabasePrototype(settings.getAttribute("id"),settings.getAttribute("catalog"));
		db.setCatalog(thisDb.getCatalog());
		parseDatabaseElementPass1(settings,db,thisDb);
		parseDatabaseElementPass2(settings,db,thisDb);
		dbom.addDatabase(thisDb);
	}
	
	private void parseDatabaseElementPass1(Element settings, Connection db, DatabasePrototype dbPrototype) throws Exception
	{
		Iterator pluginLoop = dbParsers.iterator();
		while (pluginLoop.hasNext())
		{
			((DatabaseParser)pluginLoop.next()).parseDatabaseElementPass1(settings,db,dbPrototype);
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
					pluginLoop = ((List)elementParsers.get(element.getTagName())).iterator();
					while (pluginLoop.hasNext())
					{
						if (((ElementParser)pluginLoop.next()).parseDatabaseChildElementPass1(element,db,dbPrototype))
						{
							break;
						}
					}
				}
			}
		}
	}
	
	private void parseDatabaseElementPass2(Element settings, Connection db, DatabasePrototype dbPrototype) throws Exception
	{
		Iterator pluginLoop = dbParsers.iterator();
		while (pluginLoop.hasNext())
		{
			((DatabaseParser)pluginLoop.next()).parseDatabaseElementPass2(settings,db,dbPrototype);
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
					pluginLoop = ((List)elementParsers.get(element.getTagName())).iterator();
					while (pluginLoop.hasNext())
					{
						if (((ElementParser)pluginLoop.next()).parseDatabaseChildElementPass2(element,db,dbPrototype))
						{
							break;
						}
					}
				}
			}
		}
	}
	
	private void parseTableElementPass1(Element settings, Connection db, TablePrototype tablePrototype) throws Exception
	{
		Iterator pluginLoop = tableParsers.iterator();
		while (pluginLoop.hasNext())
		{
			((TableParser)pluginLoop.next()).parseTableElementPass1(settings,db,tablePrototype);
		}
		
		NodeList nodes = settings.getChildNodes();
		for (int loop=0; loop<nodes.getLength(); loop++)
		{
			if (nodes.item(loop).getNodeType()==Node.ELEMENT_NODE)
			{
				Element element = (Element)nodes.item(loop);
				if (elementParsers.containsKey(element.getTagName()))
				{
					pluginLoop = ((List)elementParsers.get(element.getTagName())).iterator();
					while (pluginLoop.hasNext())
					{
						if (((ElementParser)pluginLoop.next()).parseTableChildElementPass1(element,db,tablePrototype))
						{
							break;
						}
					}
				}
			}
		}
	}
	
	private void parseTableElementPass2(Element settings, Connection db, TablePrototype tablePrototype) throws Exception
	{
		Iterator pluginLoop = tableParsers.iterator();
		while (pluginLoop.hasNext())
		{
			((TableParser)pluginLoop.next()).parseTableElementPass2(settings,db,tablePrototype);
		}
		
		NodeList nodes = settings.getChildNodes();
		for (int loop=0; loop<nodes.getLength(); loop++)
		{
			if (nodes.item(loop).getNodeType()==Node.ELEMENT_NODE)
			{
				Element element = (Element)nodes.item(loop);
				if (elementParsers.containsKey(element.getTagName()))
				{
					pluginLoop = ((List)elementParsers.get(element.getTagName())).iterator();
					while (pluginLoop.hasNext())
					{
						if (((ElementParser)pluginLoop.next()).parseTableChildElementPass2(element,db,tablePrototype))
						{
							break;
						}
					}
				}
			}
		}
	}
	
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
	
	public DBOM parse(InputSource in, Connection db) throws Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb = dbf.newDocumentBuilder();
		Document settings = docb.parse(in);
		return parse(settings,db);
	}
	
	public DBOM parse(Reader reader, Connection db) throws Exception
	{
		return parse(new InputSource(reader),db);
	}
	
	public DBOM parse(InputStream in, Connection db) throws Exception
	{
		return parse(new InputSource(in),db);
	}
	
	public DBOM parse(File file, Connection db) throws Exception
	{
		return parse(new FileReader(file),db);
	}
	
	public DBOM parse(String filename, Connection db) throws Exception
	{
		return parse(new FileReader(filename),db);
	}
}
