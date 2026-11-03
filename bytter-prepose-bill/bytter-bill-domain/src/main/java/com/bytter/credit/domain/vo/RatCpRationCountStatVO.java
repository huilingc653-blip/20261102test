package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.UrationParamDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 内部授信额度统计报表数据
 */
@Data
public class RatCpRationCountStatVO implements Serializable {
    private List<RatUrationDisBean> listCpRat;
    private String corpId;
    private UrationParamDTO paramDTO;
    private String naturalCur;
    private String userName;
    private Integer moneyType;
    private String reportDate;
    private BigDecimal totalMoney;
    private BigDecimal totalUseMoney;
    private BigDecimal totalApproveMoney;
    private BigDecimal totalLeftMoney;
    private BigDecimal totalOverPlusMoney;
    private String curName;
}
