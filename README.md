Using HADOOP-FDW with an HDP Sandbox VM
========================================

## Overview ##

This HadoopFDW extension is built into the Windows, OSX & Linux
distributions of [PostgreSQL](http://bigsql.org) by BigSQL.  It allows
access to Hive tables from PostgreSQL.

This document elucidates the steps needed to run it against *HDP 2.4.0 on
Hortonworks Sandbox VM*.

## Pre-Requisites ##

We have tested with HDP versions 2.3.x and 2.4.0 from Windows, OSX and
Linux using Java 8.  For the purposes of this document, we assume that
you have downloaded and installed HDP 2.4 on Hortonworks Sandbox VM from
[Hortonworks Sandbox Downloads](http://hortonworks.com/downloads/#sandbox)
as well as the Oracle JDK 8.

We also assume that the VM is accessible to the machine running
PostgreSQL with the name **hdp-vm** and that the host running PostgreSQL
can connect to **hdp-vm** on Hive TCP port 10000.

### Copy the Hive Client JARs from HDP ###

First, please determine the JAR files that you will need to copy from
the HDP VM.

Connect to the VM using SSH per Hortonworks instructions:

```bash
ssh -p 2222 root@localhost
```

Next, run the command`hadoop classpath` as the hive user to find the
root directory containing the hadoop JAR files (`/usr/hdp/2.4.0.0-169`):

```bash
[root@sandbox ~]# su - hive
[hive@sandbox ~]$ hadoop classpath
/usr/hdp/2.4.0.0-169/hadoop/conf:/usr/hdp/2.4.0.0-169/hadoop/lib/*:/usr/hdp/2.4.0.0-169/hadoop/.//*:
/usr/hdp/2.4.0.0-169/hadoop-hdfs/./:/usr/hdp/2.4.0.0-169/hadoop-hdfs/lib/*:/usr/hdp/2.4.0.0-169/hado
op-hdfs/.//*:/usr/hdp/2.4.0.0-169/hadoop-yarn/lib/*:/usr/hdp/2.4.0.0-169/hadoop-yarn/.//*:/usr/hdp/2
.4.0.0-169/hadoop-mapreduce/lib/*:/usr/hdp/2.4.0.0-169/hadoop-mapreduce/.//*::mysql-connector-java-5
.1.17.jar:mysql-connector-java-5.1.31-bin.jar:mysql-connector-java.jar:/usr/hdp/2.4.0.0-169/tez/*:/u
sr/hdp/2.4.0.0-169/tez/lib/*:/usr/hdp/2.4.0.0-169/tez/conf
```

The JAR files we need are:

```
/usr/hdp/2.4.0.0-169/
    |
    `--- hadoop/
         |
         `--- hadoop-common-2.7.1.2.4.0.0-169.jar
    |
    `--- hive/
         |
         `--- lib
              |
              `--- hive-jdbc-1.2.1000.2.4.0.0-169-standalone.jar
```

If you are trying this with a different version of HDP, you can
determine the specific versions of the JAR files by running the `ls`
commands with the glob patterns shown below:

```bash
[hive@sandbox ~]$ ls /usr/hdp/*/hadoop/hadoop-common*[0-9].jar
/usr/hdp/2.4.0.0-169/hadoop/hadoop-common-2.7.1.2.4.0.0-169.jar
[hive@sandbox ~]$ ls /usr/hdp/*/hive/lib/hive*jdbc*standalone.jar
/usr/hdp/2.4.0.0-169/hive/lib/hive-jdbc-1.2.1000.2.4.0.0-169-standalone.jar
```

Please note that the pattern `hadoop-common*[0-9].jar` precludes the
file `hadoop-common*-test.jar` from appearing.

Once you have the paths for the two JAR files, SCP them to a directory
`hive-client-lib` on the PostgreSQL host that you are installing the
Hadoop FDW on.

## Test that you are able to connect to Hive ##

Create a small JDBC program **HiveJdbcClient.java** with the following
contents:

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveJdbcClient {

    private static final String url      = "jdbc:hive2://hdp-vm:10000";
    private static final String user     = "";
    private static final String password = "";
    private static final String query    = "SHOW DATABASES";

    private static final String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] args) throws SQLException {

        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Connection con = DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();

        System.out.println("Running: " + query);
        ResultSet res = stmt.executeQuery(query);

        while (res.next()) {
            System.out.println(res.getString(1));
        }
    }
}
```

In a shell or Command Prompt, compile it with:

```sh
javac HiveJdbcClient.java
```

### Linux and OS X ###

Assuming that you copied the Hive client JAR files to the directory
`/opt/hadoop/hdp2.4/hive-client-lib`, run the following command in your
shell to execute the program:

```sh
java -cp .:$(echo /opt/hadoop/hdp2.4/hive-client-lib/*.jar | tr ' ' ':') HiveJdbcClient
```

### Windows ###

Assuming that you copied the Hive client JAR files to the directory
`C:\hdp2.4\hive-client-lib`, run the following command in the Command
Prompt to execute the program:

```bat
java -cp .;"C:\hdp2.4\hive-client-lib\hadoop-common-2.7.1.2.4.0.0-169.jar";"C:\hdp2.4\hive-client-lib\hive-jdbc-1.2.1000.2.4.0.0-169-standalone.jar" HiveJdbcClient
```

### Confirm Output ###

No matter the platform you ran the test program on, the last three lines
of its output should be:

```
Running: SHOW DATABASES
default
xademo
```

## Install Hadoop-FDW ##

After establishing a connection with Hive successfully, we can focus on
the PostgreSQL side.

Please install the Hadoop FDW in one of the following ways:

- As a binary from the PGC Update Manager.

- From source; by following the instructions in [BUILD.md](BUILD.md).

## Prepare the Environment ##

The Hadoop FDW needs two environment variables set for the PostgreSQL
server:

- **PGHOME**

    The parent of the PostgreSQL `lib` directory; typically this is the
    PostgreSQL root directory.

- **HADOOP_JDBC_CLASSPATH**

    The `CLASSPATH` referencing all the Hive client JAR files we identified previously.

We provide OS-specific examples for these variables below.

### Perform OS-Specific Setup ###

Please stop the PostgreSQL server if it is running and follow the steps
for the platform matching that of your PostgreSQL server:

### Linux ###

We assume the default PostgreSQL prefix (`/usr/local/pgsql`) on Linux
and that the Hive client JAR files were copied under the directory
`/opt/hadoop/hdp2.4/hive-client-lib`.

Assuming the JDK install location `/opt/jdk/x64/jdk1.8.0_40/`, please
run in a shell:

```sh
sudo ln -s /opt/jdk/x64/jdk1.8.0_40/jre/lib/amd64/server/libjvm.so /usr/local/pgsql/lib/libjvm.so
```

Also, in the shell, set up the requisite environment variables.  In this
example, we are using bash:

```bash
export PGHOME=/usr/local/pgsql
export HADOOP_JDBC_CLASSPATH=$PWD:$(echo /opt/hadoop/hdp2.4/hive-client-lib/*.jar | tr ' ' :)
```

Then start the PostgreSQL server from this shell to have the server pick
up the variables we set up.

### Windows ###

We assume that PostgreSQL is installed in `C:\msys2-x64\usr\local\pgsql`
and that the Hive client JAR files were copied under the directory
`C:\hdp2.4\hive-client-lib`.

Add your JDK's `bin` and `jre\bin\server` directories to the PATH
environment variable.  Assuming the Java install location of
`C:\java-platform\jdk\jdk1.8.0_77` and that you are using the Command
Prompt to set up the environment variables for the PostgreSQL server,
run these commands:

```bat
set JAVA_HOME=C:\java-platform\jdk\jdk1.8.0_77
set PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin\server;%PATH%
```

Next, in the Command Prompt, set up the requisite environment variables.

```bat
set PGHOME=C:\msys2-x64\usr\local\pgsql
set HADOOP_JDBC_CLASSPATH=C:\hdp2.4\hive-client-lib\hadoop-common-2.7.1.2.4.0.0-169.jar;C:\hdp2.4\hive-client-lib\hive-jdbc-1.2.1000.2.4.0.0-169-standalone.jar
```

Then start the PostgreSQL server from this Command Prompt to have the
server pick up the variables we set up.

### OS X ###

**NB: If you have an OS X machine where the default system Java is *not*
installed, please install the legacy Java 6 runtime from
[Apple](https://support.apple.com/kb/DL1572?locale=en_US) before
continuing otherwise the FDW will crash the PostgreSQL server because of
[this](https://bugs.openjdk.java.net/browse/JDK-713135) open issue with
the Oracle JDK.**

We assume the PostgreSQL prefix (`/usr/local/pgsql`) and that the Hive
client JAR files were copied under the directory
`/opt/hadoop/hdp2.4/hive-client-lib`.

Assuming the JDK install location `/opt/jdk/x64/jdk1.8.0_40/`, please
run in a shell:

```sh
sudo ln -s /opt/jdk/x64/jdk1.8.0_40/jre/lib/amd64/server/libjvm.dylib /usr/local/pgsql/lib/libjvm.dylib
```

Also, in the shell, set up the requisite environment variables.  In this
example, we are using bash:

```bash
export PGHOME=/usr/local/pgsql
export HADOOP_JDBC_CLASSPATH=$PWD:$(echo /opt/hadoop/hdp2.4/hive-client-lib/*.jar | tr ' ' :)
```

Then start the PostgreSQL server from this shell to have the server pick
up the variables we set up.

## Use with an HDP Sample TABLE ##

The sample TABLE `sample_07` ships as part of the HDP Sandbox VM.  We
show you how to access it from psql as superuser below:

```sql

CREATE EXTENSION hadoop_fdw;

CREATE SERVER hadoop_server FOREIGN DATA WRAPPER hadoop_fdw
  OPTIONS (HOST 'hdp-vm', PORT '10000');

CREATE USER MAPPING FOR PUBLIC SERVER hadoop_server;

CREATE FOREIGN TABLE sample_07 (
    code                   TEXT,
    description            TEXT,
    total_emp              INT,
    salary                 INT
) SERVER hadoop_server OPTIONS (TABLE 'sample_07');

```

With the `FOREIGN TABLE` in place, run a query against it to retrieve
data from Hive:

```sql
postgres=# SELECT code, total_emp FROM sample_07 ORDER BY code LIMIT 3;
  code   | total_emp
---------+-----------
 00-0000 | 134354250
 11-0000 |   6003930
 11-1011 |    299160
(3 rows)
```
