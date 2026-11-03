package com.bytter.biz.payment.api.service;

import java.util.Map;


import com.bytter.biz.payment.api.dto.NisBillDTO;

import java.util.List;

public interface INisBillService {
    /**
     * 根据票据流水id获取对应的挂接付款金额
     *
     * @param ids
     * @return
     */
    String getBillBalances(String ids);
    String getBillBalances(String ids,String payId);

    String getBillIds(String payId);

    Map<String,String> getBillIdsByPayIds(String payIds);

    void saveOrUpdateSync(String payId, String billIds);

    /**
     * 对付款占用票据进行批量写入
     * 会根据付款ID先删除再批量重新写入
     * @param nisBillDTOList
     */
    void batchNisBill(List<NisBillDTO> nisBillDTOList);
}
