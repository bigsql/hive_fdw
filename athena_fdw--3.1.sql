
CREATE FUNCTION athena_fdw_handler()
RETURNS fdw_handler
AS 'MODULE_PATHNAME'
LANGUAGE C STRICT;

CREATE FUNCTION athena_fdw_validator(text[], oid)
RETURNS void
AS 'MODULE_PATHNAME'
LANGUAGE C STRICT;

CREATE FOREIGN DATA WRAPPER athena_fdw
  HANDLER athena_fdw_handler
  VALIDATOR athena_fdw_validator;
