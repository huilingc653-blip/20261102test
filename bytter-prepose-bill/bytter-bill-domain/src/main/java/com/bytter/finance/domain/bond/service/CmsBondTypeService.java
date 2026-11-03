package com.bytter.finance.domain.bond.service;
import com.bytter.finance.domain.bond.dao.CmsBondTypeDao;
import com.bytter.finance.domain.bond.entity.CmsBondType;
import com.bytter.finance.domain.bond.vo.CmsBondTypeVo;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.enums.BondMessageEnum;
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
 * 债券类型Service
 *
 * @author ouyj
 */
@Service("cmsBondTypeService")
@Transactional(rollbackFor = Exception.class)
public class CmsBondTypeService extends BaseService<CmsBondType, String> {
    @Autowired
    private CmsBondTypeDao cmsBondTypeDao;
    @Override
    public BaseDao<CmsBondType, String> getEntityDao() {
        return cmsBondTypeDao;
    }
    /**
     * 获取债券类型列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @return 债券类型列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        return cmsBondTypeDao.getDataTable(dataTablesInput);
    }
    /**
     * 新增债券类型信息
     *
     * @param cmsBondType
     *            债券类型实体
     * @param user
     *            当前用户
     */
    public void saveBondType(CmsBondType cmsBondType, ICurrentUser user) {
        isExistType(cmsBondType);
        cmsBondType.setCreateBy(user.getUsername());
        cmsBondType.setCreateDate(new Date());
        this.saveEntity(cmsBondType);
    }
    /**
     * 判断编码或名称是否已存在
     *
     * @param cmsBondType
     *            债券类型实体
     */
    public void isExistType(CmsBondType cmsBondType) {
        boolean flag = cmsBondTypeDao.isExistType(cmsBondType.getId(), cmsBondType.getBondCode(),
                cmsBondType.getBondName());
        if (flag) {
            throw new BusinessException(BondMessageEnum.CMS_BASEDATA_BONDTYPE_EXISTENCE);
        }
    }
    /**
     * 修改债券类型信息
     *
     * @param cmsBondType
     *            债券类型实体
     * @param user
     *            当前用户
     */
    public void updateBondType(CmsBondType cmsBondType, ICurrentUser user) {
        isExistType(cmsBondType);
        cmsBondType.setUpdateBy(user.getUsername());
        cmsBondType.setUpdateDate(new Date());
        this.updateEntity(cmsBondType);
    }
    /**
     * 删除债券类型信息
     *
     * @param id
     *            债券类型id
     * @param user
     *            当前用户
     */
    public void deleteBondType(String id, ICurrentUser user) {
        boolean flag = cmsBondTypeDao.isQuoteType(id);
        if (flag) {
            throw new BusinessException(BondMessageEnum.CMS_BASEDATA_BONDTYPE_ISQUOTE);
        }
        CmsBondType cmsBondType = this.get(id);
        // 修改为已删除
        cmsBondType.setIsDelete(CmsConstant.IS_TRUE);
        cmsBondType.setUpdateDate(new Date());
        cmsBondType.setUpdateBy(user.getUsername());
        this.updateEntity(cmsBondType);
    }
    /**
     * 获取有效的债券类型集合
     *
     * @return 有效的债券类型集合
     */
    public List<CmsBondTypeVo> listValidBondType() {
        return cmsBondTypeDao.listValidBondType();
    }
}
