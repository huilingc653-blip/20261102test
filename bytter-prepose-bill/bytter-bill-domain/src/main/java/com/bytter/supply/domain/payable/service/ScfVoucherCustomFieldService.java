package com.bytter.supply.domain.payable.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.payable.dao.ScfVoucherCustomFieldDao;
import com.bytter.supply.domain.payable.entity.ScfVoucherCustomField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("scfVoucherCustomFieldService")
@Transactional(rollbackFor = Exception.class)
public class ScfVoucherCustomFieldService extends BaseService<ScfVoucherCustomField, String> {

    @Autowired
    private ScfVoucherCustomFieldDao scfVoucherCustomFieldDao;

    @Override
    public BaseDao<ScfVoucherCustomField, String> getEntityDao() {
        return scfVoucherCustomFieldDao;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return scfVoucherCustomFieldDao.getDataTablesUnion(dataTablesInput);
    }
}
