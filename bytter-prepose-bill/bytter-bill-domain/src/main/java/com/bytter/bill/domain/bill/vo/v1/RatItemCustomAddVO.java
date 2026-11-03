package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.credit.domain.entity.RatItemCustom;
import com.bytter.credit.dto.RatItemTypeDTO;
import com.bytter.credit.dto.RatSysItemDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 授信品种新增页面跳转数据
 */
@Data
public class RatItemCustomAddVO implements Serializable {

    private RatItemCustom ratItemCustomPage;
    private List<RatSysItemDTO> sysItemCodes;
    private List<RatItemTypeDTO> ritCodes;

}
