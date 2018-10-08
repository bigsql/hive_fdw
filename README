Options which are to be set
-----

The following parameters can be set on a JDBC foreign server:

drivername:	The name of the JDBC driver e.g.(org.postgresql.Driver).
		Note that drivername has to be specified for hadoop_fdw
		to work i.e. it is compulsory.

url:	        The JDBC URL that shall be used to connect to the foreign database.
	        Note that URL has to be specified for hadoop_fdw
		to work i.e. it is compulsory.

querytimeout:   The time after which a query shall automatically be terminated.
		The option can be used for terminating hung queries.

jarfile:	The path and name(e.g. folder1/folder2/abc.jar) of the JAR file
		of the JDBC driver to be used of the foreign database.

maxheapsize:    The value of the maximum heap size of the JVM being used in hadoop_fdw.
		Please read the notes about maxheapsize option in the installation 
		instructions carefully before setting a value for the option.

The following parameter can be set on a JDBC foreign table:

query:		An SQL query to define the data set on the JDBC server.

table:		The name of a table (quoted and qualified as required)
		on the foreign database table.

The following parameter can be set on a user mapping for a JDBC
foreign server:

username:	The username to use when connecting to foreign database.
		Default <none>

password:	The password to authenticate to the foreign database with.
		Default: <none>


Installing
----------

**Important**

Please ensure that the URL you pass in the url option when creating the server 
is correct and is according to the JDBC URL that is accepted by the foreign database.
Any fault in the passed JDBC URL can cause hard-to-understand errors.Please recheck 
and validate the JDBC URL before passing it in server options.


************************************************************************************************************************************************

** Installation instructions for source installation of PostgreSQL

Documentation for installing PostgreSQL from source 
can be found [here](http://www.postgresql.org/docs/current/static/installation.html).

The steps to follow to install HADOOP_FDW on source installation of PostgreSQL are:

1) Enter the /contrib directory in PostgreSQL folder.

    gitc@ubuntu:~$ cd Downloads/postgresql-9.2beta2/contrib

2) Get HADOOP_FDW source.

    $ git clone git://github.com/atris/HADOOP_FDW.git

3) Enter the HADOOP_FDW folder.

    $ cd HADOOP_FDW

4) Execute Make Clean

    $ make Clean

5) Execute Make Install 
(You may have to change to root/installation privileges before executing 
Make Install)

**Important** : Before running Make Install,please execute the following command:

    $ sudo ln -s /usr/lib/jvm/java-6-openjdk/jre/lib/amd64/server/libjvm.so /usr/lib/libjvm.so

Please replace the path in the command with the path to libjvm.so(it should be 
in Java JRE folder).

For locating libjvm.so,you can use the following command:

    $ locate libjvm.so

Please give the path to libjvm.so in the 'server' folder if multiple libjvm.so 
files are being shown.

If the above command does not work,please execute the following command

    $ sudo ln -s /usr/lib/jvm/java-8-openjdk/jre/lib/amd64/server/libjvm.so /usr/lib/libjvm.so

After running one of the above commands,please execute the following command:

    $ sudo make install
    
6) Ensure Make Install executes successfully without any warning or errors.

7) Enter psql.

    $ psql

6) Set up hadoop_fdw extension.

    CREATE EXTENSION hadoop_fdw;

7) Create a server that uses hadoop_fdw.

**Command to set up server that uses hadoop_fdw as the foreign data wrapper:**
 
     CREATE SERVER jdbc_serv4 FOREIGN DATA WRAPPER hadoop_fdw OPTIONS(
drivername 'org.sqlite.JDBC',
url 'jdbc:sqlite:/home/atri/atri1.sdb',
querytimeout '15',
jarfile '/home/atri/Downloads/sqlite-jdbc-3.7.2.jar',
maxheapsize '600'
);

** Explanation of setting up options for the server

drivername : The drivername has to be given the value of the exact class name 
which has to be loaded for the JDBC driver e.g. org.postgresql.Driver

url : The url has to be given the value of the JDBC URL of the database from 
which the data has to fetched by hadoop_fdw into PostgreSQL.

jarfile : The jarfile has to be given the value of the path and name of the JAR 
file of the JDBC driver of the database from which the data has to fetched.

querytimeout : The time after which a query will be terminated automatically.
This can be used for terminating hung queries.

maxheapsize : The value of the option shall be set to the maximum heap size of 
the JVM which is being used in jdbc fdw.It can be set from 1 Mb onwards.
This option is used for setting the maximum heap size of the JVM manually.

**Important** : Please note that setting the maximum heap size of the JVM 
manually can lead to decrease in performance of hadoop_fdw.It is recommended that
you double check the value you are setting in maxheapsize.It is not a 
compulsory option i.e. If you do net set any value for maxheapsize,the default 
value for the maximum heap size of the JVM being used in hadoop_fdw shall be used.
Please use it only if you want to set the maximum heap size of the JVM manually.
Setting it to a very low value can lead to drastic performance hit.
Also,since the JVM being used in jdbc fdw is created only once for the entire 
psql session,therefore,the first query issued that uses jdbc fdw shall set the
value of maximum heap size of the JVM(if the first query specifies a maximum heap value).

8) Create a user mapping for the server.

    gitc=# CREATE USER MAPPING FOR gitc SERVER jdbc_serv3 OPTIONS(username 'test',password 'test');

9) Create a foreign table on the server.

    gitc=# CREATE FOREIGN TABLE test16 (a int) SERVER jdbc_serv3 OPTIONS (query 'select generate_series(1,1000000)');

10) Query the foreign table.

    gitc=# SELECT * FROM test16;

The output should be :

    Connection to PostgreSQL 9.2beta2 successful.

    a    
    ---------
       1
       2
       3
       4
       5
    Time: 6080.603 ms
    gitc=# 
