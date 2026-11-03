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
import com.bytter.bill.domain.billbank.service.IBillNonTransCancelService;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.bill.domain.billbank.vo.BillSignVo;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * 不可转让撤销service
 *
 */
@Service("nonTransCancelService")
@Transactional
public class BillNonTransCancelService implements IBillNonTransCancelService {
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
     * <b>功能：</b>不可转让撤销-发起方--查询申请待发送指令票据信息<br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillTransCancelByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_024, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16);
        List<VoucherInfoVo> subList = new ArrayList<VoucherInfoVo>();
        if (!CollectionUtils.isEmpty(voucherInfoList)) {
            for (VoucherInfoVo v : voucherInfoList) {
                if ("1".equals(v.getNewStmIssuofpprInd())) {
                    subList.add(v);
                }
            }
        }
        return subList;
    }
    /**
     * <b>功能：</b>b不可转让撤销--发起方--指令发送已回应待签收<br>
     *
     * @param recordId  流水ID
     * @param voucherId 票据ID
     * @return
     */
    @Transactional
    public void updateVoucherInfo(VoucherInfoVo voucher) {
        BillAbVoucherinfo oldInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
        if(oldInfo == null){
            throw new RuntimeException("票据信息不存在！");
        }
        BeanUtils.copyNotNullProperties(oldInfo,voucher);
        //根据票据ID更新票据表信息
        //
        //NewStmIssuofpprInd出票标志  1为新系统出票；    SbbllRngStrtSn子票区间   0或者null为旧票
        if ((StringUtils.isNotBlank(voucher.getNewStmIssuofpprInd()) && "1".equals(voucher.getNewStmIssuofpprInd()))) {
            billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, AccebillConstant.NEWBILL_STATUS_CS03,
                    AccebillConstant.CIRCULATE_TF0302, AccebillConstant.NEW_DRAFT_TYPE_BC18);
            //根据流水ID更新票据流水表信息
            //修改流水表status:已回应待签收 18
            billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS18, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        }
    }
    /**
     * <b>功能：</b>不可转让撤销--根据银行接口代码及银行返回代码查询待签收数据<br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode) {
        //BRIEF_STATUS_024
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_024, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS18);
        
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>不可转让撤销--发起方查询结果--根据银行接口代码和业务标记<br>
     *
     * @param bifCode 银行接口代码
     * @return
     */
    public List<VoucherInfoVo> queryBillTransCancelResult(String bifCode) {
        String[] status = {AccebillConstant.AB_BILL_OPT_STATUS17, AccebillConstant.AB_BILL_OPT_STATUS18};
        //新票据改造
        //String[] billStatus = {AccebillConstant.AB_BILL_STATUS_010004, AccebillConstant.AB_BILL_STATUS_020001, AccebillConstant.AB_BILL_STATUS_020006, AccebillConstant.AB_BILL_STATUS_030006, AccebillConstant.AB_BILL_STATUS_030001};
        //List<VoucherInfoVo> voucherInfoList=billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_001,bifCode,null,status,billStatus);
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_024, bifCode, null, status, null);
        /*List<VoucherInfoVo> subList = new ArrayList<>();
        for (VoucherInfoVo vo : voucherInfoList) {
            if (Arrays.asList(billStatus).contains(vo.getBillStatus())
                    || ((StringUtils.isBlank(vo.getBillStatus()) && StringUtils.isBlank(vo.getEbillBlCrclIndc()) && StringUtils.isBlank(vo.getBlBsnTpcd()))
                    || statusStr.contains((vo.getBlBsnTpcd() + vo.getBillStatus() + vo.getEbillBlCrclIndc())))) {
                subList.add(vo);
            }
        }*/
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>不可转让撤销-发起方--签收同意完成后修改票据表与流水表信息<br>
     *
     * @param voucher 票据信息
     * @return
     */
    @Transactional
    public void updateVoucherInfoEndIsSend(VoucherInfoVo voucher) {
        if (logger.isDebugEnabled()) {
            logger.debug("VoucherInfoVo=" + voucher.toString());
        }
        int reNUm = 0;
        //无论同意或者拒绝，都设置申请流水为无效
        //billBankDao.updateAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
        billBankDao.updateAccValSignAndOptStaByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2,AccebillConstant.AB_BILL_OPT_STATUS19);
        BillAbAccrecords abAccrecordsOld = billBankDao.getAccrecordsByRecordId(voucher.getRecordId());
        BillAbVoucherinfo billAbVoucherinfoM = billBankDao.getVoucherInfoByVoucherId(abAccrecordsOld.getVid());
        BeanUtils.copyNotNullProperties(billAbVoucherinfoM,voucher,"id");
        //更新前一条流水为有效
        //billBankDao.updateAccrecordsValidSignByRecordId(abAccrecordsOld.getLastid(), AccebillConstant.AB_BILL_VALID_SIGN_1);
        billBankDao.updateAccrecordsInfoByRecordId3(abAccrecordsOld.getLastid(), AccebillConstant.AB_BILL_OPT_STATUS19, null,
                voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId(), AccebillConstant.AB_BANEM_Y);


        //根据票据ID更新票据表信息
        billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS03,
                AccebillConstant.CIRCULATE_TF0301, AccebillConstant.NEW_DRAFT_TYPE_BC18);
        billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);

        //同意签收 voucher.getSignUpFlag()没有值
       /* if ("SU00".equals(voucher.getSignUpFlag())) {
            //根据票据ID更新票据表信息
            billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS03,
                    AccebillConstant.CIRCULATE_TF0301, AccebillConstant.NEW_DRAFT_TYPE_BC18);
            billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
        }
        //拒绝签收
        else if ("SU01".equals(voucher.getSignUpFlag())) {
            billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS03,
                    AccebillConstant.CIRCULATE_TF0303, AccebillConstant.NEW_DRAFT_TYPE_BC18);
            billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
            //根据流水ID更新票据流水表信息
            //修改流水表status:拒收 -4
            billBankDao.updateAccrecordsInfoByRecordId2(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4, AccebillConstant.BRIEF_STATUS_024, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
            //把本条业务流水的LASTBILLSTATUS的值赋值到票据表的bill_status
            billBankDao.updateVoucherInfoBillStatus(voucher.getRecordId(), voucher.getVoucherId());
        }*/
    }

    /**  *
     * <b>功能：</b>不可转让撤销-签收方--签收同意完成后修改票据表与流水表信息<br>
     *
     * @param voucher 票据信息
     * @return
     */
    @Transactional
    public void updateVoucherInfoEnd(VoucherInfoVo voucher) {
        if (logger.isDebugEnabled()) {
            logger.debug("VoucherInfoVo=" + voucher.toString());
        }
        @SuppressWarnings("unused")
        int reNUm = 0;
        //同意签收
        if ("SU00".equals(voucher.getSignUpFlag())) {
            //根据票据ID更新票据表信息
            //修改票据表信息
            BillAbVoucherinfo voucherInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
            //不可转让撤销
            billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS03,
                    AccebillConstant.CIRCULATE_TF0301, AccebillConstant.NEW_DRAFT_TYPE_BC18);
            billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
            //根据流水ID更新票据流水表信息
            //修改流水表status:待提示收票 31
            reNUm = billBankDao.updateAccrecordsInfoByRecordId2(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        }
        //拒绝签收
        else if ("SU01".equals(voucher.getSignUpFlag())) {
            billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS03,
                    AccebillConstant.CIRCULATE_TF0303, AccebillConstant.NEW_DRAFT_TYPE_BC18);
            billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
            //根据流水ID更新票据流水表信息
            //修改流水表status:拒收 -4
            reNUm = billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
            //修改前一笔流水为可用
            //billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
            //把本条业务流水的LASTBILLSTATUS的值赋值到票据表的bill_status
            //billBankDao.updateVoucherInfoBillStatus(voucher.getRecordId(), voucher.getVoucherId());
        }
	/*	if(reNUm==0&& StringUtils.isNotEmpty(voucher.getSignUpFlag())){
			saveAccrecords(voucher);
		}*/
        //根据流水ID更新票据流水表的上个票据状态与上个科目字段
        //billBankDao.updateAccrecordsLastInfoByRecordId(voucher.getRecordId(),AccebillConstant.AB_BILL_STATUS_020006);
    }
    /**
     * <b>功能：</b>不可转让撤销-发起方--签收方拒签后修改票据表与流水表信息<br>
     *
     * @param voucher 票据信息
     * @return
     */
    @Transactional
    public void updateVoucherInfoRejection(VoucherInfoVo voucher) {
        billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_Y, AccebillConstant.NEWBILL_STATUS_CS03,
                AccebillConstant.CIRCULATE_TF0303, AccebillConstant.NEW_DRAFT_TYPE_BC18);
        billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucher.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
        //根据流水ID更新票据流水表信息  修改流水表status:拒收 -4,判断returnCode是否为撤回，是则为撤回
        String optStatus = AccebillConstant.AB_BILL_OPT_STATUS_4;
        if (AccebillConstant.AB_BILL_OPT_STATUS_3.equals(voucher.getReturnCode())) {
            optStatus = AccebillConstant.AB_BILL_OPT_STATUS_3;
        }
        //无论同意或者拒绝，都设置申请流水为无效
        billBankDao.updateAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
        BillAbAccrecords abAccrecordsOld = billBankDao.getAccrecordsByRecordId(voucher.getRecordId());
        //更新前一条流水为有效
        billBankDao.updateAccrecordsValidSignByRecordId(abAccrecordsOld.getLastid(), AccebillConstant.AB_BILL_VALID_SIGN_1);
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), optStatus, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改前一笔流水为可用
        //billBankDao.updateLastAccrecordsValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
        //把本条业务流水的LASTBILLSTATUS的值赋值到票据表的bill_status
        billBankDao.updateVoucherInfoBillStatus(voucher.getRecordId(), voucher.getVoucherId());
    }
    /**
     * <b>功能：</b>不可转让撤销-签收方--查询待发送指令票据信息<br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillTransCancelReplyByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_9024, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>不可转让撤销-签收方--查询已回应待签收的票据信息<br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillTransCancelReplyAwaitByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_9024, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS18);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>不可转让撤销-签收方签收--根据签收回复流水ID修改流水表状态为已签收<br>
     *
     * @param recordId 签收回复流水ID
     * @return
     */
    @Transactional
    public void updateAccrecordsStatusByRecordId(VoucherInfoVo voucher) {
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改从表为无效
        //billBankDao.updateVoucherinfoViceValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
    }
    /**
     * <b>功能：</b>不可转让撤销-签收方拒收--根据拒收回复流水ID修改流水表状态为已拒收状态<br>
     *
     * @param recordId 拒收回复流水ID
     * @return
     */
    @Transactional
    public void updateAccrecordsStatusRejectionByRecordId(VoucherInfoVo voucher) {
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
        //修改从表为无效
        billBankDao.updateVoucherinfoViceValidSignByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
    }
    /**
     * <b>功能：</b>不可转让撤销-根据银行接口传过来的票据信息及状态修改票据及流水信息<br>
     *
     * @param VoucherInfoVo 票据信息
     * @param status        状态
     *                      1：发起方--指令发送已回应待签收--票据ID,流水ID
     *                      2：发起方--签收同意完成后修改票据表与流水表信息--票据ID,流水ID
     *                      3：发起方--签收方拒签后修改票据表与流水表信息--票据ID,流水ID
     *                      4：签收方--签收方签收--签收回复流水ID
     *                      5：签收方--签收方拒收--拒收回复流水ID
     */
    @Transactional
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
     *
     * @param recordId   票据流水ID
     * @param status     操作状态
     * @param briefId    业务类型
     * @param returnCode 返回代码
     * @param returnMsg  返回消息
     */
    @Transactional
    public void updateAccrecordsInfoByRecordId(String recordId, Integer status,
                                               String briefId, String returnCode, String returnMsg) {
        if (logger.isDebugEnabled()) {
            logger.debug("recordId=" + recordId + ",status=" + status + ",briefId=" + briefId + ",returnCode=" + returnCode + ",returnMsg=" + returnMsg);
        }
        billBankDao.updateAccrecordsInfoByRecordId(recordId, status == null ? null : String.valueOf(status), briefId, returnCode, returnMsg, null);
    }
    /**
     * <b>功能：</b>根据票据ID更新票据表信息<br>
     *
     * @param voucherId       票据ID
     * @param boxStatus       处理标记
     * @param billStatus      票据状态
     * @param sysSubject      系统科目
     * @param ebillBlCrcLindc 流通标志
     * @param blBsnTpcd       新票据票据业务类型
     */
    @Transactional
    public void updateVoucherInfoByVoucherId(String voucherId, Integer boxStatus,
                                             String billStatus, Long sysSubject, String ebillBlCrcLindc, String blBsnTpcd) {
        billBankDao.updateVoucherInfoByVoucherId(voucherId, boxStatus, billStatus, ebillBlCrcLindc, blBsnTpcd);
    }
    /**
     * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
     *
     * @param recordId 流水ID
     * @param reCode   银行返回代码
     * @param reMsg    银行返回消息
     */
    @Transactional
    public void updateAccrecordsReCodeAndReMsgByRecordId(String recordId,
                                                         String reCode, String reMsg) {
        billBankDao.updateAccrecordsReCodeAndReMsgByRecordId(recordId, reCode, reMsg);
    }
    /**
     * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
     *
     * @param recordId 流水ID
     * @param reCode   银行返回代码
     * @param reMsg    银行返回消息
     * @param billId   银行返回消息
     */
    @Transactional
    public void updateAccrecordsReCodeAndReMsgByRecordId(String recordId,
                                                         String reCode, String reMsg, String billId) {
        billBankDao.updateAccrecordsReCodeAndReMsgByRecordId(recordId, reCode, reMsg, billId);
    }
    /**
     * <b>功能：</b>新增签收表数据<br>
     *
     * @param billSign 签收表实体
     */
    @Transactional
    public void saveBillSign(BillSignVo billSign) {
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
     *
     * @param msgId 签收表实体
     */
    public List<BillSignVo> queryBillSignListBySerialId(String msgId) {
        List<BillSign> billSignList = billSignDao.queryBillSignListBySerialId(msgId);
        List<BillSignVo> reList = BeanUtil.copyListProperties(billSignList, BillSignVo.class);
        return reList;
    }
    /**
     * <b>功能：</b>自动签收后处理来自外部的票据--不可转让撤销和不可转让撤销签收业务<br>
     *
     * @param voucher 票据主信息
     * @param briefId 业务类型
     */
    @Transactional
    public synchronized void autoSignManage(VoucherInfoVo voucher) {
        if (logger.isDebugEnabled()) {
            logger.debug("autoSign=" + voucher.toString());
        }
        billCommonBaseService.autoSignManage(voucher);
    }
}
