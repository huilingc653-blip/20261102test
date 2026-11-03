##修改人:zrs
##修改内容:修改字段类型
##修改时间:2025-2-10
ALTER TABLE BIS_PAY_POLICY add COLUMN IS_WORK_DAY varchar (1) DEFAULT '0';
##GOGO

UPDATE sys_multi_lang SET  LANG_CONTEXT='单位：{0}存在相同币种相同收款人的策略,不允许存在同时启用多个自动支付策略!'
WHERE ID='849f6b7490594cef9610c810aa837ac6';
##GOGO
