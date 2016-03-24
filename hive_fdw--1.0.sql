/*-------------------------------------------------------------------------
 *
 *                foreign-data wrapper for hive
 *
 * Copyright (c) 2012, PostgreSQL Global Development Group
 *
 * This software is released under the PostgreSQL Licence
 *
 * Author: Atri Sharma <atri.jiit@gmail.com>
 *
 * IDENTIFICATION
 *                hive_fdw/hive_fdw--1.0.sql
 *
 *-------------------------------------------------------------------------
 */

CREATE FUNCTION hive_fdw_handler()
RETURNS fdw_handler
AS 'MODULE_PATHNAME'
LANGUAGE C STRICT;

CREATE FUNCTION hive_fdw_validator(text[], oid)
RETURNS void
AS 'MODULE_PATHNAME'
LANGUAGE C STRICT;

CREATE FOREIGN DATA WRAPPER hive_fdw
  HANDLER hive_fdw_handler
  VALIDATOR hive_fdw_validator;
