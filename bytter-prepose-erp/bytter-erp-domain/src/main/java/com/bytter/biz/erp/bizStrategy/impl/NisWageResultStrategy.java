package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.entity.ErpNisWageResultView;
import com.bytter.biz.erp.service.ErpNisWageService;
import com.bytter.biz.erp.vo.request.NisWageResultReqVo;
import com.bytter.biz.erp.vo.response.NisWageResultRespVo;
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
 * 代发工资结果查询策略实现
 *
 * @author fangcf
 * @date 2026-05-13
 */
@Slf4j
@Component
public class NisWageResultStrategy implements BizStrategy {
    @Autowired
    private ErpNisWageService erpNisWageService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.NIS_WAGE_RESULT.getServiceCode();
    }

    @Override
    public Map<String, String> doBiz(Map<String, String> params) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_SUCCESS_STATUS);

        // 请求原文
        String data = params.get(ErpConstant.DO_BIZ_DATA);
        String requestNo = params.get(ErpConstant.DO_BIZ_SERIAL_NO);
        String tenantId = params.get(ErpConstant.DO_BIZ_TENANT_ID);

        // 返回data
        List<NisWageResultRespVo> resultList = new ArrayList<>();

        // 报文转换成对象
        NisWageResultReqVo resultReqVo = safeParseJson(data, NisWageResultReqVo.class);
        String checkResult = checkData(resultReqVo);
        if (ObjectUtil.isNotEmpty(checkResult)) {
            map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            map.put(ErpConstant.DO_BIZ_MSG, checkResult);
        } else {
            resultReqVo.setTenantId(tenantId);
            // 查询付款结果
            List<ErpNisWageResultView> list = erpNisWageService.listErpNisWageResult(resultReqVo.getTenantId(), resultReqVo.getWageNo(), resultReqVo.getWageDtlNo());
            if (ObjectUtil.isNotEmpty(list)) {
                resultList = BeanUtil.copyToList(list, NisWageResultRespVo.class);
                map.put(ErpConstant.DO_BIZ_MSG, "查询成功");
            } else {
                map.put(ErpConstant.DO_BIZ_MSG, "查无数据");
            }
        }
        map.put(ErpConstant.DO_BIZ_DATA, JSONUtil.toJsonStr(resultList));

        return map;
    }

    // 校验必填字段
    private String checkData(NisWageResultReqVo vo) {
        StringBuilder result = new StringBuilder(128);
        // 先判断报文是否正确
        if (ObjectUtil.isEmpty(vo)) {
            appendMsg(result, "请求报文格式不正确，解析失败！");
            return result.toString();
        }
        if (ObjectUtil.isEmpty(vo.getWageNo()) || ObjectUtil.isEmpty(vo.getWageNo().trim())) {
            appendMsg(result, "批次号不能为空");
        } else if (vo.getWageNo().length() > 30) {
            appendMsg(result, "批次号长度不能超过30");
        }
        return result.toString();
    }
}
