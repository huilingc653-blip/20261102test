

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882111', 'zh_CN', '不可拆分票据不允许做子票区间拆分！', 'ab.bill.excel.errormsg.not.split' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='ab.bill.excel.errormsg.not.split' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882112', 'zh_CN', '开始纸票区间必须小于结束子票区间！', 'ab.bill.excel.errormsg.split.start.be.end' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='ab.bill.excel.errormsg.split.start.be.end' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882113', 'zh_CN', '持有票据子票必须包含拆分子票区间！', 'ab.bill.excel.errormsg.old.not.include.split' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='ab.bill.excel.errormsg.old.not.include.split' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882114', 'zh_CN', '拆分金额和子票区间不匹配！', 'ab.bill.excel.errormsg.transmoney.not.sbbllrng' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='ab.bill.excel.errormsg.transmoney.not.sbbllrng' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882115', 'zh_CN', '拆分子票区间存在交叉！', 'ab.bill.excel.errormsg.split.sbbllrng.rep' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='ab.bill.excel.errormsg.split.sbbllrng.rep' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882116', 'zh_CN', '交易金额不能大于票面金额！', 'accebill.ticket.money.gre.bill.money.error' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='accebill.ticket.money.gre.bill.money.error' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882117', 'zh_CN', '子票区间必须重开始进行拆分！', 'accebill.ticket.sbbllrng.not.bigin.error' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='accebill.ticket.sbbllrng.not.bigin.error' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882118', 'zh_CN', '交易金额不允许超过可拆分金额！', 'accebill.ticket.transmoney.gt.billmoney.error' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='accebill.ticket.transmoney.gt.billmoney.error' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1745e9d05b0b462586cd8de70c882119', 'zh_CN', '原电票详细', 'bill.accebill.electronbill.oldinfo' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.accebill.electronbill.oldinfo' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO


##修改人：liuhb
##修改内容：增加国际化
##修改时间：2023-11-29
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '1a272a13cdfa468f972e43aae736c00e', 'zh_CN', '入账户名', 'table.abDiscount.aoaccninfIdName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.abDiscount.aoaccninfIdName' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'e1ebd5e939df4c32994c46b771f74abf', 'en_US', 'Account name', 'table.abDiscount.aoaccninfIdName' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.abDiscount.aoaccninfIdName' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人:zhous
##修改内容:V23090144 添加定时任务 线上账户票据对内背书给线下账户自动登记
##修改时间:2022-10-07
INSERT INTO sys_task (ID, TASKNAME, ACTIVE, DELETEDSW, TYPE, COMMAND, ARGUMENTS, PERIOD, SECOND, MINUTE, HOUR, DAYS, MONTH, WEEK, STARTDTM, ENDDTM, TASKOWNER, EMAIL_REMINDER)
SELECT '61f5b84413574a2382615dbf92646782', '线上账户票据对内背书给线下账户自动登记', 0, 0, null, 'com.bytter.biz.bill.accebill.service.AbBillTaskService', '2023-10-01', 1, '0', '0/1', '*', '*', '*', '?', '2023-10-07', '2050-10-07', null, null FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM sys_task WHERE ID = '61f5b84413574a2382615dbf92646782' LIMIT 1
);
COMMIT;
##GOGO

##修改人：zhengg
##修改内容：票据认领国际化异常完善
##修改时间：2023-10-09
UPDATE sys_multi_lang SET lang_key='bill.accebill.abClaim.transferClaim',lang_context='确认转待认领吗？' WHERE lang_key = 'bill.accebill.transferClaim';
##GOGO

UPDATE sys_multi_lang SET lang_key='bill.accebill.abClaim.confirmClaim',lang_context='确认认领吗？' WHERE lang_key = 'bill.accebill.confirmClaim';
##GOGO

##修改人：zhous
##修改内容：V23090144 【洋河股份】持票账户为线上，背书给内部线下账户后需要收票登记，建议根据票据状态自动更新库存票据信息
##修改时间：2023-10-07
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'bd8907f7278b43bb8873f9e2a95a1805', 'zh_CN', '日期参数未设置或格式异常，应为yyyy-MM-dd格式', 'date.format.error' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='date.format.error' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f3d', 'zh_CN', '拆票结果查询', 'bill.button.newBillSplitResultSearch' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.button.newBillSplitResultSearch' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f4d', 'en_US', 'New Bill Split Result Search', 'bill.button.newBillSplitResultSearch' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.button.newBillSplitResultSearch' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f5d', 'zh_CN', '是否需要合并', 'bill.accebill.isneedmerge' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.accebill.isneedmerge' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f6d', 'en_US', 'Is_Need_Merge', 'bill.accebill.isneedmerge' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.accebill.isneedmerge' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f7d', 'zh_CN', '可以根据此列内容判断是否需要合并操作', 'bill.isneedmerge.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.isneedmerge.desc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f8d', 'en_US', 'You can determine whether a merge operation is needed based on the content of this column', 'bill.isneedmerge.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.isneedmerge.desc' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f9d', 'zh_CN', '将新一代票据合并后回退。可根据列表中“是否需要合并”一列的提示判断是否需要此操作', 'bill.mergeReturn.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.mergeReturn.desc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f0f', 'en_US', 'Merge and roll back the new generation of bills. You can determine whether this operation is needed based on the prompt in the "Do you need to merge" column in the list', 'bill.mergeReturn.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.mergeReturn.desc' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f1f', 'zh_CN', '手动发送指令同步银行拆票结果，结果在列表“是否需要合并”一列中展示', 'bill.newBillSplitResultSearch.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.newBillSplitResultSearch.desc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f2f', 'en_US', 'Manually send instructions to synchronize the bank split results, and the results are displayed in the "Do you need to merge" column in the list', 'bill.newBillSplitResultSearch.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.newBillSplitResultSearch.desc' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f3f', 'zh_CN', '新一代票据会拆分后回退。可根据列表中“是否需要合并”一列的提示判断是否需要此操作', 'bill.repulse.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.repulse.desc' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-19
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2281ebacb7a9400fbb39105ce7b81f4f', 'en_US', 'The new generation of bills will be split and refunded. You can determine whether this operation is needed based on the prompt in the "Do you need to merge" column in the list', 'bill.repulse.desc' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.repulse.desc' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：
##修改时间：2023-07-19
insert into SYS_DICT_GROUP (ID,TYPE_GROUP_CODE,TYPE_GROUP_NAME)
select '195916229fa84e9a9412b44e0d8d5fd1', 'NEW_BILL_MERGEPROPOSAL', '新票是否需要合并' from dual
where not exists (select 1 from SYS_DICT_GROUP where id = '195916229fa84e9a9412b44e0d8d5fd1' limit 1);
##GOGO

##修改人：zmc
##修改内容：
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd1', '0', '此票据不需合并', '', '195916229fa84e9a9412b44e0d8d5fd1', '1' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd1');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd2', '1', '此业务不需合并', '', '195916229fa84e9a9412b44e0d8d5fd1', '2' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd2');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd3', '2', '此阶段不需合并', '', '195916229fa84e9a9412b44e0d8d5fd1', '3' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd3');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd4', '3', '查询失败，请重发指令查询', '', '195916229fa84e9a9412b44e0d8d5fd1', '4' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd4');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd5', '4', '建议合并', '', '195916229fa84e9a9412b44e0d8d5fd1', '5' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd5');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd6', '5', '不需合并', '', '195916229fa84e9a9412b44e0d8d5fd1', '6' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd6');
##GOGO

##修改人：zmc
##修改内容： 0,此票据不需合并  1,此业务不需合并  2,此阶段不需合并  3,查询失败，请重发指令查询  4，建议合并  5，不需合并  6,正在同步银行结果
##修改时间：2023-07-19
insert into SYS_DICT (ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT)
select '295916229fa84e9a9412b44e0d8d5fd7', '6', '正在同步银行结果', '', '195916229fa84e9a9412b44e0d8d5fd1', '7' from dual
where not exists (select 1 from SYS_DICT where id = '295916229fa84e9a9412b44e0d8d5fd7');
##GOGO

##修改人：qijh
##修改内容：增加国际化
##修改时间：2023-07-27
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '7ae93de3577c4799b4711557846381d1', 'zh_CN', '流水与单据金额不符，请核实', 'bankacc.message.paymentblending.money.diff' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bankacc.message.paymentblending.money.diff' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：qijh
##修改内容：增加国际化
##修改时间：2023-07-27
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '25e0aeb4234742009680f60dcde90d2d', 'zh_CN', '回单与流水金额不符，请核实', 'bankacc.message.receiptBlending.money.diff' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bankacc.message.receiptBlending.money.diff' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b81g3f', 'zh_CN', '请注意！第$张票的建议操作是“建议合并”。', 'newbill.operate.prompt_1' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_1' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b82g3f', 'en_US', 'Please pay attention! The recommended action for the $th ticket is "Suggest merger".', 'newbill.operate.prompt_1' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_1' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b83g3f', 'zh_CN', '请注意！第$张票的建议操作是“不需合并”。', 'newbill.operate.prompt_2' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_2' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b84g3f', 'en_US', 'Please pay attention! The recommended action for the $th ticket is "no need to merge".', 'newbill.operate.prompt_2' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_2' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b85g3f', 'zh_CN', '请注意！第$张票拆票结果同步失败。', 'newbill.operate.prompt_3' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_3' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b86g3f', 'en_US', 'Please pay attention! The synchronization of the splitting result of the $th ticket failed.', 'newbill.operate.prompt_3' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_3' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b87g3f', 'zh_CN', '请注意！第$张票拆票正在同步银行结果。', 'newbill.operate.prompt_4' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_4' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zmc
##修改内容：增加国际化
##修改时间：2023-07-31
insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '2r83ebacb7a9400fbb39105ce7b88g3f', 'en_US', 'Please pay attention! The opening of the $th ticket is synchronizing the bank results.', 'newbill.operate.prompt_4' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='newbill.operate.prompt_4' AND LANG_CODE = 'en_US' limit 1);
##GOGO

##修改人:zhumc
##修改时间:2023-06-29
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
DELIMITER $$
CREATE PROCEDURE PRO_ADD_COL()
BEGIN

IF not EXISTS (select 1 from SYS_MULTI_LANG where id = 'C807A3B073B949E1BDF8F0450442C940') THEN
    insert into SYS_MULTI_LANG (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
values ('C807A3B073B949E1BDF8F0450442C940', 'en_US', 'Drawer Bank Category', 'table.bill.bankType.payerBankType', null, null, null, null, null, null);
END IF;

IF not EXISTS (select 1 from SYS_MULTI_LANG where id = 'C807A3B073B949E1BDF8F0450442C941') THEN
    insert into SYS_MULTI_LANG (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY, CREATE_DATE, CREATE_BY, CREATE_NAME, UPDATE_DATE, UPDATE_BY, UPDATE_NAME)
values ('C807A3B073B949E1BDF8F0450442C941', 'zh_CN', '出票人银行类别', 'table.bill.bankType.payerBankType', null, null, null, null, null, null);
END IF;

END $$
DELIMITER ;
CALL PRO_ADD_COL();
DROP PROCEDURE IF EXISTS PRO_ADD_COL;
##GOGO

##修改人：zhous
##修改内容：V23080138【深南电路】-票据查询条件优化 增加国际化
##修改时间：2023-08-22

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '66d77a8e6b294d599cee2b86dfb3bbc6', 'zh_CN', '子票区间', 'bill.accebill.sti' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='bill.accebill.sti' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##修改人：zhous
##修改内容：V23080115 【青岛宜博铜业】增加开票登记快速选择到期日期及同步出票数量功能  增加国际化
##修改时间：2023-08-23

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select 'fc6f7d50e9b6431d9381642d36626055', 'zh_CN', '3个月', 'table.abVoucherinfo.endDate.threeMonth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.abVoucherinfo.endDate.threeMonth' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '3bd458fc88ed4d3fad70544fd2301280', 'zh_CN', '6个月', 'table.abVoucherinfo.endDate.sixMonth' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='table.abVoucherinfo.endDate.sixMonth' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

insert into sys_multi_lang (ID, LANG_CODE, LANG_CONTEXT, LANG_KEY)
select '67f15ec6638b4533b0f28490ee0fcfda', 'zh_CN', '请先选择出票日期！', 'accebill.message.startDate.isnotselect' from dual
where not exists (select 1 from sys_multi_lang where LANG_KEY ='accebill.message.startDate.isnotselect' AND LANG_CODE = 'zh_CN' limit 1);
##GOGO

##完善应收票据台账针对入池票据做为质押出去 zhengg 20230829
INSERT INTO ab_brief_status (ID, STATUS, STATUSNAME, BUS_TYPE)
select 103, '8018', '入池申请', '018' from dual
where not exists (select 1 from ab_brief_status where STATUS ='8018' limit 1);
##GOGO