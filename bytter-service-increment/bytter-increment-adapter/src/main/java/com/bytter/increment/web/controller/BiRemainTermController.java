package com.bytter.increment.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.increment.domain.service.BiRemainTermService;
import com.bytter.increment.domain.vo.BiRemainTermVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @ClassName BiRemainTermController
 * @Description 剩余期限维护controller
 * @Author fusb
 * @Date 2020/1/8 14:41
 * @Version 1.0
 **/
@Controller
@RequestMapping("/report/remainTerm")
public class BiRemainTermController extends BaseController {
    @Autowired
    BiRemainTermService biRemainTermService;
        @LogMessage(content = "跳转到剩余期限维护列表页面",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String remainTermList(){
        return "remainTerm/listRemainTerm";
    }
        @LogMessage(content = "剩余期限维护列表数据",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    @PostMapping("/dataTables")
    @ResponseBody
    public DataTablesOutput remainTermTables(@RequestBody DataTablesInput input){
        return biRemainTermService.remainTermTables(input);
    }
        @LogMessage(content = "跳转到新增剩余期限维护信息页面",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    @GetMapping("/add")
    public String addRemainTerm(Model model) {
        return "remainTerm/addRemainTerm";
    }
    /**
    *@Description 保存剩余期限维护信息
    *@Param vo
    *@return com.bytter.core.common.model.json.AjaxJson
    *@Date 2020/1/10 17:17
    *@Author fusb
    */
    @LogMessage(content = "保存剩余期限维护信息",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson remainTermSubmit(BiRemainTermVO vo){
        biRemainTermService.remainTermSubmit(vo,this.getCurrentUser());
        return success();
    }
    /**
    *@Description 编辑剩余期限维护信息
    *@Param id
    *@Param model
    *@return java.lang.String
    *@Date 2020/1/12 13:55
    *@Author fusb
    */
        @LogMessage(content = "编辑剩余期限维护信息",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    @GetMapping("/edit{id}")
    public String updateRemainTerm(String id,Model model) {
        BiRemainTermVO remainTerm = biRemainTermService.getBiRemainTermVOById(id);
        model.addAttribute("remainTerm",remainTerm);
        return "remainTerm/updateRemainTerm";
    }
    /**
    *@Description 删除剩余期限维护信息
    *@Param id
    *@Param model
    *@return com.bytter.core.common.model.json.AjaxJson
    *@Date 2020/1/12 13:56
    *@Author fusb
    */
    @LogMessage(content = "删除剩余期限维护信息",level = Globals.Log_Leavel_INFO,type=Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id,Model model) {
        biRemainTermService.deleteBiRemainTerm(id);
        return success();
    }
}
