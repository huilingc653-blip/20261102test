package com.bytter.guarantee.api;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.vo.FcBondAccVO;
import com.bytter.guarantee.vo.FcBondPreserveVO;

public interface IFcBondPreserveService {

    /**
     * 获取保证金维护列表数据
     */
    public DataTablesOutput getPreserveTable(DataTablesInput dataTablesInput, ICurrentUser user);

    /**
     * 保存保证金项目信息
     */
    public void tempOrsubmit(FcBondPreserveVO fcBondPreserveVO, String saveType, ICurrentUser user);

    /**
     * 删除保证金项目
     */
    public void deleteFcBondItem(String id, ICurrentUser user);

    /**
     * 释放保证金项目占用金额
     */
    public void releaseItemAmount(String id, ICurrentUser user);

    /**
     * 根据id获取保证金项目的相关信息
     */
    public FcBondPreserveVO getFcBondItemById(String id);

    /**
     * 获取审批列表数据
     */
    public DataTablesOutput<WorkFlowPersistence> getApproveDataTables(DataTablesInput dataTablesInput);

    /**
     * 根据网点和银行查询存出保证金的账户列表
     */
    public DataTablesOutput<FcBondAccVO> queryBondAccDataTables(DataTablesInput dataTablesInput, String corpId, String bankCode, String curId,
                                                                ICurrentUser user);

}
