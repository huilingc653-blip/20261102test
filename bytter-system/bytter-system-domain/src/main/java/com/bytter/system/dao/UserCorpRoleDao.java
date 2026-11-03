package com.bytter.system.dao;

import com.bytter.system.entity.UserCorpRole;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserCorpRoleDao extends BaseDao<UserCorpRole,String> {

    public List<UserCorpRole> listByCorpIdAndRoleId(String corpId, String... roleIds) {
        if(roleIds == null || roleIds.length == 0) {
            return new ArrayList<>();
        }
        if (StringUtils.isEmpty(corpId)) {
            return new ArrayList<>();
        }
        String sql="select * from sys_user_corp_role where corp_id=:corpId and role_id in (:roleIds)";
        Map<String,Object> map=new HashMap<>();
        map.put("corpId",corpId);
        map.put("roleIds",roleIds);
        return this.listBySQLAliasToBean(sql, map, UserCorpRole.class);
    }
}
