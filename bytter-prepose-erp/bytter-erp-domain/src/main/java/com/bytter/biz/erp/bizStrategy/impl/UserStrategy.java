package com.bytter.biz.erp.bizStrategy.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.erp.bizStrategy.BizStrategy;
import com.bytter.biz.erp.entity.ErpUserInfo;
import com.bytter.biz.erp.service.ErpUserInfoService;
import com.bytter.erp.common.constant.ErpConstant;

import com.bytter.erp.common.constant.ServiceCodeEnum;
import com.bytter.erp.dto.LoginUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: erp 登录用户查询策略
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/2/9
 */
@Component
public class UserStrategy implements BizStrategy {

    @Autowired
    private ErpUserInfoService erpUserInfoService;

    @Override
    public String getServiceCode() {
        return ServiceCodeEnum.ERP_USER_QUERY.getServiceCode();
    }

    @Override
    public Map<String, String> doBiz(Map<String, String> params) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_SUCCESS_STATUS);

        // 请求原文
        String data = params.get(ErpConstant.DO_BIZ_DATA);
        String requestNo = params.get(ErpConstant.DO_BIZ_SERIAL_NO);
        String tenantId = params.get(ErpConstant.DO_BIZ_TENANT_ID);

        List<ErpUserInfo> list = erpUserInfoService.queryList(tenantId);
        if (ObjectUtil.isNotEmpty( list)){
            List<LoginUserDTO> resultList = new ArrayList<>();
            for (ErpUserInfo op : list){
                LoginUserDTO loginUserVo = new LoginUserDTO();
                loginUserVo.setUsername(op.getLoginName());
                loginUserVo.setPassword(op.getPassword());
                resultList.add(loginUserVo);
            }
            map.put(ErpConstant.DO_BIZ_DATA, JSONUtil.toJsonStr(resultList));
            map.put(ErpConstant.DO_BIZ_MSG, "查询成功");
        }else{
            map.put(ErpConstant.DO_BIZ_STATUS, ErpConstant.SERVER_FAIL_STATUS);
            map.put(ErpConstant.DO_BIZ_MSG, "查询无数据");
        }

        return map;
    }
}
