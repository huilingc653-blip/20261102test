package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayVo;
import com.bytter.biz.payment.api.constant.CgsBillConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
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
 * @Description: 兑付确认
 * @date 2017-06-29 16:53:43
 */
@Repository
public class AbEmitBillPayDao extends BaseDao<AbVoucherinfo, String> {
    /**
     * 加载兑付申请列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listAbEmitBillPay(DataTablesInput dataTablesInput, List<String> corpAuthList) {
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
        sql.append(" BT_NVL_NUM(l.bailmoney,0) as bailmoney,BT_NVL_NUM(l.bailpercent,0) as bailpercent,z.bill_money-BT_NVL_NUM(c.BAIL_MONEY,0) as endPayMoney,bbt.type_name as bankTypeName,");
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
        sql.append(" left join bt_bank_acc bba on bba.bank_acc = c.bank_acc ");
        sql.append(" left join bt_bank_type bbt on bbt.id = bba.BANK_TYPE_ID ");

        // 应付票据兑付信息表
        sql.append(" left join ab_emitbill_pay e on e.aid = c.id ");
        sql.append(" where c.valid_sign= :validSign1 and ");
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
            sql.append(" and (c.back_brief_id is null or c.back_brief_id = '') ");
        }
        Map<String, Object> params = new HashMap<>();
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
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.apply_corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }

        if (dataTablesInput.getOrder() != null && !dataTablesInput.getOrder().isEmpty() && dataTablesInput.getOrder().get(0) != null) {
            if (dataTablesInput.getOrder().get(0).getColumn() == 15) {
                String orderInit = dataTablesInput.getOrder().get(0).getDir();
                dataTablesInput.setOrderString(new OrderString("(Z.BILL_MONEY-BT_NVL_NUM(C.BAIL_MONEY,0))", orderInit));
            }
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbEmitBillPayVo.class, "", false, params);
    }
    /**
     * 兑付跟踪列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbEmitBillPayVo> listAbEmitBillPayTrack(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
        sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,z.payeracc as payeracc,");
        sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.corp_id as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpIdName,");
        sql.append(" (case when c.back_brief_id = '" + AccebillConstant.BRIEF_STATUS_6120 + "' or e.BILL_STATUS=-2  ");
        sql.append(" then -1 when l.status = 95 then 96 else l.status end) as status,l.brief_id as briefId, ");
        sql.append(" (case l.status when " + AccebillConstant.AB_BILL_BUS_STATUS11 + " then 1 else 0 end) as checkboxHide,");
        // --付款单号 n.BILL_NO、付款状态n.STATUS、交易状态 b.VOUCHER_STAT
        //关联不到付款单信息且线上兑付信息表中存有付款单号则判定为对该单号的付款单撤回状态，撤回时付款单状态为已删除
        sql.append(" (case when (e.bill_no is not null and e.BILL_ID is null) then -2 else e.BILL_STATUS end) as payStatus, ");
        sql.append(" (case when e.bill_no is not null then e.bill_no else e.bill_no end) as billNo, ");//撤回状态时，bill_No从线上兑付信息表中获取
		/*交易状态为空时有两种状态：一、线上兑付申请提交，付款单为暂存状态；
		                            二、付款单撤回,此时付款单关联丢失,线上兑付信息中保留有被撤回的付款单号*//*
		sql.append(" (case when BT_NVL_CHAR(b.VOUCHER_STAT,' ')=' ' and n.STATUS = '"+xx+"' then '暂存' else '撤回' end) as voucherStat ");*/
//		sql.append(" b.VOUCHER_STAT as voucherStat ");//暂不做空值处理IS_PAYED
        sql.append(" (case when (e.is_payed='1' and e.BILL_STATUS >=95) then '0' else e.VOUCHER_STAT end) as voucherStat,");
        sql.append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ");
        //新一代票据新增新票标志、是否允许分包、子票区间开始序号、子票区间结束序号、标准金额、出票人统一信用代码、业务批次号
        sql.append(", z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,z.SBBLL_RNG_STRT_SN as sbBllRngStrtSN");
        sql.append(", z.SBBLL_RNG_END_SN as sbBllRngEndSN,z.BL_STD_AMT as blStdAmt,z.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo,c.BATCH_NO as batchNo ");
        sql.append(" from ab_voucherinfo_vice c ");
        sql.append(" left join ab_voucherinfo z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ");
        sql.append(" left join ab_accrecords l on c.id=l.vvid ");
//        sql.append(" left join sys_corp s on c.corp_id = s.id ");
//        sql.append(" left join sys_corp s2 on c.apply_corp_id = s2.id ");
        sql.append(" left join ab_emitbill_pay e on e.aid = c.id ");
        sql.append(" left join nis_billhead n on l.id = n.erp_id ");
//        sql.append(" left join bis_exc b on n.id = b.cbs_bill_id ");
        sql.append(" left join ab_accrecords l2 on l2.lastid = l.id ");
        sql.append(" where c.valid_sign= :validSign1 and ");
        sql.append(" l.brief_id = :briefId6120 ");
        //statu=95且last关联到的流水为空的数据(处理撤回后重新编辑提交兑付申请的情况) 或者 status=11 或者 status=-3撤回
        sql.append(" and ( (l.status = :status95 and l2.id is null and l.valid_sign = :validSign1 ) ");
        sql.append("     or (l.status = :status11 and l.valid_sign = :validSign0 ) ");
        sql.append("     or (l.status = :status3 and l.valid_sign = :validSign1 ) )");
        sql.append(" and l.emit_sttlmmk = :emitSttlmmk1 ");
        //sql.append(" and ").append(corpAuthSql).append(" ");
        //条件查询删除(撤回)数据,撤回时由于付款单关联不到故付款状态是空
        if (String.valueOf(CgsBillConstant.STATUE_DEL).equals(dataTablesInput.getFormdata().get("eqi_payStatus"))) {
            sql.append(" and e.BILL_STATUS is null ");
            dataTablesInput.getFormdata().remove("eqi_payStatus");
        }
        if ("96".equals(dataTablesInput.getFormdata().get("eqi_status"))) {
            //条件查询审批通过的数据(96改为95,页面上95为可兑付状态，96为审批通过，数据库里面审批通过为95故此处转换)
            sql.append(" and l.status = :status95 ");
            dataTablesInput.getFormdata().remove("eqi_status");
        }
        if (EmptyUtils.isEmptyList(dataTablesInput.getOrder())) {
            //票据信息以付款单生成时间顺序显示付款单记录
            dataTablesInput.setOrderString(new OrderString("e.BILL_DATE", "desc"));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("briefId6120", AccebillConstant.BRIEF_STATUS_6120);
        params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("status11", AccebillConstant.AB_BILL_BUS_STATUS11);
        params.put("validSign0", AccebillConstant.AB_BILL_VALID_SIGN_0);
        params.put("status3", AccebillConstant.AB_BILL_BUS_STATUS_3);
        params.put("emitSttlmmk1", AccebillConstant.AB_BILL_PAYEMITSTTLMMK1);
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.apply_corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbEmitBillPayVo.class, "", false, params);
    }
    /**
     * 加载兑付确认审批中数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<AbEmitBillPayVo> dataTableApprove(DataTablesInput dataTablesInput, List<String> businessList) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(
                " z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,z.payeracc as payeracc,z.acceptor as acceptor,z.orig_no as origNo,");
        sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,BT_NVL_CHAR(l.emit_sttlmmk,'0') as emitSttlmmk, ");
        sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.corp_id as corpId,c.corp_code as corpCode,c.corp_name as corpIdName,l.brief_id as briefId, ");
        sql.append(" c.APPLY_CORP_ID as applyCorpId,c.apply_corp_name as applyCorpName,c.apply_corp_code as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel ");
        sql.append(" from ab_voucherinfo_vice c ");
        sql.append(" left join ab_voucherinfo z on z.id=c.vid ");
        sql.append(" left join ab_accrecords l on c.id=l.vvid ");
        sql.append(" where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "l.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), AbEmitBillPayVo.class, "", false, params);
    }
    /**
     * 根据主表id 从表id查询兑付申请数据
     *
     * @param vid  主表id
     * @param vvid 从表id
     * @return 返回列表数据
     */
    @SuppressWarnings("rawtypes")
    public List<AbEmitBillPayVo> listAbEmitBillbyVidAndVvid(String vid, String vvid) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(
                " z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
        sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,");
        sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.corp_id as corpId,c.corp_code as corpCode,c.corp_name as corpIdName,"
                + "(case c.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_6120
                + "' then -1 else l.status end) as status," + "l.brief_id as briefId, ");
        sql.append(" (case l.status when " + AccebillConstant.AB_BILL_BUS_STATUS11
                + " then 1 else 0 end) as checkboxHide,");
        sql.append(" c.APPLY_CORP_ID as applyCorpId,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel ");
        sql.append(" from ab_voucherinfo_vice c ");
        sql.append(" left join ab_voucherinfo z on z.id=c.vid ");
        sql.append(" left join ab_accrecords l on c.id=l.vvid ");
        sql.append(" where c.valid_sign= :validSign1 and ");
        // 主表 :处理状态 box_status -- 处理完成 11
        // 流水表 :处理标记 valid_sign -- 可用 1
        // 操作状态 opt_status -- 已签收19
        sql.append(" ( ( ");
        //系统不需要开票补录功能时
        // 指令发送提示收票完成(线上)和开票登记完成(线下)就可以做兑付确认 不需要补录
        sql.append(" ( (l.brief_id in (:briefId003) and l.send_sign=:sendSign1 ) or (l.brief_id in (:briefId001) and l.send_sign=:sendSign0 ) or l.brief_id in (:briefId6101)) ");
        sql.append(" and c.box_status=:boxStatusY and l.valid_sign= :validSign1 and l.status=:status95 and l.opt_status=:optStatus19 )");
        // 业务类型 :brief_id -- 应付兑付确认 6120
        // 审批处理状态 :status -- 审批中 11
        sql.append(" or (l.brief_id =:briefId6120 and l.status=:status11 ) ) ");
        sql.append(" and c.id = :vvid  ");
        sql.append(" and z.id= :vid  ");
        Map<String, Object> params = new HashMap<>();
        params.put("vid", vid);
        params.put("vvid", vvid);
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("boxStatusY", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("sendSign0", new String[]{AccebillConstant.AB_BILL_SEND_SIGN_N});
        params.put("sendSign1", new String[]{AccebillConstant.AB_BILL_SEND_SIGN_Y});
        params.put("briefId001", new String[]{AccebillConstant.BRIEF_STATUS_001});
        params.put("briefId003", new String[]{AccebillConstant.BRIEF_STATUS_003});
        params.put("briefId6101", new String[]{AccebillConstant.BRIEF_STATUS_6101});
        params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("optStatus19", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("briefId6120", AccebillConstant.BRIEF_STATUS_6120);
        params.put("status11", AccebillConstant.AB_BILL_BUS_STATUS11);
        return this.listBySQLAliasToBean(sql.toString(), params, AbEmitBillPayVo.class);
    }
    /**
     * 加载电票银行承兑票据到期系统自动线下兑付确认数据
     *
     * @param isEmitMake 是否补录
     * @return
     */
    public List<AbEmitBillPayVo> listAutoAbEmitBillPayInfo(boolean isEmitMake) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.end_date as  \"date\"");
        sql.append(" from ab_voucherinfo_vice c ");
        sql.append(" left join ab_voucherinfo z on z.id=c.vid ");
        sql.append(" left join ab_accrecords l on c.id=l.vvid ");
        sql.append(" where c.valid_sign= :validSign1 and ");
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
        sql.append(" ) ");
        //电票银行承兑到期的票
        sql.append(" and z.bill_flag=:billFlag and z.bill_type=:billType and z.end_date<=:sysdate ");
        Map<String, Object> params = new HashMap<>();
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
        params.put("billFlag", AccebillConstant.AB_BILL_FLAG_ELECT);
        params.put("billType", AccebillConstant.AB_BILL_TYPE_BANK);
        params.put("sysdate", DateUtils.getDateYMD());
        return this.listBySQLAliasToBean(sql.toString(), params, AbEmitBillPayVo.class);
    }
    /**
     * 加载商票承兑票据到期系统自动线下兑付确认数据
     *
     * @param arguments 是否补录
     * @return
     */
    public List<AbEmitBillPayVo> listAutoTradeBillAbEmitBillPayInfo(boolean isEmitMake, String arguments) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" z.id as vid,c.id as vvid,l.ID as id,spay.BUSINESS_DATE as \"date\" ");
        sql.append(" from ab_voucherinfo_vice c ");
        sql.append(" left join ab_voucherinfo z on z.id=c.vid ");
        sql.append(" left join ab_accrecords l on c.id=l.vvid ");
        //商票如果做了提示付款签收且指令发送完成自动兑付完成
        sql.append(" left join ab_accrecords spay on ");
        sql.append(" z.id=spay.vid and spay.brief_id in (:briefId9020) and spay.status=:status95 and spay.opt_status=:optStatus19 and spay.SIGN_UP_FLAG='SU00' ");
        sql.append(" where c.valid_sign= :validSign1 and ");
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
        sql.append(" ) ");
        //电票银行承兑到期的票
        sql.append(" and z.bill_flag=:billFlag and z.bill_type=:billType and spay.id is not null ");
        Map<String, Object> params = new HashMap<>();
        if (!"all".equals(arguments) && StringUtils.isNotEmpty(arguments)) {
            String[] bifCodes = arguments.split(",");
            sql.append(" and ").append(SqlUtil.getInSqlByParams(bifCodes, "spay.bif_code", "bifCodes", params));
        }
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
        params.put("billFlag", AccebillConstant.AB_BILL_FLAG_ELECT);
        params.put("billType", AccebillConstant.AB_BILL_TYPE_MERCH);
        params.put("briefId9020", new String[]{AccebillConstant.BRIEF_STATUS_9020, AccebillConstant.BRIEF_STATUS_9021});
        return this.listBySQLAliasToBean(sql.toString(), params, AbEmitBillPayVo.class);
    }
    /**
     * 查询电票到期线上兑付指令交易成功数据
     *
     * @return
     */
    public List<AbEmitBillPayVo> listAutoConfrimePay() {
        StringBuilder sql = new StringBuilder("select l.id as id");
        sql.append(" from ab_voucherinfo_vice c ");
        sql.append(" left join ab_accrecords l on c.id = l.vvid ");
        sql.append(" where c.valid_sign= :validSign1  ");
        sql.append(" and l.valid_sign= :validSign1 and l.status=:status95 and l.opt_status=:optStatus19 ");
        sql.append("  and l.brief_id =:briefId6120  and l.emit_sttlmmk =:emitSttlmmk1 ");
        Map<String, Object> params = new HashMap<>();
        params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("boxStatusY", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("briefId6120", new String[]{AccebillConstant.BRIEF_STATUS_6120});
        params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("optStatus19", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("emitSttlmmk1", AccebillConstant.AB_BILL_PAYEMITSTTLMMK1);
        params.put("voucherStat", BdlinkConstant.SUCCESS);
        return this.listBySQLAliasToBean(sql.toString(), params, AbEmitBillPayVo.class);
    }
}
