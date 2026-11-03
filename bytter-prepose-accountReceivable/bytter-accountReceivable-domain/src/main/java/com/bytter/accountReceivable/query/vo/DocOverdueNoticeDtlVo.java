package com.bytter.accountReceivable.query.vo;

import lombok.Data;

import java.util.Date;

/**
 *
 */
@Data
public class DocOverdueNoticeDtlVo {

    private String id;

    private String docId;
    /**
     * 逾期原因
     */
    private String overdueReason;
    /**
     * 催收人
     */
    private String allocationBy;
    /**
     * 催收时间
     */
    private Date allocationDate;
    /**
     * 催收结果
     */
    private String allocationResult;

    private String tenantId;
    private String createBy;
    private Date createDate;

    /**
     * 非自己的，允许看到，不允许编辑
     * 1:自己的　０：其他人的
     */
    private String editFlag;
}
