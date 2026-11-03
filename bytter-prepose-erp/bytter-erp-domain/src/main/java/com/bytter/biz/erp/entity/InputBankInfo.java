package com.bytter.biz.erp.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 联行号对象 BT_INPUT_BANK_INFO
 *
 * @author kuangy 2021-03-15
 * @since 1.0.0
 */
@Data
public class InputBankInfo implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 联行号
     */
    private String sysBankCode;

    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 联行号前缀
     */
    private String bankPrefix;

    /**
     * 地区码
     */
    private String bankCityCode;

    /**
     * 数据来源
     */
    private String ibpsCode;

    /**
     * SYS_REGION省ID
     */
    private String prov;

    /**
     * SYS_REGION市ID
     */
    private String city;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新日期
     */
    private Date updateDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * SWIFT 代码
     */
    private String swiftCode;

}
