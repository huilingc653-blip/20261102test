package com.bytter.system.api;
import com.bytter.system.api.dto.InputBankInfoDTO;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/6/30.
 */
public interface IInputBankInfoService {
    /**
     * 通过银行代码查找联行号
     *
     * @param bankCode
     */
    InputBankInfoDTO getByBankCode(String bankCode);
    /**
     * 通过银行名称查找联行号
     *
     * @param bankName
     */
    InputBankInfoDTO getByBankName(String bankName);
    /**
     * 通过银行代码查找联行号（导入专用，不关联银行类型表）
     *
     * @param bankCode
     * @author zuoy 2018-07-13
     */
    InputBankInfoDTO getByBankCodeAndImport(String bankCode);
    /**
     * 通过银行代码查找联行号（导入专用，不关联银行类型表）
     *
     * @param bankName
     * @author zuoy 2018-07-13
     */
    InputBankInfoDTO getByBankNameAndImport(String bankName);
    /**
     * 根据银行代码集合查询联行号信息，包括省市信息
     *
     * @param bankCodes
     * @return 联行信息
     */
    Map<String, InputBankInfoDTO> getBankCodeInfoByBankCodes(List<String> bankCodes);
    /**
     * 获取校验联行号数据
     *
     * @param bankCodes  联行号
     * @param bankPrefix 联行号前三位
     * @return Map<bankCode, BankInfo> map集合数据
     */
    Map<String, InputBankInfoDTO> getBankListCheck(List<String> bankCodes, String bankPrefix);
    
    List<InputBankInfoDTO> getByBankCodesList(List<String> bankCodes);
    
    List<InputBankInfoDTO> getByBankNamesList(List<String> bankNames);

    List<InputBankInfoDTO> getBankCodeInfoByBankTypeIds(List<String> bankTypeIds);
}
