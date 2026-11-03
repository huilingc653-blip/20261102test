package com.bytter.basic.service;

import com.bytter.basic.dao.BtBankTypeCustomDao;
import com.bytter.basic.entity.BankTypeCustom;
import com.bytter.basic.vo.BankTypeCustomVO;
import com.bytter.basic.vo.CustomQueryVo;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.IBtSwiftInfoService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.dto.BtSwiftInfoDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 银行自定义维护
 * @date 2023-12-2218:07:33
 */
@Service("btBankTypeCustomService")
@Transactional
public class BtBankTypeCustomService extends BaseService<BankTypeCustom, String> {
    @Autowired
    private BtBankTypeCustomDao btBankTypeCustomDao;

    @Autowired
    private ISysCountryCustService sysCountryCustService;

    @Autowired
    private IBtSwiftInfoService btSwiftInfoService;

    @Override
    public BaseDao<BankTypeCustom, String> getEntityDao() {
        return btBankTypeCustomDao;
    }

    /**
     * 根据银行类型获取自定义列表
     * @param bankType
     * @return
     */
    public List<BankTypeCustom> listByBankType(String bankType, String customType) {
        return this.btBankTypeCustomDao.listByBankType(bankType, customType);
    }

    /**
     * 根据银行类型删除自定义
     * @param bankType
     */
    public void deleteByBankType(String bankType, String customType) {
        this.btBankTypeCustomDao.deleteByBankType(bankType, customType);
    }

    /**
     * 根据银行类型和接口代码获取银行自定义
     * @param bankType
     * @param bifCode
     * @return
     */
    public List<BankTypeCustomVO> listByBankTypeAndBifCode(String bankType, String bifCode, String countryId, String customType) {
        if (BdlinkConstant.COUNTRY_CODE_CHINA.equals(countryId)) {
            // 境内银行做特殊处理
            SysCountryCustDTO china = sysCountryCustService.getByEnCode(BdlinkConstant.COUNTRY_CODE_CHINA);
            countryId = china == null ? "":china.getId();
        }
        return this.btBankTypeCustomDao.listByBankTypeAndBifCode(bankType, bifCode, countryId, customType);
    }

    /**
     * 根据银行类型和接口代码获取银行自定义
     * @param bankType
     * @param bifCode
     * @return
     */
    public List<BankTypeCustomVO> listByBankTypeAndBifCode(String bankType, String bifCode, String countryId, String bankPayType, String customType) {
        if (BdlinkConstant.COUNTRY_CODE_CHINA.equals(countryId)) {
            // 境内银行做特殊处理
            SysCountryCustDTO china = sysCountryCustService.getByEnCode(BdlinkConstant.COUNTRY_CODE_CHINA);
            countryId = china == null ? "":china.getId();
        }
        return this.btBankTypeCustomDao.listByBankTypeAndBifCode(bankType, bifCode, countryId, bankPayType, customType);
    }

    /**
     * 根据银行类型和接口代码获取银行自定义
     * @param bankType
     * @param bifCode
     * @return
     */
    public BankTypeCustomVO getByBankTypeCustom(String bankType, String bifCode, String customCode, String customType) {
        return this.btBankTypeCustomDao.getByBankTypeCustom(bankType, bifCode, customCode, customType);
    }

    /**
     *
     * @param bankTypeId
     * @param bifCode
     * @param countryId
     * @return
     */
    public  List<BankTypeCustomVO> listByBankTypeIdAndBifCodeAndCountryId(String bankTypeId, String bifCode, String countryId){
        List<BankTypeCustomVO> list=  btBankTypeCustomDao.listByBankTypeIdAndBifCodeAndCountryId(bankTypeId, bifCode, countryId);
        list.forEach(i->{
            i.setBankPayTypeDisplay(i.getCustomCode()+"("+i.getCustomName()+")");
        });
        return list;
    }


    /**
     * 根据条件查询支付类型
     * @param bankTypeId
     * @param bifCode
     * @param countryId
     * @param isDomesticBank
     * @param bankCode
     * @param payTypeId
     * @return
     */
    public List<BankTypeCustomVO> listByCondition(String bankTypeId, String bifCode, String countryId, String isDomesticBank, String bankCode,String payTypeId) {
        if (CommonConstant.NO.equals(isDomesticBank)) {
            //境外
            BtSwiftInfoDTO btSwiftInfoDTO = btSwiftInfoService.getByCode(bankCode);
            if (btSwiftInfoDTO != null) {
                countryId = btSwiftInfoDTO.getCountryCode();
            }
        } else if (CommonConstant.YES.equals(isDomesticBank)) {
            //境内
            SysCountryCustDTO countryCustDTO = sysCountryCustService.getByNameZhLevelTop("中国");
            countryId = countryCustDTO.getId();
        }
        CustomQueryVo dto=new CustomQueryVo();
        dto.setBankTypeId(bankTypeId);
        dto.setBifCode(bifCode);
        dto.setCountryId(countryId);
        dto.setPayTypeId(payTypeId);
        dto.setCustomType("bankPayType");
        List<BankTypeCustomVO> data= btBankTypeCustomDao.listByCondition(dto);
        data.forEach(e->{
            e.setBankPayTypeDisplay(e.getCustomCode()+"("+e.getCustomName()+")");
        });
        return data;
    }

}
