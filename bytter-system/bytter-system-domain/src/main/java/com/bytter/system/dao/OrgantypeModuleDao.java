package com.bytter.system.dao;
import com.bytter.system.entity.OrgantypeModule;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengg
 * @version 1.0
 * @Title: Dao
 * @Description: 组织架构对应关系维护表数据库操作
 * @date 2020-05-14 16:01:41
 */
@Repository
public class OrgantypeModuleDao extends BaseDao<OrgantypeModule, String> {
    /**
     * 根据模块代码获取值
     *
     * @param sysCode 模块代码
     * @return
     */
    public OrgantypeModule getBySysCode(String sysCode) {
        String hql = "from OrgantypeModule where sysCode=?0 ";
        return getUnique(hql, sysCode);
    }

    public List<OrgantypeModule> queryBySysCode(List<String> sysCodes) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from OrgantypeModule where sysCode in (:sysCodes) ";
        params.put("sysCodes", sysCodes);
        return listByHQLAliasToBean(hql, params, OrgantypeModule.class);
    }
}
