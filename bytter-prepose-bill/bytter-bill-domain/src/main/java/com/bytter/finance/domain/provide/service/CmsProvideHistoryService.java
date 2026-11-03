package com.bytter.finance.domain.provide.service;


import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;

import com.bytter.finance.domain.provide.dao.CmsProvideHistoryDao;
import com.bytter.finance.domain.provide.entity.CmsProvideHistory;
import com.bytter.finance.domain.provide.vo.LoanQueryVO;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2023/10/25<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Service("cmsProvideHistoryService")
@Transactional
public class CmsProvideHistoryService extends BaseService<CmsProvideHistory, String> implements ITaskService {
    @Autowired
    private CmsProvideHistoryDao cmsProvideHistoryDao;
    @Override
    public BaseDao<CmsProvideHistory, String> getEntityDao() {
        return cmsProvideHistoryDao;
    }

    /**
     * 功能介绍 : 借款历史查询<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryHistoryLoan(DataTablesInput dataTablesInput, ICurrentUser user) {
        return cmsProvideHistoryDao.queryHistoryLoan(dataTablesInput);
    }


    @Override
    @XxlJob("CmsProvideHistoryService")
    public void run(String arguments) {
        logger.info("银行借款历史数据每日保存任务开始！");
        Date nowDate=new Date();
        nowDate= DateUtils.parse(DateUtils.format(nowDate,"yyyy-MM-dd"),"yyyy-MM-dd");
        nowDate= DateUtils.addDay(nowDate,-1);
        List<LoanQueryVO> loanQueryVOList= cmsProvideHistoryDao.queryLoanForReport();
        List<CmsProvideHistory> cmsProvideHistoryList=new ArrayList<>();
        for(LoanQueryVO loanQueryVO:loanQueryVOList){
            CmsProvideHistory cmsProvideHistory=new CmsProvideHistory();
            BeanUtils.copyNotNullProperties(loanQueryVO, cmsProvideHistory);
            cmsProvideHistory.setReportDate(nowDate);
            if(!EmptyUtils.isEmpty(loanQueryVO.getSendBizStatus())){
                cmsProvideHistory.setBizStatus(loanQueryVO.getSendBizStatus());
            }else {
                cmsProvideHistory.setBizStatus(loanQueryVO.getInfoBizStatus());
            }
            cmsProvideHistoryList.add(cmsProvideHistory);
        }
        cmsProvideHistoryDao.deleteHisProvide(nowDate);//防止重复执行时数据重复。
        this.batchSave(cmsProvideHistoryList);
        logger.info("银行借款历史数据每日保存任务结束！");
    }
}
