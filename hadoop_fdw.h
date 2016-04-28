/*-------------------------------------------------------------------------
 *
 * hadoop_fdw.h
 *                Foreign-data wrapper for Hadoop
 *
 * Copyright (c) 2012-2013, BigSQL Development Group
 * Portions Copyright (c) 2012-2013, PostgreSQL Global Development Group
 *
 * IDENTIFICATION
 *                hadoop_fdw/src/hadoop_fdw.h
 *
 *-------------------------------------------------------------------------
 */
#ifndef HADOOP_FDW_H
#define HADOOP_FDW_H

#include "commands/defrem.h"
#include "foreign/foreign.h"
#include "lib/stringinfo.h"
#include "optimizer/pathnode.h"
#include "optimizer/planmain.h"
#include "utils/rel.h"

extern bool is_foreign_expr(PlannerInfo *root, RelOptInfo *baserel, Expr *expr);

extern void appendWhereClause(StringInfo buf, PlannerInfo *root, RelOptInfo *baserel,
                              List *exprs, bool is_first, List **params);

extern void deparseSelectSql(StringInfo buf,
				 PlannerInfo *root,
				 RelOptInfo *baserel,
				 Bitmapset *attrs_used,
				 List **retrieved_attrs);
extern const char *hive_translate_function(FuncExpr *fe, const char *fname);
extern bool is_hive_builtin(FuncExpr *fe);
#endif   /* HADOOP_FDW_H */
