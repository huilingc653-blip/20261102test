-- 集团票据池单位冗余字段
alter table AB_POOL_CORP add `CORP_CODE` varchar(32) null comment '单位编号（冗余）' after CORP_ID;

-- 池接口冗余
alter table AB_POOLIF add `BIF_ID` varchar(32) default '' null comment '接口代码ID（冗余）' after BIF_CODE;


















