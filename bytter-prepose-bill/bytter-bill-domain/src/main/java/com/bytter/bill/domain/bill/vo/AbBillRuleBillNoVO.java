package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class AbBillRuleBillNoVO extends AbAccrecordsVO implements Serializable {
    /**
     * 票号规则表ID
     */
    private String id;
    /**
     * 银行类型ID
     */
    @BtCompositeSearch(fieldName="abr.bank_type_id")
    private String bankTypeId;
    /**
     * 银行类型
     */
    @BtCompositeSearch(fieldName="abr.bank_type_id")
    private String bankTypeName;
    /**
     * 业务类型
     */
    // @Dict(dictGroup = "accebill_billNo_insbriefid", inject =  "busTypeName")
    private String busType;
    @BtCompositeSearch(fieldName = "abr.busType")
    private String busTypeName;
    /**票号字段*/
    // @Dict(dictGroup = "accebill_billNo_billNumField", inject = "billNumFieldName")
    private String billNumField;
    @BtCompositeSearch(fieldName = "bill_number_field")
    private String billNumFieldName;
    /**
     * 匹配关系
     */
    // @Dict(dictGroup = "accebill_billNo_matchRelation", inject = "relationName")
    private String relation;
    @BtCompositeSearch(fieldName = "relation_matching")
    private String relationName;
    /**
     * 票号分隔符
     */
    @BtCompositeSearch(fieldName="abr.billno_separated")
    private String separated;
    /**有效标志*/
    @BtCompositeSearch(fieldName="abr.valid_mark")
    // @Dict(dictGroup = "valid_sign", inject = "markName")
    private String mark;
    @BtCompositeSearch(fieldName="abr.valid_mark")
    private String markName;
    /**是否汇总认领流水 0-否 1-是*/
    private String isSummaryClaimBill;
}
