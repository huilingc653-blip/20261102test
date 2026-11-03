package com.bytter.ras.service;

import com.bytter.ras.dao.RasAuditDao;
import com.bytter.ras.dao.RasReceivedDao;
import com.bytter.ras.dao.RasRevenueDao;
import com.bytter.system.api.ITaskService;
import com.bytter.core.base.AbstractService;
import com.bytter.core.utils.ExceptionStackUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: rasHisDataMoveService
 * @Description: 历史、作废数据的迁移
 * @author wangzy
 * @date 2019/6/25 16:58
 * @version V1.0
 *
 */
@Service("rasHisDataMoveService")
@Transactional
public class RasHisDataMoveService extends AbstractService implements ITaskService {
    @Autowired
    private RasConfigService rasConfigService;
    @Autowired
    private RasReceivedDao rasReceivedDao;
    @Autowired
    private RasRevenueDao rasRevenueDao;
    @Autowired
    private RasAuditDao rasAuditDao;

    @Override
    @XxlJob("RasHisDataMoveService")
    public void run(String arguments) {
        try{
            this.logger.info("============历史、作废数据迁移开始===========");
            int hisDay = rasConfigService.getHisMoveDay();
            //现金、其它的历史数据迁移
            rasRevenueDao.revenueMoneyHisDataMove(hisDay);
            rasRevenueDao.delMoneyRevenueData(hisDay);
            rasReceivedDao.receivedMoneyHisDataMove(hisDay);
            rasReceivedDao.delMoneyReceivedData(hisDay);
            //稽核表中间数据
            rasAuditDao.auditHisDataMove(hisDay);
            rasAuditDao.delAuditData(hisDay);
            //实收数据
            rasReceivedDao.receivedCancelMove();
            rasReceivedDao.delReceivedData("receivedCancel");
            rasReceivedDao.receivedHisDataMove();
            rasReceivedDao.delReceivedData("receivedHis");
            //营收数据
            rasRevenueDao.revenueCancelMove();
            rasRevenueDao.delRevenueData("revenueCancel");
            rasRevenueDao.revenueHisDataMove();
            rasRevenueDao.delRevenueData("revenueHis");
            this.logger.info("==========历史、作废数据迁移结束===========");
        }catch(Exception e){
            e.printStackTrace();
            this.logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            throw new RuntimeException(e);
        }
    }
}
