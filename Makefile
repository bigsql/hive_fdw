##########################################################################
#
#                foreign-data wrapper for Hive
#
# IDENTIFICATION
#                 hive_fdw/Makefile
# 
##########################################################################

MODULE_big = hive_fdw
OBJS = hive_fdw.o deparse.o hive_funcs.o

EXTENSION = hive_fdw
DATA = hive_fdw--3.3.sql

REGRESS = hive_fdw

HIVE_CONFIG = hive_config

SHLIB_LINK = -ljvm

UNAME = $(shell uname)

TRGTS = JAVAFILES

JAVA_SOURCES = \
        HiveJDBCUtils.java \
	HiveJDBCLoader.java \
 
PG_CPPFLAGS=-D'PKG_LIB_DIR=$(pkglibdir)'

JFLAGS = -d $(pkglibdir)

all:$(TRGTS)

JAVAFILES:
	javac $(JFLAGS) $(JAVA_SOURCES)
 
PG_CONFIG = pg_config
PGXS := $(shell $(PG_CONFIG) --pgxs)
include $(PGXS)

