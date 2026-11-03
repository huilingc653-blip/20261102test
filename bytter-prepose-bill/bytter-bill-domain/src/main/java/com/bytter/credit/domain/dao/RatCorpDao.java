package com.bytter.credit.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.credit.constant.RationConstant;
import org.springframework.stereotype.Repository;
import com.bytter.core.base.BaseDao;
import com.bytter.system.api.dto.CorpDTO;
/**
 * 操作单位集团统一授信主体标识DAO
 * 
 * @author liyh
 *
 */
@Repository
public class RatCorpDao extends BaseDao<CorpDTO, String> {
    //TODO 走基础 —》远程调用
    /**
     * 功能介绍 : 还原所有单位集团统一授信主体标识。<br>
     * <b>日期：</b>2017年7月27日<br>
     * 
     * @version 1.0
     * @author liyh
     */
//    public void resetRatGroup() {
//        String sql = " update sys_corp a set a.rat_group = :ratGroup ";
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("ratGroup", RationConstant.RAT_GROUP_0);
//        createSQLQuery(sql, params).executeUpdate();
//    }
    /**
     * 功能介绍 : 还原所有单位集团统一授信主体标识。<br>
     * <b>日期：</b>2017年7月27日<br>
     * 
     * @version 1.0
     * @author liyh
     */
//    public void updateRatGroup(List<String> corpIds) {
//        String sql = " update sys_corp a set a.rat_group = :ratGroup where " + getSQLIn(corpIds, 900, "a.id");
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("ratGroup", RationConstant.RAT_GROUP_1);
//        createSQLQuery(sql, params).executeUpdate();
//    }
}
