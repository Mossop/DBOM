package com.blueprintit.dbom.plugins;

import javax.servlet.ServletRequest;

import com.blueprintit.dbom.Field;
import com.blueprintit.dbom.TablePrototype;
import com.blueprintit.dbom.TableRecord;

/**
 * @author Dave
 */
public class BuiltInField implements Field
{
	private TablePrototype table;
	private String name;
	
	public BuiltInField(TablePrototype tp, String name)
	{
		this.name=name;
		table=tp;
	}
	
	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.Field#getFieldName()
	 */
	public String getFieldName()
	{
		return name;
	}

	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.Field#getTableName()
	 */
	public String getTableName()
	{
		return table.getName();
	}

	/* (non-Javadoc)
	 * @see com.blueprintit.dbom.Field#getValue(com.blueprintit.dbom.TableRecord, javax.servlet.ServletRequest)
	 */
	public Object getValue(TableRecord record, ServletRequest request)
	{
		return record.getInternalValue(name);
	}

}
