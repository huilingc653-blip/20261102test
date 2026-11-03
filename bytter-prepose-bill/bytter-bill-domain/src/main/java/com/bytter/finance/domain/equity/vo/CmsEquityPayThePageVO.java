package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资缴付到账-缴付页面数据
 */
@Data
public class CmsEquityPayThePageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 融资单位 */
    private String corpId;
    /** 币种 */
    private String curId;
    /** 缴付到账信息 */
    private CmsEquityPayTheVO payTheVO;
}
