package com.bytter.biz.finacial.interest.entity;

import com.bytter.core.base.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 利息流水规则表。
 *
 * @author zhaohc
 * @date 2024/4/29 16:02
 */
@Entity
@Table(name = "fc_interest_dtl_rule")
@Data
public class FcInterestDtlRule implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.bytter.core.common.hibernate.id.UUIDGenerator")
    @Column(name = "ID", unique = true, length = 32)
    private String id;

    /**
     * 银行类别
     */
    @Column(name = "bank_type_id")
    private String bankTypeId;
    /**
     * 银行名称
     */
    @Column(name = "bank_name")
    private String bankName;
    /**
     * 用途/摘要
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 入账日期
     */
    @Column(name = "trans_time")
    private Date transTime;
    /**
     * 有效标志 1-有效 0-无效
     */
    @Column(name = "valid_sign")
    private String validSign;
    /**
     * 创建人登录名称
     */
    @Column(name = "create_by")
    private String createBy;
    /**
     * 创建日期
     */
    @Column(name = "create_date")
    private Date createDate;
    /**
     * 更新人登录名称
     */
    @Column(name = "update_by")
    private String updateBy;
    /**
     * 更新日期
     */
    @Column(name = "update_date")
    private Date updateDate;


}
