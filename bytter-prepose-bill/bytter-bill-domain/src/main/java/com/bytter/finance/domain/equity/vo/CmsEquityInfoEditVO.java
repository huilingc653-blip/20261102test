package com.bytter.finance.domain.equity.vo;

import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.equity.entity.CmsEquityInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 股权融资登记-修改页面数据
 */
@Data
public class CmsEquityInfoEditVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 融资明细ids */
    private String ids;
    /** 股权融资主信息 */
    private CmsEquityInfo cmsEquityInfo;
    /** 第三方费率信息 */
    private List<DfIntermediateCostVO> costVOS;
}
