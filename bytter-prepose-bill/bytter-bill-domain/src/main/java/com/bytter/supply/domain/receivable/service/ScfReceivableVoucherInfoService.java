package com.bytter.supply.domain.receivable.service;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.receivable.dto.ScfReceivableVoucherBatchSignDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivableVoucherInfoDTO;
import com.bytter.supply.domain.receivable.entity.ScfReceivableVoucherInfo;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherImportVO;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherInfoVO;

import java.util.List;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableVoucherInfoService
 * @Description: 应收债权凭证信息服务接口
 * @since 2026/6/30
 */
public interface ScfReceivableVoucherInfoService {

    /**
     * 分页查询应收债权凭证信息
     *
     * @param input 查询条件（分页参数 + formdata中的查询字段 + orderColumns）
     * @return 分页结果
     */
    DataTablesOutput<ScfReceivableVoucherInfoVO> dataTable(DataTablesInput input);

    /**
     * 新增应收债权凭证信息
     *
     * @param voucherInfo 凭证信息
     * @return 是否成功
     */
    boolean saveVoucherInfo(ScfReceivableVoucherInfoDTO voucherInfo, ICurrentUser currentUser);

    /**
     * 修改应收债权凭证信息
     *
     * @param voucherInfo 凭证信息
     * @return 是否成功
     */
    void updateVoucherInfo(ScfReceivableVoucherInfoDTO voucherInfo);

    /**
     * 根据主键删除应收债权凭证暂存数据
     * <p>
     * 仅允许删除暂存数据，非暂存数据不允许删除。
     * type 取值与暂存单据状态一致：open-draft(凭证开立暂存)、loan-draft(融资暂存)、transfer-draft(转让暂存)、payment-draft(提示付款暂存)。
     * 凭证开立暂存删除凭证信息表记录；融资/转让/提示付款暂存删除对应业务信息表记录，并将主凭证信息表状态恢复为"开立审批通过、已签收"。
     *
     * @param id   凭证主键
     * @param type 暂存业务类型
     * @return 是否成功
     */
    boolean deleteVoucherInfo(String id, String approvalStatus);

    /**
     * 根据主键查询应收债权凭证信息详情
     *
     * @param id 主键
     * @return 凭证信息
     */
    ScfReceivableVoucherInfoVO getVoucherInfo(String id);

    /**
     * 根据主键查询应收债权凭证信息详情
     *
     * @param id 主键
     * @return 凭证信息
     */
    List<ScfReceivableVoucherInfoVO> getVoucherInfoList(String id);

    ScfReceivableVoucherInfo getVoucherInfoByCode(String voucherCode);

    /**
     * 发起融资结果查询指令，仅允许业务状态为融资中的凭证
     *
     * @param voucherCode 债权凭证编号
     * @return 是否成功
     */
    void queryFinanceResult(String voucherCode);

    /**
     * 发起到期收款查询指令，仅允许业务状态为收款中的凭证
     *
     * @param voucherCode 债权凭证编号
     * @return 是否成功
     */
    void queryDueCollectResult(String voucherCode);

    /**
     * 付款办理（付款方式52）转让选择：datatable 分页查询可转让的应收债权凭证（单据状态=开立审批通过 且 业务状态=已签收）。
     * <p>编辑回显：前端回传已选择的凭证id（selectedIds），即使其已进入"转让暂存"也一并返回（or id in selectedIds），用于展示已选择数据。
     * 凭证编号/开立单位/平台/融资产品/线上线下等检索条件由前端写在 formdata（params）中，经 DataTablesInput 透传。
     *
     * @param dataTablesInput datatable 分页/排序/检索参数
     * @param selectedIds     前端已选择的凭证id集合（逗号分隔或数组，可空）
     * @return 可转让应收债权凭证分页结果
     */
    DataTablesOutput<ScfReceivableVoucherInfoVO> listForPayTransfer(DataTablesInput dataTablesInput, String selectedIds);

    /**
     * 批量修改签收状态
     *
     * @param batchSignDTO 批量签收请求DTO（包含凭证ID列表、目标状态、线上签署标识）
     * @param currentUser 当前登录用户
     */
    void batchSign(ScfReceivableVoucherBatchSignDTO batchSignDTO,ICurrentUser currentUser);

    /**
     * 应收债权凭证补录
     * <p>
     * 补录时，仅可对"凭证编号"和"前置凭证编号"字段进行修改。
     * 补录完成后，业务状态改为"已签收"。
     */
    void supplement(ScfReceivableVoucherInfoDTO voucherInfo);

    /**
     * 应付债权凭证台账查询
     * <p>
     * 单据状态固定为"开立审批通过"、"融资审批通过"或"提示付款审批通过"，
     * 业务状态固定为"已签收"、"已融资"或"已收款"。
     */
    DataTablesOutput<ScfReceivableVoucherInfoVO> getVoucherLedger(DataTablesInput input);

    /**
     * 查询所有审批中的应收债权凭证
     * <p>
     * 审批中包括：开立审批中、融资审批中、转让审批中、提示付款审批中
     */
    DataTablesOutput<WorkFlowPersistence> getPendingApprovalVoucher(DataTablesInput input);

    /**
     * Excel批量导入应收债权凭证
     * <p>
     * 导入后凭证状态：单据状态为"开立暂存"、业务状态为"未签收"
     *
     * @param list 导入数据列表
     * @param currentUser 当前登录用户
     */
    void importVoucherInfo(List<ScfReceivableVoucherImportVO> list, ICurrentUser currentUser);

    /**
     * 查询凭证链路（树形结构）
     * <p>
     * 根据凭证ID找到当前凭证，装配转让、融资、收款等业务信息，并为已签收凭证
     * 构造签收展示节点；同时根据前置凭证编号向前递归查询前手凭证，组成树形结构返回。
     *
     * @param id 凭证主键ID
     * @return 当前凭证节点（children 中包含签收展示节点及前手凭证链路）
     */
    ScfReceivableVoucherInfoVO getLinkChain(String id);
}
