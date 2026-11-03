package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.entity.ErpCorpQueryView;
import com.bytter.biz.erp.service.CorpQueryService;
import com.bytter.biz.erp.vo.request.CorpQueryRequestVO;
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
 * 单位信息查询业务策略。
 *
 * @author zx
 * @date 2026/4/29
 */
@Component
public class CorpQueryStrategy implements BizStrategy {

    @Autowired
    private CorpQueryService corpQueryService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.ERP_CORP_QUERY.getServiceCode();
    }

    @Override
    public Map<String, String> doBiz(Map<String, String> params) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>(8);
        resultMap.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_SUCCESS_STATUS);

        // 请求原文
        String data = params.get(ErpConstant.DO_BIZ_DATA);
        String tenantId = params.get(ErpConstant.DO_BIZ_TENANT_ID);

        // 请求报文转为对象
        CorpQueryRequestVO reqVo = JSON.parseObject(data, CorpQueryRequestVO.class);
        String checkResult = checkData(reqVo);
        if (ObjectUtil.isNotEmpty(checkResult)) {
            resultMap.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            resultMap.put(ErpConstant.DO_BIZ_MSG, checkResult);
            return resultMap;
        }

        // 查询单位信息
        List<ErpCorpQueryView> list = corpQueryService.queryCorpList(reqVo, tenantId);
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
    private String checkData(CorpQueryRequestVO reqVo) {
        StringBuilder result = new StringBuilder(128);

        // 先判断报文是否正确
        if (ObjectUtil.isEmpty(reqVo)) {
            appendMsg(result, "请求报文格式不正确，解析失败！");
            return result.toString();
        }

        // 过滤条件不能全部为空
        if (isAllQueryConditionBlank(reqVo)) {
            appendMsg(result, "单位代码、单位名称和修改日期不能同时为空;");
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

    /**
     * 判断查询条件是否全部为空。
     *
     * @param reqVo 请求参数
     * @return 全空返回true
     */
    private boolean isAllQueryConditionBlank(CorpQueryRequestVO reqVo) {
        return StringUtil.isBlank_new(reqVo.getCode())
                && StringUtil.isBlank_new(reqVo.getName())
                && StringUtil.isBlank_new(reqVo.getUpdateDate());
    }
}
