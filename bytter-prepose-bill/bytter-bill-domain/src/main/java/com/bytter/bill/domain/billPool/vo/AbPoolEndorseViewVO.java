package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 背书申请详情页面数据
 */
@Data
public class AbPoolEndorseViewVO implements Serializable {
    private AbPoolEndorseVO abPoolEndorseVO;
    private List<AbLog> abLogList;
    private AbPoolEndorseVO billInfo;
    private List<AbAccrecordsVO> accrecordsList;
    private Boolean isApprove;
}
