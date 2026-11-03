package com.bytter.accountReceivable.businessConfig.service.impl;

import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("baseConfigService")
@Transactional
public class BaseConfigService {


    @DubboReference(check = false)
    private IFbsItemService fbsItemService;

    /**
     * 科目编号与名称拼接
     */
    public String getDefaultBudgetSelStr(String defaultBudgetSel,String tenantId) {
        String defaultBudgetSelStr = "";
        if (EmptyUtils.isEmpty(defaultBudgetSel)) {
            return defaultBudgetSelStr;
        }
        String[] itemCodes = defaultBudgetSel.split(";");
        for (String itemCode : itemCodes) {
            FbsItemDTO itemDTO = fbsItemService.getFbsItemByCode(itemCode);
            if (!EmptyUtils.isEmpty(itemDTO) && !EmptyUtils.isEmpty(itemDTO.getItemCode())) {
                defaultBudgetSelStr += itemDTO.getItemCode()+"("+itemDTO.getItemName()+")"+";";
            }
        }
        if (defaultBudgetSelStr.endsWith(";")) {
            defaultBudgetSelStr = defaultBudgetSelStr.substring(0,defaultBudgetSelStr.length()-1);
        }
        return defaultBudgetSelStr;
    }

}
