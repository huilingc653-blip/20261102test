package com.bytter.basic.service;

import com.bytter.basic.dao.LogDao;
import com.bytter.basic.entity.BtLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.SysResourceConstant;
import com.bytter.system.api.ICorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhouj on 2017/5/17.
 */
@Service("logService")
@Transactional
public class LogService extends BaseService<BtLog, String> {
    @Autowired
    private LogDao logDao;

    @Autowired(required = false)
    private ICorpService corpServiceI;

    @Override
    public BaseDao<BtLog, String> getEntityDao() {
        return logDao;
    }

    public DataTablesOutput getLogList(DataTablesInput input, ICurrentUser user) {
        List<String> userCorpIds = corpServiceI.getUserCorpIds(SysResourceConstant.SYS_LOG_MANAGE_VIEW, user.getId());
        return logDao.getLogList(input, userCorpIds, user.getId());
    }

}
