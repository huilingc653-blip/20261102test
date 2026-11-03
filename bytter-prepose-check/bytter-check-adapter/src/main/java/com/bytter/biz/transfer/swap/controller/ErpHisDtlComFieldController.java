package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.entity.ErpInterConfigFill;
import com.bytter.biz.transfer.swap.enums.ErpInterConfigEnum;
import com.bytter.biz.transfer.swap.service.ErpHisDtlComFieldService;
import com.bytter.biz.transfer.swap.vo.ErpHisDtlTemplateVO;
import com.bytter.biz.transfer.swap.vo.ErpHisDtlVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 单据补录信息
 */
@Controller
@RequestMapping("/erp/swap/hisDtlComField")
public class ErpHisDtlComFieldController extends BaseController {
    @Autowired
    private ErpHisDtlComFieldService erpHisDtlComFieldService;
    /**
     * 补录
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/netSetting/{id}")
    public String netSetting(@PathVariable("id")String id,@RequestParam(required = true) String type, Model model) {
        model.addAttribute("configType", ErpInterConfigEnum.valueOf(type.toUpperCase()).getCode());
        if(ErpInterConfigEnum.ACCEBILL.getCode().equalsIgnoreCase(type)){
            model.addAttribute("templateStore", null);
            return "transfer/swap/templatestorebill_info";
        }else{
            ErpHisDtlVO vo = erpHisDtlComFieldService.selectInfoById(id,type);
            model.addAttribute("templateStore", vo);
            return "transfer/swap/templatestore_info_netSet";
        }
    }
    /**
     * 批量补录
     * @param type
     * @param model
     * @return
     */
    @GetMapping("/netSettings/{type}")
    public String netSettings(@PathVariable("type")String  type,@RequestParam(value = "ids",required = true) String ids, Model model) {
        model.addAttribute("ids", ids);
        model.addAttribute("configType", ErpInterConfigEnum.valueOf(type.toUpperCase()).getCode());
        List<ErpInterConfigFill> erpInterConfigFills =  erpHisDtlComFieldService.queryErpFieldValid(type);
        model.addAttribute("configFills", erpInterConfigFills);
        return "transfer/swap/templatestore_info_netSets";
    }
    @LogMessage(content = "单据补录",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ErpHisDtlTemplateVO hisDtlTemplateVO){
        if(!EmptyUtils.isEmpty(hisDtlTemplateVO) && !EmptyUtils.isEmptyList(hisDtlTemplateVO.getSerialId())){
            erpHisDtlComFieldService.saveHisDtlComField(hisDtlTemplateVO,"",this.getCurrentUser());
        }
        return success();
    }
    //查看明细/{id}
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id")String id,@RequestParam(required = true) String type, Model model) {
        model.addAttribute("configType", ErpInterConfigEnum.valueOf(type.toUpperCase()).getCode());
        if(ErpInterConfigEnum.ACCEBILL.getCode().equalsIgnoreCase(type)){
            model.addAttribute("templateStore", null);
            return "transfer/swap/templatestorebill_view";
        }else{
            ErpHisDtlVO vo = erpHisDtlComFieldService.selectInfoById(id,type);
            model.addAttribute("templateStore", vo);
            return "transfer/swap/templatestore_view_netSet";
        }
    }
}
