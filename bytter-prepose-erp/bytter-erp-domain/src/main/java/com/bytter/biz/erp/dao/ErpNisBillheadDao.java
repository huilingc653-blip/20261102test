package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.ErpPaymentResultView;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fangcf
 * @date 2026-04-29
 */
@Repository
public class ErpNisBillheadDao extends BaseDao {

    public String queryOnlyFile(String table, String value, String where, String whereVal) {
        StringBuilder sql = new StringBuilder();
        sql.append("select " + value + " as value from " + table + " where " + where + "=:whereVal");
        Map<String, Object> map = new HashMap<>();
        map.put("whereVal", whereVal);
        List<Map<Object, Object>> map1 = this.listBySQL(sql.toString(), map);
        if (map1 != null && map1.size() > 0) return map1.get(0).get("VALUE").toString();
        return "";
    }

    public Map<Object, Object> getVAccList(String acc) {
        StringBuilder sql = new StringBuilder("select bank_name,acc_name,prov,city from v_acc_list where bank_acc=:acc");
        Map<String, Object> map = new HashMap<>();
        map.put("acc", acc);
        List<Map<Object, Object>> map1 = this.listBySQL(sql.toString(), map);
        if (map1 != null && map1.size() > 0) return map1.get(0);
        return null;
    }

    /**
     * 付款结果 单笔查询
     *
     * @param tenantId     租户id
     * @param serialNoErp  单据编号
     * @param voucherNoErp 凭证编号
     * @return 单据付款结果
     */
    public List<ErpPaymentResultView> listPaymentSingleResult(String tenantId, String serialNoErp, String voucherNoErp) {
        Map<String, Object> params = Maps.newHashMap();

        StringBuilder sql = new StringBuilder();
        sql.append(" select v.tenantId as tenantId, v.serialNoErp as serialNoErp, v.voucherNoErp as voucherNoErp, ");
        sql.append(" v.payerAccNo as payerAccNo, v.payerAccName as payerAccName, v.payerBankName as payerBankName, ");
        sql.append(" v.payeeAccNo as payeeAccNo, v.payeeAccName as payeeAccName, v.payeeBank as payeeBank, ");
        sql.append(" v.amt as amt, v.voucherStat as voucherStat, v.transTime as transTime, v.returnMsg as returnMsg ");
        sql.append(" from erp_payment_result_view v ");
        sql.append(" where v.tenantId = :tenantId and v.serialNoErp = :serialNoErp and v.voucherNoErp = :voucherNoErp ");

        params.put("tenantId", tenantId);
        params.put("serialNoErp", serialNoErp);
        params.put("voucherNoErp", voucherNoErp);

        return this.listBySQLAliasToBean(sql.toString(), params, ErpPaymentResultView.class);
    }
}
