#!/bin/sh
filepath=$1
database=$2
echo "文件路径：：：$filepath"
echo "备份库名：：：：$database"
i=0
tables=
for line in `cat $filepath`
do
  table=$line
  tables[$i]=$table
  ((i++))
done
table_str=$(echo "${tables[*]}")
echo "需要备份表：：：：：$table_str"
/usr/local/mysql/bin/mysqldump -ut33 -p123456 -P3306 --databases t33 --tables $table_str > table_bak.sql
/usr/local/mysql/bin/mysql -ut33 -p123456 -P3306 $database < table_bak.sql

