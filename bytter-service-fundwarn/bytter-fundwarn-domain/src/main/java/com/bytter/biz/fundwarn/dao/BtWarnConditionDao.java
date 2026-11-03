package com.bytter.biz.fundwarn.dao;
import com.bytter.biz.fundwarn.entity.BtWarnCondition;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 预警策略数据操作类
 * @author zhenghj
 * @date 2018-09-27
 * @version 1.0
 *
 */
@Repository
public class BtWarnConditionDao extends BaseDao<BtWarnCondition,String> {
    /**
     * 根据预警策略ID获取预警策略条件表数据
     * @param warnId
     * @return
     */
    public List<BtWarnCondition> getWarnConditionByWarnId(String warnId){
        return this.listBy("warnId",warnId);
    }
    public int deleteCondtionByWarnId(String warnId){
        Map<String,String> params=new HashMap<String,String>();
        String sql = "delete from BT_WARN_CONDITION where warn_Id=:warnId";
        Map<String, Object> map = new HashMap<>();
        map.put("warnId", warnId);
        return createSQLQuery(sql, map).executeUpdate();
    }
}
