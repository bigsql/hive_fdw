##########################################################################
#
#                foreign-data wrapper for ATHENA 
#
# IDENTIFICATION
#                 athena_fdw/Makefile
# 
##########################################################################

MODULE_big = athena_fdw
OBJS = athena_fdw.o deparse.o hive_funcs.o

EXTENSION = athena_fdw
DATA = athena_fdw--3.2.sql

REGRESS = athena_fdw

ATHENA_CONFIG = athena_config

SHLIB_LINK = -ljvm

UNAME = $(shell uname)

TRGTS = JAVAFILES

JAVA_SOURCES = \
        AthenaJDBCUtils.java \
	AthenaJDBCLoader.java \
 
PG_CPPFLAGS=-D'PKG_LIB_DIR=$(pkglibdir)'

JFLAGS = -d $(pkglibdir)

all:$(TRGTS)

JAVAFILES:
	javac $(JFLAGS) $(JAVA_SOURCES)
 
PG_CONFIG = pg_config
PGXS := $(shell $(PG_CONFIG) --pgxs)
include $(PGXS)

