package com.bytter.bill.domain.bill.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.api.billPool.dto.AbPoolDtlDTO;
import com.bytter.bill.api.billPool.service.IAccebillPoolService;
import com.bytter.bill.domain.bill.dao.AbReceiveBillDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_RECEIVEBILL)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_RECEIVEBILL)
@Service("abReceiveBillService")
@Transactional
public class AbReceiveBillService extends BaseService<AbReceiveBillVO, String>
        implements IAccebillAccbookService, IWorkFlowListener, IExcelVerifyHandler<AbReceiveBillVO> {
    @Autowired
    private AbReceiveBillDao abReceiveBillDao;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbFbsBudgetProcessService abFbsBudgetProcessService;
    @Autowired
    private AbDiscountService abDiscountService;
    @Autowired
    private AbAccbookService abAccbookService;
    @Autowired
    private AbBtBankAccService abBtBankAccService;
    @Autowired
    private AbBillAccBalService abBillAccBalService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbBillNotificationService abBillNotificationService;
	@Autowired
	private AbDictService abDictService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @Autowired
    private AbBillQueryService abBillQueryService;
    @DubboReference(check = false)
	private IAccebillPoolService accebillPoolService;
    @Autowired
    private AbFbsItemService abFbsItemService;
    @Autowired
    private AbConfigService abConfigService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private AbInputBankInfoService abInputBankInfoService;
    @Override
    public BaseDao<AbReceiveBillVO, String> getEntityDao() {
        return abReceiveBillDao;
    }
    /**
     * 列表显示应收票据登记信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbReceiveBill(DataTablesInput input, ICurrentUser userVo) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_RECEIVEBILL_QUERY, input.getCurrentUserId());
        // 当前操作人
        String userCode = userVo.getLoginName();
        String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_RESOURCE_RECEIVEBILL_QUERY,corpIds);
        DataTablesOutput<AbReceiveBillVO> dataTablesOutput = abReceiveBillDao.listAbReceiveBill(input, corpAuthList, userCode);
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG"
                    , "AB_BILL_ACCRECORDS_STATUS", "AB_ENTER_STOCK", "AB_ABRECEIVEBILL_SOURCE", "ACCEBILL_ABRECEIVE_INSIDESING"
                    , "AB_BANEM", "AB_DISCOUNT_RPDMK", "AB_STTLMMK", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            for (AbReceiveBillVO bill : listData) {
                injectValues(bill, listDictMap);
            }
        });
        return dataTablesOutput;
    }
    /**
     * 登记应收票据
     *
     * @param opType
     * @return
     */
    public void registerAbReceiveBill(AbReceiveBillVO abReceiveBillVO, String opType, ICurrentUser userVo) {
        AbVoucherinfo vouInfo = abReceiveBillVO.getAbVoucherInfo();
        if (StringUtils.isBlank(vouInfo.getPayeeBankTypeId())){
            BtBankAccDTO bankAccInfo = abBtBankAccService.getBankAccInfo(vouInfo.getPayeeacc());
            if(bankAccInfo!=null){
                vouInfo.setPayeeBankTypeId(bankAccInfo.getBankTypeId());
            }

        }
        AbVoucherinfoVice viceInfo = abReceiveBillVO.getAbVoucherinfoVice();
        if(StringUtils.isBlank(viceInfo.getSocCode())){
            String corpId = viceInfo.getCorpId();
            CorpDTO corpDTO = abCorpService.get(corpId);
            viceInfo.setSocCode(corpDTO.getSocCode());
        }
        AbAccrecords accRecords = abReceiveBillVO.getAbAccrecords();
        accRecords.setBankendrsmtmk(vouInfo.getBanendrsmtmk());
        // 贴现信息
        AbDiscount disInfo = abReceiveBillVO.getAbDiscount();
        vouInfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);// 票据变为业务处理中
        if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
            //收票登记 和 背书登记
            if ("10".equals(viceInfo.getBillSource()) || "11".equals(viceInfo.getBillSource())) {
                vouInfo.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS03);
                vouInfo.setEbillBlCrclIndc(AccebillConstant.CIRCULATE_TF0301);
            }
            //质押登记
            else if ("11".equals(viceInfo.getBillSource())) {
                vouInfo.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS03);
                vouInfo.setEbillBlCrclIndc(AccebillConstant.CIRCULATE_TF0304);
            }
            if (AccebillConstant.AB_BANEM_N.equals(vouInfo.getBanendrsmtmk())) {
                vouInfo.setEbillBlCrclIndc(AccebillConstant.CIRCULATE_TF0303);
            }
        }
        // 票据主信息
        abVoucherinfoService.saveOrUpdateAbVoucherinfo(vouInfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, userVo, viceInfo.getBillSource());
        // 从表信息
        viceInfo.setVid(vouInfo.getId());
        viceInfo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
        viceInfo.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);// 应收票据
        viceInfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);
        viceInfo.setCorpName(abReceiveBillVO.getCorpName());
        viceInfo.setApplyCorpName(abReceiveBillVO.getApplyCorpName());
        /** 上划、归集、下拨时状态设置为有效 modify by luop 2017/9/28 */
        Integer submitStatus = AccebillConstant.AB_BILL_BUS_STATUS11;
        accRecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
        if (StringUtils.isNotEmpty(accRecords.getLastid())) {
            AbAccrecords abAccrecords010 = abAccrecordsService.get(accRecords.getLastid());
            // 背书的流水
            if (abAccrecords010 != null && AccebillConstant.BRIEF_STATUS_010.equals(abAccrecords010.getBriefId())) {
                if (StringUtils.isNotEmpty(abAccrecords010.getLastid())) {
                    // 发起背书之前的流水
                    AbAccrecords abAccrecordsOld = abAccrecordsService.get(abAccrecords010.getLastid());
                    if (abAccrecordsOld != null
                            && (AccebillConstant.BRIEF_STATUS_6201.equals(abAccrecordsOld.getBriefId())
                            || AccebillConstant.BRIEF_STATUS_6202.equals(abAccrecordsOld.getBriefId())
                            || AccebillConstant.BRIEF_STATUS_6204.equals(abAccrecordsOld.getBriefId()))) {
                        submitStatus = AccebillConstant.AB_BILL_BUS_STATUS95;
                        accRecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
                        viceInfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
                    }
                }
            }
        }
        /** 上划、归集、下拨时状态设置为有效 end */
        abVoucherinfoViceService.saveVoucherinfoVice(viceInfo, userVo);
        accRecords.setVid(vouInfo.getId());
        accRecords.setVvid(viceInfo.getId());
        accRecords.setBriefId(AccebillConstant.BRIEF_STATUS_6001);
        accRecords.setReqAcctId(viceInfo.getBankAcc());// 业务接收人账号
        accRecords.setRecNm(viceInfo.getCnAccrecords());// 申请人
        accRecords.setApplyDate(new Date());// 申请日期
        // 是否挂接资金计划
        boolean isBudget = abVoucherinfoViceService.isBudget(viceInfo.getCorpId(), AccebillConstant.BRIEF_STATUS_6001);
        if (isBudget && StringUtils.isNotEmpty(abReceiveBillVO.getItemCodeStr())) {
            accRecords.setBudgetCode(abReceiveBillVO.getItemCodeStr());// 资金计划科目代码
        }
        String logType = "";
        String msg = "";
        // 保存
        if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_SAVE).equals(opType)) {
            accRecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);
            logType = AccebillConstant.ABLOG_LOGTYPE_ADD;
            msg = AccebillConstant.LANG_LOG_SAVE_SUCC;
        }
        // 提交
        if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_SUBMIT).equals(opType)) {
            accRecords.setStatus(submitStatus);
            logType = AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
            msg = AccebillConstant.LANG_LOG_SUBMIT_SUCC;
        }
        // 导入
        if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_IMPORTS).equals(opType)) {
            accRecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);
            logType = AccebillConstant.ABLOG_LOGTYPE_IMPORT;
            msg = AccebillConstant.LANG_LOG_IMPORT_SUCC;
        }
        accRecords.setBillFlag(vouInfo.getBillFlag());
        if (StringUtils.isEmpty(accRecords.getBillSource()) && viceInfo.getBillSource() != null) {
            accRecords.setBillSource(String.valueOf(viceInfo.getBillSource()));
        }
        boolean flag = abAccrecordsService.saveAccrecords(accRecords, userVo);
        abReceiveBillVO.setId(accRecords.getId());
        if (!flag) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 如果票据来源为贴现,写入贴现信息
        if (disInfo != null && (AccebillConstant.AB_BILL_SOURCE_DISCOUNT).equals(viceInfo.getBillSource())) {
            disInfo.setRecordId(accRecords.getId());
            abDiscountService.save(disInfo);
        }
        abLogService.saveAbLog(viceInfo.getId(), accRecords.getId(), AccebillConstant.BRIEF_STATUS_6001, logType, msg,
                userVo);
        // 挂接资金计划
        if (isBudget) {
            batchWriteBudget(abReceiveBillVO, opType, userVo);
        }
        if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_SUBMIT).equals(opType)) {
            if (AccebillConstant.AB_BILL_BUS_STATUS11.equals(submitStatus)) {
                // 调用审批
                AbReceiveBillVO variables = new AbReceiveBillVO();
                BeanUtils.copyProperties(vouInfo, variables);
                BeanUtils.copyProperties(viceInfo, variables);
                BeanUtils.copyProperties(accRecords, variables);
                 getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_RECEIVEBILL, accRecords.getId(), vouInfo,
                        viceInfo.getApplyCorpId(), userVo.getId());
            } else if (AccebillConstant.AB_BILL_BUS_STATUS95.equals(submitStatus)) {
                // 生成余额
                abBillAccBalService.setBankAccBillBal(accRecords.getId(), userVo);
            }
        }
        abBillAttachmentService.opAttachmentInfo(abReceiveBillVO, accRecords, userVo);
    }
    /**
     * ERP登记应收票据暂存票据
     *
     * @param abReceiveBillVO
     */
    public void registerAbReceiveBillERP(AbReceiveBillVO abReceiveBillVO) {
        AbVoucherinfo vouInfo = abReceiveBillVO.getAbVoucherInfo();
        AbVoucherinfoVice viceInfo = abReceiveBillVO.getAbVoucherinfoVice();
        AbAccrecords accRecords = abReceiveBillVO.getAbAccrecords();
        vouInfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);// 票据变为业务处理中
        // 票据主信息
        abVoucherinfoService.saveOrUpdateAbVoucherinfo(vouInfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, null);
        // 从表信息
        viceInfo.setVid(vouInfo.getId());
        viceInfo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
        viceInfo.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE1);// 应收票据
        viceInfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);
        accRecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
        abVoucherinfoViceService.saveVoucherinfoVice(viceInfo, null);
        accRecords.setVid(vouInfo.getId());
        accRecords.setVvid(viceInfo.getId());
        accRecords.setBriefId(AccebillConstant.BRIEF_STATUS_6001);
        accRecords.setReqAcctId(viceInfo.getBankAcc());// 业务接收人账号
        accRecords.setRecNm(viceInfo.getCnAccrecords());// 申请人
        accRecords.setApplyDate(new Date());// 申请日期
        String logType = "";
        String msg = "";
        // 保存
        accRecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);
        logType = AccebillConstant.ABLOG_LOGTYPE_ADD;
        msg = AccebillConstant.LANG_LOG_SAVE_SUCC;
        accRecords.setBillFlag(vouInfo.getBillFlag());
        if (StringUtils.isEmpty(accRecords.getBillSource()) && viceInfo.getBillSource() != null) {
            accRecords.setBillSource(String.valueOf(viceInfo.getBillSource()));
        }
        boolean flag = abAccrecordsService.saveAccrecords(accRecords, null);
        abReceiveBillVO.setId(accRecords.getId());
        if (!flag) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        abLogService.saveAbLog(viceInfo.getId(), accRecords.getId(), AccebillConstant.BRIEF_STATUS_6001, logType, msg,
                null);
    }
    /**
     * 应收票据详细信息
     *
     * @param accId
     * @return
     */
    @Transactional(readOnly = true)
    public AbReceiveBillVO getAbReceiveBillInfo(String accId) {
        AbReceiveBillVO billInfo = abReceiveBillDao.getAbReceiveBillInfo(accId);
        Optional.ofNullable(billInfo).ifPresent(bill->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG" , "AB_BILL_ACCRECORDS_STATUS"
                    , "AB_ENTER_STOCK", "AB_ABRECEIVEBILL_SOURCE", "ACCEBILL_ABRECEIVE_INSIDESING", "AB_BANEM", "AB_DISCOUNT_RPDMK", "AB_STTLMMK");
            // 冗余数据
            injectValues(bill, listDictMap);
        });
        String cnAccrecords = billInfo.getCnAccrecords();
        if (StringUtils.isNotBlank(cnAccrecords)){
            List<String> objects = new ArrayList<>();
            objects.add(cnAccrecords);
            Map<String, String> nameByIds = corpService.getNameByIds(objects);
            if (nameByIds.containsKey(cnAccrecords)){
                billInfo.setCnAccrecords(nameByIds.get(cnAccrecords));
            }
        }
        return billInfo;
    }
    private void injectValues(AbReceiveBillVO bill, Map<String, Map<String, DictDTO>> listDictMap) {
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
        // 票据状态
        Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_ACCRECORDS_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
                    .ifPresent(dict -> bill.setStatusName(dict.getName()));
        });
        // 所属库存
        Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
                    .ifPresent(dict -> bill.setEnterStockName(dict.getName()));
        });
        // 票据来源
        Optional.ofNullable(bill.getBillSource()).ifPresent(billSource -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ABRECEIVEBILL_SOURCE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billSource)))
                    .ifPresent(dict -> bill.setBillSourceName(dict.getName()));
        });
        // 内部业务标志：0 否；1 是
        Optional.ofNullable(bill.getInsideSing()).ifPresent(insideSing -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("ACCEBILL_ABRECEIVE_INSIDESING");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing+"")))
                    .ifPresent(dict -> bill.setInsideSingName(dict.getName()));
        });
        // 不得转让标记
        Optional.ofNullable(bill.getBanendrsmtmk()).ifPresent(banendrsmtmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(banendrsmtmk)))
                    .ifPresent(dict -> bill.setBanendrsmtmkName(dict.getName()));
        });
        // 贴现种类：RM00 买断式、RM01 回购式买断式
        Optional.ofNullable(bill.getRpdmk()).ifPresent(rpdmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_DISCOUNT_RPDMK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(rpdmk)))
                    .ifPresent(dict -> bill.setRpdmkName(dict.getName()));
        });
        // 清算方式：SM00 开启线上清算、SM01 关闭线上清算
        Optional.ofNullable(bill.getSttlmmk()).ifPresent(sttlmmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_STTLMMK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sttlmmk)))
                    .ifPresent(dict -> bill.setSttlmmkName(dict.getName()));
        });
        //出票标志: 1 新系统出票 、0 老系统出票
        Optional.ofNullable(bill.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
                    .ifPresent(dict -> bill.setNewStmIssuofpprIndName(dict.getName()));
        });
        //是否允许分包：0 否、 1 是 blgrdbagcrclPrmtIndName
        Optional.ofNullable(bill.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtIndName ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtIndName)))
                    .ifPresent(dict -> bill.setBlgrdbagcrclPrmtIndName(dict.getName()));
        });
        //业务类型
        Optional.ofNullable(bill.getBriefId()).ifPresent(briefId ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
                    .ifPresent(dict -> bill.setBriefIdName(dict.getName()));
        });
    }
    /**
     * 修改应收票据
     *
     * @param abReceiveBillVO
     * @param opType
     * @return
     */
    public boolean modifyAbReceiveBill(AbReceiveBillVO abReceiveBillVO, String opType, ICurrentUser userVo) {
        AbVoucherinfo vouInfoVo = abReceiveBillVO.getAbVoucherInfo();
        AbVoucherinfoVice viceInfoVo = abReceiveBillVO.getAbVoucherinfoVice();
        AbAccrecords accRecordsVo = abReceiveBillVO.getAbAccrecords();
        accRecordsVo.setBankendrsmtmk(vouInfoVo.getBanendrsmtmk());
        // 贴现信息
        AbDiscount disInfo = abReceiveBillVO.getAbDiscount();
        String vouId = vouInfoVo.getId();// 主信息id
        String viceId = viceInfoVo.getId();// 从表id
        String accId = accRecordsVo.getId();// 流水id
        if (StringUtils.isEmpty(vouId) || StringUtils.isEmpty(viceId) || StringUtils.isEmpty(accId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        // 更新票据主信息
        AbVoucherinfo vouInfoOld = abVoucherinfoService.get(vouId);
        AbVoucherinfo vouInfo = new AbVoucherinfo();
        BeanUtils.copyProperties(vouInfoOld, vouInfo);
        vouInfo.setNewStmIssuofpprInd(vouInfoVo.getNewStmIssuofpprInd());
        vouInfo.setBlgrdbagcrclPrmtInd(vouInfoVo.getBlgrdbagcrclPrmtInd());
        vouInfo.setSbbllRngStrtSn(vouInfoVo.getSbbllRngStrtSn());
        vouInfo.setSbbllRngEndSn(vouInfoVo.getSbbllRngEndSn());
        vouInfo.setBlStdAmt(vouInfoVo.getBlStdAmt());
        // 出票人统一识别号码
        vouInfo.setBlRmtrUnnIdNo(vouInfoVo.getBlRmtrUnnIdNo());
        // 收票人统一信用代码
        vouInfo.setBlRcvpymtpsUnnIdNo(vouInfoVo.getBlRcvpymtpsUnnIdNo());
        // 承兑人统一信用代码
        vouInfo.setBlAcptrUnnIdNo(vouInfoVo.getBlAcptrUnnIdNo());
        vouInfo.setOrigNo(vouInfoVo.getOrigNo());
        vouInfo.setBanendrsmtmk(vouInfoVo.getBanendrsmtmk());
        vouInfo.setBillFlag(vouInfoVo.getBillFlag());
        vouInfo.setBillType(vouInfoVo.getBillType());
        vouInfo.setBillMoney(vouInfoVo.getBillMoney());
        vouInfo.setStartDate(vouInfoVo.getStartDate());
        vouInfo.setEndDate(vouInfoVo.getEndDate());
        vouInfo.setPayer(vouInfoVo.getPayer());
        vouInfo.setPayeracc(vouInfoVo.getPayeracc());
        vouInfo.setPayerBankName(vouInfoVo.getPayerBankName());
        vouInfo.setPayerbankno(vouInfoVo.getPayerbankno());
        vouInfo.setPayee(vouInfoVo.getPayee());
        vouInfo.setPayeeacc(vouInfoVo.getPayeeacc());
        vouInfo.setPayeeBankName(vouInfoVo.getPayeeBankName());
        vouInfo.setPayeebankno(vouInfoVo.getPayeebankno());
        vouInfo.setAcceptor(vouInfoVo.getAcceptor());
        vouInfo.setAcceptoracc(vouInfoVo.getAcceptoracc());
        vouInfo.setAcceptorBankName(vouInfoVo.getAcceptorBankName());
        vouInfo.setAcceptorbankno(vouInfoVo.getAcceptorbankno());
        vouInfo.setAcontractNo(vouInfoVo.getAcontractNo());
        vouInfo.setAinvcnb(vouInfoVo.getAinvcnb());
        vouInfo.setPayerCdtRatgs(vouInfoVo.getPayerCdtRatgs());
        vouInfo.setPayerCdtRatgGgcy(vouInfoVo.getPayerCdtRatgGgcy());
        vouInfo.setPayerCdtRatgDuedt(vouInfoVo.getPayerCdtRatgDuedt());
        vouInfo.setUpdateBy(userVo.getLoginName());
        vouInfo.setUpdateName(userVo.getUsername());
        vouInfo.setUpdateDate(new Date());
        abVoucherinfoService.saveOrUpdateAbVoucherinfo(vouInfo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, userVo);
        // 更新从表信息
        AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(viceId);
        viceInfo.setCorpId(viceInfoVo.getCorpId());
        viceInfo.setApplyCorpId(viceInfoVo.getApplyCorpId());
        viceInfo.setBusCorpId(viceInfoVo.getBusCorpId());
        viceInfo.setBusPersonnel(viceInfoVo.getBusPersonnel());
        viceInfo.setBankAcc(viceInfoVo.getBankAcc());
        viceInfo.setBankCode(viceInfoVo.getBankCode());
        viceInfo.setEnterStock(viceInfoVo.getEnterStock());
        viceInfo.setBillSource(viceInfoVo.getBillSource());
        viceInfo.setCnAccrecords(viceInfoVo.getCnAccrecords());
        viceInfo.setAbs(viceInfoVo.getAbs());
        viceInfo.setRmk(viceInfoVo.getRmk());
        viceInfo.setAttachmentId(viceInfoVo.getAttachmentId());// 附件id
        viceInfo.setBlHoldUnnIdNo(viceInfoVo.getBlHoldUnnIdNo());// 持票人统一社会信用代码
        viceInfo.setUpdateBy(userVo.getLoginName());
        viceInfo.setUpdateName(userVo.getUsername());
        viceInfo.setUpdateDate(new Date());
        //处理所属账号信息
        if(StringUtils.isNotEmpty(viceInfo.getBankAcc())&&
                (StringUtils.isEmpty(viceInfo.getBankCode())
                        ||StringUtils.isEmpty(viceInfo.getBifCode()))
                &&StringUtils.isNotBlank (viceInfo.getBankAcc())){
            BtBankAccDTO bankAccInfo=abAccrecordsService.getBtBankAccDTO(viceInfo.getBankAcc());
            if(StringUtils.isEmpty(viceInfo.getBankCode())){
                viceInfo.setBankCode(bankAccInfo.getBankCode());
            }
            if(StringUtils.isEmpty(viceInfo.getBankName())){
                viceInfo.setBankName(bankAccInfo.getBankName());
            }
            if(StringUtils.isEmpty(viceInfo.getBifCode())){
                viceInfo.setBifCode(bankAccInfo.getBifCode());
            }
        }
        abVoucherinfoViceService.update(viceInfo);
        // 更新流水信息
        AbAccrecords accRecords = abAccrecordsService.get(accId);
        // 判断是否已经提交
        if (accRecords.getStatus() > 10) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        accRecords.setAbs(accRecordsVo.getBankendrsmtmk());
        accRecords.setBankendrsmtmk(accRecordsVo.getBankendrsmtmk());
        accRecords.setApplyDate(new Date()); // 申请日期
        accRecords.setBusinessDate(accRecordsVo.getBusinessDate());// 业务归属日期
        accRecords.setRmk(accRecordsVo.getRmk());
        accRecords.setAbs(accRecordsVo.getAbs());
        accRecords.setCnAccrecords(accRecordsVo.getCnAccrecords());
        accRecords.setRecNm(viceInfo.getCnAccrecords());// 发起人
        accRecords.setReqAcctId(viceInfo.getBankAcc());// 接收人
        accRecords.setUpdateBy(userVo.getLoginName());
        accRecords.setUpdateName(userVo.getUsername());
        accRecords.setUpdateDate(new Date());
        accRecords.setBudgetCode(abReceiveBillVO.getItemCodeStr());// 资金计划科目代码
        accRecords.setBillSource(String.valueOf(viceInfo.getBillSource()));
        accRecords.setFeemoney(accRecordsVo.getFeemoney());
        accRecords.setTrader(accRecordsVo.getTrader());
        String logType = "";
        String msg = "";
        // 暂存
        if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_EDIT).equals(opType)) {
            accRecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS0);
            logType = AccebillConstant.ABLOG_LOGTYPE_UPDATE;
            msg = AccebillConstant.LANG_LOG_UPDATE_SUCC;
        }
        // 提交
        if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_SUBMIT).equals(opType)) {
            accRecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
            logType = AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
            msg = AccebillConstant.LANG_LOG_SUBMIT_SUCC;
//            //修改--补录后生成收票登记暂存状态的票据--修改开票方票据的从表状态为处理中10
//            updateEmitBillByLastId(accRecords.getLastid(), false, userVo);
        }
        abAccrecordsService.update(accRecords, userVo);
        // 删除贴现信息
        abDiscountService.deleteByRecordId(accRecords.getId());
        // 如果票据来源为贴现,先删除贴现信息,再新增
        if (disInfo != null && (AccebillConstant.AB_BILL_SOURCE_DISCOUNT).equals(viceInfo.getBillSource())) {
            disInfo.setRecordId(accRecords.getId());
            abDiscountService.save(disInfo);
        }
        abReceiveBillVO.setAbVoucherInfo(vouInfo);
        abReceiveBillVO.setAbVoucherinfoVice(viceInfo);
        abReceiveBillVO.setAbAccrecords(accRecords);
        abLogService.saveAbLog(viceId, accId, AccebillConstant.BRIEF_STATUS_6001, logType, msg, userVo);
        // 挂接资金计划
        batchWriteBudget(abReceiveBillVO, opType, userVo);
        abBillNotificationService.completeMsg(accRecords, userVo);
        if (StringUtils.isNotEmpty(opType) && (AccebillConstant.BILL_OPERATE_SUBMIT).equals(opType)) {
            AbReceiveBillVO variables = new AbReceiveBillVO();
            BeanUtils.copyProperties(vouInfo, variables);
            BeanUtils.copyProperties(viceInfo, variables);
            BeanUtils.copyProperties(accRecords, variables);
             getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_RECEIVEBILL, accRecords.getId(), vouInfo,
                    viceInfo.getApplyCorpId(), userVo.getId());
        }
        return true;
    }
    /**
     * 删除应收票据
     *
     * @param accId
     * @return
     */
    public boolean deleteAbReceiveBill(String accId, ICurrentUser userVo) {
        if (StringUtils.isEmpty(accId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        // 流水信息
        AbAccrecords accRecords = abAccrecordsService.get(accId);
        // 删除从表信息
        AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecords.getVvid());
        viceInfo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
        viceInfo.setUpdateBy(userVo.getLoginName());
        viceInfo.setUpdateName(userVo.getUsername());
        viceInfo.setUpdateDate(new Date());
        abVoucherinfoViceService.update(viceInfo);
        // 删除流水信息
        abAccrecordsService.delete(accId,userVo);
        abLogService.saveAbLog(accRecords.getVvid(), accId, AccebillConstant.BRIEF_STATUS_6001,
                AccebillConstant.ABLOG_LOGTYPE_DELETE, AccebillConstant.LANG_LOG_DELETE_SUCC, userVo);
        // 删除资金计划
        abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_REC,
                BudgetConstant.PROCESS_SYSNAME_AB, accRecords.getVvid());
        return true;
    }
    /**
     * 批量删除应收票据
     *
     * @param accIds
     * @return
     */
    public boolean batchDeleteAbReceiveBill(String accIds, ICurrentUser userVo) {
        // 删除从表
        if (StringUtils.isEmpty(accIds)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String[] accIdArrays = accIds.split(",");
        String[] viceIdArrays = new String[accIdArrays.length];
        for (int i = 0; i < accIdArrays.length; i++) {
            // 流水信息
            AbAccrecords accRecords = abAccrecordsService.get(accIdArrays[i]);
            AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecords.getVvid());
            viceInfo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
            viceInfo.setUpdateBy(userVo.getLoginName());
            viceInfo.setUpdateName(userVo.getUsername());
            viceInfo.setUpdateDate(new Date());
            abVoucherinfoViceService.update(viceInfo);
            viceIdArrays[i] = accRecords.getVvid();
            abBillNotificationService.completeMsg(accRecords, userVo);
        }
        // 删除流水信息
        int executeNum = abAccrecordsService.batchDelete(accIdArrays, null,userVo);
        if (executeNum != accIdArrays.length) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        } else {
            abLogService.batchSaveAbLog(viceIdArrays, accIdArrays, AccebillConstant.BRIEF_STATUS_6001,
                    AccebillConstant.ABLOG_LOGTYPE_BATCHDELETE, AccebillConstant.LANG_LOG_BATCHDELETE_SUCC, userVo);
            // 批量删除资金计划
            for (int i = 0; i < viceIdArrays.length; i++) {
                abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_REC,
                        BudgetConstant.PROCESS_SYSNAME_AB, viceIdArrays[i]);
            }
            return true;
        }
    }
    /**
     * 批量提交应收票据
     *
     * @param accIds
     * @return
     */
    public boolean batchSubmitAbReceiveBill(String accIds, ICurrentUser userVo) {
        if (StringUtils.isEmpty(accIds)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String[] accIdArrays = accIds.split(",");
        for (int i = 0; i < accIdArrays.length; i++) {
            AbReceiveBillVO variables = new AbReceiveBillVO();
            AbAccrecords accRecords = abAccrecordsService.get(accIdArrays[i], true);
            // 修改流水状态
            if (StringUtils.isEmpty(accRecords.getId())) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
            }
            String[] accIdArray = accRecords.getId().split(",");
            int statusNum = abAccrecordsService.batchUpdate(accIdArray, AccebillConstant.AB_BILL_BUS_STATUS11,
                    accRecords.getStatus(), userVo);
            if (statusNum != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accRecords.getVid(), true);
            AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecords.getVvid(), true);
            BeanUtils.copyProperties(vouInfo, variables);
            BeanUtils.copyProperties(viceInfo, variables);
            BeanUtils.copyProperties(accRecords, variables);
            if (abVoucherinfoService.checkOrigNo(viceInfo.getCorpId(), vouInfo.getOrigNo(), viceInfo.getVid(), vouInfo.getSbbllRngStrtSn(), vouInfo.getSbbllRngEndSn())) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_SUBMIT_ORIGNO_REPEAT, String.valueOf(i + 1), vouInfo.getOrigNo());
            }
//            //修改--补录后生成收票登记暂存状态的票据--修改开票方票据的从表状态为处理中
//            updateEmitBillByLastId(accRecords.getLastid(), false, userVo);
            // 记录日志
            abLogService.saveAbLog(accRecords.getVvid(), accRecords.getId(), AccebillConstant.BRIEF_STATUS_6001,
                    AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
            // 修改资金计划状态
            boolean isBudget = abVoucherinfoViceService.isBudget(viceInfo.getCorpId(),
                    AccebillConstant.BRIEF_STATUS_6001);
            if (isBudget) {
                abFbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_REC,
                        BudgetConstant.PROCESS_SYSNAME_AB, viceInfo.getId(), BudgetConstant.STATUE_WAIT_CHECK, userVo);
            } else {
                // 删除资金计划
                abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_REC,
                        BudgetConstant.PROCESS_SYSNAME_AB, viceInfo.getId());
            }
            abBillNotificationService.completeMsg(accRecords, userVo);
            // 调用审批
             getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_RECEIVEBILL, accRecords.getId(), vouInfo,
                    viceInfo.getApplyCorpId(), userVo.getId());
        }
        return true;
    }
    // 审批
    public void approveOver(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
        if (StringUtils.isEmpty(buId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbAccrecords info = abAccrecordsService.get(buId, true);
        if (info == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        if (isPassOK) {
            String logType = AccebillConstant.ABLOG_LOGTYPE_APP;
            String billStatusOld = "";
            String billStatusNew = "";
            String ebillBlCrclIndc = "";
            AbVoucherinfo mainInfo = abVoucherinfoService.get(info.getVid(),true);
            //设置离线票据的票据状态和流通标志，并返回从票数据
            if("10".equals(info.getBillSource())){
                billStatusOld = AccebillConstant.AB_BILL_STATUS_030006;
                billStatusNew = AccebillConstant.NEWBILL_STATUS_CS03;
                ebillBlCrclIndc = "EM00".equals(mainInfo.getBanendrsmtmk()) ? AccebillConstant.CIRCULATE_TF0301 : AccebillConstant.CIRCULATE_TF0303;
            }else if("11".equals(info.getBillSource())){
                billStatusOld = AccebillConstant.AB_BILL_STATUS_100006;
                billStatusNew = AccebillConstant.NEWBILL_STATUS_CS03;
                ebillBlCrclIndc = "EM00".equals(mainInfo.getBanendrsmtmk()) ? AccebillConstant.CIRCULATE_TF0301 : AccebillConstant.CIRCULATE_TF0303;
            }else if("12".equals(info.getBillSource())){
                billStatusOld = AccebillConstant.AB_BILL_STATUS_180006;
                billStatusNew = AccebillConstant.NEWBILL_STATUS_CS03;
                ebillBlCrclIndc = AccebillConstant.CIRCULATE_TF0304;
            }
            AbAccebillUtils.setOffLineVoucherViceBillStatus(abVoucherinfoService,info.getVid(),info.getVvid()
                    ,info.getSendSign(),abVoucherinfoViceService,billStatusOld, billStatusNew, ebillBlCrclIndc);
            if (abAccbookService.isAccbook()) {
                boolean isAccboo = abAccbookService.checkBuIsAccbook(info);
                if (abAccbookService.isNotAutoAccbook() && isAccboo) {
                    abAccbookService.addAbAccbookInfo(info);
                    int appNum = abAccrecordsService.approve(info.getId(), AccebillConstant.AB_BILL_BUS_STATUS94, userVo);
                    if (appNum != 1) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                    abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, appMsg, userVo);
                } else {
                    busOverOp(info, logType, appMsg, userVo);
                    if (isAccboo) {
                        // 记账
                        abAccbookService.accbookVoucher(info, userVo);
                        logType = AccebillConstant.ABLOG_LOGTYPE_ACCBOOK_APP;
                        abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, AccebillConstant.LANG_LOG_AUTOACCBOOK_SUCC, userVo);
                    }
                }
            } else {
                busOverOp(info, logType, appMsg, userVo);
            }
        } else {
            String logType = AccebillConstant.ABLOG_LOGTYPE_REPULSE;
            busRepulseOp(info, logType, appMsg, userVo);
        }
    }
	/**
	 * 票据业务完成操作
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param msg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
    public void busOverOp(AbAccrecords info, String logType, String msg, ICurrentUser userVo){
		// 提交的话要调用审批(调用审批后，如果成功则修改status=95,同时票据的box_status=11，票据流入下一个可做的业务中)
        // 审批不通过，status=-1)
        Integer status = AccebillConstant.AB_BILL_BUS_STATUS95;
        int statusNum = abAccrecordsService.approve(info.getId(), status, userVo);
        if (statusNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        int upBoxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(),
                AccebillConstant.AB_BOX_STATUS_Y, userVo);
        if (upBoxStatusNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
		// 挂接资金计划
        Integer budgetStatus = BudgetConstant.STATUE_CHECK;
        abFbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_REC,
                BudgetConstant.PROCESS_SYSNAME_AB, info.getVvid(), budgetStatus, userVo);
		// 生成余额
//        if (!AccebillConstant.AB_BILL_SOURCE_PLEDGE.equals(info.getBillSource())) {
            abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
//        }
        abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
	}
	/**
	 * 票据业务打回操作
	 * @param info 票据流水信息
	 * @param logType 日志类型
	 * @param msg 日志信息
	 * @param userVo 操作用户
	 */
	@Override
    public void busRepulseOp(AbAccrecords info, String logType, String msg, ICurrentUser userVo){
		// 提交的话要调用审批(调用审批后，如果成功则修改status=95,同时票据的box_status=11，票据流入下一个可做的业务中)
        // 审批不通过，status=-1)
        Integer status =  AccebillConstant.AB_BILL_BUS_STATUS_1;
        int statusNum = abAccrecordsService.approve(info.getId(), status, userVo);
        if (statusNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
//        //修改--补录后生成收票登记暂存状态的票据--修改开票方票据的从表状态为处理中
//        updateEmitBillByLastId(info.getLastid(), true, userVo);
		// 挂接资金计划
        Integer budgetStatus = BudgetConstant.STATUE_BACK;
        abFbsBudgetProcessService.updateBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_REC,
                BudgetConstant.PROCESS_SYSNAME_AB, info.getVvid(), budgetStatus, userVo);
        abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
	}
    /**
     * 票据号码是否重复
     *
     * @param opType
     * @param abReceiveBillVO
     * @return 存在数据返回true否则false
     */
    @Transactional(readOnly = true)
    public boolean checkOrigNo(String opType, AbReceiveBillVO abReceiveBillVO) {
        String corpId = abReceiveBillVO.getCorpId();
        String origNo = abReceiveBillVO.getOrigNo();
        String id = abReceiveBillVO.getViceId();
        boolean flag = false;
        if (StringUtils.isNotEmpty(opType) && AccebillConstant.BILL_OPERATE_ADD.equals(opType)) {
            // 新增票据
            flag = abVoucherinfoService.checkOrigNo(corpId, origNo, AccebillConstant.AB_VOUCHERINFO_OP_TYPE1, abReceiveBillVO.getSbbllRngStrtSn(), abReceiveBillVO.getSbbllRngEndSn());
        }
        if (StringUtils.isNotEmpty(opType) && AccebillConstant.BILL_OPERATE_UPDATE.equals(opType)) {
            // 修改票据
//            flag = abVoucherinfoService.checkOrigNo(corpId, origNo, id, opType);
            flag = abVoucherinfoService.checkOrigNo(corpId, origNo, id, opType,abReceiveBillVO.getSbbllRngStrtSn(), abReceiveBillVO.getSbbllRngEndSn());
        }
        return flag;
    }
    /**
     * 通过票据号查询票据主信息
     *
     * @param origNo
     * @return
     */
    @Transactional(readOnly = true)
    public AbVoucherinfo getVouInfoByOrigNo(String origNo) {
        return abReceiveBillDao.getVouInfoByOrigNo(origNo);
    }
    /**
     * 导入应收票据信息
     *
     * @param list
     * @return
     */
    public boolean saveImport(List<AbReceiveBillVO> list, ICurrentUser userVo) {
        if (list != null && !list.isEmpty()) {
            //对导入数据再做一次校验
            verifyHandler(list);
            for (int i = 0; i < list.size(); i++) {
                AbReceiveBillVO abRecVo = list.get(i);
                if (StringUtils.isNotEmpty(abRecVo.getErrorMsg())) {
                    logger.error(new StringBuilder(100)
                            .append("第【").append(i+1).append("】条数据，票据号【").append(abRecVo.getOrigNo()).append("】,")
                            .append(abRecVo.getErrorMsg()).toString());
                    throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                AbVoucherinfo vouInfo = new AbVoucherinfo();// 主表信息
                AbVoucherinfoVice viceInfo = new AbVoucherinfoVice();// 从表信息
                AbAccrecords accRecords = new AbAccrecords(); // 流水信息
                // 写入主信息表
                BeanUtils.copyNotNullProperties(abRecVo, vouInfo);
                // 写入从表信息
                BeanUtils.copyNotNullProperties(abRecVo, viceInfo);
                // 写入流水信息
                BeanUtils.copyNotNullProperties(abRecVo, accRecords);
                abRecVo.setAbVoucherInfo(vouInfo);
                abRecVo.setAbVoucherinfoVice(viceInfo);
                abRecVo.setAbAccrecords(accRecords);
                // 调用登记方法
                registerAbReceiveBill(abRecVo, AccebillConstant.BILL_OPERATE_IMPORTS, userVo);
            }
        }
        return true;
    }
    /**
     * 审批回调方法
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
        this.approveOver(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
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
                .getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_RECEIVEBILL, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput<AbReceiveBillVO> dp = abReceiveBillDao.dataTableApprove(dataTablesInput, businessList);
        Optional.ofNullable(dp.getData()).ifPresent(listData -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_STATUS"
                    , "AB_ENTER_STOCK", "AB_ABRECEIVEBILL_SOURCE", "ACCEBILL_ABRECEIVE_INSIDESING", "AB_BANEM", "AB_DISCOUNT_RPDMK", "AB_STTLMMK");
            for (AbReceiveBillVO bill : listData) {
                injectValues(bill, listDictMap);
            }
        });
        return workFlowBusiness.merge(dp);
    }
    /**
     * 加载详细审核数据
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public AbReceiveBillVO getAbReceiveBillVOById(String id) {
        AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
        AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
        AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accrecordsInfo.getVvid(), true);
        AbReceiveBillVO reInfo = new AbReceiveBillVO();
        BeanUtils.copyNotNullProperties(vouInfo, reInfo);
        BeanUtils.copyNotNullProperties(viceInfo, reInfo);
        BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
        reInfo.setVid(vouInfo.getId());
        reInfo.setCreateBy(accrecordsInfo.getCreateBy());
        reInfo.setCreateName(accrecordsInfo.getCreateName());
        // 添加冗余数据
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_BLGRDBAGCRCL_PRMT_IND","AB_BILL_NEW_STM_ISSUOFPPR_IND","AB_BRIEF_STATUS","AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_ACCRECORDS_STATUS"
                , "AB_ENTER_STOCK", "AB_ABRECEIVEBILL_SOURCE", "ACCEBILL_ABRECEIVE_INSIDESING", "AB_BANEM", "AB_DISCOUNT_RPDMK", "AB_STTLMMK");
        injectValues(reInfo,listDictMap);
        return reInfo;
    }
    /**
     * 保存预算信息
     *
     * @param abReceiveBillVO
     * @param opType
     */
    public void batchWriteBudget(AbReceiveBillVO abReceiveBillVO, String opType, ICurrentUser userVo) {
        AbVoucherinfoVice viceInfo = abReceiveBillVO.getAbVoucherinfoVice();
        AbAccrecords accRecords = abReceiveBillVO.getAbAccrecords();
        AbVoucherinfo vouInfoVo = abReceiveBillVO.getAbVoucherInfo();
        List<FbsBudgetProcessDTO> budgetlist = abReceiveBillVO.getBudgetList();
//        // 获取账户信息
//        BtBankAccDTO bankAccInfo = null;
//        if(StringUtils.isNotBlank (viceInfo.getBankAcc())){
//            bankAccInfo = abAccrecordsService.getBtBankAccDTO(viceInfo.getBankAcc());
//        }
        // 币别取人民币
        String curId = abVoucherinfoViceService.getCurIdByCNY();
        if (budgetlist != null && !budgetlist.isEmpty()) {
            for (FbsBudgetProcessDTO budget : budgetlist) {
                budget.getItemCode();
                budget.setStatus(accRecords.getStatus());// 状态:0,录入,11在途,95占用
                budget.setCorpId(viceInfo.getCorpId());// 单位id
                budget.setProcessDate(accRecords.getBusinessDate());// 执行日期
                budget.setBillId(viceInfo.getId());// 从表id
                budget.setBillNo(vouInfoVo.getOrigNo());
                budget.setCurId(curId);
//                if (bankAccInfo!=null && bankAccInfo.getCurList() != null && !bankAccInfo.getCurList().isEmpty()) {
//                    budget.setCurId(bankAccInfo.getCurList().get(0));// 币别取人民币
//                }
                //添加内转判断标识  票据来源为收票或背书时，收票方式对内，同一个网点下的成员单位 判定为内转
                if(AccebillConstant.AB_BILL_SOURCE_REC.equals (abReceiveBillVO.getAbVoucherinfoVice ().getBillSource ())
                        ||AccebillConstant.AB_BILL_SOURCE_ENDORSE.equals (abReceiveBillVO.getAbVoucherinfoVice ().getBillSource ())){
                    CorpDTO corpInfo = abCorpService.getCorpIdByName (abReceiveBillVO.getAbVoucherinfoVice().getCnAccrecords ());
                    String cnAccrecordsId = null;
                    if(corpInfo!=null){
                        cnAccrecordsId = corpInfo.getId ();
                    }
                    if(AccebillConstant.AB_INSIDE_SING_Y.equals (String.valueOf (abReceiveBillVO.getAbVoucherinfoVice().getInsideSing ()))
                            && cnAccrecordsId !=null
                            && abCorpService.isTheSameNetByCorpId(abReceiveBillVO.getAbVoucherinfoVice().getCorpId (),cnAccrecordsId) ){
                        budget.setInnerSign ("1");//内转
                    }else{
                        budget.setInnerSign ("0");//非内转
                    }
                }else{
                    budget.setInnerSign ("0");
                }
                budget.setBusinessName(BudgetConstant.BUSINESS_NAME_REC);// 执行业务名称
                budget.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_AB);// 执行系统名称
                budget.setCreateBy(userVo.getLoginName());// 创建人登录名称
                budget.setCreateDate(new Date());// 创建日期
            }
        }
        // 删除资金计划信息
        abFbsBudgetProcessService.deleteBudgetProcessForBill(BudgetConstant.BUSINESS_NAME_REC,
                BudgetConstant.PROCESS_SYSNAME_AB, viceInfo.getId());
        // 写入资金计划信息
        if (budgetlist != null && !budgetlist.isEmpty()) {
            abFbsBudgetProcessService.writeBudgetProcess(budgetlist, userVo);
        }
    }
    /**
     * 根据单位id获取账户信息
     *
     * @param corpId
     * @return
     */
    @Transactional(readOnly = true)
    public List<BtBankAccDTO> getBtBankAccDTO(String corpId) {
        BtBankAccDTO params = new BtBankAccDTO();
        params.setCorpId(corpId);
        return abBtBankAccService.getBankAccList(params);
    }
    /**
     * 判断是否是内部单位
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public boolean getCorpByName(String name) {
        CorpDTO dto = abCorpService.getCorpIdByName(name);
        return dto != null;
    }
    /**
     * 根据单位名称查询单位id
     *
     * @param name 单位名称
     * @return
     */
    @Transactional(readOnly = true)
    public String getCorpIdByName(String name) {
        CorpDTO dto = abCorpService.getCorpIdByName(name);
        if (dto != null) {
            return dto.getId();
        }
        return null;
    }
    /**
     * 根据corpId判断单位是否为网点
     *
     * @param corpId
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isNetByCorpId(String corpId) {
        CorpDTO dto = abCorpService.get(corpId);
        return dto != null && "0".equals(String.valueOf(dto.getType()));
    }
    /**
     * 根据corpId判断单位是否为顶级网点
     *
     * @param corpId
     * @return
     */
    @Transactional(readOnly = true)
    public CorpDTO isTopNetByCorpId(String corpId) {
        CorpDTO dto = abCorpService.get(corpId);
        return dto;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(AbReceiveBillVO vo) {
        return null;
    }
    @Override
    @Transactional(readOnly = true)
    public void verifyHandler(List<AbReceiveBillVO> list) {
        if (list != null && !list.isEmpty()) {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND",
                    "AB_BILL_FLAG","AB_ENTER_STOCK","AB_ABRECEIVEBILL_SOURCE","ACCEBILL_ABRECEIVE_INSIDESING","AB_BANEM");

            Map<String, String> origNoMap = new HashMap<>();
            HashMap<String, CorpDTO> corpNameMap = new HashMap<>();
            HashMap<String, CorpDTO> corpCodeMap = new HashMap<>();
            Map<String, BtBankAccDTO> bankAccInfoMap = new HashMap<>();
            Map<String,DictDTO> billTypeDictMap=abDictService.listMap(AccebillConstant.AB_BILL_TYPE_DICT);
            //是否挂预算
            boolean budgetSel =abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET).toBoolean();
            //单位计划上报集合
            HashMap<String, Boolean> reportBudgetMap = new HashMap<>();
            //计划科目
            HashMap<String, FbsItemDTO> fbsItemMap = new HashMap<>();
            //这里进行判断处理
            BigDecimal standardMoney = new BigDecimal("0.01");
            BigDecimal oneFlag = new BigDecimal("1");
            // 账户信息
            for (AbReceiveBillVO abRecVo : list) {
                List<String> errorList = new ArrayList<>();
                CorpDTO corpInfo = null;
                BtBankAccDTO bankAccInfo = null;
                if (StringUtils.isEmpty(abRecVo.getApplyCorpCode()) && StringUtils.isEmpty(abRecVo.getApplyCorpName())) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_CORP_APPLYCODEANDNAME_NOTNULL);
                } else {
                    if (StringUtils.isEmpty(abRecVo.getNewStmIssuofpprIndName())
                            || !(AccebillConstant.NEW_STM_ISSUOFPPR_IND_ZERO.equals(abRecVo.getNewStmIssuofpprIndName())
                            || AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(abRecVo.getNewStmIssuofpprIndName())
                            || AccebillConstant.NEW_STM_ISSUOFPPR_IND_ZERO.equals(abRecVo.getNewStmIssuofpprInd())
                            || AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(abRecVo.getNewStmIssuofpprInd()))) {
                        errorList.add("出票标志不能为空且必须为0或者1");
                    } else if (StringUtils.isEmpty(abRecVo.getNewStmIssuofpprInd())) {
                        abRecVo.setNewStmIssuofpprInd(abRecVo.getNewStmIssuofpprIndName());

                        Optional.ofNullable(abRecVo.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd -> {
                            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
                            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd + "")))
                                    .ifPresent(dict -> abRecVo.setNewStmIssuofpprIndName(dict.getName()));
                        });
                    }
                    if (StringUtils.isNotEmpty(abRecVo.getApplyCorpCode())) {
                        if (corpCodeMap.containsKey(abRecVo.getApplyCorpCode())) {
                            corpInfo = corpCodeMap.get(abRecVo.getApplyCorpCode());
                        } else {
                            corpInfo = abCorpService.getCorpByCode(abRecVo.getApplyCorpCode());
                        }
                    } else {
                        if (corpNameMap.containsKey(abRecVo.getApplyCorpName())) {
                            corpInfo = corpNameMap.get(abRecVo.getApplyCorpName());
                        } else {
                            corpInfo = abCorpService.getCorpIdByName(abRecVo.getApplyCorpName());
                        }
                    }
                    if (corpInfo == null) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_APPLYCORPID_ISNOTEXIST);
                    } else {
                        abRecVo.setApplyCorpId(corpInfo.getId());
                        abRecVo.setApplyCorpCode(corpInfo.getCode());
                        abRecVo.setApplyCorpName(corpInfo.getName());
                        corpCodeMap.put(corpInfo.getCode(), corpInfo);
                        corpNameMap.put(corpInfo.getName(), corpInfo);
                    }
                }

                // 出票标志：新系统出票 ；是否允许拆票： 允许拆分； 子票开始序号：不允许为空
                if(AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(abRecVo.getNewStmIssuofpprIndName()) &&
                        AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ONE.equals(abRecVo.getBlgrdbagcrclPrmtIndName()) && StringUtils.isBlank(abRecVo.getSbbllRngStrtSn()) ){
                    errorList.add("新系统出票的允许拆分的子票区间开始序号不能为空!");
                }
                if (((AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(abRecVo.getNewStmIssuofpprIndName()) || AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(abRecVo.getNewStmIssuofpprInd()))
                        && (AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ONE.equals(abRecVo.getBlgrdbagcrclPrmtIndName()) || AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ONE.equals(abRecVo.getBlgrdbagcrclPrmtInd())))
                        && StringUtils.isBlank(abRecVo.getSbbllRngEndSn()) && StringUtils.isNotBlank(abRecVo.getSbbllRngStrtSn())) {
                    BigDecimal billMoney = abRecVo.getBillMoney();
                    BigDecimal divResult = billMoney.divide(standardMoney);
                    String sbbllRngStrtSn = abRecVo.getSbbllRngStrtSn();
                    BigDecimal sbbllSnBig = new BigDecimal(sbbllRngStrtSn);
                    if (sbbllSnBig.compareTo( new BigDecimal("0")) == 0){
                        errorList.add("新系统出票的允许拆分的子票区间开始序号不能全为0!");
                    }else {
                        BigDecimal subResult = sbbllSnBig.add(divResult).subtract(oneFlag);
                        abRecVo.setSbbllRngEndSn(subResult.setScale(0).toString());
                    }
                }
                if(((AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(abRecVo.getNewStmIssuofpprIndName()) || AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(abRecVo.getNewStmIssuofpprInd()))
                        && (AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ONE.equals(abRecVo.getBlgrdbagcrclPrmtIndName()) || AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ONE.equals(abRecVo.getBlgrdbagcrclPrmtInd())))
                        && StringUtils.isNotBlank(abRecVo.getSbbllRngStrtSn()) && StringUtils.isNotBlank(abRecVo.getSbbllRngEndSn())){
                    String sbbllRngStrtSn = abRecVo.getSbbllRngStrtSn();
                    BigDecimal billMoney = abRecVo.getBillMoney();
                    BigDecimal sbbllRngEndSnBig = new BigDecimal(abRecVo.getSbbllRngEndSn());
                    BigDecimal sbbllRngStrSNBig = new BigDecimal(sbbllRngStrtSn);
                    BigDecimal addResult = sbbllRngEndSnBig.subtract(sbbllRngStrSNBig).add(oneFlag);
                    BigDecimal divide = addResult.divide(new BigDecimal("100"));
                    if (divide.compareTo(billMoney) != 0){
                        errorList.add("票据票面金额和子票区间计算的金额不匹配，请核实导入的数据!");
                    }
                }

                corpInfo = null;
                if (StringUtils.isEmpty(abRecVo.getCorpCode()) && StringUtils.isEmpty(abRecVo.getCorpName())) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_CORP_CODEANDNAME_NOTNULL);
                } else {
                    if (StringUtils.isNotEmpty(abRecVo.getCorpCode())) {
                        if (corpCodeMap.containsKey(abRecVo.getCorpCode())) {
                            corpInfo = corpCodeMap.get(abRecVo.getCorpCode());
                        } else {
                            corpInfo = abCorpService.getCorpByCode(abRecVo.getCorpCode());
                        }
                    } else {
                        if (corpNameMap.containsKey(abRecVo.getCorpName())) {
                            corpInfo = corpNameMap.get(abRecVo.getCorpName());
                        } else {
                            corpInfo = abCorpService.getCorpIdByName(abRecVo.getCorpName());
                        }
                    }
                    if (corpInfo == null) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_CORPID_ISNOTEXIST);
                    } else {
                        abRecVo.setCorpId(corpInfo.getId());
                        abRecVo.setCorpCode(corpInfo.getCode());
                        abRecVo.setCorpName(corpInfo.getName());
                        corpCodeMap.put(corpInfo.getCode(), corpInfo);
                        corpNameMap.put(corpInfo.getName(), corpInfo);
                    }
                }
                //业务单位
                if (StringUtils.isNotEmpty(abRecVo.getBusCorpName())) {
                    CorpDTO busCorpDTO = null;
                    if (corpNameMap.containsKey(abRecVo.getBusCorpName())) {
                        busCorpDTO = corpNameMap.get(abRecVo.getBusCorpName());
                    } else {
                        busCorpDTO = abCorpService.getCorpIdByName(abRecVo.getBusCorpName());
                    }
                    if (busCorpDTO == null) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUSCORPID_ISNOTEXIST);
                    } else {
                        abRecVo.setBusCorpId(busCorpDTO.getId());
                    }
                }
                List<String> subNoList = NewDraftDealMethod.subTicketIntervalLenLack(abRecVo.getSbbllRngStrtSn(),abRecVo.getSbbllRngEndSn());
                abRecVo.setSbbllRngStrtSn(subNoList.get(0));
                abRecVo.setSbbllRngEndSn(subNoList.get(1));
                //内部交易根据票据号补全信息
                autoFillBillInfo(errorList,abRecVo);
                if (StringUtils.isEmpty(abRecVo.getBlgrdbagcrclPrmtIndName())
                        || !(AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ZERO.equals(abRecVo.getBlgrdbagcrclPrmtIndName())
                        || AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ONE.equals(abRecVo.getBlgrdbagcrclPrmtIndName())
                        || AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ZERO.equals(abRecVo.getBlgrdbagcrclPrmtInd())
                        || AccebillConstant.BLGRDBAGCRCL_PRMT_IND_ONE.equals(abRecVo.getBlgrdbagcrclPrmtInd()))) {
                    errorList.add("是否允许分包标志不能为空且必须为0或者1");
                } else if (StringUtils.isEmpty(abRecVo.getBlgrdbagcrclPrmtInd())) {
                    abRecVo.setBlgrdbagcrclPrmtInd(abRecVo.getBlgrdbagcrclPrmtIndName());

                    // 是否允许分包
                    Optional.ofNullable(abRecVo.getBlgrdbagcrclPrmtInd()).ifPresent(blgrdbagcrclPrmtInd -> {
                        Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_BLGRDBAGCRCL_PRMT_IND");
                        Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(blgrdbagcrclPrmtInd)))
                                .ifPresent(dict -> abRecVo.setBlgrdbagcrclPrmtIndName(dict.getName()));
                    });
                }
//                if (StringUtils.isEmpty(abRecVo.getBillTypeName())) { // 票据类型
//                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLTYPE_ISNOTNULL);
//                } else {
//                    if (AccebillConstant.AB_BILL_TYPE_BANK.equals(abRecVo.getBillTypeName())
//                            || AccebillConstant.AB_BILL_TYPE_MERCH.equals(abRecVo.getBillTypeName())) {
//                        abRecVo.setBillType(Integer.parseInt(abRecVo.getBillTypeName()));
//                    } else {
//                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLTYPE_ERROR);
//                    }
//                }
				// 票据类型不存在进行异常提醒
				if (StringUtils.isEmpty(abRecVo.getBillTypeName())) { // 票据类型
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLTYPE_ISNOTNULL);
				} else if (!billTypeDictMap.containsKey(abRecVo.getBillTypeName())) {
					errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLTYPE_ERROR);
				} else {
					abRecVo.setBillType(Integer.parseInt(billTypeDictMap.get(abRecVo.getBillTypeName()).getCode()));
					abRecVo.setBillTypeName(billTypeDictMap.get(abRecVo.getBillTypeName()).getName());
				}
                if (StringUtils.isEmpty(abRecVo.getBillFlagName())) { // 流通介质
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLFLAG_ISNOTNULL);
                } else {
                    if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(abRecVo.getBillFlagName())
                            || AccebillConstant.AB_BILL_FLAG_ELECT.equals(abRecVo.getBillFlagName())
                            || AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abRecVo.getBillFlag()))
                            || AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))) {
                        if (abRecVo.getBillFlag() == null) {
                            abRecVo.setBillFlag(Integer.parseInt(abRecVo.getBillFlagName()));

                            // 流通介质：10 纸票、11 电票
                            Optional.ofNullable(abRecVo.getBillFlag()).ifPresent(billFlag -> {
                                Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
                                Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
                                        .ifPresent(dict -> abRecVo.setBillFlagName(dict.getName()));
                            });
                        }
                    } else {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLFLAG_ERROR);
                    }
                }
                //电票所属账号必填(需要非第三方票据类型)
                if (StringUtils.isEmpty(abRecVo.getBankAcc())
                		&& AccebillConstant.AB_BILL_FLAG_ELECT.equals(abRecVo.getBillFlag())
                		&& !AbAccebillUtils.isBillTypeTP(abRecVo.getBillType())) { // 所属账号
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BANKACC_ISNOTNULL);
                } else {
                    if (StringUtils.isNotEmpty(abRecVo.getBankAcc())) {
	                    if (bankAccInfoMap.containsKey(abRecVo.getBankAcc())) {
	                        bankAccInfo = bankAccInfoMap.get(abRecVo.getBankAcc());
	                    } else {
	                        bankAccInfo = abBtBankAccService.getBankAccInfo(abRecVo.getBankAcc());
	                    }
	                    if (bankAccInfo == null) {
	                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BANKACC_ISNOTEXIST);
	                    } else {
	                        abRecVo.setBankName(bankAccInfo.getBankName());
	                        if (StringUtils.isNotEmpty(abRecVo.getCorpId())
	                                && !abRecVo.getCorpId().equals(bankAccInfo.getCorpId())) {
	                            errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BANKACCANDCORP_NOTMATCH); // 所属账号与所属单位不匹配!
	                        }
                            //判断账号是否为票据专户
	                        if(!BtBankAccConstant.ELECTRICBILL_YES.equals(bankAccInfo.getElectricBill())){
                                errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BANKACC_ISNOTELECTRICBILL);
                            }
	                    }
                    }else{
                        abRecVo.setBankName(null);
                    }
                }
                // 校验票据号
                if (origNoMap.containsKey(abRecVo.getOrigNo() + "&" + abRecVo.getSbbllRngStrtSn() + "&" + abRecVo.getSbbllRngEndSn())) {
                    abRecVo.addErrorMsg(errorList);
                    errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOTREPETITION); // 数据中票据号不能重复!
                } else {
                    origNoMap.put(abRecVo.getOrigNo() + "&" + abRecVo.getSbbllRngStrtSn() + "&" + abRecVo.getSbbllRngEndSn(), abRecVo.getOrigNo() + "&" + abRecVo.getSbbllRngStrtSn() + "&" + abRecVo.getSbbllRngEndSn());
                    // 校验票据号
                    if (StringUtils.isNotEmpty(abRecVo.getCorpId()) && StringUtils.isNotEmpty(abRecVo.getOrigNo())) {
                        boolean flag = abVoucherinfoService.checkOrigNo(abRecVo.getCorpId(), abRecVo.getOrigNo(), AccebillConstant.AB_VOUCHERINFO_OP_TYPE1,
                                abRecVo.getSbbllRngStrtSn(), abRecVo.getSbbllRngEndSn());
                        if (flag) {
                            errorList.add(AccebillConstant.ACCEBILL_MESSAGE_ORIGNOINSYSTEM_REPEAT);// 票据号在系统中已经存在,不能重复!
                        }
                    }
                }

                if (StringUtils.isEmpty(abRecVo.getEnterStockName())) { // 所属库存
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_ENTERSTOCK_ISNOTNULL);
                } else {
                    if (AccebillConstant.AB_ENTER_STOCK_CORP.equals(abRecVo.getEnterStockName())
                            || AccebillConstant.AB_ENTER_STOCK_NET.equals(abRecVo.getEnterStockName())
                            || AccebillConstant.AB_ENTER_STOCK_BANK.equals(abRecVo.getEnterStockName())
                            || AccebillConstant.AB_ENTER_STOCK_CORP.equals(abRecVo.getEnterStock())
                            || AccebillConstant.AB_ENTER_STOCK_NET.equals(abRecVo.getEnterStock())
                            || AccebillConstant.AB_ENTER_STOCK_BANK.equals(abRecVo.getEnterStock())) {
                        if (StringUtils.isEmpty(abRecVo.getEnterStock())) {
                            abRecVo.setEnterStock(abRecVo.getEnterStockName());

                            Optional.ofNullable(abRecVo.getEnterStock()).ifPresent(enterStock -> {
                                Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
                                Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock + "")))
                                        .ifPresent(dict -> abRecVo.setEnterStockName(dict.getName()));
                            });
                        }
                    } else {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_ENTERSTOCK_ERROR);
                    }
                }

                if (StringUtils.isEmpty(abRecVo.getBillSourceName())) { // 票据来源
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLSOURCE_ISNOTNULL);
                } else {
                    if (AccebillConstant.AB_BILL_SOURCE_REC.equals(abRecVo.getBillSourceName())
                            || AccebillConstant.AB_BILL_SOURCE_ENDORSE.equals(abRecVo.getBillSourceName())
                            || AccebillConstant.AB_BILL_SOURCE_PLEDGE.equals(abRecVo.getBillSourceName())
                            || AccebillConstant.AB_BILL_SOURCE_REC.equals(abRecVo.getBillSource())
                            || AccebillConstant.AB_BILL_SOURCE_ENDORSE.equals(abRecVo.getBillSource())
                            || AccebillConstant.AB_BILL_SOURCE_PLEDGE.equals(abRecVo.getBillSource())) {
                        if (StringUtils.isEmpty(abRecVo.getBillSource())) {
                            abRecVo.setBillSource(abRecVo.getBillSourceName());

                            Optional.ofNullable(abRecVo.getBillSource()).ifPresent(billSource -> {
                                Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ABRECEIVEBILL_SOURCE");
                                Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billSource + "")))
                                        .ifPresent(dict -> abRecVo.setBillSourceName(dict.getName()));
                            });
                        }
                    } else {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLSOURCE_ERROR);
                    }
                }
                if (StringUtils.isEmpty(abRecVo.getInsideSingName())) { // 收票方式
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_INSIDESING_ISNOTNULL);
                } else {
                    if (AccebillConstant.AB_INSIDE_SING_Y.equals(abRecVo.getInsideSingName())
                            || AccebillConstant.AB_INSIDE_SING_N.equals(abRecVo.getInsideSingName())
                            || AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(abRecVo.getInsideSing()))
                            || AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(abRecVo.getInsideSing()))) {
                        if (abRecVo.getInsideSing() == null) {
                            abRecVo.setInsideSing(Integer.parseInt(abRecVo.getInsideSingName()));

                            Optional.ofNullable(abRecVo.getInsideSing()).ifPresent(insideSing -> {
                                Map<String, DictDTO> dictDtoMap = listDictMap.get("ACCEBILL_ABRECEIVE_INSIDESING");
                                Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(insideSing + "")))
                                        .ifPresent(dict -> abRecVo.setInsideSingName(dict.getName()));
                            });
                        }
                    } else {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_INSIDESING_ERROR);
                    }
                }

                if (StringUtils.isEmpty(abRecVo.getBanendrsmtmkName())) { // 不得转让标记
//                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BANENDRSMTMK_ISNOTNULL);
                    abRecVo.setBanendrsmtmk(AccebillConstant.AB_BANEM_Y);
                } else {
                    if (AccebillConstant.AB_BANEM_N.equals(abRecVo.getBanendrsmtmkName())
                            || AccebillConstant.AB_BANEM_Y.equals(abRecVo.getBanendrsmtmkName())
                            || AccebillConstant.AB_BANEM_N.equals(abRecVo.getBanendrsmtmk())
                            || AccebillConstant.AB_BANEM_Y.equals(abRecVo.getBanendrsmtmk())) {
                        if (StringUtils.isEmpty(abRecVo.getBanendrsmtmk())) {
                            abRecVo.setBanendrsmtmk(abRecVo.getBanendrsmtmkName());

                            Optional.ofNullable(abRecVo.getBanendrsmtmk()).ifPresent(banendrsmtmk -> {
                                Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
                                Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(banendrsmtmk + "")))
                                        .ifPresent(dict -> abRecVo.setBanendrsmtmkName(dict.getName()));
                            });
                        }
                    } else {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BANENDRSMTMK_ERROR);
                    }
                }

                if (abRecVo.getStartDate() == null) { // 出票日期
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_STARTDATE_ISNOTNULL);
                }
                if (abRecVo.getEndDate() == null) { // 到期日期
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_ENDDATE_ISNOTNULL);
                }
                if (abRecVo.getBusinessDate() == null) { // 业务办理日期
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUSINESSDATE_ISNOTNULL);
                }
                if (abRecVo.getBillMoney() == null) { // 票据金额
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLMONEY_ISNOTNULL);
                }else {
                    if(abRecVo.getSbbllRngStrtSn().length()>1){
                        BigDecimal subMoney = (new BigDecimal(abRecVo.getSbbllRngEndSn()).subtract(new BigDecimal(abRecVo.getSbbllRngStrtSn())).add(BigDecimal.ONE)).divide(new BigDecimal(100));
                        if(subMoney.compareTo(abRecVo.getBillMoney()) != 0) errorList.add("票据金额或者子票区间填写不正确");
                    }
                }
                if (abRecVo.getStartDate() != null && abRecVo.getEndDate() != null) {
                    if (abRecVo.getStartDate().getTime() >= abRecVo.getEndDate().getTime()) {// 到期日期必须大于出票日期
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_COMSTARTTOEND);
                    }
                    // 业务办理日期必须大于等于出票日期,小于到期日期!
                    if (abRecVo.getBusinessDate() != null
                            && !(abRecVo.getBusinessDate().getTime() >= abRecVo.getStartDate().getTime()
                            && abRecVo.getBusinessDate().getTime() < abRecVo.getEndDate().getTime())) {
                        errorList.add(AccebillConstant.BILL_ACCEBILL_ABRECEIVEBILL_CHECKBUSINESSDATE);
                    }
                    // 电票不能超过一年
                    if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))) {
                        Calendar calendarElect = Calendar.getInstance();
                        calendarElect.setTime(abRecVo.getStartDate());
                        calendarElect.add(Calendar.YEAR, 1);
                        if (calendarElect.getTime().getTime() < abRecVo.getEndDate().getTime()) {
                            errorList.add(AccebillConstant.BILL_ACCEBILL_BILLFLAG_11);
                        }
                    }
                    // 纸票不能超过半年
                    if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abRecVo.getBillFlag()))) {
                        Calendar calendarPaper = Calendar.getInstance();
                        calendarPaper.setTime(abRecVo.getStartDate());
                        calendarPaper.add(Calendar.MONTH, 6);
                        if (calendarPaper.getTime().getTime() < abRecVo.getEndDate().getTime()) {
                            errorList.add(AccebillConstant.BILL_ACCEBILL_BILLFLAG_10);
                        }
                    }
                }
                if (abRecVo.getBillMoney() != null) { // 票据金额
                    // 金额最小为0.01
                    if (abRecVo.getBillMoney().compareTo(BigDecimal.valueOf(0.01)) < 0) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLMONEYMIX);
                    }
                    // 电票不能超过10亿
                    if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))
                            && (abRecVo.getBillMoney().compareTo(BigDecimal.valueOf(1000000000.00)) > 0)) {
                        errorList.add(AccebillConstant.TABLE_ABVOUCHERINFO_BILLMONEY_BILLFLAG11);
                    }
                    // 纸票金额不能超过设置的额度
                    if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(abRecVo.getBillFlag()))
                            && (abRecVo.getBillMoney().compareTo(abVoucherinfoViceService.getMaxBillMoney()) > 0)) {
                        errorList.add(AccebillConstant.TABLE_ABVOUCHERINFO_BILLFLAG10_MAXBILLMONEY);
                    }
                }
                // 电票只能是单位库存
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))
                        && StringUtils.isNotEmpty(abRecVo.getEnterStock())
                        && !AccebillConstant.AB_ENTER_STOCK_CORP.equals(abRecVo.getEnterStock())) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLFLAG11_CORPENTERSTOCK);
                }
                // 票据来源为质押时只能为单位库存
                if (AccebillConstant.AB_BILL_SOURCE_PLEDGE.equals(
                        String.valueOf(abRecVo.getBillSource())) && StringUtils.isNotEmpty(abRecVo.getEnterStock())
                        && !AccebillConstant.AB_ENTER_STOCK_CORP.equals(abRecVo.getEnterStock())) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLSOURCE_PLEDGE_ERROR);
                }
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))
                        && StringUtils.isEmpty(abRecVo.getPayeracc())
                        && !AbAccebillUtils.isBillTypeTP(abRecVo.getBillType())) { // 出票人账号
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_PAYERACC_BILLFLAG11_ISNOTNULL);
                }
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))
                        && StringUtils.isEmpty(abRecVo.getPayerBankName())
                        && !AbAccebillUtils.isBillTypeTP(abRecVo.getBillType())) { // 出票人开户银行名称
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_PAYERBANKNAME_BILLFLAG11_ISNOTNULL);
                }
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))
                        && StringUtils.isEmpty(abRecVo.getPayeeacc())
                        && !AbAccebillUtils.isBillTypeTP(abRecVo.getBillType())) { // 收票人账号
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_PAYEEACC_BILLFLAG11_ISNOTNULL);
                }
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))
                        && StringUtils.isEmpty(abRecVo.getPayeeBankName())
                        && !AbAccebillUtils.isBillTypeTP(abRecVo.getBillType())) { // 收票人开户银行名称
                    errorList.add(AccebillConstant.MESSAGE_PAYEEBANKNAME_BILLFLAG11_ISNOTNULL);
                }
                if(StringUtils.isEmpty(abRecVo.getAcceptor())
                        && !AbAccebillUtils.isBillTypeTP(abRecVo.getBillType())){
                    //为商票时，承兑人信息与出票人信息一致
                    if(AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(abRecVo.getBillType()))){
                        abRecVo.setAcceptor(abRecVo.getPayer());
                        abRecVo.setAcceptoracc(abRecVo.getPayeracc());
                        abRecVo.setAcceptorbankno(abRecVo.getPayerbankno());
                        abRecVo.setAcceptorBankName(abRecVo.getPayerBankName());
                        abRecVo.setBlAcptrUnnIdNo(abRecVo.getBlAcptrUnnIdNo() != null ? abRecVo.getBlAcptrUnnIdNo() : abRecVo.getBlRmtrUnnIdNo());
                    }
                    // 为银票时，承兑人信息为出票人银行信息，承兑人账户为0。
                    if (AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abRecVo.getBillType()))) {
                        if (StringUtils.isEmpty(abRecVo.getAcceptor())) {
                            abRecVo.setAcceptor(abRecVo.getPayerBankName());
                            abRecVo.setAcceptoracc("0");
                            abRecVo.setAcceptorbankno(abRecVo.getPayerbankno());
                            abRecVo.setAcceptorBankName(abRecVo.getPayerBankName());
                        }
                    }
                }
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))
                        && StringUtils.isEmpty(abRecVo.getAcceptoracc())
                        && !AbAccebillUtils.isBillTypeTP(abRecVo.getBillType())) { // 承兑人账号
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORACC_BILLFLAG11_ISNOTNULL);
                }
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))
                        && StringUtils.isEmpty(abRecVo.getAcceptorBankName())
                        && !AbAccebillUtils.isBillTypeTP(abRecVo.getBillType())) { // 承兑人开户行名称
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORBANKNAME_BILLFLAG11_ISNOTNULL);
                }
                //当承兑人开户行号未填写时根据承兑人开户行名称查找银行开户行号
                if(!StringUtils.isEmpty(abRecVo.getAcceptorBankName()) && StringUtils.isEmpty(abRecVo.getAcceptorbankno())){
                    InputBankInfoDTO bankInfoDTO = abInputBankInfoService.getByBankName(abRecVo.getAcceptorBankName().trim());
                    //若未查出则导入失败
                    if(bankInfoDTO == null){
                        errorList.add("根据承兑人开户行名称未查询到开户行号");
                    }else{
                        abRecVo.setAcceptorbankno(bankInfoDTO.getBankCode());
                    }
                }
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(abRecVo.getBillFlag()))
                        && StringUtils.isEmpty(abRecVo.getAcceptorbankno())
                        && !AbAccebillUtils.isBillTypeTP(abRecVo.getBillType())) { // 承兑人开户行行号
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORBANKNO_BILLFLAG11_ISNOTNULL);
                }
                // 是否挂接资金计划
                if(budgetSel && StringUtils.isNotEmpty(abRecVo.getCorpId())){
                    Boolean reportBudget = reportBudgetMap.get(abRecVo.getCorpId());
                    if (reportBudget == null) {
                        reportBudget = abVoucherinfoViceService.isBudget(abRecVo.getCorpId(), AccebillConstant.BRIEF_STATUS_6001);
                        reportBudgetMap.put(abRecVo.getCorpId(), reportBudget);
                    }
                    if (reportBudget && StringUtils.isEmpty(abRecVo.getItemCodeStr())) {
                        errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNOTEXIST);
                    }else if(reportBudget && StringUtils.isNotEmpty(abRecVo.getItemCodeStr())){
                        FbsItemDTO fbsItemDTO = fbsItemMap.get(abRecVo.getItemCodeStr());
                        //判断计划科目是否存在
                        if(fbsItemDTO==null){
                            fbsItemDTO = abFbsItemService.getFbsItemByCode(abRecVo.getItemCodeStr());
                            fbsItemMap.put(abRecVo.getItemCodeStr(), fbsItemDTO);
                        }
                        if (EmptyUtils.isEmpty(fbsItemDTO)) {
                            errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISNULL);
                        }else{
                            // 判断资金计划科目代码是否属于该业务
                            if (abAccrecordsService.itemCodeIsMatchBusiness(AccebillConstant.AB_CONFIG_RECITEM,
                                    fbsItemDTO.getItemCode())) {
                                errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_ITEMCODE_ISMATCH_BUSINESS);
                            } else {
                                FbsBudgetProcessDTO budgetProcessDTO = new FbsBudgetProcessDTO();
                                budgetProcessDTO.setItemCode(abRecVo.getItemCodeStr());
                                budgetProcessDTO.setItemName(fbsItemDTO.getItemName());
                                budgetProcessDTO.setProcessMoney(abRecVo.getBillMoney());
                                budgetProcessDTO.setProcessCurMoney(abRecVo.getBillMoney());
                                budgetProcessDTO.setExchangeRate(new BigDecimal(1));
                                List<FbsBudgetProcessDTO> budgetList = new ArrayList<>();
                                budgetList.add(budgetProcessDTO);
                                abRecVo.setBudgetList(budgetList);
                            }
                        }
                    }
                }
                abRecVo.addErrorMsg(errorList);
            }
        }
    }

    /**
    * @Description 根据票据号补全票据信息
    * @author zhous
    * @date 2023/9/27 18:03
    */
    private void autoFillBillInfo(List<String> errorList, AbReceiveBillVO abRecVo) {
        AbVoucherinfoVO voucherinfoVO = null;
        if(AccebillConstant.NEW_STM_ISSUOFPPR_IND_ONE.equals(abRecVo.getNewStmIssuofpprInd())){
            //新系统出票根据票据号和子票区间查询
            voucherinfoVO = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(abRecVo.getOrigNo(), abRecVo.getSbbllRngStrtSn(), abRecVo.getSbbllRngEndSn());
        }else{
            voucherinfoVO = abVoucherinfoService.getAbVoucherinfoVOByOrigNo(abRecVo.getOrigNo(),"0","0");
        }
        if(voucherinfoVO != null){
            voucherinfoVO.setId(null);
            voucherinfoVO.setBoxStatus(null);
            voucherinfoVO.setCreateBy(null);
            voucherinfoVO.setCreateDate(null);
            voucherinfoVO.setCreateName(null);
            voucherinfoVO.setUpdateBy(null);
            voucherinfoVO.setUpdateDate(null);
            voucherinfoVO.setUpdateName(null);
            voucherinfoVO.setOldDraftId(null);
            BeanUtils.copyNotNullProperties(voucherinfoVO,abRecVo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        return this.getAbReceiveBillVOById(aid);
    }
    /**
     * 加载详细数据
     *
     * @param aid
     * @return
     */
    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbBillInfo(String aid, String briefId) {
        String briefIdNew = briefId;
        if (StringUtils.isEmpty(briefIdNew)) {
            AbAccrecords accrecordsInfo = abAccrecordsService.get(aid, true);
            if (accrecordsInfo != null) {
                briefIdNew = accrecordsInfo.getBriefId();
            }else{
                AbPoolDtlDTO poolDtlold = accebillPoolService.getAbPoolDtlDtoByParams(aid);
                if(poolDtlold!=null){
                    //根据票据从表id和票据池交易明细id查询最新的流水信息
                    AbAccrecords abAccrecords = abAccrecordsService.getBillByPdIdAndVvId(poolDtlold.getVvId(), poolDtlold.getId());
                    if (abAccrecords != null) {
                        briefIdNew = abAccrecords.getBriefId();
                        aid = abAccrecords.getId();
                    }
                }
            }
        }
        AbVoucherinfoVO billInfo = null;
        if (StringUtils.isNotEmpty(briefIdNew)) {
            IAccebillBaseService baseService = AbAccebillUtils.getAbAccebillService(briefIdNew);
            if (baseService != null) {
                billInfo = baseService.getAbBillInfoById(aid);
                if (billInfo != null) {
                    abBillQueryService.getBillPoolInfo(billInfo);
                }
                if (billInfo == null && !(baseService instanceof AbReceiveBillService)) {
                    billInfo =  getAbBillInfoById(aid);
                }
            }
        }
        if(StringUtils.isNotEmpty(billInfo.getBusCorpId())&&billInfo!=null){
            String busCorpName=corpService.getCorpName(billInfo.getBusCorpId());
            billInfo.setBusCorpName(busCorpName);
        }
        if (ObjectUtil.isNotEmpty(billInfo)){
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BANEM", "AB_BILL_STATUS"
                    , "AB_BILL_FLAG","AB_BILL_NEW_STM_ISSUOFPPR_IND","AB_BRIEF_STATUS", "AB_BILL_COUNT_POOL_STATUS","ACCEBILL_ABRECEIVE_INSIDESING","AB_BILL_DSHNRCD","AB_BILL_NEW_DSHNRCD");
            injectValues(billInfo, listDictMap);
        }
        return billInfo;
    }
    private void injectValues(AbVoucherinfoVO bill, Map<String, Map<String, DictDTO>> listDictMap) {
        Class<? extends AbVoucherinfoVO> aClass = bill.getClass();
        // 票据类型
        Optional.ofNullable(bill.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_TYPE");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType+"")))
                    .ifPresent(dict -> bill.setBillTypeName(dict.getName()));
        });
        // 不得转让标记
        Optional.ofNullable(bill.getBanendrsmtmk()).ifPresent(banendrsmtmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(banendrsmtmk)))
                    .ifPresent(dict -> bill.setBanendrsmtmkName(dict.getName()));
        });
        // 票据状态
        Optional.ofNullable(bill.getBillStatus()).ifPresent(billStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billStatus)))
                    .ifPresent(dict -> bill.setBillStatusName(dict.getName()));
        });
        // 流通介质
        Optional.ofNullable(bill.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_FLAG");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag+"")))
                    .ifPresent(dict -> bill.setBillFlagName(dict.getName()));
        });
        // 票据池状态空或0池外1池内
        Optional.ofNullable(bill.getCountPoolStatus()).ifPresent(countPoolStatus -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_COUNT_POOL_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(countPoolStatus)))
                    .ifPresent(dict -> bill.setCountPoolStatusName(dict.getName()));
        });
        //出票标志: 1 新系统出票 、0 老系统出票
        Optional.ofNullable(bill.getNewStmIssuofpprInd()).ifPresent(newStmIssuofpprInd ->{
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_NEW_STM_ISSUOFPPR_IND");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(newStmIssuofpprInd)))
                    .ifPresent(dict -> bill.setNewStmIssuofpprIndName(dict.getName()));
        });
        // 票据状态
        Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
                    .ifPresent(dict -> bill.setBriefIdName(dict.getName()));
        });

        Map<String, Object> stringObjectMap = BeanUtils.beanToMap(bill);
        if(stringObjectMap.containsKey("dshnrcd")){
            //拒付理由
            Optional.ofNullable(stringObjectMap.get("dshnrcd")).ifPresent(dshnrcd -> {
                Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_DSHNRCD");
                Map<String, DictDTO> dictDtoMapNew = listDictMap.get("AB_BILL_NEW_DSHNRCD");
                Optional.ofNullable(dictDtoMapNew).flatMap(dictMap ->
                        {
                            DictDTO dshnrcd1 = dictMap.get(dshnrcd.toString());
                            if(dshnrcd1 == null){
                                dshnrcd1 = dictDtoMap.get(dshnrcd.toString());
                            }
                            return Optional.ofNullable(dshnrcd1);
                        }).ifPresent(dict -> stringObjectMap.put("dshnrcdName",dict.getName()));
            });
            AbVoucherinfoVO abVoucherinfoVO = BeanUtils.copyMapProperties(stringObjectMap, aClass);
            BeanUtils.copyProperties(abVoucherinfoVO,bill);
        }
    }
//
//    /**
//     * 修改--补录后生成收票登记暂存状态的票据--修改开票方票据的从表状态为处理中10 或者已完成
//     * @param lastId 上一笔流水
//     */
//    public void updateEmitBillByLastId(String lastId, boolean isEnd, ICurrentUser userVo){
//    	if(StringUtils.isNotEmpty(lastId)){
//            AbAccrecords lastAcc = abAccrecordsService.get(lastId);
//            if(lastAcc!=null
//            		&&AccebillConstant.BRIEF_STATUS_6101.equals(lastAcc.getBriefId())){
//	            if(isEnd){
//	                //打回修改--补录后生成收票登记暂存状态的票据--修改开票方票据的从表状态为处理完成
//	                abVoucherinfoViceService.submitIsEnd(lastAcc.getVvid(),userVo);
//	            }else{
//	                //修改--补录后生成收票登记暂存状态的票据--修改开票方票据的从表状态为处理中
//	                abVoucherinfoViceService.submit(lastAcc.getVvid(),userVo);
//	            }
//            }
//    	}
//    }
}
