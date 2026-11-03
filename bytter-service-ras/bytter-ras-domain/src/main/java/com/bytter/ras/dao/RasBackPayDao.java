package com.bytter.ras.dao;

import com.bytter.ras.entity.RasBackPay;
import com.bytter.ras.vo.RasBackPayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核退款管理dao
 * @date 2019/5/9 13:39
 */
@Repository
public class RasBackPayDao extends BaseDao<RasBackPay,String> {
    /**
     * 查询退款单信息通用sql
     */
    public StringBuilder getCommonSql(){
        StringBuilder sb = new StringBuilder();
        sb.append("select rb.id,rb.corp_id as corpId,sc.code as corpCode,sc.name as corpName,rb.shop_id as shopId,rs.name as shopName,rb.back_no as backNo,");
        sb.append("rb.back_date as backDate,rb.back_money as backMoney,rb.order_no as orderNo,rb.order_date as orderDate,");
        sb.append("rb.order_money as orderMoney,rb.rec_money as recMoney,rb.pay_type as payType,rb.cust_name as custName,");
        sb.append("rb.cust_acc as custAcc,rb.rmk,rb.status,rb.send_sign as sendSign,rb.return_msg as returnMsg from ");
        sb.append("ras_back_pay rb left join ras_shop rs on rb.shop_id=rs.id left join sys_corp sc on rs.corp_id=sc.id where 1=1");
        return sb;
    }

    /**
     * 退款申请处理列表加载
     * @param dataTablesInput
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,
              List<String> corpAuthList,List<String> shopAuthList) {
        StringBuilder sql = this.getCommonSql();
        Map<String,Object> params=new HashMap<>();
        String shopAuthSql= SqlUtil.getInSqlByParams(shopAuthList, "rb.shop_id","shopId", params);
        sql.append(" and ").append(shopAuthSql).append(" ");
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "rb.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(dataTablesInput,sql.toString() , RasBackPayVO.class, "rb", false,params);
    }

    /**
     * 退款申请查询列表加载
     * @param dataTablesInput
     */
    public DataTablesOutput getDataTablesUnionQuery(DataTablesInput dataTablesInput,
              List<String> corpAuthList,List<String> shopAuthList) {
        StringBuilder sql = this.getCommonSql();
        sql.append(" and rb.send_sign is not null");
        Map<String,Object> params=new HashMap<>();
        String shopAuthSql= SqlUtil.getInSqlByParams(shopAuthList, "rb.shop_id","shopId", params);
        sql.append(" and ").append(shopAuthSql).append(" ");
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "rb.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(dataTablesInput,sql.toString() , RasBackPayVO.class, "rb", false,params);
    }
}
