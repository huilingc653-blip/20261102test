package com.bytter.system.dao;
import com.bytter.system.entity.Module;
import com.bytter.core.base.BaseDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * 模块DAO层
 * Created by zhoul on 2017/12/18.
 */
@Repository
public class ModuleDao extends BaseDao<Module, String> {
    public List<String> listModuleMenuByModuleId(String moduleId) {
        String sql = "select MENU_ID as menuId from SYS_MODULE_MENU where MODULE_ID=?";
        SQLQuery sqlQuery = createSQLQuery(sql, moduleId);
        sqlQuery.addScalar("menuId", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }
    public List<String> listModuleMenuByModuleId(List<String> moduleIds) {
        String sql = "select MENU_ID as menuId from SYS_MODULE_MENU where MODULE_ID in (:moduleIds)";
        Map<String, Object> map = new HashMap<>();
        map.put("moduleIds", moduleIds);
        SQLQuery sqlQuery = createSQLQuery(sql, map);
        sqlQuery.addScalar("menuId", StandardBasicTypes.STRING);
        return sqlQuery.list();
    }
    /**
     * 移除模块和菜单的关系
     *
     * @param moduleId 模块ID
     */
    public void removeModuleMenuRelation(String moduleId) {
        String sql = "delete from SYS_MODULE_MENU WHERE MODULE_ID = ?";
        createSQLQuery(sql, moduleId).executeUpdate();
    }
    public void saveModuleMenu(String moduleId, Set<String> menus) {
        Session session = getSession();
        List<String> menuList = new ArrayList<>(menus);
        for (int i = 0; i < menuList.size(); i++) {
            String menuId = menuList.get(i);
            createSQLQuery("INSERT INTO SYS_MODULE_MENU (MODULE_ID,MENU_ID) VALUES(?,?)", moduleId, menuId).executeUpdate();
            if (i % 100 == 0) {
                session.flush();
            }
        }
        session.flush();
    }
}
