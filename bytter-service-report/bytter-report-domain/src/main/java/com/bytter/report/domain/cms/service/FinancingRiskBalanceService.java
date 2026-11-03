package com.bytter.report.domain.cms.service;
import com.bytter.bill.api.bill.service.IAcceBillBalanceQueryService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.finance.api.IProvideLoanQueryService;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.dto.FinanceLoanDTO;
import com.bytter.report.domain.cms.vo.FinancingRiskBalanceVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b> 融资风险汇总表  Service <br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2020/12/11 <br>
 */
@Service("financingRiskBalanceService")
public class FinancingRiskBalanceService extends BaseService {
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IProvideLoanQueryService provideLoanQueryService;
    @DubboReference(check = false)
	private IAcceBillBalanceQueryService acceBillBalanceQueryService;
    public DataTablesOutput getDataTablesOutput(DataTablesInput input, ICurrentUser user) {
        Object curIdObj = input.getFormdata().get("curId");
        Object queryDateObj = input.getFormdata().get("queryDate");
        if ("".equals(curIdObj)) {
            return new DataTablesOutput(input.getDraw(), Collections.emptyList(), 0, 0);
        }
        String curId = curIdObj.toString();
        Date queryDate = null;
        if (queryDateObj == null) {
            // 默认取当前日期
            queryDate = DateUtils.getDate();
        } else {
            queryDate = DateUtils.parse(queryDateObj.toString(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
        }
        long startTime = System.currentTimeMillis();
        // 获取二级成员单位
        List<CorpDTO> levelCorpList = corpService.listLevelCorpAndChildrenCorp(ResourceConstant.CMS_FINANCING_RISK_BALANCE_VIEW, user.getId(), 2);
        // 手动分页获取二级成员单位数据
        List<CorpDTO> dataCorpList = getPageCorpList(levelCorpList, input.getPageNumber(), input.getPageSize());
        List<FinancingRiskBalanceVO> financingRiskBalanceVOList = new ArrayList<>();
        // 取业务数据
        //long getBusinessTime = System.currentTimeMillis();
        for (CorpDTO corpDTO : dataCorpList) {
            FinancingRiskBalanceVO data = new FinancingRiskBalanceVO();
            List<String> childrenCorpIds = getCorpIds(corpDTO.getChildrenCorp());
            childrenCorpIds.add(corpDTO.getId());
            data.setCorpId(corpDTO.getId());
            data.setCorpName(corpDTO.getName());
            // 流贷余额 = 银行借款模块中的借款余额
            BigDecimal flowLeanRiskBal = getFlowLoanRiskBal(childrenCorpIds, curId, queryDate);
            data.setFlowLoanRiskBal(flowLeanRiskBal);
            // 票据票面余额 = 开出应付票据的余额+收入应收票据
            BigDecimal billRiskBal = getBillRiskBal(childrenCorpIds, curId, queryDate);
            data.setBillRiskBal(billRiskBal);
            // 债券票面余额 = 直接融资的余额
            BigDecimal bondRiskBal = getBondRiskBal(childrenCorpIds, curId, queryDate);
            data.setBondRiskBal(bondRiskBal);
            // 融资合计 = 流贷余额+票据票面余额+债券票面余额
            BigDecimal riskSum = BigDecimal.ZERO;
            riskSum = riskSum.add(data.getFlowLoanRiskBal());
            riskSum = riskSum.add(data.getBillRiskBal());
            riskSum = riskSum.add(data.getBondRiskBal());
            data.setFinancingSum(riskSum);
            // 利息支出合计 = 借款利息支出 + 票据利息支出 + 直接融资利息支出
            BigDecimal interestSum = BigDecimal.ZERO;
            interestSum = interestSum.add(getLoanInterest(childrenCorpIds, curId, queryDate));
            interestSum = interestSum.add(getBillInterest(childrenCorpIds, curId, queryDate));
            interestSum = interestSum.add(getDateDirectFinancingInterest(childrenCorpIds, curId, queryDate));
            // TODO: 利息净支出合计 = 上面三项业务的利息支出合计
            data.setNetInterestSum(interestSum);
            // 环比增幅  较去年底增幅
            assembleIncrease(data, childrenCorpIds, curId, queryDate);
            // 转化为万元
            data.setFlowLoanRiskBal(toTenThousandYuan(data.getFlowLoanRiskBal()));
            data.setBillRiskBal(toTenThousandYuan(data.getBillRiskBal()));
            data.setBondRiskBal(toTenThousandYuan(data.getBondRiskBal()));
            data.setFinancingSum(toTenThousandYuan(data.getFinancingSum()));
            data.setNetInterestSum(toTenThousandYuan(data.getNetInterestSum()));
            financingRiskBalanceVOList.add(data);
        }
        //System.out.println("====取业务数据耗时：" + (System.currentTimeMillis() - getBusinessTime) + "ms");
        //System.out.println("====总计耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        DataTablesOutput output = new DataTablesOutput();
        output.setData(financingRiskBalanceVOList);
        output.setDraw(input.getDraw());
        output.setRecordsTotal(levelCorpList.size());
        output.setRecordsFiltered(levelCorpList.size());
        return output;
    }
    /**
     * 流贷风险余额 = 银行借款模块中的借款余额
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 流贷风险余额
     */
    private BigDecimal getFlowLoanRiskBal(List<String> childrenCorpIds,
                                          String curId,
                                          Date queryDate) {
        List<FinanceLoanDTO> financeLoanDTOList = provideLoanQueryService.queryProvideFinance(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            // 借款余额
            BigDecimal loanMoneySum = BigDecimal.ZERO;
            for (FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getMoney() != null) {
                    loanMoneySum = loanMoneySum.add(financeLoanDTO.getMoney());
                }
            }
            return loanMoneySum;
        }
        return BigDecimal.ZERO;
    }
    /**
     * 票据票面余额 = 直接融资的票据余额
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 票据票面余额
     */
    private BigDecimal getBillRiskBal(List<String> childrenCorpIds,
                                      String curId,
                                      Date queryDate) {
        List<com.bytter.bill.api.bill.dto.FinanceLoanDTO> financeLoanDTOList = acceBillBalanceQueryService.queryProvideFinance(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            // 票据余额
            BigDecimal billBalanceMoneySum = BigDecimal.ZERO;
            for (com.bytter.bill.api.bill.dto.FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getMoney() != null) {
                    billBalanceMoneySum = billBalanceMoneySum.add(financeLoanDTO.getMoney());
                }
            }
            return billBalanceMoneySum;
        }
        return BigDecimal.ZERO;
    }
    /**
     * 债券风险余额 = 直接融资的余额
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 债券风险余额
     */
    private BigDecimal getBondRiskBal(List<String> childrenCorpIds, String curId, Date queryDate) {
        List<FinanceLoanDTO> financeLoanDTOList = provideLoanQueryService.queryInlFinance(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            BigDecimal directFinancingBalanceMoney = BigDecimal.ZERO;
            for (FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getMoney() != null) {
                    directFinancingBalanceMoney = directFinancingBalanceMoney.add(financeLoanDTO.getMoney());
                }
            }
            return directFinancingBalanceMoney;
        }
        return BigDecimal.ZERO;
    }
    /**
     * 环比增幅   较去年底增幅
     * - 环比增幅  融资合计数与去年同期相比的增幅
     * - 较去年底增幅  融资合计数与去年底相比的增幅
     * - 融资合计 = 流贷余额+票据票面余额+债券票面余额
     *
     * @param data
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     */
    private void assembleIncrease(FinancingRiskBalanceVO data,
                                  List<String> childrenCorpIds,
                                  String curId,
                                  Date queryDate) {
        // 查询日期融资合计数 融资合计 = 流贷余额+票据票面余额+债券票面余额
        BigDecimal thisYearFinancingMoneySum = data.getFinancingSum();
        data.setThisYearFinancingMoneySum(thisYearFinancingMoneySum);
        Date lastMonthSameDate = DateUtils.addMonth(queryDate, -1);
        // 上个月同期 流贷余额
        BigDecimal lastMonthSameDateLoanBalanceMoney = getFlowLoanRiskBal(childrenCorpIds, curId, lastMonthSameDate);
        // 上个月同期 票据票面余额
        BigDecimal lastMonthSameDateBillBalanceMoney = getBillRiskBal(childrenCorpIds, curId, lastMonthSameDate);
        // 上个月同期 债券票面余额
        BigDecimal lastMonthSameDateDirectFinancingBalanceMoney = getBondRiskBal(childrenCorpIds, curId, lastMonthSameDate);
        // 上个月同期融资合计数
        BigDecimal lastMonthSameDateFinancingMoneySum = BigDecimal.ZERO;
        lastMonthSameDateFinancingMoneySum = lastMonthSameDateFinancingMoneySum.add(lastMonthSameDateLoanBalanceMoney);
        lastMonthSameDateFinancingMoneySum = lastMonthSameDateFinancingMoneySum.add(lastMonthSameDateBillBalanceMoney);
        lastMonthSameDateFinancingMoneySum = lastMonthSameDateFinancingMoneySum.add(lastMonthSameDateDirectFinancingBalanceMoney);
        data.setLastMonthSameDateFinancingMoneySum(lastMonthSameDateFinancingMoneySum);
        //融资合计 环比增幅 = 总额 - 上个月同期总额
        BigDecimal chainIncrease = thisYearFinancingMoneySum.subtract(lastMonthSameDateFinancingMoneySum);
        data.setChainIncrease(toTenThousandYuan(chainIncrease));
        // 环比增长比例 = 环比增幅 / 上个月同期总额
        if (lastMonthSameDateFinancingMoneySum.compareTo(BigDecimal.ZERO)>0) {
//        if (!BigDecimal.ZERO.equals(lastMonthSameDateFinancingMoneySum)) {
            if (!BigDecimal.ZERO.equals(chainIncrease)) {
                data.setChainIncreaseRatioComparedLastMonth(chainIncrease.divide(lastMonthSameDateFinancingMoneySum, 4, BigDecimal.ROUND_HALF_UP));
            } else {
                // 环比增幅为0
                data.setChainIncreaseRatioComparedLastMonth(BigDecimal.ZERO);
            }
        }
        Date lastYearSameDate = DateUtils.addYear(queryDate, -1);
        Date lastYearEndDate = DateUtils.getYearLast(DateUtils.getYear(lastYearSameDate));
        // 去年年底 流贷余额
        BigDecimal lastYearEndLoanBalanceMoney = getFlowLoanRiskBal(childrenCorpIds, curId, lastYearEndDate);
        // 去年年底 票据票面余额
        BigDecimal lastYearEndBillBalanceMoney = getBillRiskBal(childrenCorpIds, curId, lastYearEndDate);
        // 去年年底 债券票面余额
        BigDecimal lastYearEndDirectFinancingBalanceMoney = getBondRiskBal(childrenCorpIds, curId, lastYearEndDate);
        // 去年年底 融资合计数
        BigDecimal lastYearEndFinancingMoneySum = BigDecimal.ZERO;
        lastYearEndFinancingMoneySum = lastYearEndFinancingMoneySum.add(lastYearEndLoanBalanceMoney);
        lastYearEndFinancingMoneySum = lastYearEndFinancingMoneySum.add(lastYearEndBillBalanceMoney);
        lastYearEndFinancingMoneySum = lastYearEndFinancingMoneySum.add(lastYearEndDirectFinancingBalanceMoney);
        data.setLastYearEndFinancingMoneySum(lastYearEndFinancingMoneySum);
        // 较去年底增幅
        BigDecimal increaseComparedLastYearEnd = thisYearFinancingMoneySum.subtract(lastYearEndFinancingMoneySum);
        data.setIncreaseComparedLastYearEnd(toTenThousandYuan(increaseComparedLastYearEnd));
        // 较去年底增长比例 = 较去年底增幅 / 去年年底总额
        if (lastYearEndFinancingMoneySum.compareTo(BigDecimal.ZERO)>0) {
//        if (!BigDecimal.ZERO.equals(lastYearEndFinancingMoneySum)) {
            if (increaseComparedLastYearEnd.compareTo(BigDecimal.ZERO)>0) {
//            if (!BigDecimal.ZERO.equals(increaseComparedLastYearEnd)) {
                data.setIncreaseRatioComparedLastYearEnd(increaseComparedLastYearEnd.divide(lastYearEndFinancingMoneySum, 4, BigDecimal.ROUND_HALF_UP));
            } else {
                data.setIncreaseRatioComparedLastYearEnd(BigDecimal.ZERO);
            }
        }
    }
    /**
     * 借款利息支出
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 借款利息支出
     */
    private BigDecimal getLoanInterest(List<String> childrenCorpIds,
                                       String curId,
                                       Date queryDate) {
        List<FinanceLoanDTO> financeLoanDTOList = provideLoanQueryService.queryInterestFinance(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            BigDecimal loanInterestSum = BigDecimal.ZERO;
            for (FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getMoney() != null) {
                    loanInterestSum = loanInterestSum.add(financeLoanDTO.getMoney());
                }
            }
            return loanInterestSum;
        }
        return BigDecimal.ZERO;
    }
    /**
     * 票据利息支出
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 票据利息支出
     */
    private BigDecimal getBillInterest(List<String> childrenCorpIds,
                                       String curId,
                                       Date queryDate) {
        List<com.bytter.bill.api.bill.dto.FinanceLoanDTO> financeLoanDTOList = acceBillBalanceQueryService.queryInterestFinance(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            BigDecimal billInterestSum = BigDecimal.ZERO;
            for (com.bytter.bill.api.bill.dto.FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getMoney() != null) {
                    billInterestSum = billInterestSum.add(financeLoanDTO.getMoney());
                }
            }
            return billInterestSum;
        }
        return BigDecimal.ZERO;
    }
    /**
     * 直接融资利息支出
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 直接融资利息支出
     */
    private BigDecimal getDateDirectFinancingInterest(List<String> childrenCorpIds,
                                                      String curId,
                                                      Date queryDate) {
        List<FinanceLoanDTO> financeLoanDTOList = provideLoanQueryService.queryInlInterest(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            BigDecimal dateDirectFinancingInterestSum = BigDecimal.ZERO;
            for (FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getMoney() != null) {
                    dateDirectFinancingInterestSum = dateDirectFinancingInterestSum.add(financeLoanDTO.getMoney());
                }
            }
            return dateDirectFinancingInterestSum;
        }
        return BigDecimal.ZERO;
    }
    /**
     * 获取成员单位ID列表
     *
     * @param corpDTOList 成员单位
     * @return 成员单位ID列表
     */
    private List<String> getCorpIds(List<CorpDTO> corpDTOList) {
        List<String> corpIds = new ArrayList<>(corpDTOList.size() + 1);
        for (CorpDTO corpDTO : corpDTOList) {
            corpIds.add(corpDTO.getId());
        }
        return corpIds;
    }
    /**
     * 手动分页 成员单位列表
     *
     * @param levelCorpList 单位列表
     * @param pageNumber    页码
     * @param pageSize      一页有多少条数据
     * @return 成员单位列表
     */
    private List<CorpDTO> getPageCorpList(List<CorpDTO> levelCorpList, int pageNumber, int pageSize) {
        int initialCapacity = pageSize;
        if (pageSize > levelCorpList.size()) {
            initialCapacity = levelCorpList.size();
        }
        List<CorpDTO> corpDTOList = new ArrayList<>(initialCapacity);
        int startIndex = (pageNumber - 1) * pageSize;
        if (startIndex >= levelCorpList.size()) {
            return corpDTOList;
        }
        for (int dataCount = 0, size = levelCorpList.size(); ; ) {
            corpDTOList.add(levelCorpList.get(startIndex));
            dataCount++;
            startIndex++;
            if (startIndex >= size || dataCount == pageSize) {
                break;
            }
        }
        return corpDTOList;
    }
    /**
     * 转为万元
     *
     * @param money 金额
     * @return 万元 金额
     */
    public BigDecimal toTenThousandYuan(BigDecimal money) {
        if (money != null) {
            return money.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
}
