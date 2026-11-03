package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.NisCorpBusDTO;
public interface INisCorpBusService {
    NisCorpBusDTO getBusTypeByName(String corpId, String cdSign, String busTypeName);
}
