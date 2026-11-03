package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolDtlBookVO;
import com.bytter.bill.domain.billPool.vo.AbPoolFinaApplyBookVO;
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
 * @author zhenghj
 * @version 1.0
 * @Title: Dao
 * @Description: 票据池出票融资申请明细账簿信息DO
 * @date 2020-04-14
 */
@Repository
public class AbPoolFinaApplyBookDao extends BaseDao<AbPoolFinaApplyBookVO, String> {
    public String getBaseSql(boolean isBusQuery) {
        StringBuilder sql = new StringBuilder("select ");
        //默认取账簿明细表数据
        sql.append("d.id,sc.id as corpId,sc.code as corpCode,sc.name as corpName,v.bill_type as billType,v.bill_flag as billFlag,d.orig_no as origNo,d.create_time as createTime,v.bill_money as billMoney,v.start_date as startDate,v.end_date as endDate, ");
        sql.append("v.payer,v.payee,v.acceptor,p.bp_code as poolNo,p.bp_name as poolName,d.bankendrsmtmk,d.sttlmmk,d.rsn,d.update_time as updateTime,d.rec_name as recName,d.inout_type as inoutType,d.brief_id as briefId,v.PAYERACC as payeracc ");
        sql.append(" From ");
        if (isBusQuery) {
            //支持去业务数据表信息
            sql.append("(select d.id,vv.corp_id,d.bp_id,d.brief_id, bt_nvl_char(d.orig_no,v.orig_no) as orig_no,a.bankendrsmtmk,a.sttlmmk,a.rsn,d.create_time as update_time,null as create_time,a.rec_nm as rec_Name,d.inout_type ");
            sql.append("from ab_pool_dtl d  ");
            sql.append("left join ab_accrecords a on d.id=a.pd_id ");
            sql.append("left join ab_voucherinfo_vice vv on a.vvid=vv.id ");
            sql.append("left join ab_voucherinfo v on a.vid = v.id  ");
            sql.append("where a.brief_id in ('8001')  and d.opt_status='19' and a.opt_status='19'  ) ");
        } else {
            //默认取账簿明细表数据
            sql.append(" ab_pool_dtl_book ");
        }
        sql.append(" d ");
        sql.append("left join ab_voucherinfo v on v.orig_no=d.orig_no ");
        sql.append("left join ab_pool p on d.bp_id=p.id ");
        sql.append("left join ab_pool_corp pc on pc.corp_id=d.corp_id and pc.valid_Sign=:validSign ");
        sql.append("left join sys_corp sc on sc.id=pc.corp_id  ");
        sql.append("where 1=1 ");
        return sql.toString();
    }
    public StringBuilder getBookSql() {
        StringBuilder sql = new StringBuilder("select ");
        //sql.append("fb.ID as id,APPLY_CODE as applyCode,CUST_ID as custId,fb.CORP_ID as corpId,sc.name as corpName,sc.code as corpCode,");
        sql.append("fb.ID as id,APPLY_CODE as applyCode,CUST_ID as custId,fb.CORP_ID as corpId,pc.corp_name as corpName,pc.corp_code as corpCode,");
        sql.append("BP_ID as bpId,p.bp_name as poolName,FINANCE_AMT as financeAmt,APPLY_DATE as applyDate,ST_DATE as stDate,END_DATE as endDate,");
        sql.append("APPLY_STATUS as applyStatus,APPLY_STATUS_NAME as applyStatusName,BIZ_CODE as bizCode,BIZ_NAME as bizName,");
        sql.append("REMARK as remark,CURRENCY ,CURRNAME ,BUSINESS_TYPE as businessType,BUSINESS_NAME as businessName,");
        sql.append("fb.TYPE ,SRVR_ID as srvrId,UPIMAGE ,IMAGE_NAME as imageName,UPSUCCESS ,SUCCESSNAME ,CONTRACT_CODE as contractCode,");
        sql.append("CONTRACT_ID as contractId,CONTRACT_NAME as contractName,FIRMARGIN_ACCTID as firmarginAcctid,FIRMARGIN_AMT as firmarginAmt,");
        sql.append("FIRMARGIN_PERCTGE as firmarginPerctge,BACMARGIN_ACCTID as bacmarginAcctid,ACCTID as acctId,FINPURPOSE as finPurpose,");
        sql.append("APPLYDESC as applyDesc,RETURNREASON,fb.CREATE_TIME as createTime,fb.UPDATE_TIME as updateTime,fb.bill_type as billType ");
        sql.append(" from AB_POOL_FINAAPPLY_BOOK  fb");
        sql.append(" left join ab_pool p on fb.bp_id=p.id ");
        sql.append(" left join ab_pool_corp pc on pc.corp_id=fb.corp_id and pc.valid_Sign=1 ");
        //sql.append(" left join sys_corp sc on sc.id=pc.corp_id  ");
        sql.append(" where 1=1 ");
        return sql;
    }
    public StringBuilder getDtlSql() {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" dtl.id,BOOK_ID as bookId,CORP_ID AS corpId,APPLY_CODE as applyCode,");
        sql.append(" CUST_ID as custId, ORIG_NO  as origNo,BILL_MONEY as billMoney,BUS_TYPE as busType,");
        sql.append(" BILL_STATUS as billStatus,PAYER as payer,PAYERACC as payerAcc, PAYER_BANK_NAME as payerBankName,");
        sql.append(" PAYERBANKNO as payerBankNo,PAYEE as payee,PAYEEACC as payeeAcc,PAYEE_BANK_NAME as payeeBankName,");
        sql.append(" PAYEEBANKNO  as payeeBankno,ACCEPTOR as acceptor,ACCEPTORACC as acceptorAcc,ACCEPTORBANKNO as acceptorBankNo,");
        sql.append(" ACCEPTOR_BANK_NAME as acceptorBankName,START_DATE as startDate,END_DATE as endDate, BILL_TYPE as billType,");
        sql.append(" INVCNO as invcNo,REALPAY_AMT as realpayAmt,REDMREALPAY_AMT as redmrealpayAmt,BANENDRSMTMK,PROMSFEE,");
        //sql.append(" sc.name as corpName,sc.code as corpCode");
        sql.append(" apc.corp_name as corpName,apc.corp_code as corpCode");
        sql.append(" from AB_POOL_FINAAPPLY_DTL dtl");
        //sql.append(" left join sys_corp sc on sc.id=dtl.corp_id");
        sql.append(" left join ab_pool_corp apc on apc.corp_id = dtl.corp_id");
        sql.append(" where 1=1");
        return sql;
    }
    /**
     * "查询票据池出票融资登记列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput datatableQueryBook(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<String, Object>();
        String stime = (String) dataTablesInput.getFormdata().get("led_startDate");
        if (StringUtils.isNotBlank(stime)) {
            stime = stime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_startDate", stime);
        }
        String etime = (String) dataTablesInput.getFormdata().get("led_endDate");
        if (StringUtils.isNotBlank(etime)) {
            etime = etime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_endDate", etime);
        }
        StringBuilder sql = new StringBuilder(getBookSql());
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "pc.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolFinaApplyBookVO.class, null, false, params);
    }
    /**
     * "查询票据池出票融资登记列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput datatableQueryDtl(DataTablesInput dataTablesInput) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(getDtlSql());
        String id = (String) dataTablesInput.getFormdata().get("id");
        if (StringUtils.isNotBlank(id)) {
            sql.append(" and book_id=:bookId ");
            params.put("bookId",id);
            dataTablesInput.getFormdata().remove("id");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolFinaApplyBookVO.class, null, false, params);
    }
    /**
     * "查询票据池明细账簿信息列表数据
     *
     * @param entity
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput datatableQuery(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        Map<String, Object> params = new HashMap<String, Object>();
        String stime = (String) dataTablesInput.getFormdata().get("led_startDate");
        if (StringUtils.isNotBlank(stime)) {
            stime = stime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_startDate", stime);
        }
        String etime = (String) dataTablesInput.getFormdata().get("led_endDate");
        if (StringUtils.isNotBlank(etime)) {
            etime = etime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_endDate", etime);
        }
        StringBuilder sql = new StringBuilder(getBaseSql(true));
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "sc.id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolDtlBookVO.class, null, false, params);
    }
    /**
     * 根据ID获取明细账簿信息
     *
     * @param entity
     * @return
     */
    public List<AbPoolDtlBookVO> getAbPoolDtlBookVOById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(getBaseSql(true));
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        sql.append(" and d.id=:id");
        params.put("id", id);
        return this.listBySQLAliasToBean(sql.toString(), params, AbPoolDtlBookVO.class);
    }
}
