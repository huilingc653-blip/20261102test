package com.bytter.bill.domain.billbank.service.impl;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.service.IBillCommonRevokeRequestService;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <br>
 * <b>功能：</b>通用撤回业务操作service<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-6-8 <br>
 */
@Service("billCommonRevokeRequestService")
@Transactional
public class BillCommonRevokeRequestService implements IBillCommonRevokeRequestService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BillBankDao billBankDao;
    @Autowired
    private BillSignDao billSignDao;
    @Autowired
    private BillAccrecordsDao billAccrecordsDao;
    /**
     * <b>功能：</b>通用撤回-发起方--查询申请待发送指令票据信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-8 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillCommonRevokeRequestByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_032, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS16);
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>通用撤回--发起方--指令已发送待回应<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-12 <br>
     *
     * @param recordId 流水ID
     * @return
     */
    public void updateVoucherInfo(VoucherInfoVo voucher) {
        //根据流水ID更新票据流水表信息
        //修改流水表status:已回应待签收 18
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS18, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
    }
    /**
     * <b>功能：</b>通用撤回--根据银行接口代码及银行返回代码查询待回应数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-15 <br>
     *
     * @param bifCode 银行接口代码
     * @param reCode  银行返回代码
     * @return
     */
    public List<VoucherInfoVo> queryBillRegisterAwaitRespondByBifCode(String bifCode, String reCode) {
        List<VoucherInfoVo> voucherInfoList = billBankDao.queryBill(AccebillConstant.BRIEF_STATUS_032, bifCode, reCode, AccebillConstant.AB_BILL_OPT_STATUS18);
        
        return voucherInfoList;
    }
    /**
     * <b>功能：</b>通用撤回--发起方--指令发送回应成功<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-8 <br>
     *
     * @param recordId  流水ID
     * @param voucherId 票据ID
     * @return
     */
    public void updateVoucherInfoEnd(VoucherInfoVo voucher) {
        /**
         * 待添加-3状态，撤销成功原流水状态更新为-3,以区别是否是撤销的票
         */
        //撤回业务申请流水
        BillAbAccrecords accrecords = billBankDao.getAccrecordsByRecordId(voucher.getRecordId());
        accrecords.setReturnCode(voucher.getReturnCode());
        accrecords.setReturnMsg(voucher.getReturnMsg());
        // 原始业务申请流水
        BillAbAccrecords requestAccrecords = billBankDao.getAccrecordsByRecordId(accrecords.getLastid());
        String bustype = requestAccrecords.getBriefId();
        // 原始业务申请流水的上一笔流水
        BillAbAccrecords lastAccrecords = null;
        if (!AccebillConstant.BRIEF_STATUS_023.equals(bustype)) {
            lastAccrecords = billBankDao.getAccrecordsByRecordId(requestAccrecords.getLastid());
        }
        //票据信息
        BillAbVoucherinfo voucherInfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
        //业务类型
        // 开票提示保证、承兑保证、背书提示保证指令撤回处理
        if (AccebillConstant.BRIEF_STATUS_017.equals(bustype) || AccebillConstant.BRIEF_STATUS_024.equals(bustype)) {
            //更新原始业务申请流水的上一笔流水的操作状态为待提示承兑 30
            //billBankDao.updateAccrecordsInfoByRecordId(lastAccrecords.getRecordId(), EDraftConstant.OPERATE_STATUS_ACCEPTING_30, null,null,null);
            //修改原始业务申请流水前一笔流水为可用
            billBankDao.updateLastAccrecordsValidSignByRecordId(requestAccrecords.getId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
            //更新原始业务申请流水的操作状态为已撤回 －3 对业务的撤回状态
            billBankDao.updateAccrecordsInfoByRecordId(requestAccrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS_3, null, null, null, null);
            //更新原始业务申请流水的发出日期为当前日期
            //billBankDao.updateAccrecordsNextTime(requestAccrecords.getRecordId(), new Timestamp(new java.util.Date().getTime()));
            //更新撤回业务申请流水的操作状态为已经签收成功，业务完成 19
            billBankDao.updateAccrecordsInfoByRecordId(accrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
            //更新撤回业务申请流水的发出日期为当前日期
            //billBankDao.updateAccrecordsNextTime(accrecords.getId(), new Timestamp(new java.util.Date().getTime()));
            //更新票据表的boxStatus为10，billStatus为原始业务申请流水里的上一票据状态字段的值，值为空时则为010004
            //billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), AccebillConstant.AB_BOX_STATUS_N, requestAccrecords.getLastbillcode());
            //把原始业务申请流水的LASTBILLSTATUS的值赋值到票据表的bill_status
            billBankDao.updateVoucherInfoBillStatus(requestAccrecords.getId(), requestAccrecords.getVid());
        }
        //提示承兑指令撤回处理
        if (AccebillConstant.BRIEF_STATUS_002.equals(bustype)) {
            //更新原始业务申请流水的上一笔流水的操作状态为待提示承兑 30
            //billBankDao.updateAccrecordsInfoByRecordId(lastAccrecords.getId(), EDraftConstant.OPERATE_STATUS_ACCEPTING_30, null,null);
            //修改原始业务申请流水前一笔流水为可用
            billBankDao.updateLastAccrecordsValidSignByRecordId(requestAccrecords.getId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
            //更新原始业务申请流水的操作状态为已撤回 －3 对业务的撤回状态
            billBankDao.updateAccrecordsInfoByRecordId(requestAccrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS_3, null, null, null, null);
            //更新原始业务申请流水的发出日期为当前日期
            //billBankDao.updateAccrecordsNextTime(requestAccrecords.getId(), new Timestamp(new java.util.Date().getTime()));
            //更新撤回业务申请流水的操作状态为已经签收成功，业务完成 19
            billBankDao.updateAccrecordsInfoByRecordId(accrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
            //更新撤回业务申请流水的发出日期为当前日期
            //billBankDao.updateAccrecordsNextTime(accrecords.getId(), new Timestamp(new java.util.Date().getTime()));
            //更新票据表的boxStatus为10，billStatus为原始业务申请流水里的上一票据状态字段的值，值为空时则为010004
            String billStatus = "1".equals(voucherInfo.getNewStmIssuofpprInd()) ? AccebillConstant.NEWBILL_STATUS_CS01 : AccebillConstant.AB_BILL_STATUS_010004;
            String ebillBlCrclIndc = "1".equals(voucherInfo.getNewStmIssuofpprInd()) ? AccebillConstant.CIRCULATE_TF0101 : null;
            String blBsnTpcd = "1".equals(voucherInfo.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC01 : null;
            billBankDao.updateVoucherInfoByVoucherId(voucherInfo.getId(), null, billStatus,ebillBlCrclIndc,blBsnTpcd);
            billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(accrecords.getId(), AccebillConstant.AB_BOX_STATUS_Y);
            //把原始业务申请流水的LASTBILLSTATUS的值赋值到票据表的bill_status
            //billBankDao.updateVoucherInfoBillStatus(requestAccrecords.getId(), requestAccrecords.getVid());
        }
        // 提示收票指令撤回处理
        if (AccebillConstant.BRIEF_STATUS_003.equals(bustype)) {
            if (AccebillConstant.AB_BILL_STATUS_030001.equals(voucherInfo.getBillStatus()) || (AccebillConstant.NEWBILL_STATUS_CS02.equals(voucherInfo.getBillStatus()))
                    && AccebillConstant.CIRCULATE_TF0302.equals(voucherInfo.getEbillBlCrclIndc())) {
                //更新原始业务申请流水的上一笔流水的操作状态为待提示收票 31
                //billBankDao.updateAccrecordsInfoByRecordId(lastAccrecords.getRecordId(), EDraftConstant.OPERATE_STATUS_ISSUANCE_31, null,null,null);
                //修改原始业务申请流水前一笔流水为可用
                billBankDao.updateLastAccrecordsValidSignByRecordId(requestAccrecords.getId(), AccebillConstant.AB_BILL_VALID_SIGN_1);
                //更新原始业务申请流水的操作状态为已撤回 －3 对业务的撤回状态
                billBankDao.updateAccrecordsInfoByRecordId(requestAccrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS_3, null, null, null, null);
                //更新原始业务申请流水的发出日期为当前日期
                //billBankDao.updateAccrecordsNextTime(requestAccrecords.getId(), new Timestamp(new java.util.Date().getTime()));
                //更新撤回业务申请流水的操作状态为已经签收成功，业务完成 19
                billBankDao.updateAccrecordsInfoByRecordId(accrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
                //更新撤回业务申请流水的发出日期为当前日期
                //billBankDao.updateAccrecordsNextTime(accrecords.getId(), new Timestamp(new java.util.Date().getTime()));
                //更新票据表的boxStatus为11，billStatus为原始业务申请流水里的上一票据状态字段的值，值为空时则为020006
                String billStatus = "1".equals(voucherInfo.getNewStmIssuofpprInd()) ? AccebillConstant.NEWBILL_STATUS_CS02 : AccebillConstant.AB_BILL_STATUS_020006;
                String ebillBlCrclIndc = "1".equals(voucherInfo.getNewStmIssuofpprInd()) ? AccebillConstant.CIRCULATE_TF0101 : null;
                String blBsnTpcd = "1".equals(voucherInfo.getNewStmIssuofpprInd()) ? AccebillConstant.NEW_DRAFT_TYPE_BC02 : null;
                billBankDao.updateVoucherInfoByVoucherId(voucher.getVoucherId(), null, billStatus,ebillBlCrclIndc,blBsnTpcd);
                billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(accrecords.getId(), AccebillConstant.AB_BOX_STATUS_Y);
            }
        }
        String[] typeArr = new String[]{AccebillConstant.BRIEF_STATUS_010,AccebillConstant.BRIEF_STATUS_018,AccebillConstant.BRIEF_STATUS_019,
                AccebillConstant.BRIEF_STATUS_011,AccebillConstant.BRIEF_STATUS_020,AccebillConstant.BRIEF_STATUS_021};
        if (Arrays.asList(typeArr).contains(bustype)) {
            if (StringUtil.isNotEmpty(voucher.getBillId())) {
                accrecords.setBillId(voucher.getBillId());
            }
            //if (AccebillConstant.AB_BILL_STATUS_100001.equals(voucherInfo.getBillStatus())) {
            process(voucherInfo, accrecords, requestAccrecords, lastAccrecords);
            //}
        }
        //同意清偿指令撤回处理
        if (AccebillConstant.BRIEF_STATUS_023.equals(bustype)) {
            //更新原始业务申请流水的上一笔流水的操作状态为已经签收成功，业务完成 19
            //billBankDao.updateAccrecordsInfoByRecordId(lastAccrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS19, null,null);
            //修改原始业务申请流水前一笔流水为可用
            //billBankDao.updateLastAccrecordsValidSignByRecordId(requestAccrecords.getId(),AccebillConstant.AB_BILL_VALID_SIGN_1);
            //更新原始业务申请流水的操作状态为已撤回 －3 对业务的撤回状态
            billBankDao.updateAccrecordsInfoByRecordId(requestAccrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS_3, null, null, null, null);
            //更新撤回业务申请流水的操作状态为已经签收成功，业务完成 19
            billBankDao.updateAccrecordsInfoByRecordId(accrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, accrecords.getReturnCode(), accrecords.getReturnMsg(), voucher.getBillId());
            //更新撤回业务申请流水的发出日期为当前日期
            //billBankDao.updateAccrecordsNextTime(accrecords.getId(), new Timestamp(new java.util.Date().getTime()));
            //更新票据表的boxStatus为11，billStatus为原始业务申请流水里的上一票据状态字段的值
            billBankDao.updateVoucherInfoByVoucherId(voucherInfo.getId(), null, requestAccrecords.getLastbillcode(),requestAccrecords.getLastCirculateCode(),requestAccrecords.getLastBlBsnTpcd());
            String recId = NewDraftDealMethod.agreeSettleRepulse(billSignDao,requestAccrecords.getRecid(),18,requestAccrecords.getId(),billAccrecordsDao);
            //BillSign billSign = billSignDao.get(recId);
            billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(accrecords.getId(), AccebillConstant.AB_BOX_STATUS_Y);
            //修改追索申请签收表状态为18待签收状态
            /*billBankDao.updateSignBillStatus(voucherInfo.getOrigNo(), AccebillConstant.AB_BILL_OPT_STATUS19, AccebillConstant.AB_BILL_OPT_STATUS18
                    , AccebillConstant.AB_MSGTYPE_SEARCHFOR,billSign.getSbbllRngStrtSn(),billSign.get);*/
        }
        //修改签收表状态为-3 业务撤回状态
        billBankDao.updateSignBillStatus(voucherInfo.getOrigNo(), AccebillConstant.AB_BILL_OPT_STATUS18, AccebillConstant.AB_BILL_OPT_STATUS_3, bustype,voucherInfo.getSbbllRngStrtSn(),voucherInfo.getSbbllRngEndSn());
        //新票据拆分请求确认处理
        //NewDraftDealMethod.newDraftSplitAck(billBankDao,voucher.getRecordId(),logger);
    }
    private void process(BillAbVoucherinfo voucherInfo, BillAbAccrecords accrecords, BillAbAccrecords requestAccrecords, BillAbAccrecords lastAccrecords) {
        //更新原始业务申请流水的上一笔流水的操作状态为已经签收成功，业务完成 19
//		billBankDao.updateAccrecordsInfoByRecordId(lastAccrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS19, null,null, null,null);
        String recordId=requestAccrecords.getId();
        //针对下拨业务需要再往前找一个
        if(lastAccrecords!=null
                && (AccebillConstant.BRIEF_STATUS_6201.equals(lastAccrecords.getBriefId())
                || AccebillConstant.BRIEF_STATUS_6202.equals(lastAccrecords.getBriefId())
                || AccebillConstant.BRIEF_STATUS_6204.equals(lastAccrecords.getBriefId()))){
            recordId = requestAccrecords.getLastid();
            //设置下拨归集和上划失败
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("ids",new String[]{requestAccrecords.getLastid()});
            params.put("status",AccebillConstant.AB_BILL_BUS_STATUS_2);
            params.put("optStatus",AccebillConstant.AB_BILL_OPT_STATUS_2);
            params.put("returnMsg","撤票完成作废");
            billBankDao.batchUpdateBillAbAccrecords(params);
        }
        //修改原始业务申请流水前一笔流水为可用
        billBankDao.updateLastAccrecordsValidSignByRecordId(recordId, AccebillConstant.AB_BILL_VALID_SIGN_1);
        //更新原始业务申请流水的操作状态为已撤回 －3 对业务的撤回状态
        billBankDao.updateAccrecordsInfoByRecordId(requestAccrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS_3, null, null, null, null);
        //更新原始业务申请流水的发出日期为当前日期
        //billBankDao.updateAccrecordsNextTime(requestAccrecords.getId(), new Timestamp(new java.util.Date().getTime()));
        //更新撤回业务申请流水的操作状态为已经签收成功，业务完成 19
        billBankDao.updateAccrecordsInfoByRecordId(accrecords.getId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, accrecords.getReturnCode(), accrecords.getReturnMsg(), StringUtil.isEmpty(accrecords.getBillId()) ? null : accrecords.getBillId());
        //更新撤回业务申请流水的发出日期为当前日期
        //billBankDao.updateAccrecordsNextTime(accrecords.getId(), new Timestamp(new java.util.Date().getTime()));
        //更新票据表的boxStatus为11，billStatus为原始业务申请流水里的上一票据状态字段的值
        billBankDao.updateVoucherInfoByVoucherId(voucherInfo.getId(), null, requestAccrecords.getLastbillcode(),requestAccrecords.getLastCirculateCode(),requestAccrecords.getLastBlBsnTpcd());
        billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(accrecords.getId(), AccebillConstant.AB_BOX_STATUS_Y);
    }
    /**
     * <b>功能：</b>通用撤回--指令发送回应拒收<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-16 <br>
     *
     * @param recordId 拒收回复流水ID
     * @return
     */
    public void updateAccrecordsStatusRejectionByRecordId(VoucherInfoVo voucher) {
        billBankDao.updateAccrecordsInfoByRecordId(voucher.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS_4, voucher.getReturnCode(), voucher.getReturnMsg(), voucher.getBillId());
    }
    /**
     * <b>功能：</b>根据流水ID更新票据流水表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-8 <br>
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
            //NewDraftDealMethod.newDraftSplitAck(billBankDao,recordId,logger);
        }
    }
    /**
     * <b>功能：</b>根据票据ID更新票据表信息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-8 <br>
     * @param voucherId 票据ID
     * @param boxStatus 处理标记
     * @param billStatus 票据状态
     * @param sysSubject 系统科目
     * @param ebillBlCrclIndc 流通标志
     * @param blBsnTpcd 业务代码
     */
    public void updateVoucherInfoByVoucherId(String voucherId, Integer boxStatus,
                                             String billStatus, Long sysSubject, String ebillBlCrclIndc, String blBsnTpcd) {
        billBankDao.updateVoucherInfoByVoucherId(voucherId, boxStatus, billStatus,ebillBlCrclIndc,blBsnTpcd);
    }
    /**
     * <b>功能：</b>根据流水ID更新银行返回代码及消息<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2017-6-8 <br>
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
    public void updateAccrecordsNextTime(String id, Timestamp date) {
        // TODO Auto-generated method stub
    }
}
