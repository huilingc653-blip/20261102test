package com.bytter.credit.domain.vo;

import com.bytter.credit.domain.entity.RatItemCustom;
import com.bytter.credit.dto.RatItemTypeDTO;
import com.bytter.credit.dto.RatSysItemDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 授信品种 新增/编辑页面数据(共用)
 */
@Data
public class RatItemCustomFormVO implements Serializable {
    private RatItemCustom ratItemCustomPage;
    private List<RatSysItemDTO> sysItemCodes;
    private List<RatItemTypeDTO> ritCodes;
}
