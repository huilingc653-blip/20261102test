package com.bytter.report.domain.cms.service;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.report.domain.cms.dao.BusinessVolumeDao;
import com.bytter.report.domain.cms.vo.BusinessVolumeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.*;
import com.bytter.core.constant.CmsConstant;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcExcrateAdjustService;

import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@SuppressWarnings("rawtypes")
@Service("businessVolumeService")
@Transactional
public class BusinessVolumeService extends BaseService {
    
    @Autowired
    private BusinessVolumeDao businessVolumeDao;
    @DubboReference
    private IFcExcrateAdjustService iFcExcrateAdjustService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**列表*/
    public DataTablesOutput getDataTable(DataTablesInput input) throws ParseException {
        Map<String, Object> param =  input.getFormdata();
        String corpId = (String) param.get("corpId");
        String reducedCurId = (String) param.get("reducedCurId");//折合币种

        List<String> corpIds = null;
        if(EmptyUtils.isNotEmpty(corpId)) {
            corpIds = StringUtils.splitToList(",", corpId);
        }
        String moneyType = (String)param.get("moneyType");//金额单位换算
        String isToCurr = (String)param.get("isToCurr");//折合本位币
        String cycle = (String)param.get("cycle");//对比周期
        setDate(input);

        String joinSql = "";
        //是否折合币种为是时，默认查询所有
        if(!StringUtils.isEmpty(isToCurr) && CmsConstant.IS_TRUE.equals(isToCurr)){
            List<String> curArr = new ArrayList<>();
            List<BtCurrencyDTO> btCurrency = currencyService.getBtCurrency();
            if(!EmptyUtils.isEmptyList(btCurrency)){
                for(BtCurrencyDTO dto : btCurrency){
                    curArr.add(dto.getId());
                }
                String curSql = SqlUtil.getInSql(curArr, "z.curId", true);
                joinSql += " and " + curSql;
            }
        }
        DataTablesOutput dto = businessVolumeDao.getDataTable(input,corpIds,joinSql);
        List<BusinessVolumeVO> list =  dto.getData();//当前结果
        if(!CollectionUtils.isEmpty(list)){
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrency();
            for(BusinessVolumeVO vo : list){
                if(null != corpMap){
                    if(corpMap.containsKey(vo.getCorpId())){
                        CorpDTO corpDTO = corpMap.get(vo.getCorpId());
                        if(null != corpDTO){
                            vo.setCorpName(corpDTO.getName());
                        }
                    }
                }
                if(null != currencyMap){
                    if(currencyMap.containsKey(vo.getCurId())){
                        BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
                        if(null != btCurrencyDTO){
                            vo.setCurName(btCurrencyDTO.getCurName());
                        }
                    }
                }
            }
        }
        
        List<BusinessVolumeVO> cycleList = cycleList(list,cycle,param,corpIds);//对比周期

        List<BusinessVolumeVO> showList = new ArrayList<>();
        if(CmsConstant.IS_TRUE.equals(isToCurr)){
            //折合本位币
            String mDate = (String)param.get("monthDate");
            Date monthDate = DateUtils.parse(mDate,"yyyy-MM-dd");
            cycleList = toCurr(cycleList,monthDate,reducedCurId);

            //同单位同业务品种同分类的合并
            Map<String,BusinessVolumeVO> cycleMap = new HashMap<>();

            if(!EmptyUtils.isEmptyList(cycleList)){
                /**交易笔数*/
                BigDecimal num = BigDecimal.ZERO;
                /**交易金额*/
                BigDecimal money = BigDecimal.ZERO;
                /**上月笔数*/
                BigDecimal numM = BigDecimal.ZERO;
                /**上月余额*/
                BigDecimal balanceM = BigDecimal.ZERO;
                /**去年同期笔数*/
                BigDecimal numL = BigDecimal.ZERO;
                /**去年同期余额*/
                BigDecimal balanceL = BigDecimal.ZERO;
                /**当季笔数*/
                BigDecimal numNowS = BigDecimal.ZERO;
                /**当季余额*/
                BigDecimal balanceNowS = BigDecimal.ZERO;
                /**上季笔数*/
                BigDecimal numS = BigDecimal.ZERO;
                /**上季余额*/
                BigDecimal balanceS = BigDecimal.ZERO;
                /**当年笔数*/
                BigDecimal numNowY = BigDecimal.ZERO;
                /**当年余额*/
                BigDecimal balanceNowY = BigDecimal.ZERO;
                /**去年笔数*/
                BigDecimal numY = BigDecimal.ZERO;
                /**去年余额*/
                BigDecimal balanceY = BigDecimal.ZERO;
                for(BusinessVolumeVO cycleVo : cycleList){
                    String key = cycleVo.getBusType() + ";" + cycleVo.getBusSign() + ";" + cycleVo.getCorpId();
                    if(!cycleMap.containsKey(key)) {
                        cycleMap.put(key, cycleVo);
                    }else{
                        BusinessVolumeVO voTemp = cycleMap.get(key);
                        num = voTemp.getNum();
                        numM = voTemp.getNumM();
                        numL = voTemp.getNumL();
                        numNowS = voTemp.getNumNowS();
                        numS = voTemp.getNumS();
                        numNowY = voTemp.getNumNowY();
                        numY = voTemp.getNumY();
                        money = voTemp.getMoney();
                        balanceM = voTemp.getBalanceM();
                        balanceL = voTemp.getBalanceL();
                        balanceNowS = voTemp.getBalanceNowS();
                        balanceS = voTemp.getBalanceS();
                        balanceNowY = voTemp.getBalanceNowY();
                        balanceY = voTemp.getBalanceY();
                        num = num.add(cycleVo.getNum());//交易笔数
                        numM = numM.add(cycleVo.getNumM());//上月笔数
                        numL = numL.add(cycleVo.getNumL());//去年同期笔数
                        numNowS = numNowS.add(cycleVo.getNumNowS());//当季笔数
                        numS = numS.add(cycleVo.getNumS());//当季笔数
                        numNowY = numNowY.add(cycleVo.getNumNowY());//当年笔数
                        numY = numY.add(cycleVo.getNumY()); //去年笔数
                        money = money.add(cycleVo.getMoney());
                        balanceM = balanceM.add(cycleVo.getBalanceM());//上月余额
                        balanceL = balanceL.add(cycleVo.getBalanceL());//去年同期余额
                        balanceNowS = balanceNowS.add(cycleVo.getBalanceNowS());//当季余额
                        balanceS = balanceS.add(cycleVo.getBalanceS());//上季余额
                        balanceNowY = balanceNowY.add(cycleVo.getBalanceNowY());//当年余额
                        balanceY = balanceY.add(cycleVo.getBalanceY());//去年余额

                        voTemp.setNum(num);
                        voTemp.setMoney(money);
                        voTemp.setNumM(numM);
                        voTemp.setBalanceM(balanceM);
                        voTemp.setNumL(numL);
                        voTemp.setBalanceL(balanceL);
                        voTemp.setNumNowS(numNowS);
                        voTemp.setBalanceNowS(balanceNowS);
                        voTemp.setNumS(numS);
                        voTemp.setBalanceS(balanceS);
                        voTemp.setNumM(numM);
                        voTemp.setNumNowY(numNowY);
                        voTemp.setBalanceNowY(balanceNowY);
                        voTemp.setNumY(numY);
                        voTemp.setBalanceY(balanceY);
                        cycleMap.put(key,voTemp);
                    }
                }
            }
            for(String key : cycleMap.keySet()){
                BusinessVolumeVO newVO = cycleMap.get(key);
                BigDecimal num = newVO.getNum();
                BigDecimal money = newVO.getMoney();
                if(!EmptyUtils.isEmpty(cycleMap.get(key))){
                    BeanUtils.copyNotNullProperties(cycleMap.get(key),newVO);
                    newVO.setNum(num);
                    newVO.setMoney(money);
                }
                showList.add(newVO);
            }
        }else{
            showList.addAll(cycleList);
        }

        if(!"1".equals(moneyType)){//金额单位换算
            conversion( showList,moneyType);
        }

        List<BusinessVolumeVO> newList = new ArrayList<>();
        if(CmsConstant.IS_TRUE.equals(isToCurr)) {//折合本位币
            for(BusinessVolumeVO item : showList){
                if(item.getMoney().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceY().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceS().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceL().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceNowS().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceNowY().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceM().compareTo(BigDecimal.ZERO) == 0){
                    dto.setRecordsTotal(dto.getRecordsTotal()-1);
                }else {
                    newList.add(item);
                }
            }
        }else {
            for (BusinessVolumeVO item : cycleList) {
                if (item.getMoney().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceY().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceS().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceL().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceNowS().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceNowY().compareTo(BigDecimal.ZERO) == 0 &&
                        item.getBalanceM().compareTo(BigDecimal.ZERO) == 0) {
                    dto.setRecordsTotal(dto.getRecordsTotal() - 1);
                    dto.setRecordsFiltered(dto.getRecordsFiltered() - 1);
                } else {
                    newList.add(item);
                }
            }
        }
        dto.setData(newList);
        return dto;
    }

    /**
     * 折合人民币
     */
    public List<BusinessVolumeVO> toCurr(List<BusinessVolumeVO> list,Date monthDate,String reducedCurId){
        for (BusinessVolumeVO vo : list) {
            BigDecimal rate = iFcExcrateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), reducedCurId, monthDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            if (rate == null) {
                rate = new BigDecimal(1);
                vo.setId("1");
            }
            vo.setMoney(vo.getMoney().multiply(rate));
            vo.setBalanceM((vo.getBalanceM()==null?BigDecimal.ZERO:vo.getBalanceM()).multiply(rate));
            vo.setBalanceS((vo.getBalanceS()==null?BigDecimal.ZERO:vo.getBalanceS()).multiply(rate));
            vo.setBalanceNowS((vo.getBalanceNowS()==null?BigDecimal.ZERO:vo.getBalanceNowS()).multiply(rate));
            vo.setBalanceL((vo.getBalanceL()==null?BigDecimal.ZERO:vo.getBalanceL()).multiply(rate));
            vo.setBalanceY((vo.getBalanceY()==null?BigDecimal.ZERO:vo.getBalanceY()).multiply(rate));
            vo.setBalanceNowY((vo.getBalanceNowY()==null?BigDecimal.ZERO:vo.getBalanceNowY()).multiply(rate));
        }
        return list;
    }
    
    /**金额单位换算*/
    public List<BusinessVolumeVO> conversion(List<BusinessVolumeVO> list,String moneyType){
        BigDecimal divisor = BigDecimal.ZERO;
        if("2".equals(moneyType)){//千元
            divisor = new BigDecimal(1000);
        }
        if("3".equals(moneyType)){//万元
            divisor = new BigDecimal(10000);
        }
        if("4".equals(moneyType)){//亿元
            divisor = new BigDecimal(100000000);
        }
        for (BusinessVolumeVO vo : list){
            vo.setMoney(vo.getMoney().divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            vo.setBalanceM((vo.getBalanceM()==null?BigDecimal.ZERO:vo.getBalanceM()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            vo.setBalanceS((vo.getBalanceS()==null?BigDecimal.ZERO:vo.getBalanceS()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            vo.setBalanceNowS((vo.getBalanceNowS()==null?BigDecimal.ZERO:vo.getBalanceNowS()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            vo.setBalanceL((vo.getBalanceL()==null?BigDecimal.ZERO:vo.getBalanceL()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            vo.setBalanceY((vo.getBalanceY()==null?BigDecimal.ZERO:vo.getBalanceY()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
            vo.setBalanceNowY((vo.getBalanceNowY()==null?BigDecimal.ZERO:vo.getBalanceNowY()).divide(divisor,2, BigDecimal.ROUND_HALF_UP));
        }
        return list;
    }
    
    /**对比周期*/
    public List<BusinessVolumeVO> cycleList(List<BusinessVolumeVO> list,String cycle,Map<String, Object> param,List<String> corpIds) throws ParseException {
        String date = (String)param.get("monthDate");//当前日期
        String busType = (String)param.get("busType");//品种
        String busSign = (String)param.get("busSign");//分类
        Date monthDate =DateUtils.parseDate(date,"yyyy-MM-dd");
        String curId = (String)param.get("curId");
        //对比周期集合
        List<String> cycleList = new ArrayList<>();
        if(StringUtils.isNotBlank(cycle)) {
            String[] cycleArr = cycle.split(",");
            cycleList = Arrays.asList(cycleArr);
        }
        List<BusinessVolumeVO> cycleListM = null;//上月末
        List<BusinessVolumeVO> cycleListL = null;//去年同期
        List<BusinessVolumeVO> cycleListNowS = null;//当季
        List<BusinessVolumeVO> cycleListS = null;//上季末
        List<BusinessVolumeVO> cycleListNowY = null;//当年
        List<BusinessVolumeVO> cycleListY = null;//年初
        if(EmptyUtils.isEmptyList(cycleList)|| cycleList.contains(CmsConstant.CYCLE_M)){//月：较上月，去年同期
            Date lastMonth = getLastDayOfMonth(monthDate);//上月末
            Date startDate = DateUtils.getFirstMonthDate(lastMonth);//上月第一天
            cycleListM = businessVolumeDao.getList(curId, corpIds, lastMonth,busType,busSign,startDate);//上月末

            Date upMonth = DateUtils.addYear(monthDate,-1);//去年同期
            Date startDate1 = DateUtils.getFirstMonthDate(upMonth);//去年同期第一天
            cycleListL = businessVolumeDao.getList(curId, corpIds, upMonth,busType,busSign,startDate1);//去年同期
            
            for (BusinessVolumeVO vo : list){
                boolean isExisM = false;
                boolean isExisL = false;
                for (BusinessVolumeVO m : cycleListM) {//较上月
                    if(vo.getCorpId().equals(m.getCorpId()) && vo.getBusSign().equals(m.getBusSign())){
                        vo.setBalanceM(vo.getMoney().subtract(m.getMoney()));
                        vo.setNumM(vo.getNum().subtract(m.getNum()));
                        isExisM = true;
                    }
                }

                for (BusinessVolumeVO l : cycleListL) {//去年同期
                    if(vo.getCorpId().equals(l.getCorpId()) && vo.getBusSign().equals(l.getBusSign())){
                        vo.setBalanceL(vo.getMoney().subtract(l.getMoney()));
                        vo.setNumL(vo.getNum().subtract(l.getNum()));
                        isExisL = true;
                    }
                }

                if(!isExisM){
                    vo.setBalanceM(vo.getMoney());
                    vo.setNumM(vo.getNum());
                }
                
                if(!isExisL){
                    vo.setBalanceL(vo.getMoney());
                    vo.setNumL(vo.getNum());
                }
            }
        }
        
        if(EmptyUtils.isEmptyList(cycleList)|| cycleList.contains(CmsConstant.CYCLE_S)){//季：当前季，上季度
            Date nowSeason = DateUtils.getFirstQuarterDate(monthDate);//当季第一天~查询日期
            cycleListNowS = businessVolumeDao.getList(curId,corpIds,monthDate,busType,busSign,nowSeason);//当前季
            
            Date sDate = DateUtils.addMonth(monthDate, -3);
            Date startDate = DateUtils.getFirstQuarterDate(sDate);//上季第一天
            Date eos = DateUtils.getLastQuarterDate(sDate);//上季末
            cycleListS = businessVolumeDao.getList(curId, corpIds, eos,busType,busSign,startDate);//上季末
            
            for (BusinessVolumeVO vo : list){
                boolean isExis = false;
                for (BusinessVolumeVO n : cycleListNowS) {//当前季
                    if(vo.getCorpId().equals(n.getCorpId()) && vo.getBusSign().equals(n.getBusSign())){
                        vo.setBalanceNowS(n.getMoney());
                        vo.setNumNowS(n.getNum());
                    }
                }
                
                for (BusinessVolumeVO s : cycleListS) {//上季末
                    if(vo.getCorpId().equals(s.getCorpId()) && vo.getBusSign().equals(s.getBusSign())){
                        vo.setBalanceS(vo.getBalanceNowS().subtract(s.getMoney()));
                        vo.setNumS(vo.getNumNowS().subtract(s.getNum()));
                        isExis = true;
                    }
                }
                
                if(!isExis){
                    vo.setBalanceS(vo.getBalanceNowS());
                    vo.setNumS(vo.getNumNowS());
                }
            }
        }
        
        if(EmptyUtils.isEmptyList(cycleList)|| cycleList.contains(CmsConstant.CYCLE_Y)){//年：当年，较去年
            Date nowYear = DateUtils.getFirstYearDate(monthDate);//当年第一天
            cycleListNowY = businessVolumeDao.getList(curId, corpIds, monthDate,busType,busSign,nowYear);//当年

            Date lastYear = DateUtils.addYear(monthDate,-1);
            Date startDate = DateUtils.getFirstYearDate(lastYear);//去年
            cycleListY = businessVolumeDao.getList(curId, corpIds, lastYear,busType,busSign,startDate);//去年
            
            for (BusinessVolumeVO vo : list){
                boolean isExis = false;
                for (BusinessVolumeVO n : cycleListNowY) {//当年
                    if(vo.getCorpId().equals(n.getCorpId()) && vo.getBusSign().equals(n.getBusSign())){
                        vo.setBalanceNowY(n.getMoney());
                        vo.setNumNowY(n.getNum());
                    }
                }
                
                for (BusinessVolumeVO y : cycleListY) {//去年
                    if(vo.getCorpId().equals(y.getCorpId()) && vo.getBusSign().equals(y.getBusSign())){
                        vo.setBalanceY(vo.getBalanceNowY().subtract(y.getMoney()));
                        vo.setNumY(vo.getNumNowY().subtract(y.getNum()));
                        isExis = true;
                    }
                }

                if(!isExis){
                    vo.setBalanceY(vo.getBalanceNowY());
                    vo.setNumY(vo.getNumNowY());
                }
            }
        }
        return list;
    }
    
    /**上月末*/
    public  Date getLastDayOfMonth(Date yearMonth) {
        int year = DateUtils.getYear(yearMonth);
        int month = DateUtils.getMonth(yearMonth);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1); //设置当前月的上一个月
        int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
        return cal.getTime();
    }
    
    public Date getLastDayOfLastQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 12 - 1;
        } else if (quarter == 2) {
            month = 3 - 1;
        } else if (quarter == 3) {
            month = 6 - 1;
        } else if (quarter == 4) {
            month = 9 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }


    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public  Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }
    
    private void setDate(DataTablesInput input) {
        String date = (String) input.getFormdata().get("monthDate");//获取参数时间
        String nowDate = DateUtils.getDate("yyyy-MM");//当期系统时间
        if (date.equals(nowDate)) {
            input.getFormdata().put("monthDate", DateUtils.getDateStr(new Date(), "yyyy-MM-dd"));
        } else {
            Date parse = DateUtils.parse(date, "yyyy-MM");
            Date lastMonthDate = DateUtils.getLastMonthDate(parse);
            input.getFormdata().put("monthDate", DateUtils.getDateStr(lastMonthDate, "yyyy-MM-dd"));
        }
    }


    @Override
    public BaseDao getEntityDao() {
        return businessVolumeDao;
    }
}
