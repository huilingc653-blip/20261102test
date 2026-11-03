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
import com.bytter.report.domain.cms.dao.FinancingRiskExposureBalanceDao;
import com.bytter.report.domain.cms.vo.FinancingRiskExposureBalanceVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
/**
 * <br>
 * <b>功能：</b> 融资风险敞口余额汇总表  Service <br>
 * <b>作者：</b>chenlw<br>
 * <b>日期：</b> 2020/12/8 <br>
 */
@Service("financingRiskExposureBalanceService")
public class FinancingRiskExposureBalanceService extends BaseService {
    @Autowired
    private FinancingRiskExposureBalanceDao financingRiskExposureBalanceDao;
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
        Date queryDate;
        if (queryDateObj == null) {
            // 默认取当前日期
            queryDate = DateUtils.getDate();
        } else {
            queryDate = DateUtils.parse(queryDateObj.toString(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
        }
        //long startTime = System.currentTimeMillis();
        // 获取二级成员单位
        List<CorpDTO> levelCorpList = corpService.listLevelCorpAndChildrenCorp(ResourceConstant.CMS_FINANCING_RISK_EXPOSURE_BALANCE_VIEW, user.getId(), 2);
        // 手动分页获取二级成员单位数据
        List<CorpDTO> dataCorpList = getPageCorpList(levelCorpList, input.getPageNumber(), input.getPageSize());
        List<FinancingRiskExposureBalanceVO> financingRiskExposureBalanceVOList = new ArrayList<>();
        // 取业务数据
        //long getBusinessTime = System.currentTimeMillis();
        for (CorpDTO corpDTO : dataCorpList) {
            FinancingRiskExposureBalanceVO data = new FinancingRiskExposureBalanceVO();
            List<String> childrenCorpIds = getCorpIds(corpDTO.getChildrenCorp());
            childrenCorpIds.add(corpDTO.getId());
            data.setCorpId(corpDTO.getId());
            data.setCorpName(corpDTO.getName());
            // 流贷风险敞口余额 = 银行借款模块中的借款余额 - 保证金金额
            BigDecimal flowLeanRiskExpBal = getFlowLoanRiskExpBal(childrenCorpIds, curId, queryDate);
            data.setFlowLoanRiskExpBal(flowLeanRiskExpBal);
            // 票据风险敞口余额 = 开出应付票据的余额 - 保证金金额
            BigDecimal billRiskExpBal = getBillRiskExpBal(childrenCorpIds, curId, queryDate);
            data.setBillRiskExpBal(billRiskExpBal);
            // 债券风险敞口余额 = 直接融资的余额
            BigDecimal bondRiskExpBal = getBondRiskExpBal(childrenCorpIds, curId, queryDate);
            data.setBondRiskExpBal(bondRiskExpBal);
            // 风险敞口合计 = 流贷风险敞口余额 + 票据票面风险敞口余额 + 债券风险敞口余额
            BigDecimal riskExpSum = BigDecimal.ZERO;
            riskExpSum = riskExpSum.add(flowLeanRiskExpBal);
            riskExpSum = riskExpSum.add(billRiskExpBal);
            riskExpSum = riskExpSum.add(bondRiskExpBal);
            data.setRiskExpSum(riskExpSum);
            // 利息支出合计 = 借款利息支出 + 票据利息支出 + 直接融资利息支出
            BigDecimal interestExpenseSum = BigDecimal.ZERO;
            interestExpenseSum = interestExpenseSum.add(getLoanInterestExpense(childrenCorpIds, curId, queryDate));
            interestExpenseSum = interestExpenseSum.add(getBillInterestExpense(childrenCorpIds, curId, queryDate));
            interestExpenseSum = interestExpenseSum.add(getDateDirectFinancingInterestExpense(childrenCorpIds, curId, queryDate));
            // 利息净支出合计=上面三项业务的利息支出合计-对应保证金利息收入    目前暂不考虑保证金利息收入！！
            data.setNetInterestExpenseSum(interestExpenseSum);
            // 环比增幅  较去年底增幅
            assembleIncrease(data, childrenCorpIds, curId, queryDate);
            // 转化为万元
            data.setFlowLoanRiskExpBal(toTenThousandYuan(data.getFlowLoanRiskExpBal()));
            data.setBillRiskExpBal(toTenThousandYuan(data.getBillRiskExpBal()));
            data.setBondRiskExpBal(toTenThousandYuan(data.getBondRiskExpBal()));
            data.setRiskExpSum(toTenThousandYuan(data.getRiskExpSum()));
            data.setNetInterestExpenseSum(toTenThousandYuan(data.getNetInterestExpenseSum()));
            financingRiskExposureBalanceVOList.add(data);
        }
        //System.out.println("====取业务数据耗时：" + (System.currentTimeMillis() - getBusinessTime) + "ms");
        //System.out.println("====总计耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        DataTablesOutput output = new DataTablesOutput();
        output.setData(financingRiskExposureBalanceVOList);
        output.setDraw(input.getDraw());
        output.setRecordsTotal(levelCorpList.size());
        output.setRecordsFiltered(levelCorpList.size());
        return output;
    }
    /**
     * 流贷风险敞口余额 = 银行借款模块中的借款余额 - 保证金金额
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 流贷风险敞口余额
     */
    private BigDecimal getFlowLoanRiskExpBal(List<String> childrenCorpIds,
                                             String curId,
                                             Date queryDate) {
        List<FinanceLoanDTO> financeLoanDTOList = provideLoanQueryService.queryProvideFinance(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            // 借款余额
            BigDecimal loanBalanceMoneySum = BigDecimal.ZERO;
            // 保证金金额
            BigDecimal bondMoneySum = BigDecimal.ZERO;
            for (FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getMoney() != null) {
                    loanBalanceMoneySum = loanBalanceMoneySum.add(financeLoanDTO.getMoney());
                }
                if (financeLoanDTO.getBondMoney() != null) {
                    bondMoneySum = bondMoneySum.add(financeLoanDTO.getBondMoney());
                }
            }
            return loanBalanceMoneySum.subtract(bondMoneySum);
        }
        return BigDecimal.ZERO;
    }
    /**
     * 票据风险敞口余额 = 开出应付票据的余额 - 保证金金额
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 票据风险敞口余额
     */
    private BigDecimal getBillRiskExpBal(List<String> childrenCorpIds,
                                         String curId,
                                         Date queryDate) {
        List<com.bytter.bill.api.bill.dto.FinanceLoanDTO> financeLoanDTOList = acceBillBalanceQueryService.queryExposureFinance(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            BigDecimal billBalanceMoneySum = BigDecimal.ZERO;
            for (com.bytter.bill.api.bill.dto.FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getOpenMoney() != null) {
                    billBalanceMoneySum = billBalanceMoneySum.add(financeLoanDTO.getOpenMoney());
                }
            }
            return billBalanceMoneySum;
        }
        return BigDecimal.ZERO;
//        List<FinanceLoanDTO> financeLoanDTOList = acceBillBalanceQueryService.queryProvideFinance(childrenCorpIds, curId, queryDate);
//        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
//            BigDecimal billBalanceMoneySum = BigDecimal.ZERO;
//            // 保证金金额
//            BigDecimal bondMoneySum = BigDecimal.ZERO;
//            for (FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
//                if (financeLoanDTO.getMoney() != null) {
//                    billBalanceMoneySum = billBalanceMoneySum.add(financeLoanDTO.getMoney());
//                }
//                if (financeLoanDTO.getBondMoney() != null) {
//                    bondMoneySum = bondMoneySum.add(financeLoanDTO.getBondMoney());
//                }
//            }
//            data.setBillBalanceMoney(billBalanceMoneySum);
//            return billBalanceMoneySum.subtract(bondMoneySum);
//        }
//        data.setBillBalanceMoney(BigDecimal.ZERO);
//        return BigDecimal.ZERO;
    }
    /**
     * 开出应付票据的余额
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 开出应付票据的余额
     */
    private BigDecimal getBillBalanceMoney(List<String> childrenCorpIds,
                                           String curId,
                                           Date queryDate) {
        List<com.bytter.bill.api.bill.dto.FinanceLoanDTO> financeLoanDTOList = acceBillBalanceQueryService.queryProvideFinance(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            // 开出应付票据的余额
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
     * 债券风险敞口余额 = 直接融资的余额
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 债券风险敞口余额
     */
    private BigDecimal getBondRiskExpBal(List<String> childrenCorpIds,
                                         String curId,
                                         Date queryDate) {
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
     * - 环比增幅  融资合计数（敞口）与去年同期相比的增幅
     * - 较去年底增幅  融资合计数（敞口）与去年底相比的增幅
     * - 融资合计（敞口） = 流贷余额（敞口）+票据票面余额（敞口）+债券票面余额（敞口）
     *
     * @param data
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     */
    private void assembleIncrease(FinancingRiskExposureBalanceVO data,
                                  List<String> childrenCorpIds,
                                  String curId,
                                  Date queryDate) {
        // 今年融资合计数（敞口）  融资合计数 风险敞口合计 = 流贷风险敞口余额 + 票据票面风险敞口余额 + 债券风险敞口余额
        BigDecimal thisYearFinancingOpenMoneySum = data.getRiskExpSum();
        BigDecimal thisYearFinancingOpenMoneySumWanYuan = toTenThousandYuan(thisYearFinancingOpenMoneySum);
        data.setThisYearFinancingOpenMoneySum(thisYearFinancingOpenMoneySumWanYuan);
        Date lastMonthDate = DateUtils.addMonth(queryDate, -1);
        // 上个月 流贷风险敞口余额
        BigDecimal lastMonthDateLoanBalanceOpenMoney = getFlowLoanRiskExpBal(childrenCorpIds, curId, lastMonthDate);
        // 上个月 票据风险敞口余额
        BigDecimal lastMonthDateBillBalanceOpenMoney = getBillRiskExpBal(childrenCorpIds, curId, lastMonthDate);
        // 上个月 债券风险敞口余额
        BigDecimal lastMonthDateDirectFinancingBalanceOpenMoney = getBondRiskExpBal(childrenCorpIds, curId, lastMonthDate);
        // 上个月融资合计数（敞口）   融资合计数 风险敞口合计 = 流贷风险敞口余额 + 票据票面风险敞口余额 + 债券风险敞口余额
        BigDecimal lastMonthDateFinancingOpenMoneySum = BigDecimal.ZERO;
        lastMonthDateFinancingOpenMoneySum = lastMonthDateFinancingOpenMoneySum.add(lastMonthDateLoanBalanceOpenMoney);
        lastMonthDateFinancingOpenMoneySum = lastMonthDateFinancingOpenMoneySum.add(lastMonthDateBillBalanceOpenMoney);
        lastMonthDateFinancingOpenMoneySum = lastMonthDateFinancingOpenMoneySum.add(lastMonthDateDirectFinancingBalanceOpenMoney);
        // 上个月融资合计数（敞口）- 万元
        BigDecimal lastMonthDateFinancingOpenMoneySumWanYuan = toTenThousandYuan(lastMonthDateFinancingOpenMoneySum);
        data.setLastMonthDateFinancingOpenMoneySum(lastMonthDateFinancingOpenMoneySumWanYuan);
        // 融资合计数（敞口）  环比增幅 = 当月总额 - 上个月总额
        BigDecimal chainIncreaseWanYuan = thisYearFinancingOpenMoneySumWanYuan.subtract(lastMonthDateFinancingOpenMoneySumWanYuan);
        data.setChainIncrease(chainIncreaseWanYuan);
        // 环比增长比例 = 环比增幅 / 上个月总额
        if (lastMonthDateFinancingOpenMoneySum.compareTo(BigDecimal.ZERO)>0) {
//        if (!BigDecimal.ZERO.equals(lastMonthDateFinancingOpenMoneySum)) {
            if (!BigDecimal.ZERO.equals(chainIncreaseWanYuan)) {
                data.setChainIncreaseRatioComparedLastMonth(chainIncreaseWanYuan.divide(lastMonthDateFinancingOpenMoneySumWanYuan.abs(), 5, BigDecimal.ROUND_HALF_UP));
            } else {
                // 环比增幅为0
                data.setChainIncreaseRatioComparedLastMonth(BigDecimal.ZERO);
            }
        }
        Date lastYearSameDate = DateUtils.addYear(queryDate, -1);
        Date lastYearEndDate = DateUtils.getYearLast(DateUtils.getYear(lastYearSameDate));
        // 去年年底 流贷风险敞口余额
        BigDecimal lastYearEndLoanBalanceOpenMoney = getFlowLoanRiskExpBal(childrenCorpIds, curId, lastYearEndDate);
        // 去年年底 票据风险敞口余额
        BigDecimal lastYearEndBillBalanceOpenMoney = getBillBalanceMoney(childrenCorpIds, curId, lastYearEndDate);
        // 去年年底 债券风险敞口余额
        BigDecimal lastYearEndDirectFinancingBalanceOpenMoney = getBondRiskExpBal(childrenCorpIds, curId, lastYearEndDate);
        // 去年年底 融资合计数（敞口）
        BigDecimal lastYearEndFinancingOpenMoneySum = BigDecimal.ZERO;
        lastYearEndFinancingOpenMoneySum = lastYearEndFinancingOpenMoneySum.add(lastYearEndLoanBalanceOpenMoney);
        lastYearEndFinancingOpenMoneySum = lastYearEndFinancingOpenMoneySum.add(lastYearEndBillBalanceOpenMoney);
        lastYearEndFinancingOpenMoneySum = lastYearEndFinancingOpenMoneySum.add(lastYearEndDirectFinancingBalanceOpenMoney);
        // 去年年底 融资合计数（敞口）- 万元
        BigDecimal lastYearEndFinancingOpenMoneySumWanYuan = toTenThousandYuan(lastYearEndFinancingOpenMoneySum);
        data.setLastYearEndFinancingOpenMoneySum(lastYearEndFinancingOpenMoneySumWanYuan);
        // 较去年底增幅 先转化为万元再计算
        BigDecimal increaseComparedLastYearEndWanYuan = thisYearFinancingOpenMoneySumWanYuan.subtract(lastYearEndFinancingOpenMoneySumWanYuan);
        data.setIncreaseComparedLastYearEnd(increaseComparedLastYearEndWanYuan);
        // 较去年底增长比例 = 较去年底增幅 / 去年年底总额
        if (lastYearEndFinancingOpenMoneySum.compareTo(BigDecimal.ZERO)>0) {
            if (increaseComparedLastYearEndWanYuan.compareTo(BigDecimal.ZERO)>0) {
//        if (!BigDecimal.ZERO.equals(lastYearEndFinancingOpenMoneySum)) {
//            if (!BigDecimal.ZERO.equals(increaseComparedLastYearEndWanYuan)) {
                data.setIncreaseRatioComparedLastYearEnd(increaseComparedLastYearEndWanYuan.divide(lastYearEndFinancingOpenMoneySumWanYuan, 5, BigDecimal.ROUND_HALF_UP));
            } else {
                data.setIncreaseRatioComparedLastYearEnd(BigDecimal.ZERO);
            }
        }
    }
//    /**
//     * 环比增幅   较去年底增幅
//     * - 环比增幅  融资合计数（敞口）与去年同期相比的增幅
//     * - 较去年底增幅  融资合计数（敞口）与去年底相比的增幅
//     * - 融资合计（敞口） = 流贷余额（敞口）+票据票面余额（敞口）+债券票面余额（敞口）
//     *
//     * @param data
//     * @param childrenCorpIds
//     * @param curId
//     * @param queryDate
//     */
//    private void assembleIncrease(FinancingRiskExposureBalanceVO data,
//                                  List<String> childrenCorpIds,
//                                  String curId,
//                                  Date queryDate) {
//        // 今年融资合计数（敞口）  融资合计数 风险敞口合计 = 流贷风险敞口余额 + 票据票面风险敞口余额 + 债券风险敞口余额
//        BigDecimal thisYearFinancingOpenMoneySum = data.getRiskExpSum();
//        data.setThisYearFinancingOpenMoneySum(thisYearFinancingOpenMoneySum);
//
//        Date lastYearSameDate = DateUtils.addYear(queryDate, -1);
//        // 去年同期 流贷风险敞口余额
//        BigDecimal lastYearSameDateLoanBalanceOpenMoney = getFlowLoanRiskExpBal(childrenCorpIds, curId, lastYearSameDate);
//        // 去年同期 票据风险敞口余额
//        BigDecimal lastYearSameDateBillBalanceOpenMoney = getBillRiskExpBal(childrenCorpIds, curId, lastYearSameDate);
//        // 去年同期 债券风险敞口余额
//        BigDecimal lastYearSameDateDirectFinancingBalanceOpenMoney = getBondRiskExpBal(childrenCorpIds, curId, lastYearSameDate);
//        // 去年同期融资合计数（敞口）   融资合计数 风险敞口合计 = 流贷风险敞口余额 + 票据票面风险敞口余额 + 债券风险敞口余额
//        BigDecimal lastYearSameDateFinancingOpenMoneySum = BigDecimal.ZERO;
//        lastYearSameDateFinancingOpenMoneySum = lastYearSameDateFinancingOpenMoneySum.add(lastYearSameDateLoanBalanceOpenMoney);
//        lastYearSameDateFinancingOpenMoneySum = lastYearSameDateFinancingOpenMoneySum.add(lastYearSameDateBillBalanceOpenMoney);
//        lastYearSameDateFinancingOpenMoneySum = lastYearSameDateFinancingOpenMoneySum.add(lastYearSameDateDirectFinancingBalanceOpenMoney);
//        data.setLastYearSameDateFinancingOpenMoneySum(lastYearSameDateFinancingOpenMoneySum);
//
//        // 融资合计数（敞口）  环比增幅 = 今年总额 - 去年同期总额
//        BigDecimal chainIncrease = thisYearFinancingOpenMoneySum.subtract(lastYearSameDateFinancingOpenMoneySum);
//        data.setChainIncrease(toTenThousandYuan(chainIncrease));
//        // 环比增长比例 = 环比增幅 / 去年同期总额
//        if (!BigDecimal.ZERO.equals(lastYearSameDateFinancingOpenMoneySum)) {
//            if (!BigDecimal.ZERO.equals(chainIncrease)) {
//                data.setChainIncreaseRatioComparedLastYear(chainIncrease.divide(lastYearSameDateFinancingOpenMoneySum, 4, BigDecimal.ROUND_HALF_UP));
//            } else {
//                // 环比增幅为0
//                data.setChainIncreaseRatioComparedLastYear(BigDecimal.ZERO);
//            }
//        }
//
//        Date lastYearEndDate = DateUtils.getYearLast(DateUtils.getYear(lastYearSameDate));
//        // 去年年底 流贷风险敞口余额
//        BigDecimal lastYearEndLoanBalanceOpenMoney = getFlowLoanRiskExpBal(childrenCorpIds, curId, lastYearEndDate);
//        // 去年年底 票据风险敞口余额
//        BigDecimal lastYearEndBillBalanceOpenMoney = getBillBalanceMoney(childrenCorpIds, curId, lastYearEndDate);
//        // 去年年底 债券风险敞口余额
//        BigDecimal lastYearEndDirectFinancingBalanceOpenMoney = getBondRiskExpBal(childrenCorpIds, curId, lastYearEndDate);
//        // 去年年底 融资合计数（敞口）
//        BigDecimal lastYearEndFinancingOpenMoneySum = BigDecimal.ZERO;
//        lastYearEndFinancingOpenMoneySum = lastYearEndFinancingOpenMoneySum.add(lastYearEndLoanBalanceOpenMoney);
//        lastYearEndFinancingOpenMoneySum = lastYearEndFinancingOpenMoneySum.add(lastYearEndBillBalanceOpenMoney);
//        lastYearEndFinancingOpenMoneySum = lastYearEndFinancingOpenMoneySum.add(lastYearEndDirectFinancingBalanceOpenMoney);
//        data.setLastYearEndFinancingOpenMoneySum(lastYearEndFinancingOpenMoneySum);
//
//        // 较去年底增幅
//        BigDecimal increaseComparedLastYearEnd = thisYearFinancingOpenMoneySum.subtract(lastYearEndFinancingOpenMoneySum);
//        data.setIncreaseComparedLastYearEnd(toTenThousandYuan(increaseComparedLastYearEnd));
//        // 较去年底增长比例 = 较去年底增幅 / 去年年底总额
//        if (!BigDecimal.ZERO.equals(lastYearEndFinancingOpenMoneySum)) {
//            if (!BigDecimal.ZERO.equals(increaseComparedLastYearEnd)) {
//                data.setIncreaseRatioComparedLastYearEnd(increaseComparedLastYearEnd.divide(lastYearEndFinancingOpenMoneySum, 4, BigDecimal.ROUND_HALF_UP));
//            } else {
//                data.setIncreaseRatioComparedLastYearEnd(BigDecimal.ZERO);
//            }
//        }
//    }
    /**
     * 借款利息支出
     *
     * @param childrenCorpIds
     * @param curId
     * @param queryDate
     * @return 借款利息支出
     */
    private BigDecimal getLoanInterestExpense(List<String> childrenCorpIds,
                                              String curId,
                                              Date queryDate) {
        List<FinanceLoanDTO> financeLoanDTOList = provideLoanQueryService.queryInterestFinance(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            BigDecimal loanInterestExpenseSum = BigDecimal.ZERO;
            for (FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getMoney() != null) {
                    loanInterestExpenseSum = loanInterestExpenseSum.add(financeLoanDTO.getMoney());
                }
            }
            return loanInterestExpenseSum;
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
    private BigDecimal getBillInterestExpense(List<String> childrenCorpIds,
                                              String curId,
                                              Date queryDate) {
        List<com.bytter.bill.api.bill.dto.FinanceLoanDTO> financeLoanDTOList = acceBillBalanceQueryService.queryInterestFinance(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            BigDecimal billInterestExpenseSum = BigDecimal.ZERO;
            for (com.bytter.bill.api.bill.dto.FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getMoney() != null) {
                    billInterestExpenseSum = billInterestExpenseSum.add(financeLoanDTO.getMoney());
                }
            }
            return billInterestExpenseSum;
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
    private BigDecimal getDateDirectFinancingInterestExpense(List<String> childrenCorpIds,
                                                             String curId,
                                                             Date queryDate) {
        List<FinanceLoanDTO> financeLoanDTOList = provideLoanQueryService.queryInlInterest(childrenCorpIds, curId, queryDate);
        if (!CollectionUtils.isEmpty(financeLoanDTOList)) {
            BigDecimal dateDirectFinancingInterestExpenseSum = BigDecimal.ZERO;
            for (FinanceLoanDTO financeLoanDTO : financeLoanDTOList) {
                if (financeLoanDTO.getMoney() != null) {
                    dateDirectFinancingInterestExpenseSum = dateDirectFinancingInterestExpenseSum.add(financeLoanDTO.getMoney());
                }
            }
            return dateDirectFinancingInterestExpenseSum;
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
        return financingRiskExposureBalanceDao;
    }
}
