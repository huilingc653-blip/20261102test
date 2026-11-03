package com.bytter.biz.finacial.paramconfig.controller;

import com.bytter.biz.finacial.paramconfig.service.FcNisBusTypeRuleService;
import com.bytter.biz.finacial.paramconfig.service.FcOtherBusinessRuleDefineService;
import com.bytter.biz.finacial.paramconfig.vo.FcNisBusTypeRuleVO;
import com.bytter.biz.finacial.paramconfig.vo.FcOtherBusinessRuleDefineVo;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lijinquan
 * @description: 其他收付款自动记账规则维护
 * @date 2024-10-28 14:23
 */
@Controller
@RequestMapping("/paramconfig/fcotherbusinessruledefine")
public class FcOtherBusinessRuleDefineController extends BaseController {

    @Autowired
    private FcOtherBusinessRuleDefineService fcOtherBusinessRuleDefineService;
    @Autowired
    private FcNisBusTypeRuleService fcNisBusTypeRuleService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;

    @LogMessage(content = "查询FC_BUSINESS_RULE_DEFINE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "finacial/paramconfig/businessRule/listFcOtherBusinessRuleDefine";
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fcOtherBusinessRuleDefineService.getBusninessRuleVoList(dataTablesInput);
    }

    /**
     * 添加页面
     * @param fcOtherBusinessRuleDefineVo
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(FcOtherBusinessRuleDefineVo fcOtherBusinessRuleDefineVo, Model model) {
        if (StringUtils.isNotEmpty(fcOtherBusinessRuleDefineVo.getId())) {
            fcOtherBusinessRuleDefineVo = fcOtherBusinessRuleDefineService.getBusinessRuleDefineVoById(fcOtherBusinessRuleDefineVo.getId());
            model.addAttribute("fcOtherBusinessRuleDefinePage", fcOtherBusinessRuleDefineVo);
        }
        FcVoucherTypeDTO fcVoucherTypeByCode = fcVoucherTypeService.getDtoByVoucherName("其他");
        model.addAttribute("defaultVoucherTypeId", fcVoucherTypeByCode.getId());
        return "finacial/paramconfig/businessRule/addFcOtherBusinessRuleDefine";
    }

    /**
     * 添加FC_BUSINESS_RULE_DEFINE
     * @param fcOtherBusinessRuleDefine
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSINESSRULEDEFINE_APPLY)
    @LogMessage(content = "添加FC_OTHER_BUSINESS_RULE_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcOtherBusinessRuleDefineVo fcOtherBusinessRuleDefine, HttpServletRequest request) {
        fcOtherBusinessRuleDefineService.saveBusinessRule(fcOtherBusinessRuleDefine, request,getCurrentUser());
        return success();
    }

    @GetMapping("/update")
    public String update(FcOtherBusinessRuleDefineVo fcOtherBusinessRuleDefine, Model model) {
        if (StringUtils.isNotEmpty(fcOtherBusinessRuleDefine.getId())) {
            fcOtherBusinessRuleDefine = fcOtherBusinessRuleDefineService.getBusinessRuleDefineVoById(fcOtherBusinessRuleDefine.getId());
            model.addAttribute("fcOtherBusinessRuleDefinePage", fcOtherBusinessRuleDefine);
            List<FcNisBusTypeRuleVO> nisBusTypeRuleVOList =  fcNisBusTypeRuleService.getByByBusId(fcOtherBusinessRuleDefine.getId());
            int length = nisBusTypeRuleVOList.size();
            model.addAttribute("nisBusTypeRuleVOList", nisBusTypeRuleVOList);
            model.addAttribute("length", length);
        }
        return "finacial/paramconfig/businessRule/updateFcOtherBusinessRuleDefine";
    }

    @LogMessage(content = "修改FC_OTHER_BUSINESS_RULE_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FcOtherBusinessRuleDefineVo fcOtherBusinessRuleDefine, HttpServletRequest request){
        fcOtherBusinessRuleDefineService.updateBusinessRule(fcOtherBusinessRuleDefine,request, getCurrentUser());
        return  success();
    }

    @LogMessage(content = "查询FC_OTHER_BUSINESS_RULE_DEFINE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FcOtherBusinessRuleDefineVo fcOtherBusinessRuleDefine = fcOtherBusinessRuleDefineService.getBusRuleDefineVoById(id);
        model.addAttribute("fcOtherBusinessRuleDefinePage", fcOtherBusinessRuleDefine);
        List<FcNisBusTypeRuleVO> nisBusTypeRuleVOList =  fcNisBusTypeRuleService.getByByBusId(id);
        model.addAttribute("nisBusTypeRuleVOList", nisBusTypeRuleVOList);
        return "finacial/paramconfig/businessRule/viewFcOtherBusinessRuleDefine";
    }

    @LogMessage(content = "删除FC_OTHER_BUSINESS_RULE_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        fcOtherBusinessRuleDefineService.deleteBusinessRule(id);;
        return success();
    }
}