package com.bytter.system.service.impl;

import com.bytter.system.api.IDingTalkPropertiesService;
import com.bytter.third.service.ThirdProperties;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 钉钉平台-配置Service接口
 *
 * @author chenlw
 * @date 2020/12/02
 */
@Service("dingtalkPropertiesServiceI")
@DubboService
public class DingtalkPropertiesServiceImpl implements IDingTalkPropertiesService {

    @Autowired
    private ThirdProperties thirdProperties;

    @Override
    public String getAppKey() {
        return this.thirdProperties.getDd().getAppId();
    }
    @Override
    public String getAppSecret() {
        return this.thirdProperties.getDd().getAppSecret();
    }
    @Override
    public String getAgentId() {
        return this.thirdProperties.getDd().getAgentId();
    }
    @Override
    public String getCorpId() {
        return this.thirdProperties.getDd().getCorpId();
    }
    @Override
    public boolean getPushMessageSwitch() {
        return this.thirdProperties.getDd().isPushSwitch();
    }
    @Override
    public boolean isPushApprovalCompletedMsgToApplicant() {
        return this.thirdProperties.getDd().isApprovalPush();
    }

}
