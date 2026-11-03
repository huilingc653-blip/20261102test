package com.bytter.biz.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.dao.NisAllocationReqDao;
import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.allocation.vo.NisallocationPosApproveVO;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.NotificationConstant;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 头寸调拨审批企微消息提醒
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/12/25
 */
@Slf4j
@Component
public class NisallocationPosApproveJob implements ITaskService {
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private NisAllocationReqService nisAllocationReqService;

    @XxlJob("NisallocationPosApproveJob")
    @Override
    public void run(String arguments) {
        log.info("NisallocationPosApproveJob start");
        // 先清空之前的消息
        notificationService.completeListByTag(NotificationConstant.NETBANK.ALLOCATION_POS_APPROVE);
        // 查询当天待审批的数据
        List<NisallocationPosApproveVO> list = nisAllocationReqService.queryPosApproveList();
        if (ObjectUtil.isNotEmpty(list)){
            Map<String, Object> map = new HashMap<>();
            for (NisallocationPosApproveVO nisallocationPosApproveVO : list) {
                // 发送消息
                map.put("count", nisallocationPosApproveVO.getNum());
                map.put("date", DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
                notificationService.sendToUser("", nisallocationPosApproveVO.getUserId(), NotificationConstant.NETBANK.ALLOCATION_POS_APPROVE,map);
            }
        }

    }
}