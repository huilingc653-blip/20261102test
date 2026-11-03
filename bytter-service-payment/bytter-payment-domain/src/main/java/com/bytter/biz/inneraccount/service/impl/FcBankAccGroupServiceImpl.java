package com.bytter.biz.inneraccount.service.impl;

import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.payment.api.dto.FcBankAccGroupDTO;
import com.bytter.biz.payment.api.service.IFcBankAccGroupService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
@Transactional
@DubboService
public class FcBankAccGroupServiceImpl implements IFcBankAccGroupService {
    @Autowired
    private FcBankAccGroupService fcBankAccGroupService;


    @Override
    public List<FcBankAccGroupDTO> getValidPayType() {
        List<FcBankAccGroup> list = fcBankAccGroupService.getValidPayType();
        List<FcBankAccGroupDTO> fcBankAccGroupDTOList = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(list)){
            fcBankAccGroupDTOList = BeanUtils.copyListProperties(list,FcBankAccGroupDTO.class);
        }
        return fcBankAccGroupDTOList;
    }
}
