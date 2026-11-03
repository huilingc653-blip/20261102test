package com.bytter.finance.domain.directFinance.utils;
import com.bytter.finance.constant.DfConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-06 16:10
 * @Version 1.0
 */
public abstract class DfUtils {
    /**
     * 直融业务登记
     * 
     * @return
     */
    public static String getDfdirectFinanceInfoNo() {
        return SequenceSingleton.getSequence(SequenceEnum.DF_DIRECT_FINANCE_INFO_BILL_CODE,
                DfConstant.DF_DIRECT_FINANCE_INFO_CODE);
    }
    /**
     * 直融业务还款
     * 
     * @return
     */
    public static String getDfdirectFinanceRepayNo() {
        return SequenceSingleton.getSequence(SequenceEnum.DF_DIRECT_FINANCE_REPAY_BILL_CODE,
                DfConstant.DF_DIRECT_FINANCE_REPAY_CODE);
    }
}
