-- SQL*Plus / SQLcl: @00-run-all.sql
WHENEVER SQLERROR EXIT SQL.SQLCODE
SET DEFINE OFF
SET SERVEROUTPUT ON
@01-base-schema.sql
@02-ng-schema-data.sql
@03-base-data.sql
@04-bank-input-data.sql
@05-functions-views.sql
@06-business-schema.sql
@07-business-data.sql
@08-management-data.sql
@09-bank-schema-data.sql
COMMIT;
