package com.bytter.report.domain.netbank.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.domain.netbank.dao.HomeBankAccDtlDistributeDao;
import com.bytter.report.domain.netbank.vo.HomeBankAccDtlCurDateDistributeVO;
import com.bytter.report.domain.netbank.vo.HomeBankAccDtlDistributeSearchVO;
import com.bytter.report.domain.netbank.vo.HomeBankAccDtlDistributeVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BisAccDtlDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import java.math.BigDecimal;
import java.util.*;
/**
 * 结算收支分析Service
 *
 * @author huangx 2022-03-28
 */
@Service("homeBankAccDtlDistributeService")
@Transactional
public class HomeBankAccDtlDistributeService extends BaseService<HomeBankAccDtlDistributeVO, String> {
    @Autowired
    private HomeBankAccDtlDistributeDao bankAccDtlDistributeDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @Override
    public BaseDao<HomeBankAccDtlDistributeVO, String> getEntityDao() {
        return bankAccDtlDistributeDao;
    }
    /**
     * 查询结算收支分析
     *
     * @param searchVO
     */
    public void queryBankAccDtlDistribute(Model model, HomeBankAccDtlDistributeSearchVO searchVO, ICurrentUser user) {
        //获取数据
        Map<String, Object> map = this.initBankAccDtl(searchVO, user);
        model.addAttribute("curData", map.get("curData"));
        model.addAttribute("listDtl", map.get("listDtl"));
        model.addAttribute("successExc", map.get("successExc"));
        model.addAttribute("successExc", map.get("successExc"));
        model.addAttribute("waitFillExc", map.get("waitFillExc"));
        model.addAttribute("failedExc", map.get("failedExc"));
    }
    /**
     * 金额单位转换
     * @param dtl
     * @param moneyType
     */
    private void convertMoneyType(HomeBankAccDtlDistributeVO dtl, Integer moneyType) {
        //币种折算
        if(moneyType != null && moneyType > 1){
            dtl.setStartMoney(dtl.getStartMoney().divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
            dtl.setEndMoney(dtl.getEndMoney().divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
            dtl.setGetMoney(dtl.getGetMoney().divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
            dtl.setBorrowMoney(dtl.getBorrowMoney().divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
        }
    }
    /**
     * 收支明细初始化
     * @param searchVO
     */
    public Map<String, Object> initBankAccDtl(HomeBankAccDtlDistributeSearchVO searchVO, ICurrentUser user) {
        Map<String, Object> dataMap = new HashMap<>();
        List<String> corpIds = null;
        if(StringUtils.isNotBlank(searchVO.getCorpId())){
            corpIds = new ArrayList<>();
            for(String corpId:searchVO.getCorpId().split(CommonConstant.COMMA_SPLIT)){
                corpIds.add(corpId);
            }
        } else {
            //获取资源权限范围内的单位信息
            corpIds = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, user.getId());
        }
        searchVO.setCorpIds(corpIds);
        //权限控制类型  1-单位级 2-账号级
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            List<String> accList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
            searchVO.setAccIds(accList);
        }
        if(searchVO.getMoneyType() == null){
            searchVO.setMoneyType(10000);
        }
        Date endDate = DateUtils.getDateYMD();
        Date beginDate = DateUtils.addDay(endDate, -7);
        if("30".equals(searchVO.getDateType())){
            beginDate = DateUtils.addMonth(endDate, -1);
        } else if ("90".equals(searchVO.getDateType())) {
            beginDate = DateUtils.addMonth(endDate, -3);
        }
        if(searchVO.getBeginDate() == null){
            searchVO.setBeginDate(beginDate);
            searchVO.setEndDate(endDate);
        }
        HomeBankAccDtlDistributeVO curData = this.getData(searchVO);
        convertMoneyType(curData, searchVO.getMoneyType());
        dataMap.put("curData", curData);
        //银行账户最近交易
        List<BisAccDtlDTO> listDtl = this.bankAccDtlDistributeDao.getBankAccDtl(searchVO);
        this.convertMoneyType(listDtl, searchVO.getMoneyType());
        dataMap.put("listDtl", listDtl);
        //本日交易情况
        searchVO.setBeginDate(endDate);//查询当日
        List<HomeBankAccDtlCurDateDistributeVO> listExc = this.bankAccDtlDistributeDao.getExc(searchVO);
        HomeBankAccDtlCurDateDistributeVO successExc = new HomeBankAccDtlCurDateDistributeVO();
        successExc.setTotalMoney(BigDecimal.ZERO);
        successExc.setTotalNum(0);
        HomeBankAccDtlCurDateDistributeVO waitFillExc = new HomeBankAccDtlCurDateDistributeVO();
        waitFillExc.setTotalMoney(BigDecimal.ZERO);
        waitFillExc.setTotalNum(0);
        HomeBankAccDtlCurDateDistributeVO failedExc = new HomeBankAccDtlCurDateDistributeVO();
        failedExc.setTotalMoney(BigDecimal.ZERO);
        failedExc.setTotalNum(0);
        for(HomeBankAccDtlCurDateDistributeVO vo:listExc){
            vo.setTotalMoney(vo.getTotalMoney().divide(new BigDecimal(searchVO.getMoneyType()), 2, BigDecimal.ROUND_HALF_UP));
            if(BdlinkConstant.SUCCESS.equals(vo.getVoucherStat())){
                successExc.setTotalMoney(successExc.getTotalMoney().add(vo.getTotalMoney()));
                successExc.setTotalNum(successExc.getTotalNum()+vo.getTotalNum());
            }
            if(BdlinkConstant.BANK_FAILED.equals(vo.getVoucherStat()) || BdlinkConstant.INTERFACE_FAILED.equals(vo.getVoucherStat())){
                failedExc.setTotalMoney(failedExc.getTotalMoney().add(vo.getTotalMoney()));
                failedExc.setTotalNum(failedExc.getTotalNum()+vo.getTotalNum());
            }
            if(BdlinkConstant.WAIT_FILL.equals(vo.getVoucherStat())){
                waitFillExc.setTotalMoney(waitFillExc.getTotalMoney().add(vo.getTotalMoney()));
                waitFillExc.setTotalNum(waitFillExc.getTotalNum()+vo.getTotalNum());
            }
        }
        dataMap.put("successExc", successExc);
        dataMap.put("waitFillExc", waitFillExc);
        dataMap.put("failedExc", failedExc);
        return dataMap;
    }
    /**
     * 金额单位转换
     * @param listDtl
     * @param moneyType
     */
    private void convertMoneyType(List<BisAccDtlDTO> listDtl, Integer moneyType) {
        //币种折算
        if(moneyType != null && moneyType > 1){
            for (BisAccDtlDTO vo:listDtl) {
                vo.setAmt(vo.getAmt().divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
                if(vo.getBal() != null) {
                    vo.setBal(vo.getBal().divide(new BigDecimal(moneyType), 2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
    }
    /**
     * 结算收支分析图
     * @param searchVO
     * @param user
     * @return
     */
    public Map<String, Object> createBankAccDtlDistributeBar(HomeBankAccDtlDistributeSearchVO searchVO, ICurrentUser user) {
        Map<String, Object> map = new HashMap<>();
        //x轴
        List<String> xData = new ArrayList<>();
        Date endDate = DateUtils.getDateYMD();
        Date beginDate = DateUtils.addDay(endDate, -7);
        if("30".equals(searchVO.getDateType())){
            beginDate = DateUtils.addMonth(endDate, -1);
        } else if ("90".equals(searchVO.getDateType())) {
            beginDate = DateUtils.addMonth(endDate, -3);
        }
        List<Integer> getNumbers = new ArrayList<>();
        List<BigDecimal> getMoney = new ArrayList<>();
        List<Integer> borrowNumbers = new ArrayList<>();
        List<BigDecimal> borrowMoney = new ArrayList<>();
        searchVO.setBeginDate(beginDate);
        searchVO.setEndDate(endDate);

        //重新计算指标
        HomeBankAccDtlDistributeVO curData = this.getData(searchVO);
        convertMoneyType(curData, searchVO.getMoneyType());
        map.put("curData", curData);

        Map<String, HomeBankAccDtlDistributeVO> dataMap = this.getDataGroupByTransTime(searchVO, user);
        while (beginDate.compareTo(endDate) < 0){
            beginDate = DateUtils.addDay(beginDate, 1);
            if(beginDate.compareTo(endDate) >= 0){
                beginDate = endDate;
            }
            String curDate = DateUtils.formatDate(beginDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            xData.add(curDate.substring(curDate.indexOf("-")+1));
            HomeBankAccDtlDistributeVO data = dataMap.get(curDate);
            if (data == null) {
                data = new HomeBankAccDtlDistributeVO();
                data.setGetMoney(BigDecimal.ZERO);
                data.setBorrowMoney(BigDecimal.ZERO);
                data.setGetNumbers(0);
                data.setBorrowNumbers(0);
            }
            getNumbers.add(data.getGetNumbers());
            getMoney.add(data.getGetMoney());
            borrowNumbers.add(data.getBorrowNumbers());
            borrowMoney.add(data.getBorrowMoney());
        }
        map.put("xData", JsonUtils.toJSONString(xData));
        map.put("getNumbers", JsonUtils.toJSONString(getNumbers));
        map.put("getMoney", JsonUtils.toJSONString(getMoney));
        map.put("borrowNumbers", JsonUtils.toJSONString(borrowNumbers));
        map.put("borrowMoney", JsonUtils.toJSONString(borrowMoney));
        return map;
    }
    /**
     * 根据日期获取交易流水
     * @param searchVO
     * @param user
     * @return
     */
    public Map<String, HomeBankAccDtlDistributeVO> getDataGroupByTransTime(HomeBankAccDtlDistributeSearchVO searchVO, ICurrentUser user) {
        List<String> corpIds = null;
        if(StringUtils.isNotBlank(searchVO.getCorpId())){
            corpIds = new ArrayList<>();
            for(String corpId:searchVO.getCorpId().split(CommonConstant.COMMA_SPLIT)){
                corpIds.add(corpId);
            }
        } else {
            //获取资源权限范围内的单位信息
            corpIds = corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, user.getId());
        }
        searchVO.setCorpIds(corpIds);
        //权限控制类型  1-单位级 2-账号级
        String controlType = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        if (!CommonConstant.YES.equals(controlType)) {
            List<String> accList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
            searchVO.setAccIds(accList);
        }
        if(searchVO.getMoneyType() == null){
            searchVO.setMoneyType(10000);
        }
        List<HomeBankAccDtlDistributeVO> listData = this.bankAccDtlDistributeDao.getDataGroupByTransTime(searchVO);
        Map<String, HomeBankAccDtlDistributeVO> resultMap = new HashMap<>();
        for(HomeBankAccDtlDistributeVO vo:listData ){
            //金额单位折算
            vo.setGetMoney(vo.getGetMoney().divide(new BigDecimal(searchVO.getMoneyType()), 2, BigDecimal.ROUND_HALF_UP));
            vo.setBorrowMoney(vo.getBorrowMoney().divide(new BigDecimal(searchVO.getMoneyType()), 2, BigDecimal.ROUND_HALF_UP));
            String key = vo.getTransTime();
            HomeBankAccDtlDistributeVO tempVO;
            if(resultMap.containsKey(key)) {
                tempVO = resultMap.get(key);
                tempVO.setGetMoney(tempVO.getGetMoney().add(vo.getGetMoney()));
                tempVO.setBorrowMoney(tempVO.getBorrowMoney().add(vo.getBorrowMoney()));
                tempVO.setGetNumbers(tempVO.getGetNumbers()+vo.getGetNumbers());
                tempVO.setBorrowNumbers(tempVO.getBorrowNumbers()+vo.getBorrowNumbers());
            } else {
                tempVO = new HomeBankAccDtlDistributeVO();
                tempVO.setGetMoney(vo.getGetMoney());
                tempVO.setBorrowMoney(vo.getBorrowMoney());
                tempVO.setGetNumbers(vo.getGetNumbers());
                tempVO.setBorrowNumbers(vo.getBorrowNumbers());
            }
            resultMap.put(key, tempVO);
        }
        return resultMap;
    }
    private HomeBankAccDtlDistributeVO getData(HomeBankAccDtlDistributeSearchVO searchVO){
        HomeBankAccDtlDistributeVO data = new HomeBankAccDtlDistributeVO();
        data.setStartMoney(BigDecimal.ZERO);
        data.setEndMoney(BigDecimal.ZERO);
        data.setGetMoney(BigDecimal.ZERO);
        data.setBorrowMoney(BigDecimal.ZERO);
        data.setGetNumbers(Integer.parseInt("0"));
        data.setBorrowNumbers(Integer.parseInt("0"));
        List<HomeBankAccDtlDistributeVO> listData = this.bankAccDtlDistributeDao.getDataByCorp(searchVO);
        for (HomeBankAccDtlDistributeVO vo:listData) {
            data.setStartMoney(data.getStartMoney().add(vo.getStartMoney()));
            //data.setEndMoney(data.getEndMoney().add(vo.getEndMoney()));
            data.setGetMoney(data.getGetMoney().add(vo.getGetMoney()));
            data.setBorrowMoney(data.getBorrowMoney().add(vo.getBorrowMoney()));
            data.setGetNumbers(data.getGetNumbers()+vo.getGetNumbers());
            data.setBorrowNumbers(data.getBorrowNumbers()+vo.getBorrowNumbers());
        }
        //期末金额=期初金额+流入金额-流出金额
        data.setEndMoney(data.getStartMoney().add(data.getGetMoney()).subtract(data.getBorrowMoney()));
        return data;
    }
}
