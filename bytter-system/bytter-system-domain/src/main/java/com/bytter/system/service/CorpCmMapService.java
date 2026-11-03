package com.bytter.system.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.dao.CorpCmMapDao;
import com.bytter.system.entity.CorpCmMap;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Service
 * @Description: 管理单元网点单位映射表业务逻辑操作
 * @date 2020-05-15 17:18:59
 */
@Service("corpCmMapService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class CorpCmMapService extends BaseService<CorpCmMap, String> {
    @Autowired
    private CorpCmMapDao corpCmMapDao;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(CorpCmMap entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        if (StringUtils.isNotEmpty(id)) {
            super.delete(id);
        }
    }
    /**
     * 根据ids删除
     *
     * @param ids
     */
    public void deletes(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                delete(id);
            }
        }
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(CorpCmMap entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CorpCmMap entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(CorpCmMap entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<CorpCmMap, String> getEntityDao() {
        return corpCmMapDao;
    }
    /**
     * 新增或者修改映射关系
     *
     * @param corpId 单位ID
     * @param unitCm 单元ID
     */
    public int saveCorpCmMap(String corpId, String unitCms) {
        if (StringUtils.isNotEmpty(corpId)) {
            corpCmMapDao.deleteByCorpId(corpId);
            if (StringUtils.isNotEmpty(unitCms)) {
                String[] unitCmArray = unitCms.split(",");
                List<CorpCmMap> infoList = new ArrayList<>();
                Date createDate = new Date();
                for (String unitCm : unitCmArray) {
                    if (StringUtils.isNotEmpty(unitCm)) {
                        infoList.add(new CorpCmMap(corpId, unitCm, createDate));
                    }
                }
                return corpCmMapDao.batchJdbcSave(infoList);
            }
        }
        return 0;
    }
}
