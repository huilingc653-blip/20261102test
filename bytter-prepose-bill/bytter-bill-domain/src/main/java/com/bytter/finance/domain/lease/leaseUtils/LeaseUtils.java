package com.bytter.finance.domain.lease.leaseUtils;

import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.LeaseConstant;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author tongqr
 * @Date 2022-04-09
 * @Version 1.0
 */
public abstract class LeaseUtils {
    public static void setGuaParams(Model model, String viewFlag, String isGuarantee,
            CmsFinancingLeaseInfo cmsFinancingLeaseInfo, IGuaWayServie guaWayServie,
            IGuaContractOccupServie guaContractOccupServie) {
        // 担保数据设置
        if (CmsConstant.IS_TRUE.equals(cmsFinancingLeaseInfo.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(cmsFinancingLeaseInfo.getId());
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
            model.addAttribute("guaranteeRmk", cmsFinancingLeaseInfo.getGuaRmk());
            model.addAttribute("guaWayNames", guaContractOccupDTO.getGuaWayName());
            model.addAttribute("guaWay", guaContractOccupDTO.getGuaWay());
            model.addAttribute("contractCode", guaContractOccupDTO.getContractCode());
        }
        model.addAttribute("viewFlag", viewFlag);// <!-- 查看标志；1—为查看页面-->
        model.addAttribute("isGuarantee", isGuarantee);
        model.addAttribute("isHangGuarantee", cmsFinancingLeaseInfo.getIsHangGuarantee());
    }
    /**
     * 担保参数设置（Map版本，供前后端分离接口使用）
     */
    public static void setGuaParams(Map<String, Object> model, String viewFlag, String isGuarantee,
            CmsFinancingLeaseInfo cmsFinancingLeaseInfo, IGuaWayServie guaWayServie,
            IGuaContractOccupServie guaContractOccupServie) {
        // 担保数据设置
        if (CmsConstant.IS_TRUE.equals(cmsFinancingLeaseInfo.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(cmsFinancingLeaseInfo.getId());
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
            model.put("contractIds", contractIds);
            model.put("idList", guaContractOccupDTO.getContractIds());
            model.put("guaranteeRmk", cmsFinancingLeaseInfo.getGuaRmk());
            model.put("guaWayNames", guaContractOccupDTO.getGuaWayName());
            model.put("guaWay", guaContractOccupDTO.getGuaWay());
            model.put("contractCode", guaContractOccupDTO.getContractCode());
        }
        model.put("viewFlag", viewFlag);// <!-- 查看标志；1—为查看页面-->
        model.put("isGuarantee", isGuarantee);
        model.put("isHangGuarantee", cmsFinancingLeaseInfo.getIsHangGuarantee());
    }
    /**
     * 担保处理
     *
     * @param newCmsFinancingLeaseInfo
     * @param oldCmsFinancingLeaseInfo
     * @param guaContractOccupServie
     */
    public static void handleGua(CmsFinancingLeaseInfo newCmsFinancingLeaseInfo,
            CmsFinancingLeaseInfo oldCmsFinancingLeaseInfo, IGuaContractOccupServie guaContractOccupServie,
            Integer status, String contractId) {
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        if (CmsConstant.IS_TRUE.equals(newCmsFinancingLeaseInfo.getIsHangGuarantee())) {
            handleGua(newCmsFinancingLeaseInfo, guaContractOccupDTO, contractId);
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
            if (null != oldCmsFinancingLeaseInfo) {
                if (CmsConstant.IS_TRUE.equals(oldCmsFinancingLeaseInfo.getIsHangGuarantee())) {
                    guaContractOccupServie.deleteGuaContractOccup(oldCmsFinancingLeaseInfo.getId(),
                            LeaseConstant.LEASE_GUA_SYSCODE);
                }
            }
        }
    }
    /**
     * 设置担保挂接数据
     *
     * @param cmsFinancingLeaseInfo
     * @param guaContractOccupDTO
     */
    public static void handleGua(CmsFinancingLeaseInfo cmsFinancingLeaseInfo, GuaContractOccupDTO guaContractOccupDTO,
            String contractId) {
        guaContractOccupDTO.setBusinessId(cmsFinancingLeaseInfo.getId());
        if (StringUtils.isNotEmpty(contractId)) {
            if (contractId.contains(",")) {
                guaContractOccupDTO.setContractIds(Arrays.asList(contractId.split(",")));
            } else {
                guaContractOccupDTO.setContractIds(Arrays.asList(contractId));
            }
        }
        guaContractOccupDTO.setSysCode(LeaseConstant.LEASE_GUA_SYSCODE);
        guaContractOccupDTO.setItemNo(cmsFinancingLeaseInfo.getLeaseNo());
        guaContractOccupDTO.setUseDate(new Date());
        guaContractOccupDTO.setGuaRmk(cmsFinancingLeaseInfo.getGuaRmk());
    }
    /**
     * 保证金参数设置
     * 
     * @param model
     * @param isBond
     * @param cmsFinancingLeaseInfo
     * @param fcBondHookService
     */
    public static void setBondParam(Model model, String isBond, CmsFinancingLeaseInfo cmsFinancingLeaseInfo,
            IFcBondHookService fcBondHookService) {
        model.addAttribute("configIsHookBond", isBond);
        // 授信参数是否挂保证金
        if ("1".equals(isBond)) {
            if (FcBondConstant.IS_HOOK_Y.equals(cmsFinancingLeaseInfo.getIsHookBond())) {
                // 存在挂接关系
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(cmsFinancingLeaseInfo.getId(),
                        cmsFinancingLeaseInfo.getLeaseNo());
                model.addAttribute("dto", dto);
            }
            model.addAttribute("isHookBond", cmsFinancingLeaseInfo.getIsHookBond());
            model.addAttribute("bondBankAcc", cmsFinancingLeaseInfo.getBondBankAcc());
            model.addAttribute("bondCurId", cmsFinancingLeaseInfo.getBondCurId());
            model.addAttribute("bailmoneyRate", cmsFinancingLeaseInfo.getBailmoneyRate());
            model.addAttribute("bailmoney", cmsFinancingLeaseInfo.getOriBailmoney());
            model.addAttribute("bailmoneyContract", cmsFinancingLeaseInfo.getBailmoneyContract());
            model.addAttribute("bondRate", cmsFinancingLeaseInfo.getBondRate());
        }
    }
    /**
     * 保证金参数设置（Map版本，供前后端分离接口使用）
     */
    public static void setBondParam(Map<String, Object> model, String isBond,
            CmsFinancingLeaseInfo cmsFinancingLeaseInfo, IFcBondHookService fcBondHookService) {
        model.put("configIsHookBond", isBond);
        // 授信参数是否挂保证金
        if ("1".equals(isBond)) {
            if (FcBondConstant.IS_HOOK_Y.equals(cmsFinancingLeaseInfo.getIsHookBond())) {
                // 存在挂接关系
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(cmsFinancingLeaseInfo.getId(),
                        cmsFinancingLeaseInfo.getLeaseNo());
                model.put("dto", dto);
            }
            model.put("isHookBond", cmsFinancingLeaseInfo.getIsHookBond());
            model.put("bondBankAcc", cmsFinancingLeaseInfo.getBondBankAcc());
            model.put("bondCurId", cmsFinancingLeaseInfo.getBondCurId());
            model.put("bailmoneyRate", cmsFinancingLeaseInfo.getBailmoneyRate());
            model.put("bailmoney", cmsFinancingLeaseInfo.getOriBailmoney());
            model.put("bailmoneyContract", cmsFinancingLeaseInfo.getBailmoneyContract());
            model.put("bondRate", cmsFinancingLeaseInfo.getBondRate());
        }
    }
    public static void setPledgeParam(Model model, String pledgeConfig, CmsFinancingLeaseInfo cmsFinancingLeaseInfo,
            IGuaPledgeOccupyService pledgeOccupyService) {
        model.addAttribute("pledgeConfig", pledgeConfig);// 参数设置是否挂抵质押物
        if (null != pledgeConfig && pledgeConfig.equals(CmsConstant.IS_TRUE)) {
            model.addAttribute("isRelatePledge", cmsFinancingLeaseInfo.getIsRelatePledge());
            if (null != cmsFinancingLeaseInfo.getIsRelatePledge()
                    && cmsFinancingLeaseInfo.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
                List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyService.getPledgeOccupy(cmsFinancingLeaseInfo.getId(),
                        cmsFinancingLeaseInfo.getLeaseNo());
                model.addAttribute("pledgeDTOS", occupyDTOS);
                model.addAttribute("itemNo", cmsFinancingLeaseInfo.getLeaseNo());
                model.addAttribute("businessId", cmsFinancingLeaseInfo.getId());
            }
        }
    }
    /**
     * 抵质押参数设置（Map版本，供前后端分离接口使用）
     */
    public static void setPledgeParam(Map<String, Object> model, String pledgeConfig,
            CmsFinancingLeaseInfo cmsFinancingLeaseInfo, IGuaPledgeOccupyService pledgeOccupyService) {
        model.put("pledgeConfig", pledgeConfig);// 参数设置是否挂抵质押物
        if (null != pledgeConfig && pledgeConfig.equals(CmsConstant.IS_TRUE)) {
            model.put("isRelatePledge", cmsFinancingLeaseInfo.getIsRelatePledge());
            if (null != cmsFinancingLeaseInfo.getIsRelatePledge()
                    && cmsFinancingLeaseInfo.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
                List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyService.getPledgeOccupy(cmsFinancingLeaseInfo.getId(),
                        cmsFinancingLeaseInfo.getLeaseNo());
                model.put("pledgeDTOS", occupyDTOS);
                model.put("itemNo", cmsFinancingLeaseInfo.getLeaseNo());
                model.put("businessId", cmsFinancingLeaseInfo.getId());
            }
        }
    }
}
