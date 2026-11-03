package com.bytter.system.pricing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class FcInstrateDTO implements Serializable {

    private String id;
    /**
     * 科目代码
     */

    private String subjectCode;
    /**
     * 科目Id
     */
    private String subjectId;
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 利率类型
     */
    private String instrtType;
    /**
     * 原利率
     */
    private BigDecimal oldInstrate;
    /**
     * 利率
     */
    private BigDecimal interestRate;
    /**
     * 执行日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date exeDate;
    /**
     * 期限代码
     */
    private Integer termFlag;
    /**
     * 期限单位
     */

    private String endUnit;
    /**
     * 币别Id
     */
    private String curId;
    /**
     * 币别代码
     */

    private String curCode;
    /**
     * 币别名称
     */

    private String curName;
    /**
     * 状态
     */
    private Integer status;

    /**
     * createTime
     */
    private Date createTime;
    /**
     * createBy
     */
    private String createBy;
    /**
     * updateTime
     */
    private Date updateTime;
    /**
     * updateBy
     */
    private String updateBy;
}
