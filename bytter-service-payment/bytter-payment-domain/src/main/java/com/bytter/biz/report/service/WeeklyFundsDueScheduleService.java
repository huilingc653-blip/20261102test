package com.bytter.biz.report.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import com.bytter.biz.payment.api.constant.ResourceConstant;
import com.bytter.biz.report.dao.WeeklyFundsDueScheduleDao;
import com.bytter.biz.report.vo.WeeklyFundsDueScheduleVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICalendarService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IOperationAreaService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.OperationAreaDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @author zhous
 * @version 1.0
 * @Title: Service
 * @Description: 每周资金到期用款规划表服务类
 * @date 2024-10-24
 */
@Service("weeklyFundsDueScheduleService")
@Transactional
public class WeeklyFundsDueScheduleService  extends BaseService<WeeklyFundsDueScheduleVo, String> {

    @Autowired
    private WeeklyFundsDueScheduleDao weeklyFundsDueScheduleDao;

    @DubboReference(check = false)
    private IOperationAreaService operationAreaService;

    @DubboReference(check = false)
    private ICalendarService calendarService;

    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 加载每周资金到期用款规划表数据
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput dataQuery(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuthList=corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_WEEKLYFUNDSDUESCHEDULE_QUERY, user.getId());
        DataTablesOutput<WeeklyFundsDueScheduleVo> dataTablesOutput = weeklyFundsDueScheduleDao.dataQuery(dataTablesInput, corpAuthList);
        List<WeeklyFundsDueScheduleVo> list = dataTablesOutput.getData();
        if(CollectionUtil.isNotEmpty(list)){
            Map<String, String> operationAreaMap = new HashMap();
            Map<String, CorpDTO> corpMap = new HashMap();
            Map<Date, Date> workdayMap = new HashMap();
            Map<String, BigDecimal> loanMoneyMap = new HashMap();
            for(WeeklyFundsDueScheduleVo vo : list){
                if(StringUtils.isNotEmpty(vo.getOperationAreaCode())){
                    StringBuilder operationAreaNames = new StringBuilder();
                    String operationAreaName = null;
                    List<String> operationAreaCodes = Arrays.asList(vo.getOperationAreaCode().split(","));
                    for(String code:operationAreaCodes){
                        if(operationAreaMap.containsKey(code)){
                            operationAreaName = operationAreaMap.get(code);
                        }else{
                            List<OperationAreaDTO> operationAreaDTOS = operationAreaService.queryByCode(code);
                            if(CollectionUtil.isNotEmpty(operationAreaDTOS)){
                                operationAreaDTOS.stream().forEach(operationAreaDTO -> {
                                    operationAreaNames.append(operationAreaDTO.getOperationAreaName()).append(",");
                                });
                            }
                            operationAreaName = StringUtils.isNotEmpty(operationAreaNames.toString())?operationAreaNames.toString().substring(0,operationAreaNames.toString().length()-1):"";
                            operationAreaMap.put(code,operationAreaName );
                        }
                    }
                    vo.setOperationAreaName(operationAreaName);
                }
                Date workDay = null;
                if(workdayMap.containsKey(vo.getEndDate())){
                    workDay = workdayMap.get(vo.getEndDate());
                }else{
                    //获取前两天工作日期
                    workDay = getWorkDayAgo(vo.getEndDate(),2);
                    workdayMap.put(vo.getEndDate(),workDay);
                }
                vo.setFundsDisbursementDate(workDay);
                //一个账号有多行记录时，最上面的贷款金额正常显示，其他行的留空
                if(vo.getLoanMoney() != null ){
                    if(loanMoneyMap.containsKey(vo.getBankAcc())){
                        vo.setLoanMoney(BigDecimal.ZERO);
                    }else{
                        loanMoneyMap.put(vo.getBankAcc(),vo.getLoanMoney());
                    }
                }
                if(BigDecimal.ONE.compareTo(vo.getBailPercent()) == 0 ){
                    vo.setApplyRepayMoney(vo.getLoanMoney());
                }else{
                    vo.setApplyRepayMoney(vo.getLoanMoney().add(vo.getBillMoney()));
                }
                if(StringUtils.isNotBlank(vo.getCorpId())){
                    CorpDTO corpDTO = null;
                    if(corpMap.containsKey(vo.getCorpId())){
                        corpDTO = corpMap.get(vo.getCorpId());
                    }else{
                        corpDTO = corpService.get(vo.getCorpId());
                        corpMap.put(vo.getCorpId(),corpDTO);
                    }
                    vo.setProvinceName(corpDTO.getProvinceName());
                }

                /* 00062025070305
                  1.当保证金比率为非100%保证金且为全资时，“备注”设置为“集团下拨”；
                  2.当保证金比率为非100%保证金且为控股时，“备注”设置为“地区自行划拨”；
                  3.当保证金比率为100%时，“资金下拨/划拨日期”不显示具体日期，显示为“/”；
                 */
                if(BigDecimal.ONE.compareTo(vo.getBailPercent()) == 0) {
                    vo.setFundsDisbursementDate(null);
                    vo.setRmk("100%保证金");
                } else {
                    if ("全资".equals(vo.getEquityName())) {
                        /* 00062025080511
                          当单位编码为100000或者100、非100%保证金且为全资时，备注留空；
                          其他单位保持不变（单位编码不等于100000和100），且为非100%保证金且为全资时，备注为“集团下拨”；
                         */
                        if (!"100000".equals(vo.getCorpCode()) && !"100".equals(vo.getCorpCode())) {
                            vo.setRmk("集团下拨");
                        }
                    } else if ("控股".equals(vo.getEquityName())) {
                        vo.setRmk("地区自行划拨");
                    }
                }
            }
        }
        return dataTablesOutput;
    }

    /**
     * 获取前几天工作日期
     *
     * @param stratDate 开始日期
     * @param offsetDay 开始日期前多少天
     * @return 前多少天工作日期
     */
    private Date getWorkDayAgo(Date stratDate, int offsetDay) {
        int count = 0;
        Date workDay = stratDate;
        while (count < offsetDay){
            workDay = DateUtils.addDay(workDay, -1);
            Integer workDaySign = calendarService.getWorkDaySignByTime(workDay);
            if (workDaySign != null && 1 == workDaySign) {
                count++;
            }
        }
        return workDay;
    }

    @Override
    public BaseDao<WeeklyFundsDueScheduleVo, String> getEntityDao() {
        return weeklyFundsDueScheduleDao;
    }
}