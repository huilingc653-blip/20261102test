package com.bytter.biz.receipt.service;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import com.bytter.biz.bdlink.api.enums.ReceiptMessageEnum;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.api.service.IReceiptQueryService;
import com.bytter.biz.finacial.api.vo.EAccbookBlnedVo;
import com.bytter.biz.finacial.api.vo.ELoanBillInfoVo;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.netbank.vo.FcVoucherTypeVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.receipt.dao.ReceiptQueryDao;
import com.bytter.core.base.AbstractService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 签章类型维护
 * @date 2017-06-05 10:52:28
 */
@Service("receiptQueryService")
@Transactional
public class ReceiptQueryService extends AbstractService {
    @Autowired
    private ReceiptQueryDao receiptQueryDao;
    @DubboReference(check = false)
    private IReceiptQueryService iReceiptQueryService;
    @DubboReference(check = false)
	private IFcDayCloseService fcDayCloseService;
    @Autowired
    private EPrecativeLoanBillService eprecativeLoanBillService;
    @Autowired
    private ETransBillService etransBillService;
    @Autowired
    private EAccrualBillService eaccrualBillService;
    @Autowired
    private ELoanBillService eloanBillService;
    @Autowired
    private EPoundageBillService epoundageBillService;
    @Autowired
    private ENoticeBillService enoticeBillService;
    @Autowired
    private EConsignBillService econsignBillService;
    @Autowired
    private EAcceptBillService eacceptBillService;
    @Autowired
    private EExchangeBillService eexchangeBillService;
    @Autowired
    private ECompareBillService eCompareBillService;
    @Autowired
    private FcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    /**
     * 根据网点ID获取该网点的日结日期
     *
     * @param netId
     * @return 日结日期 yyyy-MM-dd
     */
    public String getDayOverByNetId(String netId) {
        Date dayOver = fcDayCloseService.getDayCloseFinalDate(netId);
        if (dayOver == null) {
            throw new BusinessException(ReceiptMessageEnum.RECEIPT_NET_DAYOVER_MESSAGE_ERROR);
        }
        return DateUtils.date2Str(dayOver, DateUtils.date_sdf());
    }
    /**
     * 电子签章处理
     *
     * @param netId        网点ID
     * @param beginDateStr 开始日期
     * @param endDateStr   结束日期
     * @param signType   签章类型
     * @param user
     */
    public void signature(String netId, String beginDateStr, String endDateStr, String signType,ICurrentUser user) {
        Date beginDate = DateUtils.str2Date(beginDateStr, DateUtils.date_sdf());
        Date endDate = DateUtils.str2Date(endDateStr, DateUtils.date_sdf());
        Date dayOver = fcDayCloseService.getDayCloseFinalDate(netId);
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", beginDate);
        params.put("endDate", DateUtils.addDay(endDate, 1));
        params.put("dayOver", dayOver);
        params.put("netId", netId);
        if(signType.compareTo("3") > 0 || EmptyUtils.isEmpty(signType) || signType.equals("10")) {
            List<EAccbookBlnedVo> accbookBlneInfo = iReceiptQueryService.listAccbookBlneInfo(params);
            if (accbookBlneInfo != null) {
                Map<String , FcVoucherTypeDTO> fcVoucherTypeDTOMap = fcVoucherTypeService.getFcVoucherTypeIdMap();
                ArrayList<String> orderNoList = new ArrayList<>();
                for (EAccbookBlnedVo eAccbookBlnedVo : accbookBlneInfo) {
                    orderNoList.add(eAccbookBlnedVo.getOrderNo());
                    if(StringUtils.isNotBlank(eAccbookBlnedVo.getVoucherTypeId())) {
                        eAccbookBlnedVo.setVoucherType(fcVoucherTypeDTOMap.get(eAccbookBlnedVo.getVoucherTypeId()).getSysVoucherCode());
                    }
                }
                HashMap<String, List<ELoanBillInfoVo>> debitInfoHashMap = iReceiptQueryService.listAccbookBlnedAccToHashMap(orderNoList, null, true, false);
                HashMap<String, List<ELoanBillInfoVo>> creditInfoHashMap = iReceiptQueryService.listAccbookBlnedAccToHashMap(orderNoList, null, false, true);
                for (EAccbookBlnedVo accbookInfo : accbookBlneInfo) {
                    List<ELoanBillInfoVo> debitInfoList = debitInfoHashMap.get(accbookInfo.getOrderNo());
                    if (debitInfoList == null || debitInfoList.isEmpty()) {
                        continue;
                    }
                    int debitCnt = debitInfoList.size();
                    List<ELoanBillInfoVo> creditInfoList = creditInfoHashMap.get(accbookInfo.getOrderNo());
                    if (creditInfoList == null || creditInfoList.isEmpty()) {
                        continue;
                    }
                    int creditCnt = creditInfoList.size();
                    //借贷类型
                    String type = "";
                    String sumDebitAcc = "";
                    String sumCreditAcc = "";
                    String sumInterestAcc = "";//利息结算账号
                    if (debitCnt == 1 && creditCnt == 1) {
                        //一借一贷
                        type = ReceiptConstant.BILL_TYPE_B;
                        sumDebitAcc = debitInfoList.get(0).getAccNo();
                        sumCreditAcc = creditInfoList.get(0).getAccNo();
                    } else if (debitCnt == 1 && creditCnt > 1) {
                        //一借多贷
                        type = ReceiptConstant.BILL_TYPE_C;
                        sumDebitAcc = debitInfoList.get(0).getAccNo();
                        for(ELoanBillInfoVo vo:creditInfoList){
                            //,fs.sys_subject_code as sysSubjectCode,fs.subject_name as sysSubjectCodeName
                            List<FcAccDTO> fcAccList= iFcAccService.queryBySomeCondPgd(" and t.blnctr_acc='"+vo.getAccNo()+"'");
                            for(FcAccDTO fcAccDTO:fcAccList){
                                vo.setSysSubjectCode(fcAccDTO.getSysSubjectCode());
                                vo.setSysSubjectCodeName(fcAccDTO.getSubjectName());
                            }
                            if(StringUtils.isNotBlank(vo.getSysSubjectCode())) {
                                //过滤利息的分录
                                if (!SubjectConstant.SYSSUBJECT_INTEREST_INCOME.equals(vo.getSysSubjectCode().substring(0, 3))
                                        && !SubjectConstant.SYSSUBJECT_INTEREST_EXPENSES.equals(vo.getSysSubjectCode().substring(0, 3))) {
                                    sumInterestAcc = vo.getAccNo();
                                    break;
                                }
                            }
                        }
                    } else if (debitCnt > 1 && creditCnt == 1) {
                        //多借一贷
                        type = ReceiptConstant.BILL_TYPE_D;
                        sumCreditAcc = creditInfoList.get(0).getAccNo();
                        for(ELoanBillInfoVo vo:debitInfoList){
                            //,fs.sys_subject_code as sysSubjectCode,fs.subject_name as sysSubjectCodeName
                            List<FcAccDTO> fcAccList= iFcAccService.queryBySomeCondPgd(" and t.blnctr_acc='"+vo.getAccNo()+"'");
                            for(FcAccDTO fcAccDTO:fcAccList){
                                vo.setSysSubjectCode(fcAccDTO.getSysSubjectCode());
                                vo.setSysSubjectCodeName(fcAccDTO.getSubjectName());
                            }
                            if(StringUtils.isNotBlank(vo.getSysSubjectCode())) {
                                //过滤利息的分录
                                if (!SubjectConstant.SYSSUBJECT_INTEREST_INCOME.equals(vo.getSysSubjectCode().substring(0, 3))
                                        && !SubjectConstant.SYSSUBJECT_INTEREST_EXPENSES.equals(vo.getSysSubjectCode().substring(0, 3))) {
                                    sumInterestAcc = vo.getAccNo();
                                    break;
                                }
                            }
                        }
                    } else if (debitCnt > 1 && creditCnt > 1) {
//					--多借多贷
//			       --委托贷款还款 busOriType=47
                        if(signType.equals("4") || EmptyUtils.isEmpty(signType.trim())) {
                            opPrecativeBill(accbookInfo, debitCnt, creditCnt, user);
//					--委托贷款 busOriType=46
                            opPrecativeBill(accbookInfo, type, user);
                        }
                        //continue;
                    }
//				--判断回单类型
                    if(signType.equals("5") || EmptyUtils.isEmpty(signType.trim())) {
//			      --账户转换电子回单
                        opTransBill(accbookInfo, user);
                    }
//			      --利息清单电子回单
//				opAccrualsBill(accbookInfo,params);
                    //对其他记账明细判断业务处理
                    List<ELoanBillInfoVo> accbookDtlList = iReceiptQueryService.listAccbookBlnedAcc(accbookInfo.getOrderNo(), null);
                    if (accbookDtlList != null && !accbookDtlList.isEmpty()) {
                        for (ELoanBillInfoVo accbookDtlInfo : accbookDtlList) {
                            if(signType.equals("6") || EmptyUtils.isEmpty(signType.trim())) {
//						--贷款凭证电子回单
                                if (opLoanBill(accbookInfo, accbookDtlInfo, type, sumDebitAcc, sumCreditAcc, user)) {
                                    continue;
                                }
                            }
                            if(signType.equals("7") || EmptyUtils.isEmpty(signType.trim())) {
//				          --手续费电子回单
                                if (opPoundageBill(accbookInfo, accbookDtlInfo, type, sumDebitAcc, sumCreditAcc, user)) {
                                    continue;
                                }
                            }
                            if(signType.equals("8") || EmptyUtils.isEmpty(signType.trim())) {
//				          --通知单电子回单
                                if (opNoticeBill(accbookInfo, accbookDtlInfo, type, sumDebitAcc, sumCreditAcc, sumInterestAcc, user)) {
                                    continue;
                                }
                            }
                            if(signType.equals("9") || EmptyUtils.isEmpty(signType.trim())) {
//				          --委托收款电子回单
                                if (opConsignBill(accbookInfo, accbookDtlInfo, type, sumDebitAcc, sumCreditAcc, netId, user)) {
                                    continue;
                                }
                            }
                            if(signType.equals("10") || EmptyUtils.isEmpty(signType.trim())) {
//				          --承兑汇票电子回单
                                if (opAcceptBill(accbookInfo, accbookDtlInfo, type, sumDebitAcc, sumCreditAcc, user)) {
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
        }
//		--结售汇
        if(signType.equals("2") || EmptyUtils.isEmpty(signType.trim())) {
            opExchangeBill(params, user);
        }
//		委托贷款利息清单
        if(signType.equals("1") || EmptyUtils.isEmpty(signType.trim())) {
            opAccrualsBill(params, user);
            opAccrualsBillOthers(params, user);
        }
//		对账单电子回单
        if(signType.equals("3") || EmptyUtils.isEmpty(signType.trim())) {
            opCompareBill(params, user);
        }
    }
    /**
     * 承兑汇票电子回单
     *
     * @param accbookInfo    记账凭证主信息
     * @param accbookDtlInfo 记账凭证明细信息
     * @param type           借贷类型
     * @param sumDebitAcc    借方账户
     * @param sumCreditAcc   贷方账户
     *                       return 是否跳出本次循环
     */
    public boolean opAcceptBill(EAccbookBlnedVo accbookInfo, ELoanBillInfoVo accbookDtlInfo
            , String type, String sumDebitAcc, String sumCreditAcc, ICurrentUser user) {
//		--承兑汇票电子回单
        if (FinaBookConstant.GenerateMode.ACCEPTANCE_BOOK.getValue().equals(accbookInfo.getGenerateMode())) {
            if (ReceiptConstant.BILL_TYPE_B.equals(type)) {
//					--如果是借方不执行
                if (accbookDtlInfo.getAccNo().equals(sumDebitAcc)) {
                    return true;
                }
                eacceptBillService.createAcceptBill(
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getVoucherState(),
                        sumDebitAcc,
                        sumCreditAcc,
                        accbookInfo.getOrigVoucherNo(),
                        accbookInfo.getOrigVoucherId(),
                        accbookInfo.getBusGlobalId(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherTypeId(),
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(), user);
            } else if (ReceiptConstant.BILL_TYPE_D.equals(type)) {
                if (accbookDtlInfo.getAccNo().equals(sumCreditAcc)) {
                    return true;
                }
                eacceptBillService.createAcceptBill(
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getVoucherState(),
                        accbookDtlInfo.getAccNo(),
                        sumCreditAcc,
                        accbookInfo.getOrigVoucherNo(),
                        accbookInfo.getOrigVoucherId(),
                        accbookInfo.getBusGlobalId(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherTypeId(),
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(), user);
            } else if (ReceiptConstant.BILL_TYPE_C.equals(type)) {
                if (accbookDtlInfo.getAccNo().equals(sumDebitAcc)) {
                    return true;
                }
                eacceptBillService.createAcceptBill(
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getVoucherState(),
                        sumDebitAcc,
                        accbookDtlInfo.getAccNo(),
                        accbookInfo.getOrigVoucherNo(),
                        accbookInfo.getOrigVoucherId(),
                        accbookInfo.getBusGlobalId(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherTypeId(),
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(), user);
            }
        }
        return false;
    }
    /**
     * 委托收款电子回单
     *
     * @param accbookInfo    记账凭证主信息
     * @param accbookDtlInfo 记账凭证明细信息
     * @param type           借贷类型
     * @param sumDebitAcc    借方账户
     * @param sumCreditAcc   贷方账户
     * @param netId          网点ID
     *                       return 是否跳出本次循环
     */
    public boolean opConsignBill(EAccbookBlnedVo accbookInfo, ELoanBillInfoVo accbookDtlInfo
            , String type, String sumDebitAcc, String sumCreditAcc, String netId, ICurrentUser user) {
//		--委托收款电子回单
        if (FinaBookConstant.GenerateMode.ENTRUST_COLLECT_BOOK.getValue().equals(accbookInfo.getGenerateMode())) {
            int num = receiptQueryDao.getCgsBillNum(accbookInfo.getOrderNo());
            if (num > 0) {
                if (ReceiptConstant.BILL_TYPE_B.equals(type)) {
//					--如果是借方不执行
                    if (accbookDtlInfo.getAccNo().equals(sumDebitAcc)) {
                        return true;
                    }
                    econsignBillService.createConsignBill(
                            accbookInfo.getOrderNo(),
                            accbookInfo.getVoucherNo(),
                            accbookInfo.getTallyDate(),
                            accbookInfo.getVoucherState(),
                            sumDebitAcc,
                            sumCreditAcc,
                            accbookInfo.getVoucherTypeId(),
                            accbookInfo.getOrigVoucherNo(),
                            netId,
                            accbookInfo.getOperatorCode(),
                            accbookInfo.getCheckerCode(), user);
                } else if (ReceiptConstant.BILL_TYPE_D.equals(type)) {
                    if (accbookDtlInfo.getAccNo().equals(sumCreditAcc)) {
                        return true;
                    }
                    econsignBillService.createConsignBill(
                            accbookInfo.getOrderNo(),
                            accbookInfo.getVoucherNo(),
                            accbookInfo.getTallyDate(),
                            accbookInfo.getVoucherState(),
                            accbookDtlInfo.getAccNo(),
                            sumCreditAcc,
                            accbookInfo.getVoucherTypeId(),
                            accbookInfo.getOrigVoucherNo(),
                            netId,
                            accbookInfo.getOperatorCode(),
                            accbookInfo.getCheckerCode(), user);
                } else if (ReceiptConstant.BILL_TYPE_C.equals(type)) {
                    if (accbookDtlInfo.getAccNo().equals(sumDebitAcc)) {
                        return true;
                    }
                    econsignBillService.createConsignBill(
                            accbookInfo.getOrderNo(),
                            accbookInfo.getVoucherNo(),
                            accbookInfo.getTallyDate(),
                            accbookInfo.getVoucherState(),
                            sumDebitAcc,
                            accbookDtlInfo.getAccNo(),
                            accbookInfo.getVoucherTypeId(),
                            accbookInfo.getOrigVoucherNo(),
                            netId,
                            accbookInfo.getOperatorCode(),
                            accbookInfo.getCheckerCode(), user);
                }
            }
        }
        return false;
    }
    /**
     * 通知单电子回单
     *
     * @param accbookInfo    记账凭证主信息
     * @param accbookDtlInfo 记账凭证明细信息
     * @param type           借贷类型
     * @param sumDebitAcc    借方账户
     * @param sumCreditAcc   贷方账户
     *                       return 是否跳出本次循环
     */
    public boolean opNoticeBill(EAccbookBlnedVo accbookInfo, ELoanBillInfoVo accbookDtlInfo
            , String type, String sumDebitAcc, String sumCreditAcc, String sumInterestAcc, ICurrentUser user) {
//        --通知单电子回单
        if (!NetbankConstant.PAY_TYPE_09.equals(accbookInfo.getVoucherType())
                && !FinaBookConstant.GenerateMode.INTEREST.getValue().equals(accbookInfo.getGenerateMode())
                && !FinaBookConstant.GenerateMode.HANDLE_CHARGE.getValue().equals(accbookInfo.getGenerateMode())
                && !FinaBookConstant.GenerateMode.ENTRUST_COLLECT_BOOK.getValue().equals(accbookInfo.getGenerateMode())) {
            if (ReceiptConstant.BILL_TYPE_B.equals(type)) {
//					--如果是借方不执行
                if (accbookDtlInfo.getAccNo().equals(sumDebitAcc)) {
                    return true;
                }
                enoticeBillService.createNoticeBill(
                        type,
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getOrigVoucherNo(),
                        accbookInfo.getAffixAmount(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherState(),
                        sumDebitAcc,
                        sumCreditAcc,
                        sumInterestAcc,
                        accbookInfo.getVoucherTypeId(),
                        accbookInfo.getExternalCorp(),
                        accbookInfo.getExternalAcc(),
                        accbookInfo.getExternalBank(),
                        accbookInfo.getRmk(),
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(), user);
            } else if (ReceiptConstant.BILL_TYPE_C.equals(type)) {
                if (accbookDtlInfo.getAccNo().equals(sumDebitAcc)) {
                    return true;
                }
                enoticeBillService.createNoticeBill(
                        type,
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getOrigVoucherNo(),
                        accbookInfo.getAffixAmount(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherState(),
                        sumDebitAcc,
                        accbookDtlInfo.getAccNo(),
                        sumInterestAcc,
                        accbookInfo.getVoucherTypeId(),
                        accbookInfo.getExternalCorp(),
                        accbookInfo.getExternalAcc(),
                        accbookInfo.getExternalBank(),
                        accbookInfo.getRmk(),
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(), user);
            }
        }
        return false;
    }
    /**
     * 手续费电子回单
     *
     * @param accbookInfo    记账凭证主信息
     * @param accbookDtlInfo 记账凭证明细信息
     * @param type           借贷类型
     * @param sumDebitAcc    借方账户
     * @param sumCreditAcc   贷方账户
     *                       return 是否跳出本次循环
     */
    public boolean opPoundageBill(EAccbookBlnedVo accbookInfo, ELoanBillInfoVo accbookDtlInfo
            , String type, String sumDebitAcc, String sumCreditAcc, ICurrentUser user) {
//       --手续费电子回单
        if (NetbankConstant.PAY_TYPE_09.equals(accbookInfo.getVoucherType())
                || FinaBookConstant.GenerateMode.HANDLE_CHARGE.getValue().equals(accbookInfo.getGenerateMode())) {
            if (ReceiptConstant.BILL_TYPE_B.equals(type)) {
//				--如果是借方不执行
                if (accbookDtlInfo.getAccNo().equals(sumDebitAcc)) {
                    return true;
                }
                epoundageBillService.createPoundageBill(
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getOrigVoucherNo(),
                        accbookInfo.getAffixAmount(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherState(),
                        sumDebitAcc,
                        sumCreditAcc,
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(), user);
            } else if (ReceiptConstant.BILL_TYPE_D.equals(type)) {
                if (accbookDtlInfo.getAccNo().equals(sumCreditAcc)) {
                    return true;
                }
                epoundageBillService.createPoundageBill(
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getOrigVoucherNo(),
                        accbookInfo.getAffixAmount(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherState(),
                        accbookDtlInfo.getAccNo(),
                        sumCreditAcc,
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(), user);
            } else if (ReceiptConstant.BILL_TYPE_C.equals(type)) {
                if (accbookDtlInfo.getAccNo().equals(sumDebitAcc)) {
                    return true;
                }
                epoundageBillService.createPoundageBill(
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getOrigVoucherNo(),
                        accbookInfo.getAffixAmount(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherState(),
                        sumDebitAcc,
                        accbookDtlInfo.getAccNo(),
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(), user);
            }
        }
        return false;
    }
    /**
     * 贷款凭证电子回单
     *
     * @param accbookInfo    记账凭证主信息
     * @param accbookDtlInfo 记账凭证明细信息
     * @param type           借贷类型
     * @param sumDebitAcc    借方账户
     * @param sumCreditAcc   贷方账户
     *                       return 是否跳出本次循环
     */
    public boolean opLoanBill(EAccbookBlnedVo accbookInfo, ELoanBillInfoVo accbookDtlInfo
            , String type, String sumDebitAcc, String sumCreditAcc, ICurrentUser user) {
//		--贷款凭证电子回单
        if (SubjectConstant.SysSubject.ENTERPRISE_LOAN.getValue().equals(accbookDtlInfo.getSysSubjectCode())
                || SubjectConstant.SysSubject.ENTERPRISE_LOAN_LONG_TERM.getValue().equals(accbookDtlInfo.getSysSubjectCode())) {
            if (ReceiptConstant.BILL_TYPE_B.equals(type)) {
                eloanBillService.createLoanBill(
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherState(),
                        sumDebitAcc,
                        sumCreditAcc,
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(),
                        (accbookDtlInfo.getDebitMoney().add(accbookDtlInfo.getCreditMoney())),
                        accbookInfo.getOrigVoucherNo(), user);
            } else if (ReceiptConstant.BILL_TYPE_D.equals(type)) {
                if (accbookDtlInfo.getAccNo().equals(sumCreditAcc)) {
                    return true;
                }
                eloanBillService.createLoanBill(
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherState(),
                        accbookDtlInfo.getAccNo(),
                        sumCreditAcc,
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(),
                        accbookDtlInfo.getDebitMoney(),
                        accbookInfo.getOrigVoucherNo(), user);
            } else if (ReceiptConstant.BILL_TYPE_C.equals(type)) {
                if (accbookDtlInfo.getAccNo().equals(sumDebitAcc)) {
                    return true;
                }
                eloanBillService.createLoanBill(
                        accbookInfo.getOrderNo(),
                        accbookInfo.getVoucherNo(),
                        accbookInfo.getTallyDate(),
                        accbookInfo.getInterestStartDate(),
                        accbookInfo.getVoucherState(),
                        sumDebitAcc,
                        accbookDtlInfo.getAccNo(),
                        accbookInfo.getOperatorCode(),
                        accbookInfo.getCheckerCode(),
                        accbookDtlInfo.getCreditMoney(),
                        accbookInfo.getOrigVoucherNo(), user);
            }
        }
        return false;
    }
    /**
     * -判断回单类型  利息清单电子回单
     *
     * @param params
     */
    public void opAccrualsBillOthers(Map<String, Object> params, ICurrentUser user) {
//      --利息清单电子回单
        List<EAccbookBlnedVo> interestList = iReceiptQueryService.listInterestOthersInfo(params);
        if (EmptyUtils.isNotEmpty(interestList)) {
            for (EAccbookBlnedVo interestInfo : interestList) {
                if (FinaBookConstant.GenerateMode.INTEREST.getValue().equals(interestInfo.getGenerateMode())
                        || FinaBookConstant.GenerateMode.PROFIT_BOOK.getValue().equals(interestInfo.getGenerateMode())
                        || FinaBookConstant.GenerateMode.CREDIT_BOOK.getValue().equals(interestInfo.getGenerateMode())
                        || FinaBookConstant.GenerateMode.DEPOSIT.getValue().equals(interestInfo.getGenerateMode())
                        || FinaBookConstant.GenerateMode.INNER_PAY_BOOK.getValue().equals(interestInfo.getGenerateMode())
                        || FinaBookConstant.GenerateMode.CHO_INNER_FINANCE.getValue().equals(interestInfo.getGenerateMode())) {
                    eaccrualBillService.createAccrualsBillNew(
                            interestInfo.getOrderNo(),
                            interestInfo.getVoucherNo(),
                            interestInfo.getAccNo(),
                            interestInfo.getGenerateMode(),
                            interestInfo.getInterestStartDate(),
                            interestInfo.getBlnInstDate(),
                            interestInfo.getOperatorCode(),
                            interestInfo.getCheckerCode(),
                            interestInfo.getOrigVoucherNo(),
                            interestInfo.getOrigVoucherId(),interestInfo.getSummaryContent(),user);
                }
            }
        }
    }
    /**
     * -判断回单类型  账户转换电子回单
     *
     * @param accbookInfo 记账凭证信息
     * @param user
     */
    public void opTransBill(EAccbookBlnedVo accbookInfo, ICurrentUser user) {
//		--判断回单类型
//	      --账户转换电子回单
        if ((NetbankConstant.PAY_TYPE_09.equals(accbookInfo.getVoucherType())
                || !FinaBookConstant.GenerateMode.HANDLE_CHARGE.getValue().equals(accbookInfo.getGenerateMode()))
                && !FinaBookConstant.GenerateMode.ENTRUST_COLLECT_BOOK.getValue().equals(accbookInfo.getGenerateMode())) {

            //查询记账凭证明细信息设置电子回单要素
            etransBillService.createTransBill(
                    accbookInfo.getOrderNo(),
                    accbookInfo.getVoucherNo(),
                    accbookInfo.getTallyDate(),
                    accbookInfo.getVoucherState(),
                    accbookInfo.getOperatorCode(),
                    accbookInfo.getCheckerCode(), user);
        }
    }
    /**
     * --委托贷款 busOriType=46
     *
     * @param accbookInfo 记账凭证信息
     * @param type        借贷类型
     */
    public void opPrecativeBill(EAccbookBlnedVo accbookInfo, String type, ICurrentUser user) {
//		--委托贷款 busOriType=46
        if (
//				ReceiptConstant.BILL_TYPE_B.equals(type) &&
                FinaBookConstant.BusOriType.NB_PRECATIVE_LOAN.getValue().equals(accbookInfo.getBusOriType())) {
            eprecativeLoanBillService.createPrecativeBill(
                    accbookInfo.getVoucherNo(),
                    accbookInfo.getTallyDate(),
                    accbookInfo.getVoucherState(),
                    accbookInfo.getOperatorCode(),
                    accbookInfo.getCheckerCode(),
                    accbookInfo.getBusGlobalId(),
                    accbookInfo.getBusOriType(),
                    accbookInfo.getOrigVoucherNo(), user);
        }
    }
    /**
     * 多借多贷 委托贷款还款 busOriType=47
     *
     * @param accbookInfo 记账凭证信息
     * @param debitCnt    借方明细数
     * @param creditCnt   贷方明细数
     */
    public void opPrecativeBill(EAccbookBlnedVo accbookInfo, int debitCnt, int creditCnt, ICurrentUser user) {
//		--多借多贷
//       --委托贷款还款 busOriType=47
        if (FinaBookConstant.BusOriType.NB_PRECATIVE_REPAYMENT.getValue().equals(accbookInfo.getBusOriType())
                && ((debitCnt == 2 && creditCnt == 2)
                || (debitCnt == 3 && creditCnt == 3))) {
            eprecativeLoanBillService.createPrecativeBill(
                    accbookInfo.getVoucherNo(),
                    accbookInfo.getTallyDate(),
                    accbookInfo.getVoucherState(),
                    accbookInfo.getOperatorCode(),
                    accbookInfo.getCheckerCode(),
                    accbookInfo.getBusGlobalId(),
                    accbookInfo.getBusOriType(),
                    accbookInfo.getOrigVoucherNo(), user);
        }
    }
    /**
     * 结售汇  处理
     *
     * @param params 查询数据参数
     */
    public void opExchangeBill(Map<String, Object> params, ICurrentUser user) {
//		  --外层循环只控制生成多少个单子.

        String[] sysVoucherCodeS = {NetbankConstant.PAY_TYPE_46, NetbankConstant.PAY_TYPE_47};
        List<FcVoucherTypeVO> list =fcVoucherTypeService.getFcVoucherTypeVOBySysVoucherCodeS(sysVoucherCodeS);
        String[] voucherTypes = new String[2];
        if(null !=list&&list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                FcVoucherTypeVO vo = list.get(i);
                voucherTypes[i] = vo.getVoucherType();
            }
            params.put("voucherTypes", voucherTypes);
            //下面查询用的inner join fc_voucher_type vt on vt.id=fc.voucher_type
//		  --首先是非冲账数据
            List<EAccbookBlnedVo> exchangeList = iReceiptQueryService.listAccbookBlneExchangeInfo(params);
            if (exchangeList != null && exchangeList.size() > 0) {
                for (EAccbookBlnedVo exchangeInfo : exchangeList) {
                    eexchangeBillService.createExchangeBill(exchangeInfo.getOrigVoucherNo(), user);
                }
            }
//		--其次是冲账数据
            List<EAccbookBlnedVo> exchangeRushList = iReceiptQueryService.listAccbookBlneRushExchangeInfo(params);
            if (exchangeRushList != null && exchangeRushList.size() > 0) {
                for (EAccbookBlnedVo exchangeInfo : exchangeRushList) {
                    eexchangeBillService.createExchangeBill(exchangeInfo.getOrigVoucherNo(), user);
                }
            }
        }
    }
    /**
     * 委托贷款利息清单 处理
     *
     * @param params 查询数据参数
     */
    public void opAccrualsBill(Map<String, Object> params, ICurrentUser user) {
        params.put("accType", InnerAccountConstant.FCACC_LOANBYMANDATE);
        params.put("subjectCode", SubjectConstant.SysSubject.INTEREST_ENTURST_LOANS.getValue());
        List<EAccbookBlnedVo> interestList = iReceiptQueryService.lisInterestInfo(params);
        if (interestList != null && interestList.size() > 0) {
            for (EAccbookBlnedVo interestInfo : interestList) {
                eaccrualBillService.createAccrualsBill(
                        interestInfo.getOrderNo(),
                        interestInfo.getVoucherNo(),
                        interestInfo.getAccNo(),
                        FinaBookConstant.GenerateMode.PROFIT_BOOK.getValue(),
                        interestInfo.getInterestStartDate(),
                        interestInfo.getBlnInstDate(),
                        null,
                        null, user);
            }
        }
    }
    /**
     * 对账单电子回单
     * @param params
     * @param user
     */
    public void opCompareBill(Map<String, Object> params, ICurrentUser user){
        Date beginDate = (Date)params.get("beginDate");
        Date endDate = (Date)params.get("endDate");
        eCompareBillService.createCompareBill(beginDate,endDate,user);
    }
}
