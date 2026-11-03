package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.system.entity.NotificationUser;
import com.bytter.system.vo.NotificationUserVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zhoul
 * @date 2017/4/13
 */
@Repository
public class NotificationUserDao extends BaseDao<NotificationUser, String> {


    /**
     * 根据提醒分类删除用户ID
     * @param categoryId 分类ID
     */
    public void deleteByCategoryId(String categoryId) {
        String hql = "delete from NotificationUser where categoryId=?0";
        createQuery(hql, categoryId).executeUpdate();
    }

    public List<NotificationUserVO> listNotifyUserByCategoryId(String categoryId) {
        String sql = "select snu.id,snu.category_id as categoryId,snu.user_id as userId,su.login_name as userName " +
                " from sys_notification_user snu left join sys_user su on snu.user_id = su.id " +
                " where snu.category_id = :categoryId ";
        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", categoryId);
        return listBySQLAliasToBean(sql, params, NotificationUserVO.class);
    }

//    /**
//     * 根据提醒分类获取角色集合
//     * @param categoryId 分类ID
//     */
//    public List<String> getRoleByCategoryId(String categoryId) {
//        String sql = "select distinct ROLE_ID from SYS_NOTIFICATION_ROLE where category_Id = :categoryId ";
//
//        SQLQuery sqlQuery = createSQLQuery(sql);
//        sqlQuery.setParameter("categoryId", categoryId);
//        sqlQuery.addScalar("ROLE_ID", StandardBasicTypes.STRING);
//        List<String> roleIds = sqlQuery.list();
//        if (EmptyUtils.isEmpty(roleIds)) {
//            return new ArrayList<>();
//        }
//        return roleIds;
//    }

}
