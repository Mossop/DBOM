package com.blueprintit.dbom.parser;

import java.sql.Connection;

import org.w3c.dom.Element;

import com.blueprintit.dbom.TablePrototype;

/**
 * @author Dave
 */
public interface TableParser
{
	public void parseTableElementPass1(Element settings, Connection db, TablePrototype tablePrototype);
	
	public void parseTableElementPass2(Element settings, Connection db, TablePrototype tablePrototype);
}
