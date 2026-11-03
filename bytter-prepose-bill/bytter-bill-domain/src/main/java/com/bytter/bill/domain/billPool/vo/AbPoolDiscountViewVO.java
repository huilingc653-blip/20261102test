package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 贴现申请详情页面数据
 */
@Data
public class AbPoolDiscountViewVO implements Serializable {
    private AbPoolDiscountVO abPoolDiscountVO;
    private List<AbLog> abLogList;
    private AbPoolDiscountVO billInfo;
    private List<AbAccrecordsVO> accrecordsList;
    private Boolean isApprove;
}
