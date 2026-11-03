package com.bytter.finance.domain.bond.utils;

import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.bond.entity.CmsBondIssueInfo;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.system.api.constant.ConfigConstant;
import org.springframework.ui.Model;

import java.util.*;

/**
 * 债券模块挂接第三方业务工具类
 *
 * @author ouyj
 */
public abstract class BondUtils {
    /**
     * 设置担保参数
     *
     * @param model
     *            model
     * @param viewFlag
     *            是否查看页面
     * @param isGuarantee
     *            是否挂接担保
     * @param cmsBondIssueInfo
     *            债券发行实体
     * @param guaContractOccupyService
     *            挂接担保service
     */
    public static void setGuaParams(Model model, String viewFlag, String isGuarantee, CmsBondIssueInfo cmsBondIssueInfo,
            IGuaContractOccupServie guaContractOccupyService) {
        // 担保数据设置
        if (CmsConstant.IS_TRUE.equals(cmsBondIssueInfo.getIsHookGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(cmsBondIssueInfo.getId());
            guaContractOccupDTO = guaContractOccupyService.getUseGuaContractOccup(guaContractOccupDTO);
            StringBuilder contractIds = null;
            if (guaContractOccupDTO.getContractIds().size() > 1) {
                for (String contractId : guaContractOccupDTO.getContractIds()) {
                    if (contractIds == null) {
                        contractIds = contractId == null ? null : new StringBuilder(contractId);
                    } else {
                        contractIds.append(",").append(contractId);
                    }
                }
            } else {
                contractIds = new StringBuilder(guaContractOccupDTO.getContractIds().get(0));
            }
            model.addAttribute("contractIds", contractIds == null ? null : contractIds.toString());
            model.addAttribute("idList", guaContractOccupDTO.getContractIds());
            model.addAttribute("guaranteeRmk", cmsBondIssueInfo.getGuaranteeRmk());
            model.addAttribute("guaWayNames", guaContractOccupDTO.getGuaWayName());
            model.addAttribute("guaWay", guaContractOccupDTO.getGuaWay());
            model.addAttribute("contractCode", guaContractOccupDTO.getContractCode());
        }
        // 查看标志；1—为查看页面
        model.addAttribute("viewFlag", viewFlag);
        model.addAttribute("isGuarantee", isGuarantee);
        model.addAttribute("isHangGuarantee", cmsBondIssueInfo.getIsHookGuarantee());
    }

    public static void setGuaParams(Map<String,Object> model, String viewFlag, String isGuarantee, CmsBondIssueInfo cmsBondIssueInfo,
                                    IGuaContractOccupServie guaContractOccupyService) {
        // 担保数据设置
        if (CmsConstant.IS_TRUE.equals(cmsBondIssueInfo.getIsHookGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(cmsBondIssueInfo.getId());
            guaContractOccupDTO = guaContractOccupyService.getUseGuaContractOccup(guaContractOccupDTO);
            StringBuilder contractIds = null;
            if (guaContractOccupDTO.getContractIds().size() > 1) {
                for (String contractId : guaContractOccupDTO.getContractIds()) {
                    if (contractIds == null) {
                        contractIds = contractId == null ? null : new StringBuilder(contractId);
                    } else {
                        contractIds.append(",").append(contractId);
                    }
                }
            } else {
                contractIds = new StringBuilder(guaContractOccupDTO.getContractIds().get(0));
            }
            model.put("contractIds", contractIds == null ? null : contractIds.toString());
            model.put("idList", guaContractOccupDTO.getContractIds());
            model.put("guaranteeRmk", cmsBondIssueInfo.getGuaranteeRmk());
            model.put("guaWayNames", guaContractOccupDTO.getGuaWayName());
            model.put("guaWay", guaContractOccupDTO.getGuaWay());
            model.put("contractCode", guaContractOccupDTO.getContractCode());
        }
        // 查看标志；1—为查看页面
        model.put("viewFlag", viewFlag);
        model.put("isGuarantee", isGuarantee);
        model.put("isHangGuarantee", cmsBondIssueInfo.getIsHookGuarantee());
    }
    /**
     * 担保处理
     *
     * @param newCmsBondIssueInfo
     * @param oldCmsBondIssueInfo
     * @param goaContractOccupyService
     */
    public static void handleGua(CmsBondIssueInfo newCmsBondIssueInfo, CmsBondIssueInfo oldCmsBondIssueInfo,
            IGuaContractOccupServie goaContractOccupyService, Integer status, String contractId) {
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        if (CmsConstant.IS_TRUE.equals(newCmsBondIssueInfo.getIsHookGuarantee())) {
            handleGua(newCmsBondIssueInfo, guaContractOccupDTO, contractId);
            // 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
            if (0 == status) {
                goaContractOccupyService.tempUseGuaContractOccup(guaContractOccupDTO);
            } else if (1 == status) {
                goaContractOccupyService.submitUseGuaContractOccup(guaContractOccupDTO);
            } else if (2 == status) {
                goaContractOccupyService.saveUseGuaContractOccup(guaContractOccupDTO);
            } else if (3 == status) {
                goaContractOccupyService.releaseUseGuaContractOccup(guaContractOccupDTO);
            } else if (4 == status) {
                goaContractOccupyService.backUseGuaContractOccup(guaContractOccupDTO);
            } else if (5 == status) {
                goaContractOccupyService.sendBackUseGuaContractOccup(guaContractOccupDTO);
            }
        } else {
            if (null != oldCmsBondIssueInfo) {
                if (CmsConstant.IS_TRUE.equals(oldCmsBondIssueInfo.getIsHookGuarantee())) {
                    goaContractOccupyService.deleteGuaContractOccup(oldCmsBondIssueInfo.getId(),
                            ConfigConstant.CMSBOND);
                }
            }
        }
    }
    /**
     * 设置担保挂接数据
     *
     * @param cmsBondIssueInfo
     * @param guaContractOccupDTO
     */
    public static void handleGua(CmsBondIssueInfo cmsBondIssueInfo, GuaContractOccupDTO guaContractOccupDTO,
            String contractId) {
        guaContractOccupDTO.setBusinessId(cmsBondIssueInfo.getId());
        if (StringUtils.isNotEmpty(contractId)) {
            if (contractId.contains(",")) {
                guaContractOccupDTO.setContractIds(Arrays.asList(contractId.split(",")));
            } else {
                guaContractOccupDTO.setContractIds(Collections.singletonList(contractId));
            }
        }
        guaContractOccupDTO.setSysCode(ConfigConstant.CMSBOND);
        guaContractOccupDTO.setItemNo(cmsBondIssueInfo.getBondIssueNum());
        guaContractOccupDTO.setUseDate(new Date());
        guaContractOccupDTO.setGuaRmk(cmsBondIssueInfo.getGuaranteeRmk());
    }
    /**
     * 保证金参数设置
     *
     * @param model
     *            model
     * @param isBond
     *            是否挂接保证金
     * @param cmsBondIssueInfo
     *            债券发行信息
     * @param fcBondHookService
     *            挂接保证金service
     */
    public static void setBondParam(Model model, String isBond, CmsBondIssueInfo cmsBondIssueInfo,
            IFcBondHookService fcBondHookService) {
        model.addAttribute("configIsHookBond", isBond);
        // 授信参数是否挂保证金
        if ("1".equals(isBond)) {
            if (FcBondConstant.IS_HOOK_Y.equals(cmsBondIssueInfo.getIsHookBail())) {
                // 存在挂接关系
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(cmsBondIssueInfo.getId(),
                        cmsBondIssueInfo.getBondIssueNum());
                model.addAttribute("dto", dto);
            }
            model.addAttribute("isHookBond", cmsBondIssueInfo.getIsHookBail());
            model.addAttribute("bondBankAcc", cmsBondIssueInfo.getBailBankAcc());
            model.addAttribute("bondCurId", cmsBondIssueInfo.getBailCurId());
            model.addAttribute("bailmoneyRate", cmsBondIssueInfo.getBailMoneyRate());
            model.addAttribute("bailmoney", cmsBondIssueInfo.getOriBailMoney());
            model.addAttribute("bailmoneyContract", cmsBondIssueInfo.getBailMoneyContract());
            model.addAttribute("bondRate", cmsBondIssueInfo.getBondRate());
        }
    }

    public static void setBondParam(Map<String,Object> model, String isBond, CmsBondIssueInfo cmsBondIssueInfo,
                                    IFcBondHookService fcBondHookService) {
        model.put("configIsHookBond", isBond);
        // 授信参数是否挂保证金
        if ("1".equals(isBond)) {
            if (FcBondConstant.IS_HOOK_Y.equals(cmsBondIssueInfo.getIsHookBail())) {
                // 存在挂接关系
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(cmsBondIssueInfo.getId(),
                        cmsBondIssueInfo.getBondIssueNum());
                model.put("dto", dto);
            }
            model.put("isHookBond", cmsBondIssueInfo.getIsHookBail());
            model.put("bondBankAcc", cmsBondIssueInfo.getBailBankAcc());
            model.put("bondCurId", cmsBondIssueInfo.getBailCurId());
            model.put("bailmoneyRate", cmsBondIssueInfo.getBailMoneyRate());
            model.put("bailmoney", cmsBondIssueInfo.getOriBailMoney());
            model.put("bailmoneyContract", cmsBondIssueInfo.getBailMoneyContract());
            model.put("bondRate", cmsBondIssueInfo.getBondRate());
        }
    }
    /**
     * 设置抵质押物参数
     *
     * @param model
     *            model
     * @param pledgeConfig
     *            是否挂接
     * @param cmsBondIssueInfo
     *            债券发行实体
     * @param pledgeOccupyService
     *            挂接抵质押物service
     */
    public static void setPledgeParam(Model model, String pledgeConfig, CmsBondIssueInfo cmsBondIssueInfo,
            IGuaPledgeOccupyService pledgeOccupyService) {
        // 参数设置是否挂抵质押物
        model.addAttribute("pledgeConfig", pledgeConfig);
        if (null != pledgeConfig && pledgeConfig.equals(CmsConstant.IS_TRUE)) {
            model.addAttribute("isRelatePledge", cmsBondIssueInfo.getIsHookPledge());
            if (null != cmsBondIssueInfo.getIsHookPledge()
                    && cmsBondIssueInfo.getIsHookPledge().equals(CmsConstant.IS_TRUE)) {
                List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyService.getPledgeOccupy(cmsBondIssueInfo.getId(),
                        cmsBondIssueInfo.getBondIssueNum());
                model.addAttribute("pledgeDTOS", occupyDTOS);
                model.addAttribute("itemNo", cmsBondIssueInfo.getBondIssueNum());
                model.addAttribute("businessId", cmsBondIssueInfo.getId());
            }
        }
    }
    public static void setPledgeParam(Map<String,Object> model, String pledgeConfig, CmsBondIssueInfo cmsBondIssueInfo,
                                      IGuaPledgeOccupyService pledgeOccupyService) {
        // 参数设置是否挂抵质押物
        model.put("pledgeConfig", pledgeConfig);
        if (null != pledgeConfig && pledgeConfig.equals(CmsConstant.IS_TRUE)) {
            model.put("isRelatePledge", cmsBondIssueInfo.getIsHookPledge());
            if (null != cmsBondIssueInfo.getIsHookPledge()
                    && cmsBondIssueInfo.getIsHookPledge().equals(CmsConstant.IS_TRUE)) {
                List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyService.getPledgeOccupy(cmsBondIssueInfo.getId(),
                        cmsBondIssueInfo.getBondIssueNum());
                model.put("pledgeDTOS", occupyDTOS);
                model.put("itemNo", cmsBondIssueInfo.getBondIssueNum());
                model.put("businessId", cmsBondIssueInfo.getId());
            }
        }
    }
}
