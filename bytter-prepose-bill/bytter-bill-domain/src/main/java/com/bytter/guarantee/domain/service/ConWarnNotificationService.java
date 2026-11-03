package com.bytter.guarantee.domain.service;
import com.bytter.core.base.AbstractService;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.vo.ConWarnRulesVO;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.NotificationConstant;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * @Title: ConWarnNotificationService
 * @Description: 合同预警定时消息提醒
 * @author wangzy
 * @date 2019/11/6 16:18
 *
 */
@Service("conWarnNotificationService")
@Transactional
public class ConWarnNotificationService extends AbstractService implements ITaskService {
    @Autowired
    private ConWarnRulesService conWarnRulesService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Override
    @XxlJob("ConWarnNotificationService")
    public void run(String arguments) {
        try {
            this.logger.info("============合同预警消息提醒任务开始===========");
            Date today = DateUtils.getDateYMD();
            List<ConWarnRulesVO> list = conWarnRulesService.listConWarnInfoVO();
            for (ConWarnRulesVO conWarnInfoVO : list) {
                if (conWarnInfoVO != null && StringUtils.isNotEmpty(conWarnInfoVO.getEndDate())
                        && StringUtils.isNotEmpty(conWarnInfoVO.getBeforDay())
                        && StringUtils.isNotEmpty(conWarnInfoVO.getIntervalDay())) {
                    // 开始提醒日期
                    Date beginDay = DateUtils.addDay(DateUtils.getDateYMD(conWarnInfoVO.getEndDate()),
                            -conWarnInfoVO.getBeforDay());
                    if (today.getTime() == beginDay.getTime()) {
                        notificationService.send(conWarnInfoVO.getConId(), conWarnInfoVO.getCorpId(),
                                NotificationConstant.CONTRACT.CONTRACT_WARN_END, conWarnInfoVO);
                    } else if (today.getTime() > beginDay.getTime()) {
                        // 预警提醒天数循环
                        while (true) {
                            // 下一需要执行的日期
                            Date runDate = DateUtils.addDay(beginDay, conWarnInfoVO.getIntervalDay() + 1);
                            if (today.getTime() == runDate.getTime()) {
                                notificationService.send(conWarnInfoVO.getConId(), conWarnInfoVO.getCorpId(),
                                        NotificationConstant.CONTRACT.CONTRACT_WARN_END, conWarnInfoVO);
                                break;
                            } else if (runDate.getTime() > today.getTime()) {
                                // 下一需要执行的日期大于当前时间跳出循环
                                break;
                            }
                            // 将开始日期更新为最近一个执行时间
                            beginDay = runDate;
                        }
                    }
                }
            }
            this.logger.info("=============合同预警消息提醒任务结束===========");
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            throw new RuntimeException(e);
        }
    }
}
