HADOOP_FDW
========

Foreign Data Wrapper (FDW) that facilitates access to Hadoop from within PostgreSQL 9.5.                   

1) Download and Compile PostgreSQL 9.5 from source

    $ cd postgresql-9.5.x
    $ ./configure --prefix=$PWD
    $ make
    $ make install

2) Enter the /contrib directory in PostgreSQL folder.

    $ cd contrib

3) Get hadoop_fdw source.

    $ git clone https://bitbucket.org/openscg/hadoop_fdw.git

4) Execute Make Clean

    $ cd hadoop_fdw
    $ make clean

5) Create a link to your JVM in the PostgreSQL lib folder

    $ cd postgresql-9.5.x/lib
    $ ln -s /usr/java/jdk1.8.0_51/jre/lib/amd64/server/libjvm.so libjvm.so

6) Execute Make Install 

    $ make install
    
7) Download a copy of Hive-2.0.x from https://hive.apache.org and Hadoop from
   https://archive.apache.org/dist/hadoop/core/


8) Set environment variables PGHOME,HIVE_HOME,HADOOP_HOME & HADOOP_JDBC_CLASSPATH before starting up PG.These      environment variables are read at JVM initialisation time.

    SET PGHOME = PG home directory path
    SET HIVE_HOME = Hive home directory path
    SET HADOOP_HOME = Hadoop home directory path
    SET HADOOP_JDBC_CLASSPATH = .:$(echo $HIVE_HOME/lib/*.jar |  tr ' ' :):/PathToFile/hadoop-core-1.2.1.jar 

 The following parameters can be set on a Hive2 foreign server
object:

  * **`host`**: the address or hostname of the Hive2 server, Examples: "localhost" "127.0.0.1" "127.0.0.1,127.0.0.2", "server1.domain.com".
  * **`port`**: the port number of the Hive2 server. Defaults to 10000.
  

The following parameters can be set on a Hive foreign table object:

  * **`table_name`**: the name of the Hive table to query.  Defaults to the foreign table name used in the relevant CREATE command.

Here is an example:


	-- load EXTENSION first time after install.
	CREATE EXTENSION hadoop_fdw;

        -- create server object
	CREATE SERVER hadoop_serv FOREIGN DATA WRAPPER hadoop_fdw
		OPTIONS(host 'localhost', port '10000');

	-- Create a user mapping for the server.
	CREATE USER MAPPING FOR public SERVER hadoop_serv OPTIONS(username 'test', password 'test');

	-- Create a foreign table on the server.
	CREATE FOREIGN TABLE test (id int) SERVER hadoop_serv OPTIONS (schema 'exmaple',table 'oorder');

	-- Query the foreign table.
	SELECT * FROM test limit 5;