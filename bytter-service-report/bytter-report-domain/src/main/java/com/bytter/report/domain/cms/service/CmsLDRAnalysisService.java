package com.bytter.report.domain.cms.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.domain.cms.dao.CmsLDRAnalysisDao;
import com.bytter.report.domain.cms.vo.CmsLDRAnalysisVO;
import com.bytter.system.api.IBtBankService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtBankDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luxy
 * @Title: CmsLDRAnalysisService
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/1/16 15:43
 */
@Service("CmsLDRAnalysisService")
public class CmsLDRAnalysisService extends BaseService<CmsLDRAnalysisVO,String> {

    @Autowired
    private CmsLDRAnalysisDao cmsLDRAnalysisDao;
    @DubboReference
    private ICorpService iCorpService;
    @DubboReference
    private IFcExcrateAdjustService iFcExcrateAdjustService;
    @DubboReference
    private IBtBankService btBankService;

    @Override
    public BaseDao<CmsLDRAnalysisVO, String> getEntityDao() {
        return cmsLDRAnalysisDao;
    }
    private List<CmsLDRAnalysisVO> corpAnalysisVOs;//按单位分组时的查询结果
    private List<CmsLDRAnalysisVO> bankAnalysisVOs;//按银行分组时的查询结果
    /**
     * 根据融资单位查询
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput corpDatatable(DataTablesInput input, ICurrentUser user){
        List<CorpDTO> corpDTOS = iCorpService.listUserCorp(ResourceConstant.CMS_REPORT_LDR_ANALYSIS_QUERY, user.getId());
        List<String> corpIds = new ArrayList<>();
        for (CorpDTO dto:corpDTOS){
            corpIds.add(dto.getId());
        }
        // if(null==corpAnalysisVOs||corpAnalysisVOs.size()<=0){
        List<CmsLDRAnalysisVO> corpAnalysisVOs = this.queryCorpData(input, corpIds);
        // }
        return this.dealData(input,corpAnalysisVOs);
    }

    /**
     * 根据融资银行查询
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput bankDatatable(DataTablesInput input, ICurrentUser user){
        String bankType=(String)input.getFormdata().get("bankType");
        List<CorpDTO> corpDTOS = iCorpService.listUserCorp(ResourceConstant.CMS_REPORT_LDR_ANALYSIS_QUERY, user.getId());
        List<String> corpIds = new ArrayList<>();
        for (CorpDTO dto:corpDTOS){
            corpIds.add(dto.getId());
        }

        // if(null==bankAnalysisVOs||bankAnalysisVOs.size()<=0){
        List<CmsLDRAnalysisVO> bankAnalysisVOs = new ArrayList<>();
        List<CmsLDRAnalysisVO> bankVOS = this.queryBankData(input, corpIds);
        input.addFilter(MatchTypeEnum.EQ, "netFlag", "1");
        Map<String, Object> params = input.getFormdata();
        params.put("netFlag", "1");
        input.setFormdata(params);

        List<CmsLDRAnalysisVO> netVOS = new ArrayList<>();
        if (null == bankType || StringUtils.isEmpty(bankType) || (StringUtils.isNotEmpty(bankType) && bankType.equals("net"))) {// 查询条件未选择银行类型或查询条件选择了银行类型且选择的是‘结算中心’
            netVOS = this.queryBankData(input, corpIds);// 结算中心作为银行类型的数据
        }
        bankAnalysisVOs.addAll(bankVOS);
        CorpDTO corpDTO = new CorpDTO();
        if (null != netVOS && netVOS.size() > 0) {
            for (CmsLDRAnalysisVO cmsLDRAnalysisVO : netVOS) {
                cmsLDRAnalysisVO.setNetId(cmsLDRAnalysisVO.getBankType());
                corpDTO = iCorpService.get(cmsLDRAnalysisVO.getBankType());
                if (corpDTO != null) {
                    cmsLDRAnalysisVO.setBankTypeName(corpDTO.getName());
                }
            }
            bankAnalysisVOs.addAll(netVOS);
        }
        // }
        return  this.dealData(input,bankAnalysisVOs);
    }

    /**
     * 按单位分组时的数据查询
     * @param input
     * @param corpIds
     * @return
     */
    public List<CmsLDRAnalysisVO> queryCorpData(DataTablesInput input,List<String> corpIds){
        Map<String,CmsLDRAnalysisVO> dataMap=new LinkedHashMap<>();
        List<CmsLDRAnalysisVO> analysisListD=cmsLDRAnalysisDao.corpDatatable(input,corpIds,"D");//截止当前日期的数据

        if(null!=analysisListD&&analysisListD.size()>0){
            for(CmsLDRAnalysisVO analysisVO:analysisListD){
                dataMap.put(analysisVO.getCorpId(),analysisVO);
            }
        }

        String cycle=(String)input.getFormdata().get("cycle");//对比周期
        String[] cycleList=null;
        if(StringUtils.isNotEmpty(cycle)){
            cycleList=cycle.split(",");
        }
        if(cycleList!=null){
            for(int i=0;i<cycleList.length;i++) {
                String compCycle = cycleList[i];//对比周期
                List<CmsLDRAnalysisVO> analysisList = cmsLDRAnalysisDao.corpDatatable(input, corpIds, compCycle);
                if(null!=analysisList&&analysisList.size()>0){
                    if (compCycle.equals("M")) {//较上月末
                        for (CmsLDRAnalysisVO analysis : analysisList) {
                            CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getCorpId());
                            if (null != analysisVO) {
                                analysisVO.setTotalDepositsM(analysis.getTotalDepositsM());
                                analysisVO.setTotalLoansM(analysis.getTotalLoansM());
                            } else {
                                dataMap.put(analysis.getCorpId(), analysis);
                            }
                        }

                    } else if (compCycle.equals("S")) {//较上季度末
                        for (CmsLDRAnalysisVO analysis : analysisList) {
                            CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getCorpId());
                            if (null != analysisVO) {
                                analysisVO.setTotalDepositsS(analysis.getTotalDepositsS());
                                analysisVO.setTotalLoansS(analysis.getTotalLoansS());
                            } else {
                                dataMap.put(analysis.getCorpId(), analysis);
                            }
                        }

                    }else if (compCycle.equals("L")) {//较去年同期
                        for (CmsLDRAnalysisVO analysis : analysisList) {
                            CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getCorpId());
                            if (null != analysisVO) {
                                analysisVO.setTotalDepositsL(analysis.getTotalDepositsL());
                                analysisVO.setTotalLoansL(analysis.getTotalLoansL());
                            } else {
                                dataMap.put(analysis.getCorpId(), analysis);
                            }
                        }

                    }else if (compCycle.equals("Y")) {//较年初
                        for (CmsLDRAnalysisVO analysis : analysisList) {
                            CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getCorpId());
                            if (null != analysisVO) {
                                analysisVO.setTotalDepositsY(analysis.getTotalDepositsY());
                                analysisVO.setTotalLoansY(analysis.getTotalLoansY());
                            } else {
                                dataMap.put(analysis.getCorpId(), analysis);
                            }
                        }

                    }
                }
            }
        }
        List<CmsLDRAnalysisVO> analysisVOS=new ArrayList<>();
        for(String key:dataMap.keySet()){
            analysisVOS.add(dataMap.get(key));
        }
        return analysisVOS;
    }


    /**
     * 按银行分组时的数据查询
     * @param input
     * @param corpIds
     * @return
     */
    public List<CmsLDRAnalysisVO> queryBankData(DataTablesInput input,List<String> corpIds){
        String dtlFlag=(String)input.getFormdata().get("dtlFlag");//查询明细标志
        Map<String,CmsLDRAnalysisVO> dataMap=new LinkedHashMap<>();
        List<CmsLDRAnalysisVO> analysisListD=cmsLDRAnalysisDao.getBankAnalysisList(input,corpIds,"D");

        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){//查询银行或网点明细时按联行号或网点分组
            for(CmsLDRAnalysisVO analysisVO:analysisListD){
                dataMap.put(analysisVO.getBankCode(),analysisVO);
            }
        }else{
            for(CmsLDRAnalysisVO analysisVO:analysisListD){
                dataMap.put(analysisVO.getBankType(),analysisVO);
            }
        }

        String cycle=(String)input.getFormdata().get("cycle");//对比周期
        String[] cycleList=null;
        if(StringUtils.isNotEmpty(cycle)){
            cycleList=cycle.split(",");
        }
        if(cycleList!=null){
            for(int i=0;i<cycleList.length;i++) {
                String compCycle = cycleList[i];//对比周期
                List<CmsLDRAnalysisVO> analysisList = cmsLDRAnalysisDao.getBankAnalysisList(input, corpIds, compCycle);
                if(null!=analysisList&&analysisList.size()>0){
                    if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){//查询明细时按联行号分组
                        if (compCycle.equals("M")) {
                            for (CmsLDRAnalysisVO analysis : analysisList) {
                                CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getBankCode());
                                if (null != analysisVO) {
                                    analysisVO.setTotalDepositsM(analysis.getTotalDepositsM());
                                    analysisVO.setTotalLoansM(analysis.getTotalLoansM());
                                } else {
                                    dataMap.put(analysis.getBankCode(), analysis);
                                }
                            }

                        } else if (compCycle.equals("S")) {
                            for (CmsLDRAnalysisVO analysis : analysisList) {
                                CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getBankCode());
                                if (null != analysisVO) {
                                    analysisVO.setTotalDepositsS(analysis.getTotalDepositsS());
                                    analysisVO.setTotalLoansS(analysis.getTotalLoansS());
                                } else {
                                    dataMap.put(analysis.getBankCode(), analysis);
                                }
                            }

                        }else if (compCycle.equals("L")) {
                            for (CmsLDRAnalysisVO analysis : analysisList) {
                                CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getBankCode());
                                if (null != analysisVO) {
                                    analysisVO.setTotalDepositsL(analysis.getTotalDepositsL());
                                    analysisVO.setTotalLoansL(analysis.getTotalLoansL());
                                } else {
                                    dataMap.put(analysis.getBankCode(), analysis);
                                }
                            }
                        }else if (compCycle.equals("Y")) {
                            for (CmsLDRAnalysisVO analysis : analysisList) {
                                CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getBankCode());
                                if (null != analysisVO) {
                                    analysisVO.setTotalDepositsY(analysis.getTotalDepositsY());
                                    analysisVO.setTotalLoansY(analysis.getTotalLoansY());
                                } else {
                                    dataMap.put(analysis.getBankCode(), analysis);
                                }
                            }
                        }
                    }else{
                        if (compCycle.equals("M")) {
                            for (CmsLDRAnalysisVO analysis : analysisList) {
                                CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getBankType());
                                if (null != analysisVO) {
                                    analysisVO.setTotalDepositsM(analysis.getTotalDepositsM());
                                    analysisVO.setTotalLoansM(analysis.getTotalLoansM());
                                } else {
                                    dataMap.put(analysis.getBankType(), analysis);
                                }
                            }

                        } else if (compCycle.equals("S")) {
                            for (CmsLDRAnalysisVO analysis : analysisList) {
                                CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getBankType());
                                if (null != analysisVO) {
                                    analysisVO.setTotalDepositsS(analysis.getTotalDepositsS());
                                    analysisVO.setTotalLoansS(analysis.getTotalLoansS());
                                } else {
                                    dataMap.put(analysis.getBankType(), analysis);
                                }
                            }

                        }else if (compCycle.equals("L")) {
                            for (CmsLDRAnalysisVO analysis : analysisList) {
                                CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getBankType());
                                if (null != analysisVO) {
                                    analysisVO.setTotalDepositsL(analysis.getTotalDepositsL());
                                    analysisVO.setTotalLoansL(analysis.getTotalLoansL());
                                } else {
                                    dataMap.put(analysis.getBankType(), analysis);
                                }
                            }
                        }else if (compCycle.equals("Y")) {
                            for (CmsLDRAnalysisVO analysis : analysisList) {
                                CmsLDRAnalysisVO analysisVO = dataMap.get(analysis.getBankType());
                                if (null != analysisVO) {
                                    analysisVO.setTotalDepositsY(analysis.getTotalDepositsY());
                                    analysisVO.setTotalLoansY(analysis.getTotalLoansY());
                                } else {
                                    dataMap.put(analysis.getBankType(), analysis);
                                }
                            }
                        }
                    }

                }
            }
        }
        List<CmsLDRAnalysisVO> analysisVOS=new ArrayList<>();
        for(String key:dataMap.keySet()){
            analysisVOS.add(dataMap.get(key));
        }
        return analysisVOS;
    }


    /**
     * 数据处理:是否折合本位币，金额的单位转换，数据的存贷比计算，分页数据处理
     * @param input
     * @param ldrAnalysisVOS
     * @return
     */
    public DataTablesOutput dealData(DataTablesInput input,List<CmsLDRAnalysisVO> ldrAnalysisVOS){
        DataTablesOutput output = new DataTablesOutput();
        if (null == ldrAnalysisVOS || ldrAnalysisVOS.size() <= 0) {
            return output;
        }
        List<CmsLDRAnalysisVO> dataList=new ArrayList<>();//输出到前端的数据
        int startIndex=input.getStart();
        int endIndex=startIndex+input.getLength();
        if(endIndex>ldrAnalysisVOS.size()){
            endIndex=ldrAnalysisVOS.size();
        }
        if(input.getLength()<ldrAnalysisVOS.size()){//数据总数大于每页显示条数，需要把数据进行截取
            dataList=ldrAnalysisVOS.subList(startIndex,endIndex);
        }else{
            dataList=ldrAnalysisVOS;
        }

        String isConvertStandCur=(String)input.getFormdata().get("isConvertStandCur");//是否折合本位币
        String curId=(String)input.getFormdata().get("curId");//查询币别
        String moneyUnit=(String)input.getFormdata().get("moneyUnit");//币别单位

        if(null!=dataList&&dataList.size()>0){
            if(isConvertStandCur.equals(CmsConstant.IS_TRUE)){//折合本位币时把不同币别折合后数据合并
                String endDate=(String)input.getFormdata().get("endDate");
                String convertCurId = input.getFormdata().get("convertCurId").toString();//折合币别
                changeToStandCur(dataList,curId, convertCurId, endDate);
            }
            if(!moneyUnit.equals(CmsConstant.MONEY_TYPE_1)){//金额单位不是'元'，需要换算
                conversion( dataList,moneyUnit);
            }
            for (CmsLDRAnalysisVO dataVO:dataList){
                this.setDepositLoanRate(dataVO);
            }
        }
        output.setRecordsTotal(ldrAnalysisVOS.size());
        output.setRecordsFiltered(ldrAnalysisVOS.size());
        output.setData(dataList);
        output.setDraw(input.getDraw());
        return output;
    }


    /**
     * 设置存贷比
     */
    public void setDepositLoanRate(CmsLDRAnalysisVO dataVO){
        BigDecimal depositLoanRateD=BigDecimal.ZERO;
        BigDecimal depositLoanRateM=BigDecimal.ZERO;
        BigDecimal depositLoanRateS=BigDecimal.ZERO;
        BigDecimal depositLoanRateL=BigDecimal.ZERO;
        BigDecimal depositLoanRateY=BigDecimal.ZERO;
        if(null!=dataVO.getTotalLoansD()&&dataVO.getTotalLoansD().compareTo(BigDecimal.ZERO)!=0){
            depositLoanRateD=dataVO.getTotalDepositsD().divide(dataVO.getTotalLoansD(),4, BigDecimal.ROUND_HALF_UP);
        }
        if(null!=dataVO.getTotalLoansM()&&dataVO.getTotalLoansM().compareTo(BigDecimal.ZERO)!=0){
            depositLoanRateM=dataVO.getTotalDepositsM().divide(dataVO.getTotalLoansM(),4, BigDecimal.ROUND_HALF_UP);
        }
        if(null!=dataVO.getTotalLoansS()&&dataVO.getTotalLoansS().compareTo(BigDecimal.ZERO)!=0){
            depositLoanRateS=dataVO.getTotalDepositsS().divide(dataVO.getTotalLoansS(),4, BigDecimal.ROUND_HALF_UP);
        }
        if(null!=dataVO.getTotalLoansL()&&dataVO.getTotalLoansL().compareTo(BigDecimal.ZERO)!=0){
            depositLoanRateL=dataVO.getTotalDepositsL().divide(dataVO.getTotalLoansL(),4, BigDecimal.ROUND_HALF_UP);
        }
        if(null!=dataVO.getTotalLoansY()&&dataVO.getTotalLoansY().compareTo(BigDecimal.ZERO)!=0){
            depositLoanRateY=dataVO.getTotalDepositsY().divide(dataVO.getTotalLoansY(),4, BigDecimal.ROUND_HALF_UP);
        }
        dataVO.setDepositLoanRateD(depositLoanRateD);
        dataVO.setDepositLoanRateM(depositLoanRateM);
        dataVO.setDepositLoanRateS(depositLoanRateS);
        dataVO.setDepositLoanRateL(depositLoanRateL);
        dataVO.setDepositLoanRateY(depositLoanRateY);

    }

    /**金额单位换算*/
    public void conversion(List<CmsLDRAnalysisVO>list,String moneyType){
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
            for (CmsLDRAnalysisVO vo : list){
                vo.setTotalDepositsD(vo.getTotalDepositsD().divide(divisor));
                vo.setTotalDepositsM(vo.getTotalDepositsM()==null?BigDecimal.ZERO:vo.getTotalDepositsM().divide(divisor));
                vo.setTotalDepositsS(vo.getTotalDepositsS()==null?BigDecimal.ZERO:vo.getTotalDepositsS().divide(divisor));
                vo.setTotalDepositsL(vo.getTotalDepositsL()==null?BigDecimal.ZERO:vo.getTotalDepositsL().divide(divisor));
                vo.setTotalDepositsY(vo.getTotalDepositsY()==null?BigDecimal.ZERO:vo.getTotalDepositsY().divide(divisor));
                vo.setTotalLoansD(vo.getTotalLoansD().divide(divisor));
                vo.setTotalLoansM(vo.getTotalLoansM()==null?BigDecimal.ZERO:vo.getTotalLoansM().divide(divisor));
                vo.setTotalLoansS(vo.getTotalLoansS()==null?BigDecimal.ZERO:vo.getTotalLoansS().divide(divisor));
                vo.setTotalLoansL(vo.getTotalLoansL()==null?BigDecimal.ZERO:vo.getTotalLoansL().divide(divisor));
                vo.setTotalLoansY(vo.getTotalLoansY()==null?BigDecimal.ZERO:vo.getTotalLoansY().divide(divisor));
            }
        }
    }

    /**
     * 折合本位币
     */
    public List<CmsLDRAnalysisVO> changeToStandCur(List<CmsLDRAnalysisVO> list,String curId,String convertCurId,String endDate){
        Date searchEndDate= DateUtils.parse(endDate+" 23:59:59"); ////查询截止日期,拼接时分秒便于查询汇率
        if(null!=list&&list.size()>0){
            BigDecimal curExrate=BigDecimal.ONE;
            for (CmsLDRAnalysisVO vo : list) {
                if (StringUtils.isNotEmpty(convertCurId)&&!convertCurId.equals(vo.getCurId())) {
                    curExrate = iFcExcrateAdjustService.getExchangeRateBy(vo.getCurId(),convertCurId, searchEndDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//取离查询日期最近的币别汇率
                }
                if(curExrate.equals(BigDecimal.ONE)){//本位币无需折合
                    continue;
                }
                vo.setTotalDepositsD(vo.getTotalDepositsD().multiply(curExrate));
                vo.setTotalLoansD(vo.getTotalLoansD().multiply(curExrate));
                vo.setTotalDepositsM(vo.getTotalDepositsM()==null?BigDecimal.ZERO:vo.getTotalDepositsM().multiply(curExrate));
                vo.setTotalDepositsS(vo.getTotalDepositsS()==null?BigDecimal.ZERO:vo.getTotalDepositsS().multiply(curExrate));
                vo.setTotalDepositsL(vo.getTotalDepositsL()==null?BigDecimal.ZERO:vo.getTotalDepositsL().multiply(curExrate));
                vo.setTotalDepositsY(vo.getTotalDepositsY()==null?BigDecimal.ZERO:vo.getTotalDepositsY().multiply(curExrate));
                vo.setTotalLoansM(vo.getTotalLoansM()==null?BigDecimal.ZERO:vo.getTotalLoansM().multiply(curExrate));
                vo.setTotalLoansS(vo.getTotalLoansS()==null?BigDecimal.ZERO:vo.getTotalLoansS().multiply(curExrate));
                vo.setTotalLoansL(vo.getTotalLoansL()==null?BigDecimal.ZERO:vo.getTotalLoansL().multiply(curExrate));
                vo.setTotalLoansY(vo.getTotalLoansY()==null?BigDecimal.ZERO:vo.getTotalLoansY().multiply(curExrate));
            }
        }

        return list;
    }

    /**
     *
     * 根据银行类型查询某银行类型下各银行存贷比明细
     */
    public List<CmsLDRAnalysisVO> getBankDtl(DataTablesInput input){
        List<CmsLDRAnalysisVO> bankDtls=this.queryBankData(input,null);//有银行的数据
        if (CollectionUtil.isEmpty(bankDtls)) {
            return new ArrayList<>();
        }
        List<BtBankDTO> btBankDTOS = btBankService.getListByCodes(bankDtls.stream().map(CmsLDRAnalysisVO::getBankCode).collect(Collectors.toList()));
        Map<String, String> bankNameMap = btBankDTOS.stream().collect(Collectors.toMap(BtBankDTO::getBankCode, BtBankDTO::getBankName, (pre, next) -> next));
        for (CmsLDRAnalysisVO bankDtl : bankDtls) {
            bankDtl.setBankName(bankNameMap.get(bankDtl.getBankCode()));
        }
        String isConvertStandCur=(String)input.getFormdata().get("isConvertStandCur");//是否折合本位币
        String moneyUnit=(String)input.getFormdata().get("moneyUnit");//币别单位
        String curId=(String)input.getFormdata().get("curId");//查询币别
        List<CmsLDRAnalysisVO> gatherDtls=new ArrayList<>();
        if(isConvertStandCur.equals(CmsConstant.IS_TRUE)){//是否折合本位币
            String endDate=(String)input.getFormdata().get("endDate");
            String convertCurId = input.getFormdata().get("convertCurId").toString();//折合币别
            changeToStandCur(bankDtls,curId,convertCurId,endDate);
            gatherDtls=gatherData(bankDtls,CmsConstant.SORT_TYPE_1,CmsConstant.IS_TRUE);
        }else{
            gatherDtls=bankDtls;
        }
        if(!moneyUnit.equals(CmsConstant.MONEY_TYPE_1)){//金额单位不是'元'，需要换算
            conversion( gatherDtls,moneyUnit);
        }
        for (CmsLDRAnalysisVO dataVO:gatherDtls){
            this.setDepositLoanRate(dataVO);
        }
        return gatherDtls;
    }

    /**
     *
     * 根据结算中心查询结算中心下各单位的存贷比明细
     */
    public List<CmsLDRAnalysisVO> getCorpDtl(DataTablesInput input){
        List<CmsLDRAnalysisVO> netDtls=this.queryBankData(input,null);
        String isConvertStandCur=(String)input.getFormdata().get("isConvertStandCur");//是否折合本位币
        String moneyUnit=(String)input.getFormdata().get("moneyUnit");//币别单位
        String curId=(String)input.getFormdata().get("curId");//查询币别
        List<CmsLDRAnalysisVO> gatherDtls=new ArrayList<>();
        if(isConvertStandCur.equals(CmsConstant.IS_TRUE)){//是否折合本位币
            String endDate=(String)input.getFormdata().get("endDate");
            String convertCurId = input.getFormdata().get("convertCurId").toString();//折合币别
            changeToStandCur(netDtls,curId,convertCurId,endDate);
        }

        gatherDtls=netDtls;
        if(!moneyUnit.equals(CmsConstant.MONEY_TYPE_1)){//金额单位不是'元'，需要换算
            conversion( gatherDtls,moneyUnit);
        }
        CorpDTO corpDTO=new CorpDTO();
        for (CmsLDRAnalysisVO dataVO:gatherDtls){
            corpDTO=iCorpService.get(dataVO.getBankCode());
            if(corpDTO!=null){
                dataVO.setBankName(corpDTO.getName());
            }
            this.setDepositLoanRate(dataVO);
        }
        return gatherDtls;
    }

    /**
     * 折合本位币时需要将折合后相同单位不同币种的数据合并
     * @param list
     * @param sortType 统计维度 0--单位  1--银行
     * @param dtlFlag 是否查询明细 0-否 1-是
     * @return
     */
    public List<CmsLDRAnalysisVO> gatherData(List<CmsLDRAnalysisVO> list,String sortType,String dtlFlag){
        List<CmsLDRAnalysisVO> gatherVOS=new ArrayList<>();
        if (null != list && list.size() > 0) {
            Map<String, List<CmsLDRAnalysisVO>> gatherMap = new HashMap<>();//用于存放相同单位的集合
            for (CmsLDRAnalysisVO vo : list) {
                String mapKey="";
                if(dtlFlag.equals(CmsConstant.IS_FALSE)){//不查明细
                    if(sortType.equals(CmsConstant.SORT_TYPE_0)){//统计维度为单位
                        mapKey=vo.getCorpId();
                    }else{
                        mapKey=vo.getBankType();
                    }
                }else{
                    mapKey=vo.getBankCode();
                }

                List<CmsLDRAnalysisVO> gatherList = gatherMap.get(mapKey);
                if (null != gatherList && gatherList.size() > 0) {
                    gatherList.add(vo);
                } else {
                    List<CmsLDRAnalysisVO> voList = new ArrayList<>();
                    voList.add(vo);
                    gatherMap.put(mapKey, voList);
                }
            }

            CmsLDRAnalysisVO gatherVO;
            for (String key : gatherMap.keySet()) {
                gatherVO=new CmsLDRAnalysisVO();
                List<CmsLDRAnalysisVO> voList=gatherMap.get(key);
                gatherVO.setCorpId(voList.get(0).getCorpId());
                gatherVO.setCorpName(voList.get(0).getCorpName());
                gatherVO.setBankType(voList.get(0).getBankType());
                gatherVO.setBankTypeName(voList.get(0).getBankTypeName());
                gatherVO.setNetId(voList.get(0).getNetId());
                gatherVO.setBankCode(voList.get(0).getBankCode());
                gatherVO.setBankName(voList.get(0).getBankName());
                BigDecimal depositsDSum=BigDecimal.ZERO;
                BigDecimal depositsMSum=BigDecimal.ZERO;
                BigDecimal depositsSSum=BigDecimal.ZERO;
                BigDecimal depositsLSum=BigDecimal.ZERO;
                BigDecimal depositsYSum=BigDecimal.ZERO;
                BigDecimal loansDSum=BigDecimal.ZERO;
                BigDecimal loansMSum=BigDecimal.ZERO;
                BigDecimal loansSSum=BigDecimal.ZERO;
                BigDecimal loansLSum=BigDecimal.ZERO;
                BigDecimal loansYSum=BigDecimal.ZERO;
                for(CmsLDRAnalysisVO vo:voList){
                    depositsDSum=depositsDSum.add(vo.getTotalDepositsD());
                    depositsMSum=depositsMSum.add(vo.getTotalDepositsM());
                    depositsSSum=depositsSSum.add(vo.getTotalDepositsS());
                    depositsLSum=depositsLSum.add(vo.getTotalDepositsL());
                    depositsYSum=depositsYSum.add(vo.getTotalDepositsY());
                    loansDSum=loansDSum.add(vo.getTotalLoansD());
                    loansMSum=loansMSum.add(vo.getTotalLoansM());
                    loansSSum=loansSSum.add(vo.getTotalLoansS());
                    loansLSum=loansLSum.add(vo.getTotalLoansL());
                    loansYSum=loansYSum.add(vo.getTotalLoansY());
                }
                gatherVO.setTotalDepositsD(depositsDSum);
                gatherVO.setTotalDepositsM(depositsMSum);
                gatherVO.setTotalDepositsS(depositsSSum);
                gatherVO.setTotalDepositsL(depositsLSum);
                gatherVO.setTotalDepositsY(depositsYSum);
                gatherVO.setTotalLoansD(loansDSum);
                gatherVO.setTotalLoansM(loansMSum);
                gatherVO.setTotalLoansS(loansSSum);
                gatherVO.setTotalLoansL(loansLSum);
                gatherVO.setTotalLoansY(loansYSum);
                gatherVOS.add(gatherVO);
            }
        }
        return gatherVOS;
    }


    /**
    *币种校验
    *@Param origCurId	
    *@Param convertCurId	
    *@Param date
    *@return 
    */
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
