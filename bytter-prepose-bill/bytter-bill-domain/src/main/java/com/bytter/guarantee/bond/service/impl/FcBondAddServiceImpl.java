package com.bytter.guarantee.bond.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.guarantee.api.IFcBondAddService;
import com.bytter.guarantee.bond.entity.FcBondAdd;
import com.bytter.guarantee.bond.service.FcBondAddService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.vo.FcBondAccVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fcBondAddServiceImpl")
@DubboService
public class FcBondAddServiceImpl implements IFcBondAddService {
    @Autowired
    private FcBondAddService fcBondAddService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 查询追加保证金表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FcBondAddDTO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        DataTablesOutput<FcBondAddDTO> dataTablesUnion = fcBondAddService.getDataTablesUnion(dataTablesInput, user);
        for (FcBondAddDTO data : dataTablesUnion.getData()) {
            if(data.getStatus()!=null){
                ConfigDTO config = configService.getConfig("common.status", data.getStatus().toString());
                data.setStatusName(config.getValue());
            }
        }
        return dataTablesUnion;
    }

    /**
     * 查询存出保证金的账户列表
     */
    public DataTablesOutput<FcBondAccVO> queryBondAccDataTables(DataTablesInput dataTablesInput, ICurrentUser user) {

        return fcBondAddService.queryBondAccDataTables(dataTablesInput, user);
    }

    /**
     * 查询待审批追加保证金列表
     */
    public DataTablesOutput<WorkFlowPersistence> getApproveDataTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(FcBondConstant.FC_FCBONDADD_FLOW,dataTablesInput);
        List<String> businessIdList = workFlowBusiness.getBusinessIdList();
        if (CollectionUtil.isEmpty(businessIdList)) {
            return new DataTablesOutput();
        }
        String businessSql = SqlUtil.getInSql(
                workFlowBusiness.getBusinessIdList(), "m.id", true);
        DataTablesOutput dp = fcBondAddService.getApproveDataTables(dataTablesInput, businessSql);
        return workFlowBusiness.merge(dp);
    }

    /**
     * 查询历史追加明细
     */
    public List<FcBondAddDTO> queryAddHis(String bondId) {
        return fcBondAddService.queryAddHis(bondId);
    }

    /**
     * 根据id查询追加保证金的详细信息
     */
    public FcBondAddDTO queryAddVOById(String id) {
        FcBondAddDTO vo= fcBondAddService.queryAddVOById(id);
        BtBankAccDTO acc= bankAccService.getBankAccInfoByBankAcc(vo.getOutBankAcc());
        if(null != acc){
           vo.setIsOnline(acc.getIsOnline());
        }
        return vo;
    }

    /**
     * 改变追加保证金的状态为删除
     */
    public void deleteBondAdd(String id, ICurrentUser user) {
        fcBondAddService.deleteBondAdd(id, user);
    }

    public FcBondAddDTO saveEntity(FcBondAddDTO fcBondAddDTO) {
        FcBondAdd en = new FcBondAdd();
        BeanUtils.copyProperties(fcBondAddDTO, en);
        fcBondAddService.saveEntity(en);
        fcBondAddDTO.setId(en.getId());
        return fcBondAddDTO;
    }
    public void updateEntity(FcBondAddDTO fcBondAdd){
        FcBondAdd en = new FcBondAdd();
        BeanUtils.copyProperties(fcBondAdd, en);
        fcBondAddService.updateEntity(en);
    }
    /**
     * <br>
     * <b>功能：</b> 删除头寸调拨单打回保证金追加 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/8/18 0018 <br>
     */
    public void tempAddBond(String id, ICurrentUser user) {
        fcBondAddService.tempAddBond(id, user);
    }
    public String getSequenceBillNO() {
       return fcBondAddService.getSequenceBillNO();
    }

    @Override
    public DataTablesOutput<FcBondAddDTO> addHisDatatable(DataTablesInput dataTablesInput, ICurrentUser user) {
        return fcBondAddService.addHisDatatable(dataTablesInput,null);
    }

}
