package com.bytter.guarantee.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.GuaWay;
import com.bytter.guarantee.domain.service.GuaWayService;
import com.bytter.guarantee.domain.vo.GuaWayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 担保方式维护
 */
@Controller
@RequestMapping("/cms/guarantee/guaway")
public class GuaWayController extends BaseController {
    @Autowired
    private GuaWayService guaWayService;

    @GetMapping("/list")
    public String list() {
        return "cms/guarantee/listGuaWay";
    }
    /**
     * 担保方式维护列表查询
     *
     * @param input 查询参数
     * @return 列表数据
     */
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput<GuaWayVO> dataTable(@RequestBody DataTablesInput input) {
        return guaWayService.dataTable(input);
    }
    /**
     * 担保方式维护新增页面跳转
     *
     * @param model 模型
     * @return 页面路径
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "cms/guarantee/addGuaWay";
    }
    /**
     * 担保方式维护编辑页面跳转------
     */
    @GetMapping("/update")
    public String update(GuaWay race, Model model) {
        if (StringUtils.isNotEmpty(race.getId())) {
            GuaWay guaWay = guaWayService.get(race.getId(), true);
            model.addAttribute("guaWay", guaWay);
        }
        return "cms/guarantee/updateGuaWay";
    }
    /**
     * 添加担保方式维护
     *
     * @param guaWay 担保方式信息
     * @return 结果
     */
    @LogMessage(content = "添加担保方式维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(GuaWay guaWay) {
        guaWayService.save(guaWay, getUserId());
        return success();
    }
    /**
     * 修改担保方式维护
     */
    @LogMessage(content = "修改担保方式维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(GuaWay guaWay) {
        guaWayService.update(guaWay, getUserId());
        return success();
    }
    /**
     * 删除担保方式维护
     */
    @LogMessage(content = "删除担保方式维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        guaWayService.deleteGuaWay(id);
        return success();
    }
    /**
     * @return
     */
    @GetMapping("/listGuaWay")
    @ResponseBody
    public List<GuaWay> listGuaWay() {
        return guaWayService.listAll();
    }
}
