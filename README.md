HIVE2_FDW
========
Please read the README before using hive2_fdw.

Foreign Data Wrapper (FDW) that facilitates access to Hive from within PostgreSQL 9.5.

1) Download and Compile PostgreSQL 9.5 from source

$ cd postgresql-9.5.x
$ ./configure --prefix=$PWD
$ make
$ make install 

2) Enter the /contrib directory in PostgreSQL folder.
$ cd contrib

3) Get hive2_fdw source.
$ git clone https://bitbucket.org/openscg/hive2_fdw.git

4) Execute Make Clean
$ cd hive2_fdw
$ make clean

5) Create a link to your JVM in the PostgreSQL lib folder
$ cd postgresql-9.5.x/lib $ ln -s /usr/lib/jvm/java-ver/jre/lib/amd64/server/libjvm.so libjvm.so

6) Execute Make Install
$ make install