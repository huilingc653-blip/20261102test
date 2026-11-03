package com.bytter.finance.domain.equity.dto;

import com.bytter.finance.domain.equity.vo.CmsEquityInfoVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资登记-提交/暂存请求
 */
@Data
public class CmsEquityInfoSubmitDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 股权融资主信息 */
    private CmsEquityInfoVO vo;
    /** 保存类型：1提交 0暂存 */
    private String saveType;
    /** 融资明细ids */
    private String ids;
}
