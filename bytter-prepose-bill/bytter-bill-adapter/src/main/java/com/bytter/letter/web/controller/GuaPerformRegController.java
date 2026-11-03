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
import com.bytter.guarantee.velocity.tags.FcBondTag;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.entity.GuaPerformReg;
import com.bytter.letter.domain.service.GuaGuaranteeInfoService;
import com.bytter.letter.domain.service.GuaPerformRegService;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 履行登记controller
 *
 * @author fusb
 * @version 1.0
 * @Title GuaGuaranteeReciveController
 * @Description
 * @date 2019年1月7日
 */
@Controller
@RequestMapping("/letter/guarantee/guaPerformReg")
public class GuaPerformRegController extends BaseController {
    @Autowired
    GuaPerformRegService guaPerformRegService;
    @Autowired
    private GuaGuaranteeInfoService guaLgInfoService;
    @Autowired
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @GetMapping("/list")
    @LogMessage(content = "跳转到履行登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String list(Model model) {
        return "cms/guarantee/listGuaPerformReg";
    }
    /** 列表内容 **/
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return guaPerformRegService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 履行登记
     *
     * @return
     */
    @LogMessage(content = "履行登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/registerGuaPerform/{id}")
    public String receiveGuaGuarantee(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaGuaranteeInfoVO vo = guaPerformRegService.getGuaGuaranteeInfoById(id);
            model.addAttribute("vo", vo);
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                model.addAttribute("isHangBail", isHangBailConfig.getValue());// 参数设置是否挂接保证金
            }
            // 挂接的授信信息
            if (RationConstant.RAT_COMMON_Y.equals(vo.getRatmodel())) {
                model.addAttribute("urationDTO", urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, id));// 授信明细
            }
            BtCurrencyDTO guaCurDTO = currencyService.getObjectById(vo.getCurId());
            model.addAttribute("curExrate", guaCurDTO.getCurExrate());// 保函币别的汇率
            model.addAttribute("naturalSign", guaCurDTO.getNaturalSign());// 本位币标识

            //保证金相关
            if (StringUtils.isNotBlank(vo.getIsHookBond()) && "1".equals(vo.getIsHookBond())){
                FcBondTag fcBondTag = new FcBondTag();
                model.addAttribute("fcBondTag", fcBondTag);
            }
        }
        return "cms/guarantee/registerGuaPerform";
    }
    /**
     * 保存履行登记相关信息
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson saveGuaPerformReg(GuaPerformReg guaPerformReg) {
        guaPerformRegService.saveGuaPerformReg(guaPerformReg, this.getCurrentUser());
        return success();
    }
}
