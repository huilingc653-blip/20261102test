package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillSearchforManageVO;
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
 * @Description: 追索清偿处理
 * @date 2017-06-21 17:35:25
 */
@Repository
public class AbBillSearchforManageDao extends BaseDao<AbBillSearchforManageVO, String> {
    /**
     * 加载AbBillSearchforManage表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbBillSearchforManage(DataTablesInput dataTablesInput, String corpAuthSql) {
        StringBuilder sql = new StringBuilder("select * from ( ");
        sql.append("SELECT P.ID,max(P.BILL_TYPE) AS BILLTYPE,max(P.MSGTYPE),max(P.ORIG_NO) AS ORIGNO,max(p.REC_ACCT_TYPE_NAME) as bankTypeName, ");
        sql.append("max(P.BUSSDT) bussdt,max(P.BILL_MONEY) AS BILLMONEY,max(P.PAYER) payer,max(P.ACCEPTOR) acceptor,max(p.payee) payee,max(vv.type) type, ");
        sql.append("max(P.START_DATE) AS STARTDATE,max(P.END_DATE) AS endDate,max(vv.id) as vvid,max(p.reqamt) reqamt,max(p.rcrsrsncd) rcrsrsncd,max(p.rcrs_Tp) as rcrsTp,max(p.appldt) appldt, ");
        sql.append("max(P.NEW_STM_ISSUOFPPR_IND) AS newStmIssuofpprInd,max(P.BLGRDBAGCRCL_PRMT_IND) AS blgrdbagcrclPrmtInd,max(P.SBBLL_RNG_STRT_SN) as sbbllRngStrtSn," +
                "max(P.SBBLL_RNG_END_SN) as sbbllRngEndSn,max(v.BL_STD_AMT) blStdAmt,max(vv.APPLY_SOC_CODE) as socCode, ");
//        sql.append("P.REQ_NM AS REQNM,P.REC_NM AS RECNM,P.REC_ACCT_ID RECACCTID,p.REC_CORP_ID corpId,p.REC_CORP_CODE corpCode,p.REC_CORP_NAME corpName,P.STATUS,vv.BACK_ACID as lastid, ");
        sql.append("max(P.REQ_NM) AS REQNM,max(P.REC_NM) AS RECNM,max(P.REC_ACCT_ID) RECACCTID,max(p.REC_CORP_ID) corpId,max(VV.CORP_CODE) corpCode,max(VV.CORP_NAME) corpName,max(P.STATUS) status,max(vv.BACK_ACID) as lastid, ");
        Map<String, Object> params = new HashMap<>();
        sql.append(" max(vv.APPLY_CORP_ID) as applyCorpId,max(vv.APPLY_CORP_NAME) as applyCorpName,max(vv.APPLY_CORP_CODE) as applyCorpCode,max(vv.BUS_CORP_ID) as busCorpId,max(vv.BUS_PERSONNEL) as busPersonnel ");
        sql.append(" , max(vv.BUS_CORP_NAME) as busCorpName ");
        sql.append(" from AB_BILL_RECEIVEBIZ P ");
        sql.append(" left join AB_VOUCHERINFO v on v.orig_no=P.orig_no and ((P.SBBLL_RNG_STRT_SN = v.SBBLL_RNG_STRT_SN and v.SBBLL_RNG_END_SN = P.SBBLL_RNG_END_SN) or (P.SBBLL_RNG_STRT_SN > v.SBBLL_RNG_STRT_SN and v.SBBLL_RNG_END_SN > P.SBBLL_RNG_END_SN)) ");
        sql.append(" left join AB_VOUCHERINFO_VICE vv "
                + "on vv.corp_id=p.REC_CORP_ID "
                + "and v.id=vv.vid "
                + "and vv.VALID_SIGN='" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "' ");
        sql.append(" WHERE P.STATUS IN(" +//票据状态为18待签收和-2打回
                AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING + "," +
                AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE +
                ") AND P.MSGTYPE IN ('" +//票据签收业务为：022追索通知
                AccebillConstant.AB_MSGTYPE_SEARCHFOR + "') ");
        sql.append(" and p.REQ_ACCT_ID <> vv.BANK_ACC ");
        sql.append(" and (vv.id is null or vv.id not in (SELECT l2.VVID FROM AB_ACCRECORDS l2 WHERE l2.vid = v.id and l2.brief_id = '023')) ");
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (p.REC_ACCT_ID is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.REC_ACCT_ID", "bankAccs", params)).append(")");
        }
        sql.append(" group by p.id ) bail ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillSearchforManageVO.class, "bail", true, params);
    }
    /**
     * 加载审批列表数据
     *
     * @param entity
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder("select * from ( ");
        sql.append("SELECT L.ID,P.BILL_TYPE AS BILLTYPE,P.MSGTYPE,P.ORIG_NO AS ORIGNO, ");
        sql.append("P.BUSSDT,P.BILL_MONEY AS BILLMONEY,P.PAYER,P.ACCEPTOR,p.payee,l.id as businessKey,l.rcrsokmoney as rcrsokmoney, ");
        sql.append("P.START_DATE AS STARTDATE,P.END_DATE AS ENDDATE,vv.id as vvid,p.reqamt,p.rcrsrsncd,p.rcrs_Tp as rcrsTp,p.appldt, ");
        sql.append("P.REQ_NM AS REQNM,P.REC_NM AS RECNM,P.REC_ACCT_ID RECACCTID,p.REC_CORP_ID corpId,vv.CORP_CODE corpCode,vv.CORP_NAME corpName,");
        sql.append(" vv.APPLY_CORP_ID as applyCorpId,s2.name as applyCorpName,s2.code as applyCorpCode,vv.BUS_CORP_ID as busCorpId,vv.BUS_PERSONNEL as busPersonnel ");
        sql.append(" from AB_BILL_RECEIVEBIZ P ");
//        sql.append(" left join sys_corp c on p.REC_CORP_ID=c.id ");
        sql.append(" join AB_VOUCHERINFO v on v.orig_no=P.orig_no and P.SBBLL_RNG_STRT_SN = v.SBBLL_RNG_STRT_SN and P.SBBLL_RNG_END_SN = v.SBBLL_RNG_END_SN ");
        sql.append(" join AB_ACCRECORDS l on P.id=l.recid ");
        sql.append(" join AB_VOUCHERINFO_VICE vv "
                + "on vv.corp_id=p.REC_CORP_ID "
                + "and v.id=vv.vid and l.vvid = vv.id "
                + "and vv.type='" + AccebillConstant.AB_VOUCHERINFO_OP_TYPE1 + "' "
                + "and vv.VALID_SIGN='" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "' ");
        sql.append(" left join sys_corp s2 on vv.apply_corp_id = s2.id ");
        sql.append(" WHERE 1=1 ");
        sql.append(" and ").append(businessSql).append(" ");
        sql.append(" ) bail ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillSearchforManageVO.class, "bail", true);
    }
}
