package com.bytter.guarantee.bond.service.impl;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.api.IFcBondPreserveService;
import com.bytter.guarantee.bond.service.FcBondPreserveService;
import com.bytter.guarantee.vo.FcBondAccVO;
import com.bytter.guarantee.vo.FcBondPreserveVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fcBondPreserveServiceImpl")
@DubboService
public class FcBondPreserveServiceImpl implements IFcBondPreserveService {
    @Autowired
    private FcBondPreserveService fcBondPreserveService;

    /**
     * 获取保证金维护列表数据
     */
    public DataTablesOutput<FcBondPreserveVO> getPreserveTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        return fcBondPreserveService.getPreserveTable(dataTablesInput, user);
    }

    /**
     * 保存保证金项目信息
     */
    public void tempOrsubmit(FcBondPreserveVO fcBondPreserveVO, String saveType, ICurrentUser user) {
        fcBondPreserveService.tempOrsubmit(fcBondPreserveVO, saveType, user);
    }

    /**
     * 删除保证金项目
     */
    public void deleteFcBondItem(String id, ICurrentUser user) {
        fcBondPreserveService.deleteFcBondItem(id, user);
    }

    /**
     * 释放保证金项目占用金额
     */
    public void releaseItemAmount(String id, ICurrentUser user) {
        fcBondPreserveService.releaseItemAmount(id, user);
    }

    /**
     * 根据id获取保证金项目的相关信息
     */
    public FcBondPreserveVO getFcBondItemById(String id) {

        return fcBondPreserveService.getFcBondItemById(id);
    }

    /**
     * 获取审批列表数据
     */
    public DataTablesOutput<WorkFlowPersistence> getApproveDataTables(DataTablesInput dataTablesInput) {
        return fcBondPreserveService.getApproveDataTables(dataTablesInput);
    }

    /**
     * 根据网点和银行查询存出保证金的账户列表
     */
    public DataTablesOutput<FcBondAccVO> queryBondAccDataTables(DataTablesInput dataTablesInput, String corpId, String bankCode, String curId,
                                                                ICurrentUser user) {

        return fcBondPreserveService.queryBondAccDataTables(dataTablesInput, corpId, bankCode, curId, user);
    }


}
