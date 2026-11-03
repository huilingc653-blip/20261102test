package com.bytter.ras.service;

import com.bytter.ras.dao.RasShopGrantDao;
import com.bytter.ras.entity.RasShopGrant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 营收稽核 用户门店权限 操业务处理类
 * @date 2019-04-29 15:43:05
 */
@Service("rasShopGrantService")
public class RasShopGrantService extends BaseService<RasShopGrant, String> {

    @Autowired
    private RasShopGrantDao rasShopGrantDao;

    @Override
    public BaseDao<RasShopGrant, String> getEntityDao() {
        return rasShopGrantDao;
    }

    /**
     * 根据单位ID和资源代码获取对应的用户信息
     *
     * @param corpId    单位ID
     * @param grantType 授权类型 query查询权限audit稽核权限
     * @param user      用户
     */
    public List<Map<String, String>> loadShopByCorpIdOrGrantType(String corpId, String grantType, ICurrentUser user) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isEmpty(corpId)) {
            corpId = "空";
        }
        String sql = getGrantShopSql(corpId, grantType, user, params);
        List<Map<String, Object>> list = rasShopGrantDao.loadShopByCorpIdOrGrantType(sql, params);
        List<Map<String, String>> temp = new ArrayList<>();
        if (EmptyUtils.isEmpty(list)) {
            return temp;
        }
        for (Map<String, Object> info : list) {
            Map<String, String> reInfo = new HashMap<>();
            String id = info.containsKey("id") ? String.valueOf(info.get("id")) : String.valueOf(info.get("id".toUpperCase()));
            String code = info.containsKey("code") ? String.valueOf(info.get("code")) : String.valueOf(info.get("code".toUpperCase()));
            String name = info.containsKey("name") ? String.valueOf(info.get("name")) : String.valueOf(info.get("name".toUpperCase()));
            reInfo.put("text", "(" + code + ")" + name);
            reInfo.put("code", code);
            reInfo.put("name", name);
            reInfo.put("id", id);
            reInfo.put("value", id);
            temp.add(reInfo);
        }
        return temp;
    }

    /**
     * 根据用户和授权类型获取对应符合操作的门店ID
     *
     * @param grantType 授权类型 query查询权限audit稽核权限
     * @param user      用户
     */
    public List<String> getShopIdByUserOrGrantType(String grantType, ICurrentUser user) {
        return getShopIdByUserOrGrantType(grantType, null, user);
    }

    /**
     * 根据用户和授权类型获取对应符合操作的门店ID
     *
     * @param grantType 授权类型 query查询权限audit稽核权限
     * @param user      用户
     */
    public List<String> getShopIdByUserOrGrantType(String grantType, List<String> corpId, ICurrentUser user) {
        Map<String, Object> params = new HashMap<>();
        String corpIdStr = null;
        if (corpId != null && corpId.size() > 0) {
            corpIdStr = StringUtils.join(corpId, ",");
        }
        String sql = getGrantShopSql(corpIdStr, grantType, user, params);
        List<Map<String, Object>> list = rasShopGrantDao.loadShopByCorpIdOrGrantType(sql, params);
        List<String> ids = new ArrayList<>();
        for (Map<String, Object> info : list) {
            String id = info.containsKey("id") ? String.valueOf(info.get("id")) : String.valueOf(info.get("id".toUpperCase()));
            ids.add(id);
        }
        return ids;
    }

    /**
     * 获取查询sql
     *
     * @param corpId    单位id
     * @param grantType 授权类型
     * @param user      操作用户
     * @return
     */
    private String getGrantShopSql(String corpId, String grantType, ICurrentUser user, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.id,s.code,s.name from ras_shop s inner join ras_shop_grant g on s.id=g.shop_id where 1=1 ");
        params.put("userId", user.getId());
        params.put("grantSign", "1");
        if (RasConstant.GRANT_TYPE_AUDIT.equals(grantType)) {
            sql.append("and g.can_audit=:grantSign ");
    		sql.append("and s.VALID_SIGN=:validSign ");
    		params.put("validSign", RasConstant.RAS_VALID_SIGN_1);
        } else if (RasConstant.GRANT_TYPE_QUERY.equals(grantType)) {
            sql.append("and g.can_query=:grantSign ");
        } else {
            params.put("grantSign", "W");
            sql.append("and g.can_audit=:grantSign ");
        }
        if (StringUtils.isNotEmpty(corpId)) {
            String[] corps = StringUtils.split(corpId, ",");
            sql.append("and ").append(SqlUtil.getInSqlByParams(corps, "s.corp_id", params));
        }
        sql.append("and g.user_id=:userId ");
        sql.append(" order by s.code,s.name ");
        return sql.toString();
    }

    /**
     * 查询已分配授权的数据
     *
     * @param shopId 门店id
     * @param userId 用户id
     * @return
     */
    public List<RasShopGrant> listAssigned(String shopId, String userId) {
        return rasShopGrantDao.listAssigned(shopId, userId);
    }

    /**
     * 根据门店id与用户id查找授权信息
     *
     * @param shopIds 门店id
     * @param userIds 用户id
     * @return
     */
    public List<RasShopGrant> listByShopIdsAndUserIds(List<String> shopIds, List<String> userIds) {
        return rasShopGrantDao.listByShopIdsAndUserIds(shopIds, userIds);
    }


    /**
     * 保存授权数据
     *
     * @param rasShopGrantList
     */
    public void save(List<RasShopGrant> rasShopGrantList) {
        for (RasShopGrant rasShopGrant : rasShopGrantList) {
            //有门店权限的将数据写入表中、减少门店授权表中的冗余数据
            rasShopGrantDao.deleteByShopIdAndUserId(rasShopGrant);
            if ("1".equals(rasShopGrant.getCanQuery()) || "1".equals(rasShopGrant.getCanAudit())) {
                saveEntity(rasShopGrant);
            }
        }
    }
    
    /**
     * 
    * @Title: queryImportAudit  
    * @Description: TODO(营收/实收导入查询用户是否有当前授权操作权限没有则返回false)  
     */
    public boolean queryImportAudit(String userId,String shopId){
	    Map<String, Object> params = new HashMap<>();
	    params.put("userId", userId);
	    params.put("shopId", shopId);
    	return rasShopGrantDao.queryImportAudit(params);
    }

}