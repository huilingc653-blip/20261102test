package com.bytter.finance.domain.provide.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.vo.GuaContractCmsProvideLoanVO;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.LoanQueryVO;
import com.bytter.finance.domain.provide.vo.MonthlyLoanVO;
import com.bytter.finance.dto.GuaContractCmsProvideLoanDTO;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 借款信息表
 * @date 2017-07-20 10:45:09
 */
@Repository
public class LoanInfoDao extends BaseDao<CmsProvideLoanInfo, String> {
    private void querSql(DataTablesInput dataTablesInput, StringBuilder sql, Map<String, Object> params) {
        //TODO 去掉多此一举
        sql.append("select info.*,ctype.REPAY_NAME as  repayTypeName1");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" left join CMS_REPAY_TYPE ctype on info.REPAY_TYPE=ctype.id");
        sql.append(getWhereCondition(dataTablesInput, new LoanInfoVO(), null, false));
        params.putAll(dataTablesInput.getSearchValues());
    }
    /**
     * 功能介绍 : 修改列表查询<br>
     */
    public DataTablesOutput getDataUpdateTables(DataTablesInput dataTablesInput, String userName,
                                                List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select info.*,repayType.repay_name repayTypeName1 ");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" left join CMS_REPAY_TYPE repayType on info.repay_type = repayType.id");
        sql.append(" where 1=1 ");
        sql.append(" and info.status in(0 ,-1,11) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        sql.append(" and ( info.create_by = '" + userName + "' or info.create_by is null or  info.create_by = '' ) ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanInfoVO.class, null, false);
    }
    /**
     * 功能介绍 : 修改列表查询<br>
     */
    public DataTablesOutput getChangeDataTable(DataTablesInput dataTablesInput, String userName,
                                               List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        OrderString orderString = new OrderString("info.create_date", "desc");
        sql.append("select info.* ");
        sql.append(" from cms_provide_loan_info info ");
        //TODO 已有
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" where 1=1 ");
        sql.append(" and info.status in(" + CmsConstant.PREPARESEND + ") ");
        sql.append(" and (").append(getSQLIn(corpAuth, 900, "info.corp_id") + ") ");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanInfoVO.class, null, false);
    }
    /**
     * 功能介绍 : 借款利率调整查询<br>
     */
    public DataTablesOutput getDataLoanRateTables(DataTablesInput dataTablesInput, List<String> corpAuth) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        this.querSql(dataTablesInput, sql, params);
        sql.append(" where 1=1 ");
        sql.append(" and info.status =:status and info.float_way=:floatway");
        sql.append(" and ctype.CAPITAL_AND_INTEREST <> '3'");
        params.put("status", CmsConstant.COMPELETOTAL);
        params.put("floatway", CmsConstant.FLOAT_WAY_01);
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LoanInfoVO.class, "info", false,
                DynamicallyObject.getInstance(params), null);
    }
    /**
     * 功能介绍 : 申请审批列表表查询<br>
     */
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        this.querSql(dataTablesInput, sql, params);
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "info.id"));
        }
        /*
         * sql.append(" and info.status in(:status) "); List<Integer> statues = new ArrayList<Integer>();
         * statues.add(CmsConstant.COMMITAPPROVE); params.put("status", statues);
         */
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LoanInfoVO.class, "info", false,
                DynamicallyObject.getInstance(params), null);
        // return getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params,LoanInfoVO.class);
    }
    /**
     * 功能介绍 : 修改补录查询<br>
     */
    public DataTablesOutput getDataRepairTables(DataTablesInput dataTablesInput, String userName,
                                                List<String> corpAuth) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select info.*, ");
        sql.append(" repayType.repay_name repayTypeName1 ");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" left join CMS_REPAY_TYPE repayType on info.REPAY_TYPE = repayType.id ");
        sql.append(" where 1=1 ");
        sql.append(" and info.status in(95) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        // sql.append(" and info.create_by = '"+ userName +"'");
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanInfoVO.class, null, false);
    }
    /**
     * 功能介绍 : 补录审批查询列表<br>
     */
    public DataTablesOutput getDataRepairApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        this.querSql(dataTablesInput, sql, params);
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "info.id"));
        }
        /*
         * sql.append(" and info.status in(:status) "); List<Integer> statues = new ArrayList<Integer>();
         * statues.add(CmsConstant.REPAIRAPPRAVE); params.put("status", statues);
         */
        // return getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params,LoanInfoVO.class);
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LoanInfoVO.class, "info", false,
                DynamicallyObject.getInstance(params), null);
    }
    /**
     * 功能介绍 : 根据billCode查询LoanInfo对象<br>
     */
    public CmsProvideLoanInfo getLoanInfoByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from CmsProvideLoanInfo where billCode=:billCode";
        params.put("billCode", billCode);
        return (CmsProvideLoanInfo) createQuery(hql, params).uniqueResult();
    }
    /**
     * 功能介绍 : 根据ID查询LoanInfoVO对象<br>
     */
    public LoanInfoVO getLoanInfoVOById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select info.*, ");
        //TODO 名称已经包含
//        sql.append(" info.BANK_NAME bankName, ");
        sql.append(" type.name as plClassName");
        sql.append(" from cms_provide_loan_info info  ");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class and type.status!=-2 and type='"
                + BaseDataConstant.TYPE_BORROW + "' ");
        sql.append(" where info.id=:id");
        params.put("id", id);
        LoanInfoVO vo = this.getBySQLAliasToBean(sql.toString(), params, LoanInfoVO.class);
        return vo;
    }
    /**
     * 获取挂接授信的借款ID集合
     * @return
     */
    public List<String> getIdListByRat() {
        return createSQLQuery("select id from cms_provide_loan_info where is_hang_guarantee = '1'").list();
    }
    /**
     * 功能介绍 : 根据BillCOde查询LoanInfoVO对象<br>
     */
    public LoanInfoVO getLoanInfoVOByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
//        sql.append("select info.*,info.bank_name as bankName,info.bank_type_name as bankTypeName, ");
        sql.append("select info.*, ");
        sql.append(" repayType.repay_name repayTypeName1,repayType.repay_name repayTypeName,accrualType.accrual_name accrualTypeName,");
        sql.append(" type.id as plClassId,type.name as plClassName,");
        sql.append(" info.REPAY_CYCLE_NAME as repayCycleName ");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class and type.status!=-2 and type='"
                + BaseDataConstant.TYPE_BORROW + "' ");
        //TODO 程序中已补充
//        sql.append(" left join tb_base_rate base on base.id = info.reference_rate_type ");// 基准利率类型
        sql.append(" left join CMS_REPAY_TYPE repayType on info.repay_type = repayType.id");
        sql.append(" left join CMS_ACCRUAL_TYPE accrualType on info.ACCRUAL_TYPE = accrualType.id");
        sql.append(" where info.bill_code=:billCode ");
        params.put("billCode", billCode);
        LoanInfoVO vo = this.getBySQLAliasToBean(sql.toString(), params, LoanInfoVO.class);
         return vo;
    }
    /**
     * 功能介绍 : 根据BillCOde查询LoanInfoVO对象<br>
     */
    public LoanInfoVO getLoanInfoVOBySendBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
//        sql.append("select info.*,info.bank_name as bankName,info.bank_type_name as bankTypeName,type.name as plClassName,base.base_type_name as referenceRateTypeName,info.REPAY_CYCLE_NAME as repayCycleName ");
        sql.append("select info.*,type.name as plClassName,info.REPAY_CYCLE_NAME as repayCycleName ");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class and type.status!=-2 and type='" + BaseDataConstant.TYPE_BORROW + "' ");
        //TODO 程序已补充
//        sql.append(" left join tb_base_rate base on base.id = info.reference_rate_type ");// 基准利率类型
        sql.append(" left join cms_provide_loan_send send on send.loan_code = info.bill_code ");
        sql.append(" where send.bill_code=:billCode ");
        params.put("billCode", billCode);
        LoanInfoVO vo = this.getBySQLAliasToBean(sql.toString(), params, LoanInfoVO.class);
        return vo;
    }
    /**
     * 功能介绍 : 判断借款合同号是否重复<br>
     *
     * @return 重复：true 否则false
     */
    public boolean checkContractNoRepeat(CmsProvideLoanInfo loan) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select BT_NVL_NUM(count(info.contract_no),0) as cou from cms_provide_loan_info info ");
        sql.append(" where info.id <> :id and info.contract_no = :contractNo ");
        params.put("id", loan.getId());
        params.put("contractNo", loan.getContractNo());
        Map rs = getBySQL(sql.toString(), params);
        if (rs == null) {
            return false;
        }
        BigDecimal count = new BigDecimal(rs.get("cou") == null ? "0" : rs.get("cou").toString());
        return count.compareTo(BigDecimal.ZERO) != 0;
    }
    /**
     * 功能介绍 : 借款查询<br>
     */
    public DataTablesOutput queryLoanForMobile(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
//        sql.append("select bank.bank_name as bankName,info.corp_id as corpId,info.bill_code as infoBillCode,");
        //TODO 录入获取
        sql.append("select info.bank_name as bankName,info.corp_id as corpId,info.bill_code as infoBillCode,");
        sql.append(" send.bill_code as sendBillCode,info.pl_account as plAccount,send.contract_code as contractCode,");
        sql.append(" info.con_id as conId,");
        sql.append(" info.RMK as rmk,");
        sql.append(" info.create_name createName, ");
        sql.append(
                " info.create_date as createDate,info.cur_id as curId,info.pl_money as plMoney,send.money as sendMoney,");
        //TODO 补充名称信息
        sql.append(" info.corp_name as corpName,info.cur_name as curName, ");
        sql.append(" repay.sum_repay_money as sumRepayMoney,");
        sql.append(
                " (BT_NVL_NUM(send.money,0) - BT_NVL_NUM(repay.sum_repay_money,0) - BT_NVL_NUM(repayPassage.sum_repay_passage_money,0)) as send_balance_money,");
        sql.append(" BT_NVL_NUM(repayPassage.sum_repay_passage_money,0) as sum_repay_passage_money,");
        sql.append(
                " BT_NVL_NUM(send.pl_rate,info.pl_rate) as plRate,send.send_date as sendDate,send.repay_date as repayDate,");
        sql.append(" repay.repay_date as upRepayDate,send.repay_type as repayType,type.name as plClassName,");
        sql.append(" send.pl_term as plTerm,BT_NVL_NUM(dc.deferred_count,0) as deferred_count, ");
        sql.append(" info.tern_unit as ternUnit,info.consultant_fee as consultantFee,info.status as infoStatus, ");
        sql.append(" send.status as sendStatus,BT_NVL_CHAR(abolish.biz_status,info.biz_status) as infoBizStatus,");
        sql.append(" BT_NVL_CHAR(abolish.biz_status,send.biz_status) as sendBizStatus");
        sql.append(" from cms_provide_loan_info info");
        sql.append(" left join cms_provide_loan_send send on send.loan_code = info.bill_code");
        sql.append(
                " left join (select send_code,sum(repay_money) as sum_repay_money,max(repay_date) as repay_date from cms_provide_loan_repay where status >= 102 group by send_code) repay on repay.send_code = send.bill_code");
        sql.append(
                " left join (select send_code,sum(repay_money) as sum_repay_passage_money from cms_provide_loan_repay where status < 102 and status >= -1 group by send_code) repayPassage on repayPassage.send_code = send.bill_code");
        sql.append(
                " left join (select li_code as send_code,count(*) as deferred_count from cms_provide_loan_deferred where status != -2 group by li_code ) dc on dc.send_code = send.bill_code ");
        //TODO 录入从bt_bank 获取的
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class and type.status!=-2 and type='"
                + BaseDataConstant.TYPE_BORROW + "'");
        sql.append(
                " left join cms_provide_loan_abolish abolish on abolish.pli_code = info.bill_code and abolish.status > 0 and abolish.status < 110");
        sql.append(" where 1=1 ");
        sql.append(" and send.repay_date >= bt_to_date('" + DateUtils.getDate("yyyy-MM-dd") + "','yyyy-mm-dd')");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanQueryVO.class, null, false);
    }
    /**
     * 功能介绍 : 借款查询<br>
     */
    public DataTablesOutput queryLoan(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
//        sql.append("select bank.bank_name as bankName,info.corp_id as corpId,info.bill_code as infoBillCode,");
        //TODO 录入获取
        sql.append("select info.bank_name as bankName,info.corp_id as corpId,info.bill_code as infoBillCode,");
        sql.append(" send.bill_code as sendBillCode,info.pl_account as plAccount,send.contract_code as contractCode,");
        sql.append(" info.con_id as conId,");
        sql.append(" info.RMK as rmk,");
        sql.append(" info.create_name createName, ");
        sql.append(
                " info.create_date as createDate,info.cur_id as curId,info.pl_money as plMoney,send.money as sendMoney,");
        //TODO 补充名称信息
        sql.append(" info.corp_name as corpName,info.cur_name as curName, ");
        sql.append(" repay.sum_repay_money as sumRepayMoney,");
        sql.append(
                " (BT_NVL_NUM(send.money,0) - BT_NVL_NUM(repay.sum_repay_money,0) - BT_NVL_NUM(repayPassage.sum_repay_passage_money,0)) as send_balance_money,");
        sql.append(" BT_NVL_NUM(repayPassage.sum_repay_passage_money,0) as sum_repay_passage_money,");
        sql.append(
                " BT_NVL_NUM(send.pl_rate,info.pl_rate) as plRate,send.send_date as sendDate,send.repay_date as repayDate,");
        sql.append(" repay.repay_date as upRepayDate,send.repay_type as repayType,type.name as plClassName,");
        sql.append(" send.pl_term as plTerm,BT_NVL_NUM(dc.deferred_count,0) as deferred_count, ");
        sql.append(" info.tern_unit as ternUnit,info.consultant_fee as consultantFee,info.status as infoStatus, ");
        sql.append(" send.status as sendStatus,BT_NVL_CHAR(abolish.biz_status,info.biz_status) as infoBizStatus,");
        sql.append(" BT_NVL_CHAR(abolish.biz_status,send.biz_status) as sendBizStatus");
        sql.append(" from cms_provide_loan_info info");
        sql.append(" left join cms_provide_loan_send send on send.loan_code = info.bill_code");
        sql.append(
                " left join (select send_code,sum(repay_money) as sum_repay_money,max(repay_date) as repay_date from cms_provide_loan_repay where status >= 102 group by send_code) repay on repay.send_code = send.bill_code");
        sql.append(
                " left join (select send_code,sum(repay_money) as sum_repay_passage_money from cms_provide_loan_repay where status < 102 and status >= -1 group by send_code) repayPassage on repayPassage.send_code = send.bill_code");
        sql.append(
                " left join (select li_code as send_code,count(*) as deferred_count from cms_provide_loan_deferred where status != -2 group by li_code ) dc on dc.send_code = send.bill_code ");
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class and type.status!=-2 and type='"
                + BaseDataConstant.TYPE_BORROW + "'");
        sql.append(
                " left join cms_provide_loan_abolish abolish on abolish.pli_code = info.bill_code and abolish.status > 0 and abolish.status < 110");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        Map<String, Object> params = dataTablesInput.getFormdata();
        if (null != params.get("status") && StringUtils.isNotEmpty(params.get("status").toString())) {
            List<String> bizStatus = Arrays.asList(params.get("status").toString().split(","));
            if (ListUtils.isNotNullOrEmpty(bizStatus)) {
                sql.append(" and (");
                // sql.append(getSQLIn(bizStatus,900,"bt_nvl(bt_nvl(abolish.biz_status,send.biz_status),info.biz_status)"));
                sql.append(getSQLIn(bizStatus,900,"CASE WHEN abolish.biz_status IS NOT NULL THEN abolish.biz_status  WHEN send.biz_status IS NOT NULL  THEN send.biz_status ELSE info.biz_status END"));
                sql.append(") ");
            }
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanQueryVO.class, null, false);
    }
    /**
     * 功能介绍 : 查询借款补录完成的数据
     */
    public DataTablesOutput queryLoanInfoData(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
//        sql.append(" select info.id,bank.bank_name as bankName, ").append(" info.corp_id as corpId, ")
        sql.append(" select info.id,info.bank_name as bankName, ").append(" info.corp_id as corpId, ")
                .append(" info.bill_code as infoBillCode, ").append(" send.bill_code as sendBillCode, ")
                .append(" info.contract_no as contractCode, ").append(" info.create_date as createDate, ")
                .append(" info.cur_id as curId, ").append(" info.pl_money as plMoney, ")
                //TODO 补充名称信息
                .append(" info.corp_name as corpName,info.cur_name as curName, ")
                .append(" send.money as sendMoney, ").append(" BT_NVL_NUM(send.pl_rate, info.pl_rate) as plRate, ")
                .append(" send.send_date as sendDate, ").append(" send.repay_date as repayDate, ")
                .append(" send.repay_type as repayType, ").append(" info.status as infoStatus, ")
                .append(" send.status as sendStatus, ").append("  info.pl_date as plDate, ")
                .append("  info.repay_date as plReDate ").append(" from cms_provide_loan_info info ")
                .append(" left join cms_provide_loan_send send ").append(" on send.loan_code = info.bill_code ")
//                .append(" left join bt_bank bank ").append(" on bank.sys_bank_code = info.bank_id ")
                .append(" where 1 = 1 and info.status>=96 and info.status<110 ");
        if (StringUtils.isNotEmpty(corpAuth)) {
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanQueryVO.class, null, false);
    }
    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String status, String bizStatus) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(*) from cms_provide_loan_info where 1=1 and corp_id=:corpId ");
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in (:status) ");
            params.put("status", status.split(","));
        } else {
            sql.append(" and status<>-2 ");
        }
        if (EmptyUtils.isNotEmpty(bizStatus)) {
            sql.append(" and biz_status in (:bizStatus) ");
            params.put("bizStatus", bizStatus.split(","));
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
    /**
     * 查询所有补录审批通过单未还款完成数据
     *
     * @param input
     * @param ids
     * @return
     */
    public DataTablesOutput listChangeDataTable(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
//        sql.append(" select info.*, info.bank_name as bankName,ctype.REPAY_NAME as  repayTypeName1, ");
        //TODO 录入已获取
        sql.append(" select info.*,ctype.REPAY_NAME as  repayTypeName1, ");
        sql.append("        case when bt_nvl_num(send.send_money ,0) = 0 then '27' ");
        sql.append("         else '42' end as bizStatus");
        sql.append("   from cms_provide_loan_info info ");
        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" left join CMS_REPAY_TYPE ctype on info.REPAY_TYPE=ctype.id");
        sql.append("   left join (select sum(money) send_money, loan_code ");
        sql.append("                from cms_provide_loan_send ");
        sql.append("               where status >= 95 ");
        sql.append("               group by loan_code) send ");
        sql.append("     on info.bill_code = send.loan_code ");
        sql.append("  where info.status in (" + CmsConstant.PREPARESEND + "," + CmsConstant.COMPELETOTAL + ") ");
        sql.append(" and (" + getSQLIn(ids, 900, "info.corp_id") + ") ");
        return getDynamicBySQL(input, sql.toString(), LoanInfoVO.class, "info", false, null);
    }
    //TODO 这个也有问题
    public DataTablesOutput<GuaContractCmsProvideLoanVO> getGuaForCmsProvideLoan(DataTablesInput input, List<String> corpIds, List<String> businessIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select cpli.bill_code as billCode,cpli.id as id,cpli.bank_id as bankId,cpli.bank_name as bankName,cpli.contract_no as contractNo, ");
        sql.append(" cpli.cur_id as curId,cpli.pl_money plMoney,cpli.repay_date as repayDate,cpli.corp_id as CorpId, ");
        //TODO 补充币种单位名称
        sql.append(" cpli.cur_name as curName,cpli.corp_name as CorpName ");
        sql.append(" from cms_provide_loan_info cpli where cpli.is_hang_guarantee = '1' ");
        sql.append(" and (" + getSQLIn(businessIdList, 900, "cpli.id") + ")");
        if (EmptyUtils.isNotEmpty(corpIds)) {
            sql.append(" and " + getSQLIn(corpIds, 900, "cpli.corp_id"));
        } else {
            sql.append(" and 1 <> 1 ");
        }
        return getDynamicBySQL(input, sql.toString(), GuaContractCmsProvideLoanVO.class, "cpli", false);
    }
    public List<GuaContractCmsProvideLoanDTO> getGuaCmsProvideLoan() {
        StringBuilder sql = new StringBuilder();
        sql.append("select cpli.bill_code as billCode, cpli.bank_id as bankId, cpli.bank_name as bankName, cpli.contract_no as contractNo, ");
        sql.append(" cpli.cur_id as curId, cpli.cur_name as curName,  cpli.pl_money plMoney,cpli.repay_date as repayDate, ");
        sql.append(" cpli.id as id ");
        sql.append(" from  cms_provide_loan_info cpli ");
        sql.append(" where 1=1 and cpli.is_hang_guarantee = '1'");
        return this.listBySQLAliasToBean(sql.toString(), null, GuaContractCmsProvideLoanDTO.class);
    }

    /**
     * 功能介绍 : 月度贷款明细表 - 数据
     */
    public List<MonthlyLoanVO> queryMonthlyLoanDtl(MonthlyLoanVO queryParam, List<String> corpAuth) {
        Map<String,Object> params = new HashMap<>();

        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append("cell.cellName,corp.ID corpId,corp.NAME corpName,corp.code as corpCode,info.PL_CLASS as plClass,clt.NAME plClassName,bank.BANK_NAME bankName,");
        sql.append("send.ONLINE_PAYMENT_ACC sendAccNo,send.PL_TERM plTerm,info.tern_unit ternUnit,bt_nvl_num(send.MONEY, 0) as sendMoney,");
        // T25020034019【紧急】月度贷款明细表新增“已还金额” 和“待还金额”。（针对本金）
        sql.append(" bt_nvl_num(repay.sum_repay_money, 0) as sumRepayMoney, bt_nvl_num((bt_nvl_num(send.MONEY, 0) - bt_nvl_num(repay.sum_repay_money, 0)), 0) as sumUnRepayMoney, ");
        sql.append(" BT_TO_CHAR(send.SEND_DATE, 'yyyy-MM-dd') sendDate,BT_TO_CHAR(send.REPAY_DATE, 'yyyy-MM-dd') repayDateStr,");
        sql.append("send.PL_RATE plRate,info.rmk,u.USERNAME sendUser, ");
        sql.append("case when cplr.latestRate is not null then cplr.latestRate else send.PL_RATE end as latestRate ");
        sql.append("FROM sys_corp corp ");
        //sql.append("LEFT JOIN sys_corp p ON corp.PARENT_ID = p.ID  ");// AND p.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000')
        // 关联大区
        sql.append("LEFT JOIN ( " );
        sql.append("    SELECT c.id,c.NAME cellName ");
        sql.append("    FROM sys_corp c ");
        sql.append("    WHERE c.NET_ID = (SELECT id FROM sys_corp WHERE CODE = 100) ");
        sql.append("    AND c.UNIT_CM = (SELECT id FROM sys_cell_manage WHERE cell_code = '6000') ");
        sql.append(") AS cell ON FIND_IN_SET(cell.id, corp.PARENT_IDS) > 0 ");
        sql.append("JOIN cms_provide_loan_info info ON corp.ID = info.CORP_ID ");
        sql.append("LEFT JOIN cms_loan_type clt ON info.PL_CLASS = clt.CODE and clt.TYPE = ' " + BaseDataConstant.TYPE_BORROW + " ' ");
        sql.append("LEFT JOIN bt_bank bank ON info.BANK_ID = bank.SYS_BANK_CODE ");
        sql.append("JOIN cms_provide_loan_send send ON info.bill_code = send.loan_code ");
        sql.append(" left join (select send_code, sum(repay_money) as sum_repay_money, max(repay_date) as repay_date from cms_provide_loan_repay where status >= 102 group by send_code) ");
        sql.append(" repay on repay.send_code = send.bill_code ");
        sql.append("LEFT JOIN sys_user u ON send.CREATE_BY = u.LOGIN_NAME ");
        sql.append("left join (  select bill_code,pl_rate latestRate,ENTER_DATE, ROW_NUMBER() OVER (PARTITION BY bill_code ORDER BY ENTER_DATE desc) as rn ");
        sql.append(" from cms_provide_loan_rate where 1=1 ");
        if(null != queryParam.getEndRepayDate()){
            params.put("endDate", DateUtils.format(queryParam.getEndRepayDate(), "yyyy-MM-dd"));
            sql.append(" and ENTER_DATE <= bt_to_date(:endDate,'yyyy-MM-dd') ");
        }else{
            //未选日期范围，默认查小于当前日期的最新利率
            params.put("endDate", DateUtils.formatDate());
            sql.append(" and ENTER_DATE <= bt_to_date(:endDate,'yyyy-MM-dd') ");
        }
        sql.append(" ) cplr on cplr.bill_code =  info.id and cplr.rn = 1 ");
        sql.append("WHERE send.`STATUS` >= 95  ");
        sql.append(" and (send.MONEY - bt_nvl_num(repay.sum_repay_money, 0)) > 0 ");
        sql.append(" and corp.NET_ID = (SELECT id FROM sys_corp WHERE CODE = '100') ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));

        // 申请单位
        if (StringUtils.isNotEmpty(queryParam.getCorpId())) {
            List<String> corpIdList = Arrays.asList(queryParam.getCorpId().split(CommonConstant.COMMA_SPLIT));
            sql.append(" and ").append(getSQLIn(corpIdList, 900, "info.corp_id"));
        }
        // 银行账号
        if (StringUtils.isNotEmpty(queryParam.getSendAccNo())) {
            sql.append(" and send.ONLINE_PAYMENT_ACC = :onlinePaymentAcc ");
            params.put("onlinePaymentAcc", queryParam.getSendAccNo());
        }
        // 到期日期开始
        if (!ObjectUtils.isEmpty(queryParam.getStartRepayDate())) {
            sql.append(" and send.REPAY_DATE >= bt_to_date(:startDate, 'yyyy-MM-dd') ");
            params.put("startDate", DateUtils.format(queryParam.getStartRepayDate(), "yyyy-MM-dd"));
        }
        // 到期日期结束
        if (!ObjectUtils.isEmpty(queryParam.getEndRepayDate())) {
            sql.append(" and send.REPAY_DATE <= bt_to_date(:endDate, 'yyyy-MM-dd') ");
            params.put("endDate", DateUtils.format(queryParam.getEndRepayDate(), "yyyy-MM-dd"));
        }
        // 贷款品种
        if (StringUtils.isNotEmpty(queryParam.getPlClass()) && StringUtils.isNotEmpty(queryParam.getPlClass().trim())) {
            sql.append(" and info.PL_CLASS = :plClass ");
            params.put("plClass", queryParam.getPlClass());
        }
        return listBySQLAliasToBean(sql.toString(), params, MonthlyLoanVO.class);
    }
}
