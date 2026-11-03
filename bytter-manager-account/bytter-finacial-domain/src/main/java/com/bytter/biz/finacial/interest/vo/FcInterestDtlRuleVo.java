package com.bytter.biz.finacial.interest.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * 利息流水规则视图类。
 *
 * @author zhaohc
 * @date 2024/4/29 17:52
 */
@Data
public class FcInterestDtlRuleVo implements Serializable {

    private String id;

    /**
     * 银行类别
     */
    @BtCompositeSearch(fieldName = "fidr.bank_type_id")
    private String bankTypeId;
    /**
     * 银行名称
     */
    @BtCompositeSearch(fieldName = "fidr.bank_name")
    private String bankName;
    /**
     * 用途/摘要
     */
    @BtCompositeSearch(fieldName = "fidr.remark")
    private String remark;
    /**
     * 入账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transTime;
    /**
     * 有效标志 1-有效 0-无效
     */
    @BtCompositeSearch(fieldName = "fidr.valid_sign")
    private String validSign;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;

}
