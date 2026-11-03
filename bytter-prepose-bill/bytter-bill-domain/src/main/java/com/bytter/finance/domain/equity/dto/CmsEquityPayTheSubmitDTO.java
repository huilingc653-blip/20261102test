package com.bytter.finance.domain.equity.dto;

import com.bytter.finance.domain.equity.vo.CmsEquityPayTheVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资缴付到账-提交/暂存请求
 */
@Data
public class CmsEquityPayTheSubmitDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 缴付到账信息 */
    private CmsEquityPayTheVO vo;
    /** 保存类型：1提交 0暂存 */
    private String saveType;
}
