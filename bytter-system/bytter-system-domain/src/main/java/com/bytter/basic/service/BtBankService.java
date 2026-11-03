package com.bytter.basic.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.basic.dao.BtBankDao;
import com.bytter.basic.entity.BtBank;
import com.bytter.basic.entity.BtInputBankInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.BtBankDTO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK
 * @date 2017-07-20 14:28:41
 */
@Service("btBankServiceBase")
@Transactional
public class BtBankService extends BaseService<BtBank, String> {
    @Autowired
    private BtBankDao btBankDao;
    @Autowired
    //@Qualifier("btInputBankInfoServiceBase")
    private BtInputBankInfoService bankInfoService;
    private static final int IN_LEN = 100;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(BtBank entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(BtBank entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BtBank entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(BtBank entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<BtBank, String> getEntityDao() {
        return btBankDao;
    }
    /**
     * 根据联行号代码查询
     *
     * @param bankCode 联行号
     * @return
     */
    public BtBank getByBankCode(String bankCode) {
        return this.btBankDao.getByBankCode(bankCode);
    }

    /**
     * 根据联行号名称查询
     *
     * @param bankName	联行号名称
     * @return
     */
    public BtBank getByBankName(String bankName) {
        return this.btBankDao.getByBankName(bankName);
    }

    public List<BtBank> getByBankNames(List<String> bankName) {
        return this.btBankDao.getByBankNames(bankName);
    }

    /**
     * 根据联行号初始化BtBank
     *
     * @param bankCode   联行号
     * @param bankTypeId 银行类别ID
     */
    public void saveBtBank(String bankCode, String bankTypeId) {
        BtInputBankInfo bankInfo = bankInfoService.getByBankInfo(bankCode);
        if (ObjectUtil.isNotEmpty(bankInfo)) {
            BtBank btBank = this.btBankDao.getByBankCode(bankCode);
            // 联行号信息不存在
            if (EmptyUtils.isEmpty(btBank)) {
                btBank = new BtBank();
                BeanUtils.copyNotNullProperties(bankInfo, btBank);
                btBank.setBankTypeId(bankTypeId);
                btBank.setId(null); // 这里的ID 必须要和 BtInputBankInfo 一致吗？？？
                this.save(btBank);
            }
        }
    }
    /**
     * 根据银行类别ID查询
     *
     * @param bankTypeId 银行类别ID
     * @return
     */
    public List<BtBank> getByBankTypeId(String bankTypeId) {
        return this.btBankDao.getByBankTypeId(bankTypeId);
    }
    public List<BtBankDTO> getBankUnionList(){
        return this.btBankDao.getBankUnionList();
    }
    public void saveBtBank(List<BtBankAccDTO> btBankAccDTOs) {
        if (EmptyUtils.isEmpty(btBankAccDTOs)) {
            return;
        }
        Map<String, String> bankCodebankTypeIdMap = new HashMap<>();
        for (BtBankAccDTO btBankAccDTO : btBankAccDTOs) {
            if (EmptyUtils.isNotEmpty(btBankAccDTO.getBankCode())) {
                bankCodebankTypeIdMap.put(btBankAccDTO.getBankCode(), btBankAccDTO.getBankTypeId());
            }
        }
        if (bankCodebankTypeIdMap.isEmpty()) {
            return;
        }
        List<String> bankCodes = Lists.newArrayList(bankCodebankTypeIdMap.keySet());
        int len = IN_LEN;
        int times = bankCodes.size() % len > 0 ? (bankCodes.size() / len) + 1 : bankCodes.size() / len;
        int fromIndex = 0;
        int toIndex = 0;
        for (int timeIndex = 0; timeIndex < times; timeIndex++) {
            fromIndex = timeIndex * len;
            toIndex = (timeIndex + 1) * len;
            if (toIndex > bankCodes.size()) {
                toIndex = bankCodes.size();
            }
            saveBatchBtBank(bankCodes.subList(fromIndex, toIndex), bankCodebankTypeIdMap);
        }
    }
    private void saveBatchBtBank(List<String> bankCodes, Map<String, String> bankCodebankTypeIdMap) {
        List<BtBank> notExsitBanks = new ArrayList<>();
        List<BtInputBankInfo> bankInfos = bankInfoService.getBankInfoForNoBTBank(bankCodes);
        if (EmptyUtils.isEmpty(bankInfos)) {
            return;
        }
        for (BtInputBankInfo bankInfo : bankInfos) {
            BtBank btBank = new BtBank();
            BeanUtils.copyNotNullProperties(bankInfo, btBank);
            btBank.setBankTypeId(bankCodebankTypeIdMap.get(bankInfo.getBankCode()));
            notExsitBanks.add(btBank);
        }
        this.batchSave(notExsitBanks);
    }
    public List<BtBankDTO> getListByCodes(List<String> codes) {
        return btBankDao.getListByCodes(codes);
    }
}
