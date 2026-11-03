package com.bytter.supply.domain.receivable.service;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.bill.api.scf.dto.ScfPayTransferDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivableTransferInfoDTO;
import com.bytter.supply.domain.receivable.entity.ScfReceivableTransferInfo;
import com.bytter.supply.domain.receivable.vo.ScfReceivableTransferInfoGroupVO;
import com.bytter.supply.domain.receivable.vo.ScfReceivableTransferInfoVO;

import java.util.List;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableTransferInfoService
 * @Description: 债权凭证转让信息服务接口
 * @since 2026/7/8
 */
public interface ScfReceivableTransferInfoService {

    /**
     * 分页查询债权凭证转让信息
     *
     * @param input 查询条件（分页参数 + formdata中的查询字段 + orderColumns）
     * @return 分页结果
     */
    DataTablesOutput<ScfReceivableTransferInfoVO> dataTable(DataTablesInput input);

    /**
     * 查询债权凭证转让信息，可按转让状态、转让标识分别查询一对多/多对一，可按凭证编号过滤。
     * 一对多：voucherInfoList 为 1 条凭证信息，transferInfoList 为多条接收方转让信息；
     * 多对一：voucherInfoList 为多条凭证信息，transferInfoList 为 1 条接收方转让信息。
     *
     * @param transferFlag 转让标识：1 一对多，2 多对一；为空不限制转让标识
     * @param voucherCode  债权凭证编号；为空不按凭证编号过滤
     * @param voucherIds   债权凭证ID集合（逗号分隔）；为空不按凭证ID过滤。付款方式52回显场景：前端传付款单回显的应收凭证ID集合（selectedIds）联动查询
     * @param transferStatus 转让状态；为空查询所有状态
     * @return 一对多/多对一分组的转让信息
     */
    ScfReceivableTransferInfoGroupVO listTemp(String transferFlag, String voucherCode, String voucherIds, String transferStatus);

    /**
     * 一对多暂存前校验：凭证已存在多对一暂存数据时，不允许以一对多方式暂存
     *
     * @param voucherCode 凭证编号
     */
    void checkOneTemporary(String voucherCode);

    /**
     * 多对一暂存前校验：所选凭证已存在一对多暂存数据时，不允许以多对一方式暂存
     *
     * @param voucherCode 凭证编号，多个用逗号分隔
     */
    void checkManyTemporary(String voucherCode);

    /**
     * 转让前置校验，根据 transferFlag 区分一对多和多对一。
     *
     * @param dto 付款办理应收债权凭证转让信息
     */
    void paymentValidateTransfer(ScfPayTransferDTO dto);

    /**
     * 新增债权凭证转让信息
     *
     * @param transferInfoDTO 转让信息
     * @param currentUser     当前登录用户
     * @return 是否成功
     */
    boolean sendTransferInfo(ScfReceivableTransferInfoDTO transferInfoDTO, ICurrentUser currentUser);

    /**
     * 修改债权凭证转让信息
     *
     * @param transferInfoDTO 转让信息
     */
    void updateTransferInfo(ScfReceivableTransferInfoDTO transferInfoDTO);

    /**
     * 根据主键删除债权凭证转让信息
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteTransferInfo(String id);

    /**
     * 根据主键查询债权凭证转让信息详情
     *
     * @param id 主键
     * @return 转让信息
     */
    ScfReceivableTransferInfoVO getTransferInfo(String id);

    /**
     * 根据应收凭证id集合查询暂存转让信息（待转让状态），供付款方式52审批通过后提交转让组装数据
     *
     * @param voucherIds 应收凭证id集合（逗号分隔）
     * @return 暂存转让信息列表
     */
    List<ScfReceivableTransferInfoVO> listTempByVoucherIds(String voucherIds);

    /**
     * 保存转让草稿（付款方式52前端提交付款单前调用）：按转让标识分发到对应暂存逻辑，返回本次保存的草稿记录ID集合。
     * <p>多对一（transferFlag=2）：voucherIds + 接收方信息；单笔（其余）：voucherCode + 接收方信息。
     *
     * @param transferInfoDTO 转让信息
     * @return 草稿记录ID集合
     */
    List<String> temporarySaveTransfer(ScfReceivableTransferInfoDTO transferInfoDTO);

    /**
     * 按草稿ID集合查询转让暂存信息（付款方式52编辑回显反查凭证用）
     *
     * @param ids 草稿ID集合
     * @return 转让暂存信息列表
     */
    List<ScfReceivableTransferInfo> getTransferInfoByIds(List<String> ids);

    /**
     * 释放转让暂存（付款方式52再次进入变更时调用）：删除指定草稿记录，并将对应凭证的审批状态从"转让暂存"恢复为"开立审批通过"
     *
     * @param ids 待释放的草稿ID集合
     */
    void deleteTransferDrafts(List<String> ids);
}
