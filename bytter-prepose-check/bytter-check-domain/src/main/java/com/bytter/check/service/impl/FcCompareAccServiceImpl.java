package com.bytter.check.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.check.api.IFcCompareAccService;
import com.bytter.check.dto.FcCompareAccDTO;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.service.FcCompareAccService;
import com.bytter.core.utils.BeanUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luoyj
 * @date 2023/8/21.
 */
@Service("fcCompareAccServiceImpl")
@DubboService
@Transactional
public class FcCompareAccServiceImpl implements IFcCompareAccService {

    @Autowired
    private FcCompareAccService fcCompareAccService;

    @Override
    public void deleteFcCompareAccByVoucherNoAndCloseDate(List<String> voucherNos, Date closeDate) {
        fcCompareAccService.deleteFcCompareAccByVoucherNoAndCloseDate(voucherNos,closeDate);
    }

    @Override
    public List<FcCompareAccDTO> getBalanceByCondition(List<FcCompareAccDTO> fcCompareAccDTOList) {
        if (CollectionUtils.isEmpty(fcCompareAccDTOList)){
            return Lists.newArrayList();
        }
        return fcCompareAccService.getBalanceByCondition(fcCompareAccDTOList);
    }

    @Override
    public void updateById(List<FcCompareAccDTO> dtos) {
        List<FcCompareAcc> fcCompareAccs = BeanUtils.copyListProperties(dtos, FcCompareAcc.class);
        fcCompareAccService.batchUpdate(fcCompareAccs);
    }
    public List<FcCompareAccDTO> getFcCompareAccDTOBySql(String sql, Map<String, Object> params){
        return fcCompareAccService.getFcCompareAccDTOBySql( sql, params);
    }
    public void updateCompareAccBalance(Map<String, Object> params) {
        fcCompareAccService.updateCompareAccBalance( params);
    }
}
