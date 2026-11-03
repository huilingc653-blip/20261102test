
declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'FC_SUBJECT'
  and column_name = 'ACC_TYPE';
if v_count = 1 then
        v_str := 'ALTER TABLE FC_SUBJECT MODIFY ACC_TYPE VARCHAR2(6)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column FC_SUBJECT.ACC_TYPE is ''账户类型''';
end if;
end;
/

declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'GUA_EXGUARANTEE_AMOUNT'
  and column_name = 'PROPORTION';
if v_count = 1 then
        v_str := 'ALTER TABLE GUA_EXGUARANTEE_AMOUNT MODIFY PROPORTION NUMBER(8, 6)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column GUA_EXGUARANTEE_AMOUNT.PROPORTION is ''比例''';
end if;
end;
/


declare v_count number;
    v_str varchar2(1000);
begin
select count(*)
into v_count
from user_tab_columns
where table_name = 'sys_attachment_manager'
  and column_name = 'MD5';
if v_count < 1 then
        v_str := 'alter table sys_attachment_manager add MD5 varchar2(64)';
EXECUTE IMMEDIATE v_str;
EXECUTE IMMEDIATE 'comment on column sys_attachment_manager.MD5 is ''MD5''';
end if;
end;
/