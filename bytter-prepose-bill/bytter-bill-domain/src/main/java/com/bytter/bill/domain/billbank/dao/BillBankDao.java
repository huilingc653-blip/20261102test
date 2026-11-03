package com.bytter.bill.domain.billbank.dao;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.service.impl.BillQueryService;
import com.bytter.bill.domain.billbank.service.impl.EDraftExcBillService;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.bill.domain.billbank.util.StringUtil;
import com.bytter.bill.domain.billbank.vo.BankAcc;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.core.utils.BeanUtils;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfoVice;
import com.bytter.core.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 * <br>
 * <b>功能：</b>开票DAO<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-05-31 <br>
 *
 * todo 迁移需要改造 dao层反向依赖service层 不合理
 */
@Repository("billBankDao")
@Slf4j
public class BillBankDao extends BaseDao {
	private static final Logger logger = LoggerFactory.getLogger(BillBankDao.class);
	private static final long serialVersionUID = 1L;
	@Autowired
	private BillVoucherInfoDao billVoucherInfoDao;
	@Autowired
	private BillAbVoucherinfoViceDao billAbVoucherinfoViceDao;
	@Autowired
	private BillAccrecordsDao billAccrecordsDao;
	@Autowired
	@Lazy
	private EDraftExcBillService edraftExcBillService;
//	@Autowired
//	private BillSignDao billSignDao;

    /**  *
     * <b>功能：</b>查询开票待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param briefId 业务类型
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return 票据信息
     */
    public List<VoucherInfoVo> queryBill(String briefId, String bifCode,
                                         String reCode, String status) {
        return queryBill(briefId, bifCode, reCode, status,
                AccebillConstant.poolSign_2);
    }

    /**  *
     * <b>功能：</b>查询开票待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param briefId 业务类型
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return 票据信息
     */
    public List<VoucherInfoVo> queryBill(String briefId, String bifCode,
                                         String reCode, String status, String poolSign) {
        String[] statusArr = new String[]{status};
        if(AccebillConstant.AB_BILL_OPT_STATUS18.equals(status)){
            statusArr = new String[]{status,AccebillConstant.AB_BILL_OPT_STATUS17};
        }
        return queryBill(briefId, bifCode, reCode, statusArr, null, poolSign, null);
    }

    /**  *
     * <b>功能：</b>查询开票待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param briefId 业务类型
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return 票据信息
     */
    public List<VoucherInfoVo> queryBill(String briefId, String bifCode,
                                         String reCode, String status, String poolSign, Boolean isBatchOpenBill) {
        String[] statusArr = new String[]{status};
        if(AccebillConstant.AB_BILL_OPT_STATUS18.equals(status)){
            statusArr = new String[]{status,AccebillConstant.AB_BILL_OPT_STATUS17};
        }
        return queryBill(briefId, bifCode, reCode, statusArr, null, poolSign, isBatchOpenBill);
    }

    /**  *
     * <b>功能：</b>查询开票待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param briefId 业务类型
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return 票据信息
     */
    @SuppressWarnings("unchecked")
    public List<VoucherInfoVo> queryBill(String briefId, String bifCode,
                                         String reCode, String[] status, String[] billStatus) {
        return queryBill(briefId, bifCode, reCode, status, billStatus,
                AccebillConstant.poolSign_0);
    }


    /**  *
     * <b>功能：</b>查询开票待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param briefId  业务类型
     * @param bifCode  银行接口代码
     * @param reCode   银行返回代码
     * @param poolSign 包含票据池流水标识0或空非票据池流水1票据池流水2不控制
     * @return 票据信息
     */
    public List<VoucherInfoVo> queryBill(String briefId, String bifCode,
                                         String reCode, String[] status, String[] billStatus, String poolSign) {
        return queryBill(briefId, bifCode, reCode, status, billStatus, poolSign, null);
    }

    /**  *
     * <b>功能：</b>查询开票待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param briefId  业务类型
     * @param bifCode  银行接口代码
     * @param reCode   银行返回代码
     * @param poolSign 包含票据池流水标识0或空非票据池流水1票据池流水2不控制
     * @return 票据信息
     */
    public List<VoucherInfoVo> queryBill(String briefId, String bifCode,
                                         String reCode, String[] status, String[] billStatus, String poolSign, Boolean isBatchOpenBill) {

        List<VoucherInfoVo> vcInfoList = new ArrayList<VoucherInfoVo>();
        try {
            StringBuffer sqlselect = new StringBuffer();
            sqlselect.append("select z.id       as voucherId,"
                    + "z.orig_no      as origNo,"
                    + "z.bill_type    as billType,"
                    + "z.Start_Date   as startDate,"
                    + "z.end_date     as endDate,"
                    + "z.bill_money   as billMoney,"
                    + "z.banendrsmtmk as banendrsmtmk,"
                    + "z.payee        as payee,"
                    + "z.payeeacc     as payeeAcc,"
                    + "z.payee_bank_name as payeeBankName,"
                    + "z.payeebankno  as payeeBankNo,"
                    + "z.payer        as payer,"
                    + "z.payeracc     as payerAcc,"
                    + "z.payerbankno  as payerBankNo,"
                    + "z.payer_bank_name as payerBankName,"
                    + "z.acceptor     as acceptor,"
                    + "z.acceptoracc  as acceptorAcc,"
                    + "z.acceptorbankno as acceptorBankNo,"
                    + "z.acceptor_bank_name as acceptorBankName,"
                    + "z.COOPERATIVE_BANK_NO as cooperativeBankNo,"
                    + "c.IS_CREDIT_DRAW as isCreditDraw,"

                    + "z.OLD_DRAFT_ID as oldDraftId,"
                    + "z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,"
                    + "z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,"
                    + "z.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,"
                    + "z.SBBLL_RNG_END_SN as sbbllRngEndSn,"
                    + "z.BL_STD_AMT as blStdAmt,"
                    + "z.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo,"
                    + "z.BL_ACPTR_ID_TPCD as blAcptrIdTpcd,"
                    + "z.BL_RCVPYMTPS_ID_TPCD as blRcvpymtpsIdTpcd,"
                    + "z.EBILL_BL_CRCL_INDC as ebillBlCrclIndc,"
                    + "z.BL_BSN_TPCD as blBsnTpcd,"
                    + "z.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo,"
                    + "z.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo,"
                    + "c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo,"

                    + "c.box_status   as boxStatus,"
                    + "z.bill_status  as billStatus,"
                    + "null           as sysSubject,"
                    + "z.bill_flag    as billFlag,"
                    + "z.payer        as openbill,"
                    + "null           as Acceptordate,"
                    + "null           as flwNo,"
                    + "z.payer        as payerName,"
                    + "l.rec_nm       as guarantor,"
                    + "l.rec_acct_id  as Guarantoracc,"
                    + "l.rec_acctsvcr as guarantorBankNo,"
                    + "b.bank_name    as guarantorBank,"
                    + "b.BANK_TYPE_ID as guaranttorBankType,"
                    + "null           as guarantorAddr,"
                    + "null           as guarantorDate,"
                    + "l.rec_nm       as aGuarantor,"
                    + "l.rec_acct_id  as aGuarantorAcc,"
                    + "l.rec_acctsvcr as aGuarantorNo,"
                    + "null           as aGuarantorAddr,"
                    + "null           as aGuarantorDate,"
                    + "l.rec_nm       as eGuarantor,"
                    + "l.rec_acct_id  as eGuarantorAcc,"
                    + "l.rec_acctsvcr as eGuarantorNo,"

                    + "l.req_Soc_No as reqSocNo,"
                    + "l.rec_Soc_No as recSocNo,"
                    + "l.rec_Dgh_Type as recDghType,"
                    + "l.settledate as settledate,"
                    + "l.settletype as settletype,"

                    + "null           as eGuarantorDate,"
                    + "null           as payId,"
                    + "c.Cn_Accrecords as preHand,"
                    + "null            as payStatus,"
                    + "null            as guaranteeMode,"
                    + "null            as trueFlag,"
                    + "null            as drwgbckdt,"
                    + "l.id            as recordId,"
                    + "bt_concat2('1008',l.brief_id) as briefId,"
                    + "l.bif_code      as bifCode,"
                    + "l.return_code   as returnCode,"
                    + "l.return_msg    as returnMsg,"
                    + "l.apply_date    as tallyDate,"
                    + "null            as coptorId,"
                    + "z.a_contract_no as contractNo,"
                    + "null            as signBank,"
                    + "null            as signMan,"
                    + "l.apply_date    as applyDate,"
                    + "l.abs           as summary,"
                    + "null            as budgetSign,"
                    + "d.discount_date as discountDate,"
                    + "d.daynum        as daynum,"
                    + "d.interest_rates as interestRates,"
                    + "d.interest      as interest,"
                    + "d.rpdintrstrate as rpdintrstrate,"
                    + "null            as lsno,"
                    + "null            as userno,"
                    + "null            as discountType,"
                    + "null            as uuid,"
                    + "l.req_acct_id   as acctId,"
                    + "l.req_acctsvcr  as acctAcctsvcr,"
                    + "l.req_acctsvcr_ag as agcyAcctsvcr,"
                    + "d.rpdmk         as rpdmk,"
                    + "null            as bussdt,"
                    + "z.banendrsmtmk  as bankendrsmtmk,"
                    + "d.sttlmmk       as sttlmmk,"
                    + "d.rpdopendt     as rpdopendt,"
                    + "d.rpdduedt      as rpdduedt,"
                    + "d.Interest_Rates as INTEREST_RATES,"
                    + "BT_NVL_NUM(d.real_money,z.bill_money)    as rpdamt,"
                    + "d.invc_nb       as invcnb,"
                    + "null            as rate,"
                    + "null            as appldt,"
                    + "BT_NVL_CHAR(l.rsn,l.rmk)           as rsn,"
                    + "l.rec_acct_id   as backacctId,"
                    + "l.rec_acctsvcr  as backacctAcctsvcr,"
                    + "l.rcrs_tp       as rcrsTp,"
                    // + "r.msgid         as msgid,"
                    + "r.Msdt          as msgcredttm,"
                    + "null            as nexttime,"
                    + "l.opt_status    as status,"
                    + "l.brief_id      as msgtype,"
                    + "l.lastid        as lastid,"
                    + "l.bailmoney     as bailmoney,"
                    + "l.bailpercent   as bailpercent,"
                    + "l.feemoney      as feemoney,"
                    + "null            as bail,"
                    + "null            as lastsysSubject,"
                    + "l.bankendrsmtmk as transfer,"
                    + "d.paydate       as paydate,"
                    + "d.deferdate     as deferdate,"
                    + "null            as contractFlag,"
                    + "l.sign_up_flag  as signUpFlag,"
                    + "null            as checkName,"
                    + "null            as checkBankacc,"
                    + "null            as checkBank,"
                    + "null            as endorsementCount,"
                    + "null            as outgolosttype,"
                    + "l.recid         as receivebizId,"
                    + "d.Buyerinterest_Money as buyerInterest,"
                    + "d.BUYERINTEREST_RATIO as buyerRatio,"
                    + "null            as isOnestep,"
                    + "l.lastbillcode  as lastbillstatus,"
                    + "l.LAST_CIRCULATE_CODE  as lastCirculateCode,"
                    + "l.LAST_BL_BSN_TPCD  as lastBlBsnTpcd,"
                    + "null            as crmrkbypropsr,"
                    + "c.bail_type     as bailMoneyType,"
                    + "null            as guarantorBanktype,");
            if (AccebillConstant.BRIEF_STATUS_001.equals(EDraftConstant
                    .getT2BriefId(briefId))) {
                sqlselect.append("c.rmk            as remark,");
                sqlselect.append("c.batch_no       as batchNo,");
                sqlselect.append("c.emit_Type      as emitType,");
                sqlselect.append("aba.attachment_id as attachmentId,");
            } else if(AccebillConstant.BRIEF_STATUS_011.equals(briefId)
                    ||AccebillConstant.BRIEF_STATUS_018.equals(briefId)){
                sqlselect.append("l.rmk            as remark,");
                sqlselect.append("aba.attachment_id as attachmentId,");
            }else {
                sqlselect.append("l.rmk            as remark,");
                sqlselect.append("null as attachmentId,");
            }
            sqlselect
                    .append("null            as payeeBanktype,"
                            + "null            as guarantorBankname,"
                            + "null            as ucondlprmsmrk,"
                            + "BT_NVL_CHAR(d.sttlmmk,l.sttlmmk)       as paytype,"
                            + "(case when d.buyerinterest_ratio =0 then '1' when d.buyerinterest_ratio =1 then '2' else '3' end )  as payratetype,"
                            + "null            as busaccid,"
                            + "null            as iscib,"
                            + "null            as redmtype,"
                            + "null            as guarnttype,"
                            + "d.aoaccninf_id  as payinacc,"
                            + "d.aoaccninf_ac  as payinbankno,"
                            + "BT_NVL_CHAR(d.aoaccninf_name,l.req_nm)  as payinaccname,"
                            + "null            as paymenttype,"
                            + "null            as businessSign,"
                            + "null            as tradeType,"
                            + "null            as applysign,"
                            + "null            as biztyp,"
                            + "null            as collztnacctid,"
                            + "null            as ccy,"
                            + "z.bill_money    as rechMoney,"
                            + "z.bill_money    as money,"
                            + "null            as querySource,"
                            + "c.corp_id       as corpId,"
                            + "null            as mobile,"
                            + "l.req_nm        as acctName,"
                            + "l.rec_nm        as backAcctName,"
                            + "l.rec_nm     as backAcctBankName,"
                            + "null            as autoAcceptance,"
                            + "null            as autoIssuse,"
                            + "null            as bankserialno,"
                            + "c.bank_name     as acctBankName,"
                            + "c.BAIL_ACC     as bailAcc,"

                            + "c.use_type as useType,"
                            + "r.ebill_Bl_Crcl_Indcd as ebillBlCrclIndcd,"
                            + "r.intpymtprt_Way as intpymtprtWay,"
                            + "r.buyerpayintrcntno as buyerpayintrcntno,"
                            + "r.intpymt_Psn_Nm as intpymtPsnNm,"
                            + "r.intpymt_Psn_Accno as intpymtPsnAccno,"
                            + "r.bl_Rcv_Unn_Id_No as blRcvUnnIdNo,"
                            + "r.bl_Req_Unn_Id_No as blReqUnnIdNo,"
                            + "r.bl_Rcv_Id_Tpcd as blRcvIdTpcd,"
                            + "r.bl_Req_Id_Tpcd as blReqIdTpcd,"

                            + "BT_NVL_CHAR(l.Rcrsrsncd,r.dshnrcd)     as rcrsrsncd,"
                            + "bt_concat2('1008',l2.brief_id)     as lastBriefId,"
                            + "l.IS_HANDLING     as isHandling,"
                            + "l.msg_id     as msgId,");
            if (AccebillConstant.BRIEF_STATUS_011.equals(briefId)) {
                sqlselect.append("BT_NVL_CHAR(BT_NVL_CHAR(l.bill_id,l2.bill_id),l3.bill_id)     as billId,");
            } else {
                sqlselect.append("BT_NVL_CHAR(l.bill_id,l2.bill_id)     as billId,");
            }
            sqlselect.append("l.rec_cmonid     as cmonid,"
                    + "l.req_cmonid     as accCmonid,"
                    + "z.accptncagrmtnb,");
            if (BillPoolConstant.IS_RUN_POOL) {
                sqlselect.append("l.pd_id     as pdid,");
            }
            // 针对出票申请可以关联银行授信的授信编号、关联保证金查询保证账号存出账号和户名
            if (AccebillConstant.BRIEF_STATUS_001.equals(briefId)) {
                sqlselect.append("rb.contract_no     as ratContractNo,");
                sqlselect.append("fb.out_bank_acc  as outBankAcc,");
                sqlselect.append("bb.acc_name      as outBankAccName,");
            }

            sqlselect.append("z.org_sbbll_rng_strt_sn      as orgSbbllRngStrtSn,");
            sqlselect.append("z.org_sbbll_rng_end_sn      as orgSbbllRngEndSn,");
            sqlselect.append("l.copy_flag      as copyFlag,");
            sqlselect.append("l.old_acc_id      as oldAccId,");

            sqlselect.append("BT_NVL_CHAR(l.BAIL_TYPE,l2.BAIL_TYPE) as bailType ");
            sqlselect.append(" from AB_VOUCHERINFO_VICE c "
                    + "left join AB_VOUCHERINFO z  on z.id = c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del') "
                    + " left join AB_ACCRECORDS l on c.id = l.vvid"
                    + " left join AB_ACCRECORDS l2 on l.lastid=l2.id");
            if (AccebillConstant.BRIEF_STATUS_011.equals(briefId)) {
                sqlselect.append(" left join AB_ACCRECORDS l3 on l2.lastid=l3.id ");
            }
            //提示收票正面结果查询 必须没有完成业务的提示收票业务流水
            if (AccebillConstant.BRIEF_STATUS_002.equals(briefId) && reCode == null) {
                sqlselect.append(" left join AB_ACCRECORDS l3 on l3.lastid=l.id and l3.opt_status='19' and l3.brief_id='003' ");
            }
            //提示承兑正面结果查询 必须没有完成业务的提示承兑业务流水
            else if (AccebillConstant.BRIEF_STATUS_001.equals(briefId) && reCode == null) {
                sqlselect.append(" left join AB_ACCRECORDS l3 on l3.lastid=l.id and l3.opt_status='19' and l3.brief_id='002' ");

            }
            sqlselect.append(" left join bt_bank_acc b  on l.req_acct_id=b.bank_acc"
                    + " left join AB_DISCOUNT d  on l.id=d.record_id"
                    + " left join AB_BILL_RECEIVEBIZ r on l.recid=r.id"
                    + " left join sys_corp s on c.corp_id = s.id ");
            // 针对出票申请可以关联银行授信的授信编号、关联保证金查询保证账号存出账号和户名
            if (AccebillConstant.BRIEF_STATUS_001.equals(briefId)) {
                sqlselect
                        .append(" left join rat_uration ru on ru.bill_id= c.id and ru.business_name='04' ");
                sqlselect
                        .append(" left join rat_bkration rb on rb.id= ru.rat_code ");

                sqlselect.append(" left join FC_BOND_APPLY FBL on FBL.item_no=c.app_no and FBL.item_id=c.id ");
                sqlselect.append(" left join FC_BOND FB on FB.id=FBL.bond_id ");
                sqlselect.append(" LEFT JOIN BT_BANK_ACC BB on FB.Out_Bank_Acc = BB.bank_acc ");
                sqlselect.append(" LEFT JOIN AB_BILL_ATTACHMENT aba on aba.vvid=c.id  ");
            }else if(AccebillConstant.BRIEF_STATUS_011.equals(briefId)
                    ||AccebillConstant.BRIEF_STATUS_018.equals(briefId)){
                sqlselect.append(" LEFT JOIN AB_BILL_ATTACHMENT aba on aba.vvid=c.id and aba.brief_id='").append(briefId).append("' ");
            }
            sqlselect
                    .append(" where c.valid_sign='"
                            + AccebillConstant.AB_BILL_VALID_SIGN_1 + "'  ")
                    // .append(" and z.bill_flag ='" +
                    // AccebillConstant.AB_BILL_FLAG_ELECT + "' ")
                    .append(" and l.SEND_SIGN='"
                            + AccebillConstant.AB_BILL_SEND_SIGN_Y + "' ")
                    .append(" and l.STATUS='"
                            + AccebillConstant.AB_BILL_BUS_STATUS95 + "' ");

            Map<String, Object> params = new HashMap<String, Object>();
            StringBuffer sql = new StringBuffer();
            if (BillPoolConstant.IS_RUN_POOL) {
                if (AccebillConstant.poolSign_0.equals(poolSign)) {
                    sql.append(" and l.pd_id is null ");
                } else if (AccebillConstant.poolSign_1.equals(poolSign)) {
                    sql.append(" and l.pd_id is not null ");
                }
            }
            if (isBatchOpenBill != null) {
                if (isBatchOpenBill) {
                    sql.append(" and c.batch_no is not null ");
                    //针对批次开票的必须同一批次审批通过或者没有暂存和审批钟的才可以进行批次开票
                    sql.append(" and not exists (select 1 from ab_voucherinfo_vice vo inner join ab_accrecords ao on vo.id=ao.vvid where vo.batch_no=c.batch_no and ao.status not in (95,-2))  ");
                } else {
                    sql.append(" and c.batch_no is null ");
                }
            } else {
                //针对批次开票的必须同一批次审批通过或者没有暂存和审批钟的才可以进行批次开票
                if (AccebillConstant.BRIEF_STATUS_001.equals(briefId)) {
                    //sql.append(" and c.batch_no is null ");
                    sql.append(" and (c.batch_no is null or ");
                    sql.append(" (c.batch_no is not null  ");
                    sql.append("   and not exists (select 1 from ab_voucherinfo_vice vo inner join ab_accrecords ao on vo.id=ao.vvid where vo.batch_no=c.batch_no and ao.status not in (95,-2)) ) ) ");
                }
            }

            if (status != null) {
                sql.append(" and l.OPT_STATUS in (:optStatus) ");
                params.put("optStatus", status);
            }
            if (briefId != null) {
                sql.append(" and l.brief_id='"
                        + EDraftConstant.getT2BriefId(briefId) + "'");
            }
            if (bifCode != null) {
                sql.append(" and l.bif_code in (:bifCode) ");
                params.put("bifCode", StringUtil.split(bifCode,","));
            }
            if (reCode != null) {
                sql.append(" and (l.return_code='" + reCode
                        + "' or l.return_code is null)")
                        .append(" and l.valid_sign in ('"
                                + AccebillConstant.AB_BILL_VALID_SIGN_1 + "') ");
            } else
                //提示收票正面结果查询 必须没有完成业务的提示收票业务流水
                //提示承兑正面结果查询 必须没有完成业务的提示承兑业务流水
                if (AccebillConstant.BRIEF_STATUS_001.equals(briefId) || AccebillConstant.BRIEF_STATUS_002.equals(briefId)) {
                    sql.append(" and l3.id is null ");
                }
            if (billStatus != null) {
                sql.append(" and z.bill_status in (:billStatus) ");
                params.put("billStatus", billStatus);
            }
//            //查询结果需要比上次查询结果回来差3分钟
//            if(AccebillConstant.AB_BILL_OPT_STATUS18.equals(status)){
//                sql.append(" and l.UPDATE_DATE<:queryResultDate ");
//                params.put("queryResultDate", DateUtils.addMinute(new Date(),-3));
//            }
            sqlselect.append(sql);
            sqlselect.append(" order by l.bif_code,l.REQ_ACCT_ID,c.batch_no,z.A_CONTRACT_NO,l.APPLY_DATE,l.UPDATE_DATE ");
            if (logger.isDebugEnabled()) {
                this.logger.debug("sql=" + sqlselect);
            }
            // this.logger.debug("params=billStatus:"+params.get("billStatus")+",optStatus:"+params.get("optStatus"));
            List<Map<String, Object>> objStr = this.listBySQL(
                    sqlselect.toString(), params);
            // this.logger.debug("SIZE="+objStr.size()+",objStr="+objStr.toString());
            // vcInfoList=this.listBySQLAliasToBean(sqlselect.toString(), null,
            // VoucherInfoVo.class);
            vcInfoList = this.getBeanList(objStr);
            for(VoucherInfoVo vv : vcInfoList){
                if(StringUtil.isNotEmpty(vv.getTransfer())){
                    vv.setBanendrsmtmk(vv.getTransfer());
                    vv.setBankendrsmtmk(vv.getTransfer());
                }
                if(StringUtil.isNotEmpty(vv.getAcctAcctsvcr())){
                    List<Map<String, Object>> reqObjList = queryBankNameByPaysyBbankCode(vv.getAcctAcctsvcr());
                    if (reqObjList != null && !reqObjList.isEmpty()) {
                        for (Map ary : reqObjList) {
                            vv.setAcctBankName(ary.get("BANK_NAME") == null ? ""
                                            : ary.get("BANK_NAME").toString());
                        }
                    }
                }
                if(StringUtil.isNotEmpty(vv.getBackacctAcctsvcr())){
                    this.queryBankNameByPaysyBbankCode(vv.getBackacctAcctsvcr());
                    List<Map<String, Object>> reqObjList = queryBankNameByPaysyBbankCode(vv.getBackacctAcctsvcr());
                    if (reqObjList != null && !reqObjList.isEmpty()) {
                        for (Map ary : reqObjList) {
                            vv.setBackAcctBankName(ary.get("BANK_NAME") == null ? ""
                                    : ary.get("BANK_NAME").toString());
                        }
                    }
                }

            }
            // this.logger.debug("SIZE="+vcInfoList.size()+"vcInfoList="+vcInfoList.toString());
            if (logger.isDebugEnabled()) {
                this.logger.debug("SIZE=" + vcInfoList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return vcInfoList;
    }

    /**  *
     * <b>功能：</b>查询开票待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param briefId 业务类型
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return 票据信息
     */
    @SuppressWarnings("unchecked")
    public List<VoucherInfoVo> queryBillByBailType(String briefId,
                                                   String bifCode, String reCode, String status, String bailType) {
        List<VoucherInfoVo> vcInfoList = new ArrayList<VoucherInfoVo>();
        StringBuffer sqlselect = new StringBuffer();
        sqlselect
                .append("select z.id       as voucherId,"
                        + "z.orig_no      as origNo,"
                        + "z.BL_BSN_TPCD as blBsnTpcd,"
                        + "z.bill_type    as billType,"
                        + "z.Start_Date   as startDate,"
                        + "z.end_date     as endDate,"
                        + "z.bill_money   as billMoney,"
                        + "z.banendrsmtmk as banendrsmtmk,"
                        + "z.payee        as payee,"
                        + "z.payeeacc     as payeeAcc,"
                        + "z.payee_bank_name as payeeBankName,"
                        + "z.payeebankno  as payeeBankNo,"
                        + "z.payer        as payer,"
                        + "z.payeracc     as payerAcc,"
                        + "z.payerbankno  as payerBankNo,"
                        + "z.payer_bank_name as payerBankName,"
                        + "z.acceptor     as acceptor,"
                        + "z.acceptoracc  as acceptorAcc,"
                        + "z.acceptorbankno as acceptorBankNo,"
                        + "z.acceptor_bank_name as acceptorBankName,"
                        + "c.box_status   as boxStatus,"
                        + "z.bill_status  as billStatus,"
                        + "null           as sysSubject,"
                        + "z.bill_flag    as billFlag,"
                        + "z.payer        as openbill,"
                        + "null           as Acceptordate,"
                        + "null           as flwNo,"
                        + "z.payer        as payerName,"
                        + "l.rec_nm       as guarantor,"
                        + "l.rec_acct_id  as Guarantoracc,"
                        + "l.rec_acctsvcr as guarantorBankNo,"
                        + "b.bank_name    as guarantorBank,"
                        + "b.BANK_TYPE_ID as guaranttorBankType,"
                        + "null           as guarantorAddr,"
                        + "null           as guarantorDate,"
                        + "l.rec_nm       as aGuarantor,"
                        + "l.rec_acct_id  as aGuarantorAcc,"
                        + "l.rec_acctsvcr as aGuarantorNo,"
                        + "null           as aGuarantorAddr,"
                        + "null           as aGuarantorDate,"
                        + "l.rec_nm       as eGuarantor,"
                        + "l.rec_acct_id  as eGuarantorAcc,"
                        + "l.rec_acctsvcr as eGuarantorNo,"
                        + "null           as eGuarantorDate,"
                        + "null           as payId,"
                        + "c.Cn_Accrecords as preHand,"
                        + "null            as payStatus,"
                        + "null            as guaranteeMode,"
                        + "null            as trueFlag,"
                        + "null            as drwgbckdt,"
                        + "l.id            as recordId,"
                        + "bt_concat2('1008',l.brief_id)      as briefId,"
                        + "l.bif_code      as bifCode,"
                        + "l.return_code   as returnCode,"
                        + "l.return_msg    as returnMsg,"
                        + "l.apply_date    as tallyDate,"
                        + "null            as coptorId,"
                        + "z.a_contract_no as contractNo,"
                        + "null            as signBank,"
                        + "null            as signMan,"
                        + "l.apply_date    as applyDate,"
                        + "l.abs           as summary,"
                        + "null            as budgetSign,"
                        + "d.discount_date as discountDate,"
                        + "d.daynum        as daynum,"
                        + "d.interest_rates as interestRates,"
                        + "d.interest      as interest,"
                        + "null            as lsno,"
                        + "null            as userno,"
                        + "null            as discountType,"
                        + "null            as uuid,"
                        + "l.req_acct_id   as acctId,"
                        + "l.req_acctsvcr  as acctAcctsvcr,"
                        + "l.req_acctsvcr_ag as agcyAcctsvcr,"
                        + "d.rpdmk         as rpdmk,"
                        + "null            as bussdt,"
                        + "z.banendrsmtmk  as bankendrsmtmk,"
                        + "d.sttlmmk       as sttlmmk,"
                        + "d.rpdopendt     as rpdopendt,"
                        + "d.rpdduedt      as rpdduedt,"
                        + "d.Interest_Rates as INTEREST_RATES,"
                        + "d.real_money    as rpdamt,"
                        + "d.invc_nb       as invcnb,"
                        + "d.rpdintrstrate as rpdintrstrate,"
                        + "null            as rate,"
                        + "null            as appldt,"
                        + "BT_NVL_CHAR(l.rsn,l.rmk)          as rsn,"
                        + "l.rec_acct_id   as backacctId,"
                        + "l.rec_acctsvcr  as backacctAcctsvcr,"
                        + "l.rcrs_tp       as rcrsTp,"
                        // + "r.msgid         as msgid,"
                        + "r.Msdt          as msgcredttm,"
                        + "null            as nexttime,"
                        + "l.opt_status    as status,"
                        + "l.brief_id      as msgtype,"
                        + "l.lastid        as lastid,"
                        + "l.bailmoney     as bailmoney,"
                        + "l.feemoney      as feemoney,"
                        + "null            as bail,"
                        + "null            as lastsysSubject,"
                        + "l.bankendrsmtmk as transfer,"
                        + "d.paydate       as paydate,"
                        + "d.deferdate     as deferdate,"
                        + "null            as contractFlag,"
                        + "l.sign_up_flag  as signUpFlag,"
                        + "null            as checkName,"
                        + "null            as checkBankacc,"
                        + "null            as checkBank,"
                        + "null            as endorsementCount,"
                        + "null            as outgolosttype,"
                        + "l.recid         as receivebizId,"
                        + "d.Buyerinterest_Money as buyerInterest,"
                        + "null            as isOnestep,"
                        + "l.lastbillcode  as lastbillstatus,"
                        + "null            as crmrkbypropsr,"
                        + "null            as guarantorBanktype,"
                        + "l.rmk            as remark,"
                        + "null            as payeeBanktype,"
                        + "null            as guarantorBankname,"
                        + "null            as ucondlprmsmrk,"
                        + "BT_NVL_CHAR(d.sttlmmk,l.sttlmmk)       as paytype,"
                        + "(case when d.buyerinterest_ratio =0 then '1' when d.buyerinterest_ratio =1 then '2' else '3' end )  as payratetype,"
                        + "null            as busaccid,"
                        + "null            as iscib,"
                        + "null            as redmtype,"
                        + "null            as guarnttype,"
                        + "d.aoaccninf_id  as payinacc,"
                        + "d.aoaccninf_ac  as payinbankno,"
                        + "null            as paymenttype,"
                        + "null            as businessSign,"
                        + "null            as tradeType,"
                        + "null            as applysign,"
                        + "null            as biztyp,"
                        + "null            as collztnacctid,"
                        + "null            as ccy,"
                        + "z.bill_money    as rechMoney,"
                        + "z.bill_money    as money,"
                        + "null            as querySource,"
                        + "c.corp_id       as corpId,"
                        + "null            as mobile,"
                        + "l.req_nm        as acctName,"
                        + "l.rec_nm        as backAcctName,"
                        + "l.rec_nm     as backAcctBankName,"
                        + "null            as autoAcceptance,"
                        + "null            as autoIssuse,"
                        + "null            as bankserialno,"
                        + "c.bank_name     as acctBankName,"
                        + "BT_NVL_CHAR(l.Rcrsrsncd,r.dshnrcd)     as rcrsrsncd,"
                        + "bt_concat2('1008',l2.brief_id)     as lastBriefId,"
                        + "l.msg_id     as msgId," + "BT_NVL_CHAR(l.bill_id,l2.bill_id)     as billId,"
                        + "l.req_cmonid     as accCmonid,"

                        + "z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,"
                        + "z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,"
                        + "z.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,"
                        + "z.SBBLL_RNG_END_SN as sbbllRngEndSn,"
                        + "z.BL_STD_AMT as blStdAmt,"
                        + "z.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo,"
                        + "z.BL_ACPTR_ID_TPCD as blAcptrIdTpcd,"
                        + "z.BL_RCVPYMTPS_ID_TPCD as blRcvpymtpsIdTpcd,"
                        + "z.EBILL_BL_CRCL_INDC as ebillBlCrclIndc,"
                        + "z.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo,"
                        + "z.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo,"
                        + "l.LAST_CIRCULATE_CODE  as lastCirculateCode,"
                        + "l.LAST_BL_BSN_TPCD  as lastBlBsnTpcd,"

                        + "l.req_Soc_No as reqSocNo,"
                        + "l.rec_Soc_No as recSocNo,"
                        + "l.rec_Dgh_Type as recDghType,"
                        + "l.settledate as settledate,"
                        + "l.settletype as settletype,"

                        + "c.use_type as useType,"
                        + "c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo,"
                        + "r.ebill_Bl_Crcl_Indcd as ebillBlCrclIndcd,"
                        + "r.intpymtprt_Way as intpymtprtWay,"
                        + "r.buyerpayintrcntno as buyerpayintrcntno,"
                        + "r.intpymt_Psn_Nm as intpymtPsnNm,"
                        + "r.intpymt_Psn_Accno as intpymtPsnAccno,"
                        + "r.bl_Rcv_Unn_Id_No as blRcvUnnIdNo,"
                        + "r.bl_Req_Unn_Id_No as blReqUnnIdNo,"
                        + "r.bl_Rcv_Id_Tpcd as blRcvIdTpcd,"
                        + "r.bl_Req_Id_Tpcd as blReqIdTpcd,"

                        + "z.COOPERATIVE_BANK_NO as cooperativeBankNo,"
                        + "c.IS_CREDIT_DRAW as isCreditDraw,"

                        + "l.rec_cmonid     as cmonid,");
        if (BillPoolConstant.IS_RUN_POOL) {
            sqlselect.append("l.pd_id     as pdid,");
        }
        sqlselect.append("l.BAIL_TYPE 	 as bailType ");
        sqlselect
                .append("   from AB_VOUCHERINFO_VICE c left join AB_VOUCHERINFO z  on z.id = c.vid "
                        + " left join AB_ACCRECORDS l on c.id = l.vvid"
                        + " left join AB_ACCRECORDS l2 on l.lastid=l2.id"
                        + " left join bt_bank_acc b  on l.req_acct_id=b.bank_acc"
                        + " left join AB_DISCOUNT d  on l.id=d.record_id"
                        + " left join AB_BILL_RECEIVEBIZ r on l.recid=r.id"
                        + " left join sys_corp s on c.corp_id = s.id ")
                .append(" where c.valid_sign='"
                        + AccebillConstant.AB_BILL_VALID_SIGN_1 + "'  ")
                // .append(" and z.bill_flag ='" +
                // AccebillConstant.AB_BILL_FLAG_ELECT + "' ")
                .append(" and l.valid_sign in ('"
                        + AccebillConstant.AB_BILL_VALID_SIGN_1 + "') ")
                .append(" and l.SEND_SIGN='"
                        + AccebillConstant.AB_BILL_SEND_SIGN_Y + "' ")
                .append(" and l.STATUS='"
                        + AccebillConstant.AB_BILL_BUS_STATUS95 + "' ");
        StringBuffer sql = new StringBuffer();
        Map<String,Object> params=new HashMap<String,Object>();
        if (status != null) {
            if(AccebillConstant.AB_BILL_OPT_STATUS18.equals(status)){
                sql.append(" and l.OPT_STATUS in ('17','18') ");
            }else {
                sql.append(" and l.OPT_STATUS in ('" + status + "') ");
            }
        }
        if (briefId != null) {
            sql.append(" and l.brief_id='"
                    + EDraftConstant.getT2BriefId(briefId) + "' ");
        }
        if (bifCode != null) {
//            sql.append(" and l.bif_code='" + bifCode + "' ");
            sql.append(" and l.bif_code in (:bifCode) ");
            params.put("bifCode", StringUtil.split(bifCode,","));
        }
        if (reCode != null) {
            sql.append(" and (l.return_code='" + reCode
                    + "' or l.return_code is null) ");
        }
        if (bailType != null) {
            sql.append(" and l.BAIL_TYPE='" + bailType + "' ");
        }
        sqlselect.append(sql).append(" order by l.bif_code,l.id ");
        List<Map<String, Object>> objStr = this.listBySQL(sqlselect.toString(),params);
        vcInfoList = this.getBeanList(objStr);
        return vcInfoList;
    }

    /**  *
     * <b>功能：</b>根据银行代码查询已完成，不是作废的，是发送标记的，最新创建的数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-05 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据信息
     */
    @SuppressWarnings("unchecked")
    public List<VoucherInfoVo> queryBillByBifCode(String bifCode) {
        List<VoucherInfoVo> vcInfoList = new ArrayList<VoucherInfoVo>();
        StringBuffer sqlselect = new StringBuffer();
        sqlselect
                .append("select * "
                        + "from ("
                        + "select z.id       as voucherId,"
                        + "z.orig_no      as origNo,"
                        + "z.bill_type    as billType,"
                        + "z.Start_Date   as startDate,"
                        + "z.end_date     as endDate,"
                        + "z.bill_money   as billMoney,"
                        + "z.banendrsmtmk as banendrsmtmk,"
                        + "z.payee        as payee,"
                        + "z.payeeacc     as payeeAcc,"
                        + "z.payee_bank_name as payeeBankName,"
                        + "z.payeebankno  as payeeBankNo,"
                        + "z.payer        as payer,"
                        + "z.payeracc     as payerAcc,"
                        + "z.payerbankno  as payerBankNo,"
                        + "z.payer_bank_name as payerBankName,"
                        + "z.acceptor     as acceptor,"
                        + "z.acceptoracc  as acceptorAcc,"
                        + "z.acceptorbankno as acceptorBankNo,"
                        + "z.acceptor_bank_name as acceptorBankName,"
                        + "c.box_status   as boxStatus,"
                        + "z.bill_status  as billStatus,"
                        + "null           as sysSubject,"
                        + "z.bill_flag    as billFlag,"
                        + "z.payer        as openbill,"
                        + "null           as Acceptordate,"
                        + "null           as flwNo,"
                        + "z.payer        as payerName,"
                        + "l.rec_nm       as guarantor,"
                        + "l.rec_acct_id  as Guarantoracc,"
                        + "l.rec_acctsvcr as guarantorBankNo,"
                        + "b.bank_name    as guarantorBank,"
                        + "b.BANK_TYPE_ID as guaranttorBankType,"
                        + "null           as guarantorAddr,"
                        + "null           as guarantorDate,"
                        + "l.rec_nm       as aGuarantor,"
                        + "l.rec_acct_id  as aGuarantorAcc,"
                        + "l.rec_acctsvcr as aGuarantorNo,"
                        + "null           as aGuarantorAddr,"
                        + "null           as aGuarantorDate,"
                        + "l.rec_nm       as eGuarantor,"
                        + "l.rec_acct_id  as eGuarantorAcc,"
                        + "l.rec_acctsvcr as eGuarantorNo,"
                        + "null           as eGuarantorDate,"
                        + "null           as payId,"
                        + "c.Cn_Accrecords as preHand,"
                        + "null            as payStatus,"
                        + "null            as guaranteeMode,"
                        + "null            as trueFlag,"
                        + "null            as drwgbckdt,"
                        + "l.id            as recordId,"
                        + "bt_concat2('1008',l.brief_id)      as briefId,"
                        + "l.bif_code      as bifCode,"
                        + "l.return_code   as returnCode,"
                        + "l.return_msg    as returnMsg,"
                        + "l.apply_date    as tallyDate,"
                        + "null            as coptorId,"
                        + "z.a_contract_no as contractNo,"
                        + "null            as signBank,"
                        + "null            as signMan,"
                        + "l.apply_date    as applyDate,"
                        + "l.abs           as summary,"
                        + "null            as budgetSign,"
                        + "d.discount_date as discountDate,"
                        + "d.daynum        as daynum,"
                        + "d.interest_rates as interestRates,"
                        + "d.interest      as interest,"
                        + "null            as lsno,"
                        + "null            as userno,"
                        + "null            as discountType,"
                        + "null            as uuid,"
                        + "l.req_acct_id   as acctId,"
                        + "l.req_acctsvcr  as acctAcctsvcr,"
                        + "l.req_acctsvcr_ag as agcyAcctsvcr,"
                        + "d.rpdmk         as rpdmk,"
                        + "null            as bussdt,"
                        + "z.banendrsmtmk  as bankendrsmtmk,"
                        + "d.sttlmmk       as sttlmmk,"
                        + "d.rpdopendt     as rpdopendt,"
                        + "d.rpdduedt      as rpdduedt,"
                        + "d.Interest_Rates as INTEREST_RATES,"
                        + "d.real_money    as rpdamt,"
                        + "d.invc_nb       as invcnb,"
                        + "d.rpdintrstrate as rpdintrstrate,"
                        + "null            as rate,"
                        + "null            as appldt,"
                        + "BT_NVL_CHAR(l.rsn,l.rmk)           as rsn,"
                        + "l.rec_acct_id   as backacctId,"
                        + "l.rec_acctsvcr  as backacctAcctsvcr,"
                        + "l.rcrs_tp       as rcrsTp,"
                        // + "r.msgid         as msgid,"
                        + "r.Msdt          as msgcredttm,"
                        + "null            as nexttime,"
                        + "l.opt_status    as status,"
                        + "l.brief_id      as msgtype,"
                        + "l.lastid        as lastid,"
                        + "l.bailmoney     as bailmoney,"
                        + "l.feemoney      as feemoney,"
                        + "null            as bail,"
                        + "null            as lastsysSubject,"
                        + "l.bankendrsmtmk as transfer,"
                        + "d.paydate       as paydate,"
                        + "d.deferdate     as deferdate,"
                        + "null            as contractFlag,"
                        + "l.sign_up_flag  as signUpFlag,"
                        + "null            as checkName,"
                        + "null            as checkBankacc,"
                        + "null            as checkBank,"
                        + "null            as endorsementCount,"
                        + "null            as outgolosttype,"
                        + "l.recid         as receivebizId,"
                        + "d.Buyerinterest_Money as buyerInterest,"
                        + "null            as isOnestep,"
                        + "l.lastbillcode  as lastbillstatus,"
                        + "null            as crmrkbypropsr,"
                        + "null            as guarantorBanktype,"
                        + "l.rmk            as remark,"
                        + "null            as payeeBanktype,"
                        + "null            as guarantorBankname,"
                        + "null            as ucondlprmsmrk,"
                        + "BT_NVL_CHAR(d.sttlmmk,l.sttlmmk)       as paytype,"
                        + "(case when d.buyerinterest_ratio =0 then '1' when d.buyerinterest_ratio =1 then '2' else '3' end )  as payratetype,"
                        + "null            as busaccid,"
                        + "null            as iscib,"
                        + "null            as redmtype,"
                        + "null            as guarnttype,"
                        + "d.aoaccninf_id  as payinacc,"
                        + "d.aoaccninf_ac  as payinbankno,"
                        + "null            as paymenttype,"
                        + "null            as businessSign,"
                        + "null            as tradeType,"
                        + "null            as applysign,"
                        + "null            as biztyp,"
                        + "null            as collztnacctid,"
                        + "null            as ccy,"
                        + "z.bill_money    as rechMoney,"
                        + "z.bill_money    as money,"
                        + "null            as querySource,"
                        + "c.corp_id       as corpId,"
                        + "null            as mobile,"
                        + "l.req_nm        as acctName,"
                        + "l.rec_nm        as backAcctName,"
                        + "l.rec_nm     as backAcctBankName,"
                        + "null            as autoAcceptance,"
                        + "null            as autoIssuse,"
                        + "null            as bankserialno,"
                        + "c.bank_name     as acctBankName,"
                        + "BT_NVL_CHAR(l.Rcrsrsncd,r.dshnrcd)     as rcrsrsncd,"
                        + "bt_concat2('1008',l2.brief_id)     as lastBriefId,"
                        + "l.BAIL_TYPE 	  as bailType,"
                        + "c.id            as vvid,"
                        + "l.msg_id     as msgId,"
                        + "BT_NVL_CHAR(l.bill_id,l2.bill_id)     as billId,"
                        + "l.req_cmonid     as accCmonid,"

                        + "z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,"
                        + "z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,"
                        + "z.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,"
                        + "z.SBBLL_RNG_END_SN as sbbllRngEndSn,"
                        + "z.BL_STD_AMT as blStdAmt,"
                        + "z.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo,"
                        + "z.BL_ACPTR_ID_TPCD as blAcptrIdTpcd,"
                        + "z.BL_RCVPYMTPS_ID_TPCD as blRcvpymtpsIdTpcd,"
                        + "z.EBILL_BL_CRCL_INDC as ebillBlCrclIndc,"
                        + "z.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo,"
                        + "z.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo,"
                        + "l.LAST_CIRCULATE_CODE  as lastCirculateCode,"
                        + "l.LAST_BL_BSN_TPCD  as lastBlBsnTpcd,"

                        + "l.req_Soc_No as reqSocNo,"
                        + "l.rec_Soc_No as recSocNo,"
                        + "l.rec_Dgh_Type as recDghType,"
                        + "l.settledate as settledate,"
                        + "l.settletype as settletype,"

                        + "c.use_type as useType,"
                        + "c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo,"
                        + "r.ebill_Bl_Crcl_Indcd as ebillBlCrclIndcd,"
                        + "r.intpymtprt_Way as intpymtprtWay,"
                        + "r.buyerpayintrcntno as buyerpayintrcntno,"
                        + "r.intpymt_Psn_Nm as intpymtPsnNm,"
                        + "r.intpymt_Psn_Accno as intpymtPsnAccno,"
                        + "r.bl_Rcv_Unn_Id_No as blRcvUnnIdNo,"
                        + "r.bl_Req_Unn_Id_No as blReqUnnIdNo,"
                        + "r.bl_Rcv_Id_Tpcd as blRcvIdTpcd,"
                        + "r.bl_Req_Id_Tpcd as blReqIdTpcd,"

                        + "z.COOPERATIVE_BANK_NO as cooperativeBankNo,"

                        + "l.rec_cmonid     as cmonid,");
        if (BillPoolConstant.IS_RUN_POOL) {
            sqlselect.append("l.pd_id     as pdid,");
        }
        sqlselect
                .append("l.create_date as createdate"
                        + "   from AB_VOUCHERINFO_VICE c left join AB_VOUCHERINFO z  on z.id = c.vid "
                        + " left join AB_ACCRECORDS l on c.id = l.vvid"
                        + " left join AB_ACCRECORDS l2 on l.lastid=l2.id"
                        + " left join bt_bank_acc b  on l.req_acct_id=b.bank_acc"
                        + " left join AB_DISCOUNT d  on l.id=d.record_id"
                        + " left join AB_BILL_RECEIVEBIZ r on l.recid=r.id"
                        + " left join sys_corp s on c.corp_id = s.id "
//                        + " ) s ,"
                        + " left join "
                        + " (select max(l.create_date) as createdate,l.vid as vid,l.vvid as vvid  from AB_VOUCHERINFO_VICE c, AB_VOUCHERINFO z, AB_ACCRECORDS l,BT_BANK_ACC a  where z.id = c.vid  and c.id = l.vvid and c.bank_acc=a.bank_acc and a.is_online=1 and a.valid_sign=1 and "
                        + " l.bif_code in (:bifCode) and l.send_sign='"
                        + AccebillConstant.AB_BILL_SEND_SIGN_Y
                        + "' and z.bill_status<>'"
                        + AccebillConstant.AB_BILL_STATUS_000002
                        + "' and l.OPT_STATUS="
                        + AccebillConstant.AB_BILL_OPT_STATUS19
                        + " and l.status="
                        + AccebillConstant.AB_BILL_BUS_STATUS95
//                        + "  group by l.vid,l.vvid) v "
                        + "  group by l.vid,l.vvid) v on l.create_date=v.createdate and z.id=v.vid and c.id=v.vvid "
//                        + "  where  s.createdate=v.createdate and s.voucherId=v.vid and s.vvid=v.vvid order by s.bif_code and s.recordId ");
                        + "  where  v.vid is not null order by l.bif_code,l.id ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bifCode", StringUtil.split(bifCode,","));
        List<Map<String, Object>> objStr = this.listBySQL(sqlselect.toString(),
                params);
        vcInfoList = this.getBeanList(objStr);
        return vcInfoList;
    }

    /**  *
     * <b>功能：</b>查询结果赋值<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     */
    private List<VoucherInfoVo> getBeanList(List<Map<String, Object>> objStr) {
        List<VoucherInfoVo> list = new ArrayList<VoucherInfoVo>();
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = (Map) object;

                Class clz = VoucherInfoVo.class;
                Method[] ms = clz.getMethods();
                VoucherInfoVo voucherInfo = new VoucherInfoVo();
                for(Method m : ms){
                    String mName = m.getName();
                    if(!mName.startsWith("set")){
                        continue;
                    }
                    String subMName = mName.substring(3);

                    Class[] methodParameClzArr = m.getParameterTypes();
                    if(methodParameClzArr == null || methodParameClzArr.length < 1){
                        continue;
                    }
                    Class methodParame = methodParameClzArr[0];

                    try {
                        Method mt = null;
                        boolean intergeB = methodParame.getName().contains(".Integer");

                        boolean longB = methodParame.getName().contains(".Long");

                        boolean dateTB = methodParame.getName().contains(".Timestamp");

                        boolean dateDB = methodParame.getName().contains(".Date") || methodParame.getName().contains(".Timestamp");

                        boolean bigDecimalB = methodParame.getName().contains(".BigDecimal");

                        if(subMName.equalsIgnoreCase("ATTACHMENTID")){// 针对出票申请可以关联银行授信的授信编号、关联保证金查询保证账号存出账号和户名
                            mt = clz.getMethod(mName,String.class);
                            mt.invoke(voucherInfo, (ary.get(subMName.toUpperCase()) == null || StringUtil.isEmpty(ary.get(subMName.toUpperCase()).toString())) ? null : ary.get(subMName.toUpperCase()).toString());
                            Object attachmentId = ary.get("ATTACHMENTID");
                            if(attachmentId!=null&&!StringUtil.isBlank_new(attachmentId.toString())) {
                                voucherInfo.setAttachmentPath(this.getAttachmentPathByGroupId(attachmentId.toString().trim()));
                            }
                        }
                        if(!ary.containsKey(subMName.toUpperCase())){
                            continue;
                        }
                        if(intergeB){
                            mt = clz.getMethod(mName,Integer.class);
                            mt.invoke(voucherInfo,ary.get(subMName.toUpperCase()) == null ? null : Integer.parseInt(ary.get(subMName.toUpperCase()).toString()));
                        }else if(longB){
                            mt = clz.getMethod(mName,Long.class);
                            mt.invoke(voucherInfo,ary.get(subMName.toUpperCase()) == null ? null : Long.parseLong(ary.get(subMName.toUpperCase()).toString()));
                        } else if(dateTB){
                            mt = clz.getMethod(mName, Timestamp.class);
                            mt.invoke(voucherInfo,ary.get(subMName.toUpperCase()) == null ? null : DateUtils.formatTimestampYYYY_MM_DD(ary.get(subMName.toUpperCase()).toString()));
                        }else if(dateDB){
                            mt = clz.getMethod(mName, Date.class);
                            mt.invoke(voucherInfo,ary.get(subMName.toUpperCase()) == null ? null : DateUtils.getDateYMD((Date)ary.get(subMName.toUpperCase())));
                        } else if(bigDecimalB){
                            mt = clz.getMethod(mName, BigDecimal.class);
                            mt.invoke(voucherInfo,ary.get(subMName.toUpperCase()) == null ? null : new BigDecimal(ary.get(subMName.toUpperCase()).toString()));
                        }else {
                            mt = clz.getMethod(mName,String.class);
                            mt.invoke(voucherInfo, (ary.get(subMName.toUpperCase()) == null || StringUtil.isEmpty(ary.get(subMName.toUpperCase()).toString())) ? null : ary.get(subMName.toUpperCase()).toString());
                        }
                    }catch (Exception e){
                        logger.debug("errorMsg="+e.getMessage());
                    }

                }

                // 对保证briefId进行处理
                if (StringUtil.isNotEmpty(voucherInfo.getBailType()) && "1008017".equals(voucherInfo.getBriefId())) {
                    try {
                        int bailType = Integer.parseInt(voucherInfo.getBailType());
                        voucherInfo.setBriefId(voucherInfo.getBriefId() + bailType);
                    } catch (Exception e) {
                    }
                }

                // 对通用车保证lastbriefId进行处理
                if (StringUtil.isNotEmpty(voucherInfo.getBailType()) && "1008017".equals(voucherInfo.getLastBriefId())) {
                    try {
                        int bailType = Integer.parseInt(voucherInfo.getBailType());
                        voucherInfo.setLastBriefId(voucherInfo.getLastBriefId() + bailType);
                    } catch (Exception e) {
                    }
                }
                voucherInfo.setRechMoney(null);
                list.add(voucherInfo);
            }
        }
        return list;
    }

    /**  *
     * <b>功能：</b>根据流水ID更新流水表状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param recordId 流水ID
     * @param status   状态
     */
    @SuppressWarnings("unchecked")
    public int updateAccrecordsStatusByRecordId(String recordId, String status) {
        String hql = " update AB_ACCRECORDS set OPT_STATUS=:optStatus,update_date=:updateDate "
                + " where id=:id ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("optStatus", status);
        params.put("updateDate", new Date());
        params.put("id", recordId);
        if (status != null
                && !AccebillConstant.AB_BILL_OPT_STATUS19.equals(status)) {
            hql += " and OPT_STATUS!=:optStatusOK";
            params.put("optStatusOK", AccebillConstant.AB_BILL_OPT_STATUS19);
        }
        return createSQLQuery(hql, params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据流水ID更新流水表状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param recordId 流水ID
     * @param status   状态
     */
    @SuppressWarnings("unchecked")
    public int updateAccrecordsInfoByRecordId(String recordId, String status,
                                              String returnCode, String returnMsg, String billId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer updatesql = new StringBuffer("update AB_ACCRECORDS set");
        StringBuffer sql = new StringBuffer();
        sql.append(" update_date=:updateDate ");
        params.put("updateDate", new Date());
        if (status != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" OPT_STATUS=:optStatus");
            params.put("optStatus", status);
        }
        if (returnCode != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" RETURN_CODE=:returnCode");
            params.put("returnCode", returnCode);
        }
        if (StringUtil.isNotEmpty(billId)) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" BILL_ID=:billId");
            params.put("billId", billId);
        }

        if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(status) || AccebillConstant.AB_BILL_OPT_STATUS_3.equals(status) || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(status)) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" BUSINESS_DATE=:todayDate");
            params.put("todayDate", new Date());
        }
        if (status == AccebillConstant.AB_BILL_OPT_STATUS_4) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" VALID_SIGN =:validSign ");
            params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_2);
        }
        if (returnMsg != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" RETURN_MSG=:returnMsg");
            params.put("returnMsg", returnMsg);
        }
        updatesql.append(sql).append(" WHERE id =:id");
        params.put("id", recordId);
        if (status != null
                && !AccebillConstant.AB_BILL_OPT_STATUS19.equals(status)) {
            updatesql.append(" and OPT_STATUS!=:optStatusOK");
            params.put("optStatusOK", AccebillConstant.AB_BILL_OPT_STATUS19);
        }
        int size = createSQLQuery(updatesql.toString(), params).executeUpdate();
        this.flush();
        return size;
    }

    /**  *
     * 新一代票据修改票据流通标志及状态
     *
     * @param voucherId  票据ID
     * @param boxStatus  处理标记
     * @param billStatus 票据状态
     * @param ebillBlCrclIndc 票据流通标志
     * @param blBsnTpcd 票据业务类型代码
     */
    public int updateVoucherInfoByVoucherId_New(String voucherId,
                                                Integer boxStatus, String billStatus, String ebillBlCrclIndc, String blBsnTpcd) {
        StringBuffer updatesql = new StringBuffer("update AB_VOUCHERINFO set");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        if (boxStatus != null && boxStatus != 0) {
            sql.append(" BOX_STATUS=:boxStatus");
            params.put("boxStatus", boxStatus);
        }
        if (billStatus != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" BILL_STATUS=:billStatus ");
            params.put("billStatus", billStatus);
        }
        if (StringUtils.isNotBlank(ebillBlCrclIndc)) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" EBILL_BL_CRCL_INDC=:ebillBlCrclIndc ");
            params.put("ebillBlCrclIndc", ebillBlCrclIndc);
        }
        if(StringUtils.isNotBlank(blBsnTpcd)){
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" BL_BSN_TPCD=:blBsnTpcd ");
            params.put("blBsnTpcd", blBsnTpcd);
        }
        updatesql.append(sql).append(" WHERE id =:id");
        params.put("id", voucherId);
        int num = 0;
        if ((boxStatus != null && boxStatus != 0) || billStatus != null) {
            num = createSQLQuery(updatesql.toString(), params).executeUpdate();
        }
        return num;
    }

    /**  *
     * <b>功能：</b>根据票据ID更新票据表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param voucherId  票据ID
     * @param boxStatus  处理标记
     * @param billStatus 票据状态
     * @param ebillBlCrclIndc 票据流通标志
     * @param blBsnTpcd 票据业务类型代码
     */
    @SuppressWarnings("unchecked")
    public int updateVoucherInfoByVoucherId(String voucherId,
                                            Integer boxStatus, String billStatus, String ebillBlCrclIndc, String blBsnTpcd) {
        StringBuffer updatesql = new StringBuffer("update AB_VOUCHERINFO set");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        if (boxStatus != null && boxStatus != 0) {
            sql.append(" BOX_STATUS=:boxStatus");
            params.put("boxStatus", boxStatus);
        }
        if (billStatus != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" BILL_STATUS=:billStatus ");
            params.put("billStatus", billStatus);
        }
        if (ebillBlCrclIndc != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" EBILL_BL_CRCL_INDC=:ebillBlCrclIndc ");
            params.put("ebillBlCrclIndc", ebillBlCrclIndc);
        }
        if(StringUtils.isNotBlank(blBsnTpcd)){
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" BL_BSN_TPCD=:blBsnTpcd ");
            params.put("blBsnTpcd", blBsnTpcd);
        }

        //不可转让撤销
        if(StringUtils.isNotBlank(blBsnTpcd) && AccebillConstant.NEW_DRAFT_TYPE_BC18.equals(blBsnTpcd)
                && StringUtils.isNotBlank(ebillBlCrclIndc) && AccebillConstant.CIRCULATE_TF0301.equals(ebillBlCrclIndc)){
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" BANENDRSMTMK='EM00' ");
        }

        if(AccebillConstant.CIRCULATE_TF0303.equals(ebillBlCrclIndc)){
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" BANENDRSMTMK='EM01' ");
        }

        updatesql.append(sql).append(" WHERE id =:id");
        params.put("id", voucherId);
        int num = 0;
        if ((boxStatus != null && boxStatus != 0) || billStatus != null) {
            num = createSQLQuery(updatesql.toString(), params).executeUpdate();
        }
        return num;
    }

    /**  *
     * <b>功能：</b>根据流水ID更新票据从表处理状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param recordId  票据ID
     * @param boxStatus  处理标记
     */
    public int updateVoucherInfoViceBoxStatusByRecordIdId(String recordId,
                                                          Integer boxStatus) {
        if (StringUtil.isEmpty(recordId) || boxStatus == null) {
            return 0;
        }
        BillAbAccrecords acc = this.getAccrecordsByRecordId(recordId);
        if (acc == null) {
            return 0;
        }
        return updateVoucherInfoViceBoxStatusByVvid(acc.getVvid(), boxStatus);
    }

    /**  *
     * <b>功能：</b>根据流水ID更新票据从表处理状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-5-31 <br>
     *
     * @param vvid  票据ID
     * @param boxStatus  处理标记
     */
    @SuppressWarnings("unchecked")
    public int updateVoucherInfoViceBoxStatusByVvid(String vvid,
                                                    Integer boxStatus) {
        if (StringUtil.isEmpty(vvid) || boxStatus == null) {
            return 0;
        }
        StringBuffer updatesql = new StringBuffer(
                "update AB_VOUCHERINFO_VICE set");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE_DATE=:updateDate ");
        params.put("updateDate", new Date());
        if (boxStatus != null && boxStatus != 0) {
            sql.append(" ,BOX_STATUS=:boxStatus");
            params.put("boxStatus", boxStatus);
        }
        updatesql.append(sql).append(" WHERE id =(:vvid)");
        params.put("vvid", vvid);
        return createSQLQuery(updatesql.toString(), params).executeUpdate();
    }

    /**  *
     * 更新前手
     * @param vvid
     * @param cnAccrecords
     * @return
     */
    public int updateVoucherInfoViceCnAccrecordsByVvid(String vvid,
                                                       String cnAccrecords) {
        if (StringUtil.isEmpty(vvid) || cnAccrecords == null) {
            return 0;
        }
        StringBuffer updatesql = new StringBuffer(
                "update AB_VOUCHERINFO_VICE set");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE_DATE=:updateDate ");
        params.put("updateDate", new Date());
        if (StringUtil.isNotEmpty(cnAccrecords)) {
            sql.append(" ,cn_accrecords=:cnAccrecords");
            params.put("cnAccrecords", cnAccrecords);
        }
        updatesql.append(sql).append(" WHERE id =(:vvid)");
        params.put("vvid", vvid);
        return createSQLQuery(updatesql.toString(), params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据票据ID更新票据表票据号<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param voucherId 票据ID
     * @param origNo    票据号
     */
    public int updateVoucherOrigNoByVoucherId(String voucherId, String origNo, String sbbllRngStrtSn, String sbbllRngEndSn) {
        if (StringUtil.isBlank_new(origNo)) {
            return 0;
        } else if (StringUtil.isBlank_new(origNo.trim())) {
            return 0;
        }
        // 修改票据号的同时判断是否已经有此票据号非本次主表ID的数据，有则删除再把流水表被删除的voucherId变成此主表ID
        BillAbVoucherinfo voucherInfo = billVoucherInfoDao
                .getBillVoucherInfoByOrigNo(origNo,sbbllRngStrtSn, sbbllRngEndSn);
        if (voucherInfo != null && !voucherId.equals(voucherInfo.getId())) {
//			billVoucherInfoDao.deleteById(voucherInfo.getId());
            voucherInfo.setOrigNo(origNo + "_old");
            billVoucherInfoDao.update(voucherInfo);
            // 修改流水表
            String sql = "UPDATE AB_ACCRECORDS SET VID='" + voucherId
                    + "' WHERE VID='" + voucherInfo.getId() + "'";
            createSQLQuery(sql).executeUpdate();
            // 修改从表
            sql = "UPDATE AB_VOUCHERINFO_VICE SET VID='" + voucherId
                    + "' WHERE VID='" + voucherInfo.getId() + "'";
            createSQLQuery(sql).executeUpdate();
        }

        String hql = " UPDATE AB_VOUCHERINFO set ORIG_NO =:origNo, SBBLL_RNG_STRT_SN =:sbbllRngStrtSn, SBBLL_RNG_END_SN =:sbbllRngEndSn WHERE id=:id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("origNo", origNo);
        params.put("id", voucherId);
        params.put("sbbllRngStrtSn",sbbllRngStrtSn == null ? "0" : sbbllRngStrtSn);
        params.put("sbbllRngEndSn",sbbllRngEndSn == null ? "0" : sbbllRngEndSn);
        return createSQLQuery(hql, params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据流水ID更新票据流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param recordId   票据流水ID
     * @param status     操作状态
     * @param briefId    业务类型
     * @param returnCode 返回代码
     * @param returnMsg  返回消息
     */
    @SuppressWarnings("unchecked")
    public int updateAccrecordsInfoByRecordId(String recordId, String status,
                                              String briefId, String returnCode, String returnMsg, String billId) {
        return updateAccrecordsInfoByRecordId(recordId, status, briefId, returnCode, returnMsg, billId, null);
    }

    /**  *
     * <b>功能：</b>根据流水ID更新票据流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param recordId   票据流水ID
     * @param status     操作状态
     * @param briefId    业务类型
     * @param returnCode 返回代码
     * @param returnMsg  返回消息
     * @param billId     银行票据id
     * @param validSign  有效标记
     */
    @SuppressWarnings("unchecked")
    public int updateAccrecordsInfoByRecordId(String recordId, String status,
                                              String briefId, String returnCode, String returnMsg, String billId, String validSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer updatesql = new StringBuffer("update AB_ACCRECORDS set");
        StringBuffer sql = new StringBuffer();
        sql.append(" UPDATE_DATE=:updateDate");
        params.put("updateDate", new Date());
        if (status != null) {
            sql.append(",OPT_STATUS=:optStatus");
            params.put("optStatus", status);
        }
        if (StringUtil.isNotEmpty(billId)) {
            sql.append(",BILL_ID=:billId");
            params.put("billId", billId);
        }
        if (StringUtil.isNotEmpty(validSign)) {
            sql.append(",VALID_SIGN=:validSign");
            params.put("validSign", validSign);
        }
        if (briefId != null) {
            sql.append(",BRIEF_ID=:briefId");
            params.put("briefId", EDraftConstant.getT2BriefId(briefId));
        }
        if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(status) || AccebillConstant.AB_BILL_OPT_STATUS_3.equals(status) || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(status)
            // &&(AccebillConstant.BRIEF_STATUS_002.equals(briefId)
            // ||AccebillConstant.BRIEF_STATUS_003.equals(briefId)
            // ||AccebillConstant.BRIEF_STATUS_010.equals(briefId)
            // ||AccebillConstant.BRIEF_STATUS_011.equals(briefId)
            // ||AccebillConstant.BRIEF_STATUS_017.equals(briefId)
            // ||AccebillConstant.BRIEF_STATUS_018.equals(briefId)
            // ||AccebillConstant.BRIEF_STATUS_019.equals(briefId)
            // ||AccebillConstant.BRIEF_STATUS_020.equals(briefId)
            // ||AccebillConstant.BRIEF_STATUS_021.equals(briefId)
            // )
        ) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" BUSINESS_DATE=:todayDate");
            params.put("todayDate", new Date());
        }
        if (returnCode != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" RETURN_CODE=:returnCode");
            params.put("returnCode", returnCode);
        }
        if (returnMsg != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" RETURN_MSG=:returnMsg");
            params.put("returnMsg", returnMsg);
        }
        updatesql.append(sql).append(" WHERE id =:id");
        params.put("id", recordId);
        if (status != null
                && !AccebillConstant.AB_BILL_OPT_STATUS19.equals(status)) {
            //优化有的银行只返回失败，不返回撤回状态（例如通用撤回成功，但查询背书申请结果在队列中，等查询返回时返回的是失败而不是撤回状态）
            if (AccebillConstant.AB_BILL_OPT_STATUS_1.equals(status)) {
                updatesql.append(" and OPT_STATUS not in (19,-3)");
                params.put("optStatusOK", AccebillConstant.AB_BILL_OPT_STATUS19);
            } else {
                updatesql.append(" and OPT_STATUS!=:optStatusOK");
                params.put("optStatusOK", AccebillConstant.AB_BILL_OPT_STATUS19);
            }
        }
        return createSQLQuery(updatesql.toString(), params).executeUpdate();
    }

    public int updateAccrecordsInfoByRecordId3(String recordId, String status,
                                               String briefId, String returnCode, String returnMsg, String billId, String bankendrsmtmk) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer updatesql = new StringBuffer("update AB_ACCRECORDS set");
        StringBuffer sql = new StringBuffer();
        sql.append(" update_date=:updateDate");
        params.put("updateDate", new Date());
        if (StringUtil.isNotEmpty(billId)) {
            sql.append(",BILL_ID=:billId");
            params.put("billId", billId);
        }
        if (status != null) {
            sql.append(",OPT_STATUS=:optStatus");
            params.put("optStatus", status);
            if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(status) || AccebillConstant.AB_BILL_OPT_STATUS_3.equals(status) || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(status)) {
                if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(status)) {
                    sql.append(" ,VALID_SIGN=:validSign");
                    sql.append(" ,STATUS=:status");
                    params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
                    params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
                }
                // if (AccebillConstant.BRIEF_STATUS_9002.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9003.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9010.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9017.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9018.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9019.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9020.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9021.equals(briefId)) {
                sql.append(",BUSINESS_DATE=:todayDate");
                params.put("todayDate", new Date());
                // }
            }
        }

        if(bankendrsmtmk != null){
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" bankendrsmtmk=:bankendrsmtmk");
            params.put("bankendrsmtmk", bankendrsmtmk);
        }

        if (returnCode != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" RETURN_CODE=:returnCode");
            params.put("returnCode", returnCode);
        }
        if (returnMsg != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" RETURN_MSG=:returnMsg");
            params.put("returnMsg", returnMsg);
        }
        updatesql.append(sql).append(" WHERE id =:id");
        if (briefId != null) {
            updatesql.append(" and BRIEF_ID=:briefId");
            params.put("briefId", EDraftConstant.getT2BriefId(briefId));
        }
        params.put("id", recordId);
        if (status != null
                && !AccebillConstant.AB_BILL_OPT_STATUS19.equals(status)) {
            updatesql.append(" and OPT_STATUS!=:optStatusOK");
            params.put("optStatusOK", AccebillConstant.AB_BILL_OPT_STATUS19);
        }
        return createSQLQuery(updatesql.toString(), params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据流水ID更新票据流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-7-5 <br>
     *
     * @param recordId   票据流水ID
     * @param status     操作状态
     * @param briefId    业务类型
     * @param returnCode 返回代码
     * @param returnMsg  返回消息
     */
    @SuppressWarnings("unchecked")
    public int updateAccrecordsInfoByRecordId2(String recordId, String status,
                                               String briefId, String returnCode, String returnMsg, String billId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer updatesql = new StringBuffer("update AB_ACCRECORDS set");
        StringBuffer sql = new StringBuffer();
        sql.append(" update_date=:updateDate");
        params.put("updateDate", new Date());
        if (StringUtil.isNotEmpty(billId)) {
            sql.append(",BILL_ID=:billId");
            params.put("billId", billId);
        }
        if (status != null) {
            sql.append(",OPT_STATUS=:optStatus");
            params.put("optStatus", status);
            if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(status) || AccebillConstant.AB_BILL_OPT_STATUS_3.equals(status) || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(status)) {
                if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(status)) {
                    sql.append(" ,VALID_SIGN=:validSign");
                    sql.append(" ,STATUS=:status");
                    params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
                    params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
                }
                // if (AccebillConstant.BRIEF_STATUS_9002.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9003.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9010.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9017.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9018.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9019.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9020.equals(briefId)
                // ||AccebillConstant.BRIEF_STATUS_9021.equals(briefId)) {
                sql.append(",BUSINESS_DATE=:todayDate");
                params.put("todayDate", new Date());
                // }
            }
        }

        if (returnCode != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" RETURN_CODE=:returnCode");
            params.put("returnCode", returnCode);
        }
        if (returnMsg != null) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            sql.append(" RETURN_MSG=:returnMsg");
            params.put("returnMsg", returnMsg);
        }
        updatesql.append(sql).append(" WHERE id =:id");
        if (briefId != null) {
            updatesql.append(" and BRIEF_ID=:briefId");
            params.put("briefId", EDraftConstant.getT2BriefId(briefId));
        }
        params.put("id", recordId);
        if (status != null
                && !AccebillConstant.AB_BILL_OPT_STATUS19.equals(status)) {
            updatesql.append(" and OPT_STATUS!=:optStatusOK");
            params.put("optStatusOK", AccebillConstant.AB_BILL_OPT_STATUS19);
        }
        return createSQLQuery(updatesql.toString(), params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据流水ID更新票据流水表的上个票据状态与上个科目字段<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param recordId       票据流水ID
     * @param lastbillstatus 上个票据状态
     */
    @SuppressWarnings("unchecked")
    public int updateAccrecordsLastInfoByRecordId(String recordId,
                                                  String lastbillstatus) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "update AB_ACCRECORDS  set LASTBILLCODE=:LASTBILLCODE where id=:id";
        params.put("LASTBILLCODE", lastbillstatus);
        params.put("id", recordId);
        return createSQLQuery(sql, params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据前一笔流水ID修改前一笔流水有效状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param recordId 票据流水ID
     * @param validSign   状态
     */
    public int updateLastAccrecordsValidSignByRecordId(String recordId,
                                                       String validSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer updatesql = new StringBuffer("update AB_ACCRECORDS set");
        StringBuffer sql = new StringBuffer();
        sql.append(" update_date=:updateDate ");
        params.put("updateDate", new Date());
        if (StringUtil.isNotEmpty(validSign)) {
            sql.append(" ,VALID_SIGN=:validSign ");
            params.put("validSign", validSign);
            if (AccebillConstant.AB_BILL_VALID_SIGN_1.equals(validSign)) {
                sql.append(",IS_HIDE=:isHide ");
                params.put("isHide", AccebillConstant.AB_IS_HIDE_N);
            }
        }
        if (StringUtil.isEmpty(recordId)) {
            return 0;
        }
        BillAbAccrecords acc = this.getAccrecordsByRecordId(recordId);
        if (acc == null) {
            return 0;
        } else if (StringUtil.isEmpty(acc.getLastid())) {
            return 0;
        } else {
            updatesql.append(sql).append(" WHERE id =(:lastid)");
            params.put("lastid", acc.getLastid());
        }
        return createSQLQuery(updatesql.toString(), params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据流水ID修改流水有效状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param recordId  票据流水ID
     * @param validSign 状态
     */
    public int updateAccrecordsValidSignByRecordId(String recordId,
                                                   String validSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer updatesql = new StringBuffer("update AB_ACCRECORDS set");
        StringBuffer sql = new StringBuffer();
        sql.append(" update_date=:updateDate ");
        params.put("updateDate", new Date());
        if (validSign != null) {
            sql.append(" ,VALID_SIGN=:validSign");
            params.put("validSign", validSign);
            if (AccebillConstant.AB_BILL_VALID_SIGN_2.equals(validSign)) {
                sql.append(",RETURN_CODE=:returnCode ");
                params.put("returnCode", "99");
            }
        }
        updatesql.append(sql).append(" WHERE id =:id");
        params.put("id", recordId);
        return createSQLQuery(updatesql.toString(), params).executeUpdate();
    }

    public int updateAccValSignAndOptStaByRecordId(String recordId,
                                                   String validSign,String optStatus) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer updatesql = new StringBuffer("update AB_ACCRECORDS set");
        StringBuffer sql = new StringBuffer();
        sql.append(" update_date=:updateDate ");
        params.put("updateDate", new Date());
        if (validSign != null) {
            sql.append(" ,VALID_SIGN=:validSign");
            params.put("validSign", validSign);
           /* if (AccebillConstant.AB_BILL_VALID_SIGN_2.equals(validSign)) {
                sql.append(",RETURN_CODE=:returnCode ");
                params.put("returnCode", "99");
            }*/
        }
        if(optStatus != null){
            sql.append(" ,OPT_STATUS=:optStatus");
            params.put("optStatus", optStatus);
        }
        updatesql.append(sql).append(" WHERE id =:id");
        params.put("id", recordId);
        return createSQLQuery(updatesql.toString(), params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据流水ID修改从表有效状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param recordId  票据流水ID
     * @param validSign 状态
     */
    public int updateVoucherinfoViceValidSignByRecordId(String recordId,
                                                        String validSign) {
        /*
         * Map<String, Object> params=new HashMap<String, Object>();
         * StringBuffer updatesql =new
         * StringBuffer("update AB_VOUCHERINFO_VICE t set"); StringBuffer sql =
         * new StringBuffer(); if(validSign!=null){
         * sql.append(" VALID_SIGN=:validSign");
         * params.put("validSign",validSign); } updatesql.append(sql).append(
         * " WHERE id =(SELECT a.VVID FROM AB_ACCRECORDS a WHERE a.id=:id)");
         * params.put("id",recordId); return
         * createSQLQuery(updatesql.toString(),params).executeUpdate();
         */
        return 1;
    }

    /**  *
     * <b>功能：</b>根据流水ID修改上一条流水的nexttime为空<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-6 <br>
     *
     * @param recordId
     *            票据流水ID
     */
    /*
     * public void updateAccrecordsNexttimeByRecordId(Long recordId) { String
     * sql =
     * "update E_Draft_ACCRECORDS t set t.nexttime=null where t.record_id=" +
     * "(select d.lastid from E_Draft_ACCRECORDS d where d.record_id="
     * +recordId+")";
     *
     * this.updateDelBySql(sql); }
     */

    /**  *
     * <b>功能：</b>根据流水ID修改上一条流水的status为撤回状态-3<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-8 <br>
     *
     * @param recordId
     *            票据流水ID
     */
    /*
     * public void updateAccrecordsStatusByRecordId(Long recordId) { String sql
     * =
     * "update E_Draft_ACCRECORDS set status=-3 where record_id=( select t.lastid from E_Draft_ACCRECORDS t where t.record_id="
     * +recordId+")"; this.updateDelBySql(sql); }
     */

    /**  *
     * <b>功能：</b>根据票据ID流水ID修改票据表的bill_status值，由流水表的rpdmk决定是买断式还是回购式<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
     *
     * @param voucherId 票据ID
     * @param recordId  票据流水ID
     * @param status    签收状态 1:待签收 2：已签收
     */
    @SuppressWarnings("unchecked")
    public int updateVoucherInfoBillStatus(String voucherId, String recordId,
                                           Integer status, String newStmIssuofpprInd, String ebillBlCrclIndc) {

        String billStatus = "";
        String blBsnTpcd = "";
        String rpdmk = this.getDiscountRpdmkByRecordId(recordId);
        BillAbVoucherinfo billAbVoucherinfo = null;
        if(StringUtil.isBlank_new(newStmIssuofpprInd) || StringUtil.isBlank_new(ebillBlCrclIndc)){
            billAbVoucherinfo = billVoucherInfoDao.getBillAbVoucherinfoById(voucherId);
            if(billAbVoucherinfo == null){
                throw new RuntimeException("updateVoucherInfoBillStatus -- 无法找到票据信息");
            }
            newStmIssuofpprInd = billAbVoucherinfo.getNewStmIssuofpprInd();
            ebillBlCrclIndc = billAbVoucherinfo.getEbillBlCrclIndc();
        }

        if (StringUtil.isNotEmpty(rpdmk)) {
            if (status == 1) {
                if (rpdmk.equals("RM00")) {
                    billStatus = "110101";
                    if("1".equals(newStmIssuofpprInd)){
                        billStatus = AccebillConstant.NEWBILL_STATUS_CS03;
                        ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0302;
                        blBsnTpcd = AccebillConstant.NEW_DRAFT_TYPE_BC08;
                    }
                } else if (rpdmk.equals("RM01")) {
                    billStatus = "110201";
                    if("1".equals(newStmIssuofpprInd)){
                        billStatus = AccebillConstant.NEWBILL_STATUS_CS03;
                        ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0302;
                        blBsnTpcd = AccebillConstant.NEW_DRAFT_TYPE_BC09;
                    }
                }
            }
            if (status == 2) {
                if (rpdmk.equals("RM00")) {
                    billStatus = "110106";
                    if("1".equals(newStmIssuofpprInd)){
                        billStatus = AccebillConstant.NEWBILL_STATUS_CS03;
                        ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0303.equals(ebillBlCrclIndc) ? AccebillConstant.CIRCULATE_TF0303 : AccebillConstant.CIRCULATE_TF0301;
                        blBsnTpcd = AccebillConstant.NEW_DRAFT_TYPE_BC08;
                    }
                } else if (rpdmk.equals("RM01")) {
                    billStatus = "110206";
                    if("1".equals(newStmIssuofpprInd)){
                        billStatus = AccebillConstant.NEWBILL_STATUS_CS03;
                        ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0305;
                        blBsnTpcd = AccebillConstant.NEW_DRAFT_TYPE_BC09;
                    }
                }
            }
        }
        String sql = "update AB_VOUCHERINFO set bill_status=(:billStatus) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billStatus", billStatus);
        params.put("voucherId", voucherId);
        if(!StringUtil.isBlank(blBsnTpcd)){
            sql = sql + " ,BL_BSN_TPCD=(:blBsnTpcd)";
            params.put("blBsnTpcd", blBsnTpcd);
        }
        if(!StringUtil.isBlank(ebillBlCrclIndc)){
            sql = sql + " ,EBILL_BL_CRCL_INDC=(:ebillBlCrclIndc)";
            params.put("ebillBlCrclIndc", ebillBlCrclIndc);
        }
        sql = sql + " where id=:voucherId";
        return createSQLQuery(sql, params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据票据流水表id获取贴现种类<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     *
     * @param recordId 票据流水表id
     * @return String 贴现种类
     */
    public String getDiscountRpdmkByRecordId(String recordId) {
        String rpdmk = null;
        StringBuffer sqlselect = new StringBuffer();
        sqlselect
                .append("select t.rpdmk from AB_DISCOUNT t where t.record_id=:recordId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("recordId", recordId);
        List<Map<String, Object>> objStr = this.listBySQL(sqlselect.toString(),
                params);
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = (Map) object;
                rpdmk = ary.get("RPDMK").toString();
            }
        }
        return rpdmk;
    }

    /**  *
     * <b>功能：</b>根据票据ID流水ID修改票据表的bill_status值，由流水表的rcrs_tp决定是拒付追索还是非拒付追索<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
     *
     * @param voucherId 票据ID
     * @param recordId  票据流水ID
     * @param newStmIssuofpprInd  票据流水ID
     */
    @SuppressWarnings("unchecked")
    public int updateVoucherInfoBillStatusByRcrsTp(String voucherId,
                                                   String recordId, String newStmIssuofpprInd, Date endDate) {
        String billStatus = "";
        String ebillBlCrclIndc = "";
        String blBsnTpcd = "";
        Date nowDate = DateUtils.getDateYMD();
        BillAbAccrecords acc = this.getAccrecordsByRecordId(recordId);

        BillAbVoucherinfo billAbVoucherinfo = null;
        if(StringUtil.isBlank_new(newStmIssuofpprInd) || endDate == null){
            billAbVoucherinfo = billVoucherInfoDao.getBillAbVoucherinfoById(voucherId);
        }
        if(billAbVoucherinfo == null){
            throw new RuntimeException("updateVoucherInfoBillStatus -- 无法找到票据信息");
        }

        newStmIssuofpprInd = billAbVoucherinfo.getNewStmIssuofpprInd();
        endDate = billAbVoucherinfo.getEndDate();

        if (acc != null) {
            if (StringUtil.isNotEmpty(acc.getRcrsTp())) {
                if (acc.getRcrsTp().equals("RT00")) {
                    if("1".equals(newStmIssuofpprInd)){
                        billStatus = AccebillConstant.NEWBILL_STATUS_CS03;
                        ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0402;
                        blBsnTpcd = AccebillConstant.NEW_DRAFT_TYPE_BC14;
                    }else {
                        billStatus = "220607";
                    }
                } else if (acc.getRcrsTp().equals("RT01")) {
                    if("1".equals(newStmIssuofpprInd)){
                        endDate = DateUtils.getDateYMD(endDate);
                        //判断是否到期  未到期  AccebillConstant.NEWBILL_STATUS_CS03  已到期  AccebillConstant.NEWBILL_STATUS_CS04
                        billStatus = (endDate.getTime() > nowDate.getTime()) ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.NEWBILL_STATUS_CS04;
                        ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0402;
                        blBsnTpcd = AccebillConstant.NEW_DRAFT_TYPE_BC15;
                    }else {
                        billStatus = "220707";
                    }
                }
            }
        }
        String sql = "update AB_VOUCHERINFO set bill_status=(:billStatus),BL_BSN_TPCD=(:blBsnTpcd),EBILL_BL_CRCL_INDC=(:ebillBlCrclIndc)  where id=:voucherId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billStatus", billStatus);
        params.put("voucherId", voucherId);
        params.put("ebillBlCrclIndc", ebillBlCrclIndc);
        params.put("blBsnTpcd", blBsnTpcd);
        return createSQLQuery(sql, params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据票据ID流水ID修改票据表的bill_status值，由流水表的RcrsTp决定是拒付追索同意清偿还是非拒付追索同意清偿<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-8 <br>
     *
     * @param voucherId 票据ID
     * @param recordId  票据流水ID
     * @param status    签收状态 1:待签收 2：已签收
     * @param newStmIssuofpprInd   新旧票标志
     * @param blBsnTpcd    新票业务代码
     * @param endDate    票据结束时间
     * @param blReqUnnIdNo    申请人社会统一信用代码
     * @param blAcptrUnnIdNo    承兑人社会统一信用代码
     * @param blRmtrUnnIdNo    出票人社会统一信用代码
     */
    @SuppressWarnings("unchecked")
    public int updateVoucherInfoBillStatusByRcrsTp(String voucherId, String recordId, Integer status, String newStmIssuofpprInd,
                                                   String blBsnTpcd, Date endDate, String blReqUnnIdNo, String blAcptrUnnIdNo, String blRmtrUnnIdNo) {

        String billStatus = "";
        String ebillBlCrclIndc = "";//流通标志
        BillAbAccrecords acc = this.getAccrecordsByRecordId(recordId);
        Date nowDate = DateUtils.getDateYMD();

        BillAbVoucherinfo billAbVoucherinfo = null;
        if(StringUtil.isBlank_new(blRmtrUnnIdNo) || StringUtil.isBlank_new(blAcptrUnnIdNo) || StringUtil.isBlank_new(blReqUnnIdNo)
                || StringUtil.isBlank_new(blBsnTpcd) || StringUtil.isBlank_new(newStmIssuofpprInd) || endDate == null){
            billAbVoucherinfo = billVoucherInfoDao.getBillAbVoucherinfoById(voucherId);
        }
        if(billAbVoucherinfo == null){
            throw new RuntimeException("updateVoucherInfoBillStatus -- 无法找到票据信息");
        }

        newStmIssuofpprInd = billAbVoucherinfo.getNewStmIssuofpprInd();
        endDate = billAbVoucherinfo.getEndDate();
        blBsnTpcd = billAbVoucherinfo.getBlBsnTpcd();
        blReqUnnIdNo = acc.getReqSocNo();
        blAcptrUnnIdNo = billAbVoucherinfo.getBlAcptrUnnIdNo();
        blRmtrUnnIdNo = billAbVoucherinfo.getBlRmtrUnnIdNo();

        //新票逻辑
        if("1".equals(newStmIssuofpprInd)){
            if (acc != null && StringUtil.isNotEmpty(acc.getRcrsTp())) {
                blBsnTpcd = blBsnTpcd == null ? AccebillConstant.NEW_DRAFT_TYPE_BC16 : blBsnTpcd;
                if (status == 1 && acc.getRcrsTp().equals("RT00") && endDate != null) {
                    billStatus = AccebillConstant.NEWBILL_STATUS_CS04;
                    ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0402;
                } else if (status == 1 && acc.getRcrsTp().equals("RT01")) {
                    endDate = DateUtils.getDateYMD(endDate);
                    //判断是否到期  未到期  AccebillConstant.NEWBILL_STATUS_CS03  已到期  AccebillConstant.NEWBILL_STATUS_CS04
                    billStatus = (endDate.getTime() > nowDate.getTime()) ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.NEWBILL_STATUS_CS04;
                    ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0402;
                }else if (status == 2 && acc.getRcrsTp().equals("RT00")) {
                    if((blReqUnnIdNo != null && blAcptrUnnIdNo != null && blRmtrUnnIdNo != null)
                            && (blReqUnnIdNo.equals(blAcptrUnnIdNo) || blReqUnnIdNo.equals(blRmtrUnnIdNo))){
                        billStatus = AccebillConstant.NEWBILL_STATUS_CS06;
                        ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0501;
                    }else if((blReqUnnIdNo != null && blAcptrUnnIdNo != null && blRmtrUnnIdNo != null)
                            && !(blReqUnnIdNo.equals(blAcptrUnnIdNo) || blReqUnnIdNo.equals(blRmtrUnnIdNo))){
                        billStatus = AccebillConstant.NEWBILL_STATUS_CS04;
                        ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0402;
                    }
                } else if (status == 2 && acc.getRcrsTp().equals("RT01")) {
                    if((blReqUnnIdNo != null && blAcptrUnnIdNo != null && blRmtrUnnIdNo != null)
                            && (blReqUnnIdNo.equals(blAcptrUnnIdNo) || blReqUnnIdNo.equals(blRmtrUnnIdNo))){
                        billStatus = AccebillConstant.NEWBILL_STATUS_CS06;
                        ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0501;
                    }else if((blReqUnnIdNo != null && blAcptrUnnIdNo != null && blRmtrUnnIdNo != null)
                            && !(blReqUnnIdNo.equals(blAcptrUnnIdNo) || blReqUnnIdNo.equals(blRmtrUnnIdNo))){
                        billStatus = (endDate.getTime() > nowDate.getTime()) ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.NEWBILL_STATUS_CS04;
                        //billStatus = AccebillConstant.NEWBILL_STATUS_CS04;
                        ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0402;
                    }
                }
            }
        } else {
            if (acc != null && StringUtil.isNotEmpty(acc.getRcrsTp())) {
                if (status == 1 && acc.getRcrsTp().equals("RT00")) {
                    billStatus = "230601";
                } else if (status == 1 && acc.getRcrsTp().equals("RT01")) {
                    billStatus = "230701";
                }else if (status == 2 && acc.getRcrsTp().equals("RT00")) {
                    billStatus = "230606";
                } else if (status == 2 && acc.getRcrsTp().equals("RT01")) {
                    billStatus = "230706";
                }
            }
        }
        String sql = "update AB_VOUCHERINFO set bill_status=(:billStatus)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billStatus", billStatus);
        params.put("voucherId", voucherId);
        if(StringUtils.isNotBlank(ebillBlCrclIndc)){
            sql = sql + ",EBILL_BL_CRCL_INDC=(:ebillBlCrclIndc)";
            params.put("ebillBlCrclIndc",ebillBlCrclIndc);
        }
        if(StringUtils.isNotBlank(blBsnTpcd)){
            sql = sql + ",BL_BSN_TPCD=(:blBsnTpcd)";
            params.put("blBsnTpcd",blBsnTpcd);
        }
        sql = sql + "  where id=:voucherId";
        return createSQLQuery(sql, params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据流水ID及票据ID把业务流水的LASTBILLSTATUS的值赋值到票据表的bill_status<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-6 <br>
     *
     * @param recordId 票据流水ID
     */
    public int updateVoucherInfoBillStatus(String recordId, String voucherId) {
        BillAbAccrecords acc = this.getAccrecordsByRecordId(recordId);
        String billStatus = "";
        if (acc != null) {
            billStatus = acc.getLastbillcode();
            voucherId = acc.getVid();
        }
        if (StringUtil.isEmpty(billStatus)) {
            return 0;
        }
        String sql = "update AB_VOUCHERINFO set bill_status=:lastbillcode,EBILL_BL_CRCL_INDC=:lastCirculateCode,BL_BSN_TPCD=:lastBlBsnTpcd  where id=:voucherId and end_date>=:todayDate ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("lastbillcode", billStatus);
        params.put("lastCirculateCode", acc.getLastCirculateCode() == null ? "" : acc.getLastCirculateCode());
        params.put("lastBlBsnTpcd", acc.getLastBlBsnTpcd() == null ? "" : acc.getLastBlBsnTpcd());
        params.put("voucherId", voucherId);
        params.put("todayDate", new Date());
        if (logger.isDebugEnabled()) {
            logger.debug("params[lastbillcode=" + params.get("lastbillcode")
                    + ",voucherId=" + params.get("voucherId") + ",todayDate="
                    + params.get("todayDate") + "]");
            logger.debug("sql=" + sql);
        }
        return createSQLQuery(sql, params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据流水ID及票据ID把业务流水的LASTSYS_SUBJECT的值赋值到票据表的sys_Subject<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
     *
     * @param recordId
     *            票据流水ID
     */
    /*
     * public void updateVoucherInfoSysSubject(Long recordId,Long voucherId) {
     * String sql =
     * "update E_Draft_VOUCHERINFO t set t.sys_Subject=(select a.LASTSYS_SUBJECT from E_Draft_ACCRECORDS a where a.record_id="
     * +recordId+") where t.voucher_id="+voucherId; this.updateDelBySql(sql); }
     */

    /**  *
     * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-6 <br>
     *
     * @param recordId 流水ID
     * @param reCode   银行返回代码
     * @param reMsg    银行返回消息
     */
    @SuppressWarnings("unchecked")
    public int updateAccrecordsReCodeAndReMsgByRecordId(String recordId,
                                                        String reCode, String reMsg) {
        return updateAccrecordsReCodeAndReMsgByRecordId(recordId, reCode,
                reMsg, null);
    }

    /**  *
     * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-6 <br>
     *
     * @param recordId 流水ID
     * @param reCode   银行返回代码
     * @param reMsg    银行返回消息
     * @param billId   银行返回Id
     */
    @SuppressWarnings("unchecked")
    public int updateAccrecordsReCodeAndReMsgByRecordId(String recordId,
                                                        String reCode, String reMsg, String billId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer updatesql = new StringBuffer("update AB_ACCRECORDS set");
        StringBuffer sql = new StringBuffer();
        sql.append(" update_date=:updateDate ");
        params.put("updateDate", new Date());
        if (reCode != null) {
            sql.append(",RETURN_CODE=:returnCode");
            params.put("returnCode", reCode);
        }

        if (reMsg != null) {
            sql.append(",RETURN_MSG=:returnMsg");
            params.put("returnMsg", reMsg);
        }

        if (billId != null) {
            sql.append(",BILL_ID=:billId");
            params.put("billId", billId);
        }
        if (sql.length() == 0) {
            return 0;
        }
        updatesql.append(sql).append(" WHERE id =:id");
        params.put("id", recordId);
        return createSQLQuery(updatesql.toString(), params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>根据流水ID获取流水数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-13 <br>
     *
     * @param RecordId 流水ID
     */
    @SuppressWarnings("unchecked")
    public BillAbAccrecords getAccrecordsByRecordId(String RecordId) {
        String hql = "FROM BillAbAccrecords a WHERE a.id=:id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", RecordId);
        List<BillAbAccrecords> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**  *
     * <b>功能：</b>根据票据ID获取票据数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-13 <br>
     *
     * @param VoucherId 票据ID
     */
    @SuppressWarnings("unchecked")
    public BillAbVoucherinfo getVoucherInfoByVoucherId(String VoucherId) {
        String hql = "FROM BillAbVoucherinfo a WHERE a.id=:id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", VoucherId);
        List<BillAbVoucherinfo> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    public List<BillAbVoucherinfo> getVoucherInfoListByOldDraftId(String oldDraftId) {
        String hql = "FROM BillAbVoucherinfo a WHERE a.oldDraftId=:oldDraftId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("oldDraftId", oldDraftId);
        List<BillAbVoucherinfo> list = this.listByHQL(hql, params);
        return list;
    }

    public List<BillAbVoucherinfo> getVoucherInfoListByDraftId(String[] vids) {
        String hql = "FROM BillAbVoucherinfo a WHERE a.id in(:vids)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("vids", vids);
        List<BillAbVoucherinfo> list = this.listByHQL(hql, params);
        return list;
    }

    public List<BillAbVoucherinfo> getVoucherInfoListByOrigNoAndSbblNo(String origNo,String sbbllRngStrtSn,String sbbllRngEndSn) {
        String hql = "FROM BillAbVoucherinfo a WHERE a.origNo=:origNo and a.sbbllRngStrtSn=:sbbllRngStrtSn and a.sbbllRngEndSn=:sbbllRngEndSn and a.oldDraftId is null order by a.createDate desc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("origNo", origNo);
        params.put("sbbllRngStrtSn", sbbllRngStrtSn);
        params.put("sbbllRngEndSn", sbbllRngEndSn);
        List<BillAbVoucherinfo> list = this.listByHQL(hql, params);
        return list;
    }

    /*
     * public BillAccrecords getAccrecordsByLastId(Long lastId){ String hql =
     * "FROM BillAccrecords a WHERE a.lastid ="+lastId+" AND a.status <> -2";
     * List<BillAccrecords> list = this.searchByHql(hql); if(list != null &&
     * !list.isEmpty()) { return list.get(0); } else { return null; } }
     */

    /**  *
     * <b>功能：</b>根据票据号与签收状态修改签收状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-14 <br>
     *
     * @param origNo    票据号
     * @param status    签收状态
     * @param newStatus 新的签收状态
     */
    @SuppressWarnings("unchecked")
    public int updateSignBillStatus(String origNo, String status,
                                    String newStatus, String msgType,String sbbllRngStrtSn, String sbbllRngEndSn) {
        String sql = " UPDATE AB_BILL_RECEIVEBIZ set STATUS =:newStatus WHERE ORIG_NO =:origNo and sbbll_Rng_Strt_Sn = :sbbllRngStrtSn and sbbll_Rng_end_Sn = :sbbllRngEndSn AND STATUS=:status";
        Map<String, Object> params = new HashMap<String, Object>();
        if (msgType != null) {
            sql = sql + " AND MSGTYPE=:msgType";
            params.put("msgType", msgType);
        }
        params.put("newStatus", newStatus);
        params.put("origNo", origNo);
        params.put("sbbllRngStrtSn", sbbllRngStrtSn);
        params.put("sbbllRngEndSn", sbbllRngEndSn);
        params.put("status", status);
        return createSQLQuery(sql, params).executeUpdate();
    }

    /**  *
     * <b>功能：</b>查询银行账号信息视图<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据信息
     */
    public List<VAccListVo> queryVAccListVo(String bifCode) {
        List<VAccListVo> vAccListVoListNew = new ArrayList<VAccListVo>();
        List<VAccListVo> vAccListVoList = queryVAccListVoNew(bifCode);
        String[] billTypes = {"10", "11"};
        for (VAccListVo info : vAccListVoList) {
            for (String billTyp : billTypes) {
                VAccListVo accBill = new VAccListVo();
                BeanUtils.copyNotNullProperties(info,accBill);
                accBill.setBillType(billTyp);
                accBill.setBankCode(info.getBankCode());
                vAccListVoListNew.add(accBill);
            }
        }
        List<VAccListVo> retrunList = new ArrayList<VAccListVo>();
        String[] newDraftFlgs = new String[]{"0", "1"};
        if(StringUtil.isNotEmpty(BillQueryService.newDraftFlg)){
            newDraftFlgs = new String[]{BillQueryService.newDraftFlg};
        }
        for (VAccListVo info : vAccListVoListNew) {
            for(String flg : newDraftFlgs){
                VAccListVo accBill = new VAccListVo();
                BeanUtils.copyNotNullProperties(info,accBill);
                accBill.setNewDraftFlg(flg);
                retrunList.add(accBill);
                //新票银票添加一类财票
                if("1".equals(flg) && "11".equals(accBill.getBillType())){
                    accBill = new VAccListVo();
                    BeanUtils.copyNotNullProperties(info,accBill);
                    accBill.setNewDraftFlg(flg);
                    accBill.setBillType("99");
                    retrunList.add(accBill);
                }
            }
        }
        return retrunList;
    }

    /**  *
     * <b>功能：</b>查询银行账号信息视图<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据信息
     */
    @SuppressWarnings("unchecked")
    public List<VAccListVo> queryVAccListVoNew(String bifCode) {
        String sql = "select a.bank_acc as bankAcc,"
                + "a.acc_name as accName,"
                // +
                // "(select b.bank_acc from bt_bank_acc b  where b.parent_sign='1' and a.prent_acc_id=b.id) prentAcc,"
                + "a.corp_id as corpId,"
                + "d.cur_code as curCode,"
                + "c.cur_id as curId,"
                + "c.counter_acc as counterAcc,"
                + "c.super_counter_acc as superCounterAcc,"
                + "a.parent_sign as parentSign,"
                + "a.acc_type as accType,"
                + "a.bank_code as bankCode,"
                + "a.bank_name as bankName,"
                + "e.name as prov,"
                + "f.name as city,"
                + "a.private_no as privateNo,"
                + "a.org_code as orgCode,"
                + "b.bif_code as bifCode,"
                + "a.is_online as isOnline,"
                + "a.valid_sign as validSign "
                + "from bt_bank_acc a,bis_bif_init b,bt_bank_acc_cur c,bt_currency d,"
                + "sys_region e ,sys_region f " + "where a.bif_code=b.id "
                + "and a.id=c.bank_acc_id " + "and c.cur_id=d.id "
                + "and a.prov=e.id " + "and a.city=f.id "
                + "and a.electric_bill='1' " + "and a.CIRCULATION_MODE='1' "
//                + "and a.valid_sign='1' and d.cur_name='人民币' and b.bif_code='" + bifCode + "' ";
                + "and a.valid_sign='1' and d.cur_name='人民币' and b.bif_code in (:bifCode) order by b.bif_code,a.bank_acc ";
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("bifCode",StringUtil.split(bifCode,","));
        List<Map<String, Object>> objStr = this.listBySQL(sql, params);
        List<VAccListVo> vAccListVoList = new ArrayList<VAccListVo>();
        vAccListVoList = this.getBeanVAccList(objStr);
        return vAccListVoList;
    }

    /**  *
     * <b>功能：</b>查询银行账号信息视图结果赋值<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
     */
    private List<VAccListVo> getBeanVAccList(List<Map<String, Object>> objStr) {
        List<VAccListVo> list = new ArrayList<VAccListVo>();
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = (Map) object;
                VAccListVo vaccListVo = new VAccListVo();
                vaccListVo.setBankAcc(ary.get("BANKACC") == null ? "" : ary
                        .get("BANKACC").toString().trim());
                vaccListVo.setAccName(ary.get("ACCNAME") == null ? "" : ary
                        .get("ACCNAME").toString().trim());
                // vaccListVo.setPrentAcc(ary.get("PRENTACC") == null ? "" :
                // ary.get("PRENTACC").toString().trim());
                vaccListVo.setCorpId(ary.get("CORPID") == null ? "" : ary
                        .get("CORPID").toString().trim());
                vaccListVo.setCurCode(ary.get("CURCODE") == null ? "" : ary
                        .get("CURCODE").toString().trim());
                vaccListVo.setCurId(ary.get("CURID") == null ? "" : ary
                        .get("CURID").toString().trim());
                vaccListVo.setCounterAcc(ary.get("COUNTERACC") == null ? ""
                        : ary.get("COUNTERACC").toString().trim());
                vaccListVo
                        .setSuperCounterAcc(ary.get("SUPERCOUNTERACC") == null ? ""
                                : ary.get("SUPERCOUNTERACC").toString().trim());
                vaccListVo.setParentSign(ary.get("PARENTSIGN") == null ? ""
                        : ary.get("PARENTSIGN").toString().trim());
                vaccListVo.setAccType(ary.get("ACCTYPE") == null ? "" : ary
                        .get("ACCTYPE").toString().trim());
                vaccListVo.setBankCode(ary.get("BANKCODE") == null ? "" : ary
                        .get("BANKCODE").toString().trim());
                vaccListVo.setBankName(ary.get("BANKNAME") == null ? "" : ary
                        .get("BANKNAME").toString().trim());
                vaccListVo.setProv(ary.get("PROV") == null ? "" : ary
                        .get("PROV").toString().trim());
                vaccListVo.setCity(ary.get("CITY") == null ? "" : ary
                        .get("CITY").toString().trim());
                vaccListVo.setPrivateNo(ary.get("PRIVATENO") == null ? "" : ary
                        .get("PRIVATENO").toString().trim());
                vaccListVo.setOrgCode(ary.get("ORGCODE") == null ? "" : ary
                        .get("ORGCODE").toString().trim());
                vaccListVo.setBifCode(ary.get("BIFCODE") == null ? "" : ary
                        .get("BIFCODE").toString().trim());
                vaccListVo.setIsOnline(ary.get("ISONLINE") == null ? "" : ary
                        .get("ISONLINE").toString().trim());
                vaccListVo.setValidSign(ary.get("VALIDSIGN") == null ? "" : ary
                        .get("VALIDSIGN").toString().trim());
                list.add(vaccListVo);
            }
        }
        return list;
    }

    /**  *
     * <b>功能：</b>查询流水申请人与接收人账号是否是同一单位<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-8-7 <br>
     *
     * @param id
     */
    @SuppressWarnings("unchecked")
    public boolean getReqAndRecCorpIdById(String id) {
        boolean iscorp = false;
        // String sql =
        // "SELECT case　(select b.corp_id from BT_BANK_ACC b WHERE b.bank_acc=t.req_acct_id) 　when 　(select b.corp_id from BT_BANK_ACC b WHERE b.bank_acc=t.rec_acct_id) 　 then 　1　else 　0　END AS iscorp FROM AB_ACCRECORDS t WHERE t.id='"
        // + id + "'";
        String sql = "SELECT CASE (select b.corp_id from BT_BANK_ACC b WHERE b.bank_acc=a.req_acct_id) WHEN (select b.corp_id from BT_BANK_ACC b WHERE b.bank_acc=a.rec_acct_id) THEN '1' ELSE '0' END as iscorp FROM AB_ACCRECORDS a WHERE a.id='"
                + id + "'";
        List<Map<String, Object>> objStr = this.listBySQL(sql, null);
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = (Map) object;
                if ("1".equals(ary.get("ISCORP").toString())) {
                    iscorp = true;
                }
            }
        }
        return iscorp;
    }

    /**  *
     * 用于业务并发缓存处理
     **/
    private static final Map<String, Boolean> billCache = new ConcurrentHashMap<String, Boolean>();

    public boolean isInCache(String key) {
        if (billCache.containsKey(key)) {
            return true;
        } else {
            billCache.put(key, true);
            return false;
        }
    }

    public void removeFromCache(String key) {
        billCache.remove(key);
    }

    /**  *
     * <b>功能：</b>自动签收后处理来自外部的票据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     *
     * @param voucher 票据主信息
     */
    public void autoSignManage(VoucherInfoVo voucher) {
        List<String> noList = NewDraftDealMethod.subTicketIntervalLenLack(voucher.getSbbllRngStrtSn(), voucher.getSbbllRngEndSn());
        voucher.setSbbllRngStrtSn(noList.get(0));
        voucher.setSbbllRngEndSn(noList.get(1));
        String key = voucher.getBriefId() + "_" + voucher.getOrigNo() + "_" + (StringUtils.isBlank(voucher.getSbbllRngStrtSn()) ? "0" : voucher.getSbbllRngStrtSn())
                + "_" + (StringUtils.isBlank(voucher.getSbbllRngEndSn()) ? "0" : voucher.getSbbllRngEndSn()) + "_" + voucher.getAcctId() + "_" + voucher.getBillId();
        if (isInCache(key)) {
            logger.info(key + "，写入持有数据存在并发数据处理不执行数据操作");
            return;
        }
        try {

            logger.info("开始写入持有票据：key=" + key);
            // 判断是否存在本地流水记录
            if (!StringUtil.isBlank_new(voucher.getBriefId())
                    && !StringUtil.isBlank_new(voucher.getAcctId())) {
//					&& !this.getAccrecordsByParameter(voucher.getBriefId(),
//							voucher.getOrigNo(), voucher.getAcctId(),
//							voucher.getBillId())
                //去掉billId条件判断，billId无规则
                List<Map<String, Object>> holdInfo = this.getAccrecordsByParameter(voucher.getBriefId(),
                        voucher.getOrigNo(), voucher.getAcctId(), voucher.getBillId(), voucher.getSbbllRngStrtSn(), voucher.getSbbllRngEndSn());
                //System.out.println("holdInfo="+holdInfo.size());
                if (holdInfo == null || holdInfo.isEmpty()) {
                    // 根据票据号获取票据主表数据
                    BillAbVoucherinfo voucherInfo = this
                            .getVoucherinfoByCode(voucher.getOrigNo(),voucher.getSbbllRngStrtSn(), voucher.getSbbllRngEndSn());
                    if (voucherInfo == null) {
                        //System.out.println("保存票据主表信息，主表为空");
                        // 保存票据主表信息
                        //针对某些银行不返回是否可拆分标志
                        if(StringUtil.isEmpty(voucher.getBlgrdbagcrclPrmtInd()) || "0".equals(voucher.getBlgrdbagcrclPrmtInd())){
                            if(voucher.getSbbllRngEndSn() != null && !"0".equals(voucher.getSbbllRngEndSn()) && voucher.getSbbllRngEndSn().length() > 1){
                                voucher.setBlgrdbagcrclPrmtInd("1");
                            }else {
                                voucher.setBlgrdbagcrclPrmtInd("0");
                            }
                        }
                        this.saveVoucherInfo(voucher);
                        // 保存从表信息
                        this.saveVoucherInfoVice(voucher);
                    } else {
                        //System.out.println("保存票据主表信息，主表不为空");
                        voucher.setVoucherId(voucher.getVoucherId() == null ? voucherInfo
                                .getId() : voucher.getVoucherId());
                        // 根据票据号获取票据主表数据
                        BillAbVoucherinfoVice voucherInfoVice = this
                                .getVoucherinfoVice(voucher);
                        if (voucherInfoVice == null) {
                            // 保存从表信息
                            this.saveVoucherInfoVice(voucher);
                        } else {
                            voucher.setVvid(voucherInfoVice.getId());
                            this.updateVoucherInfoViceBoxStatusByVvid(
                                    voucherInfoVice.getId(),
                                    AccebillConstant.AB_BOX_STATUS_Y);
                        }
                        // 修改票据表信息boxStatus：业务处理完成（11）voucher.getBillStatus(),sys_Subject:单位开票(80080002)
                        this.updateVoucherInfoByVoucherId(voucher.getVoucherId(),
                                null, voucher.getBillStatus(), voucher.getEbillBlCrclIndc(),AccebillConstant.NEW_DRAFT_TYPE_BC02);
                    }
                    // 保存流水信息
                    saveAccrecords(voucher);
                    if (!StringUtil.isBlank_new(voucher.getOrigNo())) {
                        edraftExcBillService.saveEDraftExcBill(
                                voucher.getRecordId(),
                                EDraftConstant.MSG_TYPE_BILLINFO);
                        edraftExcBillService.saveEDraftExcBill(
                                voucher.getRecordId(),
                                EDraftConstant.MSG_TYPE_BACKINFO);
                    }

                    // 针对能查到持有的业务对待签收数据进行已签收处理
//				20210331 取消自动设置已签收操作。避免消息提醒不能自动取消，可以通过定时任务明天早上清理代签收数据
//				if (!StringUtil.isBlank_new(voucher.getBriefId())
//						&& !StringUtil.isBlank_new(voucher.getOrigNo())
//						&& (voucher.getBriefId().equals(
//								EDraftConstant.BUSINESS_9002)
//								|| voucher.getBriefId().equals(
//										EDraftConstant.BUSINESS_9003)
//								|| voucher.getBriefId().equals(
//										EDraftConstant.BUSINESS_9010)
//								|| voucher.getBriefId().equals(
//										EDraftConstant.BUSINESS_9012)
//								|| voucher.getBriefId().equals(
//										EDraftConstant.BUSINESS_9017)
//								|| voucher.getBriefId().equals(
//										EDraftConstant.BUSINESS_9018)
//								|| voucher.getBriefId().equals(
//										EDraftConstant.BUSINESS_9019))) {
//					billSignDao.updateBillSignIsSignOK(voucher.getOrigNo(),
//							voucher.getBriefId().replace("10089", ""),
//							voucher.getAcctId());
//				}
                } else if (StringUtil.isNotEmpty(voucher.getBillId())) {
                    for (Map<String, Object> holdBill : holdInfo) {
                        if (!StringUtil.isBlank_new(String.valueOf(holdBill.get("billid")))) {
                            billAccrecordsDao.updateAccrecordsBillidByBusId(String.valueOf(holdBill.get("id")), voucher.getBillId());
                        }
                    }
                }
                //System.out.println("结束持有票据写入========");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("voucher：" + voucher);
            logger.error("写入持有数据异常：" + e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            removeFromCache(key);
        }
    }

    /**  *
     * <b>功能：</b>根据参数判断是否存在本地流水记录<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     *
     * @param briefId 业务类型
     * @param code    票据号
     * @param acctId  账号
     * @return boolean 是否存在
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAccrecordsByParameter(String briefId, String code,
                                                              String acctId, String billId, String sbbllRngStrtSn, String sbbllRngEndSn) {
        if (StringUtil.isBlank_new(briefId) || StringUtil.isBlank_new(acctId)) {
//			return true;
            return null;
        }
//		boolean isExist = false;
        StringBuffer sqlselect = new StringBuffer();
        Map<String, Object> params = new HashMap<String, Object>();
        sqlselect.append("select z.id vid,l.bill_id as billid,l.id from AB_VOUCHERINFO_VICE c ");
        sqlselect.append(" left join AB_VOUCHERINFO z  on z.id = c.vid ");
        sqlselect.append(" left join AB_ACCRECORDS l on c.id = l.vvid ");
        // 对回头票进行处理 当天出去的票据当天不允许持有回来 关联下一个业务费保证锁定解锁且业务完成
        sqlselect
                .append(" left join AB_ACCRECORDS ll on l.id = ll.lastid and ll.opt_status in ('19','16','18') and ll.brief_id in('010','011','018') ");
        String[] briefIds = {"6001", EDraftConstant.getT2BriefId(briefId)};
        sqlselect.append(" where l.brief_id in (:briefIds) ");
        sqlselect
                .append(" AND ((l.brief_id!='001' and l.opt_status in ('-1','17','18')) or l.brief_id='001' ");
        // 持有数据未出去和对回头票当天出去业务 归属日期等于当天可以出来数据则不能录入
        sqlselect
                .append(" or (l.opt_status='19' and l.status in (95,96) and (ll.id is null or ll.business_date>=:toDay or (ll.opt_status in ('16','18'))) ) ");
        // 针对15和16要排除业务删除的数据
        sqlselect
                .append(" or (l.opt_status in ('15','16') and l.status!=-2 ) ");
        sqlselect.append(" ) ");

        params.put("toDay", DateUtils.getDateYMD());
        params.put("briefIds", briefIds);
        // sqlselect.append(" AND (l.VALID_SIGN in ('0','1') ");
        // sqlselect.append(" or (l.VALID_SIGN in ('2') and ll.id is not null)) ");
        if(!"001".equals(briefId)){
            if (!StringUtil.isBlank_new(code)) {
                sqlselect.append(" AND z.orig_no=:origNo ");
                params.put("origNo", code);
            }
            if (!StringUtil.isBlank_new(sbbllRngStrtSn)) {
                sqlselect.append(" AND z.SBBLL_RNG_STRT_SN=:sbbllRngStrtSn ");
                params.put("sbbllRngStrtSn", sbbllRngStrtSn);
            }
            if (!StringUtil.isBlank_new(sbbllRngEndSn)) {
                sqlselect.append(" AND z.SBBLL_RNG_END_SN=:sbbllRngEndSn ");
                params.put("sbbllRngEndSn", sbbllRngEndSn);
            }
        }
        if (!StringUtil.isBlank_new(billId) && "001".equals(briefId)) {
            sqlselect.append(" AND (l.bill_id=:billId or (l.bill_id is null and l.brief_id!='001' ) ) ");
            params.put("billId", billId);
        }
        sqlselect.append(" AND l.req_acct_id=:acctId ");
        params.put("acctId", acctId);
        List<Map<String, Object>> objStr = this.listBySQL(sqlselect.toString(),
                params);
//		if (objStr != null && !objStr.isEmpty()) {
//			isExist = true;
//		}
//		return isExist;
        return objStr;
    }

    /**  *
     * <b>功能：</b>根据票据号获取票据主表数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     *
     * @param code 票据号
     * @return 票据主信息
     */
    public BillAbVoucherinfo getVoucherinfoByCode(String code,String sbbllRngStrtSn, String sbbllRngEndSn) {
        if (StringUtil.isBlank_new(code) || StringUtil.isBlank_new(sbbllRngStrtSn) || StringUtil.isBlank_new(sbbllRngEndSn)) {
            return null;
        }
        String hql = "FROM BillAbVoucherinfo a WHERE a.origNo=:code";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        if(StringUtil.isNotEmpty(sbbllRngStrtSn)){
            hql = hql + " and sbbllRngStrtSn=:sbbllRngStrtSn";
            params.put("sbbllRngStrtSn", sbbllRngStrtSn);
        }
        if(StringUtil.isNotEmpty(sbbllRngEndSn)){
            hql = hql + " and sbbllRngEndSn=:sbbllRngEndSn";
            params.put("sbbllRngEndSn", sbbllRngEndSn);
        }
        List<BillAbVoucherinfo> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**  *
     * <b>功能：</b>根据票据主信息id和账户获取票据从表数据<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2018-01-02 <br>
     *
     * @param voucher 票据新
     * @return 票据主从表信息
     */
    public BillAbVoucherinfoVice getVoucherinfoVice(VoucherInfoVo voucher) {
        String hql = "FROM BillAbVoucherinfoVice a WHERE a.vid=:vid and a.type=:type and a.bankAcc=:bankAcc ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("vid", voucher.getVoucherId());
        // params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        String briefId = EDraftConstant.getT2BriefId(voucher.getBriefId());
        if (AccebillConstant.BRIEF_STATUS_002.equals(briefId)
                || AccebillConstant.BRIEF_STATUS_003.equals(briefId)) {
            params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
        } else {
            params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        }
        params.put("bankAcc", voucher.getAcctId());
        List<BillAbVoucherinfoVice> list = this.listByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**  *
     * 根据主表id vid 查找锁定（已拆票，拆票中）票据从表信息
     * @param vid
     * @return
     */
    public List<BillAbVoucherinfoVice> getVoucherinfoViceListByVid(String vid) {
        String hql = "FROM BillAbVoucherinfoVice a WHERE a.vid=:vid and validSign = '3'";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("vid", vid);
        // params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        List<BillAbVoucherinfoVice> list = this.listByHQL(hql, params);
        return list;
    }

    /**  *
     * 根据主表id vid 查找有效的票据从表信息
     * @param vid
     * @return
     */
    public List<BillAbVoucherinfoVice> getVoucherinfoViceListByVid_1(String vid) {
        String hql = "FROM BillAbVoucherinfoVice a WHERE a.vid=:vid and validSign = '1'";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("vid", vid);
        // params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        List<BillAbVoucherinfoVice> list = this.listByHQL(hql, params);
        return list;
    }

    public BillAbVoucherinfoVice getVoucherinfoViceListByVvid(String vvid) {
        String hql = "FROM BillAbVoucherinfoVice a WHERE a.id=:vvid ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("vvid", vvid);
        // params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        List<BillAbVoucherinfoVice> list = this.listByHQL(hql, params);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    /**  *
     * 根据主表id vid 查找锁定（已拆票，拆票中）票据从表信息
     * @param vids
     * @return
     */
    public List<BillAbVoucherinfoVice> getVoucherinfoViceListByVids(String[] vids) {
        String hql = "FROM BillAbVoucherinfoVice a WHERE a.vid in (:vids) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("vids", vids);
        // params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        List<BillAbVoucherinfoVice> list = this.listByHQL(hql, params);
        return list;
    }

    /**  *
     * 根据主表id vid 查找96（已拆票，拆票中）票据流水表信息
     * @param vid
     * @return
     */
    public List<BillAbAccrecords> getAbAccrecordsListByVidVvid(String vid,String vvid) {
        String hql = "FROM BillAbAccrecords a WHERE a.status='96' ";
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtil.isNotEmpty(vid)){
            hql = hql + " and a.vid=:vid";
            params.put("vid", vid);
        }
        if(StringUtil.isNotEmpty(vid)){
            hql = hql + " and a.vvid=:vvid";
            params.put("vvid", vvid);
        }
        // params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        List<BillAbAccrecords> list = this.listByHQL(hql, params);
        return list;
    }

    /**  *
     * 根据主表id vid 查找95（已完成）票据流水表信息
     * @param vid
     * @return
     */
    public List<BillAbAccrecords> getAbAccrecordsListByVidVvid_95(String vid,String vvid) {
        String hql = "FROM BillAbAccrecords a WHERE 1=1 ";
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtil.isNotEmpty(vid)){
            hql = hql + " and a.vid=:vid";
            params.put("vid", vid);
        }
        if(StringUtil.isNotEmpty(vid)){
            hql = hql + " and a.vvid=:vvid";
            params.put("vvid", vvid);
        }
        // params.put("type", AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        List<BillAbAccrecords> list = this.listByHQL(hql, params);
        return list;
    }

    /**  *
     * <b>功能：</b>保存票据主表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     *
     * @param voucher 票据信息
     * @return briefId 业务类型
     */
    public void saveVoucherInfo(VoucherInfoVo voucher) {
        /* 封装票据信息Bean BEGIN */
        BillAbVoucherinfo voucherInfo = new BillAbVoucherinfo();
        /*if(StringUtils.isBlank(voucher.getNewStmIssuofpprInd()) || StringUtils.isBlank(voucher.getSbbllRngStrtSn())
                || StringUtils.isBlank(voucher.getSbbllRngEndSn())){
            throw new RuntimeException("method:saveVoucherInfo。ExcInfo:票据信息不完整，请确认！");
        }*/
        //复制属性
        BeanUtils.copyNotNullProperties(voucher,voucherInfo);

        if("1".equals(voucherInfo.getNewStmIssuofpprInd()) && voucherInfo.getBlStdAmt() == null){
            voucherInfo.setBlStdAmt(BigDecimal.valueOf(0.01));
        }
        voucherInfo.setBanendrsmtmk(voucher.getBankendrsmtmk() == null ? voucher.getBanendrsmtmk() : null);
        if (StringUtil.isEmpty(voucherInfo.getBanendrsmtmk())) {
            voucherInfo.setBanendrsmtmk(AccebillConstant.AB_BANEM_Y);
        }
        voucherInfo.setOrigNo(voucher.getOrigNo()); // 票据号码
        voucherInfo.setBillType(voucher.getBillType()); // 票据类型
        voucherInfo.setBanendrsmtmk(voucher.getBanendrsmtmk()); // 转让标识
        voucherInfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
        voucherInfo.setBillStatus(voucher.getBillStatus()); // 票据状态
        if (voucher.getBillFlag() != null) {
            voucherInfo.setBillFlag(voucher.getBillFlag());
        } else {
            voucherInfo.setBillFlag(Integer
                    .valueOf(AccebillConstant.AB_BILL_FLAG_ELECT)); // 电票
        }
        voucherInfo.setBillMoney(voucher.getBillMoney()); // 票据金额
        voucherInfo.setStartDate(voucher.getStartDate()); // 出票日
        voucherInfo.setEndDate(voucher.getEndDate()); // 到期日
        voucherInfo.setPayer(voucher.getPayer());// 出票人
        voucherInfo.setPayeracc(voucher.getPayerAcc()); // 出票人账户
        voucherInfo.setPayerbankno(voucher.getPayerBankNo()); // 出票人开户银行行号
        voucherInfo.setPayerBankName(voucher.getPayerBankName());// 出票人开户银行名称
        voucherInfo.setPayee(voucher.getPayee()); // 收票人全称
        voucherInfo.setPayeeacc(voucher.getPayeeAcc()); // 收票人账号
        voucherInfo.setPayeebankno(voucher.getPayeeBankNo()); // 收票人开户行
        voucherInfo.setPayeeBankName(voucher.getPayeeBankName()); // 收票人开户行名称
        voucherInfo.setAcceptor(voucher.getAcceptor()); // 承兑人
        voucherInfo.setAcceptoracc(voucher.getAcceptorAcc()); // 承兑人账号
        voucherInfo.setAcceptorbankno(voucher.getAcceptorBankNo()); // 承兑人开户行
        voucherInfo.setAcceptorBankName(voucher.getAcceptorBankName());// 承兑行全称
        voucherInfo.setAcceptorbankaddress(voucher.getAcceptorBankAddress());// 承兑人开户行地址
        voucherInfo.setAcontractNo(voucher.getContractNo());// 承兑合同号
        voucherInfo.setAinvcnb(voucher.getInvcnb());// 发票号码
        voucherInfo.setSourceType(EDraftConstant.getSourceTypeByBriefId(voucher
                .getBriefId()));// 票据来源：收票来源
        voucherInfo.setCreateDate(new Date());

        initBankName(voucherInfo);

        if (!StringUtil.isBlank_new(voucher.getAcctId())) {
            // 获取申请人银行信息
            BankAcc bankAcc = getBankAccByAcctId(voucher.getAcctId());
            if (bankAcc != null && voucher.getAcctId().equals(voucher.getPayerAcc())) {
                voucherInfo.setPayer(bankAcc.getAcc_name());
                voucherInfo.setPayerbankno(bankAcc.getBankCode()); // 出票人开户行
                voucherInfo.setPayerBankName(bankAcc.getBankName());// 付款人银行名称
                voucherInfo.setBlRmtrUnnIdNo(bankAcc.getSocCode());
            }

            if (bankAcc != null && voucher.getAcctId().equals(voucher.getAcceptorAcc())) {
                voucherInfo.setAcceptor(bankAcc.getAcc_name()); // 承兑人
                voucherInfo.setAcceptorbankno(bankAcc.getBankCode()); // 承兑人开户行
                voucherInfo.setAcceptorBankName(bankAcc.getBankName());// 承兑行全称
                voucherInfo.setBlAcptrIdTpcd("0".equals(bankAcc.getElectricBill()) ? "DT02" : "DT01");
            }

            if (bankAcc != null && voucher.getAcctId().equals(voucher.getPayeeAcc())) {
                voucherInfo.setPayee(bankAcc.getAcc_name()); // 收款人
                voucherInfo.setPayeebankno(bankAcc.getBankCode()); // 收款人开户行
                voucherInfo.setPayeeBankName(bankAcc.getBankName()); // 收款人开户行名称
                voucherInfo.setBlRcvpymtpsIdTpcd("0".equals(bankAcc.getElectricBill()) ? "DT02" : "DT01");
            }

            // 如果银票承兑人为空时自动把出票人所属行设置为此票据的承兑人
            if (bankAcc != null && voucher.getBillType() == EDraftConstant.BILL_TYPE_10
                    && String.valueOf(voucher.getAcctId()).equals(
                    voucher.getPayerAcc())
                    && StringUtil.isBlank_new(voucher.getAcceptorBankNo())
                    && (StringUtil.isBlank_new(voucher.getAcceptorAcc()) || "0"
                    .equals(voucher.getAcceptorAcc()))) {
                voucherInfo.setAcceptoracc("0");
                voucherInfo.setAcceptor(bankAcc.getBankName());
                voucherInfo.setAcceptorbankno(bankAcc.getBankCode());
                voucherInfo.setAcceptorBankName(bankAcc.getBankName());
            }
        }
        billVoucherInfoDao.save(voucherInfo);
        voucher.setVoucherId(voucherInfo.getId());
    }

    public void initBankName(BillAbVoucherinfo voucherInfo) {
        // 补齐开票人开户行名
        if (StringUtil.isEmpty(voucherInfo.getPayerBankName()) &&
                StringUtil.isNotEmpty(voucherInfo.getPayerbankno())) {
            List<Map<String, Object>> reqObjList = queryBankNameByPaysyBbankCode(voucherInfo
                    .getPayerbankno());
            if (reqObjList != null && !reqObjList.isEmpty()) {
                for (Map ary : reqObjList) {
                    voucherInfo
                            .setPayerBankName(ary.get("BANK_NAME") == null ? ""
                                    : ary.get("BANK_NAME").toString());
                }
            }
        }
        // 补齐收票人开户行名
        if (StringUtil.isEmpty(voucherInfo.getPayeeBankName()) &&
                StringUtil.isNotEmpty(voucherInfo.getPayeebankno())) {
            List<Map<String, Object>> reqObjList = queryBankNameByPaysyBbankCode(voucherInfo
                    .getPayeebankno());
            if (reqObjList != null && !reqObjList.isEmpty()) {
                for (Map ary : reqObjList) {
                    voucherInfo
                            .setPayeeBankName(ary.get("BANK_NAME") == null ? ""
                                    : ary.get("BANK_NAME").toString());
                }
            }
        }
        // 补齐承兑人人开户行名
        if (StringUtil.isEmpty(voucherInfo.getAcceptorBankName()) &&
                StringUtil.isNotEmpty(voucherInfo.getAcceptorbankno())) {
            List<Map<String, Object>> reqObjList = queryBankNameByPaysyBbankCode(voucherInfo
                    .getAcceptorbankno());
            if (reqObjList != null && !reqObjList.isEmpty()) {
                for (Map ary : reqObjList) {
                    voucherInfo
                            .setAcceptorBankName(ary.get("BANK_NAME") == null ? ""
                                    : ary.get("BANK_NAME").toString());
                    if (voucherInfo.getBillType() != null
                            && voucherInfo.getBillType() == EDraftConstant.BILL_TYPE_10) {
                        if (StringUtil
                                .isBlank_new(voucherInfo.getAcceptoracc())) {
                            voucherInfo.setAcceptoracc("0");
                        }
                        if (StringUtil.isBlank_new(voucherInfo.getAcceptor())) {
                            voucherInfo.setAcceptor(voucherInfo
                                    .getAcceptorBankName());
                        }
                    }
                }
            }
        }
    }

    /**  *
     * <b>功能：</b>保存票据从表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     *
     * @param voucher 票据信息
     * @return briefId 业务类型
     */
    public void saveVoucherInfoVice(VoucherInfoVo voucher) {
        /* 封装票据从表信息Bean BEGIN */
        BillAbVoucherinfoVice voucherInfoVice = new BillAbVoucherinfoVice();
        voucherInfoVice.setVid(voucher.getVoucherId());
        // 获取申请人银行信息
        BankAcc bankAcc = getBankAccByAcctId(voucher.getAcctId());
        if (bankAcc != null) {
            voucherInfoVice.setCorpId(bankAcc.getCorp_id());
            voucherInfoVice.setApplyCorpId(bankAcc.getCorp_id());
            voucherInfoVice.setBankAcc(bankAcc.getBankAcc());
            voucherInfoVice.setBankCode(bankAcc.getBankCode());
            voucherInfoVice.setBankName(bankAcc.getBankName());
            voucherInfoVice.setBifCode(bankAcc.getBifId());
        }
        if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(EDraftConstant
                .getSourceTypeByBriefId(voucher.getBriefId()))) {
            voucherInfoVice.setBillSource(String
                    .valueOf(AccebillConstant.AB_BILL_SOURCE_SIGN));
            voucherInfoVice.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);
        } else {
            voucherInfoVice.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
        }
        voucherInfoVice.setInsideSing(Integer
                .valueOf(AccebillConstant.AB_INSIDE_SING_N));
        voucherInfoVice.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
        voucherInfoVice.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
        voucherInfoVice.setRatmodel("0");
        voucherInfoVice.setEnterStock(AccebillConstant.AB_ENTER_STOCK_CORP);
        voucherInfoVice.setCreateDate(new Date());
        billAbVoucherinfoViceDao.save(voucherInfoVice);
        voucher.setVvid(voucherInfoVice.getId());
    }

    /**  *
     * <b>功能：</b>保存流水信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     *
     * @param voucher 票据信息
     * @return briefId 业务类型
     */
    public void saveAccrecords(VoucherInfoVo voucher) {
        if(voucher == null){
            throw new RuntimeException("method:saveAccrecords。 ExcInfo: 入参不能为空");
        }
        BillAbAccrecords accrecordsEntity = new BillAbAccrecords();
        //复制属性
        BeanUtils.copyNotNullProperties(voucher,accrecordsEntity);

        //accrecordsEntity.setBankendrsmtmk(voucher.getBankendrsmtmk());
        if (StringUtil.isEmpty(voucher.getBankendrsmtmk())) {
            accrecordsEntity.setBankendrsmtmk(AccebillConstant.AB_BANEM_Y);
        }else {
            accrecordsEntity.setBankendrsmtmk(voucher.getBankendrsmtmk());
        }
        if (StringUtil.isEmpty(voucher.getBanendrsmtmk())) {
            accrecordsEntity.setBankendrsmtmk(AccebillConstant.AB_BANEM_Y);
        }else {
            accrecordsEntity.setBankendrsmtmk(voucher.getBanendrsmtmk());
        }
        accrecordsEntity.setBillId(voucher.getBillId());
        accrecordsEntity.setVid(voucher.getVoucherId());
        //accrecordsEntity.setVvid(voucher.getVvid());
        accrecordsEntity.setBriefId(EDraftConstant.getT2BriefId(voucher
                .getBriefId()));
        if (AccebillConstant.BRIEF_STATUS_001.equals(accrecordsEntity
                .getBriefId())) {
            accrecordsEntity
                    .setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
        } else {
            accrecordsEntity
                    .setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
        }
        if (voucher.getStatus() != null) {
            accrecordsEntity.setStatus(voucher.getStatus());
            accrecordsEntity.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS16);
        } else {
            accrecordsEntity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
            accrecordsEntity.setOptStatus(String
                    .valueOf(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK));
        }
        accrecordsEntity.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
        accrecordsEntity.setInsideSing(Integer
                .valueOf(AccebillConstant.AB_INSIDE_SING_N));
        accrecordsEntity.setBankendrsmtmk(voucher.getBankendrsmtmk() == null ? voucher.getBanendrsmtmk() : null);
        accrecordsEntity
                .setApplyDate(voucher.getApplyDate() == null ? new Date()
                        : voucher.getApplyDate());
        accrecordsEntity
                .setBusinessDate(voucher.getBussdt() == null ? new Date()
                        : voucher.getBussdt());
        if (AccebillConstant.AB_VOUCHERINFO_OP_TYPE1.equals(EDraftConstant
                .getSourceTypeByBriefId(voucher.getBriefId()))) {
            accrecordsEntity.setSignUpFlag(AccebillConstant.AB_SGNUPMK_Y);
        }
        accrecordsEntity.setEnterStockOld(AccebillConstant.AB_ENTER_STOCK_CORP);
        //accrecordsEntity.setSttlmmk(voucher.getSttlmmk());
        accrecordsEntity.setReturnCode(voucher.getReturnCode() == null ? "3"
                : voucher.getReturnCode());
        //accrecordsEntity.setReturnMsg(voucher.getReturnMsg());
        accrecordsEntity.setReqAcctId(voucher.getAcctId());
        // 获取申请人银行信息
        BankAcc bankAcc = getBankAccByAcctId(voucher.getAcctId());
        if (bankAcc != null) {
            accrecordsEntity.setReqAcctsvcr(bankAcc.getBankCode());
            accrecordsEntity.setReqNm(bankAcc.getAcc_name());
            accrecordsEntity.setBifCode(bankAcc.getBifCode());
            accrecordsEntity.setReqSocNo(bankAcc.getSocCode());
        }
        accrecordsEntity.setRecAcctId(voucher.getBackacctId());
        // 获取业务接收人银行信息
        BankAcc backBankAcc = getBankAccByAcctId(voucher.getBackacctId());
        accrecordsEntity.setRecNm(backBankAcc != null ? backBankAcc
                .getAcc_name() : voucher.getBackAcctName());
        accrecordsEntity.setRecAcctsvcr(backBankAcc != null ? backBankAcc
                .getBankCode() : voucher.getBackacctAcctsvcr());
        accrecordsEntity.setRecSocNo(backBankAcc != null ? backBankAcc.getSocCode() : voucher.getRecSocNo());
        accrecordsEntity.setRecDghType(backBankAcc != null ? ("0".equals(backBankAcc.getElectricBill()) ? "DT02" : "DT01") : voucher.getRecDghType());

        accrecordsEntity.setCreateDate(new Date());
        // 如果业务类型是提示付款已签收的数据则需要判断持有是否存在持有的票据，有则变成以做业务
        if ((AccebillConstant.BRIEF_STATUS_020.equals(accrecordsEntity
                .getBriefId()) || AccebillConstant.BRIEF_STATUS_021
                .equals(accrecordsEntity.getBriefId()))
                && StringUtil.isNotEmpty(voucher.getOrigNo())) {
            List<BillAbAccrecords> listInfo = billAccrecordsDao
                    .getHoldInfo(voucher.getOrigNo(),voucher.getSbbllRngStrtSn(),voucher.getSbbllRngEndSn());
            if (listInfo != null && listInfo.size() > 0) {
                for (BillAbAccrecords info : listInfo) {
                    if (accrecordsEntity.getVvid().equals(info.getVvid())) {
                        accrecordsEntity.setLastid(info.getId());
                        info.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
                        info.setUpdateDate(new Date());
                        billAccrecordsDao.update(info);
                    }
                }
            }
        }
        if (voucher.getBillStatus() != null
                && voucher.getBillStatus().length() == 6
                && "01".equals(voucher.getBillStatus().substring(4))) {
            accrecordsEntity.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS18);
            accrecordsEntity.setReturnCode("1");
        }
        billAccrecordsDao.save(accrecordsEntity);
        billAccrecordsDao.flush();
        if (AccebillConstant.BRIEF_STATUS_002.equals(accrecordsEntity
                .getBriefId())) {
            BillAbAccrecords openEntity = new BillAbAccrecords();
            BeanUtils.copyNotNullProperties(accrecordsEntity, openEntity);
            saveOpenBillAccrecords(openEntity, voucher);
        } else if (AccebillConstant.BRIEF_STATUS_003.equals(accrecordsEntity
                .getBriefId())) {
            BillAbAccrecords aceptEntity = new BillAbAccrecords();
            BeanUtils.copyNotNullProperties(accrecordsEntity, aceptEntity);
            saveAceptBillAccrecords(aceptEntity, voucher);
        }
        voucher.setRecordId(accrecordsEntity.getId());
    }

    //补齐开票申请流水
    public void saveOpenBillAccrecords(BillAbAccrecords openEntity, VoucherInfoVo voucher) {
        openEntity.setBriefId(AccebillConstant.BRIEF_STATUS_001);
        openEntity.setRecAcctId(voucher.getPayeeAcc());
        openEntity.setRecNm(voucher.getPayee());
        openEntity.setRecAcctsvcr(voucher.getPayeeBankNo());
        openEntity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
        openEntity.setOptStatus("19");
        openEntity.setReturnCode("99");
        if (!isCheckAccRecordsByBriefIdVid(openEntity.getBriefId(), openEntity.getVid())) {
            openEntity.setId(null);
            billAccrecordsDao.save(openEntity);
            billAccrecordsDao.flush();
        }
    }

    //补齐提示承兑申请流水
    public void saveAceptBillAccrecords(BillAbAccrecords aceptEntity, VoucherInfoVo voucher) {
        aceptEntity.setBriefId(AccebillConstant.BRIEF_STATUS_002);
        aceptEntity.setRecAcctId(voucher.getAcceptorAcc());
        aceptEntity.setRecNm(voucher.getAcceptor());
        aceptEntity.setRecAcctsvcr(voucher.getAcceptorBankNo());
        aceptEntity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
        aceptEntity.setOptStatus("19");
        aceptEntity.setReturnCode("0");
        if (!isCheckAccRecordsByBriefIdVid(aceptEntity.getBriefId(), aceptEntity.getVid())) {
            aceptEntity.setId(null);
            billAccrecordsDao.save(aceptEntity);
            billAccrecordsDao.flush();
        }
        BillAbAccrecords openEntity = new BillAbAccrecords();
        BeanUtils.copyNotNullProperties(aceptEntity, openEntity);
        saveOpenBillAccrecords(openEntity, voucher);
    }

    /**  *
     * <b>功能：</b>根据账号查询是银行账号信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     *
     * @param AcctId 账号
     */
    public BankAcc getBankAccByAcctId(String AcctId) {
        List<BankAcc> bankAccList = new ArrayList<BankAcc>();
        StringBuffer sql = new StringBuffer();
        sql.append("select t.BANK_ACC,t.BANK_NAME,t.ACC_NAME,t.CORP_ID,t.BANK_CODE,i.id as BIF_ID,i.BIF_CODE, t.ELECTRIC_BILL as electricBill, sc.SOC_CODE socCode ");
        sql.append(" from BT_BANK_ACC t inner join bis_bif_init i on t.bif_code=i.id join SYS_CORP sc on sc.id = t.CORP_ID ");
        sql.append(" WHERE t.bank_acc='").append(AcctId).append("'");
        List<Map<String, Object>> objStr = this.listBySQL(sql.toString(), null);
        bankAccList = this.getBeanByBankAcc(objStr);
        return bankAccList.size() > 0 ? bankAccList.get(0) : null;
    }

    /**  *
     * <b>功能：</b>封装银行账号信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-01-02 <br>
     */
    private List<BankAcc> getBeanByBankAcc(List<Map<String, Object>> objStr) {
        List<BankAcc> list = new ArrayList<BankAcc>();
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = (Map) object;
                BankAcc bankAcc = new BankAcc();
                bankAcc.setBankAcc(ary.get("BANK_ACC") == null ? "" : ary
                        .get("BANK_ACC").toString().trim());
                bankAcc.setBankName(ary.get("BANK_NAME") == null ? "" : ary
                        .get("BANK_NAME").toString().trim());
                bankAcc.setAcc_name(ary.get("ACC_NAME") == null ? "" : ary
                        .get("ACC_NAME").toString().trim());
                bankAcc.setCorp_id(ary.get("CORP_ID") == null ? "" : ary
                        .get("CORP_ID").toString().trim());
                bankAcc.setBankCode(ary.get("BANK_CODE") == null ? "" : ary
                        .get("BANK_CODE").toString().trim());
                bankAcc.setBifCode(ary.get("BIF_CODE") == null ? "" : ary
                        .get("BIF_CODE").toString().trim());
                bankAcc.setBifId(ary.get("BIF_ID") == null ? "" : ary
                        .get("BIF_ID").toString().trim());
                list.add(bankAcc);
            }
        }
        return list;
    }

    /**  *
     * <b>功能：</b>根据联行号获取银行名称<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-22<br>
     *
     * @param paysysbankcode 联行号
     * @return 票据信息
     */
    public List<Map<String, Object>> queryBankNameByPaysyBbankCode(
            String paysysbankcode) {
        String sql = "select t.BANK_NAME,t.IBPS_CODE from bt_input_bank_info t where t.SYS_BANK_CODE='"
                + paysysbankcode + "'";
        List<Map<String, Object>> objStr = this.listBySQL(sql, null);
        return objStr;
    }

    /**  *
     * <b>功能：</b>根据所属账户和票据主表id把持有票据设置为已使用添加VALID_SIGN=2值<br>
     * <b>作者：</b>zhengg<br>
     * <b>日期：</b> 2019-11-27 <br>
     *
     * @param acctId 所属账户
     * @param vid    票据主表ID
     */
    public void updateHoldInfo(String acctId, String vid) {
        StringBuffer updatesql = new StringBuffer("update AB_ACCRECORDS set ");
        updatesql.append(" VALID_SIGN=:validSignNew ");
        updatesql.append(" ,update_date=:updateDate ");
        updatesql.append(" WHERE REQ_ACCT_ID=:acctId and vid=:vid and vid=:vid AND OPT_STATUS=:optStatus ");
        updatesql.append(" and VALID_SIGN =:validSignOld ");
        updatesql.append(" and BRIEF_ID in (:briefId) ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("updateDate", new Date());
        params.put("vid", vid);
        params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("acctId", acctId);
        params.put("validSignNew", AccebillConstant.AB_BILL_VALID_SIGN_2);
        params.put("validSignOld", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("briefId", new String[]{
                AccebillConstant.BRIEF_STATUS_9003,
                AccebillConstant.BRIEF_STATUS_9010,
                AccebillConstant.BRIEF_STATUS_023,
                AccebillConstant.BRIEF_STATUS_9012,
                AccebillConstant.BRIEF_STATUS_9018,
                AccebillConstant.BRIEF_STATUS_9019});
        this.batchSQLExecute(updatesql.toString(), params);
    }

    /**  *
     * <b>功能：</b>判断是否存在传递的流水信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-8-7 <br>
     *
     * @param briefId
     * @param vid
     */
    public boolean isCheckAccRecordsByBriefIdVid(String briefId, String vid) {
        boolean iscorp = false;
        String sql = "SELECT ID FROM AB_ACCRECORDS a WHERE a.brief_id=:briefId and a.vid=:vid";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("briefId", briefId);
        params.put("vid", vid);
        List<Map<String, Object>> objStr = this.listBySQL(sql, params);
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = (Map) object;
                if (ary.get("ID") != null && ary.get("ID").toString().length() > 0) {
                    iscorp = true;
                }
            }
        }
        return iscorp;
    }

    /**  *
     * <b>功能：</b>获取同意签收发送待回应的流水数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2021-11-16 <br>
     *
     * @param origNo 票据号
     */
    public String getAccrecordsIdByOrigNoSign(String origNo,String sbbllRngStrtSn, String sbbllRngEndSn) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("select l.id as id, l.brief_id briefId");
        sql.append(" from  AB_VOUCHERINFO_VICE c ")
                .append(" left join AB_VOUCHERINFO z  on z.id = c.vid ")
                .append(" left join AB_ACCRECORDS l on c.id = l.vvid ")
                .append(" where c.valid_sign=:CvalidSign1 ")
                .append(" and l.valid_sign=:LvalidSign1  and l.SEND_SIGN=:sendSign1 ")
                .append(" and l.STATUS= :status95  and l.OPT_STATUS= :optStatus18 ")
                .append(" and l.brief_id in (:briefId)")
                .append(" and l.sign_up_flag= :signUpFlagSU00 ")
                .append(" and z.orig_no=:origNo  ");
        if(StringUtil.isNotEmpty(sbbllRngStrtSn)){
            sql.append(" and z.sbbll_Rng_Strt_Sn=:sbbllRngStrtSn  ");
            params.put("sbbllRngStrtSn", sbbllRngStrtSn);
        }
        if(StringUtil.isNotEmpty(sbbllRngEndSn)){
            sql.append(" and z.sbbll_Rng_End_Sn=:sbbllRngEndSn  ");
            params.put("sbbllRngEndSn", sbbllRngEndSn);
        }
        sql.append(" order by l.apply_date desc");
        params.put("origNo", origNo);
        params.put("LvalidSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("CvalidSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("sendSign1", AccebillConstant.AB_BILL_SEND_SIGN_Y);
        params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("optStatus18", AccebillConstant.AB_BILL_OPT_STATUS18);
        params.put("signUpFlagSU00", AccebillConstant.AB_SGNUPMK_Y);
        params.put("briefId", new String[]{
                AccebillConstant.BRIEF_STATUS_9002,
                AccebillConstant.BRIEF_STATUS_9003,
                AccebillConstant.BRIEF_STATUS_9010,
                AccebillConstant.BRIEF_STATUS_9012,
                AccebillConstant.BRIEF_STATUS_9018,
                AccebillConstant.BRIEF_STATUS_9019});
        List<Map<String, Object>> objStr = this.listBySQL(sql.toString(), params);
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = objStr.get(0);
                if (ary.get("ID") != null && ary.get("ID").toString().length() > 0) {
                    return ary.get("ID").toString() + "," + ary.get("BRIEFID").toString();
                }
            }
        }
        return null;
    }


    /**  *
     * <b>功能：</b>根据模块和参数名查询参数值<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2022-11-07 <br>
     *
     * @param
     */
    @SuppressWarnings("unchecked")
    public String getSysConfigByParam(String type, String name) {
        String sql = "SELECT value  FROM SYS_CONFIG sc  WHERE TYPE =:type and NAME =:name";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("name", name);

        List<Map<String, Object>> objStr = this.listBySQL(sql, params);
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = objStr.get(0);
                if (ary.get("VALUE") != null && ary.get("VALUE").toString().length() > 0) {
                    return ary.get("VALUE").toString();
                }
            }
        }
        return null;
    }

    /**  *
     * <b>功能：</b>根据日期取地点为空、币别为空的日历维护是否为工作日<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2022-11-07 <br>
     *
     * @param
     */
    @SuppressWarnings("unchecked")
    public String getWorkDaySignByTime(Date time) {
        String sql = "select c.work_day_sign as workDaySign from SYS_CALENDAR c where c.country_id is null and c.cur_id is null and c.create_date=bt_to_date('" + StringUtil.formatDate4(time) + "','yyyy-MM-dd')";

        List<Map<String, Object>> objStr = this.listBySQL(sql, null);
        if (objStr != null && !objStr.isEmpty()) {
            for (Object object : objStr) {
                Map ary = (Map) object;
                if (ary.get("WORKDAYSIGN") != null && ary.get("WORKDAYSIGN").toString().length() > 0) {
                    return ary.get("WORKDAYSIGN").toString();
                }
            }
        }
        return null;
    }

    /**  *
     * <b>功能：</b>根据附件组id获取附件路径<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2022-11-21 <br>
     *
     * @param
     */
    @SuppressWarnings("unchecked")
    public String getAttachmentPathByGroupId(String GroupId) {
        String sql = "select t.path as PATH from SYS_ATTACHMENT t where t.group_id='" + GroupId + "' ";
        StringBuilder attachmentPath = new StringBuilder(100);
        List<Map<String, Object>> objStr = this.listBySQL(sql, null);
        if (objStr != null && !objStr.isEmpty()) {
            for (Map ary : objStr) {
                if (ary.get("PATH") != null && ary.get("PATH").toString().length() > 0) {
                    if (attachmentPath.length()>0) {
                        attachmentPath.append(";");
                    }
                    attachmentPath.append(ary.get("PATH").toString());
                }
            }
        }
        return attachmentPath.toString();
    }

    /**
     * 获取拆分并且业务中数据(背书、质押、贴现、质押解除)
     * @param oldAccId 拆分持有流水ID
     * @return
     */
    public List<BillAbAccrecords> listByUnderwaySplit(String oldAccId) {
        if(StringUtil.isEmpty(oldAccId)){
            return new ArrayList<BillAbAccrecords>();
        }
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql=new StringBuilder(" FROM BillAbAccrecords a ");
        sql.append(" where  ((a.briefId in (:briefIds) and (a.status in (:status) or a.optStatus in (:optStatus))) " +
                " or (a.briefId = '6011' and a.status=95 " +
                " and NOT EXISTS(select 1 from BillAbAccrecords b where b.briefId='011' and b.lastid=a.id and b.optStatus in ('18','19','-3','-4')) ))");
        if(StringUtil.isNotEmpty(oldAccId)){
            params.put("oldAccId", oldAccId);
            sql.append(" and a.oldAccId =:oldAccId ");
        }
//        if(StringUtil.isNotEmpty(recordId)){
//            params.put("recordId", recordId);
//            sql.append(" and a.id !=:recordId ");
//        }

        sql.append(" order by a.createDate ");
        params.put("status", new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS11
                , AccebillConstant.AB_BILL_BUS_STATUS94});
        params.put("optStatus", new String[]{
                AccebillConstant.AB_BILL_OPT_STATUS15
                , AccebillConstant.AB_BILL_OPT_STATUS16
                , AccebillConstant.AB_BILL_OPT_STATUS17
//                , AccebillConstant.AB_BILL_OPT_STATUS18
                , AccebillConstant.AB_BILL_OPT_STATUS_1});
        params.put("briefIds", new String[]{
                AccebillConstant.BRIEF_STATUS_010
                , AccebillConstant.BRIEF_STATUS_011
                , AccebillConstant.BRIEF_STATUS_6011
                , AccebillConstant.BRIEF_STATUS_018
                , AccebillConstant.BRIEF_STATUS_019
                , AccebillConstant.BRIEF_STATUS_8018});
        List<BillAbAccrecords> list=this.listByHQL(sql.toString(), params);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<BillAbAccrecords>();
        }
        return list;
    }

    /**
     * 获取拆分并且待发送的数据(背书、质押、贴现、质押解除)
     * @param oldAccId 拆分持有流水ID
     * @return
     */
    public List<BillAbAccrecords> listByAwaitSendSplit(String oldAccId) {
        if(StringUtil.isEmpty(oldAccId)){
            return new ArrayList<BillAbAccrecords>();
        }
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql=new StringBuilder(" FROM BillAbAccrecords a");
        sql.append(" where a.briefId in (:briefIds) and a.status =:status and a.optStatus=:optStatus ");
        sql.append(" and EXISTS(select 1 from BillAbVoucherinfo v where v.id=a.vid and v.sbbllRngStrtSn=' ') ");
        if(StringUtil.isNotEmpty(oldAccId)){
            params.put("oldAccId", oldAccId);
            sql.append(" and (a.oldAccId =:oldAccId or a.id=:oldAccId) ");
//            sql.append(" and a.oldAccId =:oldAccId ");
        }
        sql.append(" order by a.updateDate ");
        params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS16);
        params.put("briefIds", new String[]{
                AccebillConstant.BRIEF_STATUS_010
                , AccebillConstant.BRIEF_STATUS_011
                , AccebillConstant.BRIEF_STATUS_018
                , AccebillConstant.BRIEF_STATUS_019
                , AccebillConstant.BRIEF_STATUS_8018});
        List<BillAbAccrecords> list=this.listByHQL(sql.toString(), params);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<BillAbAccrecords>();
        }
        return list;
    }


    /**
     * 获取已拆分并且可操作的明细数据(收票登记、收票签收、背书签收、质押签收、质押解除签收、贴现赎回签收)
     * @param oldAccId 拆分持有流水ID
     * @return
     */
    public List<BillAbAccrecords> listByAvaidSplit(String oldAccId) {
        return listByAvaidSplit(null,oldAccId);
    }
    /**
     * 获取已拆分并且可操作的明细数据(收票登记、收票签收、背书签收、质押签收、质押解除签收、贴现赎回签收)
     * @param vids 票据主表id list
     * @return
     */
    public List<BillAbAccrecords> listByAvaidSplit(List<String> vids) {
        return listByAvaidSplit(vids,null);
    }

    /**
     * 获取已拆分并且可操作的明细数据(收票登记、收票签收、背书签收、质押签收、质押解除签收、贴现赎回签收)
     * @param vids 票据主表id list
     * @param oldAccId 拆分持有流水ID
     * @return
     */
    public List<BillAbAccrecords> listByAvaidSplit(List<String> vids,String oldAccId) {
        if(CollectionUtils.isEmpty(vids) && StringUtil.isEmpty(oldAccId)){
            return new ArrayList<BillAbAccrecords>();
        }
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql=new StringBuilder(" FROM BillAbAccrecords a ");
        sql.append(" where a.status =:status and a.validSign =:validSign and a.briefId in (:briefIds) and a.optStatus=:optStatus ");
        sql.append(" and EXISTS(select 1 from BillAbVoucherinfoVice vv where vv.id=a.vvid and vv.boxStatus=:boxStatus)  ");
        if(!CollectionUtils.isEmpty(vids)){
            params.put("vids", vids);
            sql.append(" and vid in (:vids) ");
        }
        if(StringUtil.isNotEmpty(oldAccId)){
            params.put("oldAccId", oldAccId);
            sql.append(" and (a.oldAccId =:oldAccId or a.id=:oldAccId) ");
            sql.append(" and EXISTS(select 1 from BillAbVoucherinfo v where v.id=a.vid and v.sbbllRngStrtSn=' ')  ");
//            sql.append(" and oldAccId =:oldAccId ");
        }
        sql.append(" order by a.createDate ");
        params.put("boxStatus", AccebillConstant.AB_BOX_STATUS_Y);
        params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
        params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
        params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
        params.put("briefIds", new String[]{
                AccebillConstant.BRIEF_STATUS_6001
                , AccebillConstant.BRIEF_STATUS_9003
                , AccebillConstant.BRIEF_STATUS_6011
                , AccebillConstant.BRIEF_STATUS_9010
                , AccebillConstant.BRIEF_STATUS_9012
                , AccebillConstant.BRIEF_STATUS_9018});
        List<BillAbAccrecords> list=this.listByHQL(sql.toString(), params);
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<BillAbAccrecords>();
        }
        return list;
    }

    public int batchUpdateBillAbAccrecords(Map<String,Object> params) {
        StringBuilder hql = new StringBuilder("update BillAbAccrecords "
                + " set updateDate = :updateDate ");
        if(!params.containsKey("updateDate")){
            params.put("updateDate",new Date());
        }
        if(params.containsKey("status")){
            hql.append(",status = :status ");
        }
        if(params.containsKey("optStatus")){
            hql.append(",optStatus = :optStatus ");
        }
        if(params.containsKey("returnMsg")){
            hql.append(",returnMsg = :returnMsg ");
        }
        if(params.containsKey("copyAccIdIsNull")){
            hql.append(",copyAccId = null ");
        }
        if(params.containsKey("copyAccIdNew")){
            hql.append(",copyAccId = :copyAccIdNew ");
        }
        hql.append(" where id in (:ids)");
        if(params.containsKey("copyAccIdOld")){
            hql.append(" and copyAccId = :copyAccIdOld ");
        }
        if(params.containsKey("copyAccIdWIsNull")){
            hql.append(" and copyAccId is null ");
        }
        return this.updateDelByHql(hql.toString(),params);
    }

    public int batchUpdateVoucherinfo(Map<String,Object> params) {
        StringBuilder hql = new StringBuilder("update BillAbVoucherinfo "
                + " set updateDate = :updateDate ");
        if(!params.containsKey("updateDate")){
            params.put("updateDate",new Date());
        }
        if(params.containsKey("sbbllRngStrtSnNew")){
            hql.append(",sbbllRngStrtSn = :sbbllRngStrtSnNew ");
        }
        if(params.containsKey("sbbllRngEndSnNew")){
            hql.append(",sbbllRngEndSn = :sbbllRngEndSnNew ");
        }
        if(params.containsKey("orgSbbllRngStrtSnNew")){
            hql.append(",orgSbbllRngStrtSn = :orgSbbllRngStrtSnNew ");
        }
        if(params.containsKey("orgSbbllRngEndSnNew")){
            hql.append(",orgSbbllRngEndSn = :orgSbbllRngEndSnNew ");
        }
        hql.append(" where id in (:ids)");
        if(params.containsKey("sbbllRngStrtSnOld")){
            hql.append(" and sbbllRngStrtSn=:sbbllRngStrtSnOld ");
        }
        if(params.containsKey("sbbllRngEndSnOld")){
            hql.append(" and sbbllRngEndSn=:sbbllRngEndSnOld ");
        }
        if(params.containsKey("orgSbbllRngStrtSnOld")){
            hql.append(" and orgSbbllRngStrtSn=:orgSbbllRngStrtSnOld ");
        }
        return this.updateDelByHql(hql.toString(),params);
    }
}
