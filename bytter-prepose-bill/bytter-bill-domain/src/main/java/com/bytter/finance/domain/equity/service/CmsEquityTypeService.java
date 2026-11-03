package com.bytter.finance.domain.equity.service;
import com.bytter.finance.domain.equity.dao.CmsEquityTypeDao;
import com.bytter.finance.domain.equity.entity.CmsEquityType;
import com.bytter.finance.domain.equity.vo.CmsEquityTypeVo;
import com.bytter.finance.constant.EquityConstant;
import com.bytter.finance.enums.EquityMessageEnum;
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
 * 融资租赁类型
 *
 * @author wangf
 */
@Service("cmsEquityTypeService")
@Transactional(rollbackFor = Exception.class)
public class CmsEquityTypeService extends BaseService<CmsEquityType, String> {
    @Autowired
    private CmsEquityTypeDao cmsEquityTypeDao;
    @Override
    public BaseDao<CmsEquityType, String> getEntityDao() {
        return cmsEquityTypeDao;
    }
    /**
     * 获取融资租赁类型列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @return 融资租赁类型列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        return cmsEquityTypeDao.getDataTable(dataTablesInput);
    }
    /**
     * 新增融资租赁类型信息
     *
     * @param cmsEquityType
     *            融资租赁类型实体
     * @param user
     *            当前用户
     */
    public void saveEquityType(CmsEquityType cmsEquityType, ICurrentUser user) {
        isExistType(cmsEquityType);
        cmsEquityType.setStatus(EquityConstant.IS_TRUE);
        cmsEquityType.setCreateBy(user.getUsername());
        cmsEquityType.setCreateDate(new Date());
        this.saveEntity(cmsEquityType);
    }
    /**
     * 判断编码或名称是否已存在
     *
     * @param cmsEquityType
     *            融资租赁类型实体
     */
    public void isExistType(CmsEquityType cmsEquityType) {
        boolean flag = cmsEquityTypeDao.isExistType(cmsEquityType.getId(), cmsEquityType.getTypeCode(),
                cmsEquityType.getTypeName());
        if (flag) {
            throw new BusinessException(EquityMessageEnum.CMS_BASEDATA_EQUITYTYPE_EXISTENCE);
        }
    }
    /**
     * 修改融资租赁类型信息
     *
     * @param cmsEquityType
     *            融资租赁类型实体
     * @param user
     *            当前用户
     */
    public void updateEquityType(CmsEquityType cmsEquityType, ICurrentUser user) {
        isExistType(cmsEquityType);
        cmsEquityType.setUpdateBy(user.getUsername());
        cmsEquityType.setUpdateDate(new Date());
        this.updateEntity(cmsEquityType);
    }
    /**
     * 删除融资租赁类型信息
     *
     * @param id
     *            融资租赁类型id
     * @param user
     *            当前用户
     */
    public void deleteEquityType(String id, ICurrentUser user) {
        CmsEquityType cmsEquityType = this.get(id);
        // 修改为已删除
        cmsEquityType.setStatus(EquityConstant.IS_FALSE);
        cmsEquityType.setUpdateDate(new Date());
        cmsEquityType.setUpdateBy(user.getUsername());
        this.updateEntity(cmsEquityType);
    }
    /**
     * 获取有效的融资租赁类型集合
     *
     * @return 有效的融资租赁类型集合
     */
    public List<CmsEquityTypeVo> listEquityType() {
        return cmsEquityTypeDao.listEquityType();
    }
    /**
     * 根据code获取到对象
     *
     * @return 对应code得类型对象
     */
    public CmsEquityTypeVo getTypeByCode(String typeCode) {
        return cmsEquityTypeDao.getTypeByCode(typeCode);
    }
}
