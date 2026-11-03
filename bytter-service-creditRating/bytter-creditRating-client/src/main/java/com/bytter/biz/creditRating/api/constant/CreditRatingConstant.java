package com.bytter.biz.creditRating.api.constant;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 信用评级常量类
 */
public class CreditRatingConstant implements Serializable {

    public static final String INDICATOR_DIRECTION_CODE = "01";//指标方向Code
    public static final String MAX_POSSIBLE_VALUE_CODE = "02";//最大可能值Code
    public static final String MIN_POSSIBLE_VALUE_CODE = "03";//最小可能值Code
    public static final String IS_AUTO_SCORING_CODE = "04";//是否自动评分Code
    public static final String SCORING_METHOD_CODE = "05";//评分方法Code
    public static final String DATA_SOURCE_CODE = "06";//数据源Code
    public static final String IS_ADD_OR_SUB_GRADE_CODE = "07";//是否额外加/减分Code
    public static final String INDEX_PROPERTY_CODE = "08";//指标性质Code
    public static final String INDICATOR_DIRECTION_NAME = "指标方向";
    public static final String MAX_POSSIBLE_VALUE_NAME = "最大可能值";
    public static final String MIN_POSSIBLE_VALUE_NAME = "最小可能值";
    public static final String IS_AUTO_SCORING = "是否自动评分";
    public static final String SCORING_METHOD = "评分方法";
    public static final String DATA_SOURCE = "数据源";
    public static final String IS_ADD_OR_SUB_GRADE = "是否额外加/减分";
    public static final String INDEX_PROPERTY_NAME = "指标性质";
    public static final String SCORING_METHOD_GROUPCODE="credit_scoring_method";
    public static final Integer CREDIT_SCORING_GT = 1;//大于
    public static final Integer CREDIT_SCORING_GTE = 2;//大于等于
    public static final Integer CREDIT_SCORING_EQ = 3;//等于
    public static final Integer CREDIT_SCORING_LT = 4;//小于
    public static final Integer CREDIT_SCORING_LTE = 5;//小于等于
    public static final Integer CREDIT_SCORING_YES = 1;
    public static final Integer CREDIT_SCORING_NO = 0;
    public static final String CREDIT_DATASOURCE_CREDIT_EXPOSURE_LIMIT_CODE="01";//授信敞口额度
    public static final String CREDIT_DATASOURCE_CREDIT_LIMIT_CHANGE_CODE="02";//授信的增减变化
    public static final String CREDIT_DATASOURCE_FINANCE_DOLLAR_CODE="03";//融资成本(美元)
    public static final String CREDIT_DATASOURCE_FINANCE_RMB_CODE="04";//融资成本(人民币)
    public static final String CREDIT_DATASOURCE_BANK_LOAN_BALANCE_ODE="05";//贷款余额
    public static final String CREDIT_DATASOURCE_BANK_ADD_LOAN_CODE="06";//新增贷款
    public static final String CREDIT_DATASOURCE_BANK_LOAN_RATE_CODE ="07";//贷款利率（人民币）
    public static final String CREDIT_DATASOURCE_LC_GUARANTEE_BILL_BOND_CODE ="08";//承兑、信用证业务保证金比例
    public static final String CREDIT_DATASOURCE_CHO_DEPOSIT_RATE_CODE="09";//存款利率（人民币）

    public static final String  UP_SYMBOL=">";
    public static final String  UP_EQ_SYMBOL=">=";
    public static final String  EQ_SYMBOL="==";
    public static final String  DOWN_SYMBOL="<";
    public static final String  DOWN_EQ_SYMBOL="<=";

    public static final Integer SCORING_METHOD_GUIYI = 1;//归一法
    public static final Integer SCORING_METHOD_FENDUAN = 2;//分段法
    public static final Integer SCORING_METHOD_INTERPOLATION = 3;//插值法
    public static final Integer SCORING_METHOD_DESC = 4;//排名法(降序)
    public static final Integer SCORING_METHOD_ASC = 5;//排名法(升序)

    /**
     * D001 供应商，D002 子企业，D003 机构
      */
    public static final String DATA_SOURCE_CATEGORY_CODE_D001 = "D001";
    public static final String DATA_SOURCE_CATEGORY_CODE_D002 = "D002";
    public static final String DATA_SOURCE_CATEGORY_CODE_D003 = "D003";
    public static final String DATA_SOURCE_CATEGORY_CODE_D004 = "D004";
    public static final String DATA_SOURCE_CATEGORY_NAME_01 = "供应商";
    public static final String DATA_SOURCE_CATEGORY_NAME_02 = "子企业";
    public static final String DATA_SOURCE_CATEGORY_NAME_03 = "机构";
    public static final String DATA_SOURCE_CATEGORY_NAME_04 = "其他";




    public static final Integer IS_EFFECT_STATUS = 1;//有校
    public static final Integer NOT_EFFECT_STATUS = 0;
    public static final Integer DELETE_STATUS = -2;
    public static final Integer INIT_VERSION_NUMBER = 0;
    public static final Integer INIT_DELETE_FLAG = 0;

    /**
     * 机构类别:001-银行总行;002-银行支行;003-租赁租赁公司;004-信托公司;005-金额资产管理公司;
     * 006-证券公司、公募基金管理公司、期货公司;007-人身保险公司、财产保险公司、再保险公司、保险资产管理公司;
     * 008-国务院金融管理部门认定的其他机构;009-律所;010-会计师事务所;
     */
    public static final String  INSTITUTION_TYPE_CODE_ONE = "001";
    public static final String  INSTITUTION_TYPE_CODE_TWO = "002";
    public static final String  INSTITUTION_TYPE_CODE_THREE = "003";
    public static final String  INSTITUTION_TYPE_CODE_FOUR = "004";
    public static final String  INSTITUTION_TYPE_CODE_FIVE = "005";
    public static final String  INSTITUTION_TYPE_CODE_SIX = "006";
    public static final String  INSTITUTION_TYPE_CODE_SEVEN = "007";
    public static final String  INSTITUTION_TYPE_CODE_EIGHT = "008";
    public static final String  INSTITUTION_TYPE_CODE_NINE = "009";
    public static final String  INSTITUTION_TYPE_CODE_TEN = "010";

    /**
     * 机构数据流转状态：000-未准入;001-准入审批中;095-已准入; -1 打回；-2- 已删除;
     */
    public static final String INSTITUTION_DATA_STATUS_NOT_ADMITTED = "000";
    public static final String INSTITUTION_DATA_STATUS_PENDING_APPROVAL = "001";
    public static final String INSTITUTION_DATA_STATUS_PASS = "095";
    public static final String INSTITUTION_DATA_STATUS_REJECTED = "-1";
    public static final String INSTITUTION_DATA_STATUS_DEL = "-2";

    public static final String FUNDWARN_CREDITRATING_INSTITUTION_ACCESS = "fundwarn:creditRating:institution:access";
    public static final String FUNDWARN_CREDITRATING_INSTITUTION_QUERY = "fundwarn:creditRating:institution:query";
    public static final String FUNDWARN_CREDITRATING_INSTITUTION_DELETE = "fundwarn:creditRating:institution:delete";
    public static final String FUNDWARN_CREDITRATING_INSTITUTION_UPDATE = "fundwarn:creditRating:institution:update";
    public static final String FUNDWARN_CREDITRATING_INSTITUTION_VIEW = "fundwarn:creditRating:institution:view";

    public static final String FUNDWARN_CREDITRATING_INSTITUTION_APPROVE = "fundwarn:creditRating:institution:approve";
    /**计算模型编辑资源**/
    public final static String CREDITRATING_MODEL_EDIT = "creditRating:model:edit";
    /**计算模型新增资源**/
    public final static String CREDITRATING_MODEL_ADD = "creditRating:model:add";
    /**计算模型查询资源**/
    public final static String CREDITRATING_MODEL_QUERY = "creditRating:model:query";
    /**计算模型删除资源**/
    public final static String CREDITRATING_MODEL_DELETE = "creditRating:model:delete";

    /**评级标准编辑资源**/
    public final static String CREDITRATING_GRADE_EDIT = "creditRating:grade:edit";
    /**评级标准新增资源**/
    public final static String CREDITRATING_GRADE_ADD = "creditRating:grade:add";
    /**评级标准查询资源**/
    public final static String CREDITRATING_GRADE_QUERY = "creditRating:grade:query";
    /**评级标准删除资源**/
    public final static String CREDITRATING_GRADE_DELETE = "creditRating:grade:delete";

    /**评价任务查询资源**/
    public final static String CREDIT_CRTASKINFO_QUERY = "credit:crTaskInfo:query";

    /**评价任务操作资源**/
    public final static String CREDIT_CRTASKINFO_EDIT = "credit:crTaskInfo:edit";

    /**综合分析报表查询资源**/
    public final static String CREDIT_ANALYZEREPORT_QUERY = "credit:analyzeReport:query";

    /**
     * 新增审批中
     */
    public final static int APPROVAL_PROCESS_ADD = 11;

    /**
     * 修改审批中
     */
    public final static int APPROVAL_PROCESS_MODIFY = 12;
    /**评级标准删除资源**/
    public final static String CREDIT_EVALUATION_ANALYSIS_APPLY = "credit:evaluation:analysis:apply";
    /**
     * 审核通过
     */
    public final static int APPROVED = 95;
    /**
     * 审批驳回
     */
    public final static int APPROVAL_REJECTED = -1;
    /**
     * 刪除
     */
    public final static int DELETE = -2;

    /**
     * 操作类型：
     * 1 新增提交 addSubmit
     * 2、新增暂存 addTemp
     * 3、删除 del
     * 4、修改提交 modifySubmit
     * 5、修改暂存   modifyTemp
     * 6、rejected   打回
     */
    public final static String OPERATION_TYPE_ADD_SUBMIT = "addSubmit";
    public final static String OPERATION_TYPE_ADD_TEMP = "addTemp";
    public final static String OPERATION_TYPE_DEL = "del";
    public final static String OPERATION_TYPE_MODIFY_SUBMIT = "modifySubmit";
    public final static String OPERATION_TYPE_MODIFY_TEMP = "modifyTemp";
    public final static String OPERATION_TYPE_REJECTED = "rejected";


    /**
     * 日志操作类型常量：20-提交；21-暂存；22-审批通过；23-删除；24-审批打回; 25-修改
     */
    public final static String EVALUATION_LOG_TYPE_SUBMIT_CODE = "20";
    public final static String EVALUATION_LOG_TYPE_TEMP_CODE = "21";
    public final static String EVALUATION_LOG_TYPE_APPROVED_CODE = "22";
    public final static String EVALUATION_LOG_TYPE_DEL_CODE = "23";
    public final static String EVALUATION_LOG_TYPE_APPROVAL_RETURNED_CODE = "24";

    public final static String EVALUATION_LOG_TYPE_MODIFY_CODE = "25";

    /**
     * 日志操作类型常量描述：20-提交；21-暂存；22-审批通过；23-删除；24-审批打回; 25-修改
     */
    public final static String EVALUATION_LOG_TYPE_SUBMIT_DESCRIPTION = "提交";
    public final static String EVALUATION_LOG_TYPE_TEMP_DESCRIPTION = "暂存";
    public final static String EVALUATION_LOG_TYPE_APPROVED_DESCRIPTION = "审批通过";
    public final static String EVALUATION_LOG_TYPE_DEL_DESCRIPTION = "删除";
    public final static String EVALUATION_LOG_TYPE_APPROVAL_RETURNED_DESCRIPTION = "审批打回";
    public final static String EVALUATION_LOG_TYPE_MODIFY_DESCRIPTION = "修改审批";

    public final static String INSTITUTIONAL_MANAGEMENT = "机构管理";

    public final static String ADD_DESCRIPTION = "新增";
    public final static String UPDATE_DESCRIPTION = "修改";
    public final static String DELETED_DESCRIPTION = "删除";

    //新增提交
    public final static String EVALUATION_LOG_LANG_ADDSUBMIT = "table.evaluation.log.addSubmit";
    //编辑提交
    public final static String EVALUATION_LOG_LANG_EDITSUBMIT = "table.evaluation.log.editSubmit";
    //新增暂存
    public final static String EVALUATION_LOG_LANG_ADDTEMP = "table.evaluation.log.addTemp";
    //编辑暂存
    public final static String EVALUATION_LOG_LANG_EDITTEMP = "table.evaluation.log.editTemp";
    //任务审批打回
    public final static String EVALUATION_LOG_LANG_TASKBACK = "table.evaluation.log.taskBack";
    //任务审批通过
    public final static String EVALUATION_LOG_LANG_TASKAPPROVAL = "table.evaluation.log.taskApproval";
    //任务删除
    public final static String EVALUATION_LOG_LANG_TASKDEL = "table.evaluation.log.taskDel";
    //机构管理新增暂存
    public final static String EVALUATION_LOG_LANG_ORGADDTEMP = "table.evaluation.log.orgAddTemp";
    //机构管理新增提交
    public final static String EVALUATION_LOG_LANG_ORGADDSUBMIT = "table.evaluation.log.orgAddSubmit";
    //机构管理修改暂存
    public final static String EVALUATION_LOG_LANG_ORGEDITTEMP = "table.evaluation.log.orgEditTemp";
    //机构管理修改审批
    public final static String EVALUATION_LOG_LANG_ORGEDITSUBMIT = "table.evaluation.log.orgEditSubmit";
    //机构管理删除提交
    public final static String EVALUATION_LOG_LANG_ORGDELSUBMIT = "table.evaluation.log.orgDelSubmit";
    //机构管理新增审批通过
    public final static String EVALUATION_LOG_LANG_ORGADDAPPROVAL = "table.evaluation.log.orgAddApproval";
    //机构管理修改审批通过
    public final static String EVALUATION_LOG_LANG_ORGEDITAPPROVAL = "table.evaluation.log.orgEditApproval";
    //机构管理审批打回
    public final static String EVALUATION_LOG_LANG_ORGAPPROVALBACK = "table.evaluation.log.orgApprovalBack";
    //审批通过
    public final static String EVALUATION_LOG_LANG_APPROVAL = "table.evaluation.log.approval";
    //审批打回
    public final static String EVALUATION_LOG_LANG_APPROVALBACK = "table.evaluation.log.approvalBack";
    //评价提交
    public final static String EVALUATION_LOG_LANG_EVALUATIONSUBMIT = "table.evaluation.log.evaluationSubmit";
    //评价暂存
    public final static String EVALUATION_LOG_LANG_EVALUATIONTEMP = "table.evaluation.log.evaluationTemp";

    /**
     * 贷款余额之和
     */
    public final static String FINANCING_TYPE_1="1";
    /**
     * 发放额度合计
     */
    public final static String FINANCING_TYPE_2="2";
    /**
     * 发放利率平均值
     */
    public final static String FINANCING_TYPE_3="3";

    public final static BigDecimal maxLimit= new BigDecimal("99999999999999");
    public final static BigDecimal minLimit= new BigDecimal("-99999999999999");

}