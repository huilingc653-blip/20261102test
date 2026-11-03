package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillDiscountAccountDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountVO;
import com.bytter.bill.domain.bill.vo.AbBillDiscountManagerVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.ICorpService;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description:贴现到账Service
 * @date 2017-7-21 13:50:34
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTACCOUNT)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTACCOUNT)
@Service("abBillDiscountAccountService")
@Transactional
public class AbBillDiscountAccountService extends BaseService<AbBillDiscountAccountVO, String>
        implements IAccebillAccbookService, IWorkFlowListener {
    @Autowired
    private AbBillDiscountAccountDao abBillDiscountAccountDao;
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
    private AbAccbookService abAccbookService;
    @Autowired
    private AbBillNotificationService abBillNotificationService;
    @Autowired
    private AbDiscountService abDiscountService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Autowired
    private ICorpService corpService;
    @Override
    public BaseDao<AbBillDiscountAccountVO, String> getEntityDao() {
        return abBillDiscountAccountDao;
    }
    /**
     * 列表显示贴现申请信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillDiscountAccount(DataTablesInput input, String isApply) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTACCOUNT_QUERY, input.getCurrentUserId());
        String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(input, AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTACCOUNT_QUERY, corpIds);
        DataTablesOutput<AbBillDiscountAccountVO> dataTablesOutput = abBillDiscountAccountDao.listAbBillDiscountAccount(input, corpAuthList, isApply);
        Set<String> corpIdSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG"
                    , "AB_ABBILLDISCOUNT_STATUS", "AB_BRIEF_STATUS", "AB_ABBILLDISCOUNT_RELEASE_STATUS", "AB_ENTER_STOCK", "AB_BANEM"
                    , "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            for (AbBillDiscountAccountVO bill : listData) {
                injectValues(bill, listDictMap);
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
            for (AbBillDiscountAccountVO bill : listData){
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
    private void injectValues(AbBillDiscountAccountVO bill, Map<String, Map<String, DictDTO>> listDictMap) {
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
        Optional.ofNullable(bill.getBriefId()).ifPresent(briefId -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BRIEF_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(briefId)))
                    .ifPresent(dict -> bill.setBriefIdName(dict.getName()));
        });
        // 票据业务状态
        Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ABBILLDISCOUNT_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
                    .ifPresent(dict -> bill.setStatusName(dict.getName()));
        });
        // 商票贴现是否释放状态
        Optional.ofNullable(bill.getReleaseState()).ifPresent(releaseState -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ABBILLDISCOUNT_RELEASE_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(releaseState)))
                    .ifPresent(dict -> bill.setReleaseStateName(dict.getName()));
        });
        // 所属库存
        Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
                    .ifPresent(dict -> bill.setEnterStockName(dict.getName()));
        });
        // 不得转让标记
        Optional.ofNullable(bill.getBanendrsmtmk()).ifPresent(banendrsmtmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BANEM");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(banendrsmtmk)))
                    .ifPresent(dict -> bill.setBanendrsmtmkName(dict.getName()));
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
    }
    /**
     * 批量提交贴现申请
     *
     * @param abBillDiscountAccountVO
     * @return
     */
    public boolean batchSubmitAbBillDiscountAccount(AbBillDiscountAccountVO abBillDiscountAccountVO, ICurrentUser userVo) {
        String accIds = abBillDiscountAccountVO.getAccIds();
        //实付金额
        String realMoneys = abBillDiscountAccountVO.getRealMoneys();
        // 付款人信息
        String signeroks = abBillDiscountAccountVO.getSigneroks();
        if (StringUtils.isEmpty(accIds)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String[] signerokArrays = signeroks.split(",");
        String[] realMoney = realMoneys.split(",");
        String[] accIdArrays = accIds.split(",");
        String[] viceIdArrays = new String[accIdArrays.length];
        List<AbAccrecords> newList = new ArrayList<AbAccrecords>();
        List<AbDiscount> discountList = new ArrayList<AbDiscount>();
        for (int i = 0; i < accIdArrays.length && i < signerokArrays.length; i++) {
            // 上一笔流水信息
            AbAccrecords accRecordsLast = abAccrecordsService.get(accIdArrays[i]);
            // 主信息表
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accRecordsLast.getVid());
            // 从表
            AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecordsLast.getVvid());
            viceIdArrays[i] = accRecordsLast.getVvid();
            // 生成贴现到账流水
            AbAccrecords accRecordsNew = new AbAccrecords();
            accRecordsNew.setVid(vouInfo.getId());
            accRecordsNew.setVvid(viceInfo.getId());
            accRecordsNew.setLastid(accRecordsLast.getId());// 上一笔流水id
            accRecordsNew.setLastbillcode(vouInfo.getBillStatus());// 上一票据状态代码
            accRecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
            accRecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);// 业务处理状态
            accRecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_6012);// 业务类型
            accRecordsNew.setApplyDate(new Date());// 申请日期
            accRecordsNew.setBusinessDate(abBillDiscountAccountVO.getBusinessDate());// 业务办理日期
            accRecordsNew.setSignerok(signerokArrays[i]);// 收款人
            accRecordsNew.setBillFlag(vouInfo.getBillFlag());
            //accRecordsNew.setReqAcctId(viceInfo.getBankAcc());// 业务发起人账号
            accRecordsNew.setReqAcctId(accRecordsLast.getRecAcctId());// 业务发起人账号
            //业务接收人信息
            accRecordsNew.setRecAcctId(accRecordsLast.getRecAcctId());
            accRecordsNew.setRecAcctsvcr(accRecordsLast.getRecAcctsvcr());
            accRecordsNew.setRecNm(accRecordsLast.getRecNm());
            accRecordsNew.setRecRole(accRecordsLast.getRecRole());
            accRecordsNew.setInsideSing(accRecordsLast.getInsideSing());// 是否内部业务标识
            newList.add(accRecordsNew);
            // 生成贴现表
            AbDiscount discountNew = new AbDiscount();
            // 上一笔贴现信息
            AbDiscount discountLast = abDiscountService.getAbDiscountByRecordId(accRecordsLast.getId());
            discountNew.setRealMoney(discountLast.getRealMoney());
            discountNew.setBuyerinterestMoney(discountLast.getBuyerinterestMoney());
            discountNew.setInterestRates(discountLast.getInterestRates());
            discountNew.setInterest(discountLast.getInterest());
            discountNew.setRealMoney(new BigDecimal(realMoney[i]));
            discountNew.setRpdmk(discountLast.getRpdmk());
            discountNew.setSttlmmk(discountLast.getSttlmmk());
            discountList.add(discountNew);
        }
        // 批量新增流水
        int accNum = abAccrecordsService.batchSaveAccrecords(newList, userVo);
        // 修改box_status=10
        int viceNum = abVoucherinfoViceService.batchUpdateBoxStatus(viceIdArrays, AccebillConstant.AB_BOX_STATUS_N,
                userVo);
        if (accNum != accIdArrays.length || viceNum != viceIdArrays.length) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 取新增流水id
        List<String> accIdNewList = new ArrayList<String>();
        for (int i = 0; i < newList.size() && i < discountList.size(); i++) {
            AbAccrecords accRecordsNew = newList.get(i);
            accIdNewList.add(accRecordsNew.getId());
            AbDiscount discountNew = discountList.get(i);
            discountNew.setRecordId(accRecordsNew.getId());
        }
        String[] accIdNewArrs = new String[accIdNewList.size()];
        accIdNewList.toArray(accIdNewArrs);
        // 批量写入贴现信息
        abDiscountService.batchSave(discountList);
        abLogService.batchSaveAbLog(viceIdArrays, accIdNewArrs, AccebillConstant.BRIEF_STATUS_6012,
                AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
        // 提交审批
        for (int i = 0; i < accIdNewArrs.length; i++) {
            AbBillDiscountAccountVO variables = new AbBillDiscountAccountVO();
            AbAccrecords accRecords = abAccrecordsService.get(accIdNewArrs[i], true);
            abBillNotificationService.completeMsg(accRecords, userVo);
            // 修改从表back_brief_id为""
            abVoucherinfoViceService.updateBackBriefIdIsNull(accRecords.getVvid());
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accRecords.getVid(), true);
            AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecords.getVvid(), true);
            BeanUtils.copyProperties(vouInfo, variables);
            BeanUtils.copyProperties(viceInfo, variables);
            BeanUtils.copyProperties(accRecords, variables);
             getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTACCOUNT, accRecords.getId(), vouInfo,
                    viceInfo.getApplyCorpId(), userVo.getId());
        }
        return true;
    }
    /**
     * 贴现到账自动审批通过（不走审批流）
     *
     * @param abBillDiscountAccountVO 贴现vo
     * @param userVo 用户vo
     * @return
     */
    public boolean autoApprovalAbBillDiscountAccount(AbBillDiscountAccountVO abBillDiscountAccountVO, ICurrentUser userVo) {
        String accIds = abBillDiscountAccountVO.getAccIds();
        //实付金额
        String realMoneys = abBillDiscountAccountVO.getRealMoneys();
        // 付款人信息
        String signeroks = abBillDiscountAccountVO.getSigneroks();
        if (StringUtils.isEmpty(accIds)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String[] signerokArrays = signeroks.split(",");
        String[] realMoney = realMoneys.split(",");
        String[] accIdArrays = accIds.split(",");
        String[] viceIdArrays = new String[accIdArrays.length];
        List<AbAccrecords> newList = new ArrayList<AbAccrecords>();
        List<AbDiscount> discountList = new ArrayList<AbDiscount>();
        for (int i = 0; i < accIdArrays.length && i < signerokArrays.length; i++) {
            // 上一笔流水信息
            AbAccrecords accRecordsLast = abAccrecordsService.get(accIdArrays[i]);
            // 主信息表
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accRecordsLast.getVid());
            // 从表
            AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecordsLast.getVvid());
            viceIdArrays[i] = accRecordsLast.getVvid();
            // 生成贴现到账流水
            AbAccrecords accRecordsNew = new AbAccrecords();
            accRecordsNew.setVid(vouInfo.getId());
            accRecordsNew.setVvid(viceInfo.getId());
            accRecordsNew.setLastid(accRecordsLast.getId());// 上一笔流水id
            accRecordsNew.setLastbillcode(vouInfo.getBillStatus());// 上一票据状态代码
            accRecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
            accRecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);// 业务处理状态
            accRecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_6012);// 业务类型
            accRecordsNew.setApplyDate(new Date());// 申请日期
            accRecordsNew.setBusinessDate(abBillDiscountAccountVO.getBusinessDate());// 业务办理日期
            accRecordsNew.setSignerok(signerokArrays[i]);// 收款人
            accRecordsNew.setBillFlag(vouInfo.getBillFlag());
            //accRecordsNew.setReqAcctId(viceInfo.getBankAcc());// 业务发起人账号
            accRecordsNew.setReqAcctId(accRecordsLast.getRecAcctId());// 业务发起人账号
            //业务接收人信息
            accRecordsNew.setRecAcctId(accRecordsLast.getRecAcctId());
            accRecordsNew.setRecAcctsvcr(accRecordsLast.getRecAcctsvcr());
            accRecordsNew.setRecNm(accRecordsLast.getRecNm());
            accRecordsNew.setRecRole(accRecordsLast.getRecRole());
            accRecordsNew.setInsideSing(accRecordsLast.getInsideSing());// 是否内部业务标识
            newList.add(accRecordsNew);
            // 生成贴现表
            AbDiscount discountNew = new AbDiscount();
            // 上一笔贴现信息
            AbDiscount discountLast = abDiscountService.getAbDiscountByRecordId(accRecordsLast.getId());
            discountNew.setRealMoney(discountLast.getRealMoney());
            discountNew.setBuyerinterestMoney(discountLast.getBuyerinterestMoney());
            discountNew.setInterestRates(discountLast.getInterestRates());
            discountNew.setInterest(discountLast.getInterest());
            discountNew.setRealMoney(new BigDecimal(realMoney[i]));
            discountNew.setRpdmk(discountLast.getRpdmk());
            discountNew.setSttlmmk(discountLast.getSttlmmk());
            discountList.add(discountNew);
        }
        // 批量新增流水
        int accNum = abAccrecordsService.batchSaveAccrecords(newList, userVo);
        // 修改box_status=10
        int viceNum = abVoucherinfoViceService.batchUpdateBoxStatus(viceIdArrays, AccebillConstant.AB_BOX_STATUS_N,
                userVo);
        if (accNum != accIdArrays.length || viceNum != viceIdArrays.length) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 取新增流水id
        List<String> accIdNewList = new ArrayList<String>();
        for (int i = 0; i < newList.size() && i < discountList.size(); i++) {
            AbAccrecords accRecordsNew = newList.get(i);
            accIdNewList.add(accRecordsNew.getId());
            AbDiscount discountNew = discountList.get(i);
            discountNew.setRecordId(accRecordsNew.getId());
        }
        String[] accIdNewArrs = new String[accIdNewList.size()];
        accIdNewList.toArray(accIdNewArrs);
        // 批量写入贴现信息
        abDiscountService.batchSave(discountList);
        abLogService.batchSaveAbLog(viceIdArrays, accIdNewArrs, AccebillConstant.BRIEF_STATUS_6012,
                AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
        // 提交审批
        for (String accIdNewArr : accIdNewArrs) {
            AbAccrecords info = abAccrecordsService.get(accIdNewArr, true);
            abBillNotificationService.completeMsg(info, userVo);
            // 修改从表back_brief_id为""
            abVoucherinfoViceService.updateBackBriefIdIsNull(info.getVvid());
            String logType = AccebillConstant.ABLOG_LOGTYPE_APP;
            String appMsg = AccebillConstant.LANG_LOG_APP_SUCC;
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
        }
        return true;
    }
    /**
     * 审批回调方法
     */
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String appMsg = workFlowResult.isPass() ? AccebillConstant.LANG_LOG_APP_SUCC : AccebillConstant.LANG_LOG_APP_BACK;
        this.approveOver(workFlowResult.getBusinessKey(), workFlowResult.isPass(), appMsg, workFlowResult.getUser());
    }
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    // 审批
    public void approveOver(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
        // 通过:主表：box_status=11，流水：status=95,上一条流水valid_sign=2,可做业务票据流入下一个可做业务中。
        // 打回:主表：box_status=11,从表：back_brief_id改为BRIEF_STATUS_6012，这条贴现到账流水status=-2(已删除)
        if (StringUtils.isEmpty(buId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbAccrecords info = abAccrecordsService.get(buId, true);
        if (info == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        if (isPassOK) {
            String logType = AccebillConstant.ABLOG_LOGTYPE_APP;
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
     *
     * @param info    票据流水信息
     * @param logType 日志类型
     * @param userVo  操作用户
     */
    @Override
    public void busOverOp(AbAccrecords info, String logType, String msg, ICurrentUser userVo) {
        Integer status = AccebillConstant.AB_BILL_BUS_STATUS95;
        int statusNum = abAccrecordsService.approve(info.getId(), status, userVo);// 修改status=95,-2
        int boxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
                userVo);// 修改boxStatus=11
        if (statusNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        if (boxStatusNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        int upLastValidSignNum = abAccrecordsService.updateValidSign(info.getLastid(),
                AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);// 修改上一流水validSign=2
        if (upLastValidSignNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
    }
    /**
     * 票据业务打回操作
     *
     * @param info    票据流水信息
     * @param logType 日志类型
     * @param userVo  操作用户
     */
    @Override
    public void busRepulseOp(AbAccrecords info, String logType, String msg, ICurrentUser userVo) {
        Integer status = AccebillConstant.AB_BILL_BUS_STATUS_2;
        int statusNum = abAccrecordsService.approve(info.getId(), status, userVo);// 修改status=95,-2
        int boxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
                userVo);// 修改boxStatus=11
        if (statusNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        if (boxStatusNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        int backNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), AccebillConstant.BRIEF_STATUS_6012,
                info.getId());// 修改back_brief_id为011
        if (backNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
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
                .getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTACCOUNT, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput<AbBillDiscountAccountVO> dp = abBillDiscountAccountDao.dataTableApprove(dataTablesInput, businessList);
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ABBILLDISCOUNT_STATUS"
                , "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_BANEM", "AB_PAY_DATE", "AB_DISCOUNT_RPDMK", "AB_STTLMMK", "AB_BILL_SEND_SIGN");
        List<AbBillDiscountAccountVO> list=dp.getData();
        Map<String, CorpDTO> corpMap = corpService.getCorpMap();
        for(AbBillDiscountAccountVO reInfo:list){
            injectValues(reInfo, listDictMap);
            if (StringUtils.isNotBlank(reInfo.getBusCorpId())){
                reInfo.setBusCorpName(corpMap.get(reInfo.getBusCorpId()) != null ? corpMap.get(reInfo.getBusCorpId()).getName(): "");
            }
            if (StringUtils.isNotBlank(reInfo.getCorpId())){
                reInfo.setCorpName(corpMap.get(reInfo.getCorpId()) != null ? corpMap.get(reInfo.getCorpId()).getName(): "");
            }
        }
        return workFlowBusiness.merge(dp);
    }
    /**
     * 加载详细审核数据
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public AbBillDiscountAccountVO getAbBillDiscountAccountVOById(String id) {
        AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
        AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
        AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accrecordsInfo.getVvid(), true);
        AbDiscount disInfo = abDiscountService.getAbDiscountByRecordId(accrecordsInfo.getId());
        AbBillDiscountAccountVO reInfo = new AbBillDiscountAccountVO();
        BeanUtils.copyNotNullProperties(vouInfo, reInfo);
        BeanUtils.copyNotNullProperties(viceInfo, reInfo);
        BeanUtils.copyNotNullProperties(disInfo, reInfo);
        BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
        reInfo.setBankTypeName(StringUtils.isNotEmpty(accrecordsInfo.getVvid())?abVoucherinfoViceService.findBankTypeNameWith(accrecordsInfo.getVvid()):null);
        // 添加冗余数据
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ABBILLDISCOUNT_STATUS"
                , "AB_BRIEF_STATUS", "AB_ABBILLDISCOUNT_RELEASE_STATUS", "AB_ENTER_STOCK", "AB_BANEM");
        injectValues(reInfo, listDictMap);
        return reInfo;
    }
    @Override
    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        return this.getAbBillDiscountAccountVOById(aid);
    }

    /**
     * 更新利息和真实金额
     *
     * @param abBillDiscountAccountVO
     */
    public void updateAbBillDiscountInterestAndRealMoney(BigDecimal totalClaimAmt,String aid) {
        AbAccrecords accrecordsInfo = abAccrecordsService.get(aid, true);
        AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
        AbDiscount disInfo = abDiscountService.getAbDiscountByRecordId(accrecordsInfo.getId());
        if(totalClaimAmt.compareTo(disInfo.getRealMoney()) != 0){
            //不一致，则需要按“票面金额-流水金额”更新票据数据的贴现利息以及贴现到账净额
            disInfo.setInterest(vouInfo.getBillMoney().subtract(totalClaimAmt));
            disInfo.setRealMoney(totalClaimAmt);
            abDiscountService.update(disInfo);
        }
    }
}
