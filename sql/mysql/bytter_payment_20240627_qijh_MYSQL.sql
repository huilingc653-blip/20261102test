##修改人：qijh
##修改时间：2024.06.27
##修改原因：增加银行账号绑定账号中间表 nis_binding_bankacc

##新增“银行账号绑定账号中间表”
CREATE TABLE IF NOT EXISTS NIS_BINDING_BANKACC (
    BANK_ID                  VARCHAR(32),
    BINDING_BANK_ID          varchar(32)
) COMMENT '银行账号绑定账号中间表';
##GOGO