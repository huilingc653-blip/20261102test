package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.entity.ErpPaymentResultView;
import com.bytter.biz.erp.service.ErpNisBillheadService;
import com.bytter.biz.erp.vo.request.PaymentBatchResultReqVo;
import com.bytter.biz.erp.vo.response.PaymentResultRespVo;
import com.bytter.erp.common.constant.ErpConstant;
import com.bytter.erp.common.constant.ServiceCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 付款单批量状态查询接口
 *
 * @author fangcf
 * @date 2026-05-11
 */
@Slf4j
@Component
public class PaymentBatchResultStrategy implements BizStrategy {
    @Autowired
    private ErpNisBillheadService nisBillheadService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.PAYMENT_BATCH_RESULT.getServiceCode();
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
        List<PaymentBatchResultReqVo> payQueryList = safeParseJsonList(data, PaymentBatchResultReqVo.class);
        if (ObjectUtil.isEmpty(payQueryList)) {
            map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            map.put(ErpConstant.DO_BIZ_MSG, "请求报文格式不正确，解析失败！");
            return map;
        }
        List<PaymentResultRespVo> resultList = new ArrayList<>();
        PaymentResultRespVo respVo = null;
        for (PaymentBatchResultReqVo payQuery : payQueryList) {
            String checkResult = checkData(payQuery);
            if (ObjectUtil.isEmpty(checkResult)) {
                // 查询付款结果
                List<ErpPaymentResultView> list = nisBillheadService.getPaymentSingleResult(tenantId, payQuery.getSerialNoErp(), payQuery.getVoucherNoErp());
                if (ObjectUtil.isNotEmpty(list)) {
                    respVo = new PaymentResultRespVo();
                    BeanUtils.copyProperties(list.get(0), respVo);
                    resultList.add(respVo);
                }
            } else {
                respVo = new PaymentResultRespVo();
                respVo.setSerialNoErp(payQuery.getSerialNoErp());
                respVo.setVoucherNoErp(payQuery.getVoucherNoErp());
                respVo.setReturnMsg(checkResult);
                resultList.add(respVo);
            }
        }

        map.put(ErpConstant.DO_BIZ_DATA, JSONUtil.toJsonStr(resultList));
        return map;
    }

    // 校验必填字段
    private String checkData(PaymentBatchResultReqVo vo) {
        StringBuilder result = new StringBuilder(128);

        if (ObjectUtil.isEmpty(vo.getSerialNoErp()) || ObjectUtil.isEmpty(vo.getSerialNoErp().trim())) {
            appendMsg(result, "流水号不能为空");
        } else if (vo.getSerialNoErp().length() > 32) {
            appendMsg(result, "流水号长度不能超过32");
        }
        if (ObjectUtil.isEmpty(vo.getVoucherNoErp()) || ObjectUtil.isEmpty(vo.getVoucherNoErp().trim())) {
            appendMsg(result, "凭证号不能为空");
        } else if (vo.getVoucherNoErp().length() > 25) {
            appendMsg(result, "凭证号长度不能超过32");
        }

        return result.toString();
    }
}
