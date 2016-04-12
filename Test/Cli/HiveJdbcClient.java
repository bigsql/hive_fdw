import java.sql.*;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
 
public class HiveJdbcClient {

        private int                     NumberOfRows;
        private String[]                Iterate;
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";
 
public static void main(String[] args) throws SQLException {
      
	int                     NumberOfColumns; 
	int i=0;
	DatabaseMetaData        db_metadata;
	ResultSetMetaData       result_set_metadata;
      
	try {
		Class.forName(driverName);
	} catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
      			System.exit(1);
    		}		
              
	Connection con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "", "");
	Statement stmt = con.createStatement();
	String tableName = "testHiveDriverTable1";
	stmt.execute("drop table if exists " + tableName);
	stmt.execute("create table " + tableName + " (key int, value string)");

	DatabaseMetaData md = con.getMetaData();
	ResultSet res = md.getSchemas();
	String schema_name=null;
	String[] table;
  	table = new String[8];

	while (res.next()) {
  		System.out.println(res.getString(1));
		schema_name = res.getString(1);
	}
 
	res = md.getTables(null, schema_name, "%", null);

	result_set_metadata = res.getMetaData();

	while(res.next())
	{
    		table[i] = res.getString(3);
        	System.out.println(table[i]);
		i++;
	} 
 		ResultSet res_col  = md.getColumns(null,
                   "default",
                   "testHiveDriverTAble1",
                   null);

         while(res_col.next())
        {
                System.out.println(res_col.getString(1) + "\t" + res_col.getString(2)+ "\t" + res_col.getString(3)+"\t"+res_col.getString(4)+ "\t" + res_col.getInt(5) + "\t" + res_col.getString(6) + "\t" + res_col.getInt(7));
        }

	String sql = "select * from " + tableName;
	System.out.println("Running: " + sql);
	res = stmt.executeQuery(sql);
    
	while (res.next()) {
      		System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
    	}
    
 	sql = "show tables '" + tableName + "'";
    	System.out.println("Running: " + sql);
    	res = stmt.executeQuery(sql);
    
	if (res.next()) {
     		 System.out.println(res.getString(1));
    	}
 
	sql = "describe " + tableName;
    	System.out.println("Running: " + sql);
    	res = stmt.executeQuery(sql);
    
	while (res.next()) {
    		  System.out.println(res.getString(1) + "\t" + res.getString(2));
    	}

	String filepath = "/tmp/a.txt";
    	sql = "load data local inpath '" + filepath + "' into table " + tableName;
    	System.out.println("Running: " + sql);
    	stmt.execute(sql);
 
	sql = "select * from " + tableName;
    	System.out.println("Running: " + sql);
    	res = stmt.executeQuery(sql);
    
	while (res.next()) {
      		System.out.println(String.valueOf(res.getInt(1)) + "\t" + res.getString(2));
    	}
 
	sql="show databases";
	System.out.println("Running: " + sql);
	res = stmt.executeQuery(sql);

	while (res.next()) {
		System.out.println(res.getString(1));
	}	  
}
}
