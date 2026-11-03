package com.bytter.tally.service;
import com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.BisAccDtlCopyDTO;
import com.bytter.biz.finacial.api.dto.FcTallyRemindDtlDTO;
import com.bytter.biz.finacial.api.dto.FcUnionAccbookDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.ITallyService;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.finance.dto.CmsPrecativeLoanRepayDTO;
import com.bytter.finance.dto.CmsPrecativeLoanSendDTO;
import com.bytter.finance.dto.CmsProvideLoanRepayDTO;
import com.bytter.finance.dto.CmsProvideSendDTO;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.tally.vo.FcUnionAccbookVo;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author
 * @version 1.0
 * @Title: Service
 * @Description: BT_NET_CHALK
 * @date 2017-07-04 14:58:22
 */
@Service("finacialTallyRemindService")
@Transactional
@Slf4j
public class FinacialTallyRemindService implements ITaskService {
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    @Qualifier("TallyServiceImpl")
    private ITallyService tallyService;
    @Autowired
    private CenterPaymentService centerPaymentService;
    @Autowired
    private PrecativeTallyService precativeTallyService;
    @Autowired
    private ProvideTallyService provideTallyService;
    @Autowired
    private NisAllocationReqManualService nisAllocationReqManualService;
    @Autowired
    private NisBillheadManualService nisBillheadManualService;
    @Autowired
    private AbBillTallyService abBillTallyService;
    @Autowired
    private FcBankAccDtlService fcBankAccDtlService;
    @Autowired
    private InnerDepositService innerDepositService;
    @DubboReference(check = false)
    private IFcCorpPoolService iFcCorpPoolService;
    @DubboReference(check = false)
    private IFcAccbookService fcAccbookServiceImpl;
    @Override
    @XxlJob("FinacialTallyRemindService")
    public void run(String arguments) {
        log.info("进入网络记账待记账提醒定时任务");
        List<FcCorpPoolDTO> pools = iFcCorpPoolService.getAllCorpPool(null, null);
        List<FcTallyRemindDtlDTO> remindDtls = new ArrayList<>();
        //查询待记账数据
        List<BisExcDTO> centerPay = centerPaymentService.queryTallyBisEx();//中心申请付款
        if (!EmptyUtils.isEmptyList(centerPay)) {
            for (BisExcDTO bis : centerPay) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(bis.getPayMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(bis.getId(), ResourceConstant.NETBANK_BDLINK_BISEXC_SALEPAYMENTQUERY, NotificationConstant.FINACIAL.BISEXC_TALLY, map, bis.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(bis.getId());
                remindDtls.add(remindDtl);
            }
        }
        //网点间来账
        List<FcUnionAccbookDTO> unionAccBookList = tallyService.queryTallyFcUnionAccBook();
        List<FcUnionAccbookVo> fua = BeanUtils.copyListProperties(unionAccBookList, FcUnionAccbookVo.class);
        if (!EmptyUtils.isEmptyList(fua)) {
            for (FcUnionAccbookVo f : fua) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(f.getSendMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(f.getId(), ResourceConstant.FCUNIONACCBOOK_FCUNIONACCBOOK_APPLY, NotificationConstant.FINACIAL.FCUNIONACCBOOK_TALLY, map, f.getSendNet());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(f.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<CmsPrecativeLoanSendDTO> precativeSend = precativeTallyService.queryTallyPrecativeSend();//委托贷款发放
        if (!EmptyUtils.isEmptyList(precativeSend)) {
            for (CmsPrecativeLoanSendDTO send : precativeSend) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(send.getMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(send.getId(), ResourceConstant.FCL_PRECATIVE_TALLY_APPLY, NotificationConstant.FINACIAL.PRECATIVE_TALLY, map, send.getPrecativeId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(send.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<CmsPrecativeLoanRepayDTO> precativeRepay = precativeTallyService.queryTallyPrecativeRepay();//委托贷款还款
        //TODO 排除已记账的数据
        precativeRepay = distinctPrecativeRepayList(precativeRepay);
        if (!EmptyUtils.isEmptyList(precativeRepay)) {
            for (CmsPrecativeLoanRepayDTO repay : precativeRepay) {
                Map<String, Object> map = new HashMap<String, Object>();
                BigDecimal repayMoney = repay.getRepayMoney().add(repay.getRepayAccrual());
                map.put("money", NumberUtil.formatNumeric(repay.getRepayMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(repay.getId(), ResourceConstant.FCL_PRECATIVE_TALLY_APPLY, NotificationConstant.FINACIAL.PRECATIVE_TALLY, map, repay.getPrecativeId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(repay.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<CmsProvideSendDTO> provideSend = provideTallyService.queryTallyProvideSend();//发放
        //TODO 排除已记账的数据
        provideSend = distinctSendList(provideSend);
        if (!EmptyUtils.isEmptyList(provideSend)) {
            for (CmsProvideSendDTO sendDTO : provideSend) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(sendDTO.getMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(sendDTO.getId(), ResourceConstant.FCL_PROVIDE_TALLY_APPLY, NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, map, sendDTO.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(sendDTO.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<CmsProvideLoanRepayDTO> provideRepayOnline = provideTallyService.queryTallyProvideRepay("1");//线上
        //TODO 排除已记账的数据
        provideRepayOnline = distinctProvideRepayList(provideRepayOnline);
        if (!EmptyUtils.isEmptyList(provideRepayOnline)) {
            for (CmsProvideLoanRepayDTO repay : provideRepayOnline) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(repay.getOnlineMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(repay.getId(), ResourceConstant.FCL_PROVIDE_TALLY_APPLY, NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, map, repay.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(repay.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<CmsProvideLoanRepayDTO> provideRepayDown = provideTallyService.queryTallyProvideRepay("0");//线下
        //TODO 排除已记账的数据
        provideRepayOnline = distinctProvideRepayList(provideRepayOnline);
        if (!EmptyUtils.isEmptyList(provideRepayDown)) {
            for (CmsProvideLoanRepayDTO repay : provideRepayDown) {
                Map<String, Object> map = new HashMap<String, Object>();
                BigDecimal repayMoney = repay.getRepayMoney().add(repay.getRepayAccrual());
                map.put("money", NumberUtil.formatNumeric(repayMoney, "#,##0.00"));
                notificationService.sendToUserByResPermission(repay.getId(), ResourceConstant.FCL_PROVIDE_TALLY_APPLY, NotificationConstant.FINACIAL.CMSPROVIDE_TALLY, map, repay.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(repay.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<NisAllocationReqDTO> nisReq = nisAllocationReqManualService.queryTallyNisAllReq(12);//请款
        if (!EmptyUtils.isEmptyList(nisReq)) {
            for (NisAllocationReqDTO n : nisReq) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(n.getMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(n.getId(), ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_APPLY, NotificationConstant.FINACIAL.MONEYTRANSFERS_TALLY, map, n.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(n.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<NisAllocationReqDTO> nisHead = nisAllocationReqManualService.queryTallyNisAllReq(13);//头寸
        if (!EmptyUtils.isEmptyList(nisHead)) {
            for (NisAllocationReqDTO n : nisHead) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(n.getMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(n.getId(), ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_APPLY, NotificationConstant.FINACIAL.MONEYTRANSFERS_TALLY, map, n.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(n.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<NisAllocationReqDTO> nisDown = nisAllocationReqManualService.queryTallyNisAllReq(11);//下拨
        if (!EmptyUtils.isEmptyList(nisDown)) {
            for (NisAllocationReqDTO n : nisDown) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(n.getMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(n.getId(), ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_APPLY, NotificationConstant.FINACIAL.MONEYTRANSFERS_TALLY, map, n.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(n.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<NisBillheadDTO> billHead = nisBillheadManualService.queryTallyUnions();//单位申请付款
        if (!EmptyUtils.isEmptyList(billHead)) {
            for (NisBillheadDTO b : billHead) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(b.getPayMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(b.getId(), ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_APPLY, NotificationConstant.FINACIAL.CORPPAYMENT_TALLY, map, b.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(b.getId());
                remindDtls.add(remindDtl);
            }
        }
        //中心代保管票据
        List<com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO> ab1 = abBillTallyService.queryTallyAbAccbook("61");//出入库
        if (!EmptyUtils.isEmptyList(ab1)) {
            for (com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO a : ab1) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_OUT_PUT_TALLY_6, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO> ab2 = abBillTallyService.queryTallyAbAccbook("62");//出入库
        if (!EmptyUtils.isEmptyList(ab2)) {
            for (com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO a : ab2) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_OUT_PUT_TALLY_6, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO> ab3 = abBillTallyService.queryTallyAbAccbook("63");//出入库
        if (!EmptyUtils.isEmptyList(ab3)) {
            for (com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO a : ab3) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_OUT_PUT_TALLY_6, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO> ab4 = abBillTallyService.queryTallyAbAccbook("65");//出入库
        if (!EmptyUtils.isEmptyList(ab4)) {
            for (com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO a : ab4) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_OUT_PUT_TALLY_6, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO> ab5 = abBillTallyService.queryTallyAbAccbook("64");//出入库
        if (!EmptyUtils.isEmptyList(ab5)) {
            for (com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO a : ab5) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_OUT_PUT_TALLY_6, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO> ab6 = abBillTallyService.queryTallyAbAccbook("71");//退票
        if (!EmptyUtils.isEmptyList(ab6)) {
            for (com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO a : ab6) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_REFUND_TALLY_7, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO> ab7 = abBillTallyService.queryTallyAbAccbook("81");//托收
        if (!EmptyUtils.isEmptyList(ab7)) {
            for (com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO a : ab7) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_COLLECTION_TALLY_8, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO> ab8 = abBillTallyService.queryTallyAbAccbook("82");//托收
        if (!EmptyUtils.isEmptyList(ab8)) {
            for (com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO a : ab8) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_COLLECTION_TALLY_8, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO> ab9 = abBillTallyService.queryTallyAbAccbook("91");//背书
        if (!EmptyUtils.isEmptyList(ab9)) {
            for (com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO a : ab9) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_ENDORSE_TALLY_9, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab10 = abBillTallyService.queryTallyAbAccbook("92");//背书
        if (!EmptyUtils.isEmptyList(ab10)) {
            for (AbBillAccbookInfoDTO a : ab10) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_ENDORSE_TALLY_9, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab11 = abBillTallyService.queryTallyAbAccbook("101");//贴现
        if (!EmptyUtils.isEmptyList(ab11)) {
            for (AbBillAccbookInfoDTO a : ab11) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_TALLY_10, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab12 = abBillTallyService.queryTallyAbAccbook("102");//贴现
        if (!EmptyUtils.isEmptyList(ab12)) {
            for (AbBillAccbookInfoDTO a : ab12) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_TALLY_10, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab13 = abBillTallyService.queryTallyAbAccbook("103");//贴现
        if (!EmptyUtils.isEmptyList(ab13)) {
            for (AbBillAccbookInfoDTO a : ab13) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_TALLY_10, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab14 = abBillTallyService.queryTallyAbAccbook("104");//贴现
        if (!EmptyUtils.isEmptyList(ab14)) {
            for (AbBillAccbookInfoDTO a : ab14) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_TALLY_10, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab15 = abBillTallyService.queryTallyAbAccbook("105");//贴现
        if (!EmptyUtils.isEmptyList(ab15)) {
            for (AbBillAccbookInfoDTO a : ab15) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_TALLY_10, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab16 = abBillTallyService.queryTallyAbAccbook("106");//贴现
        if (!EmptyUtils.isEmptyList(ab16)) {
            for (AbBillAccbookInfoDTO a : ab16) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_TALLY_10, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab17 = abBillTallyService.queryTallyAbAccbook("151");//质押
        if (!EmptyUtils.isEmptyList(ab17)) {
            for (AbBillAccbookInfoDTO a : ab17) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_PLEDGE_TALLY_11, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab18 = abBillTallyService.queryTallyAbAccbook("152");//质押
        if (!EmptyUtils.isEmptyList(ab18)) {
            for (AbBillAccbookInfoDTO a : ab18) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_PLEDGE_TALLY_11, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab19 = abBillTallyService.queryTallyAbAccbook("153");//质押
        if (!EmptyUtils.isEmptyList(ab19)) {
            for (AbBillAccbookInfoDTO a : ab19) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_PLEDGE_TALLY_11, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab20 = abBillTallyService.queryTallyAbAccbook("154");//质押
        if (!EmptyUtils.isEmptyList(ab20)) {
            for (AbBillAccbookInfoDTO a : ab20) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_PLEDGE_TALLY_11, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        //中心自身库存
        List<AbBillAccbookInfoDTO> ab21 = abBillTallyService.queryTallyAbAccbook("161");//托收
        if (!EmptyUtils.isEmptyList(ab21)) {
            for (AbBillAccbookInfoDTO a : ab21) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_COLLECTION_SELF_TALLY_12, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab22 = abBillTallyService.queryTallyAbAccbook("162");//托收
        if (!EmptyUtils.isEmptyList(ab22)) {
            for (AbBillAccbookInfoDTO a : ab22) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_COLLECTION_SELF_TALLY_12, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab23 = abBillTallyService.queryTallyAbAccbook("163");//托收
        if (!EmptyUtils.isEmptyList(ab23)) {
            for (AbBillAccbookInfoDTO a : ab23) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_COLLECTION_SELF_TALLY_12, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab24 = abBillTallyService.queryTallyAbAccbook("171");//贴现
        if (!EmptyUtils.isEmptyList(ab24)) {
            for (AbBillAccbookInfoDTO a : ab24) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_TALLY_13, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab25 = abBillTallyService.queryTallyAbAccbook("172");//贴现
        if (!EmptyUtils.isEmptyList(ab25)) {
            for (AbBillAccbookInfoDTO a : ab25) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_TALLY_13, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab26 = abBillTallyService.queryTallyAbAccbook("173");//贴现
        if (!EmptyUtils.isEmptyList(ab26)) {
            for (AbBillAccbookInfoDTO a : ab26) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_TALLY_13, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab27 = abBillTallyService.queryTallyAbAccbook("174");//贴现
        if (!EmptyUtils.isEmptyList(ab27)) {
            for (AbBillAccbookInfoDTO a : ab27) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_TALLY_13, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab28 = abBillTallyService.queryTallyAbAccbook("175");//贴现
        if (!EmptyUtils.isEmptyList(ab28)) {
            for (AbBillAccbookInfoDTO a : ab28) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_TALLY_13, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        //划拨
        List<AbBillAccbookInfoDTO> ab29 = abBillTallyService.queryTallyAbAccbook("181");//划拨
        if (!EmptyUtils.isEmptyList(ab29)) {
            for (AbBillAccbookInfoDTO a : ab29) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_PAPER_TALLY_14, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        List<AbBillAccbookInfoDTO> ab30 = abBillTallyService.queryTallyAbAccbook("182");//划拨
        if (!EmptyUtils.isEmptyList(ab30)) {
            for (AbBillAccbookInfoDTO a : ab30) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBillMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getAid(), ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, NotificationConstant.FINACIAL.ABBILL_PAPER_TALLY_14, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getAid());
                remindDtls.add(remindDtl);
            }
        }
        //资金池
        List<BisAccDtlCopyDTO> bankUp = fcBankAccDtlService.queryTallyBisAccDtl("up");//归集
        if (!EmptyUtils.isEmptyList(bankUp)) {
            String corpId = "";
            for (BisAccDtlCopyDTO a : bankUp) {
                for (FcCorpPoolDTO p : pools) {
                    if (a.getPoolId().equals(p.getPoolId())) {
                        corpId = p.getCorpId();
                        continue;
                    }
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBal(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getId(), ResourceConstant.FCL_POOL_TALLY_APPLY, NotificationConstant.FINACIAL.POOL_TALLY, map, corpId);
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<BisAccDtlCopyDTO> bankDown = fcBankAccDtlService.queryTallyBisAccDtl("down");//下拨
        if (!EmptyUtils.isEmptyList(bankDown)) {
            String corpId = "";
            for (BisAccDtlCopyDTO a : bankDown) {
                for (FcCorpPoolDTO p : pools) {
                    if (a.getPoolId().equals(p.getPoolId())) {
                        corpId = p.getCorpId();
                        continue;
                    }
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getBal(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getId(), ResourceConstant.FCL_POOL_TALLY_APPLY, NotificationConstant.FINACIAL.POOL_TALLY, map, corpId);
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getId());
                remindDtls.add(remindDtl);
            }
        }
        //内部存款
        List<ChoInnerFinanceDTO> inner1 = innerDepositService.queryTallyByBusType("191");//活转定
        if (!EmptyUtils.isEmptyList(inner1)) {
            for (ChoInnerFinanceDTO a : inner1) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getPayMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getId(), ResourceConstant.FINACIAL_INNERDEPOSIT_APPLY, NotificationConstant.FINACIAL.INNER_DEPOSIT_TALLY, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<ChoInnerFinanceDTO> inner2 = innerDepositService.queryTallyByBusType("192");//定转活
        if (!EmptyUtils.isEmptyList(inner2)) {
            for (ChoInnerFinanceDTO a : inner2) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getPayMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getId(), ResourceConstant.FINACIAL_INNERDEPOSIT_APPLY, NotificationConstant.FINACIAL.INNER_DEPOSIT_TALLY, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<ChoInnerFinanceDTO> inner3 = innerDepositService.queryTallyByBusType("193");//活转通
        if (!EmptyUtils.isEmptyList(inner3)) {
            for (ChoInnerFinanceDTO a : inner3) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getPayMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getId(), ResourceConstant.FINACIAL_INNERDEPOSIT_APPLY, NotificationConstant.FINACIAL.INNER_DEPOSIT_TALLY, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getId());
                remindDtls.add(remindDtl);
            }
        }
        List<ChoInnerFinanceDTO> inner4 = innerDepositService.queryTallyByBusType("194");//通转活
        if (!EmptyUtils.isEmptyList(inner4)) {
            for (ChoInnerFinanceDTO a : inner4) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(a.getPayMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(a.getId(), ResourceConstant.FINACIAL_INNERDEPOSIT_APPLY, NotificationConstant.FINACIAL.INNER_DEPOSIT_TALLY, map, a.getCorpId());
                FcTallyRemindDtlDTO remindDtl = new FcTallyRemindDtlDTO();
                remindDtl.setBillId(a.getId());
                remindDtls.add(remindDtl);
            }
        }
        if (remindDtls.size() > 0) {
            tallyService.batchSaveRemindDtl(remindDtls);
        }
    }
    public void deleteByBusId(String busId) {
        tallyService.deleteRemindDtlByBusId(busId);
    }
    //TODO 暂时不做设计，代码复用
    private List<CmsProvideLoanRepayDTO> distinctProvideRepayList(List<CmsProvideLoanRepayDTO> provideRepayOnline) {
        if (EmptyUtils.isEmptyList(provideRepayOnline)) {
            return provideRepayOnline;
        }
        List<String> billIdList = provideRepayOnline.stream().map(CmsProvideLoanRepayDTO::getId).collect(Collectors.toList());
        List<String> tallyRecordListId = fcAccbookServiceImpl.getTallyRecordListId(billIdList);
        if (!EmptyUtils.isEmptyList(tallyRecordListId)) {
            provideRepayOnline = provideRepayOnline.stream().filter(precativeDO -> !tallyRecordListId.contains(precativeDO.getId())).collect(Collectors.toList());
        }
        return provideRepayOnline;
    }
    //TODO 代码与上面重复
    private List<CmsPrecativeLoanRepayDTO> distinctPrecativeRepayList(List<CmsPrecativeLoanRepayDTO> precativeRepay) {
        if (EmptyUtils.isEmptyList(precativeRepay)) {
            return precativeRepay;
        }
        List<String> billIdList = precativeRepay.stream().map(CmsPrecativeLoanRepayDTO::getId).collect(Collectors.toList());
        List<String> tallyRecordListId = fcAccbookServiceImpl.getTallyRecordListId(billIdList);
        if (!EmptyUtils.isEmptyList(tallyRecordListId)) {
            precativeRepay = precativeRepay.stream().filter(precativeDO -> !tallyRecordListId.contains(precativeDO.getId())).collect(Collectors.toList());
        }
        return precativeRepay;
    }
    private List<CmsProvideSendDTO> distinctSendList(List<CmsProvideSendDTO> provideSend) {
        if (EmptyUtils.isEmptyList(provideSend)) {
            return provideSend;
        }
        List<String> billIdList = provideSend.stream().map(CmsProvideSendDTO::getId).collect(Collectors.toList());
        List<String> tallyRecordListId = fcAccbookServiceImpl.getTallyRecordListId(billIdList);
        if (!EmptyUtils.isEmptyList(tallyRecordListId)) {
            provideSend = provideSend.stream().filter(precativeDO -> !tallyRecordListId.contains(precativeDO.getId())).collect(Collectors.toList());
        }
        return provideSend;
    }
}
