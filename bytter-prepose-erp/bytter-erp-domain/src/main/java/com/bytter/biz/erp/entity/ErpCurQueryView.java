package com.bytter.biz.erp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 币别信息查询视图实体。
 *
 * @author zx
 * @date 2026/4/29
 */
@Data
@Entity
@Table(name = "ERP_CUR_QUERY_VIEW", schema = "")
@SuppressWarnings("serial")
public class ErpCurQueryView implements Serializable {

    /** 视图行主键（内部使用，不对外序列化）。 */
    @Id
    @JSONField(serialize = false)
    private String id;

    /** 租户 ID（内部使用，不对外序列化）。 */
    @JSONField(serialize = false)
    private String tenantId;

    /**
     * 币别代码。
     */
    @JSONField(name = "curCode")
    private String curCode;

    /**
     * 币别名称。
     */
    @JSONField(name = "curName")
    private String curName;

    /**
     * 币种简码。
     */
    @JSONField(name = "englishCode")
    private String englishCode;

    /**
     * 币种汇率。
     */
    @JSONField(name = "curExrate")
    private String curExrate;

    /**
     * 有效标志。
     */
    @JSONField(name = "validSign")
    private String validSign;

    /**
     * 更新日期。
     */
    @JSONField(name = "updateDate")
    private String updateDate;
}
