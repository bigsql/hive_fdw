IMPORT FOREIGN SCHEMA
=====================

Here are some examples:

```sql
-- IMPORT hive test_schema to the local SCHEMA.
IMPORT FOREIGN SCHEMA test_schema
    FROM SERVER hive_server INTO test_schema;

-- IMPORT only test_tab1, test_tab2 from hive test_schema to the local
-- SCHEMA.
IMPORT FOREIGN SCHEMA test_schema
    LIMIT TO (test_tab1, test_tab2)
    FROM SERVER hive_server INTO test_schema;

-- IMPORT all other objects from the hive test_schema SCHEMA EXCEPT
-- test_tab1 and test_tab2.
IMPORT FOREIGN SCHEMA test_schema
    EXCEPT (test_tab1, test_tab2)
    FROM SERVER hive_server INTO test_schema;
```
