package com.bytter.letter.api;
import java.math.BigDecimal;

public interface IGuaGuaranteeService {
    /**
     * 查询保函预计释放额度
     * @param billId
     * @param endDate
     * @return
     */
    BigDecimal queryGuaranteeUseRatMoney(String billId, String endDate);
}
