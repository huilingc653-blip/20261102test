package com.bytter.system.api.dto;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class FcInstrateDTO implements Serializable {

    private String id;
    /**
     * 科目代码
     */
    @BtCompositeSearch(fieldName = "fc.subject_code")
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
    @BtCompositeSearch(fieldName = "fc.instrt_type")
    private String instrtType;
    /**
     * 原利率
     */
    @BtCompositeSearch(fieldName = "fc.old_instrate")
    private java.math.BigDecimal oldInstrate;
    /**
     * 利率
     */
    @BtCompositeSearch(fieldName = "fc.interest_rate")
    private java.math.BigDecimal interestRate;
    /**
     * 执行日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "fc.exe_date")
    private java.util.Date exeDate;
    /**
     * 期限单位
     */
    @BtCompositeSearch(fieldName = "fc.end_unit")
    private String endUnit;
    /**
     * 币别Id
     */
    private String curId;
    /**
     * 币别代码
     */
    @BtCompositeSearch(fieldName = "fc.cur_code")
    private String curCode;
    /**
     * 币别名称
     */
//	@BtCompositeSearch(fieldName="bt.cur_name")
    private String curName;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "fc.status")
    private Integer status;

    private String isShow;
}
