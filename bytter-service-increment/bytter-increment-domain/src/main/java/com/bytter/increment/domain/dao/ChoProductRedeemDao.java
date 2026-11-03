package com.bytter.increment.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.entity.ChoProductRedeemDetail;
import com.bytter.increment.domain.vo.ChoProductRedeemDetailVO;
import com.bytter.increment.domain.vo.ChoProductRedeemListVo;
import com.bytter.increment.domain.vo.ChoProductRedeemVo;
import com.bytter.increment.domain.vo.v1.ChoProductRedeemSelectDetailVO;
import org.springframework.stereotype.Repository;

import java.util.*;
/**
 * @Title: Dao
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Repository
public class ChoProductRedeemDao extends BaseDao<ChoProductRedeem,String> {
    /**
     * 获取列表数据
     * @param dataTablesInput
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select prd.id as id,prd.bill_no as billNo,prd.corp_id as corpId,pt.bank_type_id as bankTypeId,prd.bank_name as bankName,");
        sql.append(" pt.product_code as productCode,pt.product_name as productName,pt.product_type as productType,prd.bank_acc as bankAcc,pt.cur_id as curId, ");
        sql.append(" pt.FINANCIAL_TYPE as financialType,prd.status as status,sc.name as corpName,bt.type_name as bankTypeName,cur.cur_name as curName ");
        sql.append(" from cho_product_redeem prd ");
        sql.append(" left join sys_corp sc on sc.id = prd.corp_id ");
        sql.append(" left join cho_product_type pt on pt.product_code = prd.product_code ");
        sql.append(" left join BT_CURRENCY cur on cur.id = pt.CUR_ID ");
        sql.append(" left join BT_BANK_TYPE bt on bt.id = pt.BANK_TYPE_ID ");
        sql.append(" where 1 = 1 and prd.status in ('0','-1') ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput,sql.toString(), ChoProductRedeemListVo.class,"prd",false);
    }
    /**
     * 获取列表数据
     * @param dataTablesInput
     */
    public DataTablesOutput getDataTablesUnionForSelect(DataTablesInput dataTablesInput,String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pr.id as id, pr.bill_no as billNo, pr.corp_id as corpId, pt.bank_type_id as bankTypeId, ");
        sql.append(" pr.bank_name as bankName,pt.product_code as productCode,pt.product_name as productName, ");
        sql.append(" pt.product_type as productType, pr.bank_acc as bankAcc, pt.cur_id as curId, ");
        sql.append(" prd.redeem_money as redeemMoney, prd.redeem_date as redeemDate, prd.income_rate as incomeRate,  ");
        sql.append(" prd.income_money as incomeMoney,prd.sub_fee_money as subFeeMoney, prd.redeem_charge_money as redeemChargeMoney,");
        sql.append(" prd.charge_money as chargeMoney, pr.status as status ");
        sql.append(" from cho_product_redeem pr ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" left join cho_product_redeem_detail prd on pr.id = prd.cpr_id ");
        sql.append(" where 1 = 1 and pr.status in ('95') and (pr.COLLECTION_CONFIRM = '0' or pr.COLLECTION_CONFIRM is null)");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput,sql.toString(), ChoProductRedeemSelectDetailVO.class,"pr",false);
    }
    /**
     * 根据ID获取Vo显示实体
     * @param id
     * @return
     */
    public ChoProductRedeemVo getChoProductRedeemVoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select prd.id as id,prd.bill_no as billNo,prd.corp_id as corpId,pt.bank_type_id as bankTypeId,prd.bank_name as bankName,");
        sql.append(" pt.product_code as productCode,pt.product_name as productName,pt.product_type as productType,prd.bank_acc as bankAcc,pt.cur_id as curId, ");
        sql.append(" pt.FINANCIAL_TYPE as financialType,prd.status as status, prd.IS_STORE as isStore, prd.CERTIFICATE_WAY as certificateWay ");
        sql.append(" from cho_product_redeem prd ");
        sql.append(" left join cho_product_type pt on pt.product_code = prd.product_code ");
        sql.append(" where 1 = 1 ");
        sql.append(" and prd.id = :id ");
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        return getBySQLAliasToBean(sql.toString(),params,ChoProductRedeemVo.class);
    }
    /**
     * 审批列表数据
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select prd.id as id,prd.bill_no as billNo,prd.corp_id as corpId,pt.bank_type_id as bankTypeId,prd.bank_name as bankName,");
        sql.append(" pt.product_code as productCode,pt.product_name as productName,pt.product_type as productType,prd.bank_acc as bankAcc,pt.cur_id as curId, ");
        sql.append(" pt.FINANCIAL_TYPE as financialType,prd.status as status");
        sql.append(" from cho_product_redeem prd ");
        sql.append(" left join cho_product_type pt on pt.product_code = prd.product_code ");
        sql.append(" where 1 = 1 and prd.status = 11 and ");
        if (StringUtils.isNotEmpty(businessSql)) {
            sql.append(businessSql);
        }
        return getDynamicBySQL(dataTablesInput,sql.toString(),ChoProductRedeemVo.class,"prd",false);
    }
    /**
     * 查找赎回状态为待审批和审批通过的指定赎回产品
     * @param reqId
     * @return
     */
    public List<ChoProductRedeemDetail> listProductRedeemed(String reqId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.* from cho_product_redeem prd left join cho_product_redeem_detail d on prd.id = d.cpr_id  ");
        sql.append(" where d.req_id = :reqId and prd.status in ('11','95') ");
        Map<String,Object> params = new HashMap<>();
        params.put("reqId",reqId);
        return listBySQLAliasToBean(sql.toString(),params,ChoProductRedeemDetail.class);
    }
    /**
     * 查找赎回状态为待审批和审批通过的指定赎回产品
     * @param reqId
     * @return
     */
    public List<ChoProductRedeem> listProductRedeemedByReqId(String reqId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select prd.* from cho_product_redeem prd left join cho_product_redeem_detail d on prd.id = d.cpr_id  ");
        sql.append(" where d.req_id = :reqId and prd.status in ('11','95') ");
        Map<String,Object> params = new HashMap<>();
        params.put("reqId",reqId);
        return listBySQLAliasToBean(sql.toString(),params,ChoProductRedeem.class);
    }
    /**
     * 查找赎回状态为审批通过的指定赎回产品
     * @param reqId
     * @return
     */
    public List<ChoProductRedeemDetail> listProductRedeemApproved(String reqId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  d.* from cho_product_redeem prd left join cho_product_redeem_detail d on prd.id = d.cpr_id ");
        sql.append(" where d.req_id = :reqId and prd.status = 95 order by d.redeem_date,prd.create_date ");
        Map<String,Object> params = new HashMap<>();
        params.put("reqId",reqId);
        return listBySQLAliasToBean(sql.toString(),params, ChoProductRedeemDetail.class);
    }
    /**
     * 查询指定登记id且赎回状态为审批通过的赎回记录
     *
     * @param reqId 登记id
     * @param date  最新净值维护日期
     * @return 赎回集合
     */
    public List<ChoProductRedeem> listProductRedeemApproved(String reqId, Date date) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from cho_product_redeem prd where prd.req_id = :reqId and prd.status = 95  ");
        sql.append(" and prd.redeem_date = :redeemDate");
        sql.append(" order by prd.redeem_date,prd.create_date ");
        Map<String,Object> params = new HashMap<>();
        params.put("reqId",reqId);
        params.put("redeemDate",date);
        return listBySQLAliasToBean(sql.toString(),params,ChoProductRedeem.class);
    }
    /**
     * 查找赎回状态为审批通过的指定赎回产品
     * @param reqIds
     * @return
     */
    public List<ChoProductRedeem> listRedeemedApprovedByReqIds(Set<String> reqIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.req_id as reqId, sum(d.REDEEM_MONEY) as redeemMoney ");
        sql.append(" from cho_product_redeem prd left join cho_product_redeem_detail d on prd.id = d.cpr_id ");
        sql.append(" where d.req_id in (:reqIds) and prd.status = 95 group by d.req_id ");
        Map<String,Object> params = new HashMap<>();
        params.put("reqIds",reqIds);
        return listBySQLAliasToBean(sql.toString(),params,ChoProductRedeem.class);
    }
    /**
     * 获取指定登记产品的赎回数据
     * @param reqId
     */
    public List<ChoProductRedeem> listChoProductRedeemByReqId(String reqId) {
        StringBuilder hql = new StringBuilder();
        // hql.append(" from ChoProductRedeem prd where prd.reqId = :reqId order by prd.redeemDate desc");
        hql.append(" select * from cho_product_redeem prd where prd.req_Id = :reqId order by prd.redeem_Date desc");
        Map<String, Object> params = new HashMap<>();
        params.put("reqId", reqId);
        return listBySQLAliasToBean(hql.toString(), params, ChoProductRedeem.class);
    }
    /**
     * 获取指定登记产品的赎回数据(去掉已删除的记录)
     * @param reqId
     */
    public List<ChoProductRedeem> listRedeemUnlessDelByReqId(String reqId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM CHO_PRODUCT_REDEEM PRD WHERE PRD.REQ_ID = :reqId ");
        sql.append(" and prd.status <> -2 order by prd.redeem_date desc ");
        Map<String, Object> params = new HashMap<>(2);
        params.put("reqId",reqId);
        return listBySQLAliasToBean(sql.toString(),params,ChoProductRedeem.class);
    }
    public List<ChoProductRedeemDetail> listByReqIdRedeemDate(String reqId, Date redeemDate){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.* FROM CHO_PRODUCT_REDEEM PRD left join CHO_PRODUCT_REDEEM_detail d on PRD.id = d.cpr_id  ");
        sql.append(" where d.req_id = :reqId and d.redeem_date<= :redeemDate ");
        sql.append(" and prd.status = 95 order by d.redeem_date ");
        Map params = new HashMap();
        params.put("reqId",reqId);
        params.put("redeemDate",redeemDate);
        return listBySQLAliasToBean(sql.toString(),params,ChoProductRedeemDetail.class);
    }
    /**
     * 根据单位统计登记的理财信息
     * @param corpId
     * @return
     */
    public int getProductRedeemCountByCorpId(String corpId){
        String sql="select count(*) from cho_product_redeem where corp_id=:corpId";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql+=" and status in ('0','11','-1')";
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }

    /**
     * 查找赎回状态为审批通过的指定赎回产品
     * @param reqId
     * @return
     */
    public List<ChoProductRedeemDetailVO> listProductRedeemDetailVOApproved(String reqId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  d.*,prd.status from cho_product_redeem prd left join cho_product_redeem_detail d on prd.id = d.cpr_id ");
        sql.append(" where d.req_id = :reqId and prd.status = 95 order by d.redeem_date,prd.create_date ");
        Map<String,Object> params = new HashMap<>();
        params.put("reqId",reqId);
        return listBySQLAliasToBean(sql.toString(),params, ChoProductRedeemDetailVO.class);
    }

}
