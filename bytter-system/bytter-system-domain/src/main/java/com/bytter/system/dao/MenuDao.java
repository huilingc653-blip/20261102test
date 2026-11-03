package com.bytter.system.dao;
import com.bytter.system.api.constant.MenuConstant;
import com.bytter.system.entity.Menu;
import com.bytter.system.vo.MenuIndexVO;
import com.bytter.system.vo.MenuSimpleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Lists;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.*;
/**
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class MenuDao extends BaseDao<Menu, String> implements Serializable {
    @Override
    public List<Menu> listAll() {
        String hql = "from Menu m order by sort";
        return createQuery(hql).list();
    }
    public List<MenuSimpleVO> listByParentId(String parentId) {
        StringBuilder hql = new StringBuilder("select m.name as name,m.id as id, m.isExtend as isExtend, ");
        hql.append("(select count(*) from Menu menu where menu.parentId = m.id ) as childrenSize, ");
        hql.append("(select count(*) from Resources r where r.menuId = m.id ) as resourceSize ");
        hql.append("from Menu m where ");
        if (StringUtils.isBlank(parentId)) {
            hql.append("m.parentId=null or m.parentId=''");
            hql.append(" order by m.sort");
            return listByHQLAliasToBean(hql.toString(), MenuSimpleVO.class, true);
        } else {
            hql.append("m.parentId=:parentId");
            hql.append(" order by m.sort");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("parentId", parentId);
            return listByHQLAliasToBean(hql.toString(), map, MenuSimpleVO.class, true);
        }
    }
    public List<MenuSimpleVO> listByMenuList(List<Menu> menuList) {
        List<String> menuIds = new ArrayList<>();
        for (Menu menu : menuList) {
            menuIds.add(menu.getId());
        }
        return listByMenuId(menuIds);
    }
    public List<MenuSimpleVO> listByMenuId(List<String> menuIds) {
        StringBuilder hql = new StringBuilder("select m.name as name,m.id as id,m.parentId as parentId,m.isExtend as isExtend,");
        hql.append("(select count(*) from Menu menu where menu.parentId = m.id ) as childrenSize, ");
        hql.append("(select count(*) from Resources r where r.menuId = m.id ) as resourceSize ");
        hql.append("from Menu m where 1=1 and " + SqlUtil.getInSql(menuIds, "m.id", true));
        hql.append(" order by m.sort");
        return listByHQLAliasToBean(hql.toString(), MenuSimpleVO.class);
    }
    /**
     * 根据用户ID获取该用户所拥有角色的菜单
     *
     * @param userId
     * @return
     */
    public List<MenuSimpleVO> listMenusByUserId(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT m.id as id,m.PARENT_ID as parentId,m.NAME as name,m.SORT as sort,m.is_Extend as isExtend FROM ");
        sql.append(" SYS_MENU m LEFT JOIN SYS_ROLE_MENU rm ON m.ID = rm.MENU_ID ");
        sql.append(" LEFT JOIN SYS_ROLE r ON r.ID = rm.ROLE_ID ");
        sql.append(" LEFT JOIN SYS_USER_ROLE ur ON r.ID = ur.ROLE_ID ");
        sql.append(" WHERE ur.USER_ID =:userId AND m.status=1 order by m.sort");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        return listBySQLAliasToBean(sql.toString(), map, MenuSimpleVO.class);
    }
    /**
     * 根据用户ID获取该用户所拥有角色的菜单
     *
     * @param userId
     * @return
     */
    public List<MenuIndexVO> listMenuIdsByUserId(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT m.id,m.name,m.PARENT_ID as parentId FROM ");
        sql.append(" SYS_MENU m LEFT JOIN SYS_ROLE_MENU rm ON m.ID = rm.MENU_ID ");
        sql.append(" LEFT JOIN SYS_ROLE r ON r.ID = rm.ROLE_ID ");
        sql.append(" LEFT JOIN SYS_USER_ROLE ur ON r.ID = ur.ROLE_ID ");
        sql.append(" WHERE ur.USER_ID =:userId AND m.status=1 ");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        return listBySQLAliasToBean(sql.toString(), map, MenuIndexVO.class);
    }
    public List<Menu> listByMenuIds(String locale, List<String> ids) {
        String sql = "SELECT m.id AS id,m.PARENT_ID AS parentId,bt_nvl_char( ib.LANG_CONTEXT, m.name ) as name,m.url as url,m.icon as icon,m.old_icon as oldIcon,m.sort as sort, m.is_Extend as isExtend " +
                "FROM sys_menu m LEFT JOIN SYS_INTERNATION_BASE ib ON m.id = ib.KEY_ID and ib.LANG_CODE=:locale where  ";
        Map<String, Object> map = new HashMap<>();
        sql += SqlUtil.getInSqlByParams(ids, "m.id", map);
//        map.put("ids", ids);
        map.put("locale", locale);
        return listBySQLAliasToBean(sql, map, Menu.class);
    }
    public void deleteRoleMenu(String menuId) {
        createSQLQuery("delete from SYS_ROLE_MENU where MENU_ID=?", menuId).executeUpdate();
    }
    /**
     * 查找被分配到模块的菜单id
     *
     * @return
     */
    public List<String> listMenuIdsAssgnInModule() {
/*        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT MENU_ID FROM  SYS_MODULE_MENU ");*/
        String sql = "SELECT DISTINCT MENU_ID FROM  SYS_MODULE_MENU";
        SQLQuery sqlQuery = createSQLQuery(sql);
        return sqlQuery.list();
    }
    /**
     * 根据用户ID获取该用户所拥有角色的菜单
     *
     * @param userId
     * @return
     */
    public List<String> listFirstLayerMenuIdsByUserId(String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT m.id as id FROM ");
        sql.append(" SYS_MENU m LEFT JOIN SYS_ROLE_MENU rm ON m.ID = rm.MENU_ID ");
        sql.append(" LEFT JOIN SYS_ROLE r ON r.ID = rm.ROLE_ID ");
        sql.append(" LEFT JOIN SYS_USER_ROLE ur ON r.ID = ur.ROLE_ID ");
        sql.append(" WHERE ur.USER_ID =:userId AND m.status=1 AND m.parent_id is null order by m.sort");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<Map> mapList = this.listBySQL(sql.toString(), map);
        List<String> ids = Lists.newArrayList();
        if (EmptyUtils.isEmpty(mapList)) {
            return new ArrayList<>();
        }
        for (Map recode : mapList) {
            ids.add(((String) recode.get("id")));
        }
        return ids;
//        return listBySQLAliasToBean(sql.toString(), map, MenuSimpleVO.class);
    }
    /**
     * 根据用户ID获取该用户所拥有角色的菜单
     *
     * @param userId
     * @return
     */
    public List<String> listMenuIdsByUserIdAndParentIds(String userId, Set<String> parentIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT m.id as id from ");
        sql.append(" SYS_MENU m LEFT JOIN SYS_ROLE_MENU rm ON m.ID = rm.MENU_ID ");
        sql.append(" LEFT JOIN SYS_ROLE r ON r.ID = rm.ROLE_ID ");
        sql.append(" LEFT JOIN SYS_USER_ROLE ur ON r.ID = ur.ROLE_ID ");
        sql.append(" WHERE ur.USER_ID =:userId AND m.status=1 AND m.parent_id in (:parentIds) order by m.sort");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("parentIds", parentIds);
        List<Map> mapList = this.listBySQL(sql.toString(), map);
        List<String> ids = Lists.newArrayList();
        if (EmptyUtils.isEmpty(mapList)) {
            return new ArrayList<>();
        }
        for (Map recode : mapList) {
            ids.add(((String) recode.get("id")));
        }
        return ids;
//        return listBySQLAliasToBean(sql.toString(), map, MenuSimpleVO.class);
    }
    /**
     * 获取最顶级菜单
     *
     * @param locale
     * @return
     */
    public List<Menu> listTopMenu(String locale) {
        String sql = "SELECT m.id AS id,m.PARENT_ID AS parentId,bt_nvl_char( ib.LANG_CONTEXT, m.name ) as name,m.url as url,m.icon as icon,m.old_icon as oldIcon,m.sort as sort, m.is_Extend as isExtend " +
                "FROM sys_menu m LEFT JOIN SYS_INTERNATION_BASE ib ON m.id = ib.KEY_ID and ib.LANG_CODE=:locale where m.parent_id is null";
        Map<String, Object> map = new HashMap<>();
        map.put("locale", locale);
        return listBySQLAliasToBean(sql, map, Menu.class);
    }
    /**
     * 查询用户可以查看（有权限）的且启用的菜单id列表
     *
     * @param userId 用户ID
     * @return 菜单id列表
     */
    public List<Menu> listEnableMenuByUserId(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SRM.MENU_ID AS id, SM.PARENT_ID AS parentId, SM.NAME AS name, SM.SORT AS sort ");
        sql.append("FROM SYS_ROLE_MENU SRM ");
        sql.append("JOIN SYS_MENU SM ON SM.ID = SRM.MENU_ID  AND SM.STATUS = 1 ");
        sql.append("AND SRM.ROLE_ID IN (SELECT SUR.ROLE_ID FROM SYS_USER_ROLE SUR WHERE USER_ID = :userId) ");
        return listBySQLAliasToBean(sql.toString(), params, Menu.class);
    }
    /**
     * 查询用户可以查看（有权限）的且启用的菜单id列表
     *
     * @param userId 用户ID
     * @return 菜单id列表
     */
    public List<String> listEnableMenuIdByUserId(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT SRM.MENU_ID ");
        sql.append("FROM SYS_ROLE_MENU SRM ");
        sql.append("JOIN SYS_MENU SM ON SM.ID = SRM.MENU_ID  AND SM.STATUS = 1 ");
        sql.append("AND SRM.ROLE_ID IN (SELECT SUR.ROLE_ID FROM SYS_USER_ROLE SUR WHERE USER_ID = :userId) ");
        SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
        return sqlQuery.list();
    }
    public List<Menu> listAllMenuIdNames(String locale) {
        String sql = "SELECT m.id AS id,ib.LANG_CONTEXT as name, m.name as description,m.parent_Id as parentId " +
                "FROM sys_menu m LEFT JOIN SYS_INTERNATION_BASE ib ON m.id = ib.KEY_ID and ib.LANG_CODE=:locale where  m.status=1 ";
        Map<String, Object> map = new HashMap<>();
        map.put("locale", locale);
        return listBySQLAliasToBean(sql, map, Menu.class);
    }
    public List<MenuIndexVO> listByIdsName(List<String> menuIds, String menuName) {
        String sql = "select m.id as id,m.name as name,m.parent_Id as parentId,m.parent_Ids as parentIds,m.url as url FROM sys_menu m ";
        sql += " where m.url is not null";
        Map<String, Object> params = new HashMap<>();
        if (EmptyUtils.isNotEmpty(menuIds)) {
            sql += " and " + SqlUtil.getInSqlByParams(menuIds, "m.id", params);
        }
        if (EmptyUtils.isNotEmpty(menuName)) {
            sql += " and  m.name like BT_CONCAT3('%',:menuName,'%')";
            params.put("menuName", menuName);
        }
        sql += " order by m.sort";
        return listBySQLAliasToBean(sql.toString(), params, MenuIndexVO.class);
    }
    /**
     * 获取所有可用的菜单ID和菜单Url，用于鉴权。
     *
     * @return 菜单ID和Url集合。
     */
    public List<Map<String, String>> listEnableMenusUrl() {
        // String sql = "SELECT u.id AS userId,u.CORP_ID AS corpId FROM SYS_USER u left join SYS_USER_ROLE ur ON ur.USER_ID = u.ID WHERE ur.ROLE_ID = ?";
        String sql = "SELECT m.ID as menuId,m.URL as menuUrl from SYS_MENU m WHERE m.STATUS = ?";
        SQLQuery sqlQuery = createSQLQuery(sql, MenuConstant.STATUS_ENABLE);
        sqlQuery.addScalar("menuId", StandardBasicTypes.STRING);
        sqlQuery.addScalar("menuUrl", StandardBasicTypes.STRING);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return sqlQuery.list();
    }

    public List<MenuIndexVO> getTenantMenuIds(String tenantId) {
        String sql = "SELECT m.id as menuId from sys_menu m where m.tenant_id = :tenantId";
        Map<String, Object> params = new HashMap<>();
        params.put("tenantId", tenantId);
        return listBySQLAliasToBean(sql.toString(), params, MenuIndexVO.class);
    }
}
