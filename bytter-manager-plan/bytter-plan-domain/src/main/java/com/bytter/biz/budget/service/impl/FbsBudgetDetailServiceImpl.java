package com.bytter.biz.budget.service.impl;

import com.bytter.biz.budget.service.FbsBudgetDetailService;
import com.bytter.biz.budget.spi.dto.FbsBudgetDetailDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetDetailService;
import com.bytter.biz.budget.vo.FbsBudgetDetailVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fbsBudgetDetailServiceImpl")
@DubboService
public class FbsBudgetDetailServiceImpl implements IFbsBudgetDetailService {

    @Autowired
    private FbsBudgetDetailService fbsBudgetDetailService;
    @Override
    public List<FbsBudgetDetailDTO> getProjectDetailInfo(DataTablesInput input) {
        List<FbsBudgetDetailVO> data = fbsBudgetDetailService.getProjectDetailInfo(input).getData();
        List<FbsBudgetDetailDTO> detailDTOList = BeanUtils.copyListProperties(data, FbsBudgetDetailDTO.class);
        return detailDTOList;
    }
}
