package com.bytter.biz.erp.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 单位表
 *
 * @author fangcf
 * @since 2026-04-29
 */
@Data
public class SysCorp implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 单位名称
     */
    private String name;

    /**
     * 单位编码
     */
    private String code;

    /**
     * 租户ID
     */
    private String tenantId;

}
