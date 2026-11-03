package com.bytter.finance.domain.directFinance.service;
import com.bytter.finance.domain.directFinance.dao.DfCostTypeDao;
import com.bytter.finance.domain.directFinance.entity.DfCostType;
import com.bytter.finance.domain.directFinance.vo.DfCostTypeVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 16:39
 * @Version 1.0
 */
@Service("dfCostTypeService")
@Transactional
public class DfCostTypeService extends BaseService<DfCostType, String> {
    @Autowired
    private DfCostTypeDao dfCostTypeDao;
    /**
     * 查询所有数据
     * 
     * @param input
     * @return
     */
    public DataTablesOutput listCostType(DataTablesInput input) {
        return dfCostTypeDao.listCostType(input);
    }
    @Override
    public BaseDao getEntityDao() {
        return dfCostTypeDao;
    }
    public DfCostTypeVO getDfCostTypeById(String id) {
        DfCostType dfCostType = dfCostTypeDao.get(id);
        DfCostTypeVO dfCostTypeVO = new DfCostTypeVO();
        BeanUtils.copyProperties(dfCostType, dfCostTypeVO);
        return dfCostTypeVO;
    }
    /**
     * 检查该费用类型是否存在其他业务数据引用
     * 
     * @param id
     */
    public void deleteCheck(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        String sumCost = dfCostTypeDao.getCountCostTypeById(params);
        if (!"0".equals(sumCost)) {
            /* 存在其他特务引用则抛出异常 */
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_OTHERLINK);
        } else {
            dfCostTypeDao.delete(id);
        }
    }
    /**
     * 编码校验
     * 
     * @param id
     * @param costCode
     * @return
     */
    public boolean validateCostCode(String id, String costCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("costCode", costCode);
        boolean result = true;
        List<DfCostTypeVO> dfCostTypeVOS = dfCostTypeDao.getCountByParams(params);
        if (null == dfCostTypeVOS || dfCostTypeVOS.size() == 0) {
            return result;
        }
        if (dfCostTypeVOS.size() >= 2) {
            result = false;
        } else {
            if (StringUtils.isNotEmpty(id)) {
                if (!id.equals(dfCostTypeVOS.get(0).getId())) {
                    result = false;
                }
            } else {
                result = false;
            }
        }
        return result;
    }
    /**
     * 返回有效数据
     * 
     * @return
     */
    public List<DfCostTypeVO> getValidDfcostType() {
        return dfCostTypeDao.getValidDfCostType(null);
    }
}
