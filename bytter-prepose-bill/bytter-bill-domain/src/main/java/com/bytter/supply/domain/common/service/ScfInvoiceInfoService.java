package com.bytter.supply.domain.common.service;

import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.domain.common.dto.ScfInvoiceInfoDTO;
import com.bytter.supply.domain.common.entity.ScfInvoiceInfo;
import com.bytter.supply.domain.common.vo.ScfInvoiceInfoVO;

import java.util.List;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfInvoiceInfoService
 * @Description: 应付/应收债券发票信息服务接口
 * @since 2026/7/3
 */
public interface ScfInvoiceInfoService {

    /**
     * 分页查询发票信息
     *
     * @param input 查询条件（分页参数 + formdata中的查询字段 + orderColumns）
     * @return 分页结果
     */
    DataTablesOutput<ScfInvoiceInfoVO> dataTable(DataTablesInput input);

    /**
     * 新增发票信息
     *
     * @param invoiceInfoDTO 发票信息DTO
     * @return 是否成功
     */
    boolean saveInvoiceInfo(ScfInvoiceInfoDTO invoiceInfoDTO);

    /**
     * 修改发票信息
     *
     * @param invoiceInfo 发票信息
     * @return 是否成功
     */
    boolean updateInvoiceInfo(ScfInvoiceInfo invoiceInfo);

    /**
     * 根据主键删除发票信息
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteInvoiceInfo(String id);

    /**
     * 根据主键查询发票信息详情
     *
     * @param id 主键
     * @return 发票信息
     */
    ScfInvoiceInfoVO getInvoiceInfo(String id);

    /**
     * 根据主键查询发票信息（Entity）
     *
     * @param id 主键
     * @return 发票信息
     */
    ScfInvoiceInfo getInvoiceInfoById(String id);

    /**
     * 根据属性查询发票列表
     *
     * @param propertyName 属性名
     * @param value 属性值
     * @return 发票列表
     */
    List<ScfInvoiceInfo> listBy(String propertyName, Object value);
}
