package com.bytter.basic.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.basic.service.BtInputBankInfoService;
import com.bytter.basic.vo.BtInputBankInfoVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.InputBankInfoDTO;
/**
 * Created by zhoul on 2017/6/30.
 */
@Transactional
@Service("inputBankInfoI")
@DubboService
public class InputBankInfoImpl implements IInputBankInfoService {
    @Autowired
    private BtInputBankInfoService inputBankInfoService;
    @Override
    public InputBankInfoDTO getByBankCode(String bankCode) {
        return this.getByInputBankInfoDTO(bankCode, null);
    }
    @Override
    public InputBankInfoDTO getByBankName(String bankName) {
        return this.getByInputBankInfoDTO(null, bankName);
    }
    @Override
    public InputBankInfoDTO getByBankCodeAndImport(String bankCode) {
        BtInputBankInfoVO inputBankInfo = this.inputBankInfoService.getByBankCodeOrBankNameAndImport(bankCode, null);
        if (EmptyUtils.isEmpty(inputBankInfo)) {
            return null;
        }
        InputBankInfoDTO dto = new InputBankInfoDTO();
        BeanUtils.copyNotNullProperties(inputBankInfo, dto);
        return dto;
    }
    @Override
    public InputBankInfoDTO getByBankNameAndImport(String bankName) {
        BtInputBankInfoVO inputBankInfo = this.inputBankInfoService.getByBankCodeOrBankNameAndImport(null, bankName);
        if (EmptyUtils.isEmpty(inputBankInfo)) {
            return null;
        }
        InputBankInfoDTO dto = new InputBankInfoDTO();
        BeanUtils.copyNotNullProperties(inputBankInfo, dto);
        return dto;
    }
    private InputBankInfoDTO getByInputBankInfoDTO(String bankCode, String bankName) {
        BtInputBankInfoVO inputBankInfo = inputBankInfoService.getByBankCodeOrBankName(bankCode, bankName);
        if (EmptyUtils.isEmpty(inputBankInfo)) {
            return null;
        }
        InputBankInfoDTO dto = new InputBankInfoDTO();
        BeanUtils.copyNotNullProperties(inputBankInfo, dto);
        return dto;
    }
    /**
     * 根据银行代码集合查询联行号信息，包括省市信息
     *
     * @param bankCodes
     * @return 联行信息
     */
    @Override
    public Map<String, InputBankInfoDTO> getBankCodeInfoByBankCodes(List<String> bankCodes) {
        if (EmptyUtils.isEmpty(bankCodes)) {
            return null;
        }
        List<InputBankInfoDTO> inputBankInfoList = this.inputBankInfoService.getBankCodeInfoByBankCodes(bankCodes);
        if (EmptyUtils.isEmpty(inputBankInfoList)) {
            return null;
        }
        Map<String, InputBankInfoDTO> map = new HashMap<>();
        for (InputBankInfoDTO dto : inputBankInfoList) {
            map.put(dto.getBankCode(), dto);
        }
        return map;
    }
    @Override
    public Map<String, InputBankInfoDTO> getBankListCheck(List<String> bankCodes, String bankPrefix) {
        if (EmptyUtils.isEmpty(bankCodes)) {
            return null;
        }
        List<InputBankInfoDTO> inputBankInfoList = this.inputBankInfoService.getBankListCheck(bankCodes, bankPrefix);
        if (EmptyUtils.isEmpty(inputBankInfoList)) {
            return null;
        }
        return inputBankInfoList.stream().collect(Collectors.toMap(InputBankInfoDTO::getBankCode, Function.identity()));
    }
	@Override
	public List<InputBankInfoDTO> getByBankCodesList(List<String> bankCodes) {
		return inputBankInfoService.getBankCodeInfoByBankCodes(bankCodes);
	}
	@Override
	public List<InputBankInfoDTO> getByBankNamesList(List<String> bankNames) {
		 	int size = 0;
		 	List<InputBankInfoDTO> listAll = new ArrayList<InputBankInfoDTO>();
		 	List<String> names = new ArrayList<String>();
	        for (String name : bankNames) {
	            if (size % 500 == 0) {
	            	List<InputBankInfoDTO> list = inputBankInfoService.getBankCodeInfoByBankNames(names);
	            	if(CollectionUtils.isNotEmpty(list)) {
	            		listAll.addAll(list);
	            		size = 0;
	            		names.clear();
	            	}
	            }
	            names.add((String)name);
	            size++;
	        }
	        List<InputBankInfoDTO> listOut = inputBankInfoService.getBankCodeInfoByBankNames(bankNames);
	        listAll.addAll(listOut);
	        return listAll;
	}

    public List<InputBankInfoDTO> getBankCodeInfoByBankTypeIds(List<String> bankTypeIds){
        return inputBankInfoService.getBankCodeInfoByBankTypeIds(bankTypeIds);
    }
}
