##修改人:zhaohc
##修改内容:添加利息流水规则表
##修改时间:2024-05-07

CREATE TABLE IF NOT EXISTS `fc_interest_dtl_rule`
(
    id           varchar(32)  not null comment '主键ID' primary key,
    bank_type_id varchar(32)  null comment '银行类别',
    bank_name    varchar(150) null comment '银行名称',
    remark       varchar(150) null comment '用途/摘要',
    trans_time   datetime     null comment '入账日期',
    valid_sign   varchar(1)   null comment '有效标志',
    create_date  datetime     null comment '创建时间',
    create_by    varchar(32)  null comment '创建人',
    update_date  datetime     null comment '修改时间',
    update_by    varchar(32)  null comment '修改人',
    KEY `idx_user_id` (`bank_type_id`) USING BTREE
) collate = utf8mb3_bin
  row_format = DYNAMIC COMMENT ='利息流水规则表';

##GOGO


