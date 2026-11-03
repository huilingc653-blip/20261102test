package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbEmitBillPayDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbEmitBillPay;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
// import com.bytter.biz.payment.api.constant.NisMessageConstant;
// import com.bytter.biz.payment.api.dto.NisBillheadDTO;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 兑付确认
 * @date 2017-06-29 16:53:43
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLPAY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLPAY)
@Service("abEmitBillPayService")
@Transactional
public class AbEmitBillPayService extends BaseService<AbVoucherinfo, String>
        implements IAccebillBaseService, IWorkFlowListener, ITaskService {
    @Autowired
    private AbEmitBillPayDao abEmitBillPayDao;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbEmitBillService abEmitBillService;
    @Autowired
    private AbRatUrationService abRatUrationService;
    @Autowired
    private AbEmitBillPayDtlService abEmitBillPayDtlService;
    @Autowired
    private AbNisBillHeadService abNisBillHeadService;
    @Autowired
    private AbFcVoucherTypeService abFcVoucherTypeService;
    @Autowired
    private AbFbsBudgetProcessService abFbsBudgetProcessService;
    @Autowired
    private AbBillNotificationService abBillNotificationService;
    @Autowired
    private AbFcBondHookService abFcBondHookService;
    @Autowired
    private AbGuaranteeService abGuaranteeService;
    @Autowired
    private AbRatCpUrationService abRatCpUrationService;
    @Autowired
    private AbUserService abUserService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @DubboReference(check = false)
	private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
    @Autowired
    private AbInputBankInfoService abInputBankInfoService;
    @DubboReference(check = false)
	private IInputBankInfoService inputBankInfoService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private IBisExcService iBisExcService;
    @Autowired
    private AbClaimService abClaimService;

    @Override
    public BaseDao<AbVoucherinfo, String> getEntityDao() {
        return abEmitBillPayDao;
    }
    private void injectValues(AbEmitBillPayVo bill, Map<String, Map<String, DictDTO>> listDictMap) {
        // 票据类型：10 银票、11 商票
        Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
                    .ifPresent(dict -> bill.setBillTypeName(dict.getName()));
        });
        // 流通介质：10 纸票、11 电票
        Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
                    .ifPresent(dict -> bill.setBillFlagName(dict.getName()));
        });
        // 开出方式：0  对外开出；1 对内自开
        Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_OPENWAY");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
                    .ifPresent(dict -> bill.setInsideSingName(dict.getName()));
        });
        // 业务处理状态：-2已删除；-1打回；0 暂存；1导入;11-94审批中；95 已完成
        Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_AbEmitBillPay_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
                    .ifPresent(dict -> bill.setStatusName(dict.getName()));
        });
        // 业务状态
        Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_AbEmitBillPay_BRIEF_ID");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
                    .ifPresent(dict -> bill.setBriefIdName(dict.getName()));
        });
        // 兑付方式:0或空线下兑付,1线上兑付'
        Optional.ofNullable(bill.getEmitSttlmmk()).ifPresent(emitSttlmmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ABACCRECORDS_EMITSTTLMMK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(emitSttlmmk)))
                    .ifPresent(dict -> bill.setEmitSttlmmkName(dict.getName()));
        });
        // 付款状态
        Optional.ofNullable(bill.getPayStatus()).ifPresent(payStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("nisBillhead.status");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(payStatus+"")))
                    .ifPresent(dict -> bill.setPayStatusName(dict.getName()));
        });
        // 交易状态
        Optional.ofNullable(bill.getVoucherStat()).ifPresent(voucherStat -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("bisExc.voucherStat");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(voucherStat)))
                    .ifPresent(dict -> bill.setVoucherStatName(dict.getName()));
        });
        //出票标志: 1 新系统出票 、0 老系统出票
        Optional.ofNullable(bill.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
                    .ifPresent(dict -> bill.setNewStmIssuofpprIndName(dict.getName()));
        });
    }
    /**
     * 加载对付确认数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbEmitBillPay(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_PAY_QUERY, dataTablesInput.getCurrentUserId());
        dataTablesInput.getFormdata().put(AccebillConstant.AB_CONFIG_ISEMITMAKE, abVoucherinfoViceService.isEmitMake());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_PAY_QUERY, corpIds);
        DataTablesOutput<AbEmitBillPayVo> dataTablesOutput = abEmitBillPayDao.listAbEmitBillPay(dataTablesInput, corpAuthList);
        // 根据兑付申请线上兑付收款人设置情况查询收款人信息
        String isCashingOnlinePayee = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISCASHINGONLINEPAYEE).toFormatString();
        // 银票：默认承兑人
        boolean isBankToAcc = isCashingOnlinePayee.indexOf("1") > -1;
        boolean isBusinessToAcc = isCashingOnlinePayee.indexOf("2") > -1;
        // 商票：默认收票人
        List<AbEmitBillPayVo> data = dataTablesOutput.getData();
        // 账号集合
        Set<String> accSet = new HashSet<>();
        // 联行号集合
        Set<String> bankCodeSet = new HashSet<>();
        if (data != null && !data.isEmpty()) {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG" , "AB_BILL_OPENWAY" , "AB_BILL_AbEmitBillBack_STATUS"
                    , "AB_BILL_AbEmitBillPay_BRIEF_ID", "AB_BILL_ABACCRECORDS_EMITSTTLMMK","AB_BILL_NEW_STM_ISSUOFPPR_IND","AB_BILL_AbEmitBillPay_STATUS");
            for (AbEmitBillPayVo vo : data) {
                // 冗余数据
                injectValues(vo, listDictMap);
                if (StringUtils.isNotEmpty(vo.getPayPayeeacc())) {
                    bankCodeSet.add(vo.getPayPayeeBankNo());
                } else {
                    // 银票银行账号、开户行号保存
                    if (StringUtils.isNotEmpty(vo.getAcceptoracc()) && isBankToAcc && AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(vo.getBillType()))) {
                        vo.setPayPayeeacc(vo.getAcceptoracc());
                        if ("0".equals(vo.getAcceptoracc())) {
                            bankCodeSet.add(vo.getAcceptorbankno());
                        } else {
                            accSet.add(vo.getAcceptoracc());
                        }
                    }
                    // 商票银行账号、开户行号保存
                    if (StringUtils.isNotEmpty(vo.getPayeeacc()) && isBusinessToAcc && AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(vo.getBillType()))) {
                        vo.setPayPayeeacc(vo.getPayeeacc());
                        accSet.add(vo.getPayeeacc());
                        bankCodeSet.add(vo.getPayeebankno());
                    }
                }
            }
        }
        // 根据账号集合获得账号信息
        Map<String, ExternalCorpBankAccDTO> externalCorpBankAccVOMap = new HashMap<>();
        Map<String, BtBankAccDTO> btBankAccDTOMap = new HashMap<>();
        if (StringUtils.isNotEmpty(accSet)) {
            // 根据账号查询客商账户信息
            List<String> acclist = new ArrayList<>(accSet);
            externalCorpBankAccVOMap = externalCorpService.getExternalCorpBankAccByAcc(acclist);
            // 根据账号查询账户信息
            btBankAccDTOMap = iBtBankAccService.getBankAccByInfoAccs(acclist);
            if(CollUtil.isEmpty(btBankAccDTOMap)){
                btBankAccDTOMap = new HashMap<>();
            }
        }
        // 根据联行号集合获得联行号信息
        Map<String, InputBankInfoDTO> bankInfoMap = new HashMap<>();
        if (StringUtils.isNotEmpty(bankCodeSet)) {
            List<String> bankCodelist = new ArrayList<>(bankCodeSet);
            bankInfoMap = inputBankInfoService.getBankCodeInfoByBankCodes(bankCodelist);
        }
        // 添加收款信息
        if (data != null && !data.isEmpty()) {
            for (AbEmitBillPayVo vo : data) {
                // 如果存在审核或打回信息，补充省市信息
                if (StringUtils.isNotEmpty(vo.getPayPayeeacc()) && StringUtils.isNotEmpty(bankInfoMap) && bankInfoMap.containsKey(vo.getPayPayeeBankNo())) {
                    InputBankInfoDTO bankInfo = new InputBankInfoDTO();
                    if(StringUtils.isNotBlank(vo.getPayPayeeBankNo())){
                        bankInfo = bankInfoMap.get(vo.getPayPayeeBankNo());
                    }else {
                        bankInfo = bankInfoMap.get(vo.getPayeebankno());
                    }

                    vo.setProvName(bankInfo.getProvName());// 所在省
                    vo.setCityName(bankInfo.getCityName());// 所在市
                    vo.setPayPayeeBankNo(bankInfo.getBankCode());//行号
                    vo.setPayPayeeBankName(bankInfo.getBankName());
                    BtBankAccDTO bankAccDTO = btBankAccDTOMap.get(vo.getPayPayeeacc());
                    if (ObjectUtil.isNotEmpty(bankAccDTO)){
                        vo.setPayPayeeName(bankAccDTO.getAccName());
                    }
                } else {
                    // 银票：默认承兑人
                    if (isBankToAcc && AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(vo.getBillType()))) {
                        // 如果账号为0，则承兑人信息为银行信息，根据联行号获取信息
                        if ("0".equals(vo.getPayPayeeacc()) && StringUtils.isNotEmpty(bankInfoMap) && bankInfoMap.containsKey(vo.getAcceptorbankno())) {
                            InputBankInfoDTO bankInfo = bankInfoMap.get(vo.getAcceptorbankno());
                            vo.setPayPayeeacc(vo.getPayPayeeacc());// 收票人账号
                            vo.setPayPayeeName(bankInfo.getBankName());// 收票人名称
                            vo.setPayPayeeBankNo(bankInfo.getBankCode());// 收票人开户行行号
                            vo.setPayPayeeBankName(bankInfo.getBankName());// 收票人开户行行名
                            vo.setProv(bankInfo.getProv());// 所在省
                            vo.setProvName(bankInfo.getProvName());// 所在省
                            vo.setCity(bankInfo.getCity());// 所在市
                            vo.setCityName(bankInfo.getCityName());// 所在市
                        } else {
                            getBankAcc(vo, externalCorpBankAccVOMap, btBankAccDTOMap);
                        }
                    }
                    // 商票：默认收票人
                    if (isBusinessToAcc && AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(vo.getBillType()))) {
                        getBankAcc(vo, externalCorpBankAccVOMap, btBankAccDTOMap);
                    }
                }
            }
        }
        dataTablesOutput.setData(data);
        return dataTablesOutput;
    }
    /**
     * 通过账号补全收票人信息
     *
     * @param vo                       兑付信息数据
     * @param externalCorpBankAccVOMap 客商账户信息
     * @param btBankAccDTOMap          银行账户信息
     */
    private void getBankAcc(AbEmitBillPayVo vo, Map<String, ExternalCorpBankAccDTO> externalCorpBankAccVOMap, Map<String, BtBankAccDTO> btBankAccDTOMap) {
        if (null != externalCorpBankAccVOMap && externalCorpBankAccVOMap.containsKey(vo.getPayPayeeacc())) {
            // 根据账号查询客商账户信息
            ExternalCorpBankAccDTO externalCorpBankAccVO = externalCorpBankAccVOMap.get(vo.getPayPayeeacc());
            if (StringUtils.isNotEmpty(externalCorpBankAccVO)) {
                vo.setPayPayeeacc(externalCorpBankAccVO.getExternalAcc());// 收票人账号
                vo.setPayPayeeName(externalCorpBankAccVO.getExternalAcc());// 收票人名称
                vo.setPayPayeeBankNo(externalCorpBankAccVO.getBankSourceCode());// 收票人开户行行号
                vo.setPayPayeeBankName(externalCorpBankAccVO.getBankTypeName());// 收票人开户行行名
                vo.setProv(externalCorpBankAccVO.getAccProvince());// 所在省
                vo.setProvName(externalCorpBankAccVO.getAccProvince());// 所在省
                vo.setCity(externalCorpBankAccVO.getAccCity());// 所在市
                vo.setCityName(externalCorpBankAccVO.getAccCity());// 所在市
            }
        }
        if (null != btBankAccDTOMap && btBankAccDTOMap.containsKey(vo.getPayPayeeacc())) {
            // 根据账号查询账户信息
            BtBankAccDTO btBankAccDTO = btBankAccDTOMap.get(vo.getPayPayeeacc());
            if (StringUtils.isNotEmpty(btBankAccDTO)) {
                vo.setPayPayeeacc(btBankAccDTO.getBankAcc());// 收票人账号
                vo.setPayPayeeName(btBankAccDTO.getAccName());// 收票人名称
                vo.setPayPayeeBankNo(btBankAccDTO.getBankCode());// 收票人开户行行号
                vo.setPayPayeeBankName(btBankAccDTO.getBankName());// 收票人开户行行名
                vo.setProv(btBankAccDTO.getProv());// 所在省
                vo.setProvName(btBankAccDTO.getProvName());// 所在省
                vo.setCity(btBankAccDTO.getCity());// 所在市
                vo.setCityName(btBankAccDTO.getCityName());// 所在市
            }
        }
    }
    /**
     * 兑付跟踪列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbEmitBillPayTrack(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_PAY_QUERY, dataTablesInput.getCurrentUserId());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_PAY_QUERY, corpIds);
        DataTablesOutput<AbEmitBillPayVo> dataTablesOutput= abEmitBillPayDao.listAbEmitBillPayTrack(dataTablesInput, corpAuthList);
        List<AbEmitBillPayVo> data = dataTablesOutput.getData();
        if (data != null && !data.isEmpty()) {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG" , "AB_BILL_OPENWAY" , "AB_BILL_AbEmitBillBack_STATUS"
                    , "AB_BILL_AbEmitBillPay_BRIEF_ID", "AB_BILL_ABACCRECORDS_EMITSTTLMMK","nisBillhead.status", "bisExc.voucherStat"
                    ,"AB_BILL_NEW_STM_ISSUOFPPR_IND","AB_BILL_AbEmitBillPay_STATUS");
            // 获取外部订单号，获取付款状态信息
            List<String> erpIdList = data.stream().map(AbEmitBillPayVo::getId).distinct().collect(Collectors.toList());
            Map<String, NisBillheadDTO> billMap = nisBillheadService.listNisBillInfoByErpId(erpIdList);
            for (AbEmitBillPayVo vo : data) {
                // 重置付款状态
                NisBillheadDTO billHead = billMap.get(vo.getId());
                // (case when (e.bill_no is not null and n.id is null) then -2 else n.STATUS end) as payStatus
                if (ObjectUtil.isNotEmpty(billHead)){
                    Integer payStatus = ObjectUtil.isNotEmpty(vo.getBillNo()) && ObjectUtil.isEmpty(billHead.getId()) ? -2 : billHead.getStatus();
                    vo.setPayStatus(payStatus);
                // (case when n.bill_no is not null then n.bill_no else e.bill_no end) as billNo
                if (ObjectUtil.isNotEmpty(billHead.getBillNo())){
                    vo.setBillNo(billHead.getBillNo());
                }
                // (case when (n.is_payed='1' and n.STATUS >=95) then '0' else b.VOUCHER_STAT end) as voucherStat
                String voucherStat = ObjectUtil.equals(billHead.getIsPayed(),"1")&&billHead.getStatus()>=95?"0": billHead.getVoucherStat();
                vo.setVoucherStat(voucherStat);
                }
                // 冗余数据
                injectValues(vo, listDictMap);
                if (ObjectUtil.isNotEmpty(vo.getBusCorpId())) {
                    String busCorpName = corpService.getCorpName(vo.getBusCorpId());
                    vo.setBusCorpName(busCorpName);
                }
            }
        }
        dataTablesOutput.setData(data);
        return dataTablesOutput;
    }
    /**
     * 线下兑付确认
     *
     * @param pay
     */
    public void cashingConfirmation(AbEmitBillPayVo pay, ICurrentUser userVo) {
        int len = pay.getIds().split(",").length;
        // 批量修改主表box_status
        int num = abVoucherinfoViceService.batchSubmit(pay.getVvids().split(","), userVo);
        if (num != len) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        if(StrUtil.isNotEmpty(pay.getDateStr())){
            pay.setDate(DateUtils.parse(pay.getDateStr(),"yyyy-MM-dd"));
        }
        // 批量插入流水表数据
        List<AbAccrecords> abList = getAbAccrecords(pay, true);
        num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
        if (num != len) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        //撤回状态的流水作废
        String[] aids = pay.getIds().split(",");
        AbAccrecords abAccrecords = null;
        List<String> aidList = new ArrayList<String>();
        for (String aid : aids) {
            abAccrecords = abAccrecordsService.get(aid, true);
            if (AccebillConstant.AB_BILL_BUS_STATUS_3.equals(abAccrecords.getStatus())) {
                aidList.add(aid);
            }
        }
        if (aidList != null && !aidList.isEmpty()) {
            abAccrecordsService.batchUpdate(aidList.toArray(new String[aidList.size()]),
                    null, null, AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
        }
        // 提交后调用审批，如果成功，主表：box_status=11，流水：status=95,上一条流水valid_sign=2。（测试手动修改，模拟审批）
        // 如果打回同时主表：box_status=11,从表：back_brief_id改为BRIEF_STATUS_6120，流水：status=-2(已删除),这条兑付确认流水valid_sign=2(已删除)
        for (AbAccrecords abs : abList) {
            saveAbEmitbillPay(pay, abs, AccebillConstant.BILL_OPERATE_ADD, userVo);// 保存线上兑付信息
            AbEmitBillPayVo variables = new AbEmitBillPayVo();
            BeanUtils.copyNotNullProperties(abs.getVouInfo(), variables);
            BeanUtils.copyNotNullProperties(abs, variables);
            // 是否定时任务处理
            String userId = userVo.getId();
            getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLPAY, abs.getId(), variables,
                    abs.getVouViceInfo().getApplyCorpId(), userId);
            abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
            //完成消息
            abBillNotificationService.completeMsg(abs, userVo);
        }
        String msg = pay.isTask() ? AccebillConstant.LANG_LOG_TASK_SUCC
                : AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC;
        String logType = pay.isTask() ? AccebillConstant.ABLOG_LOGTYPE_TASK
                : AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
        abLogService.batchSaveAbLog(pay.getVvids().split(","), pay.getIds().split(","),
                AccebillConstant.BRIEF_STATUS_6120, logType, msg, userVo);
    }

    /**
     * 批量线上兑付确认
     *
     * @param pay
     */
    public void batchCashingConfirmation(AbEmitBillPayVo pay, ICurrentUser userVo) {
        //因为pay中的date是Date类型，前端无法传值到后端，so新增String类型的dateStr,这里进行转换
        if (StringUtils.isNotBlank(pay.getDateStr())){
            pay.setDate(DateUtils.parse(pay.getDateStr(), "yyyy-MM-dd"));
        }

        // 补全省市信息
        List<AbEmitBillPayOnlineVo> abEmitBillPayOnlineVos = pay.getAbEmitBillPayOnlineVos();
        // 联行号集合
        Set<String> bankCodeSet = new HashSet();
        for (AbEmitBillPayOnlineVo vo : abEmitBillPayOnlineVos) {
            bankCodeSet.add(vo.getPayPayeeBankNo());
        }
        if (StringUtils.isNotEmpty(bankCodeSet)) {
            List<String> bankCodelist = new ArrayList<>(bankCodeSet);
            Map<String, InputBankInfoDTO> bankInfoMap = inputBankInfoService.getBankCodeInfoByBankCodes(bankCodelist);
            for (AbEmitBillPayOnlineVo vo : abEmitBillPayOnlineVos) {
                if (StringUtils.isNotEmpty(vo.getPayPayeeBankNo()) && StringUtils.isNotEmpty(bankInfoMap) && bankInfoMap.containsKey(vo.getPayPayeeBankNo())) {
                    InputBankInfoDTO bankInfo = bankInfoMap.get(vo.getPayPayeeBankNo());
                    vo.setProv(bankInfo.getProv());// 所在省
                    vo.setCity(bankInfo.getCity());// 所在市
                }
            }
            pay.setAbEmitBillPayOnlineVos(abEmitBillPayOnlineVos);
        }
        int len = pay.getIds().split(",").length;
        // 批量修改主表box_status
        int num = abVoucherinfoViceService.batchSubmit(pay.getVvids().split(","), userVo);
        if (num != len) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 批量插入流水表数据
        List<AbAccrecords> abList = getAbAccrecords(pay, true);
        num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
        if (num != len) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        //撤回状态的流水作废
        String[] aids = pay.getIds().split(",");
        AbAccrecords abAccrecords = null;
        List<String> aidList = new ArrayList<String>();
        for (String aid : aids) {
            abAccrecords = abAccrecordsService.get(aid, true);
            if (AccebillConstant.AB_BILL_BUS_STATUS_3.equals(abAccrecords.getStatus())) {
                aidList.add(aid);
            }
        }
        if (aidList != null && !aidList.isEmpty()) {
            abAccrecordsService.batchUpdate(aidList.toArray(new String[aidList.size()]),
                    null, null, AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
        }
        // 提交后调用审批，如果成功，主表：box_status=11，流水：status=95,上一条流水valid_sign=2。（测试手动修改，模拟审批）
        // 如果打回同时主表：box_status=11,从表：back_brief_id改为BRIEF_STATUS_6120，流水：status=-2(已删除),这条兑付确认流水valid_sign=2(已删除)
        for (AbAccrecords abs : abList) {
            batchSaveAbEmitbillPay(pay, abs, AccebillConstant.BILL_OPERATE_ADD, userVo);// 保存线上兑付信息
            AbEmitBillPayVo variables = new AbEmitBillPayVo();
            BeanUtils.copyNotNullProperties(abs.getVouInfo(), variables);
            BeanUtils.copyNotNullProperties(abs, variables);
            // 是否定时任务处理
            String userId = userVo.getId();
            getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLPAY, abs.getId(), variables,
                    abs.getVouViceInfo().getApplyCorpId(), userId);
            abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
            //完成消息
            abBillNotificationService.completeMsg(abs, userVo);
        }
        String msg = pay.isTask() ? AccebillConstant.LANG_LOG_TASK_SUCC
                : AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC;
        String logType = pay.isTask() ? AccebillConstant.ABLOG_LOGTYPE_TASK
                : AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
        abLogService.batchSaveAbLog(pay.getVvids().split(","), pay.getIds().split(","),
                AccebillConstant.BRIEF_STATUS_6120, logType, msg, userVo);
    }

    /**
     * 批量设置兑付确认备注
     *
     * @param pay
     */
    public void batchSetEmitPayRmk(AbEmitBillPayVo pay, ICurrentUser userVo) {
        AbAccrecordsVO accRecordsVO=new AbAccrecordsVO();
        accRecordsVO.setIdArray(pay.getIds().split(","));
        accRecordsVO.setRmk(pay.getPayAbs());
        abAccrecordsService.batchUpdate(accRecordsVO, userVo);
        String msg = "批量设置兑付确认备注【"+pay.getPayAbs()+"】成功！";
        String logType = AccebillConstant.ABLOG_LOGTYPE_UPDATE;
        abLogService.batchSaveAbLog(pay.getVvids().split(","), pay.getIds().split(","),
                AccebillConstant.BRIEF_STATUS_6120, logType, msg, userVo);
    }

    /**
     * 调整票据解付金额
     *
     * @param pay
     */
    public void setBillEndPayMoney(AbEmitBillPayVo pay, ICurrentUser userVo) {
        Map<String,Object> params = new HashMap<>();
        params.put("ids",new String[]{pay.getVvid()});
        params.put("bailMoneyNew",pay.getBailmoney());
        if(pay.getBailpercent()==null){
            pay.setBailpercent(BigDecimal.ZERO);
        }
        params.put("bailpercentNew", pay.getBailpercent());
        abVoucherinfoViceService.batchUpdate(params);
        if(StrUtil.isNotEmpty(pay.getVvid()) && StrUtil.isNotEmpty(pay.getAccId())) {
            String msg = "调整票据解付金额【"+ NumberUtil.formatNumeric(pay.getEndPayMoney(),2)+"】,保证金金额【"+NumberUtil.formatNumeric(pay.getBailmoney(),2)+"】,保证比例【"+NumberUtil.formatNumeric(pay.getBailpercent().doubleValue()*100,4)+"%】成功！";
            String logType = AccebillConstant.ABLOG_LOGTYPE_UPDATE;
            abLogService.batchSaveAbLog(new String[]{pay.getVvid()}, new String[]{pay.getAccId()},
                    AccebillConstant.BRIEF_STATUS_6120, logType, msg, userVo);
        }
    }

    /**
     * ERP线下兑付确认
     *
     * @param pay
     */
    public void cashingConfirmationERP(AbEmitBillPayVo pay, ICurrentUser userVo) {
        int len = pay.getIds().split(",").length;
        // 批量修改主表box_status
        // 批量修改票据从表信息(修改处理标记为处理完成)
        abVoucherinfoViceService.updateBoxStatus(pay.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
        // 批量插入流水表数据
        List<AbAccrecords> abList = getAbAccrecords(pay, false);
        int num = abAccrecordsService.batchSaveAccrecords(abList, userVo);
        if (num != len) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        //撤回状态的流水作废
        String[] aids = pay.getIds().split(",");
        AbAccrecords abAccrecords = null;
        List<String> aidList = new ArrayList<String>();
        for (String aid : aids) {
            abAccrecords = abAccrecordsService.get(aid, true);
            if (AccebillConstant.AB_BILL_BUS_STATUS_3.equals(abAccrecords.getStatus())) {
                aidList.add(aid);
            }
        }
        if (aidList != null && !aidList.isEmpty()) {
            abAccrecordsService.batchUpdate(aidList.toArray(new String[aidList.size()]),
                    null, null, AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
        }
        // 提交后调用审批，如果成功，主表：box_status=11，流水：status=95,上一条流水valid_sign=2。（测试手动修改，模拟审批）
        // 如果打回同时主表：box_status=11,从表：back_brief_id改为BRIEF_STATUS_6120，流水：status=-2(已删除),这条兑付确认流水valid_sign=2(已删除)
        for (AbAccrecords abs : abList) {
            AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abs.getVvid(), true);
            AbEmitBillPayVo variables = new AbEmitBillPayVo();
            BeanUtils.copyNotNullProperties(abs.getVouInfo(), variables);
            BeanUtils.copyNotNullProperties(abs, variables);
            if (StringUtils.isNotBlank(variables.getVid())) {
                //释放付款单预算
                List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
                        BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, abVoucherinfoVice.getVoucherId());
                if (list.size() > 0 && !list.isEmpty()) {
                    if (StringUtils.isNotBlank(list.get(0).getBillNo())) {
                        String billNo = list.get(0).getBillNo();
                        abFbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY,
                                BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
                    }
                }
            } else {
                // 释放预算
                abFbsBudgetProcessService.releaseBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
                        BudgetConstant.PROCESS_SYSNAME_AB, abs.getVvid());
            }
            // 上一流水valid_sign=2
            String[] lastids = new String[]{abs.getLastid()};
            AbAccrecords abAccrecordsLastid = abAccrecordsService.get(abs.getLastid());
            if (!AccebillConstant.AB_BILL_BUS_STATUS_3.equals(abAccrecordsLastid.getStatus())) {
                num = abAccrecordsService.batchAppPass(lastids, true, userVo);
            }
            if (num != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
        }
        abLogService.batchSaveAbLog(pay.getVvid().split(","), pay.getIds().split(","),
                AccebillConstant.BRIEF_STATUS_6120, AccebillConstant.ABLOG_LOGTYPE_SUBMIT,
                AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
    }
    /**
     * 线上兑付付款单撤回
     *
     * @param aid 票据流水id
     * @return
     */
    public void withdraw(String aid, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(aid)) {
            List<NisBillheadDTO> list = abNisBillHeadService.listNisBillheadByErpId(aid);
            if (list != null && !list.isEmpty()) {
                NisBillheadDTO nisBillheadDTO = list.get(0);
                // 付款单逻辑删除
                abNisBillHeadService.updateNisBillhead(nisBillheadDTO.getId(), NetbankConstant.STATUE_DEL,
                        NisMessageConstant.NIS_LOG_DELETE_SUCC, userVo);
                // 票据线上兑付状态审批通过改为撤回
                String[] aidAry = {aid};
                int num = abAccrecordsService.batchUpdateStatusByTrack(aidAry, AccebillConstant.AB_BILL_BUS_STATUS_3,
                        AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
                if (num != 1) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                abLogService.saveAbLog(abAccrecordsService.get(aid).getVvid(), aid, AccebillConstant.BRIEF_STATUS_6120,
                        AccebillConstant.ABLOG_LOGTYPE_WITHDRAW, AccebillConstant.LANG_LOG_WITHDRAW_SUCC, userVo);
            }
        }
    }
    /**
     * 线上兑付确认已付款
     *
     * @param aid 票据流水id
     * @return
     */
    public void confrimepay(String aid, ICurrentUser userVo) {
        batchConfrimepay(aid, userVo);
    }
    /**
     * 批量确认已付款
     *
     * @param aids 票据流水ids
     * @return
     */
    public void batchConfrimepay(String aids, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(aids)) {
            String[] aidAry = aids.split(",");
            // 流水状态改为确认已付款状态 95为100
            int num = abAccrecordsService.batchUpdateStatusByTrack(aidAry, AccebillConstant.AB_BILL_BUS_STATUS100,
                    AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
            if (num != aidAry.length) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            String[] vvid = new String[aidAry.length];
            for (int i = 0; i < aidAry.length; i++) {
                AbAccrecords info = abAccrecordsService.get(aidAry[i]);
                vvid[i] = info.getVvid();
                // 释放授信额度
                abRatUrationService.opRatUration(info, AccebillConstant.BILL_OPERATE_REPULSE, userVo);
                // 释放内部授信额度
                abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_REPULSE, info, userVo);
                //释放资金计划
                AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(info.getVvid(), true);
                // 释放挂接担保合同
                AbEmitBillVo abEmitBillVo = abEmitBillService.getAbEmitBearVoByAid(info.getId());
                abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_REPULSE, abEmitBillVo, userVo);
                if (StringUtils.isNotBlank(abVoucherinfoVice.getVoucherId())) {
                    //释放付款单预算
                    List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
                            BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, abVoucherinfoVice.getVoucherId());
                    if (list.size() > 0) {
                        if (StringUtils.isNotBlank(list.get(0).getBillNo())) {
                            String billNo = list.get(0).getBillNo();
                            abFbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY,
                                    BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
                        }
                    }
                } else {
                    // 释放预算
                    abFbsBudgetProcessService.releaseBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
                            BudgetConstant.PROCESS_SYSNAME_AB, info.getVvid());
                }
                // 挂接保证金,线上兑付确认付款后释放保证金
                abFcBondHookService.freedFcBondApply(abVoucherinfoVice, userVo);
                //修改票据状态为结束已结清
                AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(info.getVid());
                if(abVoucherinfo == null){
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
                }
                //新票
                if("1".equals(abVoucherinfo.getNewStmIssuofpprInd())){
                    abVoucherinfoService.updateBillStatus(info.getVid(),AccebillConstant.NEWBILL_STATUS_CS06,userVo,AccebillConstant.CIRCULATE_TF0501);
                }else {
                    abVoucherinfoService.updateBillStatus(info.getVid(),AccebillConstant.AB_BILL_STATUS_000000,userVo,null);
                }
            }
            abLogService.batchSaveAbLog(vvid, aidAry, AccebillConstant.BRIEF_STATUS_6120,
                    AccebillConstant.ABLOG_CONFRIM_BATCHPAY, AccebillConstant.LANG_LOG_BATCHPAY_SUCC, userVo);
        }
    }
    /**
     * 删除线上兑付申请信息
     *
     * @param aid 票据流水id
     * @return
     */
    public void deleteByAid(String aid, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(aid)) {
            // 兑付申请流水作废status=-2
            int num = 0;
            AbAccrecords abAccrecordsCurrent = abAccrecordsService.get(aid);
            String[] aids = {aid};
            if (AccebillConstant.AB_BILL_BUS_STATUS_3.equals(abAccrecordsCurrent.getStatus())) {
                // 撤回状态的流水作废
                num = abAccrecordsService.batchUpdate(aids, null, null, AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
            } else {
                num = abAccrecordsService.batchAppPass(aids, true, userVo);
            }
            if (num != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            // 发起兑付申请业务之前的流水恢复有效
            AbAccrecords abAccrecords = abAccrecordsService.get(aid);
            while (AccebillConstant.BRIEF_STATUS_6120.equals(abAccrecords.getBriefId())) {
                abAccrecords = abAccrecordsService.get(abAccrecords.getLastid());
            }
            // 线上兑付申请信息作废
            abEmitBillPayDtlService.updateValidSignByAid(abAccrecords.getVvid(), AccebillConstant.AB_BILL_VALID_SIGN_0,
                    AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
            abAccrecordsService.updatAppPass(abAccrecords.getId(), false, userVo);
            abLogService.saveAbLog(abAccrecordsService.get(aid).getVvid(), aid, AccebillConstant.BRIEF_STATUS_6120,
                    AccebillConstant.ABLOG_LOGTYPE_DELETE, AccebillConstant.LANG_LOG_DELETE_SUCC, userVo);
        }
    }
    /**
     * 保存线上兑付信息
     *
     * @param pay
     * @param opt
     */
    protected void saveAbEmitbillPay(AbEmitBillPayVo pay, AbAccrecords abs, String opt, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(pay) && AccebillConstant.AB_BILL_PAYEMITSTTLMMK1.equals(pay.getEmitSttlmmk())) {
            AbEmitBillPay abEmitBillPay = abEmitBillPayDtlService.getAbEmitBillPayByAid(abs.getVvid());
            if (abEmitBillPay == null) {
                abEmitBillPay = new AbEmitBillPay();
            }
            abEmitBillPay.setAid(abs.getVvid());// 票据流水ID
            abEmitBillPay.setCorpId(pay.getPayCorpId());// 单位ID
            abEmitBillPay.setPayeeName(pay.getPayPayeeName());// 收票人名称
            abEmitBillPay.setPayeeAcc(pay.getPayPayeeacc());// 收票人账号
            abEmitBillPay.setPayeeBankNo(pay.getPayPayeeBankNo());// 收票人开户行行号
            abEmitBillPay.setPayeeBankName(pay.getPayPayeeBankName());// 收票人开户行行名
            abEmitBillPay.setPayMoney(pay.getPayMoney());// 兑付金额
            abEmitBillPay.setPurpose(pay.getPayUse());// 用途
            abEmitBillPay.setRemark(pay.getPayAbs());// 备注
            abEmitBillPay.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);// 有效标志：0无效；1有效
            abEmitBillPay.setBillNo(pay.getBillNo());// 付款单号
            abEmitBillPay.setProv(pay.getProv());// 所在省
            abEmitBillPay.setCity(pay.getCity());// 所在市
            if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
                abEmitBillPay.setCreateName(userVo.getUsername());// 创建人名称
                abEmitBillPay.setCreateBy(userVo.getLoginName());// 创建人登录名称
                abEmitBillPay.setCreateDate(new Date());// 创建日期
            } else {
                abEmitBillPay.setUpdateBy(userVo.getUsername());// 创建人名称
                abEmitBillPay.setUpdateName(userVo.getLoginName());// 创建人登录名称
                abEmitBillPay.setUpdateDate(new Date());// 创建日期
            }
            abEmitBillPayDtlService.saveOrUpdate(abEmitBillPay);
            abLogService.saveAbLog(abs.getVvid(), abs.getId(), AccebillConstant.BRIEF_STATUS_6120,
                    AccebillConstant.ABLOG_LOGTYPE_ADD, AccebillConstant.LANG_LOG_SAVE_SUCC, userVo);
        }
    }
    /**
     * 批量保存线上兑付信息
     *
     * @param pay
     * @param opt
     */
    protected void batchSaveAbEmitbillPay(AbEmitBillPayVo pay, AbAccrecords abs, String opt, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(pay) && AccebillConstant.AB_BILL_PAYEMITSTTLMMK1.equals(pay.getEmitSttlmmk())) {
            AbEmitBillPay abEmitBillPay = abEmitBillPayDtlService.getAbEmitBillPayByAid(abs.getVvid());
            if (abEmitBillPay == null) {
                abEmitBillPay = new AbEmitBillPay();
            }
            List<AbEmitBillPayOnlineVo> abEmitBillPayOnlineVos = pay.getAbEmitBillPayOnlineVos();
            for (AbEmitBillPayOnlineVo vo : abEmitBillPayOnlineVos) {
                String vvid = vo.getVvid();
                if (vvid.equals(abs.getVvid())) {
                    abEmitBillPay.setAid(abs.getVvid());// 票据流水ID
                    abEmitBillPay.setCorpId(vo.getPayCorpId());// 单位ID
                    abEmitBillPay.setPayeeName(vo.getPayPayeeName());// 收票人名称
                    abEmitBillPay.setPayeeAcc(vo.getPayPayeeacc());// 收票人账号
                    abEmitBillPay.setPayeeBankNo(vo.getPayPayeeBankNo());// 收票人开户行行号
                    abEmitBillPay.setPayeeBankName(vo.getPayPayeeBankName());// 收票人开户行行名
                    abEmitBillPay.setPayMoney(abs.getVouInfo().getBillMoney());// 兑付金额
                    abEmitBillPay.setPurpose(vo.getPayUse());// 用途
                    abEmitBillPay.setRemark(vo.getPayAbs());// 备注
                    abEmitBillPay.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);// 有效标志：0无效；1有效
                    abEmitBillPay.setBillNo(vo.getBillNo());// 付款单号
                    abEmitBillPay.setProv(vo.getProv());// 所在省
                    abEmitBillPay.setCity(vo.getCity());// 所在市
                    if (AccebillConstant.BILL_OPERATE_ADD.equals(opt)) {
                        abEmitBillPay.setCreateName(userVo.getUsername());// 创建人名称
                        abEmitBillPay.setCreateBy(userVo.getLoginName());// 创建人登录名称
                        abEmitBillPay.setCreateDate(new Date());// 创建日期
                    } else {
                        abEmitBillPay.setUpdateBy(userVo.getUsername());// 创建人名称
                        abEmitBillPay.setUpdateName(userVo.getLoginName());// 创建人登录名称
                        abEmitBillPay.setUpdateDate(new Date());// 创建日期
                    }
                }
            }
            abEmitBillPayDtlService.saveOrUpdate(abEmitBillPay);
            abLogService.saveAbLog(abs.getVvid(), abs.getId(), AccebillConstant.BRIEF_STATUS_6120,
                    AccebillConstant.ABLOG_LOGTYPE_ADD, AccebillConstant.LANG_LOG_SAVE_SUCC, userVo);
        }
    }
    /**
     * 根据线上兑付信息生成付款单信息
     */
    protected NisBillheadDTO saveNisBillheadDTO(AbAccrecords abAccrecords, ICurrentUser userVo) {
        AbEmitBillPay abEmitbillPay = abEmitBillPayDtlService.getAbEmitBillPayByAid(abAccrecords.getVvid());
        if (abEmitbillPay == null) {
            return null;
        }
        NisBillheadDTO nisBillheadDTO = new NisBillheadDTO();
        nisBillheadDTO.setCorpId(abEmitbillPay.getCorpId());// 所属单位
        nisBillheadDTO.setPayeeAcc(abEmitbillPay.getPayeeAcc());// 收款账号
        nisBillheadDTO.setPayeeAccName(abEmitbillPay.getPayeeName());// 收款账号名称
        nisBillheadDTO.setPayeeBankCode(abEmitbillPay.getPayeeBankNo());// 收款开户行行号
        nisBillheadDTO.setPayeeBank(abEmitbillPay.getPayeeBankName());// 收款开户行名称
        nisBillheadDTO.setPayMoney(abEmitbillPay.getPayMoney());// 金额
        nisBillheadDTO
                .setVoucherTypeId(abFcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_34).getId());// 付款方式
        nisBillheadDTO.setBillFrom(NetbankConstant.BILLFROM_3);// 单据来源
        nisBillheadDTO.setErpId(abAccrecords.getId());// 流水id(票据关联付款单id)
        nisBillheadDTO.setStatus(NetbankConstant.STATUE_TEMP);// 暂存状态
        nisBillheadDTO.setPurpose(abEmitbillPay.getPurpose());// 用途
        nisBillheadDTO.setDirectPayFlag(NetbankConstant.DIRECTPAYFLAG_Y);// 直接付款，付款时不记账
        nisBillheadDTO.setWishPayday(DateUtils.getDateYMD(new Date()));// 期望支付日期默认为当天
        nisBillheadDTO.setProv(abEmitbillPay.getProv());// 所在省
        nisBillheadDTO.setCity(abEmitbillPay.getCity());// 所在市
        nisBillheadDTO.setIsForindividual(NetbankConstant.FORINDIVIDUAL_0);//是否对私付款 默认为对公
        nisBillheadDTO.setIsQuick(NetbankConstant.ISQUICK_0);//加急标记默认为不加急
        nisBillheadDTO.setIsPayed(NetbankConstant.ISPAYED_0);//已付款标记 默认为否
        abNisBillHeadService.saveNisBillhead(nisBillheadDTO, userVo);
        this.getSession().flush();
        List<NisBillheadDTO> list = abNisBillHeadService.listNisBillheadByErpId(abAccrecords.getId());
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 根据主表id 从表id查询兑付申请数据
     *
     * @param vid  主表id
     * @param vvid 从表id
     * @return 返回列表数据
     */
    public List<AbEmitBillPayVo> listAbEmitBillbyVidAndVvid(String vid, String vvid) {
        return abEmitBillPayDao.listAbEmitBillbyVidAndVvid(vid, vvid);
    }
    /**
     * 审批操作
     *
     * @param buId     主表id数组
     * @param isPassOK 通过标识
     * @param appMsg   审批信息
     */
    public void approvalPass(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
        if (StringUtils.isEmpty(buId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbAccrecords info = abAccrecordsService.get(buId, true);
        if (info == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        // 批量修改票据主表信息(修改处理标记为处理完成)
        int num = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
        if (num != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 修改流水表数据（修改status状态为95：审批通过、修改VALID_SIGN为1：可用）
        Integer status = isPassOK ? AccebillConstant.AB_BILL_BUS_STATUS95 : AccebillConstant.AB_BILL_BUS_STATUS_2;
        int appNum = abAccrecordsService.approve(info.getId(), status, userVo);
        if (appNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        if (isPassOK) {
            AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(info.getVvid(), true);
            if (AccebillConstant.AB_BILL_PAYEMITSTTLMMK1.equals(info.getEmitSttlmmk())) {
                NisBillheadDTO nisBillheadDTO = saveNisBillheadDTO(info, userVo);
                if (nisBillheadDTO != null) {
                    // 线上兑付信息保存付款单号信息
                    AbEmitBillPay abEmitbillPay = abEmitBillPayDtlService.getAbEmitBillPayByAid(info.getVvid());
                    if (abEmitbillPay != null) {
                        abEmitbillPay.setBillNo(nisBillheadDTO.getBillNo());
                        abEmitBillPayDtlService.save(abEmitbillPay);
                    }
                }
            } else {
                if (StringUtils.isNotBlank(abVoucherinfoVice.getVoucherId())) {
                    //释放付款单预算
                    List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
                            BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, abVoucherinfoVice.getVoucherId());
                    if (list.size() > 0 && !list.isEmpty()) {
                        if (StringUtils.isNotBlank(list.get(0).getBillNo())) {
                            String billNo = list.get(0).getBillNo();
                            abFbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY,
                                    BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
                        }
                    }
                } else {
                    // 释放预算
                    abFbsBudgetProcessService.releaseBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_EMIT,
                            BudgetConstant.PROCESS_SYSNAME_AB, info.getVvid());
                }
            }
            // 上一流水valid_sign=2
            String[] lastids = new String[]{info.getLastid()};
            AbAccrecords abAccrecordsLastid = abAccrecordsService.get(info.getLastid());
            if (!AccebillConstant.AB_BILL_BUS_STATUS_3.equals(abAccrecordsLastid.getStatus())) {
                num = abAccrecordsService.batchAppPass(lastids, isPassOK, userVo);
            }
            if (num != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            // 挂接授信,审批通过只释放线下的兑付确认,线上兑付需要确认付款后释放
            if (AccebillConstant.AB_BILL_PAYEMITSTTLMMK0.equals(info.getEmitSttlmmk())) {
                abRatUrationService.opRatUration(info, AccebillConstant.BILL_OPERATE_REPULSE, userVo);
                // 内部授信
                abRatCpUrationService.ratCpLink(AccebillConstant.BILL_OPERATE_REPULSE, info, userVo);
                // 挂接担保
                AbEmitBillVo abEmitBillVo = abEmitBillService.getAbEmitBearVoByAid(abAccrecordsLastid.getId());
                abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_REPULSE, abEmitBillVo, userVo);
            }
            // 挂接保证金,审批通过只释放线下的保证金,线上兑付需要确认付款后释放
            if (AccebillConstant.AB_BILL_PAYEMITSTTLMMK0.equals(info.getEmitSttlmmk())) {
                abFcBondHookService.freedFcBondApply(abVoucherinfoVice, userVo);
            }
            //线下兑付修改票据状态为已结清标记且已付款
            if (AccebillConstant.AB_BILL_PAYEMITSTTLMMK0.equals(info.getEmitSttlmmk())) {
                AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(info.getVid());
                if(abVoucherinfo == null){
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
                }
                //新票
                if("1".equals(abVoucherinfo.getNewStmIssuofpprInd())){
                    // 使用对象更新
                    abVoucherinfo.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS06);
                    abVoucherinfo.setEbillBlCrclIndc(AccebillConstant.CIRCULATE_TF0501);
                    abVoucherinfo.setUpdateBy(userVo.getLoginName());
                    abVoucherinfo.setUpdateName(userVo.getUsername());
                    abVoucherinfo.setUpdateDate(new Date());
                    abVoucherinfoService.updateEntity(abVoucherinfo);
                    // 前面hibernate获取过该对象，后面使用hql更新的时候会偶尔导致更新不到某些字段
                    // abVoucherinfoService.updateBillStatus(info.getVid(),AccebillConstant.NEWBILL_STATUS_CS06,userVo,AccebillConstant.CIRCULATE_TF0501);
                }else {
                    abVoucherinfoService.updateBillStatus(info.getVid(),AccebillConstant.AB_BILL_STATUS_000000,userVo,null);
                }
                String[] aidAry = {info.getId()};
                abAccrecordsService.batchUpdateStatusByTrack(aidAry, AccebillConstant.AB_BILL_BUS_STATUS100,
                        AccebillConstant.AB_BILL_BUS_STATUS95, userVo);
            }
        } else {
            // 线上兑付信息作废
            abEmitBillPayDtlService.updateValidSignByAid(info.getVvid(), AccebillConstant.AB_BILL_VALID_SIGN_0,
                    AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
            int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
            if (reNum != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            // 发起兑付申请业务之前的流水恢复有效
            AbAccrecords abAccrecords = abAccrecordsService.get(buId);
            while (AccebillConstant.BRIEF_STATUS_6120.equals(abAccrecords.getBriefId())) {
                abAccrecords = abAccrecordsService.get(abAccrecords.getLastid());
            }
            abAccrecordsService.updatAppPass(abAccrecords.getId(), false, userVo);
        }
        String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
        abLogService.saveAbLog(info.getVvid(), info.getId(), AccebillConstant.BRIEF_STATUS_6120, logType, appMsg,
                userVo);
    }
    /**
     * 获取流水信息
     *
     * @param pay
     * @param flag
     * @return
     */
    protected List<AbAccrecords> getAbAccrecords(AbEmitBillPayVo pay, boolean flag) {
        List<AbAccrecords> abList = new ArrayList<AbAccrecords>();
        // 流水表id数组
        String[] aids = pay.getIds().split(",");
        List<AbEmitBillPayOnlineVo> list = pay.getAbEmitBillPayOnlineVos();
        Map<String,AbEmitBillPayOnlineVo> idMapInfo=new HashMap<>();
        if(CollectionUtil.isNotEmpty(list)){
            idMapInfo=list.stream().collect(Collectors.toMap(AbEmitBillPayOnlineVo::getId, Function.identity()));
        }
        for (String id : aids) {
            // 获取流水表信息
            AbAccrecords abAccrecords = abAccrecordsService.get(id, true);
            // 获取当前票据主，从表信息
            AbVoucherinfo ab = abVoucherinfoService.get(abAccrecords.getVid(), true);
            AbVoucherinfoVice abVice = abVoucherinfoViceService.get(abAccrecords.getVvid(), true);
            // 新的流水数据
            AbAccrecords entity = new AbAccrecords();
            // 票据主表ID
            entity.setVid(ab.getId());
            // 票据从表ID
            entity.setVvid(abVice.getId());
            entity.setVouInfo(ab);
            entity.setVouViceInfo(abVice);
            // 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
            if (flag) {
                entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
                entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
            } else {
                entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
                entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
            }
            // abVoucherinfoViceService.updateBackBriefId(abVice.getId(), "");//
            // 从表清除打回标记
            // 业务类型
            entity.setBriefId(AccebillConstant.BRIEF_STATUS_6120);
            entity.setLastid(id);
            entity.setLastbillcode(ab.getBillStatus());
            entity.setBankendrsmtmk(abAccrecords.getBankendrsmtmk());
            if (AccebillConstant.AB_BILL_PAYEMITSTTLMMK1.equals(pay.getEmitSttlmmk())) {
                entity.setEmitSttlmmk(AccebillConstant.AB_BILL_PAYEMITSTTLMMK1);
            } else {
                entity.setEmitSttlmmk(AccebillConstant.AB_BILL_PAYEMITSTTLMMK0);
            }
            // 申请日期
            entity.setBusinessDate(pay.getDate());// 业务归属日期
            entity.setApplyDate(new Date());// 申请日期
            entity.setBillFlag(ab.getBillFlag());
            // 添加保证金信息
            entity.setBailAcc(abAccrecords.getBailAcc());
            entity.setBailmoney(abAccrecords.getBailmoney());
            entity.setBailType(abAccrecords.getBailType());
            entity.setBailpercent(abAccrecords.getBailpercent());
            entity.setBailrate(abAccrecords.getBailrate());
            if(idMapInfo.containsKey(id)){
                entity.setAbs(idMapInfo.get(id).getPayUse());
                entity.setRmk(idMapInfo.get(id).getPayAbs());
            }
            abList.add(entity);
        }
        return abList;
    }
    /**
     * 根据流水查询开票信息
     *
     * @param aid 流水ID
     * @return
     */
    @Transactional(readOnly = true)
    public AbEmitBillVo getAbEmitBillVoByAid(String aid) {
        return abEmitBillService.getAbEmitBillVoByAid(aid, true);
    }
    /**
     * 根据流水查询开票信息（线上兑付新增编辑页面获取信息）
     *
     * @param aid 流水ID
     * @return
     */
    @Transactional(readOnly = true)
    public AbEmitBillVo getAbEmitBillVoByAidAndIsCashingOnlinePayee(String aid) {
        AbEmitBillVo abEmitBillVo = abEmitBillService.getAbEmitBillVoByAid(aid, true);
        if (StringUtils.isNotEmpty(abEmitBillVo.getPayPayeeacc())) {
            return abEmitBillVo;
        }
        // 根据兑付申请线上兑付收款人设置情况查询收款人信息
        String isCashingOnlinePayee = abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISCASHINGONLINEPAYEE).toFormatString();
        // 银票：默认承兑人
        boolean isBankToAcc = isCashingOnlinePayee.indexOf("1") > -1;
        boolean isBusinessToAcc = isCashingOnlinePayee.indexOf("2") > -1;
        // 商票：默认收票人
        String payPayeeacc = "";
        // 银票：默认承兑人
        if (isBankToAcc && AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abEmitBillVo.getBillType()))) {
            payPayeeacc = abEmitBillVo.getAcceptoracc();
        }
        // 商票：默认收票人
        if (isBusinessToAcc && AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(abEmitBillVo.getBillType()))) {
            payPayeeacc = abEmitBillVo.getPayeeacc();
        }
        if (StringUtils.isNotEmpty(payPayeeacc)) {
            // 如果账号为0，则承兑人信息为银行信息，根据联行号获取信息
            if ("0".equals(payPayeeacc) && AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abEmitBillVo.getBillType()))) {
                List<String> bankCodelist = new ArrayList<>();
                bankCodelist.add(abEmitBillVo.getAcceptorbankno());
                Map<String, InputBankInfoDTO> bankInfoMap = inputBankInfoService.getBankCodeInfoByBankCodes(bankCodelist);
                if (StringUtils.isNotEmpty(bankInfoMap) && bankInfoMap.containsKey(abEmitBillVo.getAcceptorbankno()) && StringUtils.isNotEmpty(bankInfoMap.get(abEmitBillVo.getAcceptorbankno()))) {
                    InputBankInfoDTO bankInfo = bankInfoMap.get(abEmitBillVo.getAcceptorbankno());
                    abEmitBillVo.setPayPayeeacc(payPayeeacc);// 收票人账号
                    abEmitBillVo.setPayPayeeName(bankInfo.getBankName());// 收票人名称
                    abEmitBillVo.setPayPayeeBankNo(bankInfo.getBankCode());// 收票人开户行行号
                    abEmitBillVo.setPayPayeeBankName(bankInfo.getBankName());// 收票人开户行行名
                    abEmitBillVo.setProv(bankInfo.getProv());// 所在省
                    abEmitBillVo.setCity(bankInfo.getCity());// 所在市
                }
            } else {
                List<String> acclist = new ArrayList<>();
                acclist.add(payPayeeacc);
                // 根据账号查询客商账户信息
                Map<String, ExternalCorpBankAccDTO> externalCorpBankAccVOMap = externalCorpService.getExternalCorpBankAccByAcc(acclist);
                if (StringUtils.isNotEmpty(externalCorpBankAccVOMap) && externalCorpBankAccVOMap.containsKey(payPayeeacc) && StringUtils.isNotEmpty(externalCorpBankAccVOMap.get(payPayeeacc))) {
                    ExternalCorpBankAccDTO externalCorpBankAccDTO = externalCorpBankAccVOMap.get(payPayeeacc);
                    abEmitBillVo.setPayPayeeacc(externalCorpBankAccDTO.getExternalAcc());// 收票人账号
                    abEmitBillVo.setPayPayeeName(externalCorpBankAccDTO.getExternalAcc());// 收票人名称
                    abEmitBillVo.setPayPayeeBankNo(externalCorpBankAccDTO.getBankSourceCode());// 收票人开户行行号
                    abEmitBillVo.setPayPayeeBankName(externalCorpBankAccDTO.getBankTypeName());// 收票人开户行行名
                    abEmitBillVo.setProv(externalCorpBankAccDTO.getAccProvince());// 所在省
                    abEmitBillVo.setCity(externalCorpBankAccDTO.getAccCity());// 所在市
                }
                // 根据账号查询账户信息
                Map<String, BtBankAccDTO> btBankAccDTOMap = iBtBankAccService.getBankAccByInfoAccs(acclist);
                if (StringUtils.isNotEmpty(btBankAccDTOMap) && btBankAccDTOMap.containsKey(payPayeeacc) && StringUtils.isNotEmpty(btBankAccDTOMap.get(payPayeeacc))) {
                    BtBankAccDTO btBankAccDTO = iBtBankAccService.getBankAccInfo(payPayeeacc);
                    abEmitBillVo.setPayPayeeacc(btBankAccDTO.getBankAcc());// 收票人账号
                    abEmitBillVo.setPayPayeeName(btBankAccDTO.getAccName());// 收票人名称
                    abEmitBillVo.setPayPayeeBankNo(btBankAccDTO.getBankCode());// 收票人开户行行号
                    abEmitBillVo.setPayPayeeBankName(btBankAccDTO.getBankName());// 收票人开户行行名
                    abEmitBillVo.setProv(btBankAccDTO.getProv());// 所在省
                    abEmitBillVo.setCity(btBankAccDTO.getCity());// 所在市
                }
            }
        }
        return abEmitBillVo;
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = abWorkFlowService
                .getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_ABEMITBILLPAY, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput dp = abEmitBillPayDao.dataTableApprove(dataTablesInput, businessList);
        List<AbEmitBillPayVo> data = dp.getData();
        if (data != null && !data.isEmpty()) {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG" , "AB_BILL_OPENWAY" , "AB_BILL_AbEmitBillBack_STATUS"
                    , "AB_BILL_AbEmitBillPay_BRIEF_ID", "AB_BILL_ABACCRECORDS_EMITSTTLMMK","AB_BILL_AbEmitBillPay_STATUS");
            for (AbEmitBillPayVo vo : data) {
                // 冗余数据
                injectValues(vo, listDictMap);
            }
        }
        dp.setData(data);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 工作流启动回调方法(无需关注)
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // Auto-generated method stub
    }
    /**
     * 审批回调方法
     *
     * @param workFlowResult processDefinitionKey
     *                       工作流类型key
     *                       businessKey
     *                       业务类型key
     *                       result
     *                       审批结果:true审批通过，false审批打回
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
        approvalPass(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
    }
    @Override
    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        return this.getAbEmitBillVoByAid(aid);
    }
    /**
     * 针对电票银行承兑票据到期系统自动线下兑付确认定时任务触发执行
     */
    @Override
    @XxlJob("AbEmitBillPayService")
    public void run(String arguments) {
        arguments = XxlJobHelper.getJobParam();
        logger.info("定时任务接收参数：【{}】", arguments);
        try {
            ICurrentUser userVo = abUserService.getScheduler();
            autoPay(userVo, arguments);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    /**
     * 电票银行承兑票据到期系统自动线下兑付确认自动业务处理
     */
    private void autoPay(ICurrentUser userVo, String arguments) {
        if (AccebillConstant.ACCEBILL_TASK_AUTOPAY.equals(arguments)) {
            this.logger.info("---启用商票提示付款签收业务完成系统自动线下兑付确认，开始执行定时任务---");
            List<AbEmitBillPayVo> autoInfoList = listAutoTradeBillAbEmitBillPayInfo(arguments);
            if (autoInfoList == null || autoInfoList.isEmpty()) {
                this.logger.info("---商票提示付款签收业务完成系统自动线下兑付确认，【0】条数据。");
                return;
            }
            this.logger.info("---商票提示付款签收业务完成系统自动线下兑付确认，【" + autoInfoList.size() + "】条数据。");
            cashingConfirmationTask(autoInfoList, userVo);
            this.logger.info("---启用商票提示付款签收业务完成系统自动线下兑付确认，结束执行定时任务---");
        }else if (AccebillConstant.ACCEBILL_TASK_AUTO_CONFRIMEPAY.equals(arguments)) {
            this.logger.info("---启用电票到期线上兑付自动确认已付款，开始执行定时任务---");
            List<AbEmitBillPayVo> autoInfoList = abEmitBillPayDao.listAutoConfrimePay();
            if(!CollectionUtil.isEmpty(autoInfoList)){
                List<String> erpList = autoInfoList.stream().map(AbEmitBillPayVo::getId).collect(Collectors.toList());
                List<NisBillheadDTO> nisBillheadDTOS = nisBillheadService.listByErpIds(erpList);
                if(CollectionUtil.isEmpty(nisBillheadDTOS)){
                    this.logger.info("---电票到期线上兑付自动确认已付款，【0】条数据。");
                    return;
                }
                Map<String, String> payIdAndErpMap = nisBillheadDTOS.stream().collect(Collectors.toMap(NisBillheadDTO::getId, NisBillheadDTO::getErpId));
                Map<String, BisExcDTO> bisExcMapByCbsBillIdsMap = iBisExcService.getBisExcMapByCbsBillIds(new ArrayList<>(payIdAndErpMap.keySet()), null, Arrays.asList(new String[]{"0"}));
                if(CollectionUtil.isEmpty(bisExcMapByCbsBillIdsMap)){
                    this.logger.info("---电票到期线上兑付自动确认已付款，【0】条数据。");
                    return;
                }
                autoInfoList = new ArrayList<>();
                for(String key : bisExcMapByCbsBillIdsMap.keySet()){
                    AbEmitBillPayVo vo = new AbEmitBillPayVo();
                    vo.setId(payIdAndErpMap.get(key));
                    autoInfoList.add(vo);
                }
            }
            if (autoInfoList == null || autoInfoList.isEmpty()) {
                this.logger.info("---电票到期线上兑付自动确认已付款，【0】条数据。");
                return;
            }
            this.logger.info("---电票到期线上兑付自动确认已付款，【" + autoInfoList.size() + "】条数据。");
            confrimePayTask(autoInfoList, userVo);
            this.logger.info("---启用电票到期线上兑付自动确认已付款，结束执行定时任务---");
        }else if(AccebillConstant.ACCEBILL_TASK_AUTO_CLAIMPAY.equals(arguments)){
            this.logger.info("---启用票据到期自动解付,兑付状态自动认领成功的票据，自动进行到期线下解付，开始执行定时任务---");
            List<AbEmitBillPayVo> autoInfoList = abClaimService.getClaimedBillPayInfo();
            if (autoInfoList == null || autoInfoList.isEmpty()) {
                this.logger.info("---票据到期自动解付,兑付状态自动认领成功的票据，自动进行到期线下解付定时任务，【0】条数据。");
                return;
            }
            this.logger.info("----票据到期自动解付,兑付状态自动认领成功的票据，自动进行到期线下解付，本次共处理【" + autoInfoList.size() + "】条数据。");
            cashingConfirmationTask(autoInfoList, userVo);
            this.logger.info("---票据到期自动解付,启用兑付状态自动认领成功的票据，自动进行到期线下解付，结束执行定时任务---");
        }else if (StringUtils.isEmpty(arguments)) {
            this.logger.info("---启用电票银行承兑票据到期系统自动线下兑付确认，开始执行定时任务---");
            List<AbEmitBillPayVo> autoInfoList = listAutoAbEmitBillPayInfo();
            if (autoInfoList == null || autoInfoList.isEmpty()) {
                this.logger.info("---电票银行承兑票据到期自动线下兑付确认，【0】条数据。");
                return;
            }
            this.logger.info("---电票银行承兑票据到期自动线下兑付确认，【" + autoInfoList.size() + "】条数据。");
            cashingConfirmationTask(autoInfoList, userVo);
            this.logger.info("---启用电票银行承兑票据到期系统自动线下兑付确认，结束执行定时任务---");
        }
    }
    private void cashingConfirmationTask(List<AbEmitBillPayVo> autoInfoList, ICurrentUser userVo) {
        for (AbEmitBillPayVo reqInfo : autoInfoList) {
            reqInfo.setIds(reqInfo.getId());
            reqInfo.setVvids(reqInfo.getVvid());
            reqInfo.setVids(reqInfo.getVid());
            reqInfo.setTask(true);
            reqInfo.setDate(reqInfo.getDate());
            cashingConfirmation(reqInfo, userVo);
        }
    }
    /**
     * 电票到期线上兑付自动确认已付款
     * @param autoInfoList
     * @param userVo
     */
    private void confrimePayTask(List<AbEmitBillPayVo> autoInfoList, ICurrentUser userVo) {
        for (AbEmitBillPayVo reqInfo : autoInfoList) {
            confrimepay(reqInfo.getId(), userVo);
        }
    }
    /**
     * 加载银票可以做自动线下兑付确认的数据
     *
     * @return 返回符合条件的数据
     */
    @Transactional(readOnly = true)
    public List<AbEmitBillPayVo> listAutoAbEmitBillPayInfo() {
        boolean isEmitMake = abVoucherinfoViceService.isEmitMake();
        return abEmitBillPayDao.listAutoAbEmitBillPayInfo(isEmitMake);
    }
    /**
     * 加载商票可以做自动线下兑付确认的数据
     *
     * @return 返回符合条件的数据
     */
    @Transactional(readOnly = true)
    public List<AbEmitBillPayVo> listAutoTradeBillAbEmitBillPayInfo(String arguments) {
        boolean isEmitMake = abVoucherinfoViceService.isEmitMake();
        return abEmitBillPayDao.listAutoTradeBillAbEmitBillPayInfo(isEmitMake, arguments);
    }
    /**
     * 未到期应付票据集合
     *
     * @param dataTablesInput 前端传参
     * @return 查询集合
     */
    public DataTablesOutput billPayInfoDataTable(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_PAY_QUERY, dataTablesInput.getCurrentUserId());
        dataTablesInput.getFormdata().put(AccebillConstant.AB_CONFIG_ISEMITMAKE, abVoucherinfoViceService.isEmitMake());
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_PAY_QUERY, corpIds);
        return abEmitBillPayDao.listAbEmitBillPay(dataTablesInput, corpAuthList);
    }
}
