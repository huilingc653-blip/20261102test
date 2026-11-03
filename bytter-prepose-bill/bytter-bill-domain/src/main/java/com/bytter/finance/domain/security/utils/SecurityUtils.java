package com.bytter.finance.domain.security.utils;

import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.SecurityConstant;
import com.bytter.finance.domain.security.entity.CmsSecurityIssue;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * @Title
 * @Description
 * @Author tongqr
 * @Date 2022-08-17
 * @Version 1.0
 */
public abstract class SecurityUtils {
    public static void setGuaParams(Model model, String viewFlag, String isGuarantee, CmsSecurityIssue cmsSecurityIssue,
            IGuaContractOccupServie guaContractOccupServie) {
        // 担保数据设置
        if (CmsConstant.IS_TRUE.equals(cmsSecurityIssue.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(cmsSecurityIssue.getId());
            guaContractOccupDTO = guaContractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
            String contractIds = null;
            if (guaContractOccupDTO.getContractIds().size() > 1) {
                for (String contractId : guaContractOccupDTO.getContractIds()) {
                    if (contractIds == null) {
                        contractIds = contractId;
                    } else {
                        contractIds += "," + contractId;
                    }
                }
            } else {
                contractIds = guaContractOccupDTO.getContractIds().get(0);
            }
            model.addAttribute("contractIds", contractIds);
            model.addAttribute("idList", guaContractOccupDTO.getContractIds());
            model.addAttribute("guaranteeRmk", cmsSecurityIssue.getGuaRmk());
            model.addAttribute("guaWayNames", guaContractOccupDTO.getGuaWayName());
            model.addAttribute("guaWay", guaContractOccupDTO.getGuaWay());
            model.addAttribute("contractCode", guaContractOccupDTO.getContractCode());
        }
        model.addAttribute("viewFlag", viewFlag);// <!-- 查看标志；1—为查看页面-->
        model.addAttribute("isGuarantee", isGuarantee);
        model.addAttribute("isHangGuarantee", cmsSecurityIssue.getIsHangGuarantee());
    }
    /**
     * 担保处理
     *
     * @param newCmsSecurityIssue
     * @param oldCmsSecurityIssueo
     * @param guaContractOccupServie
     */
    public static void handleGua(CmsSecurityIssue newCmsSecurityIssue, CmsSecurityIssue oldCmsSecurityIssueo,
            IGuaContractOccupServie guaContractOccupServie, Integer status, String contractId) {
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        if (CmsConstant.IS_TRUE.equals(newCmsSecurityIssue.getIsHangGuarantee())) {
            handleGua(newCmsSecurityIssue, guaContractOccupDTO, contractId);
            // 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
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
            } else if (5 == status) {
                guaContractOccupServie.sendBackUseGuaContractOccup(guaContractOccupDTO);
            }
        } else {
            if (null != oldCmsSecurityIssueo) {
                if (CmsConstant.IS_TRUE.equals(oldCmsSecurityIssueo.getIsHangGuarantee())) {
                    guaContractOccupServie.deleteGuaContractOccup(oldCmsSecurityIssueo.getId(),
                            SecurityConstant.SECURITY_GUA_SYSCODE);
                }
            }
        }
    }
    /**
     * 设置担保挂接数据
     *
     * @param cmsSecurityIssue
     * @param guaContractOccupDTO
     */
    public static void handleGua(CmsSecurityIssue cmsSecurityIssue, GuaContractOccupDTO guaContractOccupDTO,
            String contractId) {
        guaContractOccupDTO.setBusinessId(cmsSecurityIssue.getId());
        if (StringUtils.isNotEmpty(contractId)) {
            if (contractId.contains(",")) {
                guaContractOccupDTO.setContractIds(Arrays.asList(contractId.split(",")));
            } else {
                guaContractOccupDTO.setContractIds(Arrays.asList(contractId));
            }
        }
        guaContractOccupDTO.setSysCode(SecurityConstant.SECURITY_GUA_SYSCODE);
        guaContractOccupDTO.setItemNo(cmsSecurityIssue.getSecurityIssueCode());
        guaContractOccupDTO.setUseDate(new Date());
        guaContractOccupDTO.setGuaRmk(cmsSecurityIssue.getGuaRmk());
    }
    /**
     * 保证金参数设置
     *
     * @param model
     * @param isBond
     * @param cmsSecurityIssue
     * @param fcBondHookService
     */
    public static void setBondParam(Model model, String isBond, CmsSecurityIssue cmsSecurityIssue,
            IFcBondHookService fcBondHookService) {
        model.addAttribute("configIsHookBond", isBond);
        // 授信参数是否挂保证金
        if ("1".equals(isBond)) {
            if (FcBondConstant.IS_HOOK_Y.equals(cmsSecurityIssue.getIsHookBond())) {
                // 存在挂接关系
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(cmsSecurityIssue.getId(),
                        cmsSecurityIssue.getSecurityIssueCode());
                model.addAttribute("dto", dto);
            }
            model.addAttribute("isHookBond", cmsSecurityIssue.getIsHookBond());
            model.addAttribute("bondBankAcc", cmsSecurityIssue.getBondBankacc());
            model.addAttribute("bondCurId", cmsSecurityIssue.getBondCurId());
            model.addAttribute("bailmoneyRate", cmsSecurityIssue.getBailmoneyRate());
            model.addAttribute("bailmoney", cmsSecurityIssue.getOriBailmoney());
            model.addAttribute("bailmoneyContract", cmsSecurityIssue.getBailmoneyContract());
            model.addAttribute("bondRate", cmsSecurityIssue.getBondRate());
        }
    }
    public static void setPledgeParam(Model model, String pledgeConfig, CmsSecurityIssue cmsSecurityIssue,
            IGuaPledgeOccupyService pledgeOccupyService) {
        model.addAttribute("pledgeConfig", pledgeConfig);// 参数设置是否挂抵质押物
        if (null != pledgeConfig && pledgeConfig.equals(CmsConstant.IS_TRUE)) {
            model.addAttribute("isRelatePledge", cmsSecurityIssue.getIsRelatePledge());
            if (null != cmsSecurityIssue.getIsRelatePledge()
                    && cmsSecurityIssue.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
                List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyService.getPledgeOccupy(cmsSecurityIssue.getId(),
                        cmsSecurityIssue.getSecurityIssueCode());
                model.addAttribute("pledgeDTOS", occupyDTOS);
                model.addAttribute("itemNo", cmsSecurityIssue.getSecurityIssueCode());
                model.addAttribute("businessId", cmsSecurityIssue.getId());
            }
        }
    }
}
