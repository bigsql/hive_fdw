/*-------------------------------------------------------------------------
 *
 * athena_fdw.h
 *                Foreign-data wrapper for ATHENA
 *
 * Copyright (c) 2012-2018, BigSQL
 * Portions Copyright (c) 2012-2013, PostgreSQL Global Development Group
 *
 * IDENTIFICATION
 *                athena_fdw/src/athena_fdw.h
 *
 *-------------------------------------------------------------------------
 */
#ifndef ATHENA_FDW_H
#define ATHENA_FDW_H

#include "commands/defrem.h"
#include "foreign/foreign.h"
#include "lib/stringinfo.h"
#include "optimizer/pathnode.h"
#include "optimizer/planmain.h"
#include "utils/rel.h"

#define ATHENA_FDW_NAME				"athena_fdw"

typedef struct athenaFdwRelationInfo
{
	/*
	 * True means that the relation can be pushed down. Always true for simple
	 * foreign scan.
	 */
	bool		pushdown_safe;

	/* baserestrictinfo clauses, broken down into safe and unsafe subsets. */
	List	   *remote_conds;
	List	   *local_conds;

	/* Bitmap of attr numbers we need to fetch from the remote server. */
	Bitmapset  *attrs_used;

	/* Cached catalog information. */
	ForeignTable *table;
	ForeignServer *server;

	/* Join information */
	RelOptInfo *outerrel;
	RelOptInfo *innerrel;
	JoinType	jointype;
	List	   *joinclauses;
	Oid			foreigntableid;
} athenaFdwRelationInfo;

extern bool is_foreign_expr(PlannerInfo *root, RelOptInfo *baserel, Expr *expr);

extern const char *hive_translate_function(FuncExpr *fe, const char *fname);
extern bool is_hive_builtin(FuncExpr *fe);
extern const char *get_jointype_name(JoinType jointype);
extern List *build_tlist_to_deparse(RelOptInfo *foreign_rel);

extern void
deparseSelectStmtForRel(StringInfo buf, PlannerInfo *root, RelOptInfo *baserel,
						List *remote_conds, List **retrieved_attrs, List **params_list,
						athenaFdwRelationInfo *fpinfo, List *fdw_scan_tlist);
#endif   /* ATHENA_FDW_H */
