package com.bytter.report.domain.cms.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 银行授信余额表表单对象
 *
 * @author fangcf
 * @date 2025-07-01
 */
@Data
public class BankCreditBalanceForm implements Serializable {

    /**
     * 单位id
     */
    private String corpId;
    /**
     * 周开始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date weekStartDay;
    /**
     * 周结束日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date weekEndDay;
    /**
     * 查询标识
     */
    private String queryFlag;
}
