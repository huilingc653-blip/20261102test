package com.bytter.biz.erp.service.impl;

import com.bytter.biz.erp.base.AbstractCxfService;
import com.bytter.biz.erp.dto.ErpBankAccountDTO;
import com.bytter.biz.erp.dto.ErpBtBankAccCurDTO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.erp.constant.CommonConstant;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccCurDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import org.apache.dubbo.config.annotation.DubboReference;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuwj
 * @date 2019/8/9 13:51
 */

@WebService(serviceName = "BankAccountWs")
public class BankAccountWsServiceImpl extends AbstractCxfService implements com.bytter.erp.service.BankAccountWsService {
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

    @Override
    public String save(String params) {
        List<ErpBankAccountDTO> bankAccDTOS = JsonUtils.json2List(params, ErpBankAccountDTO.class);
        if (EmptyUtils.isEmpty(bankAccDTOS)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        //检验参数
        List<BtBankAccDTO> lists = init(bankAccDTOS, false);
        btBankAccService.syncBatchSave(lists);
        return success();
    }

    @Override
    public String update(String params) {
        List<ErpBankAccountDTO> bankAccDTOS = JsonUtils.json2List(params, ErpBankAccountDTO.class);
        if (EmptyUtils.isEmpty(bankAccDTOS)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        //检验参数
        List<BtBankAccDTO> lists = init(bankAccDTOS, true);
        btBankAccService.syncBatchUpdate(lists);
        return success();
    }

    @Override
    public String delete(String params) {
        List<BtBankAccDTO> bankAccDTOS = JsonUtils.json2List(params, BtBankAccDTO.class);
        if (EmptyUtils.isEmpty(bankAccDTOS)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        for (BtBankAccDTO bankAccDTO : bankAccDTOS) {
            if(StringUtils.isEmpty(bankAccDTO.getBankAcc()))
                throw new RuntimeException("银行账号不能为空!");
            else{
                BtBankAccDTO acc = btBankAccService.getBankAccInfo(bankAccDTO.getBankAcc());
                if(!StringUtils.isNotEmpty(acc))
                    throw new RuntimeException(String.format("银行账号为[%s]不存在!", bankAccDTO.getBankAcc()));
            }
        }
        btBankAccService.syncBatchDelete(bankAccDTOS);
        return success();
    }

    private List<BtBankAccDTO> init(List<ErpBankAccountDTO> list, boolean isEdit) {
        List<BtBankAccDTO> accList = new ArrayList<>();
        for (ErpBankAccountDTO dto : list) {
            //校验银行账号
            if (EmptyUtils.isBlank(dto.getBankAcc())) {
                throw new RuntimeException("银行账号不能为空!");
            }
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
            //校验账户名称
            if (EmptyUtils.isBlank(dto.getAccName())) {
                throw new RuntimeException("账户名称不能为空!");
            }
            //校验开户行号
            if (EmptyUtils.isBlank(dto.getBankCode())) {
                throw new RuntimeException("开户行号不能为空!");
            }
            //校验开户日期
            if (!StringUtils.isNotEmpty(dto.getRegDate())) {
                throw new RuntimeException("开户日期不能为空!");
            }
            //校验账户类别
            if (!StringUtils.isNotEmpty(dto.getAccType())) {
                throw new RuntimeException("账户类别不能为空!");
            }
            // 校验单位
            if (!EmptyUtils.isBlank(dto.getCorpName())) {
                CorpDTO corp = corpService.getCorpIdByName(acc.getCorpName());
                if (EmptyUtils.isEmpty(corp)) {
                    throw new RuntimeException(String.format("单位名称为[%s],不存在!", acc.getCorpName()));
                }
                acc.setCorpId(corp.getId());
            }else{
                throw new RuntimeException("单位名称不能为空!");
            }

            // 校验开户行
            if (!EmptyUtils.isBlank(dto.getBankCode())) {
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
            //是否上线
            acc.setIsOnline(EmptyUtils.isBlank(acc.getIsOnline()) ? "1" : acc.getIsOnline());
            //是否有效
            acc.setValidSign(EmptyUtils.isBlank(acc.getValidSign()) ? "1" : acc.getValidSign());
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
                if (!EmptyUtils.isBlank(erpCurDTO.getCurName())) {
                    String curId = currencyService.getIdByNameOrEnglishCode(erpCurDTO.getCurName());
                    if(EmptyUtils.isBlank(curId)){
                        throw new RuntimeException(String.format("币别[%s],不存在!", erpCurDTO.getCurName()));
                    }
                    curDTO.setCurId(curId);
                }else{
                    throw new RuntimeException("币别不能为空!");
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
