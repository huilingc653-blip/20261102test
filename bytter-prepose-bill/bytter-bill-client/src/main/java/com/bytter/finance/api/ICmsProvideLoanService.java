package com.bytter.finance.api;
import com.bytter.finance.dto.CmsProvideInterestDTO;
import com.bytter.finance.dto.CmsProvideLoanRepayDTO;
import com.bytter.finance.dto.GuaContractCmsProvideLoanDTO;
import java.math.BigDecimal;
import java.util.List;
/**
 * 银行借款接口
 * 
 * @author lvwl
 */
public interface ICmsProvideLoanService {
    /**
     * 记账作废
     * 
     * @param repayId
     *            还款ID
     */
    void invalidVoucher(String repayId);
    /**
     * 设置业务挂接担保合同
     * 
     * @param id
     *            业务id
     */
    void occupyGuaContract(String id);
    /**
     * 设置业务取消挂接担保合同
     * 
     * @param id
     *            业务id
     */
    void cancelGuaContract(String id);
    /**
     * 获取所有挂接担保的贷款数据
     */
    List<GuaContractCmsProvideLoanDTO> getGuaCmsProvideLoan();
    /**
     * 获取占用授信的金额
     * @param billNo
     * @param endDate
     * @return
     */
    BigDecimal queryProvideUseRatMoney(String billNo, String endDate);

    /**
     * 定时任务查询待生成需求单的融资到期
     * @return
     */
    List<CmsProvideInterestDTO> listTaskRepayForNisDemand();
}
