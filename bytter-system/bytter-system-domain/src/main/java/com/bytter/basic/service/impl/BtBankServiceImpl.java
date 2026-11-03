package com.bytter.basic.service.impl;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.basic.dao.BtInputBankInfoDao;
import com.bytter.basic.entity.BtBank;
import com.bytter.basic.service.BtBankService;
import com.bytter.basic.service.BtInputBankInfoService;
import com.bytter.basic.vo.BtInputBankInfoVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zuoy on 2017/7/20.
 */
@Service("btBankServiceI")
@DubboService
@Transactional
public class BtBankServiceImpl implements IBtBankService {
    @Autowired
    private BtBankService btBankService;
    @Autowired
    private BtInputBankInfoService btInputBankInfoServiceBase;
    @Autowired
    private BtInputBankInfoDao btInputBankInfoDao;

    @Override
    public BtBankDTO getByBankCode(String bankCode) {
        BtBank btBank = this.btBankService.getByBankCode(bankCode);
        BtBankDTO dto = new BtBankDTO();
        if (EmptyUtils.isEmpty(btBank)) {
            // 是否维护联行号信息
            BtInputBankInfoVO bankInfo = btInputBankInfoDao.getByBankCodeOrBankName(bankCode, null);
            if (EmptyUtils.isEmpty(bankInfo)) {
                return null;
            }
            BeanUtils.copyNotNullProperties(bankInfo, dto);
            return dto;
        }
        BeanUtils.copyNotNullProperties(btBank, dto);
        return dto;
    }

    @Override
    public BtBankDTO getByBankName(String bankName) {
        BtBank btBank = this.btBankService.getByBankName(bankName);
        BtBankDTO dto = new BtBankDTO();
        if (EmptyUtils.isEmpty(btBank)) {
            // 是否维护联行号信息
            BtInputBankInfoVO bankInfo = btInputBankInfoDao.getByBankCodeOrBankName(null, bankName);
            if (EmptyUtils.isEmpty(bankInfo)) {
                return null;
            }
            BeanUtils.copyNotNullProperties(bankInfo, dto);
            return dto;
        }
        BeanUtils.copyNotNullProperties(btBank, dto);
        return dto;
    }

    public List<BtBankDTO> getByBankNames(List<String> bankName) {
        // 是否维护联行号信息
        List<BtInputBankInfoVO> bankInfo = btInputBankInfoDao.getByBankCodeOrBankNames(null, bankName);
        if (EmptyUtils.isEmpty(bankInfo)) {
            return null;
        }
        return BeanUtils.copyListProperties(bankInfo, BtBankDTO.class);
    }

    @Override
    public void saveBtBank(String bankCode, String bankTypeId) {
        this.btBankService.saveBtBank(bankCode, bankTypeId);
    }
    @Override
    public List<BtBankDTO> getByBankTypeId(String bankTypeId) {
        List<BtBank> list = this.btBankService.getByBankTypeId(bankTypeId);
        List<BtBankDTO> dtoList = new ArrayList<BtBankDTO>();
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        dtoList = BeanUtils.copyListProperties(list, BtBankDTO.class);
        return dtoList;
    }
    public List<BtBankDTO> getBankUnionList(){
        return this.btBankService.getBankUnionList();
    }
    public Map<String,BtBankDTO> getBankUnionMap(){
        List<BtBankDTO> dtoList=btBankService.getBankUnionList();
        Map<String,BtBankDTO> map=new HashMap<>();
        for(BtBankDTO dto:dtoList){
            map.put(dto.getBankTypeId(),dto);
        }
        return map;
    }
    @Override
    public void saveBtBank(List<BtBankAccDTO> entityList) {
        this.btBankService.saveBtBank(entityList);
    }
    @Override
    public List<BtBankDTO> getListByCodes(List<String> codes) {
        return btBankService.getListByCodes(codes);
    }
    @Override
    public BtBankDTO getBankName(String bankCode) {
        return btInputBankInfoServiceBase.getBankName(bankCode);
    }
}
