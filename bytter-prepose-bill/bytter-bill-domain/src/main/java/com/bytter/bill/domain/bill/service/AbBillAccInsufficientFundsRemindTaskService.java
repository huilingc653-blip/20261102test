package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbBillAccInsufficientFundsRemindTaskDao;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.core.base.AbstractService;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IEquityMaintenanceService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.EquityMaintenanceDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhous
 * @Description 承兑到期和借款汇总余额不足提醒
 * @date 2023/9/28 13:45
 */
@Service("abBillAccInsufficientFundsRemindTaskService")
public class AbBillAccInsufficientFundsRemindTaskService extends AbstractService implements ITaskService {

    @Autowired
    private AbCalendarService abCalendarService;

    @Autowired
    private AbBillAccInsufficientFundsRemindTaskDao abBillAccInsufficientFundsRemindTaskDao;

    @Autowired
    private AbCorpService abCorpService;

    @DubboReference(check = false)
    private IBisAccBalService iBisAccBalService;

    @DubboReference(check = false)
    private IEquityMaintenanceService iEquityMaintenanceService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @Override
    @XxlJob("AbBillAccInsufficientFundsTaskService")
    public void run(String arguments) {
        try {
            AccInsufficientFundsRemind();
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void AccInsufficientFundsRemind() {
        this.logger.info("---承兑到期和借款汇总余额不足提醒，开始执行定时任务---");
        List<AbAccrecordsVO> acInfoList=null;
        Map<String,Object> params=new HashMap<>();
        Date nextDay = DateUtils.addDay(DateUtils.getDateYMD(),1);
        Date workDay = abCalendarService.checkWorkDay(nextDay, true);
        params.put("endDate1", nextDay);
        params.put("endDate2", DateUtils.addDay(workDay,-1));
        //是否范围查询
        boolean isRangeQuery = DateUtils.compareDate(nextDay, workDay) != 0;
        //统计还款的账户
        acInfoList = abBillAccInsufficientFundsRemindTaskDao.listAccInsufficientFundsReminder(params,isRangeQuery);
        if(acInfoList==null||acInfoList.isEmpty()){
            this.logger.info("---承兑到期和借款汇总余额不足提醒，【0】条数据。");
        }else{
            List<String> corpIdList = acInfoList.stream().map(AbAccrecordsVO::getCorpId).collect(Collectors.toList());
            List<String> bankAccList = acInfoList.stream().map(AbAccrecordsVO::getBankAcc).collect(Collectors.toList());
            Map<String, CorpDTO> corpDTOMap = abCorpService.listCorpDtoByCorpIds(corpIdList);
            List<BisAccBalDTO> bankBalList = iBisAccBalService.getBankBal(StringUtils.join(bankAccList, ","), DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            Map<String, BisAccBalDTO> balDTOMap= null;
            if(!CollectionUtil.isEmpty(bankBalList)){
                balDTOMap= bankBalList.stream().collect(Collectors.toMap(BisAccBalDTO::getBankAcc, Function.identity()));
            }
            int sendNum = 0;
            for(AbAccrecordsVO acInfo:acInfoList){
                acInfo.setBillMoney(acInfo.getBillMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
                if(balDTOMap == null || !balDTOMap.containsKey(acInfo.getBankAcc())){
                    acInfo.setBankAccBalance(BigDecimal.ZERO);
                }else {
                    acInfo.setBankAccBalance(balDTOMap.get(acInfo.getBankAcc()).getAvailBal() == null?
                            balDTOMap.get(acInfo.getBankAcc()).getBal() : balDTOMap.get(acInfo.getBankAcc()).getAvailBal());
                }
                acInfo.setBankAccBalance(acInfo.getBankAccBalance().setScale(2,BigDecimal.ROUND_HALF_UP));
                if(acInfo.getReleasePaymentMoney()!=null){
                    acInfo.setReleasePaymentMoney(acInfo.getReleasePaymentMoney().setScale(2,BigDecimal.ROUND_HALF_UP));
                }
                BigDecimal totalPayMoney = acInfo.getBillMoney().add(acInfo.getRepayMoney()).setScale(2,BigDecimal.ROUND_HALF_UP);
                acInfo.setTotalPayMoney(totalPayMoney);
                if(totalPayMoney.compareTo(acInfo.getBankAccBalance()) < 1){
                    continue;
                }
                acInfo.setCorpName(corpDTOMap.get(acInfo.getCorpId()).getName());
                if(isRangeQuery){
                    acInfo.setEndDateStr(DateUtils.format(nextDay,"yyyy-MM-dd"));
                    acInfo.setEndDate2Str(DateUtils.format(DateUtils.addDay(workDay,-1),"yyyy-MM-dd"));
                }else{
                    acInfo.setEndDateStr(DateUtils.format(nextDay,"yyyy-MM-dd"));
                }
                EquityMaintenanceDTO equityMaintenanceDTO = iEquityMaintenanceService.queryById(corpDTOMap.get(acInfo.getCorpId()).getEquityMaintenanceId());
                acInfo.setCompanyTYpeName(equityMaintenanceDTO.getEquityName());
                acInfo.setXbMoney(totalPayMoney.subtract(acInfo.getBankAccBalance()));
                notificationService.send(null, acInfo.getCorpId(), AccebillConstant.INSUFFICIENT_FUNDS_REMIND_TAG, acInfo);
                sendNum++;
            }
            this.logger.info("---承兑到期和借款汇总余额不足提醒，【"+sendNum+"】条数据。");
        }
        this.logger.info("---承兑到期和借款汇总余额不足提醒，结束执行定时任务---");
    }


}
