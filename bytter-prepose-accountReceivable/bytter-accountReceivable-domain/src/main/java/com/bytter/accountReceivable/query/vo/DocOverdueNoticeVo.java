package com.bytter.accountReceivable.query.vo;

import com.bytter.accountReceivable.docManage.vo.DocManageVo;
import com.bytter.accountReceivable.query.entity.DocOverdueNotice;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 逾期催收登记表
 */
@Data
public class DocOverdueNoticeVo extends DocManageVo {

    /**
     * 已核销金额
     */
    private BigDecimal writeOffMoney;

    /**
     * 逾期占比
     */
    private BigDecimal overdueRate;
    /**
     * 逾期金额
     */
    private BigDecimal overdueMoney;
    /**
     * 未到期金额
     */
    private BigDecimal unexpiredMoney;
    /**
     * 逾期原因
     */
    private String overdueReason;

    private DocOverdueNotice[] notices;

}
