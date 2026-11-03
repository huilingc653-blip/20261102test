package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexTranConfigOperationLogDao;
import com.bytter.biz.foreign.entity.FexTranConfigOperationLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("fexTranConfigOperationLogService")
@Transactional
public class FexTranConfigOperationLogService extends BaseService<FexTranConfigOperationLog, String> {
    @Autowired
    private FexTranConfigOperationLogDao fexTranConfigOperationLogDao;


    @Override
    public BaseDao<FexTranConfigOperationLog, String> getEntityDao() {
        return fexTranConfigOperationLogDao;
    }

    /**
     * 列表查询
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return fexTranConfigOperationLogDao.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 新增
     * @param log
     */
    public void save(FexTranConfigOperationLog log) {
         fexTranConfigOperationLogDao.save(log);
    }
}
