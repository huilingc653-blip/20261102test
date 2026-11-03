package com.bytter.ras.service;

import com.bytter.ras.dao.RasReceivedDao;
import com.bytter.ras.dao.RasRevenueDao;
import com.bytter.ras.vo.RasAuditTaskVO;
import com.bytter.system.api.ITaskService;
import com.bytter.core.base.AbstractService;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.StringUtils;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title: rasDataMoveService
 * @Description: 定时任务稽核数据迁移
 * @author wangzy
 * @date 2019/6/25 14:39
 * @version V1.0
 *
 */
@Service("rasDataMoveService")
@Transactional
public class RasDataMoveService extends AbstractService implements ITaskService {
    @Autowired
    private RasReceivedDao rasReceivedDao;
    @Autowired
    private RasRevenueDao rasRevenueDao;
    @Override
    @XxlJob("RasDataMoveService")
    public void run(String arguments) {
        try{
            this.logger.info("============营收和实收数据迁移开始===========");
            //实收数据
            rasReceivedDao.receivedDataMove();
            rasReceivedDao.delReceivedData("received");
            //营收数据
            rasRevenueDao.revenueDataMove();
            rasRevenueDao.delRevenueData("revenue");
            this.logger.info("=========营收和实收数据迁移结束===========");
        }catch(Exception e){
            e.printStackTrace();
            this.logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            throw new RuntimeException(e);
        }
    }
}
