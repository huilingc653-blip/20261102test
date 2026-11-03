package com.bytter.finance.domain.inl;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.inl.entity.CmsInlInfo;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlRepayService;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import org.springframework.ui.Model;
import java.util.Map;
public class InlUtil {
    /**
     * 内部拆借申请
     * 
     * @return
     */
    public static String getInlInfoNo() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_INL_INFO_BILL_NO, InlConstant.CMS_INL_INFO_NO);
    }
    /**
     * 内部拆借发放
     * 
     * @return
     */
    public static String getInlSendNo() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_INL_SEND_BILL_NO, InlConstant.CMS_INL_SEND_NO);
    }
    /**
     * 内部拆借申请还款
     * 
     * @return
     */
    public static String getInlRepayNo() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_INL_REPAY_BILL_NO, InlConstant.CMS_INL_REPAY_NO);
    }
    /**
     * 内部拆借作废
     * 
     * @return
     */
    public static String getInlAbolishNo() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_INL_ABOLISH_BILL_NO, InlConstant.CMS_INL_ABOLIS_NO);
    }
    /**
     * 内部拆借作废
     * 
     * @return
     */
    public static String getInlDefereedNo() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_INL_DEFERRED_BILL_NO, InlConstant.CMS_INL_DEFERRED_NO);
    }
    /**
     * 内部拆借作废
     * 
     * @return
     */
    public static String getInlAdjuestrateNo() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_INL_ADJUSTRATE_BILL_NO,
                InlConstant.CMS_INL_ADJUSTRATE_NO);
    }
    /**
     * 内部拆借作废
     * 
     * @return
     */
    public static String getInlReplayChangeNo() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_INL_REPLAYCHANGE_BILL_NO,
                InlConstant.CMS_INL_REPAYCAHNEG_NO);
    }
    /**
     * 参数设置
     * 
     * @param configService
     * @param model
     * @param id
     * @param inlInfoService
     * @param conService
     * @param guaContractOccupServie
     */
    public static void setParams(IConfigService configService, Model model, String id, InlInfoService inlInfoService,
            IConService conService, IGuaContractOccupServie guaContractOccupServie) {
        CmsInlInfo cmsInlInfo = inlInfoService.get(id);
        Map<String, Object> config = configService.listByType(ConfigConstant.INL);
        model.addAttribute("config", config);
        Map<String, Object> inlConfig = (Map<String, Object>) config.get(ConfigConstant.INL);
        if (null != config) {
            if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.IS_HANG_GUA).toString())) {
                if (CommonConstant.YES.equals(cmsInlInfo.getIsHangGuarantee())) {
                    model.addAttribute("guaInfo", guaContractOccupServie.getGuaContractOccupyDtl(cmsInlInfo.getId(),
                            cmsInlInfo.getGuaContract()));
                }
            }
            if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.IS_CON).toString())) {
                if (CommonConstant.YES.equals(cmsInlInfo.getIsCon())) {
                    ConInfoDTO conInfoDTO = conService.getConInfo(cmsInlInfo.getConId(), cmsInlInfo.getId());
                    model.addAttribute("conInfo", conInfoDTO);
                    if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.CON_TYPE).toString())
                            && null != conInfoDTO) {
                        model.addAttribute("conTypeId", conInfoDTO.getTypeId());
                    }
                }
            }
            if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.IS_HANG_CREDIT).toString())) {
                if (CommonConstant.YES.equals(cmsInlInfo.getIsHangCredit().toString())) {
                    model.addAttribute("cpURationDTO", inlInfoService.getCpratUrationDTO(cmsInlInfo.getId()));
                }
            }
        }
    }

    public static void setParams(IConfigService configService, Map<String,Object> model, String id, InlInfoService inlInfoService,
                                 IConService conService, IGuaContractOccupServie guaContractOccupServie) {
        CmsInlInfo cmsInlInfo = inlInfoService.get(id);
        Map<String, Object> config = configService.listByType(ConfigConstant.INL);
        model.put("config", config);
        Map<String, Object> inlConfig = (Map<String, Object>) config.get(ConfigConstant.INL);
        if (null != config) {
            if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.IS_HANG_GUA).toString())) {
                if (CommonConstant.YES.equals(cmsInlInfo.getIsHangGuarantee())) {
                    model.put("guaInfo", guaContractOccupServie.getGuaContractOccupyDtl(cmsInlInfo.getId(),
                            cmsInlInfo.getGuaContract()));
                }
            }
            if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.IS_CON).toString())) {
                if (CommonConstant.YES.equals(cmsInlInfo.getIsCon())) {
                    ConInfoDTO conInfoDTO = conService.getConInfo(cmsInlInfo.getConId(), cmsInlInfo.getId());
                    model.put("conInfo", conInfoDTO);
                    if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.CON_TYPE).toString())
                            && null != conInfoDTO) {
                        model.put("conTypeId", conInfoDTO.getTypeId());
                    }
                }
            }
            if (CommonConstant.YES.equals(inlConfig.get(BaseDataConstant.IS_HANG_CREDIT).toString())) {
                if (CommonConstant.YES.equals(cmsInlInfo.getIsHangCredit().toString())) {
                    model.put("cpURationDTO", inlInfoService.getCpratUrationDTO(cmsInlInfo.getId()));
                }
            }
        }
    }
    /**
     * 设置已还本金已还利息 在途本金+利息
     * 
     * @param inlRepayService
     * @param id
     * @param model
     */
    public static void setRepayInfo(InlRepayService inlRepayService, String id, Model model) {
        model.addAttribute("allReplayMoney", inlRepayService.getAllReplayMoney(id));
        model.addAttribute("allReplayInterest", inlRepayService.getAllReplayInterest(id));
        model.addAttribute("allReplayTotal", inlRepayService.getAllReplayTotal(id));
    }

    public static void setRepayInfo(InlRepayService inlRepayService, String id, Map<String,Object> model) {
        model.put("allReplayMoney", inlRepayService.getAllReplayMoney(id));
        model.put("allReplayInterest", inlRepayService.getAllReplayInterest(id));
        model.put("allReplayTotal", inlRepayService.getAllReplayTotal(id));
    }
    /**
     * 构建还本对象
     * 
     * @param repaymentSchedule
     * @param cmsInlSend
     * @param cmsRepaymentSchedule
     * @param user
     */
    public static void setRepaymentSchedule(ICurrentUser user) {
        throw new RuntimeException("实现方式修改");
    }
}
