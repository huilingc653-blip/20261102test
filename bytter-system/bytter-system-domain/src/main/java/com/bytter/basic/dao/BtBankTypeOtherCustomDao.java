package com.bytter.basic.dao;


import com.bytter.basic.entity.BankTypeOtherCustom;
import com.bytter.basic.vo.BankTypeOtherCustomVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 银行其他信息模块维护
 * @date 2023-12-22 18:07:34
 */
@Repository
public class BtBankTypeOtherCustomDao extends BaseDao<BankTypeOtherCustom, String> {

    /**
     * 根据银行类型删除其他信息模块
     * @param bankType
     */
    public void deleteByBankType(String bankType) {
        String sql = " delete from BT_BANK_TYPE_OTHER_CUSTOM where bank_type_id=:bankType ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("bankType", bankType);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 根据银行类型获取银行其他信息模块
     * @param bankType
     * @return
     */
    public List<BankTypeOtherCustom> listByBankType(String bankType) {
        String hql = " from BankTypeOtherCustom b where b.bankTypeId=:bankType ";
        Map<String, String> params = new HashMap<>();
        params.put("bankType", bankType);
        return listByHQLAliasToBean(hql, params, BankTypeOtherCustom.class);
    }

    /**
     * 根据银行类型和接口代码获取银行其他信息模块
     * @param bankType
     * @param bifCode
     * @return
     */
    public List<BankTypeOtherCustom> listByBankTypeAndBifCode(String bankType, String bifCode) {
        String hql = " from BankTypeOtherCustom b where b.bankTypeId=:bankType and b.bifCode=:bifCode ";
        Map<String, String> params = new HashMap<>();
        params.put("bankType", bankType);
        params.put("bifCode", bifCode);
        return listByHQLAliasToBean(hql, params, BankTypeOtherCustom.class);
    }

    /**
     * 获取银行类型自定义配置
     * @param bankType
     * @param bifCode
     * @param applayScope
     * @param countryId
     * @param remitType
     * @return
     */
    public BankTypeOtherCustomVO getByBankTypeOtherCustom(String bankType, String bifCode, String applayScope, String countryId, String remitType) {
        String hql = " from BankTypeOtherCustom b where b.bankTypeId=:bankType and b.bifCode=:bifCode and b.applayScope=:applayScope";
        hql += " and (countryId like :countryId or countryId is null or countryId = '')";
        hql += " and (remitType like :remitType or remitType is null or remitType = '')";
        Map<String, String> params = new HashMap<>();
        params.put("bankType", bankType);
        params.put("bifCode", bifCode);
        params.put("applayScope", applayScope);
        params.put("countryId", "%" + countryId + "%");
        params.put("remitType", "%" + remitType + "%");
        List<BankTypeOtherCustomVO> list = listByHQLAliasToBean(hql, params, BankTypeOtherCustomVO.class);
        if (EmptyUtils.isEmptyList(list)) {
            return new BankTypeOtherCustomVO();
        }
        return list.get(0);
    }
}
