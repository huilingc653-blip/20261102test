package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexProfitLossManage;
import com.bytter.biz.foreign.vo.FexProfitLossManageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2025/9/10<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Repository
public class FexProfitLossManageDao extends BaseDao<FexProfitLossManage,String> {

    /**
     *
     * 用于查看列表
     * @return
     */
    private String getCommonSqlJoin() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT f.id as id,t.bill_no AS billNo,f.TRAN_TYPE AS tranType,f.corp_id AS corpId,sc.name AS corpName,te.TRAN_CUR_PAIR_ID AS tranCurPairId, t.id as reqId,");
        sql.append("  t.BUS_DIRECTION AS busDirection,t.UPDOWN_DIRECTION AS updownDirection, f.QUOTATION_DATE AS quotationDate,f.SETTLE_DATE AS settleDate,");
        sql.append(" te.position_money AS positionMoney,f.CONTRACT_RATE AS contractRate, f.VALUATION_RATE AS valuationRate,f.CUR_ID AS curId,");
        sql.append(" c.english_code AS curEnName,f.FLOAT_MONEY AS floatMoney, f.CONVERT_FLOAT_MONEY AS convertFloatMoney,f.REAL_MONEY_TOTAL AS realMoneyTotal,");
        sql.append(" f.CONVERT_REAL_MONEY_TOTAL AS convertRealMoneyTotal,f.reference_interest_rate as referenceInterestRate, ");
        sql.append(" f.net_real_money_total AS netRealMoneyTotal,f.net_convert_real_money_total AS netConvertRealMoneyTotal, ");
        sql.append(" f.float_money_total AS netFloatMoney,f.convert_float_money_total AS netConvertFloatMoney ");
        sql.append(" from FEX_PROFIT_LOSS_MANAGE f");//损益表
        sql.append(" left join fex_tran_life_cycle ftlc on ftlc.id=f.life_cycle");//生命周期
        sql.append(" left join fex_tran_req t on t.id=f.req_id");//交易表
        sql.append(" left join fex_tran_req_ext te on te.req_id=t.id ");//交易扩展表
        sql.append(" left join bt_currency c on f.cur_id=c.id");//损益币别
        sql.append(" left join sys_corp  sc on f.corp_id=sc.id");//损益币别
        //sql.append(" left join sys_corp sc on f.corp_id =sc.id");//损益币别
        sql.append(" where 1=1");
        return sql.toString();
    }
    /**
     * 交易管理列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql=new StringBuilder(this.getCommonSqlJoin());

        sql.append(" and ").append(SqlUtil.getInSql(corpIds, "f.corp_Id", true));
        dataTablesInput.setOrderString(new OrderString("f.create_date", "desc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexProfitLossManageVO.class, "f", false);
    }

    public FexProfitLossManageVO getByReqId(String reqId) {
        String sql = getCommonSqlJoin() + " and f.req_id = :reqId";
        Map<String,String> params = new HashMap<>();
        params.put("reqId",reqId);
        return getBySQLAliasToBean(sql, params, FexProfitLossManageVO.class);
    }

    /**
     * 根据交易id获取损益集合
     * @param reqId
     * @return
     */
    public List<FexProfitLossManageVO> listFexProfitLossManage(String reqId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT f.id as id,f.req_id as reqId,f.float_money AS floatMoney,");
        sql.append("f.convert_float_money AS convertFloatMoney,f.real_money_total AS realMoneyTotal,f.convert_real_money_total AS convertRealMoneyTotal,");
        sql.append("ftr.settle_date as settleDate,ftr.contract_rate as contractRate,ftre.position_money as positionMoney,f.reference_interest_rate as referenceInterestRate, ");
        sql.append(" f.convert_date as convertDate,f.convert_cur_en_code as convertCurEnCode,f.quotation_date as quotationDate, f.quotation_method as quotationMethod,");
        sql.append(" f.convert_rate as convertRate,f.cur_id as curId,f.convert_cur_id as convertCurId , f.convert_cur_refer_id as convertCurReferId, ");
        sql.append(" f.float_Money_Total as netFloatMoney,f.convert_Float_Money_Total as netConvertFloatMoney,");
        sql.append(" f.net_real_money_total as netRealMoneyTotal,f.net_convert_real_money_total as netConvertRealMoneyTotal");
        sql.append(" from fex_profit_loss_manage f");//损益表
        sql.append(" left join fex_tran_req ftr on f.req_id = ftr.id");//交易管理表
        sql.append(" left join fex_tran_req_ext ftre on ftre.req_id = ftr.id ");//交易管理拓展表
        sql.append(" where 1=1");
        if(StringUtils.isNotBlank(reqId)){
            sql.append(" and f.req_id =:reqId");
            params.put("reqId", reqId);
        }
         return listBySQLAliasToBean(sql.toString(), params, FexProfitLossManageVO.class);
    }

    /**
     * 损益信息变更
     * @param id 损益id
     * @param referenceRate 参考利率
     * @param floatMoney 浮动损益
     * @param convertFloatMoney 折本位币浮动损益
     * @param convertRate 折本位币汇率
     */
    public void modify(String id, BigDecimal referenceRate, BigDecimal floatMoney, BigDecimal convertFloatMoney ,BigDecimal convertRate){
        Map <String, Object> params = new HashMap<>();
        StringBuilder sql=new StringBuilder();
        sql.append("update fex_profit_loss_manage  set  reference_interest_rate=:referenceRate,float_money=:floatMoney, convert_float_money=:convertFloatMoney ");
        if(null != convertRate){
            sql.append(" ,convert_rate =:convertRate");
            params.put("convertRate", convertRate);
        }
        sql.append(" where id=:id");
        params.put("id", id);
        params.put("referenceRate", referenceRate);
        params.put("floatMoney", floatMoney);
        params.put("convertFloatMoney", convertFloatMoney);

        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     *
     * @param reqId
     * @return
     */
    public FexProfitLossManage getFexProfitLossManageByReqId(String reqId) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from FexProfitLossManage where reqId = :reqId");
        Map<String,String> params = new HashMap<>();
        params.put("reqId",reqId);
        return (FexProfitLossManage)createQuery(hql.toString()).setProperties(params).uniqueResult();
    }
}
