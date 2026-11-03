package com.bytter.check.service;
import com.bytter.check.dao.FcAutoCheckResultDao;
import com.bytter.check.entity.FcAutoCheckResult;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("fcAutoCheckResultService")
@Transactional
public class FcAutoCheckResultService extends BaseService<FcAutoCheckResult, String> {
    @Autowired
    private FcAutoCheckResultDao fcAutoCheckResultDao;
    @Override
    public BaseDao<FcAutoCheckResult, String> getEntityDao() {
        return fcAutoCheckResultDao;
    }
    /**
     * 功能介绍 :查询自动对账结果信息
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        return fcAutoCheckResultDao.getDataTable(dataTablesInput);
    }
}
