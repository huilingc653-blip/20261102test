package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillCollectionAccountVo;
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
 * @Description: AB_VOUCHERINFO
 * @date 2017-06-21 15:30:56
 */
@Repository
public class AbBillCollectionAccountDao extends BaseDao<AbBillCollectionAccountVo, String> {
    /**
     * 托收到账列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, String isApply) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,c.BUSINESS_DATE as businessDate,");
        sql.append("a.orig_no as origNo,a.bill_money as billMoney,a.start_date as startDate,a.end_date as endDate,");
        sql.append("b.enter_stock as enterStock,b.corp_id as corpId,b.CORP_CODE as corpCode,b.CORP_NAME as corpName,b.inside_sing as insideSing,");
        sql.append("a.id as vid,b.id as vvid,c.id as aid,c.id as id,b.box_status as boxStatus,c.signerok as signerok,c.send_sign as sendSign,");
        sql.append(" b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel, ");
        sql.append("(case b.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_6020 + "' then " + AccebillConstant.AB_BILL_BUS_STATUS_1 + " else c.status end)  as status, ");
        //		--前手
        sql.append("case ");
        sql.append("when c.brief_id='6001' then c.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then c.rec_nm ");
        Map<String, Object> params = new HashMap<>();
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else b.cn_accrecords end as cnAccrecords ");
        //		--前手
        //新一代票据
        sql.append(" , c.BANKENDRSMTMK AS bankendrsmtmk, b.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo ");
        sql.append(" , a.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd, a.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd, a.SBBLL_RNG_STRT_SN as sbbllRngStrtSn ,");
        sql.append(" a.SBBLL_RNG_END_SN as sbbllRngEndSn , a.BL_STD_AMT as blStdAmt , a.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo ,");
        sql.append(" a.BILL_MONEY as transMoney,b.BUS_CORP_NAME as busCorpName");
        sql.append(" from AB_VOUCHERINFO_VICE b ");
        sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
        sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
        sql.append(" left join ab_brief_status bs on bs.status=c.brief_id ");
        sql.append(" WHERE b.valid_sign = :validSign1  ");
        //票据业务排除票据池流水
        sql.append(" and c.pd_id is null ");
        //审批状态查询
        if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS11).equals(input.getFormdata().get("eqs_statusName"))) {
            sql.append(" and c.valid_sign=:validSign0 and b.box_status = :boxStatusN ");
            sql.append(" and c.status = :status11 and c.brief_id = :briefId6020 ");
            input.getFormdata().remove("eqs_statusName");
            //待记账状态查询
        } else if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS94).equals(input.getFormdata().get("eqs_statusName"))) {
            sql.append(" and c.valid_sign=:validSign0 and b.box_status = :boxStatusN ");
            sql.append(" and c.status = :status94 and c.brief_id = :briefId6020 ");
            input.getFormdata().remove("eqs_statusName");
            //待确认状态查询
        } else if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS95).equals(input.getFormdata().get("eqs_statusName"))) {
            sql.append(" and c.valid_sign=:validSign1 and b.box_status = :boxStatusY ");
            //默认查询
            sql.append(" and c.brief_id in (:briefIds)");
            sql.append(" and  c.status = :status95 and c.opt_status = :optStatus19 ");
            //只对线下清算或离线模式的票据进行到账确认
            sql.append(" and (c.sttlmmk = :sttlmmkSM01 or c.send_sign = :sendSignN ) ");
            input.getFormdata().remove("eqs_statusName");
        } else {
            sql.append(" and ( ( ");
            sql.append(" c.valid_sign=:validSign1 and b.box_status = :boxStatusY ");
            //默认查询
            sql.append(" and c.brief_id in (:briefIds)");
            sql.append(" and  c.status = :status95 and c.opt_status = :optStatus19 ");
            //只对线下清算或离线模式的票据进行到账确认
            sql.append(" and (c.sttlmmk = :sttlmmkSM01 or c.send_sign = :sendSignN ) ");
            sql.append(" ) or ( ");
            sql.append(" c.valid_sign=:validSign0 and b.box_status = :boxStatusN ");
            sql.append(" and (c.status = :status11 or c.status = :status94 ) and c.brief_id = :briefId6020 ");
            sql.append(" ) or ( ");
            sql.append(" b.back_brief_id = :briefId6020 and c.valid_sign=:validSign1 and b.box_status = :boxStatusY )) ");
            //打回状态查询
            if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS_1).equals(input.getFormdata().get("eqs_statusName"))) {
                sql.append(" and b.back_brief_id = :briefId6020 and c.valid_sign=:validSign1 and b.box_status = :boxStatusY ");
                input.getFormdata().remove("eqs_statusName");
            }
        }
        if (StringUtils.isNotEmpty(isApply)) {
            sql.append(" and c.brief_id != :briefId6020 ");
        }
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("validSign0", AccebillConstant.AB_BILL_VALID_SIGN_0);
        params.put("boxStatusY", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("status11", AccebillConstant.AB_BILL_BUS_STATUS11);
        params.put("briefId6020", AccebillConstant.BRIEF_STATUS_6020);
        params.put("briefIds", new String[]{AccebillConstant.BRIEF_STATUS_020, AccebillConstant.BRIEF_STATUS_021});
        params.put("status94", AccebillConstant.AB_BILL_BUS_STATUS94);
        params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("optStatus19", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("sttlmmkSM01", AccebillConstant.AB_STTLMMK_N);
        params.put("sendSignN", AccebillConstant.AB_BILL_SEND_SIGN_N);
        params.put("boxStatusN", AccebillConstant.AB_BOX_STATUS_N);
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "b.APPLY_CORP_ID", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制Q
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (b.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.bank_acc", "bankAccs", params)).append(")");
        }
        return getDynamicBySQL(input, sql.toString(), AbBillCollectionAccountVo.class, "", false, params);
    }
    /**
     * 审批页面查询当前操作员可审批的托收到账数据
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append("a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,c.id as businessKey,");
        sql.append("a.orig_no as origNo,a.bill_money as billMoney,a.start_date as startDate,a.end_date as endDate,");
        sql.append("b.enter_stock as enterStock,b.corp_id as corpId,b.CORP_CODE as corpCode,b.CORP_NAME as corpName,b.inside_sing as insideSing,");
        sql.append(" b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel, ");
        sql.append("a.id as vid,b.id as vvid,c.id as aid,b.box_status as boxStatus,c.status as status,c.signerok as signerok ");
        sql.append(" from AB_VOUCHERINFO_VICE b ");
        sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
        sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");

        sql.append(" WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "c.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), AbBillCollectionAccountVo.class, "", false, params);
    }
}
