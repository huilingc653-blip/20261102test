package com.bytter.biz.erp.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.bytter.erp.exception.BtVerificationException;
import com.bytter.erp.handler.MessageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * erp服务类
 *
 * @author fangcf
 * @date 2026-05-15
 */
@Slf4j
@Service
public class ErpService {

    @Autowired
    private RedisService redisService;

    /**
     * 有效请求校验
     *
     * @param request 请求报文
     */
    public void validateRequest(MessageBean request) throws BtVerificationException {
        // 校验请求流水号是否重复
        this.checkSerialNoRepeat(request);
    }

    /**
     * 校验请求流水号是否重复
     *
     * @param request 请求报文
     */
    private void checkSerialNoRepeat(MessageBean request) throws BtVerificationException {
        String requestNo = DigestUtil.md5Hex(request.getTenantCode() + request.getSerialNo());
        if (ObjectUtil.isNotEmpty(requestNo)) {
            // 检查流水号是否已经处理过
            if (redisService.checkSerialNoProcessed(requestNo)) {
                log.warn("请求流水号: {} 已经处理过，拒绝重复请求！", requestNo);
                throw new BtVerificationException("流水号已处理，拒绝重复请求！");
            }
        } else {
            log.warn("请求流水号不能为空！");
            throw new BtVerificationException("请求流水号不能为空！");
        }

        // 标记流水号已处理
        redisService.markSerialProcessed(requestNo);
    }
}
