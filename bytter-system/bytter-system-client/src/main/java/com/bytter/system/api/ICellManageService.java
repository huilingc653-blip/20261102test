package com.bytter.system.api;


import com.bytter.system.api.dto.CellManageDto;

public interface ICellManageService {

    public CellManageDto getCellManageByCodeOrName1(String code, String name);
}
