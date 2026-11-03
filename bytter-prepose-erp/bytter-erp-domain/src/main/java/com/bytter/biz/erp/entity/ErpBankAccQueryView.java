package com.bytter.biz.erp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 银行账户查询视图对象。
 *
 * @author zx
 * @date 2026/4/28
 */
@Data
@Entity
@Table(name = "ERP_BANK_ACC_QUERY_VIEW", schema = "")
@SuppressWarnings("serial")
public class ErpBankAccQueryView implements Serializable {

    @Id
    @JSONField(serialize = false)
    private String id;

    /** 租户ID。 */
    @JSONField(serialize = false)
    private String tenantId;

    /** 账户名称。 */
    @JSONField(name = "accName")
    private String accName;

    /** 银行账号。 */
    @JSONField(name = "bankAcc")
    private String bankAcc;

    /** 开户行联行号。 */
    @JSONField(name = "bankCode")
    private String bankCode;

    /** 开户行名称。 */
    @JSONField(name = "bankName")
    private String bankName;

    /** 单位代码。 */
    @JSONField(name = "corpCode")
    private String corpCode;

    /** 单位名称。 */
    @JSONField(name = "corpName")
    private String corpName;

    /** 币别代码。 */
    @JSONField(name = "curCode")
    private String curCode;

    /** 币别名称。 */
    @JSONField(name = "curName")
    private String curName;

    /** 是否在线（0线下，1线上）。 */
    @JSONField(name = "isOnline")
    private String isOnline;

    /** 有效标志（0无效，1有效）。 */
    @JSONField(name = "validSign")
    private String validSign;

    /** 更新日期。 */
    @JSONField(name = "updateDate")
    private String updateDate;
}
