package com.bytter.report.domain.cms.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.domain.cms.dao.CmsLoanStatementDao;
import com.bytter.report.domain.cms.vo.CmsLoanStatementVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author luxy
 * @Title: CmsLoanStatementService
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/1/13 11:20
 */
@Service("CmsLoanStatementService")
public class CmsLoanStatementService extends BaseService<CmsLoanStatementVO,String> {
    @Autowired
    private CmsLoanStatementDao loanStatementDao;
    @DubboReference
    private ICorpService corpService;
    @DubboReference
    private IFcExcrateAdjustService iFcExcrateAdjustService;
    @Autowired
    private IDictService dictServiceI;
    @Override
    public BaseDao<CmsLoanStatementVO, String> getEntityDao() {
        return loanStatementDao;
    }

    public DataTablesOutput datatable(DataTablesInput input, ICurrentUser user){
        List<String> corps = corpService.getUserCorpIds(ResourceConstant.CMS_REPORT_LOAN_STATEMENT_QUERY, user.getId());
        DataTablesOutput output=loanStatementDao.getStatementVOs(input,corps);
        String isConvertStandCur=(String)input.getFormdata().get("isConvertStandCur");//是否折合本位币
        String moneyUnit=(String)input.getFormdata().get("moneyUnit");//币别单位
        List<CmsLoanStatementVO> voList=output.getData();
        Map<String, String> corpMap = corpService.getNameByIds(corps);
        for (CmsLoanStatementVO vo : voList) {
            vo.setCorpName(corpMap.get(vo.getCorpId()));//单位
            vo.setLoanSignName(dictServiceI.get("Loan.Sign", vo.getLoanSign()));//融资方式
        }

        List<CmsLoanStatementVO> dataList=new ArrayList<>();
        //折合本位币操作
        if(isConvertStandCur!=null&&isConvertStandCur.equals(CmsConstant.IS_TRUE)) {
            String endDate=(String)input.getFormdata().get("endDate")+"-01";
            String convertCurId=input.getFormdata().get("convertCurId").toString();
            changeToStandCur(voList, endDate, convertCurId);
//            dataList=gatherData(voList);//汇率折算修改前是多种币别折合一种币别，折合后需将数据合并，汇率折算修改后币别折算是一对一折合，无需合并数据
        }

        dataList=voList;
        if(!moneyUnit.equals(CmsConstant.MONEY_TYPE_1)){//金额单位不是'元'，需要换算
            conversion( dataList,moneyUnit);
        }
        if(null!=dataList&&dataList.size()>0){
            setRate(dataList);//设置放款和还款占比
        }
        output.setData(dataList);
        return output;
    }

    /**
     * 折合本位币操作
     */
    public void changeToStandCur(List<CmsLoanStatementVO> list,String endDate,String convertCurId){
        Date searchEndDate=new Date();//查询截止日期
        Date eDate=DateUtils.str2Date(endDate,DateUtils.date_sdf());
        Date estiSearchEndDate=DateUtils.getLastMonthDate(eDate);//预计查询截止日期(取查询结束所在月的最后一天)
        int dayDiff=DateUtils.dayDiffs(DateUtils.getMonthEndTime(),estiSearchEndDate);
        if(dayDiff==0){//如果预计查询结束日期的当月最后一天和系统当前时间月份的最后一天为同一天，则截止查询日期取系统当前日期
            searchEndDate=DateUtils.getDate();
        }else{
            searchEndDate=estiSearchEndDate;
        }
        if (null != list && list.size() > 0) {
            BigDecimal curExrate=BigDecimal.ONE;
            if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(list.get(0).getCurId())){
                curExrate = iFcExcrateAdjustService.getExchangeRateBy(list.get(0).getCurId(),convertCurId, searchEndDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//距查询日期最近的币别汇率
            }
            for (CmsLoanStatementVO loanVO : list) {
                if(curExrate.equals(BigDecimal.ONE)){//本位币无需折合
                    continue;
                }
                loanVO.setCurContractMoney(loanVO.getCurContractMoney().multiply(curExrate).setScale(2, BigDecimal.ROUND_HALF_UP));//本期合同金额
                loanVO.setCurSendMoney(loanVO.getCurSendMoney().multiply(curExrate).setScale(2, BigDecimal.ROUND_HALF_UP));//本期发放金额
                loanVO.setCurRepayMoney(loanVO.getCurRepayMoney().multiply(curExrate).setScale(2, BigDecimal.ROUND_HALF_UP));//本期还款金额
                loanVO.setLastContractMoney(loanVO.getLastContractMoney().multiply(curExrate).setScale(2, BigDecimal.ROUND_HALF_UP));//去年合同金额
                loanVO.setLastSendMoney(loanVO.getLastSendMoney().multiply(curExrate).setScale(2, BigDecimal.ROUND_HALF_UP));//去年发放金额
                loanVO.setLastRepayMoney(loanVO.getLastRepayMoney().multiply(curExrate).setScale(2, BigDecimal.ROUND_HALF_UP));//去年还款金额
            }
        }
    }

    /**金额单位换算*/
    public void conversion(List<CmsLoanStatementVO>list,String moneyType){
        if(null!=list&&list.size()>0){
            BigDecimal divisor = BigDecimal.ZERO;
            if(moneyType.equals(CmsConstant.MONEY_TYPE_1000)){//千元
                divisor = new BigDecimal(1000);
            }
            if(moneyType.equals(CmsConstant.MONEY_TYPE_10000)){//万元
                divisor = new BigDecimal(10000);
            }
            if(moneyType.equals(CmsConstant.MONEY_TYPE_100000000)){//亿元
                divisor = new BigDecimal(100000000);
            }
            for (CmsLoanStatementVO vo : list){
                vo.setCurContractMoney(vo.getCurContractMoney().divide(divisor));
                vo.setCurSendMoney(vo.getCurSendMoney().divide(divisor));
                vo.setCurRepayMoney(vo.getCurRepayMoney().divide(divisor));
                vo.setLastContractMoney(vo.getLastContractMoney().divide(divisor));
                vo.setLastSendMoney(vo.getLastSendMoney().divide(divisor));
                vo.setLastRepayMoney(vo.getLastRepayMoney().divide(divisor));
            }
        }
    }

    /**
     * 设置放款占比和还款占比
     */
    public void setRate(List<CmsLoanStatementVO> list){
        for(CmsLoanStatementVO loanVO:list){
            //设置本期放款占比和还款占比
            BigDecimal curLoanRate=BigDecimal.ZERO;
            BigDecimal curRepayRate=BigDecimal.ZERO;
            BigDecimal loanGrowthRate=BigDecimal.ZERO;//放款增长率
            BigDecimal repayGrowthRate=BigDecimal.ZERO;//还款增长率
            if(null!=loanVO.getCurContractMoney()&&loanVO.getCurContractMoney().compareTo(BigDecimal.ZERO)!=0){
                curLoanRate=loanVO.getCurSendMoney().divide(loanVO.getCurContractMoney(),4, BigDecimal.ROUND_HALF_UP);//放款占比：放款金额/合同金额，百分比显示。如果合同金额为0，则占比显示--；如果放款金额为0且合同金额不为0，则显示为0
                if(null!=loanVO.getCurSendMoney()&&loanVO.getCurSendMoney().compareTo(BigDecimal.ZERO)!=0){
                    curRepayRate=loanVO.getCurRepayMoney().divide(loanVO.getCurSendMoney(),4, BigDecimal.ROUND_HALF_UP);//还款占比：已还款金额/已放款金额，百分比显示；如果已放款金额为0，则占比显示--；如果还款金额为0且放款金额不为0，则显示为0
                }
            }
            loanVO.setCurLoanRate(curLoanRate);
            loanVO.setCurRepayRate(curRepayRate);

            //设置去年同期放款占比和还款占比
            BigDecimal lastLoanRate=BigDecimal.ZERO;
            BigDecimal lastRepayRate=BigDecimal.ZERO;
            if(null!=loanVO.getLastContractMoney()&&loanVO.getLastContractMoney().compareTo(BigDecimal.ZERO)!=0){
                lastLoanRate=loanVO.getLastSendMoney().divide(loanVO.getLastContractMoney(),4, BigDecimal.ROUND_HALF_UP);//去年同期放款占比
            }

            if(null!=loanVO.getLastSendMoney()&&loanVO.getLastSendMoney().compareTo(BigDecimal.ZERO)!=0){
                BigDecimal loanGrowthMoney=loanVO.getCurSendMoney().subtract(loanVO.getLastSendMoney());//放款增长值
                loanGrowthRate=loanGrowthMoney.divide(loanVO.getLastSendMoney(),4, BigDecimal.ROUND_HALF_UP);//放款增长率
            }
            if(null!=loanVO.getLastRepayMoney()&&loanVO.getLastRepayMoney().compareTo(BigDecimal.ZERO)!=0){
                lastRepayRate=loanVO.getLastRepayMoney().divide(loanVO.getLastContractMoney(),4, BigDecimal.ROUND_HALF_UP);//去年同期还款占比
                BigDecimal loanGrowthMoney=loanVO.getCurRepayMoney().subtract(loanVO.getLastRepayMoney());//还款增长值
                repayGrowthRate=loanGrowthMoney.divide(loanVO.getLastRepayMoney(),4, BigDecimal.ROUND_HALF_UP);//还款增长率
            }
            loanVO.setLastLoanRate(lastLoanRate);
            loanVO.setLastRepayRate(lastRepayRate);

            loanVO.setLoanGrowthRate(loanGrowthRate);
            loanVO.setRepayGrowthRate(repayGrowthRate);
        }
    }

    /**
     * 折合本位币后将相同单位的数据合并
     */
    public List<CmsLoanStatementVO> gatherData(List<CmsLoanStatementVO> list){
        List<CmsLoanStatementVO> gatherVOS=new ArrayList<>();
        if (null != list && list.size() > 0) {
            Map<String, List<CmsLoanStatementVO>> gatherMap = new HashMap<>();//用于存放相同单位相同融资种类的集合
            for (CmsLoanStatementVO loanVO : list) {
                String mapKey=loanVO.getCorpId()+"_"+loanVO.getLoanSign();//单位和融资种类
                List<CmsLoanStatementVO> gatherList = gatherMap.get(mapKey);
                if (null != gatherList && gatherList.size() > 0) {
                    gatherList.add(loanVO);
                } else {
                    List<CmsLoanStatementVO> voList = new ArrayList<>();
                    voList.add(loanVO);
                    gatherMap.put(mapKey, voList);
                }
            }

            CmsLoanStatementVO gatherVO;
            for (String key : gatherMap.keySet()) {
                gatherVO=new CmsLoanStatementVO();
                List<CmsLoanStatementVO> voList=gatherMap.get(key);
                gatherVO.setCorpId(voList.get(0).getCorpId());
                gatherVO.setCorpName(voList.get(0).getCorpName());
                gatherVO.setLoanSign(voList.get(0).getLoanSign());
                gatherVO.setLoanSignName(voList.get(0).getLoanSignName());
                BigDecimal curContractSum=BigDecimal.ZERO;
                BigDecimal curSendSum=BigDecimal.ZERO;
                BigDecimal curRepaySum=BigDecimal.ZERO;
                BigDecimal lastContractSum=BigDecimal.ZERO;
                BigDecimal lastSendSum=BigDecimal.ZERO;
                BigDecimal lastRepaySum=BigDecimal.ZERO;
                for(CmsLoanStatementVO loanVO:voList){
                    curContractSum=curContractSum.add(loanVO.getCurContractMoney());
                    curSendSum=curSendSum.add(loanVO.getCurSendMoney());
                    curRepaySum=curRepaySum.add(loanVO.getCurRepayMoney());
                    lastContractSum=lastContractSum.add(loanVO.getLastContractMoney());
                    lastSendSum=lastSendSum.add(loanVO.getLastSendMoney());
                    lastRepaySum=lastRepaySum.add(loanVO.getLastRepayMoney());
                }
                gatherVO.setCurContractMoney(curContractSum);
                gatherVO.setCurSendMoney(curSendSum);
                gatherVO.setCurRepayMoney(curRepaySum);
                gatherVO.setLastContractMoney(lastContractSum);
                gatherVO.setLastSendMoney(lastSendSum);
                gatherVO.setLastRepayMoney(lastRepaySum);
                gatherVOS.add(gatherVO);
            }
        }
        return gatherVOS;
    }


    public void checkConvertCurId(String origCurId, String convertCurId, Date date){
        Date searchEndDate = new Date();
        if (null != date) {
            searchEndDate = DateUtils.getLastMonthDate(date);//预计查询截止日期(取查询结束所在月的最后一天)
        }
        BigDecimal curExrate=BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(origCurId)){
            curExrate = iFcExcrateAdjustService.getExchangeRateBy(origCurId,convertCurId, searchEndDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//距查询日期最近的币别汇率

        }
        if (BigDecimal.ZERO.compareTo(curExrate) == 0) {
            throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
        }
    }
}
