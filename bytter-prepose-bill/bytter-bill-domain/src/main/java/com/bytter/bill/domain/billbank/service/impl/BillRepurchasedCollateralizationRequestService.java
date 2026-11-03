package com.bytter.bill.domain.billbank.service.impl;
import java.util.Date;
import java.util.List;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.pool.service.BillPoolManageImpService;
import com.bytter.bill.domain.billbank.service.BillCommonBaseService;
import com.bytter.bill.domain.billbank.service.IBillRepurchasedCollateralizationRequestService;
import com.bytter.bill.domain.billbank.service.IEDraftExcBillService;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.bill.domain.billbank.vo.BillSignVo;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <br>
 * <b>功能：</b>解除质押申请业务操作service<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-6-7 <br>
 */
@Service("billRepurchasedCollateralizationRequestService")
@Transactional
public class BillRepurchasedCollateralizationRequestService implements IBillRepurchasedCollateralizationRequestService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BillBankDao billBankDao;
    @Autowired
    private BillSignDao billSignDao;
    @Autowired
    private IEDraftExcBillService eDraftExcBillService;
    @Autowired
    private BillPoolManageImpService billPoolManageImpService;
    @Autowired
    private BillCommonBaseService billCommonBaseService;

    @Autowired
    private BillAccrecordsDao billAccrecordsDao;

    /**  *
     * <b>功能：</b>解除质押申请-发起方--查询申请待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillRepurchasedCollateralizationByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_019, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16, AccebillConstant.poolSign_2);
        //新票参数处理
        voucherInfoList = NewDraftDealMethod.dealNewDraftArg(voucherInfoList,billBankDao);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>解除质押申请--发起方--指令发送已回应待签收<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
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
        long nowTime = DateUtils.getDateYMD().getTime();
        long endTime = (voucher.getEndDate() == null ? DateUtils.getDateYMD() : voucher.getEndDate()).getTime();
        String billStatus = "1".equals(voucher.getNewStmIssuofpprInd()) ? (endTime >= nowTime ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.NEWBILL_STATUS_CS04)
                : AccebillConstant.AB_BILL_STATUS_190001;
        String ebillBlCrclIndc = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.CIRCULATE_TF0302 : null;
        String blBsnTpcd = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC12 : null;
        //根据票据ID更新票据表信息
        //修改票据表信息bill_status:质押解除待签收 190001
        billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus, ebillBlCrclIndc, blBsnTpcd);
        //根据流水ID更新票据流水表信息
        //修改流水表status:已回应待签收 17
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS17, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());


        //针对质押解除业务进行票据池处理
        billPoolManageImpService.accebillBusPoolOp(voucher.getRecordId());

//        发送申请不这种已拆票处理需要查询结果设置
        //新票据拆分请求确认处理
//        NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId());
    }
    /**
     * <b>功能：</b>解除质押申请--根据银行接口代码及银行返回代码查询待签收数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-15 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_019, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS18, AccebillConstant.poolSign_2);
        
        //新票参数处理
        voucherInfoList = NewDraftDealMethod.dealNewDraftArg(voucherInfoList,billBankDao);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>解除质押申请-发起方--签收同意完成后修改票据表与流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
     *
     * @param voucher 票据信息
     * @return
     */
    public void updateVoucherInfoEnd(VoucherInfoVo voucher) {
        BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
        if(oldInfo == null){
            throw new RuntimeException("票据信息不存在！");
        }
        BeanUtils.copyNotNullProperties(oldInfo,voucher);
        long nowTime = DateUtils.getDateYMD().getTime();
        long endTime = (voucher.getEndDate() == null ? DateUtils.getDateYMD() : voucher.getEndDate()).getTime();
        String billStatus = "1".equals(voucher.getNewStmIssuofpprInd()) ? (endTime > nowTime ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.NEWBILL_STATUS_CS04)
                : AccebillConstant.AB_BILL_STATUS_190006;
        String ebillBlCrclIndc = "1".equals(voucher.getNewStmIssuofpprInd()) ? (endTime >= nowTime ? AccebillConstant.CIRCULATE_TF0301 : AccebillConstant.CIRCULATE_TF0401) : null;
        String blBsnTpcd = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC12 : null;
        //根据票据ID更新票据表信息
        //修改票据表信息boxStatus：业务处理完成（11）bill_status:质押已签收(190006)
        billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus, ebillBlCrclIndc, blBsnTpcd);
        billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
        //根据流水ID更新票据流水表信息
        //修改流水表status:签收成功 19
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改从表为无效
        billBankDao.updateVoucherinfoViceValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
        eDraftExcBillService.saveEDraftExcBill(voucher.getRecordId(), EDraftConstant.MSG_TYPE_BACKINFO);
        //针对质押解除业务完成进行票据池处理
        billPoolManageImpService.unPledgeAutoPoolOp(voucher.getRecordId());
        //新票据拆分请求确认处理
        NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId());
    }
    /**
     * <b>功能：</b>解除质押申请-签收方--签收同意完成后修改票据表与流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-7-20 <br>
     *
     * @param voucher 票据信息
     * @return
     */
    public void updateVoucherInfoEnd2(VoucherInfoVo voucher) {
        BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
        if(oldInfo == null){
            throw new RuntimeException("票据信息不存在！");
        }
        BeanUtils.copyNotNullProperties(oldInfo,voucher);
        long nowTime = DateUtils.getDateYMD().getTime();
        long endTime = (voucher.getEndDate() == null ? DateUtils.getDateYMD() : voucher.getEndDate()).getTime();
        String billStatus = "1".equals(voucher.getNewStmIssuofpprInd()) ? (endTime > nowTime ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.NEWBILL_STATUS_CS04)
                : AccebillConstant.AB_BILL_STATUS_190006;
        String ebillBlCrclIndc = "1".equals(voucher.getNewStmIssuofpprInd()) ? (endTime >= nowTime ? AccebillConstant.CIRCULATE_TF0301 : AccebillConstant.CIRCULATE_TF0401) : null;
        String blBsnTpcd = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC12 : null;
        //根据票据ID更新票据表信息
        billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus, ebillBlCrclIndc, blBsnTpcd);
        billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
        //根据流水ID更新票据流水表信息
        //修改流水表status:签收成功 19
        int reNUm = billBankDao.updateAccrecordsInfoByRecordId2(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, AccebillConstant.BRIEF_STATUS_9019, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        if (reNUm == 0) {
            //saveAccrecords(voucher.getVoucherId(),voucher.getRecordId());
        }
        eDraftExcBillService.saveEDraftExcBill(voucher.getRecordId(), EDraftConstant.MSG_TYPE_BACKINFO);
        //针对质押解除业务成功进行票据池处理
        billPoolManageImpService.accebillBusPoolOp(voucher.getRecordId());
        //新票据拆分请求确认处理
        NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId());
    }
    /**
     * <b>功能：</b>解除质押申请-发起方--签收方拒签后修改票据表与流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
     *
     * @param voucher 票据信息
     * @return
     */
    public void updateVoucherInfoRejection(VoucherInfoVo voucher) {
        //根据票据ID更新票据表信息
        //修改票据表信息boxStatus：业务处理完成（11）
        //billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, null);
        billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
        //根据流水ID更新票据流水表信息  修改流水表status:拒收 -4,判断returnCode是否为撤回，是则为撤回
        String optStatus = AccebillConstant.AB_BILL_OPT_STATUS_4;
        if (AccebillConstant.AB_BILL_OPT_STATUS_3.equals(voucher.getReturnCode())) {
            optStatus = AccebillConstant.AB_BILL_OPT_STATUS_3;
        }
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), optStatus, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改前一笔流水为可用
        billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
        //把本条业务流水的LASTBILLSTATUS的值赋值到票据表的bill_status
        billBankDao.updateVoucherInfoBillStatus(voucher.getRecordId(), voucher.getVoucherId());
        //把本条业务流水的LASTSYS_SUBJECT的值赋值到票据表的sys_Subject
        //billBankDao.updateVoucherInfoSysSubject(voucher.getRecordId(), voucher.getVoucherId());
        //针对质押解除业务完成进行票据池处理
        billPoolManageImpService.accebillBusPoolOp(voucher.getRecordId());
        //新票据拆分请求确认处理
        NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId());
    }
    /**
     * <b>功能：</b>解除质押申请-签收方--查询待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillRepurchasedCollateralizationReplyByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_9019, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16);
        //新票参数处理
        voucherInfoList = NewDraftDealMethod.dealNewDraftArg(voucherInfoList,billBankDao);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>解除质押申请-签收方--查询已回应待签收的票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-7-20 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillRCollateralizationReplyRegisterAwaitByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_9019, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS18);
        //新票参数处理
        voucherInfoList = NewDraftDealMethod.dealNewDraftArg(voucherInfoList,billBankDao);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>解除质押申请-签收方签收--根据签收回复流水ID修改流水表状态为已签收<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
     *
     * @param recordId 签收回复流水ID
     * @return
     */
    public void updateAccrecordsStatusByRecordId(VoucherInfoVo voucher) {
        BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
        if(oldInfo == null){
            throw new RuntimeException("票据信息不存在！");
        }
        BeanUtils.copyNotNullProperties(oldInfo,voucher);
        long nowTime = DateUtils.getDateYMD().getTime();
        long endTime = (voucher.getEndDate() == null ? DateUtils.getDateYMD() : voucher.getEndDate()).getTime();
        String billStatus = "1".equals(voucher.getNewStmIssuofpprInd()) ? (endTime > nowTime ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.NEWBILL_STATUS_CS04)
                : AccebillConstant.AB_BILL_STATUS_190006;
        String ebillBlCrclIndc = "1".equals(voucher.getNewStmIssuofpprInd()) ? (endTime >= nowTime ? AccebillConstant.CIRCULATE_TF0301 : AccebillConstant.CIRCULATE_TF0401) : null;
        String blBsnTpcd = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC12 : null;
        billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus, ebillBlCrclIndc, blBsnTpcd);
        //修改流水表status:签收成功 19
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        eDraftExcBillService.saveEDraftExcBill(voucher.getRecordId(), EDraftConstant.MSG_TYPE_BACKINFO);
        //针对质押解除业务成功进行票据池处理
        billPoolManageImpService.accebillBusPoolOp(voucher.getRecordId());
    }
    /**
     * <b>功能：</b>解除质押申请-签收方拒收--根据拒收回复流水ID修改流水表状态为已拒收状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
     *
     * @param recordId 拒收回复流水ID
     * @return
     */
    public void updateAccrecordsStatusRejectionByRecordId(VoucherInfoVo voucher) {
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改从表为无效
        billBankDao.updateVoucherinfoViceValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
        //针对质押解除业务失败进行票据池处理
        billPoolManageImpService.accebillBusPoolOp(voucher.getRecordId());
    }
    /**
     * <b>功能：</b>解除质押申请-根据银行接口传过来的票据信息及状态修改票据及流水信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
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
     * @param recordId 票据流水ID
     * @param status 操作状态
     * @param briefId 业务类型
     * @param returnCode 返回代码
     * @param returnMsg 返回消息
     */
    public void updateAccrecordsInfoByRecordId(String recordId, Integer status,
                                               String briefId, String returnCode, String returnMsg) {
        updateAccrecordsInfoByRecordIdFail(recordId,status,briefId,returnCode,returnMsg);

        //若是线上可拆分新票，发送查询正面信息指令，并设置流水表指令id字段exc_id，用于查询银行是否存在此区间票据，若存在，说明拆分成功，若不存在说明没拆分成功
        //NewDraftDealMethod.newDraftSendFailConfirmExc(billBankDao,recordId,eDraftExcBillService);
    }

    /**  *
     * <b>功能：</b>根据流水ID更新票据流水表信息<br>
     * @param recordId 票据流水ID
     * @param status 操作状态
     * @param briefId 业务类型
     * @param returnCode 返回代码
     * @param returnMsg 返回消息
     */
    public void updateAccrecordsInfoByRecordIdForBaffle(String recordId, Integer status,
                                                        String briefId, String returnCode, String returnMsg) {
        updateAccrecordsInfoByRecordIdFail(recordId,status,briefId,returnCode,returnMsg);
        BillAbAccrecords accrecords = billAccrecordsDao.get(recordId);
        accrecords.setUpdateDate(new Date());
        accrecords.setExcId(null);
        billAccrecordsDao.saveOrUpdate(accrecords);
    }

    private void updateAccrecordsInfoByRecordIdFail(String recordId, Integer status,
                                                    String briefId, String returnCode, String returnMsg) {
        billBankDao.updateAccrecordsInfoByRecordId(recordId, status == null ? null : String.valueOf(status), briefId, returnCode, returnMsg, null);
        if (AccebillConstant.AB_BILL_OPT_STATUS_1.equals(String.valueOf(status))
                || AccebillConstant.AB_BILL_OPT_STATUS18.equals(String.valueOf(status))) {
            //针对质押解除业务失败进行票据池处理
            billPoolManageImpService.accebillBusPoolOp(recordId);
        }

        if(!(AccebillConstant.AB_BILL_OPT_STATUS15.equals(String.valueOf(status))
                || AccebillConstant.AB_BILL_OPT_STATUS16.equals(String.valueOf(status))
                || AccebillConstant.AB_BILL_OPT_STATUS17.equals(String.valueOf(status))
                || AccebillConstant.AB_BILL_OPT_STATUS_1.equals(String.valueOf(status))
                || AccebillConstant.AB_BILL_OPT_STATUS_2.equals(String.valueOf(status)))){
            //新票据拆分请求确认处理
            NewDraftDealMethod.newDraftSplitAck(billBankDao,recordId);
        }
    }
    /**
     * <b>功能：</b>根据票据ID更新票据表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-7 <br>
     *
     * @param voucherId       票据ID
     * @param boxStatus       处理标记
     * @param billStatus      票据状态
     * @param sysSubject      系统科目
     * @param ebillBlCrclIndc 流通标志
     * @param blBsnTpcd       业务代码
     */
    public void updateVoucherInfoByVoucherId(String voucherId, Integer boxStatus,
                                             String billStatus, Long sysSubject, String ebillBlCrclIndc, String blBsnTpcd) {
        billBankDao.updateVoucherInfoByVoucherId(voucherId, boxStatus, billStatus,ebillBlCrclIndc, blBsnTpcd);
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
     * <b>功能：</b>自动签收后处理来自外部的票据--质押解除签收业务<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-12-29 <br>
     *
     * @param voucher 票据主信息
     */
    public synchronized void autoSignManage(VoucherInfoVo voucher) {
        if (logger.isDebugEnabled()) {
            logger.debug("autoSign=" + voucher.toString());
        }
        billCommonBaseService.autoSignManage(voucher);
    }
}
