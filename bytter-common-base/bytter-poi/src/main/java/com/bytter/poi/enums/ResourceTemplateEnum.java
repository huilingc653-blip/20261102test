package com.bytter.poi.enums;
import java.util.HashSet;
import java.util.Set;
/**
 * 静态资源下载、导出模版路径枚举
 * Created by zhouj on 2017/6/15.
 */
public enum ResourceTemplateEnum {
    EXPORT_TEST_XLS("00001", "export/test/exportTemp.xls", "",""),
    EXPORT_LODOP_CLODOP_SETUP("402880455c5dcd90015c5dcdda790L00", "export/lodop/CLodop_Setup_for_Win32NT.exe", "",""),
    EXPORT_LODOP_INSTALL_LODOP32("402880455c5dcd90015c5dcdda790L01", "export/lodop/install_lodop32.exe", "",""),
    EXPORT_LODOP_INSTALL_LODOP64("402880455c5dcd90015c5dcdda790L02", "export/lodop/install_lodop64.exe", "",""),
    EXPORT_LODOP_TESTSIGNATURE("402880455c5dcd90015c5dcdda790L03", "export/lodop/402880455c5dcd90015c5dcdda790L03.png", "",""),
    EXPORT_TEST_DOC("00002", "export/test/test.docx", "",""),
    /**********基础平台***********/
    /**
     * 单位导入模板
     */
    EXPORT_SYS_CORP_XLSX("90000001", "export/sys/corp.xls", "com.bytter.system.vo.CorpVO","网点/单位导入模板"),
    EXPORT_SWIFTINFO_XLSX("90000002", "export/sys/swiftinfo.xls", "com.bytter.basic.vo.BtSwiftInfoVO","境外银行行号信息导入模板"),
    EXPORT_SYS_USER_XLSX("90000003", "export/sys/user.xls", "com.bytter.system.vo.UserVO","用户信息导入模板"),
    /**
     * 管理单元导出
     */
    EXPORT_SYS_CELL_XLSX("90000004", "export/sys/cell.xls", "","管理单元导入模板"),    //自定义数据映射导出模板
    /**
     * 组织架构单位导出
     */
    EXPORT_SYS_ORGTCORP_XLSX("90000004", "export/sys/orgtCorp.xls", "","组织架构导入模板"),    //自定义数据映射导出模板
    /**
     * 承兑汇票
     */
    EXPORT_BILL_ACCEBILL_ABEMITBILL_XLSX("1008001", "export/bill/accebill/abEmitBill.xls", "com.bytter.bill.domain.bill.vo.AbEmitBillVo","/bill","开票登记导入模板"),//开票登记
    EXPORT_BILL_ACCEBILL_ABEMITBILL_PAY_XLSX("1008001Pay", "export/bill/accebill/abEmitBillPay.xls", "com.bytter.bill.domain.bill.vo.AbEmitBillVo","/bill","付款已开票批量导入模板"),//付款已开票批量导入模板
    EXPORT_BILL_ACCEBILL_ABEMITBILLSUPPLY_XLSX("10086101", "export/bill/accebill/abEmitBillSupply.xls", "com.bytter.bill.domain.bill.vo.AbEmitBillSupplyVo","/bill","开票补录导入模板"),//开票补录
    EXPORT_BILL_ACCEBILL_ABRECEIVEBILL_XLSX("20000001", "export/bill/accebill/abReceiveBill.xls", "com.bytter.bill.domain.bill.vo.AbReceiveBillVO","/bill","收票登记导入模板"),//收票登记
    EXPORT_BILL_ACCEBILL_ABBILLDISCOUNTAPPLY_XLSX("20000002", "export/bill/accebill/abBillDiscountApply.xls", "com.bytter.bill.domain.bill.vo.AbBillDiscountApplyVO","/bill","贴现申请导入模板"),//贴现申请
    EXPORT_BILL_ACCEBILL_ABRECEIVEBILLENDORSE_XLSX("512880455c5dcd90015c5dcdda790L01", "export/bill/accebill/abReceiveBillEndorse.xls", "com.bytter.bill.domain.bill.vo.AbBillEndorseVo","/bill","票据背书导入模板"),//票据背书
    EXPORT_BILL_ACCEBILL_ABRECEIVEBILLPLEDGE_XLSX("512880455c5dcd90015c5dcdda790L02", "export/bill/accebill/abReceiveBillPledge.xls", "com.bytter.bill.domain.bill.vo.AbBillPledgeApplyVo","/bill","票据质押导入模板"),//票据质押
    EXPORT_BILL_ACCEBILL_ABGUARANTEE_XLS("20000003", "export/bill/accebill/abBillGuarantee.xls", "com.bytter.bill.domain.bill.vo.AbBillGuaranteeApplyVO","/bill","提示保证导入模板"),//提示保证
    EXPORT_BILL_ACCEBILL_ABSEARCHFORAPPLY_XLS("20000004", "export/bill/accebill/abBillSearchforApply.xls", "com.bytter.bill.domain.bill.vo.AbBillSearchforApplyVO","/bill","追索申请导入模板"),//追索申请
    EXPORT_BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_XLSX("20000005", "export/bill/accebill/abBillDiscountManager.xls", "com.bytter.bill.domain.bill.vo.AbBillDiscountManagerVO","/bill","贴现办理导入模板"),//贴现办理
    EXPORT_BILL_ACCEBILL_ABBILL_ACCEBILLINFOPAGE_XLSX("512880455c5dcd90015c5d0abillpage", "export/bill/accebill/accebillInfoPage.xlsx", "com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO","/bill","选票组件添加已选解析导入模板"),//选票组件添加已选解析
    EXPORT_BILL_ACCKEEPAMT_XLSX("20000006", "export/bill/accebill/billAccKeepAmt.xlsx", "com.bytter.bill.domain.bill.vo.BillAccKeepAmtVO","/bill","银承账号留存金额导入模板"),//银承账号留存金额导入模板
    EXPORT_BILL_ACCEBILL_TARGET_PROPORTION_XLSX("20000007", "export/bill/accebill/abEmitBillTargetProportion.xls", "com.bytter.bill.domain.bill.vo.AbEmitBillTargetProportionInfoVO","/bill","银承开票占比目标导入模板"),//银承开票占比目标导入模板
    /**
     * 承兑汇票-银行开票授信资料维护
     */
    EXPORT_BILL_ACCEBILL_CREDIT_MAINTAIN_XLSX("20000008", "export/bill/accebill/abBillCreditMaintain.xlsx", "com.bytter.bill.domain.bill.vo.AbBillCreditInfoMaintainVO","/bill","银行开票授信资料维护导入模板"),//银行开票授信资料维护导入模板
    /**
     * 承兑汇票-银行计划开票金额维护
     */
    EXPORT_BILL_ACCEBILL_PLANAMT_MAINTAIN_XLSX("20000009", "export/bill/accebill/abBillPlanAmtMaintain.xlsx", "com.bytter.bill.domain.bill.vo.AbBillPlanAmtMaintainVO","/bill","银行计划开票金额维护导入模板"),//银行计划开票金额维护导入模板
    /**
     * 票据池
     */
    EXPORT_BILLPOOL_ABPOOLEMITFINAAPPLY_XLSX("a03c23089fee4a9fbdae1f53773ae1da", "export/billpool/poolEmitFinaApply.xls", "com.bytter.bill.domain.billPool.vo.AbPoolEmitFinaApplyVO","/bill","出票融资申请导入模板"),//出票融资申请
    /**
     * 空白凭证
     */
    EXPORT_BILL_BLANKVOU_BVVOUINPUT_XLSX("210000005c5dcd90015c5dcdda790L01", "export/bill/blankvou/bvVouInputImp.xls", "com.bytter.cashVoucher.vo.BvVouInputVo","/cashVoucher","空白凭证入库导入导入模板"),//空白凭证入库导入
    /**
     * 支票管理
     */
    EXPORT_BILL_BLANKVOU_BVPAYEECHEQUE_XLSX("b9f912f778dd11e8b443060400ef5315", "export/bill/blankvou/bvpayeevhequetemplate.xlsx", "com.bytter.cashVoucher.vo.BvPayeeChequeVO","/cashVoucher","支票收支导入导入模板"),//支票收支导入
    /**
     * 黑白名单管理
     */
    EXPORT_SYS_BLACKWHITE_XLSX("e291a359a8864ccf888c5a028e9c6a03", "export/sys/sysBlackWhite.xlsx", "com.bytter.system.vo.BlackWhiteListVO","黑白名单导入模板"),//黑白名单
    /**
     * 结算管理
     */
    EXPORT_NETBANK_BUSTYPE_XLSX("10000001", "export/netbank/nisBusType.xls", "com.bytter.biz.netbank.vo.NisBusTypeExcleVO","/payment","业务种类导入模板"),//业务种类维护
    EXPORT_NETBANK_CGSBILL_XLSX("10000002", "export/netbank/cgsBill.xls", "com.bytter.biz.netbank.vo.CgsBillVO","委托收款导入模板"),//委托收款
    EXPORT_NETBANK_PERSONNELDETAIL_XLSX("10000003", "export/netbank/nisPersonnelDetail.xls", "com.bytter.system.vo.v1.NisPersonnelExcelVO","职工信息导入模板"),//职工信息维护
    EXPORT_NETBANK_EXPENSE_XLSX("10000004", "export/netbank/nisExpenseDetail.xls", "com.bytter.biz.expense.vo.NisExpenseDetailVO", "/payment","费用报销导入模板"),    //费用报销
    EXPORT_NETBANK_EXPENSE_ITEMCODE_XLSX("100000041", "export/netbank/nisExpenseDetailForFbs.xls", "com.bytter.biz.expense.vo.NisExpenseDetailVO", "/payment","费用报销(挂计划)入模板"),    //费用报销挂资金计划
    EXPORT_NETBANK_PAY_XLSX("10000005", "export/netbank/nisBillhead.xls", "com.bytter.biz.netbank.vo.NisBillheadVO", "/payment","付款办理导入模板"),    //付款办理
    EXPORT_NETBANK_PAY_ITEMCODE_XLSX("100000051", "export/netbank/nisBillheadForFbs.xls", "com.bytter.biz.netbank.vo.NisBillheadVO", "/payment","付款办理(挂计划)导入模板"),    //付款办理挂资金计划
    EXPORT_NETBANK_BISACCBAL_XLSX("10000006", "export/netbank/bisAccBal.xls", "com.bytter.biz.inneraccount.vo.BisAccBalVO", "/payment","银行账户余额导入模板"),    //银行账户余额管理
//    EXPORT_NETBANK_BISACCBAL_XLSX("10000006", "export/netbank/bisAccBal.xls", "com.bytter.biz.bankacc.vo.BisAccBalVO", "/payment","银行账户余额导入模板"),    //银行账户余额管理
    EXPORT_NETBANK_BANKACCDTL_XLSX("10000007", "export/netbank/btBankAccDtl.xls", "com.bytter.biz.inneraccount.vo.BtBankAccDtlVO", "/payment","银行账户流水导入模板"),    //银行账户流水管理
    EXPORT_NETBANK_BANKACCDTL_CONFIG("100000071", "export/netbank/btBankAccDtl.xls", "com.bytter.biz.inneraccount.vo.BtBankAccDtlImportVO", "/payment/import/excel/uploadCustom","银行账户流水管理智能导入导入模板"),    //银行账户流水管理(智能导入,"")
    EXPORT_NETBANK_BANKACCDTL_RPA("100000072", "export/netbank/btBankAccDtlRpa.xls", "com.bytter.biz.inneraccount.vo.BtBankAccDtlRpaVO", "/payment/import/excel/uploadRpa","银行账户流水RPA导入模板"),    //银行账户流水RPA导入模板
    EXPORT_NETBANK_BANKACCDTL_HISDTL("100000073", "export/netbank/bisAccDtlHis.xlsx", "com.bytter.biz.inneraccount.vo.BisAccDtlHisVO", "/payment/import/excel/upload"," 浪潮历史流水导入模板"),    //浪潮历史流水导入模板
    EXPORT_NETBANK_BANKACC_XLSX("10000008", "export/netbank/bankAcc.xls", "com.bytter.system.api.vo.BtBankAccVO", "/payment","银行账户导入模板"),    //银行账户管理
    EXPORT_NETBANK_INITACC_XLSX("10000009", "export/netbank/initAccTemplate.xls", "com.bytter.biz.inneraccount.vo.FcInitAccVO", "/payment","内部账号初始化导入模板"),    //内部账号初始化
    EXPORT_NETBANK_WAGE_XLSX("10000010", "export/netbank/nisWage.xls", "com.bytter.biz.wage.vo.NisWageVO", "/payment/import/excel/uploads","代发工资导入模板"),    //代发工资导入
    EXPORT_NETBANK_WAGE_ITEMCODE_XLSX("100000101", "export/netbank/nisWageForFbs.xls", "com.bytter.biz.wage.vo.NisWageVO", "/payment/import/excel/uploads","代发工资导入-挂计划模板"),    //代发工资导入-挂计划
    EXPORT_NETBANK_WAGE_BATCH_XLSX("10000011", "export/netbank/nisWageBatch.xls", "com.bytter.biz.wage.vo.NisWageBatchVO", "/payment/import/excel/uploads","代发工资批量导入模板"),    //代发工资批量导入
    EXPORT_NETBANK_CAPITAL_RECORD_XLSX("10000061", "export/netbank/nisCapitalRecord.xls", "com.bytter.biz.capital.vo.NisCapitalRecordVo", "/payment","资金备案台账导入模板"),    //资金备案台账
    EXPORT_NETBANK_RECEIVABLE_XLSX("10000062", "export/netbank/nisReceivable.xls", "com.bytter.biz.receivable.vo.NisReceivableVO", "/payment","收款办理导入模板"),//收款办理
    EXPORT_NETBANK_NETUKEY_XLSX("10000083", "export/netbank/netUKey.xls", "com.bytter.system.vo.BtNetUKeyVO","网银信息维护导入模板"),//网银信息维护
    EXPORT_NETBANK_NISALLOCATIONREQ_XLSX("10000085", "export/netbank/nisAllocationReq.xls", "com.bytter.biz.report.vo.NisAllocationReqVO","/payment","资金调拨导入模板"),//资金调拨导入模板
    EXPORT_NETBANK_BANKACCGROUP_XLSX("10000086", "export/netbank/bankAccGroup.xls", "com.bytter.biz.netbank.vo.FcBankAccGroupVO","/payment","银行账户组导入模板"),//银行账户组导入模板
    EXPORT_NETBANK_CHAEOPENCORP_XLSX("3396", "export/netbank/chaeXBEnableCorp.xls", "com.bytter.biz.netbank.vo.ChaeOpenCorpVO","/payment","差额下拨启用单位导入模板"),//差额下拨启用单位
    EXPORT_NETBANK_BANKACCUSE_XLSX("10000087", "export/netbank/bankAccUse.xlsx", "com.bytter.biz.netbank.vo.BtBankAccUseVO","/payment","账号用途维护导入模板"),//账号用途维护导入模板
    EXPORT_GATHER_POLICY_AGREEMENT_XLSX("10000088", "export/netbank/gatherPolicyAgreement.xlsx", "com.bytter.biz.allocation.vo.BisGatherPolicyAgreementVO","/payment","内部资金归集管理协议签订导入模板"),//内部资金归集管理协议签订导入模板
    EXPORT_NETBANK_CORPCASHIER_XLSX("10000084", "export/netbank/corpCashier.xls", "com.bytter.biz.corpCashier.vo.NisCorpCashierVO","/payment","单位出纳导入模板"),//单位出纳导入模板
    EXPORT_NETBANK_RELATEDPARTY_XLSX("10000100", "export/netbank/relatedParty.xls", "com.bytter.biz.netbank.vo.NisRelatedPartyVO","/payment","关联方导入模板"),//关联方导入模板

    /**
     * 结算管理-账户管理-银行账户管理-银行账户申请-银行账户计划冻结申请
     */
    EXPORT_NETBANK_BTBANKACCPLANFREEZE_XLSX("10000089", "export/netbank/btBankAccPlanFreeze.xlsx", "com.bytter.biz.inneraccount.vo.BtBankAccPlanFreezeVO","/payment","银行账户计划冻结申请导入模板"),

    /**
     * 结算管理-账户管理-银行账户管理-多级单位余额核算-单位归集余额维护
     */
    EXPORT_NETBANK_BISCORPPOOLBAL_XLSX("10000090", "export/netbank/bisCorpPoolBal.xlsx", "com.bytter.biz.inneraccount.vo.BisCorpPoolBalVO","/payment","单位归集余额导入模板"),
    /**
     * 结算管理-账户管理-银行账户管理-多级单位余额核算-单位归集透支金额维护
     */
    EXPORT_NETBANK_BISCORPPOOLOVERDRAFT_XLSX("10000091", "export/netbank/bisCorpPoolOverdraft.xlsx", "com.bytter.biz.inneraccount.vo.BisCorpPoolOverdraftVO","/payment","单位归集透支金额导入模板"),


    /**
     * 核算管理
     */
    EXPORT_FINACIAL_FCBANKBILLTEMP_XLSX("30000001", "export/finacial/FcBankBillTemp.xlsx", "com.bytter.check.vo.FcBankBillTempVo","/check","银行未达账初始化导入模板"),//银行未达账初始化
    EXPORT_FINACIAL_FCCOMPAREACCTEMP_XLSX("30000002", "export/finacial/FcCompareAccTemp.xlsx", "com.bytter.check.vo.FcCompareAccTempVo","/check","ERP未达账初始化导入模板"),//ERP未达账初始化
    EXPORT_FINACIAL_FCERPDATATEMP_XLSX("30000003", "export/finacial/ErpCheckData.xlsx", "com.bytter.check.vo.FcCompareAccVO","/check","ERP对账数据导入模板"),//ERP对账数据导入
    EXPORT_FINACIAL_VOUCHERTEMP_XLSX("30000004", "export/finacial/voucherimportdtl.xlsx", "com.bytter.tally.vo.FcAccbookImportVO","/tally","记账凭证导入模板"),//记账凭证导入
    /**
     * 理财产品
     **/
    EXPORT_CHO_PRODUCT_REQ_XLSX("18000001", "export/bill/accebill/abBillDiscountManager.xls", "com.bytter.increment.domain.vo.ChoProductReqImportVo","/increment","理财产品导入模板"),
    /**
     * 数据管理
     */
    EXPORT_NETBANK_INFO_XLSX("40000001", "export/netbank/btInputBankInfo.xls", "com.bytter.basic.vo.BtInputBankInfoVO","银行行号信息导入模板"),//银行行号信息维护
    EXPORT_EXTERNAL_CORP_XLSX("40000002", "export/sys/externalCorpInfo.xls", "com.bytter.system.vo.ExternalCorpInfoImportVO","客商资料导入模板"),
    EXPORT_PURPOSE_XLSX("40000003", "export/sys/purpose.xls", "com.bytter.basic.vo.BtPurposeVO","用途导入模板"),
    EXPORT_SYS_OPERATION_AREA_XLSX("40000004", "export/sys/operation_area.xls", "com.bytter.system.vo.OperationAreaVo","营运区维护导入模板"),
    EXPORT_SYS_EQUITY_MAINTENANCE_XLSX("40000005", "export/sys/equity_maintenance.xls", "com.bytter.system.vo.EquityMaintenanceVo","股权维护导入模板"),
    EXPORT_SYS_UNIT_STORE_MAINTENANCE_XLSX("40000006", "export/sys/unit_store_maintenance.xls", "com.bytter.system.vo.UnitStoreMaintenanceVo","单位仓库维护导入模板"),
    EXPORT_SYS_PROVINCE_XLSX("40000007", "export/sys/system_province.xls", "com.bytter.system.vo.SystemProvinceVo","省区维护导入模板"),

    /**
     * 资金计划
     */
    EXPORT_FBS_BUDGET_XLSX("50000001", "export/fbs/budget.xls", "com.bytter.biz.budget.vo.FbsBudgetImportVO","/budget","计划上报导入模板"),    //计划上报
    EXPORT_FBS_BUDGET_DETAIL_XLSX("50000002", "export/fbs/budgetDetail.xls", "com.bytter.biz.budget.vo.FbsBudgetDetailVO","/budget","计划明细导入模板"),    //资金计划明细
    EXPORT_FBS_BUDGET_PROCESS_XLSX("50000003", "export/budget/budgetProcess.xls", "com.bytter.biz.budget.vo.FbsBudgetProcessVO","/budget","计划执行导入模板"),    //计划执行
    EXPORT_FBS_BUDGET_INPUT_DETAIL_XLSX("50000004", "export/fbs/budgetDetail.xls", "com.bytter.biz.budget.vo.FbsBudgetInputDetailVO","/budget","计划明细批量导入模板"),    //资金计划上报界面明细导入
    /**
     * 数据交换
     */
    EXPORT_DATAEXCHANGE_DYNAMIC_IMP_XLSX("60000001", "export/dataexchange/dynamicDataImp.xls", "com.bytter.biz.dataexchange.basic.vo.ErpDynamicDataVO","自定义数据映射导入模板"),    //自定义数据映射导入模板
    EXPORT_DATAEXCHANGE_DYNAMIC_EXP_XLSX("60000002", "export/dataexchange/dynamicDataExp.xls", "","导入模板"),    //自定义数据映射导出模板
    EXPORT_FINACIAL_FCERPANDBANKACCTEMP_XLSX("60000003", "export/dataexchange/FcErpAndBankAccTemp.xlsx", "com.bytter.biz.dataexchange.basic.vo.ErpBasicDataVO","/check","ERP科目与银行账户对应关系导入模板"),//银行账号与erp科目对应关系
    /**
     * 信贷融资
     */
    EXPORT_CMS_STATISCS_EXP_XLSX("70000001", "export/cms/RatBkStatisticsDataExp.xls", "","自定义银行授信额度综合统计导出模板"),    //自定义银行授信额度综合统计导出模板
    /**
     * 信贷融资
     */
    EXPORT_CMS_STATISCS_EXP_HIS_XLSX("700000010", "export/cms/RatBkStatisticsDataHisExp.xls", "","自定义银行授信额度综合历史统计导出模板"),    //自定义银行授信额度综合统计导出模板
    /**
     * 信贷融资--内部拆借报表
     */
    EXPORT_CMS_INLREPORT_EXP_XLSX("70000002", "export/cms/inlReportImportTmp.xlsx", "com.bytter.finance.domain.inl.vo.InlReportVO","/finance","内部拆借报表导入模板"),//内部拆借报表导入模板
    /**
     * 信贷融资--还款计划导入
     */
    EXPORT_CMS_BASEDATA_REPAYMENTSCHEDULE_XLSX("70000003", "export/cms/repayTypeRepayPlan.xlsx", "com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO","/finance","还款计划导入模板"),//还款计划导入模板
    /**
     * 信贷融资--担保还款计划导入
     */
    EXPORT_CMS_BASEDATA_PLAN_XLSX("70000004", "export/cms/GuaExtContractInfoRepayPlan.xlsx", "com.bytter.guarantee.domain.vo.GuaContractInfoPlanVO","/guarantee","担保还款计划导入模板"),//还款计划导入模板
    /**
     * 信贷融资--融资租赁-融资租赁标的物导入
     */
    EXPORT_CMS_LEASE_OBJECT_INFO_XLSX("70000005", "export/cms/leaseObjectInfo.xls", "com.bytter.finance.domain.lease.vo.LeaseObjectInfoVO","/finance","融资租赁标的物导入模板"),//融资租赁标的物导入模板
    /**
     * 信贷融资--融资租赁-多合同融资租赁标的物导入
     */
    EXPORT_CMS_CONTRACT_LEASE_OBJECT_INFO_XLSX("70000006", "export/cms/contractLeaseObjectInfo.xls", "com.bytter.finance.domain.lease.vo.ContractLeaseObjectInfoVO","/finance","融资租赁标的物多合同导入模板"),//融资租赁标的物多合同导入模板
    /**
     * 信贷融资--融资租赁-融资租赁标的物修改导入
     */
    EXPORT_CMS_LEASE_UPDATE_OBJECT_INFO_XLSX("70000007", "export/cms/leaseUpdateObjectInfo.xls", "com.bytter.finance.domain.lease.vo.LeaseUpdateObjectInfoVO","/finance","融资租赁标的物修改导入模板"),//融资租赁标的物修改导入模板
    /**
     * 信贷融资--内部贷款申请导入
     */
    EXPORT_CMS_LOAN_INFO_XLSX("70000008", "export/cms/cmsLoanInfoApply.xls", "com.bytter.finance.domain.loan.vo.CmsLoanInfoVO","/finance","内部贷款申请导入模板"),//内部贷款申请导入模板
    /**
     * 信贷融资--银行借款申请导入
     */
    EXPORT_CMS_LOANINFO_APPLY_XLSX("70000009", "export/cms/loanInfoApply.xls", "com.bytter.finance.domain.provide.vo.LoanInfoVO","/finance","银行借款申请导入模板"),//借部贷款申请导入模板
    /**
     * 信贷融资--融资租赁租金计划导入
     */
    EXPORT_CMS_LEASECAPITAL_PLAN_XLSX("70000015", "export/cms/leaseCapitalPlan.xlsx", "com.bytter.finance.domain.lease.vo.LeaseCapitalPlanDelVO","/finance","融资租赁租金计划导入模板"),//融资租赁租金计划导入模板
    /**
     * 信贷融资--股权融资登记明细导入
     */
    EXPORT_CMS_EQUITY_DETAIL("70000021", "export/cms/equityDetail.xls", "com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO","/finance","股权融资登记明细导入模板"),//股权融资登记明细导入模板
    /**
     * 信贷融资--债券-债券还款计划登记导入
     */
    EXPORT_CMS_BOND_REPAYPLAN_XLSX("70000020", "export/cms/listBondRepayPlan.xlsx", "com.bytter.finance.domain.bond.vo.CmsBondRepayPlanDtlVo", "/finance", "债券还款计划登记导入模板"),
    /**
     * 信贷融资--债券-可转债登记导入
     */
    EXPORT_CMS_BOND_TRANSFER_XLSX("70000022", "export/cms/listBondTransfer.xlsx", "com.bytter.finance.domain.bond.vo.CmsBondTransferVO", "/finance", "可转债登记导入模板"),
    /**
     * 信贷融资--债券-可交债登记导入
     */
    EXPORT_CMS_BOND_SWAP_XLSX("70000023", "export/cms/listBondSwap.xlsx", "com.bytter.finance.domain.bond.vo.CmsBondSwapVO", "/finance", "可交债登记导入模板"),
    /**
     * 信贷融资--缴付/过户计划登记明细导入
     */
    EXPORT_CMS_PAYPLAN_DETAIL("70000030", "export/cms/equityPayPlan.xlsx", "com.bytter.finance.domain.equity.vo.CmsEquityPayPlanDetailVO","/finance","缴付/过户计划登记明细导入模板"),//缴付/过户计划登记明细导入模板
    /**
     * 信贷融资--股权融资回购计划登记明细导入
     */
    EXPORT_CMS_REPOPLAN_DETAIL("70000031", "export/cms/equityRepoPlan.xlsx", "com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanDetailVO","/finance","股权融资回购计划登记明细导入模板"),//股权融资回购计划登记明细导入模板
    /**
     * 信贷融资--银行借款/借款申请补录--银团贷款明细导入
     */
    EXPORT_CMS_PROVIDE_SYNDICATED_XLSX("70000041", "export/cms/syndicated.xlsx", "com.bytter.finance.domain.provide.vo.ProvideSyndicatedVo","/finance","银行借款/借款申请补录银团贷款明细导入模板"),//银行借款/借款申请补录银团贷款明细导入模板
    /**
     * 信贷融资--银行借款发放--银团贷款明细导入
     */
    EXPORT_CMS_PROVIDE_SYNDICATEDSEND_XLSX("70000042", "export/cms/syndicatedSend.xlsx", "com.bytter.finance.domain.provide.vo.ProvideSyndicatedSendVo","/finance","银团贷款明细导入模板"),//银团贷款明细导入模板
    /**
     * 信贷融资--银行借款还款/银行借款还款计划--银团贷款明细导入
     */
    EXPORT_CMS_PROVIDE_SYNDICATEDDTL_XLSX("70000043", "export/cms/syndicatedDtl.xlsx", "com.bytter.finance.domain.provide.vo.ProvideSyndicatedDtlVo","/finance","银行借款还款计划银团贷款明细导入模板"),//银行借款还款/银行借款还款计划银团贷款明细导入模板
    /**
     * 信贷融资--资产证券化资产层级信息导入
     */
    EXPORT_CMS_SECURITY_LEVEL_INFO("70000040", "export/cms/securityLevelInfo.xlsx", "com.bytter.finance.domain.security.vo.CmsSecurityLevelInfoVO","/finance","资产证券化资产层级信息导入模板"),//资产证券化资产层级信息模板
    /**
     * 信贷融资--资产证券化-还款计划导入
     */
    EXPORT_CMS_SECURITY_PLAN_XLSX("70000050", "export/cms/listSecurityPlan.xlsx", "com.bytter.finance.domain.security.vo.CmsSecurityPlanDetailVO","/finance","还款计划导入模板"),

    /**
     * 信贷融资--被担保合同管理-被担保合同导入
     */
    EXPORT_CMS_GUACONTRACTINFO_XLSX("70000060", "export/cms/guaContractInfo.xlsx", "com.bytter.guarantee.domain.vo.GuaContractExcelVO","/guarantee","被担保合同导入模板"),
    /**
     * 信贷融资--担保管理-抵质押物管理
     */
    EXPORT_CMS_GUAPLEDGEINFO_XLSX("70000061", "export/cms/guaPledgeInfo.xlsx", "com.bytter.guarantee.domain.vo.GuaPledgeInfoVO","/guarantee","抵质押物导入模板"),
    /**
     * 信贷融资--银行授信管理-银行授信额度录入
     */
    EXPORT_CMS_RATBKRATION_XLSX("70000062", "export/cms/ratBkration.xlsx", "com.bytter.credit.domain.vo.RatBkrationVO","/credit","银行授信额度导入模板"),
    /**
     * 信贷融资--授信管理-授信基础数据-授信合作银行维护
     */
    EXPORT_CMS_CREDITCOOPERATIONBANK_XLSX("70000063", "export/cms/creditCooperationBank.xlsx", "com.bytter.credit.domain.vo.RatCreditCooperationBankVO","/credit","授信合作银行维护导入模板"),
    /**
     * 信贷融资--授信管理-银行授信管理-年度融资规划登记
     */
    EXPORT_CMS_YEARFINPLANREGISTER_XLSX("70000065", "export/cms/yearFinPlanRegister.xlsx", "com.bytter.credit.domain.vo.RatYearFinPlanRegisterVO","/credit","年度融资规划登记导入模板"),
    /**
     * 信贷融资--银行授信管理-银行额度系统占用
     */
    EXPORT_CMS_RATURATIONAPPLY_XLSX("70000067", "export/cms/ratUrationApply.xlsx", "com.bytter.credit.domain.vo.RatUrationApplyVO", "/credit","银行授信占用导入模板"),
    /**
     * 营收稽核
     */
    EXPORT_RAS_TEST_XLSX("80000000", "export/ras/test.xls", "com.bytter.biz.ras.vo.TestVO","营收稽核案例办理导入模板"),//营收稽核案例办理
    EXPORT_RAS_SHOP_XLSX("80000001", "export/ras/rasShop.xls", "com.bytter.biz.ras.vo.RasShopVO","营收稽核门店管理导入模板"),//营收稽核门店管理
    EXPORT_RAS_POSBINDING_XLSX("80000002", "export/ras/rasPosBinding.xls", "com.bytter.biz.ras.vo.RasPosBindingVO","营收稽核POS机绑定导入模板"),//营收稽核POS机绑定
    EXPORT_RAS_BANK_XLSX("80000099", "export/ras/rasBankBinding.xlsx", "com.bytter.biz.ras.vo.RasBankCardBindingVO","营收稽核银行卡信息绑定导入模板"),//营收稽核银行卡信息绑定
    EXPORT_RAS_OTHER_CARD_XLSX("80000098", "export/ras/rasOtherCardBinding.xlsx", "com.bytter.biz.ras.vo.RasOtherCardBindingVO","营收稽核第三方账户信息绑定导入模板"),//营收稽核第三方账户信息绑定
    EXPORT_RAS_REVENUE_IMPORT_MODEL("80000066", "", "com.bytter.biz.ras.vo.RasRevenueDtlVO","营收数据导入模板"),//营收数据导入
    EXPORT_RAS_RECEIVED_IMPORT_MODEL("80000067", "", "com.bytter.biz.ras.vo.RasReceivedDtlVO","实收数据导入模板"),//实收数据导入
    /**
     * 外汇管理
     */
    EXPORT_FEX_FOREIGN_XLSX("100000001", "export/foreign/fexForeignReq.xls", "com.bytter.biz.foreign.vo.FexForeignReqVO","/payment","外币汇款申请导入模板"),    //外币汇款申请
    EXPORT_FEX_FOREIGN_ITEMCODE_XLSX("1000000011", "export/foreign/fexForeignReqForFbs.xls", "com.bytter.biz.foreign.vo.FexForeignReqVO","/payment","外币汇款申请挂资金计划导入模板"),    //外币汇款申请挂资金计划
    EXPORT_FEX_COTP_XLSX("1000000012", "export/foreign/fexCotp.xls", "com.bytter.biz.foreign.vo.FexCotpVO","/payment","交易对手导入导入模板"),    //交易对手导入
    EXPORT_FEX_RATE_INFO_XLSX("1000000013", "export/foreign/fexRateInfo.xls", "com.bytter.biz.foreign.vo.FexRateInfoVO","/payment","外汇牌价导入模板"),    //外汇汇率导入
    EXPORT_FEX_ADD_WORD_XLSX("1000000014", "export/foreign/fexAddWord.xls", "com.bytter.biz.foreign.vo.FexAddWordVO","/payment","交易附言导入模板"),    //交易附言导入

    /**
     * 供应链金融-应付债权凭证导入模板
     */

    EXPORT_SCF_PAYABLE_VOUCHER_XLSX("3450001", "export/scf/scfPayableVoucher.xls", "com.bytter.supply.domain.payable.vo.ScfPayableVoucherExcelVO","应付债权凭证导入模板"),

    /**
     * 供应链金融-应收债权凭证导入模板
     */
    EXPORT_SCF_RECEIVABLE_VOUCHER_XLSX("3450002", "export/scf/scfReceivableVoucher.xls", "com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherImportVO","应收债权凭证导入模板"),    //应付债权凭证导入模板

    /**
     * 第三方支付
     */
    EXPORT_TTPAY_MERCHANT_XLSX("990000005c5dcd90015c5ttpay790L00", "export/ttpay/merchantImp.xls", "com.bytter.biz.ttpay.vo.TpMerchantVO","/payment","商户信息导入模板"),    //商户信息导入
    EXPORT_TTPAY_PAYMENT_XLSX("110000050", "export/ttpay/tpPayment.xls", "com.bytter.biz.ttpay.vo.TpPaymentVO","/payment","第三方支付导入模板"),    //第三方支付
    EXPORT_TTPAY_PAYMENT_ITEMCODE_XLSX("110000051", "export/ttpay/tpPaymentForFbs.xls", "com.bytter.biz.ttpay.vo.TpPaymentVO","/payment","第三方支付（挂计划）导入模板"),    //第三方支付挂资金计划
    EXPORT_NIS_WAGE_RELEASE_CONFIG_XLSX("202511240001", "export/netbank/nisWageReleaseConfig.xls", "com.bytter.biz.wage.vo.NisWageReleaseConfigVO", "/payment", "代发工资下拨数据配置 导入模板"),

    /**
     * 机构管理
     */
    EXPORT_FUNDWARN_CREDITRATING_OWNERSHIPSTRUCTURE_XLSX("120000001", "export/creditRating/ownershipStructure.xls","com.bytter.biz.creditRating.vo.CrOwnershipStructureDataVo","/creditRating","股权结构导入模板"),
    EXPORT_FUNDWARN_CREDITRATING_CONTACTADDRESSBOOK_XLSX("120000002", "export/creditRating/contactAddressBook.xls", "com.bytter.biz.creditRating.vo.CrContactAddressBookVo","/creditRating","联系人通讯录模板"),
    EXPORT_FUNDWARN_CREDITRATING_VISITRECORDREGISTRATION_XLSX("120000003", "export/creditRating/visitRecordRegistration.xls", "com.bytter.biz.creditRating.vo.CrVisitRecordRegistrationVo","/creditRating","拜访记录登记模版"),

    /**
     * 应收单管理
     */
    EXPORT_NETBANK_RECEIVABLE_DOC_MANAGE_XLSX("10000063", "export/accountReceivable/docManage.xls", "com.bytter.accountReceivable.docManage.vo.DocManageVo","/accountReceivable","应收单管理模版"),
    ;

    private String code;
    private String path;
    private String reflex;
    private String url;
    private String chineseName;
    /**
     * 存储自定义扩展的资源枚举类
     */
    private static Set<IResourceTemplateEnum> customResourceTemplateEnumSet = new HashSet<>();
    ResourceTemplateEnum(String code, String path, String reflex,String chineseName) {
        this.code = code;
        this.path = path;
        this.reflex = reflex;
        this.chineseName =  chineseName;
    }
    ResourceTemplateEnum(String code, String path, String reflex,String url,String chineseName) {
        this(code, path, reflex,chineseName);
        this.url = url;
        this.chineseName =  chineseName;
    }
    private static IResourceTemplateEnum resourceTemplateEnum2IResourceTemplateEnum(final ResourceTemplateEnum rt) {
        if (rt == null) {
            return null;
        }
        return new IResourceTemplateEnum() {
            @Override
            public String getCode() {
                return rt.getCode();
            }
            @Override
            public String getPath() {
                return rt.getPath();
            }
            @Override
            public String getReflex() {
                return rt.getReflex();
            }
            @Override
            public String getUrl() {
                return rt.getUrl();
            }
            public String getChineseName() {
                return rt.getChineseName();
            }
        };
    }
    /**
     * 根据资源code获取 资源枚举对象
     *
     * @param code 资源code
     * @return 资源枚举对象
     */
    public static IResourceTemplateEnum getResourceTemplate(String code) {
        for (IResourceTemplateEnum rt : customResourceTemplateEnumSet) {
            if (rt.getCode().equals(code)) {
                return rt;
            }
        }
        for (ResourceTemplateEnum rt : ResourceTemplateEnum.values()) {
            if (rt.getCode().equals(code)) {
                return resourceTemplateEnum2IResourceTemplateEnum(rt);
            }
        }
        return null;
    }
    /**
     * 根据资源code 获取 资源路径
     *
     * @param code 资源code
     * @return 资源路径
     */
    public static String getResource(String code) {
        for (IResourceTemplateEnum rt : customResourceTemplateEnumSet) {
            if (rt.getCode().equals(code)) {
                return rt.getPath();
            }
        }
        for (ResourceTemplateEnum rt : ResourceTemplateEnum.values()) {
            if (rt.getCode().equals(code)) {
                return rt.getPath();
            }
        }
        return null;
    }

    public static String getChiniseNameByResource(String code) {
        for (IResourceTemplateEnum rt : customResourceTemplateEnumSet) {
            if (rt.getCode().equals(code)) {
                return rt.getChineseName();
            }
        }
        for (ResourceTemplateEnum rt : ResourceTemplateEnum.values()) {
            if (rt.getCode().equals(code)) {
            	return rt.getChineseName();
            }
        }
        return null;
    }
    /**
     * 添加自定义的资源枚举
     *
     * @param resourceTemplateEnum 自定义的资源枚举实现接口
     */
    public static void addResourceTemplateEnum(IResourceTemplateEnum resourceTemplateEnum) {
        if (resourceTemplateEnum == null) {
            return;
        }
        for (ResourceTemplateEnum rt : ResourceTemplateEnum.values()) {
            if (rt.getCode().equals(resourceTemplateEnum.getCode())) {
                throw new RuntimeException("自定义资源模板实现类的资源code与现有资源code重复！请修改自定义资源模板实现类的资源code！");
            }
        }
        customResourceTemplateEnumSet.add(resourceTemplateEnum);
    }
    public String getCode() {
        return code;
    }
    public String getPath() {
        return path;
    }
    public String getReflex() {
        return reflex;
    }
    public String getUrl(){return url;}

    public String getChineseName() {
        return chineseName;
    }

}
