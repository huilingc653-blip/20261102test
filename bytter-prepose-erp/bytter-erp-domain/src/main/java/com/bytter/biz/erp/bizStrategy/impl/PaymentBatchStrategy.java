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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 付款单批量接口
 *
 * @author fangcf
 * @date 2026-05-11
 */
@Slf4j
@Component
public class PaymentBatchStrategy implements BizStrategy {
    @Autowired
    private ErpNisBillheadService erpNisBillheadService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.PAYMENT_BATCH.getServiceCode();
    }


    @Override
    public Map<String, String> doBiz(Map<String, String> params) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_SUCCESS_STATUS);

        // 返回实体map
        List<Map<String, String>> resList = new ArrayList<>();

        // 请求原文
        String data = params.get(ErpConstant.DO_BIZ_DATA);
        String requestNo = params.get(ErpConstant.DO_BIZ_SERIAL_NO);
        String tenantId = params.get(ErpConstant.DO_BIZ_TENANT_ID);

        // 报文转换成对象
        List<PaymentReqVo> paymentVoList = safeParseJsonList(data, PaymentReqVo.class);
        if (ObjectUtil.isEmpty(paymentVoList)) {
            map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            map.put(ErpConstant.DO_BIZ_MSG, "请求报文格式不正确，解析失败！");
            return map;
        }
        for (PaymentReqVo paymentVo : paymentVoList) {
            // 返回对象
            Map<String, String> resMap = new HashMap<>();
            // 校验参数
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
            resList.add(resMap);
        }

        map.put(ErpConstant.DO_BIZ_DATA, JSONUtil.toJsonStr(resList));
        return map;
    }

}
