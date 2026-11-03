package com.bytter.biz.erp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 银行联行号查询视图对象。
 *
 * @author zx
 * @date 2026/4/29
 */
@Data
@Entity
@Table(name = "ERP_BANK_INPUT_QUERY_VIEW", schema = "")
@SuppressWarnings("serial")
public class ErpBankInputQueryView implements Serializable {

    @Id
    @JSONField(serialize = false)
    private String id;

    /** 租户ID。 */
    @JSONField(serialize = false)
    private String tenantId;

    /** 联行号。 */
    @JSONField(name = "bankCode")
    private String bankCode;

    /** 开户行名称。 */
    private String bankName;

    /** 市代码。 */
    private String cityCode;

    /** 市名称。 */
    private String cityName;

    /** 省代码。 */
    private String provCode;

    /** 省名称。 */
    private String provName;

    /** 更新日期（yyyy-MM-dd）。 */
    private String updateDate;
}
