package com.bytter.biz.payment.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 共享业务类型表DTO
 *
 * @author fangcf
 * @since 2025-05-20
 */
@Data
public class NisShareBusTypeDTO implements Serializable {

    /**
     * 主键id
     */
    private String id;
    /**
     * 共享业务类型编码
     */
    private String shareBusType;
    /**
     * 共享业务类型名称
     */
    private String shareBusTypeName;
    /**
     * 关联业务类别主键id
     */
    private String busTypeId;
    /**
     * 关联业务类别编码
     */
    private String busType;
    /**
     * 关联业务类别名称
     */
    private String busTypeName;
    /**
     * 关联业务类别名称前端显示
     */
    private String busTypeNameShow;
    /**
     * 状态：1-正常，0-禁用
     */
    private String status;
    /**
     * 状态名称：1-正常，0-禁用
     */
    private String statusName;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
}
