package com.bytter.biz.erp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 单位信息查询视图对象。
 *
 * @author zx
 * @date 2026/4/29
 */
@Data
@Entity
@Table(name = "ERP_CORP_QUERY_VIEW", schema = "")
@SuppressWarnings("serial")
public class ErpCorpQueryView implements Serializable {

    /** 单位代码。 */
    @Id
    @JSONField(name = "code")
    private String code;

    /** 租户ID。 */
    @JSONField(serialize = false)
    private String tenantId;

    /** 单位名称。 */
    @JSONField(name = "name")
    private String name;

    /** 状态（0禁用，1启用）。 */
    @JSONField(name = "status")
    private String status;

    /** 更新日期（yyyy-MM-dd）。 */
    @JSONField(name = "updateDate")
    private String updateDate;
}
