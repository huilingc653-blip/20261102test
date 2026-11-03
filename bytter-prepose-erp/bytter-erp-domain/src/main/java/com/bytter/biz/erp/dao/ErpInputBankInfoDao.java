package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.InputBankInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ErpInputBankInfoDao extends BaseDao<InputBankInfo, String> {

    /***
     * 根据联行号获取对应的联行号信息
     *
     * @param sysBankCode
     * @return 联行号信息
     */
    public InputBankInfo getOneInputBankInfo(String sysBankCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select id, sys_bank_Code as sysBankCode, bank_name as bankName ");
        sql.append(" from BT_INPUT_BANK_INFO ");
        sql.append(" where sys_bank_code = :sysBankCode ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("sysBankCode", sysBankCode);

        List<InputBankInfo> bankList = this.listBySQLAliasToBean(sql.toString(), params, InputBankInfo.class);

        if (EmptyUtils.isEmpty(bankList)) {
            return null;
        } else if (bankList.size() > 1) {
            logger.error("存在重复的联行号，请联系管理员删除重复数据！，联行号：{}", sysBankCode);
        }
        return bankList.stream().findFirst().get();
    }
}
