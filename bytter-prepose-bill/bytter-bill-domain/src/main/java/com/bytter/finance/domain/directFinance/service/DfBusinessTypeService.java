package com.bytter.finance.domain.directFinance.service;
import com.bytter.finance.domain.directFinance.dao.DfBusinessTypeDao;
import com.bytter.finance.domain.directFinance.entity.DfBusinessType;
import com.bytter.finance.domain.directFinance.vo.DfBusinessTypeVO;
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
@Service("dfBusinessTypeService")
@Transactional
public class DfBusinessTypeService extends BaseService<DfBusinessType, String> {
    @Autowired
    private DfBusinessTypeDao dfBusinessTypeDao;
    /**
     * 查询所有数据
     * 
     * @param input
     * @return
     */
    public DataTablesOutput listBusinessType(DataTablesInput input) {
        return dfBusinessTypeDao.listBusinessType(input);
    }
    /**
     * 检查该费用类型是否存在其他业务数据引用
     * 
     * @param id
     */
    public void deleteCheck(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        String sumCost = dfBusinessTypeDao.getCountBybusiTypeId(params);
        if (!"0".equals(sumCost)) {
            /* 存在其他特务引用则抛出异常 */
            throw new ParameterException(BaseDataMessageEnum.CMS_BASEDATA_OTHERLINK);
        } else {
            dfBusinessTypeDao.delete(id);
        }
    }
    @Override
    public BaseDao getEntityDao() {
        return dfBusinessTypeDao;
    }
    public DfBusinessTypeVO getDfBusinessTypeById(String id) {
        DfBusinessType dfBusinessType = dfBusinessTypeDao.get(id);
        DfBusinessTypeVO dfBusinessTypeVO = new DfBusinessTypeVO();
        BeanUtils.copyProperties(dfBusinessType, dfBusinessTypeVO);
        return dfBusinessTypeVO;
    }
    /**
     * 直融类别数据返回
     * 
     * @param params
     * @return
     */
    public List<DfBusinessTypeVO> listBusinessTypeByParams(Map<String, Object> params) {
        return dfBusinessTypeDao.listBusinessTypeByParams(params);
    }
    /**
     * 编码校验
     * 
     * @param id
     * @param businessCode
     * @return
     */
    public boolean validateBusinessCode(String id, String businessCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("businessCode", businessCode);
        boolean result = true;
        List<DfBusinessTypeVO> dfBusinessTypeVOS = dfBusinessTypeDao.getCountByParams(params);
        if (null == dfBusinessTypeVOS || dfBusinessTypeVOS.size() == 0) {
            return result;
        }
        if (dfBusinessTypeVOS.size() >= 2) {
            result = false;
        } else {
            if (StringUtils.isNotEmpty(id)) {
                if (!id.equals(dfBusinessTypeVOS.get(0).getId())) {
                    result = false;
                }
            } else {
                result = false;
            }
        }
        return result;
    }
}
