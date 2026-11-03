package com.bytter.finance.domain.provide.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.finance.domain.provide.entity.CmsProvideInterestTemp;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangyl
 * @version 1.0
 * @Title: Dao
 * @Description: 借款利息预计明细暂存表
 * @date 2023-03-29 09:58:43
 */
@Repository
public class CmsProvideInterestTempDao extends BaseDao<CmsProvideInterestTemp, String> {

    public void deleteBySendBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billCode", billCode);
        String sql = "delete from  CMS_PROVIDE_INTEREST_TEMP where bill_code=:billCode ";
        createSQLQuery(sql, params).executeUpdate();
        getSession().flush();
    }

    public List<CmsProvideInterestTemp> getListByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer();
        hql.append(" from CmsProvideInterestTemp i ");
        hql.append(" where i.billCode = :billCode ");
        params.put("billCode", billCode);
        return listByHQL(hql.toString(), params);
    }
    public List<CmsProvideInterestTemp> getListByBillCodeAndTimeMoney(String billCode, String chooseRepayDate, BigDecimal chooseRepayMoney){
        Map<String, Object> params = new HashMap<String, Object>();
        Date date = DateUtils.parseDate(chooseRepayDate);
        StringBuffer hql = new StringBuffer();
        hql.append(" from CmsProvideInterestTemp i ");
        hql.append(" where i.billCode = :billCode ");
        hql.append(" and i.chooseRepayDate = :chooseRepayDate ");
        hql.append(" and i.chooseRepayMoney = :chooseRepayMoney ");
        hql.append(" ORDER BY i.repayDate");
        params.put("billCode", billCode);
        params.put("chooseRepayDate", date);
        params.put("chooseRepayMoney", chooseRepayMoney);
        return listByHQL(hql.toString(), params);
    }
}
