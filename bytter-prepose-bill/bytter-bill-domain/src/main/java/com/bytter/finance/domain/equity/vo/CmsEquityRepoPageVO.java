package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资回购-回购页面数据
 */
@Data
public class CmsEquityRepoPageVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 融资单位 */
    private String corpId;
    /** 币种 */
    private String curId;
    /** 回购信息 */
    private CmsEquityRepoVO repoVO;
}
