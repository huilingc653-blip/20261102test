package com.bytter.check.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BisAccDtlBudgetRuleSplitDTO implements Serializable {

    private String id;

    private List<BigDecimal> amts;
}
