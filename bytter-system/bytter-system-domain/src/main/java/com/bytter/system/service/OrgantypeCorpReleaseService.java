package com.bytter.system.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.dao.OrgantypeCorpReleaseDao;
import com.bytter.system.entity.OrgantypeCorpRelease;
import com.bytter.system.vo.OrgantypeCorpReleaseVO;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Service
 * @Description: 组织架构单位层级关联发布表业务逻辑操作
 * @date 2020-05-14 16:01:41
 */
@Service("organtypeCorpReleaseService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class OrgantypeCorpReleaseService extends BaseService<OrgantypeCorpRelease, String> {
    @Autowired
    private OrgantypeCorpReleaseDao organtypeCorpReleaseDao;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(OrgantypeCorpRelease entity) {
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
    public void save(OrgantypeCorpRelease entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(OrgantypeCorpRelease entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(OrgantypeCorpRelease entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<OrgantypeCorpRelease, String> getEntityDao() {
        return organtypeCorpReleaseDao;
    }
    /**
     * 通VO保存
     *
     * @param entity
     */
    public void saveOrgantypeCorpRelease(OrgantypeCorpReleaseVO organtypeCorpReleaseVO) {
        organtypeCorpReleaseVO.setId(null);
        saveOrUpdateOrgantypeCorpRelease(organtypeCorpReleaseVO);
    }
    /**
     * 通VO更新
     *
     * @param entity
     */
    public void updateOrgantypeCorpRelease(OrgantypeCorpReleaseVO organtypeCorpReleaseVO) {
        saveOrUpdateOrgantypeCorpRelease(organtypeCorpReleaseVO);
    }
    /**
     * 通VO保存or更新
     *
     * @param entity
     */
    public void saveOrUpdateOrgantypeCorpRelease(OrgantypeCorpReleaseVO organtypeCorpReleaseVO) {
        if (organtypeCorpReleaseVO != null) {
            OrgantypeCorpRelease organtypeCorpRelease = null;
            if (StringUtils.isNotEmpty(organtypeCorpReleaseVO.getId())) {
                organtypeCorpRelease = this.get(organtypeCorpReleaseVO.getId(), true);
            } else {
                organtypeCorpRelease = new OrgantypeCorpRelease();
            }
            BeanUtils.copyNotNullProperties(organtypeCorpRelease, organtypeCorpReleaseVO);
            saveOrUpdate(organtypeCorpRelease);
        }
    }
    /**
     * 根据ID查询组织架构单位层级关联发布表VO
     *
     * @param input
     * @return
     */
    @Transactional(readOnly = true)
    public OrgantypeCorpReleaseVO getOrgantypeCorpReleaseVO(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        OrgantypeCorpRelease organtypeCorpRelease = this.get(id, true);
        if (organtypeCorpRelease == null) {
            return null;
        }
        OrgantypeCorpReleaseVO organtypeCorpReleaseVO = new OrgantypeCorpReleaseVO();
        BeanUtils.copyNotNullProperties(organtypeCorpRelease, organtypeCorpReleaseVO);
        return organtypeCorpReleaseVO;
    }
}
