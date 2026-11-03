package com.bytter.biz.foreign.dao;


import com.bytter.biz.foreign.entity.FexTranReqExt;
import com.bytter.biz.foreign.vo.FexTranReqExtVo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Entity
 * @Description: 交易管理扩展表
 * @author wuxc
 * @date 2025-09-2
 * @version 1.0
 *
 */
@Repository
public class FexTranReqExtDao extends BaseDao<FexTranReqExt,String> {


    /**
     *
     * 根据交易ID删除扩展信息
     *
     * @param reqId
     */
    public void delByReqId(String reqId) {
        String sql = " delete from fex_tran_req_ext where req_id = :reqId";
        Map<String, Object> map = new HashMap<>();
        map.put("reqId", reqId);
        this.createSQLQuery(sql).setProperties(map).executeUpdate();
    }

    /**
     *
     * 根据交易ID查询扩展信息
     *
     * @param reqId
     * @return
     */
    public FexTranReqExtVo getByReqId(String reqId) {
        String sql = getSql() + "AND fx.req_id = :reqId";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("reqId", reqId);
        return getBySQLAliasToBean(sql, params, FexTranReqExtVo.class);
    }
    /**
     *
     * 根据交易ID查询扩展信息
     *
     * @param reqId
     * @return
     */
    public List<FexTranReqExtVo> listByReqIds(List<String> reqId) {
        String sql = "select req_id as reqId,tran_rate_type as tranRateType from fex_tran_req_ext fx where 1=1 ";
        if (!CollectionUtils.isEmpty(reqId)) {
            sql += " and "+getSQLIn(reqId,900,"req_id");
        }
        HashMap<String, String> params = new HashMap<String, String>();
        return listBySQLAliasToBean(sql, params, FexTranReqExtVo.class);
    }


    private String getSql() {
        String sql = "SELECT " +
                " fx.id, fx.req_id AS reqId, fx.TRAN_CUR_PAIR_ID AS tranCurPairId," +
                " fx.is_payment_amount AS isPaymentAmount, fx.tran_rate_type AS tranRateType, fx.quotation_method AS quotationMethod," +
                " fx.position_money AS positionMoney, fx.profit_loss_cur AS profitLossCur, fx.profit_loss_money AS profitLossMoney," +
                " fx.convert_cur_type AS convertCurType, fx.convert_rate AS convertRate, fx.convert_money AS convertMoney," +
                " fx.QUOTATION_CUR_ID AS quotationCurId, fx.QUOTATION_REFER_CUR_ID AS quotationReferCurId, fx.TRAN_CUR_PAIR_BASE_ID AS tranCurPairBaseId," +
                " fx.TRAN_CUR_PAIR_REFER_ID AS tranCurPairReferId, fx.LIFE_CYCLE_STATE AS lifeCycleState," +
                " c.english_code as profitLossCurEnglishCode,c.cur_name as profitLossCurName" +
                " FROM fex_tran_req_ext fx left join bt_currency c on fx.profit_loss_cur = c.id" +
                " WHERE 1=1 ";
        return sql;
    }

    /**
     * 折本位币汇率变更
     * @param id 交易id
     * @param convertRate 折本位币汇率
     */
    public void modifyConvertRate(String id, BigDecimal convertRate){
        Map <String, Object> params = new HashMap<>();
        StringBuilder sql=new StringBuilder();
        sql.append("update fex_tran_req_ext set convert_rate=:convertRate where req_id =:id");
        params.put("id", id);
        params.put("convertRate", convertRate);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 损益币种变更
     * @param id 交易id
     * @param profitLossCur 损益币种
     */
    public void modifyProfitLossCur(String id, String profitLossCur){
        Map <String, Object> params = new HashMap<>();
        StringBuilder sql=new StringBuilder();
        sql.append("update fex_tran_req_ext set profit_loss_cur=:profitLossCur where req_id=:id");
        params.put("id", id);
        params.put("profitLossCur", profitLossCur);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
