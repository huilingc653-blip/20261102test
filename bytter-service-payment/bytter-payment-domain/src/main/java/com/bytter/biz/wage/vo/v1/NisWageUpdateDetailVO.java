package com.bytter.biz.wage.vo.v1;

import com.bytter.biz.wage.entity.NisWageDetail;
import com.bytter.biz.wage.vo.NisWageVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 代发工资明细
 */
@Data
public class NisWageUpdateDetailVO implements Serializable {
    /**
     * 代发工资明细
     */
    private NisWageDetail nisWageDetailPage;
    /**
     * 代发工资
     */
    private NisWageVO nisWagePage;
    /**
     *
     */
    private String isMustCode;
    /**
     *
     */
    private String wageId;
    /**
     *
     */
    private String voucherStat;
}
