package com.bytter.basic.service;
import com.bytter.basic.dao.SysTimeZoneDao;
import com.bytter.basic.entity.SysTimeZone;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 系统时区表
 * @date 2020-1-19 11:14:22
 */
@Service("sysTimeZoneServiceBase")
@Transactional
public class SysTimeZoneService extends BaseService<SysTimeZone, String> {
    @Autowired
    private SysTimeZoneDao sysTimeZoneDao;
    @Override
    public BaseDao<SysTimeZone, String> getEntityDao() {
        return sysTimeZoneDao;
    }
}
