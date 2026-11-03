package com.bytter.biz.job;

import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.bdlinks.service.BisExcService;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.BusinessTaskDTO;
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
public class NisallocationReqApproveJob implements ITaskService {
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private NisAllocationReqService nisAllocationReqService;

    @XxlJob("NisallocationReqApproveJob")
    @Override
    public void run(String arguments) {
        notificationService.completeListByTag(NotificationConstant.NETBANK.ALLOCATION_ASK_APPROVE);
        //拿到所有的用户
        List<UserDTO> userDTOList =  userService.listAllUser();
        if(EmptyUtils.isNotEmpty(userDTOList) && userDTOList.size()>0){
            for(UserDTO userDTO:userDTOList){
                Map<String, Object> noticeMap = new HashMap<>();
                //获取用户待审批的请款数据 netbankAllocationAsk
                List<BusinessTaskDTO> list = workFlowService.listBusinessTask(userDTO.getId(),"netbankAllocationAsk");
                if(EmptyUtils.isNotEmpty(list) && list.size()>0){
                    int count = 0;
                    List<String> ids = new ArrayList<>();
                    for (BusinessTaskDTO businessTaskDTO : list) {
                        ids.add(businessTaskDTO.getBusinessKey());
                    }
                    count = nisAllocationReqService.countByIdsNoDate(ids);
                    if(count > 0) {
                        String Date = DateUtils.getDate("yyyy-MM-dd HH:mm:ss");
                        noticeMap.put("count", count);
                        noticeMap.put("date", Date);
                        notificationService.sendToUser(null, userDTO.getId(), NotificationConstant.NETBANK.ALLOCATION_ASK_APPROVE, noticeMap);
                    }
                }
            }
        }
    }
}
