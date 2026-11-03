package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.service.ErpNisBillheadService;
import com.bytter.biz.erp.vo.request.PaymentReqVo;
import com.bytter.erp.common.constant.ErpConstant;
import com.bytter.erp.common.constant.ServiceCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 付款单单笔接口
 *
 * @author fangcf
 * @date 2026-04-29
 */
@Slf4j
@Component
public class PaymentSingleStrategy implements BizStrategy {
    @Autowired
    private ErpNisBillheadService erpNisBillheadService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.PAYMENT_SINGLE.getServiceCode();
    }


    @Override
    public Map<String, String> doBiz(Map<String, String> params) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_SUCCESS_STATUS);

        // 请求原文
        String data = params.get(ErpConstant.DO_BIZ_DATA);
        String requestNo = params.get(ErpConstant.DO_BIZ_SERIAL_NO);
        String tenantId = params.get(ErpConstant.DO_BIZ_TENANT_ID);

        // 报文转换成对象
        PaymentReqVo paymentVo = safeParseJson(data, PaymentReqVo.class);
        // 返回实体map
        Map<String, String> resMap = new HashMap<>();
        // 数据校验
        String checkResult = erpNisBillheadService.checkData(paymentVo, tenantId);
        if (ObjectUtil.isNotEmpty(checkResult)) {
            resMap.put("serialNoErp", paymentVo.getSerialNoErp());
            resMap.put("voucherNoErp", paymentVo.getVoucherNoErp());
            resMap.put("status", ErpConstant.SERVER_FAIL_STATUS);
            resMap.put("message", checkResult);
        } else {
            //处理付款单
            resMap = erpNisBillheadService.handlePaymentSingle(paymentVo);
        }

        // 返回
        map.put(ErpConstant.DO_BIZ_DATA, JSONUtil.toJsonStr(resMap));
        return map;
    }

}
