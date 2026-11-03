package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.enums.ErpInterConfigEnum;
import com.bytter.biz.transfer.swap.enums.HisDtlTemplateStatusEnum;
import com.bytter.biz.transfer.swap.service.ErpHisDtlTemplateService;
import com.bytter.biz.transfer.swap.service.ErpTemplateStoreService;
import com.bytter.biz.transfer.swap.vo.ErpHisDtlTemplateVO;
import com.bytter.biz.transfer.swap.vo.ErpTemplateBillVO;
import com.bytter.biz.transfer.swap.vo.ErpTemplateStoreVO;
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
 * Created by zhouj on 2018/5/29.
 */
@Controller
@RequestMapping("/erp/swap/hisdtltemplate")
public class ErpHisDtlTemplateController extends BaseController {
    @Autowired
    private ErpHisDtlTemplateService hisDtlTemplateService;
    @Autowired
    private ErpTemplateStoreService templateStoreService;
    @LogMessage(content = "模板匹配补录",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ErpHisDtlTemplateVO hisDtlTemplateVO){
        if(!EmptyUtils.isEmpty(hisDtlTemplateVO) && !EmptyUtils.isEmptyList(hisDtlTemplateVO.getSerialId())){
            hisDtlTemplateService.updateHisTemplate(hisDtlTemplateVO,"",this.getCurrentUser());
        }
        return success();
    }
    @LogMessage(content = "批量模板匹配补录",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifys")
    @ResponseBody
    public AjaxJson batchmodify(ErpHisDtlTemplateVO hisDtlTemplateVO){
        if(!EmptyUtils.isEmpty(hisDtlTemplateVO) && !EmptyUtils.isEmptyList(hisDtlTemplateVO.getSerialId())){
            hisDtlTemplateService.updateHisTemplate(hisDtlTemplateVO,"batch",this.getCurrentUser());
        }
        return success();
    }
    @LogMessage(content = "批量修改模板匹配状态",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/status/{status}")
    @ResponseBody
    public AjaxJson status(@RequestParam(value = "type",required = true)String  type,@RequestParam(value = "ids[]",required = true) List<String> ids, @PathVariable("status") Long status){
        if(!EmptyUtils.isEmptyList(ids)){
            if(HisDtlTemplateStatusEnum.FINISH.getStatus()==status.intValue()){
                hisDtlTemplateService.updateFinishByIds(ids,type,status,this.getCurrentUser());
            }else if(HisDtlTemplateStatusEnum.CONFIRM.getStatus()==status.intValue()){
                hisDtlTemplateService.updateConfirmByIds(ids,type,status,this.getCurrentUser());
            }else{
                hisDtlTemplateService.updateHisTemplateStatus(ids,type,status,this.getCurrentUser());
            }
        }
        return success();
    }
    //查看明细/{id}
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id")String id,@RequestParam(required = true) String type, Model model) {
        model.addAttribute("configType", ErpInterConfigEnum.valueOf(type.toUpperCase()).getCode());
        if(ErpInterConfigEnum.ACCEBILL.getCode().equalsIgnoreCase(type)){
            ErpTemplateBillVO vo = templateStoreService.selectBillById(id);
            model.addAttribute("templateStore", vo);
            return "transfer/swap/templatestorebill_view";
        }else{
            ErpTemplateStoreVO templateStoreVO = templateStoreService.selectBankccById(id);
            model.addAttribute("templateStore", templateStoreVO);
            return "transfer/swap/templatestore_view";
        }
    }
}
