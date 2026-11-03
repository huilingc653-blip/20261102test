package com.bytter.system.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.dao.ExternalCorpAndCorpDao;
import com.bytter.system.entity.ExternalCorpAndCorp;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description:
 * @date 2018/6/15 9:37
 */
@Service("externalCorpAndCorpService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExternalCorpAndCorpService extends BaseService<ExternalCorpAndCorp, String> {
    @Autowired
    private ExternalCorpAndCorpDao externalCorpAndCorpDao;
    @Override
    public BaseDao<ExternalCorpAndCorp, String> getEntityDao() {
        return externalCorpAndCorpDao;
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(ExternalCorpAndCorp entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据ID删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(ExternalCorpAndCorp entity) {
        super.updateEntity(entity);
    }
    public void save(ExternalCorpAndCorp entity) {
        super.saveEntity(entity);
    }
    /**
     * 保存或更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(ExternalCorpAndCorp entity) {
        super.saveOrUpdate(entity);
    }
    /**
     * 根据客商ID删除对应的客商单位对应关系
     *
     * @param externalCorpId
     */
    public void deleteExternalCorpAndCorp(String externalCorpId) {
        externalCorpAndCorpDao.deleteExternalCorpAndCorp(externalCorpId);
    }
    /**
     * 根据客商id查找对应的单位ID
     *
     * @param externalCorpId
     * @return
     */
    public List<String> getCorpIdsByExternalCorpId(String externalCorpId) {
        return externalCorpAndCorpDao.getCorpIdsByExternalCorpId(externalCorpId);
    }

    /**
     * 根据客商ID集合批量查询客商与单位关联关系。
     *
     * @param externalCorpIds 客商ID集合
     * @return 客商与单位关联关系集合
     */
    public List<ExternalCorpAndCorp> listByExternalCorpIds(List<String> externalCorpIds) {
        return externalCorpAndCorpDao.listByExternalCorpIds(externalCorpIds);
    }


    /**
     * 根据客商id获取所有关联单位
     * @param externalId
     * @return
     */
    public List<String> getCorpNameByExternalId(String externalId){
        return externalCorpAndCorpDao.getCorpNameByExternalId(externalId);
    }
}
