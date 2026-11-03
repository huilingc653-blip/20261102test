package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpRefundOrder;
import com.bytter.biz.ttpay.util.TtPayUtil;
import com.bytter.biz.ttpay.vo.TpRefundOrderVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: TpRefundOrderQueryDao
 * @Description: 退款查询
 * @author wangzy
 * @date 2019/10/11 9:00
 *
 */
@Repository
public class TpRefundOrderQueryDao extends BaseDao<TpRefundOrder,String> {
    /**
     * 退款申请列表查询
     * @param  input
     * @param  corpAuthList 单位List
     * @return DataTablesOutput  返回类型
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput input, List<String> corpAuthList){
        // 设置结束时间
        String etime = (String)input.getFormdata().get("led_backDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            input.getFormdata().put("led_backDate", etime);
        }
        StringBuilder sql=new StringBuilder("select t.id as id,t.ret_source as retSource,m.PAY_TYPE as payType,t.update_Name as updateName, ");
        sql.append(" t.back_Date as backDate,m.corp_id as corpId,t.create_Name as createName,t.return_Msg as returnMsg,");
        sql.append("d.mchId as mchId,t.back_No as backNo,t.order_No as orderNo,t.rmk as rmk,d.trandNo as trandNo,d.id as dtlId,t.check_Name as checkName,");
        sql.append(" d.trandMoney,t.status as status,BT_NVL_NUM(t.back_money,0) as backMoney,t.cust_name as custName,t.update_Date as updateDate,");
        sql.append(" t.voucher_Stat as voucherStat,m.merchant_no as merchantNo from TP_REFUND_ORDER t  left join ").append(TtPayUtil.getAllFieldSql());
        sql.append(" d on t.dtl_id=d.id left join TP_ALIPAY_DTL a on a.id=t.dtl_Id ").append(" left join tp_merchant m ");
        sql.append(" on m.merchant_no=d.").append(TtPayUtil.MCHID).append(" ");
        sql.append(" and d.payType=m.PAY_TYPE ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" where 1=1 ");
        Map<String,Object> params=new HashMap<String,Object>();
        sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "m.corp_id", params));
        return getDynamicBySQL(input, sql.toString(), TpRefundOrderVO.class,"", false,params);
    }
}
