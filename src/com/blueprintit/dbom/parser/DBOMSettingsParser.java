package com.blueprintit.dbom.parser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.blueprintit.dbom.DBOM;

/**
 * @author Dave
 */
public class DBOMSettingsParser
{
	public DBOMSettingsParser()
	{
	}

	public DBOM parse(Document settings, Connection db) throws Exception
	{
		DBOM dbom = new DBOM();
		return dbom;
	}
	
	public DBOM parse(InputStream in, Connection db) throws Exception
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder docb = dbf.newDocumentBuilder();
		Document settings = docb.parse(in);
		return parse(settings,db);
	}
	
	public DBOM parse(String filename, Connection db) throws Exception
	{
		return parse(new FileInputStream(filename),db);
	}
}
