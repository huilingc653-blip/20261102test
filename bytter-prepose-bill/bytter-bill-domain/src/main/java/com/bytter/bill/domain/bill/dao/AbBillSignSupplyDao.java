package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillSignSupplyVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 签收补录Dao
 *
 * @author ouyj
 */
@Repository
public class AbBillSignSupplyDao extends BaseDao<AbBillSignSupplyVo, String> {
    /**
     * 加载AbBillSignSupply表数据
     *
     * @param input        前端传参
     * @param corpAuthList 单位集合
     * @return 表集合
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbBillSignSupplyVo> listAbBillSignSupply(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<>(16);
        StringBuilder sql = new StringBuilder("SELECT p.bankendrsmtmk as bankendrsmtmk,l.rec_nm as adr,vv.corp_id as corpId,");
        sql.append(" P.ID as recid,P.BILL_TYPE AS BILLTYPE,P.MSGTYPE,P.ORIG_NO AS ORIGNO,vv.BANK_NAME as recBankTypeName,l.id as id, v.id as vid,  ");
        sql.append(" P.BUSSDT,P.BILL_MONEY AS BILLMONEY,P.PAYER,P.ACCEPTOR,v.PAYER_BANK_NAME as payerbankName,v.ACCEPTOR_BANK_NAME as acceptorbankName,vv.type, ");
        sql.append(" P.START_DATE AS STARTDATE,P.END_DATE AS ENDDATE,vv.id as vvid,p.REC_CORP_ID as recCorpId, p.sttlmmk as sttlmmk,");
        sql.append(" P.REQ_NM AS REQNM,P.REC_NM AS RECNM,P.REC_ACCT_ID RECACCTID,vv.CORP_CODE corpCode,vv.CORP_NAME corpName,p.STATUS,vv.BACK_ACID as lastid, ");
        sql.append(" vv.APPLY_CORP_ID as applyCorpId,vv.APPLY_CORP_NAME as applyCorpName,vv.APPLY_CORP_CODE as applyCorpCode,vv.BUS_CORP_ID as busCorpId,vv.BUS_PERSONNEL as busPersonnel, ");
        //		--前手
        sql.append("case ");
        sql.append("when l.brief_id='6001' then l.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then l.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023", "9101"});
        sql.append("else vv.cn_accrecords end as cnAccrecords, back1.endorsementCount as endorsementCount, vv.BUS_CORP_NAME as busCorpName ");
        sql.append(" from AB_VOUCHERINFO_VICE vv  ");
        sql.append(" left join AB_VOUCHERINFO v on v.id=vv.vid ");
        sql.append(" left join AB_ACCRECORDS l on l.vvid=vv.id ");
        sql.append(" left join AB_BILL_RECEIVEBIZ P on v.orig_no=P.orig_no and P.id=l.recid and l.status!=:status2");
        sql.append(" left join (SELECT COUNT(*) AS ENDORSEMENTCOUNT, ORIG_NO AS ORIGNO FROM AB_BACKBILLINFO BACK WHERE BACK.BUSTYPE = '010'");
        sql.append(" GROUP BY BACK.ORIG_NO) back1 ON back1.origNo = p.orig_no ");
        sql.append(" left join ab_brief_status bs on bs.status=l.brief_id ");
        //票据状态为19待签收
        sql.append(" WHERE (vv.is_sign_supply = :isSignSupply or vv.is_sign_supply is null) and ((P.STATUS in (:STATUS) ");
        //票据签收业务为：017提示保证002提示承兑003提示收票010背书申请012回购式贴现赎回018质押申请019质押解除020提示付款021逾期付款023追索同意清偿
        sql.append(" AND P.MSGTYPE IN (:MSGTYPE) ");
        sql.append(" and l.SEND_SIGN=:sendSign1 ");
        sql.append(" and l.status=:status1 ");
        sql.append(" and l.opt_status=:optStatus ");
        sql.append(" and L.VALID_SIGN = :validSign1 ");
        sql.append(" and vv.box_status=:boxStatus1 ");
        sql.append(" and vv.valid_sign=:validSign1 ");
        sql.append(" and l.brief_id in (:briefId1)");
        sql.append(" and v.end_date>=:end_date) ");
        sql.append(" or ( ")
                //审批中数据
                .append(" l.brief_id in (:briefId9101) ")
                .append(" and l.status = :status3 ")
                .append(" and vv.box_status=:boxStatus3)) ");
        params.put("isSignSupply", 0);
        params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("sendSign1", AccebillConstant.AB_BILL_SEND_SIGN_Y);
        params.put("status1", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("STATUS", new Integer[]{AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK, AccebillConstant.AB_BILL_BUS_STATUS_1});
        params.put("status2", AccebillConstant.AB_BILL_BUS_STATUS_2);
        params.put("boxStatus1", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("briefId1", new String[]{AccebillConstant.BRIEF_STATUS_9003, AccebillConstant.BRIEF_STATUS_9010,
                AccebillConstant.BRIEF_STATUS_9012, AccebillConstant.BRIEF_STATUS_9019});
        params.put("end_date", DateUtils.getDateYMD());
        params.put("briefId9101", AccebillConstant.BRIEF_STATUS_9101);
        params.put("status3", AccebillConstant.AB_BILL_BUS_STATUS11);
        params.put("boxStatus3", AccebillConstant.AB_BOX_STATUS_N);
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "vv.APPLY_CORP_ID", params);
        //用户满足对应查询权限的单位数据
        sql.append(" and ").append(corpAuthSql).append(" ");
        Map<String, Object> formdata = input.getFormdata();
        String bussdt = (String) formdata.get("led_bussdt");
        //手动拼接时间条件，时间戳类型
        if (StringUtils.isNotEmpty(bussdt)) {
            bussdt += " 23:59:59";
            formdata.put("led_bussdt", bussdt);
            input.setFormdata(formdata);
        }
        //应付应收标识
        String type = (String) formdata.get("type");
        if (StringUtils.isNotEmpty(type)) {
            //应付
            if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE0.equals(type)) {
                params.put("MSGTYPE", new String[]{AccebillConstant.AB_MSGTYPE_ACCEPT,
                        AccebillConstant.AB_MSGTYPE_GUARGN,
                        AccebillConstant.AB_MSGTYPE_HINT_PAYER,
                        AccebillConstant.AB_MSGTYPE_HINT_PAYER_OVERDUE});
            }
            //应收
            else if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(type)) {
                params.put("MSGTYPE", new String[]{AccebillConstant.AB_MSGTYPE_RECBILL,
                        AccebillConstant.AB_MSGTYPE_RECITE,
                        AccebillConstant.AB_MSGTYPE_DISCOUNT_REDEM,
                        AccebillConstant.AB_MSGTYPE_PLEDGE,
                        AccebillConstant.AB_MSGTYPE_PLEDGE_RELIEVE,
                        AccebillConstant.AB_MSGTYPE_SEARCHFOR_PAYOFF});
            }
        } else {
            params.put("MSGTYPE", new String[]{AccebillConstant.AB_MSGTYPE_ACCEPT,
                    AccebillConstant.AB_MSGTYPE_RECBILL,
                    AccebillConstant.AB_MSGTYPE_RECITE,
                    AccebillConstant.AB_MSGTYPE_DISCOUNT_REDEM,
                    AccebillConstant.AB_MSGTYPE_GUARGN,
                    AccebillConstant.AB_MSGTYPE_PLEDGE,
                    AccebillConstant.AB_MSGTYPE_PLEDGE_RELIEVE,
                    AccebillConstant.AB_MSGTYPE_HINT_PAYER,
                    AccebillConstant.AB_MSGTYPE_HINT_PAYER_OVERDUE,
                    AccebillConstant.AB_MSGTYPE_SEARCHFOR_PAYOFF});
        }
        if (input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (p.REC_ACCT_ID is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.REC_ACCT_ID", "bankAccs", params)).append(")");
        }
        return this.getDynamicBySQL(input, sql.toString(), AbBillSignSupplyVo.class, "", false, params);
    }
    /**
     * 加载审批列表数据
     *
     * @param input        列表入参数据
     * @param businessList 业务数据ID
     * @return 返回审批列表数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        Map<String, Object> params = new HashMap<>(8);
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append("P.ID,P.BILL_TYPE AS BILLTYPE,P.MSGTYPE,P.ORIG_NO AS ORIGNO,P.sgnupmk,p.REC_ACCT_TYPE_NAME as recBankTypeName, ");
        sql.append("P.BUSSDT,P.BILL_MONEY AS BILLMONEY,P.PAYER,P.ACCEPTOR,l.id as businessKey, ");
        sql.append("P.START_DATE AS STARTDATE,P.END_DATE AS ENDDATE,vv.id as vvid,p.REC_CORP_ID as recCorpId, ");
        sql.append(" vv.APPLY_CORP_ID as applyCorpId,vv.APPLY_CORP_NAME as applyCorpName,vv.APPLY_CORP_CODE as applyCorpCode,vv.BUS_CORP_ID as busCorpId,vv.BUS_PERSONNEL as busPersonnel, ");
        sql.append("P.REQ_NM AS REQNM,P.REC_NM AS RECNM,P.REC_ACCT_ID RECACCTID,rec.CODE corpCode,rec.NAME corpName, ");
        sql.append(" vv.BUS_CORP_NAME as busCorpName ");

        sql.append(",(case ");
        sql.append("when l.create_by is not null then BT_CONCAT4('(',l.create_by,')',l.create_name) ");
        sql.append("else null end) as applyName");
        sql.append(" from AB_BILL_RECEIVEBIZ P ");
//        sql.append(" left join sys_corp c on p.REC_CORP_ID=c.id ");
        sql.append(" left join AB_VOUCHERINFO v on v.orig_no=P.orig_no and P.SBBLL_RNG_STRT_SN = v.SBBLL_RNG_STRT_SN and P.SBBLL_RNG_END_SN = v.SBBLL_RNG_END_SN ");
        sql.append(" left join AB_ACCRECORDS l on P.id=l.recid ");
        sql.append(" left join AB_VOUCHERINFO_VICE vv "
                + "on vv.id=l.vvid ");
//        sql.append(" left join sys_corp s2 on vv.apply_corp_id = s2.id ");
//        sql.append(" left join bt_bank_acc acc on acc.bank_acc =p.REC_ACCT_ID ");
        sql.append(" left join sys_corp rec on rec.id = P.REC_CORP_ID ");
        sql.append(" WHERE 1=1 ");
        String applyName = input.getFormdata().get("applyName").toString();
        if (StringUtils.isNotEmpty(applyName)) {
            sql.append(" and BT_CONCAT4('(',l.create_by,')',l.create_name) like BT_CONCAT3('%',:applyName,'%')");
            params.put("applyName", applyName);
        }
        input.getFormdata().remove("applyName");
        String businessSql = SqlUtil.getInSqlByParams(businessList, "l.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        params.put("VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
        return this.getDynamicBySQL(input, sql.toString(), AbBillSignSupplyVo.class, "", false, params);
    }
}
