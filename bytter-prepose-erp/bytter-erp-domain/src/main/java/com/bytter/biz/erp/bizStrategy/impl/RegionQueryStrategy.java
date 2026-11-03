package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.entity.ErpRegionQueryView;
import com.bytter.biz.erp.service.RegionQueryService;
import com.bytter.biz.erp.vo.request.RegionRequestVO;
import com.bytter.core.utils.StringUtil;
import com.bytter.erp.common.constant.ErpConstant;
import com.bytter.erp.common.constant.ServiceCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RegionQueryStrategy
 * @Author zx
 * @Description 省市查询接口
 * @Date 2026/4/28 14:42
 */
@Component
public class RegionQueryStrategy implements BizStrategy {

    @Autowired
    private RegionQueryService regionQueryService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.ERP_REGION_QUERY.getServiceCode();
    }

    @Override
    public Map<String, String> doBiz(Map<String, String> params) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>(8);
        resultMap.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_SUCCESS_STATUS);

        // 请求原文
        String data = params.get(ErpConstant.DO_BIZ_DATA);
        String tenantId = params.get(ErpConstant.DO_BIZ_TENANT_ID);

        // 请求报文转为对象
        RegionRequestVO reqVo = JSON.parseObject(data, RegionRequestVO.class);
        String checkResult = checkData(reqVo);
        if (ObjectUtil.isNotEmpty(checkResult)) {
            resultMap.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            resultMap.put(ErpConstant.DO_BIZ_MSG, checkResult);
            return resultMap;
        }

        // 查询省市信息
        List<ErpRegionQueryView> list = regionQueryService.queryRegionList(reqVo, tenantId);
        if (ObjectUtil.isNotEmpty(list)) {
            resultMap.put(ErpConstant.DO_BIZ_MSG, "查询成功");
        } else {
            resultMap.put(ErpConstant.DO_BIZ_MSG, "查无数据");
        }
        resultMap.put(ErpConstant.DO_BIZ_DATA, JSON.toJSONString(list, SerializerFeature.WriteMapNullValue));
        return resultMap;
    }

    /**
     * 参数校验
     *
     * @param reqVo
     * @return
     */
    private String checkData(RegionRequestVO reqVo) {
        StringBuilder result = new StringBuilder(128);

        // 先判断报文是否正确
        if (ObjectUtil.isEmpty(reqVo)) {
            appendMsg(result, "请求报文格式不正确，解析失败！");
            return result.toString();
        }

        // 过滤条件不能全部为空
        if (isAllQueryConditionBlank(reqVo)) {
            appendMsg(result, "代码和名称不能同时为空;");
        }

        return result.toString();
    }

    /**
     * 判断查询条件是否全部为空。
     *
     * @param reqVo 请求参数
     * @return 全空返回true
     */
    private boolean isAllQueryConditionBlank(RegionRequestVO reqVo) {
        return StringUtil.isBlank_new(reqVo.getCityCode())
                && StringUtil.isBlank_new(reqVo.getCityName())
                && StringUtil.isBlank_new(reqVo.getProvCode())
                && StringUtil.isBlank_new(reqVo.getProvName());
    }
}
