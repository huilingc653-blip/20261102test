package com.bytter.report.domain.cms.service;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.finance.api.ICmsProvideLoanInfoService;
import com.bytter.finance.dto.CmsProvideLoanInfoDTO;
import com.bytter.report.api.constant.ResourceConstant;
import com.bytter.report.domain.cms.dao.InterestReportDao;
import com.bytter.report.domain.cms.vo.BankLoanWithholdingVO;
import com.bytter.report.domain.cms.vo.CmsProvideInterestVO;
import com.bytter.report.domain.cms.vo.InterestReportVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service("interestReportService")
public class InterestReportService extends BaseService {
    @Autowired
	private InterestReportDao interestReportDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @DubboReference(check = false)
    private ICmsProvideLoanInfoService iCmsProvideLoanInfoService;

    /**
     * 查询操作
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listInterest(DataTablesInput input, ICurrentUser user){
        List<String> ids = corpService.getUserCorpIds(ResourceConstant.BI_CMS_INTEREST_QUERY, user.getId());
        DataTablesOutput output = interestReportDao.listInterest(input, ids);
        List<InterestReportVO> list=output.getData();
        Map<String, String> corpMap = corpService.getNameByIds(ids);
        Map<String, String> currencyMap = btCurrencyService.getCurrencyNameMap();
        for(InterestReportVO vo:list){
            vo.setCorpName(corpMap.get(vo.getCorpId()));//单位
            vo.setCurName(currencyMap.get(vo.getCurId()));//币别
            if (ObjectUtil.isNotEmpty(vo.getTernUnit()) && ObjectUtil.isNotEmpty(vo.getPlTerm())) {
                if (vo.getTernUnit().equals("M")) {
                    vo.setPlTerm(vo.getPlTerm().divide(BigDecimal.valueOf(12),2, BigDecimal.ROUND_HALF_UP));
                }
                if (vo.getTernUnit().equals("D")){
                    vo.setPlTerm(vo.getPlTerm().divide(BigDecimal.valueOf(365),2, BigDecimal.ROUND_HALF_UP));
                }
            }
            if (ObjectUtil.isNotEmpty(vo.getPlRate())){
                vo.setPlRate(vo.getPlRate().multiply(BigDecimal.valueOf(100)));
            }
        }
        return output;
    }
    @Override
    public BaseDao getEntityDao() {
        return interestReportDao;
    }

    /**
     * 功能介绍 : 银行借款利息预提表<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryBankLoanWithholding(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.BI_CMS_INTEREST_WITHHOLDING_QUERY, user.getId());
        DataTablesOutput dataTablesOutput = interestReportDao.queryBankLoanWithholding(dataTablesInput, corpAuth);
        disposalOfInterest(dataTablesOutput,dataTablesInput);
        return dataTablesOutput;
    }

    /**
     * 处理预提利息
     * @param output
     */
    public void disposalOfInterest(DataTablesOutput output,DataTablesInput input) {
        List<BankLoanWithholdingVO> vos = output.getData();
        if (ObjectUtil.isNotEmpty(vos)) {
            // 预提利息开始月份;结束月份
            String beginMonth = (String) input.getFormdata().get("ged_withholdingDate");
            String endMonth = (String) input.getFormdata().get("led_withholdingDate");
            long monthsBetween = DateUtil.betweenMonth(DateUtil.parse(beginMonth + "-01"), DateUtil.parse(endMonth + "-01"), true) + 1;

            // 初始化每个月份的monthInterest数据
            List<String> sendCodes = new ArrayList<>();
            for (BankLoanWithholdingVO vo : vos) {
                vo.setMonthInterest(new BigDecimal[(int) monthsBetween]);
                sendCodes.add(vo.getSendBillCode());
            }

            // 根据发放单号查询利息明细
            List<CmsProvideInterestVO> interestList = interestReportDao.queryInterestList(sendCodes,null,null);
            if (ObjectUtil.isNotEmpty(interestList)) {
                // 根据CmsProvideInterestVO里面的billCode分组interestList
                Map<String, List<CmsProvideInterestVO>> billCodeMap = new HashMap<>();
                for (CmsProvideInterestVO vo : interestList) {
                    List<CmsProvideInterestVO> list = billCodeMap.get(vo.getBillCode());
                    if (ObjectUtil.isEmpty(list)) {
                        list = new ArrayList<>();
                    }
                    list.add(vo);
                    billCodeMap.put(vo.getBillCode(), list);
                }
                // 清空interestList
                interestList.clear();

                // 根据发放单号分组后的数据，计算出每天利息
                for (BankLoanWithholdingVO vo : vos) {
                    CmsProvideLoanInfoDTO infoDTO= iCmsProvideLoanInfoService.getInfoByBillCode(vo.getSendBillCode());
                    if (ObjectUtil.isNotEmpty(billCodeMap.get(vo.getSendBillCode()))) {
                        // 每天利息map
                        Map<String, BigDecimal> interestMap = new HashMap<>();
                        for (CmsProvideInterestVO interestVO : billCodeMap.get(vo.getSendBillCode())) {
                            // 起息日期；结束日期
                            Date interestStartDate = interestVO.getInterestStartDate();
                            Date blnInstDate = interestVO.getBlnInstDate();
                            if (ObjectUtil.isNotEmpty(interestStartDate) && ObjectUtil.isNotEmpty(blnInstDate)) {
                                // 获取起息日期和结束日期之间的天数
                                long days = DateUtil.betweenDay(interestStartDate, blnInstDate, true) + 1;
                                for (int i = 0; i < days; i++) {
                                    String day = DateUtil.format(DateUtil.offsetDay(interestStartDate, i), "yyyy-MM-dd");
                                    // 每日利息=本金*利率/360
                                    BigDecimal interest = interestVO.getLoanMoney().multiply(interestVO.getInterestRate()).divide(new BigDecimal(infoDTO.getRateBase()), 20, BigDecimal.ROUND_HALF_UP);
                                    interestMap.put(day, interest);
                                }
                            }
                        }
                        // 计算区间利息 区间利息开始日期，结束日期
                        String beginDate = (String) input.getFormdata().get("ged_interestDate");
                        String endDate = (String) input.getFormdata().get("led_interestDate");
                        Date begin = DateUtil.parse(beginDate);
                        BigDecimal sectionInterest = BigDecimal.ZERO;
                        int i = 0;
                        while (true) {
                            begin = DateUtil.offsetDay(begin, i);
                            if (begin.getTime() > DateUtil.parse(endDate).getTime()) {
                                break;
                            }
                            String day = DateUtil.formatDate(begin);
                            if (interestMap.containsKey(day)) {
                                sectionInterest = sectionInterest.add(interestMap.get(day));
                            }
                            if (i == 0) i = 1;
                        }
                        // 如果sectionInterest大于0则保留2位小数点
                        if (sectionInterest.compareTo(BigDecimal.ZERO) > 0) {
                            vo.setSectionInterest(sectionInterest.setScale(2, BigDecimal.ROUND_HALF_UP));
                        }

                        // 按月份合计interestMap里面的利息
                        Map<String, BigDecimal> monthMap = new HashMap<>();
                        for (Map.Entry<String, BigDecimal> entry : interestMap.entrySet()) {
                            String key = entry.getKey();
                            BigDecimal value = entry.getValue();
                            String month = key.substring(0, 7);
                            BigDecimal monthValue = monthMap.get(month);
                            if (ObjectUtil.isNotEmpty(monthValue)) {
                                monthValue = monthValue.add(value);
                            } else {
                                monthValue = value;
                            }
                            monthMap.put(month, monthValue);
                        }
                        // 清空interestMap
                        interestMap.clear();

                        // 设置月份值
                        BigDecimal[] monthInterest = vo.getMonthInterest();
                        int m = 0;
                        begin = DateUtil.parseDate(beginMonth + "-01");
                        for (int j = 0; j < monthsBetween; j++) {
                            begin = DateUtil.offsetMonth(begin, m);
                            String month = DateUtil.format(begin, "yyyy-MM");
                            if (monthMap.containsKey(month)) {
                                // monthMap.get(month) 保留2位小数点
                                monthInterest[j] = monthMap.get(month).setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                            if (j == 0) m = 1;
                        }
                        vo.setMonthInterest(monthInterest);
                    }
                }
            }
        }
    }
}
