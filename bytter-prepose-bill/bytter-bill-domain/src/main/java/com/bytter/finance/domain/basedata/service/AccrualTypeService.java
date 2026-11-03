package com.bytter.finance.domain.basedata.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.basedata.dao.AccrualTypeDao;
import com.bytter.finance.domain.basedata.entity.AccrualType;
import com.bytter.finance.domain.basedata.vo.AccrualTypeVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.BorrowingMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 计提方式维护
 * @date 2022-08-10 09:58:43
 */
@Service("accrualTypeService")
@Transactional
public class AccrualTypeService extends BaseService<AccrualType, String> {
    @Autowired
    private AccrualTypeDao accrualTypeDao;
    @Override
    public BaseDao<AccrualType, String> getEntityDao() {
        return accrualTypeDao;
    }
    /**
     * 查询计提方式
     ***/
    @Transactional(readOnly = true)
    public DataTablesOutput queryAccrualType(DataTablesInput input) {
        return accrualTypeDao.queryAccrualType(input);
    }
    /**
     * 功能说明：根据id、类型查询计提方式
     *
     * @param id
     */
    @Transactional(readOnly = true)
    public AccrualTypeVO queryAccrualTypeById(String id) {
        return BeanUtils.copyNewInstanceProperties(accrualTypeDao.get(id), AccrualTypeVO.class);
    }
    /**
     * 功能说明：根据类型code查询计提方式
     *
     * @param code
     */
    @Transactional(readOnly = true)
    public AccrualTypeVO queryAccrualTypeByCode(String code) {
        return accrualTypeDao.queryAccrualTypeByCode(code);
    }
    /**
     * 功能说明：根据类型名称查询计提方式
     *
     * @param name
     */
    @Transactional(readOnly = true)
    public AccrualTypeVO queryAccrualTypeByName(String name) {
        return accrualTypeDao.queryAccrualTypeByName(name);
    }
    /**
     * 修改计提方式
     ***/
    public void updateAccrualType(AccrualTypeVO accrualTypeVo, ICurrentUser user) {
        this.verify(accrualTypeVo);
        boolean isOk = judgeTypeByCode(accrualTypeVo.getAccrualCode(), accrualTypeVo.getAccrualName(),
                accrualTypeVo.getId());
        if (isOk) {
            AccrualType accrualType = BeanUtils.copyNewInstanceProperties(accrualTypeVo, AccrualType.class);
            if (!BaseDataConstant.OTHER.equals(accrualTypeVo.getFrequency())) {
                accrualType.setFrequencyDailyDate(accrualType.getFrequency());
            }
            accrualType.setStatus(BaseDataConstant.STATUS_REDACT);
            accrualType.setUpdateBy(user.getId());
            accrualType.setUpdateDate(new Date());
            updateEntity(accrualType);
        } else {
            throw new ParameterException(BorrowingMessageEnum.FIN_BORROWING_TYPE_EXIST);
        }
    }
    /**
     * 删除计提方式
     **/
    public int deleteAccrualType(String id) {
        return accrualTypeDao.deleteAccrualType(id);
    }
    /**
     * 查询删除类型编码是否存在
     **/
    @Transactional(readOnly = true)
    public boolean judgeAccrualTypeByCode(String id) {
        return accrualTypeDao.judgeAccrualTypeByCode(id).compareTo(BigDecimal.ZERO) == 1 ? false : true;
    }
    /**
     * 查询类型编码是否存在
     **/
    @Transactional(readOnly = true)
    public boolean judgeTypeByCode(String accrualCode, String accrualName, String id) {
        return accrualTypeDao.judgeTypeByCode(accrualCode, accrualName, id);
    }
    /**
     * 新增计提方式
     **/
    public void saveAccrualType(AccrualTypeVO accrualTypeVo, ICurrentUser user) {
        verify(accrualTypeVo);
        String accrualCode = accrualTypeVo.getAccrualCode();
        String accrualName = accrualTypeVo.getAccrualName();
        boolean flag = judgeTypeByCode(accrualCode, accrualName, null);
        if (flag) {
            AccrualType accrualType = BeanUtils.copyNewInstanceProperties(accrualTypeVo, AccrualType.class);
            if (!BaseDataConstant.OTHER.equals(accrualTypeVo.getFrequency())) {
                accrualType.setFrequencyDailyDate(accrualType.getFrequency());
            }
            accrualType.setStatus(accrualTypeVo.getStatus());
            accrualType.setCreateBy(user.getId());
            accrualType.setCreateDate(new Date());
            saveEntity(accrualType);
        } else {
            throw new ParameterException(BorrowingMessageEnum.FIN_BORROWING_TYPE_EXIST);
        }
    }
    /**
     * 编码类型校验
     * 
     * @param accrualTypeVo
     */
    public void verify(AccrualTypeVO accrualTypeVo) {
        if (EmptyUtils.isEmpty(accrualTypeVo.getAccrualCode())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANCODEEMPTY);
        }
        if (EmptyUtils.isEmpty(accrualTypeVo.getAccrualName())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANNAMEEMPTY);
        }
        if (!this.judgeAccrualTypeByCode(accrualTypeVo.getId())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REF_OTHER);
        }
    }
    /**
     * 获取计提方式
     * 
     * @return
     */
    public List<AccrualTypeVO> queryAccrualTypeList() {
        return accrualTypeDao.listAccrualTypeVO();
    }
    public AccrualTypeVO getisInput(AccrualTypeVO accrualTypeVo) {
        accrualTypeVo = queryAccrualTypeById(accrualTypeVo.getId());
        return accrualTypeVo;
    }
    /**
     * 启用/禁用
     * 
     * @param id
     *            主键id
     * @param validSign
     *            启用/禁用状态
     * @return
     */
    public void setValidSign(String id, String validSign) {
        AccrualType accrualType = this.get(id);
        accrualType.setValidSign(validSign);
        this.updateEntity(accrualType);
    }
}
