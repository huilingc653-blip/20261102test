package com.bytter.credit.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.credit.domain.entity.RatCreditCooperationBank;
import com.bytter.credit.domain.vo.RatCreditCooperationBankVO;
import com.bytter.credit.dto.RatSysItemDTO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 授信合作银行维护
 *
 * @author fangcf
 * @date 2024-06-20
 */
@Repository
public class RatCreditCooperationBankDao extends BaseDao<RatCreditCooperationBank, String> {

    /**
     * 查询授信合作银行维护数据
     *
     * @param input 入参
     * @return output 出参
     */
    public DataTablesOutput listOutputData(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rccb.id as id, rccb.BANK_NAME as bankName, rccb.BANK_CODE as bankCode, rccb.COOPERATION_TYPE as cooperationType, rccb.CREATE_DATE as createDate ");
        sql.append(" from RAT_CREDIT_COOPERATION_BANK rccb where 1 = 1 ");
        OrderString order = new OrderString("rccb.CREATE_DATE", "desc");
        input.setOrderString(order);
        return getDynamicBySQL(input, sql.toString(), RatCreditCooperationBankVO.class, null, false);
    }

    /**
     * 根据联行号查询授信合作银行
     *
     * @param bankCode 联行号
     * @return 授信合作银行
     */
    public List<RatCreditCooperationBank> listCooperationBankByBankCode(String bankCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rccb.id as id, rccb.BANK_NAME as bankName, rccb.BANK_CODE as bankCode, rccb.COOPERATION_TYPE as cooperationType ");
        sql.append(" from RAT_CREDIT_COOPERATION_BANK rccb where rccb.BANK_CODE = :bankCode ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("bankCode", bankCode);
        return listBySQLAliasToBean(sql.toString(), params, RatCreditCooperationBank.class);
    }
}
