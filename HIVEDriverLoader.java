/*-------------------------------------------------------------------------
 *
 *		  foreign-data wrapper for HIVE
 *
 * Copyright (c) 2012, PostgreSQL Global Development Group
 *
 * This software is released under the PostgreSQL Licence
 *
 * Author: Atri Sharma <atri.jiit@gmail.com>
 *
 * IDENTIFICATION
 *		  hive_fdw/HIVEDriverLoader.java
 *
 *-------------------------------------------------------------------------
 */

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
 
public class HIVEDriverLoader extends URLClassLoader
{

/*
 * HIVEDriverLoader
 *		Constructor of HIVEDriverLoader class.
 */
	public 
	HIVEDriverLoader(URL[] path)
	{
		super(path);
	}

/*
 * addPath
 *		Adds a path to the path of the loader.
 */
	public void 
	addPath(String path) throws MalformedURLException
	{
		addURL(new URL (path));
	}

/*
 * CheckIfClassIsLoaded
 *		Checks if a class of given classname has been loaded by the loader or not.
 */
	public Class
	CheckIfClassIsLoaded(String ClassName)
	{
		return findLoadedClass(ClassName);
	}
}
