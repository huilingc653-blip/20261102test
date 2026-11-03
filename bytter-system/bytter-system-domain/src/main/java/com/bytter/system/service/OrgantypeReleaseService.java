package com.bytter.system.service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.dao.OrgantypeReleaseDao;
import com.bytter.system.entity.OrgantypeRelease;
import com.bytter.system.vo.OrgantypeReleaseVO;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Service
 * @Description: 组织架构发版表业务逻辑操作
 * @date 2020-05-14 16:01:42
 */
@Service("organtypeReleaseService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class OrgantypeReleaseService extends BaseService<OrgantypeRelease, String> {
    @Autowired
    private OrgantypeReleaseDao organtypeReleaseDao;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(OrgantypeRelease entity) {
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
    public void save(OrgantypeRelease entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(OrgantypeRelease entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(OrgantypeRelease entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<OrgantypeRelease, String> getEntityDao() {
        return organtypeReleaseDao;
    }
    /**
     * 通VO保存
     *
     * @param organtypeReleaseVO
     */
    public void saveOrgantypeRelease(OrgantypeReleaseVO organtypeReleaseVO, ICurrentUser userVo) {
        organtypeReleaseVO.setId(null);
        saveOrUpdateOrgantypeRelease(organtypeReleaseVO, userVo);
    }
    /**
     * 通VO更新
     *
     * @param organtypeReleaseVO
     */
    public void updateOrgantypeRelease(OrgantypeReleaseVO organtypeReleaseVO, ICurrentUser userVo) {
        saveOrUpdateOrgantypeRelease(organtypeReleaseVO, userVo);
    }
    /**
     * 通VO保存or更新
     *
     * @param organtypeReleaseVO
     */
    public void saveOrUpdateOrgantypeRelease(OrgantypeReleaseVO organtypeReleaseVO, ICurrentUser userVo) {
        if (organtypeReleaseVO != null) {
            OrgantypeRelease organtypeRelease = null;
            if (StringUtils.isNotEmpty(organtypeReleaseVO.getId())) {
                organtypeRelease = this.get(organtypeReleaseVO.getId(), true);
            } else {
                organtypeRelease = new OrgantypeRelease();
            }
            if (userVo != null) {
                organtypeRelease.setCreateBy(userVo.getId());
            }
            organtypeRelease.setCreateTime(new Date());
            BeanUtils.copyNotNullProperties(organtypeReleaseVO, organtypeRelease);
            saveOrUpdate(organtypeRelease);
            organtypeReleaseVO.setId(organtypeRelease.getId());
        }
    }
    /**
     * 根据ID查询组织架构发版表VO
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public OrgantypeReleaseVO getOrgantypeReleaseVO(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        OrgantypeRelease organtypeRelease = this.get(id, true);
        if (organtypeRelease == null) {
            return null;
        }
        OrgantypeReleaseVO organtypeReleaseVO = new OrgantypeReleaseVO();
        BeanUtils.copyNotNullProperties(organtypeRelease, organtypeReleaseVO);
        return organtypeReleaseVO;
    }

    public List<OrgantypeRelease> getByOrgId(String orgtId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("orgtId", orgtId);
        return organtypeReleaseDao.getByOrgId(params);
    }
}
