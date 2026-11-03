package com.bytter.biz.erp.component;


import com.bytter.biz.erp.entity.BisServerParam;
import com.bytter.biz.erp.service.BisServerParamService;
import com.bytter.erp.common.constant.ErpConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 服务端参数初始化
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/8
 */
@Slf4j
@Component
@RefreshScope
public class CommonParam {

    @Value("${spring.redis.switch}")
    private boolean redisEnable;

    @Autowired
    private BisServerParamService serverParamService;

    private static Map<String, Object> sysParam = new HashMap<String, Object>();

    /**
     * 根据code获取对映参数值
     *
     * @param code
     * @return
     */
    public String getStrVal(String code) {
        return sysParam.get(code) == null ? "" : sysParam.get(code).toString();
    }

    public long getLongVal(String code) {
        return sysParam.get(code) == null ? -1 : Long.parseLong(sysParam.get(code).toString());
    }

    /**
     * 根据code获取对映参数值
     *
     * @param code
     * @return
     */
    public int getIntVal(String code) {
        return sysParam.get(code) == null ? -1 : Integer.parseInt(sysParam.get(code).toString());
    }

    @PostConstruct
    public void init() {
        log.info("服务端启动，初始化系统参数");
        sysParam.put(ErpConstant.REDIS_ENABLE, isRedisEnable());
        log.info("加载参数-是否启用redis：{}", isRedisEnable());
        //数据库参数
        List<BisServerParam> params = serverParamService.listParams();
        for (BisServerParam param : params) {
            sysParam.put(param.getParamCode(), param.getParamValue());
            log.info("参数：[{}]，值：[{}]，说明：[{}]", param.getParamCode(), param.getParamValue(), param.getDescription());
        }
        log.info("参数初始化完成");

    }

    public boolean isRedisEnable() {
        return redisEnable;
    }

    public void setRedisEnable(boolean redisEnable) {
        this.redisEnable = redisEnable;
    }
}
