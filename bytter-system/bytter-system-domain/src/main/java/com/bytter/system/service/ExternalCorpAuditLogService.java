package com.bytter.system.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.dao.ExternalCorpAuditLogDao;
import com.bytter.system.entity.ExternalCorpAuditLog;
/**
 * @author lifh
 * @description
 * @createDate 2019/3/11
 */
@Service("externalCorpAuditLogService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExternalCorpAuditLogService extends BaseService<ExternalCorpAuditLog, String> {
    @Autowired
    private ExternalCorpAuditLogDao externalCorpAuditLogDao;
    @Override
    public BaseDao<ExternalCorpAuditLog, String> getEntityDao() {
        return externalCorpAuditLogDao;
    }
    /**
     * 根据客商id查找审核日志
     *
     * @param id
     * @return
     */
    public List<ExternalCorpAuditLog> getLogListByCorpId(String id) {
        return externalCorpAuditLogDao.getLogListByCorpId(id);
    }
    /**
     * 根据客商id查找审核日志
     *
     * @param id
     * @return
     */
    public void deleteByCorpId(String id) {
        externalCorpAuditLogDao.deleteByCorpId(id);
    }
}
