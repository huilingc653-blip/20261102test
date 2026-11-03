package com.bytter.finance.domain.precative.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核详情页数据
 */
@Data
public class CmsPrecativeLoanApproveDetailVo implements Serializable {

    private Date opDate;
    private CmsPrecativeLoanInfoVo precativePage;
    private String taskId;
    private String viewType;
}
