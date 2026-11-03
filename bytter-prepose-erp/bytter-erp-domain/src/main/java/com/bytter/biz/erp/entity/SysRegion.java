package com.bytter.biz.erp.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 区域信息表
 *
 * @author wangce 2021-02-24
 * @since 1.0.0
 */
@Data
public class SysRegion implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 省代码
     */
    private String bankProv;
    /**
     * 市代码
     */
    private String bankInputCity;
    /**
     * 市代码
     */
    private String provInputName;
    /**
     * 名称
     */
    private String name;
    /**
     * 等级
     */
    private String reLevel;
    /**
     * 排序
     */
    private String sort;
    /**
     * 父主键
     */
    private String parentId;
    /***
     * 租户id
     */
    private String tenantId;
}
