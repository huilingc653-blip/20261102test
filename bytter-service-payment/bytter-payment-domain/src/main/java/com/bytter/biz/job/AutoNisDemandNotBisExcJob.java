package com.bytter.biz.job;

import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.NotificationConstant;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qijh
 * @date 2024/5/9.
 * 资金需求未产生下拨指令消息提醒
 */
@Slf4j
@Component
public class AutoNisDemandNotBisExcJob implements ITaskService {
    @Autowired
    private NisDemandService nisDemandService;
    @DubboReference(check = false)
    private INotificationService notificationService;


    @XxlJob("AutoNisDemandNotBisExcJob")
    @Override
    public void run(String arguments) {
        XxlJobHelper.log("此任务停用。");
        /*log.info("Execute task: AutoNisDemandNotBisExcJob");
        List<NisDemandVO> list = new ArrayList<>();
        List<NisDemandVO> payList = nisDemandService.listByAutoAllocationPay();
        if (EmptyUtils.isNotEmpty(payList)) {
            list.addAll(payList);
        }

        //承兑到期
        List<NisDemandVO> billList = nisDemandService.listByAutoAllocation(NisDemandConstant.DEMANDTYPE_02);
        if (EmptyUtils.isNotEmpty(billList)) {
            list.addAll(billList);
        }
        //融资到期
        List<NisDemandVO> cmsList = nisDemandService.listByAutoAllocation(NisDemandConstant.DEMANDTYPE_03);
        if (EmptyUtils.isNotEmpty(cmsList)) {
            list.addAll(cmsList);
        }

        notificationService.completeListByTag(NotificationConstant.NETBANK.ALLOCATION_NISDEMAND_NOTBISEXC);
        if(EmptyUtils.isNotEmpty(list) && list.size()>0) {
            Map<String, Integer> noticeMap = new HashMap<>();
            noticeMap.put("count", list.size());
            notificationService.sendToUserByResPermission(ResourceConstant.NETBANK_NISDEMAND_APPLY, NotificationConstant.NETBANK.ALLOCATION_NISDEMAND_NOTBISEXC, noticeMap);
        }*/

    }
}
