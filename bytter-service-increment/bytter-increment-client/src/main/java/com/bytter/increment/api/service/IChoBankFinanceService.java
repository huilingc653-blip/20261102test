package com.bytter.increment.api.service;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.increment.api.dto.ChoBankFinanceDTO;
import com.bytter.increment.api.dto.ChoProductRedeemDTO;
import com.bytter.increment.api.dto.DepositReportCommonDTO;
import java.math.BigDecimal;
/**
 * 银行理财接口类
 * @author huangx
 * @since 2020-6-4
 */
public interface IChoBankFinanceService {
    /**
     * 获取银行存款信息
     * @param user  用户
     * @param resource  资源
     * @param busType   10-活期转定期（定期存款） 12-活期转通知（通知存款）
     * @return
     */
    DepositReportCommonDTO getBankFinanceBalByUser(ICurrentUser user, String resource, String busType, String corpId);
    /**
     * 获取七天到期银行存款
     * @param user  用户
     * @param resource  资源
     * @param corpId  单位ID
     * @return
     */
    BigDecimal getSevenDayDeposit(ICurrentUser user, String resource, String corpId);
    /**
     * 银行流水认领-挂接接口(定转活、通转活)
     * @param confirm  1-确认；0；取消确认
     * @param businessId  业务ID
     */
    void collectionConfirmBankFinance(String confirm,String businessId);
    /**
     * 银行流水认领-挂接接口(理财赎回)
     * @param confirm  1-确认；0；取消确认
     * @param businessId  业务ID
     */
    void collectionConfirmProductRedeem(String confirm,String businessId);
    /**
     * 银行流水认领-根据ID获取数据对象(理财赎回)
     * @param id
     */
    ChoProductRedeemDTO getProductRedeemEntity(String id);
    /**
     * 银行流水认领-根据ID获取数据对象(定转活、通转活)
     * @param id
     */
    ChoBankFinanceDTO getBankFinanceEntity(String id);
    
    ChoBankFinanceDTO getBankFinanceByVoucherNo(String voucherNo);
    
    int updateBankFinanceByVoucherNo(ChoBankFinanceDTO choBankFinanceDTO);
}
