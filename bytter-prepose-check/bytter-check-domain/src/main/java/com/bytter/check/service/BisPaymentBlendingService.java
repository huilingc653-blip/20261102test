package com.bytter.check.service;

import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisPaymentBlendingVO;
import com.bytter.biz.payment.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.VNisPayBillDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.biz.payment.api.vo.VNisPayBillheadVO;
import com.bytter.check.dao.PaymentBlendingDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BisAccDtlDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BisPaymentBlendingService  implements ITaskService {
    @Autowired
    private PaymentBlendingDao paymentBlendingDao;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private PaymentBlendingService paymentBlendingService;
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 定时器调用自动付款勾兑
     */
    @XxlJob("BisPaymentBlendingService")
    @Override
    public void run(String arguments) {
        try {
            Date curDate = DateUtils.getDate();
            log.info(curDate + "---启用付款明细勾对，开始执行定时任务---");
            DataTablesInput input1 = new DataTablesInput();
            //一次默认600条
            int size = 600;
            //后台任务每批次执行笔数
            String autoCount = configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_AUTOCOUNT).getValue();
            if(EmptyUtils.isNotEmpty(autoCount)){
                size = Integer.parseInt(autoCount);
            }
            input1.setLength(size);
            //手动调拨单是否要与付款明细勾对

            String allocationAllowBlending = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ALLOCATIONALLOWBLENDING).getValue();
            String rollMateStr = configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_ROLLMATE).getValue();
            //默认滚动3天
            int rollMate = 3;
            if(EmptyUtils.isNotEmpty(rollMateStr)){
                rollMate = Integer.parseInt(rollMateStr);
            }
            DataTablesOutput out = this.paymentBlendingDao.getDtlRules(input1);
            List<BisPaymentBlendingVO> list = out.getData();
            Map<String, VNisPayBillheadVO> paybillMap = new HashMap<>();
            for (BisPaymentBlendingVO bisPaymentBlendingVO : list) {
                this.autoBisPaymentBlending(bisPaymentBlendingVO, allocationAllowBlending,paybillMap,rollMate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 单笔自动付款明细勾对方法
     * @param bisPaymentBlendingVO
     * @param allocationAllowBlending
     * @param paybillMap
     * @param rollMate
     * @throws Exception
     */
    public void autoBisPaymentBlending(BisPaymentBlendingVO bisPaymentBlendingVO,String allocationAllowBlending,Map<String,VNisPayBillheadVO> paybillMap,int rollMate) throws Exception {
//        Date transTime = bisPaymentBlendingVO.getTransTime();
//        transTime = DateUtils.addDay(transTime,rollMate);
//        Date nowDate = DateUtils.getDate();
//        if(transTime.compareTo(nowDate) < 0) {
            //更新是否自动执行标志
//        }
        int againSign = 1;
        String autoSign = "0";
        if(EmptyUtils.isNotEmpty(bisPaymentBlendingVO.getAgainSign())){
            againSign = Integer.parseInt(bisPaymentBlendingVO.getAgainSign());
            againSign = againSign +1;
            if(againSign>=3){
                againSign = 3;
                autoSign = "1";
            }
        }
        String againSignStr = againSign+"";
        paymentBlendingDao.toUpdateAutoSign(bisPaymentBlendingVO.getId(), autoSign,againSignStr);
        if (bisPaymentBlendingVO.getRules() != null) {
            DataTablesInput input = new DataTablesInput();
            DataTablesInput bisinput = new DataTablesInput();
            if (bisPaymentBlendingVO.getIsExistId().equals(CommonConstant.VALID_SIGN_NO)) {  //不存在唯一标识
                String[] bankAccTypeStrs = bisPaymentBlendingVO.getRules().split(CommonConstant.COMMA_SPLIT);
                String bisPaymenRules = "";
                for (String s : bankAccTypeStrs) {
                    bisPaymenRules = bisPaymenRules + "," + s;
                }
                if (bisPaymenRules.contains("amt")) {
                    bisinput.getFormdata().put("amt", bisPaymentBlendingVO.getAmt());
                    input.getFormdata().put("eqg_payMoney", bisPaymentBlendingVO.getAmt());
                }
                if (bisPaymenRules.contains("bankAcc")) {
                    bisinput.getFormdata().put("bankAcc", bisPaymentBlendingVO.getBankAcc());
                    input.getFormdata().put("eqs_payerAcc", bisPaymentBlendingVO.getBankAcc());
                }
                if (bisPaymenRules.contains("transTime")) {
                    Date date = bisPaymentBlendingVO.getTransTime();
                    String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
                    String beginDate = dateStr;
                    //相差天数
                    Integer transDays = bisPaymentBlendingVO.getTransDays();
                    if (transDays != null) {
                        beginDate = DateUtils.formatDate(DateUtils.addDay(date, -transDays), "yyyy-MM-dd");
                    }
                    String endDate = dateStr;
                    bisinput.getFormdata().put("transTime", dateStr);
                    input.getFormdata().put("ged_wishPayday", beginDate);
                    input.getFormdata().put("led_wishPayday", endDate);
                }
                if (bisPaymenRules.contains("corpId")) {
                    bisinput.getFormdata().put("corpId", bisPaymentBlendingVO.getCorpId());
                    input.getFormdata().put("eqs_corpId", bisPaymentBlendingVO.getCorpId());
                }
                if (bisPaymenRules.contains("accName")) {
                    bisinput.getFormdata().put("accName", bisPaymentBlendingVO.getAccName());
                    input.getFormdata().put("eqs_payerAccName", bisPaymentBlendingVO.getAccName());
                }
                //如果流水某些字段为空，则不匹配
                if(bisPaymentBlendingVO.getCdSign().equals(CommonConstant.YES)){   //收支属性为付款
                    bisinput.getFormdata().put("cdSign", bisPaymentBlendingVO.getCdSign());
                }else {
                    return;
                }

                if (bisPaymenRules.contains("curId")) {
                    if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getCurId())) {
                        return;
                    } else {
                        bisinput.getFormdata().put("curId", bisPaymentBlendingVO.getCurId());
                        input.getFormdata().put("eqs_curId", bisPaymentBlendingVO.getCurId());
                    }
                }
                if (bisPaymenRules.contains("oppAccNo")) {
                    if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getOppAccNo())) {
                        return;
                    } else {
                        bisinput.getFormdata().put("oppAccNo", bisPaymentBlendingVO.getOppAccNo());
                        input.getFormdata().put("eqs_payeeAcc", bisPaymentBlendingVO.getOppAccNo());
                    }
                }
                if (bisPaymenRules.contains("uses")) {
                    if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getUses())) {
                        return;
                    } else {
                        bisinput.getFormdata().put("uses", bisPaymentBlendingVO.getUses());
                        input.getFormdata().put("eqs_purpose", bisPaymentBlendingVO.getUses());
                    }
                }
                if (bisPaymenRules.contains("oppAccName")) {
                    if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getOppAccName())) {
                        return;
                    } else {
                        bisinput.getFormdata().put("oppAccName", bisPaymentBlendingVO.getOppAccName());
                        input.getFormdata().put("eqs_payeeAccName", bisPaymentBlendingVO.getOppAccName());
                    }
                }
                DataTablesOutput output = paymentBlendingDao.getDataTablesUnionForDtl(input,allocationAllowBlending);
                boolean flag = paymentBlendingDao.getBisAccDtlDataTable(bisinput);
                boolean isMatching = false;
                //只能一对一勾兑
                if (output.getData().size() == 1 && flag) {
                    isMatching = true;
                }
                if(isMatching){
                    List<VNisPayBillheadVO> li = output.getData();
                    //如果付款单字段为空，则不匹配
                    if (bisPaymenRules.contains("curId") && EmptyUtils.isEmpty(li.get(0).getCurId())) {
                        return;
                    }
                    if (bisPaymenRules.contains("oppAccName") && EmptyUtils.isEmpty(li.get(0).getPayeeAccName())) {
                        return;
                    }
                    if (bisPaymenRules.contains("uses") && EmptyUtils.isEmpty(li.get(0).getPurpose())) {
                        return;
                    }
                    if (bisPaymenRules.contains("oppAccNo") && EmptyUtils.isEmpty(li.get(0).getPayeeAcc())) {
                        return;
                    }
                    for(int i=0;i<li.size();i++) {
                        if (!paybillMap.containsKey(li.get(i).getId())) {
                            paybillMap.put(li.get(i).getId(), li.get(i));
                            int count = this.toUpdateBlendSign(bisPaymentBlendingVO.getId(), BtBankAccConstant.BLENDSIGN_1);
                            if (count > 0) {
                                this.toUpdateBisExc(li.get(i).getId(), bisPaymentBlendingVO.getId(), li.get(i).getIsFrom(), bisPaymentBlendingVO.getId());
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (bisPaymentBlendingVO.getIsExistId().equals(CommonConstant.VALID_SIGN_YES)) {   //存在唯一标识
            String dtlOnlyId = bisPaymentBlendingVO.getDtlOnlyId();   //流水
            String billOnlyId = bisPaymentBlendingVO.getBillOnlyId();//付款单
            String dtlOnlyValue = "";
            //BANK_SERIAL_NO,BIS_DTL_NO,ACC_NO
            if (dtlOnlyId.equals("BIS_DTL_NO")) {
                if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getBisDtlNo())) {
                    return;
                } else {
                    dtlOnlyValue = bisPaymentBlendingVO.getBisDtlNo();
                }
            }else if(dtlOnlyId.equals("BANK_SERIAL_NO")){
                if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getBankSerialNo())) {
                    return;
                } else {
                    dtlOnlyValue = bisPaymentBlendingVO.getBankSerialNo();
                }
            }else if(dtlOnlyId.equals("ACC_NO")){
                if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getAccNo())) {
                    return;
                } else {
                    dtlOnlyValue = bisPaymentBlendingVO.getAccNo();
                }
            }
            //只能一对一勾兑
            if (dtlOnlyValue != null) {
                List<VNisPayBillDTO> li = paymentBlendingDao.getBillhead(billOnlyId,dtlOnlyValue,allocationAllowBlending);
                List<BisAccDtlDTO> bisAcc = paymentBlendingDao.getBisacc(dtlOnlyId,dtlOnlyValue);
                if (li.size() == 1 && bisAcc.size() == 1) {
                    //如果付款单特定字段为空，则不匹配
                    if (dtlOnlyId.equals("BIS_DTL_NO") && EmptyUtils.isEmpty(li.get(0).getBisDtlNo())) {
                        return;
                    }
                    int count = this.toUpdateBlendSign(bisPaymentBlendingVO.getId(), BtBankAccConstant.BLENDSIGN_1);
                    if(count>0) {
                        this.toUpdateBisExc(li.get(0).getId(), bisPaymentBlendingVO.getId(), li.get(0).getIsFrom(), bisPaymentBlendingVO.getId());
                    }
                }
            }
        }
    }

    /**
     * 更新流水勾兑状态
     *
     * @param id
     * @param blendSign
     * @return
     */
    public int toUpdateBlendSign(String id, String blendSign) throws Exception {
        int result = paymentBlendingDao.toUpdateBlendSign(id, blendSign);
        if(result>0){
            log.info("更新流水勾兑状态：Id="+id+",blendSign="+blendSign);
        }
        return result;
    }

    /**
     * 更新指令与付款单的流水号
     *
     * @param id
     * @param bankSerialNo
     * @return
     */
    public void toUpdateBisExc(String id, String bankSerialNo, String isFrom, String dtlId) {
        log.error("插入中间表：Id="+id+",dtlId="+dtlId+",isFrom="+isFrom);
        //多笔明细可以勾兑一笔付款单，插入中间表
        this.paymentBlendingDao.insetBillBankSerialNo(id, isFrom, bankSerialNo, dtlId);
        //更新单据勾对标志
        paymentBlendingDao.updateBillBlendSign(id,isFrom,BtBankAccConstant.BLENDSIGN_1);
    }
}
