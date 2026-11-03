package com.bytter.tally.service;

import com.bytter.bill.api.bill.dto.AbAccrecordsDTO;
import com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO;
import com.bytter.bill.api.bill.dto.AbDiscountDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.tally.api.constant.FinaBookConstant;
import com.bytter.tally.api.constant.FinacialConstant;
import com.bytter.tally.api.constant.FinacialManualConstant;
import com.bytter.tally.api.constant.NbconsoleConstant;
import com.bytter.tally.vo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;
@Service("abBillTallyService")
@Transactional
public class AbBillTallyService {
    @DubboReference(check = false)
    private IAccebillService iAccebillService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    /*@Autowired
    private FcAccbookService fcAccbookService;*/
    /*@Autowired
    private FcAccbookDtlService fcAccbookDtlService;*/
    @DubboReference(check = false)
    private IFcAccbookDtlService iFcAccbookDtlService;
    /*@Autowired
    private FcAccbookExtService fcAccbookExtService;*/
    @DubboReference
    private IFcAccbookExtService iFcAccbookExtService;
    /*@Autowired
    private FcBusRaceService busRaceService;*/
    @DubboReference
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;

    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcSubjectService iFcSubjectService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @Autowired
    private FinacialTallyRemindService finacialTallyRemindService;

    @DubboReference
    private IFcVoucherTypeService iFcVoucherTypeService;



    public DataTablesOutput getAbBillDateTable(@RequestBody DataTablesInput dataTablesInput, String menuType, String opType, ICurrentUser user) {
        String accbookStatus = null;
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            accbookStatus = "0";
        } else if (NbconsoleConstant.OpType.EDIT.get().equals(opType)) {
            accbookStatus = "1";
        } else if (NbconsoleConstant.OpType.CHECK.get().equals(opType)) {
            accbookStatus = "2";
        }

        dataTablesInput.setResource(ResourceConstant.FINACIAL_ABBILLTALLY_APPLY);
        DataTablesOutput<AbBillAccbookInfoDTO> output = iAccebillService.getBillAccbookDataTables(dataTablesInput, menuType, accbookStatus, user);
        List<AbBillAccbookInfoDTO> list=output.getData();
        if(null != list) {
            List<String> corpIdS = iCorpService.getUserCorpIds(com.bytter.core.constant.ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_MANAGERQUERY, user.getId());
            Map<String, String> corpMap = iCorpService.getNameByIds(corpIdS);
            for (AbBillAccbookInfoDTO dto : list) {
                dto.setCorpName(corpMap.get(dto.getCorpId()));
            }
        }
        return output;
    }
    /**
     * 构建凭证
     */
    public FcAccbookPageVO createAccbook(String id, String type, String menuType, String sysRaceCode, ICurrentUser user) {
//        if (FinacialManualConstant.MANUAL_TYPE_PUT_IN_STORAGE.equals(menuType) || FinacialManualConstant.MANUAL_TYPE_MOVE_TO_SAVE.equals(menuType)
//                || FinacialManualConstant.MANUAL_TYPE_BANK_TO_CENTER.equals(menuType)) {//61.62.65
        return processAccbook(id, type, sysRaceCode, user);
        //}
    }
    /**
     * 创建凭证信息
     */
    public FcAccbookPageVO processAccbook(String id, String opType, String sysRaceCode, ICurrentUser user) {
        FcAccbookPageVO entity = new FcAccbookPageVO();
        AbBillAccbookInfoDTO info = iAccebillService.getAbVoucherinfoVOById(id);//要改 现在是通过id来获取实体的
        List<VoucherRenderControlVo> controlBeans = new ArrayList<VoucherRenderControlVo>();
        FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(sysRaceCode);
        int len = ruleDefine.getVouchers().size();
        if (len > 0) {
            controlBeans = getControlBeanEdit(len);
        }
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            entity = processTallyAccbook(info, controlBeans, sysRaceCode, ruleDefine, user);//ok
//            if (EmptyUtils.isEmpty(info.getOrderNo())) {没有这个字段流水号
//                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
//            }
            Date date = new Date();
//            Date close = iFcDayCloseService.getDayCloseFinalDate(info.getCorpId());
//           if (date.compareTo(close) < 1 || date.compareTo(close) == 0) {
//               Calendar calendar = new GregorianCalendar();
//               calendar.setTime(close);
//               calendar.add(calendar.DATE, 1); //把日期往后增加一天,整数  往后推,负数往前移动
//               Date closeDate = calendar.getTime(); //这个时间就是日期往后推一天的结果
//               entity.getAccbook().setTallyDate(closeDate);
//               entity.getAccbook().setInterestStartDate(closeDate);
//           } else {
            entity.getAccbook().setTallyDate(date);
            entity.getAccbook().setInterestStartDate(date);
            //      }
        } else {
//
//            FcAccbook books = fcAccbookService.getAccbookByBusGlobalId(info.getBusGlobalId());
            FcAccbookDTO books = iFcAccbookService.getAccbookByBusGlobalId(info.getBusGlobalId());
            FcAccbookVO accbook = new FcAccbookVO();
            if (!EmptyUtils.isEmpty(books)) {
                BeanUtils.copyNotNullProperties(books, accbook);
                accbook.setRaceName(books.getBusRaceName());
                FcVoucherTypeDTO fcVoucherTypeDTO = iFcVoucherTypeService.getFcVoucherTypeById(accbook.getVoucherType());
               if (fcVoucherTypeDTO!= null){
                   accbook.setVoucherName(fcVoucherTypeDTO.getVoucherName()) ;
               }
//                List<FcAccbookDtlVO> bookDtls = fcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
                List<FcAccbookDtlDTO> fcAccbookDtlDTOS = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
                List<FcAccbookDtlVO> bookDtls = BeanUtils.copyListProperties(fcAccbookDtlDTOS, FcAccbookDtlVO.class);
//                FcAccbookExtVO bookExt = fcAccbookExtService.getBookExtVOByBookId(accbook.getId());
                FcAccbookExtDTO fcAccbookExtDTO = iFcAccbookExtService.getBookExtVOByBookId(accbook.getId());
                FcAccbookExtVO bookExt = new FcAccbookExtVO();
                BeanUtils.copyProperties(fcAccbookExtDTO, bookExt);
                if (null == bookDtls || bookDtls.size() == 0) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
                }
                for (int i = 0; i < bookDtls.size(); i++) {
                    bookDtls.get(i).setControlVo(controlBeans.get(i));
                }
                entity.setAccbook(accbook);
                entity.setBookDtls(bookDtls);
                entity.setBookExt(bookExt);
            }
        }
        return entity;//ok
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookPageVO processTallyAccbook(AbBillAccbookInfoDTO info, List<VoucherRenderControlVo> controlBeans, String sysRaceCode, FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        //凭证传输对象，用于页面显示
        FcAccbookPageVO entity = new FcAccbookPageVO();
        //账务凭证保存对象
        FcAccbookVO accBook = new FcAccbookVO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        //entity.setAccbook(accBook);
        List<FcAccbookDtlVO> accbookDtls = null;
        accbookDtls = getAccbookDtls(info, ruleDefine, controlBeans, user);//记账时的会计分录构造ok
        accBook.setRmk(info.getAbss());// 备注
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);// 加急标志
        FcBusRaceVo fcbusRace = getFcBusRaceId(info.getCorpId(), sysRaceCode, user.getId());
        accBook.setBusRaceId(fcbusRace.getId());
        accBook.setRaceName(fcbusRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setNetId(info.getNetId());
//		accBook.setTallyDate(accrecords.getBusinessDate());
        //记账日期约定就是当前系统日期
        accBook.setTallyDate(DateUtils.getDate());
        entity.setBookExt(bookExt);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);//ok
        return entity;
    }
    private List<FcAccbookDtlVO> getAccbookDtls(AbBillAccbookInfoDTO info, FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlVo> controlBeans, ICurrentUser userVo) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        this.collectionSort(entrys);
        List<FcAccbookDtlVO> accbookDtls = new ArrayList<FcAccbookDtlVO>();
        FcAccbookDtlVO accbookDtl = null;
        int balanceWay, groupNum;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlVO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            if (null == entry.getSubjectCode()) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SUBJECT_IS_NULL);
            }
            balanceWay = entry.getBalanceWay().intValue();
            groupNum = entry.getGroupNum().intValue();
            VoucherRenderControlVo bean = controlBeans.get(groupNum - 1);
            if (FinacialConstant.ENTRY_TYPE_10.equals(entry.getEntryType())) {
                if (FinacialConstant.BALANCE_WAY_DEBIT.equals(String.valueOf(balanceWay))) {
                    accbookDtl.setDebitMoney(info.getBillMoney());
                    bean.setAccountEditable(groupNum != 3);
                    if (entry.getGroupNum() == 1) {
                        if (FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_GET.get().equals(ruleDefine.getSysRaceCode())) {
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(info.getAid());
                            if (discount != null) {
                                // bean.setAccountEditable(true);
                                BigDecimal money = info.getBillMoney().subtract(discount.getInterest());
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setDebitMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        } else if (FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_DAOZHANG.get().equals(ruleDefine.getSysRaceCode())) {
                            AbAccrecordsDTO accrecordsFather = iAccebillService.getAbAccrebyLastid(info.getLastid());
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(accrecordsFather.getId());
                            //  bean.setAccountEditable(true);
                            if (discount != null) {
                                BigDecimal money = discount.getRealMoney();
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setDebitMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        }
                    } else if (entry.getGroupNum() == 3) {
                        if (FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET.get().equals(ruleDefine.getSysRaceCode())) {
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(info.getAid());
                            if (discount != null) {
                                // bean.setAccountEditable(true);
                                BigDecimal money = info.getBillMoney().subtract(discount.getInterest());
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setDebitMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        }
                    }
                } else if (FinacialConstant.BALANCE_WAY_CREDIT.equals(String.valueOf(balanceWay))) {
                    accbookDtl.setCreditMoney(info.getBillMoney());
                    bean.setAccountEditable(groupNum != 4);
                    if (entry.getGroupNum() == 4) {
                        if (FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD.get().equals(ruleDefine.getSysRaceCode()) || FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH.get().equals(ruleDefine.getSysRaceCode())) {
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(info.getAid());
                            //   bean.setAccountEditable(true);
                            if (discount != null) {
                                BigDecimal money = discount.getRealMoney();
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setCreditMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        }
                    } else if (entry.getGroupNum() == 2) {
                        if (FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_MD.get().equals(ruleDefine.getSysRaceCode()) || FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_SH.get().equals(ruleDefine.getSysRaceCode())) {
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(info.getAid());
                            //  bean.setAccountEditable(true);
                            if (discount != null) {
                                BigDecimal money = discount.getRealMoney();
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setCreditMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        } else if (FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_GET.get().equals(ruleDefine.getSysRaceCode()) || FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_GET_TX.get().equals(ruleDefine.getSysRaceCode())) {
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(info.getAid());
                            if (discount != null) {
                                //   bean.setAccountEditable(true);
                                BigDecimal money = info.getBillMoney().subtract(discount.getInterest());
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setCreditMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        }
                    }
                }
                accbookDtl.setControlVo(bean);
            } else if (FinacialConstant.ENTRY_TYPE_40.equals(entry.getEntryType())) {
                if (FinacialConstant.BALANCE_WAY_DEBIT.equals(String.valueOf(balanceWay))) {
                    if (entry.getGroupNum() == 2) {
                        if (FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_GET.get().equals(ruleDefine.getSysRaceCode())) {
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(info.getAid());
                            if (discount != null) {
                                // bean.setAccountEditable(true);
                                BigDecimal money = discount.getInterest();
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setDebitMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        } else if (FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_DAOZHANG.get().equals(ruleDefine.getSysRaceCode())) {
                            AbAccrecordsDTO accrecordsFather = iAccebillService.getAbAccrebyLastid(info.getLastid());
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(accrecordsFather.getId());
                            //  bean.setAccountEditable(true);
                            if (discount != null) {
                                BigDecimal money = info.getBillMoney().subtract(discount.getRealMoney());
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setDebitMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        }
                    } else if (entry.getGroupNum() == 4) {
                        if (FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_GET.get().equals(ruleDefine.getSysRaceCode())) {
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(info.getAid());
                            if (discount != null) {
                                //   bean.setAccountEditable(true);
                                BigDecimal money = discount.getInterest();
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setDebitMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        }
                    }
                } else if (FinacialConstant.BALANCE_WAY_CREDIT.equals(String.valueOf(balanceWay))) {
                    if (entry.getGroupNum() == 5) {
                        if (FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_MD.get().equals(ruleDefine.getSysRaceCode()) || FinaBookConstant.SysRaceCode.NB_BILL_CORP_CENTER_DISCOUNT_NET_SH.get().equals(ruleDefine.getSysRaceCode())) {
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(info.getAid());
                            //bean.setAccountEditable(true);
                            if (discount != null) {
                                BigDecimal money = info.getBillMoney().subtract(discount.getRealMoney());
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setCreditMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        }
                    } else if (entry.getGroupNum() == 3) {
                        if (FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_MD.get().equals(ruleDefine.getSysRaceCode()) || FinaBookConstant.SysRaceCode.NB_BILL_CORP_DISCOUNT_NET_SH.get().equals(ruleDefine.getSysRaceCode())) {
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(info.getAid());
                            //  bean.setAccountEditable(true);
                            if (discount != null) {
                                BigDecimal money = info.getBillMoney().subtract(discount.getRealMoney());
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setCreditMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        } else if (FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_GET.get().equals(ruleDefine.getSysRaceCode()) || FinaBookConstant.SysRaceCode.NB_BILL_NET_DISCOUNT_BANK_GET_TX.get().equals(ruleDefine.getSysRaceCode())) {
                            AbDiscountDTO discount = iAccebillService.getAbDiscountByRecordId(info.getAid());
                            if (discount != null) {
                                //  bean.setAccountEditable(true);
                                BigDecimal money = discount.getInterest();
                                Double mo = money.doubleValue();
                                if (mo == 0d) {
                                    continue;
                                } else {
                                    accbookDtl.setCreditMoney(money);
                                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                                }
                            }
                        }
                    }
                }
                accbookDtl.setControlVo(bean);
            }
            // todo:数据转换
            com.bytter.bill.api.bill.dto.FcVoucherEntryDefineDTO entryDefineDTO = BeanUtils.copyNewInstanceProperties(entry, com.bytter.bill.api.bill.dto.FcVoucherEntryDefineDTO.class);
            String corpId = iAccebillService.getFcAccCorpId(info.getNetId(), info.getCorpId(), null, ruleDefine.getSysRaceCode(), entryDefineDTO);
            //CorpDTO accCorp=corpService.get(corpId);
            FcSubjectDTO fcSubject = iFcSubjectService.findSubjectById(entry.getSubjectCode());
            if (null == fcSubject) {
                throw new ParameterException(CollectMessageEnum.FIN_MESSAGE_SUBJECT_IS_NULL);
            }
            List<FcAccDTO> fcaccDTO = null;
            String msgCorp = "";
            if (fcSubject != null) {
                if (SubjectConstant.SysSubject.AB_INTEREST_INCOME.getValue().equals(fcSubject.getSysSubjectCode()) || SubjectConstant.SysSubject.AB_BANK_INTEREST_EXPENDITURE.getValue().equals(fcSubject.getSysSubjectCode()) || SubjectConstant.SysSubject.AB_INTEREST_PROFIT.getValue().equals(fcSubject.getSysSubjectCode())) {//501004,521005,2060
                    if (SubjectConstant.SysSubject.AB_BANK_INTEREST_EXPENDITURE.getValue().equals(fcSubject.getSysSubjectCode())) {
                        fcaccDTO = iFcAccbookService.getInterestAcc(entry.getCurCode(), info.getNetId(), info.getCorpId(), entry.getSubjectCode(), "ab", info.getAoaccninfAc(), userVo.getId(), "银行贴现利息支出");
                    } else {
                        fcaccDTO = iFcAccbookService.getInterestAcc(entry.getCurCode(), info.getNetId(), info.getCorpId(), entry.getSubjectCode(), "", "", userVo.getId(), "贴现利息收入");
                    }
                } else {
                    List<com.bytter.system.api.dto.FcAccDTO> listData = innerAccountService.listAccountByParams(entry.getCurCode(), info.getNetId(), corpId, entry.getSubjectCode());
                    // todo:数据转换
                    fcaccDTO = BeanUtils.copyListProperties(listData, FcAccDTO.class);
                    msgCorp = iCorpService.get(corpId).getName();
                }
            }
            if (fcaccDTO.size() < 1) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ACCOUNT_ISNULL, msgCorp, fcSubject.getSubjectName());
            }
            FcAccDTO fcacc = fcaccDTO.get(0);//
            accbookDtl.setAccName(fcacc.getAccName());
            accbookDtl.setAccount(fcacc.getBlnctrAcc());
            accbookDtl.setLineNo(entry.getGroupNum());
            accbookDtl.setCurCode(entry.getCurCode());
            //accbookDtl.setBalanceWay(entry.getBalanceWay());
            if (EmptyUtils.isEmpty(accbookDtl.getSubjectCode())) {
                accbookDtl.setSubjectCode(entry.getSubjectCode());
            }
            //  accbookDtl.setCurCode(info.getCurCode());
            accbookDtl.setOrigVoucherNo(info.getOrigNo());
            accbookDtl.setOrigVoucherId(info.getAid());
            accbookDtl.setSummaryContent(info.getAbss());
            accbookDtl.setOrigVoucherId(info.getAid());
            accbookDtls.add(accbookDtl);
            accbookDtl.setControlVo(bean);
        }
        return accbookDtls;
    }
    /**
     * 暂存FC_ACCBOOK
     */
    public void save(FcAccbookVoucherDTO fcAccbook, ICurrentUser user, String opType, String menuType, String id) {
        check(fcAccbook);
        validata(fcAccbook.getAccbook().getBusGlobalId());//核对有没有复核过
        //this.createFcAcc(fcAccbook.getBookDtls(),user,opType);
        setAccBookVoucher(fcAccbook, null, menuType);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        VoucherWrapperDTO dto = iFcAccbookService.saveVoucher(voucher);
        FcAccbookDTO accbook = dto.getTarget().get(0).getAccbook();
        iAccebillService.billAccbookOp(id, menuType, null, accbook.getBusGlobalId(), null, user);
        notificationComplete(menuType, id, user);
    }
    /**
     * 功能介绍 : 记账提交凭证对象
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, String id, String menuType, ICurrentUser user, String pageType) {
        check(fcAccbook);
        boolean isAutoCheck = this.isAutoCheck();
        String opType = fcAccbook.getOpType().name();
        //开户
        validata(fcAccbook.getAccbook().getBusGlobalId());
        //this.createFcAcc(fcAccbook.getBookDtls(),user,opType);
        setAccBookVoucher(fcAccbook, null, menuType);
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        if (isAutoCheck) { //获取自动复核员用户 setCheckUser(voucher);
            voucher.setAutoCheck(true);
        }
        voucher=iFcAccbookService.submitVoucher(voucher);
        if (isAutoCheck) {
            iAccebillService.billAccbookOp(id, menuType, "1", voucher.getBusGlobalId(), null, user);
        } else {
            iAccebillService.billAccbookOp(id, menuType, "0", voucher.getBusGlobalId(), null, user);
        }
        // 如果自动复核，则复核凭证通过
        completeEditNotificationInfo(voucher, user, pageType);
        completeCheckNotificationInfo(voucher, user, pageType);
        if (!isAutoCheck) {
            sendCheckNotificationInfo(voucher, user, pageType);
        }
        notificationComplete(menuType, id, user);
    }
    /***
     * 打回
     */
    public void backFcAccbook(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user, String menuType, String pageType) {
        createVoucher(fcAccbook, menuType);
        String opType = fcAccbook.getOpType().name();
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getCorpId());
        //Voucher.getTarget().get(0).getAccbook().setId(entity.getBookId());
        completeEditNotificationInfo(Voucher, user, pageType);
        completeCheckNotificationInfo(Voucher, user, pageType);
        if ("MODIFY".equals(opType)) {
            validata(Voucher.getBusGlobalId());
            iFcAccbookService.deleteVoucher(Voucher);
            iAccebillService.billAccbookOp(id, menuType, "2", fcAccbook.getAccbook().getBusGlobalId(), null, user);
            Map<String, Object> map = new HashMap<String, Object>();
            AbBillAccbookInfoDTO info = iAccebillService.getAbVoucherinfoVOById(id);
            map.put("money", NumberUtil.formatNumeric(info.getBillMoney(), "#,##0.00"));
            String notification = "";
            if ("61".equals(menuType) || "62".equals(menuType) || "63".equals(menuType) || "64".equals(menuType) || "65".equals(menuType)) {
                notification = NotificationConstant.FINACIAL.ABBILL_OUT_PUT_TALLY_6;
            }
            if ("71".equals(menuType)) {
                notification = NotificationConstant.FINACIAL.ABBILL_REFUND_TALLY_7;
            }
            if ("81".equals(menuType) || "82".equals(menuType)) {
                notification = NotificationConstant.FINACIAL.ABBILL_COLLECTION_TALLY_8;
            }
            if ("91".equals(menuType) || "92".equals(menuType)) {
                notification = NotificationConstant.FINACIAL.ABBILL_ENDORSE_TALLY_9;
            }
            if ("101".equals(menuType) || "102".equals(menuType) || "103".equals(menuType) || "104".equals(menuType) || "105".equals(menuType) || "106".equals(menuType)) {
                notification = NotificationConstant.FINACIAL.ABBILL_DISCOUNT_TALLY_10;
            }
            if ("151".equals(menuType) || "152".equals(menuType) || "153".equals(menuType) || "154".equals(menuType)) {
                notification = NotificationConstant.FINACIAL.ABBILL_PLEDGE_TALLY_11;
            }
            if ("161".equals(menuType) || "162".equals(menuType) || "163".equals(menuType)) {
                notification = NotificationConstant.FINACIAL.ABBILL_COLLECTION_SELF_TALLY_12;
            }
            if ("171".equals(menuType) || "172".equals(menuType) || "173".equals(menuType) || "174".equals(menuType) || "175".equals(menuType)) {
                notification = NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_TALLY_13;
            }
            if ("181".equals(menuType) || "182".equals(menuType)) {
                notification = NotificationConstant.FINACIAL.ABBILL_PAPER_TALLY_14;
            }
            notificationService.sendToUserByResPermission(id, ResourceConstant.FINACIAL_ABBILLTALLY_APPLY, notification, map, info.getCorpId());
        } else if ("CHECK".equals(opType)) {
            validataBack(Voucher.getBusGlobalId());
            iFcAccbookService.backVoucher(Voucher);
            sendBackNotificationInfo(Voucher, pageType);
        } else if ("BACK".equals(opType)) {
            iAccebillService.billAccbookOp(id, menuType, "-1", fcAccbook.getAccbook().getBusGlobalId(), fcAccbook.getBookExt().getInvalidReason(), user);
            finacialTallyRemindService.deleteByBusId(id);
            notificationComplete(menuType, id, user);
        }
    }
    /**
     * 构造复核传输对象
     */
    public void createVoucher(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user, String menuType, String pageType) {
        createVoucher(fcAccbook, menuType);
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getId());
        checkFcAccbook(Voucher, id, user, menuType, pageType);
    }
    /***
     * 复核
     */
    public void checkFcAccbook(VoucherWrapperDTO Voucher, String id, ICurrentUser user, String menuType, String pageType) {
        validata(Voucher.getBusGlobalId());
        validataBack(Voucher.getBusGlobalId());
        //Voucher.getTarget().get(0).getAccbook().setId(entity.getBookId());
        iFcAccbookService.checkVoucher(Voucher);
        iAccebillService.billAccbookOp(id, menuType, "1", Voucher.getBusGlobalId(), null, user);
        completeCheckNotificationInfo(Voucher, user, pageType);
        completeEditNotificationInfo(Voucher, user, pageType);
    }
    /**
     * 功能介绍 : 初始化手工记账凭证
     */
    private void setAccBookVoucher(FcAccbookVoucherDTO voucher, Integer type, String menuType) {
        if (type != null) {
            voucher.getAccbook().setBusOriType(type);
        }
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
    // 凭证封装对象
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);// 设置用户
        return voucher;
    }
    /**
     * 元素编辑控制
     */
    public List<VoucherRenderControlVo> getControlBeanEdit(int size) {
        List<VoucherRenderControlVo> controBeans = new ArrayList<VoucherRenderControlVo>();
        VoucherRenderControlVo controBean = null;
        for (int i = 0; i < size; i++) {
            controBean = new VoucherRenderControlVo();
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
        return entity;//ok
    }
    /**
     * 获取业务总类id
     */
    public FcBusRaceVo getFcBusRaceId(String netCode, String sysRaceCode, String userId) {
        CorpDTO corp = iCorpService.getNetByCorpId(netCode);
        netCode = corp.getNetId();
//        List<FcBusRaceVo> raceList = busRaceService.findBySysRaceCode(sysRaceCode);
        List<FcBusRaceDTO> fcBusRaceDTOS = iFcBusRaceService.findBySysRaceCode(sysRaceCode);
        List<FcBusRaceVo> raceList = BeanUtils.copyListProperties(fcBusRaceDTOS, FcBusRaceVo.class);
        FcBusRaceVo tempRace = null;
        for (FcBusRaceVo busRace : raceList) {
            String netId = busRace.getNetId();
            if (!StringUtils.isEmpty(netId) && netCode.equals(netId)) {
                tempRace = busRace;
                break;
            } else if (EmptyUtils.isEmpty(netId)) {
                tempRace = busRace;
            }
        }
        if (null == tempRace) {
            tempRace = new FcBusRaceVo();
            tempRace.setNetId(netCode);
            tempRace.setRaceCode(raceList.get(0).getRaceCode());
            tempRace.setRaceName(raceList.get(0).getRaceName());
            tempRace.setSysRaceCode(raceList.get(0).getSysRaceCode());
            /*FcBusRace busRace = new FcBusRace();
            BeanUtils.copyProperties(tempRace,busRace);*/
//            busRaceService.save(busRace, new String[] { netCode }, userId);
            FcBusRaceDTO fcBusRaceDTO = new FcBusRaceDTO();
            BeanUtils.copyProperties(tempRace, fcBusRaceDTO);
            iFcBusRaceService.save(fcBusRaceDTO, new String[]{netCode}, userId);
            /*raceList = busRaceService.findBySysRaceCode(sysRaceCode);
            tempRace = raceList.get(0);*/
            List<FcBusRaceDTO> fcBusRaceDTOS1 = iFcBusRaceService.findBySysRaceCode(sysRaceCode);
            FcBusRaceDTO fcBusRaceDTO1 = fcBusRaceDTOS1.get(0);
            BeanUtils.copyProperties(fcBusRaceDTO1, tempRace);
        }
        return tempRace;
    }
    private void validata(String busGlobalId) {
        if (EmptyUtils.isNotEmpty(busGlobalId)) {
//            FcAccbook info = fcAccbookService.getAccbookByBusGlobalId(busGlobalId);
            FcAccbookDTO info = iFcAccbookService.getAccbookByBusGlobalId(busGlobalId);
            if (info != null) {
                if (info.getVoucherState().equals("N")) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISPASS);
                }
            } else {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
        }
    }
    private void validataBack(String busGlobalId) {
        if (EmptyUtils.isNotEmpty(busGlobalId)) {
//            FcAccbook info = fcAccbookService.getAccbookByBusGlobalId(busGlobalId);
            FcAccbookDTO info = iFcAccbookService.getAccbookByBusGlobalId(busGlobalId);
            if (info != null) {
                if (info.getVoucherState().equals("F") || info.getVoucherType().equals("")) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISBACK);
                }
            } else {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
        }
    }
    private void createVoucher(FcAccbookVoucherDTO voucher, String menuType) {
        voucher.getAccbook().setBusOriType(getOriType(menuType));
    }
    /**
     * 返回记账的业务种类
     */
    public Integer getOriType(String menuType) {
        ConstantBean<Integer> busOriType = FinacialManualConstant.MANUALTYPE_BUSORITYPE_MAP.get(menuType);
        return busOriType.getValue();
    }
    /**
     * 内部帐号开户
     */
    private void createFcAcc(List<FcAccbookDtlDTO> bookDtls, ICurrentUser user, String opType) {
        if (!EmptyUtils.isEmptyList(bookDtls)) {
            for (FcAccbookDtlDTO dtl : bookDtls) {
                FcAccDTO accDTO = dtl.getAccDTO();
                if (!EmptyUtils.isEmpty(accDTO)) {
                    if ("SUBMIT".equals(opType)) {
                        accDTO.setAutoCheck(true);
                        // todo:数据转换
                        com.bytter.system.api.dto.FcAccDTO fcAccDTO = BeanUtils.copyNewInstanceProperties(accDTO, com.bytter.system.api.dto.FcAccDTO.class);
                        innerAccountService.saveOrUpdate(fcAccDTO, user);
                        dtl.setAccDTO(null);
                    } else {
                        com.bytter.system.api.dto.FcAccDTO account = iFcAccService.getAccDTOByAccount(accDTO.getBlnctrAcc());
                        BeanUtils.copyNotNullProperties(accDTO, account);
                        innerAccountService.updateFcAcc(account);
                    }
                }
            }
        }
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
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user, String pageType) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            if ("6".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_OUT_PUT_EDIT_6, accbook.getId(), user.getId());
            } else if ("7".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_REFUND_EDIT_7, accbook.getId(), user.getId());
            } else if ("8".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_COLLECTION_EDIT_8, accbook.getId(), user.getId());
            } else if ("9".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_ENDORSE_EDIT_9, accbook.getId(), user.getId());
            } else if ("10".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_DISCOUNT_EDIT_10, accbook.getId(), user.getId());
            } else if ("11".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_PLEDGE_EDIT_11, accbook.getId(), user.getId());
            } else if ("12".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_COLLECTION_SELF_EDIT_12, accbook.getId(), user.getId());
            } else if ("13".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_EDIT_13, accbook.getId(), user.getId());
            } else if ("14".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_PAPER_EDIT_14, accbook.getId(), user.getId());
            }
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user, String pageType) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            if ("6".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_OUT_PUT_CHECK_6, accbook.getId(), user.getId());
            } else if ("7".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_REFUND_CHECK_7, accbook.getId(), user.getId());
            } else if ("8".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_COLLECTION_CHECK_8, accbook.getId(), user.getId());
            } else if ("9".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_ENDORSE_CHECK_9, accbook.getId(), user.getId());
            } else if ("10".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_DISCOUNT_CHECK_10, accbook.getId(), user.getId());
            } else if ("11".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_PLEDGE_CHECK_11, accbook.getId(), user.getId());
            } else if ("12".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_COLLECTION_SELF_CHECK_12, accbook.getId(), user.getId());
            } else if ("13".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_CHECK_13, accbook.getId(), user.getId());
            } else if ("14".equals(pageType)) {
                notificationService.complete(NotificationConstant.FINACIAL.ABBILL_PAPER_CHECK_14, accbook.getId(), user.getId());
            }
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user, String pageType) {
        for (FcAccbookVoucherDTO voucherDTO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherDTO.getAccbook();
            // 发送消息复核提醒
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherDTO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                for (FcAccbookDtlDTO accbookDtl : bookDtls) {
                    if (null != accbookDtl.getDebitMoney()) {
                        money = money.add(accbookDtl.getDebitMoney());
                    }
                }
            }
            notifyVO.setMoney(money);
            if ("6".equals(pageType)) {
                notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ABBILL_OUT_PUT_CHECK_6, notifyVO, user.getId());
            } else if ("7".equals(pageType)) {
                notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ABBILL_REFUND_CHECK_7, notifyVO, user.getId());
            } else if ("8".equals(pageType)) {
                notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ABBILL_COLLECTION_CHECK_8, notifyVO, user.getId());
            } else if ("9".equals(pageType)) {
                notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ABBILL_ENDORSE_CHECK_9, notifyVO, user.getId());
            } else if ("10".equals(pageType)) {
                notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ABBILL_DISCOUNT_CHECK_10, notifyVO, user.getId());
            } else if ("11".equals(pageType)) {
                notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ABBILL_PLEDGE_CHECK_11, notifyVO, user.getId());
            } else if ("12".equals(pageType)) {
                notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ABBILL_COLLECTION_SELF_CHECK_12, notifyVO, user.getId());
            } else if ("13".equals(pageType)) {
                notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_CHECK_13, notifyVO, user.getId());
            } else if ("14".equals(pageType)) {
                notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ABBILL_PAPER_CHECK_14, notifyVO, user.getId());
            }
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendBackNotificationInfo(VoucherWrapperDTO voucher, String pageType) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherVO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                for (FcAccbookDtlDTO accbookDtl : bookDtls) {
                    if (null != accbookDtl.getDebitMoney()) {
                        money = money.add(accbookDtl.getDebitMoney());
                    }
                }
            }
            notifyVO.setMoney(money);
            if ("6".equals(pageType)) {
                notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ABBILL_OUT_PUT_EDIT_6, notifyVO);
            } else if ("7".equals(pageType)) {
                notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ABBILL_REFUND_EDIT_7, notifyVO);
            } else if ("8".equals(pageType)) {
                notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ABBILL_COLLECTION_EDIT_8, notifyVO);
            } else if ("9".equals(pageType)) {
                notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ABBILL_ENDORSE_EDIT_9, notifyVO);
            } else if ("10".equals(pageType)) {
                notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ABBILL_DISCOUNT_EDIT_10, notifyVO);
            } else if ("11".equals(pageType)) {
                notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ABBILL_PLEDGE_EDIT_11, notifyVO);
            } else if ("12".equals(pageType)) {
                notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ABBILL_COLLECTION_SELF_EDIT_12, notifyVO);
            } else if ("13".equals(pageType)) {
                notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_EDIT_13, notifyVO);
            } else if ("14".equals(pageType)) {
                notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ABBILL_PAPER_EDIT_14, notifyVO);
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
     * 待记账查询
     */
    public List<AbBillAccbookInfoDTO> queryTallyAbAccbook(String menuType) {
        return iAccebillService.queryTallyAbAccbook(menuType);
    }
    public void notificationComplete(String menuType, String id, ICurrentUser user) {
        AbBillAccbookInfoDTO info = iAccebillService.getAbVoucherinfoVOById(id);
        if ("61".equals(menuType) || "62".equals(menuType) || "63".equals(menuType) || "64".equals(menuType) || "65".equals(menuType)) {
            notificationService.complete(NotificationConstant.FINACIAL.ABBILL_OUT_PUT_TALLY_6, info.getAid(), user.getId());
        }
        if ("71".equals(menuType)) {
            notificationService.complete(NotificationConstant.FINACIAL.ABBILL_REFUND_TALLY_7, info.getAid(), user.getId());
        }
        if ("81".equals(menuType) || "82".equals(menuType)) {
            notificationService.complete(NotificationConstant.FINACIAL.ABBILL_COLLECTION_TALLY_8, info.getAid(), user.getId());
        }
        if ("91".equals(menuType) || "92".equals(menuType)) {
            notificationService.complete(NotificationConstant.FINACIAL.ABBILL_ENDORSE_TALLY_9, info.getAid(), user.getId());
        }
        if ("101".equals(menuType) || "102".equals(menuType) || "103".equals(menuType) || "104".equals(menuType) || "105".equals(menuType) || "106".equals(menuType)) {
            notificationService.complete(NotificationConstant.FINACIAL.ABBILL_DISCOUNT_TALLY_10, info.getAid(), user.getId());
        }
        if ("151".equals(menuType) || "152".equals(menuType) || "153".equals(menuType) || "154".equals(menuType)) {
            notificationService.complete(NotificationConstant.FINACIAL.ABBILL_PLEDGE_TALLY_11, info.getAid(), user.getId());
        }
        if ("161".equals(menuType) || "162".equals(menuType) || "163".equals(menuType)) {
            notificationService.complete(NotificationConstant.FINACIAL.ABBILL_COLLECTION_SELF_TALLY_12, info.getAid(), user.getId());
        }
        if ("171".equals(menuType) || "172".equals(menuType) || "173".equals(menuType) || "174".equals(menuType) || "175".equals(menuType)) {
            notificationService.complete(NotificationConstant.FINACIAL.ABBILL_DISCOUNT_SELF_TALLY_13, info.getAid(), user.getId());
        }
        if ("181".equals(menuType) || "182".equals(menuType)) {
            notificationService.complete(NotificationConstant.FINACIAL.ABBILL_PAPER_TALLY_14, info.getAid(), user.getId());
        }
    }
}
