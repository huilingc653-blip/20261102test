package com.bytter.finance.domain.provide.utils;

import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ProvideConstant;
import com.bytter.finance.domain.provide.entity.CmsLoanInfoViewPageVO;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-07-27 15:34
 * @Version 1.0
 */
public abstract class ProvideUtils {
    public static void setGuaParams(Model model, String viewFlag, String isGuarantee,
                                    CmsProvideLoanInfo cmsProvideLoanInfo, IGuaWayServie guaWayServie,
                                    IGuaContractOccupServie guaContractOccupServie) {
        // 担保数据设置
        if (CmsConstant.IS_TRUE.equals(cmsProvideLoanInfo.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(cmsProvideLoanInfo.getId());
            guaContractOccupDTO = guaContractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
            String contractIds = null;
            if(!EmptyUtils.isEmpty(guaContractOccupDTO.getContractIds())){
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
            }
            model.addAttribute("contractIds", contractIds);
            model.addAttribute("idList", guaContractOccupDTO.getContractIds());
            model.addAttribute("guaranteeRmk", cmsProvideLoanInfo.getGuaranteeRmk());
            model.addAttribute("guaWayNames", guaContractOccupDTO.getGuaWayName());
            model.addAttribute("guaWay", guaContractOccupDTO.getGuaWay());
            model.addAttribute("contractCode", guaContractOccupDTO.getContractCode());
        }
        model.addAttribute("viewFlag", viewFlag);// <!-- 查看标志；1—为查看页面-->
        model.addAttribute("isGuarantee", isGuarantee);
        model.addAttribute("isHangGuarantee", cmsProvideLoanInfo.getIsHangGuarantee());
    }

    /**
     * 设置担保挂接数据
     *
     * @param cmsProvideLoanInfo
     * @param guaContractOccupDTO
     */
    public static void handleGua(CmsProvideLoanInfo cmsProvideLoanInfo, GuaContractOccupDTO guaContractOccupDTO,
                                 String contractId) {
        guaContractOccupDTO.setBusinessId(cmsProvideLoanInfo.getId());
        if (StringUtils.isNotEmpty(contractId)) {
            if (contractId.contains(",")) {
                guaContractOccupDTO.setContractIds(Arrays.asList(contractId.split(",")));
            } else {
                guaContractOccupDTO.setContractIds(Arrays.asList(contractId));
            }
        }
        guaContractOccupDTO.setSysCode(ProvideConstant.PROVIDE_GUA_SYSCODE);
        guaContractOccupDTO.setItemNo(cmsProvideLoanInfo.getBillCode());
        guaContractOccupDTO.setUseDate(new Date());
        guaContractOccupDTO.setGuaRmk(cmsProvideLoanInfo.getGuaranteeRmk());
    }

    /**
     * 担保处理
     *
     * @param NewCmsProvideLoanInfo
     * @param OldCmsProvideLoanInfo
     * @param guaContractOccupServie
     */
    public static void handleGua(CmsProvideLoanInfo NewCmsProvideLoanInfo, CmsProvideLoanInfo OldCmsProvideLoanInfo,
                                 IGuaContractOccupServie guaContractOccupServie, Integer status, String contractId) {
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        if (CmsConstant.IS_TRUE.equals(NewCmsProvideLoanInfo.getIsHangGuarantee())) {
            handleGua(NewCmsProvideLoanInfo, guaContractOccupDTO, contractId);
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
            if (null != OldCmsProvideLoanInfo) {
                if (CmsConstant.IS_TRUE.equals(OldCmsProvideLoanInfo.getIsHangGuarantee())) {
                    guaContractOccupServie.deleteGuaContractOccup(OldCmsProvideLoanInfo.getId(),
                            ProvideConstant.PROVIDE_GUA_SYSCODE);
                }
            }
        }
    }

    /**
     * 保证金参数设置
     *
     * @param model
     * @param isBond
     * @param cmsProvideLoanInfo
     * @param fcBondHookService
     */
    public static void setBondParam(Model model, String isBond, CmsProvideLoanInfo cmsProvideLoanInfo,
                                    IFcBondHookService fcBondHookService) {
        model.addAttribute("configIsHookBond", isBond);
        // 授信参数是否挂保证金
        if ("1".equals(isBond)) {
            if (FcBondConstant.IS_HOOK_Y.equals(cmsProvideLoanInfo.getIsHookBond())) {
                // 存在挂接关系
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(cmsProvideLoanInfo.getId(),
                        cmsProvideLoanInfo.getBillCode());
                model.addAttribute("dto", dto);
            }
            model.addAttribute("isHookBond", cmsProvideLoanInfo.getIsHookBond());
            model.addAttribute("bondBankAcc", cmsProvideLoanInfo.getBondBankAcc());
            model.addAttribute("bondCurId", cmsProvideLoanInfo.getBondCurId());
            model.addAttribute("bailmoneyRate", cmsProvideLoanInfo.getBailmoneyRate());
            model.addAttribute("bailmoney", cmsProvideLoanInfo.getBailmoney());
            model.addAttribute("bailmoneyContract", cmsProvideLoanInfo.getBailmoneyContract());
            model.addAttribute("bondRate", cmsProvideLoanInfo.getBondRate());
        }
    }


    /**
     * 保证金参数设置
     *
     * @param model
     * @param isBond
     * @param cmsProvideLoanInfo
     * @param fcBondHookService
     */
    public static void setBondParam(CmsLoanInfoViewPageVO model, String isBond, CmsProvideLoanInfo cmsProvideLoanInfo,
                                    IFcBondHookService fcBondHookService) {
        model.setConfigIsHookBond(isBond);
        // 授信参数是否挂保证金
        if ("1".equals(isBond)) {
            if (FcBondConstant.IS_HOOK_Y.equals(cmsProvideLoanInfo.getIsHookBond())) {
                // 存在挂接关系
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(cmsProvideLoanInfo.getId(),
                        cmsProvideLoanInfo.getBillCode());
                model.setDto(dto);
            }
            model.setIsHookBond(cmsProvideLoanInfo.getIsHookBond());
            model.setBondBankAcc(cmsProvideLoanInfo.getBondBankAcc());
            model.setBondCurId(cmsProvideLoanInfo.getBondCurId());
            model.setBailmoneyRate(cmsProvideLoanInfo.getBailmoneyRate());
            model.setBailmoney(cmsProvideLoanInfo.getBailmoney());
            model.setBailmoneyContract(cmsProvideLoanInfo.getBailmoneyContract());
            model.setBondRate(cmsProvideLoanInfo.getBondRate());
        }
    }

    /**
     * 授信数据设置
     *
     * @param model
     * @param relateRat      参数是否挂授信
     * @param ratmodel       业务是否挂授信
     * @param isOpenRat      是否启用授信敞口
     * @param id             业务主键
     * @param urationService
     */
    public static void setRatParam(Model model, String relateRat, String ratmodel, String isOpenRat, String id,
                                   IRatUrationService urationService, BtCurrencyDTO cur) {
        model.addAttribute("relateRat", relateRat);// 挂接授信
        RatUrationDTO urationDTO = null;
        model.addAttribute("ratBankConfig", urationService.getRatBankCtlConfig());// 授信银行控制
        if (CmsConstant.IS_TRUE.equals(relateRat)) {
            model.addAttribute("isOpenRat", isOpenRat);// 是否启用授信敞口
            if (RationConstant.RAT_COMMON_Y.equals(ratmodel)) {
                urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.CMS, id);
                model.addAttribute("urationDTO", urationDTO);// 授信明细
                model.addAttribute("ratBankCode", urationDTO.getBankCode());// 授信银行
                // BtCurrencyDTO cur = currencyService.getNatualCurrency();
                model.addAttribute("cur", cur);
            }
        }
    }

    public static void setPledgeParam(Model model, String pledgeConfig, CmsProvideLoanInfo loanInfo,
                                      IGuaPledgeOccupyService pledgeOccupyService) {
        model.addAttribute("pledgeConfig", pledgeConfig);// 参数设置是否挂抵质押物
        if (null != pledgeConfig && pledgeConfig.equals(CmsConstant.IS_TRUE)) {
            model.addAttribute("isRelatePledge", loanInfo.getIsRelatePledge());
            if (null != loanInfo.getIsRelatePledge() && loanInfo.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
                List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyService.getPledgeOccupy(loanInfo.getId(),
                        loanInfo.getBillCode());
                model.addAttribute("pledgeDTOS", occupyDTOS);
                model.addAttribute("itemNo", loanInfo.getBillCode());
                model.addAttribute("businessId", loanInfo.getId());
            }
        }
    }

    public static void setGuaParams(CmsLoanInfoViewPageVO model, String viewFlag, String isGuarantee,
                                    CmsProvideLoanInfo cmsProvideLoanInfo, IGuaWayServie guaWayServie,
                                    IGuaContractOccupServie guaContractOccupServie) {
        // 担保数据设置
        if (CmsConstant.IS_TRUE.equals(cmsProvideLoanInfo.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(cmsProvideLoanInfo.getId());
            guaContractOccupDTO = guaContractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
            String contractIds = null;
            if(!EmptyUtils.isEmpty(guaContractOccupDTO.getContractIds())) {
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
            }
            model.setContractIds(contractIds);
            model.setIdList(guaContractOccupDTO.getContractIds());
            model.setGuaranteeRmk(cmsProvideLoanInfo.getGuaranteeRmk());
            model.setGuaWayNames(guaContractOccupDTO.getGuaWayName());
            model.setGuaWay(guaContractOccupDTO.getGuaWay());
            model.setContractCode(guaContractOccupDTO.getContractCode());
        }
        model.setViewFlag(viewFlag);// <!-- 查看标志；1—为查看页面-->
        model.setIsGuarantee(isGuarantee);
        model.setIsHangGuarantee(cmsProvideLoanInfo.getIsHangGuarantee());
    }

    /**
     * 授信数据设置
     *
     * @param model
     * @param relateRat      参数是否挂授信
     * @param ratmodel       业务是否挂授信
     * @param isOpenRat      是否启用授信敞口
     * @param id             业务主键
     * @param urationService
     */
    public static void setRatParam(CmsLoanInfoViewPageVO model, String relateRat, String ratmodel, String isOpenRat, String id,
                                   IRatUrationService urationService, BtCurrencyDTO cur) {
        model.setRelateRat(relateRat);// 挂接授信
        RatUrationDTO urationDTO = null;
        model.setRatBankConfig(urationService.getRatBankCtlConfig());// 授信银行控制
        if (CmsConstant.IS_TRUE.equals(relateRat)) {
            model.setIsOpenRat(isOpenRat);// 是否启用授信敞口
            if (RationConstant.RAT_COMMON_Y.equals(ratmodel)) {
                urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.CMS, id);
                model.setUrationDTO(urationDTO);// 授信明细
                model.setRatBankCode(urationDTO.getBankCode());// 授信银行
                // BtCurrencyDTO cur = currencyService.getNatualCurrency();
                model.setCur(cur);
            }
        }
    }

    public static void setPledgeParam(CmsLoanInfoViewPageVO model, String pledgeConfig, CmsProvideLoanInfo loanInfo,
                                      IGuaPledgeOccupyService pledgeOccupyService) {
        model.setPledgeConfig(pledgeConfig);// 参数设置是否挂抵质押物
        if (null != pledgeConfig && pledgeConfig.equals(CmsConstant.IS_TRUE)) {
            model.setIsRelatePledge(loanInfo.getIsRelatePledge());
            if (null != loanInfo.getIsRelatePledge() && loanInfo.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
                List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyService.getPledgeOccupy(loanInfo.getId(),
                        loanInfo.getBillCode());
                model.setPledgeDTOS(occupyDTOS);
                model.setItemNo(loanInfo.getBillCode());
                model.setBusinessId(loanInfo.getId());
            }
        }
    }
}
