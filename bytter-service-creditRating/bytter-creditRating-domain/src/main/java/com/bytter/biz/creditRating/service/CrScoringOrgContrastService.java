package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrScoringOrgContrastDao;
import com.bytter.biz.creditRating.entity.CrScoringOrgContrast;
import com.bytter.biz.creditRating.vo.CrdRawDataVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesOutput;

import com.bytter.system.api.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 评分机构评分指标对照表service
 * @author huangyl
 */
@Service(value = "crScoringOrgContrastService")
@Transactional
public class CrScoringOrgContrastService extends BaseService<CrScoringOrgContrast, String> implements IWorkFlowListener, ITaskService {

    @Autowired
    private CrScoringOrgContrastDao orgContrastDao;

    @Override
    public BaseDao<CrScoringOrgContrast, String> getEntityDao() {
        return orgContrastDao;
    }

    @Transactional(readOnly = true)
    public DataTablesOutput dataCollection(ICurrentUser user) {
        return new DataTablesOutput();
    }

    @Transactional(readOnly = true)
    public DataTablesOutput queryAll(String ids) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<CrdRawDataVo> queryByIds(List<String> ids) {
        return null;
    }

    public void modify(CrdRawDataVo dataVo, ICurrentUser user) {
    }


    public boolean deleteById(String id, ICurrentUser user) {
        return true;
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void run(String arguments) {

    }

    public void deleteByTaskNo(String taskNo) {
        orgContrastDao.deleteByTaskNo(taskNo);
    }
}
