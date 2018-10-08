##########################################################################
#
#                foreign-data wrapper for HADOOP 
#
# IDENTIFICATION
#                 hadoop_fdw/Makefile
# 
##########################################################################

MODULE_big = hadoop_fdw
OBJS = hadoop_fdw.o deparse.o hive_funcs.o

EXTENSION = hadoop_fdw
DATA = hadoop_fdw--2.6.sql

REGRESS = hadoop_fdw

HADOOP_CONFIG = hadoop_config

SHLIB_LINK = -ljvm

UNAME = $(shell uname)

TRGTS = JAVAFILES

JAVA_SOURCES = \
        HadoopJDBCUtils.java \
	HadoopJDBCLoader.java \
 
PG_CPPFLAGS=-D'PKG_LIB_DIR=$(pkglibdir)'

JFLAGS = -d $(pkglibdir)

all:$(TRGTS)

JAVAFILES:
	javac $(JFLAGS) $(JAVA_SOURCES)
 
PG_CONFIG = pg_config
PGXS := $(shell $(PG_CONFIG) --pgxs)
include $(PGXS)

