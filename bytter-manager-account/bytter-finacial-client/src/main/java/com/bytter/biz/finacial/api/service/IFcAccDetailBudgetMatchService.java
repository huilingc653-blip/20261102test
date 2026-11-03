package com.bytter.biz.finacial.api.service;
import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 内部账户流水
 * @date 2017-06-23 19:36:12
 */
public interface IFcAccDetailBudgetMatchService {
    /**
     * 查询内部账号流水列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 根据id查找明细信息
     *
     * @param id
     * @return
     */
    public FcAccDetailBudgetMatchVO getInfoById(String id);

    public FcAccDetailBudgetMatchVO getItemInfoById(String id);
}
