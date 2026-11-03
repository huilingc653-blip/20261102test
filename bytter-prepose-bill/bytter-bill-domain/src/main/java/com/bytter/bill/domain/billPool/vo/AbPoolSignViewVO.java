package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 电票签收详细信息页面数据
 */
@Data
public class AbPoolSignViewVO implements Serializable {
    private AbPoolSignVO abPoolSignVO;
    private List<AbLog> abLogList;
    private AbBillReceivebizVO info;
    private List<AbAccrecordsVO> accrecordsList;
    private Boolean isApprove;
}
