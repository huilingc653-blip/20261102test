package com.bytter.system.service;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.dao.ServiceConsumeTaskDao;
import com.bytter.system.entity.SysTask;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: SYS_TASK
 * @date 2017-06-19 18:44:40
 */
@Service("serviceConsumeService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class SysTaskService extends BaseService<SysTask, String> {
    @Autowired
    private ServiceConsumeTaskDao serviceConsumeTaskDao;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(SysTask entity) {
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
    public void saveServiceConsume(SysTask entity, String loginName) {
        if (entity.getDeletedsw() == null) {
            entity.setDeletedsw(0);
        }
        entity.setType(3);
        entity.setCreateBy(loginName);
        entity.setCreateDate(new Date());
        entity.setUpdateBy(loginName);
        entity.setUpdateDate(new Date());
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(SysTask entity, String loginName) {
        if (entity.getDeletedsw() == null) {
            entity.setDeletedsw(0);
        }
        entity.setUpdateBy(loginName);
        entity.setUpdateDate(new Date());
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(SysTask entity, String loginName) {
        if (entity.getDeletedsw() == null) {
            entity.setDeletedsw(0);
        }
        if (StringUtils.isBlank(entity.getId())) {
            entity.setType(3);
            entity.setCreateBy(loginName);
            entity.setCreateDate(new Date());
        }
        entity.setUpdateBy(loginName);
        entity.setUpdateDate(new Date());
        super.saveOrUpdate(entity);
    }
    public BaseDao<SysTask, String> getEntityDao() {
        return serviceConsumeTaskDao;
    }
    public DataTablesOutput getConsumeTaskDataTables(DataTablesInput input) {
        IPageResult<SysTask> pageResult = serviceConsumeTaskDao.listConsumeTask(input);
        return pageResult.convert(DataTablesOutput.class);
    }
}
