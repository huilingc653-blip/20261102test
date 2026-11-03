package com.bytter.workflow.domain.service;

import com.bytter.core.base.entity.CurrentUser;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.domain.entity.FlowCategory;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FlowMessageService {
    private final FlowCategoryService flowCategoryService;
    private final StreamBridge streamBridge;

    private final FlowMsgRecordService flowMsgRecordService;
    @DubboReference
    private IUserService userService;

    public FlowMessageService(FlowCategoryService flowCategoryService, StreamBridge streamBridge, FlowMsgRecordService flowMsgRecordService) {
        this.flowMsgRecordService = flowMsgRecordService;
        this.flowCategoryService = flowCategoryService;
        this.streamBridge = streamBridge;
    }


    /**
     * 根据分类获取目标 service
     */
    private String getInstanceByCateKey(String cateKey) {
        FlowCategory flowCategory = flowCategoryService.findByKey(cateKey);
        return flowCategory.getServiceId();
    }
    private CurrentUser buildUser(String uid) {
        UserDTO userDTO = userService.getByUserId(uid);
        CurrentUser currentUser = new CurrentUser();
        currentUser.setId(userDTO.getId());
        currentUser.setUsername(userDTO.getUsername());
        currentUser.setCorpId(userDTO.getCorpId());
        currentUser.setLoginName(userDTO.getLoginName());
        return currentUser;
    }
}
