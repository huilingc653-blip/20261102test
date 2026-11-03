package com.bytter.credit.domain.vm;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
/**
 * 授信追加DTO
 * 
 * @author liyh
 *
 */
@Data
public class RatBkRationAddVm extends BaseBkRationVm {
    /** 标识追加类型 */
    private String flag;
    /** 授信金额 合计 */
    private BigDecimal moneysum;
    /** 敞口金额 合计 */
    private BigDecimal openMoneysum;
    /** 备注 */
    private String remark;
    /** 状态 */
    private Integer status;
    /** 额度类型 */
    private String type;
    /**
     * 授信分配明细数据
     */
    private List<RatBkRationAddDtlVm> rationDtls;
    /**
     * 单品种条数
     */
    private Integer singItemSize;
    /**
     * 综合品种条数
     */
    private Integer compItemSize;
    /**
     * 总品种数
     */
    private Integer itemSize;
    /**
     * 更新授信标识
     */
    private boolean updateSign = false;
    /**
     * 币别
     */
    private String curCode;
    /**
     * 保证金追加金额
     */
    private BigDecimal bondAddMoney;
    /** 附件ID */
    private String attachmentId;
}
