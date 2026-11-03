package com.bytter.check.job;

import cn.hutool.core.date.StopWatch;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisPaymentBlendingVO;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.check.dao.PaymentBlendingDao;
import com.bytter.check.dao.RefundBlendingDao;
import com.bytter.check.service.RefundBlendingService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import com.bytter.core.tenant.SmartThreadPoolManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@Transactional
public class RefundBlendingTaskService implements ITaskService {
	static final Logger logger = LoggerFactory.getLogger(RefundBlendingTaskService.class);

    @Autowired(required = false)
    private IConfigService configService;
    @Autowired
    private RefundBlendingDao refundBlendingDao;
    @Autowired(required=false)
    private INisBillheadService nisBillheadService;
    @Autowired(required=false)
    private RefundBlendingService refundBlendingService;
    @Autowired
    private PaymentBlendingDao paymentBlendingDao;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;

    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 定时器调用自动付款勾兑处理线程池
     */
    private static ExecutorService ASYNC_EXECUTOR_REFUNDBLENDINGTASK = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(CPU_NUM*2));

    public void destroy() throws Exception {
        ASYNC_EXECUTOR_REFUNDBLENDINGTASK.shutdown();
    }

    /**
     *退款勾对定时任务
     * @param arguments
     */
    /**
     *
     */
    @XxlJob("RefundBlendingTaskService")
    @Override
    public void run(String arguments) {
        try {
            XxlJobHelper.log("---启用退款勾对，开始执行定时任务---");
            Date curDate = DateUtils.getDate();
            //退款勾对天数
            String refundBlendDays = configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_REFUNDBLENDDAYS).getValue();
            //退款勾对定时任务取数天数
            String timedTasksDays = configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_TIMEDTASKSDAYS).getValue();
            int refundBlendDaysInt = 0;
            if(EmptyUtils.isNotEmpty(refundBlendDays)){
                refundBlendDaysInt = Integer.parseInt(refundBlendDays);
            }
            int timedTasksDaysInt = 30;
            if(EmptyUtils.isNotEmpty(timedTasksDays)){
                timedTasksDaysInt = Integer.parseInt(timedTasksDays);
            }
            Date beginDate = DateUtils.addDay(curDate,-timedTasksDaysInt);
            Date endDate = curDate;
            Date payBeginDate = DateUtils.addDay(beginDate,-refundBlendDaysInt);
            Date payEndDate = DateUtils.addDay(endDate,refundBlendDaysInt);
            StopWatch stopWatch = new StopWatch("退款勾对查询效率");

            stopWatch.start("getDtlRules");
            List<BisPaymentBlendingVO> list =  refundBlendingDao.getDtlRules(beginDate,endDate);
            stopWatch.stop();
            XxlJobHelper.log(" getDtlRules all spends :"+stopWatch.prettyPrint(TimeUnit.MILLISECONDS)+", data size :"+list.size());

            stopWatch.start("getDtl");
            List<BisAccDtlDTO> dtlList = refundBlendingDao.getDtl(beginDate,endDate);
            stopWatch.stop();
            XxlJobHelper.log(" getDtl all spends :"+stopWatch.prettyPrint(TimeUnit.MILLISECONDS)+", data size :"+dtlList.size());

            stopWatch.start("listNisBillheadByRefundblendingNew");
            List<NisBillheadDTO> nisBillheadDTOList = nisBillheadService.listNisBillheadByRefundblending(payBeginDate,payEndDate);
            stopWatch.stop();
            XxlJobHelper.log(" listNisBillheadByRefundblendingNew all spends :"+stopWatch.prettyPrint(TimeUnit.MILLISECONDS)+", data size :"+nisBillheadDTOList.size());

            stopWatch.start("dealData");
            dealData(list,nisBillheadDTOList,dtlList,refundBlendDaysInt,logger);
            stopWatch.stop();
            XxlJobHelper.log(" dealData all spends :"+stopWatch.prettyPrint(TimeUnit.MILLISECONDS));

            XxlJobHelper.log("---启用退款勾对，结束执行定时任务---");
        } catch (Exception e) {
            XxlJobHelper.log(e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void dealData(List<BisPaymentBlendingVO> list,List<NisBillheadDTO> nisBillheadDTOList,List<BisAccDtlDTO> dtlList,int refundBlendDaysInt,Logger logger) {
    	 Map<String,NisBillheadDTO> paybillMap = new HashMap<>();
         String loginName = "admin";
         int count = 0;
         int count1 = 0;
         int count2 = 0;
         logger.info("dealData start");
         logger.error("dealData start");
         List<BisAccDtlDTO> bisAccDtlDTOs = new ArrayList<BisAccDtlDTO>();
         try {
         for(BisPaymentBlendingVO bisPaymentBlendingVO : list){

             //更新autoSign字段
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
             BisAccDtlDTO bisAccDtlTransferVO = new BisAccDtlDTO();
             bisAccDtlTransferVO.setId(bisPaymentBlendingVO.getId());
             bisAccDtlTransferVO.setAutoSign(autoSign);
             bisAccDtlTransferVO.setAgainSign(againSignStr);
             bisAccDtlDTOs.add(bisAccDtlTransferVO);
             int payCount = 0;
             int dtlCount = 0;
             StringBuilder dtlIds = new StringBuilder();
             StringBuilder billIds = new StringBuilder();

             if (bisPaymentBlendingVO.getIsExistId().equals(CommonConstant.VALID_SIGN_NO)) {  //不存在唯一标识
                 String[] bankAccTypeStrs = bisPaymentBlendingVO.getRules().split(CommonConstant.COMMA_SPLIT);
                 String bisPaymenRules = "";
                 for (String s : bankAccTypeStrs) {
                     bisPaymenRules = bisPaymenRules + "," + s;
                 }
                 String key = "";
                 String beginDateTemp = "";
                 String endDateTemp = "";
                 Date transTime = null;
                 NisBillheadDTO tempNisBillhead = null;
                 if (bisPaymenRules.contains("amt")) {
                     key += bisPaymentBlendingVO.getAmt().abs().setScale(2, BigDecimal.ROUND_HALF_UP)+"";
                 }
                 if (bisPaymenRules.contains("bankAcc")) {
                     key += bisPaymentBlendingVO.getBankAcc();
                 }
                 if (bisPaymenRules.contains("transTime")) {
                     Date date = bisPaymentBlendingVO.getTransTime();
                     String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");

                     beginDateTemp = DateUtils.formatDate(DateUtils.addDay(date, -refundBlendDaysInt), "yyyy-MM-dd");
                     endDateTemp = dateStr;
                     transTime = bisPaymentBlendingVO.getTransTime();
                     key += "time";
                 }
                 if (bisPaymenRules.contains("corpId")) {
                     key += bisPaymentBlendingVO.getCorpId();
                 }
                 if (bisPaymenRules.contains("accName")) {
                     key += bisPaymentBlendingVO.getAccName();
                 }
                 if (bisPaymenRules.contains("curId")) {
                     if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getCurId())) {
                         continue;
                     } else {
                         key += bisPaymentBlendingVO.getCurId();
                     }
                 }
                 if (bisPaymenRules.contains("oppAccNo")) {
                     if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getOppAccNo())) {
                         continue;
                     } else {
                         key += bisPaymentBlendingVO.getOppAccNo();
                     }
                 }
                 if (bisPaymenRules.contains("oppAccName")) {
                     if (EmptyUtils.isEmpty(bisPaymentBlendingVO.getOppAccName())) {
                         continue;
                     } else {
                         key += bisPaymentBlendingVO.getOppAccName();
                     }
                 }
                 XxlJobHelper.log("================key="+key);
                 //付款单集合中查询满足条件付款单。
                 for(NisBillheadDTO nisBillheadDTO :nisBillheadDTOList){
                     String payKey =  "";
                     if (bisPaymenRules.contains("amt")) {
                         payKey += nisBillheadDTO.getPayMoney().setScale(2, BigDecimal.ROUND_HALF_UP)+"";
                     }
                     if (bisPaymenRules.contains("bankAcc")) {
                         payKey += nisBillheadDTO.getPayerAcc();
                     }
                     if (bisPaymenRules.contains("transTime")) {
                         Date start = DateUtils.parse(beginDateTemp, "yyyy-MM-dd");
                         Date end = DateUtils.parse(endDateTemp, "yyyy-MM-dd");
                         // 业务系统中，期望支付日期是没有时分秒的，但是erp推过来的单据中期望支付日期有时分秒，导致日期判断有问题
                         Date wishPayday = DateUtils.parseDate(nisBillheadDTO.getWishPayday());
                         if(wishPayday.compareTo(start) >= 0 && wishPayday.compareTo(end) <= 0){
                             payKey += "time";
                         }
                     }
                     if (bisPaymenRules.contains("corpId")) {
                         payKey += nisBillheadDTO.getCorpId();
                     }
                     if (bisPaymenRules.contains("accName")) {
                         payKey += nisBillheadDTO.getPayerAccName();
                     }
                     if (bisPaymenRules.contains("curId")) {
                         payKey += nisBillheadDTO.getCurId();
                     }
                     if (bisPaymenRules.contains("oppAccNo")) {
                         payKey += nisBillheadDTO.getPayeeAcc();
                     }
                     if (bisPaymenRules.contains("oppAccName")) {
                         payKey += nisBillheadDTO.getPayeeAccName();
                     }
                     XxlJobHelper.log("================key="+key+"payKey="+payKey);
                     if(payKey.equals(key)){
                         billIds.append("--").append(nisBillheadDTO.getId());
                         payCount++;
                         tempNisBillhead = nisBillheadDTO;
                     }
                 }
                 //流水集合中查询满足条件的流水
                 for(BisAccDtlDTO bisAccDtlDTO : dtlList){
                     String dtlKey =  "";
                     if (bisPaymenRules.contains("amt")) {
                         dtlKey += bisAccDtlDTO.getAmt().abs().setScale(2, BigDecimal.ROUND_HALF_UP)+"";
                     }
                     if (bisPaymenRules.contains("bankAcc")) {
                         dtlKey += bisAccDtlDTO.getBankAcc();
                     }
                     if (bisPaymenRules.contains("transTime")) {
                         if(transTime.compareTo(bisAccDtlDTO.getTransTime()) >= 0){
                             dtlKey += "time";
                         }
                     }
                     if (bisPaymenRules.contains("corpId")) {
                         dtlKey += bisAccDtlDTO.getCorpId();
                     }
                     if (bisPaymenRules.contains("accName")) {
                         dtlKey += bisAccDtlDTO.getAccName();
                     }
                     if (bisPaymenRules.contains("curId")) {
                         dtlKey += bisAccDtlDTO.getCurId();
                     }
                     if (bisPaymenRules.contains("oppAccNo")) {
                         dtlKey += bisAccDtlDTO.getOppAccNo();
                     }
                     if (bisPaymenRules.contains("oppAccName")) {
                         dtlKey += bisAccDtlDTO.getOppAccName();
                     }
                     XxlJobHelper.log("================key="+key+"dtlKey="+dtlKey);
                     if(dtlKey.equals(key)){
                         dtlIds.append("--").append(bisAccDtlDTO.getId());
                         dtlCount++;
                     }
                 }
                 XxlJobHelper.log("====================payCount="+payCount+"=====dtlCount="+dtlCount);
                 //一对一匹配，勾兑成功
                 if(payCount == 1 && dtlCount == 1){
                     if (!paybillMap.containsKey(tempNisBillhead.getId())) {
                         XxlJobHelper.log("匹配上"+tempNisBillhead.getId());
                         paybillMap.put(tempNisBillhead.getId(),tempNisBillhead);

                         refundBlendingService.toUpdateBlendSignForJob(bisPaymentBlendingVO.getId(), BtBankAccConstant.BLENDSIGN_1,loginName);
                         if (StringUtils.isNotEmpty(bisPaymentBlendingVO.getBankSerialNo())) {
                             refundBlendingService.toUpdateBisExc(tempNisBillhead.getId(), bisPaymentBlendingVO.getBankSerialNo());
                         } else {
                             refundBlendingService.toUpdateBankSerialNo(bisPaymentBlendingVO.getId(), bisPaymentBlendingVO.getId());
                             refundBlendingService.toUpdateBisExc(tempNisBillhead.getId(), bisPaymentBlendingVO.getId());
                         }
                         //更新流水的共享单号
                         String sourceNum;
                         if (StringUtils.isNotEmpty(tempNisBillhead.getBursarBillNo())) {
                             sourceNum = tempNisBillhead.getBursarBillNo();
                         }else{
                             sourceNum = tempNisBillhead.getBillNo();
                         }
                         refundBlendingService.toUpdateSourceNum(bisPaymentBlendingVO.getId(), sourceNum);
                         //插入退款勾兑中间表
                         paymentBlendingDao.insetBillBankSerialNoRefund(tempNisBillhead.getId(), "nis_billhead", bisPaymentBlendingVO.getId(), bisPaymentBlendingVO.getId());
                         count++;
                     }
                 }else{
                     XxlJobHelper.log("一对一匹配不成功"+count1);
                     count1++;
                     if(payCount > 1 || dtlCount > 1){
                         XxlJobHelper.log("---退款勾对日志，未勾兑不符合一对一匹配ID："+ bisPaymentBlendingVO.getId()
                                 +"---"+"流水数量："+dtlCount+",dtlIds:"+dtlIds.toString()
                                 +"---"+"付款单数量："+payCount+",billIds:"+billIds.toString());
                         count2++;
                     }
                     XxlJobHelper.log("END");
                 }

             }
         }

         //更新已经勾兑过的
         bisAccDtlService.updateBisAccDtlDTOByAutoAndAgainSign(bisAccDtlDTOs);
         }catch(Exception e) {
        	 e.printStackTrace();
             XxlJobHelper.log("e.getMessage()="+e.getMessage());
         }finally {
             XxlJobHelper.log("dealData end");
             XxlJobHelper.log("---退款勾对日志，已勾兑数量："+ count +"，未勾兑数量："+ count1 +"，其中不符合一对一匹配数量："+ count2 +"---");
         }

    }
}
