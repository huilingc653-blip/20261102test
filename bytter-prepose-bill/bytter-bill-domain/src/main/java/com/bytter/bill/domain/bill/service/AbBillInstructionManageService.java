package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbAccrecordsDao;
import com.bytter.bill.domain.bill.dao.AbBillInstructionManageDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoDao;
import com.bytter.bill.domain.bill.dao.AbVoucherinfoViceDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbBillReceivebiz;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.bill.domain.billbank.dao.BillAccrecordsDao;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.dao.BillSignDao;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.entity.BillSign;
import com.bytter.bill.domain.billbank.pool.service.*;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaApplyVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaConVO;
import com.bytter.bill.domain.billbank.service.*;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.bill.domain.billbank.vo.BillSignVo;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.*;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 指令处理
 * @date 2017-06-21 17:35:25
 */
@Service("abBillInstructionManageService")
@Transactional
public class AbBillInstructionManageService extends BaseService<AbBillInstructionManageVO, String> {
    @Autowired
    private AbBillInstructionManageDao abBillInstructionManage;

    @Autowired
    private AbAccrecordsService abAccrecordsService;

    @Autowired
    private AbVoucherinfoService abVoucherinfoService;

    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    @Autowired
    private AbBillReceivebizService abBillReceivebizService;

    @Autowired
    private AbLogService abLogService;

    @Autowired
    private AbCorpService abCorpService;

    @Autowired
    private AbBtBankAccService abBtBankAccService;

    @Autowired
    private AbDiscountService abDiscountService;

    @Autowired
    private AbRatUrationService abRatUrationService;

    @Autowired
    private AbBillNotificationService abBillNotificationService;

    @Autowired
    private AbBillAccBalService abBillAccBalService;

    @Autowired
    private AbVoucherinfoDao abVoucherinfoDao;

    @Autowired
    private AbVoucherinfoViceDao abVoucherinfoViceDao;

    @Autowired
    private AbAccrecordsDao abAccrecordsDao;

    @Autowired
    private BillTicketSplittingUtils billTicketSplittingUtils;

    /**
     * 电票接口service
     */
    @Autowired
	private IBillBankService billBankService;
    @Autowired
	private IBillAcceptanceRequestService billAcceptanceRequestService;
    @Autowired
	private IBillIssuanceRequestService billIssuanceRequestService;
    @Autowired
	private IBillOpenGuaranteeService billOpenGuaranteeService;
    @Autowired
	private IBillAcceptGuaranteeService billAcceptGuaranteeService;
    @Autowired
	private IBillDestructionRequestService billDestructionRequestService;
    @Autowired
	private IBillEndorseRequestService billEndorseRequestService;
    @Autowired
	private IBillEndorseGuaranteeService billEndorseGuaranteeService;
    @Autowired
	private IBillDiscountRequestService billDiscountRequestService;
    @Autowired
	private IBillRepurchaseDiscountRedemptionService billRepurchaseDiscountRedemptionService;
    @Autowired
	private IBillPresentationRequestService billPresentationRequestService;
    @Autowired
	private IBillOverduePresentationRequestService billOverduePresentationRequestService;
    @Autowired
	private IBillCollateralizationRequestService billCollateralizationRequestService;
    @Autowired
	private IBillRepurchasedCollateralizationRequestService billRepurchasedCollateralizationRequestService;
    @Autowired
	private IBillRecourseRequestService billRecourseRequestService;
    @Autowired
	private IBillApprovePaymentService billApprovePaymentService;
    @Autowired
	private IBillCommonRevokeRequestService billCommonRevokeRequestService;
	@Autowired(required = false)
    private IBillNonTransCancelService nonTransCancelService;
    @Autowired
    private AbEmitBillService abEmitBillService;
    @Autowired
    private AbGuaranteeService abGuaranteeService;

    @Autowired
	private IBillPoolIntoApplyService billPoolIntoApplyService;

    @Autowired
	private IBillPoolIntoRepealService billPoolIntoRepealService;

    @Autowired
	private IBillPoolOutApplyService billPoolOutApplyService;

    @Autowired
	private IBillPoolPdTstApplyService billPoolPdTstApplyService;

    @Autowired
	private IBillPoolEmitFinaApplyService billPoolEmitFinaApplyService;

    @Autowired
    private AbAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IDictService dictService;

	@Autowired
    private BillBankDao billBankDao;
    @Autowired
    private BillSignDao billSignDao;

    @Autowired
    private BillAccrecordsDao billAccrecordsDao;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @Autowired
    private AbInputBankInfoService abInputBankInfoService;


    @Override
    public BaseDao<AbBillInstructionManageVO, String> getEntityDao() {
        return abBillInstructionManage;
    }
    /**
     * 加载AbBillInstructionManage表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillInstructionManageInfo(DataTablesInput dataTablesInput) {
        String corpAuthSql = SqlUtil.getInSql(
                abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_INSTRUCTION_QUERY, dataTablesInput.getCurrentUserId()), "c.apply_corp_id", true);
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_INSTRUCTION_QUERY, corpIds);
        DataTablesOutput<AbBillInstructionManageVO> dataTablesOutput = abBillInstructionManage.listAbBillInstructionManage(dataTablesInput, corpAuthSql);
        Set<String> corpIdSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
                    , "AB_BILL_ACCRECORDS_STATUS", "AB_BILL_INSBRIEFID_TEST", "AB_BILL_INSOPTSTATUS", "AB_SGNUPMK");
            for (AbBillInstructionManageVO bill : listData) {
                bill.injectValues(listDictMap);
                if(StrUtil.isNotBlank(bill.getApplyCorpId())){
                    corpIdSet.add(bill.getApplyCorpId());
                }
                if(StrUtil.isNotBlank(bill.getCorpId())){
                    corpIdSet.add(bill.getCorpId());
                }
                if(StrUtil.isNotBlank(bill.getBusCorpId())){
                    corpIdSet.add(bill.getBusCorpId());
                }
            }
        });
        if(corpIdSet.size() <= 0){
            return dataTablesOutput;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return dataTablesOutput;
        }
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            for (AbBillInstructionManageVO bill : listData){
                CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
                bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
                bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
            }
        });
        return dataTablesOutput;
    }
    /**
     * 加载测试AbBillInstructionManage表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillInstructionManageInfoTest(DataTablesInput dataTablesInput) {
        String corpAuthSql = SqlUtil.getInSql(
                abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_INSTRUCTION_QUERYTEST, dataTablesInput.getCurrentUserId()), "c.apply_corp_id", true);
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_INSTRUCTION_QUERYTEST, corpIds);
        DataTablesOutput<AbBillInstructionManageVO> dataTablesOutput = abBillInstructionManage.listAbBillInstructionManageTest(dataTablesInput, corpAuthSql);
        Set<String> corpIdSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_BILL_OPENWAY"
                    , "AB_BILL_ACCRECORDS_STATUS", "AB_BILL_INSBRIEFID_TEST", "AB_BILL_INSOPTSTATUS", "AB_SGNUPMK", "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            for (AbBillInstructionManageVO bill : listData) {
                bill.injectValues(listDictMap);
                if(StrUtil.isNotBlank(bill.getApplyCorpId())){
                    corpIdSet.add(bill.getApplyCorpId());
                }
                if(StrUtil.isNotBlank(bill.getCorpId())){
                    corpIdSet.add(bill.getCorpId());
                }
                if(StrUtil.isNotBlank(bill.getBusCorpId())){
                    corpIdSet.add(bill.getBusCorpId());
                }
            }
        });
        if(corpIdSet.size() <= 0){
            return dataTablesOutput;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return dataTablesOutput;
        }
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            for (AbBillInstructionManageVO bill : listData){
                CorpDTO corpDTO = stringCorpDTOMap.get(bill.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill.getBusCorpId());
                bill.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
                bill.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
            }
        });
        return dataTablesOutput;
    }

    /**
     * 指令批量成功操作
     *
     * @param instructionVo
     */
    public void batchSucce(AbBillInstructionManageVO instructionVo, ICurrentUser userVo) {
        String[] ids = StringUtils.split(instructionVo.getIds(), ",");
        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        List<AbAccrecords> accrecordsList = abAccrecordsService.listAbAccrecordsByIds(ids);
        for (AbAccrecords info : accrecordsList) {
            //当业务是签收方拒收时，则不修改流水状态
            if (!info.getBriefId().startsWith("9") && !AccebillConstant.AB_SGNUPMK_N.equals(info.getSignUpFlag())) {
                AbAccrecordsVO accrecordsVO = new AbAccrecordsVO();
                accrecordsVO.setIdArray(info.getId().split(","));
                accrecordsVO.setOptStatusNew(instructionVo.getOpType());
                int num = abAccrecordsService.batchUpdate(accrecordsVO, userVo);
				/*if (accrecordsList.size() != num) {
					throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
				}*/
            }
        }
        String msg = "";
        String logType = "";
        if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(instructionVo.getOpType())) {
            succeOKTest(accrecordsList, userVo);
            msg = AccebillConstant.LANG_LOG_BATCHINSBANK_SUCC;
            logType = AccebillConstant.ABLOG_LOGTYPE_BATCHSUCCE;
        } else if (AccebillConstant.AB_BILL_OPT_STATUS18.equals(instructionVo.getOpType())) {
            succeSignTest(accrecordsList, userVo);
            msg = "批量已回应待签收,银行电票指令处理已回应待签收！";
            logType = AccebillConstant.ABLOG_LOGTYPE_BATCHSUCCESIGN;
        } else if (AccebillConstant.AB_BILL_OPT_STATUS17.equals(instructionVo.getOpType())) {
            msg = "批量已发送待回应,银行电票指令处理已发送待回应！";
            logType = AccebillConstant.ABLOG_LOGTYPE_BATCHSENDSUCCE;
        } else if (AccebillConstant.AB_BILL_OPT_STATUS_1.equals(instructionVo.getOpType())) {
            msg = "批量失败,银行电票指令处理失败！";
            logType = AccebillConstant.ABLOG_LOGTYPE_BATCHFAILURE;
            succeErrorTest(accrecordsList, userVo);
        } else if (AccebillConstant.AB_BILL_OPT_STATUS_4.equals(instructionVo.getOpType())) {
            succeNOTest(accrecordsList);
            msg = "批量已回应拒签,银行电票指令处理已回应拒签！";
            logType = AccebillConstant.ABLOG_LOGTYPE_BATCHFAILURE;
        }
        for (AbAccrecords info : accrecordsList) {
            abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(),
                    logType, msg, userVo);
        }
    }
    /**
     * 已回应待签收测试
     */
    public void succeSignTest(List<AbAccrecords> accrecordsList, ICurrentUser userVo) {
        // 固定常量，抽离
        final Integer RECORD_STATUS_18 = AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING;
        final String RETURN_CODE_SUCCESS = AccebillConstant.AB_BILL_RETURN_CODE_1;
        final String QUERY_FINISH_MSG = "查询结果完成！";

        for (AbAccrecords info : accrecordsList) {
            // 1. 查询凭证信息
            AbVoucherinfo vouInfo = abVoucherinfoService.get(info.getVid());
            if (vouInfo == null) {
                throw new ParameterException(AccebillConstant.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
            }
            String sbbllRngStrtSn = StrUtil.trim(vouInfo.getSbbllRngStrtSn());
            if (StrUtil.isEmpty(sbbllRngStrtSn)) {
                throw new BusinessException("子票区间不允许为空！");
            }

            // 2. 公共组装BillSign & BillSignVo
            BillSign billSignInfo = setBillSign(vouInfo, info, userVo);
            BillSignVo billSign = new BillSignVo();
            BeanUtil.copyProperties(billSignInfo, billSign);
            billSign.setRmrkByPropsr(billSignInfo.getRmk());
            billSign.setBankendrsmtmk(info.getBankendrsmtmk());

            // 3. 组装收款账户类型
            fillRecAcctTypeName(billSignInfo.getRecAcctId(), billSign);

            // 4. 获取账户信息
            BtBankAccDTO bankAccDTO = abAccrecordsService.getBtBankAccInfo(billSign.getRecAcctId());

            // 5. 公共VoucherInfoVo
            VoucherInfoVo voucher = buildVoucherInfoVo(info, RETURN_CODE_SUCCESS, null);
            String recordId = info.getId();
            String briefId = info.getBriefId();

            // 分支判断
            if (AccebillConstant.BRIEF_STATUS_001.equals(briefId)) {
                billBankService.updateAccrecordsInfoByRecordId(recordId, RECORD_STATUS_18, briefId, RETURN_CODE_SUCCESS, null);
            } else if (AccebillConstant.BRIEF_STATUS_002.equals(briefId)) {
                billAcceptanceRequestService.updateVoucherInfo(voucher);
                billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_ACCEPT);
                if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                    billSign.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS01);
                    billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0302);
                    billSign.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC02);
                } else {
                    billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_020001);
                }
                if (bankAccDTO != null) {
                    billAcceptanceRequestService.saveBillSign(billSign);
                }
            } else if (AccebillConstant.BRIEF_STATUS_003.equals(briefId)) {
                billIssuanceRequestService.updateVoucherInfo(voucher);
                billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_RECBILL);
                if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                    billSign.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS02);
                    billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0302);
                    billSign.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC03);
                } else {
                    billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_030001);
                }
                if (bankAccDTO != null && BtBankAccConstant.CIRCULATIONMODE_1.equals(bankAccDTO.getCirculationMode())) {
                    billIssuanceRequestService.saveBillSign(billSign);
                }
            } else if (AccebillConstant.BRIEF_STATUS_004.equals(briefId)) {
                billDestructionRequestService.updateVoucherInfo(voucher);
            } else if (AccebillConstant.BRIEF_STATUS_010.equals(briefId)) {
                if (AccebillConstant.AB_BILL_OPT_STATUS17.equals(info.getOptStatus())) {
                    billEndorseRequestService.updateAccrecordsInfoByRecordId(voucher.getRecordId(), RECORD_STATUS_18, null, RETURN_CODE_SUCCESS, QUERY_FINISH_MSG);
                } else {
                    billEndorseRequestService.updateVoucherInfo(voucher);
                }
                billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_RECITE);
                if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                    billSign.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS03);
                    billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0302);
                    billSign.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC07);
                } else {
                    billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_100001);
                }
                if (bankAccDTO != null && BtBankAccConstant.CIRCULATIONMODE_1.equals(bankAccDTO.getCirculationMode())) {
                    billEndorseRequestService.saveBillSign(billSign);
                }
            } else if (AccebillConstant.BRIEF_STATUS_024.equals(briefId)) {
                nonTransCancelService.updateVoucherInfo(voucher);
                billSign.setMsgtype(AccebillConstant.BRIEF_STATUS_024);
                if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                    billSign.setBillStatus(vouInfo.getBillStatus());
                    billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0302);
                    billSign.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC18);
                }
                if (bankAccDTO != null && BtBankAccConstant.CIRCULATIONMODE_1.equals(bankAccDTO.getCirculationMode())) {
                    nonTransCancelService.saveBillSign(billSign);
                }
            } else if (AccebillConstant.BRIEF_STATUS_011.equals(briefId)) {
                if (AccebillConstant.AB_BILL_OPT_STATUS17.equals(info.getOptStatus())) {
                    billDiscountRequestService.updateAccrecordsInfoByRecordId(voucher.getRecordId(), RECORD_STATUS_18, null, RETURN_CODE_SUCCESS, QUERY_FINISH_MSG);
                } else {
                    billDiscountRequestService.updateVoucherInfo(voucher);
                }
            } else if (AccebillConstant.BRIEF_STATUS_013.equals(briefId)) {
                if (AccebillConstant.AB_BILL_OPT_STATUS17.equals(info.getOptStatus()) || AccebillConstant.AB_BILL_OPT_STATUS18.equals(info.getOptStatus())) {
                    billDiscountRequestService.updateAccrecordsInfoByRecordId(voucher.getRecordId(), RECORD_STATUS_18, null, RETURN_CODE_SUCCESS, QUERY_FINISH_MSG);
                } else {
                    billDiscountRequestService.updateVoucherInfo(voucher);
                }
            } else if (AccebillConstant.BRIEF_STATUS_017.equals(briefId)) {
                billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_GUARGN);
                if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                    billSign.setBillStatus(vouInfo.getBillStatus());
                    billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0302);
                    billSign.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC04);
                } else {
                    billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_170001);
                }
                if (bankAccDTO != null) {
                    String bailType = info.getBailType();
                    if (AccebillConstant.AB_BAIL_TYPE_ADD.equals(bailType)) {
                        billOpenGuaranteeService.saveBillSign(billSign);
                    } else if (AccebillConstant.AB_BAIL_TYPE_ACCEPT.equals(bailType)) {
                        billAcceptGuaranteeService.saveBillSign(billSign);
                    } else if (AccebillConstant.AB_BAIL_TYPE_RECITE.equals(bailType)) {
                        billEndorseGuaranteeService.saveBillSign(billSign);
                    }
                }
            } else if (AccebillConstant.BRIEF_STATUS_018.equals(briefId)) {
                if (AccebillConstant.AB_BILL_OPT_STATUS17.equals(info.getOptStatus())) {
                    billCollateralizationRequestService.updateAccrecordsInfoByRecordId(voucher.getRecordId(), RECORD_STATUS_18, null, RETURN_CODE_SUCCESS, QUERY_FINISH_MSG);
                } else {
                    billCollateralizationRequestService.updateVoucherInfo(voucher);
                }
                billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_PLEDGE);
                if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                    billSign.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS03);
                    billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0302);
                    billSign.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC11);
                } else {
                    billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_180001);
                }
                if (bankAccDTO != null) {
                    billCollateralizationRequestService.saveBillSign(billSign);
                }
            } else if (AccebillConstant.BRIEF_STATUS_019.equals(briefId)) {
                if (AccebillConstant.AB_BILL_OPT_STATUS17.equals(info.getOptStatus())) {
                    billRepurchasedCollateralizationRequestService.updateAccrecordsInfoByRecordId(voucher.getRecordId(), RECORD_STATUS_18, null, RETURN_CODE_SUCCESS, QUERY_FINISH_MSG);
                } else {
                    billRepurchasedCollateralizationRequestService.updateVoucherInfo(voucher);
                }
                billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_PLEDGE_RELIEVE);
                if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                    long endTime = vouInfo.getEndDate().getTime();
                    long nowTime = DateUtils.getDateYMD().getTime();
                    billSign.setBillStatus((nowTime - endTime < 0) ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.NEWBILL_STATUS_CS04);
                    billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0302);
                    billSign.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC12);
                } else {
                    billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_190001);
                }
                if (bankAccDTO != null && StringUtils.isBlank(info.getPdId())) {
                    billRepurchasedCollateralizationRequestService.saveBillSign(billSign);
                }
            } else if (AccebillConstant.BRIEF_STATUS_020.equals(briefId)) {
                billPresentationRequestService.updateVoucherInfo(voucher);
                billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_HINT_PAYER);
                if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                    billSign.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS03);
                    billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0302);
                    billSign.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC13);
                } else {
                    billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_200001);
                }
                if (bankAccDTO != null) {
                    billPresentationRequestService.saveBillSign(billSign);
                }
            } else if (AccebillConstant.BRIEF_STATUS_021.equals(briefId)) {
                billOverduePresentationRequestService.updateVoucherInfo(voucher);
                billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_HINT_PAYER_OVERDUE);
                if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                    billSign.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS04);
                    billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0302);
                    billSign.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC13);
                } else {
                    billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_210001);
                }
                if (bankAccDTO != null) {
                    billOverduePresentationRequestService.saveBillSign(billSign);
                }
            } else if (AccebillConstant.BRIEF_STATUS_022.equals(briefId)) {
                billRecourseRequestService.updateVoucherInfo(voucher);
                if (bankAccDTO != null) {
                    billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_SEARCHFOR);
                    billRecourseRequestService.saveBillSign(billSign);
                }
            } else if (AccebillConstant.BRIEF_STATUS_023.equals(briefId)) {
                billApprovePaymentService.updateVoucherInfo(voucher);
                billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_SEARCHFOR_PAYOFF);
                billSign.setBlBsnTpcd(AccebillConstant.NEW_DRAFT_TYPE_BC16);
                if (AccebillConstant.AB_RCRS_TP_PROTESTY.equals(info.getRcrsTp())) {
                    if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                        billSign.setBillStatus(AccebillConstant.NEWBILL_STATUS_CS04);
                        billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0402);
                    } else {
                        billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_230601);
                    }
                } else {
                    if ("1".equals(vouInfo.getNewStmIssuofpprInd())) {
                        long endTime = vouInfo.getEndDate().getTime();
                        long nowTime = DateUtils.getDateYMD().getTime();
                        billSign.setBillStatus((nowTime - endTime < 0) ? AccebillConstant.NEWBILL_STATUS_CS03 : AccebillConstant.NEWBILL_STATUS_CS04);
                        billSign.setEbillBlCrclIndcd(AccebillConstant.CIRCULATE_TF0402);
                    } else {
                        billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_230701);
                    }
                }
                if (bankAccDTO != null) {
                    billApprovePaymentService.saveBillSign(billSign);
                }
            } else if (AccebillConstant.BRIEF_STATUS_032.equals(briefId)) {
                billCommonRevokeRequestService.updateVoucherInfo(voucher);
            } else if (AccebillConstant.BRIEF_STATUS_9003.equals(briefId)) {
                billIssuanceRequestService.updateVoucherInfo(voucher);
            } else if (AccebillConstant.BRIEF_STATUS_9010.equals(briefId)) {
                billEndorseRequestService.updateVoucherInfo(voucher);
            } else if (AccebillConstant.BRIEF_STATUS_9012.equals(briefId)) {
                billRepurchaseDiscountRedemptionService.updateAccrecordsInfoByRecordId(recordId, RECORD_STATUS_18, briefId, RETURN_CODE_SUCCESS, null);
            } else if (AccebillConstant.BRIEF_STATUS_9019.equals(briefId)) {
                billRepurchasedCollateralizationRequestService.updateVoucherInfo(voucher);
            } else if (AccebillConstant.AB_POOL_BRIEF_8001.equals(briefId)) {
                PoolEmitFinaApplyVO paramsVo = buildPoolEmitFinaApplyVO(recordId, RECORD_STATUS_18, RETURN_CODE_SUCCESS, null);
                billPoolEmitFinaApplyService.updatePoolEmitFinaApply(paramsVo);
            } else if (AccebillConstant.AB_POOL_BRIEF_8018.equals(briefId)) {
                PoolBillVoucherVO paramsVo = buildPoolBillVoucherVO(recordId, RECORD_STATUS_18, RETURN_CODE_SUCCESS, null);
                billPoolIntoApplyService.updatePoolIntoApply(paramsVo);
            } else if (AccebillConstant.AB_POOL_BRIEF_8818.equals(briefId)) {
                PoolBillVoucherVO paramsVo = buildPoolBillVoucherVO(recordId, RECORD_STATUS_18, RETURN_CODE_SUCCESS, null);
                billPoolIntoApplyService.updatePoolIntoPledge(paramsVo);
            } else if (AccebillConstant.AB_POOL_BRIEF_8019.equals(briefId)) {
                PoolBillVoucherVO paramsVo = buildPoolBillVoucherVO(recordId, RECORD_STATUS_18, RETURN_CODE_SUCCESS, null);
                billPoolOutApplyService.updatePoolOutApply(paramsVo);
            } else if (AccebillConstant.AB_POOL_BRIEF_8118.equals(briefId)
                    || AccebillConstant.AB_POOL_BRIEF_8218.equals(briefId)) {
                PoolBillVoucherVO paramsVo = buildPoolBillVoucherVO(recordId, RECORD_STATUS_18, RETURN_CODE_SUCCESS, null);
                if (AccebillConstant.AB_POOL_BRIEF_8118.equals(briefId)) {
                    billPoolPdTstApplyService.updatePoolRemovePledge(paramsVo);
                } else {
                    billPoolPdTstApplyService.updatePoolPdTstPledge(paramsVo);
                }
            } else if (AccebillConstant.AB_POOL_BRIEF_8032.equals(briefId)) {
                PoolBillVoucherVO paramsVo = buildPoolBillVoucherVO(recordId, RECORD_STATUS_18, RETURN_CODE_SUCCESS, null);
                billPoolIntoRepealService.updatePoolIntoRepeal(paramsVo);
            }
        }
    }

    /**
     * 填充收款账户类型
     */
    private void fillRecAcctTypeName(String recAcctId, BillSignVo billSign) {
        BtBankAccDTO bankAccDTo = iBtBankAccService.getBankAccInfo(recAcctId);
        if (bankAccDTo != null) {
            InputBankInfoDTO bankInfoDTO = abInputBankInfoService.getBtInputBankInfo(bankAccDTo.getBankCode());
            if (bankInfoDTO != null) {
                billSign.setRecAcctTypeName(bankInfoDTO.getBankTypeName());
            }
        }
    }

    /**
     * 构建VoucherInfoVo
     */
    private VoucherInfoVo buildVoucherInfoVo(AbAccrecords info, String returnCode, String returnMsg) {
        VoucherInfoVo voucher = new VoucherInfoVo();
        voucher.setVoucherId(info.getVid());
        voucher.setRecordId(info.getId());
        voucher.setReturnCode(returnCode);
        voucher.setReturnMsg(returnMsg);
        return voucher;
    }

    /**
     * 构建池子通用PoolBillVoucherVO
     */
    private PoolBillVoucherVO buildPoolBillVoucherVO(String busId, Integer optStatus, String reCode, String reMsg) {
        PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
        paramsVo.setBusId(busId);
        paramsVo.setOptStatus(String.valueOf(optStatus));
        paramsVo.setReCode(reCode);
        paramsVo.setReMsg(reMsg);
        return paramsVo;
    }

    /**
     * 构建PoolEmitFinaApplyVO
     */
    private PoolEmitFinaApplyVO buildPoolEmitFinaApplyVO(String busId, Integer optStatus, String reCode, String reMsg) {
        PoolEmitFinaApplyVO paramsVo = new PoolEmitFinaApplyVO();
        paramsVo.setBusId(busId);
        paramsVo.setOptStatus(String.valueOf(optStatus));
        paramsVo.setReCode(reCode);
        paramsVo.setReMsg(reMsg);
        return paramsVo;
    }

    /**
     * 接口发送失败测试
     */
    public void succeErrorTest(List<AbAccrecords> accrecordsList, ICurrentUser userVo) {
        Integer status = -1;
        String returnCode = "F";
        String returnMsg = "交易失败！";
        String briefId = null;
        String recordId = null;
        for (AbAccrecords info : accrecordsList) {
            briefId = info.getBriefId();
            recordId = info.getId();

//			if(!info.getBriefId().startsWith("9")){
            if (AccebillConstant.BRIEF_STATUS_9003.equals(info.getBriefId())) {
                billIssuanceRequestService.updateAccrecordsInfoByRecordId(recordId, status, briefId, returnCode, returnMsg);
            } else if (AccebillConstant.BRIEF_STATUS_9019.equals(info.getBriefId())) {
                billRepurchasedCollateralizationRequestService.updateAccrecordsInfoByRecordIdForBaffle(recordId, status, briefId, returnCode, returnMsg);
            } else if (AccebillConstant.BRIEF_STATUS_9012.equals(info.getBriefId())) {
                billRepurchaseDiscountRedemptionService.updateAccrecordsInfoByRecordId(recordId, status, briefId, returnCode, returnMsg);
            } else if (AccebillConstant.BRIEF_STATUS_001.equals(info.getBriefId())) {
                billBankService.updateAccrecordsInfoByRecordId(recordId, status, briefId, returnCode, returnMsg);
            } else if (AccebillConstant.BRIEF_STATUS_010.equals(info.getBriefId())
                    || AccebillConstant.BRIEF_STATUS_9010.equals(info.getBriefId())) {
                billEndorseRequestService.updateAccrecordsInfoByRecordIdForBaffle(recordId, status, briefId, returnCode, returnMsg);
            } else if (AccebillConstant.BRIEF_STATUS_011.equals(info.getBriefId())) {
                billDiscountRequestService.updateAccrecordsInfoByRecordIdForBaffle(recordId, status, briefId, returnCode, returnMsg);
            } else if (AccebillConstant.BRIEF_STATUS_018.equals(info.getBriefId())) {
                billCollateralizationRequestService.updateAccrecordsInfoByRecordIdForBaffle(recordId, status, briefId, returnCode, returnMsg);
            } else if (AccebillConstant.BRIEF_STATUS_019.equals(info.getBriefId())) {
                billRepurchasedCollateralizationRequestService.updateAccrecordsInfoByRecordIdForBaffle(recordId, status, briefId, returnCode, returnMsg);
            } else if (AccebillConstant.BRIEF_STATUS_020.equals(info.getBriefId())) {
                billPresentationRequestService.updateAccrecordsInfoByRecordId(recordId, status, briefId, returnCode, returnMsg);
            } else if (AccebillConstant.BRIEF_STATUS_021.equals(info.getBriefId())) {
                billPresentationRequestService.updateAccrecordsInfoByRecordId(recordId, status, briefId, returnCode, returnMsg);
            } else if (AccebillConstant.AB_POOL_BRIEF_8001.equals(info.getBriefId())) {
                PoolEmitFinaApplyVO paramsVo = new PoolEmitFinaApplyVO();
                paramsVo.setBusId(recordId);
                paramsVo.setOptStatus(String.valueOf(status));
                paramsVo.setReCode(returnCode);
                paramsVo.setReMsg(returnMsg);
                billPoolEmitFinaApplyService.updatePoolEmitFinaApply(paramsVo);
            } else if (AccebillConstant.AB_POOL_BRIEF_8018.equals(info.getBriefId())) {
                PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
                paramsVo.setBusId(recordId);
                paramsVo.setOptStatus(String.valueOf(status));
                paramsVo.setReCode(returnCode);
                paramsVo.setReMsg(returnMsg);
                billPoolIntoApplyService.updatePoolIntoApply(paramsVo);
            } else if (AccebillConstant.AB_POOL_BRIEF_8818.equals(info.getBriefId())) {
                PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
                paramsVo.setBusId(recordId);
                paramsVo.setOptStatus(String.valueOf(status));
                paramsVo.setReCode(returnCode);
                paramsVo.setReMsg(returnMsg);
                billPoolIntoApplyService.updatePoolIntoPledge(paramsVo);
            } else if (AccebillConstant.AB_POOL_BRIEF_8019.equals(info.getBriefId())) {
                PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
                paramsVo.setBusId(recordId);
                paramsVo.setOptStatus(String.valueOf(status));
                paramsVo.setReCode(returnCode);
                paramsVo.setReMsg(returnMsg);
                billPoolOutApplyService.updatePoolOutApply(paramsVo);
            } else if (AccebillConstant.AB_POOL_BRIEF_8118.equals(info.getBriefId())
                    || AccebillConstant.AB_POOL_BRIEF_8218.equals(info.getBriefId())) {
                PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
                paramsVo.setBusId(recordId);
                paramsVo.setOptStatus(String.valueOf(status));
                paramsVo.setReCode(returnCode);
                paramsVo.setReMsg(returnMsg);
                billPoolPdTstApplyService.updatePoolPdTstPledge(paramsVo);
            } else if (AccebillConstant.AB_POOL_BRIEF_8032.equals(info.getBriefId())) {
                PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
                paramsVo.setBusId(recordId);
                paramsVo.setOptStatus(String.valueOf(status));
                paramsVo.setReCode(returnCode);
                paramsVo.setReMsg(returnMsg);
                billPoolIntoRepealService.updatePoolIntoRepeal(paramsVo);
            }else if (AccebillConstant.BRIEF_STATUS_024.equals(info.getBriefId())
                    || AccebillConstant.BRIEF_STATUS_9024.equals(info.getBriefId())) {
                nonTransCancelService.updateAccrecordsInfoByRecordId(recordId, status, briefId, returnCode, returnMsg);
            }else if(AccebillConstant.BRIEF_STATUS_002.equals(info.getBriefId())
                    || AccebillConstant.BRIEF_STATUS_9002.equals(info.getBriefId())){
                billAcceptanceRequestService.updateAccrecordsInfoByRecordId(recordId,status,briefId,returnCode,returnMsg);
            }
//			}
        }
    }
    /**
     * 业务处理成功测试
     */
    public void succeOKTest(List<AbAccrecords> accrecordsList, ICurrentUser userVo) {
        for (AbAccrecords info : accrecordsList) {
            AbVoucherinfo vouInfo = abVoucherinfoService.get(info.getVid(),true);
            if(StrUtil.isEmpty(vouInfo.getSbbllRngStrtSn().trim())){
                throw new BusinessException("子票区间不允许为空！");
            }
            VoucherInfoVo voucher = new VoucherInfoVo();
            voucher.setVoucherId(info.getVid());
            voucher.setRecordId(info.getId());
            voucher.setReturnCode("S");
            voucher.setReturnMsg("交易成功");
            voucher.setEndDate(new Timestamp(vouInfo.getEndDate().getTime()));
            Integer status = AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK;
            String returnCode = voucher.getReturnCode();
            String returnMsg = voucher.getReturnMsg();
            String recordId = info.getId();
            if (info.getBriefId().startsWith("9")) {
                //签收方拒收处理
                if (AccebillConstant.AB_SGNUPMK_N.equals(info.getSignUpFlag())) {
                    //电票签收如果是拒签需要特殊处理validSign为2
                    //int reVsNum=abAccrecordsService.updateValidSign(info.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2);
                    if (AccebillConstant.BRIEF_STATUS_9002.equals(info.getBriefId())) {
                        billAcceptanceRequestService.updateAccrecordsStatusRejectionByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9003.equals(info.getBriefId())) {
                        billIssuanceRequestService.updateAccrecordsStatusRejectionByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9010.equals(info.getBriefId())) {
                        billEndorseRequestService.updateAccrecordsStatusRejectionByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9012.equals(info.getBriefId())) {
                        billRepurchaseDiscountRedemptionService.updateAccrecordsStatusRejectionByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9017.equals(info.getBriefId())) {
                        if (AccebillConstant.AB_BAIL_TYPE_ADD.equals(info.getBailType())) {
                            billOpenGuaranteeService.updateAccrecordsStatusRejectionByRecordId(voucher);
                        } else if (AccebillConstant.AB_BAIL_TYPE_ACCEPT.equals(info.getBailType())) {
                            billAcceptGuaranteeService.updateAccrecordsStatusRejectionByRecordId(voucher);
                        } else if (AccebillConstant.AB_BAIL_TYPE_RECITE.equals(info.getBailType())) {
                            billEndorseGuaranteeService.updateAccrecordsStatusRejectionByRecordId(voucher);
                        }
                    } else if (AccebillConstant.BRIEF_STATUS_9018.equals(info.getBriefId())) {
                        billCollateralizationRequestService.updateAccrecordsStatusRejectionByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9019.equals(info.getBriefId())) {
                        billRepurchasedCollateralizationRequestService.updateAccrecordsStatusRejectionByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9020.equals(info.getBriefId())) {
                        billPresentationRequestService.updateAccrecordsStatusRejectionByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9021.equals(info.getBriefId())) {
                        billOverduePresentationRequestService.updateAccrecordsStatusRejectionByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9023.equals(info.getBriefId())) {
                        billApprovePaymentService.updateAccrecordsStatusRejectionByRecordId(voucher);
                    }else if (AccebillConstant.BRIEF_STATUS_9024.equals(info.getBriefId())) {
                        nonTransCancelService.updateAccrecordsStatusRejectionByRecordId(voucher);
                    }
                }
                //签收方签收成功处理
                if (AccebillConstant.AB_SGNUPMK_Y.equals(info.getSignUpFlag())) {
                    if (AccebillConstant.BRIEF_STATUS_9002.equals(info.getBriefId())) {
                        billAcceptanceRequestService.updateAccrecordsStatusByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9003.equals(info.getBriefId())) {
                        billIssuanceRequestService.updateAccrecordsStatusByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9010.equals(info.getBriefId())) {
                        billEndorseRequestService.updateAccrecordsStatusByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9012.equals(info.getBriefId())) {
                        billRepurchaseDiscountRedemptionService.updateAccrecordsStatusByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9017.equals(info.getBriefId())) {
                        /*if (AccebillConstant.AB_BAIL_TYPE_ADD.equals(info.getBailType())) {
                            billOpenGuaranteeService.updateAccrecordsStatusReplyByRecordId(voucher);
                        } else if (AccebillConstant.AB_BAIL_TYPE_ACCEPT.equals(info.getBailType())) {
                            billAcceptGuaranteeService.updateAccrecordsStatusReplyByRecordId(voucher);
                        } else if (AccebillConstant.AB_BAIL_TYPE_RECITE.equals(info.getBailType())) {
                            billEndorseGuaranteeService.updateAccrecordsStatusByRecordId(voucher);
                        }*/
                        billEndorseGuaranteeService.updateAccrecordsStatusByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9018.equals(info.getBriefId())) {
                        billCollateralizationRequestService.updateAccrecordsStatusByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9019.equals(info.getBriefId())) {
                        billRepurchasedCollateralizationRequestService.updateAccrecordsStatusByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9020.equals(info.getBriefId())) {
                        billPresentationRequestService.updateAccrecordsStatusByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9021.equals(info.getBriefId())) {
                        billOverduePresentationRequestService.updateAccrecordsStatusByRecordId(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_9023.equals(info.getBriefId())) {
                        billApprovePaymentService.updateAccrecordsStatusByRecordId(voucher);
                    }else if (AccebillConstant.BRIEF_STATUS_9024.equals(info.getBriefId())) {
                        nonTransCancelService.updateAccrecordsStatusByRecordId(voucher);
                    }
                }
            }
            if (!info.getBriefId().startsWith("9")) {
                AbBillReceivebiz abBillReceivebiz = abBillReceivebizService.getAbBillReceivebizByMsgId(info.getId());
//                BtBankAccDTO bankAccDTO = iBtBankAccService.getBankAccInfo(abBillReceivebiz.getRecAcctId());
//                InputBankInfoDTO bankInfoDTO = abInputBankInfoService.getBtInputBankInfo(bankAccDTO.getBankCode());
////                abBillReceivebiz.setRecAcctTypeName(bankInfoDTO.getBankTypeName());
                //发起方拒收时处理
                if (abBillReceivebiz != null) {
                    if (AccebillConstant.AB_SGNUPMK_N.equals(abBillReceivebiz.getSgnupmk())) {
                        if (AccebillConstant.BRIEF_STATUS_002.equals(info.getBriefId())) {
                            voucher.setSignUpFlag(AccebillConstant.AB_SGNUPMK_N);
                            billAcceptanceRequestService.updateVoucherInfoRejection(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_003.equals(info.getBriefId())) {
                            billIssuanceRequestService.updateVoucherInfoRejection(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_010.equals(info.getBriefId())) {
                            billEndorseRequestService.updateVoucherInfoRejection(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_017.equals(info.getBriefId())) {
                            if (AccebillConstant.AB_BAIL_TYPE_ADD.equals(info.getBailType())) {
                                billOpenGuaranteeService.updateAccrecordsStatusRejection(voucher);
                            } else if (AccebillConstant.AB_BAIL_TYPE_ACCEPT.equals(info.getBailType())) {
                                billAcceptGuaranteeService.updateAccrecordsStatusRejection(voucher);
                            } else if (AccebillConstant.AB_BAIL_TYPE_RECITE.equals(info.getBailType())) {
                                billEndorseGuaranteeService.updateVoucherInfoRejection(voucher);
                            }
                        } else if (AccebillConstant.BRIEF_STATUS_018.equals(info.getBriefId())) {
                            billCollateralizationRequestService.updateVoucherInfoRejection(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_019.equals(info.getBriefId())) {
                            billRepurchasedCollateralizationRequestService.updateVoucherInfoRejection(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_020.equals(info.getBriefId())) {
                            billPresentationRequestService.updateVoucherInfoRejection(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_021.equals(info.getBriefId())) {
                            billOverduePresentationRequestService.updateVoucherInfoRejection(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_023.equals(info.getBriefId())) {
                            billApprovePaymentService.updateVoucherInfoRejection(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_032.equals(info.getBriefId())) {
                            billCommonRevokeRequestService.updateAccrecordsStatusRejectionByRecordId(voucher);
                        }else if (AccebillConstant.BRIEF_STATUS_024.equals(info.getBriefId())) {
                            voucher.setSignUpFlag(AccebillConstant.AB_SGNUPMK_N);
                            nonTransCancelService.updateVoucherInfoEndIsSend(voucher);
                        }
                    }
                    //发起方签收成功处理
                    if (null == abBillReceivebiz.getSgnupmk() || AccebillConstant.AB_SGNUPMK_Y.equals(abBillReceivebiz.getSgnupmk())) {
                        if (AccebillConstant.BRIEF_STATUS_002.equals(info.getBriefId())) {
                            voucher.setSignUpFlag(AccebillConstant.AB_SGNUPMK_Y);
                            billAcceptanceRequestService.updateVoucherInfoEnd(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_003.equals(info.getBriefId())) {
                            billIssuanceRequestService.updateVoucherInfoEnd(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_004.equals(info.getBriefId())) {
                            billDestructionRequestService.updateVoucherInfoEnd(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_010.equals(info.getBriefId())) {
                            billEndorseRequestService.updateVoucherInfoEnd(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_017.equals(info.getBriefId())) {
                            if (AccebillConstant.AB_BAIL_TYPE_ADD.equals(info.getBailType())) {
                                billOpenGuaranteeService.updateAccrecordsStatusEndByRecordId(voucher);
                            } else if (AccebillConstant.AB_BAIL_TYPE_ACCEPT.equals(info.getBailType())) {
                                billAcceptGuaranteeService.updateAccrecordsStatusEndByRecordId(voucher);
                            } else if (AccebillConstant.AB_BAIL_TYPE_RECITE.equals(info.getBailType())) {
                                billEndorseGuaranteeService.updateVoucherInfoEnd(voucher);
                            }
                        } else if (AccebillConstant.BRIEF_STATUS_018.equals(info.getBriefId())) {
                            billCollateralizationRequestService.updateVoucherInfoEnd(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_019.equals(info.getBriefId())) {
                            billRepurchasedCollateralizationRequestService.updateVoucherInfoEnd(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_020.equals(info.getBriefId())) {
                            billPresentationRequestService.updateVoucherInfoEnd(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_021.equals(info.getBriefId())) {
                            billOverduePresentationRequestService.updateVoucherInfoEnd(voucher);
                        } else if (AccebillConstant.BRIEF_STATUS_023.equals(info.getBriefId())) {
                            billApprovePaymentService.updateVoucherInfoEnd(voucher);
                        }else if (AccebillConstant.BRIEF_STATUS_024.equals(info.getBriefId())) {
                            voucher.setSignUpFlag(AccebillConstant.AB_SGNUPMK_Y);
                            nonTransCancelService.updateVoucherInfoEndIsSend(voucher);
                        }
                    }
                }
                else {
                    //签收表没数据时
                    if (AccebillConstant.BRIEF_STATUS_001.equals(info.getBriefId())) {
                        //abVoucherinfoService.updateBillStatusOrOrigNo(info.getVid(),AccebillConstant.AB_BILL_STATUS_010004,String.valueOf(new Date().getTime()));
                        BillAbVoucherinfo voucherinfo = billBankDao.getVoucherInfoByVoucherId(voucher.getVoucherId());
                        if(voucherinfo != null && "1".equals(voucherinfo.getNewStmIssuofpprInd())){
                            if(voucherinfo.getBillType() == 10){//银票
                                voucher.setOrigNo("5"+new Date().getTime());
                            }else {//商票
                                voucher.setOrigNo("6"+new Date().getTime());
                            }
                        }else {
                            voucher.setOrigNo(String.valueOf(new Date().getTime()));
                        }
                        billBankService.updateVoucherInfo(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_002.equals(info.getBriefId())) {
                        voucher.setSignUpFlag(AccebillConstant.AB_SGNUPMK_Y);
                        billAcceptanceRequestService.updateVoucherInfoEnd(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_003.equals(info.getBriefId())) {
                        billIssuanceRequestService.updateVoucherInfoEnd(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_004.equals(info.getBriefId())) {
                        //退票发起方回应成功处理
                        billDestructionRequestService.updateVoucherInfoEnd(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_010.equals(info.getBriefId())) {
                        billEndorseRequestService.updateVoucherInfoEnd(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_018.equals(info.getBriefId())) {
                        billCollateralizationRequestService.updateVoucherInfoEnd(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_019.equals(info.getBriefId())) {
                        billRepurchasedCollateralizationRequestService.updateVoucherInfoEnd(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_020.equals(info.getBriefId())) {
                        billPresentationRequestService.updateVoucherInfoEnd(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_021.equals(info.getBriefId())) {
                        billOverduePresentationRequestService.updateVoucherInfoEnd(voucher);
                    }else if (AccebillConstant.BRIEF_STATUS_024.equals(info.getBriefId())) {
                        nonTransCancelService.updateVoucherInfoEndIsSend(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_011.equals(info.getBriefId())) {
                        //贴现发起方回应成功处理
                        billDiscountRequestService.updateVoucherInfoEnd(voucher);
                        List<AbDiscount> discountlist = abDiscountService.listAbDiscountinfoByRecordId(info.getId());
                        if (discountlist != null && !discountlist.isEmpty()) {
                            AbDiscount discount = discountlist.get(0);
                            if (AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK.equals(discount.getRpdmk())) {
//                                AbVoucherinfo vouInfo = abVoucherinfoService.get(info.getVid());
                                BillSign billSignInfo = setBillSign(vouInfo, info, userVo);
                                BillSignVo billSign = new BillSignVo();
                                BeanUtil.copyProperties(billSignInfo, billSign);
                                billSign.setRpdmk(discount.getRpdmk());
                                billSign.setAoaccninfId(discount.getAoaccninfId());
                                billSign.setAoaccninfAc(discount.getAoaccninfAc());
                                billSign.setRpdopendt(discount.getRpdopendt());
                                billSign.setRpdduedt(discount.getRpdduedt());
                                billSign.setRpdintrstrate(discount.getInterestRates());
                                billSign.setRpdamt(discount.getRealMoney());
                                billSign.setBuyerInterestSign(discount.getBuyerinterestSing() == null ? null : Integer.valueOf(discount.getBuyerinterestSing()));
                                billSign.setMsgtype(AccebillConstant.AB_MSGTYPE_DISCOUNT_REDEM);
                                billSign.setBillStatus(AccebillConstant.AB_BILL_STATUS_120001);
                                billDiscountRequestService.saveBillSign(billSign);
                            }
                        }
                    } else if (AccebillConstant.BRIEF_STATUS_022.equals(info.getBriefId())) {
                        //追索业务成功以后需要修改前一笔流水为可用（可以继续追索其他人）
                        //int repNum=abAccrecordsService.updatAppPass(info.getLastid(), false);
                        voucher.setEndDate(null);
                        billRecourseRequestService.updateVoucherInfoEnd(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_032.equals(info.getBriefId())) {//撤回业务回滚状态
                        billCommonRevokeRequestService.updateVoucherInfoEnd(voucher);
                    } else if (AccebillConstant.BRIEF_STATUS_017.equals(info.getBriefId())) {
                        if (AccebillConstant.AB_BAIL_TYPE_ADD.equals(info.getBailType())) {
                            billOpenGuaranteeService.updateAccrecordsStatusEndByRecordId(voucher);
                        } else if (AccebillConstant.AB_BAIL_TYPE_ACCEPT.equals(info.getBailType())) {
                            billAcceptGuaranteeService.updateAccrecordsStatusEndByRecordId(voucher);
                        } else if (AccebillConstant.AB_BAIL_TYPE_RECITE.equals(info.getBailType())) {
                            billEndorseGuaranteeService.updateVoucherInfoEnd(voucher);
                        }
                    } else if (AccebillConstant.AB_POOL_BRIEF_8001.equals(info.getBriefId())) {
                        PoolEmitFinaApplyVO paramsVo = new PoolEmitFinaApplyVO();
                        paramsVo.setBusId(recordId);
                        paramsVo.setOptStatus(String.valueOf(status));
                        paramsVo.setReCode(returnCode);
                        paramsVo.setReMsg(returnMsg);
                        billPoolEmitFinaApplyService.updatePoolEmitFinaApply(paramsVo);
                        //完成以后写入一笔合同数据
                        PoolEmitFinaConVO conVo = new PoolEmitFinaConVO();
                        conVo.setBusId(recordId);
                        conVo.setConName("首次测试合同.txt");
                        conVo.setConInf(Base64Util.encodeToString("首次测试合同,测试合同信息内容".getBytes()));
                        billPoolEmitFinaApplyService.savePoolEmitFinaCon(conVo);
                    } else if (AccebillConstant.AB_POOL_BRIEF_8018.equals(info.getBriefId())) {
                        PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
                        paramsVo.setBusId(recordId);
                        paramsVo.setOptStatus(String.valueOf(status));
                        paramsVo.setReCode(returnCode);
                        paramsVo.setReMsg(returnMsg);
                        billPoolIntoApplyService.updatePoolIntoApply(paramsVo);
                    } else if (AccebillConstant.AB_POOL_BRIEF_8818.equals(info.getBriefId())) {
                        PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
                        paramsVo.setBusId(recordId);
                        paramsVo.setOptStatus(String.valueOf(status));
                        paramsVo.setReCode(returnCode);
                        paramsVo.setReMsg(returnMsg);
                        billPoolIntoApplyService.updatePoolIntoPledge(paramsVo);
                    } else if (AccebillConstant.AB_POOL_BRIEF_8019.equals(info.getBriefId())) {
                        PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
                        paramsVo.setBusId(recordId);
                        paramsVo.setOptStatus(String.valueOf(status));
                        paramsVo.setReCode(returnCode);
                        paramsVo.setReMsg(returnMsg);
                        billPoolOutApplyService.updatePoolOutApply(paramsVo);
                    } else if (AccebillConstant.AB_POOL_BRIEF_8118.equals(info.getBriefId())
                            || AccebillConstant.AB_POOL_BRIEF_8218.equals(info.getBriefId())) {
                        PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
                        paramsVo.setBusId(recordId);
                        paramsVo.setOptStatus(String.valueOf(status));
                        paramsVo.setReCode(returnCode);
                        paramsVo.setReMsg(returnMsg);
                        if (AccebillConstant.AB_POOL_BRIEF_8118.equals(info.getBriefId())) {
                            billPoolPdTstApplyService.updatePoolRemovePledge(paramsVo);
                        } else if (AccebillConstant.AB_POOL_BRIEF_8218.equals(info.getBriefId())) {
                            billPoolPdTstApplyService.updatePoolPdTstPledge(paramsVo);
                        }
                    } else if (AccebillConstant.AB_POOL_BRIEF_8032.equals(info.getBriefId())) {
                        PoolBillVoucherVO paramsVo = new PoolBillVoucherVO();
                        paramsVo.setBusId(recordId);
                        paramsVo.setOptStatus(String.valueOf(status));
                        paramsVo.setReCode(returnCode);
                        paramsVo.setReMsg(returnMsg);
                        billPoolIntoRepealService.updatePoolIntoRepeal(paramsVo);
                    }
                }
            }
        }
    }
    /**
     * 业务拒签回应处理测试
     */
    public void succeNOTest(List<AbAccrecords> accrecordsList) {
        for (AbAccrecords info : accrecordsList) {
            AbVoucherinfo vouInfo = abVoucherinfoService.get(info.getVid(),true);
            if(StrUtil.isEmpty(vouInfo.getSbbllRngStrtSn().trim())){
                throw new BusinessException("子票区间不允许为空！");
            }
            VoucherInfoVo voucher = new VoucherInfoVo();
            voucher.setVoucherId(info.getVid());
            voucher.setRecordId(info.getId());
            voucher.setReturnCode("S");
            voucher.setReturnMsg("交易成功");
            if (!info.getBriefId().startsWith("9")) {
                if (AccebillConstant.BRIEF_STATUS_002.equals(info.getBriefId())) {
                    billAcceptanceRequestService.updateVoucherInfoRejection(voucher);
                } else if (AccebillConstant.BRIEF_STATUS_003.equals(info.getBriefId())) {
                    billIssuanceRequestService.updateVoucherInfoRejection(voucher);
                } else if (AccebillConstant.BRIEF_STATUS_010.equals(info.getBriefId())) {
                    billEndorseRequestService.updateVoucherInfoRejection(voucher);
                } else if (AccebillConstant.BRIEF_STATUS_011.equals(info.getBriefId())) {
                    //贴现发起方回应拒收处理
                    billDiscountRequestService.updateVoucherInfoRejection(voucher);
                } else if (AccebillConstant.BRIEF_STATUS_017.equals(info.getBriefId())) {
                    if (AccebillConstant.AB_BAIL_TYPE_ADD.equals(info.getBailType())) {
                        billOpenGuaranteeService.updateAccrecordsStatusRejection(voucher);
                    } else if (AccebillConstant.AB_BAIL_TYPE_ACCEPT.equals(info.getBailType())) {
                        billAcceptGuaranteeService.updateAccrecordsStatusRejection(voucher);
                    } else if (AccebillConstant.AB_BAIL_TYPE_RECITE.equals(info.getBailType())) {
                        billEndorseGuaranteeService.updateVoucherInfoRejection(voucher);
                    }
                } else if (AccebillConstant.BRIEF_STATUS_018.equals(info.getBriefId())) {
                    billCollateralizationRequestService.updateVoucherInfoRejection(voucher);
                } else if (AccebillConstant.BRIEF_STATUS_019.equals(info.getBriefId())) {
                    billRepurchasedCollateralizationRequestService.updateVoucherInfoRejection(voucher);
                } else if (AccebillConstant.BRIEF_STATUS_020.equals(info.getBriefId())) {
                    billPresentationRequestService.updateVoucherInfoRejection(voucher);
                } else if (AccebillConstant.BRIEF_STATUS_021.equals(info.getBriefId())) {
                    billOverduePresentationRequestService.updateVoucherInfoRejection(voucher);
                } else if (AccebillConstant.BRIEF_STATUS_022.equals(info.getBriefId())) {
                    //追索业务成功以后需要修改前一笔流水为可用（可以继续追索其他人）
                    //int repNum=abAccrecordsService.updatAppPass(info.getLastid(), false);
                    billRecourseRequestService.updateAccrecordsStatusRejectionByRecordId(voucher);
                } else if (AccebillConstant.BRIEF_STATUS_023.equals(info.getBriefId())) {
                    billApprovePaymentService.updateVoucherInfoRejection(voucher);
                }else if (AccebillConstant.BRIEF_STATUS_024.equals(info.getBriefId())) {
                    nonTransCancelService.updateVoucherInfoRejection(voucher);
                }
            }
        }
    }
    public BillSign setBillSign(AbVoucherinfo vouInfo, AbAccrecords info, ICurrentUser userVo) {
        BillSign billSign = new BillSign();
        billSign.setMsgid(info.getId());
        billSign.setOrigNo(vouInfo.getOrigNo());
        BeanUtils.copyNotNullProperties(vouInfo, billSign, "id");
        billSign.setBillType(vouInfo.getBillType());
        billSign.setStartDate(vouInfo.getStartDate());
        billSign.setEndDate(vouInfo.getEndDate());
        billSign.setBillMoney(vouInfo.getBillMoney());
        billSign.setPayee(vouInfo.getPayee());
        billSign.setPayeeacc(vouInfo.getPayeeacc());
        billSign.setPayeebankno(vouInfo.getPayeebankno());
        billSign.setPayer(vouInfo.getPayer());
        billSign.setPayeracc(vouInfo.getPayeracc());
        billSign.setPayerbankno(vouInfo.getPayerbankno());
        billSign.setAcceptor(vouInfo.getAcceptor());
        billSign.setAcceptoracc(vouInfo.getAcceptoracc());
        billSign.setAcceptorbankno(vouInfo.getAcceptorbankno());
        billSign.setCdtratgs(vouInfo.getPayerCdtRatgs());
        billSign.setCdtratgagcy(vouInfo.getPayerCdtRatgGgcy());
        billSign.setCdtratgduedt(vouInfo.getPayerCdtRatgDuedt());
        billSign.setBussdt(info.getApplyDate());
        billSign.setAccptncagrmtnb(vouInfo.getAccptncagrmtnb());
        billSign.setTxlctrctnb(vouInfo.getAcontractNo());
        billSign.setInvcnb(vouInfo.getAinvcnb());
        //billSign.setAcceptordate(vouInfo.getACCEPTORDATE);
        billSign.setSttlmmk(info.getSttlmmk());
        billSign.setRmk(info.getRmk());
        billSign.setBankendrsmtmk(vouInfo.getBanendrsmtmk());
        billSign.setAppldt(info.getApplyDate());
        billSign.setRcrsTp(info.getRcrsTp());
        billSign.setReqamt(info.getRcrsokmoney());
        billSign.setRcrsokmoney(info.getRcrsokmoney());
        billSign.setRcrsrsncd(info.getRcrsrsncd());
        billSign.setReqRole(info.getReqRole());
        billSign.setReqNm(info.getReqNm());
        billSign.setReqCmonid(info.getReqCmonid());
        billSign.setReqAcctId(info.getReqAcctId());
        billSign.setReqAcctsvcr(info.getReqAcctsvcr());
        billSign.setReqAcctsvcrAg(info.getReqAcctsvcrAg());
        billSign.setRecRole(info.getRecRole());
        billSign.setRecNm(info.getRecNm());
        billSign.setRecCmonid(info.getRecCmonid());
        billSign.setRecAcctId(info.getRecAcctId());
        billSign.setRecAcctsvcr(info.getRecAcctsvcr());
        billSign.setRecAcctsvcrAg(info.getRecAcctsvcrAg());
        billSign.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
        billSign.setCreateBy(userVo.getLoginName());
        billSign.setCreateName(userVo.getUsername());
        billSign.setCreateDate(new Date());
        return billSign;
    }
    /**
     * 根据银行账号获取银行账户信息
     *
     * @return
     */
    @Transactional(readOnly = true)
    public BtBankAccDTO getBtBankAccDTO(String bankAcc) {
        BtBankAccDTO bankAccDTO = null;
        BtBankAccDTO params = new BtBankAccDTO();
        params.setBankAcc(bankAcc);
        List<BtBankAccDTO> bankaccInfoList = abBtBankAccService.getBankAccList(params);
        if (bankaccInfoList != null && !bankaccInfoList.isEmpty()) {
            bankAccDTO = bankaccInfoList.get(0);
        }/*else{
			throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_REQBANKACC_ISNOTNULL);
		}*/
        return bankAccDTO;
    }
    /**
     * 指令批量发送操作
     *
     * @param instructionVo
     */
    public void batchSend(AbBillInstructionManageVO instructionVo, ICurrentUser userVo) {
        String[] ids = StringUtils.split(instructionVo.getIds(), ",");
        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        List<AbAccrecords> accrecordsList = abAccrecordsService.listAbAccrecordsByIds(ids);
        AbAccrecordsVO accrecordsVO = new AbAccrecordsVO();
        accrecordsVO.setIdArray(ids);
        accrecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS16);
        accrecordsVO.setReturnCode(AccebillConstant.AB_BILL_RETURN_CODE_3);
        int num = abAccrecordsService.batchUpdate(accrecordsVO, userVo);
        if (accrecordsList.size() != num) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        accrecordsVO = new AbAccrecordsVO();
        for (AbAccrecords info : accrecordsList) {
            accrecordsVO.setIdArray(new String[]{info.getId()});
            accrecordsVO.setMsgId(AbAccebillUtils.getMsgId());
            //对待发送的生成子票区间
            if(StringUtils.isNotEmpty(info.getOldAccId())) {
                billTicketSplittingUtils.setOnLineSplitSbbllRng(info.getOldAccId());
            }
            abAccrecordsService.batchUpdate(accrecordsVO, userVo);
            abBillNotificationService.completeMsg(info, AccebillConstant.BRIEF_STATUS_4000, userVo);
            abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(),
                    AccebillConstant.ABLOG_LOGTYPE_INSSEND, AccebillConstant.LANG_LOG_BATCHINSSEND_SUCC, userVo);
        }
    }
    /**
     * 指令批量打回操作
     *
     * @param instructionVo
     */
    public void batchRepulse(AbBillInstructionManageVO instructionVo, ICurrentUser userVo) {
        String[] ids = StringUtils.split(instructionVo.getIds(), ",");
        if (StringUtils.isEmpty(ids)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        List<AbAccrecords> accrecordsList = abAccrecordsService.listAbAccrecordsByIds(ids);
        AbAccrecordsVO accrecordsVO = new AbAccrecordsVO();
//        accrecordsVO.setIdArray(ids);
        accrecordsVO.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);
        accrecordsVO.setStatusNew(AccebillConstant.AB_BILL_BUS_STATUS_2);
        accrecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS_2);
//必须单笔单笔修改
//        int num = abAccrecordsService.batchUpdate(accrecordsVO, userVo);
//        if (accrecordsList.size() != num) {
//            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
//        }
        for (int i = 0; i < accrecordsList.size(); i++) {
            AbAccrecords info = accrecordsList.get(i);
            accrecordsVO.setIdArray(new String[]{info.getId()});
            int num = abAccrecordsService.batchUpdate(accrecordsVO, userVo);
            if (1 != num) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            if (info.getBriefId().startsWith("9")//签收业务需要回滚签收表为打回
                    || AccebillConstant.BRIEF_STATUS_023.equals(info.getBriefId())) {//同意清偿申请业务需要回滚签收表为打回
                AbBillReceivebizVO billReceivebizVo = new AbBillReceivebizVO();
                billReceivebizVo.setId(info.getRecid());
                billReceivebizVo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE);
                int reNum = abBillReceivebizService.updateAbBillReceivebiz(billReceivebizVo, userVo);
                if (reNum != 1) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
                if (reNum != 1) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
            } else if (AccebillConstant.BRIEF_STATUS_001.equals(info.getBriefId())) {//开票登记打回处理
                abRatUrationService.opRatUration(info, AccebillConstant.BILL_INSTRUCTION_OPTBACK, userVo);
                // 挂接担保
                AbEmitBillVo abEmitBillVo = abEmitBillService.getAbEmitBearVoByAid(info.getId());
                abGuaranteeService.bearContractLink(AccebillConstant.BILL_OPERATE_BACK, abEmitBillVo, userVo);
                //开票申请失败打回设置批次为空银行不支持多次提交批次号
                abVoucherinfoViceService.updateBatchNoIsNull(info.getVvid(), userVo);
            } else {//设置签收类型的不需要此处理
                if (StringUtils.isNotEmpty(info.getVvid())) {//设置从表打回业务类型
                    int reNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), info.getBriefId(), info.getId());
                    if (reNum != 1) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                }
                if (StringUtils.isNotEmpty(info.getVid())) {//修改票据为可用
                    @SuppressWarnings("unused")
                    int reNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
//					if(reNum!=1){
//						throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
//					}
                }
                AbAccrecords abAccrecords = abAccrecordsService.get(info.getLastid(), true);
                if (AccebillConstant.BRIEF_STATUS_6201.equals(abAccrecords.getBriefId())
                        || AccebillConstant.BRIEF_STATUS_6202.equals(abAccrecords.getBriefId())
                        || AccebillConstant.BRIEF_STATUS_6204.equals(abAccrecords.getBriefId())) {
//                    lastHoldRecordsVO.setIdArray(new String[]{abAccrecords.getLastid()});
                    if (abAccrecordsService.updatAppPass(abAccrecords.getLastid(), false, userVo) != 1) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                    //设置下拨归集和上划失败
                    AbAccrecordsVO lastHoldRecordsVO=new AbAccrecordsVO();
                    lastHoldRecordsVO.setIdArray(new String[]{abAccrecords.getId()});
                    lastHoldRecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS_2);
                    lastHoldRecordsVO.setStatusNew(AccebillConstant.AB_BILL_BUS_STATUS_2);
                    lastHoldRecordsVO.setReturnMsg("背书失败退回");
                    lastHoldRecordsVO.setUpdateDate(new Date());
                    abAccrecordsService.batchUpdate(lastHoldRecordsVO, userVo);
                } else {
                    int repNum = abAccrecordsService.updatAppPass(info.getLastid(), false, userVo);
                    if (repNum != 1) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                }
                abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
//                lastHoldRecordsVO.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
//                lastHoldRecordsVO.setUpdateDate(new Date());
//                abAccrecordsService.batchUpdate(lastHoldRecordsVO, userVo);
//                abAccrecords = abAccrecordsService.get(info.getLastid(),true);
                //abAccrecords.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
//                abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
//                abAccrecords.setUpdateDate(new Date());
//                abAccrecords.setUpdateBy(userVo.getLoginName());
//                abAccrecords.setUpdateName(userVo.getLoginName());
//                abAccrecordsService.saveOrUpdate(abAccrecords);
            }

            if("1".equals(instructionVo.getMergeOpt())){
                if (!info.getBriefId().startsWith("9") && !AccebillConstant.BRIEF_STATUS_023.equals(info.getBriefId())) {//签收业务和追索同意清偿业务不做合并操作
//                    AbAccrecords oldAcc = abAccrecordsService.get(info.getId());
                    if("1".equals(info.getSplitFlg())){
                        //批量付票业务进行处理
                        if("2".equals(info.getCopyFlag())){
                            billTicketSplittingUtils.splitRepulseOp(info, instructionVo.getMergeOpt());
                        }else {
                            //新票据拆分后打回处理
                            NewDraftDealMethod.draftMerge("1", info.getVid(), billBankDao, billAccrecordsDao);
                        }
                    }
                }
                //同意清偿打回处理
                if(AccebillConstant.BRIEF_STATUS_023.equals(info.getBriefId())){
                    String recId = NewDraftDealMethod.agreeSettleRepulse(billSignDao,info.getRecid(),18,info.getId(),billAccrecordsDao);
                    info.setRecid(recId);
                }
            }else if("0".equals(instructionVo.getMergeOpt())){
                //批量付票业务进行处理
                if("2".equals(info.getCopyFlag())){
                    billTicketSplittingUtils.splitRepulseOp(info, instructionVo.getMergeOpt());
                } else {
                    //新票据拆分后打回处理
                    NewDraftDealMethod.draftMerge("0", info.getVid(), billBankDao, billAccrecordsDao);
                }
            }
            abBillNotificationService.completeMsg(info, AccebillConstant.BRIEF_STATUS_4000, userVo);
            abBillNotificationService.excRepulseSendMsg(info, info.getCreateBy());
            if (AccebillConstant.BRIEF_STATUS_011.equals(info.getBriefId())) {
                //上一笔贴现申请流水变为隐藏
                AbAccrecords lastInfo = abAccrecordsService.get(info.getLastid(), true);
                lastInfo.setIsHide(AccebillConstant.AB_IS_HIDE_N);
                abAccrecordsService.update(lastInfo, userVo);
            }
            abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(),
                    AccebillConstant.ABLOG_LOGTYPE_INSREPULSE, AccebillConstant.LANG_LOG_BATCHINSREPULSE_SUCC, userVo);
        }

        //如果是不可转让撤销申请回退后，将前一笔数据设置为有效
       /* for(AbAccrecords ad : accrecordsList){
            ad.setId(ad.getLastid());
            ad.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
            ad.setUpdateDate(new Date());
            ad.setUpdateBy(userVo.getLoginName());
            ad.setUpdateName(userVo.getLoginName());
        }
        abAccrecordsService.batchSaveOrUpdate(accrecordsList);*/
    }
}
