package com.bytter.guarantee.api;
import com.bytter.core.common.datatable.DataTablesInput;
/**
 * 担保合同接口
 *
 * @author xiay
 */
public interface IGuaContractServie {
    /**
     * 检查是否存在挂接的担保合同
     * @param corpId
     * @return
     */
    boolean checkContractApprovedList(String corpId);
}
