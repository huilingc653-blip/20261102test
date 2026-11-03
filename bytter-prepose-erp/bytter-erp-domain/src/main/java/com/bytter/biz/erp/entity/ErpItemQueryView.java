package com.bytter.biz.erp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 预算科目查询视图对象。
 *
 * @author zx
 * @date 2026/4/28
 */
@Data
@Entity
@Table(name = "ERP_ITEM_QUERY_VIEW", schema = "")
@SuppressWarnings("serial")
public class ErpItemQueryView implements Serializable {

    @Id
    @JSONField(serialize = false)
    private String id;

    /** 租户ID。 */
    @JSONField(serialize = false)
    private String tenantId;

    /** 科目代码。 */
    @JSONField(name = "itemCode")
    private String itemCode;

    /** 科目名称。 */
    @JSONField(name = "itemName")
    private String itemName;
}
