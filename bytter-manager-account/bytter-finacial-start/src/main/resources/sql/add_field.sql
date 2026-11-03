# 核算管理 新增冗余字段 2023.6.5
# fc_accbook_blned 主档表不能冗余
-- alter table `fc_accbook_blned` add `USER_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
-- alter table `fc_accbook_blned` add `CORP_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
-- alter table `fc_accbook_blned` add `ACC_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
-- alter table `fc_accbook_blned` add `SUBJECT_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
-- alter table `fc_accbook_blned` add `SUBJECT_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
-- alter table `fc_accbook_blned` add `CUR_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';

alter table `fc_accbook_blned_dtl` add `SUBJECT_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_accbook_blned_dtl` add `SUBJECT_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_accbook_blned_dtl` add `BANK_CODE` varchar(32) DEFAULT NULL COMMENT '银行类别code';
alter table `fc_accbook_blned_dtl` add `SYS_SUBJECT_CODE` varchar(32) DEFAULT NULL COMMENT '科目代码code';
alter table `fc_accbook_blned_dtl` add `NET_ID` varchar(32) DEFAULT NULL COMMENT '网点id';

alter table `fc_accbook_dtl` add `ACC_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_accbook_dtl` add `SUBJECT_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_accbook_dtl` add `SUBJECT_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_accbook_dtl` add `CORP_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_accbook_dtl` add `BALANCE_WAY` decimal(1) DEFAULT NULL COMMENT '冗余字段-余额方向';
alter table `fc_accbook_dtl` add `CUR_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_accbook_dtl` add `CUR_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_accbook_dtl` add `NET_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';

# 主档表不能冗余
-- alter table `fc_accbook` add `USER_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
-- alter table `fc_accbook` add `CORP_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
-- alter table `fc_accbook` add `LOGIN_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
-- alter table `fc_accbook` add `BALANCE_WAY` decimal(1) DEFAULT NULL COMMENT '冗余字段';

alter table `fc_carry` add `USER_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';

# user_name 不能随意冗余,有2方
-- alter table `fc_charge_accrue` add `USER_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_charge_accrue` add `CUR_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_charge_accrue` add `CUR_CODE` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_charge_accrue` add `ACC_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';

alter table `fc_acc_balance` add `NET_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';

alter table `fc_interest_dtl_temp` add `ACC_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_interest_dtl_temp` add `ACC_TYPE` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_interest_dtl_temp` add `NET_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_interest_dtl_temp` add `CORP_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';

alter table `fc_interest_dtl` add `ACC_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_interest_dtl` add `ACC_TYPE` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_interest_dtl` add `NET_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_interest_dtl` add `CORP_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_interest_dtl` add `CUR_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_interest_dtl` add `CUR_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';

alter table `fc_predraw_dtl` add `ACC_TYPE` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_predraw_dtl` add `NET_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_predraw_dtl` add `ACC_NAME` varchar(500)  DEFAULT NULL COMMENT '账号名称' after account;

alter table `fc_out_accbook_dtl` add `acc_name` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_out_accbook_dtl` add `subject_id` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_out_accbook_dtl` add `subject_name` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_out_accbook_dtl` add `corp_id` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_out_accbook_dtl` add `balance_way` decimal(1) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_out_accbook_dtl` add `cur_id` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_out_accbook_dtl` add `cur_name` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_out_accbook_dtl` add `measure_unit` varchar(32) DEFAULT NULL COMMENT '冗余字段';

alter table `fc_less_pay_bill` add `ACC_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_less_pay_bill` add `ACC_FOR_DEDUCT` varchar(32) DEFAULT NULL COMMENT '冗余字段';

alter table `fc_online_ledger` add `ACC_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_online_ledger` add `ACC_FOR_DEDUCT` varchar(32) DEFAULT NULL COMMENT '冗余字段';

alter table `fc_interest_single_nod_book` add `NOTICE_ACC_TYPE` varchar(32) DEFAULT NULL COMMENT '冗余字段';

alter table `FC_INTEREST_SINGLE` add `CUR_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `FC_INTEREST_SINGLE` add `NET_ID` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `FC_INTEREST_SINGLE` add `POOL_SIGN` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `FC_INTEREST_SINGLE` add `ACC_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';

# 2023-9-16
-- 新增 主键索引
alter table FC_BUSINESS_RULE_DEFINE_POOL add primary key (`id`);

-- 2023.9.18核算模块,记账规则管理 bt_account
insert ignore into fc_business_rule_init(ID,NET_ID,SYS_RACE_CODE,SUBJ_CODE,CUR_CODE,VALID_SIGN,CREATE_TIME,CREATE_BY,UPDATE_TIME,UPDATE_BY,VOUCHER_TYPE)
values('80975da1404d4fc7bd6158282b7abf5a','','2035',null,null,1,null,null,null,null,'375911349ac440b28abb4dab32f17eb8'),('c32571eb86db486da3a77b375d050a94','','2036',null,null,1,null,null,null,null,'375911349ac440b28abb4dab32f17eb8');


# 记账平台
alter table `bt_net_chalk` add `CORP_TYPE` decimal(1) DEFAULT NULL COMMENT '冗余字段';

# 基础模块
alter table `fc_subject_day` add `NET_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fc_subject_day` add `CUR_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段';


-- 2023.10.10 记账规则管理 新增初始化脚本
insert ignore into FC_BUSINESS_RULE_INIT (ID, NET_ID, SYS_RACE_CODE,SUBJ_CODE, CUR_CODE, VALID_SIGN, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, VOUCHER_TYPE)
values('130d9aa2df014f45ae2960fb78b3da14',null,'1012',null,null,1,null,null,null,null,'c4c6708bcbc94fea92db69badad76361'),('1a9cedaa78ff4b3fbce3aade38a8396c',null,'1013',null,null,1,null,null,null,null,'c4c6708bcbc94fea92db69badad76361'),('91a64fb497374ba49476e04e942479c0',null,'1015',null,null,1,null,null,null,null,'c4c6708bcbc94fea92db69badad76361'),('d39749459ed345ec99b98147b76d1327',null,'1014',null,null,1,null,null,null,null,'c4c6708bcbc94fea92db69badad76361');

insert ignore into FC_VOUCHER_ENTRY_INIT (ID, BUSINESS_RULE_DEFINE_ID,BELONG_TYPE, BALANCE_WAY, SUBJECT_CODE, ACCOUNT, CUR_CODE, SUMMARY, ENTRY_TYPE,GROUP_NUM, CREATE_TIME, CREATE_BY, UPDATE_TIME, UPDATE_BY, VOUCHER_TYPE)
    values('456a7c76293e473d93a4fd2f2aad25a4','91a64fb497374ba49476e04e942479c0',20,1,'4028802f5c677fa7015c678761f9005b',null,null,null,30,4,null,null,null,null,null),
          ('45da260b39764c44bb7e7ee5d0c0a396','d39749459ed345ec99b98147b76d1327',20,1,'4028802f5c677fa7015c678761fb006f',null,null,null,10,2,null,null,null,null,null),
          ('46fb4d85f1f44d41ab0b4fd5c1eb173b','91a64fb497374ba49476e04e942479c0',20,-1,'4028802f5c677fa7015c678761fc0072',null,null,null,30,5,null,null,null,null,null),
          ('713ce8df2521491482960224a3e63afb','1a9cedaa78ff4b3fbce3aade38a8396c',20,1,'4028802f5c677fa7015c678761fd0078',null,null,null,30,4,null,null,null,null,null),
          ('753e61ca9fc240c09097d412f5222e9f','1a9cedaa78ff4b3fbce3aade38a8396c',20,1,'4028802f5c677fa7015c678761fd0078',null,null,null,40,1,null,null,null,null,null),
          ('865e0dd894014d379b848742c08ef525','1a9cedaa78ff4b3fbce3aade38a8396c',20,-1,'4028802f5c677fa7015c678761fa0066',null,null,null,30,5,null,null,null,null,null),
          ('a076914634f041a495aa1db73576ea0f','130d9aa2df014f45ae2960fb78b3da14',20,-1,'4028802f5c677fa7015c678761fa0065',null,null,null,30,4,null,null,null,null,null),
          ('a2bddceed9dc473ca289e1bdc699bab9','d39749459ed345ec99b98147b76d1327',20,1,'4028802f5c677fa7015c678761fd007a',null,null,null,30,4,null,null,null,null,null),
          ('abc81cc57e014c59a21d4b604b11ee14','1a9cedaa78ff4b3fbce3aade38a8396c',20,1,'4028802f5c677fa7015c678761fb006e',null,null,null,10,2,null,null,null,null,null),
          ('b4cbf9fc53db40cc8cccb305daed44e4','d39749459ed345ec99b98147b76d1327',20,1,'4028802f5c677fa7015c678761fd007a',null,null,null,40,1,null,null,null,null,null),
          ('be670068cde04ec2800dc2fe598aafa5','130d9aa2df014f45ae2960fb78b3da14',20,1,'4028802f5c677fa7015c678761fd0079',null,null,null,30,3,null,null,null,null,null),
          ('c17cf6b8a1b5443d8d1918d3c0ef11a8','130d9aa2df014f45ae2960fb78b3da14',20,1,'4028802f5c677fa7015c678761fd0079',null,null,null,10,1,null,null,null,null,null),
          ('c68da18ac52f4e308455204324ac748a','91a64fb497374ba49476e04e942479c0',20,-1,'4028802f5c677fa7015c678761fa0061',null,null,null,10,2,null,null,null,null,null),
          ('cb265f9bee61429cbd6c9a3edb884c7b','130d9aa2df014f45ae2960fb78b3da14',20,-1,'4028802f5c677fa7015c678761fa0065',null,null,null,10,2,null,null,null,null,null),
          ('d66df2be8b354c24b7f51986df6f7252','d39749459ed345ec99b98147b76d1327',20,-1,'4028802f5c677fa7015c678761fa0067',null,null,null,30,5,null,null,null,null,null),
          ('f26a4428256d485a86af89c799b1c2b7','91a64fb497374ba49476e04e942479c0',20,-1,'4028802f5c677fa7015c678761fc0072',null,null,null,40,3,null,null,null,null,null),
          ('fae34c1e155d47bfb9a5cad2a3873d69','91a64fb497374ba49476e04e942479c0',20,1,'4028802f5c677fa7015c678761fb006d',null,null,null,10,1,null,null,null,null,null),
          ('fe3dcf8de5774d3ab5d3a57191ab9a02','d39749459ed345ec99b98147b76d1327',20,-1,'4028802f5c677fa7015c678761fb006d',null,null,null,10,3,null,null,null,null,null),
          ('ffb0e1bd0e2e4792b5a2822c56076671','1a9cedaa78ff4b3fbce3aade38a8396c',20,-1,'4028802f5c677fa7015c678761fb006d',null,null,null,10,3,null,null,null,null,null);

# 基础模块 2023.11.20
# 插入银企接口记账状态 6=改账打回
insert ignore into sys_dict(ID,TYPE_CODE,TYPE_NAME,TYPE_ID,TYPE_GROUP_ID,SORT,CREATE_DATE,CREATE_NAME)
value ('04800f17284b4d469d7209e234491196','6','改账打回',null,'17ee3720dcf042c28bfd9bf5ba947089',500,null,null);
# 国际化
insert into SYS_INTERNATION_BASE(KEY_ID,LANG_CODE,LANG_CONTEXT) values('04800f17284b4d469d7209e234491196','en_US','Reject posting change'),('04800f17284b4d469d7209e234491196','zh_CN','改账打回');


