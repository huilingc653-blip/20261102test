DELIMITER $$
DROP FUNCTION IF EXISTS `BT_ABS`$$
CREATE FUNCTION `BT_ABS`(IN_DEC    REAL) RETURNS double
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_DEC   REAL;
  SET VN_DEC = ABS(IN_DEC);
RETURN VN_DEC;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_ASCII`$$
CREATE FUNCTION `BT_ASCII`(IN_STR    VARCHAR(1)) RETURNS int
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VN_INT    INTEGER;
 SET VN_INT=ASCII(IN_STR);
RETURN VN_INT;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CEIL`$$
CREATE FUNCTION `BT_CEIL`(IN_DEC    REAL) RETURNS double
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_DEC   REAL;
  SET VN_DEC = CEIL(IN_DEC);
RETURN VN_DEC;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CHAR`$$
CREATE FUNCTION `BT_CHAR`(IN_INT    INT) RETURNS varchar(10) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE   VC_STRING   VARCHAR(10);
  SET VC_STRING = CHAR(IN_INT);
RETURN (VC_STRING);
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CONCAT`$$
CREATE FUNCTION `BT_CONCAT`(IN_STR1    VARCHAR(4000),  IN_STR2     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VC_STRING    VARCHAR(4000);
  IF IN_STR1 IS NULL AND IN_STR2 IS NOT NULL then
	SET VC_STRING = IN_STR2;
  ELSEIF IN_STR2 IS NULL then
	SET VC_STRING = IN_STR1;
else
    SET VC_STRING=CONCAT(IN_STR1,IN_STR2);
END IF;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CONCAT10`$$
CREATE FUNCTION `BT_CONCAT10`(IN_STR1    VARCHAR(4000),  IN_STR2     VARCHAR(4000),  IN_STR3     VARCHAR(4000),
                              IN_STR4     VARCHAR(4000), IN_STR5     VARCHAR(4000), IN_STR6     VARCHAR(4000),
                              IN_STR7     VARCHAR(4000), IN_STR8     VARCHAR(4000), IN_STR9     VARCHAR(4000) , IN_STR10     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VC_STRING    VARCHAR(4000);
DECLARE	VC_STR1      VARCHAR(4000);
DECLARE	VC_STR2      VARCHAR(4000);
DECLARE	VC_STR3      VARCHAR(4000);
DECLARE	VC_STR4      VARCHAR(4000);
DECLARE	VC_STR5      VARCHAR(4000);
DECLARE	VC_STR6      VARCHAR(4000);
DECLARE	VC_STR7      VARCHAR(4000);
DECLARE	VC_STR8      VARCHAR(4000);
DECLARE	VC_STR9      VARCHAR(4000);
DECLARE	VC_STR10      VARCHAR(4000);
  SET VC_STR1 = IFNULL(IN_STR1,'');
  SET VC_STR2 = IFNULL(IN_STR2,'');
  SET VC_STR3 = IFNULL(IN_STR3,'');
  SET VC_STR4 = IFNULL(IN_STR4,'');
  SET VC_STR5 = IFNULL(IN_STR5,'');
  SET VC_STR6 = IFNULL(IN_STR6,'');
  SET VC_STR7 = IFNULL(IN_STR7,'');
  SET VC_STR8 = IFNULL(IN_STR8,'');
  SET VC_STR9 = IFNULL(IN_STR9,'');
  SET VC_STR10 = IFNULL(IN_STR10,'');
  SET VC_STRING = CONCAT(VC_STR1,VC_STR2);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR3);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR4);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR5);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR6);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR7);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR8);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR9);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR10);
  IF VC_STRING = '' then
	SET VC_STRING = NULL;
END IF;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CONCAT2`$$
CREATE FUNCTION `BT_CONCAT2`(IN_STR1    VARCHAR(4000),  IN_STR2     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VC_STRING    VARCHAR(4000);
  IF IN_STR1 IS NULL AND IN_STR2 IS NOT NULL then
	SET VC_STRING = IN_STR2;
  ELSEIF IN_STR2 IS NULL then
	SET VC_STRING = IN_STR1;
else
    SET VC_STRING=CONCAT(IN_STR1,IN_STR2);
END IF;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CONCAT3`$$
CREATE FUNCTION `BT_CONCAT3`(IN_STR1    VARCHAR(4000),  IN_STR2     VARCHAR(4000),  IN_STR3     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VC_STRING    VARCHAR(4000);
DECLARE	VC_STR1      VARCHAR(4000);
DECLARE	VC_STR2      VARCHAR(4000);
DECLARE	VC_STR3      VARCHAR(4000);
  SET VC_STR1 = IFNULL(IN_STR1,'');
  SET VC_STR2 = IFNULL(IN_STR2,'');
  SET VC_STR3 = IFNULL(IN_STR3,'');
  SET VC_STRING = CONCAT(VC_STR1,VC_STR2);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR3);
  IF VC_STRING = '' then
	SET VC_STRING = NULL;
END IF;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CONCAT4`$$
CREATE FUNCTION `BT_CONCAT4`(IN_STR1    VARCHAR(4000),  IN_STR2     VARCHAR(4000),  IN_STR3     VARCHAR(4000),IN_STR4     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VC_STRING    VARCHAR(4000);
DECLARE	VC_STR1      VARCHAR(4000);
DECLARE	VC_STR2      VARCHAR(4000);
DECLARE	VC_STR3      VARCHAR(4000);
DECLARE	VC_STR4      VARCHAR(4000);
  SET VC_STR1 = IFNULL(IN_STR1,'');
  SET VC_STR2 = IFNULL(IN_STR2,'');
  SET VC_STR3 = IFNULL(IN_STR3,'');
  SET VC_STR4 = IFNULL(IN_STR4,'');
  SET VC_STRING = CONCAT(VC_STR1,VC_STR2);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR3);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR4);
  IF VC_STRING = '' then
	SET VC_STRING = NULL;
END IF;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CONCAT5`$$
CREATE FUNCTION `BT_CONCAT5`(IN_STR1    VARCHAR(4000),  IN_STR2     VARCHAR(4000),  IN_STR3     VARCHAR(4000),
                             IN_STR4     VARCHAR(4000) ,IN_STR5     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VC_STRING    VARCHAR(4000);
DECLARE	VC_STR1      VARCHAR(4000);
DECLARE	VC_STR2      VARCHAR(4000);
DECLARE	VC_STR3      VARCHAR(4000);
DECLARE	VC_STR4      VARCHAR(4000);
DECLARE	VC_STR5      VARCHAR(4000);
  SET VC_STR1 = IFNULL(IN_STR1,'');
  SET VC_STR2 = IFNULL(IN_STR2,'');
  SET VC_STR3 = IFNULL(IN_STR3,'');
  SET VC_STR4 = IFNULL(IN_STR4,'');
  SET VC_STR5 = IFNULL(IN_STR5,'');
  SET VC_STRING = CONCAT(VC_STR1,VC_STR2);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR3);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR4);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR5);
  IF VC_STRING = '' then
	SET VC_STRING = NULL;
END IF;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CONCAT6`$$
CREATE FUNCTION `BT_CONCAT6`(IN_STR1    VARCHAR(4000),  IN_STR2     VARCHAR(4000),  IN_STR3     VARCHAR(4000),
                             IN_STR4     VARCHAR(4000) ,IN_STR5     VARCHAR(4000),IN_STR6     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VC_STRING    VARCHAR(4000);
DECLARE	VC_STR1      VARCHAR(4000);
DECLARE	VC_STR2      VARCHAR(4000);
DECLARE	VC_STR3      VARCHAR(4000);
DECLARE	VC_STR4      VARCHAR(4000);
DECLARE	VC_STR5      VARCHAR(4000);
DECLARE	VC_STR6      VARCHAR(4000);
  SET VC_STR1 = IFNULL(IN_STR1,'');
  SET VC_STR2 = IFNULL(IN_STR2,'');
  SET VC_STR3 = IFNULL(IN_STR3,'');
  SET VC_STR4 = IFNULL(IN_STR4,'');
  SET VC_STR5 = IFNULL(IN_STR5,'');
  SET VC_STR6 = IFNULL(IN_STR6,'');
  SET VC_STRING = CONCAT(VC_STR1,VC_STR2);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR3);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR4);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR5);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR6);
  IF VC_STRING = '' then
	SET VC_STRING = NULL;
END IF;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CONCAT7`$$
CREATE FUNCTION `BT_CONCAT7`(IN_STR1    VARCHAR(4000),  IN_STR2     VARCHAR(4000),  IN_STR3     VARCHAR(4000),
                             IN_STR4     VARCHAR(4000) ,IN_STR5     VARCHAR(4000),IN_STR6     VARCHAR(4000),IN_STR7     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VC_STRING    VARCHAR(4000);
DECLARE	VC_STR1      VARCHAR(4000);
DECLARE	VC_STR2      VARCHAR(4000);
DECLARE	VC_STR3      VARCHAR(4000);
DECLARE	VC_STR4      VARCHAR(4000);
DECLARE	VC_STR5      VARCHAR(4000);
DECLARE	VC_STR6      VARCHAR(4000);
DECLARE	VC_STR7      VARCHAR(4000);
  SET VC_STR1 = IFNULL(IN_STR1,'');
  SET VC_STR2 = IFNULL(IN_STR2,'');
  SET VC_STR3 = IFNULL(IN_STR3,'');
  SET VC_STR4 = IFNULL(IN_STR4,'');
  SET VC_STR5 = IFNULL(IN_STR5,'');
  SET VC_STR6 = IFNULL(IN_STR6,'');
  SET VC_STR7 = IFNULL(IN_STR7,'');
  SET VC_STRING = CONCAT(VC_STR1,VC_STR2);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR3);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR4);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR5);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR6);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR7);
  IF VC_STRING = '' then
	SET VC_STRING = NULL;
END IF;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CONCAT8`$$
CREATE FUNCTION `BT_CONCAT8`(IN_STR1    VARCHAR(4000),  IN_STR2     VARCHAR(4000),  IN_STR3     VARCHAR(4000),
                             IN_STR4     VARCHAR(4000), IN_STR5     VARCHAR(4000), IN_STR6     VARCHAR(4000),
                             IN_STR7     VARCHAR(4000), IN_STR8     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VC_STRING    VARCHAR(4000);
DECLARE	VC_STR1      VARCHAR(4000);
DECLARE	VC_STR2      VARCHAR(4000);
DECLARE	VC_STR3      VARCHAR(4000);
DECLARE	VC_STR4      VARCHAR(4000);
DECLARE	VC_STR5      VARCHAR(4000);
DECLARE	VC_STR6      VARCHAR(4000);
DECLARE	VC_STR7      VARCHAR(4000);
DECLARE	VC_STR8      VARCHAR(4000);
  SET VC_STR1 = IFNULL(IN_STR1,'');
  SET VC_STR2 = IFNULL(IN_STR2,'');
  SET VC_STR3 = IFNULL(IN_STR3,'');
  SET VC_STR4 = IFNULL(IN_STR4,'');
  SET VC_STR5 = IFNULL(IN_STR5,'');
  SET VC_STR6 = IFNULL(IN_STR6,'');
  SET VC_STR7 = IFNULL(IN_STR7,'');
  SET VC_STR8 = IFNULL(IN_STR8,'');
  SET VC_STRING = CONCAT(VC_STR1,VC_STR2);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR3);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR4);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR5);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR6);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR7);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR8);
  IF VC_STRING = '' then
	SET VC_STRING = NULL;
END IF;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_CONCAT9`$$
CREATE FUNCTION `BT_CONCAT9`(IN_STR1    VARCHAR(4000),  IN_STR2     VARCHAR(4000),  IN_STR3     VARCHAR(4000),
                             IN_STR4     VARCHAR(4000), IN_STR5     VARCHAR(4000), IN_STR6     VARCHAR(4000),
                             IN_STR7     VARCHAR(4000), IN_STR8     VARCHAR(4000), IN_STR9     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
DECLARE VC_STRING    VARCHAR(4000);
DECLARE	VC_STR1      VARCHAR(4000);
DECLARE	VC_STR2      VARCHAR(4000);
DECLARE	VC_STR3      VARCHAR(4000);
DECLARE	VC_STR4      VARCHAR(4000);
DECLARE	VC_STR5      VARCHAR(4000);
DECLARE	VC_STR6      VARCHAR(4000);
DECLARE	VC_STR7      VARCHAR(4000);
DECLARE	VC_STR8      VARCHAR(4000);
DECLARE	VC_STR9      VARCHAR(4000);
  SET VC_STR1 = IFNULL(IN_STR1,'');
  SET VC_STR2 = IFNULL(IN_STR2,'');
  SET VC_STR3 = IFNULL(IN_STR3,'');
  SET VC_STR4 = IFNULL(IN_STR4,'');
  SET VC_STR5 = IFNULL(IN_STR5,'');
  SET VC_STR6 = IFNULL(IN_STR6,'');
  SET VC_STR7 = IFNULL(IN_STR7,'');
  SET VC_STR8 = IFNULL(IN_STR8,'');
  SET VC_STR9 = IFNULL(IN_STR9,'');
  SET VC_STRING = CONCAT(VC_STR1,VC_STR2);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR3);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR4);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR5);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR6);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR7);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR8);
  SET VC_STRING = CONCAT(VC_STRING,VC_STR9);
  IF VC_STRING = '' then
	SET VC_STRING = NULL;
END IF;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_FLOOR`$$
CREATE FUNCTION `BT_FLOOR`(IN_DEC    REAL) RETURNS double
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_DEC   REAL;
  SET VN_DEC = FLOOR(IN_DEC);
RETURN VN_DEC;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_INITCAP`$$
CREATE FUNCTION `BT_INITCAP`(IN_STR     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VC_STRING  VARCHAR(4000);
  DECLARE VC_STRING1 VARCHAR(1);
  DECLARE VC_STRING2 VARCHAR(4000);
  SET VC_STRING1 = UPPER(SUBSTRING(IN_STR,1,1));
  SET VC_STRING2 = LOWER(SUBSTRING(IN_STR,2));
  SET VC_STRING = CONCAT(VC_STRING1, VC_STRING2) ;
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_INSTR`$$
CREATE FUNCTION `BT_INSTR`(IN_STR1   VARCHAR(4000), IN_STR2    VARCHAR(4000)) RETURNS int
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_INTEGER   INTEGER;
  SET VN_INTEGER = INSTR(IN_STR1, IN_STR2);
RETURN VN_INTEGER;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_LENGTH`$$
CREATE FUNCTION `BT_LENGTH`(IN_STR     VARCHAR(4000)) RETURNS int
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_INT  INTEGER;
  SET VN_INT = CHAR_LENGTH(IN_STR);
RETURN VN_INT;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_LOG`$$
CREATE FUNCTION `BT_LOG`(IN_DEC1    REAL, IN_DEC2   REAL) RETURNS double
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_DEC   REAL;
  SET VN_DEC = LOG(IN_DEC1,IN_DEC2);
RETURN VN_DEC;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_LOWER`$$
CREATE FUNCTION `BT_LOWER`(IN_STR     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VC_STRING VARCHAR(4000);
  SET VC_STRING = LOWER(IN_STR);
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_LTRIM`$$
CREATE FUNCTION `BT_LTRIM`(IN_STR    VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VC_STRING VARCHAR(4000);
  SET VC_STRING= LTRIM(IN_STR);
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `bt_nvl`$$
CREATE FUNCTION `bt_nvl`(IN_STR1    VARCHAR(4000),
                         IN_STR2    VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
 DECLARE  VC_STRING VARCHAR(4000);
 IF IN_STR1 ='' OR IN_STR1 IS NULL THEN
   SET VC_STRING = IN_STR2;
ELSE
   SET VC_STRING = IN_STR1;
END IF;
RETURN(VC_STRING);
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_NVL_CHAR`$$
CREATE FUNCTION `BT_NVL_CHAR`(IN_STR1    VARCHAR(4000),
                              IN_STR2    VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
 DECLARE  VC_STRING VARCHAR(4000);
 IF IN_STR1 ='' OR IN_STR1 IS NULL THEN
   SET VC_STRING = IN_STR2;
ELSE
   SET VC_STRING = IN_STR1;
END IF;
RETURN(VC_STRING);
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_NVL_DATE`$$
CREATE FUNCTION `BT_NVL_DATE`(IN_STR1   DATETIME,
                              IN_STR2    DATETIME) RETURNS datetime
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
 DECLARE  VC_STRING DATETIME;
 IF IN_STR1 ='' OR IN_STR1 IS NULL THEN
   SET VC_STRING = IN_STR2;
ELSE
   SET VC_STRING = IN_STR1;
END IF;
RETURN(VC_STRING);
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_NVL_NUM`$$
CREATE FUNCTION `BT_NVL_NUM`(IN_STR1    REAL,IN_STR2     REAL) RETURNS double
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
 DECLARE  VC_STRING  REAL;
 IF IN_STR1 ='' OR  isnull( IN_STR1 ) THEN
   SET VC_STRING = IN_STR2;
ELSE
   SET VC_STRING = IN_STR1;
END IF;
RETURN(VC_STRING);
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_POWER`$$
CREATE FUNCTION `BT_POWER`(IN_DEC1    REAL, IN_DEC2   REAL) RETURNS double
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_DEC   REAL;
  SET VN_DEC = POWER(IN_DEC1,IN_DEC2);
RETURN VN_DEC;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_REPLACE`$$
CREATE FUNCTION `BT_REPLACE`(IN_STR1   VARCHAR(4000), IN_STR2  VARCHAR(4000), IN_STR3   VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VC_STRING  VARCHAR(4000);
  SET VC_STRING=REPLACE(IN_STR1,IN_STR2,IN_STR3);
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_ROUND`$$
CREATE FUNCTION `BT_ROUND`(IN_DEC1    REAL, IN_DEC2   REAL) RETURNS double
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_DEC   REAL;
  SET VN_DEC = ROUND(IN_DEC1, IN_DEC2);
RETURN VN_DEC;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_RTRIM`$$
CREATE FUNCTION `BT_RTRIM`(IN_STR    VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VC_STRING VARCHAR(4000);
  SET VC_STRING= RTRIM(IN_STR);
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_SIGN`$$
CREATE FUNCTION `BT_SIGN`(IN_DEC    REAL) RETURNS double
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_DEC   REAL;
  SET VN_DEC = SQRT(IN_DEC);
RETURN VN_DEC;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_SUBSTR`$$
CREATE FUNCTION `BT_SUBSTR`(IN_STR   VARCHAR(4000), IN_INT1    INT, IN_INT2   INT) RETURNS varchar(4000) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
 DECLARE VC_STRING     VARCHAR(4000);
 DECLARE VN_INT        INT;
 IF in_int2=-1  THEN
   SET VN_INT = LENGTH(IN_STR);
else
   SET VN_INT =IN_INT2;
END IF;
 SET VC_STRING = SUBSTRING(IN_STR, IN_INT1, VN_INT);
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_SYSDATE`$$
CREATE FUNCTION `BT_SYSDATE`() RETURNS datetime
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VC_DATE   datetime;
  SET VC_DATE = NOW();
RETURN VC_DATE;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS BT_DATEDIFF;
CREATE FUNCTION BT_DATEDIFF(date_val DATE) RETURNS INT
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
    IF date_val IS NULL THEN
        RETURN NULL;
    END IF;
    RETURN DATEDIFF(date_val, CURDATE());
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS BT_DATEDIFF2;
CREATE FUNCTION BT_DATEDIFF2(date1 DATE, date2 DATE)
    RETURNS INT
    DETERMINISTIC
BEGIN
    -- 直接调用 MySQL 内置 DATEDIFF
RETURN DATEDIFF(date1, date2);
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_TO_CHAR`$$
CREATE FUNCTION `BT_TO_CHAR`(IN_DATE1    DATETIME, IN_STR2   VARCHAR(30)) RETURNS varchar(30) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
begin
  DECLARE VC_STR     VARCHAR(30);
  DECLARE VC_STR1    VARCHAR(30);
  DECLARE VC_DATE    DATETIME;
  SET VC_DATE = IN_DATE1;
  SET VC_STR1 = IN_STR2;
  SET VC_STR1 = REPLACE(IN_STR2, '-', '');
  SET VC_STR1 = REPLACE(VC_STR1, '/','');
  SET VC_STR1 = REPLACE(VC_STR1, ' ','');
  SET VC_STR1 = REPLACE(VC_STR1, ':','');
  IF LENGTH(VC_STR1) = 4 AND UPPER(SUBSTRING(VC_STR1, 1, 4)) = 'YYYY' THEN
    SET VC_STR = DATE_FORMAT(VC_DATE,'%Y');
  ELSEIF LENGTH(VC_STR1) = 6 AND UPPER(SUBSTRING(VC_STR1, 1, 4)) = 'YYYY'
	  AND UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM' THEN
    SET VC_STR = DATE_FORMAT(VC_DATE,'%Y%m');
    IF UPPER(IN_STR2) = 'YYYY-MM' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y-%m');
END IF;
    IF UPPER(IN_STR2) = 'YYYY/MM' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y/%m');
END IF;
  ELSEIF LENGTH(VC_STR1) = 8 AND UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM'
      AND UPPER(SUBSTRING(VC_STR1,7,2)) = 'DD' THEN
    SET VC_STR = DATE_FORMAT(VC_DATE,'%Y%m%d');
	IF UPPER(IN_STR2) = 'YYYY-MM-DD' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y-%m-%d');
END IF;
    IF UPPER(IN_STR2) = 'YYYY/MM/DD' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y/%m/%d');
END IF;
  ELSEIF LENGTH(VC_STR1) = 16 AND  UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM'
	  AND UPPER(SUBSTRING(VC_STR1,7,2)) = 'DD' AND UPPER(SUBSTRING(VC_STR1,9,4)) = 'HH24'
      AND UPPER(SUBSTRING(VC_STR1,13,2)) = 'MI'AND UPPER(SUBSTRING(VC_STR1,15,2)) = 'SS' THEN
    SET VC_STR = DATE_FORMAT(VC_DATE,'%Y%m%d%H%i%s');
	IF UPPER(IN_STR2) = 'YYYY-MM-DD HH24:MI:SS' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y-%m-%d %H:%i:%s');
END IF;
    IF UPPER(IN_STR2) = 'YYYY/MM/DD HH24:MI:SS' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y/%m/%d %H:%i:%s');
END IF;
END IF;
RETURN VC_STR;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_TO_DATE`$$
CREATE FUNCTION `BT_TO_DATE`(IN_STR1    VARCHAR(30), IN_STR2   VARCHAR(30)) RETURNS datetime
    DETERMINISTIC
    SQL SECURITY INVOKER
begin
  DECLARE VC_STR     VARCHAR(30);
  DECLARE VC_STR1    VARCHAR(30);
  DECLARE VC_DATE    DATETIME;
  SET VC_STR = REPLACE(IN_STR1, '-', '');
  SET VC_STR = REPLACE(VC_STR, '/','');
  SET VC_STR = REPLACE(VC_STR, ' ','');
  SET VC_STR = REPLACE(VC_STR, ':','');
  SET VC_STR1 = REPLACE(IN_STR2, '-', '');
  SET VC_STR1 = REPLACE(VC_STR1, '/','');
  SET VC_STR1 = REPLACE(VC_STR1, ' ','');
  SET VC_STR1 = REPLACE(VC_STR1, ':','');
  IF LENGTH(VC_STR1) = 4 AND UPPER(SUBSTRING(VC_STR1, 1, 4)) = 'YYYY' THEN
    SET VC_DATE = STR_TO_DATE(VC_STR,'%Y');
  ELSEIF LENGTH(VC_STR1) = 6 AND UPPER(SUBSTRING(VC_STR1, 1, 4)) = 'YYYY'
    AND UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM' THEN
    SET VC_DATE = STR_TO_DATE(VC_STR,'%Y%m');
  ELSEIF LENGTH(VC_STR1) = 8 AND UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM'
      AND UPPER(SUBSTRING(VC_STR1,7,2)) = 'DD' THEN
    SET VC_DATE = STR_TO_DATE(VC_STR,'%Y%m%d');
  ELSEIF LENGTH(VC_STR1) = 16 AND  UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM'
    AND UPPER(SUBSTRING(VC_STR1,7,2)) = 'DD' AND UPPER(SUBSTRING(VC_STR1,9,4)) = 'HH24'
      AND UPPER(SUBSTRING(VC_STR1,13,2)) = 'MI'AND UPPER(SUBSTRING(VC_STR1,15,2)) = 'SS' THEN
    SET VC_DATE = str_to_date(VC_STR,'%Y%m%d%H%i%s');
END IF;
RETURN VC_DATE;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_TRUNC_DATE`$$
CREATE FUNCTION `BT_TRUNC_DATE`(IN_STR    DATETIME) RETURNS date
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_STR   DATE;
  SET VN_STR = DATE(IN_STR);
RETURN VN_STR;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_TRUNC_NUM`$$
CREATE FUNCTION `BT_TRUNC_NUM`(IN_NUM    REAL) RETURNS double
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VN_NUM   REAL;
  SET VN_NUM = TRUNCATE(IN_NUM,0);
RETURN VN_NUM;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `BT_UPPER`$$
CREATE FUNCTION `BT_UPPER`(IN_STR     VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE VC_STRING VARCHAR(4000);
  SET VC_STRING = UPPER(IN_STR);
RETURN VC_STRING;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `newid`$$
CREATE FUNCTION `newid`() RETURNS varchar(32) CHARSET utf8mb4 COLLATE utf8mb4_bin
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
  DECLARE vc_str  varchar(32);
  SET vc_str = replace (UUID(),'-','');
RETURN vc_str;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `NVL`$$
CREATE FUNCTION `NVL`(IN_STR1    VARCHAR(4000),
                      IN_STR2    VARCHAR(4000)) RETURNS varchar(4000) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
BEGIN
 DECLARE  VC_STRING VARCHAR(4000);
 IF IN_STR1 ='' OR IN_STR1 IS NULL THEN
   SET VC_STRING = IN_STR2;
ELSE
   SET VC_STRING = IN_STR1;
END IF;
RETURN(VC_STRING);
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `TO_CHAR`$$
CREATE FUNCTION `TO_CHAR`(IN_DATE1    DATETIME, IN_STR2   VARCHAR(30)) RETURNS varchar(30) CHARSET utf8mb4
    DETERMINISTIC
    SQL SECURITY INVOKER
begin
  DECLARE VC_STR     VARCHAR(30);
  DECLARE VC_STR1    VARCHAR(30);
  DECLARE VC_DATE    DATETIME;
  SET VC_DATE = IN_DATE1;
  SET VC_STR1 = IN_STR2;
  SET VC_STR1 = REPLACE(IN_STR2, '-', '');
  SET VC_STR1 = REPLACE(VC_STR1, '/','');
  SET VC_STR1 = REPLACE(VC_STR1, ' ','');
  SET VC_STR1 = REPLACE(VC_STR1, ':','');
  IF LENGTH(VC_STR1) = 4 AND UPPER(SUBSTRING(VC_STR1, 1, 4)) = 'YYYY' THEN
    SET VC_STR = DATE_FORMAT(VC_DATE,'%Y');
  ELSEIF LENGTH(VC_STR1) = 6 AND UPPER(SUBSTRING(VC_STR1, 1, 4)) = 'YYYY'
	  AND UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM' THEN
    SET VC_STR = DATE_FORMAT(VC_DATE,'%Y%m');
    IF UPPER(IN_STR2) = 'YYYY-MM' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y-%m');
END IF;
    IF UPPER(IN_STR2) = 'YYYY/MM' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y/%m');
END IF;
  ELSEIF LENGTH(VC_STR1) = 8 AND UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM'
      AND UPPER(SUBSTRING(VC_STR1,7,2)) = 'DD' THEN
    SET VC_STR = DATE_FORMAT(VC_DATE,'%Y%m%d');
	IF UPPER(IN_STR2) = 'YYYY-MM-DD' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y-%m-%d');
END IF;
    IF UPPER(IN_STR2) = 'YYYY/MM/DD' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y/%m/%d');
END IF;
  ELSEIF LENGTH(VC_STR1) = 16 AND  UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM'
	  AND UPPER(SUBSTRING(VC_STR1,7,2)) = 'DD' AND UPPER(SUBSTRING(VC_STR1,9,4)) = 'HH24'
      AND UPPER(SUBSTRING(VC_STR1,13,2)) = 'MI'AND UPPER(SUBSTRING(VC_STR1,15,2)) = 'SS' THEN
    SET VC_STR = DATE_FORMAT(VC_DATE,'%Y%m%d%H%i%s');
	IF IN_STR2 = 'YYYY-MM-DD HH24:MI:SS' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y-%m-%d %H:%i:%s');
END IF;
    IF UPPER(IN_STR2) = 'YYYY/MM/DD HH24:MI:SS' THEN
       SET VC_STR = DATE_FORMAT(VC_DATE,'%Y/%m/%d %H:%i:%s');
END IF;
END IF;
RETURN VC_STR;
END$$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS `TO_DATE`$$
CREATE FUNCTION `TO_DATE`(IN_STR1    VARCHAR(30), IN_STR2   VARCHAR(30)) RETURNS datetime
    DETERMINISTIC
    SQL SECURITY INVOKER
begin
  DECLARE VC_STR     VARCHAR(30);
  DECLARE VC_STR1    VARCHAR(30);
  DECLARE VC_DATE    DATETIME;
  SET VC_STR = REPLACE(IN_STR1, '-', '');
  SET VC_STR = REPLACE(VC_STR, '/','');
  SET VC_STR = REPLACE(VC_STR, ' ','');
  SET VC_STR = REPLACE(VC_STR, ':','');

  SET VC_STR1 = REPLACE(IN_STR2, '-', '');
  SET VC_STR1 = REPLACE(VC_STR1, '/','');
  SET VC_STR1 = REPLACE(VC_STR1, ' ','');
  SET VC_STR1 = REPLACE(VC_STR1, ':','');
  IF LENGTH(VC_STR1) = 4 AND UPPER(SUBSTRING(VC_STR1, 1, 4)) = 'YYYY' THEN
    SET VC_DATE = STR_TO_DATE(VC_STR,'%Y');
  ELSEIF LENGTH(VC_STR1) = 6 AND UPPER(SUBSTRING(VC_STR1, 1, 4)) = 'YYYY'
	  AND UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM' THEN
    SET VC_DATE = STR_TO_DATE(VC_STR,'%Y%m');
  ELSEIF LENGTH(VC_STR1) = 8 AND UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM'
      AND UPPER(SUBSTRING(VC_STR1,7,2)) = 'DD' THEN
    SET VC_DATE = STR_TO_DATE(VC_STR,'%Y%m%d');

  ELSEIF LENGTH(VC_STR1) = 16 AND  UPPER(SUBSTRING(VC_STR1,5,2)) = 'MM'
	  AND UPPER(SUBSTRING(VC_STR1,7,2)) = 'DD' AND UPPER(SUBSTRING(VC_STR1,9,4)) = 'HH24'
      AND UPPER(SUBSTRING(VC_STR1,13,2)) = 'MI'AND UPPER(SUBSTRING(VC_STR1,15,2)) = 'SS' THEN
    SET VC_DATE = str_to_date(VC_STR1,'%Y%m%d%H%i%s');
END IF;
RETURN VC_DATE;
END$$
DELIMITER ;


DROP VIEW IF EXISTS `dsl_ab_bill_share_syn_view`;
CREATE SQL SECURITY INVOKER VIEW `dsl_ab_bill_share_syn_view` AS
select `a`.`ID`                                                              AS `id`,
       `a`.`TENANT_ID`                                                       AS `TENANT_ID`,
       (case when (`sc`.`CODE` = '100') then '100000' else `sc`.`CODE` end)  AS `companyCode`,
       `sc`.`NAME`                                                           AS `companyName`,
       `vv`.`BANK_ACC`                                                       AS `ticketUnitAccount`,
       `vv`.`TYPE_NAME`                                                      AS `ticketUnitBankCategory`,
       `vv`.`VALID_SIGN`                                                     AS `isEnable`,
       `v`.`ORIG_NO`                                                         AS `docNo`,
       '01'                                                                  AS `issuanceFlag`,
       (case when (`v`.`BLGRDBAGCRCL_PRMT_IND` = '1') then '0' else '1' end) AS `allowSubpackage`,
       `v`.`SBBLL_RNG_STRT_SN`                                               AS `subTicketStartNo`,
       `v`.`SBBLL_RNG_END_SN`                                                AS `subTicketEndNo`,
       `vv`.`BATCH_NO`                                                       AS `issuanceBatchNo`,
       (case
            when (`a`.`BRIEF_ID` in ('001', '003')) then '1'
            when (`a`.`BRIEF_ID` in ('6001', '9003', '9010')) then '11'
            when (`a`.`BRIEF_ID` = '010') then '17'
            when (`a`.`BRIEF_ID` = '6120') then '10'
            when (`a`.`BRIEF_ID` = '004')
                then '36' end)                                               AS `businessType`,
       `sd`.`TYPE_CODE`                                                      AS `ticketSource`,
       (case
            when (`v`.`BILL_FLAG` = 10) then '2'
            when (`v`.`BILL_FLAG` = 11)
                then '1' end)                                                AS `circulationMedium`,
       (case
            when (`v`.`BILL_TYPE` = 10) then '1'
            when (`v`.`BILL_TYPE` = 11)
                then '2' end)                                                AS `ticketType`,
       `v`.`BILL_MONEY`                                                      AS `faceValue`,
       `a`.`CREATE_BY`                                                       AS `applicant`,
       `a`.`APPLY_DATE`                                                      AS `applyDate`,
       `a`.`BUSINESS_DATE`                                                   AS `businessTime`,
       `v`.`START_DATE`                                                      AS `issuanceTime`,
       `v`.`END_DATE`                                                        AS `maturityTime`,
       `d`.`INTEREST_RATES`                                                  AS `discountRate`,
       `d`.`INTEREST`                                                        AS `discountInterest`,
       `v`.`PAYER`                                                           AS `drawer`,
       `v`.`PAYER_BANK_NAME`                                                 AS `issuingBank`,
       `v`.`PAYERACC`                                                        AS `drawerAccount`,
       `v`.`PAYERBANKNO`                                                     AS `drawerBankCode`,
       `v`.`PAYEE`                                                           AS `payee`,
       `v`.`PAYEEACC`                                                        AS `payeeAccount`,
       `v`.`PAYEE_BANK_NAME`                                                 AS `payeeBank`,
       `v`.`PAYEEBANKNO`                                                     AS `payeeBankCode`,
       `v`.`ACCEPTOR`                                                        AS `acceptor`,
       `v`.`ACCEPTORACC`                                                     AS `acceptorAccount`,
       `v`.`ACCEPTOR_BANK_NAME`                                              AS `acceptorBank`,
       `v`.`ACCEPTORBANKNO`                                                  AS `acceptorBankCode`,
       `vv`.`BAIL_ACC`                                                       AS `marginAccount`,
       `acc`.`ACC_NAME`                                                      AS `marginAccountName`,
       `acc`.`BANK_NAME`                                                     AS `marginBankName`,
       `sc2`.`NAME`                                                          AS `marginUnitName`,
       ifnull(`vv`.`BAIL_MONEY`, 0)                                          AS `marginAmount`,
       `vv`.`BAIL_PERCENT`                                                   AS `marginRatio`,
       `vv`.`BAIL_RATE`                                                      AS `marginRate`,
       '01'                                                                  AS `marginType`,
       (case
            when (`a`.`BRIEF_ID` = '6001') then `a`.`CN_ACCRECORDS`
            when (`bs`.`BUS_TYPE` in ('6001', '9002', '9011', '9017', '9018', '9020', '9023'))
                then `a`.`REC_NM` end)                                       AS `previousParty`,
       (case
            when (`bs`.`BUS_TYPE` in
                  ('001', '017', '002', '003', '010', '018', '019', '020', '011', '012', '022', '6201', '6203', '6004',
                   '023'))
                then `a`.`REC_NM` end)                                       AS `nextParty`,
       (case
            when (`a`.`SEND_SIGN` = '0') then '2'
            when (`a`.`SEND_SIGN` = '1')
                then '1' end)                                                AS `onlineOfflineFlag`,
       '3'                                                                   AS `businessStatus`,
       (case when (`a`.`OPT_STATUS` = '19') then '9' end)                    AS `instructionStatus`,
       (case
            when (`vv`.`TYPE` = '0') then 'YF'
            when (`vv`.`TYPE` = '1')
                then 'YS' end)                                               AS `receivablePayableFlag`,
       `v`.`BILL_STATUS`                                                     AS `ticketStatus`,
       `v`.`EBILL_BL_CRCL_INDC`                                              AS `circulationStatus`,
       (`v`.`BILL_MONEY` - ifnull(`vv`.`BAIL_MONEY`, 0))                     AS `maturityPaymentAmount`
from (((((((((`ab_accrecords` `a` left join `ab_voucherinfo` `v`
              on ((`a`.`VID` = `v`.`ID`))) left join `ab_voucherinfo_vice` `vv`
             on (((`a`.`VVID` = `vv`.`ID`) and (`a`.`TENANT_ID` = `vv`.`TENANT_ID`)))) left join `sys_corp` `sc`
            on (((`sc`.`ID` = `vv`.`CORP_ID`) and (`a`.`TENANT_ID` = `sc`.`TENANT_ID`)))) left join `ab_bill_source_dtl` `sd`
           on (((`sd`.`BRIEF_ID` = `a`.`BRIEF_ID`) and
                ((`vv`.`SOURCE` = `sd`.`SOURCE`) or ((`vv`.`SOURCE` is null) and (`a`.`BILL_SOURCE` = `sd`.`SOURCE`)) or
                 ((`vv`.`SOURCE` is null) and (`a`.`BILL_SOURCE` is null) and (`sd`.`SOURCE` is null))) and
                (`a`.`TENANT_ID` = `sd`.`TENANT_ID`)))) left join `ab_discount` `d`
          on (((`d`.`RECORD_ID` = `a`.`ID`) and (`a`.`TENANT_ID` = `d`.`TENANT_ID`)))) left join `bt_bank_acc` `acc`
         on (((`acc`.`BANK_ACC` = `vv`.`BAIL_ACC`) and
              (`a`.`TENANT_ID` = `acc`.`TENANT_ID`)))) left join `sys_corp` `sc2`
        on (((`sc2`.`ID` = `acc`.`CORP_ID`) and
             (`a`.`TENANT_ID` = `sc2`.`TENANT_ID`)))) left join `ab_brief_status` `bs`
       on (((`bs`.`STATUS` = `a`.`BRIEF_ID`) and
            (`a`.`TENANT_ID` = `bs`.`TENANT_ID`)))) left join `dsl_inteface_flag` `ef`
      on (((`ef`.`SOURCE_ID` = `a`.`ID`) and (`ef`.`TABLE_SOURCE` = 'DSL_AB_BILL_SHARE_SYN_VIEW') and
           (`a`.`TENANT_ID` = `ef`.`TENANT_ID`))))
where ((1 = 1) and (`a`.`STATUS` in ('95', '96', '100')) and (`a`.`OPT_STATUS` = '19') and
       (`v`.`ORIG_NO` is not null) and (`v`.`ORIG_NO` <> '') and
       (((`a`.`BRIEF_ID` = '001') and (`a`.`SEND_SIGN` = '0')) or
        ((`a`.`BRIEF_ID` = '003') and (`a`.`SEND_SIGN` = '1') and (`a`.`STATUS` >= '95') and
         (`a`.`OPT_STATUS` = '19')) or
        ((`a`.`BRIEF_ID` in ('6001', '9003', '9010')) and (`vv`.`OLD_DRAFT_ID` is null)) or
        (`a`.`BRIEF_ID` in ('010', '6120', '004'))) and (ifnull(`ef`.`SYNC_STATUS`, 'F') <> 'S') and
       (`a`.`BUSINESS_DATE` >= '2025-02-01 00:00:00'));

DROP VIEW IF EXISTS `dsl_ab_bill_view`;
CREATE SQL SECURITY INVOKER VIEW `dsl_ab_bill_view` AS
select `a`.`ID`                                                                                    AS `FMS_ID`,
       `a`.`TENANT_ID`                                                                             AS `TENANT_ID`,
       'M'                                                                                         AS `RECEIPT_TYPE`,
       'BFS'                                                                                       AS `RECEIPT_SOURCE`,
       `v`.`ORIG_NO`                                                                               AS `RECEIPT_NUM`,
       `v`.`SBBLL_RNG_STRT_SN`                                                                     AS `SUB_RECEIPT_FROM`,
       `v`.`SBBLL_RNG_END_SN`                                                                      AS `SUB_RECEIPT_TO`,
       (case when (`vv`.`TYPE` = '0') then 'AP' else 'AR' end)                                     AS `RECEIPT_PROPERTY`,
       (case
            when ((`v`.`BILL_TYPE` = 10) and (`v`.`BILL_FLAG` = 10)) then '银行承兑汇票-纸质'
            when ((`v`.`BILL_TYPE` = 10) and (`v`.`BILL_FLAG` = 11)) then '银行承兑汇票-电子'
            when ((`v`.`BILL_TYPE` = 11) and (`v`.`BILL_FLAG` = 10)) then '商业承兑汇票'
            when ((`v`.`BILL_TYPE` = 11) and (`v`.`BILL_FLAG` = 11)) then '商业承兑汇票-电子' end) AS `RECEIPT_NAME`,
       `v`.`BILL_MONEY`                                                                            AS `RECEIPT_COST`,
       'CNY'                                                                                       AS `CURRENCY_CODE`,
       (case
            when ((`a`.`BRIEF_ID` = '002') and (`acc`.`BANK_TYPE_ID` = '0e5a53a00fcf467e9fd42125614a9232'))
                then `a`.`BUSINESS_DATE`
            else `v`.`START_DATE` end)                                                             AS `CREATE_RECEIPT_DATE`,
       `v`.`END_DATE`                                                                              AS `RECEIPT_DUE_DATE`,
       `v`.`PAYER`                                                                                 AS `CREATE_RECEIPT_NAME`,
       `v`.`PAYERACC`                                                                              AS `CREATE_REC_ACCOUNT`,
       `v`.`PAYER_BANK_NAME`                                                                       AS `CREATE_REC_BANK`,
       `v`.`PAYEE`                                                                                 AS `PAYEE_NAME`,
       `v`.`PAYEEACC`                                                                              AS `PAYEE_ACCOUNT`,
       `v`.`PAYEE_BANK_NAME`                                                                       AS `PAYEE_BANK`,
       concat((`vv`.`BAIL_PERCENT` * 100), '%')                                                    AS `CASH_DEPOSIT_SCALE`,
       `vv`.`BAIL_MONEY`                                                                           AS `CASH_DEPOSIT_COST`,
       (case
            when (`a`.`BRIEF_ID` = '010') then `n`.`PURPOSE`
            when (`a`.`BRIEF_ID` = '9010') then concat('原出票人：', `v`.`PAYER`)
            else `vv`.`RMK` end)                                                                   AS `COMMT`,
       (case when (`a`.`BRIEF_ID` = '010') then 'Y' else 'N' end)                                  AS `ENDORSE_FLAG`,
       (case when (`a`.`BRIEF_ID` = '010') then `a`.`REC_NM` else NULL end)                        AS `ENDORSER`,
       (case when (`a`.`BRIEF_ID` = '010') then `a`.`CREATE_DATE` else NULL end)                   AS `ENDOSE_DATE`,
       (`v`.`BILL_MONEY` - ifnull(`vv`.`BAIL_MONEY`, 0))                                           AS `ACTUAL_COST`,
       (case
            when (`a`.`BRIEF_ID` = '6001') then '11'
            when (`a`.`BRIEF_ID` = '9010') then '12'
            when (`a`.`BRIEF_ID` = '6004') then '13'
            when (`a`.`BRIEF_ID` = '9003') then '26'
            when (`a`.`BRIEF_ID` = '6120') then '28'
            when ((`a`.`BRIEF_ID` = '004') and (`v`.`BILL_STATUS` <> 'CS05')) then '29' end)       AS `ACTION_TYPE`,
       `a`.`BUSINESS_DATE`                                                                         AS `CASHING_DATE`,
       (case
            when ((`a`.`BRIEF_ID` = '010') and (`nb`.`ID` is not null)) then `su`.`USERNAME`
            when (`a`.`BRIEF_ID` = '002') then `v`.`CREATE_NAME`
            else ifnull(`vv`.`CREATE_NAME`, `a`.`CREATE_NAME`) end)                                AS `MAKER`,
       'R'                                                                                         AS `PUSH_STATUS_FLG`,
       'R'                                                                                         AS `PROCESSED_FLAG`,
       (case
            when (`a`.`BRIEF_ID` in ('9003', '9010', '6001')) then '11'
            when (`a`.`BRIEF_ID` = '010') then '12'
            when (`a`.`BRIEF_ID` = '6004') then '13'
            when (`a`.`BRIEF_ID` in ('001', '002')) then '27'
            when ((`a`.`BRIEF_ID` = '004') and (`v`.`BILL_STATUS` <> 'CS05')) then '29' end)       AS `STATUS`,
       (case when (`a`.`BRIEF_ID` in ('9010', '010')) then `v`.`BILL_MONEY` else NULL end)         AS `ENDOSE_AMOUNT`,
       (case
            when (`a`.`BRIEF_ID` = '6120') then `a`.`BUSINESS_DATE`
            else NULL end)                                                                         AS `RECEIPT_CASH_DATE`,
       (case when (`sc`.`CODE` = '100') then '100000' else `sc`.`CODE` end)                        AS `attribute1`,
       NULL                                                                                        AS `attribute2`,
       NULL                                                                                        AS `attribute3`,
       NULL                                                                                        AS `attribute4`,
       NULL                                                                                        AS `attribute5`
from ((((((((`ab_accrecords` `a` left join `ab_voucherinfo` `v`
             on (((`a`.`VID` = `v`.`ID`) and (`a`.`TENANT_ID` = `v`.`TENANT_ID`)))) left join `ab_voucherinfo_vice` `vv`
            on (((`a`.`VVID` = `vv`.`ID`) and (`a`.`TENANT_ID` = `vv`.`TENANT_ID`)))) left join `sys_corp` `sc`
           on (((`sc`.`ID` = `vv`.`CORP_ID`) and (`a`.`TENANT_ID` = `sc`.`TENANT_ID`)))) left join `dsl_inteface_flag` `ef`
          on (((`ef`.`SOURCE_ID` = `a`.`ID`) and (`ef`.`TABLE_SOURCE` = 'DSL_AB_BILL_VIEW') and
               (`a`.`TENANT_ID` = `ef`.`TENANT_ID`)))) left join `nis_bill` `nb`
         on (((`nb`.`BILL_ID` = `a`.`ID`) and (`a`.`TENANT_ID` = `nb`.`TENANT_ID`)))) left join `nis_billhead` `n`
        on (((`nb`.`PAY_ID` = `n`.`ID`) and (`a`.`TENANT_ID` = `n`.`TENANT_ID`)))) left join `sys_user` `su`
       on (((`su`.`LOGIN_NAME` = `n`.`CREATE_BY`) and
            (`a`.`TENANT_ID` = `su`.`TENANT_ID`)))) left join `bt_bank_acc` `acc`
      on (((`acc`.`BANK_ACC` = `v`.`PAYERACC`) and (`a`.`TENANT_ID` = `acc`.`TENANT_ID`))))
where ((1 = 1) and (`a`.`BRIEF_ID` not in ('010', '9010', '6001', '9003', '011')) and
       (((`a`.`BRIEF_ID` in ('9010', '010')) and (`a`.`OPT_STATUS` in ('18', '19'))) or
        ((`a`.`BRIEF_ID` not in ('9010', '010')) and (`a`.`OPT_STATUS` = '19'))) and (`a`.`STATUS` in ('95', '96')) and
       (((`a`.`BRIEF_ID` not in ('9010', '010')) and (`a`.`OLD_ACC_ID` is null)) or
        (`a`.`BRIEF_ID` in ('9010', '010'))) and
       (((`a`.`BRIEF_ID` not in ('9010', '010')) and (`vv`.`OLD_DRAFT_ID` is null)) or
        (`a`.`BRIEF_ID` in ('9010', '010'))) and (`v`.`ORIG_NO` is not null) and (`v`.`ORIG_NO` <> '') and
       (((`vv`.`TYPE` = '1') and (`a`.`BUSINESS_DATE` > '2024-08-31') and (`a`.`APPLY_DATE` > '2024-08-31')) or
        ((`vv`.`TYPE` <> '1') and (`a`.`BUSINESS_DATE` > '2024-08-09') and (`a`.`APPLY_DATE` > '2024-08-09'))) and
       (((`a`.`BRIEF_ID` in ('6001', '9003', '9010')) and (`vv`.`OLD_DRAFT_ID` is null)) or
        (`a`.`BRIEF_ID` in ('002', '6004', '010', '011')) or
        ((`a`.`BRIEF_ID` = '004') and (`v`.`BILL_STATUS` <> 'CS05')) or
        ((`a`.`BRIEF_ID` = '001') and (`a`.`SEND_SIGN` = '0'))) and (ifnull(`ef`.`SYNC_STATUS`, 'F') <> 'S'))
order by `a`.`CREATE_DATE`;

DROP VIEW IF EXISTS `dsl_ab_emit_bail_interest_sync_view`;
CREATE SQL SECURITY INVOKER VIEW `dsl_ab_emit_bail_interest_sync_view` AS
select `ae`.`ID`                                                            AS `orderId`,
       `ae`.`TENANT_ID`                                                     AS `TENANT_ID`,
       (case when (`sc`.`CODE` = '100') then '100000' else `sc`.`CODE` end) AS `companyCode`,
       `sc`.`NAME`                                                          AS `companyName`,
       `ae`.`BANK_ACC`                                                      AS `receiptAccount`,
       `ae`.`INTEREST_MARGIN`                                               AS `marginAmount`,
       concat(`ae`.`CURRENT_MONTH`, '-', '01')                              AS `accountingTime`
from (`ab_emit_bail_interest_info` `ae` left join `sys_corp` `sc`
      on (((`ae`.`CORP_ID` = `sc`.`ID`) and (`ae`.`TENANT_ID` = `sc`.`TENANT_ID`))))
where ((`ae`.`STATUS` = '1') and (`ae`.`PUSH_STATUS` = '1'));

DROP VIEW IF EXISTS `dsl_all_bill_view`;
CREATE SQL SECURITY INVOKER VIEW `dsl_all_bill_view` AS
select `n`.`ID`              AS `id`,
       `n`.`TENANT_ID`       AS `TENANT_ID`,
       `n`.`BILL_NO`         AS `originalBillNo`,
       `n`.`BILL_NO`         AS `bill_no`,
       `n`.`STATUS`          AS `status`,
       `n`.`ERP_ID`          AS `erp_id`,
       `n`.`WISH_PAYDAY`     AS `wish_payday`,
       `n`.`PAY_MONEY`       AS `pay_money`,
       `n`.`PAYER_ACC`       AS `payer_acc`,
       `n`.`CUR_ID`          AS `cur_id`,
       `n`.`CREATE_BY`       AS `create_by`,
       `n`.`IS_PAYED`        AS `is_payed`,
       `n`.`BANK_SERIAL_NO`  AS `bank_serial_no`,
       `n`.`PURPOSE`         AS `purpose`,
       `n`.`VOUCHER_TYPE_ID` AS `voucher_type_id`
from ((`nis_billhead` `n` left join `nis_billhead` `n2`
       on (((`n2`.`SPLIT_ID` = `n`.`ID`) and (`n`.`TENANT_ID` = `n2`.`TENANT_ID`)))) left join `nis_billhead` `n3`
      on (((`n`.`MERGE_ID` = `n3`.`ID`) and (`n`.`TENANT_ID` = `n3`.`TENANT_ID`))))
where ((1 = 1) and (`n`.`ORIG_SPLIT_ID` is null) and (`n2`.`ID` is null) and (`n3`.`ID` is null) and
       (`n`.`ERP_ID` is not null))
union all
select `n2`.`ID`              AS `id`,
       `n`.`TENANT_ID`        AS `TENANT_ID`,
       `n`.`BILL_NO`          AS `originalBillNo`,
       `n2`.`BILL_NO`         AS `bill_no`,
       `n2`.`STATUS`          AS `status`,
       `n`.`ERP_ID`           AS `erp_id`,
       `n2`.`WISH_PAYDAY`     AS `wish_payday`,
       `n2`.`PAY_MONEY`       AS `pay_money`,
       `n2`.`PAYER_ACC`       AS `payer_acc`,
       `n2`.`CUR_ID`          AS `cur_id`,
       `n2`.`CREATE_BY`       AS `create_by`,
       `n2`.`IS_PAYED`        AS `is_payed`,
       `n2`.`BANK_SERIAL_NO`  AS `bank_serial_no`,
       `n2`.`PURPOSE`         AS `purpose`,
       `n2`.`VOUCHER_TYPE_ID` AS `voucher_type_id`
from (`nis_billhead` `n` left join `nis_billhead` `n2`
      on (((`n2`.`ORIG_SPLIT_ID` = `n`.`ID`) and (`n`.`TENANT_ID` = `n2`.`TENANT_ID`))))
where ((1 = 1) and (`n2`.`ID` is not null) and (`n2`.`SPLIT_ID` is not null) and (`n`.`STATUS` = -(2)) and
       exists(select 1 from `nis_billhead` `t` where (`t`.`SPLIT_ID` = `n2`.`ID`)) is false and
       (`n`.`ERP_ID` is not null));

DROP VIEW IF EXISTS `dsl_bank_acc_info_view`;
CREATE SQL SECURITY INVOKER VIEW `dsl_bank_acc_info_view` AS
select `a`.`ID`                                                              AS `BANK_ACC_ID`,
       `a`.`TENANT_ID`                                                       AS `TENANT_ID`,
       `a`.`BANK_ACC`                                                        AS `BANK_ACC`,
       (case when (`s`.`CODE` = '100') then '100000' else `s`.`CODE` end)    AS `CORP_CODE`,
       `a`.`ACC_NAME`                                                        AS `BANK_ACC_NAME`,
       `c`.`cur`                                                             AS `CUR`,
       `a`.`ELECTRIC_BILL`                                                   AS `IS_BILL_ACC`,
       `a`.`CIRCULATION_MODE`                                                AS `CIRCULATION_MODE`,
       (case `a`.`IS_DOMESTIC_BANK` when '0' then '1' when '1' then '0' end) AS `AREA`,
       `d1`.`name`                                                           AS `ACC_TYPE`,
       `n`.`NATURE_NAME`                                                     AS `ACC_NATURE`,
       `n`.`NATURE_CODE`                                                     AS `ACC_NATURE_CODE`,
       `att`.`ATTRIBUTE_NAME`                                                AS `ACC_ATTRIBUTE`,
       `att`.`ATTRIBUTE_CODE`                                                AS `ACC_ATTRIBUTE_CODE`,
       `a`.`BANK_CODE`                                                       AS `BANK_CODE`,
       `a`.`BANK_NAME`                                                       AS `BANK_NAME`,
       `t`.`BANK_TYPE`                                                       AS `BANK_TYPE_CODE`,
       `t`.`TYPE_NAME`                                                       AS `BANK_TYPE`,
       `r1`.`NAME`                                                           AS `PROV`,
       `r2`.`NAME`                                                           AS `CITY`,
       `a`.`RATES`                                                           AS `rates`,
       date_format(`a`.`REG_DATE`, '%Y-%m-%d')                               AS `REG_DATE`,
       date_format(`a`.`UPDATE_DATE`, '%Y-%m-%d %H:%i:%s')                   AS `UPDATE_DATE`,
       date_format(`a`.`CLOSE_DATE`, '%Y-%m-%d')                             AS `CLOSE_DATE`,
       (case when (`a`.`STATUS` = -(2)) then '0' else '1' end)               AS `STATUS`,
       date_format(`a`.`CHECK_EXPIRE_DATE`, '%Y-%m-%d')                      AS `CHECK_EXPIRE_DATE`,
       `a`.`CREATE_BY`                                                       AS `CREATE_BY`,
       `a`.`VALID_SIGN`                                                      AS `VALID_SIGN`,
       `a`.`IS_ONLINE`                                                       AS `IS_ONLINE`,
       `a`.`IS_CAPITAL_POOL`                                                 AS `IS_CAPITAL_POOL`,
       `a`.`RMK`                                                             AS `rmk`
from ((((((((`bt_bank_acc` `a` left join (select `bc`.`BANK_ACC_ID`                             AS `BANK_ACC_ID`,
                                                 group_concat(`c`.`ENGLISH_CODE` separator ',') AS `cur`
                                          from (`bt_bank_acc_cur` `bc` left join `bt_currency` `c`
                                                on (((`c`.`ID` = `bc`.`CUR_ID`) and (`bc`.`TENANT_ID` = `c`.`TENANT_ID`))))
                                          group by `bc`.`BANK_ACC_ID`) `c`
             on ((`c`.`BANK_ACC_ID` = `a`.`ID`))) left join `sys_corp` `s`
            on (((`s`.`ID` = `a`.`CORP_ID`) and (`a`.`TENANT_ID` = `s`.`TENANT_ID`)))) left join `bis_acc_attribute` `att`
           on (((`a`.`ATTRIBUTE_ID` = `att`.`ID`) and
                (`a`.`TENANT_ID` = `att`.`TENANT_ID`)))) left join `bt_acc_nature` `n`
          on (((`a`.`NATURE_ID` = `n`.`ID`) and (`a`.`TENANT_ID` = `n`.`TENANT_ID`)))) left join `bt_bank_type` `t`
         on (((`t`.`ID` = `a`.`BANK_TYPE_ID`) and (`a`.`TENANT_ID` = `t`.`TENANT_ID`)))) left join `sys_region` `r1`
        on (((`r1`.`ID` = `a`.`PROV`) and (`a`.`TENANT_ID` = `r1`.`TENANT_ID`)))) left join `sys_region` `r2`
       on (((`r2`.`ID` = `a`.`CITY`) and (`a`.`TENANT_ID` = `r2`.`TENANT_ID`)))) left join (select `d`.`TYPE_CODE` AS `code`, `d`.`TYPE_NAME` AS `name`
                                                                                            from (`sys_dict_group` `g` join `sys_dict` `d`
                                                                                                  on ((
                                                                                                      (`g`.`ID` = `d`.`TYPE_GROUP_ID`) and
                                                                                                      (`g`.`TENANT_ID` = `d`.`TENANT_ID`))))
                                                                                            where (`g`.`TYPE_GROUP_CODE` = 'btBankAcc.accType')) `d1`
      on ((`d1`.`code` = `a`.`ACC_TYPE`)));

DROP VIEW IF EXISTS `dsl_bis_acc_dtl_view`;
CREATE SQL SECURITY INVOKER VIEW `dsl_bis_acc_dtl_view` AS
select `dtl`.`ID`                                                                                      AS `SEQ_ID`,
       `dtl`.`TENANT_ID`                                                                               AS `TENANT_ID`,
       '01'                                                                                            AS `DJZT_CODE`,
       '正常'                                                                                          AS `DJZT`,
       `dtl`.`ID`                                                                                      AS `YHLSH`,
       (case when (`bc`.`CODE` = '100') then '100000' else `bc`.`CODE` end)                            AS `DWBH`,
       `bc`.`NAME`                                                                                     AS `DWMC`,
       (case when (`dtl`.`CD_SIGN` = '1') then '02' else '01' end)                                     AS `DJLX_CODE`,
       (case when (`dtl`.`CD_SIGN` = '1') then '付款' else '收款' end)                                 AS `DJLX_DESC`,
       (case
            when (`bi`.`BIF_CODE` = '9302') then `dtl`.`USES`
            else concat_ws('&', concat_ws('&', `dtl`.`REMARK`, `dtl`.`USES`), `dtl`.`POSTSCRIPT`) end) AS `YT`,
       `dtl`.`BANK_ACC`                                                                                AS `ZHBH`,
       `dtl`.`ACC_NAME`                                                                                AS `ZHMC`,
       `acc`.`BANK_CODE`                                                                               AS `LHH`,
       `dtl`.`BANK_NAME`                                                                               AS `KHH`,
       `dtl`.`AMT`                                                                                     AS `JE`,
       `dtl`.`OPP_ACC_NO`                                                                              AS `DFZH`,
       `dtl`.`OPP_ACC_NAME`                                                                            AS `DFHM`,
       `dtl`.`OPP_ACC_BANK`                                                                            AS `DFKHH`,
       date_format(`dtl`.`TRANS_TIME`, '%Y%m%d')                                                       AS `JYRQ`,
       date_format(`dtl`.`TRANS_TIME`, '%H%i%s')                                                       AS `JYSJ`,
       '01'                                                                                            AS `SJLY_CODE`,
       '银企直联获取'                                                                                  AS `SJLY_DESC`,
       'POST'                                                                                          AS `ACTION_TYPE`,
       `nb`.`BURSAR_BILL_NO`                                                                           AS `SOURCE_NUM`
from (((((`bis_acc_dtl` `dtl` left join `nis_bill_bank_serial_no` `ns`
          on ((`dtl`.`ID` = `ns`.`DTL_ID`))) left join `nis_billhead` `nb`
         on (((`nb`.`ID` = `ns`.`BILL_ID`) and (`dtl`.`TENANT_ID` = `nb`.`TENANT_ID`)))) left join `bt_bank_acc` `acc`
        on (((`dtl`.`BANK_ACC` = `acc`.`BANK_ACC`) and
             (`dtl`.`TENANT_ID` = `acc`.`TENANT_ID`)))) left join `sys_corp` `bc`
       on (((`bc`.`ID` = `acc`.`CORP_ID`) and (`bc`.`TENANT_ID` = `dtl`.`TENANT_ID`)))) left join `bis_bif_init` `bi`
      on (((`bi`.`ID` = `dtl`.`BIF_CODE`) and (`bi`.`TENANT_ID` = `dtl`.`TENANT_ID`))))
where ((1 = 1) and
       (((`dtl`.`TRANS_TIME` > '2024-07-20') and (`dtl`.`BIF_CODE` <> 'aef6125b6ea2497db16038e7f9e087e7')) or
        ((`dtl`.`TRANS_TIME` > '2024-07-24') and (`dtl`.`BIF_CODE` = 'aef6125b6ea2497db16038e7f9e087e7'))) and
       ((ifnull(`dtl`.`DTL_SYNC_STATUS`, 'F') <> 'S') or
        (ifnull(`nb`.`BURSAR_BILL_NO`, '') <> ifnull(`dtl`.`SOURCE_NUM`, ''))));

DROP VIEW IF EXISTS `dsl_bis_bank_receipt_dtl_view`;
CREATE SQL SECURITY INVOKER VIEW `dsl_bis_bank_receipt_dtl_view` AS
select `b`.`ID`                                                           AS `id`,
       `b`.`TENANT_ID`                                                    AS `TENANT_ID`,
       `d`.`ID`                                                           AS `serialId`,
       `pay`.`SERIAL_NO_ERP`                                              AS `serialNoErp`,
       `b`.`RECEIPT_NO`                                                   AS `receiptNo`,
       `d`.`ID`                                                           AS `transSerialNo`,
       (case when (`b`.`CD_SIGN` = '1') then '02' else '01' end)          AS `fileType`,
       concat('http://ten-dsl-cw-zijin-cdn.dslbuy.com/', `b`.`FILE_PATH`) AS `filePath`,
       `b`.`CHARAGE_DATE`                                                 AS `fileDate`,
       `b`.`PAYEE_ACC_NO`                                                 AS `PAYEE_Acc_No`,
       `b`.`PAYEE_ACC_NAME`                                               AS `PAYEE_Name`,
       `b`.`PAYEE_BRACN_NAME`                                             AS `PAYEE_Bank`,
       `acc2`.`BANK_CODE`                                                 AS `PAYEE_Code`,
       `b`.`PAY_ACC_NO`                                                   AS `PAYER_Acc_No`,
       `b`.`PAY_ACC_NAME`                                                 AS `PAYER_Acc_Name`,
       `b`.`PAY_BRANCH_NAME`                                              AS `PAYER_Bank_Name`,
       `acc`.`BANK_CODE`                                                  AS `PAYER_Code`,
       `bc`.`ENGLISH_CODE`                                                AS `Cur`,
       `b`.`TRANS_AMT`                                                    AS `Amt`
from ((((((((`bis_bank_receipt_dtl` `b` left join `bt_bank_acc` `acc` on (((`acc`.`BANK_ACC` = `b`.`PAY_ACC_NO`) and
                                                                           (`b`.`TENANT_ID` = `acc`.`TENANT_ID`)))) left join `bt_bank_acc` `acc2`
            on (((`acc2`.`BANK_ACC` = `b`.`PAYEE_ACC_NO`) and
                 (`b`.`TENANT_ID` = `acc2`.`TENANT_ID`)))) left join `bt_currency` `bc`
           on (((`b`.`CUR_ID` = `bc`.`ID`) and (`b`.`TENANT_ID` = `bc`.`TENANT_ID`)))) left join `bis_receipt_dtl` `brd`
          on (((`brd`.`RECEIPT_ID` = `b`.`ID`) and (`b`.`TENANT_ID` = `brd`.`TENANT_ID`)))) left join `bis_acc_dtl` `d`
         on (((`d`.`ID` = `brd`.`DTL_ID`) and (`d`.`CD_SIGN` = `b`.`CD_SIGN`) and (`d`.`BLEND_SIGN` = '1') and
              (`d`.`TENANT_ID` = `b`.`TENANT_ID`)))) left join `nis_bill_bank_serial_no` `nb`
        on (((`nb`.`DTL_ID` = `d`.`ID`) and (`b`.`TENANT_ID` = `nb`.`TENANT_ID`)))) left join `nis_billhead` `n`
       on (((`n`.`ID` = `nb`.`BILL_ID`) and (`b`.`TENANT_ID` = `n`.`TENANT_ID`)))) left join `erp_payinfo` `pay`
      on (((`n`.`ERP_ID` = `pay`.`ID`) and (`b`.`TENANT_ID` = `pay`.`TENANT_ID`))));

DROP VIEW IF EXISTS `dsl_bt_input_bank_info_view`;
CREATE SQL SECURITY INVOKER VIEW `dsl_bt_input_bank_info_view` AS
select `b`.`SYS_BANK_CODE`                                                            AS `SYS_BANK_CODE`,
       `b`.`TENANT_ID`                                                                AS `TENANT_ID`,
       `b`.`BANK_NAME`                                                                AS `BANK_NAME`,
       `b`.`UPDATE_DATE`                                                              AS `UPDATE_DATE`,
       `b`.`CREATE_DATE`                                                              AS `CREATE_DATE`,
       (case when (`t`.`BANK_TYPE` is null) then 'DSL01' else `t`.`BANK_TYPE` end)    AS `BANK_TYPE_CODE`,
       (case when (`t`.`TYPE_NAME` is null) then '村镇银行' else `t`.`TYPE_NAME` end) AS `BANK_TYPE_NAME`
from (`bt_input_bank_info` `b` left join `bt_bank_type` `t`
      on (((`b`.`BANK_PREFIX` = `t`.`BANK_PREFIX`) and (`b`.`TENANT_ID` = `t`.`TENANT_ID`) and
           (`b`.`BANK_NAME` like concat('%', ifnull(`t`.`BANK_NAME_PREFIX`, ''), '%')))))
where ((`t`.`BANK_NAME_PREFIX` is null) or (`b`.`BANK_NAME` like concat('%', `t`.`BANK_NAME_PREFIX`, '%')));

DROP VIEW IF EXISTS `dsl_gx_bis_acc_dtl_view`;
CREATE SQL SECURITY INVOKER VIEW `dsl_gx_bis_acc_dtl_view` AS
select `dtl`.`ID`                                                                      AS `seqId`,
       `dtl`.`TENANT_ID`                                                               AS `TENANT_ID`,
       `dtl`.`BANK_SERIAL_NO`                                                          AS `yhlsh`,
       (case when (`bc`.`CODE` = '100') then '100000' else `bc`.`CODE` end)            AS `dwbh`,
       `bc`.`NAME`                                                                     AS `dwmc`,
       (case when (`dtl`.`CD_SIGN` = '1') then '02' else '01' end)                     AS `incomeExpenseType`,
       `dtl`.`BANK_ACC`                                                                AS `zhbh`,
       `dtl`.`ACC_NAME`                                                                AS `zhmc`,
       `acc`.`BANK_CODE`                                                               AS `lhh`,
       `dtl`.`BANK_NAME`                                                               AS `khh`,
       `dtl`.`AMT`                                                                     AS `je`,
       `dtl`.`OPP_ACC_NO`                                                              AS `dfzh`,
       `dtl`.`OPP_ACC_NAME`                                                            AS `dfhm`,
       `dtl`.`OPP_ACC_BANK`                                                            AS `dfkhh`,
       (case when (`c`.`ENGLISH_CODE` = 'RMB') then 'CNY' else `c`.`ENGLISH_CODE` end) AS `currencyCode`,
       date_format(`dtl`.`TRANS_TIME`, '%Y/%m/%d')                                     AS `jyrq`,
       date_format(`dtl`.`TRANS_TIME`, '%Y/%m/%d %H:%i:%s')                            AS `jysj`,
       `dtl`.`REMARK`                                                                  AS `yt`,
       `dtl`.`POSTSCRIPT`                                                              AS `summary`,
       `dtl`.`USES`                                                                    AS `purpose`,
       `nt`.`BUS_TYPE`                                                                 AS `barCodeType`,
       `nt`.`BUS_TYPE_NAME`                                                            AS `billTypeDesc`,
       `nb`.`BURSAR_BILL_NO`                                                           AS `sourceNum`,
       (case when (`ns`.`isReturn` is null) then 'N' else `ns`.`isReturn` end)         AS `isReturn`
from (((((((`bis_acc_dtl` `dtl` left join (select `nis_bill_bank_serial_no_refund`.`DTL_ID`    AS `dtl_id`,
                                                  `nis_bill_bank_serial_no_refund`.`TENANT_ID` AS `TENANT_ID`,
                                                  `nis_bill_bank_serial_no_refund`.`BILL_ID`   AS `bill_id`,
                                                  'Y'                                          AS `isReturn`
                                           from `nis_bill_bank_serial_no_refund`
                                           union
                                           select `nis_bill_bank_serial_no`.`DTL_ID`    AS `dtl_id`,
                                                  `nis_bill_bank_serial_no`.`TENANT_ID` AS `TENANT_ID`,
                                                  `nis_bill_bank_serial_no`.`BILL_ID`   AS `bill_id`,
                                                  'N'                                   AS `isReturn`
                                           from `nis_bill_bank_serial_no`) `ns` on (((`dtl`.`ID` = `ns`.`dtl_id`) and
                                                                                     (`dtl`.`TENANT_ID` = `ns`.`TENANT_ID`)))) left join `nis_billhead` `nb`
           on (((`nb`.`ID` = `ns`.`bill_id`) and (`nb`.`TENANT_ID` = `dtl`.`TENANT_ID`)))) left join `nis_bus_type` `nt`
          on (((`nb`.`BUS_TYPE` = `nt`.`ID`) and (`nt`.`TENANT_ID` = `dtl`.`TENANT_ID`)))) left join `bt_bank_acc` `acc`
         on (((`dtl`.`BANK_ACC` = `acc`.`BANK_ACC`) and
              (`acc`.`TENANT_ID` = `dtl`.`TENANT_ID`)))) left join `sys_corp` `bc`
        on (((`bc`.`ID` = `acc`.`CORP_ID`) and (`bc`.`TENANT_ID` = `dtl`.`TENANT_ID`)))) left join `bis_bif_init` `bi`
       on (((`bi`.`ID` = `dtl`.`BIF_CODE`) and (`bi`.`TENANT_ID` = `dtl`.`TENANT_ID`)))) left join `bt_currency` `c`
      on (((`c`.`ID` = `dtl`.`CUR_ID`) and (`c`.`TENANT_ID` = `dtl`.`TENANT_ID`))))
where ((1 = 1) and (`dtl`.`TRANS_TIME` >= '2025-02-01 00:00:00.000000') and
       (`dtl`.`CREATE_DATE` > '2025-02-07 15:00:00') and (ifnull(`dtl`.`DTL_SYNC_STATUS`, 'F') <> 'S') and
       (`dtl`.`BANK_ACC` <> '15999329560036') and ((ifnull(`dtl`.`GX_DTL_SYNC_STATUS`, 'F') <> 'S') or
                                                   (ifnull(`nb`.`BURSAR_BILL_NO`, '') <>
                                                    ifnull(`dtl`.`GX_SOURCE_NUM`, ''))));

DROP VIEW IF EXISTS `dsl_payment_result_view`;
CREATE SQL SECURITY INVOKER VIEW `dsl_payment_result_view` AS
select `erp`.`SERIAL_NO_ERP`                                         AS `paymentNo`,
       `erp`.`TENANT_ID`                                             AS `TENANT_ID`,
       `n`.`bill_no`                                                 AS `paymentVoucherNo`,
       NULL                                                          AS `flowNo`,
       `erp`.`GX_VOUCHER_TYPE`                                       AS `paymentType`,
       (case
            when ((`nd`.`SYNC_STATUS` = 'S') and (`ft`.`VOUCHER_TYPE` in ('66', '67', '68', '69', '70', '71'))) then 'R'
            else 'C' end)                                            AS `paymentStatus`,
       NULL                                                          AS `actualPaymentAmount`,
       NULL                                                          AS `actualPaymentDate`,
       NULL                                                          AS `payBankAccountNo`,
       NULL                                                          AS `payBankAccountName`,
       `erp`.`SETTLE_CORP_CODE`                                      AS `company`,
       `erp`.`CORP_CODE`                                             AS `actualPaymentCompany`,
       (select group_concat(concat(`u`.`USER_CODE`, '+', `u`.`USER_NAME`) order by `u`.`USER_CODE` ASC separator
                            '#') AS `user_info`
        from (`nis_corp_cashier` `c` join `nis_corp_cashier_user` `u`
              on (((`c`.`ID` = `u`.`CORP_CASHIER_ID`) and (`c`.`TENANT_ID` = `u`.`TENANT_ID`))))
        where ((`c`.`STATUS` = 1) and (`erp`.`CORP_CODE` = `c`.`CORP_CODE`))
        group by `c`.`CORP_CODE`)                                    AS `paymentCashierUserNo`,
       `n`.`purpose`                                                 AS `summary`,
       NULL                                                          AS `billNo`,
       NULL                                                          AS `intervalBeginNo`,
       NULL                                                          AS `intervalEndNo`,
       NULL                                                          AS `billAmount`,
       NULL                                                          AS `billStatus`,
       NULL                                                          AS `customerCode`,
       NULL                                                          AS `customerName`,
       NULL                                                          AS `billDate`,
       NULL                                                          AS `paymentErrReason`,
       (case
            when ((`nd`.`SYNC_STATUS` = 'S') and (`ft`.`VOUCHER_TYPE` in ('66', '67', '68', '69', '70', '71')))
                then `nd`.`BILL_NO`
            else NULL end)                                           AS `refundBankFlowNo`,
       (case
            when ((`nd`.`SYNC_STATUS` = 'S') and (`ft`.`VOUCHER_TYPE` in ('66', '67', '68', '69', '70', '71')))
                then `nd`.`PAY_MONEY`
            else NULL end)                                           AS `refundAmount`,
       (case
            when ((`nd`.`SYNC_STATUS` = 'S') and (`ft`.`VOUCHER_TYPE` in ('66', '67', '68', '69', '70', '71')))
                then `nd`.`UPDATE_DATE`
            else NULL end)                                           AS `refundFlowTradeDate`,
       (case
            when ((`nd`.`SYNC_STATUS` = 'S') and (`ft`.`VOUCHER_TYPE` in ('66', '67', '68', '69', '70', '71')))
                then `nd`.`PAYEE_ACC_NAME`
            else NULL end)                                           AS `refundRemitter`,
       (case
            when ((`nd`.`SYNC_STATUS` = 'S') and (`ft`.`VOUCHER_TYPE` in ('66', '67', '68', '69', '70', '71')))
                then `nd`.`PAYEE_ACC`
            else NULL end)                                           AS `refundRemitterAccount`,
       (case
            when ((`nd`.`SYNC_STATUS` = 'S') and (`ft`.`VOUCHER_TYPE` in ('66', '67', '68', '69', '70', '71')))
                then '撤票'
            else NULL end)                                           AS `refundFlowSummary`,
       (case
            when ((`nd`.`SYNC_STATUS` = 'S') and (`ft`.`VOUCHER_TYPE` in ('66', '67', '68', '69', '70', '71')))
                then `nd`.`UPDATE_BY`
            else NULL end)                                           AS `refundFlowBlendingUser`,
       (case
            when ((`nd`.`SYNC_STATUS` = 'S') and (`ft`.`VOUCHER_TYPE` in ('66', '67', '68', '69', '70', '71')))
                then date_format(`nd`.`UPDATE_DATE`, '%Y-%m-%d %H:%i:%s')
            else NULL end)                                           AS `refundFlowBlendingDate`,
       (case
            when (`nd`.`SYNC_STATUS` = 'S') then '撤票'
            else ifnull(`bt`.`BUS_TYPE_NAME`, `erp`.`BUS_TYPE`) end) AS `revokePaymentReason`
from (((((`erp_payinfo` `erp` join `dsl_all_bill_view` `n`
          on (((`n`.`erp_id` = `erp`.`ID`) and (`erp`.`TENANT_ID` = `n`.`TENANT_ID`)))) left join `nis_billhead` `nd`
         on (((`nd`.`ID` = `n`.`id`) and (`nd`.`TENANT_ID` = `erp`.`TENANT_ID`)))) left join `fc_voucher_type` `ft`
        on (((`ft`.`ID` = `nd`.`VOUCHER_TYPE_ID`) and
             (`ft`.`TENANT_ID` = `erp`.`TENANT_ID`)))) left join `bis_exc` `exc`
       on (((`exc`.`CBS_BILL_ID` = `n`.`id`) and
            (`exc`.`TENANT_ID` = `erp`.`TENANT_ID`)))) left join `bt_charge_back_type` `bt`
      on (((`bt`.`BUS_TYPE` = `erp`.`BUS_TYPE`) and (`bt`.`TENANT_ID` = `erp`.`TENANT_ID`))))
where ((1 = 1) and (`n`.`status` = -(2)) and exists(select 1
                                                    from `dsl_inteface_flag` `flag`
                                                    where ((`flag`.`SOURCE_ID` = concat_ws(';', `n`.`bill_no`, 'C')) and
                                                           (`flag`.`TABLE_SOURCE` = 'dsl_payment_result') and
                                                           (`flag`.`SYNC_STATUS` = 'S'))) is false and exists(select 1
                                                                                                              from `dsl_inteface_flag` `flag`
                                                                                                              where ((`flag`.`SOURCE_ID` = concat_ws(';', `n`.`bill_no`, 'R')) and
                                                                                                                     (`flag`.`TABLE_SOURCE` = 'dsl_payment_result') and
                                                                                                                     (`flag`.`SYNC_STATUS` = 'S'))) is false)
union all
select `erp`.`SERIAL_NO_ERP`                                                 AS `paymentNo`,
       `erp`.`TENANT_ID`                                                     AS `TENANT_ID`,
       `n`.`bill_no`                                                         AS `paymentVoucherNo`,
       `dtl`.`ID`                                                            AS `flowNo`,
       `erp`.`GX_VOUCHER_TYPE`                                               AS `paymentType`,
       'S'                                                                   AS `paymentStatus`,
       (case
            when ((`av`.`ID` is not null) or (`n`.`is_payed` = '1')) then `n`.`pay_money`
            else `dtl`.`AMT` end)                                            AS `actualPaymentAmount`,
       (case
            when ((`f`.`SYS_VOUCHER_CODE` = '04') and (`n`.`status` = 95))
                then date_format(`dtl`.`TRANS_TIME`, '%Y-%m-%d %H:%i:%s')
            when (`dtl`.`BLEND_SIGN` = '1') then date_format(`dtl`.`TRANS_TIME`, '%Y-%m-%d %H:%i:%s')
            when (`aaa`.`BRIEF_ID` = '002') then `aaa`.`BUSINESS_DATE`
            when (`aa`.`BRIEF_ID` = '001') then `aa`.`BUSINESS_DATE`
            when ((`aa`.`BRIEF_ID` = '010') and (`aa`.`SEND_SIGN` = '1')) then `aa`.`APPLY_DATE`
            when ((`aa`.`BRIEF_ID` = '010') and (`aa`.`SEND_SIGN` = '0')) then `aa`.`BUSINESS_DATE`
            else date_format(`aa`.`BUSINESS_DATE`, '%Y-%m-%d %H:%i:%s') end) AS `actualPaymentDate`,
       `dtl`.`BANK_ACC`                                                      AS `payBankAccountNo`,
       `dtl`.`BANK_NAME`                                                     AS `payBankAccountName`,
       `erp`.`SETTLE_CORP_CODE`                                              AS `company`,
       `erp`.`CORP_CODE`                                                     AS `actualPaymentCompany`,
       (select group_concat(concat(`u`.`USER_CODE`, '+', `u`.`USER_NAME`) order by `u`.`USER_CODE` ASC separator
                            '#') AS `user_info`
        from (`nis_corp_cashier` `c` join `nis_corp_cashier_user` `u`
              on (((`c`.`ID` = `u`.`CORP_CASHIER_ID`) and (`c`.`TENANT_ID` = `u`.`TENANT_ID`))))
        where ((`c`.`STATUS` = 1) and (`erp`.`CORP_CODE` = `c`.`CORP_CODE`))
        group by `c`.`CORP_CODE`)                                            AS `paymentCashierUserNo`,
       `n`.`purpose`                                                         AS `summary`,
       `av`.`ORIG_NO`                                                        AS `billNo`,
       `av`.`SBBLL_RNG_STRT_SN`                                              AS `intervalBeginNo`,
       `av`.`SBBLL_RNG_END_SN`                                               AS `intervalEndNo`,
       `av`.`BILL_MONEY`                                                     AS `billAmount`,
       (case
            when (`av`.`SOURCE_TYPE` = '0') then '1'
            when (`av`.`SOURCE_TYPE` = '1')
                then '2' end)                                                AS `billStatus`,
       `av`.`PAYEEACC`                                                       AS `customerCode`,
       `av`.`PAYEE`                                                          AS `customerName`,
       date_format(`av`.`START_DATE`, '%Y-%m-%d')                            AS `billDate`,
       NULL                                                                  AS `paymentErrReason`,
       NULL                                                                  AS `refundBankFlowNo`,
       NULL                                                                  AS `refundAmount`,
       NULL                                                                  AS `refundFlowTradeDate`,
       NULL                                                                  AS `refundRemitter`,
       NULL                                                                  AS `refundRemitterAccount`,
       NULL                                                                  AS `refundFlowSummary`,
       NULL                                                                  AS `refundFlowBlendingUser`,
       NULL                                                                  AS `refundFlowBlendingDate`,
       NULL                                                                  AS `revokePaymentReason`
from ((((((((((`erp_payinfo` `erp` join `dsl_all_bill_view` `n`
               on (((`n`.`erp_id` = `erp`.`ID`) and (`erp`.`TENANT_ID` = `n`.`TENANT_ID`)))) left join `fc_voucher_type` `f`
              on (((`n`.`voucher_type_id` = `f`.`ID`) and
                   (`erp`.`TENANT_ID` = `f`.`TENANT_ID`)))) left join `bis_exc` `exc`
             on (((`exc`.`CBS_BILL_ID` = `n`.`id`) and
                  (`erp`.`TENANT_ID` = `exc`.`TENANT_ID`)))) left join `bt_charge_back_type` `bt`
            on (((`bt`.`BUS_TYPE` = `erp`.`BUS_TYPE`) and
                 (`bt`.`TENANT_ID` = `erp`.`TENANT_ID`)))) left join `nis_bill_bank_serial_no` `ns`
           on (((`n`.`id` = `ns`.`BILL_ID`) and (`erp`.`TENANT_ID` = `ns`.`TENANT_ID`)))) left join `bis_acc_dtl` `dtl`
          on (((`ns`.`DTL_ID` = `dtl`.`ID`) and (`erp`.`TENANT_ID` = `dtl`.`TENANT_ID`)))) left join `nis_bill` `nb`
         on (((`nb`.`PAY_ID` = `n`.`id`) and (`erp`.`TENANT_ID` = `nb`.`TENANT_ID`)))) left join `ab_accrecords` `aa`
        on (((`aa`.`ID` = `nb`.`BILL_ID`) and (`erp`.`TENANT_ID` = `aa`.`TENANT_ID`)))) left join `ab_voucherinfo` `av`
       on (((`av`.`ID` = `aa`.`VID`) and (`erp`.`TENANT_ID` = `av`.`TENANT_ID`)))) left join `ab_accrecords` `aaa`
      on (((`aa`.`VVID` = `aaa`.`VVID`) and (`aaa`.`BRIEF_ID` = '002') and (`aaa`.`OPT_STATUS` = '19'))))
where ((1 = 1) and (((`aa`.`ID` is not null) and
                     (((`aa`.`SEND_SIGN` = '1') and (`aaa`.`ID` is not null) and (`n`.`status` in (95, 99)) and
                       (`aaa`.`RETURN_CODE` in ('0', '99'))) or
                      ((`aa`.`SEND_SIGN` = '0') and (`aa`.`BRIEF_ID` = '001') and (`aa`.`OPT_STATUS` = '19') and
                       (`n`.`status` in (95, 99))) or ((`aa`.`SEND_SIGN` = '1') and (`aa`.`BRIEF_ID` = '010') and
                                                       (`aa`.`OPT_STATUS` in ('18', '19')) and
                                                       (`n`.`status` in (95, 99))) or
                      ((`aa`.`SEND_SIGN` = '0') and (`aa`.`BRIEF_ID` = '010') and (`aa`.`OPT_STATUS` = '19') and
                       (`n`.`status` in (95, 99))))) or
                    ((`f`.`SYS_VOUCHER_CODE` = '04') and (`n`.`status` = 95) and (`dtl`.`BLEND_SIGN` = '1')) or
                    ((((`n`.`is_payed` = '0') and (`exc`.`VOUCHER_STAT` = '0')) or
                      ((`n`.`is_payed` = '1') and (`n`.`status` = 95))) and (`dtl`.`BLEND_SIGN` = '1') and
                     (`dtl`.`ID` is not null))) and exists(select 1
                                                           from `dsl_inteface_flag` `flag`
                                                           where ((`flag`.`SOURCE_ID` = concat_ws(';', `n`.`bill_no`, 'S')) and
                                                                  (`flag`.`TABLE_SOURCE` = 'dsl_payment_result') and
                                                                  (`flag`.`SYNC_STATUS` = 'S'))) is false)
union all
select `erp`.`SERIAL_NO_ERP`                      AS `paymentNo`,
       `erp`.`TENANT_ID`                          AS `TENANT_ID`,
       `n`.`bill_no`                              AS `paymentVoucherNo`,
       NULL                                       AS `flowNo`,
       `erp`.`GX_VOUCHER_TYPE`                    AS `paymentType`,
       'E'                                        AS `paymentStatus`,
       NULL                                       AS `actualPaymentAmount`,
       NULL                                       AS `actualPaymentDate`,
       NULL                                       AS `payBankAccountNo`,
       NULL                                       AS `payBankAccountName`,
       `erp`.`SETTLE_CORP_CODE`                   AS `company`,
       `erp`.`CORP_CODE`                          AS `actualPaymentCompany`,
       (select group_concat(concat(`u`.`USER_CODE`, '+', `u`.`USER_NAME`) order by `u`.`USER_CODE` ASC separator
                            '#') AS `user_info`
        from (`nis_corp_cashier` `c` join `nis_corp_cashier_user` `u`
              on (((`c`.`ID` = `u`.`CORP_CASHIER_ID`) and (`c`.`TENANT_ID` = `u`.`TENANT_ID`))))
        where ((`c`.`STATUS` = 1) and (`erp`.`CORP_CODE` = `c`.`CORP_CODE`))
        group by `c`.`CORP_CODE`)                 AS `paymentCashierUserNo`,
       `n`.`purpose`                              AS `summary`,
       `av`.`ORIG_NO`                             AS `billNo`,
       `av`.`SBBLL_RNG_STRT_SN`                   AS `intervalBeginNo`,
       `av`.`SBBLL_RNG_END_SN`                    AS `intervalEndNo`,
       `av`.`BILL_MONEY`                          AS `billAmount`,
       `av`.`BILL_STATUS`                         AS `billStatus`,
       `av`.`PAYEEACC`                            AS `customerCode`,
       `av`.`PAYEE`                               AS `customerName`,
       date_format(`av`.`START_DATE`, '%Y-%m-%d') AS `billDate`,
       `exc`.`NEXT_RETURN_MSG`                    AS `paymentErrReason`,
       NULL                                       AS `refundBankFlowNo`,
       NULL                                       AS `refundAmount`,
       NULL                                       AS `refundFlowTradeDate`,
       NULL                                       AS `refundRemitter`,
       NULL                                       AS `refundRemitterAccount`,
       NULL                                       AS `refundFlowSummary`,
       NULL                                       AS `refundFlowBlendingUser`,
       NULL                                       AS `refundFlowBlendingDate`,
       NULL                                       AS `revokePaymentReason`
from (((((`erp_payinfo` `erp` join `dsl_all_bill_view` `n`
          on (((`n`.`erp_id` = `erp`.`ID`) and (`erp`.`TENANT_ID` = `n`.`TENANT_ID`)))) left join `bis_exc` `exc`
         on (((`exc`.`CBS_BILL_ID` = `n`.`id`) and (`erp`.`TENANT_ID` = `exc`.`TENANT_ID`)))) left join `nis_bill` `nb`
        on (((`nb`.`PAY_ID` = `n`.`id`) and (`erp`.`TENANT_ID` = `nb`.`TENANT_ID`)))) left join `ab_accrecords` `aa`
       on (((`aa`.`ID` = `nb`.`BILL_ID`) and (`erp`.`TENANT_ID` = `aa`.`TENANT_ID`)))) left join `ab_voucherinfo` `av`
      on (((`av`.`ID` = `aa`.`VID`) and (`erp`.`TENANT_ID` = `av`.`TENANT_ID`))))
where ((1 = 1) and (`exc`.`VOUCHER_STAT` = '7') and exists(select 1
                                                           from `dsl_inteface_flag` `flag`
                                                           where ((`flag`.`SOURCE_ID` = concat_ws(';', `n`.`bill_no`, 'E')) and
                                                                  (`flag`.`TABLE_SOURCE` = 'dsl_payment_result') and
                                                                  (`flag`.`SYNC_STATUS` = 'S'))) is false)
union all
select `erp`.`SERIAL_NO_ERP`                                       AS `paymentNo`,
       `erp`.`TENANT_ID`                                           AS `TENANT_ID`,
       `n`.`bill_no`                                               AS `paymentVoucherNo`,
       NULL                                                        AS `flowNo`,
       `erp`.`GX_VOUCHER_TYPE`                                     AS `paymentType`,
       'R'                                                         AS `paymentStatus`,
       NULL                                                        AS `actualPaymentAmount`,
       NULL                                                        AS `actualPaymentDate`,
       NULL                                                        AS `payBankAccountNo`,
       NULL                                                        AS `payBankAccountName`,
       `erp`.`SETTLE_CORP_CODE`                                    AS `company`,
       `erp`.`CORP_CODE`                                           AS `actualPaymentCompany`,
       (select group_concat(concat(`u`.`USER_CODE`, '+', `u`.`USER_NAME`) order by `u`.`USER_CODE` ASC separator
                            '#') AS `user_info`
        from (`nis_corp_cashier` `c` join `nis_corp_cashier_user` `u`
              on (((`c`.`ID` = `u`.`CORP_CASHIER_ID`) and (`c`.`TENANT_ID` = `u`.`TENANT_ID`))))
        where ((`c`.`STATUS` = 1) and (`erp`.`CORP_CODE` = `c`.`CORP_CODE`))
        group by `c`.`CORP_CODE`)                                  AS `paymentCashierUserNo`,
       `n`.`purpose`                                               AS `summary`,
       `av`.`ORIG_NO`                                              AS `billNo`,
       `av`.`SBBLL_RNG_STRT_SN`                                    AS `intervalBeginNo`,
       `av`.`SBBLL_RNG_END_SN`                                     AS `intervalEndNo`,
       `av`.`BILL_MONEY`                                           AS `billAmount`,
       `av`.`BILL_STATUS`                                          AS `billStatus`,
       `av`.`PAYEEACC`                                             AS `customerCode`,
       `av`.`PAYEE`                                                AS `customerName`,
       date_format(`av`.`START_DATE`, '%Y-%m-%d')                  AS `billDate`,
       NULL                                                        AS `paymentErrReason`,
       `dtl`.`ID`                                                  AS `refundBankFlowNo`,
       `dtl`.`AMT`                                                 AS `refundAmount`,
       date_format(`dtl`.`TRANS_TIME`, '%Y-%m-%d %H:%i:%s')        AS `refundFlowTradeDate`,
       `dtl`.`OPP_ACC_NAME`                                        AS `refundRemitter`,
       `dtl`.`OPP_ACC_NO`                                          AS `refundRemitterAccount`,
       `dtl`.`REMARK`                                              AS `refundFlowSummary`,
       `dtl`.`REFUND_BLEND_BY`                                     AS `refundFlowBlendingUser`,
       date_format(`dtl`.`REFUND_BLEND_TIME`, '%Y-%m-%d %H:%i:%s') AS `refundFlowBlendingDate`,
       NULL                                                        AS `revokePaymentReason`
from (((((((`erp_payinfo` `erp` join `dsl_all_bill_view` `n`
            on (((`n`.`erp_id` = `erp`.`ID`) and (`erp`.`TENANT_ID` = `n`.`TENANT_ID`)))) left join `bis_exc` `exc`
           on (((`exc`.`CBS_BILL_ID` = `n`.`id`) and
                (`erp`.`TENANT_ID` = `exc`.`TENANT_ID`)))) left join `nis_bill_bank_serial_no_refund` `r`
          on (((`r`.`BILL_ID` = `n`.`id`) and (`erp`.`TENANT_ID` = `r`.`TENANT_ID`)))) left join `bis_acc_dtl` `dtl`
         on (((`dtl`.`ID` = `r`.`DTL_ID`) and (`erp`.`TENANT_ID` = `dtl`.`TENANT_ID`)))) left join `nis_bill` `nb`
        on (((`nb`.`PAY_ID` = `n`.`id`) and (`erp`.`TENANT_ID` = `nb`.`TENANT_ID`)))) left join `ab_accrecords` `aa`
       on (((`aa`.`ID` = `nb`.`BILL_ID`) and (`erp`.`TENANT_ID` = `aa`.`TENANT_ID`)))) left join `ab_voucherinfo` `av`
      on (((`av`.`ID` = `aa`.`VID`) and (`erp`.`TENANT_ID` = `av`.`TENANT_ID`))))
where ((1 = 1) and (`dtl`.`ID` is not null) and (`dtl`.`BLEND_SIGN` = '1') and exists(select 1
                                                                                      from `dsl_inteface_flag` `flag`
                                                                                      where ((`flag`.`SOURCE_ID` = concat_ws(';', `n`.`bill_no`, 'S')) and
                                                                                             (`flag`.`TABLE_SOURCE` = 'dsl_payment_result') and
                                                                                             (`flag`.`SYNC_STATUS` = 'S'))) and
       exists(select 1
              from `dsl_inteface_flag` `flag`
              where ((`flag`.`SOURCE_ID` = concat_ws(';', `n`.`bill_no`, 'R')) and
                     (`flag`.`TABLE_SOURCE` = 'dsl_payment_result') and (`flag`.`SYNC_STATUS` = 'S'))) is false);

DROP VIEW IF EXISTS `standard_bankacc_view`;
CREATE SQL SECURITY INVOKER VIEW `standard_bankacc_view` AS
select `a`.`TENANT_ID`                                               AS `TENANT_ID`,
       `a`.`ID`                                                      AS `id`,
       `a`.`BANK_ACC`                                                AS `bankAcc`,
       `a`.`ACC_NAME`                                                AS `accName`,
       `sc`.`CODE`                                                   AS `corpCode`,
       `sc`.`NAME`                                                   AS `corpName`,
       `d`.`CUR_CODE`                                                AS `curCode`,
       `d`.`CUR_NAME`                                                AS `curName`,
       `a`.`BANK_CODE`                                               AS `bankCode`,
       `a`.`BANK_NAME`                                               AS `bankName`,
       `a`.`IS_ONLINE`                                               AS `isOnline`,
       `a`.`VALID_SIGN`                                              AS `validSign`,
       date_format(ifnull(`a`.`UPDATE_DATE`, sysdate()), '%Y-%m-%d') AS `updateDate`
from (((`bt_bank_acc` `a` left join `sys_corp` `sc`
        on (((`a`.`CORP_ID` = `sc`.`ID`) and (`a`.`TENANT_ID` = `sc`.`TENANT_ID`)))) left join `bt_bank_acc_cur` `c`
       on (((`a`.`ID` = `c`.`BANK_ACC_ID`) and (`a`.`TENANT_ID` = `c`.`TENANT_ID`)))) left join `bt_currency` `d`
      on (((`c`.`CUR_ID` = `d`.`ID`) and (`c`.`TENANT_ID` = `d`.`TENANT_ID`))));

DROP VIEW IF EXISTS `standard_bankinput_view`;
CREATE SQL SECURITY INVOKER VIEW `standard_bankinput_view` AS
select `b`.`TENANT_ID`                                               AS `TENANT_ID`,
       `b`.`ID`                                                      AS `id`,
       `b`.`SYS_BANK_CODE`                                           AS `bankCode`,
       `b`.`BANK_NAME`                                               AS `bankName`,
       `r`.`BANK_PROV`                                               AS `provCode`,
       `r`.`PROV_INPUT_NAME`                                         AS `provName`,
       `r`.`BANK_INPUT_CITY`                                         AS `cityCode`,
       `r`.`NAME`                                                    AS `cityName`,
       date_format(ifnull(`b`.`UPDATE_DATE`, sysdate()), '%Y-%m-%d') AS `updateDate`
from ((`bt_input_bank_info` `b` join `sys_bank_region` `br`
       on (((`b`.`BANK_CITY_CODE` = `br`.`BANK_CITY`) and (`b`.`TENANT_ID` = `br`.`TENANT_ID`)))) join `sys_region` `r`
      on (((`br`.`BANK_INPUT_CITY` = `r`.`BANK_INPUT_CITY`) and (`br`.`TENANT_ID` = `r`.`TENANT_ID`))));

DROP VIEW IF EXISTS `standard_corp_view`;
CREATE SQL SECURITY INVOKER VIEW `standard_corp_view` AS
select `c`.`TENANT_ID`                                                       AS `TENANT_ID`,
       `c`.`CODE`                                                            AS `code`,
       `c`.`NAME`                                                            AS `name`,
       `c`.`STATUS`                                                          AS `status`,
       date_format(ifnull(`c`.`UPDATE_TIME`, `c`.`CREATE_TIME`), '%Y-%m-%d') AS `updateDate`
from `sys_corp` `c`;

DROP VIEW IF EXISTS `standard_item_view`;
CREATE SQL SECURITY INVOKER VIEW `standard_item_view` AS
select `i`.`TENANT_ID`                                                       AS `TENANT_ID`,
       `i`.`ID`                                                              AS `id`,
       `i`.`ITEM_CODE`                                                       AS `item_code`,
       `i`.`ITEM_NAME`                                                       AS `item_name`,
       date_format(ifnull(`i`.`UPDATE_DATE`, `i`.`CREATE_DATE`), '%Y-%m-%d') AS `updateDate`
from `fbs_item` `i`;

DROP VIEW IF EXISTS `v_acc_list`;
CREATE SQL SECURITY INVOKER VIEW `v_acc_list` AS
select `a`.`ID`                                 AS `bank_acc_id`,
       `a`.`BANK_ACC`                           AS `bank_acc`,
       `a`.`ACC_NAME`                           AS `acc_name`,
       (select `b`.`BANK_ACC`
        from `bt_bank_acc` `b`
        where ((`b`.`PARENT_SIGN` = '1') and (`c`.`PRENT_ACC_ID` = `b`.`ID`) and
               (`c`.`CUR_ID` = `d`.`ID`)))      AS `prent_acc`,
       (select `b`.`BANK_ACC`
        from `bt_bank_acc` `b`
        where (`a`.`ENTITY_ACC_ID` = `b`.`ID`)) AS `entity_acc`,
       `a`.`CORP_ID`                            AS `corp_id`,
       `d`.`CUR_CODE`                           AS `cur_code`,
       `c`.`CUR_ID`                             AS `cur_id`,
       `c`.`COUNTER_ACC`                        AS `counter_acc`,
       `c`.`SUPER_COUNTER_ACC`                  AS `super_counter_acc`,
       `a`.`PARENT_SIGN`                        AS `parent_sign`,
       `a`.`ACC_TYPE`                           AS `acc_type`,
       `a`.`BANK_CODE`                          AS `bank_code`,
       `a`.`BANK_NAME`                          AS `bank_name`,
       `e`.`NAME`                               AS `prov`,
       `e`.`ID`                                 AS `provId`,
       `e`.`BANK_PROV`                          AS `provCode`,
       `f`.`NAME`                               AS `city`,
       `f`.`ID`                                 AS `cityId`,
       `e`.`BANK_INPUT_CITY`                    AS `cityCode`,
       `a`.`PRIVATE_NO`                         AS `private_no`,
       `a`.`ORG_CODE`                           AS `org_code`,
       `b`.`BIF_CODE`                           AS `bif_code`,
       `a`.`IS_ONLINE`                          AS `is_online`,
       `a`.`VALID_SIGN`                         AS `valid_sign`,
       `b`.`ID`                                 AS `bifCodeId`,
       `a`.`IS_CAPITAL_POOL`                    AS `is_capital_pool`,
       ''                                       AS `abc_secondary_account`,
       ''                                       AS `primary_account`,
       ''                                       AS `basic_account_sign`,
       `a`.`IS_DOMESTIC_BANK`                   AS `is_domestic_bank`,
       `j`.`NATURE_CODE`                        AS `nature_code`,
       `a`.`BANK_ORG_CODE`                      AS `bank_org_code`,
       `a`.`ATTRIBUTE_ID`                       AS `attribute_id`,
       `a`.`ELECTRIC_BILL`                      AS `electric_bill`,
       `a`.`TENANT_ID`                          AS `tenant_id`,
       `t`.`tenant_code`                        AS `tenant_code`,
       `a`.`BANK_TYPE_ID`                       AS `bank_type_id`,
       `k`.`TYPE_NAME`                          AS `bank_type_name`
from ((((((((`bt_bank_acc` `a` join `bis_bif_init` `b` on ((`a`.`BIF_CODE` = `b`.`ID`))) join `bt_bank_acc_cur` `c`
            on (((`a`.`ID` = `c`.`BANK_ACC_ID`) and (`a`.`TENANT_ID` = `c`.`TENANT_ID`)))) join `bt_currency` `d`
           on (((`c`.`CUR_ID` = `d`.`ID`) and (`a`.`TENANT_ID` = `d`.`TENANT_ID`)))) join `sys_region` `e`
          on ((`a`.`PROV` = `e`.`ID`))) join `sys_region` `f` on ((`a`.`CITY` = `f`.`ID`))) join `bt_acc_nature` `j`
        on (((`a`.`NATURE_ID` = `j`.`ID`) and (`a`.`TENANT_ID` = `j`.`TENANT_ID`)))) join `sys_tenant` `t`
       on ((`a`.`TENANT_ID` = `t`.`id`))) join `bt_bank_type` `k` on ((`a`.`BANK_TYPE_ID` = `k`.`ID`)))
where ((`a`.`STATUS` = '95') and (`t`.`status` = 1))
union all
select `a`.`ID`                                 AS `bank_acc_id`,
       `a`.`BANK_ACC`                           AS `bank_acc`,
       `a`.`ACC_NAME`                           AS `acc_name`,
       (select `b`.`BANK_ACC`
        from `bt_bank_acc` `b`
        where ((`b`.`PARENT_SIGN` = '1') and (`c`.`PRENT_ACC_ID` = `b`.`ID`) and
               (`c`.`CUR_ID` = `d`.`ID`)))      AS `prent_acc`,
       (select `b`.`BANK_ACC`
        from `bt_bank_acc` `b`
        where (`a`.`ENTITY_ACC_ID` = `b`.`ID`)) AS `entity_acc`,
       `a`.`CORP_ID`                            AS `corp_id`,
       `d`.`CUR_CODE`                           AS `cur_code`,
       `c`.`CUR_ID`                             AS `cur_id`,
       `c`.`COUNTER_ACC`                        AS `counter_acc`,
       `c`.`SUPER_COUNTER_ACC`                  AS `super_counter_acc`,
       `a`.`PARENT_SIGN`                        AS `parent_sign`,
       `a`.`ACC_TYPE`                           AS `acc_type`,
       `a`.`BANK_CODE`                          AS `bank_code`,
       `a`.`BANK_NAME`                          AS `bank_name`,
       `e`.`NAME_ZH`                            AS `prov`,
       `e`.`ID`                                 AS `provId`,
       `e`.`CODE`                               AS `provCode`,
       `f`.`NAME_ZH`                            AS `city`,
       `f`.`ID`                                 AS `cityId`,
       `f`.`CODE`                               AS `cityCode`,
       `a`.`PRIVATE_NO`                         AS `private_no`,
       `a`.`ORG_CODE`                           AS `org_code`,
       `b`.`BIF_CODE`                           AS `bif_code`,
       `a`.`IS_ONLINE`                          AS `is_online`,
       `a`.`VALID_SIGN`                         AS `valid_sign`,
       `b`.`ID`                                 AS `bifCodeId`,
       `a`.`IS_CAPITAL_POOL`                    AS `is_capital_pool`,
       ''                                       AS `abc_secondary_account`,
       ''                                       AS `primary_account`,
       ''                                       AS `basic_account_sign`,
       `a`.`IS_DOMESTIC_BANK`                   AS `is_domestic_bank`,
       `j`.`NATURE_CODE`                        AS `nature_code`,
       `a`.`BANK_ORG_CODE`                      AS `bank_org_code`,
       `a`.`ATTRIBUTE_ID`                       AS `attribute_id`,
       `a`.`ELECTRIC_BILL`                      AS `electric_bill`,
       `a`.`TENANT_ID`                          AS `tenant_id`,
       `t`.`tenant_code`                        AS `tenant_code`,
       `a`.`BANK_TYPE_ID`                       AS `bank_type_id`,
       `k`.`TYPE_NAME`                          AS `bank_type_name`
from ((((((((`bt_bank_acc` `a` join `bis_bif_init` `b` on ((`a`.`BIF_CODE` = `b`.`ID`))) join `bt_bank_acc_cur` `c`
            on (((`a`.`ID` = `c`.`BANK_ACC_ID`) and (`a`.`TENANT_ID` = `c`.`TENANT_ID`)))) join `bt_currency` `d`
           on (((`c`.`CUR_ID` = `d`.`ID`) and (`a`.`TENANT_ID` = `d`.`TENANT_ID`)))) join `sys_country_cust` `e`
          on (((`a`.`PROV` = `e`.`ID`) and (`a`.`TENANT_ID` = `e`.`TENANT_ID`)))) join `sys_country_cust` `f`
         on (((`a`.`CITY` = `f`.`ID`) and (`a`.`TENANT_ID` = `f`.`TENANT_ID`)))) join `bt_acc_nature` `j`
        on (((`a`.`NATURE_ID` = `j`.`ID`) and (`a`.`TENANT_ID` = `j`.`TENANT_ID`)))) join `sys_tenant` `t`
       on ((`a`.`TENANT_ID` = `t`.`id`))) join `bt_bank_type` `k` on ((`a`.`BANK_TYPE_ID` = `k`.`ID`)))
where ((`a`.`STATUS` = '95') and (`t`.`status` = 1));

DROP VIEW IF EXISTS `v_bill_merchants_info`;
CREATE SQL SECURITY INVOKER VIEW `v_bill_merchants_info` AS
select `vv`.`TENANT_ID`             AS `TENANT_ID`,
       `vv`.`ID`                    AS `id`,
       `vv`.`CORP_ID`               AS `corp_id`,
       `c`.`NAME`                   AS `corp_name`,
       `v`.`BILL_MONEY`             AS `bill_money`,
       `v`.`START_DATE`             AS `start_date`,
       `v`.`END_DATE`               AS `end_date`,
       `v`.`PAYER`                  AS `payer`,
       `v`.`PAYERACC`               AS `payeracc`,
       `v`.`PAYER_BANK_NAME`        AS `payer_bank_name`,
       `v`.`PAYEE`                  AS `payee`,
       `v`.`PAYEEACC`               AS `payeeacc`,
       `v`.`PAYEE_BANK_NAME`        AS `payee_bank_name`,
       `v`.`ACCEPTOR`               AS `acceptor`,
       `v`.`ACCEPTORACC`            AS `acceptoracc`,
       `v`.`ACCEPTORBANKNO`         AS `acceptorbankno`,
       `v`.`ACCEPTORBANKADDRESS`    AS `acceptorbankaddress`,
       `v`.`A_CONTRACT_NO`          AS `a_contract_no`,
       'abEmitBillPrintServiceImpl' AS `servicebean`
from (((`ab_voucherinfo_vice` `vv` left join `ab_voucherinfo` `v`
        on (((`vv`.`VID` = `v`.`ID`) and (`vv`.`TENANT_ID` = `v`.`TENANT_ID`)))) left join `ab_accrecords` `a`
       on (((`a`.`VVID` = `vv`.`ID`) and (`a`.`TENANT_ID` = `vv`.`TENANT_ID`)))) left join `sys_corp` `c`
      on (((`c`.`ID` = `vv`.`CORP_ID`) and (`c`.`TENANT_ID` = `vv`.`TENANT_ID`))))
where ((`a`.`BRIEF_ID` = '001') and (`a`.`STATUS` = 95) and (`v`.`BILL_TYPE` = '11') and (`v`.`BILL_FLAG` = '10') and
       (`vv`.`VALID_SIGN` = '1'));

DROP VIEW IF EXISTS `v_bis_acc_gather_policy`;
CREATE SQL SECURITY INVOKER VIEW `v_bis_acc_gather_policy` AS
select `a`.`TENANT_ID`            AS `TENANT_ID`,
       `a`.`ID`                   AS `policyId`,
       `b`.`BANK_ACC`             AS `bank_acc`,
       `a`.`BANK_ACC_ID`          AS `bank_acc_id`,
       `c`.`GATHER_POLICY_CODE`   AS `gather_policy_code`,
       `c`.`ID`                   AS `id`,
       `c`.`GATHER_POLICY_NAME`   AS `gather_policy_name`,
       `c`.`DEF_DATE`             AS `def_date`,
       `c`.`LAUNCH_TIME`          AS `launch_time`,
       `c`.`GATHER_TYPE`          AS `gather_type`,
       `c`.`FRQ_MODE`             AS `frq_mode`,
       `c`.`SERIAL_DAY`           AS `serial_day`,
       `c`.`MIN_AMT`              AS `min_amt`,
       `c`.`MAX_AMT`              AS `max_amt`,
       `c`.`GATHER_MODE`          AS `gather_mode`,
       `c`.`GATHER_AMT`           AS `gather_amt`,
       `c`.`DOWN_FIXED_AMT`       AS `down_fixed_amt`,
       `c`.`APP_NEXT_TIME`        AS `app_next_time`,
       `c`.`DIFFER_SCALE`         AS `differ_scale`,
       `c`.`IS_QUICK`             AS `is_quick`,
       `c`.`VALID_SIGN`           AS `valid_sign`,
       `a`.`BIF_CODE`             AS `bif_code_id`,
       `e`.`BIF_CODE`             AS `bif_code`,
       `f`.`BANK_ACC`             AS `parent_acc`,
       `a`.`PARENT_ACC_ID`        AS `parent_acc_id`,
       `a`.`FIRST_TIME`           AS `first_time`,
       `a`.`NEXT_TIME`            AS `next_time`,
       `a`.`APP_NEXT_TIME`        AS `appnext_time`,
       `a`.`BANK_SOURCECODE`      AS `bank_sourcecode`,
       `c`.`CUR_ID`               AS `cur_id`,
       `c`.`AUTO_FILTER_HOLIDAYS` AS `auto_filter_holidays`,
       `c`.`PURPOSE`              AS `purpose`,
       `c`.`ADD_WORD`             AS `add_word`
from ((((`bis_acc_gather_policy` `a` join `bt_bank_acc` `b`) join `bis_gather_policy` `c`) join `bis_bif_init` `e`) join `bt_bank_acc` `f`)
where ((`a`.`BANK_ACC_ID` = `b`.`ID`) and (`a`.`GATHER_POLICY_ID` = `c`.`ID`) and (`a`.`BIF_CODE` = `e`.`ID`) and
       (`a`.`PARENT_ACC_ID` = `f`.`ID`) and (`b`.`VALID_SIGN` = '1') and (`c`.`VALID_SIGN` = '1'));

DROP VIEW IF EXISTS `v_bis_exc`;
CREATE SQL SECURITY INVOKER VIEW `v_bis_exc` AS
select `a`.`ID`                  AS `id`,
       `a`.`VOUCHER_NO`          AS `voucher_no`,
       `a`.`CBS_BILL_ID`         AS `cbs_bill_id`,
       `a`.`CORP_ID`             AS `corp_id`,
       `a`.`REQ_DATE`            AS `req_date`,
       `a`.`PAYER_ACC`           AS `payer_acc`,
       `a`.`PAYER_ACC_NAME`      AS `payer_acc_name`,
       `a`.`PAYER_BANK`          AS `payer_bank`,
       `b`.`NAME`                AS `payer_prov`,
       `c`.`NAME`                AS `payer_city`,
       `e`.`CUR_CODE`            AS `cur_code`,
       `a`.`CUR_ID`              AS `cur_id`,
       `a`.`PAY_MONEY`           AS `pay_money`,
       `f`.`SYS_VOUCHER_CODE`    AS `sys_voucher_code`,
       `a`.`VOUCHER_TYPE_ID`     AS `voucher_type_id`,
       `a`.`PAYEE_ACC`           AS `payee_acc`,
       `a`.`PAYEE_ACC_NAME`      AS `payee_acc_name`,
       `a`.`PAYEE_BANK`          AS `payee_bank`,
       `a`.`PAYEE_BANK_CODE`     AS `payee_bank_code`,
       `g`.`NAME`                AS `payee_prov`,
       `h`.`NAME`                AS `payee_city`,
       `a`.`WISH_PAYDAY`         AS `wish_payday`,
       `a`.`IS_QUICK`            AS `is_quick`,
       `a`.`TRANS_MODE`          AS `trans_mode`,
       `d`.`BIF_CODE`            AS `bif_code`,
       `a`.`BIF_CODE`            AS `bif_code_id`,
       `a`.`DIF_BANK`            AS `dif_bank`,
       `a`.`AREA_SIGN`           AS `area_sign`,
       `a`.`PURPOSE`             AS `purpose`,
       `a`.`add_word`            AS `add_word`,
       `a`.`VOUCHER_FROM`        AS `voucher_from`,
       `a`.`BANK_SERIAL_NO`      AS `bank_serial_no`,
       `a`.`TRANS_TIME`          AS `trans_time`,
       `a`.`COUNTER_ACC`         AS `counter_acc`,
       `a`.`VOUCHER_STAT`        AS `voucher_stat`,
       `a`.`MID_PAYER_ACC_NO`    AS `mid_payer_acc_no`,
       `a`.`MID_PAYER_ACC_NAME`  AS `mid_payer_acc_name`,
       `a`.`MID_PAYER_BANK_NAME` AS `mid_payer_bank_name`,
       `a`.`RESEND_SIGN`         AS `resend_sign`,
       `a`.`DELAY_TIME`          AS `delay_time`,
       `a`.`CBS_BILL_NO`         AS `cbs_bill_no`,
       `a`.`TENANT_ID`           AS `TENANT_ID`,
       `i`.`tenant_code`         AS `TENANT_CODE`
from ((((((((`bis_exc` `a` left join `sys_region` `b` on ((`b`.`ID` = `a`.`PAYER_PROV`))) left join `sys_region` `c`
            on ((`c`.`ID` = `a`.`PAYER_CITY`))) join `bis_bif_init` `d`
           on ((`d`.`ID` = `a`.`BIF_CODE`))) join `bt_currency` `e`
          on (((`e`.`ID` = `a`.`CUR_ID`) and (`a`.`TENANT_ID` = `e`.`TENANT_ID`)))) join `fc_voucher_type` `f`
         on (((`f`.`ID` = `a`.`VOUCHER_TYPE_ID`) and (`a`.`TENANT_ID` = `f`.`TENANT_ID`)))) join `sys_region` `g`
        on ((`g`.`ID` = `a`.`PAYEE_PROV`))) join `sys_region` `h`
       on ((`h`.`ID` = `a`.`PAYEE_CITY`))) join `sys_tenant_manager` `i` on ((`i`.`id` = `a`.`TENANT_ID`)))
where (`i`.`TENANT_STATUS` = 2);

DROP VIEW IF EXISTS `v_bis_exc_payoff`;
CREATE SQL SECURITY INVOKER VIEW `v_bis_exc_payoff` AS
select `a`.`TENANT_ID`        AS `TENANT_ID`,
       `a`.`VOUCHER_NO`       AS `voucher_no`,
       `a`.`CBS_BILL_ID`      AS `cbs_bill_id`,
       `a`.`CORP_ID`          AS `corp_id`,
       `a`.`REQ_DATE`         AS `req_date`,
       `a`.`PAYER_ACC`        AS `payer_acc`,
       `a`.`PAYER_ACC_NAME`   AS `payer_acc_name`,
       `a`.`PAYER_BANK`       AS `payer_bank`,
       `b`.`NAME`             AS `payerProv`,
       `c`.`NAME`             AS `payerCity`,
       `e`.`CUR_CODE`         AS `cur_code`,
       `a`.`CUR_ID`           AS `cur_id`,
       `a`.`PAY_MONEY`        AS `pay_money`,
       `f`.`SYS_VOUCHER_CODE` AS `sys_voucher_code`,
       `a`.`VOUCHER_TYPE_ID`  AS `voucher_type_id`,
       `a`.`WISH_PAYDAY`      AS `wish_payday`,
       `a`.`IS_QUICK`         AS `is_quick`,
       `a`.`TRANS_MODE`       AS `trans_mode`,
       `d`.`BIF_CODE`         AS `bif_code`,
       `a`.`BIF_CODE`         AS `bif_code_id`,
       `a`.`DIF_BANK`         AS `dif_bank`,
       `a`.`AREA_SIGN`        AS `area_sign`,
       `a`.`PURPOSE`          AS `purpose`,
       `a`.`add_word`         AS `add_word`,
       `a`.`VOUCHER_FROM`     AS `voucher_from`,
       `a`.`BANK_SERIAL_NO`   AS `bank_serial_no`,
       `a`.`TRANS_TIME`       AS `trans_time`,
       `a`.`COUNTER_ACC`      AS `counter_acc`,
       `a`.`VOUCHER_STAT`     AS `voucher_stat`
from (((((`bis_exc` `a` join `sys_region` `b`) join `sys_region` `c`) join `bis_bif_init` `d`) join `bt_currency` `e`) join `fc_voucher_type` `f`)
where ((`b`.`ID` = `a`.`PAYER_PROV`) and (`c`.`ID` = `a`.`PAYER_CITY`) and (`e`.`ID` = `a`.`CUR_ID`) and
       (`f`.`ID` = `a`.`VOUCHER_TYPE_ID`) and (`d`.`ID` = `a`.`BIF_CODE`) and (`b`.`TENANT_ID` = `a`.`TENANT_ID`) and
       (`c`.`TENANT_ID` = `a`.`TENANT_ID`) and (`e`.`TENANT_ID` = `a`.`TENANT_ID`) and
       (`f`.`TENANT_ID` = `a`.`TENANT_ID`) and (`d`.`TENANT_ID` = `a`.`TENANT_ID`));

DROP VIEW IF EXISTS `v_bis_finance_exc`;
CREATE SQL SECURITY INVOKER VIEW `v_bis_finance_exc` AS
select `a`.`TENANT_ID`       AS `TENANT_ID`,
       `a`.`ID`              AS `id`,
       `a`.`VOUCHER_NO`      AS `voucher_no`,
       `a`.`CBS_BILL_ID`     AS `cbs_bill_id`,
       `a`.`CBS_BILL_NO`     AS `cbs_bill_no`,
       `a`.`CORP_ID`         AS `corp_id`,
       `a`.`REQ_DATE`        AS `req_date`,
       `b`.`BIF_CODE`        AS `bif_code`,
       `a`.`PAYER_ACC`       AS `payer_acc`,
       `a`.`PAYER_ACC_NAME`  AS `payer_acc_name`,
       `a`.`PAYER_BANK`      AS `payer_bank`,
       `a`.`CUR_ID`          AS `cur_id`,
       `c`.`CUR_CODE`        AS `cur_code`,
       `a`.`PAY_MONEY`       AS `pay_money`,
       `a`.`PURPOSE`         AS `purpose`,
       `a`.`WISH_PAYDAY`     AS `wish_payday`,
       `a`.`DAY_LIMIT`       AS `day_limit`,
       `a`.`DATE_UNIT`       AS `date_unit`,
       `a`.`BURSAR_BILL_NO`  AS `bursar_bill_no`,
       `a`.`INTEREST_RATE`   AS `interest_rate`,
       `a`.`BANK_SERIAL_NO`  AS `bank_serial_no`,
       `a`.`CERTIFICATE_WAY` AS `certificate_way`,
       `a`.`BUS_TYPE`        AS `bus_type`,
       `a`.`OPEN_DATE`       AS `open_date`,
       `a`.`PAYEE_ACC`       AS `payee_acc`,
       `a`.`PAYEE_ACC_NAME`  AS `payee_acc_name`,
       `a`.`CURR_BALANCE`    AS `curr_balance`,
       `a`.`TIME_BALANCE`    AS `time_balance`,
       `a`.`VOUCHER_STAT`    AS `voucher_stat`,
       `a`.`TRANS_TIME`      AS `trans_time`
from ((`bis_finance_exc` `a` join `bis_bif_init` `b`) join `bt_currency` `c`)
where ((`a`.`BIF_CODE` = `b`.`ID`) and (`a`.`CUR_ID` = `c`.`ID`) and (`a`.`TENANT_ID` = `b`.`TENANT_ID`) and
       (`a`.`TENANT_ID` = `c`.`TENANT_ID`));

DROP VIEW IF EXISTS `v_bis_foreign_exc`;
CREATE SQL SECURITY INVOKER VIEW `v_bis_foreign_exc` AS
select `a`.`VOUCHER_NO`             AS `voucher_no`,
       `a`.`CBS_BILL_ID`            AS `cbs_bill_id`,
       `a`.`CBS_BILL_NO`            AS `cbs_bill_no`,
       `a`.`CORP_ID`                AS `corp_id`,
       `a`.`REQ_DATE`               AS `req_date`,
       `a`.`PAY_DATE`               AS `pay_date`,
       `a`.`REMIT_TYPE`             AS `remit_type`,
       `a`.`BANK_TYPE_ID`           AS `bank_type_id`,
       `b`.`BIF_CODE`               AS `bif_code`,
       `a`.`BIF_CODE`               AS `bif_code_id`,
       `a`.`PAYER_ACC`              AS `payer_acc`,
       `a`.`PAYER_ACC_NAME`         AS `payer_acc_name`,
       `a`.`PAYER_BANK`             AS `payer_bank`,
       `a`.`PAYER_NAME`             AS `payer_name`,
       `a`.`PAYER_TEL`              AS `payer_tel`,
       `a`.`CUR_ID`                 AS `cur_id`,
       `c`.`CUR_CODE`               AS `cur_code`,
       `a`.`PAY_MONEY`              AS `pay_money`,
       `a`.`PAYEE_ACC`              AS `payee_acc`,
       `a`.`PAYEE_ACC_NAME`         AS `payee_acc_name`,
       `a`.`PAYEE_BANK`             AS `payee_bank`,
       `a`.`PAYEE_SWIFT_CODE`       AS `payee_swift_code`,
       `d`.`CODE`                   AS `payee_country_code`,
       `a`.`PAYEE_AGENT_ACC`        AS `payee_agent_acc`,
       `a`.`PAYEE_AGENT_BANK`       AS `payee_agent_bank`,
       `a`.`PAYEE_AGENT_BANK_ADD`   AS `payee_agent_bank_add`,
       `a`.`QUICK_SIGN`             AS `quick_sign`,
       `a`.`REMIT_NATURE`           AS `remit_nature`,
       `a`.`REMIT_METHOD`           AS `remit_method`,
       `a`.`COST_SIGN`              AS `cost_sign`,
       `a`.`PAY_TYPE`               AS `pay_type`,
       `a`.`SHIP_DATE`              AS `ship_date`,
       `a`.`IS_BONDED`              AS `is_bonded`,
       `a`.`BUS_NO`                 AS `bus_no`,
       `a`.`ADD_WORD`               AS `add_word`,
       `a`.`RMK`                    AS `rmk`,
       `a`.`CONTRACT_NO`            AS `contract_no`,
       `a`.`INVOICE_NO`             AS `invoice_no`,
       `a`.`TRAN_AMT1`              AS `tran_amt1`,
       `a`.`TRAN_CODE1`             AS `tran_code1`,
       `a`.`TRAN_CUR_ID1`           AS `tran_cur_id1`,
       `f`.`CUR_CODE`               AS `tran_cur_code1`,
       `a`.`TRAN_ADD_WORD1`         AS `tran_add_word1`,
       `a`.`TRAN_CODE2`             AS `tran_code2`,
       `a`.`TRAN_AMT2`              AS `tran_amt2`,
       `a`.`TRAN_CUR_ID2`           AS `tran_cur_id2`,
       `g`.`CUR_CODE`               AS `tran_cur_code2`,
       `a`.`TRAN_ADD_WORD2`         AS `tran_add_word2`,
       `a`.`COST_MODE`              AS `cost_mode`,
       `a`.`DED_ACC`                AS `ded_acc`,
       `a`.`BANK_CUR_CODE`          AS `bank_cur_code`,
       `a`.`INTEREST_DATE`          AS `interest_date`,
       `a`.`BANK_SERIAL_NO`         AS `bank_serial_no`,
       `a`.`VOUCHER_STAT`           AS `voucher_stat`,
       `a`.`VERIFY_CODE`            AS `verify_code`,
       `a`.`VOUCHER_FROM`           AS `voucher_from`,
       `a`.`FEX_BUS_TYPE`           AS `fex_bus_type`,
       `a`.`DIF_BANK`               AS `dif_bank`,
       `a`.`PAYER_NAME_EN`          AS `payer_name_en`,
       `a`.`PAYER_ADDR_EN`          AS `payer_addr_en`,
       `a`.`PAYEE_NAME_EN`          AS `payee_name_en`,
       `a`.`PAYEE_ADDR_EN`          AS `payee_addr_en`,
       `a`.`BANK_ORG_CODE`          AS `bank_Org_Code`,
       `a`.`PAYEE_BANK_CODE`        AS `payee_bank_code`,
       `a`.`PAYER_CUR_ID`           AS `payer_cur_id`,
       `e`.`CUR_CODE`               AS `payerCurCode`,
       `a`.`PAYEE_PROV`             AS `payee_prov`,
       `a`.`PAYEE_CITY`             AS `payee_city`,
       `a`.`RESEND_SIGN`            AS `resend_sign`,
       `a`.`ACC_TYPE`               AS `acc_type`,
       `a`.`PAYEE_TYPE`             AS `payee_type`,
       `a`.`REMIT_ROUTE`            AS `remit_route`,
       `a`.`REMIT_USE`              AS `remit_use`,
       `a`.`FILE_NAME`              AS `file_name`,
       `a`.`BANK_FILE_ID`           AS `bank_file_id`,
       `a`.`ARRIVAL_DATE`           AS `arrival_date`,
       `a`.`DECLARE_NO`             AS `declare_no`,
       `a`.`PAYEE_AGENT_SWIFT_CODE` AS `payee_agent_swift_code`,
       `a`.`TRANS_TIME`             AS `trans_time`,
       `a`.`CERT_TYPE`              AS `cert_type`,
       `a`.`CERT_NO`                AS `cert_no`,
       `d`.`NAME_ZH`                AS `name_zh`,
       `a`.`REMIT_ATTR`             AS `remit_attr`,
       `a`.`BUY_CUR_ID`             AS `buy_cur_id`,
       `buy`.`CUR_CODE`             AS `buyCurCode`,
       `a`.`BUY_AMOUNT`             AS `buy_amount`,
       `a`.`SELL_CUR_ID`            AS `sell_cur_id`,
       `sell`.`CUR_CODE`            AS `sellCurCode`,
       `a`.`FEX_TRAN_TYPE`          AS `fex_tran_type`,
       `a`.`SETTLE_BUS_TYPE`        AS `settle_bus_type`,
       `a`.`ACC_NATURE`             AS `acc_nature`,
       `a`.`DELEGATION_TYPE`        AS `delegationType`,
       `a`.`MAJOR_SOURCE`           AS `majorSource`,
       `a`.`DETAIL_SOURCE`          AS `detailSource`,
       `a`.`BUS_MODE`               AS `busMode`,
       `a`.`SUB_MODE`               AS `subMode`,
       `a`.`CONTRACT_RATE`          AS `contract_rate`,
       `a`.`PAYER_COUNTRY_CODE`     AS `payer_country_code`,
       `a`.`LOCAL_CLEAR_NO`         AS `local_clear_no`,
       `a`.`PAYER_SWIFT_CODE`       AS `payer_swift_code`,
       `a`.`BANK_PAY_TYPE`          AS `bank_pay_type`,
       `a`.`PAYEE_TEL`              AS `payee_tel`,
       `a`.`PAYER_ADDR`             AS `payer_addr`,
       `a`.`FIXED_MONEY`            AS `fixed_money`,
       `a`.`FOREIGN_CONTRACT_NO`    AS `foreign_contract_no`,
       `a`.`PAYER_PROV_NAME`        AS `payer_prov_name`,
       `a`.`PAYER_CITY_NAME`        AS `payer_city_name`,
       `a`.`PAYER_POST_CODE`        AS `payer_post_code`,
       `a`.`PAYER_BUILDING_NUMBER`  AS `payer_building_number`,
       `a`.`PAYER_STREET_NAME`      AS `payer_street_name`,
       `a`.`PAYEE_PROV_NAME`        AS `payee_prov_name`,
       `a`.`PAYEE_CITY_NAME`        AS `payee_city_name`,
       `a`.`PAYEE_POST_CODE`        AS `payee_post_code`,
       `a`.`PAYEE_BUILDING_NUMBER`  AS `payee_building_number`,
       `a`.`PAYEE_STREET_NAME`      AS `payee_street_name`,
       `a`.`SAVE_FIELDS`            AS `save_fields`,
       `a`.`PAYEE_EMAIL`            AS `payee_email`,
       `a`.`PAYEE_CUR_ID`           AS `payee_cur_id`,
       `payee`.`CUR_CODE`           AS `payeeCurCode`,
       `a`.`BNF_RELATE`             AS `bnf_relate`,
       `a`.`RMK_LOCAL`              AS `rmk_local`,
       `a`.`PAYER_LOCAL_CLEAR_NO`   AS `payer_local_clear_no`,
       `a`.`short_name`             AS `short_name`,
       `a`.`PAYEE_BANK_ADD`         AS `payee_bank_add`,
       `a`.`payer_id`               AS `payer_id`,
       `a`.`service_type_id`        AS `service_type_id`,
       `a`.`service_type_name`      AS `service_type_name`,
       `a`.`service_id`             AS `service_id`,
       `a`.`service_name`           AS `service_name`,
       `scc`.`CODE`                 AS `payee_bank_country_code`,
       `a`.`ded_acc_cur_id`         AS `ded_acc_cur_id`,
       `ded`.`CUR_CODE`             AS `dedAccCur`,
       `t`.`id`                     AS `tenant_id`,
       `t`.`tenant_code`            AS `tenant_code`
from ((((((((((((`bis_foreign_exc` `a` join `bis_bif_init` `b` on ((`a`.`BIF_CODE` = `b`.`ID`))) join `bt_currency` `c`
                on (((`a`.`CUR_ID` = `c`.`ID`) and (`a`.`TENANT_ID` = `c`.`TENANT_ID`)))) left join `sys_country_cust` `d`
               on (((`a`.`payee_country_code` = `d`.`ID`) and
                    (`a`.`TENANT_ID` = `d`.`TENANT_ID`)))) left join `bt_currency` `e`
              on (((`a`.`PAYER_CUR_ID` = `e`.`ID`) and
                   (`a`.`TENANT_ID` = `e`.`TENANT_ID`)))) left join `bt_currency` `f`
             on (((`a`.`TRAN_CUR_ID1` = `f`.`ID`) and (`a`.`TENANT_ID` = `f`.`TENANT_ID`)))) left join `bt_currency` `g`
            on (((`a`.`TRAN_CUR_ID2` = `g`.`ID`) and
                 (`a`.`TENANT_ID` = `g`.`TENANT_ID`)))) left join `bt_currency` `buy`
           on (((`a`.`BUY_CUR_ID` = `buy`.`ID`) and
                (`a`.`TENANT_ID` = `buy`.`TENANT_ID`)))) left join `bt_currency` `sell`
          on (((`a`.`SELL_CUR_ID` = `sell`.`ID`) and
               (`a`.`TENANT_ID` = `sell`.`TENANT_ID`)))) left join `bt_currency` `payee`
         on (((`a`.`PAYEE_CUR_ID` = `payee`.`ID`) and
              (`a`.`TENANT_ID` = `payee`.`TENANT_ID`)))) left join `bt_currency` `ded`
        on (((`a`.`ded_acc_cur_id` = `ded`.`ID`) and
             (`a`.`TENANT_ID` = `ded`.`TENANT_ID`)))) left join `sys_country_cust` `scc`
       on (((`a`.`payee_bank_country_code` = `scc`.`ID`) and
            (`a`.`TENANT_ID` = `scc`.`TENANT_ID`)))) join `sys_tenant` `t` on ((`a`.`TENANT_ID` = `t`.`id`)))
where (`t`.`status` = 1);

DROP VIEW IF EXISTS `v_bis_gather_cmd`;
CREATE SQL SECURITY INVOKER VIEW `v_bis_gather_cmd` AS
select `a`.`ID`               AS `id`,
       `a`.`VOUCHER_NO`       AS `voucher_no`,
       `a`.`BIF_CODE`         AS `bif_code_id`,
       `b`.`BIF_CODE`         AS `bif_code`,
       `a`.`CBS_BILL_ID`      AS `cbs_bill_id`,
       `a`.`CHILD_ACC_ID`     AS `child_acc_id`,
       `c`.`BANK_ACC`         AS `child_acc`,
       `a`.`PARENT_ACC_ID`    AS `parent_acc_id`,
       `d`.`BANK_ACC`         AS `parent_acc`,
       `a`.`GATHER_AMT`       AS `gather_amt`,
       `a`.`GATHER_POLICY_ID` AS `gather_policy_id`,
       `a`.`CUR_ID`           AS `cur_id`,
       `e`.`CUR_CODE`         AS `cur_code`,
       `a`.`RUN_TIME`         AS `run_time`,
       `a`.`IS_QUICK`         AS `is_quick`,
       `a`.`DIF_BANK`         AS `dif_bank`,
       `a`.`AREA_SIGN`        AS `area_sign`,
       `a`.`PURPOSE`          AS `purpose`,
       `a`.`ADD_WORD`         AS `add_word`,
       `a`.`VOUCHER_FROM`     AS `voucher_from`,
       `a`.`DTL_ID`           AS `dtl_id`,
       `a`.`CMD_STAT`         AS `cmd_stat`,
       `a`.`TRANS_TIME`       AS `trans_time`,
       `a`.`BANK_SERIAL_NO`   AS `bank_serial_no`,
       `a`.`TENANT_ID`        AS `tenant_id`,
       `t`.`tenant_code`      AS `tenant_code`
from (((((`bis_gather_cmd` `a` join `bis_bif_init` `b` on ((`a`.`BIF_CODE` = `b`.`ID`))) join `bt_bank_acc` `c`
         on (((`a`.`CHILD_ACC_ID` = `c`.`ID`) and (`a`.`TENANT_ID` = `c`.`TENANT_ID`)))) join `bt_bank_acc` `d`
        on (((`a`.`PARENT_ACC_ID` = `d`.`ID`) and (`a`.`TENANT_ID` = `d`.`TENANT_ID`)))) join `bt_currency` `e`
       on (((`a`.`CUR_ID` = `e`.`ID`) and (`a`.`TENANT_ID` = `e`.`TENANT_ID`)))) join `sys_tenant` `t`
      on ((`a`.`TENANT_ID` = `t`.`id`)))
where (`t`.`status` = 1);

DROP VIEW IF EXISTS `v_erp_receive_emit`;
CREATE SQL SECURITY INVOKER VIEW `v_erp_receive_emit` AS
select `a`.`TENANT_ID`                 AS `TENANT_ID`,
       `a`.`BUSINESS_DATE`             AS `BILL_OP_VESTING_DATE`,
       `a`.`APPLY_DATE`                AS `transact_date`,
       `abs`.`BUS_TYPE`                AS `operation_Id`,
       `a`.`ID`                        AS `id`,
       `v`.`ORIG_NO`                   AS `code`,
       `c`.`CODE`                      AS `dept_Code`,
       `vv`.`CORP_ID`                  AS `corp_id`,
       `vv`.`ABS`                      AS `abs`,
       `vv`.`RMK`                      AS `rmk`,
       `c`.`NAME`                      AS `corp_name`,
       `bt_nvl_num`(`a`.`FEEMONEY`, 0) AS `feemoney`,
       `v`.`BILL_TYPE`                 AS `bill_type`,
       `v`.`BILL_FLAG`                 AS `bill_flag`,
       `v`.`BILL_MONEY`                AS `money`,
       `v`.`ACCEPTOR`                  AS `acceptor`,
       `v`.`ACCEPTORBANKNO`            AS `acceptorbankno`,
       `v`.`ACCEPTORACC`               AS `acceptoracc`,
       `v`.`ACCEPTOR_BANK_NAME`        AS `acceptor_bank_name`,
       `v`.`START_DATE`                AS `trans_time`,
       `v`.`END_DATE`                  AS `maturity_date`,
       `v`.`PAYER`                     AS `payer`,
       `v`.`PAYER_BANK_NAME`           AS `payer_bank_name`,
       `v`.`PAYERACC`                  AS `payer_bank_acc`,
       `v`.`PAYEE`                     AS `receiver`,
       `v`.`PAYEEACC`                  AS `receiver_bank_acc`,
       `v`.`PAYEE_BANK_NAME`           AS `receiver_bank_name`,
       `a`.`BILL_SOURCE`               AS `origin`,
       `vv`.`TYPE`                     AS `receOremit`,
       (case
            when (`abs`.`BUS_TYPE` in ('6001', '9002', '9011', '9017', '9018', '9020', '9023')) then `a`.`REQ_NM`
            else '' end)               AS `pre_hand`,
       (case
            when (`abs`.`BUS_TYPE` in
                  ('001', '017', '002', '003', '010', '018', '019', '020', '011', '012', '022', '6201', '6203', '6004',
                   '023')) then `a`.`REC_NM`
            else '' end)               AS `next_hand`,
       `v`.`A_CONTRACT_NO`             AS `pact_no`,
       `v`.`NEW_STM_ISSUOFPPR_IND`     AS `newStmIssuofpprInd`,
       `v`.`BLGRDBAGCRCL_PRMT_IND`     AS `blgrdbagcrclPrmtInd`,
       `v`.`SBBLL_RNG_STRT_SN`         AS `sbbllRngStrtSn`,
       `v`.`SBBLL_RNG_END_SN`          AS `sbbllRngEndSn`,
       `v`.`BL_STD_AMT`                AS `blStdAmt`,
       `v`.`BL_RMTR_UNN_ID_NO`         AS `blRmtrUnnIdNo`
from ((((`ab_voucherinfo_vice` `vv` left join `ab_voucherinfo` `v`
         on (((`vv`.`VID` = `v`.`ID`) and (`vv`.`TENANT_ID` = `v`.`TENANT_ID`)))) left join `ab_accrecords` `a`
        on (((`a`.`VID` = `v`.`ID`) and (`a`.`VVID` = `vv`.`ID`) and
             (`a`.`TENANT_ID` = `vv`.`TENANT_ID`)))) left join `sys_corp` `c`
       on (((`c`.`ID` = `vv`.`CORP_ID`) and (`c`.`TENANT_ID` = `vv`.`TENANT_ID`)))) left join `ab_brief_status` `abs`
      on (((`abs`.`STATUS` = `a`.`BRIEF_ID`) and (`abs`.`TENANT_ID` = `a`.`TENANT_ID`))))
where ((`a`.`STATUS` = 95) and (`abs`.`BUS_TYPE` in ('6001', '010', '6012', '6020', '6120', '004', '6004', '6101')));

DROP VIEW IF EXISTS `v_erp_bill`;
CREATE SQL SECURITY INVOKER VIEW `v_erp_bill` AS
select `hp`.`TENANT_ID`               AS `TENANT_ID`,
       `hp`.`BILL_OP_VESTING_DATE`    AS `BILL_OP_VESTING_DATE`,
       `hp`.`transact_date`           AS `TRANSACT_DATE`,
       `hp`.`operation_Id`            AS `OPERATION_ID`,
       `hp`.`id`                      AS `ID`,
       `hp`.`code`                    AS `CODE`,
       `hp`.`dept_Code`               AS `DEPT_CODE`,
       `hp`.`corp_id`                 AS `CORP_ID`,
       `hp`.`corp_name`               AS `CORP_NAME`,
       `hp`.`abs`                     AS `ABS`,
       `hp`.`rmk`                     AS `RMK`,
       `hp`.`bill_type`               AS `BILL_TYPE`,
       `hp`.`bill_flag`               AS `BILL_FLAG`,
       `hp`.`money`                   AS `MONEY`,
       `hp`.`acceptor`                AS `ACCEPTOR`,
       `hp`.`acceptorbankno`          AS `ACCEPTORBANKNO`,
       `hp`.`acceptoracc`             AS `ACCEPTORACC`,
       `hp`.`acceptor_bank_name`      AS `ACCEPTOR_BANK_NAME`,
       `hp`.`trans_time`              AS `TRANS_TIME`,
       `hp`.`maturity_date`           AS `MATURITY_DATE`,
       `hp`.`payer`                   AS `PAYER`,
       `hp`.`payer_bank_name`         AS `PAYER_BANK_NAME`,
       `hp`.`payer_bank_acc`          AS `PAYER_BANK_ACC`,
       `hp`.`receiver`                AS `RECEIVER`,
       `hp`.`receiver_bank_acc`       AS `RECEIVER_BANK_ACC`,
       `hp`.`receiver_bank_name`      AS `RECEIVER_BANK_NAME`,
       `hp`.`receOremit`              AS `RECEOREMIT`,
       `hp`.`pre_hand`                AS `PRE_HAND`,
       `hp`.`next_hand`               AS `NEXT_HAND`,
       `hp`.`pact_no`                 AS `PACT_NO`,
       `temp`.`TEMPLATE_ID`           AS `template_id`,
       `bt_nvl`(`temp`.`STATUS`, '0') AS `nz_status`,
       `temp`.`SEND_RESON`            AS `send_reson`,
       `temp`.`SEND_BACK_NO`          AS `send_back_no`,
       `temp`.`REVERSE2`              AS `business_type`,
       `temp`.`REVERSE1`              AS `recode_op`,
       `hp`.`newStmIssuofpprInd`      AS `newStmIssuofpprInd`,
       `hp`.`blgrdbagcrclPrmtInd`     AS `blgrdbagcrclPrmtInd`,
       `hp`.`sbbllRngStrtSn`          AS `sbbllRngStrtSn`,
       `hp`.`sbbllRngEndSn`           AS `sbbllRngEndSn`,
       `hp`.`blStdAmt`                AS `blStdAmt`,
       `hp`.`blRmtrUnnIdNo`           AS `blRmtrUnnIdNo`
from (`v_erp_receive_emit` `hp` left join `erp_his_dtl_template` `temp`
      on (((`hp`.`id` = `temp`.`SERIAL_ID`) and (`hp`.`TENANT_ID` = `temp`.`TENANT_ID`))));



DROP VIEW IF EXISTS `v_nis_expense_detail`;
CREATE SQL SECURITY INVOKER VIEW `v_nis_expense_detail` AS
select `a`.`ID`            AS `id`,
       `a`.`EXPENSE_ID`    AS `expense_id`,
       `a`.`BANK_ACC`      AS `bank_acc`,
       `a`.`ACC_NAME`      AS `acc_name`,
       `a`.`BANK_NAME`     AS `bank_name`,
       `a`.`BANK_CODE`     AS `bank_code`,
       `a`.`purpose`       AS `purpose`,
       `a`.`RMK`           AS `rmk`,
       `a`.`EXPENSE_MONEY` AS `expense_money`,
       `a`.`DIF_BANK`      AS `dif_bank`,
       `a`.`AREA_SIGN`     AS `area_sign`,
       `d`.`BIF_CODE`      AS `bif_code`,
       `e`.`NAME`          AS `prov`,
       `f`.`NAME`          AS `city`,
       `a`.`VOUCHER_STAT`  AS `voucher_stat`,
       `a`.`CARD_TYPE`     AS `card_type`,
       `c`.`VOUCHER_NO`    AS `voucher_no`
from (((((`nis_expense_detail` `a` join `nis_billhead` `b`
          on (((`a`.`BILL_ID` = `b`.`ID`) and (`a`.`TENANT_ID` = `b`.`TENANT_ID`)))) join `bis_exc` `c`
         on (((`a`.`BILL_ID` = `c`.`CBS_BILL_ID`) and
              (`a`.`TENANT_ID` = `c`.`TENANT_ID`)))) left join `bis_bif_init` `d`
        on ((`a`.`BIF_CODE` = `d`.`ID`))) left join `sys_region` `e`
       on ((`a`.`PROV` = `e`.`ID`))) left join `sys_region` `f` on ((`a`.`CITY` = `f`.`ID`)));

DROP VIEW IF EXISTS `v_nis_pay_bill_allocation`;
CREATE SQL SECURITY INVOKER VIEW `v_nis_pay_bill_allocation` AS
select `n`.`ID`             AS `id`,
       `n`.`BILL_NO`        AS `billNo`,
       `n`.`CORP_ID`        AS `corpId`,
       `f`.`VOUCHER_NAME`   AS `voucherName`,
       `n`.`PAYER_ACC`      AS `payerAcc`,
       `n`.`PAYER_ACC_NAME` AS `payerAccName`,
       `n`.`CUR_ID`         AS `curId`,
       `n`.`WISH_PAYDAY`    AS `wishPayday`,
       `n`.`PAY_MONEY`      AS `payMoney`,
       `n`.`PAYEE_ACC`      AS `payeeAcc`,
       `n`.`PAYEE_ACC_NAME` AS `payeeAccName`,
       `n`.`PURPOSE`        AS `purpose`,
       `n`.`BUS_TYPE`       AS `busType`,
       `n`.`STATUS`         AS `status`,
       'nis_billhead'       AS `isFrom`
from ((((`nis_billhead` `n` left join `fc_voucher_type` `f`
         on ((`n`.`VOUCHER_TYPE_ID` = `f`.`ID`))) left join `nis_bus_type` `t`
        on ((`n`.`BUS_TYPE` = `t`.`ID`))) left join `bis_exc` `b`
       on (((`n`.`ID` = `b`.`CBS_BILL_ID`) and (`b`.`VOUCHER_FROM` = '1')))) left join `bt_bank_acc` `c`
      on ((`n`.`DOWN_ACC_ID` = `c`.`ID`)))
where ((1 = 1) and (`n`.`STATUS` = (case when (`f`.`IS_ACCOUNT` = 1) then 100 else 95 end)) and ((`n`.`IS_PAYED` = 1) or
                                                                                                 ((`n`.`IS_PAYED` = 0) and
                                                                                                  ((`b`.`VOUCHER_STAT` = '0') or (`b`.`VOUCHER_STAT` is null)))))
union
select `n`.`ID`             AS `id`,
       `n`.`WAGE_NO`        AS `billNo`,
       `n`.`CORP_ID`        AS `corpId`,
       '代发工资'           AS `voucherName`,
       `n`.`PAYER_ACC`      AS `payerAcc`,
       `n`.`PAYER_ACC_NAME` AS `payerAccName`,
       `n`.`CUR_ID`         AS `curId`,
       `n`.`WISH_PAYDAY`    AS `wishPayday`,
       `n`.`WAGES`          AS `payMoney`,
       ''                   AS `payeeAcc`,
       ''                   AS `payeeAccName`,
       '代发工资'           AS `purpose`,
       `n`.`BUS_TYPE`       AS `busType`,
       `n`.`STATUS`         AS `status`,
       'nis_wage'           AS `isFrom`
from `nis_wage` `n`
where ((1 = 1) and (`n`.`STATUS` = 95))
union
select `n`.`ID`            AS `id`,
       `n`.`BILL_NO`       AS `billNo`,
       `n`.`CORP_ID`       AS `corpId`,
       `f`.`VOUCHER_NAME`  AS `voucherName`,
       `n`.`CORP_BANK_ACC` AS `payerAcc`,
       `n`.`CORP_ACC_NAME` AS `payerAccName`,
       `n`.`CUR_ID`        AS `curId`,
       NULL                AS `wishPayday`,
       `n`.`PAY_MONEY`     AS `payMoney`,
       `n`.`COTP_BANK_ACC` AS `payeeAcc`,
       `n`.`COTP_ACC_NAME` AS `payeeAccName`,
       `req`.`PURPOSE`     AS `purpose`,
       `pro`.`PRO_NAME`    AS `busType`,
       `n`.`STATUS`        AS `status`,
       'fex_cash_flow'     AS `isFrom`
from (((`fex_cash_flow` `n` left join `fc_voucher_type` `f`
        on ((`n`.`VOUCHER_TYPE_ID` = `f`.`ID`))) left join `fex_tran_req` `req`
       on ((`req`.`ID` = `n`.`TRAN_BILL_ID`))) left join `fex_product` `pro`
      on ((`req`.`PRO_ID` = `pro`.`ID`)))
where ((1 = 1) and (`n`.`STATUS` = 95) and (`n`.`PAY_STATUS` = '1') and (`n`.`CD_SIGN` = '1'))
union
select `n`.`ID`                                       AS `id`,
       `n`.`BILL_NO`                                  AS `billNo`,
       `n`.`CORP_ID`                                  AS `corpId`,
       (case
            when (`n`.`TYPE` = '10') then '上划'
            when (`n`.`TYPE` = '11') then '下拨'
            when (`n`.`TYPE` = '12') then '请款'
            when (`n`.`TYPE` = '13') then '头寸调拨'
            when (`n`.`TYPE` = '14') then '上缴' end) AS `voucherName`,
       `n`.`PAYER_ACC`                                AS `payerAcc`,
       `n`.`PAYER_ACC_NAME`                           AS `payerAccName`,
       `n`.`CUR_ID`                                   AS `curId`,
       `n`.`WISH_PAYDAY`                              AS `wishPayday`,
       `n`.`MONEY`                                    AS `payMoney`,
       `n`.`PAYEE_ACC`                                AS `payeeAcc`,
       `n`.`PAYEE_ACC_NAME`                           AS `payeeAccName`,
       `n`.`purpose`                                  AS `purpose`,
       NULL                                           AS `busType`,
       `n`.`STATUS`                                   AS `status`,
       'nis_allocation_req'                           AS `isFrom`
from `nis_allocation_req` `n`
where ((1 = 1) and (`n`.`STATUS` = '95'));

DROP VIEW IF EXISTS `v_nis_wage`;
CREATE SQL SECURITY INVOKER VIEW `v_nis_wage` AS
select `a`.`ID`               AS `id`,
       `a`.`VOUCHER_NO`       AS `voucher_no`,
       `a`.`CBS_BILL_ID`      AS `cbs_bill_id`,
       `a`.`CORP_ID`          AS `corp_id`,
       `a`.`REQ_DATE`         AS `req_date`,
       `a`.`PAYER_ACC`        AS `payer_acc`,
       `a`.`PAYER_ACC_NAME`   AS `payer_acc_name`,
       `a`.`PAYER_BANK`       AS `payer_bank`,
       `b`.`NAME`             AS `payer_prov`,
       `c`.`NAME`             AS `payer_city`,
       `a`.`PAYEE_ACC`        AS `payee_acc`,
       `e`.`CUR_CODE`         AS `cur_code`,
       `a`.`CUR_ID`           AS `cur_id`,
       `a`.`PAY_MONEY`        AS `pay_money`,
       `f`.`SYS_VOUCHER_CODE` AS `sys_voucher_code`,
       `a`.`VOUCHER_TYPE_ID`  AS `voucher_type_id`,
       `a`.`WISH_PAYDAY`      AS `wish_payday`,
       `a`.`IS_QUICK`         AS `is_quick`,
       `a`.`TRANS_MODE`       AS `trans_mode`,
       `d`.`BIF_CODE`         AS `bif_code`,
       `a`.`BIF_CODE`         AS `bif_code_id`,
       `a`.`DIF_BANK`         AS `dif_bank`,
       `a`.`AREA_SIGN`        AS `area_sign`,
       `a`.`PURPOSE`          AS `purpose`,
       `a`.`add_word`         AS `add_word`,
       `a`.`VOUCHER_FROM`     AS `voucher_from`,
       `a`.`BANK_SERIAL_NO`   AS `bank_serial_no`,
       `a`.`TRANS_TIME`       AS `trans_time`,
       `a`.`COUNTER_ACC`      AS `counter_acc`,
       `a`.`VOUCHER_STAT`     AS `voucher_stat`,
       `a`.`RESEND_SIGN`      AS `resend_sign`,
       `a`.`DELAY_TIME`       AS `delay_time`,
       `a`.`CBS_BILL_NO`      AS `cbs_bill_no`,
       `a`.`TENANT_ID`        AS `TENANT_ID`,
       `i`.`tenant_code`      AS `TENANT_CODE`
from ((((((`bis_exc` `a` left join `sys_region` `b` on ((`b`.`ID` = `a`.`PAYER_PROV`))) left join `sys_region` `c`
          on ((`c`.`ID` = `a`.`PAYER_CITY`))) join `bis_bif_init` `d`
         on ((`d`.`ID` = `a`.`BIF_CODE`))) join `bt_currency` `e`
        on (((`e`.`ID` = `a`.`CUR_ID`) and (`a`.`TENANT_ID` = `e`.`TENANT_ID`)))) join `fc_voucher_type` `f`
       on (((`f`.`ID` = `a`.`VOUCHER_TYPE_ID`) and (`a`.`TENANT_ID` = `f`.`TENANT_ID`)))) join `sys_tenant` `i`
      on ((`i`.`id` = `a`.`TENANT_ID`)))
where (`i`.`status` = 1);

DROP VIEW IF EXISTS `v_nis_wage_detail`;
CREATE SQL SECURITY INVOKER VIEW `v_nis_wage_detail` AS
select `a`.`ID`           AS `id`,
       `a`.`WAGE_DTL_NO`  AS `wage_dtl_no`,
       `a`.`BANK_ACC`     AS `bank_acc`,
       `a`.`ACC_NAME`     AS `acc_name`,
       `a`.`BANK_NAME`    AS `bank_name`,
       `a`.`BANK_CODE`    AS `bank_code`,
       `a`.`purpose`      AS `purpose`,
       `a`.`WAGES_VERIFY` AS `wages_verify`,
       `a`.`DIF_BANK`     AS `dif_bank`,
       `a`.`AREA_SIGN`    AS `area_sign`,
       `d`.`BIF_CODE`     AS `bif_code`,
       `e`.`NAME`         AS `prov`,
       `f`.`NAME`         AS `city`,
       `a`.`VOUCHER_STAT` AS `voucher_stat`,
       `a`.`WORK_NO`      AS `work_no`,
       `a`.`ID_NO`        AS `id_no`,
       `a`.`ID_TYPE`      AS `id_type`,
       `c`.`VOUCHER_NO`   AS `voucher_no`
from (((((`nis_wage_detail` `a` join `nis_wage` `b`
          on (((`a`.`WAGE_ID` = `b`.`ID`) and (`a`.`TENANT_ID` = `b`.`TENANT_ID`)))) join `bis_exc` `c`
         on (((`a`.`VOUCHER_NO` = `c`.`VOUCHER_NO`) and
              (`a`.`TENANT_ID` = `c`.`TENANT_ID`)))) left join `bis_bif_init` `d`
        on ((`c`.`BIF_CODE` = `d`.`ID`))) left join `sys_region` `e`
       on ((`a`.`PROV` = `e`.`ID`))) left join `sys_region` `f` on ((`a`.`CITY` = `f`.`ID`)));

DROP VIEW IF EXISTS `view_createbusiness_info`;
CREATE SQL SECURITY INVOKER VIEW `view_createbusiness_info` AS
select `a`.`TENANT_ID`           AS `TENANT_ID`,
       `a`.`BANKINTF_CODE`       AS `bankintf_code`,
       `a`.`TRADE_CODE`          AS `trade_code`,
       `b`.`TRADE_NAME`          AS `trade_name`,
       `a`.`RUNDATE_RANGE`       AS `rundate_range`,
       `a`.`RUNTIME_RANGE`       AS `runtime_range`,
       `a`.`RUNSECOND_RANGE`     AS `runsecond_range`,
       `a`.`TRIGGER_INTEVAL`     AS `trigger_inteval`,
       `a`.`TRADE_INTEVAL`       AS `trade_inteval`,
       `a`.`THREAD_NUM`          AS `thread_num`,
       `a`.`BATCH_NUM`           AS `batch_num`,
       `a`.`BEFORE_RANGE`        AS `before_range`,
       `a`.`RUN_FLAG`            AS `run_flag`,
       `b`.`MONITOR_LQ`          AS `monitor_lq`,
       `b`.`DEFAULT_LQ`          AS `default_lq`,
       `b`.`TRIGG_IMPLCLASS`     AS `trigg_implclass`,
       `b`.`EXCEPTION_TIMERANGE` AS `exception_timerange`,
       `b`.`TRADE_TYPE`          AS `trade_type`,
       `b`.`INUSE_FLAG`          AS `inuse_flag`,
       `b`.`BATCH_FLAG`          AS `BATCH_FLAG`,
       `b`.`MUXTHREAD_FLAG`      AS `MUXTHREAD_FLAG`,
       `b`.`TIMERANGE_FLAG`      AS `TIMERANGE_FLAG`,
       `b`.`TRADEOF_BANKINTF`    AS `TRADEOF_BANKINTF`,
       `b`.`SHOW_ORDER`          AS `show_order`
from (`bp_bankinft_trade` `a` join `bp_trade_baseinfo` `b`)
where ((`a`.`TRADE_CODE` = `b`.`TRADE_CODE`) and (`a`.`TENANT_ID` = `b`.`TENANT_ID`))
order by `b`.`SHOW_ORDER`;

DROP VIEW IF EXISTS `view_createbusiness_info_bill`;
CREATE SQL SECURITY INVOKER VIEW `view_createbusiness_info_bill` AS
select `a`.`TENANT_ID`           AS `TENANT_ID`,
       `a`.`BANKINTF_CODE`       AS `bankintf_code`,
       `a`.`TRADE_CODE`          AS `trade_code`,
       `b`.`TRADE_NAME`          AS `trade_name`,
       `a`.`RUNDATE_RANGE`       AS `rundate_range`,
       `a`.`RUNTIME_RANGE`       AS `runtime_range`,
       `a`.`RUNSECOND_RANGE`     AS `runsecond_range`,
       `a`.`TRIGGER_INTEVAL`     AS `trigger_inteval`,
       `a`.`TRADE_INTEVAL`       AS `trade_inteval`,
       `a`.`THREAD_NUM`          AS `thread_num`,
       `a`.`BATCH_NUM`           AS `batch_num`,
       `a`.`BEFORE_RANGE`        AS `before_range`,
       `a`.`RUN_FLAG`            AS `run_flag`,
       `b`.`MONITOR_LQ`          AS `monitor_lq`,
       `b`.`DEFAULT_LQ`          AS `default_lq`,
       `b`.`TRIGG_IMPLCLASS`     AS `trigg_implclass`,
       `b`.`EXCEPTION_TIMERANGE` AS `exception_timerange`,
       `b`.`TRADE_TYPE`          AS `trade_type`,
       `b`.`INUSE_FLAG`          AS `inuse_flag`,
       `b`.`BATCH_FLAG`          AS `BATCH_FLAG`,
       `b`.`MUXTHREAD_FLAG`      AS `MUXTHREAD_FLAG`,
       `b`.`TIMERANGE_FLAG`      AS `TIMERANGE_FLAG`,
       `b`.`TRADEOF_BANKINTF`    AS `TRADEOF_BANKINTF`,
       `b`.`SHOW_ORDER`          AS `show_order`
from (`bp_bankinft_trade_bill` `a` join `bp_trade_baseinfo_bill` `b`)
where ((`a`.`TRADE_CODE` = `b`.`TRADE_CODE`) and (`a`.`TENANT_ID` = `b`.`TENANT_ID`))
order by `b`.`SHOW_ORDER`;

DROP VIEW IF EXISTS `view_erp_payinfo`;
CREATE SQL SECURITY INVOKER VIEW `view_erp_payinfo` AS
select `e`.`TENANT_ID`      AS `TENANT_ID`,
       `e`.`SERIAL_NO_ERP`  AS `serial_no_erp`,
       `e`.`VOUCHER_NO_ERP` AS `voucher_no_erp`,
       `n`.`BILL_NO`        AS `bill_no`,
       `n`.`PAYER_ACC`      AS `payer_acc`,
       `n`.`PAYER_ACC_NAME` AS `payer_acc_name`,
       `n`.`PAYER_BANK`     AS `payer_bank`,
       `n`.`PAY_MONEY`      AS `pay_money`,
       `b`.`TRANS_TIME`     AS `trans_time`,
       `b`.`RETURN_TIME`    AS `return_time`,
       (case
            when (`b`.`VOUCHER_STAT` = '0') then 'S'
            when (`n`.`STATUS` = -(2)) then `e`.`BUS_TYPE`
            else 'P' end)   AS `status`,
       `t`.`RMK`            AS `state_desc`,
       `e`.`BACK_DATE`      AS `back_date`
from (((`erp_payinfo` `e` left join `nis_billhead` `n`
        on (((`n`.`ERP_ID` = `e`.`ID`) and (`n`.`TENANT_ID` = `e`.`TENANT_ID`)))) left join `bis_exc` `b`
       on (((`n`.`ID` = `b`.`CBS_BILL_ID`) and
            (`n`.`TENANT_ID` = `b`.`TENANT_ID`)))) left join `bt_charge_back_type` `t`
      on (((`e`.`BUS_TYPE` = `t`.`BUS_TYPE`) and (`e`.`TENANT_ID` = `t`.`TENANT_ID`))));

DROP VIEW IF EXISTS `view_wage_temp`;
CREATE SQL SECURITY INVOKER VIEW `view_wage_temp` AS
select `t`.`TENANT_ID`        AS `TENANT_ID`,
       `t`.`ID`               AS `ID`,
       `t`.`WAGE_DTL_NO`      AS `WAGE_DTL_NO`,
       `t`.`WAGE_ID`          AS `WAGE_ID`,
       `t`.`WORK_NO`          AS `WORK_NO`,
       `t`.`USER_NAME`        AS `USER_NAME`,
       `t`.`BANK_ACC`         AS `BANK_ACC`,
       `t`.`ACC_NAME`         AS `ACC_NAME`,
       `t`.`WAGES_VERIFY`     AS `WAGES_VERIFY`,
       `t`.`purpose`          AS `purpose`,
       `t`.`PROV`             AS `PROV`,
       `t`.`CITY`             AS `CITY`,
       `t`.`BANK_CODE`        AS `BANK_CODE`,
       `t`.`BANK_NAME`        AS `BANK_NAME`,
       `t`.`DIF_BANK`         AS `DIF_BANK`,
       `t`.`AREA_SIGN`        AS `AREA_SIGN`,
       `t`.`VOUCHER_STAT`     AS `VOUCHER_STAT`,
       `t`.`RETURN_TIME`      AS `RETURN_TIME`,
       `t`.`FIRST_RETURN_MSG` AS `FIRST_RETURN_MSG`,
       `t`.`NEXT_RETURN_MSG`  AS `NEXT_RETURN_MSG`,
       `t`.`CREATE_BY`        AS `CREATE_BY`,
       `t`.`CREATE_DATE`      AS `CREATE_DATE`,
       `t`.`UPDATE_BY`        AS `UPDATE_BY`,
       `t`.`UPDATE_DATE`      AS `UPDATE_DATE`,
       `t`.`VOUCHER_NO`       AS `VOUCHER_NO`,
       `s`.`WAGES`            AS `amt`
from (`nis_wage_detail` `t` join `erp_wage_detail` `s`)
where ((`t`.`ID` = `s`.`ID`) and (`t`.`TENANT_ID` = `s`.`TENANT_ID`));

DROP VIEW IF EXISTS `view_erp_wage`;
CREATE SQL SECURITY INVOKER VIEW `view_erp_wage` AS
select `e`.`TENANT_ID`       AS `TENANT_ID`,
       `e`.`SERIAL_NO_ERP`   AS `serial_no_erp`,
       `e`.`VOUCHER_NO_ERP`  AS `voucher_no_erp`,
       `n`.`WAGE_NO`         AS `wage_no`,
       `d`.`WAGE_DTL_NO`     AS `wage_dtl_no`,
       `d`.`ID`              AS `id`,
       `d`.`amt`             AS `amt`,
       `n`.`PAYER_ACC`       AS `payer_acc`,
       `n`.`PAYER_ACC_NAME`  AS `payer_acc_name`,
       `e`.`PAYER_BANK_NAME` AS `payer_bank`,
       `n`.`WAGES`           AS `wages`,
       `b`.`TRANS_TIME`      AS `trans_time`,
       `b`.`RETURN_TIME`     AS `return_time`,
       (case
            when (`d`.`VOUCHER_STAT` = '0') then 'S'
            when (`n`.`STATUS` = -(2)) then `e`.`BUS_TYPE`
            else 'P' end)    AS `status`,
       `t`.`RMK`             AS `state_desc`,
       `e`.`BACK_DATE`       AS `back_date`
from ((((`erp_payinfo` `e` left join `nis_wage` `n`
         on (((`n`.`ERP_ID` = `e`.`ID`) and (`n`.`TENANT_ID` = `e`.`TENANT_ID`)))) left join `bt_charge_back_type` `t`
        on (((`e`.`BUS_TYPE` = `t`.`BUS_TYPE`) and (`e`.`TENANT_ID` = `t`.`TENANT_ID`)))) left join `bis_exc` `b`
       on (((`n`.`ID` = `b`.`CBS_BILL_ID`) and (`n`.`TENANT_ID` = `b`.`TENANT_ID`)))) left join `view_wage_temp` `d`
      on (((`n`.`ID` = `d`.`WAGE_ID`) and (`n`.`TENANT_ID` = `d`.`TENANT_ID`))))
where (`e`.`VOUCHER_TYPE` = '48');

DROP VIEW IF EXISTS `view_nis_cash_bill`;
CREATE SQL SECURITY INVOKER VIEW `view_nis_cash_bill` AS
select `n`.`TENANT_ID`               AS `TENANT_ID`,
       `n`.`ID`                      AS `id`,
       `n`.`BILL_NO`                 AS `bill_no`,
       `n`.`CORP_ID`                 AS `corp_id`,
       `c`.`NAME`                    AS `corp_name`,
       `n`.`VOUCHER_TYPE_ID`         AS `voucher_type_id`,
       `t`.`VOUCHER_TYPE`            AS `voucher_type`,
       `t`.`VOUCHER_NAME`            AS `voucher_name`,
       `n`.`ACC_ID`                  AS `acc_id`,
       `n`.`PAYER_ACC`               AS `payer_acc`,
       `n`.`PAYER_ACC_NAME`          AS `payer_acc_name`,
       `n`.`PAYER_BANK`              AS `payer_bank`,
       `n`.`CUR_ID`                  AS `cur_id`,
       `cur`.`CUR_NAME`              AS `cur_name`,
       `n`.`PAY_MONEY`               AS `pay_money`,
       `n`.`PAYEE_ACC`               AS `payee_acc`,
       `n`.`PAYEE_ACC_NAME`          AS `payee_acc_name`,
       `n`.`PAYEE_BANK_CODE`         AS `payee_bank_code`,
       `n`.`PAYEE_BANK`              AS `payee_bank`,
       `r`.`PROV_INPUT_NAME`         AS `prov`,
       `r`.`NAME`                    AS `city`,
       `n`.`WISH_PAYDAY`             AS `wish_payday`,
       `n`.`IS_QUICK`                AS `is_quick`,
       `n`.`IS_FORINDIVIDUAL`        AS `is_forindividual`,
       `n`.`BIF_CODE`                AS `bif_code`,
       `b`.`NAME`                    AS `bif_name`,
       `n`.`DIF_BANK`                AS `dif_bank`,
       `n`.`AREA_SIGN`               AS `area_sign`,
       `n`.`BURSAR_BILL_NO`          AS `bursar_bill_no`,
       `n`.`PURPOSE`                 AS `purpose`,
       'nisBillheadPrintServiceImpl' AS `servicebean`
from (((((`nis_billhead` `n` left join `sys_corp` `c`
          on (((`n`.`CORP_ID` = `c`.`ID`) and (`n`.`TENANT_ID` = `c`.`TENANT_ID`)))) left join `fc_voucher_type` `t`
         on (((`n`.`VOUCHER_TYPE_ID` = `t`.`ID`) and
              (`n`.`TENANT_ID` = `t`.`TENANT_ID`)))) left join `bt_currency` `cur`
        on (((`n`.`CUR_ID` = `cur`.`ID`) and (`n`.`TENANT_ID` = `cur`.`TENANT_ID`)))) left join `sys_region` `r`
       on (((`n`.`CITY` = `r`.`ID`) and (`n`.`TENANT_ID` = `r`.`TENANT_ID`)))) left join `bis_bif_init` `b`
      on (((`n`.`BIF_CODE` = `b`.`ID`) and (`n`.`TENANT_ID` = `b`.`TENANT_ID`))))
where ((`n`.`STATUS` = '95') and (`t`.`SYS_VOUCHER_CODE` = '62'));

DROP VIEW IF EXISTS `view_nis_tele_bill`;
CREATE SQL SECURITY INVOKER VIEW `view_nis_tele_bill` AS
select `n`.`TENANT_ID`               AS `TENANT_ID`,
       `n`.`ID`                      AS `id`,
       `n`.`BILL_NO`                 AS `bill_no`,
       `n`.`CORP_ID`                 AS `corp_id`,
       `c`.`NAME`                    AS `corp_name`,
       `n`.`VOUCHER_TYPE_ID`         AS `voucher_type_id`,
       `t`.`VOUCHER_TYPE`            AS `voucher_type`,
       `t`.`VOUCHER_NAME`            AS `voucher_name`,
       `n`.`ACC_ID`                  AS `acc_id`,
       `n`.`PAYER_ACC`               AS `payer_acc`,
       `n`.`PAYER_ACC_NAME`          AS `payer_acc_name`,
       `n`.`PAYER_BANK`              AS `payer_bank`,
       `n`.`CUR_ID`                  AS `cur_id`,
       `cur`.`CUR_NAME`              AS `cur_name`,
       `n`.`PAY_MONEY`               AS `pay_money`,
       `n`.`PAYEE_ACC`               AS `payee_acc`,
       `n`.`PAYEE_ACC_NAME`          AS `payee_acc_name`,
       `n`.`PAYEE_BANK_CODE`         AS `payee_bank_code`,
       `n`.`PAYEE_BANK`              AS `payee_bank`,
       `r`.`PROV_INPUT_NAME`         AS `prov`,
       `r`.`NAME`                    AS `city`,
       `n`.`WISH_PAYDAY`             AS `wish_payday`,
       `n`.`IS_QUICK`                AS `is_quick`,
       `n`.`IS_FORINDIVIDUAL`        AS `is_forindividual`,
       `n`.`BIF_CODE`                AS `bif_code`,
       `b`.`NAME`                    AS `bif_name`,
       `n`.`DIF_BANK`                AS `dif_bank`,
       `n`.`AREA_SIGN`               AS `area_sign`,
       `n`.`BURSAR_BILL_NO`          AS `bursar_bill_no`,
       `n`.`PURPOSE`                 AS `purpose`,
       'nisBillheadPrintServiceImpl' AS `servicebean`
from (((((`nis_billhead` `n` left join `sys_corp` `c`
          on (((`n`.`CORP_ID` = `c`.`ID`) and (`n`.`TENANT_ID` = `c`.`TENANT_ID`)))) left join `fc_voucher_type` `t`
         on (((`n`.`VOUCHER_TYPE_ID` = `t`.`ID`) and
              (`n`.`TENANT_ID` = `t`.`TENANT_ID`)))) left join `bt_currency` `cur`
        on (((`n`.`CUR_ID` = `cur`.`ID`) and (`n`.`TENANT_ID` = `cur`.`TENANT_ID`)))) left join `sys_region` `r`
       on (((`n`.`CITY` = `r`.`ID`) and (`n`.`TENANT_ID` = `r`.`TENANT_ID`)))) left join `bis_bif_init` `b`
      on (((`n`.`BIF_CODE` = `b`.`ID`) and (`n`.`TENANT_ID` = `b`.`TENANT_ID`))))
where ((`n`.`STATUS` = '95') and (`t`.`SYS_VOUCHER_CODE` = '06'));

DROP VIEW IF EXISTS `view_nis_tran_bill`;
CREATE SQL SECURITY INVOKER VIEW `view_nis_tran_bill` AS
select `n`.`TENANT_ID`               AS `TENANT_ID`,
       `n`.`ID`                      AS `id`,
       `n`.`BILL_NO`                 AS `bill_no`,
       `n`.`CORP_ID`                 AS `corp_id`,
       `c`.`NAME`                    AS `corp_name`,
       `n`.`VOUCHER_TYPE_ID`         AS `voucher_type_id`,
       `t`.`VOUCHER_TYPE`            AS `voucher_type`,
       `t`.`VOUCHER_NAME`            AS `voucher_name`,
       `n`.`ACC_ID`                  AS `acc_id`,
       `n`.`PAYER_ACC`               AS `payer_acc`,
       `n`.`PAYER_ACC_NAME`          AS `payer_acc_name`,
       `n`.`PAYER_BANK`              AS `payer_bank`,
       `n`.`CUR_ID`                  AS `cur_id`,
       `cur`.`CUR_NAME`              AS `cur_name`,
       `n`.`PAY_MONEY`               AS `pay_money`,
       `n`.`PAYEE_ACC`               AS `payee_acc`,
       `n`.`PAYEE_ACC_NAME`          AS `payee_acc_name`,
       `n`.`PAYEE_BANK_CODE`         AS `payee_bank_code`,
       `n`.`PAYEE_BANK`              AS `payee_bank`,
       `r`.`PROV_INPUT_NAME`         AS `prov`,
       `r`.`NAME`                    AS `city`,
       `n`.`WISH_PAYDAY`             AS `wish_payday`,
       `n`.`IS_QUICK`                AS `is_quick`,
       `n`.`IS_FORINDIVIDUAL`        AS `is_forindividual`,
       `n`.`BIF_CODE`                AS `bif_code`,
       `b`.`NAME`                    AS `bif_name`,
       `n`.`DIF_BANK`                AS `dif_bank`,
       `n`.`AREA_SIGN`               AS `area_sign`,
       `n`.`BURSAR_BILL_NO`          AS `bursar_bill_no`,
       `n`.`PURPOSE`                 AS `purpose`,
       'nisBillheadPrintServiceImpl' AS `servicebean`
from (((((`nis_billhead` `n` left join `sys_corp` `c`
          on (((`n`.`CORP_ID` = `c`.`ID`) and (`n`.`TENANT_ID` = `c`.`TENANT_ID`)))) left join `fc_voucher_type` `t`
         on (((`n`.`VOUCHER_TYPE_ID` = `t`.`ID`) and
              (`n`.`TENANT_ID` = `t`.`TENANT_ID`)))) left join `bt_currency` `cur`
        on (((`n`.`CUR_ID` = `cur`.`ID`) and (`n`.`TENANT_ID` = `cur`.`TENANT_ID`)))) left join `sys_region` `r`
       on (((`n`.`CITY` = `r`.`ID`) and (`n`.`TENANT_ID` = `r`.`TENANT_ID`)))) left join `bis_bif_init` `b`
      on (((`n`.`BIF_CODE` = `b`.`ID`) and (`n`.`TENANT_ID` = `b`.`TENANT_ID`))))
where ((`n`.`STATUS` = '95') and (`t`.`SYS_VOUCHER_CODE` = '63'));

DROP VIEW IF EXISTS `view_payee_cheque`;
CREATE SQL SECURITY INVOKER VIEW `view_payee_cheque` AS
select `p`.`TENANT_ID`                                        AS `TENANT_ID`,
       `p`.`ID`                                               AS `id`,
       `p`.`CORP_ID`                                          AS `corp_id`,
       `c`.`CODE`                                             AS `corpCode`,
       `c`.`NAME`                                             AS `corpName`,
       `p`.`ORIG_NO`                                          AS `origNo`,
       `p`.`BILL_MONEY`                                       AS `billMoney`,
       `p`.`INPUT_DATE`                                       AS `inputDate`,
       `p`.`PAYER`                                            AS `payer`,
       `p`.`PAYER_ACC`                                        AS `payerAcc`,
       `p`.`PAYER_BANK_NO`                                    AS `payerBankNo`,
       `p`.`PAYER_BANK_NAME`                                  AS `payerBankName`,
       `p`.`PAYEE`                                            AS `payee`,
       `p`.`PAYEE_ACC`                                        AS `payeeAcc`,
       `p`.`PAYEE_BANK_NO`                                    AS `payeeBankNo`,
       `p`.`PAYEE_BANK_NAME`                                  AS `payeeBankName`,
       `p`.`USE`                                              AS `USE_RMK`,
       `p`.`START_DATE`                                       AS `startDate`,
       `p`.`END_DATE`                                         AS `endDate`,
       `cur`.`CUR_NAME`                                       AS `curName`,
       `p`.`CUR_EXRATE`                                       AS `curExrate`,
       (`bt_nvl_num`(`p`.`CUR_EXRATE`, 1) * `p`.`BILL_MONEY`) AS `curMoney`
from ((`bv_payee_cheque` `p` left join `sys_corp` `c`
       on (((`p`.`CORP_ID` = `c`.`ID`) and (`p`.`TENANT_ID` = `c`.`TENANT_ID`)))) left join `bt_currency` `cur`
      on (((`cur`.`ID` = `p`.`CUR_ID`) and (`cur`.`TENANT_ID` = `p`.`TENANT_ID`))))
where (`p`.`STATUS` = 5);


