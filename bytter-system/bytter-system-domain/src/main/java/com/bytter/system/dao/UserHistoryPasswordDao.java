package com.bytter.system.dao;
import com.bytter.system.entity.UserHistoryPassword;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 用户DAO层
 *
 * @author zuoy 2017/11/15
 */
@Repository
public class UserHistoryPasswordDao extends BaseDao<UserHistoryPassword, String> {
    /**
     * 查询最近三次的旧密码
     *
     * @param userId 用户ID
     * @return
     */
    public List<UserHistoryPassword> listOldThreePasswordByUserId(String userId, Integer maxResult) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        hql.append(" from UserHistoryPassword t where t.userId = :userId order by createTime desc ");
        params.put("userId", userId);
        return this.listByHQL(hql.toString(), 0, maxResult, params);
    }
    /**
     * 查询最后一次密码
     *
     * @param userId 用户ID
     * @return
     */
    public UserHistoryPassword getLastPasswordBy(String userId) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        hql.append(" from UserHistoryPassword t where t.userId = :userId order by createTime desc ");
        params.put("userId", userId);
        List<UserHistoryPassword> list = this.listByHQL(hql.toString(), params);
        if (EmptyUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }
}
