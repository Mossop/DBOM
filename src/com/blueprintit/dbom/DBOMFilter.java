package com.blueprintit.dbom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.sql.DataSource;

import com.blueprintit.dbom.parser.DBOMSettingsParser;

/**
 * The filter intercepts web requests and adds the DBOM objects to the request.
 * 
 * @author Dave
 */
public class DBOMFilter implements Filter
{
	/**
	 * The DBOM root object.
	 */
	private DBOM dbom;
	/**
	 * The filter configuration.
	 */
	private FilterConfig config;
	/**
	 * The JNDI name of the database DataSource.
	 */
	private String dbEnv;
	
	public DBOMFilter()
	{
	}
	
	/**
	 * init attempts to load the dbom configuration settings. The file to load from is specified with
	 * the init parameter "DBOM.config".
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException
	{
		this.config=config;
		dbEnv = config.getInitParameter("DBOM.datasource");
		try
		{
			parseConfig();
		}
		catch (Exception e)
		{
			throw new ServletException("Could not load settings",e);
		}
	}
	
	/**
	 * Returns a connection to the database server.
	 * 
	 * @return The database connection.
	 * @throws NamingException
	 * @throws SQLException
	 */
	private Connection getDbConnection() throws NamingException, SQLException
	{
		Context initCtx = new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup(dbEnv);
		return ds.getConnection();
	}
	
	/**
	 * This method locates the settings file then calls an internal method to parse it.
	 * 
	 * @throws Exception
	 */
	public void parseConfig() throws Exception
	{
		String filename = config.getInitParameter("DBOM.config");
		if (filename==null)
		{
			throw new IllegalArgumentException("No config file was specified");
		}
		config.getServletContext().log("DBOMFilter: Loading configuration from "+filename);
		Connection conn = getDbConnection();
		dbom=(new DBOMSettingsParser()).parse(config.getServletContext().getRealPath(filename),conn);
		conn.close();
	}
	
	/**
	 * Currently this adds each database known into the request.
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		Connection conn;
		try
		{
			conn = getDbConnection();
		}
		catch (Exception e)
		{
			throw new ServletException("Error establishing connection to database",e);
		}
 		Iterator dbloop = dbom.getDatabasePrototypes().entrySet().iterator();
	 	while (dbloop.hasNext())
		{
			Map.Entry entry = (Map.Entry)dbloop.next();
			request.setAttribute((String)entry.getKey(),((DatabasePrototype)entry.getValue()).getDatabaseInstance(request,conn));
		}
		//request.setAttribute("DBOM",dbom);
 		chain.doFilter(request,response);
		try
		{
			conn.close();
		}
		catch (Exception e)
		{
			throw new ServletException("Error closing connection to database",e);
		}
	}

	/**
	 * Currently does nothing.
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy()
	{
	}
}
