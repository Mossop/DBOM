package com.blueprintit.dbom;

import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.sql.DataSource;

import com.blueprintit.dbom.parser.DBOMSettingsParser;

/**
 * @author Dave
 */
public class DBOMFilter implements Filter
{
	private DBOM dbom;
	private FilterConfig config;
	private String dbEnv;
	
	public DBOMFilter()
	{
	}
	
	/* 
	 * init() attempts to load the dbom configuration settings. The file to load from is specified with
	 * the init parameter "DBOM.config".
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException
	{
		this.config=config;
		dbEnv = config.getServletContext().getInitParameter("DBOM.datasource");
		try
		{
			parseConfig();
		}
		catch (Exception e)
		{
			throw new ServletException("Could not load settings",e);
		}
	}
	
	/*
	 * This method locates the settings file then calls an internal method to parse it.
	 */
	public void parseConfig() throws Exception
	{
		Context initCtx = new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup(dbEnv);
		Connection conn = ds.getConnection();
		dbom=(new DBOMSettingsParser()).parse(config.getServletContext().getRealPath(config.getInitParameter("DBOM.config")),conn);
		conn.close();
	}
	
	/* 
	 * Currently this adds each database known into the request.
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		try
		{
			Context initCtx = new InitialContext();
			Context envCtx = (Context)initCtx.lookup("java:comp/env");
			DataSource ds = (DataSource)envCtx.lookup(dbEnv);
			Connection conn = ds.getConnection();
			Iterator dbloop = dbom.getDatabases().entrySet().iterator();
			while (dbloop.hasNext())
			{
				Map.Entry entry = (Map.Entry)dbloop.next();
				request.setAttribute((String)entry.getKey(),((DatabasePrototype)entry.getValue()).getDatabaseInstance(request,conn));
			}
			request.setAttribute("DBOM",dbom);
			chain.doFilter(request,response);
			conn.close();
		}
		catch (Exception e)
		{
			throw new ServletException("Error establishing connection to database",e);
		}
	}

	/* 
	 * Currently does nothing.
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy()
	{
	}
}
