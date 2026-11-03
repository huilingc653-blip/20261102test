package com.bytter.system.service.impl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IBtBankAccAppService;
import com.bytter.system.api.IBtBankAccCommonService;
import com.bytter.system.api.IBtBankAccRevService;
import com.bytter.system.dao.BtBankAccRevDao;
import com.bytter.system.dao.BtBankAccRevHandleDao;
import com.bytter.system.entity.BtBankAccApp;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("btBankAccRevServiceI")
@DubboService
@Transactional
public class BtBankAccRevServiceImpl extends BaseService<BtBankAccApp, String> implements IBtBankAccRevService {
    @Autowired
    private BtBankAccRevDao btBankAccRevDao;
    @Autowired
    private BtBankAccRevHandleDao btBankAccRevHandleDao;
    @Autowired
    private IBtBankAccCommonService btBankAccCommonServiceI;
    @Autowired
    private IBtBankAccAppService btBankAccAppService;
    @Override
    public BaseDao<BtBankAccApp, String> getEntityDao() {
        return btBankAccRevDao;
    }
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        DataTablesOutput output = btBankAccRevDao.getDataTablesUnion(dataTablesInput, joinSql);
        output = btBankAccAppService.setJoinField(output);
        btBankAccCommonServiceI.setCurName(output);
        return output;
    }
    @Override
    public DataTablesOutput getChangeDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        DataTablesOutput output =  btBankAccRevDao.getChangeDataTablesUnion(dataTablesInput, joinSql);
        output = btBankAccAppService.setJoinField(output);
        btBankAccCommonServiceI.setCurName(output);
        return output;
    }
    @Override
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String joinSql) {
        DataTablesOutput output =  btBankAccRevHandleDao.dataTableApprove(dataTablesInput, joinSql);
        output = btBankAccAppService.setJoinField(output);
        btBankAccCommonServiceI.setCurName(output);
        return output;
    }
}
