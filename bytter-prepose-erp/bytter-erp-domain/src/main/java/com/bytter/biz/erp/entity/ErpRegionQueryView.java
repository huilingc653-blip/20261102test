package com.bytter.biz.erp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 银行账户查询视图对象。
 *
 * @author zx
 * @date 2026/4/28
 */
@Data
@Entity
@SuppressWarnings("serial")
public class ErpRegionQueryView implements Serializable {

    @Id
    @JSONField(serialize = false)
    private String id;

    /** 市代码。 */
    @JSONField(name = "cityCode")
    private String cityCode;

    /** 市名称。 */
    @JSONField(name = "cityName")
    private String cityName;

    /** 省代码。 */
    @JSONField(name = "provCode")
    private String provCode;

    /** 省名称。 */
    @JSONField(name = "provName")
    private String provName;
}
