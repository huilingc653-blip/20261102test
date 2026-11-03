package com.bytter.finance.domain.lease.service;
import com.bytter.finance.domain.basedata.entity.LoanType;
import com.bytter.finance.domain.directFinance.entity.DfCostType;
import com.bytter.finance.domain.directFinance.vo.DfCostTypeVO;
import com.bytter.finance.domain.lease.dao.LeaseTypeDao;
import com.bytter.finance.domain.lease.entity.LeaseItemType;
import com.bytter.finance.domain.lease.vo.LeaseItemTypeVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.CmsLeaseEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title 融资租赁基础数据
 * @Description
 * @Author wanggl
 * @Date 2022-4-2
 * @Version 1.0
 */
@Service("LeaseBasedataService")
@Transactional
public class LeaseBasedataService extends BaseService<LeaseItemType, String> {
    @Autowired
    private LeaseTypeDao leaseTypeDao;
    /**
     * 租赁类别查询
     *
     * @param input
     */
    public DataTablesOutput listLeaseType(DataTablesInput input) {
        return leaseTypeDao.listMsgData(input);
    }
    public LeaseItemType getLeaseTypebyName(String name) {
        return leaseTypeDao.getLeaseTypebyName(name);
    }
    /**
     * 编码校验
     *
     * @param id
     * @param code
     * @return
     */
    public boolean validateCode(String id, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        boolean result = true;
        List<LeaseItemTypeVO> leaseItemTypeVOS = leaseTypeDao.getCountByParams(params);
        if (null == leaseItemTypeVOS || leaseItemTypeVOS.size() == 0) {
            return result;
        }
        if (leaseItemTypeVOS.size() >= 2) {
            result = false;
        } else {
            if (StringUtils.isNotEmpty(id)) {
                if (!id.equals(leaseItemTypeVOS.get(0).getId())) {
                    result = false;
                }
            } else {
                result = false;
            }
        }
        return result;
    }
    /**
     * 查询类型编码是否存在
     **/
    @Transactional(readOnly = true)
    public boolean judgeTypeByCode(String id, String code, String name) {
        return leaseTypeDao.judgeTypeByCode(id, code, name);
    }
    /**
     * 查询类型编码是否存在
     **/
    @Transactional(readOnly = true)
    public boolean judgeTypeById(String id) {
        return leaseTypeDao.isOccupy(id);
    }
    public void saveLeaseType(LeaseItemType leaseItemType) {
        verify(leaseItemType);
        String code = leaseItemType.getCode();
        String name = leaseItemType.getName();
        boolean flag = judgeTypeByCode(null, code, name);
        if (!flag) {
            throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_LEASETYPE_EXISTENCE);
        }
    }
    public int updateLeaseType(String id, String code, String name, String rmk) {
        int res = 0;
        boolean flag = judgeTypeByCode(id, code, name);
        if (flag) {
            LeaseItemType leaseItemType = leaseTypeDao.get(id);
            leaseItemType.setCode(code);
            leaseItemType.setName(name);
            leaseItemType.setRmk(rmk);
            leaseTypeDao.update(leaseItemType);
        } else {
            throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_LEASETYPE_EXISTENCE);
        }
        return res;
    }
    public void deleteLeaseType(String id) {
        boolean flag = judgeTypeById(id);
        if (!flag) {
            throw new BusinessException(CmsLeaseEnum.CMS_BASEDATA_LEASETYPE_ISTENCE);
        }
    }
    public LeaseItemTypeVO getLeaseTypeById(String id) {
        LeaseItemType leaseItemType = leaseTypeDao.get(id);
        LeaseItemTypeVO leaseItemTypeVO = new LeaseItemTypeVO();
        BeanUtils.copyProperties(leaseItemType, leaseItemTypeVO);
        return leaseItemTypeVO;
    }
    @Override
    public BaseDao<LeaseItemType, String> getEntityDao() {
        return leaseTypeDao;
    }
    public void verify(LeaseItemType entity) {
        if (EmptyUtils.isEmpty(entity.getCode())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANCODEEMPTY);
        }
        if (EmptyUtils.isEmpty(entity.getName())) {
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_LOANNAMEEMPTY);
        }
    }
}
