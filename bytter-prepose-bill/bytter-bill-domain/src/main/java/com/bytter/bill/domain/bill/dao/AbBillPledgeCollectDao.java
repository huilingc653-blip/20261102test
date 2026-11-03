package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillPledgeCollectVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Dao
 * @Description: 票据质押收回
 * @date 2017-07-17
 */
@Repository
public class AbBillPledgeCollectDao extends BaseDao<AbBillPledgeCollectVo, String> {
    /**
     * 列表显示应收票据质押收回信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbBillPledgeCollectVo> listAbBillPledgeCollect(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
        sql.append(" V.BILL_FLAG AS billFlag,P.CORP_ID AS corpId,P.CORP_CODE AS corpCode,P.CORP_NAME AS corpName,V.PAYER AS payer,V.ACCEPTOR AS acceptor,V.ORIG_NO AS origNo,");
        sql.append(" V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee, ");
        sql.append(" P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,S.BRIEF_ID as briefId,");
        sql.append(" P.APPLY_CORP_ID as applyCorpId,P.APPLY_CORP_NAME as applyCorpName,P.APPLY_CORP_CODE as applyCorpCode,P.BUS_CORP_ID as busCorpId,P.BUS_CORP_NAME as busCorpName,P.BUS_PERSONNEL as busPersonnel, ");
        sql.append(" (case p.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_6019 + "' then -1 else S.STATUS end) as status, ");
        //		--前手
        sql.append("case ");
        sql.append("when s.brief_id='6001' then s.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then s.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else P.cn_accrecords end as cnAccrecords ");
        //		--前手
        sql.append(" from AB_VOUCHERINFO_VICE P");
        sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID  and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
        sql.append(" left join ab_brief_status bs on bs.status=s.brief_id ");
        sql.append(" WHERE P.VALID_SIGN=:validSign");
        sql.append(" and P.BILL_SOURCE<>:billSource");
        sql.append(" and S.Inside_Sing=:insideSing");
        sql.append(" and (v.bill_flag=:billFlag"
                + " or (v.bill_flag=:billFlag2"
                + " and (S.SEND_SIGN =:sendSign or S.SEND_SIGN is null)))");
        sql.append(" and ((P.box_status=:boxStatus1"
                + " and S.valid_sign=:validSign1"
                + " and S.opt_status=:optStatus1"
                + " and S.STATUS=:status1"
                + " and S.brief_id in(:briefId1)) "
                + " or (P.box_status =:boxStatus2"
                + " and S.valid_sign =:validSign2 "
                + " and S.brief_id in (:briefId2)))"
                + " and S.STATUS<>:status2");
        params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("billSource", AccebillConstant.AB_BILL_SOURCE_PLEDGE);
        params.put("insideSing", AccebillConstant.AB_INSIDE_SING_N);
        params.put("billFlag", AccebillConstant.AB_BILL_FLAG_PAPER);
        params.put("billFlag2", AccebillConstant.AB_BILL_FLAG_ELECT);
        params.put("sendSign", AccebillConstant.AB_BILL_SEND_SIGN_N);
        params.put("boxStatus1", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("optStatus1", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("status1", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("briefId1", new String[]{AccebillConstant.BRIEF_STATUS_018});
        params.put("boxStatus2", AccebillConstant.AB_BOX_STATUS_N);
        params.put("validSign2", AccebillConstant.AB_BILL_VALID_SIGN_0);
        params.put("briefId2", new String[]{AccebillConstant.BRIEF_STATUS_6019});
        params.put("status2", AccebillConstant.AB_BILL_BUS_STATUS_2);
        //判断是否查询打回数据，是的话得查询从表的打回标识back_brief_id等于当前的状态代码，再把查询拼接的打回清空
        if (input.getFormdata().get("eqi_status") != null && "-1".equals(input.getFormdata().get("eqi_status"))) {
            sql.append(" and P.Back_Brief_Id=:backBriefId ");
            input.getFormdata().put("eqi_status", null);
            params.put("backBriefId", AccebillConstant.BRIEF_STATUS_6019);
        }
        //判断是否查询可操作状态数据，是的话得查询从表的打回标识back_brief_id等于null
        if (input.getFormdata().get("eqi_status") != null && "95".equals(input.getFormdata().get("eqi_status"))) {
            sql.append(" and P.Back_Brief_Id is null  ");
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "p.APPLY_CORP_ID", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(") ");
        }
        DataTablesOutput dataOutInfo = getDynamicBySQL(input, sql.toString(), AbBillPledgeCollectVo.class, "", false, params);
        return dataOutInfo;
    }
    /**
     * 加载审批页面列表数据信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, List<String> businessList) {
        Map<String, Object> param = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
        sql.append(" V.BILL_FLAG AS billFlag,P.CORP_ID AS corpId,P.CORP_CODE AS corpCode,P.CORP_NAME AS corpName,V.PAYER AS payer,V.ACCEPTOR AS acceptor,V.ORIG_NO AS origNo,");
        sql.append(" V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee, ");
        sql.append(" P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,");
        sql.append(" S.ID AS businessKey,");
        sql.append(" P.APPLY_CORP_ID as applyCorpId,P.APPLY_CORP_name as applyCorpName,P.APPLY_CORP_code as applyCorpCode,P.BUS_CORP_ID as busCorpId,P.BUS_CORP_NAME as busCorpName,P.BUS_PERSONNEL as busPersonnel, ");
        sql.append(" (case p.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_6019 + "' then -1 else S.STATUS end) as status ");
        sql.append(" from AB_VOUCHERINFO_VICE P");
        sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
        sql.append(" WHERE 1=1");
        String businessSql = SqlUtil.getInSqlByParams(businessList, "s.id", param);
        sql.append(" and ").append(businessSql).append(" ");
        DataTablesOutput dataOutInfo = this.getDynamicBySQL(dataTablesInput, sql.toString(), AbBillPledgeCollectVo.class, "", false, param);
        return dataOutInfo;
    }
}
