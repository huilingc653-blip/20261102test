package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.entity.ErpPaymentResultView;
import com.bytter.biz.erp.service.ErpNisBillheadService;
import com.bytter.biz.erp.vo.request.PaymentSingleResultReqVo;
import com.bytter.biz.erp.vo.response.PaymentResultRespVo;
import com.bytter.erp.common.constant.ErpConstant;
import com.bytter.erp.common.constant.ServiceCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 付款单单笔状态查询接口
 *
 * @author fangcf
 * @date 2026-05-11
 */
@Slf4j
@Component
public class PaymentSingleResultStrategy implements BizStrategy {
    @Autowired
    private ErpNisBillheadService nisBillheadService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.PAYMENT_SINGLE_RESULT.getServiceCode();
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
        PaymentSingleResultReqVo requestVo = safeParseJson(data, PaymentSingleResultReqVo.class);

        // 校验必填字段
        String checkResult = checkData(requestVo);
        if (ObjectUtil.isNotEmpty(checkResult)) {
            map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            map.put(ErpConstant.DO_BIZ_MSG, checkResult);
            return map;
        }

        // 查询付款结果
        List<ErpPaymentResultView> list = nisBillheadService.getPaymentSingleResult(tenantId, requestVo.getSerialNoErp(), requestVo.getVoucherNoErp());
        if (ObjectUtil.isNotEmpty(list)) {
            PaymentResultRespVo respVo = new PaymentResultRespVo();
            BeanUtils.copyProperties(list.get(0), respVo);
            map.put(ErpConstant.DO_BIZ_DATA, JSONUtil.toJsonStr(respVo));
            map.put(ErpConstant.DO_BIZ_MSG, "查询成功");
        } else {
            map.put(ErpConstant.DO_BIZ_MSG, "查无数据");
        }

        return map;
    }

    // 校验必填字段
    private String checkData(PaymentSingleResultReqVo vo) {
        StringBuilder result = new StringBuilder(128);

        // 先判断报文是否正确
        if (ObjectUtil.isEmpty(vo)) {
            appendMsg(result, "请求报文格式不正确，解析失败！");
            return result.toString();
        }

        if (ObjectUtil.isEmpty(vo.getSerialNoErp()) || ObjectUtil.isEmpty(vo.getSerialNoErp().trim())) {
            appendMsg(result, "流水号不能为空");
        } else if (vo.getSerialNoErp().length() > 32) {
            appendMsg(result, "流水号长度不能超过32");
        }
        if (ObjectUtil.isEmpty(vo.getVoucherNoErp()) || ObjectUtil.isEmpty(vo.getVoucherNoErp().trim())) {
            appendMsg(result, "凭证号不能为空");
        } else if (vo.getVoucherNoErp().length() > 25) {
            appendMsg(result, "凭证号长度不能超过25");
        }

        return result.toString();
    }
}
