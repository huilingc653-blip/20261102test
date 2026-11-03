package com.bytter.system.service.impl;

import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICreditPeriodService;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import com.bytter.system.entity.TbCreditPeriod;
import com.bytter.system.service.CreditPeriodService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@DubboService
@Service("creditPeriodServiceI")
@Transactional
public class CreditPeriodServiceImpl implements ICreditPeriodService {

    @Autowired
    private CreditPeriodService creditPeriodService;

    @Override
    public TbCreditPeriodDTO getById(String id) {
        TbCreditPeriod tbCreditPeriod = creditPeriodService.get(id);
        if (EmptyUtils.isEmpty(tbCreditPeriod)) {
            return null;
        }
        TbCreditPeriodDTO tbCreditPeriodDTO = new TbCreditPeriodDTO();
        BeanUtils.copyNotNullProperties(tbCreditPeriod,tbCreditPeriodDTO);
        return tbCreditPeriodDTO;
    }

    @Override
    public Map<String, TbCreditPeriodDTO> listByIds(List<String> ids) {
        List<TbCreditPeriod> tbCreditPeriods = creditPeriodService.getEntityDao().list(ids);
        return tbCreditPeriods.stream().map(item -> {
            TbCreditPeriodDTO tbCreditPeriodDTO = new TbCreditPeriodDTO();
            BeanUtils.copyProperties(item, tbCreditPeriodDTO);
            return tbCreditPeriodDTO;
        }).collect(Collectors.toMap(TbCreditPeriodDTO::getId, Function.identity()));
    }
}
