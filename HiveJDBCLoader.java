/*-------------------------------------------------------------------------
 *
 *		  foreign-data wrapper for HIVE
 *
 * IDENTIFICATION
 *		  hive_fdw/HiveJDBCLoader.java
 *
 *-------------------------------------------------------------------------
 */

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
 
public class HiveJDBCLoader extends URLClassLoader
{

/*
 * HiveJDBCLoader
 *		Constructor of HiveJDBCLoader class.
 */
	public 
	HiveJDBCLoader(URL[] path)
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
