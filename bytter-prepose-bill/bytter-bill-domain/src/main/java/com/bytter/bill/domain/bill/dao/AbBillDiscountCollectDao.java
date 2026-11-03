package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillDiscountCollectVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luop
 * @version 1.0
 * @Title: Dao
 * @Description: 票据贴现收回
 * @date 2017-09-05
 */
@Repository
public class AbBillDiscountCollectDao extends BaseDao<AbBillDiscountCollectVo, String> {
    /**
     * 列表显示应收票据贴现收回信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillDiscountCollect(DataTablesInput input, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" a.id as vid,b.id as vvid,c.id as aid,t.id as tid,a.bill_type as billtype,");
        sql.append(" b.corp_id as corpid,b.corp_code AS corpCode,b.corp_Name AS corpName,a.bill_flag as billflag,a.payer as payer,a.acceptor as acceptor,");
        sql.append(" a.orig_no as origno,a.bill_money as billmoney,a.start_date as startdate,a.end_date as enddate,");
        sql.append(" c.brief_id as briefid,(case b.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_6212 + "' then " + AccebillConstant.AB_BILL_BUS_STATUS_1 + " else c.status end)  as status,");
        sql.append(" t.paydate as paydate,t.deferdate as deferdate,t.daynum as daynum,t.interest as interest,t.buyerinterest_money as buyerinterestmoney,");
        sql.append(" t.real_money as realmoney,c.bankendrsmtmk as bankendrsmtmk,t.invc_nb as invcnb,t.txl_ctrct_nb as txlctrctnb,b.enter_stock as enterstock,");
        sql.append(" b.APPLY_CORP_ID as applyCorpId,b.apply_corp_name as applyCorpName,b.apply_corp_code as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel, ");
        sql.append(" cast(t.rpdopendt as DATE) as rpdopendt,cast(t.rpdduedt as DATE) as rpdduedt,t.interest_rates as interestrates,c.business_date as businessDate,");
		/*贴现收回办理前 前手 从上一流水获取(贴现办理流水)
		 贴现收回办理中 前手 从上一流水的上一流水获取(贴现办理流水)
		 rec_nm = '贴入人'*/
        sql.append(" (CASE c.brief_id WHEN '" + AccebillConstant.BRIEF_STATUS_6012 + "' THEN c1.rec_acctsvcr ELSE c2.rec_acctsvcr END) as aoaccninfAc, ");
        sql.append(" (CASE c.brief_id WHEN '" + AccebillConstant.BRIEF_STATUS_6012 + "' THEN c1.rec_nm ELSE c2.rec_nm END) as aoaccninfName, ");
        sql.append(" BT_NVL_NUM(t2.interest,0) as returnInterest, ");//返还利息
        // 新一代票据列表查询新增字段 -- start
        sql.append(" a.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, a.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
        sql.append(" a.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, a.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");
        sql.append(" a.BL_STD_AMT AS blStdAmt, a.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ");
        sql.append(" a.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, a.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ");
        sql.append(" b.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo,  ");
        // -- end
        //		--前手
        sql.append("case ");
        sql.append("when c.brief_id='6001' then c.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then c.rec_nm ");
        Map<String, Object> params = new HashMap<>();
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else b.cn_accrecords end as cnAccrecords, b.BUS_CORP_NAME as busCorpName");
        //		--前手
        sql.append(" from ab_voucherinfo_vice b ");
        sql.append("  left join ab_voucherinfo a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del') ");
        sql.append("  left join ab_accrecords c on b.id = c.vvid");
        sql.append("  left join ab_accrecords c1 on c1.id = c.lastid ");
        sql.append("  left join ab_accrecords c2 on c2.id = c1.lastid ");
        sql.append("  left join ab_discount t on t.record_id = (CASE c.brief_id WHEN :briefId6012 THEN c1.id ELSE c2.id END) ");
        sql.append("  left join ab_accrecords c3 on c3.lastid = c1.id and c3.status = '11' and c3.valid_sign = '0' ");
        sql.append("  left join ab_discount t2 on t2.record_id = c3.id ");
        sql.append(" left join ab_brief_status bs on bs.status=c.brief_id ");
        sql.append(" where b.valid_sign=:validSign1 ");
        //纸票或者线下电票
        sql.append(" and (a.bill_flag = :billFlag10 or ( a.bill_flag = :billFlag11 ");
        sql.append(" and c.send_sign = :sendSignN) ) and c.inside_sing <> :insideSingY ");
        //审批状态查询
        if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS11).equals(input.getFormdata().get("eqs_statusName"))) {
            sql.append(" and c.valid_sign = :valid_sign0 and c.status = :status11 and c.brief_id=:backBriefId6212 ");
            input.getFormdata().remove("eqs_statusName");
            //可记账状态查询
        } else if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS94).equals(input.getFormdata().get("eqs_statusName"))) {
            sql.append(" and c.valid_sign = :valid_sign0 and c.status = :status94 and c.brief_id=:backBriefId6212 ");
            input.getFormdata().remove("eqs_statusName");
            //可收回查询
        } else if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS95).equals(input.getFormdata().get("eqs_statusName"))) {
            //回购式贴现(贴现到账状态)
            sql.append(" and t.rpdmk = :rpdmkRM01 and b.box_status = :boxStatusY and b.back_brief_id is null ");
            sql.append(" and c.brief_id = :briefId6012 and c.valid_sign = :validSign1 and c.status=:status95 ");
            //日期条件,纸票不做贴现赎回截止日限制
            sql.append(" and :sysdate >= t.rpdopendt   ");
            sql.append(" and :sysdate <= case a.bill_flag ");
            sql.append(" when :billFlag11 then t.rpdduedt else :sysdate end ");
            //默认加载列表
        } else {
            sql.append(" and (( ");
            //回购式贴现(贴现到账状态)
            sql.append(" t.rpdmk = :rpdmkRM01 and b.box_status = :boxStatusY ");
            sql.append(" and c.brief_id = :briefId6012 and c.valid_sign = :validSign1 and c.status=:status95 ");
            //日期条件,纸票不做贴现赎回截止日限制
            sql.append(" and :sysdate >= t.rpdopendt   ");
            sql.append(" and :sysdate <= case a.bill_flag ");
            sql.append(" when :billFlag11 then t.rpdduedt else :sysdate end  )");
            sql.append(" or ");
            sql.append(" ( c.valid_sign = :valid_sign0 and (c.status = :status11 or c.status = :status94) and c.brief_id=:backBriefId6212)) ");
            //打回状态查询
            if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS_1).equals(input.getFormdata().get("eqs_statusName"))) {
                sql.append(" and b.back_brief_id = :backBriefId6212 and t.id is not null ");
                input.getFormdata().remove("eqs_statusName");
            }
        }
        //sql.append(" and ").append(corpAuthSql).append(" ");
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("billFlag10", AccebillConstant.AB_BILL_FLAG_PAPER);
        params.put("billFlag11", Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));
        params.put("sendSignN", AccebillConstant.AB_BILL_SEND_SIGN_N);
        params.put("insideSingY", AccebillConstant.AB_INSIDE_SING_Y);
        params.put("valid_sign0", AccebillConstant.AB_BILL_VALID_SIGN_0);
        params.put("status11", AccebillConstant.AB_BILL_BUS_STATUS11);
        params.put("backBriefId6212", AccebillConstant.BRIEF_STATUS_6212);
        params.put("rpdmkRM01", AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK);
        params.put("boxStatusY", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("briefId6012", AccebillConstant.BRIEF_STATUS_6012);
        params.put("status94", AccebillConstant.AB_BILL_BUS_STATUS94);
        params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("sysdate", new Date(System.currentTimeMillis()));
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "b.apply_corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (b.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.bank_acc", "bankAccs", params)).append(")");
        }
        return this.getDynamicBySQL(input, sql.toString(), AbBillDiscountCollectVo.class, "", false, params);
    }
    /**
     * 加载审批页面列表数据信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbBillDiscountCollectVo> dataTableApprove(DataTablesInput dataTablesInput, List<String> businessList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" a.id as vid,b.id as vvid,c.id as aid,t.id as tid,a.bill_type as billtype,");
        sql.append(" b.corp_id as corpid,b.corp_code AS corpCode,b.corp_name AS corpName,a.bill_flag as billflag,a.payer as payer,a.acceptor as acceptor,");
        sql.append(" a.orig_no as origno,a.bill_money as billmoney,a.start_date as startdate,a.end_date as enddate,");
        sql.append(" c.brief_id as briefid,(case b.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_6212 + "' then " + AccebillConstant.AB_BILL_BUS_STATUS_1 + " else c.status end)  as status,");
        sql.append(" t.paydate as paydate,t.deferdate as deferdate,t.daynum as daynum,t.interest as interest,t.buyerinterest_money as buyerinterestmoney,");
        sql.append(" t.real_money as realmoney,c.bankendrsmtmk as bankendrsmtmk,t.invc_nb as invcnb,t.txl_ctrct_nb as txlctrctnb,b.enter_stock as enterstock,");
        sql.append(" b.APPLY_CORP_ID as applyCorpId,b.apply_corp_name as applyCorpName,b.apply_corp_code as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel, ");
        sql.append(" cast(t.rpdopendt as DATE) as rpdopendt,cast(t.rpdduedt as DATE) as rpdduedt,t.interest_rates as interestrates,c.business_date as businessDate,c.id as businessKey,");
        sql.append(" (CASE c.brief_id WHEN '" + AccebillConstant.BRIEF_STATUS_6012 + "' THEN c1.rec_acctsvcr ELSE c2.rec_acctsvcr END) as aoaccninfAc, ");
        sql.append(" (CASE c.brief_id WHEN '" + AccebillConstant.BRIEF_STATUS_6012 + "' THEN c1.rec_nm ELSE c2.rec_nm END) as aoaccninfName ");
        sql.append(" ,BT_NVL_NUM(t2.interest,0) as returnInterest ,b.BUS_CORP_NAME as busCorpName");//返还利息
        sql.append(" from ab_voucherinfo_vice b ");
        sql.append("  left join ab_voucherinfo a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del') ");
        sql.append("  left join ab_accrecords c on b.id = c.vvid");
        sql.append("  left join ab_accrecords c1 on c1.id = c.lastid ");
        sql.append("  left join ab_accrecords c2 on c2.id = c1.lastid ");
        sql.append("  left join ab_discount t on t.record_id = (CASE c.brief_id WHEN :briefId6012 THEN c1.id ELSE c2.id END) ");
        sql.append("  left join ab_accrecords c3 on c3.lastid = c1.id and c3.status = '11' and c3.valid_sign = '0' ");
        sql.append("  left join ab_discount t2 on t2.record_id = c3.id ");
        sql.append(" WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        params.put("briefId6012", AccebillConstant.BRIEF_STATUS_6012);
        String businessSql = SqlUtil.getInSqlByParams(businessList, "c.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return this.getDynamicBySQL(dataTablesInput, sql.toString(), AbBillDiscountCollectVo.class, "", false, params);
    }
    /**
     * 加载审批详细页面
     *
     * @param aid
     * @return
     */
    public AbBillDiscountCollectVo getAbBillDiscountCollectVoById(String aid, boolean isApprove) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" a.id as vid,b.id as vvid,c.id as aid,t.id as tid,a.bill_type as billtype,b.bank_acc as bankAcc,");
        sql.append(" a.payeracc as payeracc,a.acceptoracc as acceptoracc,a.payee as payee,a.payeeacc as payeeacc,");
        sql.append(" b.bank_name as bankName,b.corp_id as corpid,a.bill_flag as billflag,a.payer as payer,a.acceptor as acceptor,");
        sql.append(" a.orig_no as origno,a.bill_money as billmoney,a.start_date as startdate,a.end_date as enddate,c.apply_date as applyDate,");
        sql.append(" c.brief_id as briefid,(case b.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_6212 + "' then " + AccebillConstant.AB_BILL_BUS_STATUS_1 + " else c.status end)  as status,");
        sql.append(" t.paydate as paydate,t.deferdate as deferdate,t.daynum as daynum,t.interest as interest,t.buyerinterest_money as buyerinterestmoney,c.req_nm as reqNm,c.req_acct_id as reqAcctId, ");
        sql.append(" t.real_money as realmoney,c.bankendrsmtmk as bankendrsmtmk,t.invc_nb as invcnb,t.txl_ctrct_nb as txlctrctnb,b.enter_stock as enterstock,");
        sql.append(" b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_CORP_NAME as busCorpName,b.BUS_PERSONNEL as busPersonnel, ");
        sql.append(" cast(t.rpdopendt as DATE) as rpdopendt,cast(t.rpdduedt as DATE) as rpdduedt,t.interest_rates as interestrates,c.business_date as businessDate,c.id as businessKey,");
        sql.append(" (CASE c.brief_id WHEN '" + AccebillConstant.BRIEF_STATUS_6012 + "' THEN c1.rec_acctsvcr ELSE c2.rec_acctsvcr END) as aoaccninfAc, ");
        sql.append(" (CASE c.brief_id WHEN '" + AccebillConstant.BRIEF_STATUS_6012 + "' THEN c1.rec_nm ELSE c2.rec_nm END) as aoaccninfName ");
        sql.append(" ,BT_NVL_NUM(t2.interest,0) as returnInterest, ");//返还利息
        sql.append(" a.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, a.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");
        sql.append(" a.banendrsmtmk as banendrsmtmk,a.PAYER_BANK_NAME as payerBankName,a.PAYEE_BANK_NAME as payeeBankName, ");
        sql.append(" a.ACCEPTORBANKNO as acceptorbankno,a.ACCEPTOR_BANK_NAME as acceptorBankName, ");
        sql.append(" a.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,sc.name as corpName,a.BL_STD_AMT as blStdAmt ");
        sql.append(" from ab_voucherinfo_vice b ");
        sql.append(" left join sys_corp sc on sc.id = b.CORP_ID ");
        sql.append("  left join ab_voucherinfo a on a.id = b.vid");
        sql.append("  left join ab_accrecords c on b.id = c.vvid");
        sql.append("  left join ab_accrecords c1 on c1.id = c.lastid ");
        sql.append("  left join ab_accrecords c2 on c2.id = c1.lastid ");
        sql.append("  left join ab_discount t on t.record_id = (CASE c.brief_id WHEN :briefId6012 THEN c1.id ELSE c2.id END) ");
        sql.append("  left join ab_accrecords c3 on c3.lastid = c1.id and c3.status = '11' and c3.valid_sign = '0' ");
        if (isApprove) {
            sql.append("  left join ab_discount t2 on t2.record_id = c3.id ");
        } else {
            sql.append("  left join ab_discount t2 on t2.record_id = c.id ");
        }
        sql.append(" WHERE c.id = :aid ");
        Map<String, Object> params = new HashMap<>();
        params.put("briefId6012", AccebillConstant.BRIEF_STATUS_6012);
        params.put("aid", aid);
        List<AbBillDiscountCollectVo> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbBillDiscountCollectVo.class);
        if (listInfo != null && !listInfo.isEmpty()) {
            return listInfo.get(0);
        }
        return new AbBillDiscountCollectVo();
    }
}
