package com.bytter.finance.domain.equity.vo;

import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 股权融资登记-查看页面数据
 */
@Data
public class CmsEquityInfoViewVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 融资明细ids */
    private String ids;
    /** 股权融资主信息 */
    private CmsEquityInfoVO cmsEquityInfo;
    /** 股权融资明细 */
    private List<CmsEquityInfoDetailVO> detailVOS;
    /** 第三方费率信息 */
    private List<DfIntermediateCostVO> costVOS;
}
