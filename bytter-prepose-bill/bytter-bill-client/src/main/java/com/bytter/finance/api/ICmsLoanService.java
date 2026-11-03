package com.bytter.finance.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.finance.dto.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 贷款对外接口
 *
 * @author caozx
 */
public interface ICmsLoanService {
    /**
     * 获取委托贷款展期申请
     *
     * @return 委托贷款展期申请集合
     */
    List<CmsPrecativeLoanDeferredDTO> getCmsPrecativeDeferreds();
    /**
     * 根据单号获取委托贷款发放记录
     *
     * @param billCode 发放编号
     * @return 委托贷款DTO
     */
    CmsPrecativeLoanSendDTO getCmsPrecativeLoanSendByBillCode(String billCode);
    /**
     * 根据billCode获取内部贷款发放记录
     *
     * @param billCode 发放编号
     * @return 贷款发放DTO
     */
    CmsLoanSendDTO getLoanSendByBillCode(String billCode);
    /**
     * 根据id查询内部贷款还款表的数据
     *
     * @param id 还款id
     * @return 贷款还款DTO
     */
    CmsLoanRepayDTO getLoanRepayById(String id);
    /**
     * 根据id查询内部贷款还款表的数据
     *
     * @param billCode 还款编号
     * @return 贷款还款DTO
     */
    CmsLoanRepayDTO getCmsLoanRepayByBillCode(String billCode);
    /**
     * 根据单号获取委托贷款录入记录
     *
     * @param billCode 委托贷款申请编号
     * @return 委托贷款申请DTO
     */
    CmsPrecativeLoanInfoDTO getCmsPrecativeLoanInfoByBillCode(String billCode);
    /**
     * 根据单号获取委托贷款录入记录
     *
     * @param billCode 委托贷款还款编号
     * @return 委托贷款还款DTO
     */
    CmsPrecativeLoanRepayDTO getCmsPrecativeLoanRepayByBillCode(String billCode);
    /**
     * 根据billCode获取内部贷款info表数据
     *
     * @param billCode 内部贷款申请编号
     * @return 内部贷款DTO
     */
    CmsLoanInfoDTO getLoanInfoBybillCode(String billCode);
    /**
     * 查询内部贷款发放到期
     *
     * @param dayClose 网点日结日期传输显示对象
     * @return 企业贷款检查集合
     */
    List<CmsLoanSendOutCheckDTO> getLoanSendOutInfoByEndDate(FcDayCloseDTO dayClose);
    /**
     * 查询银行借款发放到期
     *
     * @param dayClose 网点日结日期传输显示对象
     * @return 银行借款检查集合
     */
    List<CmsProvideLoanSendCheckDTO> getBankLoadSendInfoByEndDate(FcDayCloseDTO dayClose);
    /**
     * 查询委托贷款发放到期
     *
     * @param dayClose 网点日结日期传输显示对象
     * @return 委托贷款检查集合
     */
    List<CmsPrecativeLoanSendCheckDTO> getPrecativeLoanInfoByEndDate(FcDayCloseDTO dayClose);
    /**
     * 根据voucherNo获取委托贷款还款单据的本金
     *
     * @param voucherNo 记账凭证号
     * @return 还款金额
     */
    BigDecimal getPrecativeLoanRepayMoney(String voucherNo);
    /**
     * @return java.math.BigDecimal
     * @Description 根据委托贷款主表信息编号查询委托贷款已还总金额
     * @Param [infoBillCode] 贷款主表信息编号
     * @Date 2019/12/2 16:39
     * @Author fusb
     */
    BigDecimal getPrecativeLoanRepayMoneySum(String infoBillCode);
    /**
     * 查询委托贷款待记账数据
     *
     * @param dataTablesInput
     */
    DataTablesOutput getDateTable(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 修改委托贷款发放表
     *
     * @param send 委托贷款发放对象
     * @param user 当前用户
     */
    void updatePrecative(CmsPrecativeLoanSendDTO send, ICurrentUser user);
    /**
     * 修改委托贷款表
     *
     * @param send 委托贷款发放对象
     * @param user 当前用户
     */
    void updatePrecativeRepay(CmsPrecativeLoanRepayDTO send, ICurrentUser user);
    /**
     * 修改内部贷款发放
     *
     * @param send 委托贷款发放对象
     */
    void updateCmsLoanSend(CmsLoanSendDTO send);
    /**
     * 是否还款完成操作
     *
     * @param repay 委托贷款还款对象
     */
    CmsLoanRepayDTO isFinishedRepay(CmsLoanRepayDTO repay);
    /**
     * 查询银行借款记账列表
     *
     * @param dataTablesInput 查询条件
     * @param user            当前用户
     * @param onlineSign      在线标志
     * @param confirm         是否确认还款
     * @return 手工记账还款列表
     */
    DataTablesOutput getProvideDateTable(DataTablesInput dataTablesInput, ICurrentUser user, String onlineSign,
                                         String confirm);
    /**
     * 根据借款编号查询银行借款表信息
     *
     * @param billcode 借款编号
     * @return 银行借款DTO
     */
    CmsProvideLoanInfoDTO getLoanInfoVOByBillCode(String billcode);
    /**
     * 根据借款发放编号查询银行借款发放表信息
     *
     * @param billCode 发放编号
     * @return 银行借款发放DTO
     */
    CmsProvideSendDTO getCmsProvideLoanSendByBillCode(String billCode);
    /**
     * 根据借款还款编号查询银行借款还款表信息
     *
     * @param billCode   还款编号
     * @param onlineSign 在线标识
     * @return 银行借款还款DTO
     */
    CmsProvideLoanRepayDTO getCmsProvideLoanRepayByBillCode(String billCode, String onlineSign);
    /**
     * 修改银行借款还款表
     *
     * @param repay 银行借款还款DTO
     * @param user  当前用户
     */
    void updateProvideRepay(CmsProvideLoanRepayDTO repay, ICurrentUser user);
    /**
     * 修改银行借款发放表
     *
     * @param send 银行借款发放DTO
     * @param user 当前用户
     */
    void updateProvide(CmsProvideSendDTO send, ICurrentUser user);
    /**
     * 获取银行借款还款金额
     *
     * @param infoBillCode 银行申请编号
     * @return 银行借款还款金额
     */
    BigDecimal getInfoRepayMoneySum(String infoBillCode);
    /**
     * 获取内部贷款还款金额
     *
     * @param sendCode 发放编号
     * @return 内部贷款还款金额
     */
    BigDecimal getSendRepayedMoney(String sendCode);
    /**
     * 查询贷款是否还款完成
     *
     * @param repay 内部贷款还款DTO
     * @return true-还款完成,false-未还款完成
     */
    boolean checkFinishedRepay(CmsLoanRepayDTO repay);
    /**
     * 查询发放待记账数据
     *
     * @return
     */
    List<CmsLoanSendDTO> querySendTallyData();
    /**
     * 查询还款待记账数据
     *
     * @return
     */
    List<CmsLoanRepayDTO> queryRepayTallyData();
    /**
     * 设置业务挂接担保合同
     *
     * @param id 业务id
     */
    void occupyGuaContract(String id);
    /**
     * 设置业务取消挂接担保合同
     *
     * @param id 业务id
     */
    void cancelGuaContract(String id);
    /**
     * 更改银行借款发放流水认领功能挂接标记为挂接
     */
    void relateBankFlow(String id);
    /**
     * 更改银行借款发放流水认领功能挂接标记为未挂接
     */
    void cancelBankFlow(String id);
    /**
     * 判断银行借款是否还款完成
     */
    void provideIsRepayComplete(String repayId, ICurrentUser user);
    /**
     * 根据借款发放id获取借款发放数据
     *
     * @param id
     * @return
     */
    CmsProvideSendDTO getCmsProvideLoanSendById(String id);
    /**
     * 查询待记账发放数据
     */
    List<CmsPrecativeLoanSendDTO> queryTallyPrecativeSend();
    /**
     * 查询还款待记账数据
     */
    List<CmsPrecativeLoanRepayDTO> queryTallyPrecativeRepay();
    /**
     * 查询发放账务数据 --待记账
     */
    List<CmsProvideSendDTO> queryTallyProvideSend();
    /**
     * 查询还款账务数据 --记账
     */
    List<CmsProvideLoanRepayDTO> queryTallyProvideRepay(String onlineSign);
    /**
     * <br>
     * <b>功能：</b> 查询贷款合同金额及还款金额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/3/12 0012 <br>
     */
    List<CmsLoanRepayMoneyDTO> queryLoanRepayMoneyByLoanAcc(String loanAcc, Date endDate);
    /**
     * 获取所有挂接担保的内部贷款数据
     */
    List<GuaContractCmsLoanDTO> getHangGuaForLoan();
    /**
     * 批量更新数据
     *
     * @param dataList 更新数据集合
     */
    void batchUpdateSend(List<CmsLoanSendDTO> dataList);
    /**
     * 根据条件获取还款信息
     *
     * @param billCode      单据编号
     * @param origVoucherNo 凭证号
     * @return 单据信息
     */
    CmsLoanRepayDTO getRepayUniqueBy(String origVoucherNo);
    /**
     * 更新还款数据
     *
     * @param repay 还款更新参数
     */
    void updateRepayEntity(CmsLoanRepayDTO repay);
    /**
     * 批量更新还款表数据
     *
     * @param dataList 还款数据
     */
    void batchUpdateRepay(List<CmsLoanRepayDTO> dataList);
}
