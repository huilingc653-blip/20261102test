package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.service.ErpNisWageService;
import com.bytter.biz.erp.vo.ErpNisWageVo;
import com.bytter.erp.common.constant.ErpConstant;
import com.bytter.erp.common.constant.ServiceCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 代发工资接口策略实现
 *
 * @author fangcf
 * @date 2026-05-13
 */
@Slf4j
@Component
public class NisWageStrategy implements BizStrategy {
    @Autowired
    private ErpNisWageService nisWageService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.NIS_WAGE.getServiceCode();
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
        ErpNisWageVo wageVo = safeParseJson(data, ErpNisWageVo.class);

        // 返回实体map
        Map<String, Object> dataMap = new HashMap<>();

        // 数据校验
        String checkResult = nisWageService.checkData(wageVo, tenantId);
        if (ObjectUtil.isNotEmpty(checkResult)) {
            dataMap.put("wageNo", wageVo.getWageNo());
            dataMap.put("status", ErpConstant.SERVER_FAIL_STATUS);
            dataMap.put("message", checkResult);
        } else {
            //处理付款单
            dataMap = nisWageService.handleNisWage(wageVo);
        }

        map.put(ErpConstant.DO_BIZ_DATA, JSONUtil.toJsonStr(dataMap));
        return map;
    }

}
