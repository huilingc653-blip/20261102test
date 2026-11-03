package com.bytter.finance.domain.loan.dao;
import com.bytter.finance.domain.loan.entity.CmsLoanDeferred;
import com.bytter.finance.domain.loan.vo.CmsLoanDeferredVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 贷款展期申请表
 * @author onlineGenerator
 * @date 2017-08-04 14:22:49
 * @version 1.0
 *
 */
@Repository
public class CmsLoanDeferredDao extends BaseDao<CmsLoanDeferred, String> {
    /**
     * <b>功能介绍：</b>查询可编辑展期数据<br>
     * <b>日期：</b>2017年8月5日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    public DataTablesOutput getEditDataTables(DataTablesInput input, String type, List<String> corpAuth,
            String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select d.id,d.corp_id as corpId,i.net_code as netId,d.bill_code as billCode,d.bill_code as deferredCode,d.create_date as createDate,d.li_code as liCode,");
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName, ");
        sql.append(
                "d.mature_date as matureDate,d.deferred_repay_date as deferredRepayDate,d.deferred_limt as deferredLimt,d.tern_unit as ternUnit,d.contract_code as  contractCode,d.status as status,d.biz_status as bizStatus ");
        sql.append("from cms_loan_info i,cms_loan_send s,cms_loan_deferred d ");
        sql.append("where i.bill_code = s.loan_code and s.bill_code = d.li_code ");
        if (CmsConstant.IS_FALSE.equals(type)) {// 展期修改
            sql.append("and d.status in (" + CmsConstant.TEMP + "," + CmsConstant.BACK + ") ");
            sql.append("and d.create_by = '" + userId + "' ");
        } else {// 展期处理
            sql.append("and d.status = " + CmsConstant.APPROVED);
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "d.corp_id"));
        return getDynamicBySQL(input, sql.toString(), CmsLoanDeferredVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>发放申请对应的展期申请列表<br>
     * <b>日期：</b>2017年8月11日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    public List<CmsLoanDeferredVO> getCmsLoanDeferredItems(String liCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        //TODO 补充名称相关信息
        sql.append(
                "select d.id,d.corp_id as corpId,i.net_code as netId,i.net_name as netName,i.corp_name as corpName,d.bill_code as billCode,d.create_date as createDate,d.li_code as liCode,");
        sql.append(
                "d.mature_date as matureDate,d.deferred_repay_date as deferredRepayDate,d.deferred_limt as deferredLimt,d.tern_unit as ternUnit,d.contract_code as  contractCode,d.status as status,d.biz_status as bizStatus,d.deferred_rate as deferredRate ");
        sql.append("from cms_loan_info i,cms_loan_send s,cms_loan_deferred d ");
        sql.append("where i.bill_code = s.loan_code and s.bill_code = d.li_code ");
        sql.append("and d.status != " + CmsConstant.DELETED);
        sql.append(" and d.li_code = :liCode order by d.deferred_repay_date desc ");
        params.put("liCode", liCode);
        return listBySQLAliasToBean(sql.toString(), params, CmsLoanDeferredVO.class);
    }
    /**
     * <b>功能介绍：</b>审批列表数据查询<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select d.id,d.corp_id as corpId,i.net_code as netId,d.bill_code as billCode,d.bill_code as deferredCode,d.create_date as createDate,d.li_code as liCode,");
        //TODO 补充名称信息
        sql.append("i.net_name as netName,i.corp_name as corpName,");
        sql.append(
                "d.mature_date as matureDate,d.deferred_repay_date as deferredRepayDate,d.deferred_limt as deferredLimt,d.tern_unit as ternUnit,d.contract_code as  contractCode,d.status as status,d.biz_status as bizStatus ");
        sql.append("from cms_loan_info i,cms_loan_send s,cms_loan_deferred d ");
        sql.append("where i.bill_code = s.loan_code and s.bill_code = d.li_code ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "d.id")).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsLoanDeferredVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>根据发放申请编号查询已审批通过的展期申请<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<CmsLoanDeferred> getCmsLoanDeferredByLiCode(String liCode) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        hql.append("from CmsLoanDeferred d where d.liCode = :liCode and d.status >= " + CmsConstant.APPROVED
                + " order by d.deferredRepayDate");
        params.put("liCode", liCode);
        return createQuery(hql.toString(), params).list();
    }
    /**
     * <b>功能介绍：</b>判断贷款展期合同号是否存在<br>
     * <b>日期：</b>2017年8月11日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    public boolean existsContractCode(String contractCode, String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select count(1) count from cms_loan_deferred d where d.contract_code = :contractCode and d.status != :status ");
        if (EmptyUtils.isNotEmpty(id)) {
            sql.append("and d.id != :id");
            params.put("id", id);
        }
        params.put("contractCode", contractCode);
        params.put("status", CmsConstant.DELETED);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
    /**
     * <b>功能介绍：</b>更新贷款账号展期信息<br>
     * <b>日期：</b>2017年10月26日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @param loanAcc
     * @param deferredDate
     */
//    public void updateLoanAccDeferredDate(String loanAcc, Date deferredDate, BigDecimal deferredRate) {
//        String sql = "update fc_acc a set a.delay_deadline_date = :deferredDate,a.delay_rate = :deferredRate where a.blnctr_acc = :loanAcc ";
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("deferredRate", deferredRate);
//        params.put("deferredDate", deferredDate);
//        params.put("loanAcc", loanAcc);
//        createSQLQuery(sql, params).executeUpdate();
//    }
    /**
     * 单位校验使用-展期申请待处理
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from cms_loan_deferred where 1 = 1 and status = 95  and corp_id = ?");
        return Integer.valueOf(createSQLQuery(sql.toString(), corpId).uniqueResult().toString());
    }
}
