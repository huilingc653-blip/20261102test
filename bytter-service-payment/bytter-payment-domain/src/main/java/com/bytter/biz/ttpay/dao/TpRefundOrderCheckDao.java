package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpRefundOrder;
import com.bytter.biz.ttpay.util.TtPayUtil;
import com.bytter.biz.ttpay.vo.TpRefundOrderVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: TpRefundOrderAuditDao
 * @Description: 退款确认
 * @author wangzy
 * @date 2019/10/11 9:01
 *
 */
@Repository
public class TpRefundOrderCheckDao extends BaseDao<TpRefundOrder,String> {
    /**
     * 退款确认列表查询
     * @param  input
     * @param  corpAuthList 单位List
     * @return DataTablesOutput  返回类型
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getCheckDataTablesUnion(DataTablesInput input, List<String> corpAuthList){
        // 设置结束时间
        String etime = (String)input.getFormdata().get("led_backDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            input.getFormdata().put("led_backDate", etime);
        }
        StringBuilder sql=new StringBuilder("select t.id as id,t.ret_source as retSource,t.cust_name as custName,m.PAY_TYPE as payType, ");
        sql.append(" t.create_Date as backDate,m.corp_id as corpId,");
        sql.append("d.mchId as mchId,t.back_No as backNo,t.order_No as orderNo,t.rmk as rmk,d.trandNo as trandNo,d.id as dtlId,");
        sql.append(" d.trandMoney,t.status as status,t.back_money as backMoney,t.refundable_amount as refundableAmount, ");
        sql.append(" m.merchant_no as merchantNo from TP_REFUND_ORDER t  left join ").append(TtPayUtil.getAllFieldSql());
        sql.append(" d on t.dtl_id=d.id left join TP_ALIPAY_DTL a on a.id=t.dtl_Id ").append(" left join tp_merchant m ");
        sql.append(" on m.merchant_no=d.").append(TtPayUtil.MCHID).append(" ");
        sql.append(" and d.payType=m.PAY_TYPE ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" where 1=1 ");
        sql.append(" and t.status = :status ");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("status", TtPayConstant.TTPAY_REFUND_CHECK_STATUS00);
        sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "m.corp_id", params));
        return getDynamicBySQL(input, sql.toString(), TpRefundOrderVO.class,"", false,params);
    }
    /**
     * 查看交易的全部退款数据
     * @param id 退款id
     * @return 退款数据
     *
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<TpRefundOrderVO> listRefundInfo(String id){
        StringBuilder sql=new StringBuilder();
        sql.append("select t.back_no as backNo,t.create_Date as backDate,t.app_Status as appStatus, ");
        sql.append(" t.voucher_Stat as voucherStat,BT_CONCAT4('(',t.create_By,')',t.create_name) as createByAndName,");
        sql.append(" t.back_Money as backMoney,t.rmk as rmk,t.id as id from TP_REFUND_ORDER t where t.id=:id");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("id", id);
        List<TpRefundOrderVO> listInfo=this.listBySQLAliasToBean(sql.toString(), params, TpRefundOrderVO.class);
        if(listInfo!=null){
            return listInfo;
        }
        return null;
    }
}
