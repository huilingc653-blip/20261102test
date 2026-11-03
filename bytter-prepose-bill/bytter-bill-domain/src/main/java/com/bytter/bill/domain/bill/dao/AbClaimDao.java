package com.bytter.bill.domain.bill.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbClaimVo;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayVo;
import com.bytter.bill.domain.bill.vo.BisAccDtlVo;
import com.bytter.bill.domain.bill.vo.listAbClaimInfoAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.constant.BtCurrencyConstant;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 票据认领处理dao
 *
 * @author ouyj
 */
@Repository
public class AbClaimDao extends BaseDao<AbClaimVo, String> {
    /**
     * 查询自动认领票据记录数据
     *
     * @param origNos        票据号数组
     * @param isEmitMake     是否补录
     * @param claimStartDate 认领起始日期
     * @param claimId        认领id
     * @param filterDeposit  是否过滤保证金为100%的数据
     * @return 自动认领票据记录数据
     */
    public List<AbClaimVo> selectClaimVoList(String[] origNos, boolean isEmitMake, Date claimStartDate, String claimId,boolean filterDeposit) {
        return selectClaimVoList(origNos,isEmitMake,claimStartDate,claimId,null,filterDeposit);
    }
    public List<AbClaimVo> selectClaimVoList(String[] origNos, boolean isEmitMake, Date claimStartDate, String claimId, Date endDate,boolean filterDeposit) {
        Map<String, Object> params = new HashMap<>(16);
        StringBuilder sql = new StringBuilder();
//        sql.append("select acc.BANK_TYPE_ID as bankTypeId,");
        billCommonSql(params, sql);
        //从表validSign=1
        sql.append(" WHERE P.VALID_SIGN=:VALID_SIGN_1");
        //过滤保证金为100%的数据
        if(filterDeposit){
            sql.append(" and (P.bail_percent != 1 or P.bail_percent is null )");
        }
        params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        if (StringUtils.isEmpty(claimId)) {
            sql.append(" and (p.claim_status in (:claimStatus) or p.claim_status is null) ");
            params.put("claimStatus", new String[]{AccebillConstant.BILL_CLAIM_STATUS_1, AccebillConstant.BILL_CLAIM_STATUS11});
        } else {
            sql.append(" and p.claim_status = :claimStatus ");
            params.put("claimStatus", AccebillConstant.BILL_CLAIM_STATUS94);
        }
        params.put("claimStartDate", claimStartDate);
        //贴现数据
        sql.append(" and ( ");
        addDiscountSql(params, sql, true);
        addCollectionSql(params, sql);
        addPaySql(params, sql, isEmitMake);
        sql.append(") ) bill ");
        sql.append(" left join bt_bank_acc acc on bill.bankAcc = acc.BANK_ACC where 1=1");
        if (!StringUtils.isEmpty(origNos)) {
            sql.append(" and bill.origNo in (:origNos) ");
            params.put("origNos", origNos);
        }
        if (!StringUtils.isEmpty(claimId)) {
            sql.append(" and bill.id = :claimId ");
            params.put("claimId", claimId);
        }
        if (endDate!=null) {
            sql.append(" and bill.claimDate < :endDate ");
            params.put("endDate", endDate);
        }
        return this.listBySQLAliasToBean(sql.toString(), params, AbClaimVo.class);
    }
    /**
     * 查询自动认领银行流水数据。
     *
     * @return 自动认领银行流水数据。
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> selectBankAccList() {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.id as claimId,m.claim_sequence AS claimSequence,m.rmk AS claimRmk,m.orig_no as origNo from AB_CLAIM_MIDDLE m ");
        sql.append(" where (m.claim_status in (:claimStatus) or m.claim_status is null) ");
        params.put("claimStatus", new String[]{AccebillConstant.BILL_CLAIM_STATUS_1, AccebillConstant.BILL_CLAIM_STATUS11});
        return listBySQL(sql.toString(), params);
    }
    /**
     * 查询自动认领银行流水数据
     *
     * @return 自动认领银行流水数据
     */
    public List<BisAccDtlVo> selectBankAccList(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        accCommonSql(null, params, sql);
        sql.append(" and (m.claim_status in (:claimStatus) or m.claim_status is null) ");
        params.put("claimStatus", new String[]{AccebillConstant.BILL_CLAIM_STATUS_1, AccebillConstant.BILL_CLAIM_STATUS11});
        if (params.get("cdSign") != null) {
            sql.append(" and acc.cd_sign = :cdSign ");
        }
        if (params.get("bankTypeId") != null) {
            sql.append(" and b.bank_type_id = :bankTypeId ");
        }
        if (params.get("field") != null && params.get("origNo") != null) {
            sql.append(" and acc.").append(params.get("field")).append(" like :origNo ");
        }
        if (params.get("bankAcc") != null) {
            sql.append(" and acc.bank_acc in (:bankAcc) ");
        }
        if (params.get("billMoney") != null) {
            sql.append(" and acc.amt = :billMoney ");
        }
        if (params.get("claimDate") != null) {
//            sql.append(" and to_char(acc.trans_time,'yyyy-MM-dd') = :claimDate ");
            sql.append(" and acc.trans_time >= :claimDateTime ");
            sql.append(" and acc.trans_time < :claimDateEndTime ");
            params.put("claimDateTime",DateUtils.parseDate((String)params.get("claimDate")));
            params.put("claimDateEndTime",DateUtils.addDay(DateUtils.parseDate((String)params.get("claimDate")),1));
        }
        if(params.get("billMoneyLeft") != null && params.get("billPayMoneyLeft") != null ){
            sql.append(" and ((acc.amt >= :billMoneyLeft ");
            sql.append(" and acc.amt <= :billMoneyRight) ");
            sql.append(" or (acc.amt >= :billPayMoneyLeft ");
            sql.append(" and acc.amt <= :billPayMoneyRight) )");
        } else if (params.get("billMoneyLeft") != null) {
            sql.append(" and acc.amt >= :billMoneyLeft ");
            sql.append(" and acc.amt <= :billMoneyRight ");
        } else if (params.get("billPayMoneyLeft") != null) {
            sql.append(" and acc.amt >= :billPayMoneyLeft ");
            sql.append(" and acc.amt <= :billPayMoneyRight ");
        }
        if (params.get("claimDateLeft") != null) {
//            sql.append(" and to_char(acc.trans_time,'yyyy-MM-dd') >= :claimDateLeft ");
//            sql.append(" and to_char(acc.trans_time,'yyyy-MM-dd') <= :claimDateRight ");
            sql.append(" and acc.trans_time >= :claimDateLeftTime ");
            sql.append(" and acc.trans_time < :claimDateRightTime ");
            params.put("claimDateLeftTime",DateUtils.parseDate((String)params.get("claimDateLeft")));
            params.put("claimDateRightTime",DateUtils.addDay(DateUtils.parseDate((String)params.get("claimDateRight")),1));
        }
        if (params.get("accIdList") != null && ((String[]) params.get("accIdList")).length > 0) {
            sql.append(" and acc.id not in (:accIdList) ");
        }
        if (params.get("transTimeLeft") != null) {
//            sql.append(" and bt_to_char(acc.trans_time,'yyyy-MM-dd') >= :transTimeLeft ");
            sql.append(" and acc.trans_time >= :transTimeLeftTime ");
            params.put("transTimeLeftTime",DateUtils.parseDate((String)params.get("transTimeLeft")));
        }
        if (params.get("ruleSql") != null) {
            String ruleSql = (String)params.get("ruleSql");
            sql.append(ruleSql);
        }
        return this.listBySQLAliasToBean(sql.toString(), params, BisAccDtlVo.class);
    }
    /**
     * 查询票据认领处理列表数据
     *
     * @param input          前端参数
     * @param corpAuthSql    单位权限集合
     * @param isEmitMake     是否补录
     * @param claimStartDate 认领起始日期
     * @return 票据认领处理列表数据
     */
    public DataTablesOutput listAbClaimInfo(DataTablesInput input, String corpAuthSql, boolean isEmitMake, Date claimStartDate) {
        Map<String, Object> params = new HashMap<>(16);
        StringBuilder sql = new StringBuilder();
        billCommonSql(params, sql);
        //从表validSign=1
        sql.append(" WHERE P.VALID_SIGN=:VALID_SIGN_1");
        params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(")");
        }
        if (input.getFormdata().get("claimQuery") == null) {
            sql.append(" and (p.claim_status in (:claimStatus) or p.claim_status is null) ");
            params.put("claimStatus", new String[]{AccebillConstant.BILL_CLAIM_STATUS_1, AccebillConstant.BILL_CLAIM_STATUS11});
        }
        params.put("claimStartDate", claimStartDate);
        //贴现数据
        sql.append(" and ( ");
        addDiscountSql(params, sql, true);
        addCollectionSql(params, sql);
        addPaySql(params, sql, isEmitMake);
        sql.append(") ) bill ");
        sql.append(" left join bt_bank_acc acc on bill.bankAcc = acc.BANK_ACC where 1=1");
        Object busType = input.getFormdata().get("claimBusType");
        if (busType != null&&StringUtils.isNotBlank(busType.toString())) {
            String claimBusType = String.valueOf(busType);
            sql.append(" and claimType in (:claimType) ");
            switch (claimBusType) {
                case "001":
                    params.put("claimType", new String[]{AccebillConstant.BRIEF_STATUS_6020, AccebillConstant.BRIEF_STATUS_020, AccebillConstant.BRIEF_STATUS_021});
                    break;
                case "002":
                    params.put("claimType", new String[]{AccebillConstant.BRIEF_STATUS_6012, AccebillConstant.BRIEF_STATUS_011});
                    break;
                default:
                    params.put("claimType", new String[]{AccebillConstant.BRIEF_STATUS_001, AccebillConstant.BRIEF_STATUS_003,
                            AccebillConstant.BRIEF_STATUS_6101, AccebillConstant.BRIEF_STATUS_6120,});
            }
        }
        return this.getDynamicBySQLForBill(input, sql.toString(), AbClaimVo.class, "bill", false, params);
    }
    /**
     * 票据记录公共查询列
     **/
    private void billCommonSql(Map<String, Object> params, StringBuilder sql) {
        if(StringUtils.isBlank(sql.toString())){
            sql.append(" select ");
        }
        sql.append(" bill.id as id,vid,vvid,aid,billType,corpId,corpName,corpCode,billFlag,payer,acceptor,origNo,billMoney,startDate,endDate,");
        sql.append(" briefId,claimType,billSort,payerBankName,payee,sendSign,bankAcc,cnAccrecords,rearAccrecords,");
        sql.append(" claimDate,claimSequence,claimPerson,claimTime,claimStatus,realMoney,sbbllRngStrtSn,sbbllRngEndSn, ");
        //todo acc.BANK_TYPE_ID as bankTypeId,  通过bankAcc 掉接口去查询把
        sql.append(" newStmIssuofpprInd,applyCorpId,applyCorpName,busCorpId,busCorpName,billBankAcc,bankTypeId ");
        sql.append(" ,bailMoney,bailPercent,payMoney ");
        sql.append(" from (  ");
        sql.append(" select  P.bank_type_id as bankTypeId,P.bank_acc as billBankAcc,P.BUS_CORP_ID as busCorpId,P.BUS_CORP_NAME as busCorpName,P.APPLY_CORP_ID as applyCorpId,P.APPLY_CORP_NAME as applyCorpName,V.ID AS vid,P.ID AS vvid,m.ID AS id,V.BILL_TYPE AS billType, s.id as aid,REAL_MONEY AS realMoney,");
        sql.append(" P.CORP_ID AS corpId,P.CORP_NAME AS corpName,p.CORP_CODE AS corpCode,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,");
        sql.append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,  ");
        sql.append(" S.BRIEF_ID AS briefId, S.BRIEF_ID AS claimType, P.type as billSort, ");
        sql.append(" v.payer_bank_name as payerBankName,v.payee as payee, ");
        sql.append(" s.send_sign as sendSign, ");
        //账号：贴现业务显示贴现办理入账账户，托收业务显示票据所属账号，兑付业务银票显示出票人账号，兑付业务商票显示承兑人账号。
        sql.append(" case ");
        sql.append(" when s.brief_id='011' then T.AOACCNINF_ID ");
        sql.append(" when s.brief_id in ('020','021') then p.BANK_ACC ");
        sql.append(" when s.bill_no is not null then s.PAYER_ACC ");
        sql.append(" when V.BILL_TYPE='10' then v.PAYERACC ");
        sql.append(" when V.BILL_TYPE='11' then v.ACCEPTORACC ");
        sql.append(" else '' end AS bankAcc, ");
        //贴现业务显示贴现日期，兑付业务显示到期日期，托收业务显示到期日期。
        sql.append(" case ");
        sql.append(" when s.brief_id='011' then t.DISCOUNT_DATE ");
        sql.append(" else V.END_DATE end AS claimDate, ");
        //前手
        sql.append("case ");
        sql.append("when s.brief_id='6001' then s.cn_accrecords ");
        sql.append("when bs.bus_type in (:bus_type_0) then s.rec_nm ");
        params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
        sql.append("else '' end as cnAccrecords, ");
        //后手
        sql.append("case ");
        sql.append("when bs.bus_type in (:bus_type_1) then s.rec_nm ");
        params.put("bus_type_1", new String[]{"001", "017", "002", "003", "010", "018", "019", "020", "011", "012", "022", "6201", "6203", "6004", "023"});
        sql.append("else '' end as rearAccrecords, ");
        sql.append(" m.claim_sequence as claimSequence, m.claim_person as claimPerson, m.claim_time as claimTime, ");
        sql.append(" BT_NVL_CHAR(p.claim_status,'11') as claimStatus ");
        //新一代票据新增出票标志、子票区间开始序号、子票区间结束序号
        sql.append(", V.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn ");
        sql.append(" ,BT_NVL_NUM(P.BAIL_MONEY,0) as bailMoney,BT_NVL_NUM(P.bail_percent,0) as bailPercent,V.bill_money-BT_NVL_NUM(P.BAIL_MONEY,0) as payMoney ");
        sql.append(" from AB_VOUCHERINFO_VICE P");
        sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID");
        sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
        sql.append(" LEFT JOIN AB_DISCOUNT T ON T.RECORD_ID=S.ID");
//        sql.append(" LEFT JOIN sys_corp sc ON sc.id=p.corp_id");
        sql.append(" left join ab_brief_status bs on bs.status=s.brief_id ");
        sql.append(" LEFT JOIN AB_CLAIM_MIDDLE m on m.id = p.CLAIM_ID");
//        sql.append(" left join nis_billhead n on s.id = n.erp_id ");
    }
    /**
     * 添加兑付数据sql
     *
     * @param params     参数
     * @param sql        sql
     * @param isEmitMake 是否补录
     */
    private void addPaySql(Map<String, Object> params, StringBuilder sql, boolean isEmitMake) {
        sql.append(" or (  ");
        sql.append(" V.END_DATE >= :claimStartDate and ( ( (");
        //系统需要开票补录功能时
        if (isEmitMake) {
            // 业务类型 brief_id -- 6101(补录)
            sql.append(" s.brief_id in (:briefId6101) ");
        } else {
            //系统不需要开票补录功能时
            // 指令发送提示收票完成(线上)和开票登记完成(线下)就可以做兑付确认 不需要补录`
            sql.append(" ( (s.brief_id in (:briefId003) and s.send_sign=:sendSign1 ) or (s.brief_id in (:briefId001) and s.send_sign=:sendSign0 ) or s.brief_id in (:briefId6101)) ");
        }
        sql.append(" and p.box_status=:boxStatusY and s.valid_sign= :validSign1 and s.status=:status95 and s.opt_status=:optStatus19 )");
        // 业务类型 :brief_id -- 应付兑付确认 6120
        // 审批处理状态 :status -- 审批中 11
        sql.append(" or (s.brief_id =:briefId6120 and s.status=:status11 ) ) ");
        if (!isEmitMake) {
            params.put("sendSign0", new String[]{AccebillConstant.AB_BILL_SEND_SIGN_N});
            params.put("sendSign1", new String[]{AccebillConstant.AB_BILL_SEND_SIGN_Y});
            params.put("briefId001", new String[]{AccebillConstant.BRIEF_STATUS_001});
            params.put("briefId003", new String[]{AccebillConstant.BRIEF_STATUS_003});
        }
        params.put("briefId6101", new String[]{AccebillConstant.BRIEF_STATUS_6101});
        params.put("briefId6120", AccebillConstant.BRIEF_STATUS_6120);
        // 线下兑付审批通过数据
        sql.append(" or ( s.brief_id =:briefId6120 and p.box_status=:boxStatusY and (s.status=:status95 or s.status=:status100) and s.valid_sign=:validSign1 ");
        sql.append(" and s.EMIT_STTLMMK = :emitSttlmmk0 ) ");
        // 线上兑付确认已付款数据
        sql.append(" or ( s.brief_id =:briefId6120 and p.box_status=:boxStatusY and s.status=:status100 and s.valid_sign=:validSign1 ");
        sql.append(" and s.EMIT_STTLMMK = :emitSttlmmk1 ) ");
        sql.append(" ))");
        params.put("status100", AccebillConstant.AB_BILL_BUS_STATUS100);
        params.put("emitSttlmmk0", AccebillConstant.AB_BILL_PAYEMITSTTLMMK0);
        params.put("emitSttlmmk1", AccebillConstant.AB_BILL_PAYEMITSTTLMMK1);
    }
    /**
     * 添加托收数据sql
     *
     * @param params 参数
     * @param sql    sql
     */
    private void addCollectionSql(Map<String, Object> params, StringBuilder sql) {
        //票据业务排除票据池流水
        sql.append(" or ( ");
        sql.append(" s.pd_id is null ");
        sql.append(" and V.END_DATE >= :claimStartDate ");
        sql.append(" and ( ( ");
        sql.append(" s.valid_sign=:validSign1 and p.box_status = :boxStatusY ");
        //默认查询
        sql.append(" and s.brief_id in (:briefIds)");
        sql.append(" and  s.status = :status95 and s.opt_status = :optStatus19 ");
        //只对线下清算或离线模式的票据进行到账确认
        sql.append(" and (s.sttlmmk = :sttlmmkSM01 or s.send_sign = :sendSignN ) ");
        sql.append(" ) or ( ");
        sql.append(" s.valid_sign=:validSign0 and p.box_status = :boxStatusN ");
        sql.append(" and (s.status = :status11 or s.status = :status94 ) and s.brief_id = :briefId6020 ");
        sql.append(" ) or ( ");
        sql.append(" p.back_brief_id = :briefId6020 and s.valid_sign != '0' and p.box_status = :boxStatusY )) ");
        sql.append(" ) ");
        params.put("boxStatusY", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("validSign0", AccebillConstant.AB_BILL_VALID_SIGN_0);
        params.put("status11", AccebillConstant.AB_BILL_BUS_STATUS11);
        params.put("briefId6020", AccebillConstant.BRIEF_STATUS_6020);
        params.put("briefIds", new String[]{AccebillConstant.BRIEF_STATUS_020, AccebillConstant.BRIEF_STATUS_021});
        params.put("status94", AccebillConstant.AB_BILL_BUS_STATUS94);
        params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("optStatus19", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("boxStatusN", AccebillConstant.AB_BOX_STATUS_N);
    }
    /**
     * 添加贴现数据sql
     *
     * @param params     参数
     * @param sql        sql
     * @param isNeedDate 是否需要判断贴现日期
     */
    private void addDiscountSql(Map<String, Object> params, StringBuilder sql, boolean isNeedDate) {
        //第一种应收贴现办理已完成或贴现到账打回
        //流水validSign=1,optStatus=19,status=95,主表boxStatus=11
        sql.append(" (((S.BRIEF_ID IN (:BRIEF_ID_1) and S.VALID_SIGN=:VALID_SIGN_2 ");
        sql.append(" AND S.OPT_STATUS=:OPT_STATUS_1 AND S.STATUS=:STATUS_1");
        sql.append(" AND P.BOX_STATUS=:BOX_STATUS_1 ");
        //只对线下清算或离线模式的票据进行到账确认
        sql.append(" and (s.sttlmmk = :sttlmmkSM01 or s.send_sign = :sendSignN ) )");
        //第二种贴现到账处于审批中
        //流水validSign=0,status=11,主表boxStatus=10
//        sql.append(" or (S.BRIEF_ID IN (:BRIEF_ID_2) and S.VALID_SIGN=:VALID_SIGN_3");
//        sql.append(" and P.BOX_STATUS=:BOX_STATUS_2 AND S.STATUS in (:STATUS_2))) ");
        sql.append(" ) ");
        if (isNeedDate) {
            sql.append(" and t.DISCOUNT_DATE >= :claimStartDate ");
        }
        sql.append(" ) ");
        params.put("BRIEF_ID_1", AccebillConstant.BRIEF_STATUS_011);
        params.put("VALID_SIGN_2", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("OPT_STATUS_1", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("STATUS_1", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("BOX_STATUS_1", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("sttlmmkSM01", AccebillConstant.AB_STTLMMK_N);
        params.put("sendSignN", AccebillConstant.AB_BILL_SEND_SIGN_N);
        params.put("BRIEF_ID_2", AccebillConstant.BRIEF_STATUS_6012);
        params.put("VALID_SIGN_3", AccebillConstant.AB_BILL_VALID_SIGN_0);
        params.put("BOX_STATUS_2", AccebillConstant.AB_BOX_STATUS_N);
        Integer[] status_2 = {AccebillConstant.AB_BILL_BUS_STATUS11, AccebillConstant.AB_BILL_BUS_STATUS94};
        params.put("STATUS_2", status_2);
    }
//    /**
//     * 查询票据认领处理的银行流水数据
//     *
//     * @param dataTablesInput 查询参数
//     * @param corpAuthSql     单位权限
//     * @return 银行流水数据
//     */
//    public DataTablesOutput listAbClaimInfoAcc(DataTablesInput dataTablesInput, String corpAuthSql) {
//        Map<String, Object> params = new HashMap<>(4);
//        StringBuilder sql = new StringBuilder();
//        accCommonSql(corpAuthSql, params, sql);
//        if (dataTablesInput.getFormdata().get("claimQueryAcc") == null) {
//            sql.append(" and (m.claim_status in (:claimStatus) or m.claim_status is null) ");
//            params.put("claimStatus", new String[]{AccebillConstant.BILL_CLAIM_STATUS_1, AccebillConstant.BILL_CLAIM_STATUS11});
//        }
//        return this.getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVo.class, "", false, params);
//    }
    /**
     * 查询票据认领处理的银行流水数据。
     *
     * @param dataTablesInput 查询参数
     * @return 银行流水数据。
     */
    @SuppressWarnings("unchecked")
    public List<listAbClaimInfoAccVO> listAbClaimInfoAcc(DataTablesInput dataTablesInput){
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql=new StringBuilder();
        sql.append(" select m.id as id,m.claim_sequence AS claimSequence,m.rmk AS claimRmk,m.orig_no as origNo from  AB_CLAIM_MIDDLE m where 1=1 ");
        if (dataTablesInput.getFormdata().get("claimQueryAcc") == null) {
            sql.append(" and (m.claim_status in (:claimStatus) or m.claim_status is null) ");
            params.put("claimStatus", new String[]{AccebillConstant.BILL_CLAIM_STATUS_1, AccebillConstant.BILL_CLAIM_STATUS11});
        }
        return this.listBySQLAliasToBean(sql.toString(), params, listAbClaimInfoAccVO.class);
    }
    /**
     * 票据认领待确认或已确认银行流水列表
     *
     * @param dataTablesInput 查询参数
     * @return 前端列表数据
     **/
    public  List<listAbClaimInfoAccVO>  listAbClaimConfirmedInfoAcc(DataTablesInput dataTablesInput) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql=new StringBuilder();
        sql.append(" select m.id as id,m.claim_sequence AS claimSequence,m.rmk AS claimRmk,m.orig_no as origNo from  AB_CLAIM_MIDDLE m where 1=1 ");
        sql.append(" and m.claim_status = :claimStatus ");
        params.put("claimStatus", dataTablesInput.getFormdata().get("claimStatus"));
        return this.listBySQLAliasToBean(sql.toString(), params, listAbClaimInfoAccVO.class);
    }
    // todo: BIS_ACC_DTL表尚未拆分，该方法可以迁到对应模块，结果集合并
    private void accCommonSql(String corpAuthSql, Map<String, Object> params, StringBuilder sql) {
        sql.append(" select acc.id as id, acc.trans_time AS transTime, ACC.corp_id AS corpId, acc.bank_acc AS bankAcc, acc.acc_name AS accName,acc.BANK_NAME AS bankName,");
        sql.append(" case when ACC.bank_type_id is not null then  ACC.bank_type_id else b.bank_type_id  end AS bankTypeId,acc.cd_sign AS cdSign, acc.cur_id AS curId, '人民币' as curName, m.claim_sequence AS claimSequence,m.rmk AS claimRmk, ");
        sql.append(" acc.remark AS remark,acc.uses AS uses, acc.postscript AS postscript, acc.bank_serial_no AS bankSerialNo, acc.claim_id as claimId,");
        sql.append(" acc.OPP_ACC_NO AS oppAccNo, acc.OPP_ACC_NAME AS oppAccName, acc.OPP_ACC_BANK AS oppAccBank, acc.amt AS amt, acc.bal AS bal, m.orig_no as origNo ");
        sql.append(" from BIS_ACC_DTL ACC ");
        sql.append(" LEFT JOIN AB_CLAIM_MIDDLE m ON m.id = acc.claim_id ");
        sql.append(" LEFT JOIN bt_bank_acc b ON b.bank_acc = acc.bank_acc ");
//        sql.append(" LEFT JOIN BT_CURRENCY CUR ON ACC.CUR_ID = CUR.ID AND CUR.CUR_NAME = :RMB ");
        sql.append(" WHERE (ACC.STATUS <> :STATUS_2 OR ACC.STATUS IS NULL) ");
        sql.append(" AND ACC.CUR_ID = :RMB ");
        sql.append(" AND (ACC.BLEND_SIGN = :BLEND_SIGN0 or ACC.BLEND_SIGN is NULL ) AND ACC.ID NOT IN ");
        sql.append(" (SELECT ACC2.ID FROM BIS_ACC_DTL ACC2 INNER JOIN BIS_EXC E ON ACC2.BANK_SERIAL_NO = E.BANK_SERIAL_NO AND ACC2.BANK_SERIAL_NO IS NOT NULL AND ACC2.BANK_SERIAL_NO <> '' ) ");
        if (corpAuthSql != null) {
            sql.append(" and ").append(corpAuthSql).append(" ");
        }
        params.put("RMB", BtCurrencyConstant.RMBID);
        params.put("STATUS_2", AccebillConstant.AB_BILL_BUS_STATUS_2);
        params.put("BLEND_SIGN0", AccebillConstant.AB_IS_PAY_N);
    }
    /**
     * 票据认领已确认列表
     *
     * @param input       查询参数
     * @param corpAuthSql 单位权限
     * @return 前端列表数据
     **/
    public DataTablesOutput listAbClaimConfirmedInfo(DataTablesInput input, String corpAuthSql) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder();
        billCommonSql(params, sql);
        //从表validSign=1
        sql.append(" WHERE P.VALID_SIGN=:VALID_SIGN_1");
        params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        sql.append(" and ").append(corpAuthSql).append(" ");
        String claimQueryStr = (String) input.getFormdata().get("claimQueryDays");
        if (!StringUtils.isEmpty(claimQueryStr)) {
            int subDays = Integer.parseInt(claimQueryStr) * -1;
            Date today = DateUtils.addDay(DateUtils.getDateYMD(), 1);
            Date endDate = DateUtils.addSecond(today, -1);
            Date startDate = DateUtils.addDay(DateUtils.getDateYMD(), subDays);
            sql.append(" and m.claim_time >= :startDateQuery ");
            sql.append(" and m.claim_time <= :endDateQuery ");
            params.put("startDateQuery", startDate);
            params.put("endDateQuery", endDate);
        }
        if (input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(")");
        }
        sql.append(" AND p.claim_status = :claimStatus95 ");
        sql.append(" and s.create_date = (select max(ss.create_date) from ab_accrecords ss where s.vvid = ss.vvid) ");
        sql.append(" ) bill where 1=1 ");
     //   sql.append(" left join bt_bank_acc acc on bill.bankAcc = acc.BANK_ACC where 1=1");
        params.put("claimStatus95", AccebillConstant.BILL_CLAIM_STATUS95);
        return this.getDynamicBySQL(input, sql.toString(), AbClaimVo.class, "bill", false, params);
    }
//    /**
//     * 票据认领待确认或已确认银行流水列表
//     *
//     * @param dataTablesInput 查询参数
//     * @param corpAuthSql     单位权限
//     * @return 前端列表数据
//     **/
//    public DataTablesOutput listAbClaimConfirmedInfoAcc(DataTablesInput dataTablesInput, String corpAuthSql) {
//        Map<String, Object> params = new HashMap<>(4);
//        StringBuilder sql = new StringBuilder();
//        accCommonSql(corpAuthSql, params, sql);
//        sql.append(" and m.claim_status = :claimStatus ");
//        params.put("claimStatus", dataTablesInput.getFormdata().get("claimStatus"));
//        return this.getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVo.class, "", false, params);
//    }
//    /**
//     * 票据认领待确认或已确认银行流水列表。
//     *
//     * @param dataTablesInput 查询参数。
//     * @return 前端列表数据。
//     */
//    @SuppressWarnings("unchecked")
//    public List<Map<String, Object>> listAbClaimConfirmedInfoAcc(DataTablesInput dataTablesInput) {
//        Map<String, Object> params = new HashMap<>(4);
//        StringBuilder sql = new StringBuilder();
//        sql.append(" select m.id as claimId,m.claim_sequence AS claimSequence,m.rmk AS claimRmk,m.orig_no as origNo from AB_CLAIM_MIDDLE m where 1=1 ");
//        sql.append(" and m.claim_status = :claimStatus ");
//        params.put("claimStatus", dataTablesInput.getFormdata().get("claimStatus"));
//        return this.listBySQL(sql.toString(), params);
//    }
    /**
     * 根据票据认领表id获取对应票据集合
     *
     * @param claimId 票据认领id
     * @return 票据认领集合
     */
    public List<AbClaimVo> getBillListByClaimId(String claimId) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT s.id AS aid, vv.id AS vvid FROM ab_voucherinfo_vice vv LEFT JOIN ab_claim_middle m ON vv.claim_id = m.id ");
        sql.append(" LEFT JOIN ab_accrecords s ON s.vvid = vv.id WHERE vv.claim_id =:claimId ");
        sql.append(" and vv.VALID_SIGN=:VALID_SIGN_1");
        sql.append(" and s.create_date = (select max(ss.create_date) from ab_accrecords ss where s.vvid = ss.vvid) ");
        params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("claimId", claimId);
        return this.listBySQLAliasToBean(sql.toString(), params, AbClaimVo.class);
    }
    /**
     * 票据认领待确认列表
     *
     * @param input       查询参数
     * @param corpAuthSql 单位权限
     * @return 前端列表数据
     **/
    public DataTablesOutput listWaitConfirmConfirmedInfo(DataTablesInput input, String corpAuthSql) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder();
        billCommonSql(params, sql);
        //从表validSign=1
        sql.append(" WHERE P.VALID_SIGN=:VALID_SIGN_1");
        params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (input.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
            sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(")");
        }
        sql.append(" and p.claim_status = :claimStatus94 ");
        sql.append(" and s.create_date = (select max(ss.create_date) from ab_accrecords ss where s.vvid = ss.vvid) ");
        sql.append(" ) bill where 1=1 ");
       // sql.append(" left join bt_bank_acc acc on bill.bankAcc = acc.BANK_ACC where 1=1");
        params.put("claimStatus94", AccebillConstant.BILL_CLAIM_STATUS94);
        return this.getDynamicBySQL(input, sql.toString(), AbClaimVo.class, "bill", false, params);
    }

    /**
    * @Description 获取已认领的业务类型是兑付的票据
    * @author zhous
    * @date 2025/2/6 13:53
    */
    public List<AbEmitBillPayVo> getClaimedBillPayInfo(boolean isEmitMake,Date claimStartDate) {
        Map<String, Object> params = new HashMap<>(16);
        StringBuilder sql = new StringBuilder();
        //从表validSign=1
        sql.append(" select bill.origNo as origNo,bill.vid AS vid,bill.vvid AS vvid,bill.id AS id,bill.claimStatus,bill.briefId,bill.billSort,bill.date from ");
        sql.append(" ( select distinct v.orig_no as origNo,V.ID AS vid,P.ID AS vvid,s.ID AS id,S.BRIEF_ID AS briefId,BT_NVL_CHAR(p.claim_status,'11') as claimStatus,P.type as billSort,bad.transTime as `date` ");
        sql.append(" from AB_VOUCHERINFO_VICE P");
        sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID");
        sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
        sql.append(" LEFT JOIN AB_DISCOUNT T ON T.RECORD_ID=S.ID");
//        sql.append(" LEFT JOIN sys_corp sc ON sc.id=p.corp_id");
        sql.append(" left join ab_brief_status bs on bs.status=s.brief_id ");
        sql.append(" LEFT JOIN AB_CLAIM_MIDDLE m on m.id = p.CLAIM_ID ");
//        sql.append(" LEFT JOIN bis_acc_dtl bad on bad.claim_id = p.CLAIM_ID ");
        //取取交易时间最大的作为业务时间
        sql.append("  LEFT JOIN (select claim_id as claimId,max(trans_time) as transTime from  bis_acc_dtl  group by claim_id) bad on bad.claimId = p.CLAIM_ID ");
        sql.append(" WHERE P.VALID_SIGN=:VALID_SIGN_1");
        params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("claimStartDate", claimStartDate);
        //贴现数据
        sql.append(" and ( ");
        addDiscountSql(params, sql, true);
        addCollectionSql(params, sql);
        addPaySql(params, sql, isEmitMake);
//        sql.append(" ) ");
//        sql.append(" left join bt_bank_acc acc on bill.bankAcc = acc.BANK_ACC where 1=1");
        sql.append(" and S.BRIEF_ID in (:claimType) ");
        params.put("claimType", new String[]{AccebillConstant.BRIEF_STATUS_001, AccebillConstant.BRIEF_STATUS_003,
                AccebillConstant.BRIEF_STATUS_6101, AccebillConstant.BRIEF_STATUS_6120});
        sql.append(") ) bill WHERE 1=1 ");
        sql.append(" and bill.briefId  in (:claimType)");
        sql.append(" and bill.claimStatus =:claimStatus95 and bill.billSort =:billType0 ");
        sql.append(" and not exists ( select 1 from ab_accrecords ba where  bill.vvid = ba.vvid and ba.brief_id =:briefId6120 and ba.valid_sign =:validSign1 and ba.status>=0  ) ");
        params.put("claimStatus95",AccebillConstant.BILL_CLAIM_STATUS95);
        params.put("billType0",AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
        return this.listBySQLAliasToBean(sql.toString(), params, AbEmitBillPayVo.class);
    }
    /**
    * @Description 取银行交易明细查询配置信息
    * @author zhengg
    * @date 2025/2/24 13:53
    */
    public List<AbClaimVo> getDtlQueryConfig(AbClaimVo vo) {
        Map<String, Object> params = new HashMap<>(16);
        StringBuilder sql = new StringBuilder();
        //从表validSign=1
        sql.append(" select d.MATCH_ELEMENTS as matchElements,d.MATCH_CONTENT as matchContent from AB_BILL_RULEBILLDTLRMK r inner join AB_BILL_RULEBILLDTLRMK_DTL d on r.id=d.BUS_ID where r.STATUS=1 and d.MATCH_ELEMENTS in ('remark','uses','postscript') ");
        if(StrUtil.isNotEmpty(vo.getBankTypeId())){
            sql.append("  and r.BANK_TYPE_ID=:bankTypeId ");
            params.put("bankTypeId",vo.getBankTypeId());
        }
        if(StrUtil.isNotEmpty(vo.getBusType())){
            sql.append(" and r.BUSTYPE=:busType ");
            params.put("busType",vo.getBusType());
        }
        sql.append(" order by d.SORT asc ");
        return this.listBySQLAliasToBean(sql.toString(), params, AbClaimVo.class);
    }
}
