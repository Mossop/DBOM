package com.blueprintit.dbom.parser;

/**
 * @author Dave
 */
public interface Parser
{
	/**
	 * This method assigns a priority to the parser. Higher values are higher priority and will
	 * be called first. User parsers should use priorities from 0 to 100.
	 * 
	 * @return A priority
	 */
	public int getPriority();
}
