package com.bytter.biz.job;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.dto.AbAccrecordsDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.netbank.entity.NisDemand;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.netbank.vo.NisBillheadVO;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.api.ICmsProvideLoanService;
import com.bytter.finance.dto.CmsProvideInterestDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author luoyj
 * @date 2024/4/25.
 * 生成资金需求清单
 */
@Slf4j
@Component
public class GenerateFundingRequirementsJob implements ITaskService {

    @Autowired
    private NisBillheadService nisBillheadService;
    @Autowired
    private NisDemandService nisDemandService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IAccebillService accebillService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private ICmsProvideLoanService cmsProvideLoanService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private ICalendarService calendarService;

    @DubboReference(check = false)
    private IUserService userService;


    @XxlJob("GenerateFundingRequirements")
    @Override
    public void run(String arguments) {

        log.info("Execute task: GenerateFundingRequirements");
        XxlJobHelper.log("开始运行：生成资金需求清单");
        // 校验当前日期是否工作日，是工作日才执行
        Integer workDaySign = calendarService.getWorkDaySignByTime(new Date());
        if (ObjectUtil.isNotEmpty(workDaySign) && 1 == workDaySign) {
            List<NisDemand> list = new ArrayList<>();
            Map<String, BtBankAccDTO> bankAccMap = new HashMap<>();
            List<BtBankAccDTO> btBankAccDTOList = btBankAccService.getBankAccList();
            for (BtBankAccDTO btBankAccDTO : btBankAccDTOList) {
                //存入是
                if ("1".equals(btBankAccDTO.getIsDemand())) {
                    bankAccMap.put(btBankAccDTO.getId(), btBankAccDTO);
                }
            }
            //满足条件的付款单
            List<NisBillheadVO> billheadList = nisBillheadService.listForFundingRequirements();
            List<NisDemand> nisBillHeadTempList = this.setNisDemandByNisBillhead(billheadList, bankAccMap);
            if (EmptyUtils.isNotEmpty(nisBillHeadTempList)) {
                XxlJobHelper.log("生成资金需求清单任务：满足条件的付款单共【{}】笔", nisBillHeadTempList.size());
                list.addAll(nisBillHeadTempList);
            }else{
                XxlJobHelper.log("生成资金需求清单任务：没有满足条件的付款单");
            }
            //满足条件的承兑到期
            List<AbAccrecordsDTO> abList = accebillService.listTaskBillForNisDemand();
            List<NisDemand> abTempList = this.setNisDemandByAb(abList, bankAccMap);
            if (EmptyUtils.isNotEmpty(abTempList)) {
                XxlJobHelper.log("生成资金需求清单任务：满足条件的承兑到期共【{}】笔", abTempList.size());
                list.addAll(abTempList);
            }else{
                XxlJobHelper.log("生成资金需求清单任务：没有满足条件的承兑到期");
            }

            //满足条件的融资到期
            List<CmsProvideInterestDTO> cmsList = cmsProvideLoanService.listTaskRepayForNisDemand();
            List<NisDemand> cmsTempList = this.setNisDemandByCms(cmsList, bankAccMap);
            if (EmptyUtils.isNotEmpty(cmsTempList)) {
                XxlJobHelper.log("生成资金需求清单任务：满足条件的融资到期共【{}】笔", cmsTempList.size());
                list.addAll(cmsTempList);
            }else{
                XxlJobHelper.log("生成资金需求清单任务：没有满足条件的融资到期");
            }

            //批量保存需求单
            if (EmptyUtils.isNotEmpty(list)) {
                nisDemandService.batchSave(list);
            }
        }else{
            XxlJobHelper.log("生成资金需求清单任务停止：当前为非工作日。");
        }
        XxlJobHelper.log("结束运行：生成资金需求清单");
    }

    /**
     * 根据付款单生成需求单
     * @param nisBillheadVOList
     * @return
     */
    private List<NisDemand> setNisDemandByNisBillhead(List<NisBillheadVO> nisBillheadVOList,Map<String,BtBankAccDTO> bankAccMap){
        List<NisDemand> list = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(nisBillheadVOList)){
            ICurrentUser user = userService.getByLoginName("admin");
            for(NisBillheadVO nisBillheadVO : nisBillheadVOList){
                if(!EmptyUtils.isEmpty(nisBillheadVO)) {
                    NisDemand entity = new NisDemand();
                    //状态是否为已确定
                    Boolean isCheck = false;
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_B));
                    //资金需求类型 为01 付款
                    entity.setDemandType(NisDemandConstant.DEMANDTYPE_01);
                    //账号信息
                    BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(nisBillheadVO.getPayerAcc(),nisBillheadVO.getCurId());
                    if(!EmptyUtils.isEmpty(btBankAccDTO) && bankAccMap.containsKey(btBankAccDTO.getId())) {
                        entity.setCorpId(btBankAccDTO.getCorpId());
                        entity.setBankAcc(btBankAccDTO.getBankAcc());
                        entity.setBankAccId(btBankAccDTO.getId());
                        entity.setAccName(btBankAccDTO.getAccName());
                        entity.setBankName(btBankAccDTO.getBankName());
                        //单位信息
                        CorpDTO corpDTO = corpService.get(btBankAccDTO.getCorpId());
                        if(!EmptyUtils.isEmpty(corpDTO)){
                            String parentids = corpDTO.getParentIds();
                            String[] parentId = parentids.split(",");
                            entity.setCorpLevel(parentId.length);
                        }
                        CorpDTO parentCorpDTO = corpService.getParentById(btBankAccDTO.getCorpId());
                        //设置上级单位
                        if(!EmptyUtils.isEmpty(parentCorpDTO)){
                            entity.setSuperCorpId(parentCorpDTO.getId());
                        }
                        //上级账号信息
                        if(EmptyUtils.isNotEmpty(btBankAccDTO.getPrentAccId())) {
                            BtBankAccDTO superBankAcc = btBankAccService.getBankAccById(btBankAccDTO.getPrentAccId());
                            if (!EmptyUtils.isEmpty(superBankAcc)) {
                                isCheck = true;
                                entity.setSuperBankAccId(superBankAcc.getId());
                                entity.setSuperBankAcc(superBankAcc.getBankAcc());
                                entity.setSuperBankName(superBankAcc.getBankName());
                                entity.setSuperAccName(superBankAcc.getAccName());
                                entity.setSuperCorpId(superBankAcc.getCorpId());
                            }
                        }
                    }else{
                        //没有账号信息的不生成需求清单
                        continue;
                    }
                    if(nisBillheadVO.getWishPayday().after(DateUtils.getDate())) {
                        entity.setDemandDate(nisBillheadVO.getWishPayday());
                    }else{
                        entity.setDemandDate(DateUtils.getDateYMD());
                    }
                    //判断需求日期是否是节假日
                    Date date = entity.getDemandDate();
                    Date nextDate = calendarService.checkWorkDay(date,true);
                    entity.setDemandDate(nextDate);

                    entity.setDemandMoney(nisBillheadVO.getPayMoney());
                    entity.setDemandAllocationMoney(nisBillheadVO.getPayMoney());
                    //付款单据信息
                    entity.setPayBillId(nisBillheadVO.getId());
                    entity.setPayBillNo(nisBillheadVO.getBillNo());
                    entity.setPayBillDate(nisBillheadVO.getWishPayday());
                    //状态
                    if(isCheck) {
                        entity.setStatus(NisDemandConstant.STATUE_CHECK);
                    }else{
                        entity.setStatus(NisDemandConstant.STATUE_TEMP);
                    }
                    entity.setIsConverge(CommonConstant.NO);
                    entity.setIsPos(CommonConstant.NO);
                    entity.setIsSummary(CommonConstant.NO);
                    if(user != null){
                        entity.setCreateBy(user.getLoginName());
                    }else{
                        entity.setCreateBy(null);
                    }
                    entity.setCreateDate(DateUtils.getDate());
                    list.add(entity);
                }
            }
        }
        return list;
    }

    /**
     * 根据承兑到期生成需求单
     * @param abList
     * @return
     */
    private List<NisDemand> setNisDemandByAb(List<AbAccrecordsDTO> abList,Map<String,BtBankAccDTO> bankAccMap){
        List<NisDemand> list = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(abList)){
            ICurrentUser user = userService.getByLoginName("admin");
            for(AbAccrecordsDTO abAccrecordsDTO : abList){
                if(!EmptyUtils.isEmpty(abAccrecordsDTO)) {
                    NisDemand entity = new NisDemand();
                    //状态是否为已确定
                    Boolean isCheck = false;
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_A));
                    //资金需求类型 为02-承兑到期
                    entity.setDemandType(NisDemandConstant.DEMANDTYPE_02);
                    BtCurrencyDTO rmbCurrentcy = btCurrencyService.getRMBCurrency();
                    //账号信息
                    BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(abAccrecordsDTO.getBankAcc(),rmbCurrentcy.getId());
                    if(!EmptyUtils.isEmpty(btBankAccDTO)&& bankAccMap.containsKey(btBankAccDTO.getId())) {
                        entity.setCorpId(btBankAccDTO.getCorpId());
                        entity.setBankAcc(btBankAccDTO.getBankAcc());
                        entity.setBankAccId(btBankAccDTO.getId());
                        entity.setAccName(btBankAccDTO.getAccName());
                        entity.setBankName(btBankAccDTO.getBankName());
                        //单位信息
                        CorpDTO corpDTO = corpService.get(btBankAccDTO.getCorpId());
                        if(!EmptyUtils.isEmpty(corpDTO)){
                            String parentids = corpDTO.getParentIds();
                            String[] parentId = parentids.split(",");
                            entity.setCorpLevel(parentId.length);
                        }
                        CorpDTO parentCorpDTO = corpService.getParentById(btBankAccDTO.getCorpId());
                        //设置上级单位
                        if(!EmptyUtils.isEmpty(parentCorpDTO)){
                            entity.setSuperCorpId(parentCorpDTO.getId());
                        }
                        //上级账号信息
                        if(EmptyUtils.isNotEmpty(btBankAccDTO.getPrentAccId())) {
                            BtBankAccDTO superBankAcc = btBankAccService.getBankAccById(btBankAccDTO.getPrentAccId());
                            if (!EmptyUtils.isEmpty(superBankAcc)) {
                                isCheck = true;
                                entity.setSuperBankAccId(superBankAcc.getId());
                                entity.setSuperBankAcc(superBankAcc.getBankAcc());
                                entity.setSuperBankName(superBankAcc.getBankName());
                                entity.setSuperAccName(superBankAcc.getAccName());
                                entity.setSuperCorpId(superBankAcc.getCorpId());
                            }
                        }
                    }else{
                        //没有账号信息的不生成需求清单
                        continue;
                    }
                    BigDecimal money = abAccrecordsDTO.getBillMoney();
                    //计算的需求金额按百位向上取整
                    if(money.compareTo(new BigDecimal(100)) > 0) {
                        money = money.divide(new BigDecimal(100), 0, BigDecimal.ROUND_UP);
                        money = money.multiply(new BigDecimal(100));
                    }else{
                        money = new BigDecimal(100);
                    }
//                    entity.setDemandDate(abAccrecordsDTO.getEndDate());
                    //工作日验证
//                    Date endDate = entity.getDemandDate();
//                    Date beginDate = DateUtils.addDay(endDate,-7);
//                    CalendarDTO calendarDTO = calendarService.getWorkDayByTime(beginDate,endDate,true);
//                    List<SysCalendarDTO> datelist = calendarDTO.getWorkDayList();
//                    Date nextDate = endDate;
//                    if(EmptyUtils.isNotEmpty(datelist) && datelist.size()>0){
//                        nextDate = datelist.get(datelist.size()-1).getCreateDate();
//                    }
//                    entity.setDemandDate(nextDate);
                    //需求日期取当天
                    entity.setDemandDate(DateUtils.getDateYMD());

                    entity.setDemandMoney(money);
                    entity.setDemandAllocationMoney(money);
                    //付款单据信息
                    entity.setPayBillId(abAccrecordsDTO.getId());
                    entity.setPayBillNo(abAccrecordsDTO.getOrigNo());
                    entity.setPayBillDate(abAccrecordsDTO.getEndDate());

                    //状态
                    if(isCheck){
                        entity.setStatus(NisDemandConstant.STATUE_CHECK);
                    }else{
                        entity.setStatus(NisDemandConstant.STATUE_TEMP);
                    }
                    entity.setIsConverge(CommonConstant.NO);
                    entity.setIsPos(CommonConstant.NO);
                    entity.setIsSummary(CommonConstant.NO);
                    if(user != null){
                        entity.setCreateBy(user.getLoginName());
                    }else {
                        entity.setCreateBy(null);
                    }
                    entity.setCreateDate(DateUtils.getDate());
                    list.add(entity);
                }
            }
        }
        return list;
    }

    /**
     * 根据借款还款计划生成需求单
     * @param cmsList
     * @return
     */
    private List<NisDemand> setNisDemandByCms(List<CmsProvideInterestDTO> cmsList,Map<String,BtBankAccDTO> bankAccMap){
        List<NisDemand> list = new ArrayList<>();
        if(EmptyUtils.isNotEmpty(cmsList)){
            ICurrentUser user = userService.getByLoginName("admin");
            for(CmsProvideInterestDTO cmsProvideInterestDTO : cmsList){
                if(!EmptyUtils.isEmpty(cmsProvideInterestDTO)) {
                    NisDemand entity = new NisDemand();
                    //状态是否为已确定
                    Boolean isCheck = false;
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_C));
                    //资金需求类型 为03-融资到期
                    entity.setDemandType(NisDemandConstant.DEMANDTYPE_03);
                    //单位信息
                    CorpDTO corpDTO = corpService.get(cmsProvideInterestDTO.getCorpId());
                    if(!EmptyUtils.isEmpty(corpDTO)){
                        String parentids = corpDTO.getParentIds();
                        String[] parentId = parentids.split(",");
                        entity.setCorpLevel(parentId.length);
                    }
                    entity.setCorpId(cmsProvideInterestDTO.getCorpId());
                    CorpDTO parentCorpDTO = corpService.getParentById(cmsProvideInterestDTO.getCorpId());
                    //设置上级单位
                    if(!EmptyUtils.isEmpty(parentCorpDTO)){
                        entity.setSuperCorpId(parentCorpDTO.getId());
                    }
                    //账号信息
                    BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(cmsProvideInterestDTO.getOnlinePaymentAccId());
                    if(!EmptyUtils.isEmpty(btBankAccDTO) && bankAccMap.containsKey(btBankAccDTO.getId())) {

                        entity.setBankAcc(btBankAccDTO.getBankAcc());
                        entity.setBankAccId(btBankAccDTO.getId());
                        entity.setAccName(btBankAccDTO.getAccName());
                        entity.setBankName(btBankAccDTO.getBankName());
                        //上级账号信息
                        if(EmptyUtils.isNotEmpty(btBankAccDTO.getPrentAccId())) {
                            BtBankAccDTO superBankAcc = btBankAccService.getBankAccById(btBankAccDTO.getPrentAccId());
                            if (!EmptyUtils.isEmpty(superBankAcc)) {
                                isCheck = true;
                                entity.setSuperBankAccId(superBankAcc.getId());
                                entity.setSuperBankAcc(superBankAcc.getBankAcc());
                                entity.setSuperBankName(superBankAcc.getBankName());
                                entity.setSuperAccName(superBankAcc.getAccName());
                                entity.setSuperCorpId(superBankAcc.getCorpId());
                            }
                        }
                    }else{
                        //没有账号信息的不生成需求清单
                        continue;
                    }
                    entity.setDemandDate(DateUtils.getDateYMD());
                    //工作日验证
                    /*Date endDate = entity.getDemandDate();
                    Date beginDate = DateUtils.addDay(endDate,-7);
                    CalendarDTO calendarDTO = calendarService.getWorkDayByTime(beginDate,endDate,true);
                    List<SysCalendarDTO> datelist = calendarDTO.getWorkDayList();
                    Date nextDate = endDate;
                    if(EmptyUtils.isNotEmpty(datelist) && datelist.size()>0){
                        nextDate = datelist.get(datelist.size()-1).getCreateDate();
                    }
                    entity.setDemandDate(nextDate);*/

                    BigDecimal demandMoney = BigDecimal.ZERO;
                    if (ObjectUtil.isNotEmpty(cmsProvideInterestDTO.getRepayMoney()) && cmsProvideInterestDTO.getRepayMoney().compareTo(BigDecimal.ZERO) > 0){
                        demandMoney = cmsProvideInterestDTO.getRepayMoney();
                    }
                    if(!EmptyUtils.isEmpty(cmsProvideInterestDTO.getPayInterest()) && cmsProvideInterestDTO.getPayInterest().compareTo(BigDecimal.ZERO) > 0){
                        demandMoney = demandMoney.add(cmsProvideInterestDTO.getPayInterest());
                    }
                    // 如果demandMoney = 0 则不生成需求单
                    if(demandMoney.compareTo(BigDecimal.ZERO) == 0){
                        continue;
                    }
                    //计算的需求金额按百位向上取整
                    if(demandMoney.compareTo(new BigDecimal(100)) > 0) {
                        demandMoney = demandMoney.divide(new BigDecimal(100), 0, BigDecimal.ROUND_UP);
                        demandMoney = demandMoney.multiply(new BigDecimal(100));
                    }else{
                        demandMoney = new BigDecimal(100);
                    }
                    entity.setDemandMoney(demandMoney);
                    entity.setDemandAllocationMoney(demandMoney);
                    //付款单据信息
                    entity.setPayBillId(cmsProvideInterestDTO.getId());
                    entity.setPayBillNo(cmsProvideInterestDTO.getBillCode());
                    entity.setPayBillDate(cmsProvideInterestDTO.getRepayDate());

                    //状态
                    if(isCheck){
                        entity.setStatus(NisDemandConstant.STATUE_CHECK);
                    }else {
                        entity.setStatus(NisDemandConstant.STATUE_TEMP);
                    }
                    entity.setIsConverge(CommonConstant.NO);
                    entity.setIsPos(CommonConstant.NO);
                    entity.setIsSummary(CommonConstant.NO);
                    if(user != null){
                        entity.setCreateBy(user.getLoginName());
                    }else {
                        entity.setCreateBy(null);
                    }
                    entity.setCreateDate(DateUtils.getDate());
                    list.add(entity);
                }
            }
        }
        return list;
    }

}
