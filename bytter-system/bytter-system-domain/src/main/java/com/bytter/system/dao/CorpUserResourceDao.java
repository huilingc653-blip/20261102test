package com.bytter.system.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.CorpUserResourceDTO;
import com.bytter.system.entity.CorpUserResource;
import com.bytter.system.vo.ResourcesSimpleVO;
import com.google.common.collect.Lists;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
/**
 * 用户特殊资源权限配置Dao
 *
 * @author zhoul
 * @since 2017/4/13.
 */
@Repository
public class CorpUserResourceDao extends BaseDao<CorpUserResource, String> {
    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcOperations;
    /**
     * 根据资源ID获取特殊权限集合
     *
     * @param resourceList 资源ID集合
     * @return 特殊权限集合
     */
    public List<CorpUserResource> listByResourceList(List<String> resourceList) {
        return listByResourceListAndCorpId(resourceList, null);
    }
    /**
     * 获取用户拥有指定特殊权限的单位
     *
     * @param userId
     * @param permission
     * @return
     */
    public List<String> listCorpIdByUserIdAndResourceId(String userId, String permission) {
        StringBuilder sql = new StringBuilder("select distinct cur.corp_Id from SYS_CORP_USER_RESOURCE cur ");
        sql.append(" join sys_Corp c on  cur.corp_Id = c.id ");
        sql.append(" where cur.user_Id = :userId");
        sql.append(" and cur.permission = :permission");
        sql.append(" and  c.status = 1");
        SQLQuery sqlQuery = createSQLQuery(sql.toString());
        sqlQuery.setParameter("userId", userId);
        sqlQuery.setParameter("permission", permission);
        return sqlQuery.list();
    }
    /**
     * 根据单位ID获取特殊权限数据
     *
     * @param corpId 单位ID
     * @return 特殊权限集合
     */
    public List<CorpUserResource> listByCorpId(String corpId) {
        return listByResourceListAndCorpId(null, corpId);
    }
    /**
     * 根据资源集合和单位ID获取特殊权限的数据
     *
     * @param resourceList 资源ID结合
     * @param corpId       单位ID
     * @return 特殊权限集合
     */
    public List<CorpUserResource> listByResourceListAndCorpId(List<String> resourceList, String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from SYS_CORP_USER_RESOURCE where 1=1 ");
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(corpId)) {
            sql.append(" and corp_id=:corpId");
            map.put("corpId", corpId);
        }
        if (!EmptyUtils.isEmpty(resourceList)) {
            sql.append(" and (" + getSQLIn(resourceList, 900, "resource_id") + ") ");
        }
        return listBySQLAliasToBean(sql.toString(), map, CorpUserResource.class);
    }
    public List<CorpUserResource> listByMenuAndCorpId(String menuId, String corpId) {
        StringBuilder hql = new StringBuilder();
        hql.append("select USER_ID,CORP_ID,RESOURCE_ID from SYS_CORP_USER_RESOURCE where 1=1 ");
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(corpId)) {
            hql.append(" and corp_id=:corpId");
            map.put("corpId", corpId);
        }
//        if (StringUtils.isNotBlank(menuId)) {
//            hql.append(" and menuId=:menuId");
//            map.put("menuId", menuId);
//        }
        return listBySQLAliasToBean(hql.toString(), map, CorpUserResource.class);
    }
    public List<CorpUserResource> listByUserId(String userId) {
        String sql = "select * from SYS_CORP_USER_RESOURCE cur where cur.user_Id=:userId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        return listBySQLAliasToBean(sql, map, CorpUserResource.class);
    }

    public List<Map<String,String>> listByUserIds(List userIds) {
        if(EmptyUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        // 别名用小写；再经 listMapBySQL 统一转小写，兼容 MySQL/Oracle 列名大小写差异
        String sql = " select distinct corp_id as corpid, user_id as userid from sys_corp_user_resource where ";
        String usersSql = SqlUtil.getInSql(Lists.newArrayList(userIds), "user_Id", true);
        sql += usersSql;
        List list = listMapBySQL(sql, null);
        return EmptyUtils.isEmpty(list) ? Collections.emptyList() : list;
    }
    /**
     * 根据用户Id获取分配单位ID集合
     *
     * @param userId
     * @return
     */
    public List<String> listCorpIdByUserId(String userId) {
        String sql = "select distinct corp_id from SYS_CORP_USER_RESOURCE cur where cur.user_Id=:userId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        return namedParameterJdbcOperations.queryForList(sql, map, String.class);
    }
    public List<CorpUserResource> listByUserIdAndCorpId(String userId, String corpId) {
        String sql = "select * from SYS_CORP_USER_RESOURCE cur where cur.user_id=:userId and cur.corp_id=:corpId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("corpId", corpId);
        return listBySQLAliasToBean(sql, map, CorpUserResource.class);
    }

    public List<CorpUserResourceDTO> list(String userId,String corpIds) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t2.RESOURCE_ID AS resourceId,t2.CREATE_TIME AS assignTime,")
                .append("t3.ID AS corpId,t3.CODE AS corpCode,t3.NAME AS corpName ")
                .append("FROM sys_user t1  ")
                .append("INNER JOIN sys_corp_user_resource t2 ON t2.USER_ID = t1.ID ")
                .append("INNER JOIN sys_corp t3 ON t3.ID = t2.CORP_ID ")
                .append("WHERE t1.STATUS = 1 and t1.id = :userId  ");
        if (ObjectUtil.isNotEmpty(corpIds)) {
            sql.append(" AND ").append(SqlUtil.getInSql(corpIds.split(","), "t3.ID", true));
        }
        return listBySQLAliasToBean(sql.toString(), params, CorpUserResourceDTO.class);
    }


    /**
     * 根据userId,corpId 删除网点单位、用户、资源关系表中的数据
     *
     * @param userId 用户ID
     * @param corpId 单位ID
     */
    public void deleteByCorpIdAndUserId(String userId, String corpId) {
        String sql = "delete from SYS_CORP_USER_RESOURCE where user_id=? and corp_id=?";
        createSQLQuery(sql, userId, corpId).executeUpdate();
    }
    /**
     * 根据userId,corpIds 删除网点单位、用户、资源关系表中的数据
     *
     * @param userId  用户ID
     * @param corpIds 单位IDS
     */
    public void deleteByCorpIdsAndUserId(String userId, List<String> corpIds) {
        StringBuilder sql = new StringBuilder("delete from SYS_CORP_USER_RESOURCE where user_Id=:userId and corp_id=:corpId ");
        Map<String, Object>[] mapArray = new HashMap[corpIds.size()];
        for (int i = 0; i < corpIds.size(); i++) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("corpId", corpIds.get(i));
            mapArray[i] = params;
        }
        namedParameterJdbcOperations.batchUpdate(sql.toString(), mapArray);
    }
    /**
     * 根据userId,sourceCorpId，corpId 通过数据库后台userId和sourceCorpId查到数据批量插入corpId的资源信息
     *
     * @param userId       用户ID
     * @param sourceCorpId 资源来源单位
     * @param corps        被复制单位
     */
    public void saveBatchCorpUserResource(String userId, String sourceCorpId, List<String> corps) {
        StringBuilder sql = new StringBuilder("insert into SYS_CORP_USER_RESOURCE(CORP_ID,USER_ID,RESOURCE_ID,PERMISSION,MENU_ID) ");
        sql.append(" select :corpId,:userId,RESOURCE_ID,PERMISSION,MENU_ID ");
        sql.append(" From SYS_CORP_USER_RESOURCE where corp_id=:sourceCorpId and user_id=:userId ");
        Map<String, Object>[] mapArray = new HashMap[corps.size()];
        for (int i = 0; i < corps.size(); i++) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("sourceCorpId", sourceCorpId);
            params.put("corpId", corps.get(i));
            mapArray[i] = params;
        }
        namedParameterJdbcOperations.batchUpdate(sql.toString(), mapArray);
    }
    public void batchSaveData(List<CorpUserResource> list) {
//        StringBuilder sql = new StringBuilder("insert into SYS_CORP_USER_RESOURCE(CORP_ID,USER_ID,RESOURCE_ID) ");
//        sql.append("values (:corpId,:userId,:resourceId)");
//        Map<String, Object>[] mapArray = new HashMap[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            CorpUserResource corpUserResource = list.get(i);
//            Map<String, Object> params = new HashMap<>();
//            params.put("corpId", corpUserResource.getCorpId());
//            params.put("userId", corpUserResource.getUserId());
//            params.put("resourceId", corpUserResource.getResourceId());
//            mapArray[i] = params;
//        }
//        namedParameterJdbcOperations.batchUpdate(sql.toString(), mapArray);
        this.batchJdbcSave(list);
    }
    /**
     * 根据用户ID，以及菜单ID或者资源ID删除
     *
     * @param userId         用户ID
     * @param resourceIdList 资源ID集合
     */
    private void deleteByUserIdAndMenuIdOrRecourceId(String userId, Set<String> resourceIdList) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append("delete from SYS_CORP_USER_RESOURCE where user_id=:userId ");
        if (!CollectionUtils.isEmpty(resourceIdList)) {
            List resourceIds = new ArrayList();
            resourceIds.addAll(resourceIdList);
            sql.append(" and (" + getSQLIn(resourceIds, 900, "resource_id") + ") ");
        }
        createSQLQuery(sql.toString(), map).executeUpdate();
    }
    /**
     * 根据用户ID和菜单ID集合删除特殊权限记录
     *
     * @param userId         用户ID
     * @param resourceIdList 资源ID
     */
    public void deleteByUserIdAndResourceId(String userId, Set<String> resourceIdList) {
        if (!CollectionUtils.isEmpty(resourceIdList)) {
            deleteByUserIdAndMenuIdOrRecourceId(userId, resourceIdList);
        }
    }
    /**
     * 删除用户的特殊权限
     *
     * @param userId
     * @param corpId
     */
    public void deleteSpecialByUserId(String userId, String corpId) {
        createSQLQuery("delete from SYS_CORP_USER_RESOURCE where user_id=? and corp_id != ?", userId, corpId).executeUpdate();
    }
    /**
     * 通过{@link CorpUserResource} 对象根据usreId、corpId进行批量删除
     *
     * @param corpUserResourceList 特殊权限的UI想
     */
    public void deleteByCorpUser(List<CorpUserResource> corpUserResourceList) {
        String sql = "delete from SYS_CORP_USER_RESOURCE where user_id=:userId ";
        Map<String, Object>[] mapArray = new HashMap[corpUserResourceList.size()];
        for (int i = 0; i < corpUserResourceList.size(); i++) {
            CorpUserResource corpUserResource = corpUserResourceList.get(i);
            Map<String, Object> params = new HashMap<>();
            params.put("userId", corpUserResource.getUserId());
            mapArray[i] = params;
        }
        namedParameterJdbcOperations.batchUpdate(sql, mapArray);
    }
    public void copyCorpUserResource(String sourceUserId, List<CorpUserResource> corpUserResources) {
        String sql = "insert into SYS_CORP_USER_RESOURCE(CORP_ID,USER_ID,RESOURCE_ID,PERMISSION,MENU_ID)" +
                " select CORP_ID,:userId,RESOURCE_ID,PERMISSION,MENU_ID from SYS_CORP_USER_RESOURCE where user_id=:sourceUserId ";
        Map<String, Object>[] mapArray = new HashMap[corpUserResources.size()];
        for (int i = 0; i < corpUserResources.size(); i++) {
            CorpUserResource corpUserResource = corpUserResources.get(i);
            Map<String, Object> params = new HashMap<>();
            params.put("userId", corpUserResource.getUserId());
            params.put("sourceUserId", sourceUserId);
            mapArray[i] = params;
        }
        namedParameterJdbcOperations.batchUpdate(sql, mapArray);
    }
    /**
     * 获取当前用户拥有对应单位的资源信息
     *
     * @return
     */
    public List<ResourcesSimpleVO> listCorpResourcesByActiveUser(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.corp_id, a.permission from SYS_CORP_USER_RESOURCE a  ")
                //.append("left join sys_resources b on a.resource_id=b.id ")
                .append("where a.user_id=:userId and a.permission is not null and a.resource_id is not null");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(userId)) {
            params.put("userId", userId);
        } else {
            params.put("userId", "无");
        }
        return listBySQLAliasToBean(sql.toString(), params, ResourcesSimpleVO.class, 5000);
    }
    /**
     * 根据用户ID和资源权限值获取用户拥有对应单位的资源信息
     * Created by chenlw on 2019/04/29.
     *
     * @param userId
     * @param permission
     * @return
     */
    public List<ResourcesSimpleVO> listCorpResourcesByUserIdAndPermission(String userId, String permission) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct a.corp_id,a.permission ")
                .append("from SYS_CORP_USER_RESOURCE a  ")
                //.append("left join sys_resources b on a.resource_id=b.id ")
                .append("where a.user_id=:userId and a.PERMISSION like :permission");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("permission", "%" + permission + "%");
        sql.append(" order by a.corp_id asc ");
        return this.listBySQLAliasToBean(sql.toString(), params, ResourcesSimpleVO.class);
    }
    /**
     * 用户赋予某个单位的所有资源的权限
     *
     * @param userId      用户id
     * @param corpId      单位id
     * @param resourceIds 资源id
     */
    public void saveBatchCorpUserResourceByReourceIds(String userId, String corpId, List<String> resourceIds) {
        StringBuilder sql = new StringBuilder("insert into SYS_CORP_USER_RESOURCE(USER_ID,CORP_ID,RESOURCE_ID,PERMISSION,MENU_ID,TENANT_ID) ");
        sql.append(" select :userId,:corpId,r.ID,r.PERMISSION,r.MENU_ID, r.TENANT_ID ");
        sql.append(" From SYS_RESOURCES r where r.ID in (:resourceIds)");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("corpId", corpId);
        params.put("resourceIds", resourceIds);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 获取当前用户拥有对应单位的资源信息
     *
     * @return
     */
    public List<ResourcesSimpleVO> listCorpResourcesByActiveUser(String userId, String menuId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cur.permission,cur.corp_id from sys_corp_user_resource cur ");
        sql.append(" where cur.menu_id=:menuId and cur.user_id=:userId");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(userId)) {
            params.put("userId", userId);
        } else {
            params.put("userId", "无");
        }
        if (StringUtils.isNotEmpty(menuId)) {
            params.put("menuId", menuId);
        } else {
            params.put("menuId", "无");
        }
        return listBySQLAliasToBean(sql.toString(), params, ResourcesSimpleVO.class, 5000);
    }
    /**
     * 获取所有有序的单位id
     *
     * @return
     */
    public List<String> listAllCorps() {
        String sql = "select id from sys_corp order by create_time,code";
        Map<String, Object> map = new HashMap<>();
        return namedParameterJdbcOperations.queryForList(sql, map, String.class);
    }
}
