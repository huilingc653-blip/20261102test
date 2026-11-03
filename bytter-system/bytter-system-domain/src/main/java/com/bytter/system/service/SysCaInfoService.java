package com.bytter.system.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.dao.SysCaInfoDao;
import com.bytter.system.entity.SysCaInfo;
/**
 * @author qijh
 * @version 1.0
 * @Title: Service
 * @Description: SYS_CA_INFO
 * @date 2017-10-26 17:03:01
 */
@Service("sysCaInfoService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SysCaInfoService extends BaseService<SysCaInfo, String> {
    @Autowired
    private SysCaInfoDao sysCaInfoDao;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(SysCaInfo entity) {
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
    public void save(SysCaInfo entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(SysCaInfo entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(SysCaInfo entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<SysCaInfo, String> getEntityDao() {
        return sysCaInfoDao;
    }
}
