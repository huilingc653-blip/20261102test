package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranLifeCycle;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FexTranLifeCycleDao extends BaseDao<FexTranLifeCycle,String> {
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, String joinSql) {
        // 构建基础SQL
        StringBuilder sqlBuilder = new StringBuilder(getSql());
        sqlBuilder.append(" and ").append(joinSql);
        HashMap<String, Object> params = new HashMap<>();
        Object tranCurPairIdObj = input.getFormdata().get("eqs_tranCurPairId");
        if (tranCurPairIdObj != null && StringUtils.isNotEmpty(tranCurPairIdObj.toString())) {
            String tranCurPairIdsStr = tranCurPairIdObj.toString();
            String[] tranCurPairIds = tranCurPairIdsStr.split(",");
            sqlBuilder.append(" and (");
            for (int i = 0; i < tranCurPairIds.length; i++) {
                String tranCurPairId = tranCurPairIds[i];
                String[] idItems = tranCurPairId.split("_");
                String baseParamKey = "tranCurPairBaseId_" + i;
                String referParamKey = "tranCurPairReferId_" + i;
                // 拼接条件SQL
                sqlBuilder.append(String.format("  ((t.tran_cur_pair_base_id = :%s and t.tran_cur_pair_refer_id = :%s) or (t.tran_cur_pair_base_id = :%s and t.tran_cur_pair_refer_id = :%s))",
                        baseParamKey, referParamKey, referParamKey, baseParamKey));
                params.put(baseParamKey, idItems[0]);
                params.put(referParamKey, idItems[1]);
                if (i != tranCurPairIds.length-1) {
                    sqlBuilder.append(" or ");
                }
            }
            sqlBuilder.append(" )");
            input.getFormdata().remove("eqs_tranCurPairId");
        }
        return getDynamicBySQL(input, sqlBuilder.toString(), FexTranLifeCycleVo.class, "", false, params);
    }

    public DataTablesOutput<FexTranLifeCycleVo> dataTableApprove(DataTablesInput input, String joinSql) {
        StringBuilder sql = new StringBuilder(getSql());
        sql.append(" and ").append(joinSql);
        HashMap<String, Object> params = new HashMap<>();
        Object tranCurPairIdObj = input.getFormdata().get("eqs_tranCurPairId");
        if (tranCurPairIdObj != null && StringUtils.isNotEmpty(tranCurPairIdObj.toString())) {
            String[] tranCurPairId = tranCurPairIdObj.toString().split("_");
            if (tranCurPairId.length >= 2) {
                sql.append(" and ((t.tran_cur_pair_base_id = :tranCurPairBaseId and t.tran_cur_pair_refer_id = :tranCurPairReferId)");
                sql.append(" or (t.tran_cur_pair_base_id = :tranCurPairReferId and t.tran_cur_pair_refer_id = :tranCurPairBaseId))");
                params.put("tranCurPairBaseId", tranCurPairId[0]);
                params.put("tranCurPairReferId", tranCurPairId[1]);
            }
            input.getFormdata().remove("eqs_tranCurPairId");
        }
        return getDynamicBySQL(input, sql.toString(), FexTranLifeCycleVo.class, "", false, params);
    }

    /**
     *
     * 通过交易ID查询生命周期
     *
     * @param reqId
     * @param opType
     * @return
     */
    public FexTranLifeCycleVo getByReqId(String reqId, String opType) {
        String sql = getSql() + " and ftlc.req_id = :reqId and ftlc.op_type = :opType";
        Map<String,String> params = new HashMap<>();
        params.put("reqId", reqId);
        params.put("opType", opType);
        return getBySQLAliasToBean(sql, params, FexTranLifeCycleVo.class);
    }

    /**
     *
     * 获取流程中的生命周期
     *
     * @param reqIds
     * @return
     */
    public List<FexTranLifeCycleVo> getTemporaryLifeCycle(List<String> reqIds) {
        String sql = getSql() + " and " + getSQLIn(reqIds, 500, "ftlc.req_id") + " and ftlc.life_status in ('0','-1','11')";
        return listBySQLAliasToBean(sql, null, FexTranLifeCycleVo.class);
    }

    public FexTranLifeCycleVo getByUnion(String id) {
        String sql = getSql() + " and ftlc.id = :id";
        Map<String,String> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql, params, FexTranLifeCycleVo.class);
    }

    private String getSql() {
        String sql = "select ftlc.id, ftlc.req_id as reqId, ftlc.corp_id as corpId, ftlc.bill_no as billNo, ftlc.op_type as opType, " +
                "ftlc.op_date as opDate, ftlc.expires_date as expiresDate, ftlc.settle_date as settleDate, ftlc.contract_rate as contractRate, " +
                "COALESCE(ftlc.spot_rate, ftlc.early_rate, ftlc.deferred_rate) as spotRate, " +
                "ftlc.process_rate as processRate, ftlc.tran_settle_method as tranSettleMethod, ftlc.is_payment_amount as isPaymentAmount, ftlc.buy_cur_id as buyCurId, ftlc.sell_cur_id as sellCurId, " +
                "ftlc.buy_money as buyMoney, ftlc.sell_money as sellMoney, ftlc.buy_bal as buyBal, ftlc.sell_bal as sellBal, ftlc.money as money, " +
                "ftlc.profit_loss_cur as profitLossCur, ftlc.profit_loss_money as profitLossMoney, ftlc.record_rate as recordRate, ftlc.bank_acc as bankAcc, ftlc.cur_id as curId, " +
                "ftlc.early_rate as earlyRate, ftlc.early_type as earlyType, ftlc.deferred_rate as deferredRate, ftlc.deferred_type as deferredType, ftlc.close_settle_date as closeSettleDate, " +
                "ftlc.close_rate as closeRate, ftlc.close_forward_rate as closeForwardRate, ftlc.convert_cur_type as convertCurType, ftlc.convert_rate as convertRate, ftlc.convert_loss_money as convertLossMoney, " +
                "ftlc.payee_acc as payeeAcc, ftlc.payer_acc as payerAcc, ftlc.early_settlement_type as earlySettlementType, ftlc.early_settlement_rate as earlySettlementRate, ftlc.close_loss_money as closeLossMoney, " +
                "ftlc.delivery_loss_money as deliveryLossMoney, ftlc.all_loss_money as allLossMoney, ftlc.tran_addr_id as tranAddrId, ftlc.tran_addr as tranAddr, ftlc.rmk as rmk, " +
                "ftlc.create_by as createBy, ftlc.create_date as createDate, ftlc.update_by as updateBy, ftlc.update_date as updateDate, ftlc.life_status as lifeStatus, " +
                "case when r.tran_type = '1' and r.fex_tran_type = '1' and (r.is_swap_tran = '0' or r.is_swap_tran is null) and r.bus_direction = '1' " +
                "then COALESCE(ftlc.buy_money, ftlc.op_money) " +
                "when r.tran_type = '1' and r.fex_tran_type = '1' and (r.is_swap_tran = '0' or r.is_swap_tran is null) and r.bus_direction = '2' " +
                "then COALESCE(ftlc.sell_money, ftlc.op_money) else ftlc.op_money end as opMoney, " +
                "ftlc.real_buy_money as realBuyMoney, ftlc.real_sell_money as  realSellMoney, t.quotation_method quotationMethod, t.quotation_cur_id quotationCurId, t.quotation_refer_cur_id quotationReferCurId," +
                "r.bill_no relBillNo,t.tran_cur_pair_id  tranCurPairId, t.tran_cur_pair_base_id  tranCurPairBaseId, t.tran_cur_pair_refer_id  tranCurPairReferId," +
                "r.bus_direction busDirection, t.position_money positionMoney,r.tran_date tranDate, r.delivery_type deliveryType,r.tran_type tranType " +
                "from fex_tran_life_cycle ftlc  " +
                "LEFT JOIN fex_tran_req r ON  r.ID = ftlc.req_id " +
                "LEFT JOIN fex_tran_req_ext t ON t.req_id = ftlc.req_id where 1=1";
        return sql;
    }

    /**
     * 根据交易id获取生命周期集合
     * @param reqId
     * @return
     */
    public List<FexTranLifeCycleVo> listFexTranLifeCycle(String reqId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT f.* from fex_tran_life_cycle f");
        sql.append(" where 1=1");
        if(StringUtils.isNotBlank(reqId)){
            sql.append(" and f.req_id =:reqId");
            params.put("reqId", reqId);
        }
        return listBySQLAliasToBean(sql.toString(), params, FexTranLifeCycleVo.class);
    }


}
