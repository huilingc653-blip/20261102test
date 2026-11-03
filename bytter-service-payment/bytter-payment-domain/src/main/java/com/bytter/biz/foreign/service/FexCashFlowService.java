package com.bytter.biz.foreign.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.bdlink.api.dto.BisExcChangeMsg;
import com.bytter.biz.bdlinks.service.impl.BisForeignExcServiceImpl;
import com.bytter.biz.foreign.dao.FexCashFlowCustInfoDao;
import com.bytter.biz.foreign.dao.FexCashFlowDao;
import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexCashFlowCustInfo;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.vo.FexCashFlowCustInfoVO;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.foreign.vo.FexCotpBankAccVO;
import com.bytter.biz.foreign.vo.FexCotpVO;
import com.bytter.biz.foreign.vo.FexTranCustInfoVO;
import com.bytter.biz.foreign.vo.FexTranReqConfigVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.limit.enums.EnterpriseLimitMessageEnum;
import com.bytter.biz.limit.service.EnterpriseLimitControlService;
import com.bytter.saas.management.exchange.api.dto.PaymentLimitContextDto;
import com.bytter.saas.management.exchange.api.dto.PaymentLimitResultDto;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.api.IAttachmentService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import com.bytter.system.api.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 交易管理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@WorkFlow(FexWorkFlowConstant.FEX_CASH_FLOW)
@Service("fexCashFlowService")
@Transactional
public class FexCashFlowService extends BaseService<FexCashFlow, String> implements IWorkFlowListener, IWorkFlowRevokeListener, ICorpDisableVerifyService {
    @Autowired
    private FexCashFlowDao fexCashFlowDao;
    @Autowired
    private EnterpriseLimitControlService enterpriseLimitControlService;
    @Autowired
    private FexMessageService fexMessageService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private IWorkFlowService workFlowServiceImpl;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private FexTranReqService fexTranReqService;
    @DubboReference(check = false)
    private IAttachmentService attachmentService;
    @Autowired
    private FexCashFlowCustInfoDao fexCashFlowCustInfoDao;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @Autowired
    private BisForeignExcServiceImpl bisForeignExcService;
    @DubboReference(check = false)
    private IConfigService configService;
    // @Autowired
    // private BisForeignExcService bisForeignExcBackService;
    @DubboReference(check = false)
    private IMultiLangService multiLangService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    @Autowired
    private FexCotpService fexCotpService;
    @Autowired
    private FexCotpBankAccService fexCotpBankAccService;

    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;

    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;

    @Autowired
    private FexTranCustInfoService fexTranCustInfoService;

    @Autowired
    private FexTranReqConfigService fexTranReqConfigService;


    @Override
    public BaseDao<FexCashFlow, String> getEntityDao() {
        return fexCashFlowDao;
    }

    private void updateExternalCorpInfo(FexCashFlowVO fexCashFlow, ICurrentUser user) {
        fexCotpBankAccService.updateExternalCorpInfo(fexCashFlow, user);
    }

    private void setPayeeExternalCorpInfo(BisForeignExcDTO bisExc, FexCashFlowVO fexCashFlow, FexTranReqVO fexTranReq) {
        FexCotpVO cotp = getFexCotpById(fexCashFlow.getCotpId());
        if (cotp == null) {
            return;
        }
        String countryName = StringUtils.isNotBlank(cotp.getCountryName()) ? cotp.getCountryName() : getCountryName(cotp.getCountryId());
        bisExc.setClsFlag(cotp.getClsFlag());
        bisExc.setPayeeProv(cotp.getCountryId());
        if ("中国".equals(countryName)) {
            bisExc.setPayeeCity(cotp.getCityName());
            bisExc.setPayeeProvName(StringEscapeUtils.unescapeHtml(StringUtils.isNotBlank(cotp.getProvName()) ? cotp.getProvName() : countryName));
            bisExc.setPayeeCityName(StringEscapeUtils.unescapeHtml(cotp.getCityName()));
        } else {
            bisExc.setPayeeCity(StringUtils.isNotBlank(cotp.getAddressNameEn()) ? cotp.getAddressNameEn() : StringUtils.isNotBlank(cotp.getAddressName()) ? cotp.getAddressName() : cotp.getAddress());
            bisExc.setPayeeProvName(StringEscapeUtils.unescapeHtml(cotp.getState()));
            String payeeCityName = StringUtils.isNotBlank(fexTranReq.getCotpAddressNameEn()) ? fexTranReq.getCotpAddressNameEn()
                    : StringUtils.isNotBlank(cotp.getAddressNameEn()) ? cotp.getAddressNameEn() : cotp.getAddressName();
            bisExc.setPayeeCityName(StringEscapeUtils.unescapeHtml(payeeCityName));
        }
        bisExc.setPayeeNameEn(StringEscapeUtils.unescapeHtml(cotp.getCotpNameEn()));
        bisExc.setPayeeAddrEn(StringEscapeUtils.unescapeHtml(cotp.getAddressEn()));
        bisExc.setPayeeStreetName(StringEscapeUtils.unescapeHtml(cotp.getStreetAddr()));
        bisExc.setPayeeBuildingNumber(StringEscapeUtils.unescapeHtml(cotp.getBuildingNumber()));
        bisExc.setPayeeTel(cotp.getTel());
    }

    private FexCotpVO getFexCotpById(String id) {
        return StringUtils.isBlank(id) ? null : fexCotpService.getFexcotpById(id);
    }

    private String getFexCotpBankName(FexCashFlowVO fexCashFlow) {
        FexCotpBankAccVO bankAcc = getFexCotpBankAcc(fexCashFlow);
        return bankAcc == null ? null : bankAcc.getBankName();
    }

    private FexCotpBankAccVO getFexCotpBankAcc(FexCashFlowVO fexCashFlow) {
        if (StringUtils.isBlank(fexCashFlow.getCotpAccId())) {
            return null;
        }
        return fexCotpBankAccService.getFexFexCotpBankAcc(fexCashFlow.getCotpAccId());
    }

    private String getCountryName(String id) {
        SysCountryCustDTO country = getCountry(id);
        return country == null ? "" : country.getNameZh();
    }

    private SysCountryCustDTO getCountry(String id) {
        if (StringUtils.isBlank(id) || sysCountryCustService == null) {
            return null;
        }
        return sysCountryCustService.getById(id);
    }

    /**
     * 查询交易信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder joinSql = new StringBuilder();
        String resource = "";
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        if ("list".equals(opType)) {
            joinSql.append(" and t.status  in (" + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK + "," + NetbankConstant.STATUE_CHECK + ")");
            resource = ResourceConstant.FOREIGN_FEXCASHFLOW_QUERY;
        }else{
            resource = ResourceConstant.FOREIGN_FEXCASHFLOW_ALL_QUERY;
        }
        //支付状态
        String payStatusSearch = (String) dataTablesInput.getFormdata().get("eqs_payStatusSearch");
        if(StringUtils.isNotEmpty(payStatusSearch)) {
            if ("w".equals(payStatusSearch)) {
                //待付款
                dataTablesInput.getFormdata().put("eqs_payStatus", CommonConstant.NO);
                dataTablesInput.getFormdata().put("eqs_cdSign", CommonConstant.YES);
                joinSql.append(" and exc.voucher_Stat is null");
            } else if ("p".equals(payStatusSearch)) {
                //已付款
                dataTablesInput.getFormdata().put("eqs_payStatus", CommonConstant.YES);
                dataTablesInput.getFormdata().put("eqs_cdSign", CommonConstant.YES);
                joinSql.append(" and exc.voucher_Stat is null");
            } else if ("w1".equals(payStatusSearch)) {
                //待收款
                dataTablesInput.getFormdata().put("eqs_payStatus", CommonConstant.NO);
                dataTablesInput.getFormdata().put("eqs_cdSign", CommonConstant.NO);
                joinSql.append(" and exc.voucher_Stat is null");
            } else if ("p1".equals(payStatusSearch)) {
                //已收款
                dataTablesInput.getFormdata().put("eqs_payStatus", CommonConstant.YES);
                dataTablesInput.getFormdata().put("eqs_cdSign", CommonConstant.NO);
                joinSql.append(" and exc.voucher_Stat is null");
            } else {
                dataTablesInput.getFormdata().put("eqs_voucherStat", payStatusSearch);
            }
        }

        if("dtl".equals(opType)){
            //收款流水认领只查询未勾兑的数据
            joinSql.append(" and (t.BANK_FLOW_SIGN = '0' or  t.BANK_FLOW_SIGN is null )");
            joinSql.append(" and t.status  in (" + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK + "," + NetbankConstant.STATUE_CHECK + ")");
        }
        if (!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "t.corp_Id", true);
            joinSql.append(" and " + corpAuthSql);
        }
        // 预测现金流不推送结算执行
        joinSql.append(" and  t.bill_No is not null ");
        appendExternalCorpNameFilters(dataTablesInput, joinSql);
        DataTablesOutput output = fexCashFlowDao.getDataTablesUnion(dataTablesInput, joinSql.toString());
        if (output != null) {
            injectListValues(output.getData());
        }
        return output;
    }

    private void injectListValues(List<FexCashFlowVO> listData) {
        if (EmptyUtils.isEmpty(listData)) {
            return;
        }
        // 单位ID集合
        List<String> corpIdList = listData.stream().map(FexCashFlowVO::getCorpId).filter(ObjectUtil::isNotEmpty).distinct().collect(Collectors.toList());
        List<String> coptIdList = listData.stream().map(FexCashFlowVO::getAllocatCotpId).filter(ObjectUtil::isNotEmpty).distinct().collect(Collectors.toList());
        corpIdList.addAll(coptIdList);
        // 币种ID集合
        List<String> curIdList = listData.stream().map(FexCashFlowVO::getCurId).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        // 主体单位银行ID集合
        List<String> bankTypeIdList = listData.stream().map(FexCashFlowVO::getCorpBank).filter(org.apache.commons.lang3.StringUtils::isNotEmpty).collect(Collectors.toList());
        // 资金调拨交易的交易对手银行存的是 bt_bank_type 的 id，一并取出来用于回填银行名称
        List<String> coptBankTypeIdList = listData.stream().map(FexCashFlowVO::getAllocatCotpBank).filter(org.apache.commons.lang3.StringUtils::isNotEmpty).collect(Collectors.toList());
        bankTypeIdList.addAll(coptBankTypeIdList);
        List<String> externalCorpIds = new ArrayList<>();
        List<String> externalCorpBankAccIds = new ArrayList<>();
        List<String> externalCorpBankAccs = new ArrayList<>();
        for (FexCashFlowVO vo : listData) {
            addIfNotEmpty(externalCorpIds, getExternalCorpId(vo));
            addIfNotEmpty(externalCorpBankAccIds, vo.getCotpAccId());
            if (StringUtils.isBlank(vo.getCotpAccId())) {
                addIfNotEmpty(externalCorpBankAccs, vo.getCotpBankAcc());
            }
        }
        Map<String, CorpDTO> corpMap = EmptyUtils.isEmpty(corpIdList) ? new HashMap<>() : corpService.listCorpDtoByCorpIds(corpIdList);
        Map<String, BtCurrencyDTO> currencyMap = EmptyUtils.isEmpty(curIdList) ? new HashMap<>() : btCurrencyService.getCurrencyByIds(curIdList);
        Map<String, BankTypeDTO> bankTypeMap = EmptyUtils.isEmpty(bankTypeIdList) ? new HashMap<>() : bankTypeService.listBankTypeMapByIds(bankTypeIdList);
        Map<String, DictDTO> voucherStatDict = dictService.listMap("bisExc.voucherStat");
        // 落地限额处理结果文案：批量取一次，供前后端分离接口直接展示 landingLimitResultName
        Map<String, DictDTO> landingResultDict = dictService.listMap(EnterpriseLimitControlService.LANDING_RESULT_DICT);
        Map<String, ExternalCorpDTO> externalCorpMap = getExternalCorpMap(externalCorpIds);
        Map<String, ExternalCorpBankAccDTO> externalCorpBankAccMap = getExternalCorpBankAccMap(externalCorpBankAccIds);
        Map<String, ExternalCorpBankAccDTO> externalCorpBankAccByAccMap = getExternalCorpBankAccMapByBankAcc(externalCorpBankAccs);
        listData.forEach(item -> {
            Optional.ofNullable(item.getCurId()).flatMap(curId -> Optional.ofNullable(currencyMap)
                            .flatMap(curMap -> Optional.ofNullable(currencyMap.get(curId))))
                    .ifPresent(currency -> {
                        item.setCurName(currency.getCurName());
                        item.setEnglishCode(currency.getEnglishCode());
                    });
            Optional.ofNullable(item.getCorpId()).flatMap(corpId -> Optional.ofNullable(corpMap)
                            .flatMap(corpListMap -> Optional.ofNullable(corpMap.get(corpId))))
                    .ifPresent(corp -> item.setCorpName(corp.getName()));
            Optional.ofNullable(item.getAllocatCotpId()).flatMap(corpId -> Optional.ofNullable(corpMap)
                            .flatMap(corpListMap -> Optional.ofNullable(corpMap.get(corpId))))
                    .ifPresent(corp -> item.setAllocatCotpName(corp.getName()));
            Optional.ofNullable(item.getCorpBank()).flatMap(corpBank -> Optional.ofNullable(bankTypeMap)
                            .flatMap(bankMap -> Optional.ofNullable(bankTypeMap.get(corpBank))))
                    .ifPresent(bankType -> item.setCorpBankName(bankType.getTypeName()));
            // 资金调拨交易：交易对手银行存的是 bt_bank_type 的 id，按 id 回填银行名称，
            // 否则列表「交易对手银行」列（绑定 allocatCotpBankName）会为空
            Optional.ofNullable(item.getAllocatCotpBank()).flatMap(corpBank -> Optional.ofNullable(bankTypeMap)
                            .flatMap(bankMap -> Optional.ofNullable(bankTypeMap.get(corpBank))))
                    .ifPresent(bankType -> item.setAllocatCotpBankName(bankType.getTypeName()));
            item.setVoucherStatName(voucherStatDict.getOrDefault(item.getVoucherStat(), DictDTO.EMPTY).getName());
            // 未涉及落地（值为 null）时字典取不到，名称留空
            item.setLandingLimitResultName(landingResultDict.getOrDefault(item.getLandingLimitResult(), DictDTO.EMPTY).getName());
            fillExternalCorpInfo(item, getExternalCorp(externalCorpMap, getExternalCorpId(item)));
            ExternalCorpBankAccDTO bankAcc = getExternalCorpBankAcc(externalCorpBankAccMap, item.getCotpAccId());
            if (bankAcc == null) {
                bankAcc = getExternalCorpBankAccByBankAcc(externalCorpBankAccByAccMap, item.getCotpBankAcc());
            }
            fillExternalCorpBankAccInfo(item, bankAcc);
        });
    }

    /**
     * 根据ID获取交易信息
     * @param id
     * @return
     */
    public FexCashFlowVO getByIdUnion(String id){
        FexCashFlowVO cashFlowVO = fexCashFlowDao.getByIdUnion(id);
        injectValues(cashFlowVO);
        return cashFlowVO;
    }

    private void injectValues(FexCashFlowVO cashFlowVO) {
        if (cashFlowVO == null) {
            return;
        }
        injectListValues(Arrays.asList(cashFlowVO));
    }

    /**
     * 根据billNo获取交易信息
     * @param billNo
     * @return
     */
    public FexCashFlowVO getByBillNoUnion(String billNo){
        FexCashFlowVO cashFlowVO = fexCashFlowDao.getByBillNoUnion(billNo);
        injectValues(cashFlowVO);
        return cashFlowVO;
    }

    /**
     * 根据交易获取交易信息
     * @param tranBillId
     * @return
     */
    public List<FexCashFlow> listByTranBillId(String tranBillId){
        return fexCashFlowDao.listBy("tranBillId", tranBillId);
    }

    /**
     * 根据交易获取交易信息
     * @param tranBillId
     * @return
     */
    public List<FexCashFlowVO> listByTranBillIdUnion(String tranBillId){
        List<FexCashFlowVO> listData = fexCashFlowDao.listByTranBillIdUnion(tranBillId);
        injectListValues(listData);
        return listData;
    }

    /**
     * 根据结算号获取交易信息(只获取明细)
     * @param settleNo
     * @return
     */
    public List<FexCashFlowVO> listBySettleNoUnion(String settleNo){
        List<FexCashFlowVO> listData = fexCashFlowDao.listBySettleNoUnion(settleNo);
        injectListValues(listData);
        return listData;
    }

    /**
     * 批量提交
     *
     * @param ids
     */
    public void batchSubmit(String ids, ICurrentUser user) {
        logger.info("fexCashFlowService batchSubmit:" + ids);
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FexCashFlow entity = this.get(id, true);
            FexCashFlowVO fexCashFlow = new FexCashFlowVO();
            BeanUtils.copyNotNullProperties(entity, fexCashFlow);
            fexCashFlow.setStatus(CommonConstant.STATUE_WAIT_CHECK);
            fexCashFlow.setUpdateBy(user.getLoginName());
            fexCashFlow.setUpdateDate(DateUtils.getDate());
            //编辑付款单，控制原状态为0暂存/-1打回
            Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
            this.updateFexCashFlowStatus(fexCashFlow, oldStatus);
            entity.setStatus(CommonConstant.STATUE_WAIT_CHECK);
            // 暂存/打回明细批量提交时，在启动审批前按待审批状态核对限额，缺少占用则补占。
            syncEnterpriseLimit(entity, user);

            //启动审批
            Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
            if (CommonConstant.STATUE_WAIT_CHECK.equals(fexCashFlow.getStatus())) {
                messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
                BeanUtils.copyNotNullProperties(entity, fexCashFlow);
                logger.info("fexCashFlowService startWorkFlow:" + JSONUtil.toJsonStr(fexCashFlow));
                //inject(fexCashFlow);
                 getStartHandler().handler(FexWorkFlowConstant.FEX_CASH_FLOW, entity.getId(), fexCashFlow, entity.getCorpId(), user.getId());
                logger.info("fexCashFlowService startWorkFlow success");
            }
            //生成日志
            fexMessageService.saveMessage(entity.getId(), messageType, "", FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW, user);
            //完成通知提醒
            notificationService.complete(NotificationConstant.FOREIGN.FOREIGN_CASHFLOW_APPLY, entity.getId(), user.getId());
            logger.info("fexCashFlowService success");
        }
    }

    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        FexCashFlow fexCashFlow = this.get(workFlowResult.getBusinessKey(), true);
        FexCashFlowVO fexCashFlowVO = new FexCashFlowVO();
        BeanUtils.copyNotNullProperties(fexCashFlow, fexCashFlowVO);
        //更新审批状态
        fexCashFlowVO.setStatus(NetbankConstant.STATUE_TEMP);
        fexCashFlowVO.setUpdateBy(user.getLoginName());
        fexCashFlowVO.setUpdateDate(DateUtils.getDate());
        //审批付款单，更新原状态控制为待审批
        Integer[] oldStatus = {NetbankConstant.STATUE_WAIT_CHECK};
        this.updateFexCashFlowStatus(fexCashFlowVO, oldStatus);
        //生成日志
        fexMessageService.saveMessage(fexCashFlow.getId(), FexMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, FexMessageConstant.FEX_LOG_WITHDRAW_SUCC, FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW, user);
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub

    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        logger.info("fexCashFlowService flowCompleted:" + workFlowResult.getBusinessKey());
        ICurrentUser user = workFlowResult.getUser();
        FexCashFlow entity = this.get(workFlowResult.getBusinessKey(), true);
        boolean pass = Boolean.TRUE.equals(workFlowResult.isPass());
        // 落地限额（银行端限额服务）：企业端审批通过后，仅对"审批通过后会生成线上银企汇款指令"的资金收付付款现金流
        // 判断是否达到落地限额（见 isLandingLimitReached，buildEnterpriseLimitContext 内已含 tranType=4/cdSign=YES/
        // settleType=1/账户在线/金额>0 等线上口径）；命中则 markApprovePassForLanding 把现金流<b>提前置为审批通过
        // （status 11→95）</b>，但"审批通过的内容"（支付状态编排/汇款指令 saveBisForeignExc 等）<b>暂不执行</b>——
        // 延后到内管端落地限额审批通过后的回调 completeTranApprove(->doFlowCompleted) 才真正执行，落地标识
        // LANDING_LIMIT_RESULT 置 0（待执行）作幂等依据（银行未通过前不生成指令）。
        if (pass && isOnlineInstruction(entity) && isLandingLimitReached(entity)) {
            markApprovePassForLanding(entity, user);
            fexMessageService.saveMessage(entity.getId(), FexMessageConstant.MESSAGE_TYPE_SUBMIT,
                    "已提交银行端落地限额审批，审批通过后自动生效", FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW, user);
            return;
        }
        this.doFlowCompleted(entity, user, pass, false);
    }

    /**
     * 审批通过/打回收尾执行（内部方法，含落地回调"单据已提前置 95"入口）。
     * 内容为原 flowCompleted 收尾逻辑（支付状态编排 + saveBisForeignExc 发指令 + 终态 CAS + 日志）。
     *
     * @param alreadyApproved 单据是否已处于审批通过（status=95，落地限额回调入口）：
     *                        是则终态 CAS 旧状态白名单仅 {95}；否则与 flowCompleted 入口一致（{11}）。
     */
    private void doFlowCompleted(FexCashFlow entity, ICurrentUser user, boolean pass, boolean alreadyApproved) {
        FexCashFlowVO fexCashFlowVO = new FexCashFlowVO();
        BeanUtils.copyNotNullProperties(entity, fexCashFlowVO);
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        if (pass) {
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            fexCashFlowVO.setStatus(CommonConstant.STATUE_CHECK);//审批通过
            if (ForeignConstant.SETTLE_TYPE_2.equals(entity.getSettleType())) {
                // 净额合并单没有交易 ID；审批通过不代表实际收付完成。
                fexCashFlowVO.setPayStatus(CommonConstant.NO);
            } else if (CommonConstant.NO.equals(entity.getCdSign())) {
                FexTranReqVO fexTranReq = this.fexTranReqService.getByIdUnion(entity.getTranBillId());
                //支付状态更新为1-已收款
                fexCashFlowVO.setPayStatus(CommonConstant.YES);
                if(ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())) {
                    //资金调拨（不同币种）收款现金流生成付款指令
                    if (!fexTranReq.getCurId().equals(fexTranReq.getTargetAccCurId())
                            && !CommonConstant.YES.equals(fexTranReq.getIsPayed())) {
                        fexCashFlowVO.setPayStatus(CommonConstant.NO);
                        this.saveBisForeignExc(entity, user);
                    }
                } else if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
                    //外汇买卖交易：即期、购汇、本金交割，固定金额为1-买入金额，非掉期交易
                    fexCashFlowVO.setPayStatus(CommonConstant.YES);
                }
            }else {
                //付款现金流：离线账号或者是否已线下支付为是，审批通过后，支付状态更新为1-已付款，不生成指令
                //线下付款标志为1-是，不生成指令
                FexTranReqVO fexTranReq = this.fexTranReqService.getByIdUnion(entity.getTranBillId());
                //获取付款账号
                BtBankAccDTO payerAcc = btBankAccService.getBankAccById(entity.getCorpAccId());
                if (CommonConstant.YES.equals(fexTranReq.getIsPayed()) ||
                        payerAcc == null || BtBankAccConstant.ISONLINE_0.equals(payerAcc.getIsOnline())) {
                    //支付状态更新为1-已付款
                    fexCashFlowVO.setPayStatus(CommonConstant.YES);
                }else{
                    //支付状态更新为1-已付款
                    fexCashFlowVO.setPayStatus(CommonConstant.YES);
                    //资金收付交易且结算类型为全额审批通过后产生指令
                    if (ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())
                            && ForeignConstant.SETTLE_TYPE_1.equals(entity.getSettleType())) {
                        //收支方向为：1-付款 且 支付状态为：0-待付款 走银企直连产生指令
                        fexCashFlowVO.setPayStatus(CommonConstant.NO);
                        //发送汇款指令
                        this.saveBisForeignExc(entity, user);
                    } else if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())
                            && ForeignConstant.SETTLE_TYPE_1.equals(entity.getSettleType())) {
                        //即期结汇的外汇买卖交易付款现金流产生付款指令
                        fexCashFlowVO.setPayStatus(CommonConstant.YES);
                    }else if(ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())){
                        fexCashFlowVO.setPayStatus(CommonConstant.NO);
                        //资金调拨付款现金流生成付款指令
                        this.saveBisForeignExc(entity, user);
                    }
                }
            }
        } else {
            fexCashFlowVO.setStatus(CommonConstant.STATUE_BACK);//审批打回
        }
        fexCashFlowVO.setUpdateBy(user.getLoginName());
        fexCashFlowVO.setUpdateDate(DateUtils.getDate());
        //审批现金流，更新原状态控制为待审批（落地回调入口单据已是 95，白名单仅 {95}）
        Integer[] oldStatus = alreadyApproved
                ? new Integer[]{CommonConstant.STATUE_CHECK} : new Integer[]{NetbankConstant.STATUE_WAIT_CHECK};
        this.updateFexCashFlowStatus(fexCashFlowVO, oldStatus);
        //生成日志
        fexMessageService.saveMessage(entity.getId(), messageType, "", FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW, user);
    }

    /**
     * 落地限额处理结果回调收尾（银行端限额服务 -> 企业端）：
     * 按处理结果完成审批通过（pass=true）/ 打回（pass=false）收尾。
     * 与 flowCompleted 的区别：不再判断落地限额（处理单已生成，直接按结果收尾）。
     * 内管端回调可能不带审批人 user，取单据 updateBy（兜底 createBy）作为操作人。
     * <p>
     * 落地预置单据（markApprovePassForLanding 已提前置 95、审批内容未执行）回调时，按 LANDING_LIMIT_RESULT=0 识别：
     * 先把落地标识条件翻转（仅 result=0 可翻）为 A/R 作为并发互斥闸门——翻转成功者才收尾；失败（0 行）说明
     * 已被并发回调收尾，抛状态变更异常（单据已 95 且 result 非 0，重试将落入 handler 幂等分支，不会双收尾）。
     * 翻转成功后单据已是 95，直接以"已提前置 95"入口调 doFlowCompleted（终态 CAS 白名单 {95}），不再假复位 95→11。
     */
    public void completeTranApprove(String businessId, boolean pass, ICurrentUser user) {
        FexCashFlow entity = this.get(businessId, true);
        if (entity == null) {
            throw new ParameterException("资金收付现金流单据不存在: " + businessId);
        }
        if (user == null) {
            user = buildFallbackUser(entity);
        }
        if (isLandingPending(entity)) {
            // 落地标识条件翻转（仅 LANDING_LIMIT_RESULT=0 可翻）作互斥闸门：0 行 = 已被并发回调翻转收尾，中止
            if (fexCashFlowDao.updateLandingLimitResultIfPending(entity.getId(),
                    pass ? EnterpriseLimitControlService.LANDING_RESULT_AGREE
                         : EnterpriseLimitControlService.LANDING_RESULT_REJECT) < 1) {
                throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
            // 单据已是 95：以"已提前置 95"入口直接执行收尾内容
            this.doFlowCompleted(entity, user, pass, true);
        } else {
            this.doFlowCompleted(entity, user, pass, false);
        }
    }

    /**
     * 是否"审批通过后会生成线上银企汇款指令"的资金收付付款现金流（落地限额仅对该类现金流生效，其余类型收尾不受影响）。
     * 线上口径（tranType=4/cdSign=YES/settleType=1/账户在线/金额>0）由 {@link #buildEnterpriseLimitContext} 承载，
     * 此处仅补 flowCompleted 通过分支 saveBisForeignExc 的"未线下已支付"触发条件（线下已付/离线不产生落地审批）。
     */
    private boolean isOnlineInstruction(FexCashFlow entity) {
        if (entity == null || StringUtils.isBlank(entity.getTranBillId())) {
            return false;
        }
        FexTranReqVO fexTranReq = this.fexTranReqService.getByIdUnion(entity.getTranBillId());
        return fexTranReq != null && !CommonConstant.YES.equals(fexTranReq.getIsPayed());
    }

    /**
     * 落地校验：资金收付现金流是否达到落地限额。由内管判断是否需要发起落地审批。
     * context == null（非资金收付全额付款现金流/账户不在线等，见 buildEnterpriseLimitContext）= 不落地，返回 false。
     * 内管判断失败（服务不可用/返回异常）时抛错中止审批收尾，不放行。
     */
    private boolean isLandingLimitReached(FexCashFlow entity) {
        PaymentLimitContextDto ctx = buildEnterpriseLimitContext(entity, getCurrentUser());
        if (ctx == null) {
            return false;
        }
        PaymentLimitResultDto<Boolean> result = enterpriseLimitControlService.isLandingApprovalRequired(ctx);
        // 落地预判断失败码统一映射企业端限额消息枚举，避免降级为未知错误。
        EnterpriseLimitControlService.requireSuccess(result);
        if (result.getData() == null) {
            throw new BusinessException("资金收付落地限额判断失败: 内管未返回判断结果");
        }
        return result.getData();
    }

    /**
     * 落地限额命中：企业端审批通过（workflow 已 complete）即把现金流<b>提前置为审批通过（status 11→95）</b>，
     * 并记更新人/更新时间；但"审批通过的内容"（支付状态编排/汇款指令 saveBisForeignExc 等）<b>暂不执行</b>，
     * 延后到内管端落地限额审批通过回调 {@link #completeTranApprove}（->doFlowCompleted）再真正执行。
     * <p>
     * 状态更新守卫与 flowCompleted 收尾 CAS 同源（同一 VO 装配 + 旧状态白名单 {STATUE_WAIT_CHECK}）——仅当现金流
     * 当前仍为 11 待审批才翻转；并发下状态已变则抛状态变更异常（NIS_PAY_MESSAGE_STATUS_CHANGE），避免覆盖并发收尾。
     * 落地回调入口白名单为 {95}，见 {@link #completeTranApprove}。
     */
    private void markApprovePassForLanding(FexCashFlow entity, ICurrentUser user) {
        FexCashFlowVO vo = new FexCashFlowVO();
        BeanUtils.copyNotNullProperties(entity, vo);
        // 提前置为审批通过，但内容（指令/支付状态）不执行，待回调 doFlowCompleted
        vo.setStatus(CommonConstant.STATUE_CHECK);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        // 旧状态白名单与 flowCompleted 收尾 CAS 一致（仅 11 待审批）
        Integer[] oldStatus = {NetbankConstant.STATUE_WAIT_CHECK};
        this.updateFexCashFlowStatus(vo, oldStatus);
        // 落地标识=待执行（内容未跑，等内管回调）。updateFexCashFlowStatus 的 HQL 不含此列，单独落库。
        fexCashFlowDao.updateLandingLimitResult(entity.getId(), EnterpriseLimitControlService.LANDING_RESULT_PENDING);
    }

    /**
     * 内管端回调可能不带审批人 user，取单据 updateBy（兜底 createBy）作为操作人。
     */
    private ICurrentUser buildFallbackUser(FexCashFlow entity) {
        String loginName = StringUtils.isNotBlank(entity.getUpdateBy()) ? entity.getUpdateBy() : entity.getCreateBy();
        UserDTO user = new UserDTO();
        user.setId(loginName);
        user.setLoginName(loginName);
        user.setUsername(loginName);
        return user;
    }

    private boolean isLandingPending(FexCashFlow entity) {
        return entity != null
                && CommonConstant.STATUE_CHECK.equals(entity.getStatus())
                && EnterpriseLimitControlService.LANDING_RESULT_PENDING.equals(entity.getLandingLimitResult());
    }

    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(FexWorkFlowConstant.FEX_CASH_FLOW, dataTablesInput);
        StringBuilder businessSql = new StringBuilder(SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "t.id", true));
        appendExternalCorpNameFilters(dataTablesInput, businessSql);
        DataTablesOutput<FexCashFlowVO> dp = this.fexCashFlowDao.dataTableApprove(dataTablesInput, businessSql.toString());
        Optional.ofNullable(dp.getData()).ifPresent(this::injectListValues);
        return workFlowBusiness.merge(dp);
    }

    /**
     * 结算退回
     *
     * @param ids
     */
    public void batchBack(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FexCashFlow entity = this.get(id, true);
            //净额不允许做结算退回操作
            if(ForeignConstant.SETTLE_TYPE_2.equals(entity.getSettleType())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_SETTLETYPE_1, entity.getBillNo());
            }
            if(CommonConstant.STATUE_CHECK.equals(entity.getStatus())
                    && (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType()) || ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType()))){
                BisForeignExcDTO bisForeignExcDTO = bisForeignExcService.getByCbsBillId(id);
                if(bisForeignExcDTO!=null && BdlinkConstant.WAIT_FILL.equals(bisForeignExcDTO.getVoucherStat())){
                    //指令退回
                    bisForeignExcService.backBisForeignExcForCashFlow(bisForeignExcDTO.getId(),user);
                }else{
                    throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, entity.getBillNo());
                }
                entity.setStatus(CommonConstant.STATUE_BACK);
                entity.setUpdateBy(user.getLoginName());
                entity.setUpdateDate(DateUtils.getDate());
                this.saveOrUpdate(entity);
            }else {
                //资金调拨/外汇买卖交易联动修改另一笔流水的状态
                if ((ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType()) || ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())
                        && StringUtils.isNotBlank(entity.getTranBillId()))) {
                    List<FexCashFlow> list = this.fexCashFlowDao.listBy("tranBillId", entity.getTranBillId());
                    for (FexCashFlow cashFlow : list) {
                        if(CommonConstant.STATUE_DELETE.equals(cashFlow.getStatus())){
                            continue;
                        }
                        //现金流【状态】必须为0-暂存或者-1-打回
                        if (!CommonConstant.STATUE_TEMP.equals(cashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(cashFlow.getStatus())) {
                            throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, cashFlow.getBillNo());
                        }
                        cashFlow.setStatus(CommonConstant.STATUE_DELETE);
                        cashFlow.setUpdateBy(user.getLoginName());
                        cashFlow.setUpdateDate(DateUtils.getDate());
                        this.updateEntity(cashFlow);
                    }
                }else {
                    //现金流【状态】必须为0-暂存或者-1-打回
                    if (!CommonConstant.STATUE_TEMP.equals(entity.getStatus()) && !CommonConstant.STATUE_BACK.equals(entity.getStatus())
                            && !CommonConstant.STATUE_CHECK.equals(entity.getStatus())) {
                        throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, entity.getBillNo());
                    }
                    entity.setStatus(CommonConstant.STATUE_DELETE);
                    entity.setUpdateBy(user.getLoginName());
                    entity.setUpdateDate(DateUtils.getDate());
                    this.updateEntity(entity);
                }
                //打回交易
                if(StringUtils.isNotBlank(entity.getTranBillId())) {
                    fexTranReqService.backFexTranReq(entity.getTranBillId(), user);
                }
            }
        }
    }

    /**
     * 净额结算
     *
     * @param ids
     */
    public void merge(String ids, ICurrentUser user) {
        //合并金额
        BigDecimal payMoney = BigDecimal.ZERO;
        //合并附件
        List<String> attachmentList = new ArrayList<String>();
        List<FexCashFlow> list = new ArrayList<FexCashFlow>();
        FexCashFlow entity = new FexCashFlow();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FexCashFlow t = this.get(id, true);
            BeanUtils.copyNotNullProperties(t, entity);
            //合计金额
            if(CommonConstant.YES.equals(t.getCdSign())){
                payMoney = payMoney.add(t.getPayMoney().multiply(new BigDecimal(-1)));
            }else {
                payMoney = payMoney.add(t.getPayMoney());
            }
            if (!StringUtils.isBlank(t.getAttachmentId())) {
                attachmentList.add(t.getAttachmentId());
            }
            list.add(t);
        }
        //合并附件
        if (!EmptyUtils.isEmptyList(attachmentList)) {
            entity.setAttachmentId(attachmentService.mergeGroupIds(attachmentList));
        }
        entity.setId(null);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_CASH_FLOW));
        entity.setSettleNo(SequenceSingleton.getSequence(SequenceEnum.FEX_CASH_FLOW_SETTLENO));
        entity.setSettleType(ForeignConstant.SETTLE_TYPE_2);
        //汇总金额
        if(payMoney.compareTo(BigDecimal.ZERO)<0){
            entity.setCdSign(CommonConstant.YES);
        }else{
            entity.setCdSign(CommonConstant.NO);
        }
        entity.setPayMoney(payMoney.abs());
        entity.setStatus(NetbankConstant.STATUE_TEMP);
        entity.setPayStatus(CommonConstant.NO);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        //交易号码、产品、现金流类型为空
        entity.setTranBillId(null);
        entity.setCashFlowType(null);
        //生成合并单据
        this.saveEntity(entity);

        //更新合并明细单状态
        for (FexCashFlow t : list) {
            t.setStatus(NetbankConstant.STATUE_DEL);
            //保存合并单号
            t.setSettleNo(entity.getSettleNo());
            t.setUpdateBy(user.getLoginName());
            t.setUpdateDate(DateUtils.getDate());
            this.updateEntity(t);
            //完成通知提醒
            notificationService.complete(NotificationConstant.FOREIGN.FOREIGN_CASHFLOW_APPLY, t.getId(), user.getId());
        }
    }

    /**
     * 取消净额结算
     *
     * @param ids
     */
    public void mergeCancel(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FexCashFlow entity = this.get(id, true);
            //获取合并明细
            List<FexCashFlow> list = fexCashFlowDao.listBy("settleNo", entity.getSettleNo());
            for (FexCashFlow t : list) {
                if(id.equals(t.getId())){
                    continue;
                }
                t.setStatus(NetbankConstant.STATUE_TEMP);
                t.setSettleNo(null);
                t.setUpdateBy(user.getLoginName());
                t.setUpdateDate(DateUtils.getDate());
                this.updateEntity(t);
            }

            //删除合并单据
            entity.setStatus(NetbankConstant.STATUE_DEL);
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
            this.updateEntity(entity);
            //完成通知提醒
            notificationService.complete(NotificationConstant.FOREIGN.FOREIGN_CASHFLOW_APPLY, entity.getId(), user.getId());
        }
    }

    /**
     * 修改费用报销单
     *
     * @param fexCashFlow
     */
    public void updateFexCashFlow(FexCashFlowVO fexCashFlow, ICurrentUser user) {
        FexCashFlow entity = fexCashFlowDao.get(fexCashFlow.getId(), true);
        if (ForeignConstant.SETTLE_TYPE_2.equals(entity.getSettleType())
                && (CommonConstant.STATUE_CHECK.equals(entity.getStatus())
                || "4".equals(fexCashFlow.getOpType()))) {
            // 净额结算审批完成后，只允许在实际收付完成时确认支付状态。
            if (!CommonConstant.STATUE_CHECK.equals(entity.getStatus())
                    || !"4".equals(fexCashFlow.getOpType())
                    || !CommonConstant.NO.equals(entity.getPayStatus())
                    || !CommonConstant.YES.equals(fexCashFlow.getPayStatus())) {
                throw new ParameterException("净额结算单只能在实际收付完成后由待收付更新为已收付");
            }
            BtBankAccDTO corpAcc = btBankAccService.getBankAccById(entity.getCorpAccId());
            if (corpAcc == null || !BtBankAccConstant.ISONLINE_0.equals(corpAcc.getIsOnline())) {
                throw new ParameterException("线上净额结算尚未接通银行指令及结果回写，不能手工确认收付");
            }
            entity.setPayStatus(CommonConstant.YES);
            entity.setRmk(fexCashFlow.getRmk());
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(entity);
            return;
        }
        /**操作类型 0-结算处理、1-修改支付方式、2-修改单位账户、3-交易对手账户、4-更新支付状态*/
        if("0".equals(fexCashFlow.getOpType())){
            //结算处理
            if(CommonConstant.NO.equals(entity.getCdSign())){
                entity.setFactsMoney(fexCashFlow.getFactsMoney());
                entity.setFactsDate(fexCashFlow.getFactsDate());
                entity.setAttachmentId(fexCashFlow.getAttachmentId());
                entity.setRmk(fexCashFlow.getRmk());
            }else{
                entity.setDifBank(fexCashFlow.getDifBank());
            }
            entity.setAttachmentId(fexCashFlow.getAttachmentId());
            entity.setRmk(fexCashFlow.getRmk());
            entity.setStatus(fexCashFlow.getStatus());
            //新增自定义信息
            fexCashFlowCustInfoDao.deleteCustFieldInfoByBusId(fexCashFlow.getId());
            List<FexCashFlowCustInfo> fexTranCustInfos = new ArrayList<>();
            if (!EmptyUtils.isEmptyList(fexCashFlow.getFexCashFlowCustInfos())) {
                for (FexCashFlowCustInfoVO fexCashFlowCustInfoVO : fexCashFlow.getFexCashFlowCustInfos()) {
                    if (StringUtils.isNotEmpty(fexCashFlowCustInfoVO.getFieldId())) {
                        FexCashFlowCustInfo fexCashFlowCustInfo = new FexCashFlowCustInfo();
                        BeanUtils.copyProperties(fexCashFlowCustInfoVO, fexCashFlowCustInfo);
                        fexCashFlowCustInfo.setBusId(entity.getId());
                        fexTranCustInfos.add(fexCashFlowCustInfo);
                    }
                }
            }
            fexCashFlowCustInfoDao.batchSave(fexTranCustInfos);
        }else if("1".equals(fexCashFlow.getOpType())){
            //修改支付方式
            entity.setVoucherTypeId(fexCashFlow.getVoucherTypeId());
            entity.setRmk(fexCashFlow.getRmk());
        }else if("2".equals(fexCashFlow.getOpType())){
            //修改单位账户
            entity.setCorpAccId(fexCashFlow.getCorpAccId());
            entity.setCorpBankAcc(fexCashFlow.getCorpBankAcc());
            entity.setCorpAccName(fexCashFlow.getCorpAccName());
            entity.setCorpBank(fexCashFlow.getCorpBank());
            entity.setVoucherTypeId(fexCashFlow.getVoucherTypeId());
            entity.setRmk(fexCashFlow.getRmk());

            if(StringUtils.isNotBlank(entity.getTranBillId())) {
                if (ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())) {
                    //资金调拨交易联动修改另一笔流水的交易对手信息以及联动修改交易信息
                    List<FexCashFlow> list = this.fexCashFlowDao.listBy("tranBillId", entity.getTranBillId());
                    for(FexCashFlow cashFlow:list){
                        if(cashFlow.getId().equals(entity.getId()) || CommonConstant.STATUE_DELETE.equals(cashFlow.getStatus())){
                            continue;
                        }else{
                            //现金流【状态】必须为0-暂存或者-1-打回
                            if(!CommonConstant.STATUE_TEMP.equals(cashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(cashFlow.getStatus())){
                                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, cashFlow.getBillNo());
                            }else {
                                cashFlow.setCotpAccId(entity.getCorpAccId());
                                cashFlow.setCotpBankAcc(entity.getCorpBankAcc());
                                cashFlow.setCotpAccName(entity.getCorpAccName());
                                cashFlow.setCotpBank(entity.getCorpBank());
                                cashFlow.setUpdateBy(user.getLoginName());
                                cashFlow.setUpdateDate(DateUtils.getDate());
                                this.saveOrUpdate(cashFlow);
                            }
                        }
                    }

                    //现金流的收付方向为付款，则更新交易的源单位账户信息，收款则更新交易的目标账户信息
                    FexTranReq tranReq = fexTranReqService.get(entity.getTranBillId(), true);
                    if(CommonConstant.YES.equals(entity.getCdSign())){
                        tranReq.setSrcAccId(entity.getCorpAccId());
                        tranReq.setSrcBank(entity.getCorpBank());
                        tranReq.setSrcBankAcc(entity.getCorpBankAcc());
                        tranReq.setSrcAccName(entity.getCorpAccName());
                    }else{
                        tranReq.setTargetAccId(entity.getCorpAccId());
                        tranReq.setTargetBank(entity.getCorpBank());
                        tranReq.setTargetBankAcc(entity.getCorpBankAcc());
                        tranReq.setTargetAccName(entity.getCorpAccName());
                    }
                    tranReq.setUpdateBy(user.getLoginName());
                    tranReq.setUpdateDate(DateUtils.getDate());
                    fexTranReqService.saveOrUpdate(tranReq);
                } else if (ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())) {
                    //资金收付交易联动修改交易信息
                    FexTranReq tranReq = fexTranReqService.get(entity.getTranBillId(), true);
                    tranReq.setCorpAccId(entity.getCorpAccId());
                    tranReq.setCorpBank(entity.getCorpBank());
                    tranReq.setCorpBankAcc(entity.getCorpBankAcc());
                    tranReq.setCorpAccName(entity.getCorpAccName());
                    tranReq.setUpdateDate(DateUtils.getDate());
                    tranReq.setUpdateBy(user.getLoginName());
                    fexTranReqService.saveOrUpdate(tranReq);
                }
            }
        }else if("3".equals(fexCashFlow.getOpType())){
            //修改交易对手账户
            entity.setCotpAccId(fexCashFlow.getCotpAccId());
            entity.setCotpBankAcc(fexCashFlow.getCotpBankAcc());
            entity.setCotpAccName(fexCashFlow.getCotpAccName());
            entity.setCotpBank(fexCashFlow.getCotpBank());
            entity.setCurId(fexCashFlow.getCurId());

            if(StringUtils.isNotBlank(entity.getTranBillId())) {
                if (ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())) {
                    //资金调拨交易联动修改另一笔流水的主体账户信息以及联动修改交易信息
                    List<FexCashFlow> list = this.fexCashFlowDao.listBy("tranBillId", entity.getTranBillId());
                    for (FexCashFlow cashFlow : list) {
                        if (cashFlow.getId().equals(entity.getId()) || CommonConstant.STATUE_DELETE.equals(cashFlow.getStatus())) {
                            continue;
                        } else {
                            //现金流【状态】必须为0-暂存或者-1-打回
                            if (!CommonConstant.STATUE_TEMP.equals(cashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(cashFlow.getStatus())) {
                                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, cashFlow.getBillNo());
                            } else {
                                cashFlow.setCorpAccId(entity.getCotpAccId());
                                cashFlow.setCorpBankAcc(entity.getCotpBankAcc());
                                cashFlow.setCorpAccName(entity.getCotpAccName());
                                cashFlow.setCorpBank(entity.getCotpBank());
                                cashFlow.setUpdateBy(user.getLoginName());
                                cashFlow.setUpdateDate(DateUtils.getDate());
                                this.saveOrUpdate(cashFlow);
                            }
                        }
                    }

                    //现金流的收付方向为付款，则更新交易的目标账户信息，收款则更新交易的源单位账户信息
                    FexTranReq tranReq = fexTranReqService.get(entity.getTranBillId(), true);
                    if (CommonConstant.YES.equals(entity.getCdSign())) {
                        tranReq.setTargetAccId(entity.getCotpAccId());
                        tranReq.setTargetBank(entity.getCotpBank());
                        tranReq.setTargetBankAcc(entity.getCotpBankAcc());
                        tranReq.setTargetAccName(entity.getCotpAccName());
                    } else {
                        tranReq.setSrcAccId(entity.getCotpAccId());
                        tranReq.setSrcBank(entity.getCotpBank());
                        tranReq.setSrcBankAcc(entity.getCotpBankAcc());
                        tranReq.setSrcAccName(entity.getCotpAccName());
                    }
                    tranReq.setUpdateBy(user.getLoginName());
                    tranReq.setUpdateDate(DateUtils.getDate());
                    fexTranReqService.saveOrUpdate(tranReq);
                } else if (ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())) {

                    updateExternalCorpInfo(fexCashFlow, user);

                    //资金收付交易联动修改交易信息
                    FexTranReq tranReq = fexTranReqService.get(entity.getTranBillId(), true);
                    tranReq.setCotpAccId(entity.getCotpAccId());
                    tranReq.setCotpBank(entity.getCotpBank());
                    tranReq.setCotpBankAcc(entity.getCotpBankAcc());
                    tranReq.setCotpAccName(entity.getCotpAccName());
                    tranReq.setCurId(entity.getCurId());
                    tranReq.setUpdateDate(DateUtils.getDate());
                    tranReq.setUpdateBy(user.getLoginName());
                    fexTranReqService.saveOrUpdate(tranReq);
                }
            }
        }else if("4".equals(fexCashFlow.getOpType())){
            //更新支付状态
            entity.setPayStatus(fexCashFlow.getPayStatus());
            entity.setRmk(fexCashFlow.getRmk());
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);

        //结算处理-审批挂接
        if("0".equals(fexCashFlow.getOpType())){
            //启动审批
            Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
            if (CommonConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
                messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
                BeanUtils.copyNotNullProperties(entity, fexCashFlow);
                //inject(fexCashFlow);
                 getStartHandler().handler(FexWorkFlowConstant.FEX_CASH_FLOW, entity.getId(), fexCashFlow, entity.getCorpId(), user.getId());
            }
            //生成日志
            fexMessageService.saveMessage(entity.getId(), messageType, "", FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW, user);
            //完成通知提醒
            notificationService.complete(NotificationConstant.FOREIGN.FOREIGN_CASHFLOW_APPLY, entity.getId(), user.getId());
        }
    }

    /**
     * 外汇买卖交易现金流统计
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesForeignReport(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        //外汇买卖交易
        sql.append(" and t.tran_Type='" + ForeignConstant.TRAN_TYPE_1+"'");
        sql.append(" and t.status=" + ForeignConstant.STATUE_CHECK);
        String resource = ResourceConstant.FOREIGN_FEXTRANREQ_CASHFLOW_REPORT_QUERY;
        String joinSql = "";
        if (!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "t.corp_Id", true);
            joinSql += " and " + corpAuthSql;
        }
        sql.append(joinSql);
        //过滤掉交易失败的数据
        sql.append(" and (exc.voucher_stat not in ('9','2') or exc.voucher_no is null)");
        appendExternalCorpNameFilters(dataTablesInput, sql);
        DataTablesOutput dataTablesOutput = fexCashFlowDao.getDataTablesReport(dataTablesInput, sql.toString());
        this.setValues(dataTablesOutput);
        return dataTablesOutput;
    }

    /**
     * 汇款指令生成
     *
     * @param entity
     */
    public void saveBisForeignExc(FexCashFlow entity, ICurrentUser user) {
        FexCashFlowVO fexCashFlow = this.getByIdUnion(entity.getId());
        if(fexCashFlow == null){
            fexCashFlow = new FexCashFlowVO();
            BeanUtils.copyNotNullProperties(entity, fexCashFlow);
            if(!ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())) {
                //获取交易对手银行
                String cotpBankName = getFexCotpBankName(fexCashFlow);
                if (StringUtils.isNotBlank(cotpBankName)) {
                    fexCashFlow.setCotpBankName(cotpBankName);
                }
            }
        }
        //获取交易
        FexTranReqVO fexTranReq = null;
        if(StringUtils.isNotBlank(fexCashFlow.getTranBillId())){
            fexTranReq = fexTranReqService.getByIdUnion(fexCashFlow.getTranBillId());
        }else{
            fexTranReq = new FexTranReqVO();
        }
        //线下付款标志为1-是，不生成指令
        if(CommonConstant.YES.equals(fexTranReq.getIsPayed())){
            return;
        }
        //获取付款账号
        BtBankAccDTO payerAcc = null;
        if(CommonConstant.NO.equals(entity.getCdSign())) {
            //收款现金流 交易对手为付款方
            payerAcc = btBankAccService.getBankAccById(fexCashFlow.getCotpAccId());
        }else{
            //付款现金流 交易对手为收款方
            payerAcc = btBankAccService.getBankAccById(fexCashFlow.getCorpAccId());
        }
        if (!CommonConstant.NO.equals(entity.getCdSign()) && !ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
            //离线账号不生成指令
            if (payerAcc == null || BtBankAccConstant.ISONLINE_0.equals(payerAcc.getIsOnline())) {
                return;
            }
        }
        //如果是外汇买卖交易
        if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())){
            //离线账号不生成指令
            if (payerAcc == null || BtBankAccConstant.ISONLINE_0.equals(payerAcc.getIsOnline())) {
                return;
            }
        }
        //生成付款指令
        BisForeignExcDTO bisExc = new BisForeignExcDTO();
        bisExc.setVoucherFrom(ForeignConstant.FROM_TRAN);
        bisExc.setExcSource(ForeignConstant.EXC_SOURCE_CASH_FLOW);
        bisExc.setCbsBillId(fexCashFlow.getId());
        bisExc.setCbsBillNo(fexCashFlow.getBillNo());
        bisExc.setCorpId(fexCashFlow.getCorpId());
        bisExc.setReqDate(DateUtils.getDate());
        bisExc.setPayDate(fexCashFlow.getSettleDate());
        bisExc.setRemitType(fexTranReq.getRemitType());
        bisExc.setBankTypeId(fexCashFlow.getCorpBank());
        bisExc.setFixedMoney(fexTranReq.getFixedMoney());
        //新增跨行标志和外汇业务类型
        bisExc.setDifBank(fexCashFlow.getDifBank());
        bisExc.setFexBusType(fexTranReq.getFexBusType());
        bisExc.setSaveFields(fexTranReq.getSaveFields());
        bisExc.setSettleBusType(fexTranReq.getSettleBusType());
        if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
            //外汇买卖交易存入交易对手账号
            bisExc.setCotpBankAcc(StringEscapeUtils.unescapeHtml(fexCashFlow.getCotpBankAcc()));
        }
        if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
            //外汇买卖交易：付款方信息(卖出信息-付款方)
            bisExc.setPayerAcc(StringEscapeUtils.unescapeHtml(fexTranReq.getSrcBankAcc()));
            bisExc.setPayerAccName(StringEscapeUtils.unescapeHtml(fexTranReq.getSrcAccName()));
            payerAcc = btBankAccService.getBankAccInfo(fexTranReq.getSrcBankAcc());
            if (payerAcc != null) {
                bisExc.setPayerBank(StringEscapeUtils.unescapeHtml(payerAcc.getBankName()));
                bisExc.setBifCode(payerAcc.getBifCode());
            }
        } else {
            //付款方信息
            bisExc.setPayerAcc(StringEscapeUtils.unescapeHtml(payerAcc.getBankAcc()));
            bisExc.setPayerAccName(StringEscapeUtils.unescapeHtml(payerAcc.getAccName()));
            bisExc.setPayerBank(StringEscapeUtils.unescapeHtml(payerAcc.getBankName()));
            bisExc.setBifCode(payerAcc.getBifCode());
        }
        bisExc.setPayerName(StringEscapeUtils.unescapeHtml(fexTranReq.getCorpContact()));
        bisExc.setPayerTel(fexTranReq.getCorpContactTel());
        bisExc.setCurId(fexCashFlow.getCurId());
        //付款账号币种
        bisExc.setPayerCurId(fexCashFlow.getPayerCurId());
        bisExc.setPayMoney(fexCashFlow.getPayMoney());

        // 自定义薪资指令
        setCustExc(bisExc, fexCashFlow.getTranBillId());

        //收款方信息
        BtBankAccDTO bankAcc = null;
        if((ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType()))
                && CommonConstant.NO.equals(entity.getCdSign())) {
            bisExc.setPayeeAcc(StringEscapeUtils.unescapeHtml(fexCashFlow.getCorpBankAcc()));
            bisExc.setPayeeAccName(StringEscapeUtils.unescapeHtml(fexCashFlow.getCorpAccName()));
            bankAcc = btBankAccService.getBankAccInfo(fexCashFlow.getCorpBankAcc());
            if (bankAcc != null) {
                bisExc.setPayeeBank(StringEscapeUtils.unescapeHtml(bankAcc.getBankName()));
                bisExc.setPayeeSwiftCode(bankAcc.getSwiftCode());
                bisExc.setPayeeBankCode(bankAcc.getBankCode());
            }
            //获取收款方的省市地区
            bisExc.setPayeeProv(fexTranReq.getCotpProvName());
            //设置地点
            if ("中国".equals(fexTranReq.getCotpCountryName())) {
                bisExc.setPayeeCity(fexTranReq.getCotpCityName());
            } else {
                bisExc.setPayeeCity(fexTranReq.getCotpAddressName());
            }
            bisExc.setBankTypeId(payerAcc.getBankTypeId());
        }else{
            if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
                //外汇买卖交易付款流水生成指令
                //收款方信息（买入信息-收款方账号）
                bisExc.setPayeeAcc(StringEscapeUtils.unescapeHtml(fexTranReq.getCorpBankAcc()));
                bisExc.setPayeeAccName(StringEscapeUtils.unescapeHtml(fexTranReq.getCorpAccName()));
                bankAcc = btBankAccService.getBankAccInfo(fexTranReq.getCorpBankAcc());
                if (bankAcc != null) {
                    bisExc.setPayeeBank(StringEscapeUtils.unescapeHtml(bankAcc.getBankName()));
                    bisExc.setPayeeSwiftCode(StringEscapeUtils.unescapeHtml(bankAcc.getSwiftCode()));
                    bisExc.setPayeeBankCode(StringEscapeUtils.unescapeHtml(bankAcc.getBankCode()));
                }
            } else {
                bisExc.setPayeeAcc(StringEscapeUtils.unescapeHtml(fexCashFlow.getCotpBankAcc()));
                bisExc.setPayeeAccName(StringEscapeUtils.unescapeHtml(fexCashFlow.getCotpAccName()));
                if (ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())) {
                    bankAcc = btBankAccService.getBankAccInfo(fexCashFlow.getCotpBankAcc());
                    if (bankAcc != null) {
                        bisExc.setPayeeBank(StringEscapeUtils.unescapeHtml(bankAcc.getBankName()));
                        bisExc.setPayeeSwiftCode(bankAcc.getSwiftCode());
                        bisExc.setPayeeBankCode(bankAcc.getBankCode());
                    }
                } else {
                    FexCotpBankAccVO cotpBankAcc = getFexCotpBankAcc(fexCashFlow);
                    bisExc.setPayeeBank(StringEscapeUtils.unescapeHtml(fexCashFlow.getCotpBankName()));
                    bisExc.setPayeeSwiftCode(fexTranReq.getCotpSwiftCode());
                    if (cotpBankAcc != null) {
                        bisExc.setPayeeBank(StringEscapeUtils.unescapeHtml(cotpBankAcc.getBankName()));
                        bisExc.setPayeeSwiftCode(cotpBankAcc.getBicCode());
                        bisExc.setPayeeBankCode(cotpBankAcc.getLocalClearNo());
                    }
                }
            }
            setPayeeExternalCorpInfo(bisExc, fexCashFlow, fexTranReq);
        }
        //调拨交易存入收款方账号的币种
        bisExc.setPayeeCurId(fexCashFlow.getPayeeCurId());

        bisExc.setPayeeCountryCode(fexTranReq.getCountryId());
        if(StringUtils.isEmpty(bisExc.getPayeeCountryCode())){
            bisExc.setPayeeCountryCode(fexTranReq.getCotpCountry());
        }
        bisExc.setPayeeAgentBank(StringEscapeUtils.unescapeHtml(fexTranReq.getCotpAgentBank()));
        bisExc.setPayeeAgentBankAdd(StringEscapeUtils.unescapeHtml(fexTranReq.getCotpAgentBankAddr()));
        bisExc.setPayeeAgentAcc(StringEscapeUtils.unescapeHtml(fexTranReq.getAgentBankAcc()));
        //交易对手银行地址
        bisExc.setPayeeBankAdd(fexTranReq.getCotpBankAdrr());
        //其他信息
        bisExc.setQuickSign(fexTranReq.getIsQuick());
        bisExc.setRemitNature(fexTranReq.getRemitNature());
        bisExc.setRemitMethod(fexTranReq.getRemitMethod());
        bisExc.setCostSign(fexTranReq.getCostSign());
        bisExc.setPayType(fexTranReq.getPayType());
        bisExc.setShipDate(fexTranReq.getShipDate());
        bisExc.setIsBonded(fexTranReq.getIsBonded());
        bisExc.setBusNo(fexTranReq.getBusNo());
        bisExc.setContractNo(fexTranReq.getContractNo());
        bisExc.setInvoiceNo(fexTranReq.getInvoiceNo());
        bisExc.setAddWord(StringEscapeUtils.unescapeHtml(fexTranReq.getAddWord()));
        //资金用途
        if(StringUtils.isNotBlank(fexTranReq.getCapitalUse())) {
            bisExc.setRmk(StringEscapeUtils.unescapeHtml(fexTranReq.getCapitalUse()));
        } else {
            bisExc.setRmk(StringEscapeUtils.unescapeHtml(fexTranReq.getRmk()));
        }
        bisExc.setRmkLocal(StringEscapeUtils.unescapeHtml(fexTranReq.getRmkLocal()));
        bisExc.setTranCode1(fexTranReq.getTranCode1());
        bisExc.setTranAmt1(fexTranReq.getTranAmt1());
        bisExc.setTranCurId1(fexTranReq.getTranCurId1());
        bisExc.setTranAddWord1(fexTranReq.getTranAddWord1());
        bisExc.setTranCode2(fexTranReq.getTranCode2());
        bisExc.setTranAmt2(fexTranReq.getTranAmt2());
        bisExc.setTranCurId2(fexTranReq.getTranCurId2());
        bisExc.setTranAddWord2(fexTranReq.getTranAddWord2());

        //新增信息
        /**费用交付方式  1-现金 2-支票 3-账户 默认1*/
        bisExc.setCostMode(ForeignConstant.COST_MODE_1);
        /**扣费账号 默认汇款账号*/
        bisExc.setDedAcc(fexTranReq.getCostAcc());
        /**钞汇代码 1-钞 2-汇 默认2*/
        if (StringUtils.isNotBlank(fexTranReq.getBankNote())) {
            bisExc.setBankCurCode(fexTranReq.getBankNote());
        } else {
            bisExc.setBankCurCode(ForeignConstant.BANK_CUR_CODE_2);
        }
        /**起息日期 默认汇款日期*/
        bisExc.setInterestDate(fexCashFlow.getSettleDate());

        if(ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())){
            CorpDTO corpPayer = corpService.get(payerAcc.getCorpId());
            if (corpPayer != null) {
                // 取账号的英文名称
                bisExc.setPayerNameEn(StringEscapeUtils.unescapeHtml(payerAcc.getAccNameEn()));
                bisExc.setPayerAddrEn(StringEscapeUtils.unescapeHtml(corpPayer.getAddressEn()));
                bisExc.setPayerAddr(StringEscapeUtils.unescapeHtml(corpPayer.getAddress()));
                if ("中国".equals(corpPayer.getCountryName())) {
                    //付款方省份名称
                    bisExc.setPayerProvName(StringEscapeUtils.unescapeHtml(corpPayer.getProvinceName()));
                    //付款方城镇名称
                    bisExc.setPayerCityName(StringEscapeUtils.unescapeHtml(corpPayer.getCityName()));
                } else {
                    //付款方省份名称
                    bisExc.setPayerProvName(StringEscapeUtils.unescapeHtml(corpPayer.getState()));
                    //付款方城镇名称
                    bisExc.setPayerCityName(StringEscapeUtils.unescapeHtml(corpPayer.getCountryAddrName()));
                }
                //付款方邮政编号
                bisExc.setPayerPostCode(StringEscapeUtils.unescapeHtml(corpPayer.getPostCode()));
                //付款方街道名称
                bisExc.setPayerStreetName(StringEscapeUtils.unescapeHtml(corpPayer.getAddress1()));
                //付款方建筑编号
                bisExc.setPayerBuildingNumber(StringEscapeUtils.unescapeHtml(corpPayer.getAddress2()));
            }
            CorpDTO corpPayee = corpService.get(bankAcc.getCorpId());
            if (corpPayee != null) {
                // 取账号的英文名称
                bisExc.setPayeeNameEn(StringEscapeUtils.unescapeHtml(bankAcc.getAccNameEn()));
                bisExc.setPayeeAddrEn(StringEscapeUtils.unescapeHtml(corpPayee.getAddressEn()));
                bisExc.setPayeeBankCode(bankAcc.getBankCode());
                if ("中国".equals(corpPayer.getCountryName())) {
                    //收款方省份名称
                    bisExc.setPayeeProvName(StringEscapeUtils.unescapeHtml(corpPayee.getProvinceName()));
                    //收款方城镇名称
                    bisExc.setPayeeCityName(StringEscapeUtils.unescapeHtml(corpPayee.getCityName()));
                } else {
                    //付款方省份名称
                    bisExc.setPayeeProvName(StringEscapeUtils.unescapeHtml(corpPayee.getState()));
                    //付款方城镇名称
                    bisExc.setPayeeCityName(StringEscapeUtils.unescapeHtml(corpPayee.getCountryAddrName()));
                }
                //收款方邮政编号
                bisExc.setPayeePostCode(StringEscapeUtils.unescapeHtml(corpPayee.getPostCode()));
                //收款方街道名称
                bisExc.setPayeeStreetName(StringEscapeUtils.unescapeHtml(corpPayee.getAddress1()));
                //收款方建筑编号
                bisExc.setPayeeBuildingNumber(StringEscapeUtils.unescapeHtml(corpPayee.getAddress2()));
            }
        }else{
            //付款单位
            CorpDTO corp = corpService.get(fexCashFlow.getCorpId());
            if (corp != null) {
                // 取账号的英文名称
                bisExc.setPayerNameEn(StringEscapeUtils.unescapeHtml(payerAcc.getAccNameEn()));
                bisExc.setPayerAddrEn(StringEscapeUtils.unescapeHtml(corp.getAddressEn()));
                bisExc.setPayerAddr(StringEscapeUtils.unescapeHtml(corp.getAddress()));
                if ("中国".equals(corp.getCountryName())) {
                    //付款方省份名称
                    bisExc.setPayerProvName(StringEscapeUtils.unescapeHtml(corp.getProvinceName()));
                    //付款方城镇名称
                    bisExc.setPayerCityName(StringEscapeUtils.unescapeHtml(corp.getCityName()));
                } else {
                    //付款方省份名称
                    bisExc.setPayerProvName(StringEscapeUtils.unescapeHtml(corp.getState()));
                    //付款方城镇名称
                    bisExc.setPayerCityName(StringEscapeUtils.unescapeHtml(corp.getCountryAddrName()));
                }
                //付款方邮政编号
                bisExc.setPayerPostCode(StringEscapeUtils.unescapeHtml(corp.getPostCode()));
                //付款方街道名称
                bisExc.setPayerStreetName(StringEscapeUtils.unescapeHtml(corp.getAddress1()));
                //付款方建筑编号
                bisExc.setPayerBuildingNumber(StringEscapeUtils.unescapeHtml(corp.getAddress2()));
            }
            setPayeeExternalCorpInfo(bisExc, fexCashFlow, fexTranReq);
        }
        //越南语转换
        bisExc.setPayeeAddrEn(this.formatVietnamese(bisExc.getPayeeAddrEn()));
        if(payerAcc!=null) {
            bisExc.setBankOrgCode(payerAcc.getBankOrgCode());
        } else if (StringUtils.isNotBlank(fexTranReq.getBranchCode())){
            bisExc.setBankOrgCode(fexTranReq.getBranchCode());
        }
        //中国银行取统一社会信用代码
        BisBifInitDTO bifInitDTO = bisBifInitService.getObjectById(bisExc.getBifCode());
        if (bifInitDTO != null && "中国银行".equals(bifInitDTO.getName())) {
            CorpDTO corp = corpService.get(fexTranReq.getCorpId());
            bisExc.setBankOrgCode(corp!=null?corp.getSocCode():"");
        }
        // 外汇买卖交易暂时不设置收款方英文名称
        if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
            bisExc.setPayeeNameEn(null);
        }
        //进口日期
        bisExc.setArrivalDate(fexTranReq.getArrivalDate());
        //报关单号
        bisExc.setDeclareNo(fexTranReq.getDeclareNo());
        //资金性质
        bisExc.setAccNature(fexTranReq.getAccNature());
        //合同利率
        bisExc.setContractRate(fexTranReq.getContractRate());
        //中国农业银行特殊字段：账户类别（汇款账号）、收款人类型、汇款去向、汇款用途
        bisExc.setAccType(fexTranReq.getAccType());
        bisExc.setPayeeType(fexTranReq.getPayeeType());
        bisExc.setRemitRoute(fexTranReq.getRemitRoute());
        if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
            //结汇资金用途
            bisExc.setPurpose(fexTranReq.getPurpose());
            //汇款用途 对应 结汇资金详细用途
            bisExc.setRemitUse(fexTranReq.getPurposeDtl());
            bisExc.setFexProp(fexTranReq.getFexProp());
        } else {
            bisExc.setRemitUse(fexTranReq.getRemitUse());
        }
        //费用币种
        bisExc.setRemChgTolAmtCur(fexTranReq.getCostCurId());
        //费用总额
        bisExc.setRemChgTolAmt(fexTranReq.getCostMoney());
        //本地清算号
        bisExc.setLocalClearNo(fexTranReq.getLocalClearNo());
        //付款清算号
        bisExc.setPayerLocalClearNo(payerAcc.getLocalClearNo());
        //支付类型
        bisExc.setBankPayType(fexTranReq.getBankPayType());
        //受益人关联关系
        bisExc.setBnfRelate(fexTranReq.getBnfRelate());
        //监管类型
        bisExc.setRegulatoryType(fexTranReq.getRegulatoryType());
        //保存附件名称
        if (StringUtils.isNotBlank(fexTranReq.getAttachmentId())) {
            List<AttachmentDTO> list = attachmentService.findAttachmentByGroupId(fexTranReq.getAttachmentId());
            if (EmptyUtils.isNotEmpty(list)){
                String fileName = "";
                for (AttachmentDTO dto:list) {
                    if (StringUtils.isBlank(fileName)) {
                        fileName = dto.getFilePath();
                    } else {
                        fileName = fileName + CommonConstant.COMMA_SPLIT + dto.getFilePath();
                    }
                }
                bisExc.setFileName(fileName);
            }
        }
        //起运国
        if (StringUtils.isNotBlank(fexTranReq.getOriginCountry())) {
            SysCountryCustDTO custDTO = sysCountryCustService.getById(fexTranReq.getOriginCountry());
            if (custDTO != null) {
                bisExc.setOriginCountry(custDTO.getEnCode());
            }
        }
        //买入币种和卖出币种
        bisExc.setBuyCurId(fexTranReq.getBuyCurId());
        bisExc.setSellCurId(fexTranReq.getSellCurId());
        //外汇合同号
        bisExc.setForeignContractNo(fexTranReq.getForeignContractNo());
        //外汇交易类型 0-即期、1-远期
        bisExc.setFexTranType(fexTranReq.getFexTranType());
        //交割类型 0-本金交割、1-无本金交割
        bisExc.setDeliveryType(fexTranReq.getDeliveryType());
        //交割期限
        bisExc.setDeliveryDeadline(fexTranReq.getDeliveryDeadline());
        //费用支出账号币种
        bisExc.setDedAccCurId(fexTranReq.getCostCurId());
        //交易汇率类型
        bisExc.setTranRateType(fexTranReq.getTranRateType());
        //招商云
        bisExc.setGoodsAmount(fexTranReq.getGoodsAmount());
        bisExc.setAdvanceChargeAmount(fexTranReq.getAdvanceChargeAmount());
        bisExc.setAdvanceContractRate(fexTranReq.getAdvanceContractRate());
        bisExc.setRptGateDate(fexTranReq.getRptGateDate());
        if (StringUtils.isNotBlank(fexTranReq.getRptGateEntId())) {
            CorpDTO rptGateEnt = corpService.get(fexTranReq.getRptGateEntId());
            if (rptGateEnt != null) {
                bisExc.setRptGateEntName(rptGateEnt.getName());
                if (StringUtils.isNotBlank(rptGateEnt.getSocCode()) && rptGateEnt.getSocCode().length()>=17) {
                    //报关经营单位名称的组织机构代码：取报关经营单位名称的统一社会信用代码的第9位至第16位+“-”+第17位
                    bisExc.setRptGateEntNo(rptGateEnt.getSocCode().substring(8,16) + '-' + rptGateEnt.getSocCode().substring(16,17));
                }
            }
        }
        bisExc.setRptGateType(fexTranReq.getRptGateType());
        bisExc.setGeneralTradeAmount(fexTranReq.getGeneralTradeAmount());
        bisExc.setFeedAmount(fexTranReq.getFeedAmount());
        bisExc.setOtherTradeAmount(fexTranReq.getOtherTradeAmount());
        bisExc.setBorderTradeAmount(fexTranReq.getBorderTradeAmount());
        bisExc.setCustomAmount(fexTranReq.getCustomAmount());
        bisExc.setOffshoreAmount(fexTranReq.getOffshoreAmount());
        bisExc.setOtherAmount(fexTranReq.getOtherAmount());
        bisExc.setInvestAmt(fexTranReq.getInvestAmt());
        bisExc.setInvestNo(fexTranReq.getInvestNo());
        bisExc.setInvestApplyNbr(fexTranReq.getInvestApplyNbr());
        bisExc.setOftenNo(fexTranReq.getOftenNo());
        bisExc.setDirectInvestmentCertNo(fexTranReq.getDirectInvestmentCertNo());
        if(null != fexTranReq.getAdvanceRate()){
            BigDecimal advanceRate = fexTranReq.getAdvanceRate().multiply(new BigDecimal(100));
            bisExc.setAdvanceRate(advanceRate.intValue());
        }

        bisExc.setCheckDay(fexTranReq.getCheckDay());
        // 判断指令发送模式
        String bisExcAutoAccount = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_BISEXC_AUTOACCOUNT).getValue();
        if (CommonConstant.YES.equalsIgnoreCase(bisExcAutoAccount)) {
            bisExc.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
            this.bisForeignExcService.autoSendBisForeignExc(bisExc, user);
        } else {
            bisExc.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
            this.bisForeignExcService.addBisForeignExc(bisExc, user);
        }
    }


    /**
     * 买卖交易指令生成
     *
     * @param entity
     */
    public Boolean saveBisForeignExc(FexCashFlow entity, ICurrentUser user, String billNo) {
        FexCashFlowVO fexCashFlow = this.getByIdUnion(entity.getId());
        if(fexCashFlow == null){
            fexCashFlow = new FexCashFlowVO();
            BeanUtils.copyNotNullProperties(entity, fexCashFlow);
            if(!ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())) {
                //获取交易对手银行
                String cotpBankName = getFexCotpBankName(fexCashFlow);
                if (StringUtils.isNotBlank(cotpBankName)) {
                    fexCashFlow.setCotpBankName(cotpBankName);
                }
            }
        }
        //获取交易
        FexTranReqVO fexTranReq = null;
        if(StringUtils.isNotBlank(fexCashFlow.getTranBillId())){
            fexTranReq = fexTranReqService.getByIdUnion(fexCashFlow.getTranBillId());
        }else{
            fexTranReq = new FexTranReqVO();
        }
        //线下付款标志为1-是，不生成指令
        if(CommonConstant.YES.equals(fexTranReq.getIsPayed())){
            return Boolean.FALSE;
        }
        //获取付款账号
        BtBankAccDTO payerAcc = btBankAccService.getBankAccById(fexCashFlow.getCorpAccId());
        if (!CommonConstant.NO.equals(entity.getCdSign()) && !ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
            //离线账号不生成指令
            if (payerAcc == null || BtBankAccConstant.ISONLINE_0.equals(payerAcc.getIsOnline())) {
                return Boolean.FALSE;
            }
        }
        //如果是外汇买卖交易
        if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())){
            //离线账号不生成指令
            if (payerAcc == null || BtBankAccConstant.ISONLINE_0.equals(payerAcc.getIsOnline())) {
                return Boolean.FALSE;
            }
        }
        //生成付款指令
        BisForeignExcDTO bisExc = new BisForeignExcDTO();
        bisExc.setVoucherFrom(ForeignConstant.FROM_TRAN);
        bisExc.setExcSource(ForeignConstant.EXC_SOURCE_REQ);
        bisExc.setCbsBillId(fexCashFlow.getTranBillId());
        bisExc.setCbsBillNo(billNo);
        bisExc.setCorpId(fexCashFlow.getCorpId());
        bisExc.setReqDate(DateUtils.getDate());
        bisExc.setPayDate(fexCashFlow.getSettleDate());
        bisExc.setRemitType(fexTranReq.getRemitType());
        bisExc.setBankTypeId(fexCashFlow.getCorpBank());
        bisExc.setFixedMoney(fexTranReq.getFixedMoney());
        //新增跨行标志和外汇业务类型
        bisExc.setDifBank(fexCashFlow.getDifBank());
        bisExc.setFexBusType(fexTranReq.getFexBusType());
        bisExc.setSaveFields(fexTranReq.getSaveFields());
        bisExc.setSettleBusType(fexTranReq.getSettleBusType());
        if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
            //外汇买卖交易存入交易对手账号
            bisExc.setCotpBankAcc(StringEscapeUtils.unescapeHtml(fexCashFlow.getCotpBankAcc()));
        }
        if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
            //外汇买卖交易：付款方信息(卖出信息-付款方)
            bisExc.setPayerAcc(StringEscapeUtils.unescapeHtml(fexTranReq.getSrcBankAcc()));
            bisExc.setPayerAccName(StringEscapeUtils.unescapeHtml(fexTranReq.getSrcAccName()));
            payerAcc = btBankAccService.getBankAccInfo(fexTranReq.getSrcBankAcc());
            if (payerAcc != null) {
                bisExc.setPayerBank(StringEscapeUtils.unescapeHtml(payerAcc.getBankName()));
                bisExc.setBifCode(payerAcc.getBifCode());
            }
        } else {
            //付款方信息
            bisExc.setPayerAcc(StringEscapeUtils.unescapeHtml(payerAcc.getBankAcc()));
            bisExc.setPayerAccName(StringEscapeUtils.unescapeHtml(payerAcc.getAccName()));
            bisExc.setPayerBank(StringEscapeUtils.unescapeHtml(payerAcc.getBankName()));
            bisExc.setBifCode(payerAcc.getBifCode());
        }
        bisExc.setPayerName(StringEscapeUtils.unescapeHtml(fexTranReq.getCorpContact()));
        bisExc.setPayerTel(fexTranReq.getCorpContactTel());
        bisExc.setCurId(fexCashFlow.getCurId());
        //付款账号币种
        bisExc.setPayerCurId(fexCashFlow.getPayerCurId());
        bisExc.setPayMoney(fexCashFlow.getPayMoney());
        //收款方信息
        BtBankAccDTO bankAcc = null;
        if((ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType()))
                && CommonConstant.NO.equals(entity.getCdSign())) {
            bisExc.setPayeeAcc(StringEscapeUtils.unescapeHtml(fexCashFlow.getCorpBankAcc()));
            bisExc.setPayeeAccName(StringEscapeUtils.unescapeHtml(fexCashFlow.getCorpAccName()));
            bankAcc = btBankAccService.getBankAccInfo(fexCashFlow.getCorpBankAcc());
            if (bankAcc != null) {
                bisExc.setPayeeBank(StringEscapeUtils.unescapeHtml(bankAcc.getBankName()));
                bisExc.setPayeeSwiftCode(bankAcc.getSwiftCode());
                bisExc.setPayeeBankCode(bankAcc.getBankCode());
            }
            //获取收款方的省市地区
            bisExc.setPayeeProv(fexTranReq.getCotpProvName());
            //设置地点
            if ("中国".equals(fexTranReq.getCotpCountryName())) {
                bisExc.setPayeeCity(fexTranReq.getCotpCityName());
            } else {
                bisExc.setPayeeCity(fexTranReq.getCotpAddressName());
            }
            bisExc.setBankTypeId(payerAcc.getBankTypeId());
        }else{
            if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
                //外汇买卖交易付款流水生成指令
                //收款方信息（买入信息-收款方账号）
                bisExc.setPayeeAcc(StringEscapeUtils.unescapeHtml(fexTranReq.getCorpBankAcc()));
                bisExc.setPayeeAccName(StringEscapeUtils.unescapeHtml(fexTranReq.getCorpAccName()));
                bankAcc = btBankAccService.getBankAccInfo(fexTranReq.getCorpBankAcc());
                if (bankAcc != null) {
                    bisExc.setPayeeBank(StringEscapeUtils.unescapeHtml(bankAcc.getBankName()));
                    bisExc.setPayeeSwiftCode(StringEscapeUtils.unescapeHtml(bankAcc.getSwiftCode()));
                    bisExc.setPayeeBankCode(StringEscapeUtils.unescapeHtml(bankAcc.getBankCode()));
                }
            } else {
                bisExc.setPayeeAcc(StringEscapeUtils.unescapeHtml(fexCashFlow.getCotpBankAcc()));
                bisExc.setPayeeAccName(StringEscapeUtils.unescapeHtml(fexCashFlow.getCotpAccName()));
                if (ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())) {
                    bankAcc = btBankAccService.getBankAccInfo(fexCashFlow.getCotpBankAcc());
                    if (bankAcc != null) {
                        bisExc.setPayeeBank(StringEscapeUtils.unescapeHtml(bankAcc.getBankName()));
                        bisExc.setPayeeSwiftCode(bankAcc.getSwiftCode());
                        bisExc.setPayeeBankCode(bankAcc.getBankCode());
                    }
                } else {
                    String cotpBankName = StringUtils.isNotEmpty(fexCashFlow.getCotpBankName()) ? fexCashFlow.getCotpBankName() : fexCashFlow.getCotpBank();
                    bisExc.setPayeeBank(StringEscapeUtils.unescapeHtml(cotpBankName));
                    bisExc.setPayeeSwiftCode(fexTranReq.getCotpSwiftCode());

                    FexCotpBankAccVO cotpBankAcc = getFexCotpBankAcc(fexCashFlow);
                    if (cotpBankAcc != null) {
                        bisExc.setPayeeBank(StringEscapeUtils.unescapeHtml(cotpBankAcc.getBankName()));
                        bisExc.setPayeeSwiftCode(cotpBankAcc.getBicCode());
                        bisExc.setPayeeBankCode(cotpBankAcc.getLocalClearNo());
                    }
                }
            }
            setPayeeExternalCorpInfo(bisExc, fexCashFlow, fexTranReq);
        }
        //调拨交易存入收款方账号的币种
        bisExc.setPayeeCurId(fexCashFlow.getPayeeCurId());
        bisExc.setPayeeCountryCode(fexTranReq.getCountryId());
        if(StringUtils.isEmpty(bisExc.getPayeeCountryCode())){
            bisExc.setPayeeCountryCode(fexTranReq.getCotpCountry());
        }
        bisExc.setPayeeAgentBank(StringEscapeUtils.unescapeHtml(fexTranReq.getCotpAgentBank()));
        bisExc.setPayeeAgentBankAdd(StringEscapeUtils.unescapeHtml(fexTranReq.getCotpAgentBankAddr()));
        bisExc.setPayeeAgentAcc(StringEscapeUtils.unescapeHtml(fexTranReq.getAgentBankAcc()));
        //交易对手银行地址
        bisExc.setPayeeBankAdd(fexTranReq.getCotpBankAdrr());
        //其他信息
        bisExc.setQuickSign(fexTranReq.getIsQuick());
        bisExc.setRemitNature(fexTranReq.getRemitNature());
        bisExc.setRemitMethod(fexTranReq.getRemitMethod());
        bisExc.setCostSign(fexTranReq.getCostSign());
        bisExc.setPayType(fexTranReq.getPayType());
        bisExc.setShipDate(fexTranReq.getShipDate());
        bisExc.setIsBonded(fexTranReq.getIsBonded());
        bisExc.setBusNo(fexTranReq.getBusNo());
        bisExc.setContractNo(fexTranReq.getContractNo());
        bisExc.setInvoiceNo(fexTranReq.getInvoiceNo());
        bisExc.setAddWord(StringEscapeUtils.unescapeHtml(fexTranReq.getAddWord()));
        //资金用途
        if(StringUtils.isNotBlank(fexTranReq.getCapitalUse())) {
            bisExc.setRmk(StringEscapeUtils.unescapeHtml(fexTranReq.getCapitalUse()));
        } else {
            bisExc.setRmk(StringEscapeUtils.unescapeHtml(fexTranReq.getRmk()));
        }
        bisExc.setRmkLocal(StringEscapeUtils.unescapeHtml(fexTranReq.getRmkLocal()));
        bisExc.setTranCode1(fexTranReq.getTranCode1());
        bisExc.setTranAmt1(fexTranReq.getTranAmt1());
        bisExc.setTranCurId1(fexTranReq.getTranCurId1());
        bisExc.setTranAddWord1(fexTranReq.getTranAddWord1());
        bisExc.setTranCode2(fexTranReq.getTranCode2());
        bisExc.setTranAmt2(fexTranReq.getTranAmt2());
        bisExc.setTranCurId2(fexTranReq.getTranCurId2());
        bisExc.setTranAddWord2(fexTranReq.getTranAddWord2());

        //新增信息
        /**费用交付方式  1-现金 2-支票 3-账户 默认1*/
        bisExc.setCostMode(ForeignConstant.COST_MODE_1);
        /**扣费账号 默认汇款账号*/
        bisExc.setDedAcc(fexTranReq.getCostAcc());
        /**钞汇代码 1-钞 2-汇 默认2*/
        if (StringUtils.isNotBlank(fexTranReq.getBankNote())) {
            bisExc.setBankCurCode(fexTranReq.getBankNote());
        } else {
            bisExc.setBankCurCode(ForeignConstant.BANK_CUR_CODE_2);
        }
        /**起息日期 默认汇款日期*/
        bisExc.setInterestDate(fexCashFlow.getSettleDate());

        if(ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())){
            CorpDTO corpPayer = corpService.get(payerAcc.getCorpId());
            if (corpPayer != null) {
                // 取账号的英文名称
                bisExc.setPayerNameEn(StringEscapeUtils.unescapeHtml(payerAcc.getAccNameEn()));
                bisExc.setPayerAddrEn(StringEscapeUtils.unescapeHtml(corpPayer.getAddressEn()));
                bisExc.setPayerAddr(StringEscapeUtils.unescapeHtml(corpPayer.getAddress()));
                if ("中国".equals(corpPayer.getCountryName())) {
                    //付款方省份名称
                    bisExc.setPayerProvName(StringEscapeUtils.unescapeHtml(corpPayer.getProvinceName()));
                    //付款方城镇名称
                    bisExc.setPayerCityName(StringEscapeUtils.unescapeHtml(corpPayer.getCityName()));
                } else {
                    //付款方省份名称
                    bisExc.setPayerProvName(StringEscapeUtils.unescapeHtml(corpPayer.getState()));
                    //付款方城镇名称
                    bisExc.setPayerCityName(StringEscapeUtils.unescapeHtml(corpPayer.getCountryAddrName()));
                }
                //付款方邮政编号
                bisExc.setPayerPostCode(StringEscapeUtils.unescapeHtml(corpPayer.getPostCode()));
                //付款方街道名称
                bisExc.setPayerStreetName(StringEscapeUtils.unescapeHtml(corpPayer.getAddress1()));
                //付款方建筑编号
                bisExc.setPayerBuildingNumber(StringEscapeUtils.unescapeHtml(corpPayer.getAddress2()));
            }
            CorpDTO corpPayee = corpService.get(bankAcc.getCorpId());
            if (corpPayee != null) {
                // 取账号的英文名称
                bisExc.setPayeeNameEn(StringEscapeUtils.unescapeHtml(bankAcc.getAccNameEn()));
                bisExc.setPayeeAddrEn(StringEscapeUtils.unescapeHtml(corpPayee.getAddressEn()));
                bisExc.setPayeeBankCode(bankAcc.getBankCode());
                if ("中国".equals(corpPayer.getCountryName())) {
                    //收款方省份名称
                    bisExc.setPayeeProvName(StringEscapeUtils.unescapeHtml(corpPayee.getProvinceName()));
                    //收款方城镇名称
                    bisExc.setPayeeCityName(StringEscapeUtils.unescapeHtml(corpPayee.getCityName()));
                } else {
                    //付款方省份名称
                    bisExc.setPayeeProvName(StringEscapeUtils.unescapeHtml(corpPayee.getState()));
                    //付款方城镇名称
                    bisExc.setPayeeCityName(StringEscapeUtils.unescapeHtml(corpPayee.getCountryAddrName()));
                }
                //收款方邮政编号
                bisExc.setPayeePostCode(StringEscapeUtils.unescapeHtml(corpPayee.getPostCode()));
                //收款方街道名称
                bisExc.setPayeeStreetName(StringEscapeUtils.unescapeHtml(corpPayee.getAddress1()));
                //收款方建筑编号
                bisExc.setPayeeBuildingNumber(StringEscapeUtils.unescapeHtml(corpPayee.getAddress2()));
            }
        }else{
            //付款单位
            CorpDTO corp = corpService.get(fexCashFlow.getCorpId());
            if (corp != null) {
                // 取账号的英文名称
                bisExc.setPayerNameEn(StringEscapeUtils.unescapeHtml(payerAcc.getAccNameEn()));
                bisExc.setPayerAddrEn(StringEscapeUtils.unescapeHtml(corp.getAddressEn()));
                bisExc.setPayerAddr(StringEscapeUtils.unescapeHtml(corp.getAddress()));
                if ("中国".equals(corp.getCountryName())) {
                    //付款方省份名称
                    bisExc.setPayerProvName(StringEscapeUtils.unescapeHtml(corp.getProvinceName()));
                    //付款方城镇名称
                    bisExc.setPayerCityName(StringEscapeUtils.unescapeHtml(corp.getCityName()));
                } else {
                    //付款方省份名称
                    bisExc.setPayerProvName(StringEscapeUtils.unescapeHtml(corp.getState()));
                    //付款方城镇名称
                    bisExc.setPayerCityName(StringEscapeUtils.unescapeHtml(corp.getCountryAddrName()));
                }
                //付款方邮政编号
                bisExc.setPayerPostCode(StringEscapeUtils.unescapeHtml(corp.getPostCode()));
                //付款方街道名称
                bisExc.setPayerStreetName(StringEscapeUtils.unescapeHtml(corp.getAddress1()));
                //付款方建筑编号
                bisExc.setPayerBuildingNumber(StringEscapeUtils.unescapeHtml(corp.getAddress2()));
            }
            setPayeeExternalCorpInfo(bisExc, fexCashFlow, fexTranReq);
        }
        //越南语转换
        bisExc.setPayeeAddrEn(this.formatVietnamese(bisExc.getPayeeAddrEn()));
        if(payerAcc!=null) {
            bisExc.setBankOrgCode(payerAcc.getBankOrgCode());
        } else if (StringUtils.isNotBlank(fexTranReq.getBranchCode())){
            bisExc.setBankOrgCode(fexTranReq.getBranchCode());
        }
        //中国银行取统一社会信用代码
        BisBifInitDTO bifInitDTO = bisBifInitService.getObjectById(bisExc.getBifCode());
        if (bifInitDTO != null && "中国银行".equals(bifInitDTO.getName())) {
            CorpDTO corp = corpService.get(fexTranReq.getCorpId());
            bisExc.setBankOrgCode(corp!=null?corp.getSocCode():"");
        }
        // 外汇买卖交易暂时不设置收款方英文名称
        if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
            bisExc.setPayeeNameEn(null);
        }
        //进口日期
        bisExc.setArrivalDate(fexTranReq.getArrivalDate());
        //报关单号
        bisExc.setDeclareNo(fexTranReq.getDeclareNo());
        //资金性质
        bisExc.setAccNature(fexTranReq.getAccNature());
        //合同利率
        bisExc.setContractRate(fexTranReq.getContractRate());
        //中国农业银行特殊字段：账户类别（汇款账号）、收款人类型、汇款去向、汇款用途
        bisExc.setAccType(fexTranReq.getAccType());
        bisExc.setPayeeType(fexTranReq.getPayeeType());
        bisExc.setRemitRoute(fexTranReq.getRemitRoute());
        if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
            //结汇资金用途
            bisExc.setPurpose(fexTranReq.getPurpose());
            //汇款用途 对应 结汇资金详细用途
            bisExc.setRemitUse(fexTranReq.getPurposeDtl());
            bisExc.setFexProp(fexTranReq.getFexProp());
        } else {
            bisExc.setRemitUse(fexTranReq.getRemitUse());
        }
        //费用币种
        bisExc.setRemChgTolAmtCur(fexTranReq.getCostCurId());
        //费用总额
        bisExc.setRemChgTolAmt(fexTranReq.getCostMoney());
        //本地清算号
        bisExc.setLocalClearNo(fexTranReq.getLocalClearNo());
        //付款清算号
        bisExc.setPayerLocalClearNo(payerAcc.getLocalClearNo());
        //支付类型
        bisExc.setBankPayType(fexTranReq.getBankPayType());
        //受益人关联关系
        bisExc.setBnfRelate(fexTranReq.getBnfRelate());
        //监管类型
        bisExc.setRegulatoryType(fexTranReq.getRegulatoryType());
        //保存附件名称
        if (StringUtils.isNotBlank(fexTranReq.getAttachmentId())) {
            List<AttachmentDTO> list = attachmentService.findAttachmentByGroupId(fexTranReq.getAttachmentId());
            if (EmptyUtils.isNotEmpty(list)){
                String fileName = "";
                for (AttachmentDTO dto:list) {
                    if (StringUtils.isBlank(fileName)) {
                        fileName = dto.getFilePath();
                    } else {
                        fileName = fileName + CommonConstant.COMMA_SPLIT + dto.getFilePath();
                    }
                }
                bisExc.setFileName(fileName);
            }
        }
        //起运国
        if (StringUtils.isNotBlank(fexTranReq.getOriginCountry())) {
            SysCountryCustDTO custDTO = sysCountryCustService.getById(fexTranReq.getOriginCountry());
            if (custDTO != null) {
                bisExc.setOriginCountry(custDTO.getEnCode());
            }
        }
        //买入币种和卖出币种
        bisExc.setBuyCurId(fexTranReq.getBuyCurId());
        bisExc.setSellCurId(fexTranReq.getSellCurId());
        //外汇合同号
        bisExc.setForeignContractNo(fexTranReq.getForeignContractNo());
        //外汇交易类型 0-即期、1-远期
        bisExc.setFexTranType(fexTranReq.getFexTranType());
        //交割类型 0-本金交割、1-无本金交割
        bisExc.setDeliveryType(fexTranReq.getDeliveryType());
        //交割期限
        bisExc.setDeliveryDeadline(fexTranReq.getDeliveryDeadline());
        //费用支出账号币种
        bisExc.setDedAccCurId(fexTranReq.getCostCurId());
        //交易汇率类型
        bisExc.setTranRateType(fexTranReq.getTranRateType());
        //招商云
        bisExc.setGoodsAmount(fexTranReq.getGoodsAmount());
        bisExc.setAdvanceChargeAmount(fexTranReq.getAdvanceChargeAmount());
        bisExc.setAdvanceContractRate(fexTranReq.getAdvanceContractRate());
        bisExc.setRptGateDate(fexTranReq.getRptGateDate());
        if (StringUtils.isNotBlank(fexTranReq.getRptGateEntId())) {
            CorpDTO rptGateEnt = corpService.get(fexTranReq.getRptGateEntId());
            if (rptGateEnt != null) {
                bisExc.setRptGateEntName(rptGateEnt.getName());
                if (StringUtils.isNotBlank(rptGateEnt.getSocCode()) && rptGateEnt.getSocCode().length()>=17) {
                    //报关经营单位名称的组织机构代码：取报关经营单位名称的统一社会信用代码的第9位至第16位+“-”+第17位
                    bisExc.setRptGateEntNo(rptGateEnt.getSocCode().substring(8,16) + '-' + rptGateEnt.getSocCode().substring(16,17));
                }
            }
        }
        bisExc.setRptGateType(fexTranReq.getRptGateType());
        bisExc.setGeneralTradeAmount(fexTranReq.getGeneralTradeAmount());
        bisExc.setFeedAmount(fexTranReq.getFeedAmount());
        bisExc.setOtherTradeAmount(fexTranReq.getOtherTradeAmount());
        bisExc.setBorderTradeAmount(fexTranReq.getBorderTradeAmount());
        bisExc.setCustomAmount(fexTranReq.getCustomAmount());
        bisExc.setOffshoreAmount(fexTranReq.getOffshoreAmount());
        bisExc.setOtherAmount(fexTranReq.getOtherAmount());
        bisExc.setInvestAmt(fexTranReq.getInvestAmt());
        bisExc.setInvestNo(fexTranReq.getInvestNo());
        bisExc.setInvestApplyNbr(fexTranReq.getInvestApplyNbr());
        bisExc.setOftenNo(fexTranReq.getOftenNo());
        bisExc.setDirectInvestmentCertNo(fexTranReq.getDirectInvestmentCertNo());
        if(null != fexTranReq.getAdvanceRate()){
            BigDecimal advanceRate = fexTranReq.getAdvanceRate().multiply(new BigDecimal(100));
            bisExc.setAdvanceRate(advanceRate.intValue());
        }
        bisExc.setCheckDay(fexTranReq.getCheckDay());
        // 判断指令发送模式
        String bisExcAutoAccount = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_BISEXC_AUTOACCOUNT).getValue();
        if (CommonConstant.YES.equalsIgnoreCase(bisExcAutoAccount)) {
            bisExc.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
            this.bisForeignExcService.autoSendBisForeignExc(bisExc, user);
        } else {
            bisExc.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
            this.bisForeignExcService.addBisForeignExc(bisExc, user);
        }

        return Boolean.TRUE;
    }

    /**
     * 根据交易获取最早的一笔现金流
     * @param tranBillId
     * @param cdSign
     * @return
     */
    public FexCashFlow getFirstCashFlowByTranReqId(String tranBillId, String cdSign){
        List<FexCashFlowVO> list = fexCashFlowDao.listByTranBillId(tranBillId, cdSign);
        FexCashFlow entity = null;
        if(!EmptyUtils.isEmpty(list)){
            FexCashFlowVO fexCashFlowVO = list.get(0);
            entity = new FexCashFlow();
            BeanUtils.copyNotNullProperties(fexCashFlowVO, entity);
        }
        return entity;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的交易需求单据，不允许禁用！
            int num = this.fexCashFlowDao.getCashFlowCountByCorpId(corpId);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_CASHFLOW,
                        multiLangService.getLang(CorpVerifyMessageConstant.FEX_VALID_DISABLE_CASHFLOW_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }

    public void setValues(DataTablesOutput dataTablesOutput){
        if(dataTablesOutput == null){
            return;
        }
        injectListValues(dataTablesOutput.getData());
    }

    /**
     * 越南本地语言转换
     * @param sourceCode
     * @return
     */
    public String formatVietnamese(String sourceCode) {
        // 测试
        //sourceCode = "G3, Vinhomes Greenbay, Nam Từ Liêm, Hà Nội";
        if (StringUtils.isNotBlank(sourceCode)) {
            sourceCode = sourceCode.replace("ế","e");
            sourceCode = sourceCode.replace("ò","o");
            sourceCode = sourceCode.replace("á","a");
            sourceCode = sourceCode.replace("ố","o");
            sourceCode = sourceCode.replace("à","a");
            sourceCode = sourceCode.replace("ả","a");
            sourceCode = sourceCode.replace("ì","i");
            sourceCode = sourceCode.replace("ă","a");
            sourceCode = sourceCode.replace("ắ","a");
            sourceCode = sourceCode.replace("ớ","o");
            sourceCode = sourceCode.replace("ờ","o");
            sourceCode = sourceCode.replace("ợ","o");
            sourceCode = sourceCode.replace("ù","u");
            sourceCode = sourceCode.replace("ổ","o");
            sourceCode = sourceCode.replace("ô","o");
            sourceCode = sourceCode.replace("ể","e");
            sourceCode = sourceCode.replace("ấ","a");
            sourceCode = sourceCode.replace("ú","u");
            sourceCode = sourceCode.replace("ạ","a");
            sourceCode = sourceCode.replace("ộ","o");
            sourceCode = sourceCode.replace("â","a");
            sourceCode = sourceCode.replace("ử","u");
            sourceCode = sourceCode.replace("ẩ","a");
            sourceCode = sourceCode.replace("ị","i");
            sourceCode = sourceCode.replace("ậ","a");
            sourceCode = sourceCode.replace("ọ","o");
            sourceCode = sourceCode.replace("ẹ","e");
            sourceCode = sourceCode.replace("ầ","a");
            sourceCode = sourceCode.replace("ề","e");
            sourceCode = sourceCode.replace("ụ","u");
            sourceCode = sourceCode.replace("ừ","u");
            sourceCode = sourceCode.replace("ặ","a");
            sourceCode = sourceCode.replace("Đ","D");
            sourceCode = sourceCode.replace("ặ","a");
            sourceCode = sourceCode.replace("ụ","u");
            sourceCode = sourceCode.replace("ồ","o");
            sourceCode = sourceCode.replace("í","i");
            sourceCode = sourceCode.replace("ự","u");
            sourceCode = sourceCode.replace("ữ","u");
            sourceCode = sourceCode.replace("ỏ","o");
            sourceCode = sourceCode.replace("è","e");
            sourceCode = sourceCode.replace("é","e");
            sourceCode = sourceCode.replace("ỗ","o");
            sourceCode = sourceCode.replace("ê","e");
            sourceCode = sourceCode.replace("ở","o");
            sourceCode = sourceCode.replace("ỗ","o");
            sourceCode = sourceCode.replace("ó","o");
            sourceCode = sourceCode.replace("ệ","e");
            sourceCode = sourceCode.replace("ỉ","i");
            sourceCode = sourceCode.replace("ủ","u");
            sourceCode = sourceCode.replace("ỉ","i");
            sourceCode = sourceCode.replace("ỡ","o");
            sourceCode = sourceCode.replace("ơ","o");
            sourceCode = sourceCode.replace("ứ","u");
        }
        return sourceCode;
    }

    /**
     * 根据银行流水获取现金流
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForRefundCheck(DataTablesInput dataTablesInput) {
        StringBuilder joinSql = new StringBuilder();
        appendExternalCorpNameFilters(dataTablesInput, joinSql);
        DataTablesOutput output = fexCashFlowDao.getDataTablesUnionForRefundCheck(dataTablesInput, joinSql.toString());
        if (output != null) {
            injectListValues(output.getData());
        }
        return output;
    }

    /**
     * 更新银行流水号
     * @param id
     * @param bankSerialNo
     * @return
     */
    public int updateBankSerialNo(String id, String bankSerialNo, String payStatus){
        return fexCashFlowDao.updateBankSerialNo(id, bankSerialNo, payStatus);
    }

    /**
     * 根据银行流水号获取现金流
     * @param bankSerialNo
     * @return
     */
    public FexCashFlow getByBankSerialNo(String bankSerialNo) {
        return fexCashFlowDao.getUniqueBy("bankSerialNo", bankSerialNo);
    }

    /**
     * 根据关联现金流号码获取现金流
     * @param relBillNo
     * @return
     */
    public FexCashFlow getByRelBillNo(String relBillNo) {
        return fexCashFlowDao.getUniqueBy("relBillNo", relBillNo);
    }

    /**
     * 根据交易ID获取收款现金流
     * @param tranBillId
     * @return
     */
    public FexCashFlow getReceiptCashFlowByTranBillId(String tranBillId) {
        List<FexCashFlow> receiptCashFlow = this.fexCashFlowDao.listBy("tranBillId", tranBillId);
        FexCashFlow result = null;
        if (EmptyUtils.isNotEmpty(receiptCashFlow)) {
            for (FexCashFlow cashFlow:receiptCashFlow) {
                if (CommonConstant.NO.equals(cashFlow)
                        && !CommonConstant.STATUE_DELETE.equals(cashFlow.getStatus())) {
                    result = cashFlow;
                }
            }
        }
        return result;
    }

    /**
     * 带原状态更新现金流
     *
     * @param vo        现金流
     * @param oldStatus 原状态控制
     * @return
     */
    public void updateFexCashFlowStatus(FexCashFlowVO vo, Integer[] oldStatus) {
        int result = this.fexCashFlowDao.updateFexCashFlowStatus(vo, oldStatus);
        if (result < 1) {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getBillNo());
        }
    }

    /**
     *
     * 设置自定义指令数据
     *
      * @param bisExc
     * @param reqId
     */
    private void setCustExc(BisForeignExcDTO bisExc, String reqId){
        FexTranReq fexTranReq = fexTranReqService.get(reqId);
        List<FexTranCustInfoVO> fexTranCustInfoVOS = fexTranCustInfoService.listCustInfoByBusId(reqId);
        if (CollectionUtil.isNotEmpty(fexTranCustInfoVOS)
                && StringUtils.isNotEmpty(fexTranReq.getTemplateId())
                && StringUtils.isNotEmpty(fexTranReq.getVersionNo())){

            List<FexTranReqConfigVo> templateConfig = fexTranReqConfigService.getTemplateConfig(fexTranReq.getTemplateId(), fexTranReq.getVersionNo());
            Map<String, FexTranReqConfigVo> templateConfigMap = templateConfig.stream()
                    .collect(Collectors.toMap(
                            FexTranReqConfigVo::getFieldCode,
                            vo -> vo,
                            (oldVal, newVal) -> oldVal // 冲突保留旧值
                    ));
            // 扩展指令数据
            Map<String, Object> bankExtraMap = new HashMap<>();
            // 自定义数据
            Map<String, String> systemCustMap = new HashMap<>();
            for (FexTranCustInfoVO fexTranCustInfoVO : fexTranCustInfoVOS){
                FexTranReqConfigVo fexTranReqConfigVo = templateConfigMap.get(fexTranCustInfoVO.getFieldCode());
                if(null == fexTranReqConfigVo){
                    continue;
                }
                Integer isExc = fexTranReqConfigVo.getIsExc();
                if(1==isExc && StringUtils.isNotEmpty(fexTranCustInfoVO.getLabelBank())){
                    systemCustMap.put(fexTranCustInfoVO.getLabelBank(), fexTranCustInfoVO.getFieldVal());
                }
            }
            if (CollectionUtil.isNotEmpty(systemCustMap)){
                bankExtraMap.put("systemCust", systemCustMap);
                bisExc.setBankExtra(JSONUtil.toJsonStr(bankExtraMap));
            }
        }
    }


    /**
     * 更新付款指令状态
     */
    public int updateVoucherState(BisExcChangeMsg bisExcChangeMsg) {
        int rows = fexCashFlowDao.updateVoucherState(bisExcChangeMsg.getCbsBillId(), bisExcChangeMsg.getVoucherStat());
        if (BdlinkConstant.SUCCESS.equals(bisExcChangeMsg.getVoucherStat())) {
            FexCashFlow cashFlow = this.get(bisExcChangeMsg.getCbsBillId());
            if (isEnterpriseLimitBusiness(cashFlow)) {
                String sourceBizId = resolveEnterpriseLimitSourceBizId(cashFlow);
                if (StringUtils.isBlank(sourceBizId)) {
                    throw new BusinessException(EnterpriseLimitMessageEnum.LIMIT_CONTROL_SOURCE_IDENTITY_INCOMPLETE);
                }
                // 仅资金收付付款指令成功后将在途占用转为成功用量。
                EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.markSuccess(UserContextHelper.helper().getTenantId(),
                        EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW, sourceBizId));
            }
        }
        return rows;
    }

    /**
     * 将交易对手名称筛选转换为客商ID筛选，避免 SQL 直接查询 fex_cotp。
     */
    private void appendExternalCorpNameFilters(DataTablesInput dataTablesInput, StringBuilder sql) {
        if (dataTablesInput == null || EmptyUtils.isEmpty(dataTablesInput.getFormdata()) || sql == null) {
            return;
        }
        appendExternalCorpNameFilter(dataTablesInput.getFormdata(), sql, "likes_cotpName");
        appendExternalCorpNameFilter(dataTablesInput.getFormdata(), sql, "eqs_cotpName");
    }

    /**
     * 处理单个交易对手名称筛选字段。
     */
    private void appendExternalCorpNameFilter(Map<String, Object> params, StringBuilder sql, String field) {
        List<String> externalCorpIds = resolveExternalCorpIdsByName(params, field);
        if (externalCorpIds == null) {
            return;
        }
        if (EmptyUtils.isEmpty(externalCorpIds)) {
            sql.append(" and 1=0");
            return;
        }
        sql.append(" and ").append(SqlUtil.getInSql(externalCorpIds, "t.cotp_Id", true));
    }

    /**
     * 通过客商名称查询客商ID，并从动态查询参数中移除原名称条件。
     */
    private List<String> resolveExternalCorpIdsByName(Map<String, Object> params, String field) {
        if (params == null || StringUtils.isBlank(field)) {
            return null;
        }
        Object name = params.remove(field);
        if (name == null || StringUtils.isBlank(name.toString())) {
            return null;
        }
        if (externalCorpService == null) {
            return new ArrayList<>();
        }
        List<ExternalCorpDTO> externalCorps = externalCorpService.findExternalCorpLikeName(name.toString());
        List<String> externalCorpIds = new ArrayList<>();
        if (EmptyUtils.isEmpty(externalCorps)) {
            return externalCorpIds;
        }
        for (ExternalCorpDTO externalCorp : externalCorps) {
            if (externalCorp != null && StringUtils.isNotBlank(externalCorp.getId())
                    && !externalCorpIds.contains(externalCorp.getId())) {
                externalCorpIds.add(externalCorp.getId());
            }
        }
        return externalCorpIds;
    }

    /**
     * 批量查询客商资料，批量接口未返回的历史客商按ID回退查询。
     */
    private Map<String, ExternalCorpDTO> getExternalCorpMap(List<String> externalCorpIds) {
        Map<String, ExternalCorpDTO> externalCorpMap = new HashMap<>();
        if (EmptyUtils.isEmpty(externalCorpIds) || externalCorpService == null) {
            return externalCorpMap;
        }
        Map<String, ExternalCorpDTO> batchMap = externalCorpService.queryFexExternalCorpMapByIds(externalCorpIds);
        if (!EmptyUtils.isEmpty(batchMap)) {
            externalCorpMap.putAll(batchMap);
        }
        for (String externalCorpId : externalCorpIds) {
            if (externalCorpMap.containsKey(externalCorpId)) {
                continue;
            }
            ExternalCorpDTO externalCorp = externalCorpService.getById(externalCorpId);
            if (externalCorp != null && StringUtils.isNotBlank(externalCorp.getId())) {
                externalCorpMap.put(externalCorp.getId(), externalCorp);
            }
        }
        return externalCorpMap;
    }

    /**
     * 批量查询客商账户资料。
     */
    private Map<String, ExternalCorpBankAccDTO> getExternalCorpBankAccMap(List<String> bankAccIds) {
        Map<String, ExternalCorpBankAccDTO> bankAccMap = new HashMap<>();
        if (EmptyUtils.isEmpty(bankAccIds) || externalCorpService == null) {
            return bankAccMap;
        }
        Map<String, ExternalCorpBankAccDTO> batchMap = externalCorpService.queryFexExternalCorpBankAccMapByIds(bankAccIds);
        if (!EmptyUtils.isEmpty(batchMap)) {
            bankAccMap.putAll(batchMap);
        }
        return bankAccMap;
    }

    /**
     * 按客商账号查询账户资料，兜底处理账户ID为空的历史数据。
     */
    private Map<String, ExternalCorpBankAccDTO> getExternalCorpBankAccMapByBankAcc(List<String> bankAccs) {
        Map<String, ExternalCorpBankAccDTO> bankAccMap = new HashMap<>();
        if (EmptyUtils.isEmpty(bankAccs) || externalCorpService == null) {
            return bankAccMap;
        }
        for (String bankAcc : bankAccs) {
            ExternalCorpBankAccDTO bankAccDTO = externalCorpService.queryByBankAcc(bankAcc);
            if (bankAccDTO == null || StringUtils.isBlank(bankAccDTO.getExternalAcc())) {
                continue;
            }
            bankAccMap.put(bankAcc, bankAccDTO);
            bankAccMap.put(bankAccDTO.getExternalAcc(), bankAccDTO);
        }
        return bankAccMap;
    }

    /**
     * 从客商缓存中获取当前现金流交易对手。
     */
    private ExternalCorpDTO getExternalCorp(Map<String, ExternalCorpDTO> externalCorpMap, String externalCorpId) {
        if (externalCorpMap == null || externalCorpMap.isEmpty() || StringUtils.isBlank(externalCorpId)) {
            return null;
        }
        return externalCorpMap.get(externalCorpId);
    }

    /**
     * 从客商账户缓存中按账户ID获取账户。
     */
    private ExternalCorpBankAccDTO getExternalCorpBankAcc(Map<String, ExternalCorpBankAccDTO> bankAccMap, String bankAccId) {
        if (bankAccMap == null || bankAccMap.isEmpty() || StringUtils.isBlank(bankAccId)) {
            return null;
        }
        return bankAccMap.get(bankAccId);
    }

    /**
     * 从客商账户缓存中按账号获取账户。
     */
    private ExternalCorpBankAccDTO getExternalCorpBankAccByBankAcc(Map<String, ExternalCorpBankAccDTO> bankAccMap, String bankAcc) {
        if (bankAccMap == null || bankAccMap.isEmpty() || StringUtils.isBlank(bankAcc)) {
            return null;
        }
        return bankAccMap.get(bankAcc);
    }

    /**
     * 获取现金流交易对手客商ID，兼容历史oppId字段。
     */
    private String getExternalCorpId(FexCashFlowVO vo) {
        if (vo == null) {
            return null;
        }
        if (StringUtils.isNotBlank(vo.getOppId())) {
            return vo.getOppId();
        }
        return vo.getCotpId();
    }

    /**
     * 回填现金流交易对手客商展示字段。
     */
    private void fillExternalCorpInfo(FexCashFlowVO vo, ExternalCorpDTO externalCorp) {
        if (vo == null || externalCorp == null) {
            return;
        }
        vo.setOppId(externalCorp.getId());
        vo.setCotpName(externalCorp.getName());
        if (!ForeignConstant.TRAN_TYPE_4.equals(vo.getTranType()) && StringUtils.isNotBlank(externalCorp.getName())) {
            vo.setCotpBankName(externalCorp.getName());
        }
    }

    /**
     * 回填现金流交易对手客商账户展示字段。
     */
    private void fillExternalCorpBankAccInfo(FexCashFlowVO vo, ExternalCorpBankAccDTO bankAcc) {
        if (vo == null || bankAcc == null) {
            return;
        }
        vo.setCotpAccId(bankAcc.getId());
        if (StringUtils.isBlank(vo.getOppId())) {
            vo.setOppId(bankAcc.getExternalCorpId());
        }
        if (StringUtils.isNotBlank(bankAcc.getExternalAcc())) {
            vo.setCotpBankAcc(bankAcc.getExternalAcc());
        }
        if (StringUtils.isNotBlank(bankAcc.getExternalAccName())) {
            vo.setCotpAccName(bankAcc.getExternalAccName());
        }
        if (StringUtils.isNotBlank(bankAcc.getBank())
                && (ForeignConstant.TRAN_TYPE_4.equals(vo.getTranType()) || StringUtils.isBlank(vo.getCotpBankName()))) {
            vo.setCotpBankName(bankAcc.getBank());
        }
    }

    /**
     * 保存资金收付明细后同事务占用银行限额。
     */
    @Override
    public void saveEntity(FexCashFlow entity) {
        super.saveEntity(entity);
        // 明细取得ID后同步限额，按状态和付款适用范围决定核对/补占或释放。
        syncEnterpriseLimit(entity);
    }

    /**
     * 更新资金收付明细后按新旧快照调整银行限额。
     */
    @Override
    public void updateEntity(FexCashFlow entity) {
        super.updateEntity(entity);
        // 金额、账户、币种、结算日期或状态修改后，将最新快照交给限额服务同步占用。
        syncEnterpriseLimit(entity);
    }

    /**
     * 保存或更新资金收付明细后同步银行限额。
     */
    @Override
    public void saveOrUpdate(FexCashFlow entity) {
        super.saveOrUpdate(entity);
        // 覆盖保存或更新的统一入口，避免业务经此入口变更明细后遗漏限额同步。
        syncEnterpriseLimit(entity);
    }

    /**
     * 物理删除资金收付明细前释放银行限额。
     */
    @Override
    public void delete(String id) {
        ICurrentUser user = getCurrentUser();
        FexCashFlow entity = this.get(id, true);
        if (isEnterpriseLimitBusiness(entity)) {
            // 仅删除可能占用过限额的资金收付付款明细时通知内管释放。
            EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.release(resolveEnterpriseLimitTenantId(user),
                    EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW, resolveEnterpriseLimitSourceBizId(entity),
                    user == null ? null : user.getId()));
        }
        super.delete(id);
    }

    /**
     * 批量物理删除资金收付明细前释放银行限额。
     */
    @Override
    public void delete(List<String> ids) {
        ICurrentUser user = getCurrentUser();
        for (String id : ids) {
            FexCashFlow entity = this.get(id, true);
            if (isEnterpriseLimitBusiness(entity)) {
                // 批量删除时仅按可能占用过限额的付款业务逐笔释放。
                EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.release(resolveEnterpriseLimitTenantId(user),
                        EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW, resolveEnterpriseLimitSourceBizId(entity),
                        user == null ? null : user.getId()));
            }
        }
        super.delete(ids);
    }

    /**
     * 银行落地审批同意后，为境外资金收付付款明细补办付款指令。
     *
     * <p>调用链：银行端落地审批回调 A → 来源适配器 →
     * 本方法。银行端回调可能重复到达，因此先检查是否已存在有效 BisForeignExc；
     * 已有有效指令时直接返回，避免重复生成境外付款指令。</p>
     */
    public void continueAfterEnterpriseLimitApproval(String cashFlowId, ICurrentUser user) {
        FexCashFlow entity = this.get(cashFlowId, true);
        // 只有仍处于审批通过状态才能续办；期间被编辑、打回或删除时不允许继续。
        if (entity == null || !CommonConstant.STATUE_CHECK.equals(entity.getStatus())) {
            throw new BusinessException(EnterpriseLimitMessageEnum.LIMIT_CONTROL_FEX_CONTINUE_STATUS_CHANGED);
        }
        BisForeignExcDTO existing = bisForeignExcService.getByCbsBillId(cashFlowId);
        // 已有打回/作废之外的指令视为已续办成功，Dubbo 重试不再生成新指令。
        if (existing != null && StringUtils.isNotBlank(existing.getVoucherNo())
                && !BdlinkConstant.BACK.equals(existing.getVoucherStat())
                && !BdlinkConstant.INVALIDATE.equals(existing.getVoucherStat())) {
            return;
        }
        // 复用企业审批通过后生成境外付款指令的入口，金额、账号和结算条件校验保持一致。
        this.saveBisForeignExc(entity, user);
    }

    /**
     * 银行落地审批拒绝后，将境外资金收付付款明细打回并释放本次限额占用。
     *
     * <p>调用链：银行端落地审批回调 R → 来源适配器 →
     * 本方法。银行拒绝意味着当前付款尝试不能写境外指令，明细应回到可编辑状态，
     * 企业修改后可再次提交并重新走支付/落地限额校验。</p>
     */
    public void rejectAfterEnterpriseLimitApproval(String cashFlowId, ICurrentUser user) {
        FexCashFlow entity = this.get(cashFlowId, true);
        if (entity == null) {
            // 明细已不存在，无需处理。
            return;
        }
        if (CommonConstant.STATUE_BACK.equals(entity.getStatus())) {
            // 重复拒绝/重复回调且明细已经打回时，只确保释放占用后返回。
            EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.release(resolveEnterpriseLimitTenantId(user),
                    EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW,
                    resolveEnterpriseLimitSourceBizId(entity), user.getId()));
            return;
        }
        if (!CommonConstant.STATUE_CHECK.equals(entity.getStatus())) {
            // 审批通过状态以外的明细不接受拒绝结果，避免覆盖用户新状态。
            throw new BusinessException(EnterpriseLimitMessageEnum.LIMIT_CONTROL_FEX_REJECT_STATUS_CHANGED);
        }
        FexCashFlowVO vo = new FexCashFlowVO();
        BeanUtils.copyNotNullProperties(entity, vo);
        vo.setStatus(CommonConstant.STATUE_BACK);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        this.updateFexCashFlowStatus(vo, new Integer[]{CommonConstant.STATUE_CHECK});
        // 释放当前付款尝试占用，后续重新提交会创建新的付款尝试。
        EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.release(resolveEnterpriseLimitTenantId(user),
                EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW,
                resolveEnterpriseLimitSourceBizId(entity), user.getId()));
        fexMessageService.saveMessage(entity.getId(), FexMessageConstant.MESSAGE_TYPE_CHECK_BACK, "",
                FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW, user);
    }

    /**
     * 根据资金收付付款明细的最新快照占用、调整或释放银行限额。
     */
    private void syncEnterpriseLimit(FexCashFlow entity) {
        // 基础保存/更新入口从当前上下文取得操作人，复用统一的占用与释放判断。
        syncEnterpriseLimit(entity, getCurrentUser());
    }

    /**
     * 使用明确的业务操作人同步资金收付限额，兼容工作流和批量提交入口。
     */
    private void syncEnterpriseLimit(FexCashFlow entity, ICurrentUser user) {
        if (entity == null || StringUtils.isBlank(entity.getId())) {
            return;
        }
        if (!isEnterpriseLimitBusiness(entity)) {
            // 非资金收付付款（如外汇交易生命周期预测现金流）从未占用限额，不发起无效释放请求。
            return;
        }
        String tenantId = resolveEnterpriseLimitTenantId(user);
        String operatorId = user == null ? null : user.getId();
        boolean applicable = ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())
                && CommonConstant.YES.equals(entity.getCdSign())
                && ForeignConstant.SETTLE_TYPE_1.equals(entity.getSettleType())
                && !isEnterpriseLimitOfflinePayment(entity)
                && entity.getPayMoney() != null && entity.getPayMoney().compareTo(BigDecimal.ZERO) > 0;
        if (isEnterpriseLimitNonOccupyingStatus(entity.getStatus()) || !applicable) {
            // 暂存态的付款明细若仍处于限额适用范围，其占用归属交易单、不得释放：暂存只表示尚未在
            // 结算执行环节发起付款指令，不表示放弃付款，保持提交时已建立的占用即可。
            if (shouldKeepEnterpriseLimitOccupied(entity, applicable)) {
                return;
            }
            // 删除/打回，或已不属于正金额的全额付款业务时，释放该明细原有占用。
            EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.release(tenantId, EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW,
                    resolveEnterpriseLimitSourceBizId(entity), operatorId));
            return;
        }
        if (user == null || StringUtils.isBlank(user.getId())) {
            throw new BusinessException(EnterpriseLimitMessageEnum.LIMIT_CONTROL_FEX_OPERATOR_MISSING);
        }
        PaymentLimitContextDto context = buildEnterpriseLimitContext(entity, user);
        if (context == null) {
            // 付款账户不存在或非在线账户时不适用银行限额，清理该明细原有占用。
            EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.release(tenantId, EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW,
                    resolveEnterpriseLimitSourceBizId(entity), operatorId));
            return;
        }
        // 由内管核对最新明细与已有占用；首次提交或释放后重提无占用时，补做校验和占用。
        EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.verifyOrOccupy(context));
    }

    /**
     * 资金收付主交易提交时立即校验并占用支付限额。
     *
     * <p>主交易审批通过后才生成现金流，因此使用主交易 ID 作为稳定来源 ID；后续现金流同步继续使用
     * {@code tranBillId}，从而核对同一笔占用，避免主交易和现金流重复累计。</p>
     */
    public void occupyEnterpriseLimitForTranSubmit(FexTranReq entity, ICurrentUser user) {
        if (entity == null || StringUtils.isBlank(entity.getId())
                || !ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())) {
            return;
        }
        String tenantId = resolveEnterpriseLimitTenantId(user);
        boolean applicable = CommonConstant.YES.equals(entity.getCdSign())
                && !CommonConstant.YES.equals(entity.getIsPayed())
                && entity.getPayMoney() != null && entity.getPayMoney().compareTo(BigDecimal.ZERO) > 0;
        if (!applicable) {
            // 主交易此次不属于付款限额范围，提交前尚未占用，无需调内管释放。
            return;
        }
        if (user == null || StringUtils.isBlank(user.getId())) {
            throw new BusinessException(EnterpriseLimitMessageEnum.LIMIT_CONTROL_FEX_OPERATOR_MISSING);
        }
        BtBankAccDTO account = btBankAccService.getBankAccById(entity.getCorpAccId());
        if (account == null || !CommonConstant.YES.equals(account.getIsOnline())) {
            // 离线账户的主交易不进入支付限额，不产生无效释放请求。
            return;
        }
        PaymentLimitContextDto context = new PaymentLimitContextDto();
        context.setTenantId(tenantId);
        context.setSourceBizType(EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW);
        // 资金收付限额以交易单作为来源：来源ID传交易单ID，业务编号传交易单bill_no。
        context.setSourceBizId(entity.getId());
        context.setBusinessNo(entity.getBillNo());
        context.setUnitId(entity.getCorpId());
        context.setAccountId(entity.getCorpAccId());
        context.setAccountMask(entity.getCorpBankAcc());
        context.setCurrencyId(entity.getCurId());
        context.setAmount(entity.getPayMoney());
        context.setBusinessDate(entity.getSettleDate());
        context.setBizStatus(String.valueOf(CommonConstant.STATUE_WAIT_CHECK));
        context.setOperatorId(user.getId());
        context.setOperatorName(user.getUsername());
        // 主交易提交属于交互入口，内管返回失败码时转为业务异常，使本地状态更新和审批启动一并回滚。
        EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.verifyOrOccupy(context));
    }

    /** 资金收付主交易撤回或审批打回时释放提交阶段的额度占用。 */
    public void releaseEnterpriseLimitForTranReq(String tranReqId, ICurrentUser user) {
        if (StringUtils.isBlank(tranReqId)) {
            return;
        }
        EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.release(
                resolveEnterpriseLimitTenantId(user), EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW,
                tranReqId, user == null ? null : user.getId()));
    }

    /**
     * 暂存、删除和打回状态不占用企业端银行限额。
     */
    private boolean isEnterpriseLimitNonOccupyingStatus(Integer status) {
        return CommonConstant.STATUE_TEMP.equals(status)
                || CommonConstant.STATUE_DELETE.equals(status)
                || CommonConstant.STATUE_BACK.equals(status);
    }

    private PaymentLimitContextDto buildEnterpriseLimitContext(FexCashFlow entity, ICurrentUser user) {
        if (entity == null || StringUtils.isBlank(entity.getId())
                || !ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())
                || !CommonConstant.YES.equals(entity.getCdSign())
                || !ForeignConstant.SETTLE_TYPE_1.equals(entity.getSettleType())
                || entity.getPayMoney() == null || entity.getPayMoney().compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        FexTranReq tranReq = StringUtils.isBlank(entity.getTranBillId())
                ? null : fexTranReqService.get(entity.getTranBillId());
        if (tranReq == null || StringUtils.isBlank(tranReq.getId()) || StringUtils.isBlank(tranReq.getBillNo())) {
            throw new BusinessException(EnterpriseLimitMessageEnum.LIMIT_CONTROL_SOURCE_IDENTITY_INCOMPLETE);
        }
        if (CommonConstant.YES.equals(tranReq.getIsPayed())) {
            return null;
        }
        BtBankAccDTO account = btBankAccService.getBankAccById(entity.getCorpAccId());
        if (account == null || !CommonConstant.YES.equals(account.getIsOnline())) {
            return null;
        }
        PaymentLimitContextDto context = new PaymentLimitContextDto();
        context.setTenantId(resolveEnterpriseLimitTenantId(user));
        context.setSourceBizType(EnterpriseLimitControlService.BIZ_TYPE_FEX_CASH_FLOW);
        // 资金收付限额始终以交易单作为来源：来源ID传交易单ID，业务编号传交易单bill_no。
        context.setSourceBizId(tranReq.getId());
        context.setBusinessNo(tranReq.getBillNo());
        context.setUnitId(entity.getCorpId());
        context.setAccountId(entity.getCorpAccId());
        context.setAccountMask(entity.getCorpBankAcc());
        // 限额跨服务契约传企业端原币种 ID，管理端按 ID 完成规则匹配和汇率折算。
        context.setCurrencyId(entity.getCurId());
        context.setAmount(entity.getPayMoney());
        context.setBusinessDate(entity.getSettleDate() == null ? entity.getCashFlowDate() : entity.getSettleDate());
        context.setBizStatus(String.valueOf(entity.getStatus()));
        context.setOperatorId(user == null ? null : user.getId());
        context.setOperatorName(user == null ? null : user.getUsername());
        return context;
    }

    /**
     * 判断现金流是否属于支付限额覆盖的资金收付付款业务。
     * 生命周期、买卖、收款和净额现金流不会产生限额占用，因此保存、更新、删除时均不调用释放接口。
     */
    private boolean isEnterpriseLimitBusiness(FexCashFlow entity) {
        return entity != null
                && ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())
                && CommonConstant.YES.equals(entity.getCdSign())
                && ForeignConstant.SETTLE_TYPE_1.equals(entity.getSettleType());
    }

    /** 判断资金收付交易单是否已在线下支付，线下已付不参与银行限额。 */
    private boolean isEnterpriseLimitOfflinePayment(FexCashFlow entity) {
        if (entity == null || StringUtils.isBlank(entity.getTranBillId())) {
            return false;
        }
        FexTranReq tranReq = fexTranReqService.get(entity.getTranBillId());
        return tranReq != null && CommonConstant.YES.equals(tranReq.getIsPayed());
    }

    /** 资金收付限额以主交易为统计主体，付款现金流不得使用自身ID。 */
    String resolveEnterpriseLimitSourceBizId(FexCashFlow entity) {
        if (entity == null) {
            return null;
        }
        return ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())
                ? entity.getTranBillId() : entity.getId();
    }

    /**
     * 优先使用线程/Dubbo租户上下文，兼容后台任务用户对象未携带tenantId的场景。
     */
    private String resolveEnterpriseLimitTenantId(ICurrentUser user) {
        String tenantId = UserContextHelper.helper().getTenantId();
        return StringUtils.isNotBlank(tenantId) || user == null ? tenantId : user.getTenantId();
    }

    /**
     * 非空且未重复的ID加入集合。
     */
    private void addIfNotEmpty(List<String> list, String id) {
        if (StringUtils.isNotBlank(id) && !list.contains(id)) {
            list.add(id);
        }
    }

    /**
     * 判断暂存态的资金收付付款明细是否应保持支付限额占用。
     *
     * <p>资金收付明细在主交易审批通过后才生成，默认配置（isSkipSettle=0）下落库状态为暂存；该状态只表示
     * "尚未在结算执行环节发起付款指令"，不代表放弃付款。限额占用在交易单提交时即已建立、归属交易单本身，
     * 按《银行限额管理_企业端实现文档》§2.4"暂存、待审批、退回、审批完成均继续占用，仅单据删除时释放"的
     * 口径，暂存不得触发释放，否则会出现"提交已占用、审批通过生成暂存明细即释放"的缺陷。</p>
     *
     * <p>保持占用即不做任何改动：占用已由交易单提交建立，暂存不改变付款金额、单位、账户与币种，
     * 因此既不请求释放，也不重复占用。退款勾对生成的结算执行单（billFrom=4）以及删除、打回状态
     * 沿用原有释放语义，不在本判定范围内。</p>
     *
     * @param entity     资金收付明细
     * @param applicable 该明细是否仍处于支付限额适用范围
     * @return true 表示保持占用（跳过释放），false 表示维持原有释放逻辑
     */
    private boolean shouldKeepEnterpriseLimitOccupied(FexCashFlow entity, boolean applicable) {
        return applicable && entity != null
                && CommonConstant.STATUE_TEMP.equals(entity.getStatus())
                && !ForeignConstant.BILL_FROM_4.equals(entity.getBillFrom())
                && StringUtils.isNotBlank(entity.getTranBillId());
    }
}
