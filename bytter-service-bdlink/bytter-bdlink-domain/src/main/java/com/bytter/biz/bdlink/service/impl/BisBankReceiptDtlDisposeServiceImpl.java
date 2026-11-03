package com.bytter.biz.bdlink.service.impl;
import com.bytter.biz.bdlink.entity.BisBankReceiptDtlDispose;
import com.bytter.biz.bdlink.service.BisBankReceiptDtlDisposeService;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlDisposeVO;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlDisposeService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import com.bytter.increment.api.service.IChoInnerFinanceService;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.dto.BankTypeDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 银行电子回单
 * @author jiangw
 * @since 2023/5/4 14:36
 */
@Service("bisBankReceiptDtlDisposeServiceImpl")
@DubboService
public class BisBankReceiptDtlDisposeServiceImpl implements IBisBankReceiptDtlDisposeService {
    @Autowired
    BisBankReceiptDtlDisposeService bisBankReceiptDtlDisposeService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private IChoInnerFinanceService choInnerFinanceService;
    @Override
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = bisBankReceiptDtlDisposeService.getDataTablesUnion(dataTablesInput);
        List<BisBankReceiptDtlDisposeVO> bisBankReceiptDtlDisposeVOList = dataTablesOutput.getData();
        for(BisBankReceiptDtlDisposeVO bisBankReceiptDtlDisposeVO:bisBankReceiptDtlDisposeVOList){
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(bisBankReceiptDtlDisposeVO.getBankTypeId());
            if(!EmptyUtils.isEmpty(bankTypeDTO)){
                bisBankReceiptDtlDisposeVO.setBankTypeName(bankTypeDTO.getTypeName());
            }
        }
        return dataTablesOutput;
    }
    @Override
    public BisBankReceiptDtlDisposeVO EntityById(String id) {
        BisBankReceiptDtlDisposeVO bisBankReceiptDtlDisposeVO = bisBankReceiptDtlDisposeService.EntityById(id);
        if(!EmptyUtils.isEmpty(bisBankReceiptDtlDisposeVO)){
            BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(bisBankReceiptDtlDisposeVO.getBankTypeId());
            if(!EmptyUtils.isEmpty(bankTypeDTO)){
                bisBankReceiptDtlDisposeVO.setBankTypeName(bankTypeDTO.getTypeName());
            }
        }
        return bisBankReceiptDtlDisposeVO;
    }
    @Override
    public BisBankReceiptDtlDispose get(String id) {
        return bisBankReceiptDtlDisposeService.get(id, true);
    }
    @Override
    public boolean bankTypeDate(String bankTypeId) {
        return bisBankReceiptDtlDisposeService.bankTypeDate(bankTypeId);
    }
    @Override
    public void save(BisBankReceiptDtlDispose bisBankReceiptDtlDispose, ICurrentUser user) {
        bisBankReceiptDtlDisposeService.save(bisBankReceiptDtlDispose, user);
    }
    @Override
    public void deleteEntity(BisBankReceiptDtlDispose bisBankReceiptDtlDispose) {
        bisBankReceiptDtlDisposeService.delete(bisBankReceiptDtlDispose.getId());
    }
    @Override
    public void update(BisBankReceiptDtlDispose bisBankReceiptDtlDispose, ICurrentUser user) {
        bisBankReceiptDtlDisposeService.update(bisBankReceiptDtlDispose, user);
    }
    @Override
    public void updateEntity(BisBankReceiptDtlDispose bisBankReceiptDtlDispose) {
        bisBankReceiptDtlDisposeService.updateEntity(bisBankReceiptDtlDispose);
    }
    @Override
    public String getById(String origvouchId){
        ChoInnerFinanceDTO choInnerFinance = choInnerFinanceService.getInnerDepositByBillNo(origvouchId);
        if(choInnerFinance==null){
            return null;
        }
        return choInnerFinance.getDepositRateType();
    }
}
