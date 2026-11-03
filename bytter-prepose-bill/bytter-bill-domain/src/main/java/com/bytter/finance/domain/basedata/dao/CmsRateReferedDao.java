package com.bytter.finance.domain.basedata.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @Title: 利率功能数据操作类
 *
 * @author: xiay
 * @date: 2023-04-23 下午2:19:43
 * @version V1.0
 */
@Repository
public class CmsRateReferedDao extends BaseDao<T, String> {
    /**
     * 判断贷款利率是否被引用
     * @param rateMaintence
     * @param date
     * @return
     */
    public boolean isAdductionLoan(TbRateMaintenceDTO rateMaintence, Date date){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("select 1 from cms_loan_info loan ");
        sql.append(
                " where loan.status<>-2 and loan.reference_rate_type = :baseRateid and loan.repay_cycle = :creditPeriodId and loan.cur_code = :moneyType ");
        sql.append(" and loan.loan_date >= BT_TO_DATE(:beform,'yyyy-MM-dd')");
        if (date != null) {
            sql.append(" and loan.loan_date < BT_TO_DATE(:after, 'YYYY-MM-DD')");
            params.put("after", DateUtils.formatDate(date));
        }
        params.put("baseRateid", rateMaintence.getBaseRateId());
        params.put("creditPeriodId", rateMaintence.getCreditPeriodId());
        params.put("moneyType", rateMaintence.getMoneyType());
        params.put("beform", DateUtils.formatDate(rateMaintence.getTimeOfTakeEffect()));
        List list = this.createSQLQuery(sql.toString(), params).list();
        if (EmptyUtils.isEmptyList(list)) {
            return false;
        }
        return true;
    }
    /**
     * 判断借款利率是否被引用
     * @param tbLoanRateMaintence
     * @param date
     * @return
     */
    public boolean isAdductionProvide(TbRateMaintenceDTO tbLoanRateMaintence, Date date) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("select 1 from cms_provide_loan_info loan ");
        sql.append(
                " where loan.status not in(-2,-3) and loan.reference_rate_type = :baseRateid and loan.repay_cycle = :creditPeriodId and loan.cur_id = :moneyType ");
        sql.append(" and loan.pl_date >= BT_TO_DATE(:beform,'yyyy-MM-dd')");
        if (date != null) {
            sql.append(" and loan.pl_date < BT_TO_DATE(:after, 'YYYY-MM-DD')");
            params.put("after", DateUtils.formatDate(date));
        }
        params.put("baseRateid", tbLoanRateMaintence.getBaseRateId());
        params.put("creditPeriodId", tbLoanRateMaintence.getCreditPeriodId());
        params.put("moneyType", tbLoanRateMaintence.getMoneyType());
        params.put("beform", DateUtils.formatDate(tbLoanRateMaintence.getTimeOfTakeEffect()));
        List list = this.createSQLQuery(sql.toString(), params).list();
        if (EmptyUtils.isEmptyList(list)) {
            return false;
        }
        return true;
    }
    /**
     * 判断委托贷款利率是否被引用
     * @param rateMaintence
     * @param date
     * @return
     */
    public boolean isAdductionPrecative(TbRateMaintenceDTO rateMaintence, Date date) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("select 1 from cms_precative_loan_info loan ");
        sql.append(
                " where loan.status<>-2 and loan.reference_rate_type = :baseRateid and loan.repay_cycle = :creditPeriodId and loan.cur_id = :moneyType ");
        sql.append(" and loan.start_date >= BT_TO_DATE(:beform,'yyyy-MM-dd')");
        if (date != null) {
            sql.append(" and loan.start_date < BT_TO_DATE(:after, 'YYYY-MM-DD')");
            params.put("after", DateUtils.formatDate(date));
        }
        params.put("baseRateid", rateMaintence.getBaseRateId());
        params.put("creditPeriodId", rateMaintence.getCreditPeriodId());
        params.put("moneyType", rateMaintence.getMoneyType());
        params.put("beform", DateUtils.formatDate(rateMaintence.getTimeOfTakeEffect()));
        List list = this.createSQLQuery(sql.toString(), params).list();
        if (EmptyUtils.isEmptyList(list)) {
            return false;
        }
        return true;
    }
}
