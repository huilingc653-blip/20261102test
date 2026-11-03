package com.bytter.system.api;
import com.bytter.system.api.dto.BankTypeByAccDTO;
import com.bytter.system.api.dto.BankTypeCustomDTO;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BankTypeOtherCustomDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Created by zhangxb on 2017/5/22 0022.
 */
public interface IBankTypeService {
    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    BankTypeDTO getObjectById(String id);
    /**
     * 根据有效标志过滤
     *
     * @param validSign 可空
     * @return
     */
    List<BankTypeDTO> listByValidSign(String validSign);
    /**
     * 根据有效标志，人行号前缀过滤
     *
     * @param validSign  可空
     * @param bankPrefix
     * @return
     */
    List<BankTypeDTO> listBySignOrPrefix(String validSign, String bankPrefix);
    /**
     * 根据银行类别名称查找
     *
     * @param typeName
     * @return
     */
    BankTypeDTO getByTypeName(String typeName);
    /**
     * 根据银行类型名称获取银行类型
     *
     * @return
     */
    List<BankTypeDTO> getByBankTypeNames(Set<String> bankTypeNames);
    List<BankTypeDTO> getBankTypeNameAndBankTypeByIds(List<String> ids);

    /**
     * 根据银行类型ID集合获取银行类型名称。
     *
     * @param ids 银行类型ID集合。
     * @return 银行类型名称Map。
     */
    Map<String, BankTypeDTO> listBankTypeMapByIds(List<String> ids);
    /**
     * 根据id查找银行类别名称
     * @param ids
     * @return
     */
    List<String> getBankTypeNameByIds(List<String> ids);
    /**
     * 根据条件查询银行类型ID。
     *
     * @param params bankCode，pledgeeAcc。
     * @param interiorApply 是否内部申请。
     * @return 银行类型编号。
     */
    public String getBankTypeId(Map<String,Object> params, Integer interiorApply);
    Map<String, BankTypeDTO> getBankTypeMap();
    /**
     * 通过账号查询，账号类型，银行类型，开户类型等信息
     * @return
     */
    List<BankTypeByAccDTO> listBankTypeByAcc(List<String> accList);

    /**
     * 根据银行类型名称获取银行类型
     * @return
     */
    List<BankTypeDTO> getByBankType(String bankType);


    /**
     * 根据上送代码获取信息
     * @param bankType
     * @param bifCode
     * @param customType
     * @param customType
     * @return
     */
    BankTypeCustomDTO getByBankBankTypeCustom(String bankType, String bifCode, String customCode, String customType);

    /**
     * 获取银行类型自定义配置
     *
     * @param bankType
     * @param bifCode
     * @param applayScope
     * @param countryId
     * @param remitType
     * @return
     */
    BankTypeOtherCustomDTO getByBankTypeOtherCustom(String bankType, String bifCode, String applayScope, String countryId, String remitType);
}
