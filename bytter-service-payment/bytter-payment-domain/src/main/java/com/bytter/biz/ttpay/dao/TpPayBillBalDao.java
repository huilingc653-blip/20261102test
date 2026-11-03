package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.util.TtPayUtil;
import com.bytter.biz.ttpay.vo.TpPayBillBalVO;
import com.bytter.biz.ttpay.vo.TpPaymentVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author tongqr
 * @version 1.0
 * @Title: DAO
 * @Description: 商户信息对账单明细Dao
 * @date 2022-02-15
 */
@Repository
public class TpPayBillBalDao extends BaseDao<T, String> {
    /**
     * @param input
     * @return DataTablesOutput    返回类型
     * @throws
     * @Title: 商户信息余额查询信息查询列表
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" d.id,d.mchId,d.balDate,d.totalAmount,d.availableAmount, d.freezeAmount,m.corp_id as corpId,m.PAY_TYPE as payType,m.merchant_no as merchantNo ");
        sql.append(" from ( select id, '003'  as payType, APP_ID  as mchId, BAL_DATE as balDate,null as totalAmount,AVAILABLE_AMOUNT as availableAmount,PENDING_AMOUNT as freezeAmount from TP_WEIXINPAY_BAL  ");
        sql.append(" union all select id, '002'  as payType, APP_ID  as mchId,BAL_DATE as balDate,TOTAL_AMOUNT as totalAmount,AVAILABLE_AMOUNT as availableAmount,FREEZE_AMOUNT as freezeAmount   from TP_ALIPAY_BAL) d");
        sql.append(" left join tp_merchant_var mv on mv.var_code in ( :appIds ) and mv.var_value_str = d.").append(TtPayUtil.MCHID).append(" ");
        sql.append(" left join tp_merchant m on m.id = mv.mch_id and d.payType = m.PAY_TYPE ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" where 1=1 and m.valid_sign = :validSign ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appIds", TtPayConstant.APPIDS);
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        if (corpAuthList == null || corpAuthList.size() < 1) {
            sql.append(" and 1!=1 ");
        } else {
            sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "m.corp_id", params));
        }
        return getDynamicBySQL(input, sql.toString(), TpPayBillBalVO.class, "", false, params);
    }
    /**
     * 获取指定商户支付宝、微信余额
     * @param corpId 单位id
     * @param merchantNo 商户号
     * @param payType 支付方式（002支付宝、003微信）
     * @return 余额对象
     */
    public TpPayBillBalVO getTpPayBillBal(String corpId, String merchantNo, String payType) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" d.id,d.mchId,d.balDate,d.totalAmount,d.availableAmount, d.freezeAmount,m.corp_id as corpId,m.PAY_TYPE as payType,m.merchant_no as merchantNo ");
        sql.append(" from ( select id, '003'  as payType, APP_ID  as mchId, BAL_DATE as balDate,null as totalAmount,AVAILABLE_AMOUNT as availableAmount,PENDING_AMOUNT as freezeAmount from TP_WEIXINPAY_BAL  ");
        sql.append(" union all select id, '002'  as payType, APP_ID  as mchId,BAL_DATE as balDate,TOTAL_AMOUNT as totalAmount,AVAILABLE_AMOUNT as availableAmount,FREEZE_AMOUNT as freezeAmount   from TP_ALIPAY_BAL) d");
        sql.append(" left join tp_merchant_var mv on mv.var_code in ( :appIds ) and mv.var_value_str = d.").append(TtPayUtil.MCHID).append(" ");
        sql.append(" left join tp_merchant m on m.id = mv.mch_id and d.payType = m.PAY_TYPE ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" where 1=1 and m.valid_sign = :validSign ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appIds", TtPayConstant.APPIDS);
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        if(StringUtils.isNotEmpty(corpId)){
            sql.append(" and m.corp_id = :corpId ");
            params.put("corpId", corpId);
        }
        if(StringUtils.isNotEmpty(merchantNo)){
            sql.append(" and m.merchant_no = :merchantNo ");
            params.put("merchantNo", merchantNo);
        }
        if(StringUtils.isNotEmpty(payType)){
            sql.append(" and m.PAY_TYPE = :payType ");
            params.put("payType", payType);
        }
        sql.append(" order by d.baldate desc ");
        List<TpPayBillBalVO> list = this.listBySQLAliasToBean(sql.toString(), params, TpPayBillBalVO.class);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
