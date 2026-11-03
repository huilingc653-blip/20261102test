package com.bytter.system.service.impl;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IEquityMaintenanceService;
import com.bytter.system.api.dto.EquityMaintenanceDTO;
import com.bytter.system.entity.EquityMaintenance;
import com.bytter.system.service.EquityMaintenanceService;
import com.bytter.system.vo.EquityMaintenanceVo;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@DubboService
@Service("equityMaintenanceServiceImpl")
public class EquityMaintenanceServiceImpl implements IEquityMaintenanceService {

    @Autowired
    EquityMaintenanceService equityMaintenanceService;
    @Override
    public EquityMaintenanceDTO queryById(String id) {
        EquityMaintenanceDTO dto = new EquityMaintenanceDTO();
        List<String> codes = Lists.newArrayList();
        codes.add(id);
        List<EquityMaintenanceDTO> equityMaintenanceDTOS = this.queryByIds(codes);
        if (equityMaintenanceDTOS != null && equityMaintenanceDTOS.size()>0){
            dto = equityMaintenanceDTOS.get(0);
        }
        return dto;
    }

    @Override
    public List<EquityMaintenanceDTO> queryByIds(List<String> ids) {
        List<EquityMaintenanceDTO> dtoList = Lists.newArrayList();
        List<EquityMaintenance> equityMaintenances = equityMaintenanceService.queryByCodes(ids);
        if (!EmptyUtils.isEmpty(equityMaintenances)) {
            dtoList = BeanUtils.copyListProperties(equityMaintenances, EquityMaintenanceDTO.class);
        }
        return dtoList;
    }

    @Override
    public List<EquityMaintenanceDTO> queryAll() {
        List<EquityMaintenanceVo> json = equityMaintenanceService.json();
        if (CollectionUtils.isEmpty(json)){
            return null;
        }
        return BeanUtils.copyListProperties(json, EquityMaintenanceDTO.class);
    }
}
