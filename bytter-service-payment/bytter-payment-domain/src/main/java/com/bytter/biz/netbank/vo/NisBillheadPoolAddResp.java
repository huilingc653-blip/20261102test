package com.bytter.biz.netbank.vo;

import com.bytter.biz.netbank.entity.NisBillheadPool;
import lombok.Data;

@Data
public class NisBillheadPoolAddResp extends NisBillheadPoolInitConfigVO {
    private NisBillheadPool nisBillheadPage;
    private String wishPayday;
    private String requiredBusiness;
}
