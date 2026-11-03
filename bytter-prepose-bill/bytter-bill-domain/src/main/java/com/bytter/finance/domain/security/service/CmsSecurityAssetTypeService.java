package com.bytter.finance.domain.security.service;
import com.bytter.finance.domain.security.dao.CmsSecurityAssetTypeDao;
import com.bytter.finance.domain.security.entity.CmsSecurityAssetType;
import com.bytter.finance.domain.security.vo.CmsSecurityAssetTypeVO;
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
 * 基础资产类型
 *
 * @author tongqr
 */
@Service("cmsSecurityAssetTypeService")
@Transactional(rollbackFor = Exception.class)
public class CmsSecurityAssetTypeService extends BaseService<CmsSecurityAssetType, String> {
    @Autowired
    private CmsSecurityAssetTypeDao cmsSecurityAssetTypeDao;
    @Override
    public BaseDao<CmsSecurityAssetType, String> getEntityDao() {
        return cmsSecurityAssetTypeDao;
    }
    /**
     * 获取基础资产类型列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @return 基础资产类型列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        return cmsSecurityAssetTypeDao.getDataTable(dataTablesInput);
    }
    /**
     * 新增基础资产类型信息
     *
     * @param cmsSecurityAssetType
     *            基础资产类型实体
     * @param user
     *            当前用户
     */
    public void saveSecurityAssetType(CmsSecurityAssetType cmsSecurityAssetType, ICurrentUser user) {
        isExistType(cmsSecurityAssetType);
        cmsSecurityAssetType.setStatus(SecurityConstant.IS_FALSE);
        cmsSecurityAssetType.setCreateBy(user.getUsername());
        cmsSecurityAssetType.setCreateDate(new Date());
        this.saveEntity(cmsSecurityAssetType);
    }
    /**
     * 判断编码或名称是否已存在
     *
     * @param cmsSecurityAssetType
     *            基础资产类型实体
     */
    public void isExistType(CmsSecurityAssetType cmsSecurityAssetType) {
        boolean flag = cmsSecurityAssetTypeDao.isExistType(cmsSecurityAssetType.getId(),
                cmsSecurityAssetType.getAssetTypeCode(), cmsSecurityAssetType.getAssetTypeName());
        if (flag) {
            throw new BusinessException(SecurityMessageEnum.CMS_BASEDATA_SECURITYASSETTYPE_EXISTENCE);
        }
    }
    /**
     * 修改基础资产类型信息
     *
     * @param cmsSecurityAssetType
     *            基础资产类型实体
     * @param user
     *            当前用户
     */
    public void updateSecurityAssetType(CmsSecurityAssetType cmsSecurityAssetType, ICurrentUser user) {
        isExistType(cmsSecurityAssetType);
        cmsSecurityAssetType.setUpdateBy(user.getUsername());
        cmsSecurityAssetType.setUpdateDate(new Date());
        this.updateEntity(cmsSecurityAssetType);
    }
    /**
     * 删除基础资产类型信息
     *
     * @param id
     *            基础资产类型id
     * @param user
     *            当前用户
     */
    public void deleteSecurityAssetType(String id, ICurrentUser user) {
        boolean flag = cmsSecurityAssetTypeDao.isQuoteType(id);
        if (flag) {
            throw new BusinessException(SecurityMessageEnum.CMS_BASEDATA_SECURITYASSETTYPE_ISQUATE);
        }
        CmsSecurityAssetType cmsSecurityAssetType = this.get(id);
        // 修改为已删除
        cmsSecurityAssetType.setStatus(SecurityConstant.IS_TRUE);
        cmsSecurityAssetType.setUpdateDate(new Date());
        cmsSecurityAssetType.setUpdateBy(user.getUsername());
        this.updateEntity(cmsSecurityAssetType);
    }
    /**
     * 获取有效的基础资产类型集合
     *
     * @return 有效的融资租赁类型集合
     */
    public List<CmsSecurityAssetTypeVO> listSecurityAssetType() {
        return cmsSecurityAssetTypeDao.listSecurityAssetType();
    }
    /**
     * 根据code获取到对象
     *
     * @return 对应code得类型对象
     */
    public CmsSecurityAssetTypeVO getAssetTypeByCode(String assetTypeCode) {
        return cmsSecurityAssetTypeDao.getAssetTypeByCode(assetTypeCode);
    }
}
