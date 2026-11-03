package com.bytter.credential.domain.international.service.impl;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.credential.api.dto.GuaContractInternationalDTO;
import com.bytter.credential.api.dto.LcBillArrivalDTO;
import com.bytter.credential.api.service.ILcEnrolService;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.service.BillArrivalService;
import com.bytter.credential.domain.international.service.CreatedCreditService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.credential.domain.international.service.ReceiveLetterService;
import com.bytter.credential.domain.international.service.SupplementCreditService;
import com.bytter.credential.domain.international.vo.GuaContractInternationalVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-07-24 11:55
 * @Version 1.0
 */
@Transactional
@Service("LcEnrolServiceImpl")
@DubboService
public class LcEnrolServiceImpl implements ILcEnrolService {
    @Autowired
    private CreatedCreditService createdCreditService;
    @Autowired
    private BillArrivalService billArrivalService;
    @Autowired
    private SupplementCreditService lcenrolseService;
    @Autowired
    private ReceiveLetterService receiveLetterService;
    @Override
    public void updateLcGuaById(String busId) {
        createdCreditService.updateLcGuaById(busId);
    }
    @Override
    public void deleteLcGuaById(String busId) {
        createdCreditService.deleteLcGuaById(busId);
    }
    @Override
    public void addBillArrivalMark(String id) {
        LcBillArrival arrival = billArrivalService.get(id);
        arrival.setBankFlowSign(CommonConstant.YES);
        billArrivalService.updateEntity(arrival);
    }
    @Override
    public void deletBillArrivalMark(String id) {
        LcBillArrival arrival = billArrivalService.get(id);
        arrival.setBankFlowSign(CommonConstant.NO);
        billArrivalService.updateEntity(arrival);
    }
    @Override
    public LcBillArrivalDTO getBillArrivalById(String id) {
        LcBillArrival lcBillArrival = billArrivalService.get(id);
        LcBillArrivalDTO lcBillArrivalDTO = new LcBillArrivalDTO();
        BeanUtils.copyProperties(lcBillArrival, lcBillArrivalDTO);
        return lcBillArrivalDTO;
    }
    @Override
    public List<String> getLcEnrolIdByRat(){
        return receiveLetterService.getIdByRat();
    }
    @Override
    public List<GuaContractInternationalDTO> getGuaForInternational() {
        return receiveLetterService.getGuaForInternational();
    }
}
