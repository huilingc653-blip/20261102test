package com.bytter.report.domain.cms.dao;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.report.domain.cms.entity.CmsPrecativeLoanRepay;
import com.bytter.report.domain.cms.vo.CmsPrecativeLoanRepayVo;
import com.bytter.finance.dto.CmsPrecativeLoanRepayDTO;
import com.bytter.finance.enums.PrecativeMessageEnum;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 委托贷款还款信息表
 * @author onlineGenerator
 * @date 2017-08-02 14:05:07
 * @version 1.0
 *
 */
@Repository
public class CmsPrecativeLoanRepayDao extends BaseDao<CmsPrecativeLoanRepay, String> {
    /***
     * 查询归还列表
     */
    public DataTablesOutput getLoanRepay(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select b.bill_Code as infoBillCode , a.bill_code as sendCode ,a.contract_Code as contractCode , a.precative_Id as precativeId , ");
        sb.append(
                " b.loan_Opp as corpId, a.term as term , a.tern_unit as ternUnit ,b.pl_type as plType , a.status as status , b.loan_Send_Out_Type as loanSendOutType ,");
        sb.append(
                " a.money as sendMoney, b.cur_id as curId , a.send_Date as sendDate ,a.deadline_Date as deadlineDate , ");
        //TODO 补充名称相关信息
        sb.append(
                " b.cur_name as curName,b.precative_name as precativeName,b.loanopp_name as loanOppName,b.corp_name as corpName, ");
        sb.append(
                " a.biz_status as bizStatus , a.repay_Type as repayWay ,a.interest_Way as interestWay , (a.money - BT_NVL_NUM(d.sumMoney,0)-BT_NVL_NUM(c.repayMoney,0)) as sumMoney");
        sb.append(
                " from cms_precative_loan_send a inner join cms_precative_loan_info b on a.father_code = b.bill_code ");
        sb.append(
                " left join (select sum(repay_money) as repayMoney, send_code from cms_precative_loan_repay where status <> -2 group by send_code ) c ");
        sb.append(" on a.bill_code = c.send_code ");
        sb.append(
                " left join (select sum(repay_money) as sumMoney, send_code from cms_precative_loan_repay where repay_type !=4  and status <> -2 group by send_code ) d ");
        sb.append(" on a.bill_code = d.send_code ");
        sb.append(" where a.status in (102,103,104)  and (c.repayMoney is null or c.repayMoney < a.money) ");
        sb.append(
                " and not exists(select 1 from cms_precative_loan_repay plr where  plr.send_code = a.bill_code  and (plr.status = 0 or plr.status = 11  or plr.status = -1 or plr.status = 95) ) ");
        sb.append(" and ").append(getSQLIn(corpAuth, 900, "b.loan_opp"));
        return getDynamicBySQL(input, sb.toString(), CmsPrecativeLoanRepayVo.class, null, false);
    }
    /***
     * 查询贷款还款计划
     */
    public DataTablesOutput dataRepayPlanable(DataTablesInput input) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select b.bill_Code as infoBillCode , a.id as sendId , a.deferred_date as deferredrepaydate, a.deferred_rate as deferredRate, a.bill_code as sendCode ,a.contract_Code as contractCode , a.precative_Id as precativeId , ");
        sb.append(
                " b.loan_Opp as corpId, a.term as term , a.tern_unit as ternUnit ,b.pl_type as plType , a.status as status , b.loan_Send_Out_Type as loanSendOutType ,");
        //TODO 补充名称相关信息
        sb.append(" b.cur_name as curName,b.precative_name as precativeName,b.loanopp_name as loanOppName,b.corp_name as corpName,");
        sb.append(
                " a.money as sendMoney, b.cur_id as curId , a.send_Date as sendDate ,a.deadline_Date as deadlineDate ,  a.biz_status as bizStatus , a.repay_Type as repayWay ,a.interest_Way as interestWay");
        sb.append(
                " from cms_precative_loan_send a inner join cms_precative_loan_info b on a.father_code = b.bill_code ");
        // sb.append(" left join cms_precative_loan_deferred d on a.bill_code=d.li_code ");
        sb.append(
                " left join (select sum(repay_money) as repayMoney, send_code from cms_precative_loan_repay   where status <> -2 group by send_code ) c ");
        sb.append(
                " on a.bill_code = c.send_code  where a.status in (102,103,104)  and (c.repayMoney is null or c.repayMoney < a.money) ");
        sb.append(
                " and not exists(select 1 from cms_precative_loan_repay plr where  plr.send_code = a.bill_code  and (plr.status = 0 or plr.status = -1) ) ");
        return getDynamicBySQL(input, sb.toString(), CmsPrecativeLoanRepayVo.class, null, false);
    }
    /**
     * 查询还款账务数据
     */
    public DataTablesOutput getPrecativeRepayDataTables(DataTablesInput dataTablesInput, List<String> corpAuth,
            String opType, ICurrentUser user) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select r.rmk as rmk, r.id as id, r.bill_Code as billCode, a.repay_Type as repayWay,a.send_Date as sendDate,a.deadline_Date as deadlineDate, ");
        sb.append(
                " a.interest_Way as interestWay, r.send_code as sendCode, r.create_date as createDate, b.bill_code as infoBillCode, ");
        //TODO 补充名称相关信息
        sb.append(" b.cur_name as curName,b.precative_name as precativeName,b.loanopp_name as loanOppName,b.corp_name as corpName,");
        sb.append(
                " a.contract_Code as contractCode, b.cur_id as curId, b.precative_id as precativeId,b.purpose as purpose, a.money as sendMoney,r.repay_money as repayMoney , r.corp_id as corpId,  r.repay_date as repayDate, ");
        sb.append(
                " r.status as status, r.biz_status as bizStatus, r.tally_sign as tallySign,b.loan_Send_Out_Type as loanSendOutType ");
        sb.append(
                " from cms_precative_loan_repay r left join cms_precative_loan_send a on r.send_code = a.bill_Code  left join cms_precative_loan_info b  on a.father_code = b.bill_code ");
        sb.append(" where 1= 1 ");
        if (FinacialManualConstant.MANUAL_STATUS_INIT.equals(opType)) {
            sb.append(
                    " and r.status = 97 and (r.tally_Sign ='' or r.tally_Sign ='0' or r.tally_Sign ='6' or r.tally_sign is null)");
        } else if (FinacialManualConstant.MANUAL_STATUS_EDIT.equals(opType)) {
            sb.append(" and r.status = 97 and (r.tally_Sign in (5,4,3)) and r.tally_code = '").append(user.getId())
                    .append("'");
        } else if (FinacialManualConstant.MANUAL_STATUS_CHECK.equals(opType)) {
            sb.append(" and r.status = 97 and (r.tally_Sign ='3') and r.tally_code != '").append(user.getId())
                    .append("'");
        } else {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_TALLYDATA_ERROR);
        }
        return getDynamicBySQL(dataTablesInput, sb.toString(), CmsPrecativeLoanRepayVo.class, null, false);
    }
    /***
     * 查询归还修改打回列表
     */
    public DataTablesOutput getEditLoanRepay(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select r.id as id, r.bill_Code as billCode, a.repay_Type as repayWay,a.send_Date as sendDate,a.deadline_Date as deadlineDate, ");
        sb.append(
                " a.interest_Way as interestWay, r.send_code as sendCode, r.create_date as createDate, b.bill_code as infoBillCode, ");
        //TODO 补充名称相关信息
        sb.append(
                " b.cur_name as curName,b.precative_name as precativeName,b.loanopp_name as loanOppName,b.corp_name as corpName, ");
        sb.append(
                " a.contract_Code as contractCode, b.cur_id as curId,  r.repay_money as repayMoney , r.corp_id as corpId,  r.repay_date as repayDate, ");
        sb.append(" r.status as status, r.biz_status as bizStatus, b.loan_Send_Out_Type as loanSendOutType ");
        sb.append(
                " from cms_precative_loan_repay r left join cms_precative_loan_send a on r.send_code = a.bill_Code  left join cms_precative_loan_info b  on a.father_code = b.bill_code ");
        sb.append(" where 1= 1 ");
        sb.append(" and r.status <> -2 and (r.status = 0 or r.status = -1) ");
        sb.append(" and ").append(getSQLIn(corpAuth, 900, "r.corp_id"));
        return getDynamicBySQL(input, sb.toString(), CmsPrecativeLoanRepayVo.class, null, false);
    }
    /** 查询在途/归还金额 **/
    public CmsPrecativeLoanRepayVo queryPassageMoney(String billCode, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select BT_NVL_NUM(sum(a.repay_money),0) as passageMoney,BT_NVL_NUM(sum(a.REPAY_ACCRUAL),0) as repayAccrual from cms_precative_loan_repay a inner join cms_precative_loan_send b on a.send_code = b.bill_code where 1=1 ");
        if ("passage".equals(type)) { // 在途金额
            sql.append(" and ((a.status != -2) and  a.status < 102) and bt_nvl(a.repay_type,'') <> 4 ");
        } else if ("repay".equals(type)) {/// 已还金额
            sql.append(" and a.status >= 102 and BT_NVL(a.repay_type,'sss') <> '4' ");
        }
        sql.append(" and b.bill_code=:billCode ");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("billCode", billCode);
        return getBySQLAliasToBean(sql.toString(), param, CmsPrecativeLoanRepayVo.class);
    }
    /**
     * 功能介绍 : 查询借款单总共还款金额
     */
    public BigDecimal getInfoRepayMoneySum(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select BT_NVL_NUM(sum(repay.repay_money),0) as SUMMONEY from ");
        sql.append(" cms_precative_loan_send send ");
        sql.append(" left join cms_precative_loan_repay repay on repay.send_code = send.bill_code ");
        sql.append(" where repay.status = :status and send.father_code = :billCode ");
        params.put("status", CmsConstant.COMPELETOTAL);
        params.put("billCode", billCode);
        Map rs = getBySQL(sql.toString(), params);
        if (rs == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(rs.get("SUMMONEY").toString());
    }
    /** 查询在途/已还金额列表 **/
    public List<CmsPrecativeLoanRepayVo> queryRepayList(String billCode, String type) {
        StringBuilder sql = new StringBuilder();
        //TODO 补充名称信息
        sql.append(
                " select a.id as id , a.bill_code as billCode , a.repay_Type as repayType , a.corp_id as corpId , info.corp_name as corpName, a.repay_Date as repayDate ,a.repay_Money as repayMoney , a.contract_Code as contractCode , a.repay_Accrual as repayAccrual ,a.biz_status as bizStatus from cms_precative_loan_repay a ");
        sql.append(" inner join cms_precative_loan_send b on a.send_code = b.bill_code ");
        sql.append(" left join cms_precative_loan_info info  on b.father_code = info.bill_code ");
        if ("passage".equals(type)) { // 在途金额
            sql.append(" where ((a.status <> -2) and  a.status < 102) ");
        } else {/// 已还金额
            sql.append(" where a.status >= 102 ");
        }
        sql.append(" and b.bill_code=:billCode and BT_NVL(a.repay_type,'sss') <> '4'  ");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("billCode", billCode);
        return listBySQLAliasToBean(sql.toString(), param, CmsPrecativeLoanRepayVo.class);
    }
    /***
     * 根据billCode/id 查询归还信息
     */
    public CmsPrecativeLoanRepayVo getRepayByBillCode(String billCode, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from cms_precative_loan_repay a  ");
        if ("id".equals(type)) {
            sb.append(" where a.id=:id  ");
            params.put("id", billCode);
        } else {
            sb.append(" where a.bill_code=:billCode  ");
            params.put("billCode", billCode);
        }
        return this.getBySQLAliasToBean(sb.toString(), params, CmsPrecativeLoanRepayVo.class);
    }
    /**
     * 加载审核列表数据
     * 
     * @param input
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        //TODO 补充名称相关信息
        sql.append("select c.*,d.cur_id,d.cur_name as curName,d.precative_name as precativeName,d.loanopp_name as loanOppName,d.corp_name as corpName from ")
                .append(" (select a.*,b.father_code from cms_precative_loan_repay a ")
                .append(" left join cms_precative_loan_send b on a.send_code=b.bill_code) c ")
                .append(" left join cms_precative_loan_info d on c.father_code=d.bill_code where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "c.id"));
        }
        return getDynamicBySQL(input, sql.toString(), CmsPrecativeLoanRepayVo.class, "", false);
    }
    /**
     * 利息确认列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listRepayInteretAffirm(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select r.id as id , a.id as sendId, b.loan_opp as loanOpp, b.precative_id as precativeId, b.bill_code as infoBillCode , ");
        sql.append(
                " r.bill_code as billCode,a.bill_code as sendCode, r.create_date as createDate, b.cur_id as curId, a.money as sendMoney, ");
        //TODO 补充名称相关信息
        sql.append(
                " b.cur_name as curName,b.precative_name as precativeName,b.loanopp_name as loanOppName,b.corp_name as corpName, ");
        sql.append(
                " r.repay_money as repayMoney, r.repay_accrual as repayAccrual,r.repay_date as repayDate, r.repayway as repayway, r.biz_status as bizStatus ");
        sql.append(" from cms_precative_loan_repay r ");
        sql.append(" left join cms_precative_loan_send a on r.send_code = a.bill_code ");
        sql.append(" left join cms_precative_loan_info b on b.bill_code = a.father_code ");
        sql.append("where r.status = " + CmsConstant.APPROVED);
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "r.corp_id"));
        return getDynamicBySQL(input, sql.toString(), CmsPrecativeLoanRepayVo.class, "", false);
    }
    /**
     * 还款补录列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listRepayAdditional(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select r.id as id , a.id as sendId, b.loan_opp as loanOpp, b.precative_id as precativeId, b.bill_code as infoBillCode , ");
        sql.append(
                " r.bill_code as billCode,a.bill_code as sendCode, r.create_date as createDate, b.cur_id as curId, a.money as sendMoney, ");
        //TODO 补充名称相关信息
        sql.append(" b.cur_name as curName,b.precative_name as precativeName,b.loanopp_name as loanOppName,b.corp_name as corpName, ");
        sql.append(
                " r.repay_money as repayMoney, r.repay_accrual as repayAccrual,r.repay_date as repayDate, r.repayway as repayway, r.biz_status as bizStatus ");
        sql.append(" from cms_precative_loan_repay r ");
        sql.append(" left join cms_precative_loan_send a on r.send_code = a.bill_code ");
        sql.append(" left join cms_precative_loan_info b on b.bill_code = a.father_code ");
        sql.append("where r.status = " + CmsConstant.COMPELETOTAL);
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "r.corp_id"));
        return getDynamicBySQL(input, sql.toString(), CmsPrecativeLoanRepayVo.class, "", false);
    }
    /**
     * 功能介绍 : 查询借款单总共还款金额
     */
    public BigDecimal getPrecativeLoanRepayMoney(String voucherNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select BT_NVL_NUM(repay.repay_money,0) as REPAYMONEY from ");
        sql.append(" cms_precative_loan_repay repay ");
        sql.append(" where repay.voucher_No = :voucherNo ");
        params.put("voucherNo", voucherNo);
        Map rs = getBySQL(sql.toString(), params);
        if (rs == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(rs.get("REPAYMONEY").toString());
    }
    /***
     * 
     * 功能介绍 获取CmsPrecativeLoanRepay实体。<br>
     * <b>日期：</b>2018年3月15日<br>
     * 
     * @version 1.0
     * @author zhengxn
     */
    public CmsPrecativeLoanRepay getRepay(String billCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from cms_precative_loan_repay where bill_Code = :billCode");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("billCode", billCode);
        return getBySQLAliasToBean(sql.toString(), param, CmsPrecativeLoanRepay.class);
    }
    /**
     * 单位校验使用-待记账
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpIdTally(String corpId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(r.id) ");
        sb.append(
                " from cms_precative_loan_repay r left join cms_precative_loan_send a on r.send_code = a.bill_Code  left join cms_precative_loan_info b  on a.father_code = b.bill_code ");
        sb.append(" where 1= 1 ");
        sb.append(
                " and r.status = 97 and (r.tally_Sign ='' or r.tally_Sign ='0' or r.tally_Sign ='6' or r.tally_sign is null)");
        sb.append(" and b.precative_id = :corpId");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sb.toString(), param).uniqueResult().toString());
    }
    /**
     * 单位校验使用 --未还清
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByPrecativeId(String corpId) {
        StringBuilder sql = new StringBuilder();
        // 放款金额-已还款金额
        sql.append(" select sum(BT_NVL_NUM( (s.money - BT_NVL_NUM(repayLoan.repayMoney, 0)), 0)) as repayMoney ");
        sql.append(" from cms_precative_loan_info a ");
        sql.append(" left join cms_precative_loan_send s on a.bill_code = s.father_code ");
        sql.append(" left join (select BT_NVL_NUM(sum(repay_money), 0) repayMoney,clr.send_code ");
        sql.append(
                " from cms_precative_loan_repay clr  where clr.status >= 102  and BT_NVL(clr.repay_type, 'sss') <> '4'  ");
        sql.append(" group by clr.send_code ");
        sql.append(" ) repayLoan on s.bill_code = repayLoan.send_code ");
        sql.append("  where 1 = 1 and s.status =102  and a.status != -2 ");
        sql.append(" and a.PRECATIVE_ID = :corpId");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        CmsPrecativeLoanRepayVo vo = getBySQLAliasToBean(sql.toString(), param, CmsPrecativeLoanRepayVo.class);
        if (null != vo && null != vo.getRepayMoney()) {
            if (vo.getRepayMoney().compareTo(BigDecimal.ZERO) > 0) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }
    /**
     * 单位校验使用 --未还清
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        // 放款金额-已还款金额
        sql.append(" select sum(BT_NVL_NUM( (s.money - BT_NVL_NUM(repayLoan.repayMoney, 0)), 0)) as repayMoney ");
        sql.append(" from cms_precative_loan_info a ");
        sql.append(" left join cms_precative_loan_send s on a.bill_code = s.father_code ");
        sql.append(" left join (select BT_NVL_NUM(sum(repay_money), 0) repayMoney,clr.send_code ");
        sql.append(
                " from cms_precative_loan_repay clr  where clr.status >= 102  and BT_NVL(clr.repay_type, 'sss') <> '4'  ");
        sql.append(" group by clr.send_code ");
        sql.append(" ) repayLoan on s.bill_code = repayLoan.send_code ");
        sql.append("  where 1 = 1 and s.status =102  and a.status != -2 ");
        sql.append(" and a.loan_opp = :corpId");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        CmsPrecativeLoanRepayVo vo = getBySQLAliasToBean(sql.toString(), param, CmsPrecativeLoanRepayVo.class);
        if (null != vo && null != vo.getRepayMoney()) {
            if (vo.getRepayMoney().compareTo(BigDecimal.ZERO) > 0) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }
    /**
     * 单位校验使用 --还款待补录、还款利息待确认
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId1(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from cms_precative_loan_repay where 1 = 1 and status = 102 and corp_id = ?");// 待补录
        Integer num = Integer.valueOf(createSQLQuery(sql.toString(), corpId).uniqueResult().toString());
        String sql1 = "select count(*) from cms_precative_loan_repay r left join cms_precative_loan_send s "
                + " on r.send_code = s.bill_code  left join cms_precative_loan_info i on s.father_code = i.bill_code "
                + " where 1 = 1 and r.status = 95  and (i.precative_id  = ? or i.loan_opp = ? ) ";// 利息待确认
        Integer num1 = Integer.valueOf(createSQLQuery(sql1, corpId, corpId).uniqueResult().toString());
        return num + num1;
    }
    /**
     * 查询还款待记账数据
     */
    public List<CmsPrecativeLoanRepayDTO> queryTallyPrecativeRepay() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select r.id as id,bt_nvl(r.repay_money,0) as repayMoney,bt_nvl(r.repay_accrual,0) as repayAccrual,a.precative_Id as precativeId  ");
        sb.append(" from cms_precative_loan_repay r ");
        sb.append(" left join cms_precative_loan_send a on r.send_code = a.bill_Code ");
        sb.append("  left join cms_precative_loan_info b  on a.father_code = b.bill_code ");
        sb.append(" where 1= 1 ");
        sb.append(
                " and r.status = 97 and (r.tally_Sign ='' or r.tally_Sign ='0' or r.tally_Sign ='6' or r.tally_sign is null)");
        //TODO 还款记账数据变更，在程序中校验了
//        sb.append(" and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = r.id ) ");
        return listBySQLAliasToBean(sb.toString(), null, CmsPrecativeLoanRepayDTO.class);
    }
}
