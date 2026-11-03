package com.bytter.biz.erp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 客商账号信息查询视图实体。
 *
 * @author zx
 * @date 2026/4/29
 */
@Data
@Entity
@Table(name = "ERP_EXTERNAL_CORP_ACC_QUERY_VIEW", schema = "")
@SuppressWarnings("serial")
public class ErpExternalCorpAccQueryView implements Serializable {

    /** 视图行主键（内部使用，默认不参与对外 JSON 输出）。 */
    @Id
    @JSONField(serialize = false)
    private String id;

    /** 租户 ID（内部使用，默认不参与对外 JSON 输出）。 */
    @JSONField(serialize = false)
    private String tenantId;

    /** 客商编码。 */
    @JSONField(name = "code")
    private String code;

    /** 客商名称。 */
    @JSONField(name = "name")
    private String name;

    /** 客商类型。 */
    @JSONField(name = "externalType")
    private String externalType;

    /** 银行账号。 */
    @JSONField(name = "externalAcc")
    private String externalAcc;

    /** 联行号。 */
    @JSONField(name = "bankCode")
    private String bankCode;

    /** 银行名称。 */
    @JSONField(name = "bankName")
    private String bankName;
}
