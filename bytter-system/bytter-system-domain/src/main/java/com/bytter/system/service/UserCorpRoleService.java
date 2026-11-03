package com.bytter.system.service;

import com.bytter.system.dao.UserCorpRoleDao;
import com.bytter.system.entity.UserCorpRole;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserCorpRoleService extends BaseService<UserCorpRole, String> {

    @Autowired
    private UserCorpRoleDao userCorpRoleDao;

    @Override
    public BaseDao<UserCorpRole, String> getEntityDao() {
        return userCorpRoleDao;
    }

    public boolean assignUserCorpRole(String userId, String corpId, List<String> roles) {
        String deleteSql="delete from sys_user_corp_role where user_id = :userId and corp_id = :corpId";
        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("corpId",corpId);
        userCorpRoleDao.batchSQLExecute(deleteSql, params);
        if(roles ==null || roles.isEmpty()){
            return true;
        }
        List<UserCorpRole> userCorpRoles = new ArrayList<>();
        roles.forEach(role->{
            UserCorpRole userCorpRole = new UserCorpRole();
            userCorpRole.setUserId(userId);
            userCorpRole.setCorpId(corpId);
            userCorpRole.setRoleId(role);
            userCorpRoles.add(userCorpRole);
        });
        return userCorpRoleDao.batchSave(userCorpRoles) > 0;
    }

    public List<UserCorpRole> userCorpRoles(String userId, String corpId) {
        String sql = "select * from sys_user_corp_role where user_id = :userId and corp_id = :corpId";
        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("corpId",corpId);
        return userCorpRoleDao.listBySQLAliasToBean(sql, params, UserCorpRole.class);
    }

    public void copyUserCorpRole(String userId, String sourceCorpId, List<String> corps) {
        if(corps == null || corps.isEmpty()){
            return;
        }
        String querySql = "select * from sys_user_corp_role where user_id = :userId and corp_id=:sourceCorpId";
        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("sourceCorpId",sourceCorpId);
        List<UserCorpRole> userCorpRoles = userCorpRoleDao.listBySQLAliasToBean(querySql, params, UserCorpRole.class);
        String inCorps = SqlUtil.getInSql(corps, "corp_id", true);
        String delSql = "delete from sys_user_corp_role where user_id = :userId and " + inCorps;
        userCorpRoleDao.createSQLQuery(delSql, params).executeUpdate();
        List<UserCorpRole> copyCorpRoleList = new ArrayList<>();
        for (String corp : corps) {
            for (UserCorpRole userCorpRole : userCorpRoles) {
                UserCorpRole copyUserCorpRole = new UserCorpRole();
                copyUserCorpRole.setUserId(userId);
                copyUserCorpRole.setCorpId(corp);
                copyUserCorpRole.setRoleId(userCorpRole.getRoleId());
                copyCorpRoleList.add(copyUserCorpRole);
            }
        }
        userCorpRoleDao.batchSave(copyCorpRoleList);
    }
}
