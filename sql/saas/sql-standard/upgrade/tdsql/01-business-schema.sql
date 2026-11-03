##付款表增加“返回信息”字段
##TDSQL 不支持存储过程，字段已并入基线建表脚本：sql/saas/sql-standard/tdsql/01-base-schema.sql -> nis_billhead.RETURN_MSG
##GOGO


##付款表增加“交易时间”字段
##TDSQL 不支持存储过程，字段已并入基线建表脚本：sql/saas/sql-standard/tdsql/01-base-schema.sql -> nis_billhead.TRANS_TIME
##GOGO


##企业版本管理表增加“第一次成功部署时间”字段
##TDSQL 不支持存储过程，该字段基线建表脚本已包含：sql/saas/sql-standard/tdsql/09-bank-schema-data.sql -> BP_TENANT_VERSION_MANAGER.FIRST_DEPLOY_TIME
##GOGO