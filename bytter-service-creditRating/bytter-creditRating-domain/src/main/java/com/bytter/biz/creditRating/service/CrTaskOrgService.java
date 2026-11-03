package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.dao.CrTaskOrgDao;
import com.bytter.biz.creditRating.entity.CrTaskOrgInfo;
import com.bytter.biz.creditRating.vo.CrTaskOrgInfoVo;
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
 * 任务机构表service
 *
 * @author huangyl
 */
@Service(value = "crTaskOrgService")
@Transactional
public class CrTaskOrgService extends BaseService<CrTaskOrgInfo, String> implements IWorkFlowListener, ITaskService {

    @Autowired
    private CrTaskOrgDao taskOrgDao;

    @Override
    public BaseDao<CrTaskOrgInfo, String> getEntityDao() {
        return taskOrgDao;
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
        taskOrgDao.deleteByTaskNo(taskNo);
    }

    public List<CrTaskOrgInfoVo> getListTaskNoByOrgName(String orgName) {
        return taskOrgDao.getListTaskNoByOrgName(orgName);
    }

    public Map<String, List<CrTaskOrgInfoVo>> getListByTaskNoList(List<String> taskNoList) {
        List<CrTaskOrgInfoVo> taskCorpList = taskOrgDao.getListByTaskNoList(taskNoList);
        return taskCorpList.stream().collect(Collectors.groupingBy(CrTaskOrgInfoVo::getTaskNo));
    }

    public List<CrTaskOrgInfoVo> getListByTaskNo(String taskNo) {
        return taskOrgDao.getListByTaskNo(taskNo);
    }

    public DataTablesOutput queryByOrgName(String orgName, ICurrentUser user){

        return  taskOrgDao.queryByOrgName(orgName);

    }
}
