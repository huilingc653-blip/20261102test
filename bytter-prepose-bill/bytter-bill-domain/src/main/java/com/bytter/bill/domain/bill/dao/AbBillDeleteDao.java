package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.vo.AbBillDeleteVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 票据删除
 * @date 2020-06-01
 */
@Repository
public class AbBillDeleteDao extends BaseDao<AbVoucherinfo, String> {
    /**
     * 加载票据删除列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillDelete(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
        sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,c.source as source,z.SOURCE_TYPE as type,");
//        sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.corp_id as corpId,s.code as corpCode,s.name as corpIdName,");
        sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.corp_id as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpIdName,");
        sql.append( "(case c.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_9004 + "' then -1 else l.status end) as status,l.brief_id as briefId, ");
        sql.append(" (case l.status when " + AccebillConstant.AB_BILL_BUS_STATUS11 + " then 1 else 0 end) as checkboxHide,l.send_sign as sendSign, ");
        //		--前手
        sql.append("case ");
        sql.append("when l.brief_id='6001' then l.cn_accrecords ");
        sql.append("when l.brief_id in ('001','002','9002','003','004') then '' ");
        sql.append("when bs.bus_type in (:bus_type_0) then l.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else c.cn_accrecords end as cnAccrecords,");
//        sql.append(" c.APPLY_CORP_ID as applyCorpId,s2.name as applyCorpName,s2.code as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ");
        sql.append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel ");
        //		--前手
        //新一代票据新增出票标志、是否允许分包、子票区间开始序号、子票区间结束序号、标准金额、出票人统一信用代码、业务批次号、票据单位统一识别号码
        sql.append(", z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,z.SBBLL_RNG_STRT_SN as sbBllRngStrtSN");
        sql.append(", z.SBBLL_RNG_END_SN as sbBllRngEndSN,z.BL_STD_AMT as blStdAmt,c.BATCH_NO as batchNo,c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo,z.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo ");
        sql.append(" from ab_voucherinfo_vice c ");
        sql.append(" left join ab_voucherinfo z on z.id=c.vid  and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" left join ab_accrecords l on c.id=l.vvid ");
//        sql.append(" left join sys_corp s on c.corp_id = s.id ");
//        sql.append(" left join sys_corp s2 on c.apply_corp_id = s2.id ");
        sql.append(" left join ab_brief_status bs on bs.status=l.brief_id ");
        sql.append(" where ");
        // 从表 :处理状态 box_status -- 处理完成 11
        // 业务类型 brief_id -- (6004,004)  流水有效状态 l.valid_sign= 1
        // 应付未用退票\应收退票申请的数据必须是纸票或者线下电票申请的数据 z.bill_flag='10' or l.send_sign='0'
        // 流水表:操作状态 opt_status -- 已签收19  审批状态-已审批95
        sql.append(" ((c.box_status=:C_BOX_STATUS "
                + " and (l.valid_sign= :L_Valid_Sign  or (l.valid_sign= :L_Valid_Sign2 and l.send_sign=:L_SEND_SIGY))"
                + " and l.status= :L_STATUS95 "
                + " and l.opt_status=:L_OPT_STATUS "
                + " and l.brief_id in (:L_BRIEF_ID) "
                //+ " and (z.bill_flag=:Z_BILL_FLAG or l.send_sign=:L_SEND_SIGN)"
                + ") ");
        params.put("C_BOX_STATUS", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("L_Valid_Sign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("L_Valid_Sign2", AccebillConstant.AB_BILL_VALID_SIGN_2);
        params.put("L_STATUS95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("L_OPT_STATUS", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("L_BRIEF_ID", new String[]{AccebillConstant.BRIEF_STATUS_004, AccebillConstant.BRIEF_STATUS_6004});
        //params.put("Z_BILL_FLAG", AccebillConstant.AB_BILL_FLAG_PAPER);
        //params.put("L_SEND_SIGN", AccebillConstant.AB_BILL_SEND_SIGN_N);
        params.put("L_SEND_SIGY", AccebillConstant.AB_BILL_SEND_SIGN_Y);
        // 业务类型 :brief_id -- 票据删除 9004
        // 审批处理状态 :status -- 审批中 11
        sql.append(" or (l.brief_id = :L_BRIEF_ID9004 "
                + "and l.status= :L_STATUS11 )) ");
        params.put("L_BRIEF_ID9004", AccebillConstant.BRIEF_STATUS_9004);
        params.put("L_STATUS11", AccebillConstant.AB_BILL_BUS_STATUS11);
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.apply_corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillDeleteVO.class, "", false, params);
    }
    /**
     * 加载退票审批中数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, List<String> businessList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(
                " z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
        sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,z.SOURCE_TYPE as type,");
        sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.corp_id as corpId,c.corp_code as corpCode,c.corp_name as corpIdName,l.brief_id as briefId,");
        sql.append(" c.APPLY_CORP_ID as applyCorpId,c.apply_corp_name as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel ");
        sql.append(" from ab_voucherinfo_vice c ");
        sql.append(" left join ab_voucherinfo z on z.id=c.vid  and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" left join ab_accrecords l on c.id=l.vvid ");
//        sql.append(" left join sys_corp s on c.corp_id = s.id ");
//        sql.append(" left join sys_corp s2 on c.apply_corp_id = s2.id ");
        sql.append(" where 1=1 ");
        String businessSql = SqlUtil.getInSqlByParams(businessList, "l.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillDeleteVO.class, "", false, params);
    }
    /**
     * 根据从表id查询票据流水表迁移数据到流水历史表sql
     */
    public void moveAccrecordsSql(String vvid) {
        StringBuilder sql = new StringBuilder(" insert into AB_ACCRECORDS_HIS ");
        sql.append("(ID,VID,VVID,RECID,TRID,LASTID,LASTBILLCODE,VALID_SIGN,");
        sql.append(" IS_HIDE,STATUS,OPT_STATUS,SEND_SIGN,BILL_SOURCE,RETURN_CODE,");
        sql.append(" RETURN_MSG,BRIEF_ID,INSIDE_SING,ENTER_STOCK_NEW,ENTER_STOCK_OLD,BAIL_TYPE,");
        sql.append(" RECALL_TYPE,BANKENDRSMTMK,STTLMMK,APPLY_DATE,BUSINESS_DATE,RCRSOKMONEY,CN_ACCRECORDS,");
        sql.append(" SIGN_UP_FLAG,ABS,RMK,CREDIT_CODE,BUDGET_CODE,BAIL_ACC,BAILMONEY,BAILPERCENT,");
        sql.append(" BAILRATE,FEEMONEY,RCRSRSNCD,RCRS_TP,SIGNEROK,BIF_CODE,ADR,REQ_ROLE,REQ_NM,");
        sql.append(" REQ_CMONID,REQ_ACCT_ID,REQ_ACCTSVCR,REQ_ACCTSVCR_AG,REC_ROLE,REC_NM,REC_CMONID,REC_ACCT_ID,");
        sql.append(" REC_ACCTSVCR,REC_ACCTSVCR_AG,BUS_GLOBAL_ID,ATTACHMENT_ID,CDT_RATGS,CDT_RATG_AGCY,");
        sql.append("  CDT_RATG_DUEDT,EMIT_STTLMMK,CREATE_NAME,CREATE_BY,CREATE_DATE,UPDATE_NAME,UPDATE_BY,UPDATE_DATE,");
        sql.append("  BILL_ID,MSG_ID,TRADER,RSN,PD_ID,IS_AUTOSIGN ");
        sql.append(" ) ");
        sql.append(" select ");
        sql.append(" L.ID,L.VID,L.VVID,L.RECID,L.TRID,L.LASTID,L.LASTBILLCODE,L.VALID_SIGN,");
        sql.append(" L.IS_HIDE,L.STATUS,L.OPT_STATUS,L.SEND_SIGN,L.BILL_SOURCE,L.RETURN_CODE,");
        sql.append(" L.RETURN_MSG,L.BRIEF_ID,L.INSIDE_SING,L.ENTER_STOCK_NEW,L.ENTER_STOCK_OLD,L.BAIL_TYPE,");
        sql.append(" L.RECALL_TYPE,L.BANKENDRSMTMK,L.STTLMMK,L.APPLY_DATE,L.BUSINESS_DATE,L.RCRSOKMONEY,L.CN_ACCRECORDS,");
        sql.append(" L.SIGN_UP_FLAG,L.ABS,L.RMK,L.CREDIT_CODE,L.BUDGET_CODE,L.BAIL_ACC,L.BAILMONEY,L.BAILPERCENT,");
        sql.append(" L.BAILRATE,L.FEEMONEY,L.RCRSRSNCD,L.RCRS_TP,L.SIGNEROK,L.BIF_CODE,L.ADR,REQ_ROLE,L.REQ_NM,");
        sql.append(" L.REQ_CMONID,L.REQ_ACCT_ID,L.REQ_ACCTSVCR,L.REQ_ACCTSVCR_AG,L.REC_ROLE,L.REC_NM,L.REC_CMONID,L.REC_ACCT_ID,");
        sql.append(" L.REC_ACCTSVCR,L.REC_ACCTSVCR_AG,L.BUS_GLOBAL_ID,L.ATTACHMENT_ID,L.CDT_RATGS,L.CDT_RATG_AGCY,");
        sql.append(" L.CDT_RATG_DUEDT,L.EMIT_STTLMMK,L.CREATE_NAME,L.CREATE_BY,L.CREATE_DATE,L.UPDATE_NAME,L.UPDATE_BY,L.UPDATE_DATE,");
        sql.append(" L.BILL_ID,L.MSG_ID,L.TRADER,L.RSN,L.PD_ID,IS_AUTOSIGN ");
        sql.append("  from AB_ACCRECORDS L where L.vvid='").append(vvid).append("'");
        this.createSQLQuery(sql.toString()).executeUpdate();
    }
    /**
     * 根据从表id查询票据从表迁移数据到从表历史表sql
     */
    public void moveVoucherinfoViceSql(String vvid) {
        StringBuilder sql = new StringBuilder(" insert into AB_VOUCHERINFO_VICE_HIS ");
        sql.append("(ID,VID,CORP_ID,APP_NO,BANK_ACC,BANK_CODE,BANK_NAME,BIF_CODE,BILL_SOURCE,CN_ACCRECORDS,INSIDE_SING,VALID_SIGN,");
        sql.append(" BOX_STATUS,RAT_MODEL,CREDIT_CODE,BUDGET_CODE,BAIL_ACC,BAIL_MONEY,BAIL_PERCENT,BAIL_RATE,FEE_MONEY,ENTER_STOCK,BACK_BRIEF_ID,");
        sql.append(" BACK_ACID,TYPE,ABS,VOUCHER_ID,ATTACHMENT_ID,SOURCE,RMK,CREATE_NAME,CREATE_BY,CREATE_DATE,UPDATE_NAME,UPDATE_BY,UPDATE_DATE,IS_PAY,");
        sql.append(" BAIL_CONTRACT,BAIL_MODEL,IS_OPEN_RAT,BEAR_MODEL,POOL_STATUS,INSIDE_RAT_MODEL,INSIDE_CREDIT_CODE,EMIT_TYPE,EMIT_GUANDIS_SIGN,");
        sql.append(" RAT_BANK_CORP_TYPE,RAT_BANK_CORP_ID,BATCH_NO,IS_ENSURE,BAIL_TYPE,CLAIM_STATUS,CLAIM_ID,APPLY_CORP_ID,BUS_CORP_ID,BUS_PERSONNEL ");
        sql.append(" ) ");
        sql.append(" select ");
        sql.append(" vv.ID,vv.VID,vv.CORP_ID,vv.APP_NO,vv.BANK_ACC,vv.BANK_CODE,vv.BANK_NAME,vv.BIF_CODE,vv.BILL_SOURCE,vv.CN_ACCRECORDS,vv.INSIDE_SING,vv.VALID_SIGN,");
        sql.append(" vv.BOX_STATUS,vv.RAT_MODEL,vv.CREDIT_CODE,vv.BUDGET_CODE,vv.BAIL_ACC,vv.BAIL_MONEY,vv.BAIL_PERCENT,vv.BAIL_RATE,vv.FEE_MONEY,vv.ENTER_STOCK,vv.BACK_BRIEF_ID,");
        sql.append(" vv.BACK_ACID,vv.TYPE,vv.ABS,vv.VOUCHER_ID,vv.ATTACHMENT_ID,vv.SOURCE,vv.RMK,vv.CREATE_NAME,vv.CREATE_BY,vv.CREATE_DATE,vv.UPDATE_NAME,vv.UPDATE_BY,vv.UPDATE_DATE,vv.IS_PAY,");
        sql.append(" vv.BAIL_CONTRACT,vv.BAIL_MODEL,vv.IS_OPEN_RAT,vv.BEAR_MODEL,vv.POOL_STATUS,vv.INSIDE_RAT_MODEL,vv.INSIDE_CREDIT_CODE,vv.EMIT_TYPE,vv.EMIT_GUANDIS_SIGN,");
        sql.append(" vv.RAT_BANK_CORP_TYPE,vv.RAT_BANK_CORP_ID,vv.BATCH_NO,vv.IS_ENSURE,vv.BAIL_TYPE,vv.CLAIM_STATUS,vv.CLAIM_ID,vv.APPLY_CORP_ID,vv.BUS_CORP_ID,vv.BUS_PERSONNEL ");
        sql.append(" from AB_VOUCHERINFO_VICE vv where vv.id='").append(vvid).append("'");
        this.createSQLQuery(sql.toString()).executeUpdate();
    }
    /**
     * 根据主表id查询票据主表迁移数据到主表历史表sql
     */
    public void moveVoucherinfoSql(String vid) {
        StringBuilder sql = new StringBuilder(" insert into AB_VOUCHERINFO_HIS ");
        sql.append("(ID,ORIG_NO,BILL_TYPE,BANENDRSMTMK,BOX_STATUS,BILL_STATUS,BILL_FLAG,BILL_MONEY,START_DATE,");
        sql.append(" END_DATE,PAYER,PAYERACC,PAYER_BANK_NAME,PAYERBANKNO,PAYEE,PAYEEACC,PAYEE_BANK_NAME,PAYEEBANKNO,ACCEPTOR,");
        sql.append(" ACCEPTORACC,ACCEPTORBANKNO,ACCEPTOR_BANK_NAME,ACCEPTORBANKADDRESS,AR_XCLX,A_CONTRACT_NO,A_INVCNB,");
        sql.append(" ACCPTNCAGRMTNB,PAYER_CDT_RATGS,PAYER_CDT_RATG_AGCY,PAYER_CDT_RATG_DUEDT,SOURCE_TYPE,CREATE_NAME,");
        sql.append(" CREATE_BY,CREATE_DATE,UPDATE_NAME,UPDATE_BY,UPDATE_DATE");
        sql.append(" ) ");
        sql.append(" select ");
        sql.append(" v.ID,v.ORIG_NO,v.BILL_TYPE,v.BANENDRSMTMK,v.BOX_STATUS,v.BILL_STATUS,v.BILL_FLAG,v.BILL_MONEY,v.START_DATE,");
        sql.append(" v.END_DATE,v.PAYER,v.PAYERACC,v.PAYER_BANK_NAME,v.PAYERBANKNO,v.PAYEE,v.PAYEEACC,v.PAYEE_BANK_NAME,v.PAYEEBANKNO,v.ACCEPTOR,");
        sql.append(" v.ACCEPTORACC,v.ACCEPTORBANKNO,v.ACCEPTOR_BANK_NAME,v.ACCEPTORBANKADDRESS,v.AR_XCLX,v.A_CONTRACT_NO,v.A_INVCNB,");
        sql.append(" v.ACCPTNCAGRMTNB,v.PAYER_CDT_RATGS,v.PAYER_CDT_RATG_AGCY,v.PAYER_CDT_RATG_DUEDT,v.SOURCE_TYPE,v.CREATE_NAME,");
        sql.append(" v.CREATE_BY,v.CREATE_DATE,v.UPDATE_NAME,v.UPDATE_BY,v.UPDATE_DATE");
        sql.append(" from AB_VOUCHERINFO v where v.id='").append(vid).append("'");
        this.createSQLQuery(sql.toString()).executeUpdate();
    }
}
