package com.blueprintit.dbom.plugins;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.w3c.dom.Element;

import com.blueprintit.dbom.TablePrototype;
import com.blueprintit.dbom.parser.TableParser;

/**
 * @author Dave
 */
public class DefaultTableParser implements TableParser
{
	public DefaultTableParser()
	{		
	}
	
	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.parser.TableParser#parseTableElementPass1(org.w3c.dom.Element, java.sql.Connection, com.blueprintit.dbom.TablePrototype)
	 */
	public void parseTableElementPass1(Element settings, Connection db, TablePrototype tablePrototype)
	{
		try
		{
			DatabaseMetaData data = db.getMetaData();
			ResultSet columns = data.getColumns(db.getCatalog(),null,tablePrototype.getName(),"*");
			while (columns.next())
			{
				tablePrototype.addField(new BuiltInField(tablePrototype,columns.getString(4)));
				System.out.println("Adding field "+columns.getString(4));
			}
			ResultSet keys = data.getPrimaryKeys(tablePrototype.getDatabasePrototype().getCatalog(),null,tablePrototype.getName());
			while (keys.next())
			{
				tablePrototype.markAsKeyField(keys.getString(4));
				System.out.println("Marking field "+columns.getString(4)+" as a key field");
			}
		}
		catch (Exception e)
		{
		}
	}

	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.parser.TableParser#parseTableElementPass2(org.w3c.dom.Element, java.sql.Connection, com.blueprintit.dbom.TablePrototype)
	 */
	public void parseTableElementPass2(Element settings, Connection db, TablePrototype tablePrototype)
	{
	}

	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.parser.Parser#getPriority()
	 */
	public int getPriority()
	{
		return 200;
	}

}
