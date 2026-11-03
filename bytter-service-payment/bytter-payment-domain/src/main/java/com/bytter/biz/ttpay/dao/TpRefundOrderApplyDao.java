package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpRefundOrder;
import com.bytter.biz.ttpay.util.TtPayUtil;
import com.bytter.biz.ttpay.vo.TpPayBillDtlVO;
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
 * @Title: TpRefundOrderApplyDao
 * @Description: 退款申请
 * @author wangzy
 * @date 2019/10/10 10:33
 *
 */
@Repository
public class TpRefundOrderApplyDao extends BaseDao<TpRefundOrder,String> {
    /**
     * 退款申请列表查询
     * @param  input
     * @param  corpAuthList 单位List
     * @return DataTablesOutput  返回类型
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getApplyDataTablesUnion(DataTablesInput input, List<String> corpAuthList){
        // 设置结束时间
        String etime = (String)input.getFormdata().get("led_trandTime");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            input.getFormdata().put("led_trandTime", etime);
        }
        // 查询退款申请sql，当appStatus为11时审批中，其它为未申请
        StringBuilder sql=new StringBuilder("select");
        sql.append(" d.id,d.mchId,d.serialNo,d.trandNo,d.payAcc,d.trandTime,d.trandMoney,m.corp_id as corpId,");
        sql.append(" m.merchant_no as merchantNo,case when t.app_status=:appStatus then '11' else '0' end  as appStatus, ");
        sql.append(" t.id as refundId,BT_NVL_NUM(o.refundedMoney,0) as refundedMoney,");
        sql.append(" m.PAY_TYPE as PAYTYPE from ").append(TtPayUtil.getAllFieldSql()).append(" d left join ");
        sql.append("  TP_REFUND_ORDER t on t.dtl_Id=d.id and t.app_Status=:appStatus left join tp_merchant m ");
        sql.append(" on m.merchant_no=d.").append(TtPayUtil.MCHID).append(" ");
        sql.append(" and d.payType=m.PAY_TYPE");
        sql.append("  left join (select tp.dtl_Id as dtlId,sum(tp.back_money) as refundedMoney from TP_REFUND_ORDER tp");
        sql.append("  where tp.voucher_stat=:voucherStat group by tp.dtl_Id) o on o.dtlId=d.id ");
        sql.append(" where 1=1 ");
        String appStatus = (String)input.getFormdata().get("ins_appStatus");
        // 当查询条件不为11时，要根据审批状态进行查询
        if(!StringUtils.equals(TtPayConstant.TTPAY_REFUND_APPLY_STATUS11,appStatus)){
            if(StringUtils.equals(TtPayConstant.TTPAY_REFUND_APPLY_STATUS0,appStatus)){
                // 查询审批条件为未申请时则状态不为11
                sql.append(" and t.app_Status is null");
            }
            // 去掉查询条件
            input.getFormdata().remove("ins_appStatus");
        }
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("appStatus",TtPayConstant.TTPAY_REFUND_APPLY_STATUS11);
        params.put("voucherStat",TtPayConstant.TTPAY_REFUND_VOUCHER_STATUS0);
        sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "m.corp_id", params));
        return getDynamicBySQL(input, sql.toString(), TpPayBillDtlVO.class,"", false,params);
    }
    /**
     * 查看交易的全部退款数据
     * @param dtlId 交易流水id
     * @param isApprove 是否只查审批中数据
     * @return 退款数据
     *
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<TpRefundOrderVO> listRefundInfo(String dtlId,boolean isApprove){
        StringBuilder sql=new StringBuilder();
        sql.append("select t.back_no as backNo,t.create_Date as backDate,t.msg as msg, ");
        sql.append(" t.app_Status as appStatus,t.voucher_Stat as voucherStat,BT_CONCAT4('(',t.create_By,')',t.create_name) as createByAndName,");
        sql.append(" t.back_Money as backMoney,t.rmk as rmk,t.id as id from TP_REFUND_ORDER t where t.dtl_id=:dtlId");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("dtlId", dtlId);
        if(isApprove){
            sql.append(" and t.app_Status = :appStatus");
            params.put("appStatus",TtPayConstant.TTPAY_REFUND_APPLY_STATUS11);
        }
        sql.append(" order by t.create_Date ");
        List<TpRefundOrderVO> listInfo=this.listBySQLAliasToBean(sql.toString(), params, TpRefundOrderVO.class);
        if(listInfo!=null){
            return listInfo;
        }
        return null;
    }
    /**
     * 加载审批列表数据
     *
     * @param dataTablesInput 查询条件
     * @param businessList 审批中流水
     * @return 返回列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput,List<String> businessList) {
        // 设置结束时间
        String etime = (String)dataTablesInput.getFormdata().get("led_backDate");
        if(StringUtils.isNotBlank(etime)){
            etime = etime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_backDate", etime);
        }
        StringBuilder sql=new StringBuilder("select t.id as id,t.ret_source as retSource,t.cust_name as custName, ");
        sql.append(" t.create_Date as backDate,m.corp_id as corpId,m.PAY_TYPE as payType,d.serialNo,");
        sql.append("d.mchId as mchId,t.back_No as backNo,t.order_No as orderNo,t.rmk as rmk,d.trandNo as trandNo,d.id as dtlId,");
        sql.append(" d.trandMoney,t.status as status,t.back_money as backMoney,t.refundable_amount as refundableAmount, ");
        sql.append(" m.merchant_no as merchantNo from TP_REFUND_ORDER t  left join ").append(TtPayUtil.getAllFieldSql());
        sql.append(" d on t.dtl_id=d.id ").append(" left join tp_merchant m ");
        sql.append(" on m.merchant_no=d.").append(TtPayUtil.MCHID).append(" ");
        sql.append(" and d.payType=m.PAY_TYPE ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" where 1=1 ");
        Map<String,Object> param = new HashMap<String,Object>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "t.id", param);
        sql.append(" and ").append(businessSql).append(" ");
        DataTablesOutput dataOutInfo=this.getDynamicBySQL(dataTablesInput, sql.toString(), TpRefundOrderVO.class, "", false,param);
        return dataOutInfo;
    }
    /**
     * 手机审批查询退款信息
     * @param id 退款id
     * @return 退款数据
     *
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TpRefundOrderVO getRefundInfo(String id){
        StringBuilder sql=new StringBuilder();
        sql.append("select t.back_no as backNo,t.create_Date as backDate,m.corp_id as corpId, ");
        sql.append(" d.mchId as mchId,t.BACK_MONEY as backMoney,t.create_name as createName,t.ORDER_NO as trandNo,");
        sql.append(" t.CUST_NAME as custName,t.CUST_ACC as custAcc,t.RET_SOURCE as retSource, ");
        sql.append(" t.ORDER_DATE as orderDate,t.ORDER_MONEY as trandMoney,t.rec_Money as recMoney,t.PAY_TYPE as payType, ");
        sql.append(" t.REFUNDABLE_AMOUNT as refundableAmount,t.rmk as rmk,d.serialNo as serialNo from TP_REFUND_ORDER t left join ");
        sql.append(" ").append(TtPayUtil.getAllFieldSql()).append(" d on t.dtl_id=d.id ");
        //sql.append(" left join sys_corp c on c.id=t.corp_id ");
        sql.append(" where t.id=:id");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("id", id);
        List<TpRefundOrderVO> listInfo=this.listBySQLAliasToBean(sql.toString(), params, TpRefundOrderVO.class);
        if(listInfo!=null){
            return listInfo.get(0);
        }
        return null;
    }
    /**
     * 根据单位id获取此单位下有没有处理中的退款信息
     * @param corpId
     * @return 有效退款数据
     */
    public int getTpRefundByCorpId(String corpId){
        StringBuilder sql = new StringBuilder(" select count(*) from ");
        sql.append(" TP_REFUND_ORDER where ");
        sql.append(" corp_id=:corpId and APP_STATUS = :appStatus ");
        Map<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("appStatus", TtPayConstant.TTPAY_REFUND_APPLY_STATUS11);
        return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
    }
}
