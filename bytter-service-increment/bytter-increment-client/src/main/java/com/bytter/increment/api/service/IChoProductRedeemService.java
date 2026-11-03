package com.bytter.increment.api.service;

import com.bytter.increment.api.dto.ChoProductRedeemDetailDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IChoProductRedeemService {

    /**
     * 查找赎回状态为审批通过的指定赎回产品
     * @param reqId
     * @return
     */
    public List<ChoProductRedeemDetailDTO> listProductRedeemApproved(String reqId);
}
