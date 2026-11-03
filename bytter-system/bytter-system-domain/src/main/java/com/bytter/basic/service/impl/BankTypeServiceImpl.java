package com.bytter.basic.service.impl;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.basic.dao.BtBankTypeDao;
import com.bytter.basic.entity.BankType;
import com.bytter.basic.service.BtBankTypeCustomService;
import com.bytter.basic.service.BtBankTypeOtherCustomService;
import com.bytter.basic.service.BtBankTypeService;
import com.bytter.basic.vo.BankTypeCustomVO;
import com.bytter.basic.vo.BankTypeOtherCustomVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.BankTypeByAccDTO;
import com.bytter.system.api.dto.BankTypeCustomDTO;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BankTypeOtherCustomDTO;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author zhangxb
 * @since 2017/5/22
 */
@Transactional
@Service("bankTypeServiceI")
@DubboService
public class BankTypeServiceImpl implements IBankTypeService {
    @Autowired
    private BtBankTypeService btBankTypeService;
    @Autowired
    private BtBankTypeDao btBankTypeDao;


    @Autowired
    private BtBankTypeCustomService btBankTypeCustomService;

    @Autowired
    private BtBankTypeOtherCustomService btBankTypeOtherCustomService;

    @Autowired
    private Ii18nService i18nServiceI;
    @Override
    public BankTypeDTO getObjectById(String id) {
        if(EmptyUtils.isEmpty(id)){
            return null;
        }
        BankType bankType = btBankTypeService.findById(id);
        BankTypeDTO bankTypeDTO = new BankTypeDTO();
        if (!EmptyUtils.isEmpty(bankType)) {
            BeanUtils.copyNotNullProperties(bankType, bankTypeDTO);
        }
        return bankTypeDTO;
    }
    /**
     * 根据有效标志过滤
     *
     * @param validSign
     * @return
     */
    @Override
    public List<BankTypeDTO> listByValidSign(String validSign) {
        List<BankType> bankTypeList = btBankTypeService.getListByValidSign(validSign);
        List<BankTypeDTO> list = BeanUtils.copyListProperties(bankTypeList, BankTypeDTO.class);
        return list;
    }
    /**
     * 根据有效标志，人行号前缀过滤
     *
     * @param validSign
     * @param bankPrefix
     * @return
     */
    @Override
    public List<BankTypeDTO> listBySignOrPrefix(String validSign, String bankPrefix) {
        List<BankType> bankTypeList = btBankTypeService.listBySignOrPrefix(validSign, bankPrefix);
        List<BankTypeDTO> list = BeanUtils.copyListProperties(bankTypeList, BankTypeDTO.class);
        return list;
    }
    /**
     * 根据银行类别名称查找
     *
     * @param typeName
     * @return
     */
    @Override
    public BankTypeDTO getByTypeName(String typeName) {
        List<BankType> bankTypeList = this.btBankTypeService.getByTypeName(typeName);
        BankTypeDTO bankTypeDTO = null;
        if (!EmptyUtils.isEmptyList(bankTypeList)) {
            BankType bankType = bankTypeList.get(0);
            bankTypeDTO = new BankTypeDTO();
            BeanUtils.copyNotNullProperties(bankType, bankTypeDTO);
        }
        return bankTypeDTO;
    }
    @Override
    public List<BankTypeDTO> getByBankTypeNames(Set<String> bankTypeNames) {
        List<BankType> bankTypeList = this.btBankTypeService.getByBankTypeNames(bankTypeNames);
        if (EmptyUtils.isEmptyList(bankTypeList)) {
            return null;
        }
        return BeanUtils.copyListProperties(bankTypeList, BankTypeDTO.class);
    }
    @Override
    public List<BankTypeDTO> getBankTypeNameAndBankTypeByIds(List<String> ids) {
        List<BankType> bankTypeList = this.btBankTypeService.getBankTypeNameAndBankTypeByIds(ids);
        if (EmptyUtils.isEmptyList(bankTypeList)) {
            return null;
        }
        return BeanUtils.copyListProperties(bankTypeList, BankTypeDTO.class);
    }

    @Override
    public Map<String, BankTypeDTO> listBankTypeMapByIds(List<String> ids) {
        if (EmptyUtils.isEmpty(ids)) {
            return null;
        }
        List<BankTypeDTO> bankTypeList = getBankTypeNameAndBankTypeByIds(ids);
        if (EmptyUtils.isEmpty(bankTypeList)) {
            return null;
        }
        return bankTypeList.stream().collect(Collectors.toMap(BankTypeDTO::getId, Function.identity()));
    }

    @Override
    public List<String> getBankTypeNameByIds(List<String> ids) {
        return btBankTypeService.getBankTypeNameByIds(ids);
    }
    @Override
    public String getBankTypeId(Map<String, Object> params, Integer interiorApply) {
        return btBankTypeService.getBankTypeId(params, interiorApply);
    }
    @Override
    public Map<String, BankTypeDTO> getBankTypeMap() {
        List<BankType> list = this.btBankTypeService.listAll();
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<BankTypeDTO> resultList = BeanUtils.copyListProperties(list, BankTypeDTO.class);
        return resultList.stream().collect(Collectors.toMap(BankTypeDTO::getId, Function.identity()));
    }
    @Override
    public List<BankTypeByAccDTO> listBankTypeByAcc(List<String> accList){
        if (CollectionUtils.isEmpty(accList)) {
            return null;
        }
       return btBankTypeDao.listBankTypeByAcc(accList);
    }

    @Override
    public List<BankTypeDTO> getByBankType(String bankType) {
        List<BankTypeDTO> result = Lists.newArrayList();
        BankType bankTypeDB = btBankTypeService.findByType(bankType);
        if (ObjectUtil.isNotEmpty(bankTypeDB)){
            BankTypeDTO bankTypeDTO = new BankTypeDTO();
            BeanUtils.copyNotNullProperties(bankTypeDB, bankTypeDTO);
            result.add(bankTypeDTO);
        }
        return result;
    }



    /**
     * 根据上送代码获取信息
     * @param bankType
     * @param bifCode
     * @param customType
     * @param customType
     * @return
     */
    @Override
    public BankTypeCustomDTO getByBankBankTypeCustom(String bankType, String bifCode, String customCode, String customType) {
        BankTypeCustomVO bankTypeCustom = btBankTypeCustomService.getByBankTypeCustom(bankType, bifCode, customCode, customType);
        BankTypeCustomDTO bankTypeCustomDTO = new BankTypeCustomDTO();
        if (bankTypeCustom != null) {
            BeanUtils.copyNotNullProperties(bankTypeCustom, bankTypeCustomDTO);
        }
        return bankTypeCustomDTO;
    }

    @Override
    public BankTypeOtherCustomDTO getByBankTypeOtherCustom(String bankType, String bifCode, String applayScope, String countryId, String remitType) {
        BankTypeOtherCustomVO bankTypeOtherCustom = btBankTypeOtherCustomService.getByBankTypeOtherCustom(bankType, bifCode, applayScope, countryId, remitType);
        BankTypeOtherCustomDTO bankTypeOtherCustomDTO = new BankTypeOtherCustomDTO();
        if (bankTypeOtherCustom != null) {
            BeanUtils.copyNotNullProperties(bankTypeOtherCustom, bankTypeOtherCustomDTO);
        }
        bankTypeOtherCustomDTO.setNeedField(StringUtils.isBlank(bankTypeOtherCustomDTO.getNeedField())?"":bankTypeOtherCustomDTO.getNeedField());
        bankTypeOtherCustomDTO.setRequiredField(StringUtils.isBlank(bankTypeOtherCustomDTO.getRequiredField())?"":bankTypeOtherCustomDTO.getRequiredField());
        return bankTypeOtherCustomDTO;
    }
}
