package com.bytter.increment.domain.service.impl;

import com.bytter.core.utils.BeanUtils;
import com.bytter.increment.api.dto.ChoProductRedeemDetailDTO;
import com.bytter.increment.api.service.IChoProductRedeemService;
import com.bytter.increment.domain.dao.ChoProductRedeemDao;
import com.bytter.increment.domain.entity.ChoProductRedeemDetail;
import com.bytter.system.api.dto.ExternalCorpDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("choProductRedeemServiceImpl")
@Transactional
@DubboService
public class ChoProductRedeemServiceImpl implements IChoProductRedeemService {

    @Autowired
    private ChoProductRedeemDao choProductRedeemDao;

    @Override
    public List<ChoProductRedeemDetailDTO> listProductRedeemApproved(String reqId) {
        List<ChoProductRedeemDetail> choProductRedeemDetails = choProductRedeemDao.listProductRedeemApproved(reqId);
        return BeanUtils.copyListProperties(choProductRedeemDetails, ChoProductRedeemDetailDTO.class);
    }
}
