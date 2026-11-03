package com.bytter.biz.finacial.closedeal.service;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bytter.biz.finacial.accbook.dao.FcAccbookDtlDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.EmergencySign;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant.PayType;
import com.bytter.biz.finacial.api.constant.SubjectConstant.SysSubject;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.closedeal.CloseMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.finacial.api.vo.FcCarryVO;
import com.bytter.biz.finacial.closedeal.dao.FcCarryDao;
import com.bytter.biz.finacial.closedeal.entity.FcCarry;
import com.bytter.biz.finacial.closedeal.vo.FcAccVoucherVO;
import com.bytter.biz.finacial.closedeal.vo.FcCarryVo;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 结转记录表
 * @date 2017-06-27 09:55:51
 */
@Service("fcCarryService")
@Transactional
public class FcCarryService extends BaseService<FcCarry, String> {
    @Autowired
    private FcCarryDao fcCarryDao;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IFcVoucherTypeService voucherTypeService;
    @DubboReference(check = false)
    private IFcBusRaceService busRaceService;
    @DubboReference(check = false)
    private IInnerAccountService accountService;
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    @Autowired
    private FcAccbookDtlDao fcAccbookDtlDao;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private IFcSubjectService fcSubjectService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService iFcBusinessRuleDefineService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IUserService iUserService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    /**
     * 用于凭证 0 借方或贷方
     */
    public final static BigDecimal ZERO = new BigDecimal(0);

    /**
     * 查询
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getFcCarry(DataTablesInput input, String resource, String userId) {
        String netId = "";
        if (input.getFormdata().get("netId") != null) {
            netId = ((String) input.getFormdata().get("netId")).trim();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date opDate = null;
        try {
            opDate = sdf.parse((String) input.getFormdata().get("opDate"));
        } catch (ParseException e) {
        }
        if (EmptyUtils.isEmpty(opDate)) {
            opDate = new Date();
        }
        List<FcCarryVo> listStatus = new ArrayList<FcCarryVo>();
        List<FcCarryVo> carryList = findDayCarryForStatus(netId, opDate, resource, userId);
        List<String> netIds = iCorpService.getUserNetIds(resource,0 ,userId);// 得到用户有权限的网点id
        List<CorpDTO> netList = iCorpService.listCorpDTOByCorpIds2(netIds);
        if (!EmptyUtils.isEmpty(opDate) && EmptyUtils.isNotEmpty(netId)) {
            List<CorpDTO> newnetList = new ArrayList<CorpDTO>();
            for (CorpDTO net : netList) {
                if (net.getId().equals(netId)) {
                    newnetList.add(net);
                }
            }
            netList = newnetList;
        }
        for (CorpDTO net : netList) {
            FcCarryVo carry = null;
            for (FcCarryVo c : carryList) {
                if (net.getId().equals(c.getNetId()) && c.getVoucherEndDate().compareTo(opDate) >= 0) {
                    carry = c;
                    break;
                }
            }
            if (carry != null) {
                FcCarryVo status = new FcCarryVo();
                // 反日结
                if (carry.getRestoreCarryTime() != null) {
                    /*
                     * status.setCarryTime(carry.getCarryTime());
                     * status.setNetId(carry.getNetId());
                     * status.setStatus(CloseConstant.RESERVE_DAY_STATUS);
                     * status.setRestoreCarryOptor(carry.getRestoreCarryOptor())
                     * ; status.setRestoreCarryTime(carryTime);
                     */
                    listStatus.add(status);
                } else {// 日结
                    status.setCarryTime(carry.getCarryTime());
                    status.setNetId(carry.getNetId());
                    status.setNetName(carry.getNetName());
                    status.setStatus(CloseConstant.DAY_STATUS);
                    status.setOperatorCode(carry.getOperatorCode());
                    status.setOpDate(opDate);
                    listStatus.add(status);
                }
            } else {
                // 未日结
                FcCarryVo status = new FcCarryVo();
                status.setNetId(net.getId());
                status.setStatus(CloseConstant.NO_DAY_STATUS);
                status.setNetName(net.getName());
                status.setOpDate(opDate);
                listStatus.add(status);
            }
        }
        List<FcCarryVo> showList = new ArrayList<FcCarryVo>();
        Integer start = input.getStart();
        Integer length = input.getLength();
        int i = 0;
        for (; i < length; i++) {
            if (start > listStatus.size() - 1) {
                break;
            }
            showList.add(listStatus.get(start));
            start++;
        }
        return new DataTablesOutput(input.getDraw(), showList, listStatus.size(), listStatus.size());
    }

    /**
     * 获取当前登陆用户的网点ID
     */
    public CorpDTO getUserNet(ICurrentUser user) {
        // UserDTO userDTO = userService.getByLoginName(user.getLoginName());
        CorpDTO corpDto = iCorpService.get(user.getCorpId());
        while (CorpConstant.CORP_TYPE_CODE_CORP.equals(corpDto.getType().toString())) {
            String parentId = corpDto.getParentId();
            if (!StringUtils.isEmpty(parentId)) {
                corpDto = iCorpService.get(parentId);
            } else {
                break;
            }
        }
        return corpDto;
    }

    /***
     * 查询出某一天日结列表
     */
    @Transactional(readOnly = true)
    public List<FcCarryVo> findDayCarryForStatus(String netId, Date opDate, String resource, String userId) {
        List<String> corpAuth = iCorpService.getUserCorpIds(resource, userId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("carryType", CloseConstant.CarryType.CARRY_TYPE_DAY_OVER.getValue());
        if (EmptyUtils.isNotEmpty(netId)) {
            params.put("netId", netId);
        }
        if (!EmptyUtils.isEmpty(opDate)) {
            params.put("voucherEndDate", opDate);
            params.put("voucherStartDate", opDate);
        }
        List<FcCarryVo> carryForStatus = fcCarryDao.findCarryForStatus(params, corpAuth);
        List<String> corpIds = carryForStatus.stream().map(FcCarryVo::getNetId).collect(Collectors.toList());
        List<String> userIds = carryForStatus.stream().map(FcCarryVo::getOperatorCode).collect(Collectors.toList());
        Map<String, String> nameMap = iCorpService.getNameByIds(corpIds);
        Map<String, String> loginNameMap = iUserService.getLoginNameByIds(userIds);
        carryForStatus.forEach(a -> {
            a.setNetName(nameMap.get(a.getNetId()));
            a.setOperatorCode(loginNameMap.get(a.getOperatorCode()));
        });
        return carryForStatus;
    }

    /**
     * 日结日志查询
     */
    @Transactional(readOnly = true)
    public DataTablesOutput doQueryDayLog(DataTablesInput dataTablesInput, String resource, String userId) {
        List<String> corpAuth = iCorpService.getUserCorpIds(resource, userId);
        DataTablesOutput dataTablesOutput = fcCarryDao.getFcCarryLog(dataTablesInput, corpAuth);
        List data = dataTablesOutput.getData();
        List<FcCarryVo> list = new ArrayList<>();
        for (Object datum : data) {
            FcCarryVo vo = (FcCarryVo) datum;
            list.add(vo);
        }
        List<String> operatorCodes = list.stream().map(FcCarryVo::getOperatorCode).collect(Collectors.toList());
        List<String> restoreCarryOptors = list.stream().map(FcCarryVo::getRestoreCarryOptor).collect(Collectors.toList());
        List<String> netIdList = list.stream().map(FcCarryVo::getNetId).collect(Collectors.toList());
        Map<String, String> operMap = iUserService.getLoginNameByIds(operatorCodes);
        Map<String, String> restoreMap = iUserService.getLoginNameByIds(restoreCarryOptors);
        Map<String, String> nameMap = iCorpService.getNameByIds(netIdList);
        list.forEach(a -> {
            a.setOperatorCode(operMap.get(a.getOperatorCode()));
            a.setRestoreCarryOptor(restoreMap.get(a.getRestoreCarryOptor()));
            a.setNetName(nameMap.get(a.getNetId()));
        });
        dataTablesOutput.setData(list);
        return dataTablesOutput;
    }

    public BaseDao<FcCarry, String> getEntityDao() {
        return fcCarryDao;
    }

    /**
     * 功能介绍 : 查询最近一次未反结转的结转数据。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public FcCarry queryNoReverseFcCarries(String netCode, String carryType) {
        return fcCarryDao.queryNoReverseFcCarries(netCode, carryType);
    }

    /**
     * 功能介绍 ：查询损益类科目记账区间的发生额 。<br>
     * <b>日期：</b>2018年1月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput<FcAccVoucherVO> findProfitYear(Map<String, Object> param, DataTablesInput input) {
        List<FcAccDTO> fcAccList = getAccList(param);
        if (CollectionUtils.isEmpty(fcAccList)) {
            return new DataTablesOutput(input.getDraw(), null, 0, 0);
        }
        List<String> accList = fcAccList.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        Map<String, FcAccDTO> accMap = fcAccList.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, java.util.function.Function.identity()));
        DataTablesOutput<FcAccVoucherVO> output = fcCarryDao.findProfitYear(param, input, accList);
        List<FcAccVoucherVO> fcAccVoucherVOList = output.getData();
        for (FcAccVoucherVO vo : fcAccVoucherVOList) {
            FcAccDTO accDTO = accMap.get(vo.getAccount());
            vo.setBalanceWay(accDTO.getBalanceWay());
            vo.setBalance(vo.getBalance().multiply(new BigDecimal(accDTO.getBalanceWay())));
            if (StringUtils.isNotEmpty(vo.getCurId())) {
                BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(vo.getCurId());
                vo.setCurName(btCurrencyDTO.getCurName());
            }
        }
        return output;
    }

    /**
     * 根据查询条件获取内部账号信息
     *
     * @param param 查询参数
     * @return 内部账户集合
     */
    private List<FcAccDTO> getAccList(Map<String, Object> param) {
        FcAccDTO fcAccDto = new FcAccDTO();
        fcAccDto.setNetId(param.get("netCode").toString());
        return iFcAccService.getFcCarryList(fcAccDto);
    }

    /**
     * 构建本年利润结转凭证
     * @param param
     * @return
     */
    public VoucherWrapper createProfitYearVoucher(Map<String, Object> param) {
        VoucherWrapper voucher = new VoucherWrapper();
        Map<String, VoucherWrapper> vouMap = getNewProfitYearVoucher(param);
        for (Entry<String, VoucherWrapper> entry : vouMap.entrySet()) {
            VoucherWrapper vou = entry.getValue();
            if (vou != null && !vou.getTarget().isEmpty()) {
                voucher.addTargets(vou.getTarget());
            }
        }
        return sumMoney(voucher);
    }

    /**
     * 结转本年利润
     * @param param
     * @return
     */
    public Map<String, VoucherWrapper> getNewProfitYearVoucher(Map<String, Object> param) {
        Integer busOriType = (Integer) param.get("busOriType");// 损益转本年利润
        String userCode = (String) param.get("userCode");
        Date opDate = (Date) param.get("opDate");
        String netCode = (String) param.get("netCode");
//		String summary = "本年利润";
        String summary = multiLangServiceI.getLang("table.fcCarry.yearIncome");
        String payOutTitle = multiLangServiceI.getLang("voucher.entryDefine.payOut");
        String incomeTitle = multiLangServiceI.getLang("voucher.entryDefine.income");
        String title = null;
        // 本年利润： 311
        // 企业活期存款：2120
        List<FcAccDTO> accList = getAccList(param);
        if (CollectionUtils.isEmpty(accList)) {
            return null;
        }
        List<FcAccVoucherVO> lst = this.fcCarryDao.queryProfitYear(param, accList.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList()));//损益类科目账号发生额明细
//		判断集合是否有数据
        if (lst == null || lst.size() == 0) {
            return null;
        }
        List<String> fcAccAccounts = lst.stream().map(FcAccVoucherVO::getAccount).collect(Collectors.toList());
        Map<String, FcAccDTO> fcAccDTOMap = iFcAccService.queryFcAccByIdsOrAccs(Lists.newArrayList(), fcAccAccounts);
        lst.stream().forEach(vo -> {
            if (fcAccDTOMap.containsKey(vo.getAccount())) {
                vo.setBalanceWay(fcAccDTOMap.get(vo.getAccount()).getBalanceWay());
            }
        });
//		获取损益结转会计分录
//		收入分录
        FcBusinessRuleDefineDTO fcBusinessRuleDefineIncome = getBusinessRuleExp(FinaBookConstant.SysRaceCode.CARRYOVER_PROFIT_BOOK_INCOME.getValue());
        Map<String, FcVoucherEntryDefineDTO> voucherEntryDefineIncome = getVoucherEntryDefine(fcBusinessRuleDefineIncome);//会计分录科目编码
//		支出分录
        FcBusinessRuleDefineDTO fcBusinessRuleDefinePayOut = getBusinessRuleExp(FinaBookConstant.SysRaceCode.CARRYOVER_PROFIT_BOOK_PAYOUT.getValue());
        Map<String, FcVoucherEntryDefineDTO> voucherEntryDefinePayOut = getVoucherEntryDefine(fcBusinessRuleDefinePayOut);
        Map<String, List<Object[]>> inList = new HashMap<String, List<Object[]>>();
        Map<String, List<Object[]>> outList = new HashMap<String, List<Object[]>>();
        Map<String, BigDecimal> inMap = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> outMap = new HashMap<String, BigDecimal>();
        Map<String, String> accMap = new HashMap<String, String>();
        Map<String, String> curMap = new HashMap<String, String>();
        for (FcAccVoucherVO voucher : lst) {
            String account = voucher.getAccount();// 收入账号
            String subjectCode = voucher.getSubjectCode();// 账户科目
            String subjectName = voucher.getSubjectName(); //账号科目名称
            String curCode = voucher.getCurId();
            String balancyWay = String.valueOf(voucher.getBalanceWay());
            BigDecimal amount = voucher.getBalance();
            String curName = voucher.getCurName();
            curMap.put(curCode, curName);
            String deposit = accMap.get(curCode);
            if (EmptyUtils.isEmpty(deposit)) {
                // 查询网点下的本年利润账号
                deposit = getNetProfitAcc(curCode, netCode).getBlnctrAcc();
                if (StringUtils.isBlank(deposit)) {
                    // throw new BusinessException("网点代码：" + netCode + ",币别代码" +
                    // curCode + ",没有开本年利润账户。");
                }
                accMap.put(curCode, deposit);
            }
            //目前看到的现象是 subjectCode 为中文而voucherEntryDefineIncome 中保存的是id
            if ("-1".equals(balancyWay) && (voucherEntryDefineIncome.containsKey(subjectCode) || voucherEntryDefineIncome.containsKey(subjectName))) {// 收入记账凭证,根据记账规则收入分录
                List<Object[]> temps = inList.get(curCode);
                if (null == temps) {
                    temps = new ArrayList<Object[]>();
                    inList.put(curCode, temps);
                }
                String dtlSummary = summary;
                FcVoucherEntryDefineDTO voucherEntryDefineDTO = voucherEntryDefineIncome.get(subjectCode);
                if (!EmptyUtils.isEmpty(voucherEntryDefineDTO)) {
                    if (EmptyUtils.isNotEmpty(voucherEntryDefineDTO.getSummary())) {
                        dtlSummary = voucherEntryDefineDTO.getSummary();
                    }
                }
                temps.add(new Object[]{amount, ZERO, account, dtlSummary});
                BigDecimal inBal = inMap.get(curCode);
                if (null == inBal) {
                    inBal = BigDecimal.ZERO;
                }
                inBal = inBal.add(amount);
                inMap.put(curCode, inBal);
                title = incomeTitle + "-" + curName;
            } else if ("1".equals(balancyWay) && (voucherEntryDefinePayOut.containsKey(subjectCode) || voucherEntryDefinePayOut.containsKey(subjectName))) {// 付出记账凭证,根据记账规则支出分录
                List<Object[]> temps = outList.get(curCode);
                if (null == temps) {
                    temps = new ArrayList<Object[]>();
                    outList.put(curCode, temps);
                }
                String dtlSummary = summary;
                FcVoucherEntryDefineDTO voucherEntryDefineDTO = voucherEntryDefinePayOut.get(subjectCode);
                if (!EmptyUtils.isEmpty(voucherEntryDefineDTO)) {
                    if (EmptyUtils.isNotEmpty(voucherEntryDefineDTO.getSummary())) {
                        dtlSummary = voucherEntryDefineDTO.getSummary();
                    }
                }
                temps.add(new Object[]{ZERO, amount, account, dtlSummary});
                BigDecimal outBal = outMap.get(curCode);
                if (null == outBal) {
                    outBal = BigDecimal.ZERO;
                }
                outBal = outBal.add(amount);
                outMap.put(curCode, outBal);
                title = payOutTitle + "-" + curName;
            }
        }
        Map<String, VoucherWrapper> vouMap = new HashMap<String, VoucherWrapper>();
        for (Entry<String, BigDecimal> entry : inMap.entrySet()) {
            String curCode = entry.getKey();
            List<Object[]> temps = inList.get(curCode);
            BigDecimal bal = entry.getValue();
            String deposit = accMap.get(curCode);
            if (EmptyUtils.isEmpty(deposit)) {
                // throw new BusinessException("网点代码：" + netCode + ",币别代码" +
                // curCode + ",没有开本年利润账户。");
            }
            String curName = curMap.get(curCode);
            if (EmptyUtils.isEmpty(curName)) {
                curName = "";// TODO
            }
            title = incomeTitle + "-" + curName;
            if (bal.compareTo(BigDecimal.ZERO) != 0) {
                String dtlSummary = summary;//获取贷方定义的摘要内容
                FcVoucherEntryDefineDTO defineDTO = getProfitBookDefine(fcBusinessRuleDefineIncome, Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT));
                if (!EmptyUtils.isEmpty(defineDTO)) {
                    if (EmptyUtils.isNotEmpty(defineDTO.getSummary())) {
                        dtlSummary = defineDTO.getSummary();
                    }
                }
                temps.add(new Object[]{ZERO, bal, deposit, dtlSummary});
                VoucherWrapper voucher = vouMap.get(curCode);
                if (voucher == null) {
                    voucher = new VoucherWrapper();
                    voucher.setUserCode(userCode);
                    voucher.setOriType(busOriType);
                    vouMap.put(curCode, voucher);
                }
                voucher.getTarget().add(this.getFcFinacialAccbook(busOriType, userCode, opDate, getFcFinacialAccbookDtls(temps), title, netCode, FinaBookConstant.SysRaceCode.CARRYOVER_PROFIT_BOOK_INCOME.getValue(), fcBusinessRuleDefineIncome.getVoucherType()));
            }
        }
        for (Entry<String, BigDecimal> entry : outMap.entrySet()) {
            String curCode = entry.getKey();
            List<Object[]> temps = outList.get(curCode);
            BigDecimal bal = entry.getValue();
            String deposit = accMap.get(curCode);
            if (EmptyUtils.isEmpty(deposit)) {
                // throw new BusinessException("网点代码：" + netCode + ",币别代码" +
                // curCode + ",没有开本年利润账户。");
            }
            String curName = curMap.get(curCode);
            if (EmptyUtils.isEmpty(curName)) {
                curName = "";// TODO
            }
            title = payOutTitle + "-" + curName;
            if (bal.compareTo(BigDecimal.ZERO) != 0) {
                List<Object[]> dtls = new ArrayList<Object[]>();
                String dtlSummary = summary;//获取借方定义的摘要内容
                FcVoucherEntryDefineDTO defineDTO = getProfitBookDefine(fcBusinessRuleDefinePayOut, Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT));
                if (!EmptyUtils.isEmpty(defineDTO)) {
                    if (EmptyUtils.isNotEmpty(defineDTO.getSummary())) {
                        dtlSummary = defineDTO.getSummary();
                    }
                }
                dtls.add(new Object[]{bal, ZERO, deposit, dtlSummary});
                dtls.addAll(temps);
                VoucherWrapper voucher = vouMap.get(curCode);
                if (voucher == null) {
                    voucher = new VoucherWrapper();
                    voucher.setUserCode(userCode);
                    voucher.setOriType(busOriType);
                    vouMap.put(curCode, voucher);
                }
                voucher.getTarget().add(this.getFcFinacialAccbook(busOriType, userCode, opDate, getFcFinacialAccbookDtls(dtls), title, netCode, FinaBookConstant.SysRaceCode.CARRYOVER_PROFIT_BOOK_PAYOUT.getValue(), fcBusinessRuleDefineIncome.getVoucherType()));
            }
        }
        return vouMap;
    }

    public FcVoucherEntryDefineDTO getProfitBookDefine(FcBusinessRuleDefineDTO fcBusinessRuleDefine, Integer balanceWay) {
        List<FcVoucherEntryDefineDTO> vouchers = fcBusinessRuleDefine.getVouchers();
        if (EmptyUtils.isNotEmpty(vouchers)) {
            for (FcVoucherEntryDefineDTO defineDTO : vouchers) {
                if (balanceWay.equals(defineDTO.getBalanceWay())) {
                    return defineDTO;
                }
            }
        }
        return null;
    }

    /**
     * 获得一张结转凭证,用于新建立凭证
     * @param busOriType
     * @param userCode
     * @param opDate
     * @param accBookDtl
     * @param rmk
     * @param netId
     * @return
     */
    private FcAccbookVoucherVO getFcFinacialAccbook(Integer busOriType, String userCode, Date opDate, List<FcAccbookDtl> accBookDtl, String rmk, String netId, String sysRaceCode, String voucherType) {
        FcAccbookVoucherVO accbookVO = new FcAccbookVoucherVO();
        FcAccbook accBook = new FcAccbook();
        if (accBookDtl.size() < 2) {
            return null;
        }
        accBook.setBusOriType(busOriType);
        accBook.setEnrolTime(new Date());
        accBook.setTallyDate(opDate);
        accBook.setInterestStartDate(opDate);
        if (EmptyUtils.isEmpty(voucherType)) {
            voucherType = getVoucherType();
        }
        accBook.setVoucherType(voucherType);
        accBook.setBusRaceId(getBusRaceBySysRace(netId, sysRaceCode).getId());
        accBook.setEmergencySign(EmergencySign.EMERGENCY_SIGN_GENERAL.getValue());
        accBook.setRmk(rmk);
        accBook.setOperatorCode(userCode);
        accbookVO.setBookDtls(accBookDtl);
        accbookVO.setAccbook(accBook);
        return accbookVO;
    }

    private FcBusRaceDTO getBusRaceBySysRace(String netId, String sysRaceCode) {
        List<FcBusRaceDTO> list = busRaceService.findByNetIdSysRaceCode(sysRaceCode, netId);
        if (EmptyUtils.isEmptyList(list)) {
            throw new ParameterException(CloseMessageEnum.FINACIAL_NO_BUSRACE_ALERT_INFO2);
        }
        return list.get(0);
    }

    /**
     * 构造凭证的明细列表,用于新建立凭证
     *
     * @param data
     * @return
     */
    private List<FcAccbookDtl> getFcFinacialAccbookDtls(List<Object[]> data) {
        List<FcAccbookDtl> lists = new ArrayList<FcAccbookDtl>();
        Integer lineNo = 0;
        for (Object[] row : data) {
            lists.add(getFcFinacialAccbookDtl((BigDecimal) row[0], (BigDecimal) row[1], (String) row[2], (String) row[3], ++lineNo));
        }
        return lists;
    }

    /**
     * 构造凭证明细,用于新建立凭证
     *
     * @param lend
     * @param loan
     * @param acc
     * @param summary
     * @param lindNo
     * @return
     */
    private FcAccbookDtl getFcFinacialAccbookDtl(BigDecimal lend, BigDecimal loan, String acc, String summary, Integer lindNo) {
        if (lend.compareTo(BigDecimal.ZERO) == 0 && loan.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        FcAccbookDtl dtl = new FcAccbookDtl();
        dtl.setSummaryContent(summary);
        dtl.setCreditMoney(loan);
        dtl.setDebitMoney(lend);
        dtl.setVoucherType(getVoucherType());
        dtl.setLineNo(lindNo);
        dtl.setAccount(acc);
        return dtl;
    }

    /**
     * 获取付款方式
     *
     * @return
     */
    private String getVoucherType() {
        FcVoucherTypeDTO voucherTypeDTO = voucherTypeService.getSysFcVoucherType(PayType.PAY_TYPE_11.getValue());
        if (null == voucherTypeDTO) {
            // throw new ParameterException(IMessageEnum);
        }
        return voucherTypeDTO.getId();
    }

    private FcAccDTO getNetProfitAcc(String curCode, String netCode) {
        List<FcAccDTO> list = accountService.listAccountByMoreParams(curCode, netCode, netCode, null, SysSubject.PROFIT_YEAR.get());
        if (EmptyUtils.isEmptyList(list)) {
            throw new ParameterException(CloseMessageEnum.FINACIAL_NO_PROFIT_YEAR_ACC_ALERT_INFO1);
        }
        return list.get(0);
    }

    /**
     * 对借贷方借贷方金额进行汇总
     *
     * @param voucher
     * @return
     */
    private VoucherWrapper sumMoney(VoucherWrapper voucher) {
        for (FcAccbookVoucherVO book : voucher.getTarget()) {
            BigDecimal debit = new BigDecimal(0);
            BigDecimal credit = new BigDecimal(0);
            for (FcAccbookDtl dtls : book.getBookDtls()) {
                debit = debit.add(dtls.getDebitMoney());
                credit = credit.add(dtls.getCreditMoney());
            }
            book.setDebitSum(debit);
            book.setCreditSum(credit);
        }
        return voucher;
    }

    /**
     * 损益转本年利润保存
     *
     * @param param
     */
    public void submitVoucher(Map<String, Object> param) {
//		数据校验
        submitValidateVoucherWrapperDTO(param);
        String userId = param.get("userCode").toString();
        String netId = String.valueOf(param.get("netCode"));
//		构造数据
        Map<String, VoucherWrapper> vouMap = this.getNewProfitYearVoucher(param);
//		如果没有数据则给出提示
        if (vouMap == null || vouMap.size() == 0) {
            throw new ParameterException(CloseMessageEnum.FINACIAL_HAS_DATA_ALERT_INFO5);
        }
        for (Entry<String, VoucherWrapper> entry : vouMap.entrySet()) {
            VoucherWrapper vou = entry.getValue();
            if (vou != null && !vou.getTarget().isEmpty()) {
//				循环收入和支持记账
                for (FcAccbookVoucherVO fcAccbookVoucherVO : vou.getTarget()) {
//					构建收入或支出记账
                    List<FcAccbookVoucherVO> FcAccbookVoucherVOS = new ArrayList<>();
                    VoucherWrapper subVoucher = new VoucherWrapper();
                    FcAccbookVoucherVOS.add(fcAccbookVoucherVO);
                    subVoucher.setTarget(FcAccbookVoucherVOS);
                    subVoucher.setOpType(OperateType.SUBMIT);
                    //VoucherWrapper 转换为 VoucherWrapperDTO
                    VoucherWrapperDTO voucherWrapperDTO = transVoucher(subVoucher);
//				手动记账
                    voucherWrapperDTO.setAutoAccounter(false);
                    voucherWrapperDTO.setNetId(netId);
//				设置是否自动复核
                    voucherWrapperDTO.setAutoCheck(isAutoCheck());
                    voucherWrapperDTO.setUserCode(vou.getUserCode());
                    iFcAccbookService.submitVoucher(voucherWrapperDTO);
                    List<FcAccbookVoucherDTO> list = voucherWrapperDTO.getTarget();
                    if (EmptyUtils.isNotEmpty(list) && list.size() > 0) {
                        notificationService.sendIgnoreMySelf(list.get(0).getAccbook().getId(), voucherWrapperDTO.getNetId(), NotificationConstant.FINACIAL.CLOSE_FCCARRY_CHECK, userId);
                    }
                    if (isAutoCheck()) {
                        //复核通过后构建FcCarry
                        FcCarry fcCarry = createFcCarry(param, voucherWrapperDTO);
                        UserDTO userDTO = iUserService.getByUserId(fcCarry.getOperatorCode());
                        fcCarry.setUsername(userDTO.getUsername());
                        saveEntity(fcCarry);
                    }
                }
            }
        }
    }

    /**
     * 构建待复核损益结转本年利润数据
     *
     * @param params
     * @return
     */
    public VoucherWrapperDTO getCheckProfitYear(Map<String, Object> params) {
        List<FcAccbookVoucherVO> fcAccbookVoucherVOS = getFcAccbookVoucherVO(params);
        VoucherWrapper wrapper = new VoucherWrapper();
        wrapper.setOriType(Integer.valueOf(params.get("busOriType").toString()));//业务来源
        wrapper.setTarget(fcAccbookVoucherVOS);
        sumMoney(wrapper);
        VoucherWrapperDTO voucherWrapperDTO = transVoucher(wrapper);
        return voucherWrapperDTO;
    }

    /**
     * 打回后重新提交操作
     *
     * @param params
     */
    public void rebackCheckProfitYear(Map<String, Object> params) {
        String userId = params.get("userCode").toString();
        List<FcAccbookVoucherVO> fcAccbookVoucherVOS = getFcAccbookVoucherVO(params);
        //		如果没有数据则给出提示
        if (fcAccbookVoucherVOS == null || fcAccbookVoucherVOS.size() == 0) {
            throw new ParameterException(CloseMessageEnum.FINACIAL_HAS_DATA_ALERT_INFO5);
        }
        String busOriType = params.get("busOriType").toString();
        for (FcAccbookVoucherVO fcAccbookVoucherVO : fcAccbookVoucherVOS) {
            VoucherWrapper wrapper = new VoucherWrapper();
            wrapper.setOriType(Integer.valueOf(busOriType));//业务来源
            wrapper.addTarget(fcAccbookVoucherVO);
            wrapper.setBusGlobalId(fcAccbookVoucherVO.getAccbook().getBusGlobalId());
            VoucherWrapperDTO voucherWrapperDTO = transVoucher(wrapper);
            voucherWrapperDTO.setUserCode(params.get("userCode").toString());
            //				手动记账
            voucherWrapperDTO.setAutoAccounter(false);
//				设置是否自动复核
            voucherWrapperDTO.setAutoCheck(isAutoCheck());
            iFcAccbookService.submitVoucher(voucherWrapperDTO);
            notificationService.sendIgnoreMySelf(fcAccbookVoucherVO.getAccbook().getId(), fcAccbookVoucherVO.getAccbook().getNetId(), NotificationConstant.FINACIAL.CLOSE_FCCARRY_CHECK, userId);
        }
    }

    /**
     * 删除结转凭证
     *
     * @param params
     */
    public void deleteProfitYear(Map<String, Object> params) {
        List<FcAccbookVoucherVO> fcAccbookVoucherVOS = getFcAccbookVoucherVO(params);
        //如果没有数据则给出提示
        if (fcAccbookVoucherVOS == null || fcAccbookVoucherVOS.size() == 0) {
            throw new ParameterException(CloseMessageEnum.FINACIAL_HAS_DATA_ALERT_INFO5);
        }
        String busOriType = params.get("busOriType").toString();
        for (FcAccbookVoucherVO fcAccbookVoucherVO : fcAccbookVoucherVOS) {
            VoucherWrapper wrapper = new VoucherWrapper();
            wrapper.setOriType(Integer.valueOf(busOriType));//业务来源
            wrapper.addTarget(fcAccbookVoucherVO);
            wrapper.setBusGlobalId(fcAccbookVoucherVO.getAccbook().getBusGlobalId());
            VoucherWrapperDTO voucherWrapperDTO = transVoucher(wrapper);
            voucherWrapperDTO.setUserCode(params.get("userCode").toString());
            voucherWrapperDTO.setAutoCheck(isAutoCheck());
            iFcAccbookService.deleteFcAccbook(voucherWrapperDTO);
        }
    }

    /**
     * 复核操作
     *
     * @param params
     */
    public void checkProfitYear(Map<String, Object> params) {
        String userId = params.get("userCode").toString();
        List<FcAccbookVoucherVO> fcAccbookVoucherVOS = getFcAccbookVoucherVO(params);
        //		如果没有数据则给出提示
        if (fcAccbookVoucherVOS == null || fcAccbookVoucherVOS.size() == 0) {
            throw new ParameterException(CloseMessageEnum.FINACIAL_HAS_DATA_ALERT_INFO5);
        }
        String busOriType = params.get("busOriType").toString();
        for (FcAccbookVoucherVO fcAccbookVoucherVO : fcAccbookVoucherVOS) {
            VoucherWrapper wrapper = new VoucherWrapper();
            wrapper.setOriType(Integer.valueOf(busOriType));//业务来源
            wrapper.addTarget(fcAccbookVoucherVO);
            wrapper.setBusGlobalId(fcAccbookVoucherVO.getAccbook().getBusGlobalId());
            VoucherWrapperDTO voucherWrapperDTO = transVoucher(wrapper);
            voucherWrapperDTO.setUserCode(params.get("userCode").toString());
            //				手动记账
            voucherWrapperDTO.setAutoAccounter(false);
//				设置是否自动复核
            voucherWrapperDTO.setAutoCheck(isAutoCheck());
            if ("1".equals(params.get("flag").toString())) {
//				记账复核提交
                iFcAccbookService.checkVoucher(voucherWrapperDTO);
//				复核通过后构建FcCarry
                FcCarry fcCarry = createFcCarry(params, voucherWrapperDTO);
                UserDTO userDTO = iUserService.getByUserId(fcCarry.getOperatorCode());
                fcCarry.setUsername(userDTO.getUsername());
                saveEntity(fcCarry);
            } else {
//				记账复核打回
                FcAccbookExtDTO fcAccbookExtDTO = new FcAccbookExtDTO();
                fcAccbookExtDTO.setBackReason(params.get("backReason").toString());
                voucherWrapperDTO.getTarget().get(0).setBookExt(fcAccbookExtDTO);
                iFcAccbookService.backVoucher(voucherWrapperDTO);
            }
            notificationService.complete(NotificationConstant.FINACIAL.CLOSE_FCCARRY_CHECK, fcAccbookVoucherVO.getAccbook().getId(), userId);
        }
    }

    /**
     * 损益结转复核通过构建FcCarry
     *
     * @param params
     * @param voucherWrapperDTO
     * @return
     */
    public FcCarry createFcCarry(Map<String, Object> params, VoucherWrapperDTO voucherWrapperDTO) {
        FcCarry fcCarry = new FcCarry();
        String userCode = String.valueOf(params.get("userCode"));
        UserDTO userDTO = iUserService.getByUserId(userCode);
//		结转日期
        Date tallyDate = voucherWrapperDTO.getTarget().get(0).getAccbook().getTallyDate();
        fcCarry.setCarryType(CloseConstant.CarryType.CARRY_TYPE_INCOME.getValue());
        fcCarry.setNetId(params.get("netId").toString());
        fcCarry.setCarryTime(new Date());
        fcCarry.setOperatorCode(params.get("userCode").toString());
        fcCarry.setVoucherStartDate(tallyDate);
        fcCarry.setVoucherEndDate(tallyDate);
        fcCarry.setCreateBy(userDTO.getUsername());
        fcCarry.setCreateTime(new Date());
        fcCarry.setUpdateBy(userDTO.getUsername());
        fcCarry.setUpdateTime(new Date());
        return fcCarry;
    }

    /**
     * 构建List<FcAccbookVoucherVO>
     *
     * @param fcAccbooks
     * @param params
     * @return
     */
    public List<FcAccbookVoucherVO> createFcAccbookVoucherVO(List<FcAccbook> fcAccbooks, Map<String, Object> params) {
        List<FcAccbookVoucherVO> fcAccbookVoucherVOS = new ArrayList<>();
        for (FcAccbook fcAccbook : fcAccbooks) {
            FcAccbookVoucherVO fcAccbookVoucherVO = new FcAccbookVoucherVO();
            fcAccbookVoucherVO.setAccbook(fcAccbook);
            fcAccbookVoucherVO.setBookDtls(fcAccbookDtlDao.getAccbookDtlsByBookId(fcAccbook.getId()));
            fcAccbookVoucherVO.setNetCode(params.get("netId").toString());
            fcAccbookVoucherVOS.add(fcAccbookVoucherVO);
        }
        return fcAccbookVoucherVOS;
    }

    /**
     * 获取待复核得账务数据
     *
     * @param params
     * @return
     */
    public List<FcAccbookVoucherVO> getFcAccbookVoucherVO(Map<String, Object> params) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("voucherState", params.get("voucherState"));
        param.put("redblueSign", FinacialConstant.REDBLUE_SIGN_BLUE);
        param.put("busOriType", params.get("busOriType"));
        param.put("operatorCode", params.get("userCode"));
        if (StringUtils.isNotEmpty(params.get("netId").toString())) {
            param.put("netId", params.get("netId").toString());
        }
        if (StringUtils.isNotEmpty(params.get("curCode").toString())) {
            param.put("curCode", params.get("curCode").toString());
        }
        List<FcAccbookVoucherVO> fcAccbookVoucherVOS = createFcAccbookVoucherVO(fcCarryDao.getCheckProfitYear(param), params);
        return fcAccbookVoucherVOS;
    }

    /**
     * 提交前数据校验
     *
     * @param params
     */
    public void submitValidateVoucherWrapperDTO(Map<String, Object> params) {
//	    增加状态参数
        params.put("voucherState", new String[]{FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.getValue(), FinaBookConstant.VoucherStatus.STATUS_BACK.getValue()});//待复核状态
        params.put("netId", params.get("netCode").toString());//网点
        VoucherWrapperDTO voucherWrapperDTO = this.getCheckProfitYear(params);
        if (voucherWrapperDTO != null && voucherWrapperDTO.getTarget() != null && voucherWrapperDTO.getTarget().size() > 0) {
            throw new ParameterException(CloseMessageEnum.FINACIAL_HAS_DATA_ALERT_INFO3);
        }
    }

    /**
     * VoucherWrapper 转换为 VoucherWrapperDTO
     *
     * @param voucher
     * @return
     */
    private VoucherWrapperDTO transVoucher(VoucherWrapper voucher) {
        VoucherWrapperDTO wrapperDTO = new VoucherWrapperDTO();
        BeanUtils.copyProperties(voucher, wrapperDTO);//复制属性
        //凭证对象
        List<FcAccbookVoucherVO> target = voucher.getTarget();
        if (null != target && !target.isEmpty()) {
            List<FcAccbookVoucherDTO> ntarget = new ArrayList<FcAccbookVoucherDTO>();
            for (FcAccbookVoucherVO voucherVO : target) {
                FcAccbookVoucherDTO voucherDTO = new FcAccbookVoucherDTO();
                BeanUtils.copyProperties(voucherVO, voucherDTO);
                FcAccbook book = voucherVO.getAccbook();
                if (null == book.getInterestStartDate()) {
                    book.setInterestStartDate(book.getTallyDate());//起息日期默认记账日期
                }
                if (null != book) {
                    FcAccbookDTO bookDTO = new FcAccbookDTO();
                    BeanUtils.copyProperties(book, bookDTO);
                    if (StringUtils.isEmpty(bookDTO.getVoucherName()) && StringUtils.isNotEmpty(bookDTO.getVoucherType())) {
                        bookDTO.setVoucherName(fcVoucherTypeService.getFcVoucherTypeById(bookDTO.getVoucherType()).getVoucherName());
                    }
                    if (StringUtils.isEmpty(bookDTO.getRaceName()) && StringUtils.isNotEmpty(bookDTO.getBusRaceId())) {
                        FcBusRaceDTO busRaceDTO = busRaceService.findFcBusRaceById(bookDTO.getBusRaceId());
                        if (busRaceDTO != null) {
                            bookDTO.setRaceName(busRaceDTO.getRaceName() == null ? "" : busRaceDTO.getRaceName());
                        }
                    }
                    voucherDTO.setAccbook(bookDTO);
                }
                FcAccbookExt bookExt = voucherVO.getBookExt();
                if (null != bookExt) {
                    FcAccbookExtDTO bookExtDTO = new FcAccbookExtDTO();
                    BeanUtils.copyProperties(bookExt, bookExtDTO);
                    voucherDTO.setBookExt(bookExtDTO);
                }
                List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
                if (null != bookDtls && !bookDtls.isEmpty()) {
                    List<FcAccbookDtlDTO> bookDtlDTOs = BeanUtils.copyListProperties(bookDtls, FcAccbookDtlDTO.class);
                    for (FcAccbookDtlDTO bookDtlDTO : bookDtlDTOs) {
//							判断账户信息是否存在，不存在则添加账户信息
                        if (null == bookDtlDTO.getAccDTO()) {
                            FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(bookDtlDTO.getAccount());
                            if (StringUtils.isEmpty(fcAccDTO.getSubjectName()) && StringUtils.isNotEmpty(fcAccDTO.getSubjectId())) {
                                fcAccDTO.setSubjectName(fcSubjectService.findSubjectById(fcAccDTO.getSubjectId()).getSubjectName());
                            }
                            // todo:数据转换
                            com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(fcAccDTO, com.bytter.biz.finacial.api.dto.FcAccDTO.class);
                            bookDtlDTO.setAccDTO(accDTO);
                        }
                    }
                    voucherDTO.setBookDtls(bookDtlDTOs);
                }
                ntarget.add(voucherDTO);
            }
            wrapperDTO.setTarget(ntarget);
        }
        //原凭证对象
        List<FcAccbookVoucherVO> oriTarget = voucher.getOriTarget();
        if (null != oriTarget && !oriTarget.isEmpty()) {
            List<FcAccbookVoucherDTO> ntarget = new ArrayList<FcAccbookVoucherDTO>();
            for (FcAccbookVoucherVO voucherVO : oriTarget) {
                FcAccbookVoucherDTO voucherDTO = new FcAccbookVoucherDTO();
                BeanUtils.copyProperties(voucherVO, voucherDTO);
                List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
                if (null != bookDtls && !bookDtls.isEmpty()) {
                    List<FcAccbookDtlDTO> nbookDtlDTOs = BeanUtils.copyListProperties(bookDtls, FcAccbookDtlDTO.class);
                    voucherDTO.setBookDtls(nbookDtlDTOs);
                }
                ntarget.add(voucherDTO);
            }
            wrapperDTO.setOriTarget(ntarget);
        }
        return wrapperDTO;
    }

    /**
     * VoucherWrapperDTO 转换为 VoucherWrapper
     *
     * @param voucher
     * @return
     */
    private VoucherWrapper transVoucher(VoucherWrapperDTO voucher) {
        VoucherWrapper wrapper = new VoucherWrapper();
        BeanUtils.copyProperties(voucher, wrapper);//复制属性
        //凭证对象
        List<FcAccbookVoucherDTO> target = voucher.getTarget();
        if (null != target && !target.isEmpty()) {
            List<FcAccbookVoucherVO> ntarget = new ArrayList<FcAccbookVoucherVO>();
            for (FcAccbookVoucherDTO voucherDTO : target) {
                FcAccbookVoucherVO voucherVO = new FcAccbookVoucherVO();
                BeanUtils.copyProperties(voucherDTO, voucherVO);
                FcAccbookDTO bookDTO = voucherDTO.getAccbook();
                if (null == bookDTO.getInterestStartDate()) {
                    bookDTO.setInterestStartDate(bookDTO.getTallyDate());//起息日期默认记账日期
                }
                if (null != bookDTO) {
                    FcAccbook book = new FcAccbook();
                    BeanUtils.copyProperties(bookDTO, book);
                    voucherVO.setAccbook(book);
                }
                FcAccbookExtDTO bookExtDTO = voucherDTO.getBookExt();
                if (null != bookExtDTO) {
                    FcAccbookExt bookExt = new FcAccbookExt();
                    BeanUtils.copyProperties(bookExtDTO, bookExt);
                    bookExt.setExternalProv(bookExtDTO.getProv());//省市
                    bookExt.setExternalCity(bookExtDTO.getCity());
                    voucherVO.setBookExt(bookExt);
                }
                List<FcAccbookDtlDTO> bookDtls = voucherDTO.getBookDtls();
                if (null != bookDtls && !bookDtls.isEmpty()) {
                    List<FcAccbookDtl> accbookDtls = new ArrayList<FcAccbookDtl>();
                    List<FcAccbookDtl> nbookDtls = BeanUtils.copyListProperties(bookDtls, FcAccbookDtl.class);
                    for (FcAccbookDtl accbookDtl : nbookDtls) {
                        accbookDtls.add(accbookDtl);
                    }
                    voucherVO.setBookDtls(accbookDtls);
                }
                ntarget.add(voucherVO);
            }
            wrapper.setTarget(ntarget);
        }
        //原凭证对象
        List<FcAccbookVoucherDTO> oriTarget = voucher.getOriTarget();
        if (null != oriTarget && !oriTarget.isEmpty()) {
            List<FcAccbookVoucherVO> ntarget = new ArrayList<FcAccbookVoucherVO>();
            for (FcAccbookVoucherDTO voucherDTO : oriTarget) {
                FcAccbookVoucherVO voucherVO = new FcAccbookVoucherVO();
                BeanUtils.copyProperties(voucherDTO, voucherVO);
                List<FcAccbookDtlDTO> bookDtls = voucherDTO.getBookDtls();
                if (null != bookDtls && !bookDtls.isEmpty()) {
                    List<FcAccbookDtl> nbookDtls = BeanUtils.copyListProperties(bookDtls, FcAccbookDtl.class);
                    voucherVO.setBookDtls(nbookDtls);
                }
                ntarget.add(voucherVO);
            }
            wrapper.setOriTarget(ntarget);
        }
        return wrapper;
    }

    /**
     * 根据netId获取最近得一条损益结转记录
     *
     * @param params
     * @return
     */
    public FcCarry getMaxCarryTimeFcCarryByNetId(Map<String, Object> params) {
        List<FcCarry> fcCarries = fcCarryDao.getFcCarryByNetId(params);
        if (fcCarries != null && fcCarries.size() > 0) {
            return fcCarries.get(0);
        }
        return null;
    }

    /**
     * 根据系统凭证种类查询业务规则,
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = iFcBusinessRuleDefineService.getBusinessRuleExp(map);
        if (null == entity) {
            throw new ParameterException(CloseMessageEnum.FINACIAL_HAS_DATA_ALERT_INFO4);
        }
        return entity;
    }

    /**
     * 返回会计分录map，会计分录科目编码为key值
     *
     * @param fcBusinessRuleDefineDTO
     * @return
     */
    public Map<String, FcVoucherEntryDefineDTO> getVoucherEntryDefine(FcBusinessRuleDefineDTO fcBusinessRuleDefineDTO) {
        Map<String, FcVoucherEntryDefineDTO> FcVoucherEntryDefineDTOS = Maps.uniqueIndex(fcBusinessRuleDefineDTO.getVouchers(), new Function<FcVoucherEntryDefineDTO, String>() {
            @Override
            public String apply(FcVoucherEntryDefineDTO fcVoucherEntryDefineDTO) {
                return fcVoucherEntryDefineDTO.getSubjectCode();
            }
        });
        return FcVoucherEntryDefineDTOS;
    }

    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }

    public List<FcCarry> getDayOverFcCarry(String netId, Date date) {
        if (StringUtils.isEmpty(netId) || date == null) {
            return null;
        }
        return fcCarryDao.getFcCarry(netId, CloseConstant.CarryType.CARRY_TYPE_DAY_OVER.getValue(), date);
    }

    public List<FcCarryVO> getFcCarryList(FcCarryVO vo) {
        return fcCarryDao.getFcCarryList(vo);
    }
}
