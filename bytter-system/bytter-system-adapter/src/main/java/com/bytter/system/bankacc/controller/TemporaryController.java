package com.bytter.system.bankacc.controller;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.workflow.bpm.context.WorkFlowRetryHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author YonJar
 * @date 2023/6/28.
 * @description
 */
@Deprecated
@RestController
@RequestMapping("/v1-system")
public class TemporaryController {

    @Resource
    private WorkFlowRetryHandler workFlowRetryHandler;

    /*@Autowired
    private InitNetDayCloseMQProducer producer;
    @GetMapping("/test01")
    public void test(@RequestParam("netId") String netId, @RequestParam("userId") String userId){
        // admin: userId = 402880425b6b0a58015b6b0abeb10000 , netId = 402880425b8abcfb015b8ac16e980000
        InitNetDayCloseMessageDTO bo = InitNetDayCloseMessageDTO.builder().netId(netId).userId(userId).build();
        producer.sendMessage(bo);
        System.out.println("已发送消息！");
    }*/

    /**
     * 重试工作流审批后的方法--请使用对应的租户登录，再执行
     * @param id
     * @return
     */
    @GetMapping("/retry/workflowBus")
    public Boolean retry(@RequestParam("id") String id){
        ICurrentUser user = UserContextHelper.helper().getCurrentUser();
        workFlowRetryHandler.retry(id, user);
        return true;
    }
}
