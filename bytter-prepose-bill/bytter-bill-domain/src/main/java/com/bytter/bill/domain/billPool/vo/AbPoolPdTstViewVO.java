package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 质押托管转换详情页面数据
 */
@Data
public class AbPoolPdTstViewVO implements Serializable {
    private Object abReceiveBillPage;
    private AbPoolOutApplyVO poolDTO;
    private List<AbLog> abLogPage;
    private List<AbPoolPdTstVO> tstList;
    private List<AbAccrecordsVO> accrecordsList;
}
