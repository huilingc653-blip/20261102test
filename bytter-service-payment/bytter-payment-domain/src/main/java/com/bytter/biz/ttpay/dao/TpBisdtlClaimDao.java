package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.util.TtPayUtil;
import com.bytter.biz.ttpay.vo.TPpBisdtlclaimVO;
import com.bytter.biz.ttpay.vo.TpMerchantVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.bdlink.api.constant.BisAccBalConstant;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class TpBisdtlClaimDao  extends BaseDao<T,String> {
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList){
        StringBuilder sql=new StringBuilder("select ");
        sql.append(" d.id,d.appId,d.mchId,d.serialNo,d.trandNo,d.payAcc,d.fbsStatus,d.tradeType,");
        sql.append("case when d.trandMoney>0 then 0 else 1 end as cdSign,");
        sql.append("d.trandTime,d.trandMoney,m.corp_id as corpId,m.PAY_TYPE as payType,d.busType,t.bus_type_name as busTypeName ");
        sql.append(" from (select id,\n" +
                "                   '003'          as payType,\n" +
                "                   APP_ID         as appId,\n" +
                "                   MCH_ID         as mchId,\n" +
                "                   TRANSACTION_ID as serialNo,\n" +
                "                   TRADE_NO       as trandNo,\n" +
                "                   TRADE_TYPE     as tradeType,\n" +
                "                   null           as payAcc,\n" +
                "                   TRADE_TIME     as trandTime,\n" +
                "                   ORDER_FEE      as trandMoney,\n" +
                "                   FBS_STATUS     as fbsStatus,\n" +
                "                   BUS_TYPE       as busType\n" +
                "            from TP_WEIXINPAY_DTL\n" +
                "            union all\n" +
                "            select id,\n" +
                "                   '002'                                                           as payType,\n" +
                "                   APP_ID                                                          as appId,\n" +
                "                   MCH_ID                                                          as mchId,\n" +
                "                   ACCOUNT_ID                                                      as serialNo,\n" +
                "                   TRADE_NO                                                        as trandNo,\n" +
                "                   TRADE_TYPE                                                      as tradeType,\n" +
                "                   PAY_ACC                                                         as payAcc,\n" +
                "                   HAPPEN_TIME                                                     as trandTime,\n" +
                "                   CASE WHEN PAYEE_MONEY = 0 THEN PAYER_MONEY ELSE PAYEE_MONEY END AS trandMoney,\n" +
                "                   FBS_STATUS                                                      as fbsStatus,\n" +
                "                   BUS_TYPE                                                        as busType\n" +
                "            from TP_ALIPAY_DTL) d ");
        sql.append(" left join tp_merchant_var mv on mv.var_code in ( :appIds ) and mv.var_value_str = d.").append(TtPayUtil.APPID).append(" ");
        sql.append(" left join tp_merchant m on m.id = mv.mch_id and d.payType = m.PAY_TYPE ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" left join nis_bus_type t on d.busType = t.id ");
        sql.append(" where 1=1 and m.valid_sign = :validSign ");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("appIds", TtPayConstant.APPIDS);
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        if(corpAuthList==null||corpAuthList.size()<1){
            sql.append(" and 1!=1 ");
        }else{
            sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "m.corp_id", params));
        }
        String fbsStatus = (String) input.getFormdata().get("eqs_fbsStatus");
        input.getFormdata().remove("eqs_fbsStatus");
        if(StringUtils.isNotBlank(fbsStatus)){
            if(BisAccBalConstant.FBS_STATUS_N.equals(fbsStatus)){
                sql.append(" and (d.fbsStatus is null or d.fbsStatus = '0' or d.fbsStatus='') ");
            }else{
                sql.append(" and d.fbsStatus =:fbsStatus");
                params.put("fbsStatus",fbsStatus);
            }
        }
        return getDynamicBySQL(input, sql.toString(), TPpBisdtlclaimVO.class,"", false,params);
    }
    public List<TpMerchantVO> getMchId(String paytype, String corpAuthList) {
        Map<String,Object> params=new HashMap<>();
        StringBuilder sql=new StringBuilder("select ");
        sql.append(" m.ID,m.parent_Id as parentId,m.corp_id as corpId,m.merchant_No as merchantNo,m.PAY_TYPE as PAYTYPE ");
        sql.append(" from TP_MERCHANT m ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" where 1=1   ");
        if(corpAuthList.length()>0){
            sql.append(" and m.corp_id in (:corpIds) ");
        }
        params.put("corpIds",corpAuthList);
        sql.append("and m.PAY_TYPE=:paytype");
        params.put("paytype",paytype);
        sql.append(" order by bt_nvl_num(m.sort,0),c.code,m.merchant_No ");
        return listBySQLAliasToBean(sql.toString(), params, TpMerchantVO.class);
    }
    /**
     * 根据id查找银行账户流水信息
     *
     * @param id
     * @return BtBankAccDtlVO
     */
    public TPpBisdtlclaimVO getByUnion(String id) {
        StringBuilder sql=new StringBuilder("select ");
        sql.append(" d.id,d.appId,d.mchId,d.serialNo,d.trandNo,d.payAcc,d.fbsStatus,d.tradeType,");
        sql.append("case when d.trandMoney>0 then 0 else 1 end as cdSign,");
        sql.append("d.trandTime,d.trandMoney,m.corp_id as corpId,m.PAY_TYPE as payType,d.busType,t.bus_type_name as busTypeName ");
        sql.append(" from  (select id,\n" +
                "                   '003'          as payType,\n" +
                "                   APP_ID         as appId,\n" +
                "                   MCH_ID         as mchId,\n" +
                "                   TRANSACTION_ID as serialNo,\n" +
                "                   TRADE_NO       as trandNo,\n" +
                "                   TRADE_TYPE     as tradeType,\n" +
                "                   null           as payAcc,\n" +
                "                   TRADE_TIME     as trandTime,\n" +
                "                   ORDER_FEE      as trandMoney,\n" +
                "                   FBS_STATUS     as fbsStatus,\n" +
                "                   BUS_TYPE       as busType\n" +
                "            from TP_WEIXINPAY_DTL\n" +
                "            union all\n" +
                "            select id,\n" +
                "                   '002'                                                           as payType,\n" +
                "                   APP_ID                                                          as appId,\n" +
                "                   MCH_ID                                                          as mchId,\n" +
                "                   ACCOUNT_ID                                                      as serialNo,\n" +
                "                   TRADE_NO                                                        as trandNo,\n" +
                "                   TRADE_TYPE                                                      as tradeType,\n" +
                "                   PAY_ACC                                                         as payAcc,\n" +
                "                   HAPPEN_TIME                                                     as trandTime,\n" +
                "                   CASE WHEN PAYEE_MONEY = 0 THEN PAYER_MONEY ELSE PAYEE_MONEY END AS trandMoney,\n" +
                "                   FBS_STATUS                                                      as fbsStatus,\n" +
                "                   BUS_TYPE                                                        as busType\n" +
                "            from TP_ALIPAY_DTL) d ");
        sql.append(" left join tp_merchant_var mv on mv.var_code in ( :appIds ) and mv.var_value_str = d.").append(TtPayUtil.APPID).append(" ");
        sql.append(" left join tp_merchant m on m.id = mv.mch_id and d.payType = m.PAY_TYPE ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" left join nis_bus_type t on d.busType = t.id ");
        sql.append(" where  d.id = :id and m.valid_sign = :validSign ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("appIds", TtPayConstant.APPIDS);
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        return getBySQLAliasToBean(sql.toString(), params, TPpBisdtlclaimVO.class);
    }
    /**
     * 根据id 更新微信
     * @param id
     * @param fbsStatus
     * @param busType
     * @return
     */
    public int updateWeiXinById(String id, String fbsStatus,String busType){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" update TP_WEIXINPAY_DTL set FBS_STATUS = :fbsStatus,bus_type = :busType where id = :id ");
        params.put("id",id);
        params.put("fbsStatus",fbsStatus);
        params.put("busType",busType);
        return this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 根据id 更新支付宝
     * @param id
     * @param fbsStatus
     * @param busType
     * @return
     */
    public int updateAliById(String id, String fbsStatus,String busType){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" update TP_ALIPAY_DTL set FBS_STATUS = :fbsStatus,bus_type = :busType where id = :id ");
        params.put("id",id);
        params.put("fbsStatus",fbsStatus);
        params.put("busType",busType);
        return this.batchSQLExecute(sql.toString(), params);
    }
}
