package com.bytter.finance.domain.loan.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.loan.dao.CmsContractDao;
import com.bytter.finance.domain.loan.dao.CmsLoanInfoDao;
import com.bytter.finance.domain.loan.dao.CmsLoanSendDao;
import com.bytter.finance.domain.loan.entity.CmsContract;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.enums.LoanMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

;
/**
 * @Title: Service
 * @Description: CMS_CONTRACT
 * @author onlineGenerator
 * @date 2017-08-02 10:07:46
 * @version 1.0
 *
 */
@Service("cmsContractService")
@Transactional
public class CmsContractService extends BaseService<CmsContract, String> {
    @Autowired
    private CmsContractDao cmsContractDao;
    @Autowired
    private CmsLoanSendDao cmsLoanSendDao;
    @Autowired
    private CmsLoanInfoDao cmsLoanInfoDao;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IFcBusRaceService fcBusRaceService;
    @DubboReference(check = false)
    private IFcAccbookService fcAccbookService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IFcVoucherTypeService iFcVoucherTypeService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IAccountGrantService iAccountGrantService;
    @DubboReference(check = false)
    private IUserService userService;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(CmsContract entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsContract entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsContract entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(CmsContract entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<CmsContract, String> getEntityDao() {
        return cmsContractDao;
    }
    /**
     * <b>功能介绍：</b>根据licode查询合同<br>
     * <b>日期：</b>2017年8月22日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public CmsContract findCmsContractByliCode(String liCode) {
        return cmsContractDao.findCmsContractByliCode(liCode);
    }
    /**
     * <b>功能介绍：</b>保存合同<br>
     * <b>日期：</b>2017年8月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void saveContract(CmsContract entity, ICurrentUser user) {
        super.saveEntity(entity);
        CmsLoanSend cmsLoanSend = cmsLoanSendDao.getCmsLoanSendByBillCode(entity.getLiCode());// 发放数据
        CmsLoanInfo cmsLoanInfo = cmsLoanInfoDao.findByBillCode(cmsLoanSend.getLoanCode());// 贷款数据
        if (isAccounting()) {// 是否记账
            if (isAutoAccounting()) {// 是否自动记账
                cmsLoanSend.setStatus(CmsConstant.COMPELETOTAL);// 记账完成
                cmsLoanSend.setBizStatus(CmsConstant.BUSINESSSTATE_42);
                // 构建凭证传输对象
                FcAccbookVoucherDTO accbookVourcher = processAccbook(cmsLoanInfo, cmsLoanSend, user);
                // 记账提交暂存
                VoucherWrapperDTO voucher = submit(accbookVourcher, user.getId(), "send");
                // 设置记账返回数据
                FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
                List<FcAccbookDtlDTO> accbookdtl = voucher.getTarget().get(0).getBookDtls();
                // 记账流水号
                cmsLoanSend.setOrderNo(accbook.getOrderNo());
                // 记账凭证号
                cmsLoanSend.setVoucherNo(accbook.getVoucherNo());
                // 设置发放账号
                setSendAcc(cmsLoanSend, accbookdtl);
            } else {
                cmsLoanSend.setStatus(CmsConstant.WAITTOTAL);// 待记账
                cmsLoanSend.setBizStatus(CmsConstant.BUSINESSSTATE_91);
                //
            }
        } else {
            cmsLoanSend.setStatus(CmsConstant.COMPELETOTAL);// 记账完成
            cmsLoanSend.setBizStatus(CmsConstant.BUSINESSSTATE_42);
        }
        cmsLoanSendDao.saveOrUpdate(cmsLoanSend);
        notificationService.complete(NotificationConstant.CMS.LOAN_CONTRACT, cmsLoanSend.getId(), user.getId());
        if (cmsLoanSend.getStatus() == CmsConstant.WAITTOTAL) {
            // 新增记账待办
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("money", NumberUtil.formatNumeric(cmsLoanSend.getLoanMoney(), "#,##0.00"));
            notificationService.sendToUserByResPermission(cmsLoanSend.getId(),
                    com.bytter.biz.finacial.api.constant.ResourceConstant.FCL_LOAN_TALLY_APPLY,
                    NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, map, cmsLoanSend.getCorpId());
        }
        msService.insertProcessMsg(cmsLoanSend.getBillCode(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_CREATE_CONTRACT, user);
    }
    /**
     * <b>功能介绍：</b>设置发放申请的贷款和发放账号<br>
     * <b>日期：</b>2017年8月30日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @param cmsLoanSend
     * @param accbookdtl
     */
    public void setSendAcc(CmsLoanSend cmsLoanSend, List<FcAccbookDtlDTO> accbookdtl) {
        for (FcAccbookDtlDTO dtl : accbookdtl) {
            if (dtl.getCreditMoney().compareTo(new BigDecimal(0)) > 0) {// 贷
                cmsLoanSend.setProvideLoanAcc(dtl.getAccount());// 设置贷款账号//设置放款账号
                //TODO 设置账户名称
                cmsLoanSend.setInterestAccName(dtl.getAccName());
            }
            if (dtl.getDebitMoney().compareTo(new BigDecimal(0)) > 0) {// 借
                cmsLoanSend.setLoanAcc(dtl.getAccount());
                //TODO 设置账户名称
                cmsLoanSend.setAccName(dtl.getAccName());
            }
        }
    }
    /**
     * 功能：创建凭证信息
     */
    public FcAccbookVoucherDTO processAccbook(CmsLoanInfo loanInfo, CmsLoanSend sendInfo, ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(FinaBookConstant.SysRaceCode.NB_ENTERPRISE_LOAN.get(),
                user);
        entity = processTallyAccbook(loanInfo, sendInfo, ruleDefine, user);
        return entity;
    }
    /**
     * 功能：根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode, ICurrentUser user) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookVoucherDTO processTallyAccbook(CmsLoanInfo loanInfo, CmsLoanSend sendInfo,
            FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        List<FcAccbookDtlDTO> accbookDtls = null;
        String voucherType = ruleDefine.getVoucherType();// 获取凭证种类
        if (EmptyUtils.isEmpty(voucherType) || EmptyUtils.isEmpty(voucherType.trim())) {
            voucherType = iFcVoucherTypeService.getSysFcVoucherType().getId();// 获取系统默认凭证种类
        }
        accbookDtls = getAccbookDtls(loanInfo, sendInfo, ruleDefine, user);
        accBook.setBusRaceId(
                getFcBusRaceId(loanInfo.getNetCode(), FinaBookConstant.SysRaceCode.NB_ENTERPRISE_LOAN, user.getId()));// 设置业务种类ID
        accBook.setVoucherType(voucherType);
        accBook.setTallyDate(sendInfo.getLoanDate());
        accBook.setInterestStartDate(sendInfo.getLoanDate());
        accBook.setEmergencySign(FinaBookConstant.EmergencySign.EMERGENCY_SIGN_FAST.getValue());
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        entity.setNetCode(loanInfo.getNetCode());// 网点
        return entity;
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(CmsLoanInfo loanInfo, CmsLoanSend sendInfo,
            FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        this.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                // 业务规则定义里的组号不能为空
            }
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            // 借：1120企业放款 申请单位
            // 贷：2120企业活期 申请单位
            if (FinacialConstant.ENTRY_TYPE_10 == entry.getEntryType().intValue()) {
                balanceWay = entry.getBalanceWay().intValue();
                if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {// 借
                    accbookDtl = new FcAccbookDtlDTO();
                    if (null != entry.getSubjectCode()) {
                        FcAccDTO dto = createNewLoanAcc(loanInfo, sendInfo, entry.getSubjectCode(), balanceWay, user);// 新开企业放款账号
                        accbookDtl.setAccount(dto.getBlnctrAcc());
                        accbookDtl.setCurCode(loanInfo.getCurCode());
                        accbookDtl.setDebitMoney(sendInfo.getLoanMoney());// 设置借方发生额
                        accbookDtl.setAccName(dto.getAccName());
                        accbookDtl.setOrigVoucherNo(sendInfo.getBillCode());
                        accbookDtl.setSummaryContent(loanInfo.getPurpose());
                        accbookDtls.add(accbookDtl);
                    }
                } else {// 贷
                    accbookDtl = new FcAccbookDtlDTO();
                    if (null != entry.getSubjectCode()) {
                        List<FcAccDTO> list = innerAccountService.listAccountByParams(loanInfo.getCurCode(),
                                loanInfo.getNetCode(), loanInfo.getCorpId(), entry.getSubjectCode());
                        String netName = iCorpService.get(loanInfo.getNetCode()).getName();
                        String corpName = iCorpService.get(loanInfo.getCorpId()).getName();
                        if (EmptyUtils.isEmptyList(list)) {// 网点[{0}]单位[{1}]，企业活期账号不存在
                            throw new ParameterException(LoanMessageEnum.CMS_ACCBOOK_2120_NOTEXISTS, netName, corpName);
                        }
                        FcAccDTO dto = list.get(0);
                        accbookDtl.setAccount(dto.getBlnctrAcc());
                        accbookDtl.setCurCode(loanInfo.getCurCode());
                        accbookDtl.setCreditMoney(sendInfo.getLoanMoney());// 设置贷方发生额
                        accbookDtl.setAccName(dto.getAccName());
                        accbookDtl.setOrigVoucherNo(sendInfo.getBillCode());
                        accbookDtl.setSummaryContent(loanInfo.getPurpose());
                        accbookDtls.add(accbookDtl);
                    }
                }
            }
        }
        return accbookDtls;
    }
    /**
     * 功能：对业务规则排序
     */
    public void collectionSort(List<FcVoucherEntryDefineDTO> entrys) {
        Collections.sort(entrys, new Comparator<FcVoucherEntryDefineDTO>() {
            public int compare(FcVoucherEntryDefineDTO o1, FcVoucherEntryDefineDTO o2) {
                return o1.getGroupNum().compareTo(o2.getGroupNum());
            }
        });
    }
    /**
     * <b>功能介绍：</b>获取业务种类ID<br>
     * <b>日期：</b>2017年8月23日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getFcBusRaceId(String netCode, ConstantBean<String> sysBean, String userId) {
        List<FcBusRaceDTO> busRace = fcBusRaceService.findBySysRaceCode(sysBean.get());
        if (EmptyUtils.isEmptyList(busRace)) {
            FcBusRaceDTO tempRace = new FcBusRaceDTO();
            tempRace.setNetId(netCode);
            tempRace.setRaceCode(sysBean.get());
            tempRace.setRaceName(sysBean.getName());
            tempRace.setSysRaceCode(sysBean.get());
            fcBusRaceService.save(tempRace, new String[] { netCode }, userId);
            busRace = fcBusRaceService.findByNetIdSysRaceCode(sysBean.get(), netCode);
        }
        return busRace.get(0).getId();
    }
    /**
     * 功能介绍 : 记账提交凭证对象
     */
    public VoucherWrapperDTO submit(FcAccbookVoucherDTO fcAccbook, String userId, String sign) {
        setAccBookVoucher(fcAccbook, sign);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, userId);
        VoucherWrapperDTO voucherWrapperDTO = fcAccbookService.submitVoucher(voucher);
        return voucherWrapperDTO;
    }
    /**
     * 功能介绍 : 初始化手工记账凭证
     */
    public void setAccBookVoucher(FcAccbookVoucherDTO voucher, String sign) {
        createVoucher(voucher, sign);
        List<FcAccbookDtlDTO> bookDtls = new ArrayList<FcAccbookDtlDTO>();
        // 初始化数据
        for (FcAccbookDtlDTO bookDtl : voucher.getBookDtls()) {
            if (null == bookDtl.getDebitMoney()) {
                bookDtl.setDebitMoney(BigDecimal.ZERO);
            }
            if (null == bookDtl.getCreditMoney()) {
                bookDtl.setCreditMoney(BigDecimal.ZERO);
            }
            // 如果明细数据为空，则不保存非空明细
            if (StringUtils.isEmpty(bookDtl.getSummaryContent()) && StringUtils.isEmpty(bookDtl.getAccount())
                    && null == bookDtl.getDebitMoney() && null == bookDtl.getCreditMoney()) {
            } else {
                bookDtls.add(bookDtl);
            }
        }
        // 明细进行先借后贷的排序
        Collections.sort(bookDtls, new Comparator<FcAccbookDtlDTO>() {
            public int compare(FcAccbookDtlDTO o1, FcAccbookDtlDTO o2) {
                return o1.getCreditMoney().abs().compareTo(BigDecimal.ZERO);
            }
        });
        voucher.setBookDtls(bookDtls);
    }
    public void createVoucher(FcAccbookVoucherDTO voucher, String sign) {
        if ("repay".equals(sign)) {
            voucher.getAccbook().setBusOriType(FinaBookConstant.BusOriType.NB_ENTERPRISE_REPAYMENT.get());
        } else {
            voucher.getAccbook().setBusOriType(FinaBookConstant.BusOriType.NB_ENTERPRISE_LOAN.get());
        }
    }
    /***
     * 凭证封装对象
     */
    public VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);// 设置用户
        voucher.setAutoCheck(true);
        return voucher;
    }
    /**
     * <b>功能介绍：</b>新开企业放款账号<br>
     * <b>日期：</b>2017年10月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @param loanInfo
     * @param sendInfo
     * @param subjectId
     * @param balanceWay
     * @return
     */
    public FcAccDTO createNewLoanAcc(CmsLoanInfo loanInfo, CmsLoanSend sendInfo, String subjectId, int balanceWay,
            ICurrentUser user) {
        FcAccDTO acc = new FcAccDTO();
        // 网点
        acc.setNetId(loanInfo.getNetCode());
        // 单位
        acc.setCorpId(loanInfo.getCorpId());
        // 币别
        acc.setCurId(loanInfo.getCurCode());
        // 账户类型
        acc.setAccType(InnerAccountConstant.FCACC_LOAN);
        // 科目
        acc.setSubjectId(subjectId);
        // 自动复核
        acc.setAutoCheck(true);
        // 账户名
        acc.setAccName(multiLangServiceI.getLang("cms.loan.tally.loanAccName") + "(" + loanInfo.getBillCode() + ")");
        // 余额方向
        acc.setBalanceWay(balanceWay);
        // 计息标志
        acc.setInterestSign(CmsConstant.IS_TRUE);
        // 开户日期
        acc.setAccOpenTime(new Date());
        // 账号启用日期
        acc.setAccStartDate(sendInfo.getLoanDate());
        // 起息日期
        acc.setInterestStartDate(sendInfo.getLoanDate());
        // 到期日期
        acc.setDeadlineDate(sendInfo.getRepayDate());
        // 设置期限
        if (CmsConstant.CMS_TERN_UNIT_MEONTH.equals(loanInfo.getTernUnit())) {
            acc.setNotifyType(getLoanTermNew(sendInfo.getLoanDate(), sendInfo.getRepayDate()));
        } else {
            acc.setNotifyType(DateUtils.dayDiffs(sendInfo.getRepayDate(), sendInfo.getLoanDate()));
        }
        // 设置期限单位
        acc.setEndUnit(CmsConstant.CMS_TERN_UNIT_MEONTH.equals(loanInfo.getTernUnit())
                ? CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH : CmsConstant.CMS_LOAN_TERN_UNIT_DAY);
        // 利率
        acc.setInterestRate(sendInfo.getLoanRate());
        // 逾期利率
        acc.setOverdueRate(sendInfo.getOverdueRate());
        // 展期利率
        acc.setDelayRate(sendInfo.getDeferredRate());
        // 合同编号
        acc.setContractNo(sendInfo.getContractCode());
        // 计息方式
        acc.setPredrawSign(sendInfo.getInterestWay());
        List<FcAccDTO> list = innerAccountService.listAccountByMoreParams(loanInfo.getCurCode(), loanInfo.getNetCode(),
                loanInfo.getCorpId(), null, SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get());
        if (!EmptyUtils.isEmptyList(list)) {
            // 扣息账号
            acc.setAccForDeduct(list.get(0).getBlnctrAcc());
        }
        String account = innerAccountService.saveOrUpdate(acc, user);
        // 自动授权
        CorpDTO corpDTO = iCorpService.get(loanInfo.getCorpId());
        List<String> tallyUsers = getAutoCheckUser(corpDTO.getNetId());
        tallyUsers.add(user.getId());
        FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(account);
        setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), tallyUsers);
        return innerAccountService.getByBlnctrAcc(account);
    }
    /**
     * <b>功能介绍：</b>是否记账<br>
     * <b>日期：</b>2017年8月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public boolean isAccounting() {
        return configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_ACCOUNTING).toInteger()
                .intValue() == 1;
    }
    /**
     * <b>功能介绍：</b>是否自动记账<br>
     * <b>日期：</b>2017年9月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @return
     */
    public boolean isAutoAccounting() {
        return configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_INTERIOR_AUTOMATIC_TALLY)
                .toInteger().intValue() == 1;
    }
    /**
     * <b>功能介绍：</b>两个日期的月份<br>
     * <b>日期：</b>2017年11月28日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @param startDate
     * @param endDate
     * @return
     */
    public int getLoanTermNew(Date startDate, Date endDate) {
        Calendar startC = Calendar.getInstance();
        Calendar endC = Calendar.getInstance();
        startC.setTime(startDate);
        endC.setTime(endDate);
        int addMonth = endC.get(Calendar.MONTH) - startC.get(Calendar.MONTH);
        int startLastDay = DateUtils.getDayOfMonth(startDate);// 开始时间当前月总共多少天
        int endLastDay = DateUtils.getDayOfMonth(endDate);// 结束时间当前月总共多少天
        if (startLastDay == startC.get(Calendar.DAY_OF_MONTH)) {// 开始日期月末最后一天
            if (endLastDay == endC.get(Calendar.DAY_OF_MONTH)) {// 结束日期月末最后一天
                addMonth++;// 月份+1
            } else {
                if (endC.get(Calendar.DAY_OF_MONTH) > startC.get(Calendar.DAY_OF_MONTH)) {// 结束日期当月天数 > 开始日期单月天数
                    addMonth++;// 月份+1
                }
            }
        } else {
            if (endLastDay == endC.get(Calendar.DAY_OF_MONTH)) {// 结束日期月末最后一天
                addMonth++;// 月份+1
            } else {
                if (endC.get(Calendar.DAY_OF_MONTH) > startC.get(Calendar.DAY_OF_MONTH)) {// 结束日期当月天数 > 开始日期单月天数
                    addMonth++;// 月份+1
                }
            }
        }
        return (endC.get(Calendar.YEAR) - startC.get(Calendar.YEAR)) * 12 + addMonth;
    }
    /**
     * 获取天数或月数
     *
     * @param endDate
     *            结束日期
     * @param type
     *            类型(2:天 1：月)
     * @return
     * @throws ParseException
     */
    public int getLoanTerm(String startDate, String endDate, String type) {
        Calendar c1;
        Calendar c2;
        try {
            c1 = DateUtils.parseCalendar(startDate, "yyyy年MM月dd日");
            c2 = DateUtils.parseCalendar(endDate, "yyyy年MM月dd日");
            if ("1".equals(type)) {
                int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
                if (c2.get(Calendar.DATE) - c1.get(Calendar.DATE) > 0) {
                    return year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH) + 1;
                } else {
                    return year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
                }
            } else {
                long time1 = c1.getTimeInMillis();
                long time2 = c2.getTimeInMillis();
                long between_days = (time2 - time1) / (1000 * 3600 * 24);
                return Integer.parseInt(String.valueOf(between_days));
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }
    public String showRate(Double number) {
        if (number == null) {
            return "";
        }
        Integer rate = configService.getConfig(ConfigConstant.SYS, "rate").toInteger();
        String formatNumber;
        if (rate.equals(1)) {
            DecimalFormat df = new DecimalFormat("0.00");
            formatNumber = df.format(number * 1000) + "‰";
        } else {
            DecimalFormat df = new DecimalFormat("0.0000");
            formatNumber = df.format(number * 100) + "%";
        }
        return formatNumber;
    }
    /**
     * 获取自动记账人
     */
    public List<String> getAutoCheckUser(String netId) {
        CorpDTO corpDTO = iCorpService.get(netId);
        if (ObjectUtil.isEmpty(corpDTO)){
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_NETID_NOTNULL);
        }
        ConfigDTO tallyConfig = configService.getConfig(ConfigConstant.FCL, "autoAccounter");
        ConfigDTO checkeConfig = configService.getConfig(ConfigConstant.FCL, "autoChecker");
        String tallyValue = tallyConfig.getValue();
        if (EmptyUtils.isEmpty(tallyValue)) {// 自动记账人为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOACCOUNTER_NULL,corpDTO.getName());
        }
        String checkValue = checkeConfig.getValue();
        if (EmptyUtils.isEmpty(checkValue)) {// 自动复核人为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL,corpDTO.getName());
        }
        tallyValue = tallyValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        checkValue = checkValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        String[] netTallyUsers = tallyValue.split(";");// 分割
        String[] netCheckUsers = checkValue.split(";");// 分割
        Map<String, String> tallyUserInfo = new HashMap<String, String>();
        for (String netUser : netTallyUsers) {
            String[] temps = netUser.split("=");// '='分割为网点和记账人
            if(temps.length != 2){
                throw new ParameterException("请检查自动记账人参数设置！");
            }
            tallyUserInfo.put(temps[0].trim(), temps[1].trim());
        }

        String userCode1 = tallyUserInfo.get(corpDTO.getCode());// 获取记账人
        UserDTO userDTO1 = userService.getByLoginName(userCode1);
        if (null == corpDTO || null == userCode1) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOACCOUNTER_NULL, corpDTO.getName());
        }
        Map<String, String> userInfo = new HashMap<String, String>();
        for (String netUser : netCheckUsers) {
            String[] temps = netUser.split("=");// '='分割为网点和记账人
            if(temps.length != 2){
                throw new ParameterException("请检查自动复核人参数设置！");
            }
            userInfo.put(temps[0].trim(), temps[1].trim());
        }

        String userCode = userInfo.get(corpDTO.getCode());// 获取复核人
        UserDTO userDTO = userService.getByLoginName(userCode);
        if (null == userDTO || null == corpDTO) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL, corpDTO.getName());
        }
        if (null != userDTO && null != userDTO1) {
            List<String> users = new ArrayList<>();
            users.add(userDTO.getId());
            users.add(userDTO1.getId());
            return users;
        }
        return new ArrayList<>();
    }
    private void setAccountGrant(String accountId, String corpId, List<String> users) {
        users.removeAll(Collections.singleton(null));
        for (String user : users) {
            AccountGrantDTO ag = new AccountGrantDTO();
            ag.setAccountId(accountId);
            ag.setAccountType(1);
            ag.setUserId(user);
            ag.setCorpId(corpId);
            iAccountGrantService.saveAccountGrant(ag);
        }
    }
}
