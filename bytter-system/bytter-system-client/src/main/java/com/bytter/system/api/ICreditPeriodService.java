package com.bytter.system.api;

import com.bytter.system.api.dto.TbCreditPeriodDTO;

import java.util.List;
import java.util.Map;

public interface ICreditPeriodService {
    TbCreditPeriodDTO getById(String id);

    Map<String,TbCreditPeriodDTO> listByIds(List<String> ids);
}
