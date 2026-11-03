package com.bytter.biz.budget.spi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaveCtlBudgetDTO implements Serializable {
    /**
     * 报告单位
     */
    private  List<String> reportCorpIds;
    /**
     *控制单位
     */
    private List<String> controlCorpIds;
}
