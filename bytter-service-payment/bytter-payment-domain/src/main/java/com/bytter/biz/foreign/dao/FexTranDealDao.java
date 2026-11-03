package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranDeal;
import com.bytter.biz.foreign.vo.FexTranDealVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @Title: Dao
 * @Description: 交易管理处理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexTranDealDao extends BaseDao<FexTranDeal,String> {
    /**
     * 交易组表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql=new StringBuilder();
        sql.append("select t.id as id,t.tran_Req_Id as tranReqId,r.bill_no as tranReqNo,t.corp_Id as corpId,t.delivery_Sign as deliverySign,t.old_Settle_Date as oldSettleDate,");
        sql.append(" t.new_Settle_Date as newSettleDate,t.settle_Money as settleMoney,t.settle_Rate as settleRate,t.wish_Rate as wishRate,t.bal as bal,");
        sql.append(" t.default_Date as defaultDate,t.cur_Id as curId,t.default_Money as defaultMoney,t.delivery_Rate as deliveryRate,");
        sql.append(" t.delivery_Date as deliveryDate,t.status as status,t.tran_Addr as tranAddr,t.tran_Addr_Id as tranAddrId,t.rmk as rmk,t.op_Type as opType,");
        sql.append(" t.create_By as createBy, t.create_Date as createDate, t.update_By as updateBy, t.update_Date as updateDate ");
        sql.append(" from fex_tran_deal t left join fex_tran_req r on t.tran_Req_Id=r.id");
        sql.append(" where 1=1");
        return sql.toString();
    }

    /**
     * 根据ID获取交易信息
     * @param id
     * @return
     */
    public FexTranDealVO getByIdUnion(String id){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexTranDealVO.class);
    }

    /**
     * 根据交易ID获取交易交割信息
     * @param opType
     * @param tranReqId
     * @return
     */
    public List<FexTranDeal> listByTranReqId(String opType, String tranReqId){
        StringBuilder hql=new StringBuilder(" from FexTranDeal t where 1=1 ");
        hql.append(" and t.opType=:opType and t.tranReqId=:tranReqId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("opType", opType);
        params.put("tranReqId", tranReqId);
        return listByHQLAliasToBean(hql.toString(), params, FexTranDeal.class);
    }

    /**
     * 根据交易ID获取交易交割信息
     * @param opType
     * @param tranReqId
     * @return
     */
    public List<FexTranDealVO> listVOByTranReqId(String opType, String tranReqId){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.op_Type=:opType and t.tran_Req_Id=:tranReqId");
        if(ForeignConstant.OP_TYPE_1.equals(opType)){
            sql.append(" and t.delivery_Sign = r.fixed_Money");
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("opType", opType);
        params.put("tranReqId", tranReqId);
        sql.append(" order by t.create_date");
        return listBySQLAliasToBean(sql.toString(), params, FexTranDealVO.class);
    }

    /**
     * 获取审批信息
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput<FexTranDealVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and " + businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexTranDealVO.class, "t", false);
    }

    /**
     * 根据单位统计交易需求
     * @param corpId
     * @return
     */
    public int getTranDealCountByCorpId(String corpId){
        String sql="select count(*) from fex_tran_app where corp_id=:corpId";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql+=" and status in ('0','11','-1')";
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
}
