package com.bytter.supply.domain.receivable.service;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.receivable.dto.ScfReceivablePaymentInfoDTO;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.vo.ScfReceivablePaymentInfoVO;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivablePaymentInfoService
 * @Description: 债权凭证收款明细服务接口
 * @since 2026/7/10
 */
public interface ScfReceivablePaymentInfoService {

    /**
     * 分页查询债权凭证收款明细
     *
     * @param input 查询条件（分页参数 + formdata中的查询字段 + orderColumns）
     * @return 分页结果
     */
    DataTablesOutput<ScfReceivablePaymentInfoVO> dataTable(DataTablesInput input);

    /**
     * 新增债权凭证收款明细
     *
     * @param paymentInfo 收款明细信息
     * @return 是否成功
     */
    boolean savePaymentInfo(ScfReceivablePaymentInfoDTO paymentInfo);

    /**
     * 修改债权凭证收款明细
     *
     * @param paymentInfo 收款明细信息
     */
    void updatePaymentInfo(ScfReceivablePaymentInfoDTO paymentInfo);

    /**
     * 根据主键删除债权凭证收款明细
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deletePaymentInfo(String id);

    /**
     * 根据凭证编号查询债权凭证收款明细详情
     *
     * @param voucherCode 凭证编号
     * @return 收款明细信息
     */
    ScfReceivablePaymentInfoVO getPaymentInfo(String voucherCode);

    /** 根据收款记录主键查询详情（含关联应收债权凭证信息） */
    ScfReceivablePaymentInfoVO getPaymentInfoById(String id);

    /**
     * 单条收款处理
     * <p>
     * 仅可对"单据状态"为"审批通过"、"业务状态"为"已签收"的电子债权凭证，进行"收款"处理。
     * 若为线下处理，则收款处理审批通过后，自动生成债权凭证到期收款记录。
     *
     * @param paymentDTO  收款请求
     * @param currentUser 当前登录用户
     */
    void sendPayment(ScfReceivablePaymentInfoDTO paymentDTO, ICurrentUser currentUser);

    /**
     * 收款暂存
     * <p>
     * 保存收款明细时，将对应应收凭证单据状态置为"提示付款暂存"、
     * 收款明细收款状态置为"待收款"。
     *
     * @param paymentDTO 收款明细请求
     * @param currentUser 当前登录用户
     */
    void temporarySave(ScfReceivablePaymentInfoDTO paymentDTO, ScfReceivableVoucherInfo voucher);
}
