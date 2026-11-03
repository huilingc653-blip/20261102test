package com.bytter.tally.service;

import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookDtlService;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.api.ICmsPrecativeLoanSendService;
import com.bytter.finance.api.ICmsProcessMsgService;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.dto.CmsPrecativeLoanInfoDTO;
import com.bytter.finance.dto.CmsPrecativeLoanRepayDTO;
import com.bytter.finance.dto.CmsPrecativeLoanSendDTO;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.dto.*;
import com.bytter.tally.api.constant.FinaBookConstant;
import com.bytter.tally.api.constant.FinacialConstant;
import com.bytter.tally.api.constant.FinacialManualConstant;
import com.bytter.tally.api.constant.NbconsoleConstant;
import com.bytter.tally.dto.FcAccbookPageDTO;
import com.bytter.tally.vo.FcAccbookExtVO;
import com.bytter.tally.vo.FcAccbookNotifyVO;
import com.bytter.tally.vo.FcBusRaceVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
/**
 * @author
 * @version 1.0
 * @Title: Service
 * @Description: 委托贷款发放还款手工账
 * @date 2018-01-10
 */
@Service("precativeTallyService")
@Transactional
public class PrecativeTallyService {
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    /*@Autowired
    private FcBusRaceService busRaceService;*/
    @DubboReference(check = false)
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IInnerAccountService iInnerAccountService;
    @DubboReference(check = false)
    private ICmsLoanService iCmsLoanService;
    /*@Autowired
    private FcAccbookDtlService fcAccbookDtlService;*/
    @DubboReference(check = false)
    private IFcAccbookDtlService iFcAccbookDtlService;
    /*@Autowired
    private FcAccbookService fcAccbookService;*/
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IFcSubjectService iFcSubjectService;
    @DubboReference(check = false)
    private ICmsProcessMsgService msService;
    @DubboReference(check = false)
    private IConService conService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IAccountGrantService iAccountGrantService;

    @DubboReference(check = false)
    private ICmsPrecativeLoanSendService iCmsPrecativeLoanSendService;
    /**
     * 查询CmsPrecativeLoanSendDTO
     */
    @Transactional(readOnly = true)
    public CmsPrecativeLoanSendDTO queryPrecativeSend(String billCode) {
        if (EmptyUtils.isEmpty(iCmsLoanService)) {
            iCmsLoanService = (ICmsLoanService) ApplicationContextUtil.getContext().getBean("cmsLoanServiceImpl");
        }
        return iCmsLoanService.getCmsPrecativeLoanSendByBillCode(billCode);
    }
    /**
     * 查询CmsPrecativeLoanRepayDTO
     */
    @Transactional(readOnly = true)
    public CmsPrecativeLoanRepayDTO queryPrecativeRepay(String billCode) {
        if (EmptyUtils.isEmpty(iCmsLoanService)) {
            iCmsLoanService = (ICmsLoanService) ApplicationContextUtil.getContext().getBean("cmsLoanServiceImpl");
        }
        return iCmsLoanService.getCmsPrecativeLoanRepayByBillCode(billCode);
    }
    // 查询数据
    @Transactional(readOnly = true)
    public DataTablesOutput getDateTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        if (EmptyUtils.isEmpty(iCmsLoanService)) {
            iCmsLoanService = (ICmsLoanService) ApplicationContextUtil.getContext().getBean("cmsLoanServiceImpl");
        }
        return iCmsLoanService.getDateTable(dataTablesInput, user);
    }
    /**
     * 构建凭证
     */
    public FcAccbookPageDTO createAccbook(String key, String type, String menuType, ICurrentUser user) {
        if (FinacialManualConstant.MANUAL_TYPE_PRECATIVE_SEND.equals(menuType)) {
            return processAccbook(key, type, user);
        } else {
            return processAccbookRepay(key, type, user);
        }
    }
    /**
     * 元素编辑控制
     */
    public List<VoucherRenderControlDTO> getControlBeanEdit(String sysCode, int size) {
        List<VoucherRenderControlDTO> controBeans = new ArrayList<VoucherRenderControlDTO>();
        VoucherRenderControlDTO controBean = null;
        for (int i = 0; i < size; i++) {
            controBean = new VoucherRenderControlDTO();
            controBean.setSummaryEditable(true);
            controBean.setVoucherTypeEditable(true);
            controBean.setAccountEditable(false);
            controBeans.add(controBean);
        }
        return controBeans;
    }
    /**
     * 根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        if (EmptyUtils.isEmpty(fcBusinessRuleDefineService)) {
            fcBusinessRuleDefineService = (IFcBusinessRuleDefineService) ApplicationContextUtil.getContext().getBean("fcBusinessRuleDefineServiceImpl");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;
    }
    /**
     * 创建凭证信息
     */
    public FcAccbookPageDTO processAccbook(String billCode, String opType, ICurrentUser user) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        CmsPrecativeLoanSendDTO send = this.queryPrecativeSend(billCode);
        List<VoucherRenderControlDTO> controlBeans = new ArrayList<VoucherRenderControlDTO>();
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(SysRaceCode.NB_PRECATIVE_LOAN.get());
            int len = ruleDefine.getVouchers().size();
            if (len > 0) {
                controlBeans = getControlBeanEdit(SysRaceCode.NB_PRECATIVE_LOAN.get(), len);
            }
            entity = processTallyAccbook(send, controlBeans, ruleDefine, user);
        } else {
            if (EmptyUtils.isEmpty(send.getOrderNo())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
//            FcAccbookVO books = fcAccbookService.getBookVOByOrderNo(send.getOrderNo(), FinacialConstant.BOOLEAN_FALSE);
            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getBookVOByOrderNo(send.getOrderNo(), FinacialConstant.BOOLEAN_FALSE);
            FcAccbookDTO accbook = new FcAccbookDTO();
            if (!EmptyUtils.isEmpty(fcAccbookDTO)) {
                BeanUtils.copyNotNullProperties(fcAccbookDTO, accbook);
                FcBusRaceVo fcbusRace = getFcBusRaceId(send.getPrecativeId(), SysRaceCode.NB_PRECATIVE_LOAN, user.getId());
                accbook.setRaceName(fcbusRace.getRaceName());
            }
            List<FcAccbookDtlDTO> bookDtls = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            controlBeans = getControlBeanEdit(SysRaceCode.NB_PRECATIVE_LOAN.get(), bookDtls.size());
            for (int i = 0; i < bookDtls.size(); i++) {
                VoucherRenderControlDTO bean = controlBeans.get(i);
                if (NbconsoleConstant.OpType.EDIT.get().equals(opType)) {
                    if (i / 2 == 0) {
                        if (bookDtls.get(i).getDebitMoney().compareTo(BigDecimal.ZERO) > 0) {
                            if (!EmptyUtils.isEmpty(bookDtls.get(i).getSubjectCode())) {
                                FcSubjectDTO fc = iFcSubjectService.findSubjectByCode(bookDtls.get(i).getSubjectCode());
                                bookDtls.get(i).setSubjectCode(fc.getId());
                            }
                            bean.setAccountEditable(true);
                        } else {
                            FcAccDTO accDTO = fcAccService.getAccDTOByAccount(bookDtls.get(i).getAccount());
                            com.bytter.biz.finacial.api.dto.FcAccDTO dto = new com.bytter.biz.finacial.api.dto.FcAccDTO();
                            BeanUtils.copyProperties(accDTO, dto);
                            bookDtls.get(i).setAccDTO(dto);
                            bookDtls.get(i).setSubjectCode(accDTO.getSubjectId());
                            bean.setCreateAccountAble(true);
                        }
                    } else {
                        if (bookDtls.get(i).getCreditMoney().compareTo(BigDecimal.ZERO) > 0) {
                            if (!EmptyUtils.isEmpty(bookDtls.get(i).getSubjectCode())) {
                                FcSubjectDTO fc = iFcSubjectService.findSubjectByCode(bookDtls.get(i).getSubjectCode());
                                bookDtls.get(i).setSubjectCode(fc.getId());
                            }
                            bean.setAccountEditable(true);
                        } else {
                            FcAccDTO accDTO = fcAccService.getAccDTOByAccount(bookDtls.get(i).getAccount());
                            com.bytter.biz.finacial.api.dto.FcAccDTO dto = new com.bytter.biz.finacial.api.dto.FcAccDTO();
                            BeanUtils.copyProperties(accDTO, dto);
                            bookDtls.get(i).setAccDTO(dto);
                            bookDtls.get(i).setSubjectCode(accDTO.getSubjectId());
                            bean.setCreateAccountAble(true);
                        }
                    }
                }
                bookDtls.get(i).setControlVo(bean);
            }
            entity.setBookDtls(bookDtls);
            entity.setAccbook(accbook);
        }
        return entity;
    }
    /**
     * 创建凭证信息
     */
    public FcAccbookPageDTO processAccbookRepay(String billCode, String opType, ICurrentUser user) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        CmsPrecativeLoanRepayDTO repay = iCmsLoanService.getCmsPrecativeLoanRepayByBillCode(billCode);
        List<VoucherRenderControlDTO> controlBeans = new ArrayList<VoucherRenderControlDTO>();
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(SysRaceCode.NB_PRECATIVE_REPAYMENT.get());
            int len = ruleDefine.getVouchers().size();
            if (len > 0) {
                controlBeans = getControlBeanEdit(SysRaceCode.NB_PRECATIVE_REPAYMENT.get(), len);
            }
            entity = processTallyRepayAccbook(repay, controlBeans, ruleDefine, user);
        } else {
            if (EmptyUtils.isEmpty(repay.getOrderNo())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
//            FcAccbookVO books = fcAccbookService.getBookVOByOrderNo(repay.getOrderNo(),FinacialConstant.BOOLEAN_FALSE);
            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getBookVOByOrderNo(repay.getOrderNo(), FinacialConstant.BOOLEAN_FALSE);
            FcAccbookDTO accbook = new FcAccbookDTO();
            if (!EmptyUtils.isEmpty(fcAccbookDTO)) {
                BeanUtils.copyNotNullProperties(fcAccbookDTO, accbook);
                accbook.setRaceName(fcAccbookDTO.getRaceName());
            }
//        	List<FcAccbookDtlVO> bookDtls = fcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
            List<FcAccbookDtlDTO> fcAccbookDtlDTOS = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
            List<FcAccbookDtlDTO> bookDtls = BeanUtils.copyListProperties(fcAccbookDtlDTOS, FcAccbookDtlDTO.class);
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            controlBeans = getControlBeanEdit(SysRaceCode.NB_PRECATIVE_REPAYMENT.get(), bookDtls.size());
            for (int i = 0; i < bookDtls.size(); i++) {
                bookDtls.get(i).setControlVo(controlBeans.get(i));
            }
            FcAccbookExtVO bookExt = new FcAccbookExtVO();
            entity.setAccbook(accbook);
            entity.setBookDtls(bookDtls);
            entity.setBookExt(bookExt);
        }
        return entity;
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookPageDTO processTallyAccbook(CmsPrecativeLoanSendDTO send, List<VoucherRenderControlDTO> controlBeans, FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        CmsPrecativeLoanInfoDTO info = iCmsLoanService.getCmsPrecativeLoanInfoByBillCode(send.getFatherCode());
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = getAccbookDtls(ruleDefine, controlBeans, info, send, user);
        accBook.setRmk(send.getPurpose());// 备注
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);// 加急标志
        FcBusRaceVo fcbusRace = getFcBusRaceId(send.getPrecativeId(), SysRaceCode.NB_PRECATIVE_LOAN, user.getId());
        accBook.setBusRaceId(fcbusRace.getId());
        accBook.setRaceName(fcbusRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setTallyDate(send.getSendDate());
        accBook.setInterestStartDate(send.getSendDate());
        entity.setBookExt(bookExt);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 设置还款凭证信息
     */
    public FcAccbookPageDTO processTallyRepayAccbook(CmsPrecativeLoanRepayDTO repay, List<VoucherRenderControlDTO> controlBeans, FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getRepayAccbookDtls(ruleDefine, controlBeans, repay, user);
        entity.setBookDtls(accbookDtls);
        accBook.setRmk(repay.getRmk());// 备注
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);// 加急标志
        FcBusRaceVo fcbusRace = getFcBusRaceId(repay.getCorpId(), SysRaceCode.NB_PRECATIVE_REPAYMENT, user.getId());
        accBook.setBusRaceId(fcbusRace.getId());
        accBook.setRaceName(fcbusRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        accBook.setTallyDate(repay.getRepayDate());
        accBook.setInterestStartDate(repay.getRepayDate());
        return entity;
    }
    /**
     * 获取业务总类id
     */
    public FcBusRaceVo getFcBusRaceId(String netCode, ConstantBean<String> sysRaceBean, String userId) {
        CorpDTO corp = corpService.getNetByCorpId(netCode);
        netCode = corp.getNetId();
//    	List<FcBusRaceVo> raceList = busRaceService.findBySysRaceCode(sysRaceBean.get());
        List<FcBusRaceDTO> fcBusRaceDTOS = iFcBusRaceService.findBySysRaceCode(sysRaceBean.get());
        List<FcBusRaceVo> raceList = BeanUtils.copyListProperties(fcBusRaceDTOS, FcBusRaceVo.class);
        FcBusRaceVo tempRace = null;
        for (FcBusRaceVo busRace : raceList) {
            String netId = busRace.getNetId();
            if (!EmptyUtils.isEmpty(netId) && netCode.equals(netId)) {
                tempRace = busRace;
                break;
            } else if (EmptyUtils.isEmpty(netId)) {
                tempRace = busRace;
            }
        }
        if (null == tempRace) {
            tempRace = new FcBusRaceVo();
            tempRace.setNetId(netCode);
            tempRace.setRaceCode(sysRaceBean.get());
            tempRace.setRaceName(sysRaceBean.getName());
            tempRace.setSysRaceCode(sysRaceBean.get());
			/*FcBusRace fcBusRace = new FcBusRace();
			BeanUtils.copyProperties(tempRace,fcBusRace);
			busRaceService.save(fcBusRace, new String[]{netCode},userId);
			raceList = busRaceService.findBySysRaceCode(sysRaceBean.get());
			tempRace = raceList.get(0);*/
            FcBusRaceDTO fcBusRaceDTO = new FcBusRaceDTO();
            BeanUtils.copyProperties(tempRace, fcBusRaceDTO);
            iFcBusRaceService.save(fcBusRaceDTO, new String[]{netCode}, userId);
            List<FcBusRaceDTO> fcBusRaceDTOS1 = iFcBusRaceService.findBySysRaceCode(sysRaceBean.get());
            FcBusRaceDTO fcBusRaceDTO1 = fcBusRaceDTOS1.get(0);
            BeanUtils.copyProperties(fcBusRaceDTO1, tempRace);
        }
        return tempRace;
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlDTO> controlBeans, CmsPrecativeLoanInfoDTO loanInfo, CmsPrecativeLoanSendDTO send, ICurrentUser user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        this.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay, groupNum;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            if (null == entry.getSubjectCode()) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SUBJECT_IS_NULL);
            }
            if (FinacialConstant.ENTRY_TYPE_10.intValue() == entry.getEntryType().intValue()) {
                balanceWay = entry.getBalanceWay().intValue();
                groupNum = entry.getGroupNum().intValue();
                /**
                 * 1.借：企业活期存款 （委托方）
                 * 2.贷：委托存款 （新开户）
                 * 3.借：委托贷款 （新开户）
                 * 4.贷：企业活期存款（贷款方）
                 */
                VoucherRenderControlDTO bean = controlBeans.get(groupNum - 1);
                if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay && (1 == groupNum || 3 == groupNum)) {
//                    accbookDtl.setAccount(send.getDepositAcc());
                    if (3 == groupNum) {
                        // 贷款方 新开委托贷款账号
                        if (null != entry.getSubjectCode()) {
                            bean.setCreateAccountAble(true);
                            accbookDtl.setDebitMoney(send.getMoney());
                            accbookDtl.setSubjectCode(entry.getSubjectCode());
                        }
                    } else {
                        // 委托方企业活期账号
                        if (null != entry.getSubjectCode()) {
                            bean.setAccountEditable(true);
                            accbookDtl.setDebitMoney(send.getMoney());
                            accbookDtl.setSubjectCode(entry.getSubjectCode());
                        }
                    }
                } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay && (2 == groupNum || 4 == groupNum)) {
//                    accbookDtl.setAccount(send.getLoanAcc());
                    if (2 == groupNum) {
                        bean.setCreateAccountAble(true);
                        accbookDtl.setCreditMoney(send.getMoney());
                        accbookDtl.setSubjectCode(entry.getSubjectCode());
                    } else {
                        // 4.贷：企业活期存款（贷款方）
                        if (null != entry.getSubjectCode()) {
                            bean.setAccountEditable(true);
                            accbookDtl.setCreditMoney(send.getMoney());
                            accbookDtl.setSubjectCode(entry.getSubjectCode());
                        }
                    }
                }
                /* 这里加得有问题。t6没有
                if(!StringUtil.isBlank_new(accbookDtl.getAccount())) {
                    FcAccDTO accDTO = fcAccService.getAccDTOByAccount(accbookDtl.getAccount());
                    if (null != accDTO) {
                        accbookDtl.setAccName(accDTO.getAccName());
                        accbookDtl.setBlnctrAcc(accbookDtl.getAccount());
                    }
                }*/
                accbookDtl.setCurCode(loanInfo.getCurId());
                accbookDtl.setOrigVoucherNo(send.getBillCode());
                accbookDtl.setOrigVoucherId(send.getId());
                accbookDtl.setSummaryContent(loanInfo.getPurpose());
                accbookDtls.add(accbookDtl);
                accbookDtl.setControlVo(bean);
            }
        }
        return accbookDtls;
    }
    /**
     * 功能：还款时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getRepayAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlDTO> controlBeans, CmsPrecativeLoanRepayDTO repay, ICurrentUser user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        CmsPrecativeLoanSendDTO send = iCmsLoanService.getCmsPrecativeLoanSendByBillCode(repay.getSendCode());
        CmsPrecativeLoanInfoDTO info = iCmsLoanService.getCmsPrecativeLoanInfoByBillCode(send.getFatherCode());
        FcAccbookDtlDTO accbookDtl = null;
        this.collectionSort(entrys);
        String[] blnctrFcAcc = new String[4];
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay, groupNum;
        blnctrFcAcc[2] = send.getDepositAcc();
        blnctrFcAcc[1] = send.getLoanAcc();
        for (FcVoucherEntryDefineDTO entry : entrys) {
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            accbookDtl.setSummaryContent(EmptyUtils.isEmpty(entry.getSummary()) ? repay.getRmk() : entry.getSummary());
            /**
             * 现还款凭证与原凭证借贷相反
             * 借：企业活期存款 –贷款方
             * 贷：委托贷款
             * 借：委托存款
             * 贷：企业活期账号 -委托方
             * 企业活期账号默认取开户记账的账号
             * 借：企业活期账号 -贷款方(利息)
             * 贷：企业活期存款 –委托方(利息)
             */
            balanceWay = entry.getBalanceWay().intValue();
            groupNum = entry.getGroupNum().intValue();
            VoucherRenderControlDTO beanNum = controlBeans.get(groupNum - 1);
            if (!CmsConstant.REPAY_TYPE_4.equals(repay.getRepayType())) {
                if (FinacialConstant.ENTRY_TYPE_10.intValue() == entry.getEntryType().intValue()) {
                    if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay && (1 == groupNum || 3 == groupNum)) {
                        if (3 == groupNum) {
                            FcAccDTO acc = innerAccountService.getByBlnctrAcc(blnctrFcAcc[2]);
                            accbookDtl.setAccName(acc.getAccName());
                            accbookDtl.setAccount(blnctrFcAcc[2]);//委托存款
                        } else {
                            //企业活期存款 –贷款方 设置企业活期账号不可选择
                            if (EmptyUtils.isEmpty(blnctrFcAcc[0])) {
                                List<FcAccDTO> list = innerAccountService.listAccountByParams(entry.getCurCode(), info.getCorpId(), repay.getCorpId(), entry.getSubjectCode());
                                if (EmptyUtils.isEmptyList(list)) {
                                    CorpDTO corpDTO = corpService.get(repay.getCorpId());
                                    throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FCACC_IS_NULL, corpDTO.getName());
                                }
                                FcAccDTO dto = list.get(0);
                                blnctrFcAcc[0] = dto.getBlnctrAcc();
                                accbookDtl.setAccName(dto.getAccName());
                            }
                            accbookDtl.setAccount(blnctrFcAcc[0]);
                        }
                        accbookDtl.setDebitMoney(repay.getRepayMoney());
                    } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay && (2 == groupNum || 4 == groupNum)) {
                        if (2 == groupNum) {
                            FcAccDTO acc = innerAccountService.getByBlnctrAcc(blnctrFcAcc[1]);
                            accbookDtl.setAccName(acc.getAccName());
                            accbookDtl.setAccount(blnctrFcAcc[1]);
                        } else {
                            // 贷：2120企业活期-委托方 4
                            if (EmptyUtils.isEmpty(blnctrFcAcc[3])) {
                                List<FcAccDTO> list = innerAccountService.listAccountByParams(entry.getCurCode(), info.getCorpId(), send.getPrecativeId(), entry.getSubjectCode());
                                if (EmptyUtils.isEmptyList(list)) {
                                    CorpDTO corpDTO = corpService.get(info.getPrecativeId());
                                    throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FCACC_IS_NULL, corpDTO.getName());
                                }
                                FcAccDTO dto = list.get(0);
                                blnctrFcAcc[3] = dto.getBlnctrAcc();
                                accbookDtl.setAccName(dto.getAccName());
                            }
                            accbookDtl.setAccount(blnctrFcAcc[3]);
                        }
                        accbookDtl.setCreditMoney(repay.getRepayMoney());
                    }
                    accbookDtl.setControlVo(beanNum);
                    accbookDtl.setSummaryContent(repay.getRmk());
                    accbookDtl.setOrigVoucherNo(repay.getBillCode());
                    accbookDtl.setOrigVoucherId(repay.getId());
                    accbookDtl.setControlVo(beanNum);
                    if (accbookDtl.getCreditMoney().doubleValue() != 0D || accbookDtl.getDebitMoney().doubleValue() != 0D) {
                        accbookDtls.add(accbookDtl);
                    }
                }
            }
            //归还利息
            if (!CmsConstant.REPAY_TYPE_1.equals(repay.getRepayType())) {
                if (FinacialConstant.ENTRY_TYPE_40.intValue() == entry.getEntryType().intValue()) {
					/*if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
	                    accbookDtl.setAccount(blnctrFcAcc[0]);
	                    accbookDtl.setDebitMoney(repay.getRepayAccrual());//还款利息
					} else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay) {
	                    accbookDtl.setAccount(blnctrFcAcc[3]);
	                    accbookDtl.setCreditMoney(repay.getRepayAccrual());//还款利息
	                }*/
                    /**
                     借：2120企业活期-借款方 1
                     贷：2120企业活期-委托方 4
                     */
                    if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                        //借：2120企业活期-借款方 1
                        List<FcAccDTO> list = innerAccountService.listAccountByParams(entry.getCurCode(), info.getCorpId(), repay.getCorpId(), entry.getSubjectCode());
                        if (EmptyUtils.isEmptyList(list)) {
                            CorpDTO corpDTO = corpService.get(repay.getCorpId());
                            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FCACC_IS_NULL, corpDTO.getName());
                        }
                        FcAccDTO dto = list.get(0);
                        accbookDtl.setAccount(dto.getBlnctrAcc());
                        accbookDtl.setDebitMoney(repay.getRepayAccrual());//只计算计息
                        accbookDtl.setAccName(dto.getAccName());
                    } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay) {
                        // 贷：2120企业活期-委托方 4
                        List<FcAccDTO> list = innerAccountService.listAccountByParams(entry.getCurCode(), info.getCorpId(), send.getPrecativeId(), entry.getSubjectCode());
                        if (EmptyUtils.isEmptyList(list)) {
                            CorpDTO corpDTO = corpService.get(send.getPrecativeId());
                            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_FCACC_IS_NULL, corpDTO.getName());
                        }
                        FcAccDTO dto = list.get(0);
                        accbookDtl.setAccount(dto.getBlnctrAcc());
                        accbookDtl.setAccName(dto.getAccName());
                        accbookDtl.setCreditMoney(repay.getRepayAccrual());//只计算计息
                    }
                    accbookDtl.setSummaryContent(repay.getRmk());
                    accbookDtl.setOrigVoucherNo(repay.getBillCode());
                    accbookDtl.setOrigVoucherId(repay.getId());
                    accbookDtl.setControlVo(beanNum);
                    BigDecimal getCreditMoney = accbookDtl.getCreditMoney();
                    BigDecimal getDebitMoney = accbookDtl.getDebitMoney();
                    if ((StringUtils.isNotEmpty(getCreditMoney) && getCreditMoney.doubleValue() != 0D) || (StringUtils.isNotEmpty(getDebitMoney) && getDebitMoney.doubleValue() != 0D)) {
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
            @Override
            public int compare(FcVoucherEntryDefineDTO o1, FcVoucherEntryDefineDTO o2) {
                return o1.getGroupNum().compareTo(o2.getGroupNum());
            }
        });
    }
    /**
     * 提交前数据校验
     */
    private void check(FcAccbookVoucherDTO fcAccbook) {
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getVoucherType())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHERTYPE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getInterestStartDate())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_INTERESTSTARTDATE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getTallyDate())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_TALLYDATE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getEmergencySign())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_EMERGENCYSIGN_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getBusRaceId())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_BUSRACE_NO);
        }
        for (FcAccbookDtlDTO accbookDtl : fcAccbook.getBookDtls()) {
            if (EmptyUtils.isEmpty(accbookDtl.getAccount())) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_NO);
            }
            if (EmptyUtils.isEmpty(accbookDtl.getSummaryContent())) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_SUMMARYCONTENT_NO);
            }
        }
    }
    /**
     * 功能介绍 : 记账提交凭证对象
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, String billCode, String menuType, ICurrentUser user) {
        check(fcAccbook);
        validata(billCode, billCode);
        boolean isAutoCheck = this.isAutoCheck();
        String opType = fcAccbook.getOpType().name();
        //委托贷款委托存款开户
        this.createFcAcc(fcAccbook.getBookDtls(), user, opType, billCode);
        setAccBookVoucher(fcAccbook, menuType);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId()); //这里可能有个 记账员的坑；打回的时候应该设置为当前用户
        if (isAutoCheck) { //获取自动复核员用户 setCheckUser(voucher);
            voucher.setAutoCheck(true);
        }
        voucher = iFcAccbookService.submitVoucher(voucher); // 注意里面有一堆逻辑
        if (FinacialManualConstant.MANUAL_TYPE_PRECATIVE_SEND.equals(menuType)) {
            updateEntity(voucher, billCode, isAutoCheck, "1", user, isAutoCheck);
            if ("SUBMIT".equals(opType)) {
                msService.insertProcessMsg(billCode, "C2", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_PRESEND_TALLY_SUBMIT, user);
            }
            if ("MODIFY".equals(opType)) {
                msService.insertProcessMsg(billCode, "C2", CmsProcessMsgConstant.MSGA_TALLYEDITSUBMIT, CmsProcessMsgConstant.MSGA_PRESEND_EDITTALLY_SUBMIT, user);
            }
        } else {
            updateRepayEntity(voucher, billCode, isAutoCheck, "1", user);
            if ("SUBMIT".equals(opType)) {
                msService.insertProcessMsg(billCode, "C3", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_PREREPAY_TALLY_SUBMIT, user);
            }
            if ("MODIFY".equals(opType)) {
                msService.insertProcessMsg(billCode, "C3", CmsProcessMsgConstant.MSGA_TALLYEDITSUBMIT, CmsProcessMsgConstant.MSGA_PREREPAY_EDITTALLY_SUBMIT, user);
            }
        }
        // 如果自动复核，则复核凭证通过
        completeEditNotificationInfo(voucher, user);
        completeCheckNotificationInfo(voucher, user);
        if (!isAutoCheck) {
            sendCheckNotificationInfo(voucher, user);
        }
    }
    /**
     * 暂存FC_ACCBOOK
     */
    public void save(FcAccbookVoucherDTO fcAccbook, String billCode, ICurrentUser user, String menuType) {
        check(fcAccbook);
        validata(billCode, billCode);
        String opType = fcAccbook.getOpType().name();
        //委托贷款委托存款开户
        this.createFcAcc(fcAccbook.getBookDtls(), user, opType, billCode);
        //CmsPrecativeLoanSendDTO entity = this.queryPrecativeSend(billCode);
        setAccBookVoucher(fcAccbook, menuType);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        voucher = iFcAccbookService.saveVoucher(voucher);
        //FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
		/*entity.setOrderNo(accbook.getOrderNo());
		entity.setTallyCode(accbook.getOperatorCode());
		entity.setBookId(accbook.getId());
		entity.setBusGlobalId(accbook.getBusGlobalId());
		entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue()); */
        if (FinacialManualConstant.MANUAL_TYPE_PRECATIVE_SEND.equals(menuType)) {
            updateEntity(voucher, billCode, false, "2", user, false);
            msService.insertProcessMsg(billCode, "C2", CmsProcessMsgConstant.MSGA_TALLYMODIFY, CmsProcessMsgConstant.MSGA_PRESEND_TALLY_TEMP, user);
        } else {
            updateRepayEntity(voucher, billCode, false, "2", user);
            msService.insertProcessMsg(billCode, "C3", CmsProcessMsgConstant.MSGA_TALLYMODIFY, CmsProcessMsgConstant.MSGA_PREREPAY_TALLY_TEMP, user);
        }
    }
    /***
     * 打回
     */
    public void backFcAccbook(FcAccbookVoucherDTO fcAccbook, String billCode, ICurrentUser user, String menuType) {
        createVoucher(fcAccbook, menuType);
        String opType = fcAccbook.getOpType().name();
        CmsPrecativeLoanSendDTO entity = this.queryPrecativeSend(billCode);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getCorpId());
        Voucher.getTarget().get(0).getAccbook().setId(entity.getBookId());
        completeEditNotificationInfo(Voucher, user);
        completeCheckNotificationInfo(Voucher, user);
        String type = "";
        if ("MODIFY".equals(opType)) {
            validata(billCode, billCode);
            type = "5";
			/*entity.setBookId(null);
			entity.setOrderNo(null);
			entity.setBusGlobalId(null);*/
            //entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK.getValue());
            iFcAccbookService.deleteVoucher(Voucher);
        } else if ("CHECK".equals(opType)) {
            validataBack(billCode, billCode);
            type = "4";
            //entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_BACK.getValue());
            iFcAccbookService.backVoucher(Voucher);
            sendBackNotificationInfo(Voucher);
        }
        if (FinacialManualConstant.MANUAL_TYPE_PRECATIVE_SEND.equals(menuType)) {
            updateEntity(Voucher, billCode, false, type, user, false);
            if ("MODIFY".equals(opType)) {
                msService.insertProcessMsg(billCode, "C2", CmsProcessMsgConstant.MSGA_EDITTALLYBACK, CmsProcessMsgConstant.MSGA_PRESEND_EDITTALLYBACK, user);
            }
            if ("CHECK".equals(opType)) {
                msService.insertProcessMsg(billCode, "C2", CmsProcessMsgConstant.MSGA_TALLYCHECKBACK, CmsProcessMsgConstant.MSGA_PRESEND_CHECKBACK, user);
            }
        } else {
            updateRepayEntity(Voucher, billCode, false, type, user);
            if ("MODIFY".equals(opType)) {
                msService.insertProcessMsg(billCode, "C3", CmsProcessMsgConstant.MSGA_EDITTALLYBACK, CmsProcessMsgConstant.MSGA_PREREPAY_EDITTALLY_BACK, user);
            }
            if ("CHECK".equals(opType)) {
                msService.insertProcessMsg(billCode, "C3", CmsProcessMsgConstant.MSGA_TALLYCHECKBACK, CmsProcessMsgConstant.MSGA_PREREPAY_CHECKBACK, user);
            }
        }
    }
    /**
     * 构造复核传输对象
     */
    public void createVoucher(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user, String menuType) {
        createVoucher(fcAccbook, menuType);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getId());
        checkFcAccbook(Voucher, id, user, menuType);
    }
    /***
     * 复核
     */
    public void checkFcAccbook(VoucherWrapperDTO Voucher, String billCode, ICurrentUser user, String menuType) {
        //TODO 复核成功后需要修改send表status 为102  bizStatus 42
        //CmsPrecativeLoanSendDTO entity = this.queryPrecativeSend(billCode);
        /*Voucher.getTarget().get(0).getAccbook().setId(entity.getBookId());*/
        validata(billCode, billCode);
        validataBack(billCode, billCode);
        iFcAccbookService.checkVoucher(Voucher);
		/*FcAccbookDTO accbook = Voucher.getTarget().get(0).getAccbook();
		entity.setVoucherNo(accbook.getVoucherNo());
		entity.setStatus(102);
		entity.setBizStatus("42");
		entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());*/
        completeCheckNotificationInfo(Voucher, user);
        completeEditNotificationInfo(Voucher, user);
        //iCmsLoanService.updatePrecative(entity, user);
        if (FinacialManualConstant.MANUAL_TYPE_PRECATIVE_SEND.equals(menuType)) {
            updateEntity(Voucher, billCode, false, "3", user, false);
            msService.insertProcessMsg(billCode, "C2", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_PRESEND_CHECKOK, user);
            List<String> users = new ArrayList<>();
            users.add(user.getId());
            FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(Voucher.getBookVoucher().getBookDtls().get(1).getAccount());
            setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), users);
            FcAccDTO newLoanAcc1 = innerAccountService.getByBlnctrAcc(Voucher.getBookVoucher().getBookDtls().get(2).getAccount());
            setAccountGrant(newLoanAcc1.getId(), newLoanAcc1.getCorpId(), users);
        } else {
            updateRepayEntity(Voucher, billCode, false, "3", user);
            msService.insertProcessMsg(billCode, "C3", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_PREREPAY_CHECKOK, user);
        }
    }
    // 凭证封装对象
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        //获取记账员ID
        if(StringUtils.isNotEmpty(voucher.getBusGlobalId())){
            FcAccbookDTO fcAccbookDTO = iFcAccbookService.getAccbookByBusGlobalId(voucher.getBusGlobalId());
            //设置记账员ID
            fcAccbook.getAccbook().setOperatorCode(fcAccbookDTO.getOperatorCode());
        }else {
            fcAccbook.getAccbook().setOperatorCode(userId);
        }

        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);// 设置用户
        voucher.setAutoAccounter(false);
        return voucher;
    }
    /**
     * 功能介绍 : 初始化手工记账凭证
     */
    private void setAccBookVoucher(FcAccbookVoucherDTO voucher, String menuType) {
        createVoucher(voucher, menuType);
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
            if (StringUtils.isEmpty(bookDtl.getSummaryContent()) && StringUtils.isEmpty(bookDtl.getAccount()) && null == bookDtl.getDebitMoney() && null == bookDtl.getCreditMoney()) {
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
    private void createVoucher(FcAccbookVoucherDTO voucher, String menuType) {
        voucher.getAccbook().setBusOriType(getOriType(menuType));
    }
    /**
     * 返回记账的业务种类
     */
    public Integer getOriType(String menuType) {
        if (FinacialManualConstant.MANUAL_TYPE_PRECATIVE_SEND.equals(menuType)) {
            return FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.get();
        } else {
            return FinaBookConstant.BusOriType.NB_PRECATIVE_REPAYMENT.get();
        }
    }
    private void updateEntity(VoucherWrapperDTO voucher, String key, boolean flag, String opType, ICurrentUser user, boolean isAutoCheck) {
        CmsPrecativeLoanSendDTO entity = this.queryPrecativeSend(key);
        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        List<FcAccbookDtlDTO> bookDtls = voucher.getTarget().get(0).getBookDtls();
        entity.setOrderNo(accbook.getOrderNo());
        entity.setTallyCode(accbook.getOperatorCode());
        entity.setVoucherNo(accbook.getVoucherNo());
        entity.setBookId(accbook.getId());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setDepositAcc(bookDtls.get(1).getAccount());//设置委存账号
        entity.setLoanAcc(bookDtls.get(2).getAccount());//设置委贷账号
        if ("1".equals(opType)) {//提交
            if (flag) { //获取自动复核员用户 setCheckUser(voucher);
                entity.setStatus(102);
                entity.setBizStatus("42");
                entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
                // 委托贷款对接合同管理，履约环节为发放,自动复核通过，则生成履约信息
                this.createSegmentInfo(entity);
            } else {
                entity.setBizStatus("93");
                entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
            }
            notificationService.complete(NotificationConstant.FINACIAL.PRECATIVE_TALLY, entity.getId(), user.getId());
        } else if ("2".equals(opType)) {//暂存
            entity.setBizStatus("92");
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());
            notificationService.complete(NotificationConstant.FINACIAL.PRECATIVE_TALLY, entity.getId(), user.getId());
        } else if ("3".equals(opType)) {//复核
            entity.setStatus(102);
            entity.setBizStatus("42");
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
            // 委托贷款对接合同管理，履约环节为发放,复核通过，则生成履约信息
            this.createSegmentInfo(entity);
        } else if ("4".equals(opType)) {//复核打回
            entity.setBizStatus("94");
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
            CmsPrecativeLoanSendDTO dto = iCmsPrecativeLoanSendService.getById(entity.getId());
            entity.setTallyCode(dto.getTallyCode());
        } else if ("5".equals(opType)) {//改账打回
            entity.setBookId(null);
            entity.setOrderNo(null);
            entity.setBusGlobalId(null);
            entity.setBizStatus("95");
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_BACK.getValue());
            CmsPrecativeLoanSendDTO dto = iCmsPrecativeLoanSendService.getById(entity.getId());
            entity.setTallyCode(dto.getTallyCode());
            //TODO 销户
            innerAccountService.cancelFcAcc(entity.getDepositAcc(), accbook.getTallyDate(), user);// 委存账号销户
            innerAccountService.cancelFcAcc(entity.getLoanAcc(), accbook.getTallyDate(), user);// 委贷账号销户
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("money", NumberUtil.formatNumeric(entity.getMoney(), "#,##0.00"));
            notificationService.sendToUserByResPermission(entity.getId(), ResourceConstant.FCL_PRECATIVE_TALLY_APPLY, NotificationConstant.FINACIAL.PRECATIVE_TALLY, map, entity.getPrecativeId());
        }
        iCmsLoanService.updatePrecative(entity, user);
        if (isAutoCheck) { //获取自动复核员用户 setCheckUser(voucher);
            String netId = corpService.getNetByCorpId(entity.getPrecativeId()).getNetId();
            List<String> users = getAutoCheckUser(netId);
            FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(voucher.getBookVoucher().getBookDtls().get(1).getAccount());
            setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), users);
            FcAccDTO newLoanAcc1 = innerAccountService.getByBlnctrAcc(voucher.getBookVoucher().getBookDtls().get(2).getAccount());
            setAccountGrant(newLoanAcc1.getId(), newLoanAcc1.getCorpId(), users);
        }
        notificationService.complete(NotificationConstant.FINACIAL.PRECATIVE_TALLY, entity.getId(), user.getId());
    }
    private void updateRepayEntity(VoucherWrapperDTO voucher, String key, boolean flag, String opType, ICurrentUser user) {
        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        CmsPrecativeLoanRepayDTO entity = this.queryPrecativeRepay(key);
        entity.setOrderNo(accbook.getOrderNo());
        entity.setTallyCode(accbook.getOperatorCode());
        entity.setVoucherNo(accbook.getVoucherNo());
        if ("1".equals(opType)) {//提交
            if (flag) { //获取自动复核员用户 setCheckUser(voucher);
                entity.setStatus(102);
                entity.setBizStatus("48");
                entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
                // 委托贷款对接合同管理，履约环节为还款，记账，复核自动通过履约信息
                this.toDealConInfo(entity);
            } else {
                entity.setBizStatus("93");//记账待复核
                entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
            }
            notificationService.complete(NotificationConstant.FINACIAL.PRECATIVE_TALLY, entity.getId(), user.getId());
        } else if ("2".equals(opType)) {//暂存
            entity.setBizStatus("92");//记账暂存
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());
            notificationService.complete(NotificationConstant.FINACIAL.PRECATIVE_TALLY, entity.getId(), user.getId());
        } else if ("3".equals(opType)) {//复核
            entity.setStatus(102);
            entity.setBizStatus("48");
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
            // 委托贷款对接合同管理，履约环节为还款，手工记账，复核通过生成履约信息
            this.toDealConInfo(entity);
        } else if ("4".equals(opType)) {//复核打回
            entity.setBizStatus("94");//记账复核打回
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
        } else if ("5".equals(opType)) {//改账打回
            entity.setBizStatus("95");//改账打回
            entity.setOrderNo(null);
            entity.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_BACK.getValue());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("money", NumberUtil.formatNumeric(entity.getRepayAccrual(), "#,##0.00"));
            notificationService.sendToUserByResPermission(entity.getId(), ResourceConstant.FCL_PRECATIVE_TALLY_APPLY, NotificationConstant.FINACIAL.PRECATIVE_TALLY, map, entity.getPrecativeId());
        }
        iCmsLoanService.updatePrecativeRepay(entity, user);
    }
    /**
     * 内部帐号开户
     */
    private void createFcAcc(List<FcAccbookDtlDTO> bookDtls, ICurrentUser user, String opType, String billCode) {
        if (!EmptyUtils.isEmptyList(bookDtls)) {
            for (FcAccbookDtlDTO dtl : bookDtls) {
                com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtl.getAccDTO();
                if (!EmptyUtils.isEmpty(accDTO)) {
                    if ("SUBMIT".equals(opType)) {
                        accDTO.setAutoCheck(true);
                        // todo:数据转换
                        FcAccDTO fcAcc = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);
                        iInnerAccountService.saveOrUpdate(fcAcc, user);
                        dtl.setAccDTO(null);
                        FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(accDTO.getBlnctrAcc());
                        CmsPrecativeLoanSendDTO sendDTO = this.queryPrecativeSend(billCode);
                        List<String> users = new ArrayList<>();
                        users.add(user.getId());
                        users.add(sendDTO.getCreateBy());
                        users.add(sendDTO.getInfoCreateBy());
                        users.add(sendDTO.getInfoUpdateBy());
                        setAccountGrant(fcAccDTO.getId(), fcAccDTO.getCorpId(), users);
                    } else {
                        FcAccDTO account = iFcAccService.getAccDTOByAccount(accDTO.getBlnctrAcc());
                        BeanUtils.copyNotNullProperties(accDTO, account);
                        iInnerAccountService.updateFcAcc(account);
                    }
                }
            }
        }
    }
    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }
    /**
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.PRECATIVE_EDIT, accbook.getId(), user.getId());
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            notificationService.complete(NotificationConstant.FINACIAL.PRECATIVE_CHECK, accbook.getId(), user.getId());
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherDTO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherDTO.getAccbook();
            // 发送消息复核提醒
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherDTO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                money = bookDtls.get(0).getDebitMoney();
            }
            notifyVO.setMoney(money);
            notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.PRECATIVE_CHECK, notifyVO, user.getId());
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendBackNotificationInfo(VoucherWrapperDTO voucher) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherVO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                money = bookDtls.get(0).getDebitMoney().setScale(2);
            }
            notifyVO.setMoney(money);
            notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.PRECATIVE_EDIT, notifyVO);
        }
    }
    public void swapData(CmsPrecativeLoanSendDTO send, CmsPrecativeLoanInfoDTO info, FcSubjectDTO subject, CorpDTO net, FcAccDTO dto) {
        if (null == dto || EmptyUtils.isEmpty(dto.getBlnctrAcc())) {
            dto.setNetId(info.getCorpId());
            dto.setNetName(net.getName());
            dto.setCurId(info.getCurId());
            dto.setAccType(subject.getAccType());
            dto.setCorpId(info.getLoanOpp());
            dto.setCorpName(info.getLoanOppName());
            dto.setBalanceWay(subject.getBalanceWay());
            dto.setEndUnit(info.getTernUnit());
            if ("09".equals(subject.getAccType())) {
                //dto.setPrecationAcc(dto.getPrecationAcc());//委托存款账号
                dto.setAccName(info.getPrecativeName());
                dto.setPrecationCorp(info.getPrecativeId());
                dto.setPredrawSign(info.getInterestWay());
                dto.setDelayRate(send.getDeferredRate());
                dto.setDelayDeadlineDate(send.getDeferredDate());
                dto.setDeadlineDate(send.getDeadlineDate());
                //设置期限
                if (CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH.equals(info.getTernUnit())) {
                    dto.setNotifyType(getLoanTermNew(send.getSendDate(), send.getDeadlineDate()));
                } else {
                    dto.setNotifyType(DateUtils.dayDiffs(send.getDeadlineDate(), send.getSendDate()));
                }
                //设置期限单位
                dto.setEndUnit(CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH.equals(info.getTernUnit()) ? CmsConstant.CMS_LOAN_TERN_UNIT_MEONTH : CmsConstant.CMS_LOAN_TERN_UNIT_DAY);
            } else {
                dto.setAccName(info.getLoanOppName());
                dto.setPredrawSign("5");
            }
            dto.setAccName(info.getPrecativeName());
            dto.setPoundageRate(info.getPoundageRate());
            dto.setOverdueRate(send.getOverdueRate());
            dto.setDelayRate(send.getDeferredRate());
            dto.setInterestSign(CmsConstant.IS_TRUE);
            dto.setInterestRate(send.getRate());
            dto.setContractNo(send.getContractCode());
            dto.setDeadlineDate(send.getDeadlineDate());
            dto.setContractNo(send.getContractCode());
        }
    }
    /**
     * 两个日期的月份
     */
    public int getLoanTermNew(Date startDate, Date endDate) {
        Calendar startC = Calendar.getInstance();
        Calendar endC = Calendar.getInstance();
        startC.setTime(startDate);
        endC.setTime(endDate);
        int addMonth = endC.get(Calendar.MONTH) - startC.get(Calendar.MONTH);
        int startLastDay = DateUtils.getDayOfMonth(startDate);//开始时间当前月总共多少天
        int endLastDay = DateUtils.getDayOfMonth(endDate);//结束时间当前月总共多少天
        if (startLastDay == startC.get(Calendar.DAY_OF_MONTH)) {//开始日期月末最后一天
            if (endLastDay == endC.get(Calendar.DAY_OF_MONTH)) {//结束日期月末最后一天
                addMonth++;//月份+1
            } else {
                if (endC.get(Calendar.DAY_OF_MONTH) > startC.get(Calendar.DAY_OF_MONTH)) {//结束日期当月天数 > 开始日期单月天数
                    addMonth++;//月份+1
                }
            }
        } else {
            if (endLastDay == endC.get(Calendar.DAY_OF_MONTH)) {//结束日期月末最后一天
                addMonth++;//月份+1
            } else {
                if (endC.get(Calendar.DAY_OF_MONTH) > startC.get(Calendar.DAY_OF_MONTH)) {//结束日期当月天数 > 开始日期单月天数
                    addMonth++;//月份+1
                }
            }
        }
        return (endC.get(Calendar.YEAR) - startC.get(Calendar.YEAR)) * 12 + addMonth;
    }
    private void validata(String sendBillCode, String repayBillCode) {
        String cmsType = sendBillCode.substring(0, 2);
        if (!"C3".equals(cmsType)) {
            if (EmptyUtils.isNotEmpty(sendBillCode)) {
                CmsPrecativeLoanSendDTO sendInfo = iCmsLoanService.getCmsPrecativeLoanSendByBillCode(sendBillCode);
                if (sendInfo != null) {
                    if (sendInfo.getStatus() == 102) {
                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISPASS);
                    }
                }
            }
        } else {
            if (EmptyUtils.isNotEmpty(repayBillCode)) {
                CmsPrecativeLoanRepayDTO repayInfo = iCmsLoanService.getCmsPrecativeLoanRepayByBillCode(repayBillCode);
                if (repayInfo != null) {
                    if (repayInfo.getStatus() == 102 || repayInfo.getStatus() == 12) {
                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISPASS);
                    }
                }
            }
        }
    }
    private void validataBack(String sendBillCode, String repayBillCode) {
        String cmsType = sendBillCode.substring(0, 2);
        if (!"C3".equals(cmsType)) {
            if (EmptyUtils.isNotEmpty(sendBillCode)) {
                CmsPrecativeLoanSendDTO sendInfo = iCmsLoanService.getCmsPrecativeLoanSendByBillCode(sendBillCode);
                if (sendInfo != null) {
                    if (EmptyUtils.isNotEmpty(sendInfo.getTallySign()) && sendInfo.getTallySign().equals("4") || sendInfo.getTallySign().equals("0")) {
                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISBACK);
                    }
                }
            }
        } else {
            if (EmptyUtils.isNotEmpty(repayBillCode)) {
                CmsPrecativeLoanRepayDTO repayInfo = iCmsLoanService.getCmsPrecativeLoanRepayByBillCode(repayBillCode);
                if (repayInfo != null) {
                    if (EmptyUtils.isNotEmpty(repayInfo.getTallySign()) && repayInfo.getTallySign().equals("4") || repayInfo.getTallySign().equals("0")) {
                        throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISBACK);
                    }
                }
            }
        }
    }
    /**
     * @return void
     * @Description 对接的合同信息处理
     * @Param [repay, user]
     * @Date 2019/11/14 14:31
     * @Author fusb
     */
    public void toDealConInfo(CmsPrecativeLoanRepayDTO repay) {
        CmsPrecativeLoanSendDTO send = iCmsLoanService.getCmsPrecativeLoanSendByBillCode(repay.getSendCode());
        CmsPrecativeLoanInfoDTO info = iCmsLoanService.getCmsPrecativeLoanInfoByBillCode(send.getFatherCode());
        ConInfoDTO conDTO = conService.getConInfo("", info.getId());
        // 如果合同终止不产生履约信息和终止信息
        if (!EmptyUtils.isEmpty(conDTO)) {  //如果对接合同管理
            // 业务发起人
            UserDTO user = null;
            if (StringUtils.isNotEmpty(repay.getUpdateBy())) {
                user = userService.getByUserId(repay.getUpdateBy());
            } else {
                user = userService.getByUserId(repay.getCreateBy());
            }
            if (!CmsConstant.CON_INFO_STATUS_STOP.equals(conDTO.getStatus())) { // 合同未终止
                String segment = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.PRECATIVE_SEGMENT).toFormatString();
                if (segment.contains(CmsConstant.SEGMENT_REPAY)) {  // 生成履约信息
                    ConInfoRecordDTO conRe = new ConInfoRecordDTO();
                    conRe.setConId(conDTO.getId());
                    conRe.setOuterId(repay.getId());
                    conRe.setOpMsg(CmsConstant.PRECATIVE_LOAN_REPAY);
                    conRe.setOpDate(repay.getRepayDate());
                    conRe.setOpNo(repay.getBillCode());
                    conRe.setOpMoney(repay.getRepayMoney());
                    conService.performanceConOp(conRe, user);
                }
                // 委托贷款已还总金额
                BigDecimal repaySum = iCmsLoanService.getPrecativeLoanRepayMoneySum(info.getBillCode());
                if (info.getMoney().compareTo(repaySum.add(repay.getRepayMoney())) == 0) { // 最后一次还款完成，终止合同
                    ConInfoRecordDTO conReDTO = new ConInfoRecordDTO();
                    conReDTO.setConId(conDTO.getId());
                    conReDTO.setOuterId(repay.getId());
                    conReDTO.setOpMsg(CmsConstant.PRECATIVE_LOAN_REPAY);
                    conReDTO.setOpDate(DateUtils.getDate());
                    conReDTO.setOpNo(repay.getBillCode());
                    conService.terminateConOp(conReDTO, user);
                }
            }
        }
    }
    /**
     * @return void
     * @Description 委托贷款发放生成履约信息
     * @Param [send]
     * @Date 2019/12/2 19:49
     * @Author fusb
     */
    public void createSegmentInfo(CmsPrecativeLoanSendDTO send) {
        // 业务发起人
        UserDTO user = null;
        if (StringUtils.isNotEmpty(send.getUpdateBy())) {
            user = userService.getByUserId(send.getUpdateBy());
        } else {
            user = userService.getByUserId(send.getCreateBy());
        }
        CmsPrecativeLoanInfoDTO loanInfo = iCmsLoanService.getCmsPrecativeLoanInfoByBillCode(send.getFatherCode());
        ConInfoDTO conDTO = conService.getConInfo("", loanInfo.getId());
        if (!EmptyUtils.isEmpty(conDTO)) { // 如果对接合同管理
            if (!CmsConstant.CON_INFO_STATUS_STOP.equals(conDTO.getStatus())) {
                ConInfoRecordDTO dto = new ConInfoRecordDTO();
                // 如果合同终止不产生履约信息和终止信息
                dto.setConId(conDTO.getId());
                dto.setOuterId(send.getId());
                dto.setOpMsg(CmsConstant.PRECATIVE_LOAN_SEND);
                dto.setOpDate(send.getSendDate());
                dto.setOpNo(send.getBillCode());
                dto.setOpMoney(send.getMoney());
                conService.performanceConOp(dto, user);
            }
        }
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
    /**
     * 获取自动记账人
     */
    public List<String> getAutoCheckUser(String netId) {
        ConfigDTO checkeConfig = configService.getConfig(ConfigConstant.FCL, "autoChecker");
        String checkValue = checkeConfig.getValue();
        if (EmptyUtils.isEmpty(checkValue)) {//自动复核人为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL);
        }
        checkValue = checkValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        String[] netCheckUsers = checkValue.split(";");//分割
        Map<String, String> userInfo = new HashMap<String, String>();
        for (String netUser : netCheckUsers) {
            String[] temps = netUser.split("=");//'='分割为网点和记账人
            if (temps.length == 2) {
                userInfo.put(temps[0].trim(), temps[1].trim());
            }
        }
        CorpDTO corpDTO = corpService.get(netId);
        String userCode = userInfo.get(corpDTO.getCode());//获取复核人
        UserDTO userDTO = userService.getByLoginName(userCode);
        if (null != userDTO) {
            List<String> users = new ArrayList<>();
            users.add(userDTO.getId());
            return users;
        }
        throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL, userCode);
    }
    /**
     * 查询待记账发放数据
     */
    public List<CmsPrecativeLoanSendDTO> queryTallyPrecativeSend() {
        return iCmsLoanService.queryTallyPrecativeSend();
    }
    /**
     * 查询待记账发放数据
     */
    public List<CmsPrecativeLoanRepayDTO> queryTallyPrecativeRepay() {
        return iCmsLoanService.queryTallyPrecativeRepay();
    }
}
