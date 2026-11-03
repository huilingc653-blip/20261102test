package com.bytter.bill.api.scf.service;

import com.bytter.bill.api.scf.dto.ScfPayableVoucherSelDTO;
import com.bytter.bill.api.scf.dto.ScfPayBindDTO;
import com.bytter.bill.api.scf.dto.ScfPayGenerateDTO;
import com.bytter.bill.api.scf.dto.ScfPayTransferDTO;
import com.bytter.bill.api.scf.dto.ScfReceivableVoucherSelDTO;
import com.bytter.bill.api.scf.dto.ScfVoucherValidateDTO;
import com.bytter.bill.api.scf.dto.ScfVoucherValidateResultDTO;

import java.util.List;

/**
 * 供应链金融-付款办理跨模块门面
 * <p>
 * 供 bytter-service-payment 模块通过 dubbo 调用：
 * - 51 应付债权凭证：选已开立凭证关联 / 未开立则付款审批通过后生成暂存凭证
 * - 52 应收债权凭证：保存转让草稿（前端提交付款单前调用）、按转让草稿反查凭证（付款单编辑回显）
 *
 * @author zhukr
 * @since 2026/8/18
 */
public interface IScfVoucherPayService {

    /**
     * 按凭证ID集合查询应付债权凭证（付款单view回显，不限状态）
     *
     * @param ids 凭证ID集合
     * @return 应付债权凭证
     */
    List<ScfPayableVoucherSelDTO> getPayableVoucherByIds(List<String> ids);

    /**
     * 校验凭证：付款办理提交前校验凭证状态、金额合计、供应商/平台/融资产品一致性
     *
     * @param dto 校验入参（凭证id集合/付款金额/平台/融资产品/供应商）
     * @return 校验结果
     */
    ScfVoucherValidateResultDTO validateVoucher(ScfVoucherValidateDTO dto);

    /**
     * 51-未开立：付款审批通过后生成一笔"暂存/未开立"的应付债权凭证
     * <p>
     * 凭证金额取自付款单，单据来源为"付款单生成(PAYMENT)"，平台/融资产品可后续修改并反写付款单。
     * 付款单与凭证的关联由中间表 nis_scf_info 承载，本方法只负责生成凭证并返回凭证ID，由付款侧写入中间表。
     *
     * @param dto 付款单信息
     * @return 新生成的应付债权凭证ID
     */
    String generateTempPayableVoucher(ScfPayGenerateDTO dto);

    /**
     * 51-已开立：付款审批通过后校验已关联的应付债权凭证仍为"已开立/审批通过"状态
     * <p>
     * 付款单与已开立凭证的关联在保存付款单时已写入中间表 nis_scf_info，此处仅做审批通过时的状态校验。
     *
     * @param dto 付款单号 + 凭证id集合
     */
    void bindPayableVoucher(ScfPayBindDTO dto);

    /**
     * 52-应收债权凭证：保存转让草稿（前端提交付款单前调用）
     * <p>保存即把对应凭证置为"转让暂存"；再次变更时先释放原转让暂存（前端回传 transferIds，逗号分隔），再保存当前转让数据。
     * 审批通过后的转让执行（executeTransferByIds）另期实现，本方法仅承载前后端交互。
     *
     * @param dto 转让数据（转让标识/凭证id（多个逗号分隔）/接收方/金额/日期/用途）
     * @return 本次保存的转让草稿ID集合（逗号拼接），付款单保存时写入中间表 nis_scf_info
     */
    String saveVoucherTransfer(ScfPayTransferDTO dto);

    /**
     * 52-应收债权凭证：按转让草稿ID集合反查应收债权凭证（付款单编辑回显）
     * <p>由转让草稿反查凭证编号再查凭证，合并转让暂存信息（接收方/金额/日期/用途）返回。
     *
     * @param transferIds 转让草稿ID集合
     * @return 应收债权凭证回显列表（凭证信息 + 转让暂存信息，按凭证去重）
     */
    List<ScfReceivableVoucherSelDTO> getReceivableVoucherByTransferIds(List<String> transferIds);
}
