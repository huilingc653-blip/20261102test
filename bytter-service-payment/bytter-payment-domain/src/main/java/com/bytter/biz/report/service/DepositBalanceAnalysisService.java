package com.bytter.biz.report.service;
import com.bytter.biz.report.dao.DepositBalanceAnalysisDao;
import com.bytter.biz.report.vo.DepositBalanceAnalysisVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
/**
 * 分析系统当前存款余额与上月的差异、与年初的差异。
 *
 * @author qinl
 * @description 存款余额对比分析service
 * @date 2020.02.12
 */
@Service("depositBalanceAnalysisService")
@Transactional
public class DepositBalanceAnalysisService extends BaseService<DepositBalanceAnalysisVO, String> {
    @Autowired
    private DepositBalanceAnalysisDao depositBalanceAnalysisDao;
    @DubboReference(check = false)
	private IFcExcrateAdjustService iFcExcrateAdjustService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Override
    public BaseDao<DepositBalanceAnalysisVO, String> getEntityDao() {
        return depositBalanceAnalysisDao;
    }
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    /**
     * 统计报表入口，条件组装
     *
     * @param input 传参
     * @return 界面显示数据
     */
    public DataTablesOutput<DepositBalanceAnalysisVO> conditionsEntrance(DataTablesInput input) {
        //性能优化，首次进入报表页面不加载列表
        if (input.getDraw() == 1) return new DataTablesOutput<>();
        //传参判断项
        if (this.checkConditions(input)) return new DataTablesOutput<>();
        String statistical = (String) input.getFormdata().get("statistical");//维度统计参数
        String depositType = (String) input.getFormdata().get("depositType");//内部/银行存款参数
        String type = statistical.concat(depositType);
        DataTablesOutput<DepositBalanceAnalysisVO> output = new DataTablesOutput<>();
        //根据查询条件设置对应的标准时间yyyy-MM-dd
        this.setDate(input);
        //维度统计
        switch (type) {
            case "00"://按单位统计全部
                injectBalance(input, output, true, null);//注入余额
                injectContrastBalance(input, output, true, null);//注入对比余额
                break;
            case "01"://按单位银行存款
                injectBalance(input, output, true, true);
                injectContrastBalance(input, output, true, true);
                break;
            case "02"://按单位内部存款
                injectBalance(input, output, true, false);
                injectContrastBalance(input, output, true, false);
                break;
            case "10"://按银行类型统计全部
                injectBalance(input, output, false, null);
                injectContrastBalance(input, output, false, null);
                break;
            case "11"://按银行类型统计银行存款
                injectBalance(input, output, false, true);
                injectContrastBalance(input, output, false, true);
                break;
            case "12"://按银行类型统计内部存款
                injectBalance(input, output, false, false);
                injectContrastBalance(input, output, false, false);
                break;
        }
        //更新金额-折合本位币和金额单位
        this.moneyFilter(input, output);
        //分页过滤器
        this.pageFilter(input, output);
        return output;
    }
    /**
     * 注入对比余额信息
     *
     * @param input      数据列
     * @param output     注入后的数据列
     * @param isCorp     是否单位统计维度
     * @param isBankType 是否银行类型
     */
    public void injectContrastBalance(DataTablesInput input, DataTablesOutput<DepositBalanceAnalysisVO> output, boolean isCorp, Boolean isBankType) {
        String date = (String) input.getFormdata().get("date");
        Date parseDate = DateUtils.parse(date, "yyyy-MM-dd");
        String[] contrastCycle = ((String)input.getFormdata().get("contrastCycle")).split(",");
        //获取当前时间余额
        List<DepositBalanceAnalysisVO> data = output.getData();
        for (String s : contrastCycle) {
            switch (s) {
                case "1"://获取上个月
                    Date dateNextMonth = DateUtils.addMonth(parseDate, -1);
                    Date lastMonthDate = DateUtils.getLastMonthDate(dateNextMonth);
                    List<DepositBalanceAnalysisVO> listBalance1 = this.getListBalance(input, lastMonthDate, isCorp, isBankType);
                    this.injectContrastBlanceInfo(data, listBalance1, 1);
                    break;
                case "2"://获取上季度末
                    Date dateNext = DateUtils.addMonth(parseDate, -3);
                    Date lastQuarterDate = DateUtils.getLastQuarterDate(dateNext);
                    List<DepositBalanceAnalysisVO> listBalance2 = this.getListBalance(input, lastQuarterDate, isCorp, isBankType);
                    this.injectContrastBlanceInfo(data, listBalance2, 2);
                    break;
                case "3"://获取3个月前
                    Date dateThreeMonth = DateUtils.addMonth(parseDate, -3);
                    List<DepositBalanceAnalysisVO> listBalance3 = this.getListBalance(input, dateThreeMonth, isCorp, isBankType);
                    this.injectContrastBlanceInfo(data, listBalance3, 3);
                    break;
                case "4"://较上年同期
                    Date dateNextYear = DateUtils.addMonth(parseDate, -12);
                    List<DepositBalanceAnalysisVO> listBalance4 = this.getListBalance(input, dateNextYear, isCorp, isBankType);
                    this.injectContrastBlanceInfo(data, listBalance4, 4);
                    break;
                case "5"://较年初
                    Date lastNextYear = DateUtils.addMonth(parseDate, -12);
                    Date firstYearDate = DateUtils.getLastYearDate(lastNextYear);
                    List<DepositBalanceAnalysisVO> listBalance5 = this.getListBalance(input, firstYearDate, isCorp, isBankType);
                    this.injectContrastBlanceInfo(data, listBalance5, 5);
            }
        }
    }
    /**
     * 数据列表注入已经查询完毕的月
     *
     * @param input      查询条件
     * @param output     数据列表
     * @param isCorp     是否单位统计维度
     * @param isBankType 是否银行类别 null-全部，true-银行，false-内部
     */
    public void injectBalance(DataTablesInput input, DataTablesOutput<DepositBalanceAnalysisVO> output, boolean isCorp, Boolean isBankType) {
        String curId = (String) input.getFormdata().get("curId");
        String corpId = (String) input.getFormdata().get("corpId");
        String searchDate = (String) input.getFormdata().get("date");
        String[] ids = StringUtils.split(corpId, ",");
        List<DepositBalanceAnalysisVO> calculate = null;
        if (isCorp) {//按单位维度统计
            if (isBankType == null) {//查询全部余额
                List<DepositBalanceAnalysisVO> listBank = depositBalanceAnalysisDao.selectCorpListForBank(searchDate, curId, ids);//获取所有银行账户余额
                List<DepositBalanceAnalysisVO> listInner = depositBalanceAnalysisDao.selectCorpListForInner(searchDate, curId, ids);//获取内部账户余额
                listBank.addAll(listInner);
                calculate = this.calculateForCorp(listBank,input);
            } else if (isBankType) {//查询银行余额
                List<DepositBalanceAnalysisVO> list = depositBalanceAnalysisDao.selectCorpListForBank(searchDate, curId, ids);//获取所有银行账户余额
                calculate = this.calculateForCorp(list,input);//余额计算
            } else {//查询内部余额
                List<DepositBalanceAnalysisVO> list = depositBalanceAnalysisDao.selectCorpListForInner(searchDate, curId, ids);//获取内部账户余额
                calculate = this.calculateForCorp(list,input); //余额计算
            }
        } else {//按银行类型维度统计
            if (isBankType == null) {//查询全部余额
                List<DepositBalanceAnalysisVO> listBank = depositBalanceAnalysisDao.selectTypeListForBank(searchDate, curId, ids); //获取所有银行账户余额
                calculate = this.calculateForCorp(listBank,input);
                //获取内部账户余额
                List<DepositBalanceAnalysisVO> listInner = depositBalanceAnalysisDao.selectTypeListForInner(searchDate, curId,ids);
                List<DepositBalanceAnalysisVO> listInnerCalculate = this.calculateForBankType(listInner, ids,input);
                calculate.addAll(listInnerCalculate);//余额计算
            } else if (isBankType) {//查询银行余额
                List<DepositBalanceAnalysisVO> list = depositBalanceAnalysisDao.selectTypeListForBank(searchDate, curId, ids);//获取所有银行账户余额
                calculate = this.calculateForCorp(list,input);//余额计算
            } else {//查询内部余额
                List<DepositBalanceAnalysisVO> list = depositBalanceAnalysisDao.selectTypeListForInner(searchDate, curId,ids);//获取内部账户余额
                calculate = this.calculateForBankType(list, ids,input);//余额计算
            }
        }
        output.setData(calculate);
    }
    /**
     * 数据列表注入已经查询完毕的月
     *
     * @param input      查询条件
     * @param isCorp     是否单位统计维度
     * @param isBankType 是否银行类别 null-全部，true-银行，false-内部
     * @return 数据列
     */
    private List<DepositBalanceAnalysisVO> getListBalance(DataTablesInput input, Date date, boolean isCorp, Boolean isBankType) {
        String curId = (String) input.getFormdata().get("curId");
        String corpId = (String) input.getFormdata().get("corpId");
        String searchDate = DateUtils.format(date, "yyyy-MM-dd");
        String[] ids = corpId.split(",");
        List<DepositBalanceAnalysisVO> calculate = null;
        if (isCorp) {//按单位维度统计
            if (isBankType == null) {//查询全部余额
                List<DepositBalanceAnalysisVO> listBank = depositBalanceAnalysisDao.selectCorpListForBank(searchDate, curId, ids);//获取所有银行账户余额
                List<DepositBalanceAnalysisVO> listInner = depositBalanceAnalysisDao.selectCorpListForInner(searchDate, curId, ids);//获取内部账户余额
                listBank.addAll(listInner);
                calculate = this.calculateForCorp(listBank,input);
            } else if (isBankType) {//查询银行余额
                List<DepositBalanceAnalysisVO> list = depositBalanceAnalysisDao.selectCorpListForBank(searchDate, curId, ids);//获取所有银行账户余额
                calculate = this.calculateForCorp(list,input);//余额计算
            } else {//查询内部余额
                List<DepositBalanceAnalysisVO> list = depositBalanceAnalysisDao.selectCorpListForInner(searchDate, curId, ids);//获取内部账户余额
                calculate = this.calculateForCorp(list,input); //余额计算
            }
        } else {//按银行类型维度统计
            if (isBankType == null) {//查询全部余额
                List<DepositBalanceAnalysisVO> listBank = depositBalanceAnalysisDao.selectTypeListForBank(searchDate, curId, ids); //获取所有银行账户余额
                calculate = this.calculateForCorp(listBank,input);
                //获取内部账户余额
                List<DepositBalanceAnalysisVO> listInner = depositBalanceAnalysisDao.selectTypeListForInner(searchDate, curId,ids);
                List<DepositBalanceAnalysisVO> listInnerCalculate = this.calculateForBankType(listInner, ids,input);
                calculate.addAll(listInnerCalculate);//余额计算
            } else if (isBankType) {//查询银行余额
                List<DepositBalanceAnalysisVO> list = depositBalanceAnalysisDao.selectTypeListForBank(searchDate, curId, ids);//获取所有银行账户余额
                calculate = this.calculateForCorp(list,input);//余额计算
            } else {//查询内部余额
                List<DepositBalanceAnalysisVO> list = depositBalanceAnalysisDao.selectTypeListForInner(searchDate, curId,ids);//获取内部账户余额
                calculate = this.calculateForBankType(list, ids,input);//余额计算
            }
        }
        return calculate;
    }
    /**
     * 对比数据与余额数据进行合并
     *
     * @param data          待合并的数据
     * @param listBalance   合并数据
     * @param contrastCycle 比较类型
     */
    private void injectContrastBlanceInfo(List<DepositBalanceAnalysisVO> data, List<DepositBalanceAnalysisVO> listBalance, int contrastCycle) {
        if (contrastCycle == 1) {
            for (DepositBalanceAnalysisVO vo : data) {
                for (DepositBalanceAnalysisVO voNext : listBalance) {
                    if (vo.getCorpName().equals(voNext.getCorpName())) {
                        if(voNext.getBalance() == null){
                            voNext.setBalance(BigDecimal.ZERO);
                        }
                        if(vo.getBalance() == null){
                            vo.setBalance(BigDecimal.ZERO);
                        }
                        vo.setMonthBalance(voNext.getBalance());//设置当前时期余额
                        BigDecimal subtract = vo.getBalance().subtract(voNext.getBalance());//差额
                        vo.setMonthDifferenceBalance(subtract);//设置差额
                        if (vo.getBalance().intValue() != 0) {
                            BigDecimal subtract2 = subtract.divide(vo.getBalance(), 6, RoundingMode.HALF_UP);//比例
                            vo.setMonthProportion(subtract2);
                        } else {
                            vo.setMonthProportion(new BigDecimal("0"));
                        }
                    }
                }
            }
        } else if (contrastCycle == 2) {
            for (DepositBalanceAnalysisVO vo : data) {
                for (DepositBalanceAnalysisVO voNext : listBalance) {
                    if (vo.getCorpName().equals(voNext.getCorpName())) {
                        vo.setQuarterBalance(voNext.getBalance());//设置当前时期余额
                        BigDecimal subtract = vo.getBalance().subtract(voNext.getBalance());//差额
                        vo.setQuarterDifferenceBalance(subtract);//设置差额
                        if (vo.getBalance().intValue() != 0) {
                            BigDecimal subtract2 = subtract.divide(vo.getBalance(), 6, RoundingMode.HALF_UP);//比例
                            vo.setQuarterProportion(subtract2);
                        } else {
                            vo.setQuarterProportion(new BigDecimal("0"));
                        }
                    }
                }
            }
        } else if (contrastCycle == 3) {
            for (DepositBalanceAnalysisVO vo : data) {
                for (DepositBalanceAnalysisVO voNext : listBalance) {
                    if (vo.getCorpName().equals(voNext.getCorpName())) {
                        vo.setThreeMonthBalance(voNext.getBalance());//设置当前时期余额
                        BigDecimal subtract = vo.getBalance().subtract(voNext.getBalance());//差额
                        vo.setThreeMonthDifferenceBalance(subtract);//设置差额
                        if (vo.getBalance().intValue() != 0) {
                            BigDecimal subtract2 = subtract.divide(vo.getBalance(), 6, RoundingMode.HALF_UP);//比例
                            vo.setThreeMonthProportion(subtract2);
                        } else {
                            vo.setThreeMonthProportion(new BigDecimal("0"));
                        }
                    }
                }
            }
        } else if (contrastCycle == 5) {
            for (DepositBalanceAnalysisVO vo : data) {
                for (DepositBalanceAnalysisVO voNext : listBalance) {
                    if (vo.getCorpName().equals(voNext.getCorpName())) {
                        vo.setYearBalance(voNext.getBalance());//设置当前时期余额
                        BigDecimal subtract = vo.getBalance().subtract(voNext.getBalance());//差额
                        vo.setYearDifferenceBalance(subtract);//设置差额
                        if (vo.getBalance().intValue() != 0) {
                            BigDecimal subtract2 = subtract.divide(vo.getBalance(), 6, RoundingMode.HALF_UP);//比例
                            vo.setYearProportion(subtract2);
                        } else {
                            vo.setYearProportion(new BigDecimal("0"));
                        }
                    }
                }
            }
        } else if (contrastCycle == 4) {
            for (DepositBalanceAnalysisVO vo : data) {
                for (DepositBalanceAnalysisVO voNext : listBalance) {
                    if (vo.getCorpName().equals(voNext.getCorpName())) {
                        vo.setYearOverYearBalance(voNext.getBalance());//设置当前时期余额
                        BigDecimal subtract = vo.getBalance().subtract(voNext.getBalance());//差额
                        vo.setYearOverYearDifferenceBalance(subtract);//设置差额
                        if (vo.getBalance().intValue() != 0) {
                            BigDecimal subtract2 = subtract.divide(vo.getBalance(), 6, RoundingMode.HALF_UP);//比例
                            vo.setYearOverYearProportion(subtract2);
                        } else {
                            vo.setYearOverYearProportion(new BigDecimal("0"));
                        }
                    }
                }
            }
        }
    }
    /**
     * 金额单位过滤
     */
    public void moneyFilter(DataTablesInput input, DataTablesOutput<DepositBalanceAnalysisVO> output) {
        String moneyUnit = (String) input.getFormdata().get("moneyUnit");//金额单位
        //金额单位换算
        BigDecimal moneyType;
        switch (moneyUnit) {
            case "2":
                moneyType = new BigDecimal("1000");
                break;
            case "3":
                moneyType = new BigDecimal("10000");
                break;
            case "4":
                moneyType = new BigDecimal("100000000");
                break;
            default:
                moneyType = new BigDecimal("1");
                break;
        }
        for (DepositBalanceAnalysisVO vo : output.getData()) {
            if (vo.getBalance() != null) {
                BigDecimal divide = vo.getBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setBalance(divide);
            }
            if (vo.getMonthBalance() != null) {
                BigDecimal divide = vo.getMonthBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setMonthBalance(divide);
            }
            if (vo.getQuarterBalance() != null) {
                BigDecimal divide = vo.getQuarterBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setQuarterBalance(divide);
            }
            if (vo.getThreeMonthBalance() != null) {
                BigDecimal divide = vo.getThreeMonthBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setThreeMonthBalance(divide);
            }
            if (vo.getYearOverYearBalance() != null) {
                BigDecimal divide = vo.getYearOverYearBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setYearOverYearBalance(divide);
            }
            if (vo.getYearBalance() != null) {
                BigDecimal divide = vo.getYearBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setYearBalance(divide);
            }
            //设置差额
            if (vo.getMonthDifferenceBalance() != null) {
                BigDecimal divide = vo.getMonthDifferenceBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setMonthDifferenceBalance(divide);
            }
            if (vo.getQuarterDifferenceBalance() != null) {
                BigDecimal divide = vo.getQuarterDifferenceBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setQuarterDifferenceBalance(divide);
            }
            if (vo.getYearDifferenceBalance() != null) {
                BigDecimal divide = vo.getYearDifferenceBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setYearDifferenceBalance(divide);
            }
            if (vo.getThreeMonthDifferenceBalance() != null) {
                BigDecimal divide = vo.getThreeMonthDifferenceBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setThreeMonthDifferenceBalance(divide);
            }
            if (vo.getYearOverYearDifferenceBalance() != null) {
                BigDecimal divide = vo.getYearOverYearDifferenceBalance().divide(moneyType, 2, RoundingMode.HALF_UP);
                vo.setYearOverYearDifferenceBalance(divide);
            }
        }
    }
    /**
     * 币种折算过滤
     */
    public void moneyFilter(DataTablesInput input, List<DepositBalanceAnalysisVO> list) {
        String changeCur = (String) input.getFormdata().get("changeCur");//是否折合币种
        String reducedCurId = (String) input.getFormdata().get("reducedCurId");//折合币种
        String date = (String) input.getFormdata().get("date");//时间
        Map<String, BtCurrencyDTO> curMap = currencyService.getCurrency();
        for (DepositBalanceAnalysisVO vo : list) {
            BtCurrencyDTO btCurrencyDTO = curMap.get(vo.getCurId());
            vo.setCurName(btCurrencyDTO.getCurName());
            BigDecimal curExrate = new BigDecimal(1);
            if ("1".equals(changeCur)) {
                BigDecimal exchangeRateBy = iFcExcrateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), reducedCurId, DateUtils.parse(date,"yyyy-MM-dd"), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (exchangeRateBy != null) {
                    curExrate = exchangeRateBy;
                }
            }
            if (vo.getBalance() != null) {
                BigDecimal multiply = vo.getBalance().multiply(curExrate);
                vo.setBalance(multiply);
            }
            if (vo.getMonthBalance() != null) {
                BigDecimal multiply = vo.getMonthBalance().multiply(curExrate);
                vo.setMonthBalance(multiply);
            }
            if (vo.getQuarterBalance() != null) {
                BigDecimal multiply = vo.getQuarterBalance().multiply(curExrate);
                vo.setQuarterBalance(multiply);
            }
            if (vo.getThreeMonthBalance() != null) {
                BigDecimal multiply = vo.getThreeMonthBalance().multiply(curExrate);
                vo.setThreeMonthBalance(multiply);
            }
            if (vo.getYearOverYearBalance() != null) {
                BigDecimal multiply = vo.getYearOverYearBalance().multiply(curExrate);
                vo.setYearOverYearBalance(multiply);
            }
            if (vo.getYearBalance() != null) {
                BigDecimal multiply = vo.getYearBalance().multiply(curExrate);
                vo.setYearBalance(multiply);
            }
            //设置差额
            if (vo.getMonthDifferenceBalance() != null) {
                BigDecimal multiply = vo.getMonthDifferenceBalance().multiply(curExrate);
                vo.setMonthDifferenceBalance(multiply);
            }
            if (vo.getQuarterDifferenceBalance() != null) {
                BigDecimal multiply = vo.getQuarterDifferenceBalance().multiply(curExrate);
                vo.setQuarterDifferenceBalance(multiply);
            }
            if (vo.getYearDifferenceBalance() != null) {
                BigDecimal multiply = vo.getYearDifferenceBalance().multiply(curExrate);
                vo.setYearDifferenceBalance(multiply);
            }
            if (vo.getThreeMonthDifferenceBalance() != null) {
                BigDecimal multiply = vo.getThreeMonthDifferenceBalance().multiply(curExrate);
                vo.setThreeMonthDifferenceBalance(multiply);
            }
            if (vo.getYearOverYearDifferenceBalance() != null) {
                BigDecimal multiply = vo.getYearOverYearDifferenceBalance().multiply(curExrate);
                vo.setYearOverYearDifferenceBalance(multiply);
            }
        }
    }
    /**
     * 分页过滤器
     */
    public void pageFilter(DataTablesInput input, DataTablesOutput<DepositBalanceAnalysisVO> output) {
        List<DepositBalanceAnalysisVO> list = output.getData();
        if (EmptyUtils.isEmpty(list)) {
            output.setDraw(0);
            output.setRecordsTotal(0);
            output.setRecordsFiltered(0);
            output.setData(new ArrayList<DepositBalanceAnalysisVO>());
            return;
        }
        int totalNum = list.size();
        Integer pageNumber = input.getPageNumber();
        int page = (totalNum + 10 - 1) / 10;
        if (pageNumber == page) {
            output.setData(list.subList(input.getStart(), totalNum));
        } else {
            if (input.getLength() > totalNum) {
                output.setData(list.subList(input.getStart(), totalNum));
            } else {
                output.setData(list.subList(input.getStart(), input.getStart() + 10));
            }
        }
        output.setRecordsTotal(list.size());
        output.setRecordsFiltered(list.size());
    }
    /**
     * 时间控制项，控制时间的核心方法：将例如2020-1转换成2020-01-19当期，或曾经2019-12转2019-12-31
     *
     * @param input 带传入的时间参数
     */
    private void setDate(DataTablesInput input) {
        String date = (String) input.getFormdata().get("date");//获取参数时间
        String nowDate = DateUtils.getDate("yyyy-MM");//当期系统时间
        if (date.equals(nowDate)) {
            input.getFormdata().put("date", DateUtils.getDateStr(new Date(), "yyyy-MM-dd"));
        } else {
            Date parse = DateUtils.parse(date, "yyyy-MM");
            Date lastMonthDate = DateUtils.getLastMonthDate(parse);
            input.getFormdata().put("date", DateUtils.getDateStr(lastMonthDate, "yyyy-MM-dd"));
        }
    }
    /**
     * 条件检查项
     *
     * @param input 条件输入流
     * @return true or false
     */
    private boolean checkConditions(DataTablesInput input) {
        String corpId = (String) input.getFormdata().get("corpId");//单位
        String date = (String) input.getFormdata().get("date");//统计月份
        String depositType = (String) input.getFormdata().get("depositType");//内部/银行存款
//        String curId = (String) input.getFormdata().get("curId");//币别
        String changeCur = (String) input.getFormdata().get("changeCur");//是否折合本位币
        String statistical = (String) input.getFormdata().get("statistical");//维度统计
        String moneyUnit = (String) input.getFormdata().get("moneyUnit");//金额单位
        if (StringUtils.isEmpty(corpId)) return true;
        if (StringUtils.isEmpty(date)) return true;
        if (StringUtils.isEmpty(depositType)) return true;
//        if (StringUtils.isEmpty(curId)) return true;
        if (StringUtils.isEmpty(changeCur)) return true;
        if (StringUtils.isEmpty(statistical)) return true;
        if(CommonConstant.YES.equals(changeCur)){
            StringBuilder sb = new StringBuilder();
            List<BtCurrencyDTO> btCurrency = currencyService.getBtCurrency();
            for(BtCurrencyDTO dto:btCurrency){
                sb.append(dto.getId()).append(",");
            }
            String s = sb.toString();
            String substring = s.substring(0, s.length() - 2);
            input.getFormdata().put("curId",substring);
        }
        return StringUtils.isEmpty(moneyUnit);
    }
    /**
     * 计算规则，按照单位维度进行余额计算
     *
     * @param list 需要计算的数据列
     * @return 计算结果列表
     */
    public List<DepositBalanceAnalysisVO> calculateForCorp(List<DepositBalanceAnalysisVO> list,DataTablesInput input) {
        moneyFilter(input,list);
        Map<String, DepositBalanceAnalysisVO> map = new HashMap<>();//用于单位分类
        for (DepositBalanceAnalysisVO vo : list) {
            if (map.containsKey(vo.getCorpName())) {
                if (vo.getInnerId() != null) {
                    map.get(vo.getCorpName()).getInnerLinkShow().add(vo.getInnerId());//设置超链接
                }
                if (vo.getBankId() != null) {
                    map.get(vo.getCorpName()).getBankLinkShow().add(vo.getBankId());//设置超链接
                }
                BigDecimal bigDecimal = map.get(vo.getCorpName()).getBalance();
                if(bigDecimal == null){
                    bigDecimal = BigDecimal.ZERO;
                }
                if(vo.getBalance() == null){
                    vo.setBalance(BigDecimal.ZERO);
                }
                BigDecimal addAfter = bigDecimal.add(vo.getBalance());
                vo.setBalance(addAfter); //设置金额
                vo.setInnerLinkShow(map.get(vo.getCorpName()).getInnerLinkShow());
                vo.setBankLinkShow(map.get(vo.getCorpName()).getBankLinkShow());
                map.put(vo.getCorpName(), vo);
                continue;
            }
            if (vo.getInnerId() != null) {
                vo.getInnerLinkShow().add(vo.getInnerId());//设置超链接
            }
            if (vo.getBankId() != null) {
                vo.getBankLinkShow().add(vo.getBankId());//设置超链接
            }
            map.put(vo.getCorpName(), vo);
        }
        return insertData(map);
    }
    /**
     * 计算规则，按照银行类别维度进行余额计算
     *
     * @param list 需要计算的数据列
     * @param ids  需要过滤的单位
     * @return 计算结果列表
     */
    public List<DepositBalanceAnalysisVO> calculateForBankType(List<DepositBalanceAnalysisVO> list, String[] ids,DataTablesInput input) {
        moneyFilter(input,list);
        Map<String, DepositBalanceAnalysisVO> map = new HashMap<>();
        //用于单位查询条件，过滤单位类型
        Set<String> filter = new HashSet<>();
        for(String id:ids){
            CorpDTO netByCorpId = corpService.getNetByCorpId(id);
            filter.add(netByCorpId.getId());
        }
        for (DepositBalanceAnalysisVO vo : list) {
            //过滤单位
            if (filter.contains(vo.getNetId())) {
                if (map.containsKey(vo.getNetName())) {
                    if (vo.getInnerId() != null) {
                        map.get(vo.getNetName()).getInnerLinkShow().add(vo.getInnerId());//设置超链接
                    }
                    if (vo.getBankId() != null) {
                        map.get(vo.getNetName()).getBankLinkShow().add(vo.getBankId());//设置超链接
                    }
                    BigDecimal bigDecimal = map.get(vo.getNetName()).getBalance();
                    BigDecimal addAfter = bigDecimal.add(vo.getBalance());
                    //设置金额
                    vo.setBalance(addAfter);
                    vo.setInnerLinkShow(map.get(vo.getNetName()).getInnerLinkShow());
                    vo.setBankLinkShow(map.get(vo.getNetName()).getBankLinkShow());
                    map.put(vo.getNetName(), vo);
                    continue;
                }
                if (vo.getInnerId() != null) {
                    vo.getInnerLinkShow().add(vo.getInnerId());//设置超链接
                }
                if (vo.getBankId() != null) {
                    vo.getBankLinkShow().add(vo.getBankId());//设置超链接
                }
                map.put(vo.getNetName(), vo);
            }
        }
        return insertData(map);
    }
    /**
     * map转list
     *
     * @param map 待转化的数据
     * @return list
     */
    private List<DepositBalanceAnalysisVO> insertData(Map<String, DepositBalanceAnalysisVO> map) {
        List<DepositBalanceAnalysisVO> calculateList = new ArrayList<>();
        for (String key : map.keySet()) {
            DepositBalanceAnalysisVO vo = new DepositBalanceAnalysisVO();
            vo.setCorpName(key);
            vo.setBalance(map.get(key).getBalance());
            vo.setBankLinkShow(map.get(key).getBankLinkShow());
            vo.setInnerLinkShow(map.get(key).getInnerLinkShow());
            calculateList.add(vo);
        }
        return calculateList;
    }
    /**
     * 余额详细数据获取，查询出该余额的所有账号与具体余额
     *
     * @param bankList  银行账号列表
     * @param innerList 内部账号列表
     * @return 余额账号汇总
     */
    public List<DepositBalanceAnalysisVO> selectDetailsList(String[] bankList, String[] innerList) {
        List<DepositBalanceAnalysisVO> depositBalanceAnalysisVOS = depositBalanceAnalysisDao.selectDetailsForBank(bankList);
        List<DepositBalanceAnalysisVO> depositBalanceAnalysisVOS1 = depositBalanceAnalysisDao.selectDetailsForInner(innerList);
        depositBalanceAnalysisVOS.addAll(depositBalanceAnalysisVOS1);
        return depositBalanceAnalysisVOS;
    }
    /**
     *币种转换校验
     *@Param origCurId 原币种
     *@Param convertCurId   转换币种
     *@Param date 日期
     *@Param isAllCur 是否查所有币种
     *@return
     */
    public void checkConvertCurId(String origCurId, String convertCurId, String date,String isAllCur){
        //如果时间为空，则指定为当前时间
        if(StringUtils.isBlank(date)){
            date = DateUtils.getDate("yyyy-MM-dd");
        }
        //如果是查询所以币种
        if(CommonConstant.YES.equals(isAllCur)){
            Date parse;
            //时间处理
            if(StringUtils.isNotBlank(date) && date.length()==7){
                parse = DateUtils.parse(date, "yyyy-MM");
                parse = DateUtils.getLastMonthDate(parse);
            }else{
                parse = DateUtils.parse(date, "yyyy-MM-dd");
            }
            //时间处理为23:59:59
            String format = DateUtils.format(parse, "yyyy-MM-dd");
            String concat = format.concat(" 23:59:59");
            parse = DateUtils.parse(concat, "yyyy-MM-dd HH:mm:ss");
            List<BtCurrencyDTO> btCurrency = currencyService.getBtCurrency();
            for(BtCurrencyDTO dto:btCurrency){
                BigDecimal curExrate=BigDecimal.ZERO;
                if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(dto.getId())){
                    curExrate = iFcExcrateAdjustService.getExchangeRateBy(dto.getId(),convertCurId, parse, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//距查询日期最近的币别汇率
                }
                if (BigDecimal.ZERO.compareTo(curExrate) == 0) {
                    throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
                }
            }
        }else{
            Date parse;
            //时间处理
            if(StringUtils.isNotBlank(date) && date.length()==7){
                parse = DateUtils.parse(date, "yyyy-MM");
                parse = DateUtils.getLastMonthDate(parse);
            }else{
                parse = DateUtils.parse(date, "yyyy-MM-dd");
            }
            //时间处理为23:59:59
            String format = DateUtils.format(parse, "yyyy-MM-dd");
            String concat = format.concat(" 23:59:59");
            parse = DateUtils.parse(concat, "yyyy-MM-dd HH:mm:ss");
            BigDecimal curExrate = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(origCurId)) {
                curExrate = iFcExcrateAdjustService.getExchangeRateBy(origCurId, convertCurId, parse, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//距查询日期最近的币别汇率
            }
            if (BigDecimal.ZERO.compareTo(curExrate) == 0) {
                throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
            }
        }
    }
}
