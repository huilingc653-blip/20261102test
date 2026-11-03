package com.bytter.biz.erp.controller;


import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.erp.constant.CommonConstant;
import com.bytter.erp.dto.ErpBankAccountDTO;
import com.bytter.erp.dto.ErpBtBankAccCurDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/erp/bankaccount")
public class BankAccountErpController {
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
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
    public void save(@RequestBody List<ErpBankAccountDTO> list) {
        List<BtBankAccDTO> accList = init(list, false);
        btBankAccService.syncBatchSave(accList);
    }

    @PostMapping("/update")
    @ResponseBody
    public void update(@RequestBody List<ErpBankAccountDTO> list) {
        List<BtBankAccDTO> accList = init(list, true);
        this.btBankAccService.syncBatchUpdate(accList);
    }

    @PostMapping("/delete")
    @ResponseBody
    public void delete(@RequestBody List<ErpBankAccountDTO> list) {
        List<BtBankAccDTO> accList = BeanUtils.copyListProperties(list, BtBankAccDTO.class);
        this.btBankAccService.syncBatchDelete(accList);
    }

    private List<BtBankAccDTO> init(List<ErpBankAccountDTO> list, boolean isEdit) {
        List<BtBankAccDTO> accList = new ArrayList<>();
        for (ErpBankAccountDTO dto : list) {
            BtBankAccDTO acc = btBankAccService.getBankAccInfo(dto.getBankAcc());
            if (isEdit) {
                if (EmptyUtils.isEmpty(acc)) {
                    throw new RuntimeException(String.format("银行账号为[%s],不存在!", dto.getBankAcc()));
                }
            } else {
                if (!EmptyUtils.isEmpty(acc)) {
                    throw new RuntimeException("已存在相同的账号");
                }
                acc = new BtBankAccDTO();
            }
            BeanUtils.copyNotNullProperties(dto, acc);

            // 校验单位
            if (EmptyUtils.isBlank(acc.getCorpId())) {
                CorpDTO corp = corpService.getCorpIdByName(acc.getCorpName());
                if (EmptyUtils.isEmpty(corp)) {
                    throw new RuntimeException(String.format("单位名称为[%s],不存在!", acc.getCorpName()));
                }
                acc.setCorpId(corp.getId());
            }

            // 校验开户行
            if (!EmptyUtils.isBlank(acc.getBankCode())) {
                InputBankInfoDTO btBank = inputBankInfoService.getByBankCode(acc.getBankCode());
                if (EmptyUtils.isEmpty(btBank)) {
                    throw new RuntimeException(String.format("开户行号为[%s],不存在!", acc.getBankCode()));
                }
                acc.setBankCode(acc.getBankCode());
                acc.setBankName(btBank.getBankName());
                // 带出省
                acc.setProv(btBank.getProv());
                // 带出市
                acc.setCity(btBank.getCity());
                // 带出银行类别
                acc.setBankTypeId(btBank.getBankTypeId());
                if (!EmptyUtils.isBlank(btBank.getBankTypeId())) {
                    // 根据银行类型带出任一接口代码
                    List<BisBifInitDTO> bifList = bisBifInitService.listBySignOrBankType("1", btBank.getBankTypeId());
                    if (!EmptyUtils.isEmptyList(bifList)) {
                        acc.setBifCode(bifList.get(0).getId());
                    }
                }
            }


            // -------------默认值-------------------
            acc.setIsOnline(EmptyUtils.isBlank(acc.getIsOnline()) ? "1" : acc.getIsOnline());
            // 是否资金池账号
            acc.setIsCapitalPool(EmptyUtils.isBlank(acc.getIsCapitalPool()) ? "0" : acc.getIsCapitalPool());
            // 流通方式-线上
            acc.setCirculationMode(EmptyUtils.isBlank(acc.getCirculationMode()) ? "1" : acc.getCirculationMode());
            acc.setStatus(CommonConstant.STATUE_CHECK);
            // 票据
            acc.setElectricBill(EmptyUtils.isBlank(acc.getElectricBill()) ? "0" : acc.getElectricBill());

            List<BtBankAccCurDTO> btBankAccCurDTOList = new ArrayList<>();
            for (ErpBtBankAccCurDTO erpCurDTO : dto.getBankAccCurList()) {
                BtBankAccCurDTO curDTO = new BtBankAccCurDTO();
                BeanUtils.copyNotNullProperties(erpCurDTO, curDTO);
                // 设置币别
                if (EmptyUtils.isBlank(curDTO.getCurId())) {
                    String curId = currencyService.getIdByNameOrEnglishCode(erpCurDTO.getCurName());
                    if(EmptyUtils.isBlank(curId)){
                        throw new RuntimeException(String.format("币别[%s],不存在!", erpCurDTO.getCurName()));
                    }
                    curDTO.setCurId(curId);
                }
                // 设置父账户
                if (!EmptyUtils.isBlank(erpCurDTO.getPrentBankAcc())) {
                    BtBankAccDTO parentAccount = btBankAccService.getBankAccByBankAccAndCurId(erpCurDTO.getPrentBankAcc(), curDTO.getCurId());
                    if(EmptyUtils.isEmpty(parentAccount)){
                        throw new RuntimeException(String.format("父账号为[%s],不存在!", erpCurDTO.getPrentBankAcc()));
                    }
                    curDTO.setPrentAccId(parentAccount.getId());
                }
                btBankAccCurDTOList.add(curDTO);
            }
            acc.setBtBankAccCurDTOList(btBankAccCurDTOList);
            accList.add(acc);
        }
        return accList;
    }
}
