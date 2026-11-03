package com.bytter.bill.api.bill.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行开票授信资料维护表DTO对象
 *
 * @author fangcf
 * @date 2025-06-26
 */
@Data
public class AbBillCreditInfoMaintainDTO implements Serializable {

    /**
     * 主键
     */
    private String id;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 银行类型
     */
    private String bankTypeId;
    /**
     * 银行类型名称
     */
    private String bankTypeName;
    /**
     * 授信类型：敞口和低风险
     */
    private String creditType;
    /**
     * 授信类型名称
     */
    private String creditTypeName;
    /**
     * 保证金比例
     */
    private BigDecimal bondRatio;
    /**
     * 开票手续费
     */
    private BigDecimal openBillRate;
    /**
     * 保证金利率
     */
    private String bondRate;
    /**
     * 是否能正常使用：1-是，0-否
     */
    private String isUsed;
    /**
     * 无
     */
    private String isDeleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
}
