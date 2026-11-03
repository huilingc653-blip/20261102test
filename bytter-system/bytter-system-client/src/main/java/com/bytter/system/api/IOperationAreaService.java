package com.bytter.system.api;

import com.bytter.system.api.dto.OperationAreaDTO;

import java.util.List;

public interface IOperationAreaService {
    OperationAreaDTO queryById(String id);

    List<OperationAreaDTO> queryByIds(List<String> ids);

    List<OperationAreaDTO> queryByCode(String code);

    public List<OperationAreaDTO> getOperationArea();
}
