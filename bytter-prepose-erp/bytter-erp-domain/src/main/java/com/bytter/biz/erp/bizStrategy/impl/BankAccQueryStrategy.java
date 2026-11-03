package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.service.BankAccQueryService;
import com.bytter.biz.erp.entity.ErpBankAccQueryView;
import com.bytter.biz.erp.vo.request.BankAccQueryRequestVO;
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
 * @ClassName BankAccQueryStrategy
 * @Author zx
 * @Description 银行账户查询接口
 * @Date 2026/4/28 14:42
 */
@Component
public class BankAccQueryStrategy implements BizStrategy {

    @Autowired
    private BankAccQueryService bankAccQueryService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.ERP_BANK_ACC_QUERY.getServiceCode();
    }

    @Override
    public Map<String, String> doBiz(Map<String, String> params) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>(8);
        resultMap.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_SUCCESS_STATUS);

        // 请求原文
        String data = params.get(ErpConstant.DO_BIZ_DATA);
        String tenantId = params.get(ErpConstant.DO_BIZ_TENANT_ID);

        // 请求报文转为对象
        BankAccQueryRequestVO reqVo = JSON.parseObject(data, BankAccQueryRequestVO.class);
        String checkResult = checkData(reqVo);
        if (ObjectUtil.isNotEmpty(checkResult)) {
            resultMap.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            resultMap.put(ErpConstant.DO_BIZ_MSG, checkResult);
            return resultMap;
        }

        // 查询银行账户信息
        List<ErpBankAccQueryView> list = bankAccQueryService.queryBankAccList(reqVo, tenantId);
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
    private String checkData(BankAccQueryRequestVO reqVo) {
        StringBuilder result = new StringBuilder(128);

        // 先判断报文是否正确
        if (ObjectUtil.isEmpty(reqVo)) {
            appendMsg(result, "请求报文格式不正确，解析失败！");
            return result.toString();
        }

        // 过滤条件不能全部为空
        if (isAllQueryConditionBlank(reqVo)) {
            appendMsg(result, "账号、账号名称、单位代码、单位名称和修改日期不能同时为空;");
        }

        if (!StringUtil.isBlank_new(reqVo.getUpdateDate())) {
            try {
                DateUtil.getDateByString(reqVo.getUpdateDate(), "yyyy-MM-dd");
            } catch (Exception e) {
                appendMsg(result, "日期格式错误，应为yyyy-MM-dd");
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
    private boolean isAllQueryConditionBlank(BankAccQueryRequestVO reqVo) {
        return StringUtil.isBlank_new(reqVo.getBankAcc())
                && StringUtil.isBlank_new(reqVo.getAccName())
                && StringUtil.isBlank_new(reqVo.getCorpCode())
                && StringUtil.isBlank_new(reqVo.getCorpName())
                && StringUtil.isBlank_new(reqVo.getUpdateDate());
    }
}
