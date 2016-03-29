HIVE2_FDW
========

Foreign Data Wrapper (FDW) that facilitates access to Hive Server2 from within PostgreSQL 9.5.                   
https://hive.apache.org/

1) Download and Compile PostgreSQL 9.5 from source

    $ cd postgresql-9.5.x
    $ ./configure --prefix=$PWD
    $ make
    $ make install

2) Enter the /contrib directory in PostgreSQL folder.

    $ cd contrib

3) Get hive2_fdw source.

    $ git clone https://bitbucket.org/openscg/hive2fdw.git

4) Execute Make Clean

    $ cd hive2_fdw
    $ make clean

5) Create a link to your Oracle JVM in the PostgreSQL lib folder

    $ cd postgresql-9.5.x/lib
    $ ln -s /usr/java/jdk1.8.0_51/jre/lib/amd64/server/libjvm.so libjvm.so

6) Execute Make Install 

    $ make install
    
7) Download a copy of Hive and Hadoop from
   from https://hive.apache.org/downloads.html and https://archive.apache.org/dist/hadoop/core/


8) Set environment variables PGHOME,HIVE_HOME,HADOOP_HOME & HIVE_JDBC_CLASSPATH before starting up PG.These      environment variables are read at JVM initialisation time.

    SET PGHOME = PG home directory path
    SET HIVE_HOME = Hive home directory path
    SET HADOOP_HOME = Hadoop home directory path
    SET HIVE_JDBC_CLASSPATH = .:$(echo $HIVE_HOME/lib/*.jar |  tr ' ' :):hadoop-core-1.2.1.jar 

 The following parameters can be set on a Hive2 foreign server
object:

  * **`host`**: the address or hostname of the Hive2 server, Examples: "localhost" "127.0.0.1" "127.0.0.1,127.0.0.2", "server1.domain.com".
  * **`port`**: the port number of the Hive2 server. Defaults to 10000.
  

The following parameters can be set on a Hive foreign table object:

  * **`table_name`**: the name of the Hive table to query.  Defaults to the foreign table name used in the relevant CREATE command.

Here is an example:

```
	-- load EXTENSION first time after install.
	CREATE EXTENSION hive2_fdw;

	-- create server object
	CREATE SERVER hive_serv FOREIGN DATA WRAPPER hive2_fdw
		OPTIONS(host 'localhost', port '10000');

	-- Create a user mapping for the server.
	CREATE USER MAPPING FOR public SERVER hive_serv OPTIONS(username 'test', password 'test');

	-- Create a foreign table on the server.
	CREATE FOREIGN TABLE test (id int) SERVER hive_serv OPTIONS (table 'oorder');

	-- Query the foreign table.
	SELECT * FROM test limit 5;