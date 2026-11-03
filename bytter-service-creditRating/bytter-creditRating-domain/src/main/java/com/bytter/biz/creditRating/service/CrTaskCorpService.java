package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrTaskCorpDao;
import com.bytter.biz.creditRating.entity.CrTaskCorpInfo;
import com.bytter.biz.creditRating.vo.CrTaskCorpInfoVo;
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
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 任务组织表service
 *
 * @author huangyl
 */
@Service(value = "crTaskCorpService")
@Transactional
public class CrTaskCorpService extends BaseService<CrTaskCorpInfo, String> implements IWorkFlowListener, ITaskService {

    @Autowired
    private CrTaskCorpDao taskCorpDao;

    @Override
    public BaseDao<CrTaskCorpInfo, String> getEntityDao() {
        return taskCorpDao;
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
        taskCorpDao.deleteByTaskNo(taskNo);
    }

    public List<CrTaskCorpInfoVo> getListTaskNoByCorpId(String corpIds) {
        return taskCorpDao.getListTaskNoByCorpId(corpIds);
    }

    public List<CrTaskCorpInfoVo> getListTaskNoByTaskNo(String taskNo) {
        return taskCorpDao.getListTaskNoByTaskNo(taskNo);
    }

    public Map<String, List<CrTaskCorpInfoVo>> getListByTaskNoList(List<String> taskNoList) {
        List<CrTaskCorpInfoVo> taskCorpList = taskCorpDao.getListByTaskNoList(taskNoList);
        return taskCorpList.stream().collect(Collectors.groupingBy(CrTaskCorpInfoVo::getTaskNo));
    }
}
