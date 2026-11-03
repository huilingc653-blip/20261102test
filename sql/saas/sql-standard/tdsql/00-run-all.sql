-- mysql client: SOURCE 00-run-all.sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
SOURCE 01-base-schema.sql;
SOURCE 02-ng-schema-data.sql;
SOURCE 03-base-data.sql;
SOURCE 04-bank-input-data.sql;
SOURCE 05-functions-views.sql;
SOURCE 06-business-schema.sql;
SOURCE 07-business-data.sql;
SOURCE 08-management-data.sql;
SOURCE 09-bank-schema-data.sql;
SET FOREIGN_KEY_CHECKS = 1;
