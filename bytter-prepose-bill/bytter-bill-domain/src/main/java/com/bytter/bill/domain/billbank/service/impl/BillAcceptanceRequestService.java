package com.bytter.bill.domain.billbank.service.impl;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.dao.BillVoucherInfoDao;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.entity.BillSign;
import com.bytter.bill.domain.billbank.service.BillCommonBaseService;
import com.bytter.bill.domain.billbank.service.IBillAcceptanceRequestService;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.bill.domain.billbank.util.BeanUtils;
import com.bytter.bill.domain.billbank.vo.BillSignVo;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>开票提示承兑业务操作service<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-6-1 <br>
 */
@Service("billAcceptanceRequestService")
@Transactional
public class BillAcceptanceRequestService implements IBillAcceptanceRequestService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BillBankDao billBankDao;
    @Autowired
    private BillSignDao billSignDao;
    @Autowired
    private BillAccrecordsDao billAccrecordsDao;
    @Autowired
    private BillVoucherInfoDao billVoucherInfoDao;
    @Autowired
    private BillCommonBaseService billCommonBaseService;
    /**
     * <b>功能：</b>提示承兑-发起方--查询申请待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillAcceptanceByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_002, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>提示承兑--发起方--指令发送已回应待签收--修改提示承兑流水表状态为已回应待签收及票据表状态为提示承兑待签收<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param recordId  流水ID
     * @param voucherId 票据ID
     * @return
     */
    public void updateVoucherInfo(VoucherInfoVo voucher) {
        BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
        if(oldInfo == null){
            throw new RuntimeException("票据信息不存在！");
        }
        BeanUtils.copyNotNullProperties(oldInfo,voucher);
        //根据票据ID更新票据表信息
        //修改票据表信息bill_status:提示承兑待签收 020001
        //NewStmIssuofpprInd出票标志  1为新系统出票；    SbbllRngStrtSn子票区间   0或者null为旧票
        if ((StringUtils.isNotBlank(voucher.getNewStmIssuofpprInd()) && "1".equals(voucher.getNewStmIssuofpprInd()))) {
            billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, AccebillConstant.NEWBILL_STATUS_CS01,
                    AccebillConstant.CIRCULATE_TF0302, AccebillConstant.NEW_DRAFT_TYPE_BC02);
        } else {
            billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, AccebillConstant.AB_BILL_STATUS_020001,null,null);
        }
        //根据流水ID更新票据流水表信息
        //修改流水表status:已回应待签收 18
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS18, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
    }
    /**
     * <b>功能：</b>提示承兑--根据银行接口代码及银行返回代码查询待签收数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-14 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_002, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS18);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>提示承兑--发起方查询结果--根据银行接口代码和业务标记<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2017-12-25 <br>
     *
     * @param bifCode 银行接口代码
     * @return
     */
    public List<VoucherInfoVo> queryBillAcceptResult(String bifCode) {
        String[] status = {AccebillConstant.AB_BILL_OPT_STATUS19};
        //新票据改造
        String[] billStatus = {AccebillConstant.AB_BILL_STATUS_010004, AccebillConstant.AB_BILL_STATUS_020001, AccebillConstant.AB_BILL_STATUS_020006
                , AccebillConstant.AB_BILL_STATUS_030006, AccebillConstant.AB_BILL_STATUS_030001,AccebillConstant.AB_BILL_STATUS_100001,AccebillConstant.AB_BILL_STATUS_100006};
        //List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_001,bifCode,null,status,billStatus);
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_001, bifCode, null, status, null);
        List<VoucherInfoVo> subList = new ArrayList<VoucherInfoVo>();
        for (VoucherInfoVo vo : voucherInfoList) {
            if("应付票据".equals(vo.getReturnMsg()) || (vo.getLastid() == null && vo.getReturnMsg() != null && vo.getReturnMsg().startsWith("提示承兑正面票据状态交易结果成功"))){
                continue;
            }
            if (Arrays.asList(billStatus).contains(vo.getBillStatus()) || "1".equals(vo.getNewStmIssuofpprInd())) {
                subList.add(vo);
            }
        }
        return subList;
    }
    /**
     * <b>功能：</b>提示承兑-发起方--签收同意完成后修改票据表与流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-1-8 <br>
     *
     * @param voucher 票据信息
     * @return
     */
    public void updateVoucherInfoEndIsSend(VoucherInfoVo voucher) {
        if (logger.isDebugEnabled()) {
            logger.debug("VoucherInfoVo=" + voucher.toString());
        }
        int reNUm = 0;
        BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
        if(oldInfo == null){
            throw new RuntimeException("票据信息不存在！");
        }
        BeanUtils.copyNotNullProperties(oldInfo,voucher);
        //同意签收
        if ("SU00".equals(voucher.getSignUpFlag())) {
            //根据票据ID更新票据表信息
            if ("1".equals(voucher.getNewStmIssuofpprInd())) {
                billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS02,
                        AccebillConstant.CIRCULATE_TF0101,AccebillConstant.NEW_DRAFT_TYPE_BC02);
            } else {
                //修改票据表信息boxStatus：业务处理完成（11）bill_status:提示承兑已签收(020006)
                billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.AB_BILL_STATUS_020006, null, null);
            }
            billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
            //根据流水ID更新票据流水表信息
            //修改流水表status:待提示收票 31
            reNUm = billBankDao.updateAccrecordsInfoByRecordId2(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, AccebillConstant.BRIEF_STATUS_002,
                    voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
            if (reNUm == 0 && StringUtil.isNotEmpty(voucher.getSignUpFlag())) {
                billBankDao.updateAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
            }
        }
        //拒绝签收
        else if ("SU01".equals(voucher.getSignUpFlag())) {
            if ("1".equals(voucher.getNewStmIssuofpprInd())) {
                billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS01,
                        AccebillConstant.CIRCULATE_TF0101,AccebillConstant.NEW_DRAFT_TYPE_BC01);
            } else {
                //根据票据ID更新票据表信息
                //修改票据表信息boxStatus：业务处理中（10）bill_status:出票已登记(010004)
                billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.AB_BILL_STATUS_010004, null,null);
            }
            billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
            //根据流水ID更新票据流水表信息
            //修改流水表status:拒收 -4
            reNUm = billBankDao.updateAccrecordsInfoByRecordId2(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4, AccebillConstant.BRIEF_STATUS_002, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
            if (reNUm == 0) {
                billBankDao.updateAccrecordsInfoByRecordId2(voucher.getRecordId(), null, AccebillConstant.BRIEF_STATUS_001, "2", "承兑拒签", voucher.getBillId());
            }
            //修改前一笔流水为可用
            billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
        }
        if (reNUm == 0 && StringUtil.isNotEmpty(voucher.getSignUpFlag())) {
            saveAccrecords(voucher);
        }
        //根据流水ID更新票据流水表的上个票据状态与上个科目字段
        //billBankDao.updateAccrecordsLastInfoByRecordId(voucher.getRecordId(),AccebillConstant.AB_BILL_STATUS_020006);
    }
    /**
     * <b>功能：</b>提示承兑-签收方--签收同意完成后修改票据表与流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param voucher 票据信息
     * @return
     */
    public void updateVoucherInfoEnd(VoucherInfoVo voucher) {
        if (logger.isDebugEnabled()) {
            logger.debug("VoucherInfoVo=" + voucher.toString());
        }
        @SuppressWarnings("unused")
        int reNUm = 0;
        BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
        if(oldInfo == null){
            throw new RuntimeException("票据信息不存在！");
        }
        BeanUtils.copyNotNullProperties(oldInfo,voucher);
        //同意签收
        if ("SU00".equals(voucher.getSignUpFlag())) {
            //根据票据ID更新票据表信息
            //修改票据表信息boxStatus：业务处理完成（11）bill_status:提示承兑已签收(020006)
            BillAbVoucherinfo voucherInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
            //当票据状态不为收票待签收或收票已签收才更改(当提示承兑已收到成功结果，而签收方返回结果有延迟还没修改状态，导致做了提示收票后提示承兑签收结果才返回修改了票据状态)
            if (voucherInfo != null
                    && !AccebillConstant.AB_BILL_STATUS_030001.equals(voucherInfo.getBillStatus())
                    && !AccebillConstant.AB_BILL_STATUS_030006.equals(voucherInfo.getBillStatus())) {
                billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.AB_BILL_STATUS_020006, null, null);
            }
            //新票据业务处理
            if (voucherInfo != null && AccebillConstant.NEWBILL_STATUS_CS01.equals(voucherInfo.getBillStatus())
                    && AccebillConstant.CIRCULATE_TF0302.equals(voucherInfo.getEbillBlCrclIndc())) {
                billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS02,
                        AccebillConstant.CIRCULATE_TF0101, AccebillConstant.NEW_DRAFT_TYPE_BC02);
            }
            billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
            //根据流水ID更新票据流水表信息
            //修改流水表status:待提示收票 31
            reNUm = billBankDao.updateAccrecordsInfoByRecordId2(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        }
        //拒绝签收
        else if ("SU01".equals(voucher.getSignUpFlag())) {
            if ("1".equals(voucher.getNewStmIssuofpprInd())) {
                //新票据业务处理
                billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS01,
                        AccebillConstant.CIRCULATE_TF0101, AccebillConstant.NEW_DRAFT_TYPE_BC01);
            }else {
                //根据票据ID更新票据表信息
                //修改票据表信息boxStatus：业务处理中（10）bill_status:出票已登记(010004)
                billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.AB_BILL_STATUS_010004, null,null);
            }
            billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
            //根据流水ID更新票据流水表信息
            //修改流水表status:拒收 -4
            reNUm = billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
            //修改前一笔流水为可用
            billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
        }
	/*	if(reNUm==0&& StringUtils.isNotEmpty(voucher.getSignUpFlag())){
			saveAccrecords(voucher);
		}*/
        //根据流水ID更新票据流水表的上个票据状态与上个科目字段
        //billBankDao.updateAccrecordsLastInfoByRecordId(voucher.getRecordId(),AccebillConstant.AB_BILL_STATUS_020006);
    }
    /**
     * <b>功能：</b>提示承兑-发起方--签收方拒签后修改票据表与流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param voucher 票据信息
     * @return
     */
    public void updateVoucherInfoRejection(VoucherInfoVo voucher) {
        BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
        if(oldInfo == null){
            throw new RuntimeException("票据信息不存在！");
        }
        if("1".equals(voucher.getNewStmIssuofpprInd())){
            billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS01,
                    AccebillConstant.CIRCULATE_TF0101,AccebillConstant.NEW_DRAFT_TYPE_BC01);
        }else {
            //根据票据ID更新票据表信息
            //修改票据表信息boxStatus：业务处理中（10）bill_status:出票已登记(010004)
            billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.AB_BILL_STATUS_010004,null,null);
        }
        billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
        //根据流水ID更新票据流水表信息  修改流水表status:拒收 -4,判断returnCode是否为撤回，是则为撤回
        String optStatus = AccebillConstant.AB_BILL_OPT_STATUS_4;
        if (AccebillConstant.AB_BILL_OPT_STATUS_3.equals(voucher.getReturnCode())) {
            optStatus = AccebillConstant.AB_BILL_OPT_STATUS_3;
        }
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), optStatus, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改前一笔流水为可用
        billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
    }
    /**
     * <b>功能：</b>提示承兑-签收方--查询待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillAcceptanceReplyByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_9002, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>提示承兑-签收方--查询已回应待签收的票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-04-26 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillAcceptanceReplyAwaitByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_9002, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS18);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>提示承兑-签收方签收--根据签收回复流水ID修改流水表状态为已签收<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param recordId 签收回复流水ID
     * @return
     */
    public void updateAccrecordsStatusByRecordId(VoucherInfoVo voucher) {
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改从表为无效
        //billBankDao.updateVoucherinfoViceValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
    }
    /**
     * <b>功能：</b>提示承兑-签收方拒收--根据拒收回复流水ID修改流水表状态为已拒收状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-1 <br>
     *
     * @param recordId 拒收回复流水ID
     * @return
     */
    public void updateAccrecordsStatusRejectionByRecordId(VoucherInfoVo voucher) {
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改从表为无效
        billBankDao.updateVoucherinfoViceValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
    }
    /**
     * <b>功能：</b>提示承兑-根据银行接口传过来的票据信息及状态修改票据及流水信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-5 <br>
     *
     * @param VoucherInfoVo 票据信息
     * @param status        状态
     *                      1：发起方--指令发送已回应待签收--票据ID,流水ID
     *                      2：发起方--签收同意完成后修改票据表与流水表信息--票据ID,流水ID
     *                      3：发起方--签收方拒签后修改票据表与流水表信息--票据ID,流水ID
     *                      4：签收方--签收方签收--签收回复流水ID
     *                      5：签收方--签收方拒收--拒收回复流水ID
     */
    public void upateVoucherAndAccrecordsByVoucherInfoAndStatus(
            VoucherInfoVo voucher, int status) {
        switch (status) {
            //发起方--指令发送已回应待签收
            case 1:
                updateVoucherInfo(voucher);
                //发起方--签收同意完成后修改票据表与流水表信息
            case 2:
                updateVoucherInfoEnd(voucher);
                //发起方--签收方拒签后修改票据表与流水表信息
            case 3:
                updateVoucherInfoRejection(voucher);
                //签收方--签收方签收
            case 4:
                updateAccrecordsStatusByRecordId(voucher);
                //签收方--签收方拒收
            case 5:
                updateAccrecordsStatusRejectionByRecordId(voucher);
            default:
                break;
        }
    }
    /**
     * <b>功能：</b>根据流水ID更新票据流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-5 <br>
     *
     * @param recordId   票据流水ID
     * @param status     操作状态
     * @param briefId    业务类型
     * @param returnCode 返回代码
     * @param returnMsg  返回消息
     */
    public void updateAccrecordsInfoByRecordId(String recordId, Integer status,
                                               String briefId, String returnCode, String returnMsg) {
        if (logger.isDebugEnabled()) {
            logger.debug("recordId=" + recordId + ",status=" + status + ",briefId=" + briefId + ",returnCode=" + returnCode + ",returnMsg=" + returnMsg);
        }
        billBankDao.updateAccrecordsInfoByRecordId(recordId, status == null ? null : String.valueOf(status), briefId, returnCode, returnMsg, null);
    }
    /**
     * <b>功能：</b>根据票据ID更新票据表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-5 <br>
     *
     * @param voucherId  票据ID
     * @param boxStatus  处理标记
     * @param billStatus 票据状态
     * @param sysSubject 系统科目
     * @param ebillBlCrcLindc 流通标志
     * @param blBsnTpcd 新票据票据业务类型
     */
    public void updateVoucherInfoByVoucherId(String voucherId, Integer boxStatus,
                                             String billStatus, Long sysSubject, String ebillBlCrcLindc, String blBsnTpcd) {
        billBankDao.updateVoucherInfoByVoucherId(voucherId, boxStatus, billStatus,ebillBlCrcLindc, blBsnTpcd);
    }
    /**
     * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-6 <br>
     *
     * @param recordId 流水ID
     * @param reCode   银行返回代码
     * @param reMsg    银行返回消息
     */
    public void updateAccrecordsReCodeAndReMsgByRecordId(String recordId,
                                                         String reCode, String reMsg) {
        billBankDao.updateAccrecordsReCodeAndReMsgByRecordId(recordId, reCode, reMsg);
    }
    /**
     * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-6 <br>
     *
     * @param recordId 流水ID
     * @param reCode   银行返回代码
     * @param reMsg    银行返回消息
     * @param billId   银行返回消息
     */
    public void updateAccrecordsReCodeAndReMsgByRecordId(String recordId,
                                                         String reCode, String reMsg, String billId) {
        billBankDao.updateAccrecordsReCodeAndReMsgByRecordId(recordId, reCode, reMsg, billId);
    }
    /**
     * <b>功能：</b>新增签收表数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
     *
     * @param billSign 签收表实体
     */
    public synchronized void saveBillSign(BillSignVo billSign) {
        if (logger.isDebugEnabled()) {
            logger.debug("BillSign=" + billSign.toString());
        }
        if(StringUtil.isBlank_new(billSign.getNewStmIssuofpprInd())){
            if("1,2,3".contains(billSign.getOrigNo().substring(0,1))){
                billSign.setNewStmIssuofpprInd("0");
            }else {
                billSign.setNewStmIssuofpprInd("1");
            }
        }
        billCommonBaseService.saveBillSign(billSign);
    }
    /**
     * <b>功能：</b>查询银行账号信息视图<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
     *
     * @param bifCode 银行接口代码
     * @return 票据信息
     */
    public List<VAccListVo> queryVAccListVo(String bifCode) {
        List<VAccListVo> vAccListVoList = billBankDao.queryVAccListVo(bifCode);
        return vAccListVoList;
    }
    /**
     * <b>功能：</b>根据serialId查询签收表数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-21 <br>
     *
     * @param msgId 签收表实体
     */
    public List<BillSignVo> queryBillSignListBySerialId(String msgId) {
        List<BillSign> billSignList = billSignDao.queryBillSignListBySerialId(msgId);
        List<BillSignVo> reList = BeanUtil.copyListProperties(billSignList, BillSignVo.class);
        return reList;
    }
    public void saveAccrecords(VoucherInfoVo voucher) {
        BillAbVoucherinfo voucherInfo = billVoucherInfoDao.get(voucher.getVoucherId());
        String recordId = billAccrecordsDao.getAccrecordsInfoByVoucherIdAndBriefId(voucher.getVoucherId(), AccebillConstant.BRIEF_STATUS_002);
        if (StringUtil.isEmpty(recordId)) {
            BillAbAccrecords accrecords = billAccrecordsDao.get(voucher.getRecordId());
            // 生成新的流水信息(提示承兑申请流水)
            // 新的流水数据
            BillAbAccrecords entity = new BillAbAccrecords();
            BeanUtils.copyNotNullProperties(accrecords,entity,"id");
            entity.setVid(voucherInfo.getId());// 票据主表ID
            //entity.setVvid(accrecords.getVvid());// 票据从表ID
            entity.setLastid(accrecords.getId());// 保存上一笔流水ID
            entity.setLastbillcode("1".equals(voucherInfo.getNewStmIssuofpprInd()) ? voucherInfo.getBillStatus(): accrecords.getBriefId());// 保存上一笔业务类型
            entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);// 有效标志  0未完成；1可用；2已结束
            entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);// 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
            //同意签收
            if ("SU00".equals(voucher.getSignUpFlag())) {
                entity.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);//操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签
            } else {
                entity.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS_4);//操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签
            }
            entity.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);//是否发送指令标志：0否；1是 ；
            // entity.setReturnCode();//返回代码
            // entity.setReturnMsg();//处理结果
            entity.setBriefId(AccebillConstant.BRIEF_STATUS_002);// 流水业务类型002 提示承兑
            //entity.setInsideSing(accrecords.getInsideSing());// 内部业务标志
            //entity.setEnterStockNew(abVice.getEnterStock());// 库存
            //entity.setEnterStockOld(abAccrecords.getEnterStockNew());// 把上一条所属库存保存到旧的所属库存
            //entity.setBailType(accrecords.getBailType());//保证类型:01开票保证，02承兑保证；03背书保证
            //entity.setBankendrsmtmk(accrecords.getBankendrsmtmk());// 不得转让标记：EM00可再转让；EM01 不得转让
            entity.setApplyDate(new Date());// 申请日期
            entity.setBusinessDate(new Date());// 业务办理日期
            //entity.setBifCode(accrecords.getBifCode());
            //entity.setCnAccrecords(accrecords.getCnAccrecords());
            entity.setSignUpFlag(voucher.getSignUpFlag());
            entity.setBillFlag(voucherInfo.getBillFlag());
            entity.setReturnCode("0");
            entity.setReturnMsg("提示承兑返回结果自动生成流水");
            // .......业务申请人...，业务接收人....
            //entity.setReqRole(accrecords.getReqRole());//业务申请人类别
            //entity.setReqNm(accrecords.getReqNm());//业务申请人
            //entity.setReqCmonid(accrecords.getReqCmonid());//业务申请人组织机构代码
            //entity.setReqAcctId(accrecords.getReqAcctId());//业务申请人账号
            //entity.setReqAcctsvcr(accrecords.getReqAcctsvcr());//业务申请人开户行号
            //entity.setReqAcctsvcrAg(accrecords.getReqAcctsvcrAg());//业务申请人承接行号
            //entity.setRecRole(accrecords.getRecRole());//业务接收人类别
            //entity.setRecNm(accrecords.getRecNm());//业务接收人名称
            //entity.setRecCmonid(accrecords.getRecCmonid());//业务接收人组织机构代码
            //entity.setRecAcctId(accrecords.getRecAcctId());//业务接收人账号
            //entity.setRecAcctsvcr(accrecords.getRecAcctsvcr());//业务接收人开户行号
            //entity.setRecAcctsvcrAg(accrecords.getRecAcctsvcrAg());//业务接收人承接行号
            //entity.setBillId(accrecords.getBillId());
            entity.setCreateDate(new Date());
            billAccrecordsDao.save(entity);
        } else {
            //同意签收
            if ("SU00".equals(voucher.getSignUpFlag())) {
                billBankDao.updateAccrecordsInfoByRecordId2(recordId, AccebillConstant.AB_BILL_OPT_STATUS19, AccebillConstant.BRIEF_STATUS_002, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
            } else {
                billBankDao.updateAccrecordsInfoByRecordId2(recordId, AccebillConstant.AB_BILL_OPT_STATUS_4, AccebillConstant.BRIEF_STATUS_002, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
            }
        }
    }
    /**
     * <b>功能：</b>自动签收后处理来自外部的票据--提示承兑和提示承兑签收业务<br>
     * <b>作者：</b>郑刚<br>
     * <b>日期：</b> 2019-06-11 <br>
     *
     * @param voucher 票据主信息
     * @param briefId 业务类型
     */
    public synchronized void autoSignManage(VoucherInfoVo voucher) {
        if (logger.isDebugEnabled()) {
            logger.debug("autoSign=" + voucher.toString());
        }
        billCommonBaseService.autoSignManage(voucher);
    }
}
