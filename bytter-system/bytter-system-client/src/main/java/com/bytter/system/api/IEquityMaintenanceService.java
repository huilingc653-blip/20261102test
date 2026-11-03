package com.bytter.system.api;

import com.bytter.system.api.dto.EquityMaintenanceDTO;

import java.util.List;

public interface IEquityMaintenanceService {

    EquityMaintenanceDTO queryById(String id);

    List<EquityMaintenanceDTO> queryByIds(List<String> ids);

    List<EquityMaintenanceDTO> queryAll();

}
