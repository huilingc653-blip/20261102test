package com.bytter.credit.domain.vm;
import com.bytter.core.annotation.Dict;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
    /**
 * 授信详情通用字段类
 */
@Data
public class BaseBkRationDtlVm implements Serializable {
    protected String id;
    /**
     * 授信主键
     */
    protected String ratCode;
    /**
     * 品种标识
     */
    protected String itemFlag;
    /**
     * 授信品种排序
     */
    protected Integer itemOrder;
    /**
     * 授信品种
     */
    @Dict(tableName = "RAT_ITEM_CUSTOM", valueFieldName = "id", showFiledName = "NAME", inject = "itemName")
    protected String itemCode;
    /**
     * 授信品种名称
     */
    protected String itemName;
    /**
     * 授信金额
     */
    protected BigDecimal money;
    /**
     * 敞口金额
     */
    protected BigDecimal openMoney;
    /**
     * 综合额度使用标识
     */
    private String compSign;
}
