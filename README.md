HIVE2_FDW
========
Please read the README before using hive2_fdw.

hive2_FDW
==============

Foreign Data Wrapper (FDW) that facilitates access to Hive from within PostgreSQL 9.5.


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


8) Set environment variables PGHOME,HIVE_HOME,HADOOP_HOME & HIVE_JDBC_CLASSPATH before starting up PG.These environment variables are read at JVM initialization time.

   $SET PGHOME = PG home directory path
   $SET HIVE_HOME = Hive home directory path
   $SET HADOOP_HOME = Hadoop home directory path
   $SET HIVE_JDBC_CLASSPATH = .:$(echo $HIVE_HOME/lib/*.jar |  tr ' ' :):hadoop-core-1.2.1.jar 


9) Enter psql & Set up hive2_fdw extension.

    $ psql

     CREATE EXTENSION hive2_fdw;
 
     CREATE SERVER hive_serv FOREIGN DATA WRAPPER hive2_fdw 
     OPTIONS(drivername 'org.apache.hive.jdbc.HiveDriver',url 'jdbc:hive2://localhost:10000/default',querytimeout '15',jarfile '/home/braj/hive/lib/hive-jdbc-1.2.1-standalone.jar');

10) Create a user mapping for the server.

      CREATE USER MAPPING FOR public SERVER hive_serv OPTIONS(username 'test', password 'test');

11) Create a foreign table on the server.

      CREATE FOREIGN TABLE test1 (id int) SERVER hive_serv OPTIONS (query 'SELECT * FROM test2');

12) Query the foreign table.

      SELECT * FROM test1 limit 5;

The output should be :

    id   
    ---------
       1
       2
       3
       4
       5