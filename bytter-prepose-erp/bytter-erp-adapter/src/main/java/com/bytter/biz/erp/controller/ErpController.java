package com.bytter.biz.erp.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.bizStrategy.BizStrategyContext;
import com.bytter.biz.erp.service.ErpService;
import com.bytter.erp.common.constant.ErpConstant;
import com.bytter.erp.exception.BtVerificationException;
import com.bytter.erp.handler.MessageBean;
import com.bytter.system.api.ISysTenantManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 处理controller
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/8
 */
@Slf4j
@RestController
@RequestMapping("/erp-basic")
public class ErpController {

    @DubboReference(check = false)
    private ISysTenantManagerService sysTenantManagerService;
    @Autowired
    private BizStrategyContext strategyContext;
    @Autowired
    private ErpService erpService;

    @Value("${request.mode}")
    private String requestMode;// 请求模式

    @RequestMapping(value = "/execute", method = {RequestMethod.POST})
    public MessageBean execute(@Valid @RequestBody MessageBean request) {
        log.info("收到请求 - 机构: {}, 调用服务: {}, 版本：{},流水号: {}",
                request.getTenantCode(), request.getServiceCode(), request.getVersion(), request.getSerialNo());

        //返回报文体
        MessageBean response = new MessageBean();
        response.setSerialNo(request.getSerialNo());
        response.setTenantCode(request.getTenantCode());
        response.setServiceCode(request.getServiceCode());
        response.setData(request.getData());

        try {

            // 请求模式，部署在企业（ent）时，对请求报文进行参数校验
            if (ErpConstant.REQUEST_MODE_ENT.equals(requestMode)) {
                // 有效请求校验
                erpService.validateRequest(request);
            }

            // 校验客户号是否有效
            String tenantId = sysTenantManagerService.queryTenantIdByCode(request.getTenantCode());
            if (ObjectUtil.isEmpty(tenantId)) {
                throw new BtVerificationException("客户号无效");
            }

            //开始解密获取明文
            String deData = request.getData();
            log.info("流水号: {},请求为：{}", request.getSerialNo(), deData);

            //根据ServiceCode和版本号获取业务处理策略
            BizStrategy strategy = strategyContext.getStrategy(request.getServiceCode(), request.getVersion());
            if (strategy == null) {
                throw new BtVerificationException("请求接口: " + request.getServiceCode() + ", 版本: " + request.getVersion() + " 无效。");
            }

            //封装业务处理请求参数
            Map<String, String> bizParam = new HashMap<String, String>();
            bizParam.put(ErpConstant.DO_BIZ_SERIAL_NO, request.getSerialNo());
            bizParam.put(ErpConstant.DO_BIZ_TENANT_CODE, request.getTenantCode());
            bizParam.put(ErpConstant.DO_BIZ_TENANT_ID, tenantId);
            bizParam.put(ErpConstant.DO_BIZ_DATA, deData);

            //业务处理
            Map<String, String> bizMap = strategy.doBiz(bizParam);
            response.setStatus(bizMap.get(ErpConstant.DO_BIZ_STATUS));
            response.setMsg(bizMap.get(ErpConstant.DO_BIZ_MSG));

            if (!StrUtil.isBlank(bizMap.get(ErpConstant.DO_BIZ_DATA))) {
                String enData = bizMap.get(ErpConstant.DO_BIZ_DATA);
                response.setData(enData);
            }

        } catch (BtVerificationException e) {
            log.error("流水号: {},参数校验异常：", request.getSerialNo(), e);
            response.setStatus(ErpConstant.SERVER_FAIL_STATUS);
            response.setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("流水号: {},业务处理异常：", request.getSerialNo(), e);
            response.setStatus(ErpConstant.SERVER_FAIL_STATUS);
            response.setMsg("业务处理异常，请联系银行相关人员！");
        }
        log.info("流水号: {},返回客户端报文为：{}", request.getSerialNo(), JSONUtil.toJsonStr(response));
        return response;
    }

}
