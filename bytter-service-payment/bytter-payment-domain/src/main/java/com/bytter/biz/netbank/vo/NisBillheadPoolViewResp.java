package com.bytter.biz.netbank.vo;

import com.bytter.biz.netbank.vo.NisBillheadPoolVO;
import lombok.Data;

import java.util.List;

@Data
public class NisBillheadPoolViewResp extends NisBillheadPoolInitConfigVO {
    private NisBillheadPoolVO nisBillheadPage;
    private String isSplitOrMerge;
    private List<NisBillheadPoolVO> listMerge;
    private String businessName;
    private String processSysname;
    private String isBudget;
    private String busType;
}
