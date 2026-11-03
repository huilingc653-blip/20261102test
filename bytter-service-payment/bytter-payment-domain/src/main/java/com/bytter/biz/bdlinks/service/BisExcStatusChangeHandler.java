package com.bytter.biz.bdlinks.service;

import com.bytter.biz.allocation.dao.NisAllocationReqDao;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.dto.BisExcChangeMsg;
import com.bytter.biz.foreign.dao.FexCashFlowDao;
import com.bytter.biz.netbank.dao.NisBillheadDao;
import com.bytter.biz.wage.dao.NisWageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luoyj
 * @date 2024/3/25.
 */
@Transactional
@Component
public class BisExcStatusChangeHandler {

    @Autowired
    private NisBillheadDao nisBillheadDao;

    @Autowired
    private NisAllocationReqDao nisAllocationReqDao;

    @Autowired
    private NisWageDao nisWageDao;

    @Autowired
    private FexCashFlowDao fexCashFlowDao;

    /**
     * 收款指令状态变更
     */
    public void processBisExcChangeMsg(String voucherFrom, String cbsBillId, String voucherStat){
        switch (voucherFrom) {
            case BdlinkConstant.FROM_NIS_PAY:
                nisBillheadDao.updateVoucherState(cbsBillId, voucherStat);
            case BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN:
            case BdlinkConstant.FROM_ALLOCATION_TYPE_ASK:
            case BdlinkConstant.FROM_ALLOCATION_TYPE_POS:
                nisAllocationReqDao.updateVoucherState(cbsBillId, voucherStat);
                break;
            case BdlinkConstant.FROM_NIS_WAGE:
                nisWageDao.updateVoucherState(cbsBillId, voucherStat);
                break;
            default:
                break;
        }
    }

    public void processBisGatherExcChangeMsg(BisExcChangeMsg bisExcChangeMsg) {

    }

    public void processBisForeignExcChangeMsg(String cbsBillId, String voucherStat) {
        fexCashFlowDao.updateVoucherState(cbsBillId, voucherStat);
    }
}
