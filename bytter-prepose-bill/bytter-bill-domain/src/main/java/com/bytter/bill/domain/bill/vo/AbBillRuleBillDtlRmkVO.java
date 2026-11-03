package com.bytter.bill.domain.bill.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Data
public class AbBillRuleBillDtlRmkVO implements Serializable {
    /**
     * 票号规则表ID
     */
    private String id;
    /**
     * 银行类型ID
     */
    @BtCompositeSearch(fieldName = "bank_type_id")
    private String bankTypeId;
    /**
     * 银行类型
     */
    private String bankTypeName;
    /**
     * 业务类型
     */
    @BtCompositeSearch(fieldName = "bustype")
    private String bustype;
    /**
     * 业务类型名称
     */
    private String bustypeName;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 匹配规则明细列表（替代原先 matchRule/matchElements 等 request 参数）
     */
    private List<AbBillRuleBillDtlRmkDtlVO> ruleDtl;
}
