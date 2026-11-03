package com.bytter.credential.domain.international.dao;
import com.bytter.credential.domain.international.entity.LcIncoming;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
@Repository
public class IncomingLetterDao extends BaseDao<LcIncoming,String> {
    /**
    *
    *@Param id
    *@return 
    */
    public Integer getLcIncomingByLcId(String id){
        StringBuilder sql = new StringBuilder();
        sql.append(" select bt_nvl_num(count(*),0) from LC_INCOMING where status = 5 and lc_id = ?");
        String totalMoneyStr = createSQLQuery(sql.toString(), id).uniqueResult().toString();
        return Integer.valueOf(totalMoneyStr);
    }
    
}
