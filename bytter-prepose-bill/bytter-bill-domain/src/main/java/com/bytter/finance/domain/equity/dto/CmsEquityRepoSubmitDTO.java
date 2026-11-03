package com.bytter.finance.domain.equity.dto;

import com.bytter.finance.domain.equity.vo.CmsEquityRepoVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资回购-提交/暂存请求
 */
@Data
public class CmsEquityRepoSubmitDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 回购信息 */
    private CmsEquityRepoVO vo;
    /** 保存类型：1提交 0暂存 */
    private String saveType;
}
