package com.bytter.credit.domain.service;
import com.bytter.credit.domain.dao.RatProcessLogDao;
import com.bytter.credit.domain.entity.RatProcessLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
 * @Title: Service
 * @Description: RAT_PROCESS_LOG
 * @author onlineGenerator
 * @date 2017-07-20 14:49:34
 * @version 1.0
 *
 */
@Service("ratProcessLogService")
@Transactional
public class RatProcessLogService extends BaseService<RatProcessLog, String> {
    @Autowired
    private RatProcessLogDao ratProcessLogDao;
    /**
     * 删除对象
     * 
     * @param entity
     */
    public void deleteEntity(RatProcessLog entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     * 
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     * 
     * @param entity
     */
    public void save(RatProcessLog entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     * 
     * @param entity
     */
    public void update(RatProcessLog entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     * 
     * @param entity
     */
    public void saveOrUpdate(RatProcessLog entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RatProcessLog, String> getEntityDao() {
        return ratProcessLogDao;
    }
}
