package com.bytter.biz.erp.controller;

import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.erp.constant.CommonConstant;
import com.bytter.erp.dto.ErpNisBillheadDTO;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/erp/nisBillhead")
public class NisBillheadErpController {
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IInputBankInfoService inputBankInfoService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;

    @PostMapping("/save")
    @ResponseBody
    public void save(@RequestBody List<ErpNisBillheadDTO> list) {
        List<NisBillheadDTO> accList = init(list);
        nisBillheadService.syncBatchSave(accList);
    }

    @PostMapping("/delete")
    @ResponseBody
    public void delete(@RequestBody List<ErpNisBillheadDTO> list) {
        List<NisBillheadDTO> accList = BeanUtils.copyListProperties(list, NisBillheadDTO.class);
        this.nisBillheadService.syncBatchDelete(accList);
    }

    @PostMapping("/getVoucherStat")
    @ResponseBody
    public Map<String,Object> getVoucherStat(@RequestBody ErpNisBillheadDTO dto) {
        String voucherStat = this.nisBillheadService.getVoucherStatByErpId(dto.getErpId(), dto.getBillFrom());
        Map<String,Object> map = new HashMap<>();
        map.put("voucherStat",voucherStat);
        return map;
    }

    private List<NisBillheadDTO> init(List<ErpNisBillheadDTO> list) {
        List<NisBillheadDTO> accList = new ArrayList<>();
        for (ErpNisBillheadDTO dto : list) {
            NisBillheadDTO entity = new NisBillheadDTO();
            BeanUtils.copyNotNullProperties(dto, entity);
            // 校验单位
            if (EmptyUtils.isBlank(dto.getCorpId())) {
                CorpDTO corp = corpService.getCorpIdByName(dto.getCorpName());
                if (EmptyUtils.isEmpty(corp)) {
                    throw new RuntimeException(String.format("单位名称为[%s],不存在!", dto.getCorpName()));
                }
                entity.setCorpId(corp.getId());
            }

            // 必填项
            if (EmptyUtils.isEmpty(dto.getErpId())) {
                throw new RuntimeException("ErpID不能为空!");
            }
//            if(EmptyUtils.isEmpty(dto.getCreateBy())){
//                throw new RuntimeException(String.format("ErpID为[%s]，创建用户[createBy]不能为空", dto.getCreateBy()));
//            }
            if (EmptyUtils.isEmpty(dto.getPayMoney())) {
                throw new RuntimeException(String.format("ErpID为[%s]，付款单金额[payMoney]不能为空!", dto.getErpId()));
            }
            if (EmptyUtils.isBlank(dto.getBillFrom())) {
                throw new RuntimeException(String.format("ErpID为[%s]，单据来源[billFrom]不能为空!", dto.getErpId()));
            }
            if (EmptyUtils.isBlank(dto.getPurpose())) {
                throw new RuntimeException(String.format("ErpID为[%s]，用途[purpose]不能为空!", dto.getErpId()));
            }
            if (EmptyUtils.isEmpty(dto.getWishPayday())) {
                throw new RuntimeException(String.format("ErpID为[%s]，期望支付日期[wishPayday]不能为空!", dto.getErpId()));
            }

            // 校验开户行
            if (!EmptyUtils.isBlank(dto.getPayeeBankCode())) {
                InputBankInfoDTO btBank = inputBankInfoService.getByBankCode(dto.getPayeeBankCode());
                if (EmptyUtils.isEmpty(btBank)) {
                    throw new RuntimeException(String.format("收款账户的开户行号为[%s],不存在!", dto.getPayeeBankCode()));
                }
                entity.setPayeeBankCode(btBank.getBankCode());
                entity.setPayeeAccName(btBank.getBankName());
                // 带出省
                entity.setProv(btBank.getProv());
                // 带出市
                entity.setCity(btBank.getCity());
                // 带出银行类别
                entity.setBankTypeId(btBank.getBankTypeId());
                if (!EmptyUtils.isBlank(btBank.getBankTypeId())) {
                    // 根据银行类型带出任一接口代码
                    List<BisBifInitDTO> bifList = bisBifInitService.listBySignOrBankType("1", btBank.getBankTypeId());
                    if (!EmptyUtils.isEmptyList(bifList)) {
                        entity.setBifCode(bifList.get(0).getId());
                    }
                }
            }

            // 设置币别
            if (EmptyUtils.isBlank(entity.getCurId())) {
                String curId = currencyService.getIdByNameOrEnglishCode(entity.getCurName());
                if (EmptyUtils.isBlank(curId)) {
                    throw new RuntimeException(String.format("币别[%s],不存在!", entity.getCurName()));
                }
                entity.setCurId(curId);
            }

            // -------------默认值-------------------
            entity.setStatus(CommonConstant.STATUE_TEMP);
            accList.add(entity);
        }
        return accList;
    }
}
