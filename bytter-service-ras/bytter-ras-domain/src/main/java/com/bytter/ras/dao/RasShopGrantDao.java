package com.bytter.ras.dao;

import com.bytter.ras.entity.RasShopGrant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: Dao
 * @Description: 营收稽核用户门店权限
 * @date 2019-04-29 15:43:05
 */
@Repository
public class RasShopGrantDao extends BaseDao<RasShopGrant, String> {


    /**
     * 根据单位ID和资源代码获取对应的用户信息
     *
     * @param sql    查询sql
     * @param params 查询参数
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> loadShopByCorpIdOrGrantType(String sql, Map<String, Object> params) {
        return this.listBySQL(sql, params);
    }


    /**
     * 查询已分配授权的数据
     *
     * @param shopId 门店id
     * @param userId 用户id
     * @return
     */
    public List<RasShopGrant> listAssigned(String shopId, String userId) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> map = new HashMap<String, Object>();
        hql.append("from RasShopGrant where 1=1 ");
        if (!EmptyUtils.isBlank(shopId)) {
            hql.append(" and shopId=:shopId ");
            map.put("shopId", shopId);
        }
        if (!EmptyUtils.isBlank(userId)) {
            hql.append(" and userId=:userId ");
            map.put("userId", userId);
        }
        return createQuery(hql.toString(), map).list();
    }

    /**
     * 根据门店id与用户id查找授权信息
     *
     * @param shopIds 门店id
     * @param userIds 用户id
     * @return
     */
    public List<RasShopGrant> listByShopIdsAndUserIds(List<String> shopIds, List<String> userIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("shopIds", shopIds);
        map.put("userIds", userIds);
        String sql = "select ag1.* FROM ras_shop_grant ag1 left join ras_shop_grant ag2 on ag1.id=ag2.id " +
                " where ag1.shop_id in (:shopIds) and ag2.user_id in (:userIds) ";
        return listBySQLAliasToBean(sql, map, RasShopGrant.class);
    }

    /**
     * 根据门店id和用户id删除授权信息
     *
     * @param rasShopGrant
     */
    public void deleteByShopIdAndUserId(RasShopGrant rasShopGrant) {
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", rasShopGrant.getShopId());
        map.put("userId", rasShopGrant.getUserId());
        createSQLQuery("delete from RAS_SHOP_GRANT where SHOP_ID=:shopId and USER_ID=:userId", map).executeUpdate();
    }
    
    /**
     * 
    * @Title: queryImportAudit  
    * @Description: TODO(营收/实收导入查询用户是否有当前授权操作权限没有则返回false)  
    * @param @param shopId
    * @param @param userId
    * @param @return    参数  
    * @return boolean    返回类型  
    * @throws
     */
    public boolean queryImportAudit(Map<String, Object> params){

        StringBuilder hql = new StringBuilder();
        hql.append("from RasShopGrant where 1=1  and canAudit=1");
        hql.append(" and shopId=:shopId ");
        hql.append(" and userId=:userId ");
        List<RasShopGrant> list =createQuery(hql.toString(), params).list();
        if(list.size()>0){
        	return true;
        }
    	return false;
    }
}
