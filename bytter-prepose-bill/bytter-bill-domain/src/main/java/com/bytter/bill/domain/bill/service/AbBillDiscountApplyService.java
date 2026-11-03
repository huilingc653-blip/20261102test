package com.bytter.bill.domain.bill.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.constant.BillWorkFlowConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbBillDiscountApplyDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.vo.AbBillDiscountApplyVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoVO;
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
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtBankService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description:贴现申请Service
 * @date 2017-07-03 11:42:27
 */
@WorkFlow(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTAPPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTAPPLY)
@Service("abBillDiscountApplyService")
@Transactional
public class AbBillDiscountApplyService extends BaseService<AbBillDiscountApplyVO, String>
        implements IAccebillBaseService, IWorkFlowListener, IUserExcelVerifyHandler<AbBillDiscountApplyVO> {
    @Autowired
    private AbBillDiscountApplyDao abBillDiscountApplyDao;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbBtBankAccService abBtBankAccService;
    @Autowired
    private AbInputBankInfoService abInputBankInfoService;
    @Autowired
    private AbBillAccBalService abBillAccBalService;
    @Autowired
    private AbBillNotificationService abBillNotificationService;
    @Override
    public BaseDao<AbBillDiscountApplyVO, String> getEntityDao() {
        return abBillDiscountApplyDao;
    }
    @Autowired
    private AbDiscountService abDiscountService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ICorpService corpService;

    @Autowired
    private BillTicketSplittingUtils billTicketSplittingUtils;

    @DubboReference
    private IBtBankService iBtBankService;

    /**
     * 列表显示贴现申请信息
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listAbBillDiscountApply(DataTablesInput input) {
        List<String> corpAuthList = abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTAPPLY_QUERY, input.getCurrentUserId());
        input.getFormdata().put(AccebillConstant.AB_CONFIG_ISNOTDISCOUNTAPPLY, abVoucherinfoViceService.isNotDiscountApply());
        String corpIds = input.getFormdata().get("ins_corpId") == null ? null : String.valueOf(input.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(input,AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTAPPLY_QUERY,corpIds);
        DataTablesOutput<AbBillDiscountApplyVO> dataTablesOutput = abBillDiscountApplyDao.listAbBillDiscountApply(input, corpAuthList);
        Set<String> corpIdSet = new HashSet<>();
        Set<String> banknameSet = new HashSet<>();
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ABBILLDISCOUNT_STATUS"
                    , "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_STTLMMK", "AB_BILL_SEND_SIGN", "AB_DISCOUNT_RPDMK"
                    , "AB_BILL_NEW_STM_ISSUOFPPR_IND", "AB_BILL_BLGRDBAGCRCL_PRMT_IND");
            for (AbBillDiscountApplyVO bill : listData) {
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
                if(StrUtil.isNotBlank(bill.getBankName()) && StrUtil.isBlank(bill.getBankTypeName())){
                    banknameSet.add(bill.getBankName());
                }
            }
        });
        if(CollectionUtil.isNotEmpty(banknameSet)){
            List<BtBankDTO> btBankDTO = iBtBankService.getByBankNames(new ArrayList<>(banknameSet));
            if(CollectionUtil.isNotEmpty(btBankDTO)){
                Map<String, String> collect = btBankDTO.stream().collect(Collectors.toMap(BtBankDTO::getBankName, BtBankDTO::getBankTypeName));
                for (AbBillDiscountApplyVO bill : dataTablesOutput.getData()){
                    if(StrUtil.isNotBlank(bill.getBankTypeName())){
                        continue;
                    }
                    bill.setBankTypeName(collect.get(bill.getBankName()));
                }
            }
        }
        if(corpIdSet.size() <= 0){
            return dataTablesOutput;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return dataTablesOutput;
        }
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            for (AbBillDiscountApplyVO bill : listData){
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
    private void injectValues(AbBillDiscountApplyVO bill,Map<String, Map<String, DictDTO>> listDictMap ){
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
        // 所属库存
        Optional.ofNullable(bill.getEnterStock()).ifPresent(enterStock -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_ENTER_STOCK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(enterStock)))
                    .ifPresent(dict -> bill.setEnterStockName(dict.getName()));
        });
        // 是否发送指令标志：0否； 1是
        Optional.ofNullable(bill.getSendSign()).ifPresent(sendSign -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_BILL_SEND_SIGN");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sendSign)))
                    .ifPresent(dict -> bill.setSendSignName(dict.getName()));
        });
        // 清算方式 SM00线上清算、SMO1线下清算
        Optional.ofNullable(bill.getSttlmmk()).ifPresent(sttlmmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_STTLMMK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(sttlmmk)))
                    .ifPresent(dict -> bill.setSttlmmkName(dict.getName()));
        });
        // 贴现种类：RM00 买断式、RM01 回购式买断式
        Optional.ofNullable(bill.getRpdmk()).ifPresent(rpdmk -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_DISCOUNT_RPDMK");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(rpdmk)))
                    .ifPresent(dict -> bill.setRpdmkName(dict.getName()));
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
     * @param abBillDiscountApplyVO
     * @return
     */
    public boolean batchSubmitAbBillDiscountApply(AbBillDiscountApplyVO abBillDiscountApplyVO, ICurrentUser userVo) {
        String accIds = abBillDiscountApplyVO.getAccIds();
        if (StringUtils.isEmpty(accIds)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        List<AbAccrecords> newList = new ArrayList<AbAccrecords>();
        //贴现明细
        List<AbDiscount> abDiscountList = abBillDiscountApplyVO.getAbDiscountList();

        // 新一代票据处理
        Map<String, String> stringStringMap = billTicketSplittingUtils.ticketSplittingNew(accIds, abBillDiscountApplyVO.getTransMoneys(),
                abBillDiscountApplyVO.getSbbllRngStrtSnSplit(),abBillDiscountApplyVO.getSbbllRngEndSnSplit(), userVo);
        abBillDiscountApplyVO.setAccIds(stringStringMap.get("accIds"));
        accIds = abBillDiscountApplyVO.getAccIds();
        String[] accIdArrays =accIds.split(",");
        String[] viceIdArrays = new String[accIdArrays.length];

        for (int i = 0; i < accIdArrays.length; i++) {
            // 上一笔流水信息
            AbAccrecords accRecordsLast = abAccrecordsService.get(accIdArrays[i],true);
            // 主信息表
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accRecordsLast.getVid(),true);
            // 从表
            AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecordsLast.getVvid(),true);
            viceIdArrays[i] = accRecordsLast.getVvid();
            // 生成贴现申请流水
            AbAccrecords accRecordsNew = new AbAccrecords();
            accRecordsNew.setVid(vouInfo.getId());
            accRecordsNew.setVvid(viceInfo.getId());
            accRecordsNew.setLastid(accRecordsLast.getId());// 上一笔流水id
            if("2".equals(accRecordsLast.getCopyFlag())) {
                accRecordsNew.setSplitFlg(accRecordsLast.getSplitFlg());
                accRecordsNew.setCopyFlag(accRecordsLast.getCopyFlag());
                accRecordsNew.setOldAccId(accRecordsLast.getOldAccId());
            }
            accRecordsNew.setLastbillcode(vouInfo.getBillStatus());// 上一票据状态代码
            accRecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
            accRecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);// 业务处理状态
            accRecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_6011);// 业务类型
            accRecordsNew.setApplyDate(new Date());// 申请日期
            accRecordsNew.setBillFlag(vouInfo.getBillFlag());
            accRecordsNew.setBusinessDate(abBillDiscountApplyVO.getBusinessDate());// 业务办理日期
            accRecordsNew.setInsideSing(abBillDiscountApplyVO.getInsideSing());
             // 是否内部业务标识
            accRecordsNew.setReqAcctId(viceInfo.getBankAcc()); // 贴出人账号
            accRecordsNew.setRecNm(abBillDiscountApplyVO.getRecNm());// 贴入人名称
            accRecordsNew.setRecAcctsvcr(abBillDiscountApplyVO.getRecAcctsvcr());// 贴入人行号
            accRecordsNew.setRecAcctId(abBillDiscountApplyVO.getRecAcctId());// 贴入人账号
            accRecordsNew.setVouInfo(vouInfo);
            accRecordsNew.setVouViceInfo(viceInfo);
            accRecordsNew.setRmk(abBillDiscountApplyVO.getRmk());
            accRecordsNew.setSttlmmk(abBillDiscountApplyVO.getSttlmmk());//清算方式
            newList.add(accRecordsNew);
        }
        // 修改box_status=10
        int viceNum = abVoucherinfoViceService.batchUpdateBoxStatus(viceIdArrays, AccebillConstant.AB_BOX_STATUS_N,
                userVo);
        if (viceNum != viceIdArrays.length) {
            logger.error("应该更新数："+viceIdArrays.length);
            logger.error("实际更新数："+viceNum);
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 批量新增流水
        int accNum = abAccrecordsService.batchSaveAccrecords(newList, userVo);
        if(accNum != accIdArrays.length){
            logger.error("应该增加流水数："+accIdArrays.length);
            logger.error("实际新增流水数："+accNum);
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 取新增流水id
        List<String> accIdNewList = new ArrayList<String>();
        for (int i = 0; i < newList.size(); i++) {
            AbAccrecords accRecordsNew = newList.get(i);
            accIdNewList.add(accRecordsNew.getId());
            AbDiscount abDis = abDiscountList.get(i);
            abDis.setRecordId(accRecordsNew.getId());
            abDiscountService.save(abDis);
        }
        String[] accIdNewArrs = new String[accIdNewList.size()];
        accIdNewList.toArray(accIdNewArrs);
        abLogService.batchSaveAbLog(viceIdArrays, accIdNewArrs, AccebillConstant.BRIEF_STATUS_6011,
                AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
        // 提交审批
        for (int i = 0; i < accIdNewArrs.length; i++) {
            // 生成余额
            abBillAccBalService.setBankAccBillBal(accIdNewArrs[i], userVo);
            AbBillDiscountApplyVO variables = new AbBillDiscountApplyVO();
            AbAccrecords accRecords = abAccrecordsService.get(accIdNewArrs[i], true);
            abBillNotificationService.completeMsg(accRecords, userVo);
            // 修改从表back_brief_id为""
            abVoucherinfoViceService.updateBackBriefIdIsNull(accRecords.getVvid());
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accRecords.getVid(), true);
            AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecords.getVvid(), true);
            AbDiscount discount = abDiscountService.getAbDiscountByRecordId(accRecords.getId());
            BeanUtils.copyProperties(vouInfo, variables);
            BeanUtils.copyProperties(viceInfo, variables);
            BeanUtils.copyProperties(accRecords, variables);
            BeanUtils.copyProperties(discount, variables);
             getStartHandler().handler(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTAPPLY, accRecords.getId(), vouInfo,
                    viceInfo.getApplyCorpId(), userVo.getId());
        }
        return true;
    }
    /**
     * ERP批量提交贴现申请
     *
     * @param abBillDiscountApplyVO
     * @return
     */
    public boolean batchSubmitAbBillDiscountApplyERP(AbBillDiscountApplyVO abBillDiscountApplyVO, ICurrentUser userVo) {
        String accIds = abBillDiscountApplyVO.getAccIds();
        if (StringUtils.isEmpty(accIds)) {
            throw new RuntimeException("传递的参数值不能为空!");
        }
        String[] accIdArrays = accIds.split(",");
        String[] viceIdArrays = new String[accIdArrays.length];
        List<AbAccrecords> newList = new ArrayList<AbAccrecords>();
        for (int i = 0; i < accIdArrays.length; i++) {
            // 上一笔流水信息
            AbAccrecords accRecordsLast = abAccrecordsService.get(accIdArrays[i]);
            // 主信息表
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accRecordsLast.getVid());
            // 从表
            AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accRecordsLast.getVvid());
            viceIdArrays[i] = accRecordsLast.getVvid();
            // 生成贴现申请流水
            AbAccrecords accRecordsNew = new AbAccrecords();
            accRecordsNew.setVid(vouInfo.getId());
            accRecordsNew.setVvid(viceInfo.getId());
            accRecordsNew.setLastid(accRecordsLast.getId());// 上一笔流水id
            accRecordsNew.setLastbillcode(vouInfo.getBillStatus());// 上一票据状态代码
            accRecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);// 有效标志
            accRecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);// 业务处理状态
            accRecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_6011);// 业务类型
            accRecordsNew.setApplyDate(new Date());// 申请日期
            accRecordsNew.setBillFlag(vouInfo.getBillFlag());
            accRecordsNew.setBusinessDate(abBillDiscountApplyVO.getBusinessDate());// 业务办理日期
            accRecordsNew.setInsideSing(abBillDiscountApplyVO.getInsideSing());
            // 是否内部业务标识
            accRecordsNew.setReqAcctId(viceInfo.getBankAcc()); // 贴出人账号
            accRecordsNew.setRecNm(abBillDiscountApplyVO.getRecNm());// 贴入人名称
            accRecordsNew.setRecAcctsvcr(abBillDiscountApplyVO.getRecAcctsvcr());// 贴入人行号
            accRecordsNew.setRecAcctId(abBillDiscountApplyVO.getRecAcctId());// 贴入人账号
            accRecordsNew.setVouInfo(vouInfo);
            accRecordsNew.setVouViceInfo(viceInfo);
            accRecordsNew.setRmk(abBillDiscountApplyVO.getRmk());
            newList.add(accRecordsNew);
        }
        // 批量新增流水
        int accNum = abAccrecordsService.batchSaveAccrecords(newList, userVo);
        // 修改box_status=10
        int viceNum = abVoucherinfoViceService.batchUpdateBoxStatus(viceIdArrays, AccebillConstant.AB_BOX_STATUS_Y,
                userVo);
        for (AbAccrecords abs : newList) {
            int upLastValidSignNum = abAccrecordsService.updateValidSign(abs.getLastid(),
                    AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);// 修改上一流水validSign=2
            if (upLastValidSignNum != 1) {
				throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            abBillNotificationService.sendMsg(abs, AccebillConstant.BRIEF_STATUS_011);
            // 修改从表back_brief_id为""
            abVoucherinfoViceService.updateBackBriefIdIsNull(abs.getVvid());
            abLogService.saveAbLog(abs.getVvid(), abs.getId(), abs.getBriefId(), AccebillConstant.ABLOG_LOGTYPE_APP, AccebillConstant.LANG_LOG_APP_SUCC, userVo);
        }
        return true;
    }
    /**
     * 导入
     *
     * @param list
     * @return
     */
    public boolean saveImport(List<AbBillDiscountApplyVO> list, ICurrentUser userVo) {
        if (list != null && !list.isEmpty()) {
            // 返回校验后的数据信息
            for (AbBillDiscountApplyVO applyVo : list) {
                // 新增贴现信息
                List<AbDiscount> abDiscountList = new ArrayList<>();
                AbDiscount discount = new AbDiscount();
                //贴现种类
                discount.setRpdmk(applyVo.getRpdmk());
                //清算方式
                discount.setSttlmmk(applyVo.getSttlmmk());
                //贴现日期
                discount.setDiscountDate(applyVo.getDiscountDate());
                //入账账户
                discount.setAoaccninfId(applyVo.getAoaccninfId());
                //入账行号
                discount.setAoaccninfAc(applyVo.getAoaccninfAc());
                discount.setAoaccninfName(applyVo.getAoaccninfName());
                //贴现利率
                discount.setInterestRates(applyVo.getInterestRates().divide(new BigDecimal(100)));
                //赎回开放日
                discount.setRpdopendt(applyVo.getRpdopendt());
                //赎回截止日
                discount.setRpdduedt(applyVo.getRpdduedt());
                //赎回利率
                if (applyVo.getRpdintrstRate() != null) {
                    discount.setRpdintrstRate(applyVo.getRpdintrstRate().divide(new BigDecimal(100)));
                }
                //发票号码
                discount.setInvcNb(applyVo.getInvcNb());
                //交易合同号
                discount.setTxlCtrctNb(applyVo.getTxlCtrctNb());
                abDiscountList.add(discount);
                applyVo.setAbDiscountList(abDiscountList);
                applyVo.setTransMoneys(NumberUtil.decimalFormat("##0.00",applyVo.getTransMoney().doubleValue()));
                batchSubmitAbBillDiscountApply(applyVo, userVo);
            }
        }
        return true;
    }
    /**
     * 调用审批
     *
     * @param buId
     * @param isPassOK
     * @param appMsg
     */
    public void approveOver(String buId, boolean isPassOK, String appMsg, ICurrentUser userVo) {
        // 通过:从表：box_status=11，流水：status=95,上一条流水valid_sign=2,可做业务票据流入贴现办理中。
        // 打回:从表：box_status=11,从表：back_brief_id改为BRIEF_STATUS_6011，这条贴现申请流水status=-2(已删除)
        if (StringUtils.isEmpty(buId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbAccrecords info = abAccrecordsService.get(buId, true);
        if (info == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        Integer status = isPassOK ? AccebillConstant.AB_BILL_BUS_STATUS95 : AccebillConstant.AB_BILL_BUS_STATUS_2;
        int statusNum = abAccrecordsService.approve(info.getId(), status, userVo);// 修改status=95,-2
        int boxStatusNum = abVoucherinfoViceService.updateBoxStatus(info.getVvid(), AccebillConstant.AB_BOX_STATUS_Y,
                userVo);// 修改boxStatus=11
        if (statusNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        if (boxStatusNum != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        if (isPassOK) {
            int upLastValidSignNum = abAccrecordsService.updateValidSign(info.getLastid(),
                    AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);// 修改上一流水validSign=2
            if (upLastValidSignNum != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            abAccrecordsService.updateApprover(info.getId(), userVo);
            //贴现申请审批通过不做审批通过拆分业务处理交给贴现办理来完成
//            if("2".equals(info.getCopyFlag())){
//                billTicketSplittingUtils.splitConsentOp(info);
//            }
            abBillNotificationService.sendMsg(info, AccebillConstant.BRIEF_STATUS_011);
        } else {
            int backNum = abVoucherinfoViceService.updateBackBriefId(info.getVvid(), AccebillConstant.BRIEF_STATUS_6011,
                    info.getId());// 修改back_brief_id为6011
            if (backNum != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            //批量付票业务进行处理
            if("1".equals(info.getSplitFlg()) && "2".equals(info.getCopyFlag())) {
                billTicketSplittingUtils.splitRepulseOp(info);
            }
        }
        String msg = appMsg;
        String logType = isPassOK ? AccebillConstant.ABLOG_LOGTYPE_APP : AccebillConstant.ABLOG_LOGTYPE_REPULSE;
        abLogService.saveAbLog(info.getVvid(), info.getId(), info.getBriefId(), logType, msg, userVo);
        if (!isPassOK) { // 审批打回
            // 生成余额
            abBillAccBalService.setBankAccBillBal(info.getId(), userVo);
        }
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
                .getWorkFlowBusiness(BillWorkFlowConstant.BILL_ACCEBILL_DISCOUNTAPPLY, dataTablesInput);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput dp = abBillDiscountApplyDao.dataTableApprove(dataTablesInput, businessList);
        List<AbBillDiscountApplyVO> list=dp.getData();
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ABBILLDISCOUNT_STATUS"
                , "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_STTLMMK", "AB_BILL_SEND_SIGN", "AB_DISCOUNT_RPDMK");
        for(AbBillDiscountApplyVO vo:list){
            injectValues(vo, listDictMap);
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
    public AbBillDiscountApplyVO getAbBillDiscountApplyVOById(String id) {
        AbAccrecords accrecordsInfo = abAccrecordsService.get(id, true);
        AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecordsInfo.getVid(), true);
        AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accrecordsInfo.getVvid(), true);
        AbDiscount disInfo = abDiscountService.getAbDiscountByRecordId(accrecordsInfo.getId());
        AbBillDiscountApplyVO reInfo = new AbBillDiscountApplyVO();
        BeanUtils.copyNotNullProperties(vouInfo, reInfo);
        BeanUtils.copyNotNullProperties(viceInfo, reInfo);
        BeanUtils.copyNotNullProperties(accrecordsInfo, reInfo);
        if(disInfo!=null){
            BeanUtils.copyNotNullProperties(disInfo, reInfo);
        }
        reInfo.setId(accrecordsInfo.getId());
        // 添加冗余数据
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_BILL_TYPE", "AB_BILL_FLAG", "AB_ABBILLDISCOUNT_STATUS"
                , "AB_BRIEF_STATUS", "AB_ENTER_STOCK", "AB_STTLMMK", "AB_BILL_SEND_SIGN", "AB_DISCOUNT_RPDMK");
        injectValues(reInfo, listDictMap);
        return reInfo;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(AbBillDiscountApplyVO obj, Object... user) {
        return null;
    }
    @Override
    @Transactional(readOnly = true)
    public void verifyHandler(List<AbBillDiscountApplyVO> list, Object... user) {
        if (list != null && !list.isEmpty()) {
            Map<String, String> origNoMap = new HashMap<>();
            HashMap<String, CorpDTO> corpNameMap = new HashMap<>();
            HashMap<String, CorpDTO> corpCodeMap = new HashMap<>();
            Map<String, BtBankAccDTO> bankAccDTOMap = new HashMap<>();
            Map<String, InputBankInfoDTO> bankNameInfoDTOMap = new HashMap<>();
            Map<String, InputBankInfoDTO> bankCodeInfoDTOMap = new HashMap<>();
            String userId = "";
            if (user[0] != null) {
                ICurrentUser userInfo = (ICurrentUser) user[0];
                userId = userInfo.getId();
            }
            //同一批背书子票区间是否重叠判断
            Map<String, List<AbBillDiscountApplyVO>> splitSbbllRngMap = new HashMap<>();
            String corpAuthSql = SqlUtil.getInSql(
                    abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTAPPLY_QUERY, userId),
                    "p.APPLY_CORP_ID", true);
            //List<String> corpAuthList=corpServiceImpl.getUserCorpIds(AccebillConstant.AB_BILL_ACCEBILL_ABBILLDISCOUNTAPPLY_QUERY,input.getCurrentUserId());
            for (AbBillDiscountApplyVO applyVo : list) {
                if(StrUtil.isEmpty(applyVo.getSbbllRngStrtSn())){
                    applyVo.setSbbllRngStrtSn("0");
                }
                if(StrUtil.isEmpty(applyVo.getSbbllRngEndSn())){
                    applyVo.setSbbllRngEndSn("0");
                }
                List<String> errorList = new ArrayList<>();
                CorpDTO corpInfo = null;
                BtBankAccDTO bankAccInfo = null;
                InputBankInfoDTO bankInfo = null;
                if (StringUtils.isEmpty(applyVo.getApplyCorpCode()) && StringUtils.isEmpty(applyVo.getApplyCorpName())) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_CORP_APPLYCODEANDNAME_NOTNULL);
                } else {
                    if (StringUtils.isNotEmpty(applyVo.getApplyCorpCode())) {
                        if (corpCodeMap.containsKey(applyVo.getApplyCorpCode())) {
                            corpInfo = corpCodeMap.get(applyVo.getApplyCorpCode());
                        } else {
                            corpInfo = abCorpService.getCorpByCode(applyVo.getApplyCorpCode());
                        }
                    } else {
                        if (corpNameMap.containsKey(applyVo.getApplyCorpName())) {
                            corpInfo = corpNameMap.get(applyVo.getApplyCorpName());
                        } else {
                            corpInfo = abCorpService.getCorpIdByName(applyVo.getApplyCorpName());
                        }
                    }
                    if (corpInfo == null) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_APPLYCORPID_ISNOTEXIST);
                    } else {
                        applyVo.setApplyCorpId(corpInfo.getId());
                        applyVo.setApplyCorpCode(corpInfo.getCode());
                        applyVo.setApplyCorpName(corpInfo.getName());
                        corpCodeMap.put(corpInfo.getCode(), corpInfo);
                        corpNameMap.put(corpInfo.getName(), corpInfo);
                    }
                }
                if (StringUtils.isEmpty(applyVo.getCorpCode()) && StringUtils.isEmpty(applyVo.getCorpName())) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_CORP_CODEANDNAME_NOTNULL);
                    applyVo.addErrorMsg(errorList);
                    continue;
                } else {
                    if (StringUtils.isNotEmpty(applyVo.getCorpCode())) {
                        if (corpCodeMap.containsKey(applyVo.getCorpCode())) {
                            corpInfo = corpCodeMap.get(applyVo.getCorpCode());
                        } else {
                            corpInfo = abCorpService.getCorpByCode(applyVo.getCorpCode());
                        }
                    } else {
                        if (corpNameMap.containsKey(applyVo.getCorpName())) {
                            corpInfo = corpNameMap.get(applyVo.getCorpName());
                        } else {
                            corpInfo = abCorpService.getCorpIdByName(applyVo.getCorpName());
                        }
                    }
                    if (corpInfo == null) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_CORPID_ISNOTEXIST);
                        applyVo.addErrorMsg(errorList);
                        continue;
                    } else {
                        applyVo.setCorpCode(corpInfo.getCode());
                        applyVo.setCorpName(corpInfo.getName());
                        applyVo.setCorpId(corpInfo.getId());
                        corpCodeMap.put(corpInfo.getCode(), corpInfo);
                        corpNameMap.put(corpInfo.getName(), corpInfo);
                    }
                }
                //业务单位
                if (StringUtils.isNotEmpty(applyVo.getBusCorpName())) {
                    CorpDTO busCorpDTO;
                    if (corpNameMap.containsKey(applyVo.getBusCorpName())) {
                        busCorpDTO = corpNameMap.get(applyVo.getBusCorpName());
                    } else {
                        busCorpDTO = abCorpService.getCorpIdByName(applyVo.getBusCorpName());
                    }
                    if (busCorpDTO == null) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_BUSCORPID_ISNOTEXIST);
                    }else{
                        applyVo.setBusCorpId(busCorpDTO.getId());
                    }
                }

                Map<String, Object> params = new HashMap<>();
                params.put("origNo", applyVo.getOrigNo());
                params.put("sbbllRngStrtSn", applyVo.getSbbllRngStrtSnOld());
                params.put("sbbllRngEndSn", applyVo.getSbbllRngEndSnOld());
                params.put("corpId", applyVo.getCorpId());
                AbBillDiscountApplyVO billInfo = this.getAbBillDiscountApplyVOByParams(params,
                        corpAuthSql);
                if (billInfo == null) {
                    errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOOP);
                    applyVo.addErrorMsg(errorList);
                    continue;
                } else if (billInfo.getStatus().equals(AccebillConstant.AB_BILL_BUS_STATUS11)) {
                    errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_APPROVEOP);
                    applyVo.addErrorMsg(errorList);
                    continue;
                } else {
                    applyVo.setAccIds(billInfo.getAccId());
                    applyVo.setBillFlag(billInfo.getBillFlag());
                    applyVo.setBillType(billInfo.getBillType());
                    applyVo.setEnterStock(billInfo.getEnterStock());
                    applyVo.setBillMoney(billInfo.getBillMoney());
                    applyVo.setStartDate(billInfo.getStartDate());
                    applyVo.setEndDate(billInfo.getEndDate());
                    applyVo.setBillMoney(billInfo.getBillMoney());
                    applyVo.setBillType(billInfo.getBillType());
                    applyVo.setBillFlag(billInfo.getBillFlag());
                    applyVo.setStartDate(billInfo.getStartDate());
                    applyVo.setEndDate(billInfo.getEndDate());
                    applyVo.setBlgrdbagcrclPrmtInd(billInfo.getBlgrdbagcrclPrmtInd());
                }

                if (origNoMap.containsKey(applyVo.getOrigNo()) && !"1".equals(billInfo.getBlgrdbagcrclPrmtInd())) {
                    errorList.add(AccebillConstant.LANG_ERROR_ORIGNO_NOTREPETITION);
                    applyVo.addErrorMsg(errorList);
                    continue;
                } else {
                    origNoMap.put(applyVo.getOrigNo(), applyVo.getOrigNo());
                }
                //不允许拆票的票据交易金额需要与票据金额一样，允许拆分的票据交易金额不能大于票据金额
                if((!"1".equals(billInfo.getBlgrdbagcrclPrmtInd()) && (applyVo.getBillMoney().compareTo(applyVo.getTransMoney()) != 0) )
                        || (applyVo.getBillMoney().compareTo(applyVo.getTransMoney()) < 0)){

                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_ERROR_BILLISNONULL);
                }
                if (StringUtils.isNotEmpty(applyVo.getInsideSingName())) {
                    if (AccebillConstant.AB_INSIDE_SING_Y.equals(applyVo.getInsideSingName())
                            || AccebillConstant.AB_INSIDE_SING_N.equals(applyVo.getInsideSingName())) {
                        applyVo.setInsideSing(Integer.parseInt(applyVo.getInsideSingName()));
                    } else {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTAPPLY_INSIDESING_ERROR);
                        continue;
                    }
                }
                // 中心代管的纸票,只能进行内部贴现
                if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(billInfo.getBillFlag()))
                        && AccebillConstant.AB_ENTER_STOCK_NET.equals(String.valueOf(billInfo.getEnterStock()))
                        && AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(applyVo.getInsideSing()))) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTAPPLY_BILLFLAG10ANDNET_TOINNER);
                    applyVo.addErrorMsg(errorList);
                    continue;
                }
                // 银行托管的纸票,只能贴到银行
                if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(String.valueOf(billInfo.getBillFlag()))
                        && AccebillConstant.AB_ENTER_STOCK_BANK.equals(String.valueOf(billInfo.getEnterStock()))
                        && AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(applyVo.getInsideSing()))) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTREC_BILLFLAG10ANDBANK_TOOUTER);
                    applyVo.addErrorMsg(errorList);
                    continue;
                }
                // 单位库存的电票,只能贴到银行
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(billInfo.getBillFlag()))
                        && AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(applyVo.getInsideSing()))) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTREC_BILLFLAG11_TOOUTER);
                    applyVo.addErrorMsg(errorList);
                    continue;
                }
                // 贴入到结算中心
                if (applyVo.getInsideSing() != null
                        && AccebillConstant.AB_INSIDE_SING_Y.equals(String.valueOf(applyVo.getInsideSing()))) {
                    if (StringUtils.isEmpty(applyVo.getRecAcctId())) {
                        applyVo.setRecAcctsvcr(null);
                        if (StringUtils.isEmpty(applyVo.getRecNm())) {
                            errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTRECACCTID_ISNOTNULL);
                            applyVo.addErrorMsg(errorList);
                            continue;
                        }
                    } else {
                        if (bankAccDTOMap.containsKey(applyVo.getRecAcctId())) {
                            bankAccInfo = bankAccDTOMap.get(applyVo.getRecAcctId());
                        } else {
                            bankAccInfo = abBtBankAccService.getBankAccInfo(applyVo.getRecAcctId());
                        }
                        if (bankAccInfo == null) {
                            errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTRECACCTID_ISNOTEXIST);
                        } else {
                            bankAccDTOMap.put(bankAccInfo.getBankAcc(), bankAccInfo);
                            applyVo.setRecNm(bankAccInfo.getAccName());
                            applyVo.setRecAcctsvcr(bankAccInfo.getBankCode());
                        }
                    }
                }
                // 贴入到银行
                if (applyVo.getInsideSing() != null
                        && AccebillConstant.AB_INSIDE_SING_N.equals(String.valueOf(applyVo.getInsideSing()))) {
                    if (StringUtils.isEmpty(applyVo.getRecAcctsvcr()) && StringUtils.isEmpty(applyVo.getRecNm())) {
                        errorList.add(AccebillConstant.LANG_ERROR_DISCOUNTREC_CODEANDNAME_NONULL);
                    } else {
                        if (StringUtils.isNotEmpty(applyVo.getRecAcctsvcr())) {
                            if (bankCodeInfoDTOMap.containsKey(applyVo.getRecAcctsvcr())) {
                                bankInfo = bankCodeInfoDTOMap.get(applyVo.getRecAcctsvcr());
                            } else {
                                bankInfo = abInputBankInfoService.getByBankCode(applyVo.getRecAcctsvcr());
                                if (bankInfo != null) {
                                    bankNameInfoDTOMap.put(bankInfo.getBankName(), bankInfo);
                                    bankCodeInfoDTOMap.put(bankInfo.getBankCode(), bankInfo);
                                }
                            }
                        }
                        if (bankInfo == null && StringUtils.isNotEmpty(applyVo.getRecNm())) {
                            if (bankNameInfoDTOMap.containsKey(applyVo.getRecNm())) {
                                bankInfo = bankNameInfoDTOMap.get(applyVo.getRecNm());
                            } else {
                                bankInfo = abInputBankInfoService.getByBankName(applyVo.getRecNm());
                                if (bankInfo != null) {
                                    bankNameInfoDTOMap.put(bankInfo.getBankName(), bankInfo);
                                    bankCodeInfoDTOMap.put(bankInfo.getBankCode(), bankInfo);
                                }
                            }
                        }
                        // 贴入人银行数据校验
                        if (bankInfo == null) {
                            errorList.add(AccebillConstant.LANG_ERROR_DISCOUNTREC_CODEORNAME);
                        } else {
                            applyVo.setRecAcctsvcr(bankInfo.getBankCode());
                            applyVo.setRecNm(bankInfo.getBankName());
                        }
                    }
                }
                // 贴现种类
                if (StringUtils.isEmpty(applyVo.getRpdmkName())) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_RPDMK_ISNOTNULL);
                } else {
                    if (AccebillConstant.AB_DISCOUNT_RPDMK_BUYOUT.equals(applyVo.getRpdmkName())
                            || AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK.equals(applyVo.getRpdmkName())) {
                        applyVo.setRpdmk(applyVo.getRpdmkName());
                    } else {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_RPDMK_ERROR);
                    }
                }
                // 清算方式
                if (StringUtils.isEmpty(applyVo.getSttlmmkName())) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_STTLMMK_ISNOTNULL);
                } else {
                    if (AccebillConstant.AB_STTLMMK_Y.equals(applyVo.getSttlmmkName())
                            || AccebillConstant.AB_STTLMMK_N.equals(applyVo.getSttlmmkName())) {
                        applyVo.setSttlmmk(applyVo.getSttlmmkName());
                    } else {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_STTLMMK_ERROR);
                    }
                }
                // 贴现日期
                if (applyVo.getDiscountDate() == null) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTDATE_ISNOTNULL);
                } else {
                    if (applyVo.getDiscountDate().getTime() - applyVo.getStartDate().getTime() < 0
                            || applyVo.getDiscountDate().getTime() - applyVo.getEndDate().getTime() >= 0) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_DISCOUNTDATE_COMPARESTARTANDEND);// 贴现申请日期必须大于等于出票日期,小于到期日期!
                    }
                }
                // 赎回开放日,截止日
                if (AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK.equals(applyVo.getRpdmkName())) {
                    if (applyVo.getRpdopendt() == null) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_RM01_RPDOPENDT_ISNOTNULL);
                    }
                    if (applyVo.getRpdduedt() == null) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_RM01_RPDDUEDT_ISNOTNULL);
                    }
                    if (applyVo.getRpdopendt() != null && applyVo.getRpdduedt() != null) {
                        if (applyVo.getRpdopendt().getTime() - applyVo.getRpdduedt().getTime() >= 0) {
                            errorList.add(AccebillConstant.BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_COMDUETOOPENMSG);// 赎回截止日必须大于赎回开放日
                        }
                        if (applyVo.getDiscountDate() != null) {
                            if (applyVo.getDiscountDate().getTime() - applyVo.getRpdopendt().getTime() > 0) {
                                errorList.add(AccebillConstant.BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_COMRPDOPENDTMSG);// 赎回开放日不能小于贴现日期!
                            }
                            if (applyVo.getDiscountDate().getTime() - applyVo.getRpdopendt().getTime() >= 0) {
                                errorList.add(AccebillConstant.BILL_ACCEBILL_ABBILLDISCOUNTMANAGER_COMRPDDUEDTMSG);// 赎回截止日必须大于贴现日期!
                            }
                        }
                        if (!(applyVo.getRpdopendt().getTime() - applyVo.getStartDate().getTime() >= 0
                                && applyVo.getRpdopendt().getTime() - applyVo.getEndDate().getTime() < 0)) {// 赎回开放日必须大于等于出票日期小于到期日期!
                            errorList.add(AccebillConstant.BILL_ACCEBILL_RPDOPENDT_COMPARESTARTANDEND);
                        }

                        if (!(applyVo.getRpdduedt().getTime() - applyVo.getStartDate().getTime() >= 0
                                && applyVo.getRpdduedt().getTime() - applyVo.getEndDate().getTime() < 0)) {// 赎回截止日必须大于出票日期小于到期日期
                            errorList.add(AccebillConstant.BILL_ACCEBILL_RPDDUEDT_COMPARESTARTANDEND);
                        }
                    }

                }
                // 贴现利率
                if (applyVo.getInterestRates() == null) {
                    errorList.add(AccebillConstant.ACCEBILL_MESSAGE_INTERESTRATES_ISNOTNULL);
                } else {
                    if (!(applyVo.getInterestRates().compareTo(BigDecimal.valueOf(0.0000)) >= 0
                            && applyVo.getInterestRates().compareTo(BigDecimal.valueOf(100.0000)) <= 0)) {
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_INTERESTRATES_ERROR);//
                    }
                }
                // 入账账户,行号
                if (StringUtils.isNotEmpty(String.valueOf(applyVo.getBillFlag()))
                        && AccebillConstant.AB_BILL_FLAG_ELECT.equals(String.valueOf(applyVo.getBillFlag()))) {
                    if (StringUtils.isEmpty(applyVo.getAoaccninfAc())) {// 入账行号
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_AOACCNINFAC_BILLFLAG11_ISNOTNULL);
                    }
                    if (StringUtils.isEmpty(applyVo.getAoaccninfId())) {// 入账账户
                        errorList.add(AccebillConstant.ACCEBILL_MESSAGE_AOACCNINFID_BILLFLAG11_ISNOTNULL);
                    }
                }


                if(!"1".equals(billInfo.getBlgrdbagcrclPrmtInd())
                        && (StrUtil.isNotEmpty(applyVo.getSbbllRngStrtSnSplit()) || StrUtil.isNotEmpty(applyVo.getSbbllRngEndSnSplit()))){
                    //不可拆分票据不允许做子票区间拆分！
                    errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_NOT_SPLIT);
                } else  {
                    //设置了拆分子票区间处理
                    if (StrUtil.isEmpty(applyVo.getSbbllRngEndSnSplit())
                            && StrUtil.isNotEmpty(applyVo.getSbbllRngStrtSnSplit())) {
//							结束子票区间为空开始子票区间有值自动根据结算结果设置结束子票区间
                        String sbbllRngEndSnSplit=StrUtil.fill(applyVo.getTransMoney()
                                .multiply(new BigDecimal("100"))
                                .add(new BigDecimal(applyVo.getSbbllRngStrtSnSplit()))
                                .subtract(new BigDecimal("1"))
                                .setScale(0,BigDecimal.ROUND_HALF_UP).toString(),'0',12,true);
                        applyVo.setSbbllRngEndSnSplit(sbbllRngEndSnSplit);
                    }
//						开始拆分子票区间和结束子票区间 必须大于等于原票据子票区间 和 小于等于结束子票区间
                    if (StrUtil.isNotEmpty(applyVo.getSbbllRngStrtSnSplit())
                            && StrUtil.isNotEmpty(applyVo.getSbbllRngEndSnSplit()) ) {
                        long strtSnSplit=Long.parseLong(applyVo.getSbbllRngStrtSnSplit());
                        long endSnSplit=Long.parseLong(applyVo.getSbbllRngEndSnSplit());
                        if(strtSnSplit>=endSnSplit){
                            errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_SPLIT_START_BE_END);
                        }
                        if(strtSnSplit<Long.parseLong(billInfo.getSbbllRngStrtSn())
                                ||endSnSplit>Long.parseLong(billInfo.getSbbllRngEndSn())){
                            errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_OLD_NOT_INCLUDE_SPLIT);
                        }
                        BigDecimal transMoney= new BigDecimal(applyVo.getSbbllRngEndSnSplit())
                                .subtract(new BigDecimal(applyVo.getSbbllRngStrtSnSplit()))
                                .add(new BigDecimal("1"))
                                .divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP);
                        //拆分金额和子票区间匹配
                        if(applyVo.getTransMoney().compareTo(transMoney)!=0){
                            errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_TRANSMONEY_NOT_SBBLLRNG);
                        }

                        //判断批量背书的子票区间是否有重叠
                        String splitSbbllRngMapKey=StrUtil.builder(billInfo.getOrigNo(),"-",billInfo.getSbbllRngStrtSn(),"-",billInfo.getSbbllRngEndSn()).toString();
                        if(CollectionUtil.isEmpty(splitSbbllRngMap.get(splitSbbllRngMapKey))){
                            splitSbbllRngMap.put(splitSbbllRngMapKey,new ArrayList<>());
                        } else {
                            for(AbBillDiscountApplyVO splitVo : splitSbbllRngMap.get(splitSbbllRngMapKey)){
                                if((strtSnSplit>=Long.parseLong(splitVo.getSbbllRngStrtSnSplit())
                                        &&strtSnSplit<=Long.parseLong(splitVo.getSbbllRngEndSnSplit()))
                                        ||(endSnSplit>=Long.parseLong(splitVo.getSbbllRngStrtSnSplit())
                                        &&endSnSplit<=Long.parseLong(splitVo.getSbbllRngEndSnSplit()))){
                                    errorList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_SPLIT_SBBLLRNG_REP);
                                    break;
                                }
                            }
                        }
                        splitSbbllRngMap.get(splitSbbllRngMapKey).add(applyVo);
                    }
                }
                applyVo.addErrorMsg(errorList);
            }
        }
    }
    /**
     * 根据单位和票据号贴现票据信息
     * @param params
     * @param corpAuthSql
     * @return
     */
    public AbBillDiscountApplyVO  getAbBillDiscountApplyVOByParams(Map<String,Object> params, String corpAuthSql){
        return abBillDiscountApplyDao.getAbBillDiscountApplyVOByParams(params,corpAuthSql);
    }
    @Override
    @Transactional(readOnly = true)
    public AbVoucherinfoVO getAbBillInfoById(String aid) {
        return this.getAbBillDiscountApplyVOById(aid);
    }
    /**
     * 通过单位id串获取单位信息集合
     *
     * @param corpIds
     * @return
     */
    @Transactional(readOnly = true)
    public List<CorpDTO> listSysCorp(String corpIds, String corpNames) {
        if (StringUtils.isEmpty(corpIds)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String[] idArr = corpIds.split(",");
        String[] nameArr = corpNames.split(",");
        List<CorpDTO> list = new ArrayList<CorpDTO>();
        for (int i = 0; i < idArr.length; i++) {
            CorpDTO corp = abCorpService.get(idArr[i]);
            if (corp == null) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_SYSCORP_ISNOTEXIST, nameArr[i]);
            }
            list.add(corp);
        }
        return list;
    }
}
