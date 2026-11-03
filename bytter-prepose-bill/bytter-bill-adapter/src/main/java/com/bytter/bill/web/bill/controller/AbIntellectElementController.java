package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbIntellectElementService;
import com.bytter.bill.domain.bill.vo.AbIntellectElementVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * author: liusy
 * date: 2022/10/20 10:30
 * description: 选票要素控制器
 */
@Controller
@RequestMapping("/bill/intellect/element")
public class AbIntellectElementController extends BaseController {
    @Autowired
    private AbIntellectElementService abIntellectElementService;
    /**
     * 选票要素列表跳转
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String listElement(Model model) {
        return "bill/accebill/intellect/element/listElement";
    }
    /**
     * 选票要素列表跳转
     * @param model
     * @return
     */
    @GetMapping("/selectDictGroup")
    public String selectDictGroup(Model model) {
        return "bill/accebill/intellect/element/selectDictGroup";
    }
    /**
     * 选票要素列表查询
     * @param input
     * @param model
     * @return
     */
    @ResponseBody
    @PostMapping("/datatable")
    public DataTablesOutput datatable(@RequestBody DataTablesInput input, Model model) {
        return abIntellectElementService.datatable(input);
    }
    /**
     * 选票要素新增页面跳转
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "bill/accebill/intellect/element/addElement";
    }
    /**
     * 选票要素编辑页面跳转
     * @param id 主键id
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String edit(String id, Model model) {
        model.addAttribute("element", abIntellectElementService.getAbIntellectElementVOById(id));
        return "bill/accebill/intellect/element/editElement";
    }
    /**
     * 选票要素详情页面跳转
     * @param id 主键id
     * @param model
     * @return
     */
    @GetMapping("/view")
    public String view(String id, Model model) {
        model.addAttribute("element", abIntellectElementService.getAbIntellectElementVOById(id));
        return "bill/accebill/intellect/element/viewElement";
    }
    /**
     * 选票票要素保存操作
     * @param abIntellectElementVO
     * @param model
     * @return
     */
    @ResponseBody
    @PostMapping("/save")
    public AjaxJson save(AbIntellectElementVO abIntellectElementVO, Model model) {
        abIntellectElementService.saveElement(abIntellectElementVO, getCurrentUser());
        return success();
    }
    @ResponseBody
    @PostMapping("/update")
    public AjaxJson update(AbIntellectElementVO abIntellectElementVO, Model model) {
        abIntellectElementService.updateElement(abIntellectElementVO, getCurrentUser());
        return success();
    }
    /**
     * 选票要素删除操作
     * @param id
     * @param model
     * @return
     */
    @ResponseBody
    @PostMapping("/del")
    public AjaxJson del(String id, Model model) {
        abIntellectElementService.deleteElement(id);
        return success();
    }
    /**
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/listElement")
    public List<AbIntellectElementVO> listElement(){
        return abIntellectElementService.listElement();
    }
}
