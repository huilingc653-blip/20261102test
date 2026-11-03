package com.bytter.credit.domain.vm;
import lombok.Data;
import java.io.Serializable;
/**
 * 授信模块公用字段
 */
@Data
public class BaseBkRationVm implements Serializable {
    protected String id;
    /** 授信编号 */
    protected String ratCode;
    /** 网点编号 */
    protected String netCode;
    /** 单位代码 */
    protected String corpId;
    /** 授信种类 */
    protected String ratType;
    /** 银行类型 */
    protected String bankType;
    /** 银行编号 */
    protected String bankCode;
    /**
     * 银行名称
     */
    protected String bankName;
}
