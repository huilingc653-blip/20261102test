package com.bytter.system.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.constants.BaseDataConstant;
import com.bytter.system.entity.TbCreditPeriod;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 贷款期限表
 * @author onlineGenerator
 * @date 2017-07-26 17:50:25
 * @version 1.0
 *
 */
@Repository
public class TbCreditPeriodDao extends BaseDao<TbCreditPeriod, String> {


    /**
     * <b>功能介绍：</b>获取人民币期限<br>
     * <b>日期：</b>2017年7月27日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<TbCreditPeriod> getTbCreditPeriod() {
        StringBuilder sql = new StringBuilder("from TbCreditPeriod t order by code");
        return createQuery(sql.toString()).list();
    }


    /**
     * <b>功能介绍：</b>获取人民币期限<br>
     * <b>日期：</b>2017年7月27日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<TbCreditPeriod> getrmbTbCreditPeriod() {
        StringBuilder sql = new StringBuilder("from TbCreditPeriod t where t.moneyType = ?0 order by code");
        return createQuery(sql.toString(), 1).list();
    }
    /**
     * <b>功能介绍：</b>获取外币期限<br>
     * <b>日期：</b>2017年7月27日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public List<TbCreditPeriod> getforeignTbCreditPeriod() {
        StringBuilder sql = new StringBuilder("from TbCreditPeriod t where t.moneyType = ?0 order by code");
        return createQuery(sql.toString(), 2).list();
    }
    /**
     * <b>功能介绍：</b>根据编号查找期限<br>
     * <b>日期：</b>2017年7月31日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    public TbCreditPeriod findCreditPeriodByCode(String code) {
        return getUniqueBy("code", code);
    }
    /**
     * <b>功能介绍：</b>是否被贷款利率引用<br>
     * <b>日期：</b>2017年9月9日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @param period
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean isReferedRate(TbCreditPeriod period) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select id from TB_Loan_Rate_Maintence r where r.credit_period_id = :id and r.status = :status ");
        params.put("id", period.getId());
        params.put("status", BaseDataConstant.STATUS_PASS);
        List result = listBySQL(sql.toString(), params);
        if (EmptyUtils.isEmptyList(result)) {
            return false;
        } else {
            return true;
        }
    }
}
