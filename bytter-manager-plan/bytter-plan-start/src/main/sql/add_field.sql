# 新增冗余字段 2023.4.17
alter table `fbs_budget` add `CORP_CODE` varchar(32) DEFAULT NULL COMMENT '冗余字段';
alter table `fbs_budget` add `CORP_NAME` varchar(255) DEFAULT NULL COMMENT '冗余字段';
alter table `fbs_budget` add `CUR_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段-币别名称';

alter table `fbs_budget_process` add `CORP_NAME` varchar(255) DEFAULT NULL COMMENT '冗余字段';
alter table `fbs_budget_process` add `CUR_NAME` varchar(32) DEFAULT NULL COMMENT '冗余字段-币别名称';
