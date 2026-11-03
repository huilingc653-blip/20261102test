package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.entity.ErpCurQueryView;
import com.bytter.biz.erp.service.CurQueryService;
import com.bytter.biz.erp.vo.request.CurQueryRequestVO;
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.StringUtil;
import com.bytter.erp.common.constant.ErpConstant;
import com.bytter.erp.common.constant.ServiceCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 币别信息查询业务策略。
 *
 * @author zx
 * @date 2026/4/29
 */
@Component
public class CurQueryStrategy implements BizStrategy {

    @Autowired
    private CurQueryService curQueryService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.ERP_CUR_QUERY.getServiceCode();
    }

    @Override
    public Map<String, String> doBiz(Map<String, String> params) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>(8);
        resultMap.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_SUCCESS_STATUS);

        // 请求原文
        String data = params.get(ErpConstant.DO_BIZ_DATA);
        String tenantId = params.get(ErpConstant.DO_BIZ_TENANT_ID);

        // 请求报文转为对象
        CurQueryRequestVO reqVo = JSON.parseObject(data, CurQueryRequestVO.class);
        String checkResult = checkData(reqVo);
        if (ObjectUtil.isNotEmpty(checkResult)) {
            resultMap.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            resultMap.put(ErpConstant.DO_BIZ_MSG, checkResult);
            return resultMap;
        }

        // 查询币别信息
        List<ErpCurQueryView> list = curQueryService.queryCurList(reqVo, tenantId);
        if (ObjectUtil.isNotEmpty(list)) {
            resultMap.put(ErpConstant.DO_BIZ_MSG, "查询成功");
        } else {
            resultMap.put(ErpConstant.DO_BIZ_MSG, "查无数据");
        }
        resultMap.put(ErpConstant.DO_BIZ_DATA, JSON.toJSONString(list, SerializerFeature.WriteMapNullValue));
        return resultMap;
    }

    /**
     * 校验请求参数。
     *
     * @param reqVo 请求参数
     * @return 校验失败信息，校验通过返回空串
     */
    private String checkData(CurQueryRequestVO reqVo) {
        StringBuilder result = new StringBuilder(128);

        if (ObjectUtil.isEmpty(reqVo)) {
            appendMsg(result, "请求报文格式不正确，解析失败！");
            return result.toString();
        }

        if (!StringUtil.isBlank_new(reqVo.getUpdateDate())) {
            try {
                DateUtil.getDateByString(reqVo.getUpdateDate(), "yyyy-MM-dd");
            } catch (Exception e) {
                appendMsg(result, "日期格式错误，应为" + "yyyy-MM-dd");
            }
        }
        return result.toString();
    }

}
