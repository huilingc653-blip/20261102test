package com.bytter.credit.domain.entity;

import com.bytter.core.base.entity.IdEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行授信年度计划表
 *
 * @author huao
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Access(AccessType.FIELD)
@Table(name = "year_bank_credit_plan")
public class YearBankCreditPlan extends IdEntity implements Serializable {

    /**
     * 单位id
     */
    @Column(name = "corp_id")
    private String corpId;

    /**
     * 年度
     */
    @Column(name = "year")
    private Integer year;

    /**
     * 授信类型
     */
    @Column(name = "credit_type")
    private String creditType;

    /**
     * 币别id
     */
    @Column(name = "cur_id")
    private String curId;

    /**
     * 计划授信金额
     */
    @Column(name = "credit_amount")
    private BigDecimal creditAmount;

    /**
     * 银行id
     */
    @Column(name = "bank_id")
    private String bankId;

    /**
     * 银行编码
     */
    @Column(name = "bank_code")
    private String bankCode;

    /**
     * 银行名称
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 是否正常使用 0-否 1-是
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建人登录名称
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建人名称
     */
    @Column(name = "create_name")
    private String createName;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新人登录名称
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 更新人名称
     */
    @Column(name = "update_name")
    private String updateName;

    /**
     * 更新日期
     */
    @Column(name = "update_time")
    private Date updateTime;

}
