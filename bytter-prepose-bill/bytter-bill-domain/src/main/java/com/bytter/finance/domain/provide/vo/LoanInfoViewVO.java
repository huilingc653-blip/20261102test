package com.bytter.finance.domain.provide.vo;

import com.bytter.system.api.dto.ConfigDTO;
import lombok.Data;

/**
 * 借款信息查看返回 VO
 * 旧接口：/cms/provide/info/viewLoanInfo/{billCode}
 *
 * @author bytter
 */
@Data
public class LoanInfoViewVO {

    /**
     * 借款信息
     */
    private LoanInfoVO loanInfo;

    /**
     * 是否启用担保（配置）
     */
    private ConfigDTO gua;
}
