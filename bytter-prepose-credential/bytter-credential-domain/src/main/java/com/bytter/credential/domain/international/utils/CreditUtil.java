package com.bytter.credential.domain.international.utils;

import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.api.constant.InternationalConstant;
import com.bytter.credential.api.constant.LcEnrolConstant;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.service.LcDepositService;
import com.bytter.credential.domain.international.service.SupplementPaymentService;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDtlDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.enums.FcBondMessageEnum;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-07 9:26
 * @Version 1.0
 */
public abstract class CreditUtil {
    /**
     * 获取开证单号
     * @return
     */
    public static String getLcNo(){
        return SequenceSingleton.getSequence(SequenceEnum.LC_ENROL_BILL_NO, LcEnrolConstant.LC_BILL_CODE_PREFIX);
    }
    /**
     * 获取到单单号
     * @return
     */
    public static String getArrivalNo(){
        return SequenceSingleton.getSequence(SequenceEnum.LC_BILL_ARRIVAL_BILL_NO,LcEnrolConstant.LC_BILL_CODE_ARRIVAL);
    }
    /**
     * 获取到单/交单单号
     * @return
     */
    public static String getPaymentNo(){
        return SequenceSingleton.getSequence(SequenceEnum.LC_PAYMENT_BILL_NO,LcEnrolConstant.LC_PAYMENT);
    }
    /**
     * 押汇单号
     * @return
     */
    public static String getDepostNo(){
        return SequenceSingleton.getSequence(SequenceEnum.LC_DEPOST_BILL_NO,LcEnrolConstant.LC_DEPOSIT);
    }
    /**
     * 展期单号
     * @return
     */
    public static String getDepostDelayNo(){
        return SequenceSingleton.getSequence(SequenceEnum.LC_DEPOST_DELAY_BILL_NO,LcEnrolConstant.LC_DEPOSIT_DELAY);
    }
    /**
     * 获取还押汇单号
     * @return
     */
    public static String getDepostRepayNo(){
        return SequenceSingleton.getSequence(SequenceEnum.LC_DEPOST_REPAY_BILL_NO, LcEnrolConstant.LC_DEPOSIT_REPAY);
    }
    /**
     * 构建授信占用/释放DTO
     * @param lcEnrol
     * @param urationDTO
     * @param Money  释放金额
     */
    public static void setLCCreditRatUrationDTO(LcEnrol lcEnrol, RatUrationDTO urationDTO, BigDecimal Money, String isAllRelease){
        urationDTO.setRatCode(lcEnrol.getRationCode());
        urationDTO.setCurCode(lcEnrol.getIssuingCurId());//币别ID
        urationDTO.setBillId(lcEnrol.getId());//业务ID
        urationDTO.setBillNo(lcEnrol.getLcNo());//业务编号
        urationDTO.setSysCode(ConfigConstant.LCS);//国际信用证
        urationDTO.setBusinessName(RationConstant.BUSINESS_NAME_09);//国际信用证占用
        urationDTO.setContactNo(lcEnrol.getContractNo());//合同号
        urationDTO.setBeginDate(lcEnrol.getIssuingDate());//开证日期
        urationDTO.setEndDate(lcEnrol.getExpiryDate());//有效期
        urationDTO.setMoney(Money);//释放/占用金额
        urationDTO.setBillMoney(lcEnrol.getCreditMoney());//业务金额
        if (StringUtils.isNotEmpty(lcEnrol.getIsOpenRat()) && "1".equals(lcEnrol.getIsOpenRat())) {
            if (StringUtils.isEmpty(lcEnrol.getIsHangBond())) {
                urationDTO.setBailRate(BigDecimal.ZERO);//保证金比例
                urationDTO.setBondMoney(BigDecimal.ZERO);//保证金金额
                urationDTO.setBondCurId(lcEnrol.getIssuingCurId());//保证金币别
            } else {
                if (null == lcEnrol.getBondMoney()) {//不填保证金时则默认保证金为0
                    urationDTO.setBailRate(BigDecimal.ZERO);//保证金比例
                    urationDTO.setBondMoney(BigDecimal.ZERO);//保证金金额
                } else {
                    urationDTO.setBailRate(lcEnrol.getBondPercent());//保证金比例
                    urationDTO.setBondMoney(lcEnrol.getBondMoney());//保证金金额
                }
                urationDTO.setBondCurId(lcEnrol.getBondCurId());//保证金币别
            }
        }
/*        urationDTO.setBailRate(lcEnrol.getBondPercent());//保证金比例
        urationDTO.setBondMoney(lcEnrol.getBondMoney());//保证金金额
        urationDTO.setBondCurId(lcEnrol.getBondCurId());//保证金币别*/
        urationDTO.setIsOpenRat(lcEnrol.getIsOpenRat());//是否启用授信敞口
        urationDTO.setCorpId(lcEnrol.getCorpId());//网点/单位
        urationDTO.setRatItemCode(lcEnrol.getItemCode());
        if (null != lcEnrol.getBillName()) {
            urationDTO.setBillName(lcEnrol.getBillName());//设置业务品种
        }
        if (StringUtils.isNotEmpty(isAllRelease) && InternationalConstant.ISALLRELEASE_Y.equals(isAllRelease)) {
//            释放全部授信
            urationDTO.setAllRelease(true);
        }
        if (InternationalConstant.RATMODEL_1.equals(lcEnrol.getRatmodel())) {
            List<RatUrationDtlDTO> ratUrationDtlDTOS = new ArrayList<>();
            RatUrationDtlDTO ratUrationDtl = new RatUrationDtlDTO();
            ratUrationDtl.setItemCode(lcEnrol.getItemCode());
            ratUrationDtl.setItemMoney(lcEnrol.getItemMoney());
            ratUrationDtlDTOS.add(ratUrationDtl);
            urationDTO.setApplyDtls(ratUrationDtlDTOS);
        }
    }
    /**
     *
     * @param lcEnrol 信用证对象
     * @param ratUrationDTO 需要构建的授信占用对象
     * @param nativeCur 本位币
     */
    public static void CreateRatUrationDTO(LcEnrol lcEnrol, RatUrationDTO ratUrationDTO, BtCurrencyDTO nativeCur) {
        if (null == lcEnrol.getSpillOverRate()) {
            lcEnrol.setSpillOverRate(BigDecimal.ZERO);
        }
//            判断是否本位币
        if (nativeCur.getId().equals(lcEnrol.getIssuingCurId())) {
//                本位币
//                是否溢短装
            if (InternationalConstant.ISSPILLOVER_Y.equals(lcEnrol.getIsSpillOver())) {
//                    溢短装占用授信
                CreditUtil.setLCCreditRatUrationDTO(lcEnrol,ratUrationDTO,lcEnrol.getCreditMoney().multiply(lcEnrol.getSpillOverRate().add(new BigDecimal(1))),null);
            } else {
//                    非溢短装占用授信
                CreditUtil.setLCCreditRatUrationDTO(lcEnrol,ratUrationDTO,lcEnrol.getCreditMoney(),null);
            }
        }else {
//                外币
//                是否折算本位币
            if (InternationalConstant.ISNATIVECUR_Y.equals(lcEnrol.getIsNativeCur())) {
                //                是否溢短装
                if (InternationalConstant.ISSPILLOVER_Y.equals(lcEnrol.getIsSpillOver())) {
                    //                    溢短装占用授信    ，接口已经进行过汇率折算，这里直接使用开证金额
                    CreditUtil.setLCCreditRatUrationDTO(lcEnrol,ratUrationDTO,lcEnrol.getCreditMoney().multiply(lcEnrol.getSpillOverRate().add(new BigDecimal(1))),null);
                } else {
                    //                    非溢短装占用授信
                    CreditUtil.setLCCreditRatUrationDTO(lcEnrol,ratUrationDTO,lcEnrol.getCreditMoney(),null);
                }
            } else {
                //                是否溢短装
                if (InternationalConstant.ISSPILLOVER_Y.equals(lcEnrol.getIsSpillOver())) {
                    //                    溢短装占用授信
                    CreditUtil.setLCCreditRatUrationDTO(lcEnrol,ratUrationDTO,lcEnrol.getCreditMoney().multiply(lcEnrol.getSpillOverRate().add(new BigDecimal(1))),null);
                } else {
                    //                    非溢短装占用授信
                    CreditUtil.setLCCreditRatUrationDTO(lcEnrol,ratUrationDTO,lcEnrol.getCreditMoney(),null);
                }
            }
        }
    }
    /**
     * 金额校验
     * @param entityPre
     * @param nowEntity
     * @param bondHookService
     */
    public static void bondCheck(LcEnrol entityPre,LcEnrol nowEntity, IFcBondHookService bondHookService){
        FcBondDTO fcBondDTO = bondHookService.getFcBondMsgById(nowEntity.getBondId());
        if (entityPre.getBondId().equals(nowEntity.getBondId())) {//占用同一保证金但是金额不同时则需校验
            BigDecimal availMoney = fcBondDTO.getAvailMoney().add(entityPre.getBondMoney());//上一次占用+可用余额
            if (availMoney.compareTo(nowEntity.getBondMoney()) == -1) {
                throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECK);
            }
        } else {//占用不同保证金则直接比较
            if (fcBondDTO.getAvailMoney().compareTo(nowEntity.getBondMoney()) == -1) {
                throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECK);
            }
        }
    }
    /**
     * 设置保证金占用信息
     * @param dto
     * @param entity
     * @return
     */
    public static FcBondApplyDTO setFcBondApplyDTO(FcBondApplyDTO dto, LcEnrol entity){
        dto.setApplyType(FcBondConstant.APPLY_TYPE_BUSINESS);//占用类型0:业务占用，1手工占用
        dto.setItemType(FcBondConstant.ITEM_TYPE_INTERNATIONAL);//0银行授信1银行借款2承兑汇票6-国际结算
        dto.setItemNo(entity.getLcNo());//项目单号
        dto.setItemId(entity.getId());//占用保证金的项目id
        dto.setCorpId(entity.getCorpId());//单位id
        dto.setBankId(entity.getIssuingBank());//联行号
        dto.setItemMoney(entity.getCreditMoney());//项目金额
        dto.setBondPercent(entity.getBondPercent());//保证金比例
        dto.setStartDate(entity.getIssuingDate());//项目开始时间
        dto.setEndDate(entity.getExpiryDate());//项目结束时间
        dto.setBondContract(entity.getBondContract());//保证金合同
        dto.setBondId(entity.getBondId());//保证金id
        dto.setApplyMoney(entity.getBondMoney());//保证金金额,即占用金额
        dto.setRemark(entity.getRmk());//备注
        return dto;
    }
    /**
     * 信用证保证金通用处理方法
     * @param entityPre
     * @param nowEntity
     * @param bondHookService
     */
    public static void handleBond(LcEnrol entityPre, LcEnrol nowEntity, IFcBondHookService bondHookService, ICurrentUser user) {
        FcBondApplyDTO bondApplyDTO = null;
        if (null == entityPre) {//新增时占用
            if (InternationalConstant.ISHANGBOND_Y.equals(nowEntity.getIsHangBond())) {
                bondApplyDTO = new FcBondApplyDTO();
                setFcBondApplyDTO(bondApplyDTO, nowEntity);
                bondApplyDTO.setCreateDate(new Date());
                bondApplyDTO.setCreateBy(user.getId());
                bondApplyDTO.setStatus(FcBondConstant.STATUE_APPLY);
                bondHookService.addFcBondApply(bondApplyDTO);
            }
        } else{//更新时占用
            if (InternationalConstant.ISHANGBOND_Y.equals(entityPre.getIsHangBond())) {//修改前占用
                FcBondApplyDTO bondApplyDTOPre = bondHookService.getFcBondApply(entityPre.getId(),entityPre.getLcNo(),entityPre.getBondId());
                if (InternationalConstant.ISHANGBOND_Y.equals(nowEntity.getIsHangBond())) {//修改后占用
                    bondCheck(entityPre,nowEntity,bondHookService);
                    if(bondApplyDTOPre.getBondId().equals(nowEntity.getBondId())){//修改前后为同一保证金账户
                        setFcBondApplyDTO(bondApplyDTOPre, nowEntity);
                        bondApplyDTOPre.setUpdateDate(new Date());
                        bondApplyDTOPre.setUpdateBy(user.getId());
                        bondApplyDTOPre.setStatus(FcBondConstant.STATUE_APPLY);
                        bondHookService.updateFcBondApply(bondApplyDTOPre);//之前占用现在依旧占用，则更新
                    }else{//修改前后为不同保证金账户
                        bondHookService.updateFcBondApplyData(bondApplyDTOPre);//释放之前的占用额度
                        bondApplyDTO = new FcBondApplyDTO();
                        setFcBondApplyDTO(bondApplyDTO, nowEntity);
                        bondApplyDTO.setCreateDate(new Date());
                        bondApplyDTO.setCreateBy(user.getId());
                        bondApplyDTO.setStatus(FcBondConstant.STATUE_APPLY);
                        bondHookService.addFcBondApply(bondApplyDTO);//新增占用
                    }
                }else {//修改后不占用
                    bondHookService.updateFcBondApplyData(bondApplyDTOPre);//之前占用现在不占用，则释放之前的占用额度
                }
            }else {//修改前不占用
                if (InternationalConstant.ISHANGBOND_Y.equals(nowEntity.getIsHangBond())) {
                    bondApplyDTO = new FcBondApplyDTO();
                    setFcBondApplyDTO(bondApplyDTO, nowEntity);
                    bondApplyDTO.setCreateDate(new Date());
                    bondApplyDTO.setCreateBy(user.getId());
                    bondApplyDTO.setStatus(FcBondConstant.STATUE_APPLY);
                    bondHookService.addFcBondApply(bondApplyDTO);//之前为占用现在占用，则新增占用
                }
            }
        }
    }
    /**
     * 释放保证金
     * @param bondHookService
     */
    public static void releaseBond(LcEnrol entity, IFcBondHookService bondHookService) {
        if (InternationalConstant.ISHANGBOND_Y.equals(entity.getIsHangBond())) {
            FcBondApplyDTO bondApplyDTO = new FcBondApplyDTO();
            setFcBondApplyDTO(bondApplyDTO, entity);
            bondApplyDTO.setStatus(FcBondConstant.STATUE_RELEASE);
            bondHookService.updateFcBondApply(bondApplyDTO);//之前占用现在不占用，则释放之前的占用额度
        }
    }
    /**
     * 删除保证金
     */
    public static  void deleteBond(LcEnrol entity, IFcBondHookService bondHookService){
        if (InternationalConstant.ISHANGBOND_Y.equals(entity.getIsHangBond())) {
            FcBondApplyDTO bondApplyDTO = bondHookService.getFcBondApply(entity.getId(),entity.getLcNo(),entity.getBondId());
            bondHookService.updateFcBondApplyData(bondApplyDTO);//之前占用现在不占用，则释放之前的占用额度
        }
    }
    /**
     * 判断还押汇金额+付汇金额是否大于开证金额，如果大于则释放占用保证金金额
     * @param lcEnrol
     * @param supplementPaymentService
     * @param lcDepositService
     * @param bondHookService
     */
    public static void handleReleaseBond(LcEnrol lcEnrol, SupplementPaymentService supplementPaymentService, LcDepositService lcDepositService, IFcBondHookService bondHookService){
        BigDecimal allPaymentMoney = supplementPaymentService.getAllPaymentMoneyByLcId(lcEnrol);
        BigDecimal allDepositMoney = lcDepositService.getAllPaymentMoneyByLcId(lcEnrol);
        if (lcEnrol.getCreditMoney().compareTo(allPaymentMoney.add(allDepositMoney)) <= 0) {
            releaseBond(lcEnrol, bondHookService);
        }
    }
    /**
     * 设置担保挂接数据
     * @param lcEnrol
     * @param guaContractOccupDTO
     */
    public static void handleGua(LcEnrol lcEnrol, GuaContractOccupDTO guaContractOccupDTO, String contractId) {
        guaContractOccupDTO.setBusinessId(lcEnrol.getId());
        if (StringUtils.isNotEmpty(contractId)) {
            if (contractId.contains(",")) {
                guaContractOccupDTO.setContractIds(Arrays.asList(contractId.split(",")));
            } else {
                guaContractOccupDTO.setContractIds(Arrays.asList(contractId));
            }
        }
        guaContractOccupDTO.setSysCode(ConfigConstant.LCS);
        guaContractOccupDTO.setItemNo(lcEnrol.getLcNo());
        guaContractOccupDTO.setUseDate(new Date());
        guaContractOccupDTO.setGuaRmk(lcEnrol.getGuaranteeRmk());
    }
    /**
     * 担保处理
     *
     * @param NewlcEnrol
     * @param OldlcEnrol
     * @param guaContractOccupServie
     */
    public static void handleGua(LcEnrol NewlcEnrol, LcEnrol OldlcEnrol, IGuaContractOccupServie guaContractOccupServie, Integer status, String contractId) {
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        if (InternationalConstant.IS_HANG_GUARANTEE_YES.equals(NewlcEnrol.getIsHangGuarantee())) {
            CreditUtil.handleGua(NewlcEnrol, guaContractOccupDTO, contractId);
            //0暂存，1在审使用，2使用中，3释放,4打回
            if (0 == status) {
                guaContractOccupServie.tempUseGuaContractOccup(guaContractOccupDTO);
            } else if (1 == status) {
                guaContractOccupServie.submitUseGuaContractOccup(guaContractOccupDTO);
            } else if (2 == status) {
                guaContractOccupServie.saveUseGuaContractOccup(guaContractOccupDTO);
            } else if (3 == status) {
                guaContractOccupServie.releaseUseGuaContractOccup(guaContractOccupDTO);
            } else if (4 == status) {
                guaContractOccupServie.backUseGuaContractOccup(guaContractOccupDTO);
            }
        } else {
            if (null != OldlcEnrol) {
                if (InternationalConstant.IS_HANG_GUARANTEE_YES.equals(OldlcEnrol.getIsHangGuarantee())) {
                    guaContractOccupServie.deleteGuaContractOccup(OldlcEnrol.getId(), "lcs");
                }
            }
        }
    }
    /**
     * 担保参数设置
     * @param model
     * @param lcEnrol 实体对象
     * @param guaWayServie
     */
    public static void setGuaParams(Model model, LcEnrol lcEnrol, IGuaWayServie guaWayServie, String isGuarantee, String viewFlag, IGuaContractOccupServie guaContractOccupServie){
        //        担保数据设置
        if (InternationalConstant.IS_HANG_GUARANTEE_YES.equals(lcEnrol.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(lcEnrol.getId());
            guaContractOccupDTO = guaContractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
            String contractIds = null;
            if (EmptyUtils.isNotEmpty(guaContractOccupDTO.getContractIds()) &&
                    guaContractOccupDTO.getContractIds().size() > 1) {
                for (String contractId : guaContractOccupDTO.getContractIds()) {
                    if (contractIds == null) {
                        contractIds = contractId;
                    } else {
                        contractIds += ","+contractId;
                    }
                }
            } else {
                contractIds = guaContractOccupDTO.getContractIds().get(0);
            }
            model.addAttribute("contractIds", contractIds);
            model.addAttribute("idList", guaContractOccupDTO.getContractIds());
            model.addAttribute("guaranteeRmk", lcEnrol.getGuaranteeRmk());
            model.addAttribute("guaWayNames", guaContractOccupDTO.getGuaWayName());
            model.addAttribute("guaWay", guaContractOccupDTO.getGuaWay());
            model.addAttribute("contractCode", guaContractOccupDTO.getContractCode());
        }
         model.addAttribute("viewFlag", viewFlag);//<!-- 查看标志；1—为查看页面-->
        model.addAttribute("isGuarantee", isGuarantee);
        model.addAttribute("isHangGuarantee", lcEnrol.getIsHangGuarantee());
    }
    /**
     * 设置被开证单位
     * @param lcEnrol
     * @param corpService
     * @param model
     */
    public static void setReCorpName(LcEnrol lcEnrol, ICorpService corpService, Model model){
        //        被开证单位
        if (InternationalConstant.ISRECREDIT_Y.equals(lcEnrol.getIsReCredit())) {
            String reCorpNames = "";
            if (lcEnrol.getReCorpCode() != null && lcEnrol.getReCorpCode().contains(",")) {
                String[] reCorpNameArr = lcEnrol.getReCorpCode().split(",");
                for (int i = 0; i < reCorpNameArr.length; i++) {
                    if (i == 0) {
                        reCorpNames = corpService.getCorpByCode(reCorpNameArr[i]).getName();
                    } else {
                        reCorpNames += "," + corpService.getCorpByCode(reCorpNameArr[i]).getName();
                    }
                }
            } else {
                reCorpNames = corpService.getCorpByCode(lcEnrol.getReCorpCode()).getName();
            }
            model.addAttribute("reCorpName",reCorpNames);
        }
    }
    /**
     * 相关参数设置
     * @param lcEnrol
     * @param btBankAccService
     * @param urationService
     * @param model
     */
    public static LcEnrolVO setParams(LcEnrol lcEnrol, IBtBankAccService btBankAccService, IRatUrationService urationService, IBtCurrencyService currencyService, Model model){
        LcEnrolVO lcEnrolVO = new LcEnrolVO();
        BeanUtils.copyProperties(lcEnrol,lcEnrolVO);
        // 注入保证金币别名称
        lcEnrolVO.setBondCurName((StrUtil.isBlank(lcEnrolVO.getBondCurId()) ? null : (currencyService.getObjectById(lcEnrolVO.getBondCurId()).getCurName())));
        lcEnrolVO.setRatExchangeRate(lcEnrol.getExchangeRate());
        model.addAttribute("lcEnrol", lcEnrolVO);
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(lcEnrol.getApplicantAcc());
        if (bankAccInfo != null) {
            model.addAttribute("applicantAccShow", bankAccInfo.getBankName());
            if(null!=bankAccInfo.getCurNameList() &&bankAccInfo.getCurNameList().size()>0){
                model.addAttribute("curName", StringUtils.listTtoString(bankAccInfo.getCurNameList()));
            }
        }
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.LCS,lcEnrol.getId());
            model.addAttribute("ratUration", ratUrationDTO);
            String RatBankCtl = urationService.getRatBankCtlConfig();
            model.addAttribute("ratBankConfig", RatBankCtl);
            if (CommonConstant.YES.equals(RatBankCtl)) {
                model.addAttribute("applicantAccShow", btBankAccService.getBankAccInfo(lcEnrol.getApplicantAcc()).getBankName());
            }
        }
        return lcEnrolVO;
    }
}
