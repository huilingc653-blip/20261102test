package com.bytter.biz.finacial.interest.service;

import com.bytter.biz.finacial.accbook.service.FcAccbookService;
import com.bytter.biz.finacial.api.constant.*;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.biz.finacial.interest.bean.AccInterestBean;
import com.bytter.biz.finacial.interest.bean.AccInterestInfo;
import com.bytter.biz.finacial.interest.bean.AccTypeInterestInfoBean;
import com.bytter.biz.finacial.interest.dao.FcInterestSingleLoanDao;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.entity.FcInterestSingle;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookDtlVO;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookVO;
import com.bytter.biz.finacial.interest.vo.FcInterestSingleVO;
import com.bytter.biz.finacial.interest.vo.InterestResult;
import com.bytter.biz.finacial.paramconfig.service.FcVoucherCashService;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherCashVo;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.enums.ProvideMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.AccountDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <br>
 * <b>功能：</b> 内部贷款个别计息 Service  <br>
 * <b>作者：</b> chenlw <br>
 * <b>日期：</b> 2020-12-24  <br>
 */
@Service("fcInterestSingleLoanService")
@Transactional
public class FcInterestSingleLoanService extends BaseService<FcInterestSingle, String> {
    @Autowired
    private FcInterestSingleLoanDao fcInterestSingleLoanDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IFcDayCloseService dayCloseService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired(required = false)
    private FcInterestService fcInterestService;
    @Autowired(required = false)
    private FcEntrustLoanRepayService loanRepayService;
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    @DubboReference(check = false)
    private IFcSubjectService fcSubjectService;
    @Autowired
    private FcAccbookService fcAccbookService;
    @Autowired
    private FcVoucherCashService fcVoucherCashService;
    @Autowired(required = false)
    protected IFcVoucherTypeService iFcVoucherTypeService;
    @DubboReference(check = false)
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService iFcOnlineLedgerService;
    @DubboReference(check = false)
    private IFcInterestService interestService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @Override
    public BaseDao<FcInterestSingle, String> getEntityDao() {
        return fcInterestSingleLoanDao;
    }
    public FcInterestSingleVO getEntityById(String id) {
        FcInterestSingleVO vo = fcInterestSingleLoanDao.getFcInterestSingleLoan(id);
        if (Objects.isNull(vo)) return vo;
        com.bytter.system.api.dto.FcAccDTO dto = iFcAccService.getFcAccByCorpIdAndBlnctrAcc(vo.getCorpId(), vo.getAccount());
        vo.setCurId(dto.getCurId());
        vo.setNetId(dto.getNetId());
        vo.setPoolSign(dto.getPoolSign());
        return vo;
    }
    public DataTablesOutput queryFcInterestSingleLoan(DataTablesInput input, ICurrentUser currentUser) {
        String opType = (String) input.getFormdata().get("opType");
        String resource = ResourceConstant.FINACIAL_INTEREST_SINGLE_LOAN_HAND;
        if (EmptyUtils.isNotEmpty(opType) && opType.equals("checkList")) {
            // 复核权限
            resource = ResourceConstant.FINACIAL_INTEREST_SINGLE_LOAN_CHECK;
        }
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, currentUser.getId()), "t.corp_Id", true);
        Object ins_corpId = input.getFormdata().get("ins_corpId");
        String commonSql = null;
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            commonSql = " and " + corpAuthSql;
        } else {
            commonSql = " and 1 <> 1 ";
        }
        DataTablesOutput dataTablesOutput = fcInterestSingleLoanDao.queryFcInterestSingleLoan(input, currentUser.getId(), commonSql);
        this.setValues(dataTablesOutput);
        return dataTablesOutput;
    }
    /**
     * 查询利息明细的记账日期和起息日期
     *
     * @param fcAcc    内部账户对象
     * @param userCode 当前登录人
     * @param opType   initList-经办 check-复核
     * @return
     */
    @Transactional(readOnly = true)
    public List<AccTypeInterestInfoBean> queryCanEditAccTypeInterestInfo(FcAccDTO fcAcc, String userCode, String opType) {
        Integer[] status = new Integer[]{InterestConstant.INTEREST_BACK, InterestConstant.INTEREST_STATUE_TEMP};
        if (EmptyUtils.isNotEmpty(opType) && !"initList".equals(opType)) {
            status = new Integer[]{InterestConstant.INTEREST_WAIT_CHECK};
        }
        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("accTypeCode", fcAcc.getAccType());
        queryParam.put("netId", fcAcc.getNetId());
        List<com.bytter.system.api.dto.FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByBlnctrAccsAndAcctype(Lists.newArrayList(fcAcc.getBlnctrAcc()), queryParam);
        List<String> accounts = fcAccDTOS.stream().map(com.bytter.system.api.dto.FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        List<AccTypeInterestInfoBean> accTypeInterestInfoBeans = fcInterestSingleLoanDao.queryAccTypeInterestInfo(accounts, userCode, status);
        accTypeInterestInfoBeans.stream().peek(item -> item.setAccType(fcAcc.getAccType()));
        return accTypeInterestInfoBeans;
    }
    /**
     * 查询内部账户
     *
     * @param currentUser 当前登录人
     * @param accType     账户类别
     * @return
     */
    public List<AccountDTO> getInterestAccount(ICurrentUser currentUser, String accType, String resource) {
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        try {
            // 获取资源下的单位ID
            List<String> userCorpIds = this.corpService.getUserCorpIds(resource, currentUser.getId());
            StringBuilder joinSql = new StringBuilder();
            joinSql.append(" and t.pool_sign = '0' ");
            if (EmptyUtils.isNotEmpty(accType)) {
                joinSql.append(" and t.acc_type= ").append(accType).append(" ");
            }
            List<String> accStates = new ArrayList<>();
            accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
            List<FcAccDTO> listFcAcc = fcInterestSingleLoanDao.getFcAccByCorpIdList(userCorpIds, accStates, joinSql.toString());
            if (!EmptyUtils.isEmptyList(listFcAcc)) {
                list = copyFcAccDTOList(listFcAcc);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            return null;
        }
        return list;
    }
    /**
     * 将FcAccDTO集合cope到AccountDTO集合
     *
     * @param listFcAcc 内部账户集合
     * @return
     */
    public List<AccountDTO> copyFcAccDTOList(List<FcAccDTO> listFcAcc) {
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        for (FcAccDTO fcAcc : listFcAcc) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setBankAccId(fcAcc.getId());
            accountDTO.setCorpId(fcAcc.getCorpId());
            accountDTO.setCorpName(fcAcc.getCorpName());
            accountDTO.setBankAcc(fcAcc.getBlnctrAcc());
            accountDTO.setAccName(fcAcc.getAccName());
            accountDTO.setBankName(fcAcc.getCorpName());
            accountDTO.setBankCode(fcAcc.getBankCode());
            accountDTO.setCurId(fcAcc.getCurId());
            accountDTO.setCurName(fcAcc.getCurName());
            accountDTO.setFcBalance(fcAcc.getFcBalance());
            list.add(accountDTO);
        }
        return list;
    }
    /**
     * 经办界面根据账户主键查询所属网点日结日期计算出记账日期和止息日期
     *
     * @param accountId   账户ID
     * @param opType      操作类型  initList-经办 checkList-复核
     * @param currentUser 当前登录人
     * @return 记账日期和止息日期
     */
    public Map<String, Object> tallyDateInit(String accountId, String opType, ICurrentUser currentUser) {
        com.bytter.system.api.dto.FcAccDTO fcAcc = iFcAccService.getFcAccDTO(accountId);//根据内部账户id查询内部账户信息
        if (fcAcc == null) {
            return null;
        }
        Date closeDate = dayCloseService.getDayCloseFinalDate(fcAcc.getNetId());//根据内部账户所属网点查询该网点的日结日期
        // todo:数据转换
        FcAccDTO fcAccDTO = BeanUtils.copyNewInstanceProperties(fcAcc, FcAccDTO.class);
        List<AccTypeInterestInfoBean> accTypeInterestInfos = queryCanEditAccTypeInterestInfo(fcAccDTO, currentUser.getId(), opType);
        Map<String, Object> map = tallyDateInit(accTypeInterestInfos, closeDate);
        if (map != null) {
            map.put("netId", fcAcc.getNetId());//网点ID
            map.put("netName", fcAcc.getNetName());//网点名称
            map.put("curId", fcAcc.getCurId());//币别ID
            map.put("curName", fcAcc.getCurName());//币别名称
            map.put("poolSign", InnerAccountConstant.POOL_SIGN_0);// 资金池标识 0-非资金池；1-资金池
            map.put("interestSign", fcAcc.getInterestSign());//计息标志
        }
        return map;
    }
    /**
     * 查询指定账户个别计息谢谢
     *
     * @param account 内部账户
     * @return
     */
    public FcInterestSingleVO queryFcInterestSingle(String account) {
        return fcInterestSingleLoanDao.queryFcInterestSingle(account);
    }
    /**
     * 初始化日期显示
     *
     * @param accTypeInterestInfos 查询利息明细的记账日期和起息日期信息
     * @param closeDate            网点的日结日期
     * @return 记账日期数据
     */
    public Map<String, Object> tallyDateInit(List<AccTypeInterestInfoBean> accTypeInterestInfos, Date closeDate) {
        Date blnInstDate = null;
        Date tallyDate = null;
        boolean isShowBlnInstDate = true;
        Long compareBlnInstDateTime = null;
        if (accTypeInterestInfos != null && accTypeInterestInfos.size() > 0) {
            for (AccTypeInterestInfoBean accTypeInterestInfo : accTypeInterestInfos) {
                Long blnInstDateTime = accTypeInterestInfo.getBlnInstDate().getTime();
                if (compareBlnInstDateTime == null) {
                    compareBlnInstDateTime = blnInstDateTime;
                    blnInstDate = accTypeInterestInfo.getBlnInstDate();
                    tallyDate = accTypeInterestInfo.getTallyDate();
                    continue;
                }
                if (compareBlnInstDateTime.longValue() != blnInstDateTime.longValue()) {
                    isShowBlnInstDate = false;
                    break;
                }
            }
        }
        if (!isShowBlnInstDate) {
            blnInstDate = null;
            tallyDate = null;
        }
        // 判断止息日期是否为空，若为空则网点日结日期延后一天
        if (blnInstDate != null) {
            blnInstDate = DateUtils.addDay(blnInstDate, 1);
        } else {
            blnInstDate = DateUtils.addDay(closeDate, 1);
        }
        // 判断记账日期是否为空，若为空则网点日结日期延后一天
        if (tallyDate == null) {
            tallyDate = DateUtils.addDay(closeDate, 1);
        }
        Map<String, Object> dataInfo = new HashMap<>();
        dataInfo.put("blnInstDate", blnInstDate);
        dataInfo.put("tallyDate", tallyDate);
        return dataInfo;
    }
    /**
    * <br>
    * <b>功能：</b> 查询积数清单数据 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/26 0026 <br>
    */
    public Map<String, Object> queryCanEditAccuBalance(DataTablesInput pgd, FcInterestSingleVO entity, String opType, String sessionId) {
        if (CmsConstant.REPAY_TYPE_1.equals(entity.getPayWay())) {
            return new HashMap<>();
        }
        if (EmptyUtils.isNotEmpty(entity.getId())) {
            entity = getEntityById(entity.getId());
        }
        BigDecimal money = entity.getMoney();
        Map<String, Object> result = fcInterestService.queryCanEditAccuBalance(pgd, entity.getNetId(), entity.getAccType(), opType,
                sessionId, entity.getAccount(), InterestConstant.GENERATEMODE_INTEREST_PART);
        if (CmsConstant.REPAY_TYPE_2.equals(entity.getPayWay())) {
            List<Map> accuBalances = (List) result.get("rows");
            //与账号对比
            BigDecimal totACumu = BigDecimal.ZERO;
            BigDecimal totAAdjustCumu = BigDecimal.ZERO;
            for(Map accuBalanceInfo : accuBalances) {
                String account = (String) accuBalanceInfo.get("ACCOUNT");//账号
                if ("AllTotal".equals(account) || "total".equals(account)) {
                    continue;
                }

                BigDecimal cumu =  NumberUtil.objToBigDecimal(accuBalanceInfo.get("CUMU")) ;//积数
                BigDecimal adjustCumu = NumberUtil.objToBigDecimal(accuBalanceInfo.get("ADJUSTCUMU"));//调整积数
                if (null == cumu) {
                    cumu = BigDecimal.ZERO;
                }
                if (null == adjustCumu) {
                    adjustCumu = BigDecimal.ZERO;
                }
                if (cumu.compareTo(money) >= 0) {
                    BigDecimal newCumu = money;
                    BigDecimal newAdjustCumu = BigDecimal.ZERO;
                    if (null != adjustCumu) {
                        if (cumu.compareTo(BigDecimal.ZERO) != 0) {
                            newAdjustCumu = (adjustCumu.multiply(money)).divide(cumu, 2);
                        } else {
                            newAdjustCumu = adjustCumu;
                        }
                    }
                    cumu = money;
                    adjustCumu = newAdjustCumu;
                    accuBalanceInfo.put("CUMU", newCumu);
                    accuBalanceInfo.put("ADJUSTCUMU", newAdjustCumu);
                }
                totACumu = totACumu.add(cumu);
                totAAdjustCumu = totAAdjustCumu.add(adjustCumu);
            }
            for(Map accuBalanceInfo : accuBalances) {
                String account = (String) accuBalanceInfo.get("ACCOUNT");//账号
                if ("AllTotal".equals(account) || "total".equals(account)) {
                    accuBalanceInfo.put("CUMU", totACumu);
                    accuBalanceInfo.put("ADJUSTCUMU", totAAdjustCumu);
                }
            }
            List<Map> footerList = (List) result.get("footer");
            for(Map accuBalanceInfo : footerList) {
                String account = (String) accuBalanceInfo.get("ACCOUNT");//账号
                if ("AllTotal".equals(account) || "total".equals(account)) {
                    accuBalanceInfo.put("CUMU", totACumu);
                    accuBalanceInfo.put("ADJUSTCUMU", totAAdjustCumu);
                }
            }
        }
        return result;
    }
    /**
    * <br>
    * <b>功能：</b> 处理个别计息 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/23 0023 <br>
    */
    protected boolean proInterestaccrualBySingle(FcInterestSingleVO entity,
                                                ICurrentUser currentUser,
                                                OperateType opType,
                                                boolean poolFlag,
                                                FcInterestSingleVO fcInterestSingle) {
        //恢复拆分的计息积数
        loanRepayService.reduceAccuBalance(entity.getAccount(), entity.getId());
        //判断还款方式
        String payWay = entity.getPayWay();
        if (!"4".equals(payWay)) {//需还本金
            if (EmptyUtils.isEmpty(entity.getMoney())) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANREPAY_REPAYMONEYEMPTY);//不满足条件
            }
        } else {
        }
        if (null != fcInterestSingle && EmptyUtils.isNotEmpty(fcInterestSingle.getBusGlobalId())) {
            //删除凭证
            deleteInterestVoucher(fcInterestSingle.getBusGlobalId(), currentUser);
        }
        if (opType == OperateType.SUBMIT) {
            processSubmitInterestVoucher(entity, currentUser);//生成凭证
        }
        BigDecimal payMoney = entity.getMoney();//还款方式为还本及所有利息或还所有利息
        if (CmsConstant.REPAY_TYPE_3.equals(entity.getPayWay()) || CmsConstant.REPAY_TYPE_4.equals(entity.getPayWay())){
            payMoney = null;
        }
        InterestResult interestResult = new InterestResult();
        if (!CmsConstant.REPAY_TYPE_1.equals(entity.getPayWay())) {
            interestResult = fcInterestService.interestLoanSingle(entity.getNetId(), entity.getAccount(), entity.getAccType(),
                    entity.getTallyDate(), entity.getEndDate(), currentUser, opType, payMoney, entity.getId());
        } else {
            //删除利息数据
            if (EmptyUtils.isNotEmpty(entity.getId())) {
                fcInterestService.deleteLoanSignleInterest(entity.getId());
            }
        }
        boolean itsSuccess = saveOrUpdateFcInterestSingleEntity(entity, interestResult, currentUser);
        if (opType == OperateType.SUBMIT) {
            //processAutoCheckSingle(entity, currentUser, OperateType.CHECK);//判断是否自动复核
        }
        // 保存 / 修改个别计息数据
        return itsSuccess;
    }
    /**
     * <br>
     * <b>功能：</b> 处理自动复核 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/29 0029 <br>
     */
    public void processAutoCheckSingle(FcInterestSingleVO entity, ICurrentUser user, OperateType opType) {
        Boolean autoCheck = fcInterestService.isBatchInstAutoCheck();//自动复核参数
        if (autoCheck) {
            checkSingleInterest(entity, opType, user, InterestConstant.INTEREST_STATUE_CHECK);
        }
    }
    /**
     * 个别计息与提交
     *
     * @param entity      个别计息请求参数对象
     * @param currentUser 当前登录人
     */
    public AjaxJson doInterestaccrualBySingle(FcInterestSingleVO entity, ICurrentUser currentUser) {
        AjaxJson json = new AjaxJson();
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(entity.getPoolSign());
        OperateType opType = null;
        if ("interest".equals(entity.getOpType())) {
            opType = OperateType.INTEREST;
            entity.setStatus(InterestConstant.INTEREST_STATUE_TEMP);
        } else if ("submit".equals(entity.getOpType())) {
            opType = OperateType.SUBMIT;
            entity.setStatus(InterestConstant.INTEREST_WAIT_CHECK);
        }
        boolean excute = true;//执行标志 true-执行 false-停止
        FcInterestSingleVO fcInterestSingle = queryFcInterestSingle(entity.getAccount());
        String itsSuccess = "";
        //界面标志pageFlag 0-新增界面 1-修改界面 默认空也是新增界面
        if (EmptyUtils.isEmpty(entity.getPageFlag())) {
            entity.setPageFlag(0);
        }
        if (entity.getPageFlag() == 0) {
            //判断是否存在个别计息数据，不存在则计息，存在则提示
            if (fcInterestSingle != null) {
                if (EmptyUtils.isNotEmpty(entity.getId())) {
                    if (entity.getId().equals(fcInterestSingle.getId())) {
                        excute = true;
                    } else {
                        excute = false;
                    }
                } else {
                    excute = false;
                }
                itsSuccess = multiLangServiceI.getLang("finacial.interest.data.exist.account");//该账户已存在个别计息数据！
            }
        }
        if (excute) {
            excute = proInterestaccrualBySingle(entity, currentUser, opType, poolFlag, fcInterestSingle);
            if (excute && opType == OperateType.INTEREST) {
                itsSuccess = multiLangServiceI.getLang("finacial.interest.interest.success");
            } else if (excute && opType == OperateType.SUBMIT) {
                itsSuccess = multiLangServiceI.getLang("common.message.submit.success");
            }
        }
        json.setParams(new String[]{entity.getId()});
        json.setSuccess(excute);
        json.setObj(itsSuccess);
        json.setMsg(itsSuccess);
        return json;
    }
    /**
     * 保存或修改个别计息
     *
     * @param entityVo       个别计息对象
     * @param interestResult 计息结果对象
     * @param currentUser    当前登录人
     * @return 操作是否成功
     */
    public boolean saveOrUpdateFcInterestSingleEntity(FcInterestSingleVO entityVo,
                                                      InterestResult interestResult,
                                                      ICurrentUser currentUser) {
        if (CmsConstant.REPAY_TYPE_4.equals(entityVo.getPayWay()) && (interestResult == null
                || interestResult.getAccInterestInfo() == null
                || interestResult.getAccInterestInfo().getInterestDtls() == null)) {
            logger.info("本计息周期内没有可以计息的数据，不能计息!");
            // 不满足记账凭证条件
            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_INTERESTNODATA);
        }
        Date startDate = entityVo.getStartDate();
        Date endDate = entityVo.getEndDate();
        Date interestTime = DateUtils.getDate();
        BigDecimal interest = new BigDecimal(0.00);
        List<FcInterestDtlTemp> list = new ArrayList<>();
        if (!CmsConstant.REPAY_TYPE_1.equals(entityVo.getPayWay())) {
            AccInterestInfo accInterestInfo = interestResult.getAccInterestInfo();
            if (!EmptyUtils.isEmpty(accInterestInfo)) {
                list = interestResult.getAccInterestInfo().getInterestDtls();
                if (EmptyUtils.isEmpty(list)) {
                    list = new ArrayList<FcInterestDtlTemp>();
                }
                List<Date> startDateList = new ArrayList<>();
                List<Date> endDateList = new ArrayList<>();
                for (FcInterestDtlTemp entity : list) {
                    interest = interest.add(entity.getInterest());
                    startDateList.add(entity.getInterestStartDate());
                    endDateList.add(entity.getBlnInstDate());
                    interestTime = entity.getInterestTime();
                }
                if (!EmptyUtils.isEmptyList(list)) {
                    startDate = Collections.min(startDateList);
                    endDate = Collections.max(endDateList);
                }
            }
        }
        if (EmptyUtils.isEmpty(startDate)) {
            com.bytter.system.api.dto.FcAccDTO accDTO = iFcAccService.getAccDTOByAccount(entityVo.getAccount());
            startDate = accDTO.getInterestStartDate();
        }
        FcInterestSingle fis = new FcInterestSingle();
        if (EmptyUtils.isNotEmpty(entityVo.getId())) {
            fis = get(entityVo.getId());
        }
        fis.setCorpId(entityVo.getCorpId());
        fis.setInterestTime(interestTime);
        fis.setAccount(entityVo.getAccount());
        fis.setAccName(entityVo.getAccName());
        fis.setCurId(entityVo.getCurId());
        fis.setNetId(entityVo.getNetId());
        fis.setPoolSign(entityVo.getPoolSign());
        fis.setAccType(entityVo.getAccType());
        fis.setStartDate(startDate);
        fis.setEndDate(endDate);
        fis.setMoney(entityVo.getMoney());
        fis.setInterest(interest);
        fis.setTallyDate(entityVo.getTallyDate());
        fis.setOrderNo(entityVo.getOrderNo());
        fis.setBusGlobalId(entityVo.getBusGlobalId());
        fis.setPayWay(entityVo.getPayWay());
        fis.setStatus(entityVo.getStatus());
        fis.setAffixAmount(entityVo.getAffixAmount());
        // 界面标志 0-新增页面 1-修改页面 2-复核页面 3-查看页面
        switch (entityVo.getPageFlag()) {
            case 0:
                fis.setCreateBy(currentUser.getId());
                fis.setCreateDate(new Date());
                break;
            case 1:
                fis.setUpdateBy(currentUser.getId());
                fis.setUpdateDate(new Date());
                break;
            case 2:
                fis.setCheckerCode(currentUser.getId());
                fis.setCheckDate(new Date());
                break;
            case 3:
                break;
            default:
                break;
        }
        saveOrUpdate(fis);
        getEntityDao().flush();
        String singleId = fis.getId();
        if (!CmsConstant.REPAY_TYPE_1.equals(entityVo.getPayWay())) {
            for (FcInterestDtlTemp entity : list) {
                entity.setSingleId(fis.getId());
                fcInterestService.updateInterestSingle(entity.getId(), singleId);
            }
        }
        entityVo.setId(singleId);
        return true;
    }
    /**
     * 删除个别计息
     *
     * @param id          主键
     * @param account     账户
     * @param netCode     网点ID
     * @param accTypeCode 账户类型
     * @param user        当前登录人
     * @return
     */
    public AjaxJson deleteInterestSingle(String id, String account, String netCode, String accTypeCode, ICurrentUser user) throws Exception {
        AjaxJson json = new AjaxJson();
        //fcInterestSingleDdaDao.delete(id);
        FcInterestSingle entity = get(id);
        if (StringUtils.isNotEmpty(accTypeCode) && accTypeCode.startsWith(",")) {
            accTypeCode = accTypeCode.substring(accTypeCode.indexOf(",") + 1);
        }
        fcInterestService.doDeleteByBatch(netCode, accTypeCode, user.getId(), account, InterestConstant.GENERATEMODE_INTEREST_PART);
        //删除凭证
        deleteInterestVoucher(entity.getBusGlobalId(), user);
        fcInterestSingleLoanDao.deleteInterestSingle(entity, InterestConstant.INTEREST_STATUE_DELETE);
        json.setSuccess(true);
        return json;
    }
    /**
     * <br>
     * <b>功能：</b> 删除个别计息凭证 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/20 0020 <br>
     */
    public void deleteInterestVoucher(String busGlobalId, ICurrentUser user) {
        //删除凭证
        if (EmptyUtils.isNotEmpty(busGlobalId)) {
            VoucherWrapperDTO voucher = new VoucherWrapperDTO();
            voucher.setUserCode(user.getId());
            voucher.setUser(user);
            voucher.setBusGlobalId(busGlobalId);//全局业务ID
            iFcAccbookService.deleteVoucher(voucher);
        }
    }
    /**
     * <br>
     * <b>功能：</b> 提交处理账务 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/19 0019 <br>
     */
    public void processSubmitInterestVoucher(FcInterestSingleVO entity, ICurrentUser user) {
        FcAccbookVoucherDTO voucherDTO = createFcAccbook(entity, "initList", user);
        //提交凭证
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.addTarget(voucherDTO);
        voucher.setUserCode(user.getId());
        voucher.setUser(user);
        FcAccbookDTO accbook = iFcAccbookService.submitFcAccbook(voucher);//提交凭证
        //因为不同域了，所以不能这样写
//        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        entity.setOrderNo(accbook.getOrderNo());//凭证流水号
        entity.setBusGlobalId(accbook.getBusGlobalId());//全局ID
    }
    /**
     * 企业定期-个别计息复核/打回
     *
     * @param fisList     待处理数据
     * @param currentUser 当前登录人
     * @return
     */
    /*public AjaxJson doProcessInterestCheck(List<FcInterestSingleVO> fisList, ICurrentUser currentUser) {
        AjaxJson json = new AjaxJson();
        if (fisList == null || fisList.size() == 0) {
            json.setSuccess(false);
            json.setMsg(multiLangServiceI.getLang("common.message.confirm.selectData"));
            return json;
        }
        Integer status = null;
        OperateType opType = null;
        if ("check".equals(fisList.get(0).getOpType())) {
            opType = OperateType.CHECK;
            status = InterestConstant.INTEREST_STATUE_CHECK;
        } else if ("back".equals(fisList.get(0).getOpType())) {
            opType = OperateType.BACK;
            status = InterestConstant.INTEREST_BACK;
        }
        List<String> ids = new ArrayList<>();
        for (FcInterestSingleVO entity : fisList) {
            ids.add(entity.getId());
            boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(entity.getPoolSign());
            entity = getEntityById(entity.getId());
            AccInterestBean accInterestBean = new AccInterestBean(entity.getNetId(), entity.getAccType(), entity.getTallyDate(),
                    DateUtils.addDay(entity.getEndDate(), 1), currentUser, opType, entity.getAccount(), poolFlag,
                    InterestConstant.GENERATEMODE_INTEREST_PART, entity.getAffixAmount(), FinaBookConstant.BusOriType.IST_INTEREST_FIX.get());
            processCheckInterestVoucher(entity, currentUser, opType);
            fcInterestService.processSingleInterestCheck(accInterestBean);
            entity.setStatus(status);
            if (OperateType.CHECK.equals(opType)) {
                entity.setCheckDate(new Date());
                entity.setCheckerCode(currentUser.getId());
            }
            json.setSuccess(true);
        }
        if (json.isSuccess()) {
            updateFcInterestSingleById(ids, currentUser, status);
        }
        return json;
    }*/
    /**
     * <br>
     * <b>功能：</b> 个别计息复核 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/29 0029 <br>
     */
    public void checkSingleInterest(FcInterestSingleVO entity, OperateType opType, ICurrentUser currentUser, Integer status) {
        List<String> ids = new ArrayList<>();
        ids.add(entity.getId());
        boolean poolFlag = false;
        entity = getEntityById(entity.getId());
        AccInterestBean accInterestBean = new AccInterestBean(entity.getNetId(), entity.getAccType(), entity.getTallyDate(),
                DateUtils.addDay(entity.getEndDate(), 1), currentUser, opType, entity.getAccount(), poolFlag,
                InterestConstant.GENERATEMODE_INTEREST_PART, entity.getAffixAmount(), FinaBookConstant.BusOriType.IST_INTEREST_LOAN.get());
        processCheckInterestVoucher(entity, currentUser, opType);
        fcInterestService.processSingleInterestCheck(accInterestBean);
        entity.setStatus(status);
        if (OperateType.CHECK.equals(opType)) {
            entity.setCheckDate(new Date());
            entity.setCheckerCode(currentUser.getId());
        }
        updateFcInterestSingleById(ids, currentUser, status);
    }
    /**
     * <br>
     * <b>功能：</b> 复核个别计息还款凭证 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/23 0023 <br>
     */
    public void processCheckInterestVoucher(FcInterestSingleVO entity, ICurrentUser user, OperateType opType) {
        FcAccbookVoucherDTO voucherDTO = createFcAccbook(entity, NbconsoleConstant.OpType.CHECK.get(), user);
        //提交凭证
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.addTarget(voucherDTO);
        voucher.setUserCode(user.getId());
        voucher.setUser(user);
        voucher.setBusGlobalId(entity.getBusGlobalId());
        if (OperateType.CHECK.equals(opType)) {
            FcOnlineLedgerDTO onlineLedgerDTO = iFcOnlineLedgerService.getByBlnctrAcc(entity.getAccount());//账户余额
            BigDecimal checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
            iFcAccbookService.checkFcAccbook(voucher);//复核凭证
            //处理冲预提
            Date interestTime = DateUtils.getDate();
            if (checkBalance.compareTo(entity.getMoney()) > 0) {//部分取款
                fcInterestService.interestRushPredraw(entity.getAccount(), user, entity.getId(), entity.getMoney(), interestTime);
            } else {
                fcInterestService.interestRushPredraw(entity.getAccount(), user, entity.getId(), null, interestTime);
            }
        } else {
            iFcAccbookService.backFcAccbook(voucher);//打回凭证
        }
    }
    /**
     * <br>
     * <b>功能：</b> 构建计息凭证传输对象 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/2/4 0004 <br>
     */
    public FcAccbookVoucherDTO createFcAccbook(FcInterestSingleVO entity, String opType, ICurrentUser user) {
        FcAccbookVoucherDTO accbook = new FcAccbookVoucherDTO();
        FcAccbookDTO book = new FcAccbookDTO();
        List<FcAccbookDtlDTO> bookDtls = null;
        if (EmptyUtils.isEmpty(entity)) {
            return null;
        }
        FcInterestSingle single = get(entity.getId());
        Date endDate = DateUtils.addDay(single.getEndDate(), 1);
        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal accruedMoney = BigDecimal.ZERO;//冲预提金额
        if (!EmptyUtils.isEmpty(entity) && !NbconsoleConstant.OpType.CHECK.get().equals(opType)) {
            String orderNo = "";
            if (EmptyUtils.isNotEmpty(entity.getId())) {
                orderNo = entity.getOrderNo();
            }
            if (CmsConstant.REPAY_TYPE_1.equals(entity.getPayWay())) {
            } else if (CmsConstant.REPAY_TYPE_2.equals(entity.getPayWay())) {//还本及本金对应的利息
                FcOnlineLedgerDTO onlineLedgerDTO = iFcOnlineLedgerService.getByBlnctrAcc(entity.getAccount());//账户余额
                BigDecimal checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
                if (checkBalance.compareTo(entity.getMoney()) > 0) {//部分还款
                    interest = interestService.calTellerMoneyInterest(entity.getAccount(), entity.getMoney(), orderNo, endDate, user);
                    accruedMoney = interestService.getRushPreDrawMoney(entity.getAccount(), entity.getMoney(), entity.getId());//冲预提金额
                } else {//全部还款
                    interest = interestService.calTellerMoneyInterest(entity.getAccount(), null, orderNo, endDate, user);
                    accruedMoney = interestService.getRushPreDrawMoney(entity.getAccount(), null, entity.getId());//冲预提金额
                }
            } else {//还本及所有的利息、还所有的利息
                interest = interestService.calTellerMoneyInterest(entity.getAccount(), null, orderNo, endDate, user);
                accruedMoney = interestService.getRushPreDrawMoney(entity.getAccount(), null, entity.getId());//冲预提金额
            }
            entity.setInterest(interest);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", getSysRaceCode().get());//个别计息贷款
        FcBusinessRuleDefineDTO ruleDefine = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        if (EmptyUtils.isNotEmpty(opType) && "initList".equals(opType)
                && !InterestConstant.INTEREST_BACK.equals(entity.getStatus())) {//记账
            accbook.setNetCode(innerAccountService.getByBlnctrAcc(entity.getAccount()).getNetId());
            bookDtls = getAccbookDtls(entity, ruleDefine, accruedMoney, user.getId());
            accbook.setBookDtls(bookDtls);
            book.setBusOriType(getOriType());
            String raceId = iFcBusRaceService.getNetBusRace(getSysRaceCode().get(), accbook.getNetCode(), user.getId());
            book.setBusRaceId(raceId);
            book.setBusRaceName(getSysRaceCode().getName());
            book.setTallyDate(entity.getTallyDate());
            book.setInterestStartDate(entity.getTallyDate());
            book.setVoucherType(ruleDefine.getVoucherType());
            book.setAffixAmount(entity.getAffixAmount());//附件张数
            book.setEmergencySign(FinaBookConstant.EmergencySign.EMERGENCY_SIGN_GENERAL.get());
            accbook.setAccbook(book);
        } else {
            String BusGlobalId = entity.getBusGlobalId();
            accbook = iFcAccbookService.getVoucher(getOriType(), BusGlobalId).getBookVoucher();
            if (DateUtils.dayDiffs(entity.getTallyDate(), accbook.getAccbook().getTallyDate()) != 0) {
                bookDtls = getAccbookDtls(entity, ruleDefine, accruedMoney, user.getId());
                accbook.setBookDtls(bookDtls);
            }
        }
        return accbook;
    }
    /**
    * <br>
    * <b>功能：</b> 根据参数获取利息类科目账户 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/27 0027 <br>
    */
    public FcAccDTO queryCorpSubjectAcc(String subjectId, String netId, String corpId, String curId, String userId) {
        FcSubjectDTO subjectDTO = fcSubjectService.findSubjectById(subjectId);
        if (null == subjectDTO) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_SUBJECT_NULL);
        }
        List<com.bytter.biz.finacial.api.dto.FcAccDTO> list = fcAccbookService.getInterestAcc(curId, netId, corpId,
                subjectId, "", "", userId, subjectDTO.getName());
        com.bytter.biz.finacial.api.dto.FcAccDTO dto = list.get(0);
        return dto;
    }
    /**
    * <br>
    * <b>功能：</b> 查询单位企业活期账号 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/27 0027 <br>
    */
    public FcAccDTO queryCorpFcAcc(String subjectId, String netId, String corpId, String curId){
        FcSubjectDTO subjectDTO = fcSubjectService.findSubjectById(subjectId);
        if (null == subjectDTO) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_SUBJECT_NULL);
        }
        List<com.bytter.system.api.dto.FcAccDTO> dtos = null;
        Set<String> corpIds = new HashSet<>();
        corpIds.add(corpId);
        String msgCorp = "";
        dtos = iFcAccService.getFcAccBySysSubjectIdFilterBankCode(subjectId,null, netId, corpIds, curId) ;
        if(dtos.size() < 1) {
            msgCorp = corpService.get(netId).getName();
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ACCOUNT_ISNULL, msgCorp, subjectDTO.getName());
        }
        com.bytter.system.api.dto.FcAccDTO dto = dtos.get(0);
        // todo:数据转换
        return BeanUtils.copyNewInstanceProperties(dto, FcAccDTO.class);
    }
    /**
     * <br>
     * <b>功能：</b> 生成账务明细 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/2/4 0004 <br>
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(FcInterestSingleVO entity, FcBusinessRuleDefineDTO ruleDefine, BigDecimal accruedMoney, String userId) {
        FcAccbookDtlDTO accbookDtl = null;
        String account = entity.getAccount();//计息账号
        com.bytter.system.api.dto.FcAccDTO fcAccDTO = innerAccountService.getByBlnctrAcc(account);
        BigDecimal money = entity.getMoney();
        if (CmsConstant.REPAY_TYPE_4.equals(entity.getPayWay())) {
            money = BigDecimal.ZERO;
        }
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        BigDecimal rushMoney = BigDecimal.ZERO;
        //摘要
        String accTypeRmk = multiLangServiceI.getLang("table.fcacc.accType03");
        String interestRmk = multiLangServiceI.getLang("finacial.interest.single");
        String rushRmk = multiLangServiceI.getLang("table.interet.predrawRush");
        for (FcVoucherEntryDefineDTO define : ruleDefine.getVouchers()) {
            /**
             *  借：企业活期存款               其他
             *    贷：内部贷款                 其他
             *       利息收入-内部贷款          利息
             * 借：应收利息-内部贷款           冲预提
             *     贷：利息收入-内部贷款       冲预提
             */
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            com.bytter.system.api.dto.FcAccDTO acc = null;
            if (define.getEntryType().intValue() != FinacialConstant.ENTRY_TYPE_30) {//非冲预提
                if (define.getEntryType().intValue() == FinacialConstant.ENTRY_TYPE_40) {//结息 利息收入-内部贷款
                    if (entity.getInterest().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestInExp())) {//利息收入
                        acc = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                        // todo:数据转换
                        FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                        accbookDtl.setAccDTO(accDTO);
                    } else {
                        FcAccDTO dto = queryCorpSubjectAcc(define.getSubjectCode(), fcAccDTO.getNetId(), fcAccDTO.getCorpId(), fcAccDTO.getCurId(), userId);
                        accbookDtl.setAccDTO(dto);
                        accbookDtl.setBlnctrAcc(dto.getBlnctrAcc());
                    }
                    accbookDtl.setCreditMoney(entity.getInterest());
                } else {
                    if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).equals(define.getBalanceWay())) {//其他-借 企业活期存款
                        //取贷款账户对应的扣息活期账户
                        String accForDeduct = fcAccDTO.getAccForDeduct();
                        if (EmptyUtils.isNotEmpty(accForDeduct)) {
                            com.bytter.system.api.dto.FcAccDTO deductDto = innerAccountService.getByBlnctrAcc(accForDeduct);
                            if (EmptyUtils.isEmpty(deductDto) || EmptyUtils.isEmpty(deductDto.getBlnctrAcc())) {
                                //查询本单位企业活期存款账户
                                FcAccDTO dto = queryCorpFcAcc(define.getSubjectCode(), fcAccDTO.getNetId(), fcAccDTO.getCorpId(), fcAccDTO.getCurId());
                                accbookDtl.setAccDTO(dto);
                            } else {
                                // todo:数据转换
                                FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(deductDto, FcAccDTO.class);
                                accbookDtl.setAccDTO(accDTO);
                            }
                        } else {
                            //查询本单位企业活期存款账户
                            FcAccDTO dto = queryCorpFcAcc(define.getSubjectCode(), fcAccDTO.getNetId(), fcAccDTO.getCorpId(), fcAccDTO.getCurId());
                            accbookDtl.setAccDTO(dto);
                        }
                        accbookDtl.setDebitMoney(entity.getInterest().add(money));
                    } else {//其他-贷 内部贷款
                        // todo:数据转换
                        FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(fcAccDTO, FcAccDTO.class);
                        accbookDtl.setAccDTO(accDTO);
                        accbookDtl.setBlnctrAcc(account);
                        accbookDtl.setCreditMoney(money);
                    }
                }
                accbookDtl.setBlnctrAcc(accbookDtl.getAccDTO().getBlnctrAcc());
                accbookDtl.setVoucherType(define.getVoucherType());
                accbookDtl.setSummaryContent(accTypeRmk + interestRmk + "-" + account);//计息摘要
            } else if (define.getEntryType().intValue() == FinacialConstant.ENTRY_TYPE_30) {//冲预提
                if (accruedMoney.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                if (define.getBalanceWay().intValue() == Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue()) {//借 应收利息-内部贷款
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestRecPay())) {//利息支出
                        acc = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestRecPay());
                        // todo:数据转换
                        FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                        accbookDtl.setAccDTO(accDTO);
                    } else {
                        FcAccDTO dto = queryCorpSubjectAcc(define.getSubjectCode(), fcAccDTO.getNetId(), fcAccDTO.getCorpId(), fcAccDTO.getCurId(), userId);
                        accbookDtl.setAccDTO(dto);
                    }
                    accbookDtl.setDebitMoney(BigDecimal.ZERO.subtract(accruedMoney));
                } else {//贷  利息收入-内部贷款
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestInExp())) {//利息收入
                        acc = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                        // todo:数据转换
                        FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                        accbookDtl.setAccDTO(accDTO);
                    } else {
                        FcAccDTO dto = queryCorpSubjectAcc(define.getSubjectCode(), fcAccDTO.getNetId(), fcAccDTO.getCorpId(), fcAccDTO.getCurId(), userId);
                        accbookDtl.setAccDTO(dto);
                    }
                    accbookDtl.setCreditMoney(BigDecimal.ZERO.subtract(accruedMoney));
                }
                accbookDtl.setVoucherType(define.getVoucherType());
                accbookDtl.setSummaryContent(rushRmk);
            }
            if (!EmptyUtils.isEmpty(accbookDtl.getAccDTO()) && EmptyUtils.isNotEmpty(accbookDtl.getAccDTO().getBlnctrAcc())) {
                com.bytter.system.api.dto.FcAccDTO fcAcc = innerAccountService.getByBlnctrAcc(accbookDtl.getAccDTO().getBlnctrAcc());
                if (!EmptyUtils.isEmpty(fcAcc)) {
                    // todo:数据转换
                    FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(fcAcc, FcAccDTO.class);
                    accbookDtl.setAccDTO(accDTO);
                    accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcAcc.getAccName());
                }
            }
            accbookDtl.setOrigVoucherId(entity.getId());
            accbookDtl.setRushMoney(rushMoney);
            FcVoucherCashVo fcVoucherCashVo;
//			设置现金流项目
            if (accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0 || accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) < 0) {
                fcVoucherCashVo = fcVoucherCashService.getItemBySubject(accbookDtl.getAccDTO().getSubjectId(), null);
                if (null != fcVoucherCashVo) {
                    accbookDtl.setCashFlux(fcVoucherCashVo.getCreditItem());
                }
            } else if (accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) > 0 || accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) < 0) {
                fcVoucherCashVo = fcVoucherCashService.getItemBySubject(null, accbookDtl.getAccDTO().getSubjectId());
                if (null != fcVoucherCashVo) {
                    accbookDtl.setCashFlux(fcVoucherCashVo.getDebitItem());
                }
            }
            if (accbookDtl.getCreditMoney().doubleValue() != 0D || accbookDtl.getDebitMoney().doubleValue() != 0D) {
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
    }
    /**
     * <br>
     * <b>功能：</b> 个别计息定期 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/2/4 0004 <br>
     */
    public ConstantBean<String> getSysRaceCode() {
        return FinaBookConstant.SysRaceCode.IST_INTEREST_LOAN;
    }
    public Integer getOriType() {
        return FinaBookConstant.BusOriType.IST_INTEREST_LOAN.get();
    }// 这里一个入口
    public Map<String, Object> createInterestVoucher(FcInterestSingleVO entity, String opType, ICurrentUser user) {
        entity = getEntityById(entity.getId());
        Map<String, Object> result = new HashMap<String, Object>();
        List<FcFinacialAccbookVO> accbook = new ArrayList<FcFinacialAccbookVO>();
        FcFinacialAccbookVO accbookVO = new FcFinacialAccbookVO();
        accbook.add(accbookVO);
        FcAccbookVoucherDTO voucherDTO = createFcAccbook(entity, opType, user);// 全局id
        if (null == voucherDTO) {
            return result;
        }
        FcAccbookDTO book = voucherDTO.getAccbook();
        if (null == book.getBusRaceName()) {
            FcBusRaceDTO raceDTO = iFcBusRaceService.findFcBusRaceById(book.getBusRaceId());
            if (null != raceDTO) {
                book.setBusRaceName(raceDTO.getRaceName());
            }
        }
        BeanUtils.copyProperties(book, accbookVO);
        List<FcFinacialAccbookDtlVO> fcAccbookDtls = new ArrayList<>();
        for (FcAccbookDtlDTO dtlDTO : voucherDTO.getBookDtls()) {
            FcFinacialAccbookDtlVO dtlVO = new FcFinacialAccbookDtlVO();
            BeanUtils.copyProperties(dtlDTO, dtlVO);
            if (null == dtlDTO.getAccDTO()) {
                // todo:数据转换
                com.bytter.system.api.dto.FcAccDTO accDTOByAccount = iFcAccService.getAccDTOByAccount(dtlDTO.getAccount());
                FcAccDTO fcAccDTO = BeanUtils.copyNewInstanceProperties(accDTOByAccount, FcAccDTO.class);
                dtlDTO.setAccDTO(fcAccDTO);
            }
            dtlVO.setFcAcc(dtlDTO.getAccDTO());
            fcAccbookDtls.add(dtlVO);
        }
        accbookVO.setFcAccbookDtls(fcAccbookDtls);
        BigDecimal accbookTot = BigDecimal.ZERO;
        BigDecimal accbookCreditTot = BigDecimal.ZERO;
        for (FcFinacialAccbookVO fcFinacialAccbook : accbook) {
            accbookTot = BigDecimal.ZERO;
            accbookCreditTot = BigDecimal.ZERO;
            fcFinacialAccbook.setTallyDate(entity.getTallyDate());
            fcFinacialAccbook.setInterestStartDate(entity.getTallyDate());
            List<FcFinacialAccbookDtlVO> accbookDtls = fcFinacialAccbook.getFcAccbookDtls();
            for (FcFinacialAccbookDtlVO dtl : accbookDtls) {
                accbookCreditTot = accbookCreditTot.add(dtl.getCreditMoney());
                accbookTot = accbookTot.add(dtl.getDebitMoney());
            }
            //设置汇总信息
            fcFinacialAccbook.setCreditSum(accbookCreditTot);
            fcFinacialAccbook.setDebitSum(accbookTot);
        }
        result.put("voucherInfo", accbook);
        result.put("voucherTypeName", iFcVoucherTypeService.getFcVoucherTypeById(accbook.get(0).getVoucherType()).getVoucherName());
        return result;
    }
    /**
     * 更新个别利息表
     *
     * @param ids
     * @param currentUser
     * @param status
     */
    public void updateFcInterestSingleById(List<String> ids, ICurrentUser currentUser, Integer status) {
        fcInterestSingleLoanDao.updateFcInterestSingleById(ids, currentUser.getId(), status);
    }

    public void setValues(DataTablesOutput dataTablesOutput){
        List<FcInterestSingleVO> list = dataTablesOutput.getData();
        if(list.size() > 0){
            Map<String, com.bytter.system.api.dto.CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrency();
            Map<String, com.bytter.system.api.dto.FcAccDTO> fcAccDTOMap = fcAccService.listAllFcAcc();
            for(FcInterestSingleVO vo : list){
                //单位
                if(null != corpMap){
                    if(corpMap.containsKey(vo.getCorpId())){
                        com.bytter.system.api.dto.CorpDTO corpDTO = corpMap.get(vo.getCorpId());
                        if(null != corpDTO){
                            vo.setCorpName(corpDTO.getName());
                        }
                    }
                }
                //币种
                if(null != currencyMap){
                    if(currencyMap.containsKey(vo.getCurId())){
                        BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
                        if(null != btCurrencyDTO){
                            vo.setCurName(btCurrencyDTO.getCurName());
                        }
                    }
                }
                if(null != fcAccDTOMap){
                    if(fcAccDTOMap.containsKey(vo.getAccount())){
                        com.bytter.system.api.dto.FcAccDTO fcAccDTO = fcAccDTOMap.get(vo.getAccount());
                        if(null != fcAccDTO){
                            vo.setAccName(fcAccDTO.getAccName());
                            if(StringUtil.isBlank_new(vo.getCurName())) {
                                if (currencyMap.containsKey(fcAccDTO.getCurId())) {
                                    BtCurrencyDTO btCurrencyDTO = currencyMap.get(fcAccDTO.getCurId());
                                    if (null != btCurrencyDTO) {
                                        vo.setCurName(btCurrencyDTO.getCurName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
