package com.bytter.biz.finacial.inneraccount.service.impl;
import com.bytter.biz.finacial.api.service.IFcAccDetailBudgetMatchService;
import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.biz.finacial.inneraccount.service.FcAccDetailBudgetMatchService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@DubboService
@Service("fcAccDetailBudgetMatchServiceImpl")
public class FcAccDetailBudgetMatchServiceImpl implements IFcAccDetailBudgetMatchService {
    @Autowired
    private FcAccDetailBudgetMatchService fcAccDetailBudgetMatchService;
    /**
     * 查询内部账号流水列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        return fcAccDetailBudgetMatchService.getDataTablesUnion(dataTablesInput, user);
    }
    /**
     * 根据id查找明细信息
     *
     * @param id
     * @return
     */
    public FcAccDetailBudgetMatchVO getInfoById(String id) {
        return fcAccDetailBudgetMatchService.getInfoById(id);
    }

    @Override
    public FcAccDetailBudgetMatchVO getItemInfoById(String id) {
        return fcAccDetailBudgetMatchService.getItemInfoById(id);
    }
}
