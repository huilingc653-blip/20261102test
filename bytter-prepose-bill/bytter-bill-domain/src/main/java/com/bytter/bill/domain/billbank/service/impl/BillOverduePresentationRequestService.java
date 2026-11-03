package com.bytter.bill.domain.billbank.service.impl;
import java.util.List;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.service.BillCommonBaseService;
import com.bytter.bill.domain.billbank.service.IBillOverduePresentationRequestService;
import com.bytter.bill.domain.billbank.service.IEDraftExcBillService;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.bill.domain.billbank.vo.BillSignVo;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <br>
 * <b>功能：</b>逾期提款付款业务操作service<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-6-20 <br>
 */
@Service("billOverduePresentationRequestService")
@Transactional
public class BillOverduePresentationRequestService implements IBillOverduePresentationRequestService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BillBankDao billBankDao;
    @Autowired
    private BillSignDao billSignDao;
    @Autowired
    private IEDraftExcBillService eDraftExcBillService;
    @Autowired
    private BillCommonBaseService billCommonBaseService;
    /**
     * <b>功能：</b>逾期提款付款-发起方--查询申请待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillOverduePresentationRequestByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_021, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16, AccebillConstant.poolSign_2);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>逾期提款付款--发起方--指令发送已回应待签收<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
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
        String billStatus = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEWBILL_STATUS_CS04 : AccebillConstant.AB_BILL_STATUS_210001;
        String ebillBlCrclIndc = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.CIRCULATE_TF0302 : null;
        String blBsnTpcd = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC13 : null;
        //根据票据ID更新票据表信息
        //修改票据表信息bill_status:逾期提款付款待签收 200001
        billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus, ebillBlCrclIndc, blBsnTpcd);
        //根据流水ID更新票据流水表信息
        //修改流水表status:已回应待签收 18
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS18, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //新票据拆分请求确认处理
        NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId());
    }
    /**
     * <b>功能：</b>逾期提款付款--根据银行接口代码及银行返回代码查询待签收数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_021, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS18, AccebillConstant.poolSign_2);
        
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>逾期提款付款-发起方--签收同意完成后修改票据表与流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
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
        String billStatus = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEWBILL_STATUS_CS06 : AccebillConstant.AB_BILL_STATUS_000000;
        String ebillBlCrclIndc = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.CIRCULATE_TF0501 : null;
        String blBsnTpcd = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC13 : null;
        //根据票据ID更新票据表信息
        //修改票据表信息boxStatus：业务处理完成（11）bill_status:结束已结清(000000)
        billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus, ebillBlCrclIndc, blBsnTpcd);
        billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
        //根据流水ID更新票据流水表信息
        //修改流水表status:签收成功 19
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改从表为无效
        billBankDao.updateVoucherinfoViceValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
        eDraftExcBillService.saveEDraftExcBill(voucher.getRecordId(), EDraftConstant.MSG_TYPE_BACKINFO);
        //新票据拆分请求确认处理
        NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId());
    }
    /**
     * <b>功能：</b>逾期提款付款-发起方--签收方拒签后修改票据表与流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
     *
     * @param voucher 票据信息
     * @return
     */
    public void updateVoucherInfoRejection(VoucherInfoVo voucher) {
        //根据票据ID更新票据表信息
        //修改票据表信息boxStatus：业务处理完成（11）
        billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, voucher.getBillStatus(), voucher.getEbillBlCrclIndc(), voucher.getBlBsnTpcd());
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
        //新票据拆分请求确认处理
        NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId());
    }
    /**
     * <b>功能：</b>逾期提款付款-签收方--查询待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillOverduePresentationRequestReplyByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_9021, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>逾期提款付款-签收方--查询已回应待签收的票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillOverduePresentationReplyRegisterAwaitByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_9021, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS18);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>逾期提款付款-签收方签收--根据签收回复流水ID修改流水表状态为已签收<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
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
        String billStatus = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEWBILL_STATUS_CS06 : AccebillConstant.AB_BILL_STATUS_000000;
        String ebillBlCrclIndc = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.CIRCULATE_TF0501 : null;
        String blBsnTpcd = "1".equals(voucher.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC13 : null;
        billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus, ebillBlCrclIndc, blBsnTpcd);
        //修改流水表status:签收成功 19
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        eDraftExcBillService.saveEDraftExcBill(voucher.getRecordId(), EDraftConstant.MSG_TYPE_BACKINFO);
    }
    /**
     * <b>功能：</b>逾期提款付款-签收方拒收--根据拒收回复流水ID修改流水表状态为已拒收状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-20 <br>
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
     * <b>功能：</b>逾期提款付款-根据银行接口传过来的票据信息及状态修改票据及流水信息<br>
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
        billBankDao.updateAccrecordsInfoByRecordId(recordId, status == null ? null : String.valueOf(status), briefId, returnCode, returnMsg, null);
        if(!(AccebillConstant.AB_BILL_OPT_STATUS15.equals(String.valueOf(status)) || AccebillConstant.AB_BILL_OPT_STATUS16.equals(String.valueOf(status))
                || AccebillConstant.AB_BILL_OPT_STATUS17.equals(String.valueOf(status)) || AccebillConstant.AB_BILL_OPT_STATUS_1.equals(String.valueOf(status))
                || AccebillConstant.AB_BILL_OPT_STATUS_2.equals(String.valueOf(status)))){
            //新票据拆分请求确认处理
            NewDraftDealMethod.newDraftSplitAck(billBankDao,recordId);
        }
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
     * @param sysSubject 流通标志
     * @param sysSubject 业务代码
     */
    public void updateVoucherInfoByVoucherId(String voucherId, Integer boxStatus,
                                             String billStatus, Long sysSubject, String ebillBlCrclIndc, String blBsnTpcd) {
        billBankDao.updateVoucherInfoByVoucherId(voucherId, boxStatus, billStatus, ebillBlCrclIndc, blBsnTpcd);
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
}
