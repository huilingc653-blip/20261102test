package com.bytter.supply.domain.payable.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.supply.domain.payable.dao.ScfCustFieldConfigDao;
import com.bytter.supply.domain.payable.dao.ScfPayConfigDao;
import com.bytter.supply.domain.payable.entity.ScfCustFieldConfig;
import com.bytter.supply.domain.payable.vo.ScfCustFieldConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("scfCustFieldConfigService")
@Transactional(rollbackFor = Exception.class)
public class ScfCustFieldConfigService extends BaseService<ScfCustFieldConfig, String> {

    @Autowired
    private ScfCustFieldConfigDao scfCustFieldConfigDao;

    @Autowired
    private ScfPayConfigDao scfPayConfigDao;

    @Override
    public BaseDao<ScfCustFieldConfig, String> getEntityDao() {
        return scfCustFieldConfigDao;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return scfCustFieldConfigDao.getDataTablesUnion(dataTablesInput);
    }

    public DataTablesOutput getDataTablesUnionValid(DataTablesInput dataTablesInput) {
        return scfCustFieldConfigDao.getDataTablesUnionValid(dataTablesInput);
    }


}
