package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.ErpNisWageResultView;
import com.bytter.biz.erp.entity.NisWage;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author fangcf
 * @date 2026-05-13
 */
@Repository
public class ErpNisWageDao extends BaseDao {

    /**
     * 代发工资交易结果查询
     *
     * @param tenantId  租户id
     * @param wageNo    工资单据号码
     * @param wageDtlNo 工资明细单号
     * @return 代发工资交易结果
     */
    public List<ErpNisWageResultView> listErpNisWageResult(String tenantId, String wageNo, String wageDtlNo) {
        Map<String, Object> params = Maps.newHashMap();

        StringBuilder sql = new StringBuilder();
        sql.append(" select v.tenantId as tenantId, v.WAGE_DTL_NO as wageDtlNo, v.WAGE_NO as wageNo, v.WORK_NO as workNo, ");
        sql.append(" v.PAYER_ACC_NO as payerAccNo, v.PAYER_ACC_NAME as payerAccName, v.PAYER_BANK_NAME as payerBankName, ");
        sql.append(" v.PAYEE_ACC_NO as payeeAccNo, v.PAYEE_ACC_NAME as payeeAccName, v.PAYEE_BANK_NAME as payeeBankName, ");
        sql.append(" v.amt as amt, v.VOUCHER_STAT as voucherStat, v.TRANS_TIME as transTime, v.RETURN_MSG as returnMsg ");
        sql.append(" from ERP_NIS_WAGE_RESULT_VIEW v ");
        sql.append(" where v.tenantId = :tenantId and v.WAGE_NO = :wageNo ");
        if (StringUtils.isNotEmpty(wageDtlNo)) {
            sql.append(" and v.WAGE_DTL_NO = :wageDtlNo ");
            params.put("wageDtlNo", wageDtlNo);
        }
        params.put("tenantId", tenantId);
        params.put("wageNo", wageNo);

        return this.listBySQLAliasToBean(sql.toString(), params, ErpNisWageResultView.class);
    }

    /**
     * 获取代发工资单
     *
     * @param tenantId 租户id
     * @param wageNo   工资单据号码
     * @return 单据
     */
    public NisWage getOne(String tenantId, String wageNo) {
        Map<String, Object> params = Maps.newHashMap();

        StringBuilder sql = new StringBuilder();
        sql.append(" select * from NIS_WAGE nw ");
        sql.append(" where nw.TENANT_ID = :tenantId and nw.WAGE_NO = :wageNo ");

        params.put("tenantId", tenantId);
        params.put("wageNo", wageNo);

        return this.getBySQLAliasToBean(sql.toString(), params, NisWage.class);
    }
}
