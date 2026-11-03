package com.bytter.finance.domain.provide.dao;
import com.bytter.finance.domain.provide.entity.ProvideSyndicatedDtl;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedDtlVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银团贷款还款明细Dao
 * @date 2022-07-22 10:45:09
 */
@Repository
public class ProvideSyndicatedDtlDao extends BaseDao<ProvideSyndicatedDtl, String> {
    /**
     * 通过还款计划id删除数据
     *
     * @param params
     */
    public void deleteLoanSyndicatedByscheduleId(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from CMS_PROVIDE_SYNDICATEDDTL where SCHEDULE_ID = :scheduleId ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 通过还款支付id删除数据
     *
     * @param params
     */
    public void deleteLoanSyndicatedByrepayId(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from CMS_PROVIDE_SYNDICATEDDTL where REPAY_ID = :repayId ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 通过还款id查询银团还款信息
     * 
     * @param repayId
     * @return
     */
    public List<ProvideSyndicatedDtlVo> getByRepayId(String repayId) {
        if (!StringUtils.isNotEmpty(repayId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select s.id as id, s.direct_finance_id as directFinanceId, s.schedule_id as scheduleId, s.scheduledetailed_id as scheduleDetailedId, ");
        sql.append(
                " s.leading_bank as leadingBank, s.bank_name as bankName, s.bank_code as bankCode, s.surplus_money as surplusMoney, ");
        sql.append(
                " s.repayment_money as repaymentMoney, s.repayment_money_share as repaymentMoneyShare, s.repayment_ist as repaymentIst, ");
        sql.append(
                " s.create_by as createBy, s.create_date as createDate, s.update_by as updateBy, s.update_date as updateDate ");
        sql.append(" from CMS_PROVIDE_SYNDICATEDDTL s ");
        sql.append(" where 1 = 1 ");
        sql.append(" and s.repay_id=:repayId");
        Map<String, String> params = new HashMap<String, String>();
        params.put("repayId", repayId);
        List<ProvideSyndicatedDtlVo> list = listBySQLAliasToBean(sql.toString(), params, ProvideSyndicatedDtlVo.class);
        return list;
    }
    public List<ProvideSyndicatedDtlVo> getByScheduleDetailedId(String scheduleDetailedId) {
        if (!StringUtils.isNotEmpty(scheduleDetailedId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select s.id as id, s.direct_finance_id as directFinanceId, s.schedule_id as scheduleId, s.scheduledetailed_id as scheduleDetailedId, ");
        sql.append(
                " s.leading_bank as leadingBank, s.bank_name as bankName, s.bank_code as bankCode, s.surplus_money as surplusMoney, ");
        sql.append(
                " s.repayment_money as repaymentMoney, s.repayment_money_share as repaymentMoneyShare, s.repayment_ist as repaymentIst, ");
        sql.append(
                " s.create_by as createBy, s.create_date as createDate, s.update_by as updateBy, s.update_date as updateDate ");
        sql.append(" from CMS_PROVIDE_SYNDICATEDDTL s ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(scheduleDetailedId)) {
            sql.append(" and s.scheduledetailed_id=:scheduleDetailedId");
            params.put("scheduleDetailedId", scheduleDetailedId);
        }
        List<ProvideSyndicatedDtlVo> list = listBySQLAliasToBean(sql.toString(), params, ProvideSyndicatedDtlVo.class);
        return list;
    }
}
