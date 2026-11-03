package com.bytter.biz.payment.api.service;
import com.bytter.biz.payment.api.dto.NisExpenseDetailDTO;
import java.math.BigDecimal;
import java.util.List;
/**
 * 费用报销接口类
 * @author huangx
 * @since 2017-09-02 09:49:44
 */
public interface INisExpenseDetailService {
    /**
     * 根据付款Id获取交易失败的费用报销明细列表
     * @param billId 付款单ID
     * @return 费用报销集合
     */
    List<NisExpenseDetailDTO> listByBillIdFailure(String billId);
    /**
     * 根据付款Id获取等待结果的费用报销明细列表
     * @param billId 付款单ID
     * @return 费用报销集合
     */
    List<NisExpenseDetailDTO> listByBillIdWaitResult(String billId);
    /**
     * 获取费用报销单总金额
     * @param billId
     * @return
     */
    BigDecimal getSumExpenseMoneyByBillId(String billId);
    /**
     * 获取费用报销单失败总金额
     * @param billId
     * @return
     */
    BigDecimal getSumFailExpenseMoneyByBillId(String billId);
    /**
     * 根据付款Id获取交易失败的费用报销明细列表
     * @param billId 付款单ID
     * @return 费用报销集合
     */
    List<NisExpenseDetailDTO> listByBillId(String billId);
    /**
     * 获取ERP待处理单据
     * @return
     */
    List<NisExpenseDetailDTO> listErpTodoBill();
}
