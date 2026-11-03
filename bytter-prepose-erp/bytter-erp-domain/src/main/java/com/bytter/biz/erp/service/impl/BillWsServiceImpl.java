package com.bytter.biz.erp.service.impl;

import com.bytter.bill.api.bill.dto.AbBillCollectionApplyDTO;
import com.bytter.bill.api.bill.dto.AbBillDiscountApplyDTO;
import com.bytter.bill.api.bill.dto.AbBillEndorseDTO;
import com.bytter.bill.api.bill.dto.AbBillPledgeApplyDTO;
import com.bytter.bill.api.bill.dto.AbEmitBillDTO;
import com.bytter.bill.api.bill.dto.AbReceiveBillDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.erp.base.AbstractCxfService;
import com.bytter.biz.erp.dto.ErpAbBillCollectionApplyDTO;
import com.bytter.biz.erp.dto.ErpAbBillDiscountApplyDTO;
import com.bytter.biz.erp.dto.ErpAbBillEndorseDTO;
import com.bytter.biz.erp.dto.ErpAbBillPledgeApplyDTO;
import com.bytter.biz.erp.dto.ErpAbEmitBillDTO;
import com.bytter.biz.erp.dto.ErpAbReceiveBillDTO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import org.apache.dubbo.config.annotation.DubboReference;

import javax.jws.WebService;
import java.util.List;

/**
 * @author yuwj
 */

@WebService(serviceName = "BillWs")
public class BillWsServiceImpl extends AbstractCxfService implements com.bytter.erp.service.BillWsService {
    @DubboReference(check = false)
    private IAccebillService accebillService;

    @Override
    public String emitSave(String params) {
        ErpAbEmitBillDTO dto = JsonUtils.parseObject(params,ErpAbEmitBillDTO.class);
        if (EmptyUtils.isEmpty(dto)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        AbEmitBillDTO abEmitBillDTO = new AbEmitBillDTO();
        BeanUtils.copyNotNullProperties(dto, abEmitBillDTO);

        accebillService.saveAbEmitBillERP(abEmitBillDTO);
        return success();
    }

    @Override
    public String emitCashingConfirmation(String params) {
        List<ErpAbEmitBillDTO> list = JsonUtils.json2List(params,ErpAbEmitBillDTO.class);
        if (EmptyUtils.isEmpty(list)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        List<AbEmitBillDTO> emitList = BeanUtils.copyListProperties(list, AbEmitBillDTO.class);
        this.accebillService.cashingConfirmationERP(emitList);
        return success();
    }

    @Override
    public String receiveBillSave(String params) {
        ErpAbReceiveBillDTO dto = JsonUtils.parseObject(params, ErpAbReceiveBillDTO.class);
        if (EmptyUtils.isEmpty(dto)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        AbReceiveBillDTO abReceiveBillDTO = new AbReceiveBillDTO();
        BeanUtils.copyNotNullProperties(dto, abReceiveBillDTO);

        accebillService.saveAbReceiveBillERP(abReceiveBillDTO);
        return success();
    }

    @Override
    public String endorse(String params) {
        List<ErpAbBillEndorseDTO> list = JsonUtils.json2List(params,ErpAbBillEndorseDTO.class);
        List<AbBillEndorseDTO> endorseList = BeanUtils.copyListProperties(list, AbBillEndorseDTO.class);
        this.accebillService.endorseERP(endorseList);
        return success();
    }

    @Override
    public String pledgeApply(String params) {
        List<ErpAbBillPledgeApplyDTO> list = JsonUtils.json2List(params,ErpAbBillPledgeApplyDTO.class);
        List<AbBillPledgeApplyDTO> pledgeList = BeanUtils.copyListProperties(list, AbBillPledgeApplyDTO.class);
        this.accebillService.pledgeApplyERP(pledgeList);
        return success();
    }

    @Override
    public String discountApply(String params) {
        List<ErpAbBillDiscountApplyDTO> list = JsonUtils.json2List(params,ErpAbBillDiscountApplyDTO.class);
        List<AbBillDiscountApplyDTO> discountList = BeanUtils.copyListProperties(list, AbBillDiscountApplyDTO.class);
        this.accebillService.discountApplyERP(discountList);
        return success();
    }

    @Override
    public String collectionApply(String params) {
        List<ErpAbBillCollectionApplyDTO> list = JsonUtils.json2List(params,ErpAbBillCollectionApplyDTO.class);
        List<AbBillCollectionApplyDTO> collectionList = BeanUtils.copyListProperties(list, AbBillCollectionApplyDTO.class);
        this.accebillService.collectionApplyERP(collectionList);
        return success();
    }

}
