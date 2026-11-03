package com.bytter.biz.job;

import com.bytter.biz.bdlinks.service.BisExcService;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.cloud.common.core.persistence.vo.IUser;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.UserDTO;
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
 * @date 2024/5/23
 * 付款成功提醒
 */
@Slf4j
@Component
public class AutoNIsDemandBisExcSuccess implements ITaskService {
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private BisExcService bisExcService;
    @DubboReference(check = false)
    private IUserService userService;

    @XxlJob("AutoNIsDemandBisExcSuccess")
    @Override
    public void run(String arguments) {
        XxlJobHelper.log("开始任务：付款成功提醒");
        //取未生成消息提醒的指令
        List<BisExcVO> list =  bisExcService.listByVoucherStatForSuccessJob();

        notificationService.completeListByTag(NotificationConstant.NETBANK.ALLOCATION_NISDEMAND_SUCCESS);
        if(EmptyUtils.isNotEmpty(list) && list.size()>0) {
            List<String> ids = new ArrayList<>();
            for(BisExcVO bisExcVO : list){
                Map<String, Object> noticeMap = new HashMap<>();
                noticeMap.put("bursarBillNo", bisExcVO.getBursarBillNo());
                noticeMap.put("payerAccName", bisExcVO.getPayerAccName());
                noticeMap.put("payeeAcc", bisExcVO.getPayeeAcc());
                noticeMap.put("payeeAccName", bisExcVO.getPayeeAccName());
                noticeMap.put("payMoney", bisExcVO.getPayMoney());
                noticeMap.put("purpose", bisExcVO.getPurpose());
                UserDTO userDTO = userService.getByLoginName(bisExcVO.getOpCode());
                ids.add(bisExcVO.getId());
                if(!EmptyUtils.isEmpty(userDTO)) {
                    notificationService.sendToUser(bisExcVO.getId(), userDTO.getId(), NotificationConstant.NETBANK.ALLOCATION_NISDEMAND_SUCCESS, noticeMap);
                }
            }
            //更新消息提醒标志
            this.bisExcService.updateNoticeSign(ids);
        }
    }
}
