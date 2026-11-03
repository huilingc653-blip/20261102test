package com.bytter.basic.service;


import com.bytter.basic.dao.BtBankTypeOtherCustomDao;
import com.bytter.basic.entity.BankTypeOtherCustom;
import com.bytter.basic.vo.BankTypeOtherCustomVO;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.dto.SysCountryCustDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 银行其他信息模块维护-其他信息模块
 * @date 2023-12-2218:07:33
 */
@Service("btBankTypeOtherCustomService")
@Transactional
public class BtBankTypeOtherCustomService extends BaseService<BankTypeOtherCustom, String> {
    @Autowired
    private BtBankTypeOtherCustomDao bankTypeOtherCustomDao;

    @Autowired
    private ISysCountryCustService sysCountryCustService;

    @Override
    public BaseDao<BankTypeOtherCustom, String> getEntityDao() {
        return bankTypeOtherCustomDao;
    }

    /**
     * 根据银行类型获取其他信息模块列表
     *
     * @param bankType
     * @return
     */
    public List<BankTypeOtherCustom> listByBankType(String bankType) {
        return this.bankTypeOtherCustomDao.listByBankType(bankType);
    }

    /**
     * 根据银行类型删除其他信息模块
     *
     * @param bankType
     */
    public void deleteByBankType(String bankType) {
        this.bankTypeOtherCustomDao.deleteByBankType(bankType);
    }

    /**
     * 根据银行类型和接口代码获取银行其他信息模块
     *
     * @param bankType
     * @param bifCode
     * @return
     */
    public List<BankTypeOtherCustom> listByBankTypeAndBifCode(String bankType, String bifCode) {
        return this.bankTypeOtherCustomDao.listByBankTypeAndBifCode(bankType, bifCode);
    }

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
    public BankTypeOtherCustomVO getByBankTypeOtherCustom(String bankType, String bifCode, String applayScope, String countryId, String remitType) {
        if (BdlinkConstant.COUNTRY_CODE_CHINA.equals(countryId)) {
            // 境内银行做特殊处理
            SysCountryCustDTO china = sysCountryCustService.getByEnCode(BdlinkConstant.COUNTRY_CODE_CHINA);
            countryId = china == null ? "":china.getId();
        }
        return this.bankTypeOtherCustomDao.getByBankTypeOtherCustom(bankType, bifCode, applayScope, countryId, remitType);
    }
}
