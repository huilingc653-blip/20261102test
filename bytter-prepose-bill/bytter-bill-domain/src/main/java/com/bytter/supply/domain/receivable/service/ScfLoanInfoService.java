package com.bytter.supply.domain.receivable.service;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.receivable.dto.ScfLoanInfoDTO;
import com.bytter.supply.domain.receivable.vo.ScfLoanInfoVO;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfLoanInfoService
 * @Description: 融资业务主信息服务接口
 * @since 2026/7/3
 */
public interface ScfLoanInfoService {

    /**
     * 分页查询融资业务主信息
     *
     * @param input 查询条件（分页参数 + formdata中的查询字段 + orderColumns）
     * @return 分页结果
     */
    DataTablesOutput<ScfLoanInfoVO> dataTable(DataTablesInput input);

    /**
     * 发起融资
     *
     * @param loanInfo     融资信息
     * @param currentUser  当前登录用户
     * @return 是否成功
     */
    boolean sendLoan(ScfLoanInfoDTO loanInfo, ICurrentUser currentUser);

    /**
     * 修改融资业务主信息
     *
     * @param loanInfo 融资信息
     */
    void updateLoanInfo(ScfLoanInfoDTO loanInfo);

    /**
     * 根据主键删除融资业务主信息
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteLoanInfo(String id);

    /**
     * 根据主键查询融资业务主信息详情
     *
     * @param id 主键
     * @return 融资信息
     */
    ScfLoanInfoVO getLoanInfo(String id);

    /**
     * 根据债权凭证编号查询融资业务主信息详情
     *
     * @param voucherCode 债权凭证编号
     * @return 融资信息
     */
    ScfLoanInfoVO getLoanInfoByVoucherCode(String voucherCode);
}