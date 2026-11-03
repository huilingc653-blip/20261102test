package com.bytter.letter.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.entity.GuaGuaranteeInfo;
import com.bytter.letter.domain.service.GuaGuaranteeInfoService;
import com.bytter.letter.domain.service.GuaGuaranteeReceiveService;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
/**
 * 保函领取controller
 *
 * @author fusb
 * @version 1.0
 * @Title GuaGuaranteeReciveController
 * @Description
 * @date 2019年1月7日
 */
@Controller
@RequestMapping("/letter/guarantee/guaGuaranteeRecive")
public class GuaGuaranteeReceiveController extends BaseController {
    @Autowired
    GuaGuaranteeReceiveService guaGuaranteeReceiveService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private GuaGuaranteeInfoService guaLgInfoService;
    @Autowired
    private IFcBondHookService fcBondHookService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @Autowired
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @GetMapping("/list")
    @LogMessage(content = "跳转到保函领取列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String list(Model model) {
        return "cms/guarantee/listGuaGuaranteeReceive";
    }
    /** 列表内容 **/
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return guaGuaranteeReceiveService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 领取保函
     *
     * @return
     */
    @LogMessage(content = "领取保函", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/receiveGuaGurantee/{id}")
    public String receiveGuaGuarantee(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaGuaranteeInfoVO vo = guaGuaranteeReceiveService.getGuaGuaranteeInfoById(id);
            GuaGuaranteeInfo LgInfo = guaGuaranteeReceiveService.get(id);
            String guaranteeedCorp = "";
            if (!StringUtils.isBlank(LgInfo.getGuaranteeedCorp())) {// 设置被担保单位
                String[] corpIds = LgInfo.getGuaranteeedCorp().split(",");
                for (int i = 0; i < corpIds.length; i++) {
                    if (StringUtils.isBlank(corpIds[i])) {
                        continue;
                    }
                    CorpDTO corpDTO = corpService.get(corpIds[i]);
                    if (null != corpDTO) {
                        guaranteeedCorp += corpDTO.getName() + ",";
                    }
                }
                vo.setGuaranteeedCorp(LgInfo.getGuaranteeedCorp());
            }
            model.addAttribute("guaranteeedCorp", guaranteeedCorp);
            model.addAttribute("vo", vo);
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            model.addAttribute("isHangBail", isHangBailConfig.getValue());// 参数设置是否挂接保证金
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                setBondMsg(LgInfo, model);
            }
            ConfigDTO openRatConfig = guaLgInfoService.getIsStartOpenRat();// 参数设置是否启用敞口授信
            model.addAttribute("startOpenRat", openRatConfig.getValue());
            ConfigDTO config = guaLgInfoService.getRatContains();
            model.addAttribute("ratContains", config.getValue());// 获取挂接授信参数
            if (config.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                if (LgInfo.getRatmodel() != null && LgInfo.getRatmodel().equals(LetterConstant.GUA_COMMON_Y)) {// 挂接授信
                    RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, id);
                    model.addAttribute("urationDTO", urationDTO);// 授信明细
                    model.addAttribute("ratBankCode", urationDTO.getBankCode());// 授信银行
                    RatUrationDTO ratUrationDTO = new RatUrationDTO();
                    ratUrationDTO.setSysCode(ConfigConstant.GUA);
                    ratUrationDTO.setBillId(id);
                    ratUrationDTO.setStatus(RationConstant.RAT_USE_STATUS_USE);
                    List<RatUrationDTO> ratUrationDTOS = urationService.getRatUrationList(ratUrationDTO);
                    RatUrationDTO oriUrationDTO = ratUrationDTOS.get(0);
                    model.addAttribute("ratApplyMoneyPre", oriUrationDTO.getMoney());
                }
            }
            CorpDTO corpDTO = corpService.get(vo.getCorpId());// 保函申请单位
            if (null != corpDTO) {
                String corpCurId = corpDTO.getCurId();
                BigDecimal exchangeRate = rateAdjustService.getExchangeRateBy(vo.getCurId(), corpCurId,
                        vo.getStartDate(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                model.addAttribute("exchangeRate", exchangeRate);// 外币折算单位本位币时的汇率
                model.addAttribute("corpCurId", corpCurId);// 单位本位币
                String naturalSign = LetterConstant.GUA_COMMON_Y;// 本位币标识
                if (null != corpCurId && !corpCurId.equals(vo.getCurId())) {
                    naturalSign = LetterConstant.GUA_COMMON_N;// 非单位本位币时显示是否折合本位币
                }
                model.addAttribute("naturalSign", naturalSign);
            }
        }
        String operator = getCurrentUser().getUsername();
        model.addAttribute("operator", operator);
        return "cms/guarantee/receiveGuaGuarantee";
    }
    /**
     * 打回保函
     */
    @LogMessage(content = "打回保函信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/repulse/{id}")
    @ResponseBody
    public AjaxJson repulse(@PathVariable("id") String id) {
        guaLgInfoService.repulse(id, this.getCurrentUser());
        return success();
    }
    /**
     * 保存保函领取相关信息
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson saveGuaGuaranteeReceive(GuaGuaranteeInfoVO vo) {
        guaGuaranteeReceiveService.saveGuaGuaranteeReceive(vo, this.getCurrentUser());
        return success();
    }
    /**
     * 保存保函领取相关信息
     */
    @PostMapping("/fail")
    @ResponseBody
    public AjaxJson failGuaGuaranteeReceive(GuaGuaranteeInfoVO vo) {
        guaGuaranteeReceiveService.getfail(vo, this.getCurrentUser());
        return success();
    }
    /**
     * 设置保证金相关信息
     */
    public void setBondMsg(GuaGuaranteeInfo LgInfoVo, Model model) {
        if (LgInfoVo.getIsHookBond() != null && (LgInfoVo.getIsHookBond().equals(LetterConstant.GUA_COMMON_Y))) {// 外部挂接保证金
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(LgInfoVo.getId(), LgInfoVo.getGuaranteeNo());
            if (dto != null) {
                model.addAttribute("isHookBond", LgInfoVo.getIsHookBond());
                model.addAttribute("bondBankAcc", dto.getBankAcc());
                model.addAttribute("bailmoney", dto.getApplyMoney());
                model.addAttribute("bondCurId", dto.getCurId());
                model.addAttribute("bailmoneyRate", dto.getBondPercent());
                model.addAttribute("bailmoneyContract", dto.getBondContract());
                model.addAttribute("bondId", dto.getBondId());
            }
        } else {
            model.addAttribute("isHookBond", LgInfoVo.getIsHookBond());
            model.addAttribute("bailmoney", LgInfoVo.getBondMoney());
            model.addAttribute("bondCurId", LgInfoVo.getBondCurId());
            model.addAttribute("bailmoneyRate", LgInfoVo.getBondPercent());
            model.addAttribute("bailmoneyContract", LgInfoVo.getBondContract());
        }
    }
    /**
     * 设置授信相关信息
     */
    public void setRatMsg(GuaGuaranteeInfo LgInfoVo, Model model) {
        RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, LgInfoVo.getId());// 授信明细
        model.addAttribute("ratItemCode", ratUrationDTO.getRatItemCode());
        model.addAttribute("isRmbRat", LgInfoVo.getIsRmbRat());// 是否折合本位币
        model.addAttribute("billName", ratUrationDTO.getBillName());
        model.addAttribute("ratCode", ratUrationDTO.getRatCode());
        model.addAttribute("itemFlag", ratUrationDTO.getItemFlag());
        model.addAttribute("itemMoney", ratUrationDTO.getMoney());
    }
}
