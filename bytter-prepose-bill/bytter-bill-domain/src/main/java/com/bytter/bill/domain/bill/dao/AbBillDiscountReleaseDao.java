package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountVO;
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
 * @Title: Dao
 * @Descriptiont贴现释放Dao
 */
@Repository
public class AbBillDiscountReleaseDao extends BaseDao<AbBillDiscountAccountVO, String> {
    /**
     * 列表显示贴现释放信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillDiscountAccount(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("select ");
        if (input.getFormdata().containsKey("checkboxHide")) {
            sql.append("case when p.rat_model=2 then '1' else '0' end as checkboxHide,");
            input.getFormdata().remove("checkboxHide");
        }
        sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
        sql.append(" P.CORP_ID AS corpId,p.CORP_CODE AS corpCode,p.CORP_Name AS corpName,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,t.occupancy_ratio*V.BILL_MONEY as occupancyMoney,");
        sql.append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,S.SIGNEROK AS signerok,");
        sql.append(" S.BRIEF_ID AS briefId,(case  when P.back_brief_id=:back_brief_id_0 then -1 else S.status end)  AS status,");
        params.put("back_brief_id_0", AccebillConstant.BRIEF_STATUS_6012);
        sql.append(" T.PAYDATE AS paydate,T.DEFERDATE AS deferdate,T.DAYNUM AS daynum,T.INTEREST AS interest,T.BUYERINTEREST_MONEY AS buyerinterestMoney,");
        sql.append(" T.REAL_MONEY AS realMoney,S.BANKENDRSMTMK AS bankendrsmtmk,T.INVC_NB AS invcNb,T.TXL_CTRCT_NB AS txlCtrctNb,P.ENTER_STOCK AS enterStock");
        sql.append(" ,t.DISCOUNT_DATE as discountDate,btbank.bank_name as bankName,(t.occupancy_ratio*V.BILL_MONEY)/100 as occupancyRatio,p.rat_model as releaseState, ");
        sql.append(" P.APPLY_CORP_ID as applyCorpId,p.APPLY_CORP_NAME as applyCorpName,p.APPLY_CORP_CODE as applyCorpCode,P.BUS_CORP_ID as busCorpId,P.BUS_PERSONNEL as busPersonnel, ");
        //		--前手
        sql.append("case ");
        sql.append("when s.brief_id='6001' then s.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then s.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else P.cn_accrecords end as cnAccrecords ");
        //		--前手
        // 新一代票据列表查询新增字段 -- start
        sql.append(" , V.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, V.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
        sql.append(" V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");
        sql.append(" V.BL_STD_AMT AS blStdAmt, V.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ");
        sql.append(" V.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, V.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ");
        sql.append(" P.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo, P.BUS_CORP_NAME as busCorpName");
        // -- end
        sql.append(" from AB_VOUCHERINFO_VICE P");
        sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
        sql.append(" LEFT JOIN AB_DISCOUNT T ON T.RECORD_ID=S.ID");
        sql.append(" LEFT JOIN bt_bank btbank ON btbank.sys_bank_code=t.aoaccninf_ac");
        sql.append(" left join ab_brief_status bs on bs.status=s.brief_id ");

        //从表validSign=1
        sql.append(" WHERE 1=1");
        sql.append(" and s.status = 95 ");
        sql.append(" and S.BRIEF_ID IN ('011') ");
        sql.append(" and S.OPT_STATUS in('16','19') ");
        if (StringUtils.isBlank(input.getFormdata().get("releaseState").toString())) {
            input.getFormdata().remove("releaseState");
            sql.append(" and P.RAT_MODEL in ('1','2')");
        } else {
            sql.append(" and P.RAT_MODEL = '" + input.getFormdata().get("releaseState") + "'");
        }
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "P.APPLY_CORP_ID", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(") ");
        }
        params.put("syscode", AccebillConstant.AB_CONFIG_TYPE);
        return this.getDynamicBySQL(input, sql.toString(), AbBillDiscountAccountVO.class, "", false, params);
    }
}
