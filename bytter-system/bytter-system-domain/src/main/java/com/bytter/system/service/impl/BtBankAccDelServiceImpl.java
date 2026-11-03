package com.bytter.system.service.impl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IBtBankAccAppService;
import com.bytter.system.api.IBtBankAccCommonService;
import com.bytter.system.api.IBtBankAccDelService;
import com.bytter.system.dao.BtBankAccDelDao;
import com.bytter.system.entity.BtBankAccApp;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("btBankAccDelServiceI")
@DubboService
@Transactional
public class BtBankAccDelServiceImpl extends BaseService<BtBankAccApp, String> implements IBtBankAccDelService {
    @Autowired
    private BtBankAccDelDao btBankAccDelDao;
    @Autowired
    private IBtBankAccCommonService btBankAccCommonServiceI;
    @Autowired
    private IBtBankAccAppService btBankAccAppService;
    @Override
    public BaseDao<BtBankAccApp, String> getEntityDao() {
        return btBankAccDelDao;
    }
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        DataTablesOutput output = btBankAccDelDao.getDataTablesUnion(dataTablesInput, joinSql);
        output = this.btBankAccAppService.setJoinField(output);
        btBankAccCommonServiceI.setCurName(output);
        return output;
    }
}
