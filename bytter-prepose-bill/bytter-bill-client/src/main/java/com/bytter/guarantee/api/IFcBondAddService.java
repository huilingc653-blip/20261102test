package com.bytter.guarantee.api;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.vo.FcBondAccVO;

import java.util.List;

public interface IFcBondAddService {

    /**
     * 查询追加保证金表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FcBondAddDTO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) ;

    /**
     * 查询存出保证金的账户列表
     */
    public DataTablesOutput<FcBondAccVO> queryBondAccDataTables(DataTablesInput dataTablesInput, ICurrentUser user);
    /**
     * 查询待审批追加保证金列表
     */

    public DataTablesOutput<WorkFlowPersistence> getApproveDataTables(DataTablesInput dataTablesInput) ;
    /**
     * 查询历史追加明细
     */
    public List<FcBondAddDTO> queryAddHis(String bondId) ;
    /**
     * 根据id查询追加保证金的详细信息
     */
    public FcBondAddDTO queryAddVOById(String id) ;
    /**
     * 改变追加保证金的状态为删除
     */
    public void deleteBondAdd(String id, ICurrentUser user) ;

    FcBondAddDTO saveEntity(FcBondAddDTO fcBondAddDTO) ;
    public void updateEntity(FcBondAddDTO fcBondAdd);
    /**
     * <br>
     * <b>功能：</b> 删除头寸调拨单打回保证金追加 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/8/18 0018 <br>
     */
    public void tempAddBond(String id, ICurrentUser user);
    String getSequenceBillNO();

    DataTablesOutput<FcBondAddDTO> addHisDatatable(DataTablesInput dataTablesInput, ICurrentUser user) ;
}
