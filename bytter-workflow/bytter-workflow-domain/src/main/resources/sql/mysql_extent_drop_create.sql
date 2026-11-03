-- auto-generated definition
drop table if exists act_extend_category;
create table act_extend_category
(
    ID                varchar(32)             not null
        primary key,
    PARENT_ID         varchar(32) default '0' null,
    NAME              varchar(50)             null,
    CATE_KEY          varchar(30)             null comment '流程分类KEY',
    TYPE              varchar(1)              null comment '0 审批流程，1业务流程',
    URL               varchar(100)            null comment '审批列表URL',
    SORT              decimal(3)              null comment '排序',
    MOBILE_URL        varchar(100)            null comment '手机审批详情的URL',
    NOTIFICATION_ID   varchar(32)             null comment '审批消息ID',
    MUST_APPROVE      tinyint     default 0   null comment '是否一定挂审批，0不是（默认）、1是',
    BATCH_REVOKE_ABLE tinyint     default 0   null comment '是否支持批量撤回,1=支持,0=不支持',
    MENU_ID           varchar(32)             null comment '菜单ID',
    MOBILE_AVE_ABLE   tinyint     default 0   null comment '是否开启手机审批',
    RESOURCE_CONTENT  varchar(200)            null comment '权限资源',
    CREATE_BY         varchar(255)            null comment '创建人',
    UPDATE_BY         varchar(255)            null comment '更新人',
    CREATE_DATE       datetime                null comment '创建时间',
    UPDATE_DATE       datetime                null comment '更新时间',
    SERVICE_ID        varchar(100)            null comment '流程所属服务ID',
    MENU_NAME         varchar(100)            null comment '菜单名称'
)
    comment '流程分类表' collate = utf8_bin;

create index IDX_BPM_FLOW_CATEGORY_KEY
    on act_extend_category (CATE_KEY);

INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('003fbd92d08744f89666f95186b01abe', '02523ab286714d96823300edb953e2ea', '贷款管理', 'cmsloan', '1', null, 1,
        null, null, 0, 1, '4b1bcffdaa464e92a44f31bee53d6451', null, null, null, null, null, null, null, '内部贷款');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('00b37b9156e3453fb720034177ea5e99', '7f71ed7f6a1f46ddb428fb18973a8614', '协定存款登记', 'choAgreeddepositReg',
        '1', '/agreeddeposit/choinneragreeddeposit/listApprove/0', 1,
        '/agreeddeposit/choinneragreeddeposit/mobileApprove', '29ea8c4c284240a0ba014c6ce78acb71', 0, 1, null, null,
        'sys:flow:approve:choAgreeddepositReg', null, null, null, null, 'bytter-service-increment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('00b92ec05fbb4c09a5abd0ef865db3d8', '1c064cbc7a934d469c9131234a35fb34', '追加保证金', 'netbankFcBondAdd', '1',
        '/bond/fcbondAddition/listApprove', 2, '/bond/fcbondAddition/mobileApprove', null, 0, 0,
        '34109c0aa94c44d2bc9b08b1e2c9f968', null, 'sys:flow:approve:netbankFcBondAdd', null, null, null, null,
        'bytter-service-payment', '追加保证金');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('011e6146b184417c8008288029778bde', '9dd1aed4f5834305a666bee0e4f3166f', '对外担保合同终止', 'guaExtContractEnd',
        '1', '/cms/guarantee/guaExtContractEnd/approveList', 3, '/cms/guarantee/guaExtContractEnd/mobileApprove', null,
        0, 1, '89d47adc6f9d429f9cca5bc9b83c56b8', 1, 'sys:flow:approve:guaExtContractEnd', null, null, null, null,
        'bytter-prepose-guarantee', '对外担保合同终止');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('0170b5c492624b1c928013ba0be72fd7', '02523ab286714d96823300edb953e2ea', '股权融资', 'Equity', '1', null, 11,
        null, null, 0, 1, null, 1, null, null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('01f54b36e086469ead0929fd0cbd799e', '08ff60f9895742a48ad7105d5b610a44', '外汇买卖交易', 'fexTranReqForeign',
        '1', '/foreign/fextranreq/listApprove/1', 1, '/foreign/fextranreq/mobileApprove',
        'a58da8792ab94ed8a03d321479b9d0ed', 0, 1, null, 1, 'sys:flow:approve:fexTranReqForeign', null, null, null, null,
        'bytter-service-payment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('02523ab286714d96823300edb953e2ea', null, '信贷融资', 'cms', '1', null, 4, null, null, 1, 1,
        '28183155434f425e8ce436c2b19eb2fd', null, null, null, null, null, null, null, '信贷融资');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('03b86b51d30d4a479d9b6bca322ea02c', '7f71ed7f6a1f46ddb428fb18973a8614', '协定存款到期处理',
        'choAgreeddepositPos', '1', '/agreeddeposit/choinneragreeddeposit/listApprove/1', 2,
        '/agreeddeposit/choinneragreeddeposit/mobileApprove', 'f5ee717cfc6e4ee798e9d31a7562881c', 0, 1, null, null,
        'sys:flow:approve:choAgreeddepositPos', null, null, null, null, 'bytter-service-increment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('04ea972c192846c39da7e03e91657514', '32d5a1d717fa458bb33d3eae3c8f6466', '应收票据', 'billReceive', '1', null, 2,
        null, null, 0, 1, 'a6b30d0b46174b088f279c3eb8263686', null, null, null, null, null, null, null, '应收票据');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('04ff11afbbf84af288c987b227e5c48d', '25ba61a2a4ad408b8cca9c4aba89ef3a', '银行授信额度调整', 'ratBkrationAdjust',
        '1', '/cms/rat/ratbkrationadjust/listApprove', 5, '/cms/rat/ratbkrationadjust/mobileApprove', null, 0, 1,
        'c7ddd7ff11f845589427a76347c2c77d', null, 'sys:flow:approve:ratBkrationAdjust', null, null, null, null,
        'bytter-prepose-credit', '银行授信管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('054b1c93dd50403bad2a4032b8df25a3', 'd6e02a26c6214751afa435ed835beda7', '额度管理', 'limit', '1', null, 1, null,
        null, 0, 1, 'a758531404bf4343b0ab09c8a50363ab', 1, null, null, null, null, null, null, '额度管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('0637e846ac1c4eb8b89cf846274b1960', 'uu1446f65aiu48feb62b96501dcbd527', '债券还款处理', 'cmsBondPay', '1',
        '/cms/bond/cmsbondpay/listApprove', null, null, null, 0, 1, 'd2e2a7bc7acd4126aaed4b94a2735038', 1,
        'sys:flow:approve:cmsBondPay', null, null, null, null, 'bytter-service-finance', '债券还款处理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('0755d4b14a4b4cc787d57e2e4190310d', '9dd1aed4f5834305a666bee0e4f3166f', '被担保合同登记', 'guaContractRegist',
        '1', '/cms/guarantee/guaContractInfo/listApprove', 1, '/cms/guarantee/guaContractInfo/mobileApprove', null, 0,
        1, 'f14571104df048cbabb52b14981220cb', null, 'sys:flow:approve:guaContractRegist', null, null, null, null,
        'bytter-prepose-guarantee', '被担保合同登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('08ff60f9895742a48ad7105d5b610a44', 'd113f998a2dd49aaaff2907fa64d5ac1', '交易管理', 'fexTranReq', '1', null, 5,
        null, null, 0, 1, null, 1, null, null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('0a0eb625b2f94523ae7816f5e151c028', 'f0893e845b3d4f479a7640d220c0955a', '票据签收', 'accebillSign', '1',
        '/bill/accebill/abbillreceivebiz/listApprove', 1, '/bill/accebill/abbillreceivebiz/mobileApprove', null, 0, 1,
        'be9970c5284143ddb321f91042d18f83', 1, null, null, null, null, null, 'bytter-prepose-bill', '票据签收');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('0a70388baaae4ba9b2a16ffb76f00593', '5a3270cba98144ccac5fa886d3454a1b', '内部资金拆借作废', 'cmsInlAbolish',
        '1', '/inl/abolish/listApprove', null, null, null, 0, 1, null, 1, 'sys:flow:approve:cmsInlAbolish', null, null,
        null, null, 'bytter-service-finance', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('0c3e9cc35c24403f9f9c465a60505c18', 'df12a9d025bc4695ad20fdf1c373a007', '合同履约', 'conConstractPerformance',
        '1', '/contract/conperformance/listApprove', 5, '/contract/conperformance/mobileApprove', null, 0, 1,
        'fea92527468341158c25fc1526aa1e27', 1, 'sys:flow:approve:conConstractPerformance', null, null, null, null,
        'bytter-prepose-guarantee', '合同履约');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('0c965d29c1de4d6083718af7b510b7eb', 'd113f998a2dd49aaaff2907fa64d5ac1', '规则管理', 'fexRule', '1', null, 3,
        null, null, 0, 1, null, null, null, null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('0cc9fe63bdc8428a9929cbb4b2ce922a', '054b1c93dd50403bad2a4032b8df25a3', '额度申领核准', 'abPoolLimitApplyCheck',
        '1', '/billPool/applyCheck/approveList', 2, '/billPool/applyCheck/mobileApprove', null, 0, 1,
        'a758531404bf4343b0ab09c8a50363ab', 1, 'sys:flow:approve:abPoolLimitApplyCheck', null, null, null, null,
        'bytter-prepose-bill', '额度管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('0e4b113506634ed2a8de33103098adf9', '63c3faf05c1c461788abf5ae9205a9ec', '理财赎回', 'choProductRedeem', '1',
        '/chowmatistic/choproductredeem/listApprove', 2, '/chowmatistic/choproductredeem/mobileApprove',
        '5fb6a60d15e0421286023feee5db8f71', 0, 1, 'c244aac85ae54411bd285b4ae028ebb1', null,
        'sys:flow:approve:choProductRedeem', null, null, null, null, 'bytter-service-increment', '理财赎回');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('0e935d5da42848d0b7311bfef1ed5902', '55e77d690af841cfbb7724f94d985711', '银行借款发放', 'cmsProvideLoanSend',
        '1', '/cms/provide/loansend/listSendApprove', 3, '/cms/provide/loansend/mobileSendApprove', null, 0, 1,
        '4b331030c5834640a4eb121952b643c2', null, 'sys:flow:approve:cmsProvideLoanSend', null, null, null, null,
        'bytter-service-finance', '借款发放');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('1084ebdf12494cce9f5ef96f0fe0748c', 'd113f998a2dd49aaaff2907fa64d5ac1', '结算管理', 'fexSysCashFlow', '1', null,
        6, null, '0c73a76a21994cfba9ae169e903ddbad', 0, 1, '76e7a9b63de04e67a224aa36b8a2d370', 1, null, null, null,
        null, null, null, '结算管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('10e2593396de473da62575bbfa8489d8', 'd6e02a26c6214751afa435ed835beda7', '票据管理', 'billManage', '1', null, 5,
        null, null, 0, 1, '15a8872848304beeb12a27099f0da113', 1, null, null, null, null, null, null, '票据管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('1126c57850234bf1906fa4046b9b8bce', '9dd1aed4f5834305a666bee0e4f3166f', '对外担保合同登记',
        'guaExtContractRegist', '1', '/cms/guarantee/guaExtContractInfo/listApprove', 3,
        '/cms/guarantee/guaExtContractInfo/mobileApprove', null, 0, 1, 'f1e04d8f6e6448fe8d1ccbaae847796d', 1,
        'sys:flow:approve:guaExtContractRegist', null, null, null, null, 'bytter-prepose-guarantee',
        '对外担保合同登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('12fba591be2d4261a5b4eabd87cbe281', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁标的物登记', 'LeaseObjectInfo',
        '1', '/cms/LeaseObjectInfo/listApprove', null, null, null, 0, 1, 'b6bd74267afa447ba898a4ff6766aa51', 1,
        'sys:flow:approve:LeaseObjectInfo', null, null, null, null, 'bytter-service-finance', '融资租赁标的物登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('1397029bb7a64bb99ae0de71b0a92886', '5a3270cba98144ccac5fa886d3454a1b', '内部资金拆借展期', 'cmsInlDeferred',
        '1', '/inl/deferred/listApprove', null, null, null, 0, 1, null, 1, 'sys:flow:approve:cmsInlDeferred', null,
        null, null, null, 'bytter-service-finance', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('1729388183627317249', '5a3270cba98144ccac5fa886d3454a1b', '内部拆借还款修改', 'cmsInlRepayChange', '1',
        '/inl/repay/listRepayUpdate', null, '', '', 0, 1, '3fc5d509930a4d0e8be864f0c75f18ce', 1, null, 'niexy1',
        'niexy1', '2023-11-28 14:34:08', '2023-11-28 14:43:32', null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('17de971b4c764fcda026afb84f0287c0', 'bd798f5d681b49ca9d7a8a9f4ceec8bd', '委托收款', 'netbankCgsBillPayee', '1',
        '/netbank/cgsbill/listApprovePayee', 10, null, null, 0, 1, 'bb5a018aad78475ca20eccce4a932dcc', 0,
        'sys:flow:approve:netbankCgsBillPayee', null, null, null, null, 'bytter-service-payment', '委托收款');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('17eaca610f354f1089bb54a75a7838c0', '53935ae1501645d6be2f3ad92ba4e064', '利息豁免', 'istValuaExempt', '1',
        '/valua/xhIstValuaExempt/approveList', null, null, null, 0, 1, null, null, 'sys:flow:approve:istValuaExempt',
        null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('17ecfaaa6c5d4645b2ebfe3bc6e94fb4', 'uu1446f65aiu48feb62b96501dcbd527', '债券还款计划修改',
        'updateCmsBondRepayPlan', '1', '/cms/bond/updatecmsbondrepayplan/listApprove', null, null, null, 0, 1,
        '8311515a84b04816992e6e7426150d3f', 1, 'sys:flow:approve:updateCmsBondRepayPlan', null, null, null, null, null,
        '债券还款计划修改');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('180392ca4988425591faa261b0140327', 'fae79b3a86cd4531b3e79479d4cac163', '直营付款', 'netbankBdlinkPay', '1',
        '/bdlink/bisexc/listApprove', 500, '', '', 0, 0, 'd466013f3dde4a2ab5f814039f3eb3f5', 0,
        'sys:flow:approve:netbankBdlinkPay', null, '超级管理员', null, '2023-07-24 17:52:03', 'bytter-service-bdlink',
        '自营付款');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('183c677920c644348504e822cb5439c7', '67d5f1a6041c458e8c64abd07d80fead', '保证金存入', 'abPoolBailDtlInApply',
        '1', '/billPool/bail/inApprove', 1, '/billPool/bail/mobileInApprove', null, 0, 1,
        'd8c161d38b6341ac96715e3ce3b01955', 1, 'sys:flow:approve:abPoolBailDtlInApply', null, null, null, null,
        'bytter-prepose-bill', '保证金存入');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('196d5787108341049b4c8f4ca228c117', '02523ab286714d96823300edb953e2ea', '股东借款', 'cmsBfs', '1', null, 20,
        null, null, 0, 1, '3338e32af88e450fbe52c473e1332400', 1, null, null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('1a155a9dacc94b02bdf9038483a136fc', '6d7d608f4d0f4944988ce7f3759b95e8', '银行账户变更申请', 'netbankBankaccRev',
        '1', '/bankacc/btbankaccapp/listApprove/2', 102, '/bankacc/btbankaccapp/mobileApprove',
        '05156e9ec6fb4470939dff8973cc506c', 0, 0, '402880265c5dd71e015c5ddae45c0004', null,
        'sys:flow:approve:netbankBankaccRev', null, null, null, null, 'bytter-service-payment', '银行账户变更申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('1aaff46ff5b04261999376950f1b57db', 'b071b2378dd246f4aa853a352a0d0e0b', '出票融资申请', 'abPoolEmitfinaApply',
        '1', '/billpool/emitFinaApply/approve', 1, '/billpool/emitFinaApply/mobileApprove', null, 0, 1,
        'cff6c6397e4f4ca38450cc07d452432a', 1, 'sys:flow:approve:abPoolEmitfinaApply', null, null, null, null,
        'bytter-prepose-bill', '出票融资申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('1c064cbc7a934d469c9131234a35fb34', 'a48d5e38146443359ff9f9d456c317a0', '银行保证金管理', 'netbankFcBond', '1',
        null, 500, null, null, 0, 1, '6b540ef2aa494a76932d1586bb575603', null, null, null, null, null, null, null,
        '银行保证金管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('1db5a132b93544fb91c7f1443dd5efe5', '6d7d608f4d0f4944988ce7f3759b95e8', '银行账户流水管理', 'netbankBankaccDtl',
        '1', '/bankacc/btbankaccdtl/listApprove', 105, '/bankacc/btbankaccdtl/mobileApprove',
        '1accc267b7544c4c8553aa1ed4a25649', 0, 1, '322a997b5b7b4df0880b19389a7d45c8', null,
        'sys:flow:approve:netbankBankaccDtl', null, null, null, null, 'bytter-service-payment', '银行账户流水管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('20b34d17ccfa4cc4845119e2f1c2e2d9', 'f0893e845b3d4f479a7640d220c0955a', '通用撤回', 'accebillRecall', '1',
        '/bill/accebill/abbillRecall/listApprove', 5, '/bill/accebill/abbillRecall/mobileApprove',
        '5c22027be9534aecbc50809709bacd02', 0, 1, '988eda5c92434b5a907228f0ddd4b75e', null,
        'sys:flow:approve:accebillRecall', null, null, null, null, 'bytter-prepose-bill', '通用撤回');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('21fb4abf996f43668771dda1af1bd80e', '55e77d690af841cfbb7724f94d985711', '银行借款还款补录',
        'cmsProvideLoanRepayRepair', '1', '/cms/provide/loanrepay/listRepayRepairApprove', 5,
        '/cms/provide/loanrepay/mobileRepayApprove', null, 0, 1, '96216f1e906b4d47abb9cb7b32f2fa4e', null,
        'sys:flow:approve:cmsProvideLoanRepayRepair', null, null, null, null, 'bytter-service-finance', '借款还款补录');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('2376d8dbe6554f138ec91e1cd86d62c1', '564deccaf9dc4093b8a823bc89390c60', '请款', 'netbankAllocationAsk', '1',
        '/allocation/nisallocationreq/listApprove/12', 12, '', '', 0, 1, '4028802e5c675881015c6763cce30011', 1,
        'sys:flow:approve:netbankAllocationAsk', null, '测试人员', null, '2023-08-21 15:49:05',
        'bytter-service-payment', '请款');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('23af6dc9025849f9928357d4ab975579', '2521e91e96f14c6482f3705edf3e395e', '银行定期转活期', 'choBankT2C', '1',
        '/chowmatistic/chobankfinance/listApprove/11', 2, '/chowmatistic/chobankfinance/mobileApprove', null, 0, 1,
        '0e66d17b03f14dde8b69c3acec40786e', 1, 'sys:flow:approve:choBankT2C', null, null, null, null,
        'bytter-service-increment', '定期转活期');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('245fd91ce6234c78aadbd361cef5e2ba', '7f71ed7f6a1f46ddb428fb18973a8614', '内部活期转定期', 'choInnerC2T', '1',
        '/chowmatistic/choinnerfinance/listApprove/10', 4, '/chowmatistic/choinnerfinance/mobileApprove',
        '1bc85a705e67402d8395ba5d02cd1a91', 0, 1, null, null, 'sys:flow:approve:choInnerC2T', null, null, null, null,
        'bytter-service-increment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('248ff1e5717e4f6a98a84d2eaea2149f', 'df12a9d025bc4695ad20fdf1c373a007', '合同终止', 'conConstractSuspend', '1',
        '/contract/suspend/suspendListApprove', 8, '/contract/suspend/mobile/approve', null, 0, 1,
        '88090546c77242578acaa28cdd06f686', 1, 'sys:flow:approve:conConstractSuspend', null, null, null, null,
        'bytter-prepose-guarantee', '合同终止');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('24fe7756278447aca8934976e9d84c2f', null, '基础平台', 'sys', '1', null, 500, null, null, 0, 1, null, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('2521e91e96f14c6482f3705edf3e395e', '63c7205f61af4e5ebedf73f72647209d', '银行理财', 'bankFinance', '1', null, 1,
        null, null, 0, 1, '8eeafec2da5a4794a4d2f436fef02d80', null, null, null, null, null, null, null,
        '银行存款类投资管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('25ba61a2a4ad408b8cca9c4aba89ef3a', '02523ab286714d96823300edb953e2ea', '授信管理', 'rat', '1', null, 4, null,
        null, 1, 1, '57abaa37db0144d790991c421c9720e5', null, null, null, null, null, null, null, '授信管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('27eeedb803004cd693c60df107453f6e', '003fbd92d08744f89666f95186b01abe', '贷款还款', 'cmsloanRepay', '1',
        '/cms/loan/cmsloanrepay/listRepayApprove', 4, '/cms/loan/cmsloanrepay/mobileRepayApprove',
        '49550a59381e4929bc2f5b1c722fda4c', 0, 1, 'b85d2e52bb3149699f5f08ad0e166f21', null,
        'sys:flow:approve:cmsloanRepay', null, null, null, null, 'bytter-service-finance', '贷款还款申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('280b77c8cc8d4b38bff92cd4ff50bf48', '04ea972c192846c39da7e03e91657514', '收票登记', 'accebillReceiveBill', '1',
        '/bill/accebill/abreceivebill/listApprove', 1, '/bill/accebill/abreceivebill/mobileApprove',
        'a73641c2c2da471e9bb8762adf499003', 0, 1, 'fd10e4bf16ca4994b5376e098f597824', null,
        'sys:flow:approve:accebillReceiveBill', null, null, null, null, 'bytter-prepose-bill', '收票登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('291819d7be324e9184bba52943341f96', 'a48d5e38146443359ff9f9d456c317a0', '成员单位对账', 'corpCompareBill', '1',
        '/corpCompareBill/list/approve', null, null, null, 0, 1, null, null, 'sys:flow:approve:corpCompareBill', null,
        null, null, null, 'bytter-service-payment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('2bf804483cdf43899eb9aba3bc766dbc', '0c965d29c1de4d6083718af7b510b7eb', '主体单位默认银行账户维护',
        'fexCorpBankAcc', '1', '/foreign/fexcorpbankacc/listApprove', 10, '/foreign/fexcorpbankacc/mobileApprove', null,
        0, 1, null, null, 'sys:flow:approve:fexCorpBankAcc', null, null, null, null, 'bytter-service-payment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('2c1b1c7bccf74935852fbdd47c69c5e4', '918303850a10498883cd5eddc3d31ca1', '信用证交单入库', 'lcReceiveArrival',
        '1', '/international/credit/arrivalletter/arrivalApproveList', 2,
        '/international/credit/arrivalletter/mobileViewReceiveArrival', null, 0, 1, 'bbbbd2d2041e4513bf6e3d9f9b8c0bd8',
        null, 'sys:flow:approve:lcReceiveArrival', null, null, null, null, 'bytter-prepose-credential', '交单登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('2dbc122eeb624450a7f9b6942e995285', '04ea972c192846c39da7e03e91657514', '贴现办理', 'accebillDiscountManager',
        '1', '/bill/accebill/abBillDiscountManager/listApprove', 6,
        '/bill/accebill/abBillDiscountManager/mobileApprove', 'a73641c2c2da471e9bb8762adf499011', 0, 1,
        '5da98ec86a814ec3a69675050d6f0836', null, 'sys:flow:approve:accebillDiscountManager', null, null, null, null,
        'bytter-prepose-bill', '贴现办理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('2e2b3803043f4d99a469dc08283e3a4b', '808376c2ae4146a78143155ad4ba7778', '第三方付款办理', 'tpPayment', '1',
        '/ttpay/tpPayment/listApprove', 1, null, null, 0, 1, 'b1112b6b239e490ea92ee05366981dc7', 0,
        'sys:flow:approve:tpPayment', null, null, null, null, 'bytter-service-payment', '第三方付款办理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('311d9fbeb77a423c9607633840086bdc', '9dd1aed4f5834305a666bee0e4f3166f', '保函申请', 'guaGuaranteeApply', '1',
        '/letter/guarantee/guaGuaranteeInfo/listApprove', 4, '/letter/guarantee/guaGuaranteeInfo/mobileApprove', null,
        0, 1, 'c13f76f40042472dbb884ae46db70a76', null, 'sys:flow:approve:guaGuaranteeApply', null, null, null, null,
        'bytter-prepose-letter', '保函申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('320ba2e58b4e44fa8b8505688587ba72', 'f0893e845b3d4f479a7640d220c0955a', '不可转让撤销', 'nonassignableCancel',
        '1', '/bill/accebill/nonassignableCancel/listApprove', 6, '', '', 0, 1, '9db2d4b2feda49ea8d0185d813c03867', 1,
        null, null, 'duanyw', null, '2023-10-17 16:26:31', 'bytter-prepose-bill', '不可转让撤销');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('32d5a1d717fa458bb33d3eae3c8f6466', null, '承兑汇票', 'accebill', '1', null, 3, null, null, 0, 1,
        '402880455c616213015c6162e77d0000', null, null, null, null, null, null, null, '承兑汇票');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('3311541b02ce402482877c17c40b8818', 'f37f48e7002a401ebe6b21e09395d1fa', '委托贷款展期申请',
        'cmsPrecativeDeferred', '1', '/cms/precative/precativeloandeferred/listDeferredApprove', 9,
        '/cms/precative/precativeloandeferred/mobileDeferredApprove', 'c87d5ef440444f7e965b9033b17c8942', 0, 1,
        '3eed0c44954f4033958858229a801693', null, 'sys:flow:approve:cmsPrecativeDeferred', null, null, null, null,
        'bytter-service-finance', '委托贷款展期申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('338667e2f16f4328942215ba86e3cd5c', '4bc0a959d1654c9da04edc2d20a88085', '开票登记', 'accebillAbEmitBill', '1',
        '/bill/accebill/abemitbill/listApprove', 1, '/bill/accebill/abemitbill/mobileApprove',
        '5ff2fe20e7f248b093488f6eb312f901', 0, 1, 'db7539c3b67a40628bb0b20547350073', 1,
        'sys:flow:approve:accebillAbEmitBill', null, null, null, null, 'bytter-prepose-bill', '开票登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('346cd67c18dc473b9bdd854e1fc9a3a8', '7f71ed7f6a1f46ddb428fb18973a8614', '内部定期转活期', 'choInnerT2C', '1',
        '/chowmatistic/choinnerfinance/listApprove/11', 5, '/chowmatistic/choinnerfinance/mobileApprove',
        '1c9309acc0444c9e92cce42529584943', 0, 1, null, null, 'sys:flow:approve:choInnerT2C', null, null, null, null,
        'bytter-service-increment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('35986704f7a74c358d2c19cec3945f48', '10e2593396de473da62575bbfa8489d8', '出票申请', 'abPoolEmitApply', '1',
        '/billpool/emitApply/approve', 4, '/billpool/emitApply/mobileApprove', null, 0, 1,
        '0bff1059ca1d4181923424e36015cb7f', 1, 'sys:flow:approve:abPoolEmitApply', null, null, null, null,
        'bytter-prepose-bill', '出票申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('392233eee3b94e2d8df6310649020298', '1c064cbc7a934d469c9131234a35fb34', '保证金维护', 'netbankFcBondPreserve',
        '1', '/bond/fcbondpreserve/listApprove', 3, '/bond/fcbondpreserve/mobileApprove', null, 0, 1,
        'dad1723289e64188ad03d586657cfbbd', null, 'sys:flow:approve:netbankFcBondPreserve', null, null, null, null,
        'bytter-prepose-guarantee', '保证金维护');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('3a3237a2d1974e1d9017372a5191b260', '04ea972c192846c39da7e03e91657514', '贴现收回', 'accebillDiscountCollect',
        '1', '/bill/accebill/abbilldiscountcollect/listApprove', 19,
        '/bill/accebill/abbilldiscountcollect/mobileApprove', 'a73641c2c2da471e9bb8762adf499019', 0, 1,
        '0f8c1125836246969a7c997752ac4f5b', null, 'sys:flow:approve:accebillDiscountCollect', null, null, null, null,
        'bytter-prepose-bill', '贴现收回');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('3a8e536a95ac4b04bb0d6cc537f58ee1', null, '资金计划', 'fbsBudget', '1', null, 3, null, null, 0, 1,
        '402880275c8b05d6015c8b60ad1a0002', null, null, null, null, null, null, null, '资金计划');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('3c44eadcb8f44682b04c08a65b39a4d5', '0170b5c492624b1c928013ba0be72fd7', '股权融资登记', 'cmsEquityInfo', '1',
        '/cms/equity/info/listApprove', 1, null, null, 0, 1, '8d57812472d14409b8a6e2bbd18620f8', 1,
        'sys:flow:approve:cmsEquityInfo', null, null, null, null, 'bytter-service-finance', '股权融资登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('3c44eadcb8f44682b04c08a65b39a4d6', '0170b5c492624b1c928013ba0be72fd7', '缴付/过户计划登记', 'cmsEquityPayPlan',
        '1', '/cms/equity/payPlan/listApprove', 2, null, null, 0, 1, '8d57812472d14409b8a6e2bbd18620f9', 1,
        'sys:flow:approve:cmsEquityPayPlan', null, null, null, null, 'bytter-service-finance', '缴付/过户计划登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('3c44eadcb8f44682b04c08a65b39a4d7', '0170b5c492624b1c928013ba0be72fd7', '缴付/过户计划修改',
        'cmsEquityPayPlanUpdate', '1', '/cms/equity/payPlan/listUpdateApprove', 3, null, null, 0, 1,
        '8d57812472d14409b8a6e2bbd1862010', 1, 'sys:flow:approve:cmsEquityPayPlanUpdate', null, null, null, null,
        'bytter-service-finance', '缴付/过户计划修改');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('3c44eadcb8f44682b04c08a65b39a4d8', '0170b5c492624b1c928013ba0be72fd7', '股权融资回购计划登记',
        'cmsEquityRepoPlan', '1', '/cms/equity/repoPlan/listApprove', 5, null, null, 0, 1,
        '8d57812472d14409b8a6e2bbd1862011', 1, 'sys:flow:approve:cmsEquityRepoPlan', null, null, null, null,
        'bytter-service-finance', '股权融资回购计划登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('3c44eadcb8f44682b04c08a65b39a4d9', '0170b5c492624b1c928013ba0be72fd7', '股权融资回购计划修改',
        'cmsEquityRepoPlanUpdate', '1', '/cms/equity/repoPlan/listUpdateApprove', 6, null, null, 0, 1,
        '8d57812472d14409b8a6e2bbd1862012', 1, 'sys:flow:approve:cmsEquityRepoPlanUpdate', null, null, null, null,
        'bytter-service-finance', '股权融资回购计划修改');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('3e6ececfea4745a0b0a3b39564c10239', 'd113f998a2dd49aaaff2907fa64d5ac1', '交易对手管理', 'fexCotpManage', '1',
        null, 2, null, null, 0, 1, '88cb524b44dc4d8d8ec7e56000e072cd', 1, null, null, null, null, null, null,
        '外汇管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('3ef95c31f30345d59ec3b03f1cfb143e', '04ea972c192846c39da7e03e91657514', '票据归集',
        'abTransferCollection', '1', '/bill/accebill/abbilltransfercollection/listApprove', 13,
        '/bill/accebill/abbilltransfercollection/mobileApprove', 'a73641c2c2da471e9bb8762adf499005', 0, 1,
        '660bda8e2b8b4b9eb24e04431d131113', null, 'sys:flow:approve:accebillTransferCollection', null, null, null, null,
        'bytter-prepose-bill', '票据归集');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('4130d76e571f4468a22a8f3833bc100c', '08ff60f9895742a48ad7105d5b610a44', '展期/提前交割', 'fexTranDealDf', '1',
        '/foreign/fextranreq/listTranDealApprove/1', 4, '/foreign/fextranreq/mobileTranDealApprove/1',
        '69ebac1bd0444534967f8b8bfa8fb15b', 0, 1, 'ab979840628242d796c2631562bbf48c', 1,
        'sys:flow:approve:fexTranDealDf', null, null, null, null, null, '外汇买卖交易');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('423e1435b1a842d792e546ed76fa235c', '564deccaf9dc4093b8a823bc89390c60', '上划', 'netbankAllocationUp', '1',
        '/allocation/nisallocationreq/listApprove/10', 10, '/allocation/nisallocationreq/mobileApprove', '', 0, 1,
        '4028802e5c5c2e2d015c5c5732ba002f', 1, 'sys:flow:approve:netbankAllocationUp', null, '测试人员', null,
        '2023-08-21 15:48:47', 'bytter-service-payment', '上划');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('425cf3488b7c47b9a5a4498a1f1ad5ef', '25ba61a2a4ad408b8cca9c4aba89ef3a', '集团授信额度追加', 'ratBkrationCpAdd',
        '1', '/cms/rat/ratbkrationadd/listCpAddApprove', 4, '/cms/rat/ratbkrationadd/mobileApprove',
        '0efca8ad581640c5abb2be48e40f9fe8', 0, 1, '9997b6949de34bc4bdf90e817da96364', null,
        'sys:flow:approve:ratBkrationCpAdd', null, null, null, null, 'bytter-prepose-credit', '集团授信额度追加');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('4321b97d112845df9cb86b9c83b0930b', '04ea972c192846c39da7e03e91657514', '质押申请', 'accebillPledgeApply', '1',
        '/bill/accebill/abbillpledgeapply/listApprove', 8, '/bill/accebill/abbillpledgeapply/mobileApprove',
        'a73641c2c2da471e9bb8762adf499008', 0, 1, '1d07387d0a0b408a8a2991452bb4b9a3', null,
        'sys:flow:approve:accebillPledgeApply', null, null, null, null, 'bytter-prepose-bill', '质押申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('444b256d89e44b7cbca22493dcd83355', '3e6ececfea4745a0b0a3b39564c10239', '交易对手维护', 'fexCotp', '1',
        '/foreign/fexCotp/listFexCotpApprove', 10, '/foreign/fexCotp/mobileApprove', '', 0, 1,
        '092481b0942e45b09935deec35e81ac6', 1, 'sys:flow:approve:fexCotp', null, 'zp001', null, '2023-11-15 19:56:19',
        'bytter-service-payment', '交易对手维护');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('4452ddc56e21449cb11f6a4a6f01756d', '003fbd92d08744f89666f95186b01abe', '贷款展期', 'cmsloanDeferred', '1',
        '/cms/loan/cmsloandeferred/listDeferredApprove', 5, '/cms/loan/cmsloandeferred/mobileDeferredApprove',
        '762e713114f2430485129f87287d1fc5', 0, 1, 'ad2dbbf6848742eb8231e3c8fba96026', null,
        'sys:flow:approve:cmsloanDeferred', null, null, null, null, 'bytter-service-finance', '贷款展期申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('44bd7dced0d441e5a2c89e36ac14e229', '10e2593396de473da62575bbfa8489d8', '贴现申请', 'abPoolDiscountApply', '1',
        '/billPool/discount/listApprove', 2, '/billPool/discount/mobileApprove', null, 0, 1,
        '5c68e7cde61a4d44bb4164ee2c2304f9', 1, 'sys:flow:approve:abPoolDiscountApply', null, null, null, null,
        'bytter-prepose-bill', '贴现申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('44ef51bd98dc4349bbd5fca6efc8c449', '25ba61a2a4ad408b8cca9c4aba89ef3a', '内部授信额度占用', 'ratCprationApply',
        '1', '/cms/rat/cprationapply/listApprove', 11, '/cms/rat/cprationapply/mobileApprove', null, 0, 1,
        '69fe3328d79a43c08c6a8a1b9db11eb6', null, 'sys:flow:approve:ratCprationApply', null, null, null, null,
        'bytter-prepose-credit', '内部授信额度占用');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('472d765e95124b7082c82b2997f8cd21', 'ac6c41877ddc41bd9c504d8d2f416e7e', '直融业务还款申请',
        'dfDirectFinanceRepay', '1', '/cms/dfDirectFinanceRepay/listApprove', 20,
        '/cms/dfDirectFinanceRepay/mobileApprove', null, 0, 1, null, 1, 'sys:flow:approve:dfDirectFinanceRepay', null,
        null, null, null, 'bytter-service-finance', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('4a04c5f37d2d4d599ab27da832a8b89c', 'caa1e8ab7f0d4fc3bccaa3cd96e469b5', '交易需求申请', 'fexTranApp', '1',
        '/foreign/fexTranApp/listTranAppApprove', null, '/foreign/fexTranApp/mobileApprove',
        '58c3b524a1b24bdca24178466d14fa68', 0, 1, '88cb524b44dc4d8d8ec7e56000e072cd', 1, 'sys:flow:approve:fexTranApp',
        null, null, null, null, 'bytter-service-payment', '外汇管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('4ba8ca9d347d469698e804a848d21bad', '6d7d608f4d0f4944988ce7f3759b95e8', '银行账户变更办理',
        'btbankaccrevhandle', '1', '/bankacc/btbankaccrevhandle/listApprove', 109,
        '/bankacc/btbankaccrevhandle/mobileApprove', 'b445fd4385b943228d51da0514102724', 0, 1,
        '402880265c86f1df015c870bf64e002b', 1, 'sys:flow:approve:btbankaccrevhandle', null, null, null, null,
        'bytter-service-payment', '银行账号变更办理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('4bc0a959d1654c9da04edc2d20a88085', '32d5a1d717fa458bb33d3eae3c8f6466', '应付票据', 'billEmit', '1', null, 1,
        null, null, 0, 1, '38d6293b5fbf4597aa52222b429724ae', null, null, null, null, null, null, null, '应付票据');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('4bc8af5a8259417688b93dbcb72480ba', '53f6b699b3b34240b9465c4864477eb4', '资产证券化发行', 'cmsSecurityIssue',
        '1', '/cms/security/issue/listApprove', 2, null, null, 0, 1, 'e6f10d70032842919827e3cde415ed9b', 1, null, null,
        null, null, null, 'bytter-service-finance', '资产证券化发行');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('4f13017864ec44c3b0f61ea9b8eb11f6', '55e77d690af841cfbb7724f94d985711', '银行借款利率调整',
        'cmsProvideLoanRate', '1', '/cms/provide/cmsprovideloanrate/listInfoApprove', 11, null, null, 0, 1,
        '98a22d5dd35647439e995aeab499b05e', 0, null, null, null, null, null, null, '借款利率调整');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('4f1ec00ced7843dab1b267560ea7e3f7', '6d7d608f4d0f4944988ce7f3759b95e8', '银行账户开户办理', 'btbankacchandle',
        '1', '/bankacc/btbankacchandle/listApprove', 108, '/bankacc/btbankacchandle/mobileApprove',
        '0bdbbc04afe149d291cbfc662f22f440', 0, 1, '402880265c67cc3c015c6837f65e0045', 1,
        'sys:flow:approve:btbankacchandle', null, null, null, null, 'bytter-service-payment', '银行账户开户办理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('507191e6b06b4db7b0b42de46b251f68', '55e77d690af841cfbb7724f94d985711', '银行借款申请补录',
        'cmsProvideLoanInfoRepair', '1', '/cms/provide/loaninfo/listRepairApprove', 2,
        '/cms/provide/loaninfo/mobileInfoApprove', null, 0, 1, '7294a4484a4c4c50a0e5246ae92c3029', 1,
        'sys:flow:approve:cmsProvideLoanInfoRepair', null, null, null, null, 'bytter-service-finance', '借款申请补录');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('50e7d302bc5d464d848dfb360399d4e9', '9dd1aed4f5834305a666bee0e4f3166f', '保函更改申请', 'guaGuaranteeChange',
        '1', '/letter/guarantee/guaGuaranteeChangeInfo/listApprove', 5,
        '/letter/guarantee/guaGuaranteeChangeInfo/mobileApprove', null, 0, 1, '08fa6399385140f1869465ee9a240f50', null,
        'sys:flow:approve:guaGuaranteeChange', null, null, null, null, 'bytter-prepose-letter', '保函更改申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('514a2fc062934f4c8ea1ee981a28c3e9', '054b1c93dd50403bad2a4032b8df25a3', '额度分配', 'abPoolLimitAllot', '1',
        '/billpool/limitAllot/listApprove', 3, '/billpool/limitAllot/mobileApprove', null, 0, 1,
        '2e243b334e4d4890a3db42ac0ce8db88', 1, 'sys:flow:approve:abPoolLimitAllot', null, null, null, null,
        'bytter-prepose-bill', '额度分配');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('518a8d98df1545418aef4d33d82f24c8', '04ea972c192846c39da7e03e91657514', '贴现申请', 'accebillDiscountApply',
        '1', '/bill/accebill/abBillDiscountApply/listApprove', 5, '/bill/accebill/abBillDiscountApply/mobileApprove',
        'a73641c2c2da471e9bb8762adf499010', 0, 1, '922ed39817534cadb358161889301132', null,
        'sys:flow:approve:accebillDiscountApply', null, null, null, null, 'bytter-prepose-bill', '贴现申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('5282f78cbb5c466e84e0f6766cb12f42', '7248874ae83d4ddd82a184d42bb0e1ad', '信用证开证', 'lcCreateCredit', '1',
        '/international/credit/createCredit/listCreditApprove', 100,
        '/international/credit/createCredit/mobileViewCredit', null, 0, 1, '7a3816a79ed94719a79a211438ebb3da', null,
        'sys:flow:approve:lcCreateCredit', null, null, null, null, 'bytter-prepose-credential', '信用证开证');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('52b7ab1596ed44378075db443c237234', '04ea972c192846c39da7e03e91657514', '托收申请', 'abbillcollectionapply',
        '1', '/bill/accebill/abbillcollectionapply/listApprove', 17,
        '/bill/accebill/abbillcollectionapply/mobileApprove', 'a73641c2c2da471e9bb8762adf499002', 0, 1,
        '1db49b82597f4ef68d68e3d8f0a41545', null, 'sys:flow:approve:abbillcollectionapply', null, null, null, null,
        'bytter-prepose-bill', '托收申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('53935ae1501645d6be2f3ad92ba4e064', null, '内部计价', 'valua', '1', null, null, null, null, 0, 1, null, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('53f6b699b3b34240b9465c4864477eb4', '02523ab286714d96823300edb953e2ea', '资产证券化', 'security', '1', null, 14,
        null, null, 0, 1, '8605f4ceff024cd18b8229ac3df44233', 1, null, null, null, null, null, null, '资产证券化');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('549a02d1c1f94997a6b794654a28f209', '08ff60f9895742a48ad7105d5b610a44', '违约', 'fexTranDealDe', '1',
        '/foreign/fextranreq/listTranDealApprove/2', 5, '/foreign/fextranreq/mobileTranDealApprove/2',
        'fa723c9c8dff4a6bafac348cf41bb5a5', 0, 1, 'ab979840628242d796c2631562bbf48c', 1,
        'sys:flow:approve:fexTranDealDe', null, null, null, null, null, '外汇买卖交易');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('55bd11aca63f46be9cf7bb7c9359f6b8', '04ea972c192846c39da7e03e91657514', '解除质押', 'accebillPledgeRelieve',
        '1', '/bill/accebill/abbillpledgerelieve/listApprove', 9, '/bill/accebill/abbillpledgerelieve/mobileApprove',
        'a73641c2c2da471e9bb8762adf499013', 0, 1, '2285781ff40a4d4891680e0423869f62', null,
        'sys:flow:approve:accebillPledgeRelieve', null, null, null, null, 'bytter-prepose-bill', '解除质押');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('55e77d690af841cfbb7724f94d985711', '02523ab286714d96823300edb953e2ea', '银行借款', 'cmsProvideLoan', '1', null,
        2, null, null, 0, 1, 'bf54345e8eda4aea9f517cca80a7adba', null, null, null, null, null, null, null, '银行借款');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('55fa81b9f9a94c33a0b8d14705f81e6a', 'bd798f5d681b49ca9d7a8a9f4ceec8bd', '委托付款', 'netbankCgsBillPayer', '1',
        '/netbank/cgsbill/listApprovePayer', 20, null, null, 0, 1, 'bb5a018aad78475ca20eccce4a932dcc', null,
        'sys:flow:approve:netbankCgsBillPayer', null, null, null, null, 'bytter-service-payment', '委托收款');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('564deccaf9dc4093b8a823bc89390c60', 'a48d5e38146443359ff9f9d456c317a0', '资金调拨', 'netbankAllocation', '1',
        null, 500, null, null, 0, 1, '2a164d43b1d042bdbd8eabe715fd72e4', null, null, null, null, null, null, null,
        '资金调拨');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('577f1f5ed83b4102aca5e3b69b9f24cb', '08ff60f9895742a48ad7105d5b610a44', '资金收付交易', 'fexTranReqPay', '1',
        '/foreign/fextranreq/listApprove/4', 3, '/foreign/fextranreq/mobileApprove', '18f7c0d6174b4f9986e34f7f1d419a4b',
        0, 1, null, 1, 'sys:flow:approve:fexTranReqPay', null, null, null, null, 'bytter-service-payment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('584e50ae3a9840f4b8c34e1629852256', '25ba61a2a4ad408b8cca9c4aba89ef3a', '银行额度系统占用', 'ratUrationApply',
        '1', '/cms/rat/raturationapply/listSendApprove', 6, '/cms/rat/raturationapply/mobileApprove',
        'fe666504fac44e12bcfa07b9e8789bc1', 0, 1, 'f50067f19ac5403c927a743d30b5915c', null,
        'sys:flow:approve:ratUrationApply', null, null, null, null, 'bytter-prepose-credit', '银行额度系统占用');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('5a3270cba98144ccac5fa886d3454a1b', '02523ab286714d96823300edb953e2ea', '资金拆借', 'CapitalLending', '1', null,
        null, null, null, 0, 1, null, 1, null, null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('5a3b54d48ff54c1dbf970c0574598b7e', '53f6b699b3b34240b9465c4864477eb4', '资产证券化还款确认',
        'cmsSecurityRepaySure', '1', '/cms/security/repaySure/listApprove', 6, null, null, 0, 1,
        'db10c93d689a45b38a1cd44c23b17830', 1, null, null, null, null, null, 'bytter-service-finance', '还款确认');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('5a532df93e75494ca62a222ac68d21f3', '4bc0a959d1654c9da04edc2d20a88085', '兑付确认', 'accebillAbEmitBillPay',
        '1', '/bill/accebill/abemitbillpay/listApprove', 4, '/bill/accebill/abemitbillpay/mobileApprove',
        '5ff2fe20e7f248b093488f6eb312f902', 0, 1, '15dfb2276b9e4a7dbd01024c709c2873', null,
        'sys:flow:approve:accebillAbEmitBillPay', null, null, null, null, 'bytter-prepose-bill', '兑付确认');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('5ac49b78e6344b0eaeec36806ae13952', '4bc0a959d1654c9da04edc2d20a88085', '未用退票', 'accebillAbEmitBillBack',
        '1', '/bill/accebill/abemitbillback/listApprove', 3, '/bill/accebill/abemitbillback/mobileApprove',
        '5ff2fe20e7f248b093488f6eb312f904', 0, 1, '6e36dd3994664be2a40a3e5ce420a88a', null,
        'sys:flow:approve:accebillAbEmitBillBack', null, null, null, null, 'bytter-prepose-bill', '未用退票');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('5b022edcc94e49c3a743473da89c19b7', 'a48d5e38146443359ff9f9d456c317a0', '费用报销', 'netbankExpense', '1',
        '/expense/nisexpensedetail/listApprove', 101, '/expense/nisexpensedetail/mobileApprove', '', 0, 1,
        '4028802f5c5ca03d015c5d08b3420000', 1, 'sys:flow:approve:netbankExpense', null, 'meiyf01', null,
        '2023-09-14 16:58:26', 'bytter-service-payment', '费用报销制单');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('5c57d5a6c08d473fbd2fe8e34b540359', 'f37f48e7002a401ebe6b21e09395d1fa', '委托贷款还款', 'cmsPrecativeRepay',
        '1', '/cms/precative/loanrepay/listRepayApprove', 7, '/cms/precative/loanrepay/mobileRepayApprove',
        '7c6e6b8e3f7a40939221a94795243804', 0, 1, '268b32af2dd64b99aa308802103d4681', null,
        'sys:flow:approve:cmsPrecativeRepay', null, null, null, null, 'bytter-service-finance', '委托贷款归还');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('5cc72fb3497e485e9f8ed8e3495bb840', 'f37f48e7002a401ebe6b21e09395d1fa', '委托贷款录入(委托方)',
        'cmsPrecativeInfoPre', '1', '/cms/precative/loaninfo/listApprove', 1, '/cms/precative/loaninfo/mobileApprove',
        '018a5a85fa574dc79d718f61e9df0916', 0, 1, 'a650157820b249138e4e4d3771db0b94', null,
        'sys:flow:approve:cmsPrecativeInfoPre', null, null, null, null, 'bytter-service-finance', '委托贷款录入');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('5f245229483f437b9921d42a053cdfcc', '55e77d690af841cfbb7724f94d985711', '借款展期申请', 'cmsProvideDeferred',
        '1', '/cms/provide/provideloandeferred/listApprove', 6,
        '/cms/provide/provideloandeferred/mobileDeferredApprove', null, 0, 1, '6234f536c3a341ea915a19f05f270412', null,
        'sys:flow:approve:cmsProvideDeferred', null, null, null, null, 'bytter-service-finance', '借款展期申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('6294848d837f42f0b812337d399d59bd', '25ba61a2a4ad408b8cca9c4aba89ef3a', '银行授信额度调剂', 'ratBkrationRelief',
        '1', '/cms/rat/ratbkrationrelief/listApprove', 5, '/cms/rat/ratbkrationrelief/mobileApprove',
        '179dbb6793074f679e0e8c81602618d8', 0, 1, '7d670155173a43fcb4b822a27907e03d', null,
        'sys:flow:approve:ratBkrationRelief', null, null, null, null, 'bytter-prepose-credit', '银行授信额度调剂');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('62dd2803970c477eb84b0d5d298e916a', 'd113f998a2dd49aaaff2907fa64d5ac1', '敞口上报', 'fexExposureMain', '1',
        '/foreign/fexExposure/listApprove', 7, '/foreign/fexExposure/mobileApprove', '', 0, 1,
        '72dbb69a263f4091a300c00520edbc06', 1, 'sys:flow:approve:fexExposureMain', null, 'meiyf01', null,
        '2023-08-23 17:19:03', null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('631605f6b9bb43bbbe9eb7302e6c21b4', '02523ab286714d96823300edb953e2ea', '融资租赁', 'Lease', '1', null, null,
        null, null, 0, 1, '28183155434f425e8ce436c2b19eb2fd', 1, null, null, null, null, null, null, '信贷融资');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('63c3faf05c1c461788abf5ae9205a9ec', '63c7205f61af4e5ebedf73f72647209d', '理财类投资', 'choProduct', '1', null,
        2, null, null, 0, 1, '11c845f733fd46ccb8397b17ba03d4ca', null, null, null, null, null, null, null,
        '银行理财类投资管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('63c7205f61af4e5ebedf73f72647209d', null, '理财管理', 'chowmatistic', '1', null, 2, null, null, 0, 1,
        '90f6219604ea4303a6b2c3d99d177d7d', null, null, null, null, null, null, null, '理财管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('657b141180a24c378b3c9060ae7ff020', 'f37f48e7002a401ebe6b21e09395d1fa', '委托贷款展期补录',
        'cmsPreDeferredRepair', '1', '/cms/precative/precativeloandeferred/listRepairApprove', 11,
        '/cms/precative/precativeloandeferred/mobileDeferredRepairApprove', 'c87d5ef440444f7e965b9033b17c8942', 0, 1,
        '53b7e5f9248b48cd82eabaaa54ff671f', null, 'sys:flow:approve:cmsPrecativeDeferredRepair', null, null, null, null,
        'bytter-service-finance', '委托贷款展期补录');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('67182fc663fa4b6c97c1bb8042a00848', '6d7d608f4d0f4944988ce7f3759b95e8', '银行账户余额管理', 'netbankBankaccBal',
        '1', '/bankacc/bisaccbal/listApprove', 104, '/bankacc/bisaccbal/mobileApprove',
        'd3253b5bb9f24412b5a05ab3dd2aba23', 0, 1, '16df50dcd9544ddfb808f9213994e151', null,
        'sys:flow:approve:netbankBankaccBal', null, null, null, null, 'bytter-service-payment', '银行账户余额管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('67d5f1a6041c458e8c64abd07d80fead', 'd6e02a26c6214751afa435ed835beda7', '保证金管理', 'bail', '1', null, 2,
        null, null, 0, 1, '4e55b9e1dc9b48df87d42cec7fac0e14', 1, null, null, null, null, null, null, '保证金管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('6b0d5429d0fb4d8a8b891d9a801bc387', 'f0893e845b3d4f479a7640d220c0955a', '提示收票', 'accebillReceiveApply', '1',
        '/bill/accebill/abbillReceiveApply/listApprove', 4, '/bill/accebill/abbillReceiveApply/mobileApprove',
        '5c22027be9534aecbc50809709bacd01', 0, 1, '78979fbe31a04d73971bc74f8e0098ff', null,
        'sys:flow:approve:accebillReceiveApply', null, null, null, null, 'bytter-prepose-bill', '提示收票');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('6b1446f65a4748feb62b96501dcbd527', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁租金支付申请',
        'cmsLeasePayInfo', '1', '/cms/lease/leasepay/listLeasePayApprove', null, '', '', 0, 1,
        '6cc7796a1fbd4af99611f2d0cca0a5c5', 1, 'sys:flow:approve:cmsLeasePayInfo', null, '测试人员', null,
        '2023-08-11 10:52:42', 'bytter-service-finance', '融资租赁租金支付申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('6b1446f65aiu48feb62b96501dcbd527', 'f0893e845b3d4f479a7640d220c0955a', '签收补录', 'accebillSignSupply', '1',
        '/bill/accebill/abbillSignSupply/listApprove', 2, null, null, 0, 1, '88551480b4df4292oyjiu2bbd9pp634a', 1,
        'sys:flow:approve:accebillSignSupply', null, null, null, null, 'bytter-prepose-bill', '签收补录');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('6c636c36211a4b61bc30287900a42c0f', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁回购', 'afterLeaseBuyBack',
        '1', '/cms/afterLease/buyBack/listApprove', null, null, null, 0, 1, '2e55cbc9c2564ce5b06d092b9150fd23', 1,
        'sys:flow:approve:afterLeaseBuyBack', null, null, null, null, 'bytter-service-finance', '融资租赁回购');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('6d7d608f4d0f4944988ce7f3759b95e8', 'a48d5e38146443359ff9f9d456c317a0', '账户管理', 'netbankBankacc', '1', null,
        500, null, null, 0, 1, 'b132ba019e2a4139815203b96b3de76c', 1, null, null, null, null, null, null, '账户管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('6efe1953c3ca124833eae4189bb7b435', '2521e91e96f14c6482f3705edf3e395e', '银行支取通知', 'choBankGETN', '1',
        '/chowmatistic/chobankfinance/listApprove/14', 5, '/chowmatistic/chobankfinance/mobileApprove',
        'f03091c1890e4cccbee9e8ad60bc9af6', 0, 1, '16d59661301a46d5b5ce2e9081f81cd8', 1, 'sys:flow:approve:choBankGETN',
        null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('70b535131cf6453b9bee6f3ec1dc458c', '054b1c93dd50403bad2a4032b8df25a3', '额度分配核准', 'abPoolLimitAllotCheck',
        '1', '/billpool/limitAllotCheck/listApprove', 4, '/billpool/limitAllotCheck/mobileApprove', null, 0, 1,
        'f01b49ede05d4e09bdc505b8158cc7f7', 1, 'sys:flow:approve:abPoolLimitAllotCheck', null, null, null, null,
        'bytter-prepose-bill', '额度分配核准');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('71db3a4a62ba4431ba7403892907b7a0', '10e2593396de473da62575bbfa8489d8', '提示付款', 'abPoolCollectionApply',
        '1', '/billpool/collection/approve', 5, '/billpool/collection/mobileApprove', null, 0, 1,
        '5a803f9f09bf4a13acc19f489b3236c7', 1, 'sys:flow:approve:abPoolCollectionApply', null, null, null, null,
        'bytter-prepose-bill', '提示付款');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('7248874ae83d4ddd82a184d42bb0e1ad', '78782099c66d4713bd425c694fa00cf8', '信用证开证', 'lcopen', '1', null, 1,
        null, null, 0, 1, 'feaf8940db824f5f88a70f0523639378', null, null, null, null, null, null, null,
        '开出信用证登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('740a5e051522453eac8ff2796e7c2123', '55e77d690af841cfbb7724f94d985711', '借款展期补录',
        'cmsProvideDeferredRepair', '1', '/cms/provide/provideloandeferred/listRepairApprove', 7,
        '/cms/provide/provideloandeferred/mobileDeferredApprove', null, 0, 1, 'f2836360a6ad4875b2c484d8ebbc08db', null,
        'sys:flow:approve:cmsProvideDeferredRepair', null, null, null, null, 'bytter-service-finance', '借款展期补录');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('74904a7472a74b699701a15147ff679d', 'f37f48e7002a401ebe6b21e09395d1fa', '委托贷款发放', 'cmsPrecativeSend', '1',
        '/cms/precative/loansend/listApprove', 5, '/cms/precative/loansend/mobileSendApprove',
        'ba75f62417904ff19c8086fe096c3765', 0, 1, 'd02db7e8512146408b28047aad45f565', null,
        'sys:flow:approve:cmsPrecativeSend', null, null, null, null, 'bytter-service-finance', '委托贷款发放');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('74ef48866d8a4526b3043a974c089720', 'f0893e845b3d4f479a7640d220c0955a', '提示承兑', 'accebillAcceptApply', '1',
        '/bill/accebill/abbillAcceptApply/listApprove', 3, '/bill/accebill/abbillAcceptApply/mobileApprove',
        '8807509a88c84848b705375f28d8a626', 0, 1, '3893adfd5f774ed59bbef84d6b434606', null,
        'sys:flow:approve:accebillAcceptApply', null, null, null, null, 'bytter-prepose-bill', '提示承兑');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('765dca03e5f649cf9c7f04b00ccce5eb', '10e2593396de473da62575bbfa8489d8', '电票签收', 'abPoolSignApply', '1',
        '/billPool/abSign/listApprove', 1, '/billPool/abSign/mobileApprove', null, 0, 1,
        '0b43286a392342c5a776e5b2040a0f3b', 1, 'sys:flow:approve:abPoolSignApply', null, null, null, null,
        'bytter-prepose-bill', '电票签收');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('76b403aa01f040c49affc32275c46258', '0170b5c492624b1c928013ba0be72fd7', '股权融资缴付到账/过户',
        'cmsEquityPayThe', '1', '/cms/equity/payThe/listApprove', 4, null, null, 0, 1,
        'ad44163e3ee440c2a9d6345e0ac80ac5', 1, 'sys:flow:approve:cmsEquityPayThe', null, null, null, null,
        'bytter-service-finance', '股权融资缴付到账/过户');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('77b5108283b44b4d9dae86cdb743b42b', 'f37f48e7002a401ebe6b21e09395d1fa', '委托贷款补录', 'cmsPrecativeRepair',
        '1', '/cms/precative/loaninfo/listRepairApprove', 3, '/cms/precative/loaninfo/mobileRepairApprove',
        'd61c91c010854e87b5d7788eead79890', 0, 1, 'c4ad07b203d34440b422b983c15423b2', null,
        'sys:flow:approve:cmsPrecativeRepair', null, null, null, null, 'bytter-service-finance', '委托贷款补录');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('78176ae35a6e467eb1d860f72d9ccd70', '5a3270cba98144ccac5fa886d3454a1b', '内部拆借申请（拆出方）', 'cmsInlOutInfo',
        '1', '/inl/change/listApprove', null, '', '', 0, 0, '038baecde33b4ff3814766fa6939fcec', 0,
        'sys:flow:approve:cmsInlOutInfo', null, '超级管理员', null, '2023-07-24 15:20:27', 'bytter-service-finance',
        '内部拆借申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('78782099c66d4713bd425c694fa00cf8', null, '国际结算', 'receive', '1', null, 5, null, null, 0, 1,
        'e837e42f607d4d94b1e65ca4d49e6113', null, null, null, null, null, null, null, '国际结算');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('78ece80ab771466f9454761764cfe96e', 'df12a9d025bc4695ad20fdf1c373a007', '合同变更', 'conConstractChange', '1',
        '/contract/conchange/listApprove', 4, '/contract/conchange/mobileApprove', null, 0, 1,
        '70af20778dbd4c49a2498429e79570c6', 1, 'sys:flow:approve:conConstractChange', null, null, null, null,
        'bytter-prepose-guarantee', '合同变更');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('7a0b4aa8f7dd4c2cba660612d315b5a8', '25ba61a2a4ad408b8cca9c4aba89ef3a', '内部授信额度分配', 'ratCprationDis',
        '1', '/cms/rat/ratcprationdis/listApprove', 10, '/cms/rat/ratcprationdis/mobileApprove', null, 0, 1,
        '45c00a9ec7ea4a4fb567121aaeb184c0', null, 'sys:flow:approve:ratCprationDis', null, null, null, null,
        'bytter-prepose-credit', '内部授信额度分配');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('7b2ef8bf47f84994b127628eeba50245', '2521e91e96f14c6482f3705edf3e395e', '银行活期转通知', 'choBankC2N', '1',
        '/chowmatistic/chobankfinance/listApprove/12', 3, '/chowmatistic/chobankfinance/mobileApprove',
        'c54052ef0284417ba4eb17a771bd2250', 0, 1, 'c3496bf3db2348e09e0b88e1ef734b09', 1, 'sys:flow:approve:choBankC2N',
        null, null, null, null, 'bytter-service-increment', '活期转通知');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('7ee5cbffbe1249b5870c62a314858e5c', '918303850a10498883cd5eddc3d31ca1', '出口押汇', 'lcReceiveMortgage', '1',
        '/international/credit/mortgageletter/mortgageApproveList', null,
        '/international/credit/mortgageletter/mobileViewMortgageLetter', null, 0, 1, '7efe27a18ff2496a9791e21b96233878',
        null, 'sys:flow:approve:lcReceiveMortgage', null, null, null, null, 'bytter-prepose-credential', '出口押汇');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('7f71ed7f6a1f46ddb428fb18973a8614', '63c7205f61af4e5ebedf73f72647209d', '内部理财', 'InnerFinance', '1', null,
        3, null, null, 0, 1, null, null, null, null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('7f78d7bb4c2c4776936e5da602505b05', '9dd1aed4f5834305a666bee0e4f3166f', '抵质押物登记', 'guaPledgeRegist', '1',
        '/cms/guarantee/pledgeInfo/listApprove', 0, '/cms/guarantee/pledgeInfo/mobileApprove', null, 0, 1,
        '2c6b0706d4164da3b90554f630faf357', null, 'sys:flow:approve:guaPledgeRegist', null, null, null, null,
        'bytter-prepose-guarantee', '抵质押物登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('7fb7943b906c417aac89839e5c3b5672', 'f0893e845b3d4f479a7640d220c0955a', '追索清偿', 'accebillSearchforManage',
        '1', '/bill/accebill/abbillSearchforManage/listApprove', 8,
        '/bill/accebill/abbillSearchforManage/mobileApprove', '5c22027be9534aecbc50809709bacd04', 0, 1,
        'cd29ddb4b22c405382631a74dc76f48a', 1, null, null, null, null, null, 'bytter-prepose-bill', '追索清偿');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('808376c2ae4146a78143155ad4ba7778', null, '第三方支付', 'ttpay', '1', null, 6, null, null, 0, 1,
        'd03c955371304c9b8f1b7ff034f8b52d', null, null, null, null, null, null, null, '第三方支付');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('8105e4ce58b64f1cb6d3cd6ffd0c22f2', '808376c2ae4146a78143155ad4ba7778', '退款申请', 'tpRefundOrderApply', '1',
        '/ttpay/refundOrderApply/approve', 1, '/ttpay/refundOrderApply/mobileApprove', null, 0, 1,
        'a0fc989ad0d840a9a73d0f801523842e', null, 'sys:flow:approve:tpRefundOrderApply', null, null, null, null,
        'bytter-service-payment', '退款申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('8678cfa7ab4e4e23a9b04564409167a3', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁补录',
        'cmsFinancingLeaseRepair', '1', '/cms/lease/financing/info/listRepairApprove', 2, null, null, 0, 1,
        '5ffce231589046c9831871ce4a785c3e', 1, 'sys:flow:approve:cmsFinancingLeaseRepair', null, null, null, null,
        'bytter-service-finance', '融资租赁补录');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('88b2d7c9cde2452b96507303b912708e', '25ba61a2a4ad408b8cca9c4aba89ef3a', '内部授信额度追加', 'ratCprationAdd',
        '1', '/cms/rat/ratcprationadd/listRatCpAddApprove', 9, '/cms/rat/ratcprationadd/mobileApprove', null, 0, 1,
        '186c42ded0d1486388b532ccb5255947', null, 'sys:flow:approve:ratCprationAdd', null, null, null, null,
        'bytter-prepose-credit', '内部授信额度追加');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('88f702aba1874c9ab19269ff62c8b4c7', 'a48d5e38146443359ff9f9d456c317a0', '收款管理', 'netbankReceivable', '1',
        '/receivable/nisreceivable/listApprove', 200, null, 'b168b458eb7946f38bd8f5f2b93678e8', 0, 1,
        '3bbf1a1564c04fffa39dcca25acfb20e', 1, 'sys:flow:approve:netbankReceivable', null, null, null, null,
        'bytter-service-payment', '收款办理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('8d2aebe8050c451aa809665c5ad77b95', '08ff60f9895742a48ad7105d5b610a44', '资金调拨交易', 'fexTranReqAllocation',
        '1', '/foreign/fextranreq/listApprove/3', 2, '/foreign/fextranreq/mobileApprove',
        'b53dbdb8779d4485b4b4e0be1900d70e', 0, 1, null, 1, 'sys:flow:approve:fexTranReqAllocation', null, null, null,
        null, 'bytter-service-payment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('8e044000d6594fe3a6718f528b3c846c', '7248874ae83d4ddd82a184d42bb0e1ad', '付汇申请', 'lcPayment', '1',
        '/international/credit/payment/listPaymentApprove', 300, '/international/credit/payment/mobileViewPayment',
        null, 0, 1, '63691cc8bec14ca2abfcd02368e7f033', null, 'sys:flow:approve:lcPayment', null, null, null, null,
        'bytter-prepose-credential', '申请付汇');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('8ec93814a58a49e9b04d64daba2d2b3c', '67d5f1a6041c458e8c64abd07d80fead', '保证金提取', 'abPoolBailDtlOutApply',
        '1', '/billPool/bail/outApprove', 2, '/billPool/bail/mobileOutApprove', null, 0, 1,
        'a1b2346ab45d432283afa6d1515a35ee', 1, 'sys:flow:approve:abPoolBailDtlOutApply', null, null, null, null,
        'bytter-prepose-bill', '保证金提取');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('91353514c51147029c607c16d9c9f11e', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁退租', 'afterLeaseRefund', '1',
        '/cms/afterLease/refund/listApprove', null, null, null, 0, 1, 'fd3fd073c3ca45d0b3eba7f4458e49ff', 1,
        'sys:flow:approve:afterLeaseRefund', null, null, null, null, 'bytter-service-finance', '融资租赁退租');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('9162534be9534bd89d8027ccd4cc1c22', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁申请', 'cmsFinancingLeaseInfo',
        '1', '/cms/lease/financing/info/listInfoApprove', 1, null, null, 0, 1, '1bee87ec34ae4c58b596093b6ed568db', 1,
        'sys:flow:approve:cmsFinancingLeaseInfo', null, null, null, null, 'bytter-service-finance', '融资租赁申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('918303850a10498883cd5eddc3d31ca1', '78782099c66d4713bd425c694fa00cf8', '收到信用证登记', 'lcreceive', '1',
        null, 2, null, null, 0, 1, '2f5615d048d54846b5a89e4f5099730c', null, null, null, null, null, null, null,
        '收到信用证登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('91b433468a274226b1428b94d81b278f', '7f71ed7f6a1f46ddb428fb18973a8614', '内部活期转通知', 'choInnerC2N', '1',
        '/chowmatistic/choinnerfinance/listApprove/12', 6, '/chowmatistic/choinnerfinance/mobileApprove',
        '405f9f614b7943f4b1e38384538c5fd7', 0, 1, null, null, 'sys:flow:approve:choInnerC2N', null, null, null, null,
        'bytter-service-increment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('92e77e6a052747af890ec91d936f7076', 'd113f998a2dd49aaaff2907fa64d5ac1', '走个流程', 'Zperson', '1',
        '/basic/business/person/listApprove', 500, null, null, 0, 1, '0b0dd16c88204096945492d46127f84d', 1,
        'sys:flow:approve:Zperson', null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('94a2826a40734e64b19c2728e480a056', '24fe7756278447aca8934976e9d84c2f', '双管理员', 'sysDualManager', '1',
        '/dual/manager/approveList', 500, null, null, 0, 1, null, null, 'sys:flow:approve:sysDualManager', null, null,
        null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('95bc41c052324e489dac9a7d2cc66e8f', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁变更', 'cmsLeaseUpdate', '1',
        '/cms/lease/update/listUpdateApprove', 12, null, null, 0, 1, '9a693a3156324c69af0e0415a140b49c', 1,
        'sys:flow:approve:cmsLeaseUpdate', null, null, null, null, 'bytter-service-finance', '融资租赁变更');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('9820c5d4922b40b49fa9a7c34413aedc', '0c965d29c1de4d6083718af7b510b7eb', '交易对手默认银行账户维护',
        'fexCotpDefaultAcc', '1', '/foreign/fexcotpdefaultacc/listApprove', 20,
        '/foreign/fexcotpdefaultacc/mobileApprove', null, 0, 1, null, null, 'sys:flow:approve:fexCotpDefaultAcc', null,
        null, null, null, 'bytter-service-payment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('9c27fe2cd74c4985ae38d50249b334d9', '25ba61a2a4ad408b8cca9c4aba89ef3a', '银行授信额度分配', 'ratBkrationDis',
        '1', '/cms/rat/ratbkrationdis/listApprove', 2, '/cms/rat/ratbkrationdis/mobileApprove',
        'd35b94d74aa442ecab455bd34b979915', 0, 1, '8f8e634fc57c49c8bb5770a9cff0820c', null,
        'sys:flow:approve:ratBkrationDis', null, null, null, null, 'bytter-prepose-credit', '银行授信额度分配');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('9d683b60ab5a4150b7b030b82bb927d2', '25ba61a2a4ad408b8cca9c4aba89ef3a', '内部授信额度录入', 'ratCpration', '1',
        '/cms/rat/ratcpration/listApprove', 8, '/cms/rat/ratcpration/mobileApprove', null, 0, 1,
        '9886659799c940be80689de0cee51963', null, 'sys:flow:approve:ratCpration', null, null, null, null,
        'bytter-prepose-credit', '内部授信额度录入');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('9dd1aed4f5834305a666bee0e4f3166f', '02523ab286714d96823300edb953e2ea', '担保管理', 'gua', '1', null, 5, null,
        null, 0, 1, 'f6651ae1b18b4da18ccf5490fbd655d0', null, null, null, null, null, null, null, '担保管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('9f4f6ace443047aa94fa9622ddfa7b1e', '7f71ed7f6a1f46ddb428fb18973a8614', '协定存款终止', 'choAgreeddepositTerm',
        '1', '/agreeddeposit/choinneragreeddeposit/listApprove/2', 3,
        '/agreeddeposit/choinneragreeddeposit/mobileApprove', '1982b7a1a3154fdc8289351fbaf20500', 0, 1, null, null,
        'sys:flow:approve:choAgreeddepositTerm', null, null, null, null, 'bytter-service-increment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('a2274e9e4c104640b7bb5dfeb37a8451', 'a48d5e38146443359ff9f9d456c317a0', '付款办理', 'netbankPay', '1',
        '/netbank/nisbillhead/listApprove', 102, '', '', 0, 1, '4028802f5c2eac34015c2ec1b9c40036', 1,
        'sys:flow:approve:netbankPay', null, 'zp001', null, '2023-11-17 14:41:33', 'bytter-service-payment',
        '付款办理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('a28d81099a174e7cb3e9b77f3bd02b6e', '1084ebdf12494cce9f5ef96f0fe0748c', '结算执行', 'fexCashFlow', '1',
        '/foreign/fexcashflow/listApprove', 10, '/foreign/fexcashflow/mobileApprove',
        '5f19c5dadcba42038219f9400ac629b9', 0, 1, '31c20dccdebc472ab2d1bf9efd2e5a4e', 1, 'sys:flow:approve:fexCashFlow',
        null, null, null, null, 'bytter-service-payment', '结算执行');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('a41dbb49fa71441da053ab10827b2e49', '5a3270cba98144ccac5fa886d3454a1b', '内部资金拆借', 'inl', '1', null, null,
        null, null, 0, 1, null, 1, null, null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('a48d5e38146443359ff9f9d456c317a0', null, '结算管理', 'netbank', '1', '', 1, '', '', 0, 0,
        '4028802f5bf6b978015bf6bb00190000', 0, null, null, '超级管理员', null, '2023-07-10 14:59:59', null, '结算管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('a59e6b068ba6424f83d18214e0e2f8b6', 'df12a9d025bc4695ad20fdf1c373a007', '合同签署', 'conConstractSign', '1',
        '/contract/signed/signApprove', 2, '/contract/signed/mobile/approve', null, 0, 1,
        '712c609fa8e94c3482de44598f7af81c', 1, 'sys:flow:approve:conConstractSign', null, null, null, null,
        'bytter-prepose-guarantee', '合同签署');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('a6eb7ca67fdc4856a729fa06bd7aa602', '7f71ed7f6a1f46ddb428fb18973a8614', '内部取消通知预约', 'choInnerB2N', '1',
        '/chowmatistic/choinnerfinance/listApprove/15', 8, '/chowmatistic/choinnerfinance/mobileApprove',
        '0f5929e69dea49c4b20bf4037f59996e', 0, 1, null, null, 'sys:flow:approve:choInnerB2N', null, null, null, null,
        'bytter-service-increment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('a6f8326cdbf6425994ca93a4ca412bc1', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁展期', 'LeaseExtend', '1',
        '/cms/LeaseExtend/listApprove', null, null, null, 0, 1, '587b8b9315254b68ba99fc9e02414136', 1,
        'sys:flow:approve:LeaseExtend', null, null, null, null, 'bytter-service-finance', '融资租赁展期');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('a7a19b14f9bb4e549a7acaca512aa64d', '55e77d690af841cfbb7724f94d985711', '借款作废', 'cmsProvideAbolish', '1',
        '/cms/provide/abolish/listAbolishApprove', 10, '/cms/provide/abolish/mobileAbolishApprove', null, 0, 1,
        '8fdb849016014c788af4df547142091a', null, 'sys:flow:approve:cmsProvideAbolish', null, null, null, null,
        'bytter-service-finance', '借款作废申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('a8fb56299c05459f9440c67ba83d9441', '04ea972c192846c39da7e03e91657514', '票据下拨',
        'abTransferDownManage', '1', '/bill/accebill/abbilltransferdownmanage/listApprove', 15,
        '/bill/accebill/abbilltransferdownmanage/mobileApprove', 'a73641c2c2da471e9bb8762adf499007', 0, 1,
        '223592929f2d4155b24a6d7468eaad1a', null, 'sys:flow:approve:accebillTransferDownManage', null, null, null, null,
        'bytter-prepose-bill', '票据下拨');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('a93c99ccbfe04804aeea158f63ff7e26', '5a3270cba98144ccac5fa886d3454a1b', '内部资金拆借申请', 'cmsInlInfo', '1',
        '/inl/info/listApprove', null, null, null, 0, 1, '038baecde33b4ff3814766fa6939fcec', 1,
        'sys:flow:approve:cmsInlInfo', null, null, null, null, 'bytter-service-finance', '内部拆借申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('aa838fe922204b88907c0036bbd19e8f', '003fbd92d08744f89666f95186b01abe', '贷款申请', 'cmsloanInfo', '1',
        '/cms/loan/cmsloaninfo/listAddApprove', 1, '/cms/loan/cmsloaninfo/mobileAddApprove',
        '3c1b8c9669ac4c279e1d0a93affa6b9b', 0, 1, '87b01bef4018403280085d265a9f1edc', null,
        'sys:flow:approve:cmsloanInfo', null, null, null, null, 'bytter-service-finance', '贷款申请录入');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ac6c41877ddc41bd9c504d8d2f416e7e', '02523ab286714d96823300edb953e2ea', '直融业务', 'df', '1', null, 10, null,
        null, 0, 1, null, null, null, null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('adee2223531045d694a5a9294c8f4f6a', '4bc0a959d1654c9da04edc2d20a88085', '开票补录', 'accebillAbEmitBillSupply',
        '1', '/bill/accebill/abEmitBillSupply/listApprove', 2, '/bill/accebill/abEmitBillSupply/mobileApprove',
        '5ff2fe20e7f248b093488f6eb312f903', 0, 1, 'c5b3aa8c9ec54c35bf82f0deb1c87585', null,
        'sys:flow:approve:accebillAbEmitBillSupply', null, null, null, null, 'bytter-prepose-bill', '开票补录');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ae3f0b82edaa4d8b9b2b61b8f272adf0', '55e77d690af841cfbb7724f94d985711', '银行借款还款申请',
        'cmsProvideLoanRepay', '1', '/cms/provide/loanrepay/listRepayApprove', 4,
        '/cms/provide/loanrepay/mobileRepayApprove', null, 0, 1, '1f7c58b4e7e147dcbac86333c39190f9', 1,
        'sys:flow:approve:cmsProvideLoanRepay', null, null, null, null, 'bytter-service-finance', '借款还款申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ae9791b2ceb04caa9720ae54740efc01', '32d5a1d717fa458bb33d3eae3c8f6466', '票据维护', 'billMaintain', '1', null,
        4, null, null, 0, 1, '402880455c616213015c6162e77d0000', 1, null, null, null, null, null, null, '承兑汇票');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('aeeb1e21883b4872b028d759033883a4', '7f71ed7f6a1f46ddb428fb18973a8614', '内部通知转活期', 'choInnerN2C', '1',
        '/chowmatistic/choinnerfinance/listApprove/13', 9, '/chowmatistic/choinnerfinance/mobileApprove',
        'd7ccccbfb44441e78e0492a41c5f0ab8', 0, 1, null, null, 'sys:flow:approve:choInnerN2C', null, null, null, null,
        'bytter-service-increment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('aefcc674090c4a579043a0b292c96557', '2521e91e96f14c6482f3705edf3e395e', '银行活期转定期', 'choBankC2T', '1',
        '/chowmatistic/chobankfinance/listApprove/10', 1, '/chowmatistic/chobankfinance/mobileApprove',
        '3e75a4bf2a2844d98a3ac42c64de43a6', 0, 1, 'b9ea94320ff440bea030b1df211904df', null,
        'sys:flow:approve:choBankC2T', null, null, null, null, 'bytter-service-increment', '活期转定期');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('af650592caa34a8bb14e2e25f8baec91', '04ea972c192846c39da7e03e91657514', '票据下拨申请',
        'accebillTransferDownApply', '1', '/bill/accebill/abbilltransferdownapply/listApprove', 14,
        '/bill/accebill/abbilltransferdownapply/mobileApprove', 'a73641c2c2da471e9bb8762adf499015', 0, 1,
        '076fc12405394502918a50dc8207e3c5', null, 'sys:flow:approve:accebillTransferDownApply', null, null, null, null,
        'bytter-prepose-bill', '下拨申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b071b2378dd246f4aa853a352a0d0e0b', 'd6e02a26c6214751afa435ed835beda7', '融资申请管理', 'emitFina', '1', null,
        6, null, null, 0, 1, '25c821b9de5a41a3bcde88e4e93bf1af', 1, null, null, null, null, null, null, '融资申请管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b0b6164ff3524e22ae5bf2f0e31fefcd', '0170b5c492624b1c928013ba0be72fd7', '股权融资回购', 'cmsEquityRepo', '1',
        '/cms/equity/repo/listApprove', 7, null, null, 0, 1, 'c9add981ea4f4ed2a9ab50983c42b5d1', 1,
        'sys:flow:approve:cmsEquityRepo', null, null, null, null, 'bytter-service-finance', '股权融资回购');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b1bdb21f90ef4b4c9b877316d2191ffa', '55e77d690af841cfbb7724f94d985711', '银行借款申请', 'cmsProvideLoanInfo',
        '1', '/cms/provide/loaninfo/listInfoApprove', 1, '/cms/provide/loaninfo/mobileInfoApprove', '', 0, 1,
        'e537b1989b1a439ea256d4c8f824f4b1', 0, 'sys:flow:approve:cmsProvideLoanInfo', null, 'meiyf02', null,
        '2023-08-11 10:43:35', 'bytter-service-finance', '借款申请录入');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b3c36c632e914e04a6606731c374201d', '3a8e536a95ac4b04bb0d6cc537f58ee1', '计划上报', 'fbsBudgetInput', '1',
        '/budget/fbsbudget/listApprove', 500, '', '', 0, 0, 'ad726048e4c14b7a95538e556c3ee2df', 0,
        'sys:flow:approve:fbsBudgetInput', null, '超级管理员', null, '2023-07-24 15:44:29', 'bytter-manager-plan',
        '计划上报');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b3f76e47bd6d4191b3bf7673a0f6cc3a', 'uu1446f65aiu48feb62b96501dcbd527', '债券还息处理', 'cmsBondPayInterest',
        '1', '/cms/bond/cmsbondpayinterest/listApprove', null, null, null, 0, 1, 'a33e8ef41cc14e1c82f6ec711bd9b4e3', 1,
        'sys:flow:approve:cmsBondPayInterest', null, null, null, null, 'bytter-service-finance', '债券还息处理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b44f46dc7a1c463bbb5d56d0173e34a6', 'db9858a6268a43639fead1a7df6aca17', '出池申请', 'abPoolOutApply', '1',
        '/billPool/outApply/poolOutApprove', 2, '/billPool/outApply/mobileApprove', null, 0, 1,
        '59d7aca2c4f847b0a89d56055f206d50', 1, 'sys:flow:approve:abPoolOutApply', null, null, null, null,
        'bytter-prepose-bill', '池管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b637cf99b79b4b89afa5031dec9c0174', '3a8e536a95ac4b04bb0d6cc537f58ee1', 'Plan A', 'oyjStudent', '1',
        '/jStudent/listApprove', 1, '111111111', '', 0, 1, '', 1, 'sys:flow:approve:oyjStudent', null, '超级管理员',
        null, '2023-07-24 16:04:25', null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b731f2f25cd14c13a085afcaa3427577', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁租金计划登记',
        'cmsLeaseCapitalPlanInfo', '1', '/cms/lease/leasecapitalplan/listInfoApprove', 4, '', '', 0, 1,
        '1e0ae2c4c4db4758a97d2423f3eaae92', 1, 'sys:flow:approve:cmsLeaseCapitalPlanInfo', null, '测试人员', null,
        '2023-08-11 10:50:51', 'bytter-service-finance', '融资租赁租金计划登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b776bcb16c674f6d9b753e19f25cdf3d', '918303850a10498883cd5eddc3d31ca1', '信用证登记入库', 'lcReceiveStorage',
        '1', '/international/credit/receiveletter/receiveApproveList', 1,
        '/international/credit/receiveletter/mobileViewReceiveLetterOfCredit', null, 0, 1,
        '84bb86f5617c47f0af705ffa8756a626', null, 'sys:flow:approve:lcReceiveStorage', null, null, null, null,
        'bytter-prepose-credential', '信用证登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b8e4dbd992014229b9b585d567c92013', '003fbd92d08744f89666f95186b01abe', '贷款补录', 'cmsloanRepair', '1',
        '/cms/loan/cmsloaninfo/listRepairApprove', 2, '/cms/loan/cmsloaninfo/mobileRepairApprove',
        '63fc11f5abe14eedac3ba5f8500e0cb3', 0, 1, 'bd242a56f68d45bd91584598cdb5ea27', null,
        'sys:flow:approve:cmsloanRepair', null, null, null, null, 'bytter-service-finance', '贷款申请补录');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('b918087da24041b8ab99d35932d16eb6', '003fbd92d08744f89666f95186b01abe', '贷款发放', 'cmsloanSend', '1',
        '/cms/loan/cmsloansend/listSendApprove', 3, '/cms/loan/cmsloansend/mobileSendApprove',
        'cdfb8430ee094897a2c76286e4f1671a', 0, 1, 'b57f72d8c2c344838cf9baf8207bf76d', null,
        'sys:flow:approve:cmsloanSend', null, null, null, null, 'bytter-service-finance', '贷款发放');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('bc3aa2d3b6684ca38e2c203bb1ab9bd2', '7f71ed7f6a1f46ddb428fb18973a8614', '内部通知预约', 'choInnerN2B', '1',
        '/chowmatistic/choinnerfinance/listApprove/14', 7, '/chowmatistic/choinnerfinance/mobileApprove',
        '784e8cf69e0a4d558b70981a58a956b2', 0, 1, null, null, 'sys:flow:approve:choInnerN2B', null, null, null, null,
        'bytter-service-increment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('bd798f5d681b49ca9d7a8a9f4ceec8bd', 'a48d5e38146443359ff9f9d456c317a0', '内部托收', 'netbankCgsbill', '1', null,
        100, null, null, 0, 1, 'bb5a018aad78475ca20eccce4a932dcc', null, null, null, null, null, null, null,
        '委托收款');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('be1d5acb796646159572a12ac8203529', '3a8e536a95ac4b04bb0d6cc537f58ee1', '计划执行', 'fbsBudgetProcess', '1',
        '/budget/fbsBudgetProcess/listApprove', 500, '/budget/fbsBudgetProcess/mobileApprove',
        '81a8aaaaf9fe42e8a1b8fc50b873b47b', 0, 1, '34e0ad4d49184439a751946e6d6d3530', null,
        'sys:flow:approve:fbsBudgetProcess', null, null, null, null, 'bytter-manager-plan', '计划执行');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('bf2c3f92a5fd49708780a23ceb6260f3', '3e6ececfea4745a0b0a3b39564c10239', '交易对手银行账户', 'fexCotpBankAcc',
        '1', '/foreign/fexcotpbankacc/listApprove', 20, '/foreign/fexcotpbankacc/mobileApprove', '', 0, 1, '', 0,
        'sys:flow:approve:fexCotpBankAcc', null, 'zp001', null, '2023-11-15 19:56:29', 'bytter-service-payment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('bg2bf55a97544518bbe29e2e71685100', '32d5a1d717fa458bb33d3eae3c8f6466', '背书请求', 'endorseRequest', '1', null,
        5, null, null, 0, 1, null, null, null, null, null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('bg2bf55a97544518bbe29e2e71685101', 'bg2bf55a97544518bbe29e2e71685100', '背书请求', 'accebillEndorseRequest',
        '1', '/bill/accebill/endorseRequest/listApprove', 1, null, null, 0, 1, null, null, null, null, null, null, null,
        'bytter-prepose-bill', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('bg2bf55a97544518bbe29e2e71685102', 'bg2bf55a97544518bbe29e2e71685100', '背书请求应答',
        'accebillEndorseResponse', '1', '/bill/accebill/endorseResponse/listApprove', 2, null, null, 0, 1, null, null,
        null, null, null, null, null, 'bytter-prepose-bill', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('c1b3fbd899da4765bf13fc80b5af2e73', '7248874ae83d4ddd82a184d42bb0e1ad', '展期申请', 'lcDepositDelay', '1',
        '/international/credit/lcDepositDelay/listLcDepositDelayApprove', 500,
        '/international/credit/lcDepositDelay/mobileViewDepositDelay', null, 0, 1, 'aaaa00772a2844b39b5a64e5cd2922a5',
        null, 'sys:flow:approve:lcDepositDelay', null, null, null, null, 'bytter-prepose-credential', '申请展期');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('c242b006846c4fe4851ff9195f4c3413', '04ea972c192846c39da7e03e91657514', '贴现赎回申请',
        'accebillDiscountRansom', '1', '/bill/accebill/abbilldiscountransom/listApprove', 20,
        '/bill/accebill/abbilldiscountransom/mobileApprove', 'a73641c2c2da471e9bb8762adf499014', 0, 1,
        '9075fbcdb9374b7f9097438bdc829d85', null, 'sys:flow:approve:accebillDiscountRansom', null, null, null, null,
        'bytter-prepose-bill', '贴现赎回申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('c2f97cfe0b14469a850e3882b6674cc1', '08ff60f9895742a48ad7105d5b610a44', 'NDF交割', 'fexTranDealNdf', '1',
        '/foreign/fextranreq/listTranDealApprove/3', 6, '/foreign/fextranreq/mobileTranDealApprove/3',
        '97447844ded7467faa697adc8168ea98', 0, 1, null, 1, 'sys:flow:approve:fexTranDealNdf', null, null, null, null,
        'bytter-service-payment', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('c32cb6c4353e402d991ef3a94736d691', '7248874ae83d4ddd82a184d42bb0e1ad', '押汇申请', 'lcDeposit', '1',
        '/international/credit/lcDeposit/listDepositApprove', 400, '/international/credit/lcDeposit/mobileViewDeposit',
        null, 0, 1, 'ee53987f17a3420782bdf5ccfa0c1ab1', null, 'sys:flow:approve:lcDeposit', null, null, null, null,
        'bytter-prepose-credential', '进口押汇');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('c342a4d791d34d668304801f2509458f', 'df12a9d025bc4695ad20fdf1c373a007', '合同登记', 'conConstractRegister', '1',
        '/contract/conregister/listApprove', 3, '/contract/conregister/mobileApprove', null, 0, 1,
        'ae36cfaf7e424c5d9e763bac8feae423', 1, 'sys:flow:approve:conConstractRegister', null, null, null, null,
        'bytter-prepose-guarantee', '合同登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('c622c0e49e5848cca2ef83713f33bb42', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁标的物修改',
        'LeaseUpdateObjectInfo', '1', '/cms/lease/UpdateObjectInfo/listApprove', null, null, null, 0, 1,
        '0a76385f46324b7399fa239ec1c677cb', 1, 'sys:flow:approve:LeaseUpdateObjectInfo', null, null, null, null,
        'bytter-service-finance', '融资租赁标的物修改');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('c72976eab3354f2ba0af5fac6835cf24', '6d7d608f4d0f4944988ce7f3759b95e8', '银行账户解冻申请',
        'netbankBankaccUnFreeze', '1', '/bankacc/btbankaccunfreeze/listApprove', 107,
        '/bankacc/btbankaccunfreeze/mobileApprove', '1ccb354f74b746c1950ceddfb2c2bb15', 0, 1,
        '2f62da407fcb450a9902142b2649636f', null, 'sys:flow:approve:netbankBankaccUnFreeze', null, null, null, null,
        'bytter-service-payment', '银行账户解冻申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ca702e2a4efa4a039d4b0cd34edb0691', '6d7d608f4d0f4944988ce7f3759b95e8', '银行账户开户申请', 'netbankBankaccApp',
        '1', '/bankacc/btbankaccapp/listApprove/1', 101, '/bankacc/btbankaccapp/mobileApprove',
        '846ffc76350f42c69eb5208abc7abdec', 0, 1, '402880265c34a048015c34a709300002', null,
        'sys:flow:approve:netbankBankaccApp', null, null, null, null, 'bytter-service-payment', '银行账号开户申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('caa1e8ab7f0d4fc3bccaa3cd96e469b5', 'd113f998a2dd49aaaff2907fa64d5ac1', '交易需求管理', 'fexTranAppManage', '1',
        null, 4, null, null, 0, 1, '35f61b0bd18548449ae19655392f58de', 1, null, null, null, null, null, null,
        '交易需求管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('cadbbf7e6d244987afdc4e1ec3e5720d', 'uu1446f65aiu48feb62b96501dcbd527', '债券还款计划登记', 'cmsBondRepayPlan',
        '1', '/cms/bond/cmsbondrepayplan/listApprove', null, null, null, 0, 1, 'acdcf21999c4482887a2a2c79d4fe758', 1,
        'sys:flow:approve:cmsBondRepayPlan', null, null, null, null, null, '债券还款计划登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('cb59b145e7fe4b9fa16caa841f978580', '04ea972c192846c39da7e03e91657514', '票据背书', 'accebillEndorse', '1',
        '/bill/accebill/abbillendorse/listApprove', 4, '/bill/accebill/abbillendorse/mobileApprove',
        'a73641c2c2da471e9bb8762adf499004', 0, 1, 'c39edb6257fc4bb4a70a46425f4495d3', null,
        'sys:flow:approve:accebillEndorse', null, null, null, null, 'bytter-prepose-bill', '票据背书');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('cb6fb2a450f642399a1ebf9eb5f830c2', 'd113f998a2dd49aaaff2907fa64d5ac1', '外币汇款', 'fexForeignReq', '1',
        '/foreign/fexforeignreq/listApprove', 1, '/foreign/fexforeignreq/mobileApprove', null, 0, 1,
        '60d20b150ee049398c3d91b1d4ae0861', null, 'sys:flow:approve:fexForeignReq', null, null, null, null,
        'bytter-service-payment', '外币汇款申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('cbb14ee501a04c8682f6f0a2309ad796', '04ea972c192846c39da7e03e91657514', '票据移存', 'accebillInvenTransfer',
        '1', '/bill/accebill/abbillinventransfer/listApprove', 2, '/bill/accebill/abbillinventransfer/mobileApprove',
        'a73641c2c2da471e9bb8762adf499009', 0, 1, 'd37b9aa25e324f1e9eac602639927061', null,
        'sys:flow:approve:accebillInvenTransfer', null, null, null, null, 'bytter-prepose-bill', '票据移存');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('cd456921ddd44cf68cf0e53d936b6250', '196d5787108341049b4c8f4ca228c117', '股东借款还款', 'cmsBfsRepay', '1',
        '/cms/bfs/repay/listBfsRepayApprove', 21, null, null, 0, 1, 'b112e1548d0949c0ba6902d97f4c7650', 1, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('cf8205c57e1c485fa6862e1b4930ec16', '04ea972c192846c39da7e03e91657514', '质押收回', 'accebillPledgeCollect',
        '1', '/bill/accebill/abbillpledgecollect/listApprove', 10, '/bill/accebill/abbillpledgecollect/mobileApprove',
        'a73641c2c2da471e9bb8762adf499006', 0, 1, 'f99e8b43784446fd8b9ee6b36f5b4279', null,
        'sys:flow:approve:accebillPledgeCollect', null, null, null, null, 'bytter-prepose-bill', '质押收回');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d05e5226c6d1445f8471e18a3da45284', '054b1c93dd50403bad2a4032b8df25a3', '额度申领', 'abPoolLimitApply', '1',
        '/abpool/limitApply/approveList', 1, '/abpool/limitApply/mobileApprove', null, 0, 1,
        'a758531404bf4343b0ab09c8a50363ab', 1, 'sys:flow:approve:abPoolLimitApply', null, null, null, null,
        'bytter-prepose-bill', '额度管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d113f998a2dd49aaaff2907fa64d5ac1', null, '外汇管理', 'foreign', '1', null, null, null, null, 0, 1,
        '88cb524b44dc4d8d8ec7e56000e072cd', null, null, null, null, null, null, null, '外汇管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d2da24b7132e4a5db5f84419b95adfef', '63c3faf05c1c461788abf5ae9205a9ec', '理财登记', 'choProductReq', '1',
        '/chowmatistic/choproductreq/listApprove', 1, '/chowmatistic/choproductreq/mobileApprove',
        '8fdc03a98eef4451affa94037f1cb270', 0, 1, 'a6ede075de4246b69cb223664f1e80c8', null,
        'sys:flow:approve:choProductReq', null, null, null, null, 'bytter-service-increment', '理财登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d38b7af945d74c5e9ae0778f987d2537', '08ff60f9895742a48ad7105d5b610a44', '外汇期权交易', 'fexTranReqOption', '1',
        '/foreign/fextranreq/listApprove/2', 2, '/foreign/fextranreq/mobileApprove', 'f49f34d1ba83492b969c4f68ebb7edf0',
        0, 1, '4dfa9ebe56d341bfba3d030c51ffad1a', 1, 'sys:flow:approve:fexTranReqOption', null, null, null, null,
        'bytter-service-payment', '外汇期权交易');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d3b98178051040d9b48c3a1a997eec9c', '53f6b699b3b34240b9465c4864477eb4', '资产证券化登记', 'cmsSecurityInfo',
        '1', '/cms/security/info/listApprove', 1, null, null, 0, 1, '563cd8b7f325499a946a962ce3377cd5', 1, null, null,
        null, null, null, 'bytter-service-finance', '资产证券化登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d3ed227372b646ac98f37155989beb6d', '25ba61a2a4ad408b8cca9c4aba89ef3a', '银行授信额度录入', 'ratBkration', '1',
        '/cms/rat/ratbkration/listApprove', 1, '/cms/rat/ratbkration/mobileApprove', '2a4be72500ee4cb1b6c5962d07ffc3ef',
        0, 1, '3a8ba449df644ef5b22c553b5e0cbb1f', null, 'sys:flow:approve:ratBkration', null, null, null, null,
        'bytter-prepose-credit', '银行授信额度录入');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d4be7deefb524e5492a20054cf62c582', 'f37f48e7002a401ebe6b21e09395d1fa', '委托贷款录入(借款方)',
        'cmsPrecativeInfoLoan', '1', '/cms/precative/loaninfo/listApproveLoan', 2,
        '/cms/precative/loaninfo/mobileApprove', '018a5a85fa574dc79d718f61e9df0916', 0, 1,
        'a650157820b249138e4e4d3771db0b94', null, 'sys:flow:approve:cmsPrecativeInfoLoan', null, null, null, null,
        'bytter-service-finance', '委托贷款录入');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d5251be17dc443d29386c44637f39506', '04ea972c192846c39da7e03e91657514', '票据取票', 'accebillInvenFetch', '1',
        '/bill/accebill/abbillinvenfetch/listApprove', 3, '/bill/accebill/abbillinvenfetch/mobileApprove',
        'a73641c2c2da471e9bb8762adf499017', 0, 1, '7c8df2c262394a89a4f305066ded0856', null,
        'sys:flow:approve:accebillInvenFetch', null, null, null, null, 'bytter-prepose-bill', '票据取票');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d54fa8f77c074e2b95e62b983bd9288d', '5a3270cba98144ccac5fa886d3454a1b', '内部资金拆借利率调整',
        'cmsInlAdjustrate', '1', '/inl/adjustrate/listApprove', null, null, null, 0, 1, null, 1,
        'sys:flow:approve:cmsInlAdjustrate', null, null, null, null, 'bytter-service-finance', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d6839f51c43543769965470ac0909493', '53f6b699b3b34240b9465c4864477eb4', '资产证券化还款计划修改',
        'cmsSecurityPlanUpdate', '1', '/cms/security/plan/listUpdateApprove', 5, null, null, 0, 1,
        'd59f39223fb34743bac5ccff741dc794', 1, null, null, null, null, null, 'bytter-service-finance', '还款计划修改');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d6e02a26c6214751afa435ed835beda7', null, '集团票据池', 'billPool', '1', null, 4, null, null, 0, 1,
        'd73c35f292494dd899c139eac39d229a', 1, null, null, null, null, null, null, '集团票据池');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d749db63c5e949aeac7ebb99b528c1af', '04ea972c192846c39da7e03e91657514', '托收到账', 'abbillcollectionaccount',
        '1', '/bill/accebill/abbillcollectionaccount/listApprove', 18,
        '/bill/accebill/abbillcollectionaccount/mobileApprove', 'a73641c2c2da471e9bb8762adf499018', 0, 1,
        '7d81fd3e093f42deaedd902f3ce687d0', null, 'sys:flow:approve:abbillcollectionaccount', null, null, null, null,
        'bytter-prepose-bill', '托收到账');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d8510df0dcb84c89b1a23609c2c0cd50', '04ea972c192846c39da7e03e91657514', '贴现到账', 'accebillDiscountAccount',
        '1', '/bill/accebill/abBillDiscountAccount/listApprove', 7,
        '/bill/accebill/abBillDiscountAccount/mobileApprove', 'a73641c2c2da471e9bb8762adf499016', 0, 0,
        'a8c85ef549a84b0fbbe8966cb2ff7fbb', 0, 'sys:flow:approve:accebillDiscountAccount', null, null, null, null,
        'bytter-prepose-bill', '贴现到账');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('d98b4de7f5004892a5b3e627f4081029', '564deccaf9dc4093b8a823bc89390c60', '上缴', 'netbankAllocationTurn', '1',
        '/allocation/nisallocationreq/listApprove/14', 14, '/allocation/nisallocationreq/mobileApprove',
        'fb742402090149918dcc7ac218b74b79', 0, 1, '4028802e5c5d50ad015c5d8c6d970019', 1,
        'sys:flow:approve:netbankAllocationTurn', null, null, null, null, 'bytter-service-payment', '上缴');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('da60fc7c612b4a2db4acb52e6a0a1be7', '1c064cbc7a934d469c9131234a35fb34', '存入保证金', 'netbankFcBondSave', '1',
        '/bond/fcbond/listApprove', 1, '/bond/fcbond/mobileApprove', '', 0, 1, 'e1389d872dfa4198a84eb5de97c1b01a', 0,
        'sys:flow:approve:netbankFcBondSave', null, 'meiyf01', null, '2023-08-12 13:50:25', 'bytter-prepose-guarantee',
        '存入保证金');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('db4cf73f2c224352a6b40b9aafe5ac35', 'f0893e845b3d4f479a7640d220c0955a', '追索通知', 'accebillSearchforApply',
        '1', '/bill/accebill/abbillSearchforApply/listApprove', 7, '/bill/accebill/abbillSearchforApply/mobileApprove',
        '5c22027be9534aecbc50809709bacd03', 0, 1, '2d7f32ec0bbc4d70917f9e43ca825112', 1, null, null, null, null, null,
        'bytter-prepose-bill', '追索通知');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('db9858a6268a43639fead1a7df6aca17', 'd6e02a26c6214751afa435ed835beda7', '池管理', 'poolApple', '1', null, 3,
        null, null, 0, 1, '59d7aca2c4f847b0a89d56055f206d50', 1, null, null, null, null, null, null, '池管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('dcea5aa6b0e94c8590aea1b6a62519e2', 'f0893e845b3d4f479a7640d220c0955a', '提示保证', 'accebillGuaranteeApply',
        '1', '/bill/accebill/abbillGuaranteeApply/listApprove', 2, '/bill/accebill/abbillGuaranteeApply/mobileApprove',
        '5c22027be9534aecbc50809709bacdcb', 0, 1, 'd2dea480c6214d9d9581956ade72bc9b', null,
        'sys:flow:approve:accebillGuaranteeApply', null, null, null, null, 'bytter-prepose-bill', '提示保证');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ddf9c4f5849c47c48ea72a630e44bca6', '5a3270cba98144ccac5fa886d3454a1b', '内部资金拆借放款', 'cmsInlSend', '1',
        '/inl/send/listApprove', null, '', '', 0, 0, '', 0, 'sys:flow:approve:cmsInlSend', null, '超级管理员', null,
        '2023-07-24 15:35:53', 'bytter-service-finance', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('df12a9d025bc4695ad20fdf1c373a007', '', '合同管理-111', 'contract', '1', '', 7, '', '', 0, 1,
        '2b3ace2e58aa4dc294f198b14d98a4db', 1, null, null, 'meiyf01', null, '2023-11-03 10:17:21', null, '合同管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('dfddf5f013e4454686e08f8b2f61e698', '5a3270cba98144ccac5fa886d3454a1b', '内部资金拆借还款', 'cmsInlRepay', '1',
        '/inl/repay/listApprove', null, null, null, 0, 1, null, 1, 'sys:flow:approve:cmsInlRepay', null, null, null,
        null, 'bytter-service-finance', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('e0e87eee9fde4c13a12406b8c1ece6ac', '25ba61a2a4ad408b8cca9c4aba89ef3a', '授信延期', 'ratRationDelay', '1',
        '/cms/rat/ratbkrationdelay/listDelayApprove', 7, '', '', 0, 0, 'd49d1ddbd47d485395ef7ceaa011ba96', 0,
        'sys:flow:approve:ratRationDelay', null, 'meiyf01', null, '2023-08-12 09:11:18', 'bytter-prepose-credit',
        '授信延期');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('e27380aab3994e39801a29c1ad46950a', '196d5787108341049b4c8f4ca228c117', '股东借款登记', 'cmsBfsInfo', '1',
        '/cms/bfs/info/listBfsInfoApprove', 21, null, null, 0, 1, '9695b60e2d754c67ade1659a4ce50720', 1, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('e6784eeb04e349db96fd4d57e8b98c99', '2521e91e96f14c6482f3705edf3e395e', '银行通知转活期', 'choBankN2C', '1',
        '/chowmatistic/chobankfinance/listApprove/13', 4, '/chowmatistic/chobankfinance/mobileApprove',
        'f03091c1890e4cccbee9e8ad60bc9af6', 0, 1, '333b762a50ca4d56b3b7f922f1a4cd6a', 1, 'sys:flow:approve:choBankN2C',
        null, null, null, null, 'bytter-service-increment', '通知转活期');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('e6a5136d69184613b93f839292219d8b', '53f6b699b3b34240b9465c4864477eb4', '资产证券化还款计划登记',
        'cmsSecurityPlan', '1', '/cms/security/plan/listApprove', 4, null, null, 0, 1,
        '0bff8cf327234844be4c08bd54c14f74', 1, null, null, null, null, null, 'bytter-service-finance', '还款计划登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('e72bf55a97544518bbe29e2e71685162', 'ae9791b2ceb04caa9720ae54740efc01', '票据删除', 'accebillAbBillDelete', '1',
        '/bill/accebill/abbilldelete/listApprove', 1, '/bill/accebill/abbilldelete/mobileApprove', null, 0, 1,
        '984bcd3add3b4387b6d8707d5c5a6f73', 1, 'sys:flow:approve:accebillAbBillDelete', null, null, null, null,
        'bytter-prepose-bill', '票据删除');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('e7ea1556c8b1465e8f26a32c02c3fab2', '04ea972c192846c39da7e03e91657514', '应收退票', 'accebillAbReceiveBillBack',
        '1', '/bill/accebill/abreceivebillback/listApprove', 16, '/bill/accebill/abreceivebillback/mobileApprove',
        'a73641c2c2da471e9bb8762adf499012', 0, 1, 'e58af091b967453f916e8a3ed760eeed', null,
        'sys:flow:approve:accebillAbReceiveBillBack', null, null, null, null, 'bytter-prepose-bill', '退票申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ea8c58eac1934554b30cbc080d353bee', '6d7d608f4d0f4944988ce7f3759b95e8', '银行账户注销申请', 'netbankBankaccDel',
        '1', '/bankacc/btbankaccapp/listApprove/3', 103, '/bankacc/btbankaccapp/mobileApprove',
        '900b89dc14774623a97758e6bc7a3a49', 0, 1, '402880265c62d254015c6310b5150003', null,
        'sys:flow:approve:netbankBankaccDel', null, null, null, null, 'bytter-service-payment', '银行账户注销申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('eb168501e37d47d6afe61d2c76fb7bca', '53f6b699b3b34240b9465c4864477eb4', '资产证券化到账确认',
        'cmsSecurityToAccount', '1', '/cms/security/toAccount/listApprove', 3, null, null, 0, 1,
        '438ef15aba074858abfd50b1d9537243', 1, null, null, null, null, null, 'bytter-service-finance', '发行到账确认');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ed87b03d3f6145d296e091272eb6e8bc', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁续租', 'afterLeaseContinue',
        '1', '/cms/afterLease/continue/listApprove', null, null, null, 0, 1, '2eaad81ba55d4325b54a4a9bbebacf4d', 1,
        'sys:flow:approve:afterLeaseContinue', null, null, null, null, 'bytter-service-finance', '融资租赁续租');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ed9e50484ce24120aceebcb7e97777d3', 'df12a9d025bc4695ad20fdf1c373a007', '合同起草', 'conConstractDraft', '1',
        '/contract/condraft/listApprove', 1, '/contract/condraft/mobileApprove', null, 0, 1,
        'e2c8e3dd48344b758b9838a119519b0a', 1, 'sys:flow:approve:conConstractDraft', null, null, null, null,
        'bytter-prepose-guarantee', '合同起草');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ef33a2bf30c2433880252fd55ad66cf9', '10e2593396de473da62575bbfa8489d8', '背书申请', 'abPoolEndorseApply', '1',
        '/billPool/endorse/listApprove', 3, '/billPool/endorse/mobileApprove', null, 0, 1,
        '290a66054ac44ae8b8331870cf9dafd6', 1, 'sys:flow:approve:abPoolEndorseApply', null, null, null, null,
        'bytter-prepose-bill', '背书申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('efa7154e837346d89e2eb3bd5e07c1be', '25ba61a2a4ad408b8cca9c4aba89ef3a', '内部授信调剂', 'ratCprationRelief',
        '1', '/cms/rat/ratcprationrelief/listApprove', 12, '/cms/rat/ratcprationrelief/mobileApprove', null, 0, 1,
        '5cf6dd28135f40e5bae4c1b9f4113934', null, 'sys:flow:approve:ratCprationRelief', null, null, null, null,
        'bytter-prepose-credit', '内部授信额度调剂');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f0893e845b3d4f479a7640d220c0955a', '32d5a1d717fa458bb33d3eae3c8f6466', '电票业务', 'accebill', '1', null, 3,
        null, null, 0, 1, '918afac2c28e4ccc824b69c971b1119b', null, null, null, null, null, null, null, '电票业务');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f1386571094a41b1a229097ed33ee0d3', '25ba61a2a4ad408b8cca9c4aba89ef3a', '银行授信额度追加', 'ratBkrationAdd',
        '1', '/cms/rat/ratbkrationadd/listApprove', 3, '/cms/rat/ratbkrationadd/mobileApprove',
        'bcc7f092a6644ec887f4addbfbe144d6', 0, 1, 'ecf416c285104e9da30eb7ac32a98f1d', null,
        'sys:flow:approve:ratBkrationAdd', null, null, null, null, 'bytter-prepose-credit', '银行授信额度追加');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f216dee99b514af59a7d8bd14691ad98', 'db9858a6268a43639fead1a7df6aca17', '入池申请', 'abPollIntoapply', '1',
        '/billPool/intoApply/pooIntoApprove', 1, '/billPool/intoApply/mobileApprove', null, 0, 1,
        '59d7aca2c4f847b0a89d56055f206d50', 1, 'sys:flow:approve:abPollIntoapply', null, null, null, null,
        'bytter-prepose-bill', '池管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f216dee99b514af59a7d8bd14691ad99', 'db9858a6268a43639fead1a7df6aca17', '入池撤销审批', 'abPollIntoRepeal', '1',
        '/billPool/intoRepeal/pooIntoRepealApprove', 4, null, null, 0, 1, '5147ebf726fd4eabaee23829d89779b6', null,
        'sys:flow:approve:abPollIntoRepeal', null, null, null, null, 'bytter-prepose-bill', '入池撤销');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f2f181a3b6f74c129c6f2945985897e9', '6d7d608f4d0f4944988ce7f3759b95e8', '银行账户冻结申请',
        'netbankBankaccFreeze', '1', '/bankacc/btbankaccfreeze/listApprove', 106,
        '/bankacc/btbankaccfreeze/mobileApprove', 'adc78eead68e49dda1b74cef2380a0fc', 0, 1,
        '402880265ca45996015ca55022ae0027', null, 'sys:flow:approve:netbankBankaccFreeze', null, null, null, null,
        'bytter-service-payment', '银行账户冻结申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f2ff515786194606b94e47d605408410', '3a8e536a95ac4b04bb0d6cc537f58ee1', '计划追加', 'fbsBudgetSuperadd', '1',
        '/budget/fbsbudget/listSuperAddApprove', 500, '/budget/fbsbudgetsuperadd/mobileApprove', null, 0, 1,
        'e2e893a49d994ddcb03f00fa0886a110', null, 'sys:flow:approve:fbsBudgetSuperadd', null, null, null, null,
        'bytter-manager-plan', '计划追加');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f33256068909476f973250e5b7016f98', '564deccaf9dc4093b8a823bc89390c60', '头寸调拨', 'netbankAllocationPos', '1',
        '/allocation/nisallocationreq/listApprove/13', 13, '/allocation/nisallocationreq/mobileApprove',
        '90ab35f8f0ca4ae0828875aad8f7cd8a', 0, 1, '4028802e5c675881015c6767b8270014', 1,
        'sys:flow:approve:netbankAllocationPos', null, null, null, null, 'bytter-service-payment', '头寸调拨');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f37f48e7002a401ebe6b21e09395d1fa', '02523ab286714d96823300edb953e2ea', '委托贷款', 'cmsPrecative', '1', null,
        3, null, null, 0, 1, '4007b8a99f454b2abf2557eac3bec997', null, null, null, null, null, null, null, '委托贷款');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f753b4258f7843c49a2597894cb42d43', '04ea972c192846c39da7e03e91657514', '票据上划',
        'abTransferUpApply', '1', '/bill/accebill/abbilltransferupapply/listApprove', 12,
        '/bill/accebill/abbilltransferupapply/mobileApprove', 'a73641c2c2da471e9bb8762adf499001', 0, 1,
        'a214363e445f4fff8ef71d9504741606', null, 'sys:flow:approve:accebillAbBillTransferUpApply', null, null, null,
        null, 'bytter-prepose-bill', '票据上划');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f8d32c2e09ef43fa87e13a86c3be43b7', '631605f6b9bb43bbbe9eb7302e6c21b4', '融资租赁租金计划修改',
        'cmsLeaseCapitalPlanChange', '1', '/cms/lease/updateLeaseCapitalPlan/listInfoApprove', 4, null, null, 0, 1,
        'e1cb2fb17f154a7482d7c4f4af76ce19', 1, 'sys:flow:approve:cmsLeaseCapitalPlanChange', null, null, null, null,
        'bytter-service-finance', '融资租赁租金计划修改');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('f9788ef9b48340cb959530bb00f0d761', 'db9858a6268a43639fead1a7df6aca17', '质押托管转换', 'abPoolPdsTs', '1',
        '/billPool/pdtst/poolTstApprove', 3, '/billPool/pdtst/mobileApprove', null, 0, 1,
        '59d7aca2c4f847b0a89d56055f206d50', 1, 'sys:flow:approve:abPoolPdsTs', null, null, null, null,
        'bytter-prepose-bill', '池管理');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('faa87b4b3d1a45cd9651c586130dea2b', '7248874ae83d4ddd82a184d42bb0e1ad', '信用证补录', 'lcSupplementCredit', '1',
        '/international/credit/supplementCredit/listSupplementCreditApprove', 200,
        '/international/credit/supplementCredit/mobileViewSupplementCredit', null, 0, 1,
        'fe25d1cd7f9846428c88fe31b260305e', null, 'sys:flow:approve:lcSupplementCredit', null, null, null, null,
        'bytter-prepose-credential', '信用证补录');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('fae79b3a86cd4531b3e79479d4cac163', 'a48d5e38146443359ff9f9d456c317a0', '银企直连', 'netbankBdlink', '1', null,
        500, null, null, 0, 1, '96dcec827e474c9896673ccf1699f100', null, null, null, null, null, null, null,
        '银企直联');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('fc06a82de5314f04b5a1899bc93c26b9', 'ac6c41877ddc41bd9c504d8d2f416e7e', '直融业务登记', 'dfDirectFinanceInfo',
        '1', '/cms/dfDirectFinanceInfo/listApprove', 10, '/cms/dfDirectFinanceInfo/mobileApprove', null, 0, 1, null,
        null, 'sys:flow:approve:dfDirectFinanceInfo', null, null, null, null, 'bytter-service-finance', null);
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('fcd72c50bc79401b8909a891507c6702', 'a48d5e38146443359ff9f9d456c317a0', '代发工资', 'netbankWage', '1',
        '/wage/niswage/listApprove', null, '/wage/niswage/mobileApprove', null, 0, 1,
        '7c3bd19d9ad947f2837543d977c1e7bd', null, 'sys:flow:approve:netbankWage', null, null, null, null,
        'bytter-service-payment', '代发工资制单');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ff58f0712e9842c0b93fe5363af04d1a', '564deccaf9dc4093b8a823bc89390c60', '下拨', 'netbankAllocationDown', '1',
        '/allocation/nisallocationreq/listApprove/11', 11, '/allocation/nisallocationreq/mobileApprove', '', 0, 1,
        '4028802e5c5d50ad015c5d8df5cb001a', 1, 'sys:flow:approve:netbankAllocationDown', null, '超级管理员', null,
        '2023-08-21 16:58:13', 'bytter-service-payment', '下拨');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('hyyy46f65aiu48feb62b96501dcbd527', 'uu1446f65aiu48feb62b96501dcbd527', '债券发行', 'cmsBondIssueInfo', '1',
        '/cms/bond/issue/listApprove', 1, null, null, 0, 1, 'uuii02121db54d95a72bbcbf9a980184', 1,
        'sys:flow:approve:cmsBondIssueInfo', null, null, null, null, 'bytter-service-finance', '债券发行');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('hyyy46f65aiu48feb62b96501dcpd501', 'uu1446f65aiu48feb62b96501dcbd527', '债券可转债登记', 'cmsBondTransfer',
        '1', '/cms/bond/transfer/listApprove', 1, null, null, 0, 1, 'f6daecfabfdb4a519dff990d09f1c80c', 1, null, null,
        null, null, null, 'bytter-service-finance', '可转债登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('hyyy46f65aiu48feb62b96501dcpd502', 'uu1446f65aiu48feb62b96501dcbd527', '债券可交债登记', 'cmsBondSwap', '1',
        '/cms/bond/swap/listApprove', 4, null, null, 0, 1, 'e6bd8577838a4c4ead33352362a2783e', 1, null, null, null,
        null, null, 'bytter-service-finance', '可交债登记');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('klyy46f65aiu48feb62b96501dcbd527', '63c3faf05c1c461788abf5ae9205a9ec', '理财续期', 'choProductRenewal', '1',
        '/chowmatistic/choproductRenewal/listApprove', 1, null, null, 0, 1, 'uuiiad121db54d95a72bbcbf9a980199', 1, null,
        null, null, null, null, 'bytter-service-increment', '理财续期');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('nnyy46f65aiu48feb62b96501dcbd527', '63c3faf05c1c461788abf5ae9205a9ec', '理财产品购买申请',
        'choProductBuyApply', '1', '/chowmatistic/choproductBuyApply/listApprove', 1, null, null, 0, 1,
        'lk551480b4df4292oyfe22bbd9fb6101', 1, null, null, null, null, null, 'bytter-service-increment',
        '理财产品购买申请');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('ooyy46f65aiu48feb62b96501dcpd501', 'uu1446f65aiu48feb62b96501dcbd527', '债券展期', 'cmsBondExtend', '1',
        '/cms/bond/extend/listApprove', 5, null, null, 0, 1, '869ddcbfb3bc47e0809ebf906c64d705', 1, null, null, null,
        null, null, 'bytter-service-finance', '债券展期');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('uu1446f65aiu48feb62b96501dcbd527', '02523ab286714d96823300edb953e2ea', '债劵', 'bond', '1', null, 13, null,
        null, 0, 1, '728685abdbdd43e7bc9854da544bd2a1', 1, null, null, null, null, null, null, '债券');
INSERT INTO act_extend_category (ID, PARENT_ID, NAME, CATE_KEY, TYPE, URL, SORT, MOBILE_URL, NOTIFICATION_ID,
                                 MUST_APPROVE, BATCH_REVOKE_ABLE, MENU_ID, MOBILE_AVE_ABLE, RESOURCE_CONTENT, CREATE_BY,
                                 UPDATE_BY, CREATE_DATE, UPDATE_DATE, SERVICE_ID, MENU_NAME)
VALUES ('uuyy46f65aiu48feb62b96501dcbd527', 'uu1446f65aiu48feb62b96501dcbd527', '债劵注册', 'cmsBondRegisterInfo', '1',
        '/cms/bond/register/listApprove', 1, null, null, 0, 1, 'uuii01121db54d95a72bbcbf9a980184', 1,
        'sys:flow:approve:cmsBondRegisterInfo', null, null, null, null, 'bytter-service-finance', '债券注册');

-- auto-generated definition
drop table if exists act_extend_delegation;
create table act_extend_delegation
(
    ID                varchar(32)  not null
        primary key,
    PRINCIPAL         varchar(32)  null comment '授权人',
    PROXY             varchar(32)  null comment '被授权人',
    STATUS            int(12)      null comment '1、暂存 2、待确认 3、打回 4、已确认 5、结束',
    START_TIME        datetime     null comment '开始时间',
    END_TIME          datetime     null comment '结束时间',
    CREATE_BY         varchar(32)  null,
    UPDATE_BY         varchar(32)  null,
    CREATE_DATE       datetime     null,
    UPDATE_DATE       datetime     null,
    DELEGATION_NO     varchar(20)  not null comment '序列号',
    PROXY_CORP_ID     varchar(32)  null,
    PRINCIPAL_CORP_ID varchar(32)  null comment '授权人单位ID',
    PROXY_NAME        varchar(100) null,
    PRINCIPAL_NAME    varchar(100) null
)
    charset = utf8mb4;

-- auto-generated definition
drop table if exists act_extend_delegation_rel;
create table act_extend_delegation_rel
(
    ID            varchar(32)  not null
        primary key,
    DELEGATION_ID varchar(32)  null comment 'BPM_DELEGATION表的id',
    CATE_ID       varchar(32)  null comment 'BPM_FLOW_CATEGORY表id',
    CREATE_DATE   datetime     null,
    UPDATE_DATE   datetime     null,
    CREATE_BY     varchar(32)  null,
    UPDATE_BY     varchar(32)  null,
    CATE_KEY      varchar(255) null
)
    charset = utf8mb4;

-- auto-generated definition
drop table if exists act_extend_model;
create table act_extend_model
(
    ID                 varchar(32)              not null comment 'ID'
        primary key,
    NAME               varchar(200)             null comment '名称',
    PRO_DEF_ID         varchar(64)              null comment '流程定义ID。ACT_RE_PROCDEF表ID',
    PRO_DEF_KEY        varchar(64)              null comment '流程的KEY',
    CATEGORY_ID        varchar(32)              null comment '流程分类ID',
    TENANT_ID          varchar(32)              null comment '租户ID',
    CORP_ID            varchar(32)              null comment '单位ID',
    DEPLOYMENT_ID      varchar(64)              null comment 'ACT_RE_DEPLOYMENT表的ID',
    REVOKE_ABLE        tinyint      default 0   null comment '0表示不允许撤回，1表示允许撤回',
    BATCH_REVOKE_ABLE  tinyint      default 0   null comment '是否支持批量撤回，1支持，0不支持',
    MUST_APPROVE       tinyint      default 1   null comment '必须挂审批：0不是，1是（默认）',
    MODEL_DATA         text                     null comment '流程模型数据',
    MOBILE_AVE_ABLE    tinyint      default 0   null comment '是否是移动审批：值为0时表示不是；值为1或空置表示是（默认）',
    STATUS             varchar(1)   default '1' null comment '状态:1启用（默认）；0停用；',
    MODEL_VERSION      int          default 1   null comment '模型版本',
    VERSION            int                      null comment '版本号，乐观锁',
    CREATE_BY          varchar(100) default ''  null comment '创建人',
    CREATE_DATE        datetime                 null comment '创建时间',
    UPDATE_BY          varchar(100) default ''  null comment '更新人',
    UPDATE_DATE        datetime                 null comment '更新时间',
    DEPLOYMENT_VERSION int                      null comment '部署版本',
    CORP_NAME          varchar(255)             null comment '单位名称',
    CORP_CODE          varchar(255)             null comment '单位编码'
)
    comment '流程模型表' collate = utf8_bin;

-- auto-generated definition
drop table if exists act_extend_msg_record;
create table act_extend_msg_record
(
    id           varchar(64)   not null
        primary key,
    business_key varchar(64)   null,
    status       int           null,
    msg          varchar(1000) null,
    ex_msg       text          null,
    cate_key     varchar(64)   null comment '流程分类',
    corp_id      varchar(32)   null,
    create_by    varchar(32)   null,
    update_by    varchar(32)   null,
    create_date  datetime      null,
    update_date  datetime      null,
    constraint act_extend_msg_record_pk2
        unique (business_key, corp_id, cate_key)
)
    charset = utf8mb4;

-- auto-generated definition
drop table if exists act_extend_option;
create table act_extend_option
(
    ID           varchar(32)       not null
        primary key,
    CATEGORY     varchar(32)       null comment '所属流程分类',
    NAME         varchar(20)       null comment '配置名称',
    FIELD_NAME   varchar(30)       null comment '流程条件字段名',
    EVENT        varchar(100)      null comment '任务节点事件',
    TYPE         decimal(1)        null comment '类型 1、流向。2、任务节点',
    SELECT_TYPE  varchar(2)        null,
    DICT_GROUP   varchar(200)      null,
    MSG_TPL      varchar(255)      null comment '手机端的模版消息',
    CREATE_TIME  datetime          null,
    UPDATE_TIME  datetime          null,
    VISIBLE      tinyint default 0 null,
    SORT         int               null,
    STABLE_FIELD tinyint default 0 null
)
    comment '流程模型条件配置表' collate = utf8_bin;

INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('00ef7a2ffdba43dcacc314f55cc563c7', 'b3c36c632e914e04a6606731c374201d', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('01999db064664bfd9343c35ce9ba59bb', '01f54b36e086469ead0929fd0cbd799e', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 4, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('01f887be0a884bb3aada76098cbff755', 'e6784eeb04e349db96fd4d57e8b98c99', '金额', 'payMoney', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('031652ed23cf443f918887efd585be4c', 'cb6fb2a450f642399a1ebf9eb5f830c2', '汇款单号', 'billNo', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0367c00c09d14103ba520a085374bab2', '0755d4b14a4b4cc787d57e2e4190310d', '合同编号', 'contractCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0395d2bde34244ee976b1de8ef6366c4', 'a7a19b14f9bb4e549a7acaca512aa64d', '借款单号', 'pliCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('039e81b8aec8448697ad6e02608a2c6f', '9820c5d4922b40b49fa9a7c34413aedc', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0423fb606c3249598390011fca9895f4', '2bf804483cdf43899eb9aba3bc766dbc', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('04a556e3f6764536b3c82fcf6eb468ce', 'a8fb56299c05459f9440c67ba83d9441', '下拨单号', 'allocatedCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('071471016ac64685aedd0905f438525e', '549a02d1c1f94997a6b794654a28f209', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('072a1eb3bc524e96aa040970d6bd2811', '280b77c8cc8d4b38bff92cd4ff50bf48', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('074ecf111d4746b0975295cfac8c83f2', '549a02d1c1f94997a6b794654a28f209', '交易号码', 'tranReqNo', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('082728f000104e4790a35557107e709a', '50e7d302bc5d464d848dfb360399d4e9', '保函变更单编号', 'guaraChangeNo', null,
        4, null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('08a189aa5a52403aa22fa899bf21c5b9', '01f54b36e086469ead0929fd0cbd799e', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0945b38860a64c6f8c11f74a311e1c6f', '44ef51bd98dc4349bbd5fca6efc8c449', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('09544a9925b94caf84799ee353a564bf', '0e935d5da42848d0b7311bfef1ed5902', '发放单号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('098df406a7ac458098b8683b7fb46d13', 'a2274e9e4c104640b7bb5dfeb37a8451', '是否对私付款', 'isForindividual', null,
        1, '2', 'sf_yn', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0a9ab300b675497f8e0ea501bf649344', 'efa7154e837346d89e2eb3bd5e07c1be', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0b60f58c220f4cb5a08caeb12653827f', '577f1f5ed83b4102aca5e3b69b9f24cb', '单据号', 'billNo', null, 5, null, null,
        null, null, null, 1, 0, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0bab65054a2f40379fdcbdba5df3f790', '7ee5cbffbe1249b5870c62a314858e5c', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0cc9f934027642adbb07225561be470e', 'b8e4dbd992014229b9b585d567c92013', '贷款金额', 'loanMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0cd829e2927b428989ba8cc28d8525f0', '04ff11afbbf84af288c987b227e5c48d', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0e8b26f63d6c4da29de7ebb691d256be', 'f1386571094a41b1a229097ed33ee0d3', '追加额度', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0eb87b12b73249cab8feb9308e337eb4', 'ff58f0712e9842c0b93fe5363af04d1a', '金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0f02d31c047947b8a037c166d8c1a719', '71db3a4a62ba4431ba7403892907b7a0', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('0f822515895347a6b049e94149d18e80', '549a02d1c1f94997a6b794654a28f209', '申请时间', 'createDate', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('109808616cd547feba4872a207f0e5ac', '5c57d5a6c08d473fbd2fe8e34b540359', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('10bafa26e4f14cd89847a1a493087181', '2dbc122eeb624450a7f9b6942e995285', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('117673c3bc2240c89772800dff12d2e7', 'e0e87eee9fde4c13a12406b8c1ece6ac', '授信录入编号', 'ratCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('11fa54de4ff04e918bbd0d822c7d9c15', '35986704f7a74c358d2c19cec3945f48', '申请单号', 'appNo', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1220a7369eed4e5293a401f56f9d3676', '4130d76e571f4468a22a8f3833bc100c', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('13430f3318ad4fab9f0712edb3bf554a', '311d9fbeb77a423c9607633840086bdc', '保函编号', 'guaranteeNo', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1356b06f297149f8b6a07e7d1806201a', '507191e6b06b4db7b0b42de46b251f68', '金额', 'plMoney', null, 1, '1', null,
        null, '2017-11-28 14:07:08', '2017-11-28 14:08:02', null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('13cea8172ca8429aaa627387151ac6e8', 'f2f181a3b6f74c129c6f2945985897e9', '业务单号', 'freezeNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('140af90a3fdf4858a14b0c7faa74ff33', 'da60fc7c612b4a2db4acb52e6a0a1be7', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('15086fdf46464c8691d496801b87b800', 'a28d81099a174e7cb3e9b77f3bd02b6e', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('15d09ebef23442dc8d47f288e5111ed8', '5b022edcc94e49c3a743473da89c19b7', '报销单号', 'expenseId', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1663effd4a3f44039f5beca5dd382271', '444b256d89e44b7cbca22493dcd83355', '', '', null, 3, null, null, null, null,
        null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1675710316845002754', 'da60fc7c612b4a2db4acb52e6a0a1be7', '创建人', 'userId', null, 1, '', null, null,
        '2023-07-03 11:37:27', null, 0, null, 0);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1675710316895334401', '00b92ec05fbb4c09a5abd0ef865db3d8', null, null, null, 3, null, null, null,
        '2023-07-03 11:37:27', '2023-07-03 14:53:48', 0, null, 0);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1675759896034926593', '00b92ec05fbb4c09a5abd0ef865db3d8', '更新人', 'uid', null, 1, '0', null, null,
        '2023-07-03 14:53:48', null, 0, null, 0);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1683360354180624386', '7f78d7bb4c2c4776936e5da602505b05', 'pledgeMoney', '金额', null, 1, '1', null, null,
        '2023-07-24 14:15:58', null, 0, null, 0);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1683360354193207297', '7f78d7bb4c2c4776936e5da602505b05', null, null, null, 3, null, null, null,
        '2023-07-24 14:15:58', null, 0, null, 0);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('16e60b4bb6d84595b12077392813a1ff', 'aa838fe922204b88907c0036bbd19e8f', '贷款金额', 'loanMoney', null, 1, '1',
        null, null, null, '2017-11-27 17:40:26', null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('17397b714bf44ba1b2801809cb81779d', '2376d8dbe6554f138ec91e1cd86d62c1', '资金池标识', 'poolSign', null, 1, '2',
        'poolSign', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('17fffb71a5f64378ae2d98f8023d9b12', 'a6eb7ca67fdc4856a729fa06bd7aa602', '金额', 'payMoney', null, 1, null, null,
        null, null, null, 1, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('18d424320bd84ec7bee290d6f6f36274', 'e7ea1556c8b1465e8f26a32c02c3fab2', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('191052f3e82a40d1b75cf93b3000db6a', '00b92ec05fbb4c09a5abd0ef865db3d8', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('197cd4a38cef4de4bf9e5f0c1dd04c5f', 'b776bcb16c674f6d9b753e19f25cdf3d', '单据号', 'lcNo', null, 5, null, null,
        null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('19e2c40230b148fa9ba49f08ddb354f6', 'f1386571094a41b1a229097ed33ee0d3', '授信追加编号', 'addCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1a7f376e4d20486f889e1c954067e600', 'c2f97cfe0b14469a850e3882b6674cc1', '交易号码', 'tranReqNo', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1bec0b1905614c89a75f1670b0e4a664', '4130d76e571f4468a22a8f3833bc100c', '申请时间', 'createDate', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1c2ae3058d7a4a60a1a4017795a34dc8', 'efa7154e837346d89e2eb3bd5e07c1be', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1c47861c648845c899caa031670a03c9', 'd8510df0dcb84c89b1a23609c2c0cd50', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1caf9c44b5e64e7b841ff0c558df4da7', 'adee2223531045d694a5a9294c8f4f6a', '申请单号', 'appNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1d3c03b4180b4fc89712bc936e19698b', '74904a7472a74b699701a15147ff679d', '发放编号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1e50f2f9b011429084626d676ec97db0', 'd5251be17dc443d29386c44637f39506', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('1f9cbb7aacfc4c429d744a049daa864c', '7b2ef8bf47f84994b127628eeba50245', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('2089f6abad5346359962c40bbd1f5971', '04ff11afbbf84af288c987b227e5c48d', '授信调整编号', 'adjustCode', null, 5,
        null, null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('20c748bf0c9e4f0aae619259b2f15aaf', 'ed9e50484ce24120aceebcb7e97777d3', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('21038ddadda74c5885440abaf4e534de', '00b37b9156e3453fb720034177ea5e99', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('21611eb86aa14fd397b0d675c46bf436', '5b022edcc94e49c3a743473da89c19b7', '金额', 'expenseMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('21d237fb8843439aab6a61480643db32', '00b92ec05fbb4c09a5abd0ef865db3d8', '追加编号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('227c8dc33cbd44afbef64114b808e9f7', '4321b97d112845df9cb86b9c83b0930b', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('2292932e5ce2441ea30b66ad95015dea', '472d765e95124b7082c82b2997f8cd21', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('234d2ceb318247888953799640feb17c', '5cc72fb3497e485e9f8ed8e3495bb840', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('236831cca89946f5aceeb1b7d3d7348d', 'efa7154e837346d89e2eb3bd5e07c1be', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('24451a5237504618b395b9c260c4162b', '6294848d837f42f0b812337d399d59bd', '调剂额度', 'reliefMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('245de21865e94e3ea4d793cfd8b369e7', 'b1bdb21f90ef4b4c9b877316d2191ffa', '金额', 'plMoney', null, 1, '1', null,
        null, '2017-11-28 14:06:48', '2017-11-28 14:07:57', null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('251a354e7b5a4a0785cbaaeb44b02f4e', '00b37b9156e3453fb720034177ea5e99', '账号', 'blnctrAcc', null, 1, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('2635eaa4847a4c128ff9cac176b54f99', 'a8fb56299c05459f9440c67ba83d9441', '下拨金额', 'allocationMoney', null, 1,
        '1', null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('28b3ba1602564569b52283ce05f90f6a', '03b86b51d30d4a479d9b6bca322ea02c', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('296ef0a730e04e7b8aeb70ed98cbf42d', '657b141180a24c378b3c9060ae7ff020', '展期单号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('29c89757da6445a78acb4978891ca980', '392233eee3b94e2d8df6310649020298', '占用编号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('29f8d5298bb0417593cfad0f5d0e38b0', '9f4f6ace443047aa94fa9622ddfa7b1e', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('2a22e099d58642e3be69d313c6e5f4c8', 'c32cb6c4353e402d991ef3a94736d691', '单据号', 'depositNo', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('2a7ea9386f2c483696ac02b4e4afe9b9', '8d2aebe8050c451aa809665c5ad77b95', '单据号', 'billNo', null, 5, null, null,
        null, null, null, 1, 0, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('2b3b0d21f4914201a1edf8d07627d7b7', 'a28d81099a174e7cb3e9b77f3bd02b6e', '申请时间', 'createDate', null, 5, null,
        null, null, null, null, 1, 4, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('2e230595dde34a42887ccd16ae4c376e', '50e7d302bc5d464d848dfb360399d4e9', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('2e817cc2651240b68f9bf7f41d884745', '425cf3488b7c47b9a5a4498a1f1ad5ef', '授信追加编号', 'addCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('2f616f1ea2fb4e3da430f12e54c16815', 'e6784eeb04e349db96fd4d57e8b98c99', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('2ff5b3edb39b4146848b38ffc6fc43a9', '7a0b4aa8f7dd4c2cba660612d315b5a8', '授信分配编号', 'disCode', null, 5,
        null, null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('30f84351252b4ae0bfae766f8ceda16b', '423e1435b1a842d792e546ed76fa235c', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('31cf32b48734488db571a3a3b1c5eba0', 'f33256068909476f973250e5b7016f98', '金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('32399e034cb34a51845ed3b1a4b0ac9f', 'c1b3fbd899da4765bf13fc80b5af2e73', '单据号', 'delayNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('35df1e33b82b49f6acb0658022e611d7', 'c32cb6c4353e402d991ef3a94736d691', '单据号', 'depositNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('35ec1d23ffbb4e9996b6d81d823ace47', '4a04c5f37d2d4d599ab27da832a8b89c', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3637503dbee6425389635c21d3bb7d1e', '180392ca4988425591faa261b0140327', '金额', 'payMoney', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('364268b448044fa19c7c39c6707dce30', 'c2f97cfe0b14469a850e3882b6674cc1', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3789a659d0fb4af3bfb01638ee97aeda', '9f4f6ace443047aa94fa9622ddfa7b1e', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('37be46dbc9d44fc8a04780dc8022f7b7', 'cbb14ee501a04c8682f6f0a2309ad796', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('37e00c302329485c88919508626477f2', '8d2aebe8050c451aa809665c5ad77b95', '金额', 'payMoney', null, 1, '1', null,
        null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('381be93e7138452083d9f68834dff0a4', 'a28d81099a174e7cb3e9b77f3bd02b6e', '收付方向', 'cdSignName', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('387e900096ed48debdf73787cbd3f3ef', '346cd67c18dc473b9bdd854e1fc9a3a8', '金额', 'payMoney', null, 1, null, null,
        null, null, null, 1, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('38b1f2a81d4348c2a1c857eb1d6da97b', '5cc72fb3497e485e9f8ed8e3495bb840', '贷款编号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3a0703c6e01243ac9315280c9d89d09b', '577f1f5ed83b4102aca5e3b69b9f24cb', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 4, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3a5102b0d5e042269d607645ec8b902f', '20b34d17ccfa4cc4845119e2f1c2e2d9', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3b616e6e23724a7fae57aabc2fc2a6b0', '518a8d98df1545418aef4d33d82f24c8', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3c595647f25d4392beb1a23228de0620', '4a04c5f37d2d4d599ab27da832a8b89c', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3d4ac10cb60b4f7a86c58d646f2d4218', '7f78d7bb4c2c4776936e5da602505b05', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3da2407816084f4eb0464f8836199a6e', 'b776bcb16c674f6d9b753e19f25cdf3d', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3f046b0a2cb643e3b9ffbff5b59d5f5b', 'c1b3fbd899da4765bf13fc80b5af2e73', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3f322cfb08d44ea1be6ead3c1a60d8c7', '03b86b51d30d4a479d9b6bca322ea02c', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('3fb742304df24898afe619ed857764c5', 'd98b4de7f5004892a5b3e627f4081029', '金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4043f22b8ca441f18f109d32c92303af', '01f54b36e086469ead0929fd0cbd799e', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('407184158c73494e8ceed92a09e77206', 'd3ed227372b646ac98f37155989beb6d', '授信录入编号', 'ratCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4145258106594bf3a3b614dc949872d8', '44ef51bd98dc4349bbd5fca6efc8c449', '授信占用申请编号', 'applyCode', null,
        5, null, null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('415dd203242a4402ad404e89a95da0be', '5a532df93e75494ca62a222ac68d21f3', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('432b73e56c914ea1840c9614b7b65a21', 'c72976eab3354f2ba0af5fac6835cf24', '金额', 'freezeMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('43980d41ff8c422bbdff59d280919897', '8d2aebe8050c451aa809665c5ad77b95', '源账户单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('43e983006d144db18a2865793ea1987a', '7ee5cbffbe1249b5870c62a314858e5c', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('452d722ba99442b380492cc134597d3b', '44ef51bd98dc4349bbd5fca6efc8c449', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('45fa72d70d544dabadf8b6df6e056f0d', '01f54b36e086469ead0929fd0cbd799e', '单据号', 'billNo', null, 5, null, null,
        null, null, null, 1, 0, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('46335680c51a4182b8c1db4f91d9236c', 'bf2c3f92a5fd49708780a23ceb6260f3', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('46908e744db44460a46f0587b12f35a1', 'b1bdb21f90ef4b4c9b877316d2191ffa', '借款单号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('477a52f562254f48a361e435fd38314e', 'be1d5acb796646159572a12ac8203529', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('47dcc5988b1c4fd8bd8f48911708c084', '8d2aebe8050c451aa809665c5ad77b95', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('480b1fdeec604171b4712bbe8faa0410', '74904a7472a74b699701a15147ff679d', '发放金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('48afd71b459e48149e157726d88cad45', '338667e2f16f4328942215ba86e3cd5c', '申请单号', 'appNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4940efa669944074b66b3088b3c1cd1e', '5282f78cbb5c466e84e0f6766cb12f42', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('49d6f66063f54b64b77f9ccab29d7a0a', '7a0b4aa8f7dd4c2cba660612d315b5a8', '授信分配编号', 'disCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4ab209251b554db29a7bb857d219226f', '8d2aebe8050c451aa809665c5ad77b95', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 5, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4ba21fd6b22b49bc96f7855409ac6913', '7f78d7bb4c2c4776936e5da602505b05', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4bb7408c203d49babb163e9e6a9c95d9', '8d2aebe8050c451aa809665c5ad77b95', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4bcfa9c60db04113a17ef2b7d8d9588b', '21fb4abf996f43668771dda1af1bd80e', '还款单号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4d28e0fb94364f3b821c959df859ecf3', '03b86b51d30d4a479d9b6bca322ea02c', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0001', '280b77c8cc8d4b38bff92cd4ff50bf48', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0002', '280b77c8cc8d4b38bff92cd4ff50bf48', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0003', '280b77c8cc8d4b38bff92cd4ff50bf48', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0004', 'cbb14ee501a04c8682f6f0a2309ad796', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0005', 'cbb14ee501a04c8682f6f0a2309ad796', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0006', 'cbb14ee501a04c8682f6f0a2309ad796', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0007', 'd5251be17dc443d29386c44637f39506', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0008', 'd5251be17dc443d29386c44637f39506', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0009', 'd5251be17dc443d29386c44637f39506', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0010', 'cb59b145e7fe4b9fa16caa841f978580', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0011', 'cb59b145e7fe4b9fa16caa841f978580', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0012', 'cb59b145e7fe4b9fa16caa841f978580', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0013', '518a8d98df1545418aef4d33d82f24c8', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0014', '518a8d98df1545418aef4d33d82f24c8', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0015', '518a8d98df1545418aef4d33d82f24c8', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0016', '2dbc122eeb624450a7f9b6942e995285', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0017', '2dbc122eeb624450a7f9b6942e995285', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0018', '2dbc122eeb624450a7f9b6942e995285', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0019', 'd8510df0dcb84c89b1a23609c2c0cd50', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0020', 'd8510df0dcb84c89b1a23609c2c0cd50', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0021', 'd8510df0dcb84c89b1a23609c2c0cd50', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0022', '4321b97d112845df9cb86b9c83b0930b', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0023', '4321b97d112845df9cb86b9c83b0930b', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0024', '4321b97d112845df9cb86b9c83b0930b', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0025', '55bd11aca63f46be9cf7bb7c9359f6b8', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0026', '55bd11aca63f46be9cf7bb7c9359f6b8', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0027', '55bd11aca63f46be9cf7bb7c9359f6b8', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0028', 'cf8205c57e1c485fa6862e1b4930ec16', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0029', 'cf8205c57e1c485fa6862e1b4930ec16', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0030', 'cf8205c57e1c485fa6862e1b4930ec16', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0031', 'f753b4258f7843c49a2597894cb42d43', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0032', 'f753b4258f7843c49a2597894cb42d43', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0033', 'f753b4258f7843c49a2597894cb42d43', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0034', '3ef95c31f30345d59ec3b03f1cfb143e', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0035', '3ef95c31f30345d59ec3b03f1cfb143e', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0036', '3ef95c31f30345d59ec3b03f1cfb143e', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0037', 'e7ea1556c8b1465e8f26a32c02c3fab2', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0038', 'e7ea1556c8b1465e8f26a32c02c3fab2', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0039', 'e7ea1556c8b1465e8f26a32c02c3fab2', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0040', '52b7ab1596ed44378075db443c237234', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0041', '52b7ab1596ed44378075db443c237234', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0042', '52b7ab1596ed44378075db443c237234', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0043', 'd749db63c5e949aeac7ebb99b528c1af', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0044', 'd749db63c5e949aeac7ebb99b528c1af', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0045', 'd749db63c5e949aeac7ebb99b528c1af', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0046', '3a3237a2d1974e1d9017372a5191b260', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0047', '3a3237a2d1974e1d9017372a5191b260', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0048', '3a3237a2d1974e1d9017372a5191b260', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0049', 'c242b006846c4fe4851ff9195f4c3413', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0050', 'c242b006846c4fe4851ff9195f4c3413', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0051', 'c242b006846c4fe4851ff9195f4c3413', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0053', '338667e2f16f4328942215ba86e3cd5c', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0055', 'adee2223531045d694a5a9294c8f4f6a', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0056', 'adee2223531045d694a5a9294c8f4f6a', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0057', 'adee2223531045d694a5a9294c8f4f6a', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0058', '5ac49b78e6344b0eaeec36806ae13952', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0059', '5ac49b78e6344b0eaeec36806ae13952', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0060', '5ac49b78e6344b0eaeec36806ae13952', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0061', '5a532df93e75494ca62a222ac68d21f3', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0062', '5a532df93e75494ca62a222ac68d21f3', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0063', '5a532df93e75494ca62a222ac68d21f3', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0064', 'dcea5aa6b0e94c8590aea1b6a62519e2', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0065', 'dcea5aa6b0e94c8590aea1b6a62519e2', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0066', 'dcea5aa6b0e94c8590aea1b6a62519e2', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0067', '74ef48866d8a4526b3043a974c089720', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0068', '74ef48866d8a4526b3043a974c089720', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0069', '74ef48866d8a4526b3043a974c089720', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0070', '6b0d5429d0fb4d8a8b891d9a801bc387', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0071', '6b0d5429d0fb4d8a8b891d9a801bc387', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0072', '6b0d5429d0fb4d8a8b891d9a801bc387', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0073', '0a0eb625b2f94523ae7816f5e151c028', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0074', '0a0eb625b2f94523ae7816f5e151c028', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0075', '0a0eb625b2f94523ae7816f5e151c028', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0076', '20b34d17ccfa4cc4845119e2f1c2e2d9', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0077', '20b34d17ccfa4cc4845119e2f1c2e2d9', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0078', '20b34d17ccfa4cc4845119e2f1c2e2d9', '撤回类型', 'recallType', null, 1, '2',
        'AB_RECALL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0079', '20b34d17ccfa4cc4845119e2f1c2e2d9', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0080', 'db4cf73f2c224352a6b40b9aafe5ac35', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0081', 'db4cf73f2c224352a6b40b9aafe5ac35', '追索金额', 'rcrsokmoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0082', 'db4cf73f2c224352a6b40b9aafe5ac35', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0083', 'db4cf73f2c224352a6b40b9aafe5ac35', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0084', 'db4cf73f2c224352a6b40b9aafe5ac35', '追索类型', 'rcrsTp', null, 1, '2',
        'AB_RCRS_TP', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0085', 'db4cf73f2c224352a6b40b9aafe5ac35', '追索理由', 'rcrsrsncd', null, 1, '2',
        'AB_RCRSRSNCD', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0086', '7fb7943b906c417aac89839e5c3b5672', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0087', '7fb7943b906c417aac89839e5c3b5672', '同意清偿金额', 'rcrsokmoney', null, 1,
        '1', null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0088', '7fb7943b906c417aac89839e5c3b5672', '流程介质', 'billFlag', null, 1, '2',
        'AB_BILL_FLAG', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0089', '7fb7943b906c417aac89839e5c3b5672', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0090', '7fb7943b906c417aac89839e5c3b5672', '追索类型', 'rcrsTp', null, 1, '2',
        'AB_RCRS_TP', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb0091', '7fb7943b906c417aac89839e5c3b5672', '追索理由', 'rcrsrsncd', null, 1, '2',
        'AB_RCRSRSNCD', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4e6caec61eaf48bfb665798a95eb6023', '338667e2f16f4328942215ba86e3cd5c', '票面金额', 'billMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('4f52e052a97b4230aa4f381b860e765a', '88b2d7c9cde2452b96507303b912708e', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('505222dc5a01449aaf94a5ef4fe15b5c', '444b256d89e44b7cbca22493dcd83355', '交易对手类别', 'cotpTypeName', null, 5,
        null, null, null, null, null, 1, 2, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('50fe1b2c36fa4b7fa6b045375a640e3a', '7ee5cbffbe1249b5870c62a314858e5c', '单据号', 'mortgageNo', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('513e4a629a7743d6b6251af80803f555', '27eeedb803004cd693c60df107453f6e', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('51c30f21c82e442b91acd1dcee3fa0bc', 'bf2c3f92a5fd49708780a23ceb6260f3', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('51edc376a18d4cfea01783fb9590f923', 'aefcc674090c4a579043a0b292c96557', '金额', 'payMoney', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('523188753f7d4c6492c02af0be06ac92', '011e6146b184417c8008288029778bde', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('533c06e1041c45c6b28005c3daef9cc8', 'b918087da24041b8ab99d35932d16eb6', '发放方式', 'loanSendOutType', null, 1,
        '2', 'cms.loan.loanSendOutType', null, '2017-11-27 17:42:42', null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('561e69037900430384ba70d96048fc19', '5c57d5a6c08d473fbd2fe8e34b540359', '还款编号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('57a5119bd15a4027a76b75eb4d37d7c5', '0a0eb625b2f94523ae7816f5e151c028', '签收业务类型', 'msgtype', null, 1, '2',
        'AB_BILL_SIGNERTYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('581b0f3f9eeb43699f6c7c5f673e22cc', '5cc72fb3497e485e9f8ed8e3495bb840', '贷款金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('58e7cad5fbe5485daaa6d1abf45c6f2b', '1126c57850234bf1906fa4046b9b8bce', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5927472d775140d98dee9328cc9f92b6', '04ff11afbbf84af288c987b227e5c48d', '授信调整编号', 'adjustCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('59c84f91914d41a09555027a80cea7fb', '0755d4b14a4b4cc787d57e2e4190310d', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5a661cf096ee45b7b2e5a63c02babd4c', '011e6146b184417c8008288029778bde', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5abfe42d9ce7412192147b87004ef8fa', 'd38b7af945d74c5e9ae0778f987d2537', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5ad4a3457e354e9d907a4bf26467468c', 'fc06a82de5314f04b5a1899bc93c26b9', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5aebe34e5397482c8f32ae32206216f4', 'bf2c3f92a5fd49708780a23ceb6260f3', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 5, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5af0fcead089413988e6dd1a886974ff', 'ca702e2a4efa4a039d4b0cd34edb0691', '业务单号', 'accNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5b771122fb42421dbe5c5275bf54cf42', '35986704f7a74c358d2c19cec3945f48', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5c1a4f80b7154dffaf52bfd585f15725', '9820c5d4922b40b49fa9a7c34413aedc', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5c963fbd4a854eadb40f393543353454', '77b5108283b44b4d9dae86cdb743b42b', '贷款金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5cc65c31f87e461ea7e1f393f4043a03', 'fc06a82de5314f04b5a1899bc93c26b9', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5d25080a726c41c1afa54bfacb9a661c', 'b8e4dbd992014229b9b585d567c92013', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5d3d74e0da6347d0924ea85aa2a78b4c', '00b37b9156e3453fb720034177ea5e99', '单据号', 'billNo', null, 4, null, null,
        null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5e04dacdeb55417ea46502f1ec5c9203', '740a5e051522453eac8ff2796e7c2123', '展期单号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5e0eaa755eb3411e8cc86fcab7a01173', 'ae3f0b82edaa4d8b9b2b61b8f272adf0', '金额', 'repayMoney', null, 1, '1',
        null, null, '2017-11-28 14:08:21', null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5e15adf0b45b405b9cd4edcffcc26773', '5c57d5a6c08d473fbd2fe8e34b540359', '还款金额', 'repayMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('5e4e0ffe969c43f39a26efefd4177c89', '392233eee3b94e2d8df6310649020298', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('60aa3bd30b2e49bfbdc7bfaf6131b222', '01f54b36e086469ead0929fd0cbd799e', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('61116ce830b746e5b2aa0833457dff63', 'aa838fe922204b88907c0036bbd19e8f', null, null, null, 3, null, null, null,
        null, '2017-11-27 17:40:26', null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6170b67185824a74baf666e099d9907c', '88b2d7c9cde2452b96507303b912708e', '授信追加编号', 'addCode', null, 5,
        null, null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('618477e24b634b7593cc579e1f20d4a4', '577f1f5ed83b4102aca5e3b69b9f24cb', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('622cd699ab634eff9e842a7abf772fab', 'a28d81099a174e7cb3e9b77f3bd02b6e', '现金流/净额结算号码', 'billNo', null,
        5, null, null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('628ce026d8f14ab7b74f6c93f50f7ef7', 'cf8205c57e1c485fa6862e1b4930ec16', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('62e16c1ad8ac425db3a9b76eeff7ef2f', 'ae3f0b82edaa4d8b9b2b61b8f272adf0', '是否提前还款', 'isAdvancedRepay', null,
        1, '2', 'cms.repay.isAdvancedRepay', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('63205d3127214f89bc5013f47f037921', '71db3a4a62ba4431ba7403892907b7a0', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('64ee752835ab4d28b963f3fbe0e870e5', '27eeedb803004cd693c60df107453f6e', '还款申请编号', 'billCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('657cfc8099ed4c85952d4423ae117d44', '71db3a4a62ba4431ba7403892907b7a0', '票据号', 'origNo', null, 5, null, null,
        null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6584479f53264f37ba5cd9f99d8abd0a', '1a155a9dacc94b02bdf9038483a136fc', '业务单号', 'accNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('65a942c41837408fbf1f8d2c93e373d8', '8105e4ce58b64f1cb6d3cd6ffd0c22f2', '退款申请号', 'backNo', null, 5, null,
        null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6a6cd94d5ba2475fb4b8b8e5ab5a8262', '6294848d837f42f0b812337d399d59bd', '授信调剂编号', 'refCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6aea143d466c4b2990c32d1d33147d6e', '311d9fbeb77a423c9607633840086bdc', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6b6cc882cf7e405ba68f1d6190933b48', '03b86b51d30d4a479d9b6bca322ea02c', '账号', 'blnctrAcc', null, 1, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6b7242deedf045049de39209e1fd48c1', 'a2274e9e4c104640b7bb5dfeb37a8451', '付款单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6c517e76675c450ba665892c7befabf2', '55bd11aca63f46be9cf7bb7c9359f6b8', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6d82ca7894294491a47c3453e65927c7', '577f1f5ed83b4102aca5e3b69b9f24cb', '币种', 'curId', null, 1, null, null,
        null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6db96b6db56d46ef9b7ffca587860448', '04ff11afbbf84af288c987b227e5c48d', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6de6dd69b7b2464a91aae8a0afb536ce', '8d2aebe8050c451aa809665c5ad77b95', '目标账户单位', 'targetCorpName', null,
        5, null, null, null, null, null, 1, 2, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6df001a926064d768010561653c8efbc', 'd3ed227372b646ac98f37155989beb6d', '授信额度', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6e3756c851d04415b6d5e882e3c41196', '77b5108283b44b4d9dae86cdb743b42b', '贷款编号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6ec6b58b1b8648419a8db20ca89e3443', 'f2f181a3b6f74c129c6f2945985897e9', '金额', 'freezeMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6f367af411cf44e9825cb7d884139933', '44ef51bd98dc4349bbd5fca6efc8c449', '授信占用申请编号', 'applyCode', null,
        4, null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6f404dc15dc74f6d8da4fd08a084de5f', '88b2d7c9cde2452b96507303b912708e', '授信追加编号', 'addCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6f864d6330ba4196adb5898224c90043', 'faa87b4b3d1a45cd9651c586130dea2b', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6f8aa3c044a94547adea7a808fe2adcf', 'b1bdb21f90ef4b4c9b877316d2191ffa', null, null, null, 3, null, null, null,
        '2017-11-28 14:06:48', '2017-11-28 14:07:57', null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('6fc6b9a6f73040a6b79922f0f9a409e4', '577f1f5ed83b4102aca5e3b69b9f24cb', '收付方向', 'cdSignName', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('703ef08030eb43f3a076a231cf704f26', 'aefcc674090c4a579043a0b292c96557', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('709ca8197df04d20b3688b9a1e8002fc', '507191e6b06b4db7b0b42de46b251f68', '借款单号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('711bf43725fa470290f39cfa0db8208c', '91b433468a274226b1428b94d81b278f', '单据号', 'billNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('72eb2ec046cc4d3bb2d851cdc792989d', '01f54b36e086469ead0929fd0cbd799e', '金额', 'sellMoney', null, 1, '1', null,
        null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('72f9fc1b199b48fbbefb71b166146ff3', '03b86b51d30d4a479d9b6bca322ea02c', '单据号', 'billNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('73143df9b59c48a99c549a1acb80d6e4', '67182fc663fa4b6c97c1bb8042a00848', '业务单号', 'accNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('74e646ca4cd04023bd20bf8643b7b0da', 'd749db63c5e949aeac7ebb99b528c1af', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('74fe3850e50e432c86fafa276ae485bd', '00b37b9156e3453fb720034177ea5e99', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('752c2a2433e54ceea3f1fb33142d5300', '444b256d89e44b7cbca22493dcd83355', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('75e2041cb65e44368a98b104c8427887', 'fc06a82de5314f04b5a1899bc93c26b9', '单据号', 'billCode', null, 5, null,
        null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('75e7b61345d6490bb3b62ee421f27cd1', '392233eee3b94e2d8df6310649020298', '项目单号', 'billNo', null, 5, null,
        null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('76860d88cc1e40249af0c0c956eb4186', '8d2aebe8050c451aa809665c5ad77b95', '币种', 'curId', null, 1, null, null,
        null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('76984957c97a4e2a819145ee04c56ef5', '577f1f5ed83b4102aca5e3b69b9f24cb', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('76a017796fb749818bb21a6856e275cf', '9c27fe2cd74c4985ae38d50249b334d9', '分配金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('76a5d7f03e9c49eeb99ac77f396bb421', 'da60fc7c612b4a2db4acb52e6a0a1be7', '存出编号', 'billNo', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('76b7f24c4bc64658b33562779bd6cd24', 'd38b7af945d74c5e9ae0778f987d2537', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('77dce947e0e342068e6d6a8f054712be', 'ff58f0712e9842c0b93fe5363af04d1a', '资金池标识', 'poolSign', null, 1, '2',
        'poolSign', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('797ffcf3c0dd4331837ae100c789c997', '3311541b02ce402482877c17c40b8818', '展期单号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('7b9e0c59f2854d449a91249d40b6f7c7', '011e6146b184417c8008288029778bde', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('7d6c49586ae947d6b3fb97b731d2a150', 'faa87b4b3d1a45cd9651c586130dea2b', '单据号', 'lcNo', null, 4, null, null,
        null, null, null, null, null, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('7dba0e8ebdc7490a9d6a7372b3dd8c5e', '7ee5cbffbe1249b5870c62a314858e5c', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('7dc05fa831db4e63a768cf51eae35980', '245fd91ce6234c78aadbd361cef5e2ba', '金额', 'payMoney', null, 1, null, null,
        null, null, null, 1, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('7dfb72604c4a4b3eb1b88b1f8683e6c7', 'c242b006846c4fe4851ff9195f4c3413', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('7ec728c83ec14e6bbea2f11f99201a40', 'c72976eab3354f2ba0af5fac6835cf24', '业务单号', 'freezeNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('812e252e1aac406da316db21b7ae7609', '00b37b9156e3453fb720034177ea5e99', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('813a286eac4a422d9e3a7b45570f76e6', 'bf2c3f92a5fd49708780a23ceb6260f3', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 6, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('818b362997614fcaabf603669612aaf6', '472d765e95124b7082c82b2997f8cd21', '单据号', 'billCode', null, 5, null,
        null, null, null, null, 1, 1, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('834d1b5d2a4c44f6a5ae2ea4577f9ddb', '472d765e95124b7082c82b2997f8cd21', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 4, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('8480e91ffe02497c97da8e9acc93a0c1', '584e50ae3a9840f4b8c34e1629852256', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('85411baef1704a4d8096e02b3a02880f', '584e50ae3a9840f4b8c34e1629852256', '占用金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('877d58530ab146189eb50948e8bda502', '6b0d5429d0fb4d8a8b891d9a801bc387', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('87cf1bb5732442a4ba0c9b3ecf6f0b18', 'db4cf73f2c224352a6b40b9aafe5ac35', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('8905ea6e664145389822b1835e17cd19', '5c57d5a6c08d473fbd2fe8e34b540359', '还款方式', 'repayType', null, 1, '2',
        'cms.repayType', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('898df2ef12cf4860bb5374377ae27089', '7a0b4aa8f7dd4c2cba660612d315b5a8', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('89e048ade58d4541a9403662aca9fe15', 'c1b3fbd899da4765bf13fc80b5af2e73', '单据号', 'delayNo', null, 5, null,
        null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('8d1b1d36b81d412196ac0fb1316a1822', '9d683b60ab5a4150b7b030b82bb927d2', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('8d365b9921e742dcb8a4654c4da25a51', 'c1b3fbd899da4765bf13fc80b5af2e73', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('8f6268ca91d545ba818be19441b2893d', '0e4b113506634ed2a8de33103098adf9', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('8f7de5b963144a4785cc5aab55141563', '54ef2d64d84042f68a6e58e4013374bc', '支付类型', 'payType', null, 1, '2',
        'nisPersonnelDetail.cardType', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('90330aaf954a478c99f3a4f62e103a02', '27eeedb803004cd693c60df107453f6e', '还款类型', 'repayType', null, 1, '2',
        'cms.repayType', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('91b0a4bdbd1b412095944929e3d15963', 'b918087da24041b8ab99d35932d16eb6', '发放申请编号', 'billCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('91bcae9705c24ddc87312e8320c5ae47', '2bf804483cdf43899eb9aba3bc766dbc', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('938ff6990dbc487085512e01b85ee456', '88b2d7c9cde2452b96507303b912708e', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('94c3f9e64e49436a8667865ad8cd2128', 'c342a4d791d34d668304801f2509458f', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('95f764c5484849acb7c8fb02f13cd023', 'd38b7af945d74c5e9ae0778f987d2537', '单据号', 'billNo', null, 5, null, null,
        null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9650b4b5193c4ebf8facc36d9cf92980', '21fb4abf996f43668771dda1af1bd80e', null, null, null, 3, null, null, null,
        '2017-11-28 14:07:22', '2017-11-28 14:08:34', null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9658ad4dc211414f89a0cf6eb8ac1349', '67182fc663fa4b6c97c1bb8042a00848', '金额', 'bal', null, 1, '1', null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9799b189717d46248948e59a8404fcc2', '7a0b4aa8f7dd4c2cba660612d315b5a8', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('981fc3ad64654b77b88ffa569d9ec580', 'efa7154e837346d89e2eb3bd5e07c1be', '授信调剂编号', 'refCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('983615c4db1f409280cc2b5c59b2d9ea', '8e044000d6594fe3a6718f528b3c846c', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('98ae54109af64f6e80d4a869619a8cc3', 'd38b7af945d74c5e9ae0778f987d2537', '币种', 'sellCurId', null, 1, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('98ed0e05f8d64dfd903aa72c36b13ca5', '7f78d7bb4c2c4776936e5da602505b05', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('99172c5722d34dd197995dcf2618ef4f', '8e044000d6594fe3a6718f528b3c846c', '单据号', 'paymentNo', null, 5, null,
        null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9a4221c0ad9b415eb62acccf658de40b', '507191e6b06b4db7b0b42de46b251f68', null, null, null, 3, null, null, null,
        '2017-11-28 14:07:08', '2017-11-28 14:08:02', null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9b1f2cabeaff4d8a813a4072b274587d', '311d9fbeb77a423c9607633840086bdc', '保函编号', 'guaranteeNo', null, 5,
        null, null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9c453fa3cc7e447a817043c1fded0e22', '1db5a132b93544fb91c7f1443dd5efe5', '金额', 'amt', null, 1, '1', null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9c60ec4da23842b3aacb5aee39598f3d', '549a02d1c1f94997a6b794654a28f209', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9c9a450eadfb47b58adac8119f8ab024', 'ae3f0b82edaa4d8b9b2b61b8f272adf0', '还款单号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9dddf9d46cf542fe8310073875b226dc', 'f2ff515786194606b94e47d605408410', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9ea727de25f045028a5562cb99be7bca', '00b92ec05fbb4c09a5abd0ef865db3d8', '追加编号', 'billNo', null, 5, null,
        null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('9ef6644a25db424f8cdf8bf3b5b6e1d4', '04ff11afbbf84af288c987b227e5c48d', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a0d7cbda99744e66b7debc328a99a130', '50e7d302bc5d464d848dfb360399d4e9', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a17d774b0bd945b197eadb4d4832a8bb', '77b5108283b44b4d9dae86cdb743b42b', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a1c6b596aa8c4464a16bd6305911b939', '9f4f6ace443047aa94fa9622ddfa7b1e', '单据号', 'billNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a1d4aa5744e143d885b7b760f8261d20', 'be1d5acb796646159572a12ac8203529', '金额', 'processMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a1e504353379417c8ba6497d133f3b07', 'dcea5aa6b0e94c8590aea1b6a62519e2', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a29b8ba802be44b38e1142b30fea12ca', 'cb59b145e7fe4b9fa16caa841f978580', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a2a8a3252d684a029a51acb0ca24fc23', '01f54b36e086469ead0929fd0cbd799e', '币种', 'sellCurId', null, 1, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a2d341060aae46b2a5f3b4b93d3c1b89', '392233eee3b94e2d8df6310649020298', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a2e41c8f3655415d8d2e7e2c34660fbd', '1aaff46ff5b04261999376950f1b57db', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a335b3597dd5452cb3576fda5ef33d80', 'a2274e9e4c104640b7bb5dfeb37a8451', '是否在线', 'isOnline', null, 1, '2',
        'sf_yn', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a3ff40af416d4805a2000cfbc26cc7fe', 'da60fc7c612b4a2db4acb52e6a0a1be7', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a542a284340c4ecc9e1c510e8e140aa7', '1126c57850234bf1906fa4046b9b8bce', '单据号', 'guaNo', null, 5, null, null,
        null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a6eb7ca67fdc4856a729fa06bd7aa602', 'a6eb7ca67fdc4856a729fa06bd7aa602', '单据号', 'billNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a739ceee670745228ca580f26dc43a0f', '2c1b1c7bccf74935852fbdd47c69c5e4', '单据号', 'arrivalCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a77a1d2f9a6d4acabb5207e2f7fa9dd2', '577f1f5ed83b4102aca5e3b69b9f24cb', '金额', 'payMoney', null, 1, '1', null,
        null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a7b8e7d7fa0742d9b5b679b270657ddb', '2bf804483cdf43899eb9aba3bc766dbc', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a8291c3485df43328226edf2449242b0', '180392ca4988425591faa261b0140327', '指令编号', 'voucherNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a891cc16f2b14215bd4c359cf2088779', 'aa838fe922204b88907c0036bbd19e8f', '贷款申请编号', 'billCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a8f1b3fdc7b5442fad279cbc94957f23', '248ff1e5717e4f6a98a84d2eaea2149f', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('a9afb9e294ab43ada9f3e1f009c7bde5', '577f1f5ed83b4102aca5e3b69b9f24cb', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('aa1f678f479c484b947b9d33bd2beabf', '91b433468a274226b1428b94d81b278f', '金额', 'payMoney', null, 1, null, null,
        null, null, null, 1, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('aa98d2c1781446538cc461e200de7d2e', '78ece80ab771466f9454761764cfe96e', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('aad8439c09464fb3be426ca298982e1a', '4a04c5f37d2d4d599ab27da832a8b89c', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 2, 4, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('aca0707ef3ef4ea38c62319699109af5', '7fb7943b906c417aac89839e5c3b5672', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ad289933ca3b408fb62513dd22f4da24', 'faa87b4b3d1a45cd9651c586130dea2b', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ae3d5ff3394d4e2dbc01b295568939d4', '0c3e9cc35c24403f9f9c465a60505c18', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ae612df9c56d4e0c923022fe2e481b95', 'faa87b4b3d1a45cd9651c586130dea2b', '单据号', 'lcNo', null, 5, null, null,
        null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('aef8da7ac704413f892d6722d4f4a179', '584e50ae3a9840f4b8c34e1629852256', '授信占用申请编号', 'applyCode', null,
        4, null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('aff67b4628f24294b50354a633e108f6', 'fc06a82de5314f04b5a1899bc93c26b9', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b06b4706e4854df7b6442c344675e6b6', 'd3ed227372b646ac98f37155989beb6d', '授信模式', 'ratMode', null, 1, '2',
        'RatBkration.RatMode', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b1106834a1aa4187893024990a245f1c', '3ef95c31f30345d59ec3b03f1cfb143e', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b134d39e0e3f4ff1aad9831e306abba6', '2c1b1c7bccf74935852fbdd47c69c5e4', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b1d6227b968b4fc69e1d957346057715', 'ff58f0712e9842c0b93fe5363af04d1a', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b25357a586fc4343988de9930c4a9b6e', 'd4be7deefb524e5492a20054cf62c582', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b2d774c78ab44f73b22fba8e0bf8816b', 'bf2c3f92a5fd49708780a23ceb6260f3', '交易对手名称', 'cotpName', null, 5,
        null, null, null, null, null, 1, 1, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b3a8531dd6874520bb5ba8ac9507ba09', 'fcd72c50bc79401b8909a891507c6702', '工资单号', 'wageNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b4538b778204413bad80d0ec50a4181b', 'bc3aa2d3b6684ca38e2c203bb1ab9bd2', '金额', 'payMoney', null, 1, null, null,
        null, null, null, 1, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b4ad25282b9d40dc9720b0029b38a748', '5282f78cbb5c466e84e0f6766cb12f42', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b54873199b9941cf97deea54c0941304', '4130d76e571f4468a22a8f3833bc100c', '交易号码', 'tranReqNo', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b59f47bcd7c24f998fde2cbb50901ae7', 'af650592caa34a8bb14e2e25f8baec91', '申请单号', 'allocatedCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b6187b9beb7444d7abf3df08c07a2b82', '35986704f7a74c358d2c19cec3945f48', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b6d996936bc84ff3a9559d6ca6c79eeb', '4a04c5f37d2d4d599ab27da832a8b89c', '单据号', 'billNo', null, 5, null, null,
        null, null, null, 1, 0, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b750b63cf73b47faa6ddafc12745188c', '50e7d302bc5d464d848dfb360399d4e9', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b77f3906c1b1442c9532b85b828a8a98', '5282f78cbb5c466e84e0f6766cb12f42', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b81569b4033048218cf6c5acc4bf0fe5', '23af6dc9025849f9928357d4ab975579', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b84f9a4edefa4ce887c37c133fcb968e', 'f2ff515786194606b94e47d605408410', '金额', 'checkMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b89f92ae38554e2682004c4b5c839075', '444b256d89e44b7cbca22493dcd83355', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('b8c7aaac54f44269b995892d9141225b', 'fc06a82de5314f04b5a1899bc93c26b9', '单据号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('bac5ef81913c455d9ccc207fc54f52c6', '423e1435b1a842d792e546ed76fa235c', '资金池标识', 'poolSign', null, 1, '2',
        'poolSign', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('bad0da456ca74bc488e7474bc6c75ac5', 'da60fc7c612b4a2db4acb52e6a0a1be7', '存出编号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('bbe3886eab3143ab8129cb63cc00cb0d', '0755d4b14a4b4cc787d57e2e4190310d', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('bc272c14deec48129f015b3d5c832cf4', '0a0eb625b2f94523ae7816f5e151c028', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('bc4ebce0d3e64d16a10b85c6a57e860d', '00b92ec05fbb4c09a5abd0ef865db3d8', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('bc8789205bcc446ead172caf58fbe023', 'ea8c58eac1934554b30cbc080d353bee', '业务单号', 'accNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('bc9c942bfb98452eba145366a5b7eb5c', '8e044000d6594fe3a6718f528b3c846c', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('bdade13a40b741aca70e6e3e1996804a', '311d9fbeb77a423c9607633840086bdc', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('be409667f7b54b659d46eeff14c2e7dc', '27eeedb803004cd693c60df107453f6e', '还款金额', 'repayMoney', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('be40bac8692c46ba866588fd593fcaff', 'a28d81099a174e7cb3e9b77f3bd02b6e', '币种', 'curId', null, 1, null, null,
        null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('be4183e75af845f781f159209963a05c', 'a8fb56299c05459f9440c67ba83d9441', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('bea18f3cb4754eaf8eed73c1107906e2', '2bf804483cdf43899eb9aba3bc766dbc', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c19a47027b7446438b1de4e6fffc7a39', '1aaff46ff5b04261999376950f1b57db', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c1a14a9c6ae2439bbee292e5e6875b9c', '52b7ab1596ed44378075db443c237234', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c1f19475aab446c08b87af4c6149969e', 'd98b4de7f5004892a5b3e627f4081029', '资金池标识', 'poolSign', null, 1, '2',
        'poolSign', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c307b103dde149bfa169ce7c4ef9a19b', '88b2d7c9cde2452b96507303b912708e', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c6183fb3571840d2b0137b34a27ffa92', '5282f78cbb5c466e84e0f6766cb12f42', '单据号', 'lcNo', null, 5, null, null,
        null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c6285da0f74d4006b8c6cb44952b2537', '1aaff46ff5b04261999376950f1b57db', '申请单号', 'appNo', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c72657935f6642a1a6a41a98df41502b', '2376d8dbe6554f138ec91e1cd86d62c1', '金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c7baaec5058e44ceb4d12afe8c4bbc25', 'a28d81099a174e7cb3e9b77f3bd02b6e', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c82014f33f1943f3ad2cf43d0df13428', '4a04c5f37d2d4d599ab27da832a8b89c', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c88938daa9604b72a078494f8f19ac3a', 'da60fc7c612b4a2db4acb52e6a0a1be7', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c8ac3c27fb334e9983c0138130c20838', 'd98b4de7f5004892a5b3e627f4081029', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c8c7c83bc2c8449585ff896a23a16f39', '9f4f6ace443047aa94fa9622ddfa7b1e', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('c9cbdd35c4dd44b9817997ae02239b6a', 'd2da24b7132e4a5db5f84419b95adfef', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ca52fe8e4cac4a1698712401898e56af', 'c2f97cfe0b14469a850e3882b6674cc1', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('cb3af975a49c4b888f34f6259688cc77', 'af650592caa34a8bb14e2e25f8baec91', '申请金额', 'allocationMoney', null, 1,
        '1', null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('cb49728b8bea4a0e8297106dedc08153', '7f78d7bb4c2c4776936e5da602505b05', '抵质押物编号', 'pledgeNo', null, 5,
        null, null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('cb92718e8a1545cdae693a981d33ae77', '71db3a4a62ba4431ba7403892907b7a0', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('cbb25dffc05f45c9b0bc5658792bf09a', '472d765e95124b7082c82b2997f8cd21', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('cc8b46ad4f5a453781a2e40615a1986c', '1aaff46ff5b04261999376950f1b57db', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('cd4893b571c340cab8389d288cad2ac0', '5ac49b78e6344b0eaeec36806ae13952', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ce1f418dfd874b1197b219a272d1fd5b', 'efa7154e837346d89e2eb3bd5e07c1be', '授信调剂编号', 'refCode', null, 5,
        null, null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('cf3f9d8a846a4331b59d267d7db25aa9', '00b37b9156e3453fb720034177ea5e99', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d070b2a64c124c3f884786324110797a', '0e935d5da42848d0b7311bfef1ed5902', '金额', 'money', null, 1, '1', null,
        null, '2017-11-28 14:07:22', null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d0714bab34784dc59dc9ad90a91a25fe', 'bf2c3f92a5fd49708780a23ceb6260f3', '交易对手银行账户', 'bankAcc', null, 5,
        null, null, null, null, null, 1, 2, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d0724af5698f4aeeb01ddbb4a9639b3d', 'f753b4258f7843c49a2597894cb42d43', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d073da89f2174a669fde06f20f536576', 'b918087da24041b8ab99d35932d16eb6', null, null, null, 3, null, null, null,
        null, '2017-11-27 17:42:42', null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d09a205557844fecbff36c263edd76a7', '50e7d302bc5d464d848dfb360399d4e9', '保函变更单编号', 'guaraChangeNo', null,
        5, null, null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d0addcfcc02f45eaaaa3f63a3831f42b', '8105e4ce58b64f1cb6d3cd6ffd0c22f2', '退款申请号', 'backNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d0bd21bbe87a4b6a8d12b09aa9cac6f8', '7f78d7bb4c2c4776936e5da602505b05', '抵质押物编号', 'pledgeNo', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d0e0c583e1514cf58febc5ac7c80c788', 'a28d81099a174e7cb3e9b77f3bd02b6e', '金额', 'payMoney', null, 1, '1', null,
        null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d2254810ab2b4e049e2112ef7d54b7e2', '472d765e95124b7082c82b2997f8cd21', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d2edab0c0db74ec1802919957f17cb51', '1126c57850234bf1906fa4046b9b8bce', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d32243b63fc742a0a6054f709a9115a9', '1db5a132b93544fb91c7f1443dd5efe5', '业务单号', 'accNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d3bc2f0620d34bbfb42e3cd52861c09f', '9d683b60ab5a4150b7b030b82bb927d2', '授信录入编号', 'ratCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d40d9342ed21431a826d6b8c55c5c36e', '4452ddc56e21449cb11f6a4a6f01756d', '展期申请编号', 'billCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d415e7abf41943b0ac4b621f6de3e615', '1126c57850234bf1906fa4046b9b8bce', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d475f5557a84401ba891207f4fdd90d3', 'cb6fb2a450f642399a1ebf9eb5f830c2', '汇款类型', 'remitType', null, 1, '2',
        'fexForeignReq.remitType', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d5e5c948d070412ea0f0b498119ead0f', '54ef2d64d84042f68a6e58e4013374bc', '价钱', 'price', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d6465744350c4c7eaf151f56841b222a', '9d683b60ab5a4150b7b030b82bb927d2', '授信录入编号', 'ratCode', null, 5,
        null, null, null, null, null, 1, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d6acdec54b3349c6bb2fd711338bbd9c', '423e1435b1a842d792e546ed76fa235c', '金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d71b8e0e1e1c4a0185d05d4303f0cbf3', '0a0eb625b2f94523ae7816f5e151c028', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d80319b9cb8f42de86db845540b173c6', 'aeeb1e21883b4872b028d759033883a4', '单据号', 'billNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d8abcfa5e0414aeca1747613e7982fe5', '3a3237a2d1974e1d9017372a5191b260', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d9650017b4af49f1bda56fbed372a880', 'b918087da24041b8ab99d35932d16eb6', '发放金额', 'loanMoney', null, 1, '1',
        null, null, null, '2017-11-27 17:42:42', null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d997da03028d4d12b4af0d999259863e', 'd4be7deefb524e5492a20054cf62c582', '贷款编号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('d9db797bad0b4600b7aa57974db7cedd', '03b86b51d30d4a479d9b6bca322ea02c', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('da30168271d440198431d88c8df215d5', '8e044000d6594fe3a6718f528b3c846c', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('da3aee3ff4ab4d878c37ec12bdbe41db', '444b256d89e44b7cbca22493dcd83355', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('da3e82dabc9c498eab492c55ab647b47', '9c27fe2cd74c4985ae38d50249b334d9', '授信分配编号', 'disCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('dafea2110af545c0893e6edecec0d43a', 'b776bcb16c674f6d9b753e19f25cdf3d', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('dcc3edf95b1049a2a9fbfaa74c9b07f1', '8d2aebe8050c451aa809665c5ad77b95', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 6, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('dcdd254da4e54844953aabd6018a9c68', '5f245229483f437b9921d42a053cdfcc', '展期单号', 'billCode', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('deb7918c18fc4771ae114f3a18084c58', '5b022edcc94e49c3a743473da89c19b7', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('dece7b961c714eb19db70f08a47cbec2', 'a2274e9e4c104640b7bb5dfeb37a8451', '付款方式', 'voucherType', null, 1, '1',
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('df407a4bf3ca44bb826494a2c8efd46f', '9820c5d4922b40b49fa9a7c34413aedc', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('dfddc2d159e8406598e491b48f6bee07', '9d683b60ab5a4150b7b030b82bb927d2', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('dffe8fc9a3b84ce1a21abf535a1a7527', 'f2ff515786194606b94e47d605408410', '计划周期类型', 'timeType', null, 1,
        '2', 'fbs.timeType', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e11dfe94f4b14bc681d73caf5a8f65e7', '7a0b4aa8f7dd4c2cba660612d315b5a8', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e196a07af0034a639427f7c6043ec945', 'd38b7af945d74c5e9ae0778f987d2537', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e28384700bbe426fac1dcb2ce1cb259e', 'faa87b4b3d1a45cd9651c586130dea2b', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e3397e5f9f4449d58c3b1eee21284586', 'b776bcb16c674f6d9b753e19f25cdf3d', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e37f8d4136f444b999ad77fa91023ec7', 'a2274e9e4c104640b7bb5dfeb37a8451', '金额', 'payMoney', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e38bf55ce1594423beccaa6b31402ab9', '9d683b60ab5a4150b7b030b82bb927d2', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e3a133d26b0c4867bdadd98f4356ddcc', 'c32cb6c4353e402d991ef3a94736d691', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e3d59b5d37254e4d9f4a01d52061c01f', 'd38b7af945d74c5e9ae0778f987d2537', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 4, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e44dfa11d4404ac68ac96a478ebc6a81', 'b776bcb16c674f6d9b753e19f25cdf3d', '单据号', 'lcNo', null, 4, null, null,
        null, null, null, null, null, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e473564372ae478ca46116d4cab6ecf5', '011e6146b184417c8008288029778bde', '单据号', 'guaNo', null, 5, null, null,
        null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e4818fab5023424e8785bca405cf6d02', 'a2274e9e4c104640b7bb5dfeb37a8451', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e4e1638f7cc5430393e3d5c744cf5fb1', '00b92ec05fbb4c09a5abd0ef865db3d8', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e54519b209f7453387f44bb63f48ebfa', 'f33256068909476f973250e5b7016f98', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e5bf0a04577f4008b9c18921c0bdd2fa', '0a0eb625b2f94523ae7816f5e151c028', '签收标志', 'signUpFlag', null, 1, '2',
        'AB_BILL_SGNUPMK', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e5da217c14aa4cd7952dfb27f3c58918', 'af650592caa34a8bb14e2e25f8baec91', null, null, null, 3, null, null, null,
        null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e649bc720b3d4dc58518f2263fbd4fd2', '4452ddc56e21449cb11f6a4a6f01756d', '展期利率', 'deferredRate', null, 1,
        '1', null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e68ea120cc74421b86fb6b2c63c0da74', '35986704f7a74c358d2c19cec3945f48', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e6b3a166eb9c46498f65803446ffcfe3', '7ee5cbffbe1249b5870c62a314858e5c', '单据号', 'mortgageNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e6d359d98c66440bb39a5586bf9128ea', '21fb4abf996f43668771dda1af1bd80e', '金额', 'repayMoney', null, 1, '1',
        null, null, '2017-11-28 14:08:34', null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e736972a3ed2434489fb3da7400493c9', '2c1b1c7bccf74935852fbdd47c69c5e4', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e7decb38e8ae4b6c937aa01595841683', '9820c5d4922b40b49fa9a7c34413aedc', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e7f06feb72f0428eace62ad45320621e', 'c32cb6c4353e402d991ef3a94736d691', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e8f8aca082fa46fd973b2cc114ad819e', '0755d4b14a4b4cc787d57e2e4190310d', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e97820f1b6d9484d860b026627b07d1c', 'c32cb6c4353e402d991ef3a94736d691', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e994987aee6344d0b8c211a181605db8', '4130d76e571f4468a22a8f3833bc100c', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('e9dd50021dd343b5b6f2ecbc1b9bd217', '9820c5d4922b40b49fa9a7c34413aedc', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ea60383afd354874a0c22f24953ebfc7', '8e044000d6594fe3a6718f528b3c846c', '单据号', 'paymentNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('eac5162f020a446198ea6aa969fd9948', 'bc3aa2d3b6684ca38e2c203bb1ab9bd2', '单据号', 'billNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ebd50fd0198941488c11abb9a1cbbe4d', '311d9fbeb77a423c9607633840086bdc', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ebf8759e5e4f48879981cd7d8da50eaf', 'b8e4dbd992014229b9b585d567c92013', '贷款申请编号', 'billCode', null, 4,
        null, null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ec04411e7abc4e45a0c72cc0d548ee1c', '5282f78cbb5c466e84e0f6766cb12f42', '单据号', 'lcNo', null, 4, null, null,
        null, null, null, null, null, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ec314abcfcea4aeea190ba48a516faaf', '01f54b36e086469ead0929fd0cbd799e', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ed102623be9043ba891c5ddbd8baca5c', 'bf2c3f92a5fd49708780a23ceb6260f3', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 4, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ee8057a44f9d47d8a88143d8aec15c92', '2376d8dbe6554f138ec91e1cd86d62c1', '业务单号', 'billNo', null, 4, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ef0b6db67ada4142833cc34884214c09', '44ef51bd98dc4349bbd5fca6efc8c449', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('efa00d0ccb624b5996cfd2f6dbfdd40d', '392233eee3b94e2d8df6310649020298', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('efc5f3d73a004363953156bbf089b9b9', 'bf2c3f92a5fd49708780a23ceb6260f3', '账户币种', 'curName', null, 5, null,
        null, null, null, null, 1, 3, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f1353468258f4653a474bed75f704c93', '472d765e95124b7082c82b2997f8cd21', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f2472e88f93c43b7b261fd72037211c6', '9f4f6ace443047aa94fa9622ddfa7b1e', '申请时间', 'createTime', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f2d1d1b1bde84ba4b81de6b3a03d83f1', '338667e2f16f4328942215ba86e3cd5c', '票据类型', 'billType', null, 1, '2',
        'AB_BILL_TYPE', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f2f9fd8db3834291a8817bc86db8aab3', '444b256d89e44b7cbca22493dcd83355', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f301f5f10ad14f4b9d2cf85bd65ffe23', 'd4be7deefb524e5492a20054cf62c582', '贷款金额', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f3389eacc4a9423982de69722188de65', '425cf3488b7c47b9a5a4498a1f1ad5ef', '追加额度', 'money', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f41334faac5247a3b221b3ba67c0f02d', '8d2aebe8050c451aa809665c5ad77b95', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 4, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f493373dd0e94666bb93b1d3b00e3963', '9f4f6ace443047aa94fa9622ddfa7b1e', '账号', 'blnctrAcc', null, 1, null,
        null, null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f493e1a8724c4f9bb9ec5270777a570f', '2c1b1c7bccf74935852fbdd47c69c5e4', '单据号', 'arrivalCode', null, 5, null,
        null, null, null, null, 1, 0, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f6004f7a6a5749fdbc99179e83bdc3c9', '4a04c5f37d2d4d599ab27da832a8b89c', '申请时间', 'createDate', null, 5, null,
        null, null, null, null, 1, 5, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f7ee009ed254451799ae35bd81c235b2', 'aeeb1e21883b4872b028d759033883a4', '金额', 'payMoney', null, 1, null, null,
        null, null, null, 1, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f89f043ed0134195ad249659acf75d50', 'c1b3fbd899da4765bf13fc80b5af2e73', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f8c1a9dbf1054aacaaabb7173b78baa6', 'b3c36c632e914e04a6606731c374201d', '计划周期类型', 'timeType', null, 1,
        '2', 'fbs.timeType', null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f8ffe9deb0164a98b0dc486be3134565', '245fd91ce6234c78aadbd361cef5e2ba', '单据号', 'billNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('f9a0edf0518c43baa488535b45cf94d6', 'fc06a82de5314f04b5a1899bc93c26b9', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('fa0f4f9e06134be28325397af26bf343', '74ef48866d8a4526b3043a974c089720', '票据号', 'origNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('fa362d74981f46119ad987d81b5a3f2b', 'd38b7af945d74c5e9ae0778f987d2537', '金额', 'sellMoney', null, 1, '1', null,
        null, null, null, 1, 1, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('fb2bda22f85f4739bddfad9c3e01c69c', '577f1f5ed83b4102aca5e3b69b9f24cb', '申请单位', 'corpName', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('fb840831e9fb4cdd8ca1165f009b97fe', '7b2ef8bf47f84994b127628eeba50245', '金额', 'payMoney', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('fbe3fcdc39014712a3c96e556181be30', '2c1b1c7bccf74935852fbdd47c69c5e4', '申请人', 'username', null, 5, null,
        null, null, null, null, 1, 2, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('fc76392d4829468fb9dea236af9e187e', '346cd67c18dc473b9bdd854e1fc9a3a8', '单据号', 'billNo', null, 4, null, null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('fc9f228fd39247029eee71351518d20d', '23af6dc9025849f9928357d4ab975579', '金额', 'payMoney', null, 1, '1', null,
        null, null, null, null, null, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ff0ad4910be143ebbd2003f39af62c6b', 'c2f97cfe0b14469a850e3882b6674cc1', '申请时间', 'createDate', null, 5, null,
        null, null, null, null, 1, 3, 1);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ff1818099d984b72a65ac2634bc21aee', '444b256d89e44b7cbca22493dcd83355', '交易对手名称', 'cotpName', null, 5,
        null, null, null, null, null, 1, 1, null);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ffa70fd6f66f4ebfb34fcf08d2d4deee', 'a59e6b068ba6424f83d18214e0e2f8b6', '单据号', 'businessId', null, 5, null,
        null, null, null, null, 2, 0, 2);
INSERT INTO act_extend_option (ID, CATEGORY, NAME, FIELD_NAME, EVENT, TYPE, SELECT_TYPE, DICT_GROUP, MSG_TPL,
                               CREATE_TIME, UPDATE_TIME, VISIBLE, SORT, STABLE_FIELD)
VALUES ('ffbb1a16b57d4f18a5ff71f8113be2a0', '0755d4b14a4b4cc787d57e2e4190310d', '合同编号', 'contractCode', null, 5,
        null, null, null, null, null, 1, 0, 2);
