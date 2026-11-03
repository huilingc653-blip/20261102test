package com.bytter.biz.finacial.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 利息流水规则数据类。
 *
 * @author zhaohc
 * @date 2024/4/30 10:44
 */
@Data
public class FcInterestDtlRuleDTO implements Serializable {

    private String id;

    /**
     * 银行类别
     */
    private String bankTypeId;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 用途/摘要
     */
    private String remark;
    /**
     * 入账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transTime;
    /**
     * 有效标志 1-有效 0-无效
     */
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
