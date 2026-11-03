package com.bytter.finance.domain.security.service;
import com.bytter.finance.domain.security.dao.CmsSecurityTypeDao;
import com.bytter.finance.domain.security.entity.CmsSecurityType;
import com.bytter.finance.domain.security.vo.CmsSecurityTypeVO;
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
 * 资产证券化类型
 *
 * @author tongqr
 */
@Service("cmsSecurityTypeService")
@Transactional(rollbackFor = Exception.class)
public class CmsSecurityTypeService extends BaseService<CmsSecurityType, String> {
    @Autowired
    private CmsSecurityTypeDao cmsSecurityTypeDao;
    @Override
    public BaseDao<CmsSecurityType, String> getEntityDao() {
        return cmsSecurityTypeDao;
    }
    /**
     * 获取资产证券化类型列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @return 资产证券化类型列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        return cmsSecurityTypeDao.getDataTable(dataTablesInput);
    }
    /**
     * 新增资产证券化类型信息
     *
     * @param cmsSecurityType
     *            资产证券化类型实体
     * @param user
     *            当前用户
     */
    public void saveSecurityType(CmsSecurityType cmsSecurityType, ICurrentUser user) {
        isExistType(cmsSecurityType);
        cmsSecurityType.setStatus(SecurityConstant.IS_FALSE);
        cmsSecurityType.setCreateBy(user.getUsername());
        cmsSecurityType.setCreateDate(new Date());
        this.saveEntity(cmsSecurityType);
    }
    /**
     * 判断编码或名称是否已存在
     *
     * @param cmsSecurityType
     *            资产证券化类型实体
     */
    public void isExistType(CmsSecurityType cmsSecurityType) {
        boolean flag = cmsSecurityTypeDao.isExistType(cmsSecurityType.getId(), cmsSecurityType.getTypeCode(),
                cmsSecurityType.getTypeName());
        if (flag) {
            throw new BusinessException(SecurityMessageEnum.CMS_BASEDATA_SECURITYTYPE_EXISTENCE);
        }
    }
    /**
     * 修改资产证券化类型信息
     *
     * @param cmsSecurityType
     *            资产证券化类型实体
     * @param user
     *            当前用户
     */
    public void updateSecurityType(CmsSecurityType cmsSecurityType, ICurrentUser user) {
        isExistType(cmsSecurityType);
        cmsSecurityType.setUpdateBy(user.getUsername());
        cmsSecurityType.setUpdateDate(new Date());
        this.updateEntity(cmsSecurityType);
    }
    /**
     * 删除资产证券化类型信息
     *
     * @param id
     *            资产证券化类型id
     * @param user
     *            当前用户
     */
    public void deleteSecurityType(String id, ICurrentUser user) {
        boolean flag = cmsSecurityTypeDao.isQuoteType(id);
        if (flag) {
            throw new BusinessException(SecurityMessageEnum.CMS_BASEDATA_SECURITYTYPE_ISQUATE);
        }
        CmsSecurityType cmsSecurityType = this.get(id);
        // 修改为已删除
        cmsSecurityType.setStatus(SecurityConstant.IS_TRUE);
        cmsSecurityType.setUpdateDate(new Date());
        cmsSecurityType.setUpdateBy(user.getUsername());
        this.updateEntity(cmsSecurityType);
    }
    /**
     * 获取有效的资产证券化类型集合
     *
     * @return 有效的融资租赁类型集合
     */
    public List<CmsSecurityTypeVO> listSecurityType() {
        return cmsSecurityTypeDao.listSecurityType();
    }
    /**
     * 根据code获取到对象
     *
     * @return 对应code得类型对象
     */
    public CmsSecurityTypeVO getTypeById(String id) {
        return cmsSecurityTypeDao.getTypeById(id);
    }
}
