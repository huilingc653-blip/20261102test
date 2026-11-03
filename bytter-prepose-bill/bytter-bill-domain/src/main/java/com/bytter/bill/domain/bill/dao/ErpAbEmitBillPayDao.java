package com.bytter.bill.domain.bill.dao;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillPayAndReceVO;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijinquan
 * @description:
 * @date 2024-09-03 19:01
 */
@Repository
public class ErpAbEmitBillPayDao extends BaseDao{

    /**
     * 应付票据公共sql
     *
     * @return
     */
    public StringBuilder getPaySql() {
        StringBuilder sql = new StringBuilder("select a.id,i.BILL_FLAG as billFlag,i.BILL_TYPE as billType,i.ORIG_NO as origNo,v.bank_Name as bankName,v.BANK_TYPE_ID as bankTypeId,");
        sql.append(" i.PAYER_BANK_NAME as payerBankName,i.START_DATE as startDate,i.END_DATE as endDate,i.BILL_MONEY as billMoney,i.payee as payee,");
        sql.append(" i.payeeacc,i.payeebankno,i.payee_Bank_Name as payeeBankName,");
        sql.append(" v.BAIL_MONEY as bailMoney,BT_NVL_CHAR(bd.BRIEF_ID,a.BRIEF_ID) as briefId,BT_NVL_CHAR(bd.send_sign,a.send_sign) as sendSign,v.emit_type as emitType,v.emit_guandis_sign as emitGuandisSign,");
        sql.append(" (case when p.EXC_TRANS_TIME is not null then p.EXC_TRANS_TIME else bd.BUSINESS_DATE end) as payDate,i.PAYER as payer,v.corp_Id as corpId,BT_CONCAT4('(',v.CORP_CODE,')',v.CORP_NAME) as corpCodeAndName");
        sql.append(" ,(case when i.bill_status is not null then i.bill_status when bd.id is not null then '000000' else '030006' end) as billStatus ");
        sql.append(" ,(case when p.EXC_TRANS_TIME is null and bd.BUSINESS_DATE is null then '0' else '1' end) as isPay ");
        sql.append(" ,(case when (i.bill_status is null or i.bill_status not in ('170001','170006','010004','020001','020006','030001','000002'))  then '1' else '0' end) as emitOpenOk ");
        sql.append(" ,(case when br.id is not null then br.BUSINESS_DATE when a.SEND_SIGN ='0' and bbr.BUSINESS_DATE is null then a.BUSINESS_DATE when a.SEND_SIGN ='0' then bbr.BUSINESS_DATE else null end) as emitOpenDate ");
        sql.append(" ,v.APPLY_CORP_ID as applyCorpId,v.APPLY_CORP_NAME as applyCorpName,v.APPLY_CORP_CODE as applyCorpCode,v.BUS_CORP_ID as busCorpId,v.BUS_CORP_NAME as busCorpName,v.BUS_PERSONNEL as busPersonnel, v.bank_acc as bankAcc ");
        sql.append(" , i.new_stm_issuofppr_ind as newStmIssuofpprInd, i.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd,  ");
        sql.append(" i.sbbll_rng_strt_sn as sbbllRngStrtSn, i.sbbll_rng_end_sn as sbbllRngEndSn, i.bl_std_amt as blStdAmt, i.bl_rmtr_unn_id_no as blRmtrUnnIdNo,v.rmk " );
        sql.append(" from ab_voucherinfo_vice v inner join ab_voucherinfo i on v.vid=i.id and (i.OLD_DRAFT_ID is null or i.OLD_DRAFT_ID not like '%-del')  inner join ab_accrecords a on v.id = a.vvid ");
        sql.append(" left join ab_accrecords ba on v.id = ba.vvid and ba.opt_status = '19'and ba.brief_id='004'and ba.status = 95 ");
        sql.append(" left join ab_accrecords br on  v.id = br.vvid and br.opt_status = '19' and br.brief_id = '003' and br.status = 95 ");
        sql.append(" left join ab_accrecords bd on  v.id = bd.vvid and bd.opt_status = '19' and bd.brief_id = '6120' and bd.status in (95,100) ");
        sql.append(" left join ab_accrecords bbr on  v.id = bbr.vvid and bbr.opt_status = '19' and bbr.brief_id = '6101' and bbr.status = 95 ");
        sql.append(" left join ab_emitbill_pay p on v.id=p.aid ");
        sql.append(" where a.opt_status = '19'  and a.status = 95 and v.type = '0' and a.BRIEF_ID = '001' and ba.brief_id is null ");
        return sql;
    }

    /**
     * 加载应付票据台账已结清数据
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput billPayDataTable(DataTablesInput dataTablesInput,List<String> origNos) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from (");
        sql.append(this.getPaySql());

        sql.append(" ) bill");
        sql.append(" where 1=1 ");
        sql.append(" and bill.billStatus = 'CS06' "); //已结清
        sql.append(" and bill.origNo in (:origNos) ");
        params.put("origNos",origNos);

        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillPayAndReceVO.class, "bill", true, params);
    }



    /**
     * 加载兑付申请列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbEmitBillPay(DataTablesInput dataTablesInput, List<String> origNos) {
        boolean isEmitMake = dataTablesInput.getFormdata().containsKey(AccebillConstant.AB_CONFIG_ISEMITMAKE)
                && "true".equals(String.valueOf(dataTablesInput.getFormdata().get(AccebillConstant.AB_CONFIG_ISEMITMAKE)));
        dataTablesInput.getFormdata().remove(AccebillConstant.AB_CONFIG_ISEMITMAKE);
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,");
        sql.append(" l.BUSINESS_DATE as businessDate,z.payeracc as payeracc,");
        // 收票人信息
        sql.append(" z.payee as payee,z.payeeacc as payeeacc,z.payeebankno as payeebankno,z.payee_bank_name as payeeBankName,");
        // 承兑人信息
        sql.append(" z.acceptor as acceptor,z.acceptoracc as acceptoracc,z.acceptorbankno as acceptorbankno,z.acceptor_bank_name as acceptorBankName,");
        sql.append(" z.orig_no as origNo,");
        sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,");
        sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.CORP_ID as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpIdName,"
                + "(case c.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_6120
                + "' then -1 else l.status end) as status," + "l.brief_id as briefId, ");
        sql.append(" (case l.status when " + AccebillConstant.AB_BILL_BUS_STATUS11
                + " then 1 else 0 end) as checkboxHide,");
        // 线上兑付收款信息，如果表有信息直接查询，如果没有根据兑付申请线上兑付收款人设置情况查询收款人信息
        sql.append(" e.payee_acc as payPayeeacc,e.payee_name as payPayeeName, ");
        sql.append(" e.payee_bank_no as payPayeeBankNo,e.payee_bank_name as payPayeeBankName, ");
        sql.append(" e.purpose as payUse,e.remark as payAbs,");
        sql.append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode");
        sql.append(",c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ");
        // 线上兑付收款信息
        //新一代票据新增新票标志、是否允许分包、子票区间开始序号、子票区间结束序号、标准金额、出票人统一信用代码
        sql.append(", z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,z.SBBLL_RNG_STRT_SN as sbBllRngStrtSN");
        sql.append(", z.SBBLL_RNG_END_SN as sbBllRngEndSN,z.BL_STD_AMT as blStdAmt,z.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo,c.BATCH_NO as batchNo, c.BUS_CORP_NAME as busCorpName");
        sql.append(" from ab_voucherinfo_vice c ");
        sql.append(" left join ab_voucherinfo z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ");
        sql.append(" left join ab_accrecords l on c.id=l.vvid ");

        // 应付票据兑付信息表
        sql.append(" left join ab_emitbill_pay e on e.aid = c.id ");
        sql.append(" where 1=1 ");
        sql.append(" AND z.orig_no in (:origNos) ");
        sql.append(" and c.valid_sign= :validSign1 and");
        // 主表 :处理状态 box_status -- 处理完成 11
        // 流水表 :处理标记 valid_sign -- 可用 1
        // 操作状态 opt_status -- 已签收19
        sql.append(" ( ( ");
        if (isEmitMake) {//系统需要开票补录功能时
            // 业务类型 brief_id -- 6101(补录)
            sql.append(" l.brief_id in (:briefId6101) ");
        } else {//系统不需要开票补录功能时
            // 指令发送提示收票完成(线上)和开票登记完成(线下)就可以做兑付确认 不需要补录`
            sql.append(" ( (l.brief_id in (:briefId003) and l.send_sign=:sendSign1 ) or (l.brief_id in (:briefId001) and l.send_sign=:sendSign0 ) or l.brief_id in (:briefId6101)) ");
        }
        sql.append(" and c.box_status=:boxStatusY and l.valid_sign= :validSign1 and l.status=:status95 and l.opt_status=:optStatus19 )");
        // 业务类型 :brief_id -- 应付兑付确认 6120
        // 审批处理状态 :status -- 审批中 11
        sql.append(" or (l.brief_id =:briefId6120 and l.status=:status11 ) ) ");
        // 条件查询打回数据
        if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS_1)
                .equals(dataTablesInput.getFormdata().get("eqi_status"))) {
            sql.append(" and c.back_brief_id = :briefId6120 ");
            dataTablesInput.getFormdata().remove("eqi_status");
        }
        if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS95)
                .equals(dataTablesInput.getFormdata().get("eqi_status"))) {
            sql.append(" and c.back_brief_id is null ");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("origNos",origNos);
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("boxStatusY", AccebillConstant.AB_BOX_STATUS_Y);
        if (isEmitMake) {
            params.put("briefId6101", new String[]{AccebillConstant.BRIEF_STATUS_6101});
        } else {
            params.put("sendSign0", new String[]{AccebillConstant.AB_BILL_SEND_SIGN_N});
            params.put("sendSign1", new String[]{AccebillConstant.AB_BILL_SEND_SIGN_Y});
            params.put("briefId001", new String[]{AccebillConstant.BRIEF_STATUS_001});
            params.put("briefId003", new String[]{AccebillConstant.BRIEF_STATUS_003});
            params.put("briefId6101", new String[]{AccebillConstant.BRIEF_STATUS_6101});
        }
        params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("optStatus19", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("briefId6120", AccebillConstant.BRIEF_STATUS_6120);
        params.put("status11", AccebillConstant.AB_BILL_BUS_STATUS11);
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbEmitBillPayVo.class, "", false, params);
    }

}
