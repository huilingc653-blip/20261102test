package com.bytter.biz.transfer.swap.service;
import com.bytter.biz.transfer.swap.dao.ErpDataTypeFieldDao;
import com.bytter.biz.transfer.swap.entity.ErpDataTypeField;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Transactional
@Service("erpDataTypeFieldService")
public class ErpDataTypeFieldService extends BaseService<ErpDataTypeField,String> {
    @Autowired
    private ErpDataTypeFieldDao erpDataTypeFieldDao;
    @Override
    public BaseDao<ErpDataTypeField, String> getEntityDao() {
        return erpDataTypeFieldDao;
    }
    public List<ErpDataTypeField> queryErpDataTypeFieldValid(String type) {
        return erpDataTypeFieldDao.queryErpDataTypeFieldValid(type);
    }
}
