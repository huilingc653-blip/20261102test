package com.bytter.system.dao;

import cn.hutool.core.text.CharSequenceUtil;
import com.bytter.system.api.dto.RoleResourceDTO;
import com.bytter.system.entity.Resources;
import com.bytter.system.vo.ResourcesSimpleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.google.common.collect.Lists;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class ResourcesDao extends BaseDao<Resources, String> {

    /**
     * 根据资源ID集合查询
     *
     * @param permissionList 资源ID集合
     * @return
     */
    public List<Resources> listByPermission(List<String> permissionList) {
        String hql = "from Resources where permission in (:permissionList)";
        Map<String, Object> map = new HashMap<>();
        map.put("permissionList", permissionList);
        return createQuery(hql, map).list();
    }

    /**
     * 根据资源ID查询
     *
     * @param resPermission 资源ID
     * @return
     */
    public List<Resources> listByPermission(String resPermission) {
        String hql = "from Resources where permission = :resPermission";
        Map<String, Object> map = new HashMap<>();
        map.put("resPermission", resPermission);
        return createQuery(hql, map).list();
    }

    /**
     * 获取所有的资源，带菜单
     *
     * @return
     */
    public List<ResourcesSimpleVO> listResource() {
        StringBuilder hql = new StringBuilder("select r.menu_id as menuId,r.menu.name as menuName,r.id as id,r.name as name from Resources r");
        List<ResourcesSimpleVO> list = listByHQLAliasToBean(hql.toString(), ResourcesSimpleVO.class);
        return list;
    }

    /**
     * 获取所有资源
     * @return
     */
    public List<Resources> listResourceV2() {
       return listAll();
    }

    /**
     * 以菜单表为主表，获取资源权限和菜单。
     * @return
     */
    public List<Resources> listResourceV3(){
        String sql = "select sr.PERMISSION as permission,sm.URL as url from sys_menu sm left join sys_resources sr on sm.id = sr.MENU_ID " +
                " where sm.STATUS = 1 ";
        return listBySQLAliasToBean(sql,null, Resources.class);
    }

    /**
     * 根据用户ID查所有资源
     *
     * @param userId
     * @return
     */
    public List<ResourcesSimpleVO> listResourceByUserId(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT res.ID as id,res.NAME as name,res.PERMISSION as permission,res.MENU_ID as menuId ")
                .append(" from SYS_RESOURCES res ")
                .append(" LEFT JOIN SYS_PERMISSION per ON res.id = per.RESOURCES_ID ")
                .append(" LEFT JOIN SYS_USER_ROLE ur ON ur.ROLE_ID = per.ROLE_ID ")
                .append(" LEFT JOIN SYS_USER u ON u.id = ur.USER_ID ")
                .append(" WHERE u.ID = :userId");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        return listBySQLAliasToBean(sql.toString(), map, ResourcesSimpleVO.class);
    }

    public List<ResourcesSimpleVO> listResourceByUserIdNew(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT res.ID as id,res.NAME as name,res.PERMISSION as permission,res.MENU_ID as menuId ")
                .append(" from SYS_RESOURCES res left join sys_corp_user_resource scur on res.id = scur.resource_id ")
                .append(" WHERE scur.USER_ID = :userId");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        return listBySQLAliasToBean(sql.toString(), map, ResourcesSimpleVO.class);
    }

    /**
     * 根据用户ID查所有资源
     *
     * @param userId
     * @return
     */
    public List<ResourcesSimpleVO> listResourceByUserId2(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT res.ID as id,res.NAME as name,res.MENU_ID as menuId ")
                .append(" from SYS_RESOURCES res ")
                .append(" LEFT JOIN SYS_PERMISSION per ON res.id = per.RESOURCES_ID ")
                .append(" LEFT JOIN SYS_USER_ROLE ur ON ur.ROLE_ID = per.ROLE_ID ")
                .append(" LEFT JOIN SYS_USER u ON u.id = ur.USER_ID ")
                .append(" WHERE u.ID = :userId");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<Map> mapList = listBySQL(sql.toString(), map);
        if (EmptyUtils.isEmpty(mapList)) {
            return Lists.newArrayListWithCapacity(0);
        }
        return BeanUtils.copyMapListProperties(mapList, ResourcesSimpleVO.class);
    }

    /**
     * 根据角色ID所关联使用的资源
     *
     * @param roleId 角色ID
     * @return 资源ID集合
     */
    public List<String> findResourceIdByRoleId(String roleId) {
        String sql = "select RESOURCES_ID as resourceId from SYS_PERMISSION where ROLE_ID = ?";
        SQLQuery sqlQuery = createSQLQuery(sql, roleId);
        sqlQuery.addScalar("resourceId", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }

    /**
     * 根据资源ID查询所关联使用的角色
     *
     * @param resourceId 资源ID
     * @return 角色名称集合
     */
    public List<String> findUsedRoleNamesByResourceId(String resourceId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select distinct r.name as roleName from SYS_PERMISSION sp ");
        sql.append(" left join sys_role r on sp.role_id = r.id ");
        sql.append(" where RESOURCES_ID = ? ");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), resourceId);
        sqlQuery.addScalar("roleName", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }

    /**
     * 根据角色ID删除资源和角色的关系
     *
     * @param roleId 角色ID
     */
    public void deleteByRoleId(String roleId) {
        createSQLQuery("delete from SYS_PERMISSION where ROLE_ID=?", roleId).executeUpdate();
    }

    /**
     * 根据权限资源ID删除资源和角色的关系
     *
     * @param resourceId 权限资源ID
     */
    public void deletePermissionByResourceId(String resourceId) {
        createSQLQuery("delete from SYS_PERMISSION where RESOURCES_ID=?", resourceId).executeUpdate();
    }


    /**
     * 根据ID查所有资源
     *
     * @param resourceIds
     * @return
     */
    public List<ResourcesSimpleVO> listResourceByResourceIds(Set<String> resourceIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  res.ID as id,res.PERMISSION as permission,res.MENU_ID as menuId ")
                .append(" from SYS_RESOURCES res ")
                .append(" WHERE 1=1 and ");
        String resourceIdCon = SqlUtil.getInSql(Lists.newArrayList(resourceIds), "res.ID", true);
//        Map<String, Object> map = new HashMap<>();
//        map.put("resourceIds", resourceIds);
        return listBySQLAliasToBean(sql.toString() + resourceIdCon, null, ResourcesSimpleVO.class);
    }

    /**
     * 根据资源ID集合查询
     *
     * @param menuId 菜单id
     * @return
     */
    public List<Resources> listByMenuId(String menuId) {
        String hql = "from Resources where menuId = :menuId ";
        Map<String, Object> map = new HashMap<>();
        map.put("menuId", menuId);
        return createQuery(hql, map).list();
    }

    public List<RoleResourceDTO> listRoleResourceByRole(String roleIds) {
        String sql = " SELECT t1.ROLE_ID AS roleId, t2.NAME AS roleName, t1.RESOURCES_ID AS resourceId FROM sys_permission t1 INNER JOIN sys_role t2 ON t2.ID = t1.ROLE_ID  WHERE 1 = 1 ";
        Map<String, Object> map = new HashMap<>();
        if (CharSequenceUtil.isNotEmpty(roleIds)) {
            sql += " AND ROLE_ID IN (:roleIds)";
            map.put("roleIds", roleIds.split(","));
        }
        return listBySQLAliasToBeanNew(sql, map, RoleResourceDTO.class);
    }



    /**
     * 获取所有资源
     * @return
     */
    public List<Resources> listResourceByTenant() {
        String sql = "select ID as id, DESCRIPTION as description, NAME as name,PERMISSION as permission, STATUS as status, URL as url  from sys_resources ";
        return listBySQLAliasToBeanNew(sql, null, Resources.class);
    }
}
