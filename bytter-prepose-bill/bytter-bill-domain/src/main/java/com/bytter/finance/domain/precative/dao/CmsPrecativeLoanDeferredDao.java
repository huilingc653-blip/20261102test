package com.bytter.finance.domain.precative.dao;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanDeferred;
import com.bytter.finance.domain.precative.vo.PrecativeLoanDeferredVo;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.dto.CmsPrecativeLoanDeferredDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 委托贷款展期申请表
 * @author onlineGenerator
 * @date 2017-08-03 10:48:22
 * @version 1.0
 *
 */
@Repository
public class CmsPrecativeLoanDeferredDao extends BaseDao<CmsPrecativeLoanDeferred, String> {
    /**
     * 功能介绍 委托贷款展期申请table表。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author zhengxn
     */
    public DataTablesOutput queryApply(DataTablesInput input, boolean LoanDfNum, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select i.bill_code as voucherNo,i.precative_id as precativeId , i.corp_id as corpId,");
        sql.append(
                " i.loan_opp as loanOpp , i.cur_id as curId, i.term as term , i.tern_unit as ternUnit, i.money as loanMoney,i.pl_type as plType,");
        //TODO 补充名称相关信息
        sql.append(
                " i.cur_name as curName,i.precative_name as precativeName,i.loanopp_name as loanOppName,i.corp_name as corpName,");
        sql.append(
                " s.bill_code as sendNo,s.contract_code as contractCode, s.money as money ,s.send_date as sendDate,s.status as status,s.biz_Status as bizStatus,");
        sql.append(
                " s.deadline_date as deadlineDate, s.create_date as createDate, BT_NVL_NUM(repay.repay_money, 0) as repayMoney,");
        sql.append(" (select max(d.deferred_start_date)  from cms_precative_loan_deferred d");
        sql.append(" where d.li_code = s.bill_code and d.status <> -2) as deferredStartDate,");// 上次展期开始日期
        sql.append(" (select max(db.deferred_repay_date) from cms_precative_loan_deferred db");
        sql.append(" where db.li_code = s.bill_code and db.deferred_repay_date <> (select max(da.deferred_start_date)");
        sql.append(
                " from cms_precative_loan_deferred da  where da.li_code = s.bill_code  and da.status <> -2)  and db.status <> -2) as deferredRepayDate");// 上次展期到期日期
        sql.append(" from cms_precative_loan_send s ");
        sql.append(" left join cms_precative_loan_info i ");
        sql.append(" on i.bill_code = s.father_code ");
        sql.append(" left join (select lr.SEND_CODE, sum(lr.REPAY_MONEY) repay_money ");
        sql.append(" from CMS_PRECATIVE_LOAN_REPAY lr where lr.status >= 102 group by lr.SEND_CODE) repay ");
        if (LoanDfNum) {
            sql.append(" on s.bill_code = repay.send_code where s.status in(102,104) ");
        } else {
            sql.append(" on s.bill_code = repay.send_code where s.status=102 ");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "i.loan_opp"));
        /*
         * sql.append(" and not exists(select plr.send_code from cms_precative_loan_repay plr where ");
         * sql.append(" plr.send_code = s.bill_code and plr.status >=0 and plr.status< 102) ");
         */
        sql.append(" and ((repay.repay_money is null and s.money > 0) or s.money > repay.repay_money)");
        return getDynamicBySQL(input, sql.toString(), PrecativeLoanDeferredVo.class, "", false);
    }
    /**
     * 功能介绍 委托贷款展期修改/打回table表。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author zhengxn
     */
    public DataTablesOutput queryTable(DataTablesInput input, String pageType, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select d.id as id,i.bill_code as voucherNo,d.bill_code as billCode ,s.bill_code as sendNo,d.expiry_handle_type as expiryHandleType,d.deferred_start_date as deferredStartDate,");
        //TODO 补充名称相关信息
        sql.append(
                " i.cur_name as curName,i.precative_name as precativeName,i.loanopp_name as loanOppName,i.corp_name as corpName, ");
        sql.append(
                " d.deferred_repay_date as deferredRepayDate ,i.loan_opp as loanOpp, d.deferred_limt as deferredLimt,d.tern_unit as ternUnit,d.create_date as createDate,d.status as status,d.biz_status as bizStatus,");
        sql.append(
                " i.precative_id as precativeId , s.send_Date as sendDate ,s.deadline_Date as deadlineDate,s.money as money ");
        sql.append(" from cms_precative_loan_deferred d ");
        sql.append(" left join cms_precative_loan_send s ");
        sql.append(" on d.li_code = s.bill_code ");
        sql.append(" left join cms_precative_loan_info i ");
        sql.append(" on i.bill_code = s.father_code ");
        sql.append(" where 1=1 ");
        if ("listupdate".equals(pageType)) {
            sql.append(" and d.status <= " + CmsConstant.TEMP + " and d.status >= " + CmsConstant.BACK);// 修改/打回
        }
        if ("additional".equals(pageType)) {// 补录
            sql.append(" and d.status =" + CmsConstant.APPROVED);// 审核通过
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "i.loan_opp"));
        return getDynamicBySQL(input, sql.toString(), PrecativeLoanDeferredVo.class, "", false);
    }
    /**
     * 功能介绍 委托贷款展期修改/打回页面明细。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author zhengxn
     */
    public CmsPrecativeLoanDeferred getDeferredByLiCode(String sendNo) {
        return getUniqueBy("liCode", sendNo);
    }
    /**
     * 功能介绍 委托贷款展期修改/打回页面明细。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author zhengxn
     */
    public CmsPrecativeLoanDeferred getDeferredByBillCode(String billCode) {
        return getUniqueBy("billCode", billCode);
    }
    /**
     * 功能介绍 委托贷款展期处理。<br>
     * <b>日期：</b>2017年8月25日<br>
     * 
     * @version 1.0
     * @author zhengxn
     */
    public DataTablesOutput queryHandle(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select i.bill_code as voucherNo , s.bill_code as sendNo , d.bill_code as billCode, i.contract_code as contractCode, i.corp_id as corpId,");
        sql.append(
                " s.deposit_acc as depositAcc, s.loan_acc as loanAcc, i.precative_id as precativeId ,i.loan_opp as loanOpp ,i.cur_id as curId , i.money as money,");
        //TODO 补充名称相关信息
        sql.append(" i.cur_name as curName,i.precative_name as precativeName,i.loanopp_name as loanOppName,i.corp_name as corpName, ");
        sql.append(
                " i.term as term , i.tern_unit as ternUnit, i.start_date as startDate , i.end_date as endDate , d.deferred_repay_date as deferredRepayDate,");
        sql.append(" d.biz_status as bizStatus from cms_precative_loan_deferred d");
        sql.append(" left join cms_precative_loan_send s");
        sql.append(" on d.li_code = s.bill_code");
        sql.append(" left join cms_precative_loan_info i");
        sql.append(" on i.bill_code = s.father_code");
        sql.append(" where d.status = 104 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "i.loan_opp"));
        return getDynamicBySQL(input, sql.toString(), PrecativeLoanDeferredVo.class, "", false);
    }
    /**
     * 功能介绍 委托贷款展期删除。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author zhengxn
     */
    public int deleteDef(CmsPrecativeLoanDeferred deferred) {
        StringBuilder hql = new StringBuilder();
        hql.append("update CmsPrecativeLoanDeferred set status =" + CmsConstant.DELETED + ",biz_status = "
                + CmsConstant.DELETED + "where id=:id");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", deferred.getId());
        // updateSendStatus(deferred,CmsConstant.COMPELETOTAL,null);
        return batchExecute(hql.toString(), param);
    }
    /**
     * 功能介绍 委托贷款展期申请后修改发放表状态。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author zhengxn
     */
    public int updateSendStatus(CmsPrecativeLoanDeferred deferred, Integer status, String bizStatus) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> param = new HashMap<String, Object>();
        hql.append("update CmsPrecativeLoanSend set status=:status,bizStatus=:bizStatus ");
        if (CmsConstant.COMPELTEXTENDATE == status) {
            hql.append(" , deferredDate=:deferredDate , deferred_rate =:deferredRate");
            param.put("deferredDate", deferred.getDeferredRepayDate());
            param.put("deferredRate", deferred.getDeferredRate());
        }
        hql.append(" where 1 = 1 and billCode =:billCode");
        param.put("billCode", deferred.getLiCode());
        param.put("status", status);
        param.put("bizStatus", bizStatus);
        return batchExecute(hql.toString(), param);
    }
    /**
     * 功能介绍 验证展期合同号是否重复。<br>
     * <b>日期：</b>2017年8月3日<br>
     * 
     * @version 1.0
     * @author zhengxn
     */
    public boolean verifyContractCode(String contractCode, String billCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> param = new HashMap<String, Object>();
        sql.append(" select * from cms_precative_loan_deferred d where d.contract_code =:contractCode");
        if (!EmptyUtils.isEmpty(billCode)) {
            sql.append(" and d.bill_Code <> :billCode");
            param.put("billCode", billCode);
        }
        param.put("contractCode", contractCode);
        List list = listBySQL(sql.toString(), param);
        if (list.size() > 0 && list != null) {
            return false;
        }
        return true;
    }
    /** 审核列表查询 **/
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sb = new StringBuilder();
        //TODO 补充名称相关信息
//        sb.append("select * from cms_precative_loan_deferred d where 1 = 1 and ");
        sb.append("select i.cur_name as curName,i.precative_name as precativeName,i.loanopp_name as loanOppName,i.corp_name as corpName,d.* from cms_precative_loan_deferred d ");
        sb.append(" left join cms_precative_loan_send s");
        sb.append(" on d.li_code = s.bill_code");
        sb.append(" left join cms_precative_loan_info i");
        sb.append(" on i.bill_code = s.father_code");
        sb.append(" where 1 = 1 ");
        if (ObjectUtil.isNotEmpty(ids)) {
            sb.append(" and ").append(getSQLIn(ids, 900, "d.id")).append(" ");
        }
        return getDynamicBySQL(input, sb.toString(), PrecativeLoanDeferredVo.class, "", false);
    }
    /**
     * <b>功能介绍：</b>获取委托贷款展期申请<br>
     * <b>日期：</b>2017年8月25日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @return
     */
    public List<CmsPrecativeLoanDeferredDTO> getCmsPrecativeDeferreds() {
        StringBuilder hql = new StringBuilder("select * from cms_precative_loan_deferred d where d.status >= "
                + CmsConstant.COMPELTEXTENDATE + " order by d.bill_Code ");
        return listBySQLAliasToBean(hql.toString(), null, CmsPrecativeLoanDeferredDTO.class);
    }
    /**
     * <b>功能介绍：</b>获取展期次数<br>
     * <b>日期：</b>2017年9月18日<br>
     * <b>作者：</b>zhengxn<br>
     * 
     * @version 1.0
     */
    public Integer getPrecativeLoanSendCountByBillCode(String billCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                " select count(1) sendCount from cms_precative_loan_deferred d left join cms_precative_loan_send s on d.li_code = s.bill_code where s.bill_code = :billCode and d.status <> -2 ");
        params.put("billCode", billCode);
        java.math.BigDecimal sendCount = (java.math.BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return sendCount.intValue();
    }
    /**
     * <b>功能介绍：</b>获取展期次数明细列表<br>
     * <b>日期：</b>2017年9月18日<br>
     * <b>作者：</b>zhengxn<br>
     * 
     * @version 1.0
     */
    public List<PrecativeLoanDeferredVo> getPrecativeLoanSendListByBillCode(String liCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select d.corp_id as corpId, d.bill_code as billCode,d.li_code as liCode,d.contract_code as contractCode,");
        sql.append(" d.mature_date as matureDate,d.deferred_limt as deferredLimt,d.tern_unit as ternUnit,");
        //TODO 补充名称相关信息
        sql.append(" i.cur_name as curName,i.precative_name as precativeName,i.loanopp_name as loanOppName,i.corp_name as corpName, ");
        sql.append(
                " d.deferred_repay_date as deferredRepayDate,d.deferred_rate as deferredRate,d.biz_status as bizStatus");
        sql.append("  from cms_precative_loan_deferred d ");
        sql.append("  left join cms_precative_loan_send s");
        sql.append("  on d.li_code = s.bill_code ");
        //TODO 补充名称相关信息
        sql.append(" left join cms_precative_loan_info i");
        sql.append(" on i.bill_code = s.father_code");
        sql.append("  where d.li_Code =:liCode ");
        sql.append(" and d.status != -2 order By d.create_date desc");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("liCode", liCode);
        return listBySQLAliasToBean(sql.toString(), param, PrecativeLoanDeferredVo.class);
    }
    /**
     * 单位校验使用
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select count(*) from cms_precative_loan_deferred where 1 = 1 and status in (21,104) and corp_id = ?");
        return Integer.valueOf(createSQLQuery(sql.toString(), corpId).uniqueResult().toString());
    }
}
