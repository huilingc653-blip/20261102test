package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillDiscountApplyVO;
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
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description:贴现申请Dao
 * @date 2017-07-03 11:54:04
 */
@Repository
public class AbBillDiscountApplyDao extends BaseDao<AbBillDiscountApplyVO, String> {
    /**
     * 查询sql
     *
     * @return
     */
    public String getSqlQuery(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder("select p.bank_Acc as bankAcc,p.type_name as bankTypeName,");
        sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,P.BANK_CODE AS bankCode,");
        sql.append(" P.CORP_ID AS corpId,p.corp_code AS corpCode,p.corp_name as corpName,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,");
        sql.append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee,");
        sql.append(" S.BRIEF_ID AS briefId,(case when P.back_brief_id=:back_brief_id_0 then -1 else S.status end)  AS status,");
        params.put("back_brief_id_0", AccebillConstant.BRIEF_STATUS_6011);
        sql.append(" P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,S.SEND_SIGN AS sendSign,S.RMK AS RMK ");
        sql.append(" ,ba.num as attachmentNum,ba.attachment_id as attachmentId,s.REC_ACCTSVCR as recAcctsvcr ,s.REQ_ACCTSVCR as reqAcctsvcr,");
        sql.append(" P.APPLY_CORP_ID as applyCorpId,p.apply_corp_name as applyCorpName,p.apply_corp_code as applyCorpCode," );
        sql.append(  "P.BUS_CORP_ID as busCorpId,P.BUS_PERSONNEL as busPersonnel, ");
        //		--前手
        sql.append("case ");
        sql.append("when s.brief_id='6001' then s.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then s.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else P.cn_accrecords end as cnAccrecords ");
        //		--前手
        //新一代票据 start
        sql.append(" ,V.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, V.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
        sql.append(" V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");
        sql.append(" V.BL_STD_AMT AS blStdAmt, V.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ");
        sql.append(" V.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, V.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ");
        sql.append(" P.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo , V.BILL_MONEY as transMoney, P.BUS_CORP_NAME as busCorpName");
        //end
        sql.append(" from AB_VOUCHERINFO_VICE P");
        sql.append(" INNER JOIN AB_VOUCHERINFO V ON V.ID=P.VID  and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
        sql.append(" LEFT JOIN ab_bill_attachment ba on ba.acid in (s.id,s.lastid) and ba.brief_id='" + AccebillConstant.BRIEF_STATUS_6011 + "' ");
        sql.append(" left join ab_brief_status bs on bs.status=s.brief_id ");
        //从表validSign=1
        sql.append(" WHERE P.VALID_SIGN=:VALID_SIGN_1");
        params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        //第一种业务已完成，或打回的票据
        //流水validSign=1,optStaus=19,status=95,主表boxStatus=11
        sql.append(" AND ((( S.VALID_SIGN=:VALID_SIGN_2 AND S.OPT_STATUS=:OPT_STATUS_1");
        params.put("VALID_SIGN_2", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("OPT_STATUS_1", AccebillConstant.AB_BILL_OPT_STATUS19);
        sql.append(" AND S.STATUS=:STATUS_1 AND P.BOX_STATUS=:BOX_STATUS_1)");
        params.put("STATUS_1", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("BOX_STATUS_1", AccebillConstant.AB_BOX_STATUS_Y);
        //应收票据登记，纸票
        sql.append(" AND ((S.BRIEF_ID IN (:BRIEF_ID_1)");
        params.put("BRIEF_ID_1", new String[]{AccebillConstant.BRIEF_STATUS_6001, AccebillConstant.BRIEF_STATUS_6019,
                AccebillConstant.BRIEF_STATUS_6212});
        sql.append(" AND P.BILL_SOURCE <> :BILL_SOURCE)");
        params.put("BILL_SOURCE", AccebillConstant.AB_BILL_SOURCE_PLEDGE);
        //提示收票签收，背书申请签收，质押解除签收，回购式贴现赎回签收，且为电票
        sql.append(" or (S.BRIEF_ID IN (:BRIEF_ID_2) AND S.SEND_SIGN=:SEND_SIGN_1)))");
        params.put("BRIEF_ID_2", new String[]{AccebillConstant.BRIEF_STATUS_9003, AccebillConstant.BRIEF_STATUS_9010,
                AccebillConstant.BRIEF_STATUS_9019, AccebillConstant.BRIEF_STATUS_9012});
        params.put("SEND_SIGN_1", AccebillConstant.AB_BILL_SEND_SIGN_Y);
        //第二种，贴现申请审批中的票据
        //业务贴现申请，流水validSign=0,status=11,主表boxStatus=10
        sql.append(" or (S.BRIEF_ID IN (:BRIEF_ID_3) and S.STATUS=:STATUS_2");
        params.put("BRIEF_ID_3", AccebillConstant.BRIEF_STATUS_6011);
        params.put("STATUS_2", AccebillConstant.AB_BILL_BUS_STATUS11);
        sql.append(" and P.BOX_STATUS=:BOX_STATUS_2 AND S.VALID_SIGN=:VALID_SIGN_3))");
        params.put("BOX_STATUS_2", AccebillConstant.AB_BOX_STATUS_N);
        params.put("VALID_SIGN_3", AccebillConstant.AB_BILL_VALID_SIGN_0);
        //票据为可再转让
        sql.append(" and BT_NVL_CHAR(S.BANKENDRSMTMK,V.BANENDRSMTMK)=:BANENDRSMTMK");
        params.put("BANENDRSMTMK", AccebillConstant.AB_BANEM_Y);
        return sql.toString();
    }
    /**
     * 列表显示贴现申请信息
     *
     * @param input
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillDiscountApply(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(getSqlQuery(params));
        //判断是否查询打回数据，是的话得查询从表的打回标识back_brief_id等于当前的状态代码，清空查询条件
        if (input.getFormdata().get("eqi_status") != null && "-1".equals(input.getFormdata().get("eqi_status"))) {
            sql.append(" and P.Back_Brief_Id=:Back_Brief_Id_1 ");
            params.put("Back_Brief_Id_1", AccebillConstant.BRIEF_STATUS_6011);
            input.getFormdata().put("eqi_status", null);
        }
        //判断是否查询可操作状态数据，是的话得查询从表的打回标识back_brief_id等于null
        if (input.getFormdata().get("eqi_status") != null && "95".equals(input.getFormdata().get("eqi_status"))) {
            sql.append(" and (P.Back_Brief_Id is null or P.Back_Brief_Id!=:Back_Brief_Id_1)  ");
            params.put("Back_Brief_Id_1", AccebillConstant.BRIEF_STATUS_6011);
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "p.APPLY_CORP_ID", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (input.getFormdata().containsKey(AccebillConstant.AB_CONFIG_ISNOTDISCOUNTAPPLY)
                && "true".equals(String.valueOf(input.getFormdata().get(AccebillConstant.AB_CONFIG_ISNOTDISCOUNTAPPLY)))) {
            sql.append(" and 1<>1 ");
            input.getFormdata().remove(AccebillConstant.AB_CONFIG_ISNOTDISCOUNTAPPLY);
        }
        if (input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(")");
        }
        return this.getDynamicBySQL(input, sql.toString(), AbBillDiscountApplyVO.class, "", false, params);
    }
    /**
     * 贴现申请审批列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select p.bank_Acc as bankAcc,p.type_name as bankTypeName,");
        sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
        sql.append(" P.CORP_ID AS corpId,P.CORP_NAME AS corpName,sc.code AS corpCode,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,");
        sql.append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee,");
        sql.append(" S.BRIEF_ID AS briefId,S.id as businessKey,");

        sql.append(" V.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, V.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
        sql.append(" V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");

        sql.append(" P.APPLY_CORP_ID as applyCorpId,s2.name as applyCorpName,s2.code as applyCorpCode,P.BUS_CORP_ID as busCorpId, P.BUS_CORP_NAME as busCorpName,P.BUS_PERSONNEL as busPersonnel, ");
        sql.append(" P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,S.SEND_SIGN AS sendSign,S.RMK AS RMK ");
        sql.append(" from AB_VOUCHERINFO_VICE P");
        sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
        sql.append(" LEFT JOIN sys_corp sc ON sc.id=p.corp_id");
        sql.append(" left join sys_corp s2 on p.apply_corp_id = s2.id ");
        //从表validSign=1
        sql.append(" WHERE 1=1 ");
        String businessSql = SqlUtil.getInSqlByParams(businessList, "S.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return this.getDynamicBySQL(input, sql.toString(), AbBillDiscountApplyVO.class, "", false, params);
    }
    /**
     * 根据单位和票据号贴现票据信息
     *
     * @param params
     * @param corpAuthSql
     * @return
     */
    public AbBillDiscountApplyVO getAbBillDiscountApplyVOByParams(Map<String, Object> params, String corpAuthSql) {
        StringBuilder sql = new StringBuilder(getSqlQuery(params));
        sql.append(" and V.orig_no=:origNo and P.corp_id=:corpId");
        sql.append(" and V.sbbll_rng_strt_sn=:sbbllRngStrtSn and V.sbbll_rng_end_sn=:sbbllRngEndSn");
        if(!params.containsKey("sbbllRngStrtSn")){
            params.put("sbbllRngStrtSn","0");
        }
        if(!params.containsKey("sbbllRngEndSn")){
            params.put("sbbllRngEndSn","0");
        }
        if (StringUtils.isNotEmpty(corpAuthSql)) {
            sql.append(" and ").append(corpAuthSql).append(" ");
        }
        return this.getBySQLAliasToBean(sql.toString(), params, AbBillDiscountApplyVO.class);
    }
}
