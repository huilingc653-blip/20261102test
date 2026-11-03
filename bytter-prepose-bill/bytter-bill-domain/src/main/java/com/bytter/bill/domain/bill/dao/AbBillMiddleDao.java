package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbBillMiddle;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
* @Title: Dao
* @Description: 票据中间表
* @author onlineGenerator
* @date 2017-11-13 11:00:24
* @version 1.0
*
*/
@Repository
public class AbBillMiddleDao extends BaseDao<AbBillMiddle,String>{
    /**
     * 通过业务id和删除票据中间表数据
     * @param buId 业务ID
     * @param type 中间表类型
     */
    public int deleteBillMiddleByBuIdAndType(String buId, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from AB_BILL_MIDDLE where BU_ID = :buId and type=:type");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("buId", buId);
        params.put("type", type);
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
