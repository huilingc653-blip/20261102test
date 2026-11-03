package com.bytter.system.dao;
import com.bytter.system.entity.SysConfigLog;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
@Repository
public class SysConfigLogDao extends BaseDao<SysConfigLog, String> {
    public void saveEntity(SysConfigLog sysConfigLog) {
        save(sysConfigLog);
    }
}
