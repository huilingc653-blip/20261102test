package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrdDataSourceCategoryDao;
import com.bytter.biz.creditRating.entity.CrdDataSourceCategory;
import com.bytter.biz.creditRating.vo.CrdDataSourceCategoryVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.dto.IWorkFlowResult;

import com.bytter.system.api.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "dataSourceCategoryService")
public class CrdDataSourceCategoryService extends BaseService<CrdDataSourceCategory, String> implements IWorkFlowListener, ITaskService {

    @Autowired
    private CrdDataSourceCategoryDao dataSourceCategoryDao;

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void run(String arguments) {

    }

    @Override
    public BaseDao<CrdDataSourceCategory, String> getEntityDao() {
        return dataSourceCategoryDao;
    }

    public List<CrdDataSourceCategoryVo> dataTable(){
        return dataSourceCategoryDao.dataTable();
    }
}
