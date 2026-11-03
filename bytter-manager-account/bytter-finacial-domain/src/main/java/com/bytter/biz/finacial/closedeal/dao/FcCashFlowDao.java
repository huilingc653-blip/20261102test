package com.bytter.biz.finacial.closedeal.dao;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.closedeal.entity.FcCashFlow;
import com.bytter.core.base.BaseDao;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_CASH_FLOW
* @author onlineGenerator
* @date 2017-07-17 18:36:08
* @version 1.0
*
*/
@Repository
public class FcCashFlowDao extends BaseDao<FcCashFlow,String>{
    /**
     *
     * @param params
     */
    public void deleteByMonth(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from fc_cash_flow ");
        sql.append(" where net_code = :netId ");
        sql.append(" and TALLY_DATE >= BT_TO_DATE(:startDate,'yyyy-MM-dd')  ");
        sql.append(" and TALLY_DATE <= BT_TO_DATE(:endDate,'yyyy-MM-dd')  ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
