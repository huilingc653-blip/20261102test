package com.bytter.finance.domain.basedata.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.basedata.dao.RepayTypeDao;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.BorrowingMessageEnum;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 还款方式维护
 * @date 2022-08-10 09:58:43
 */
@Service("repayTypeService")
@Transactional
public class RepayTypeService extends BaseService<RepayType, String> {
    @Autowired
    private RepayTypeDao repayTypeDao;
    @DubboReference(check = false)
    private IDictService iDictService;
    public void save(RepayType entity) {
        super.saveEntity(entity);
    }
    @Override
    public BaseDao<RepayType, String> getEntityDao() {
        return repayTypeDao;
    }
    /**
     * 查询还款方式
     ***/
    @Transactional(readOnly = true)
    public DataTablesOutput getRepayType(DataTablesInput input) {
        return repayTypeDao.getRepayType(input);
    }
    /**
     * 功能说明：根据id、类型查询还款方式
     *
     * @param id
     */
    @Transactional(readOnly = true)
    public RepayTypeVO getRepayTypeById(String id) {
        return BeanUtils.copyNewInstanceProperties(get(id), RepayTypeVO.class);
    }

    @Transactional(readOnly = true)
    public RepayTypeVO getRepayTypeInfoById(String id) {
        return repayTypeDao.getRepayTypeById(id);
    }

    /**
     * 根据还款方式ID批量获取还款方式。
     * @param ids
     * @return
     */
    public Map<String,RepayTypeVO> listRepayTypeByIds(String... ids){
        List<RepayTypeVO> repayTypeList = repayTypeDao.listRepayTypeByIds(ids);
        return repayTypeList.stream().collect(Collectors.toMap(RepayTypeVO::getId, repayType -> repayType));
    }
    /**
     * 功能说明：根据类型code查询还款方式
     *
     * @param code
     */
    @Transactional(readOnly = true)
    public RepayTypeVO getRepayTypeByCode(String code) {
        return repayTypeDao.getRepayTypeByCode(code);
    }
    /**
     * 功能说明：根据类型名称查询还款方式
     *
     * @param name
     */
    @Transactional(readOnly = true)
    public RepayTypeVO getRepayTypeByName(String name) {
        return repayTypeDao.getRepayTypeByName(name);
    }
    /**
     * 删除还款方式
     **/
    public int deleteRepayType(String id) {
        return repayTypeDao.deleteRepayType(id);
    }
    /**
     * 查询删除类型编码是否存在
     **/
    @Transactional(readOnly = true)
    public boolean judgeRepayTypeByCode(String id) {
        return repayTypeDao.judgeRepayTypeByCode(id).compareTo(BigDecimal.ZERO) == 1 ? false : true;
    }
    /**
     * 查询类型编码是否存在
     **/
    @Transactional(readOnly = true)
    public boolean judgeTypeByCode(String repayCode, String repayName, String id) {
        return repayTypeDao.judgeTypeByCode(repayCode, repayName, id);
    }
    /**
     * 新增还款方式
     **/
    public void saveRepayType(RepayTypeVO repayTypeVo, ICurrentUser user) {
        this.verify(repayTypeVo);
        boolean flag = judgeTypeByCode(repayTypeVo.getRepayCode(), repayTypeVo.getRepayName(), null);
        if (flag) {
            RepayType repayType = BeanUtils.copyNewInstanceProperties(repayTypeVo, RepayType.class);
            if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getCapitalFrequencyType())) {
                if (!BaseDataConstant.OTHER.equals(repayType.getCapitalFrequency())) {
                    repayType.setCapitalFrequencyNum(repayType.getCapitalFrequency());
                }
            }
            if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getInterestFrequencyType())) {
                if (!BaseDataConstant.OTHER.equals(repayType.getInterestFrequency())) {
                    repayType.setInterestFrequencyNum(repayType.getInterestFrequency());
                }
            }
            repayType.setCreateBy(user.getId());
            repayType.setCreateDate(new Date());
            repayType.setCreateName(user.getLoginName());
            save(repayType);
        } else {
            throw new ParameterException(BorrowingMessageEnum.FIN_BORROWING_TYPE_EXIST);
        }
    }
    /**
     * 拼接还款方式分类(D:按时间周期；C：按次数；P：不规则（有计划）；N随借随还（无计划）；示例：DD)
     *
     * @param repayTypeVo
     * @return
     */
    private String spellRepayClass(RepayTypeVO repayTypeVo) {
        StringBuilder repayClass = new StringBuilder();
        repayClass.append(repayTypeVo.getCapitalFrequencyType());
        if (CommonConstant.VALID_SIGN_YES.equals(repayTypeVo.getIsFrequencySame())) {
            repayClass.append(repayTypeVo.getCapitalFrequencyType());
        } else {
            repayClass.append(repayTypeVo.getInterestFrequencyType());
        }
        return repayClass.toString();
    }
    /**
     * 数据校验
     *
     * @param repayTypeVo
     */
    public void verify(RepayTypeVO repayTypeVo) {
        if (EmptyUtils.isEmpty(repayTypeVo.getRepayCode())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANCODEEMPTY);
        }
        if (EmptyUtils.isEmpty(repayTypeVo.getRepayName())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANNAMEEMPTY);
        }
        if (!this.judgeRepayTypeByCode(repayTypeVo.getId())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REF_OTHER);
        }
    }
    /**
     * 查询所有非删除状态有效还款方式
     *
     * @return
     */
    public List<RepayTypeVO> queryRepayTypeList() {
        return repayTypeDao.listRepayTypeVO();
    }

    /**
     * 查询所有非删除状态有效还款方式(除了随借随还)
     * @return
     */
    public List<RepayTypeVO> queryNoNRepayTypeList() {
        return repayTypeDao.listNoNRepayTypeVO();
    }

    /**
     * 查询所有非删除状态有效还款方式(无本金与利息无关随借随还)
     * @return
     */
    public List<RepayTypeVO> queryNoTRepayTypeList() {
        return repayTypeDao.listNoTRepayTypeVO();
    }

    /**
     * 修改还款方式
     ***/
    public int updateRepayType(RepayTypeVO repayTypeVo, ICurrentUser user) {
        this.verify(repayTypeVo);
        int res = 0;
        boolean isOk = judgeTypeByCode(repayTypeVo.getRepayCode(), repayTypeVo.getRepayName(), repayTypeVo.getId());
        if (isOk) {
            RepayType repayType = BeanUtils.copyNewInstanceProperties(repayTypeVo, RepayType.class);
            if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getCapitalFrequencyType())) {
                if (!BaseDataConstant.OTHER.equals(repayType.getCapitalFrequency())) {
                    repayType.setCapitalFrequencyNum(repayType.getCapitalFrequency());
                }
            }
            if (BaseDataConstant.FREQUENCY_TYPE_D.equals(repayType.getInterestFrequencyType())) {
                if (!BaseDataConstant.OTHER.equals(repayType.getInterestFrequency())) {
                    repayType.setInterestFrequencyNum(repayType.getInterestFrequency());
                }
            }
            repayType.setUpdateBy(user.getId());
            repayType.setUpdateName(user.getLoginName());
            repayType.setUpdateDate(new Date());
            repayType.setStatus(BaseDataConstant.STATUS_REDACT);
            this.updateEntity(repayType);
        } else {
            throw new ParameterException(BorrowingMessageEnum.FIN_BORROWING_TYPE_EXIST);
        }
        return res;
    }
    public RepayTypeVO getisInput(RepayTypeVO repayTypeVo) {
        repayTypeVo = getRepayTypeById(repayTypeVo.getId());
        return repayTypeVo;
    }
    /**
     * 启用/禁用
     *
     * @param id
     *            主键id
     * @param validSign
     *            启用禁用状态
     */
    public void setValidSign(String id, String validSign) {
        RepayType repayType = get(id);
        repayType.setValidSign(validSign);
        this.updateEntity(repayType);
    }
}
