package com.bytter.biz.netbank.vo;

import com.bytter.biz.netbank.vo.NisBillheadPoolVO;
import lombok.Data;

@Data
public class NisBillheadPoolSplitResp extends NisBillheadPoolInitConfigVO {
    private NisBillheadPoolVO nisBillheadPage;
    private String reportBudget;
    private String businessName;
    private String processSysname;
    private String busType;
    private String isBudget;
    private String settlType;
}
