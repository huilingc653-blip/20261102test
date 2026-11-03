package com.bytter.bill.domain.billbank.service.impl;
import java.util.List;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.service.BillCommonBaseService;
import com.bytter.bill.domain.billbank.service.IBillOpenGuaranteeService;
import com.bytter.bill.domain.billbank.vo.BillSignVo;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <br>
 * <b>功能：</b>开票提示保证业务操作service<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-6-2 <br>
 */
@Service("billOpenGuaranteeService")
@Transactional
public class BillOpenGuaranteeService implements IBillOpenGuaranteeService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BillBankDao billBankDao;
    @Autowired
    private BillSignDao billSignDao;
    @Autowired
    private BillCommonBaseService billCommonBaseService;
    /**
     * <b>功能：</b>开票提示保证--查询申请待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-2 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillOpenGuaranteeByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBillByBailType(AccebillConstant.BRIEF_STATUS_017, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16, AccebillConstant.AB_BAIL_TYPE_ADD);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>开票提示保证--发起方--指令发送已回应待签收--根据流水ID修改提示保证流水表状态为已回应待签收<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-2 <br>
     *
     * @param recordId 流水ID
     * @return
     */
    public void updateAccrecordsStatusByRecordId(VoucherInfoVo voucher) {
        //修改流水表status:已回应待签收 18
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS18, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
    }
    /**
     * <b>功能：</b>开票提示保证-根据银行接口代码及银行返回代码查询待签收数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-14 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBillByBailType(AccebillConstant.BRIEF_STATUS_017, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS18, AccebillConstant.AB_BAIL_TYPE_ADD);
        
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>开票提示保证--发起方--同意签收完成后--根据流水ID修改提示保证流水表状态为业务已完成<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-2 <br>
     *
     * @param recordId 流水ID
     * @return
     */
    public void updateAccrecordsStatusEndByRecordId(VoucherInfoVo voucher) {
        //修改流水表status:业务已完成 19
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //保证业务成功以后需要修改前一笔流水为可用
        billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
        //保证业务成功以后需要修改本笔流水为不可用
        billBankDao.updateAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
    }
    /**
     * <b>功能：</b>提示保证-发起方--签收方拒签后修改流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-2 <br>
     *
     * @param recordId 流水ID
     * @return
     */
    public void updateAccrecordsStatusRejection(VoucherInfoVo voucher) {
        //修改流水表status:业务已完成 -4
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改前一笔流水为可用
        billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
        //billBankDao.updateAccrecordsStatusAndNexttimeByRecordId(voucher.getRecordId(),EDraftConstant.OPERATE_STATUS_ACCEPTING_30);
    }
    /**
     * <b>功能：</b>开票提示保证-签收方--查询待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-2 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillOpenGuaranteeReplyByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBillByBailType(AccebillConstant.BRIEF_STATUS_9017, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16, AccebillConstant.AB_BAIL_TYPE_ADD);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>提示保证-签收方签收--根据签收回复流水ID修改流水表状态为已签收<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-2 <br>
     *
     * @param recordId 签收回复流水ID
     * @return
     */
    public void updateAccrecordsStatusReplyByRecordId(VoucherInfoVo voucher) {
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改从表为无效
        billBankDao.updateVoucherinfoViceValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
    }
    /**
     * <b>功能：</b>提示保证-签收方拒收--根据拒收回复流水ID修改流水表状态为已拒收状态<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-2 <br>
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
     * <b>功能：</b>提示保证-根据银行接口传过来的票据信息及状态修改票据及流水信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-5 <br>
     *
     * @param VoucherInfoVo 票据信息
     * @param status        状态
     *                      1：发起方--指令发送已回应待签收--流水ID
     *                      2：发起方--签收同意完成后修改票据表与流水表信息--流水ID
     *                      3：发起方--签收方拒签后修改票据表与流水表信息--流水ID
     *                      4：签收方--签收方签收--签收回复流水ID
     *                      5：签收方--签收方拒收--拒收回复流水ID
     */
    public void upateVoucherAndAccrecordsByVoucherInfoAndStatus(
            VoucherInfoVo voucher, int status) {
        switch (status) {
            //发起方--指令发送已回应待签收
            case 1:
                updateAccrecordsStatusByRecordId(voucher);
                //发起方--签收同意完成后修改票据表与流水表信息
            case 2:
                updateAccrecordsStatusEndByRecordId(voucher);
                //发起方--签收方拒签后修改票据表与流水表信息
            case 3:
                updateAccrecordsStatusRejection(voucher);
                //签收方--签收方签收
            case 4:
                updateAccrecordsStatusReplyByRecordId(voucher);
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
     * @param ebillBlCrclIndc 流通标志
     * @param blBsnTpcd 业务代码
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
}
