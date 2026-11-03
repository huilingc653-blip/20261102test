package com.bytter.finance.domain.security.service;
import com.bytter.finance.domain.security.dao.CmsSecurityLevelDao;
import com.bytter.finance.domain.security.entity.CmsSecurityLevel;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelVO;
import com.bytter.finance.constant.SecurityConstant;
import com.bytter.finance.enums.SecurityMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * 资产证券化层级
 *
 * @author tongqr
 */
@Service("cmsSecurityLevelService")
@Transactional(rollbackFor = Exception.class)
public class CmsSecurityLevelService extends BaseService<CmsSecurityLevel, String> {
    @Autowired
    private CmsSecurityLevelDao cmsSecurityLevelDao;
    @Override
    public BaseDao<CmsSecurityLevel, String> getEntityDao() {
        return cmsSecurityLevelDao;
    }
    /**
     * 获取资产证券化层级列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @return 资产证券化层级列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        return cmsSecurityLevelDao.getDataTable(dataTablesInput);
    }
    /**
     * 新增资产证券化层级信息
     *
     * @param cmsSecurityLevel
     *            资产证券化层级实体
     * @param user
     *            当前用户
     */
    public void saveSecurityLevel(CmsSecurityLevel cmsSecurityLevel, ICurrentUser user) {
        isExistLevel(cmsSecurityLevel);
        cmsSecurityLevel.setStatus(SecurityConstant.IS_FALSE);
        cmsSecurityLevel.setCreateBy(user.getUsername());
        cmsSecurityLevel.setCreateDate(new Date());
        this.saveEntity(cmsSecurityLevel);
    }
    /**
     * 判断编码或名称是否已存在
     *
     * @param cmsSecurityLevel
     *            资产证券化层级实体
     */
    public void isExistLevel(CmsSecurityLevel cmsSecurityLevel) {
        boolean flag = cmsSecurityLevelDao.isExistLevel(cmsSecurityLevel.getId(), cmsSecurityLevel.getLevelCode(),
                cmsSecurityLevel.getLevelName());
        if (flag) {
            throw new BusinessException(SecurityMessageEnum.CMS_BASEDATA_SECURITYLEVEL_EXISTENCE);
        }
    }
    /**
     * 修改资产证券化层级信息
     *
     * @param cmsSecurityLevel
     *            资产证券化层级实体
     * @param user
     *            当前用户
     */
    public void updateSecurityLevel(CmsSecurityLevel cmsSecurityLevel, ICurrentUser user) {
        isExistLevel(cmsSecurityLevel);
        cmsSecurityLevel.setUpdateBy(user.getUsername());
        cmsSecurityLevel.setUpdateDate(new Date());
        this.updateEntity(cmsSecurityLevel);
    }
    /**
     * 删除资产证券化层级信息
     *
     * @param id
     *            资产证券化层级id
     * @param user
     *            当前用户
     */
    public void deleteSecurityLevel(String id, ICurrentUser user) {
        boolean flag = cmsSecurityLevelDao.isQuoteType(id);
        if (flag) {
            throw new BusinessException(SecurityMessageEnum.CMS_BASEDATA_SECURITYLEVEL_ISQUATE);
        }
        CmsSecurityLevel cmsSecurityLevel = this.get(id);
        // 修改为已删除
        cmsSecurityLevel.setStatus(SecurityConstant.IS_TRUE);
        cmsSecurityLevel.setUpdateDate(new Date());
        cmsSecurityLevel.setUpdateBy(user.getUsername());
        this.updateEntity(cmsSecurityLevel);
    }
    /**
     * 获取有效的资产证券化层级集合
     *
     * @return 有效的资产证券化层级集合
     */
    public List<CmsSecurityLevelVO> listSecurityLevel() {
        return cmsSecurityLevelDao.listSecurityLevel();
    }
    /**
     * 根据code获取到对象
     *
     * @return 对应code得层级对象
     */
    public CmsSecurityLevelVO getLevelByCode(String levelCode) {
        return cmsSecurityLevelDao.getLevelByCode(levelCode);
    }
    /**
     * 根据名称获取到对象
     *
     * @return 对应名称得类型对象
     */
    public CmsSecurityLevelVO getLevelByName(String levelName) {
        return cmsSecurityLevelDao.getLevelByName(levelName);
    }
}
