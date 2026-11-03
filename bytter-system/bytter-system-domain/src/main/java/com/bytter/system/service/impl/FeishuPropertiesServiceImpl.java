package com.bytter.system.service.impl;

import com.bytter.system.api.IFeishuPropertiesService;
import com.bytter.third.service.ThirdProperties;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 飞书平台-配置Service接口
 *
 * @author liyh
 * @date 2022/10/26
 */
@Service("feishuPropertiesServiceI")
@DubboService
public class FeishuPropertiesServiceImpl implements IFeishuPropertiesService {

    @Autowired
    private ThirdProperties thirdProperties;
    @Override
    public String getAppSecret() {
        return this.thirdProperties.getFs().getAppSecret();
    }
    @Override
    public String getAgentId() {
        return this.thirdProperties.getFs().getAgentId();
    }
    @Override
    public boolean getPushMessageSwitch() {
        return this.thirdProperties.getFs().isPushSwitch();
    }
    @Override
    public boolean isPushApprovalCompletedMsgToApplicant() {
        return this.thirdProperties.getFs().isApprovalPush();
    }
}
