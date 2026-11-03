package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranOptionDeal;
import com.bytter.biz.foreign.vo.FexTranOptionDealVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @Title: Dao
 * @Description: 交易管理处理管理处理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexTranOptionDealDao extends BaseDao<FexTranOptionDeal,String> {
    /**
     * 交易管理处理组表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql=new StringBuilder();
        sql.append("select t.id as id,t.tran_Req_Id as tranReqId,t.op_Date as opDate,t.op_Type as opType,t.market_Rate as marketRate,t.avg_Rate as avgRate,");
        sql.append(" t.tran_Settle_Method as tranSettleMethod,t.pro_Id as proId,p.pro_Name as proName,t.corp_Id as corpId,t.cotp_Id as cotpId,c.cotp_Name as cotpName,");
        sql.append(" t.settle_Date as settleDate,t.cur_Id as curId,t.settle_Money as settleMoney,t.refund_Date as refundDate,t.refund_Money as refundMoney,");
        sql.append(" t.tran_Addr as tranAddr,t.tran_Addr_Id as tranAddrId,t.rmk as rmk,");
        sql.append(" t.create_By as createBy, t.create_Date as createDate, t.update_By as updateBy, t.update_Date as updateDate ");
        sql.append(" from fex_tran_option_deal t left join fex_tran_req r on t.tran_Req_Id=r.id");
        sql.append(" left join fex_product p on t.pro_Id=p.id");
        sql.append(" left join fex_cotp c on t.cotp_Id=c.id");
        sql.append(" where 1=1");
        return sql.toString();
    }

    /**
     * 根据ID获取交易管理处理信息
     * @param id
     * @return
     */
    public FexTranOptionDealVO getByIdUnion(String id){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexTranOptionDealVO.class);
    }

    /**
     * 根据交易ID获取交易管理处理信息
     * @param opType
     * @param tranReqId
     * @return
     */
    public FexTranOptionDealVO getByTranReqIdUnion(String opType, String tranReqId){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.op_Type=:opType and t.tran_Req_Id=:tranReqId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("opType", opType);
        params.put("tranReqId", tranReqId);
        return getBySQLAliasToBean(sql.toString(), params, FexTranOptionDealVO.class);
    }

    /**
     * 根根据交易ID获取交易管理处理信息
     * @param tranReqId
     * @return
     */
    public List<FexTranOptionDealVO> listVOByTranReqId(String tranReqId){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.tran_Req_Id=:tranReqId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tranReqId", tranReqId);
        sql.append(" order by t.create_date");
        return listBySQLAliasToBean(sql.toString(), params, FexTranOptionDealVO.class);
    }
}
