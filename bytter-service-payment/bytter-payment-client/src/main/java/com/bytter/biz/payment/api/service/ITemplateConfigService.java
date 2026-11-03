package com.bytter.biz.payment.api.service;

import com.bytter.biz.payment.api.dto.FexTranTemplateConfigDetailDTO;

import java.util.List;

public interface ITemplateConfigService {

    /**
     * 根据银行类型ID和支付类型is查询配置模板
     *
     * @param
     * @return
     */
    List<FexTranTemplateConfigDetailDTO> listOfChildrenByBankPayIds(List<String> bankPayIds, List<String> bankTypeId);
}
