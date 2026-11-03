package com.bytter.system.dao;
import com.bytter.system.entity.NotificationRole;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class NotificationRoleDao extends BaseDao<NotificationRole, String> {
    /**
     * 根据提醒分类删除角色ID
     *
     * @param categoryId 分类ID
     */
    public void deleteByCategoryId(String categoryId) {
        String hql = "delete from NotificationRole where categoryId=?0";
        createQuery(hql, categoryId).executeUpdate();
    }
    /**
     * 根据提醒分类获取角色集合
     *
     * @param categoryId 分类ID
     */
    public List<String> getRoleByCategoryId(String categoryId) {
        String sql = "select distinct ROLE_ID from SYS_NOTIFICATION_ROLE where category_Id = :categoryId ";
        SQLQuery sqlQuery = createSQLQuery(sql);
        sqlQuery.setParameter("categoryId", categoryId);
        sqlQuery.addScalar("ROLE_ID", StandardBasicTypes.STRING);
        List<String> roleIds = sqlQuery.list();
        if (EmptyUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        return roleIds;
    }
}
