package com.bytter.system.dao;
import cn.hutool.core.text.CharSequenceUtil;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.RoleResourceDTO;
import com.bytter.system.api.dto.UserRoleDTO;
import com.bytter.system.entity.Role;
import com.bytter.system.vo.RoleVo;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.hibernate.id.UUIDGenerator;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 角色DAO层
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class RoleDao extends BaseDao<Role, String> {
    /**
     * 根据角色ID,删除角色和用户的数据
     *
     * @param roleId 角色ID
     */
    public List<String> listUserIdsByRoleId(String roleId) {
        return createSQLQuery("select user_id from SYS_USER_ROLE where role_id=?", roleId).list();
    }
    /**
     * 根据角色ID,删除角色和用户的数据
     *
     * @param roleId 角色ID
     */
    public void deleteUserRoleByRoleId(String roleId, List<String> userIds) {
    	if(!CollectionUtils.isEmpty(userIds)) {
	        String userIdCon = SqlUtil.getInSql(userIds, "user_id", true);
	        createSQLQuery("delete from SYS_USER_ROLE where ROLE_ID=? and " + userIdCon, roleId).executeUpdate();
    	}
    }
    /**
     * 根据角色ID,删除角色和用户的数据
     *
     * @param roleId 角色ID
     */
    public void deleteUserRoleByRoleId(String roleId) {
        createSQLQuery("delete from SYS_USER_ROLE where ROLE_ID=?", roleId).executeUpdate();
    }
    /**
     * 根据用户ID，删除角色和用户的数据
     *
     * @param userId 用户ID
     */
    public void deleteUserRoleByUserId(String userId) {
        createSQLQuery("delete from SYS_USER_ROLE where USER_ID=?", userId).executeUpdate();
    }
    /**
     * 根据用户ID，删除角色和用户的数据
     *
     * @param userId 用户ID
     */
    public void deleteUserRoleByUserIdAndRoleId(String userId, String roleId) {
        createSQLQuery("delete from SYS_USER_ROLE where USER_ID=? and ROLE_ID=?", userId, roleId).executeUpdate();
    }
    /**
     * 根据角色ID，删除角色和菜单的关系
     *
     * @param roleId 角色ID
     */
    public void deleteByRoleId(String roleId) {
        createSQLQuery("delete from SYS_ROLE_MENU where ROLE_ID=?", roleId).executeUpdate();
    }
    public void insertUserRole(String userId, String roleId) {
        IdentifierGenerator identifierGenerator = UUIDGenerator.buildSessionFactoryUniqueIdentifierGenerator();
        String id = (String) identifierGenerator.generate(null, null);
        createSQLQuery("insert into SYS_USER_ROLE (USER_ID,ROLE_ID,ID) VALUES(?,?,?)", userId, roleId, id).executeUpdate();
    }
    public List<String> findMenuIdByRoleId(String roleId) {
        String sql = "select MENU_ID as menuId from SYS_ROLE_MENU where ROLE_ID = ?";
        SQLQuery sqlQuery = createSQLQuery(sql, roleId);
        sqlQuery.addScalar("menuId", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }
    public List<String> findResourceIdByRoleId(String roleId) {
        String sql = "select RESOURCES_ID as resourceId from SYS_PERMISSION where ROLE_ID = ?";
        SQLQuery sqlQuery = createSQLQuery(sql, roleId);
        sqlQuery.addScalar("resourceId", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }
    public List<Map<String, String>> findUserByRoleId(String roleId) {
        String sql = "SELECT u.id AS userId,u.CORP_ID AS corpId FROM SYS_USER u left join SYS_USER_ROLE ur ON ur.USER_ID = u.ID WHERE ur.ROLE_ID = ?";
        SQLQuery sqlQuery = createSQLQuery(sql, roleId);
        sqlQuery.addScalar("userId", StandardBasicTypes.STRING);
        sqlQuery.addScalar("corpId", StandardBasicTypes.STRING);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }
    public IPageResult<RoleVo> getAllRole(TableGridInput input) {
        Map<String, Object> map = new HashMap<String, Object>();
        String name = (String) input.get("name");
        String code = (String) input.get("code");
        StringBuilder sql = new StringBuilder();
        sql.append(" select code,name,id,description,useable from sys_role where 1=1 ");
        if (StringUtils.isNotBlank(name)) {
            sql.append("and name like :name ");
            map.put("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(code)) {
            sql.append("and code like :code ");
            map.put("code", "%" + code + "%");
        }
        String order = input.getOrder();
        String sort = input.getSort();
        if (StringUtils.isNotBlank(sort) && StringUtils.isNotBlank(order)) {
            sql.append("order by ").append(order).append(" ").append(sort);
        }
        return getPageResultBySQL(input, sql.toString(), map, RoleVo.class);
    }
//    /**
//     * 根据查询条件查询符合条件的角色信息
//     * @param params 角色查询条件
//     * @return 符合条件的角色信息
//     */
//    public List<Role> getAllRole(Map<String,Object> params){
//    	StringBuilder hql=new StringBuilder("from Role r where 1=1 ");
//    	if(params.containsKey("name")){
//    		hql.append(" and r.name like :name ");
//    	}
//    	hql.append(" order by r.createTime asc ");
//    	return this.listByHQL(hql.toString(), params);
//    }
    public List<RoleVo> findSpecialRoleByCorpId(String corpId) {
        final StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT role.* FROM SYS_ROLE role");
        HashMap<String, Object> map = new HashMap<>();
        return listBySQLAliasToBean(sql.toString(), map, RoleVo.class);
    }

    public List<RoleVo> findAllRoles() {
        String sql = "SELECT DISTINCT role.* FROM SYS_ROLE role";
        HashMap<String, Object> map = new HashMap<>();
        return listBySQLAliasToBean(sql, map, RoleVo.class);
    }

    public List<Role> listByIds(List<String> ids) {
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        String hql = "from Role where id in :ids";
        Query query = createQuery(hql, map);
        return query.list();
    }
//    /**
//     * 根据角色ID获取用户ID列表
//     *
//     * @param roleId
//     * @return
//     */
//    public List<String> getUserIdListByRoleId(String roleId) {
//        StringBuffer sql = new StringBuffer();
//        sql.append(" SELECT DISTINCT sur.USER_ID AS userId FROM SYS_USER_ROLE sur WHERE ROLE_ID = ? ");
//        SQLQuery sqlQuery = createSQLQuery(sql.toString(), roleId);
//        sqlQuery.addScalar("userId", StandardBasicTypes.STRING);
//        return sqlQuery.list();
//    }
    public List<String> getRoleIdsByUserId(String userId) {
        String sql = "select ROLE_ID from SYS_USER_ROLE where USER_ID=?";
        SQLQuery sqlQuery = createSQLQuery(sql, userId);
        sqlQuery.addScalar("ROLE_ID", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }

    /**
     * 根据用户ID获取角色ID列表。
     *
     * @param userId 用户ID。
     * @param tenantId 租户ID。
     * @return 角色ID列表。
     */
    public List<String> getRoleIdsByUserId(String userId, String tenantId) {
        String sql = "select ROLE_ID from SYS_USER_ROLE where USER_ID=? and TENANT_ID=? ";
        SQLQuery sqlQuery = createSQLQuery(sql, userId, tenantId);
        sqlQuery.addScalar("ROLE_ID", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }

    public List<UserRoleDTO> listUserRole(String userIds, String roleIds) {
        String sql = "SELECT USER_ID AS userId, ROLE_ID AS roleId FROM sys_user_role WHERE 1 = 1 ";
        Map<String, Object> map = new HashMap<>();
        if (CharSequenceUtil.isNotEmpty(userIds)) {
            sql += " AND USER_ID IN (:userIds) ";
            map.put("userIds", userIds.split(","));
        }
        if (CharSequenceUtil.isNotEmpty(roleIds)) {
            sql += " AND ROLE_ID IN (:roleIds) ";
            map.put("roleIds", roleIds.split(","));
        }
        return listBySQLAliasToBean(sql, map, UserRoleDTO.class);
    }

    public List<RoleResourceDTO> listUserRoleResource(String userId, String roleIds) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r.ID AS roleId, r.NAME AS roleName, resources.RESOURCES_ID AS resourceId ")
                .append("FROM SYS_ROLE r ")
                .append("JOIN SYS_PERMISSION resources ON resources.ROLE_ID = r.ID ")
                .append("JOIN SYS_USER_ROLE sur ON sur.ROLE_ID = r.ID ")
                .append("WHERE sur.USER_ID = :userId ");
        if (CharSequenceUtil.isNotEmpty(roleIds)){
            sql.append("AND r.ID IN (:roleIds) ");
            params.put("roleIds", roleIds.split(","));
        }
        params.put("userId", userId);
        return listBySQLAliasToBean(sql.toString(), params, RoleResourceDTO.class);
    }

    public List<UserRoleDTO> getUserRole(Set<String> userIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sr.NAME as roleName, sur.USER_ID AS userId\n" +
                "FROM SYS_USER_ROLE sur\n" +
                "LEFT JOIN SYS_ROLE sr ON SUR.role_id = sr.ID \n" +
                "WHERE sur.USER_ID in :userIds");
        Map<String, Object> params = new HashMap<>();
        params.put("userIds", EmptyUtils.isEmpty(userIds)?null:userIds);
        return listBySQLAliasToBean(sql.toString(), params, UserRoleDTO.class);
    }
}
