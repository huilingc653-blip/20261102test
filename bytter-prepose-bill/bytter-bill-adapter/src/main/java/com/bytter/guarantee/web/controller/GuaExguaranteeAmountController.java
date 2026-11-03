package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.domain.service.GuaExguaranteeAmountService;
import com.bytter.guarantee.domain.vo.GuaExguaranteeAmountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/cms/guarantee/guaExGuaranteeAmount")
class GuaExguaranteeAmountController extends BaseController {
    @Autowired
    private GuaExguaranteeAmountService guaExguaranteeAmountService;
    /**
     * 接口代码维护列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询接口代码维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/guarantee/listExGuaContractAmount";
    }
    /**
     * 用途维护表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return guaExguaranteeAmountService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "cms/guarantee/addExGuaContractAmount";
    }
    /**
     * 添加
     *
     * @param vo
     * @return
     */
    @LogMessage(content = "添加外部定价限额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(GuaExguaranteeAmountVO vo) {
        guaExguaranteeAmountService.saveBtPurpose(vo, this.getCurrentUser().getLoginName());
        return success();
    }
    /**
     * 编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(String id, Model model) {
        GuaExguaranteeAmountVO exguaranteeAmountVO = guaExguaranteeAmountService.getByUnion(id);
        model.addAttribute("amountVO", exguaranteeAmountVO);
        return "cms/guarantee/updateExGuaContractAmount";
    }
    /**
     * 修改
     *
     * @param vo
     * @return
     */
    @LogMessage(content = "添加外部定价限额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(GuaExguaranteeAmountVO vo) {
        guaExguaranteeAmountService.modifyBtPurpose(vo, this.getCurrentUser().getLoginName());
        return success();
    }
    /**
     * 查看详情页
     *
     * @return
     */
    @LogMessage(content = "查询详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        GuaExguaranteeAmountVO exguaranteeAmountVO = guaExguaranteeAmountService.getByUnion(id);
        model.addAttribute("amountVO", exguaranteeAmountVO);
        return "cms/guarantee/viewExGuaContractAmount";
    }
    @GetMapping("/corpId.json/{id}")
    @ResponseBody
    public Object getCorpIdByPurposeId(@PathVariable("id") String id) {
        return new TreeGridResponse<>(guaExguaranteeAmountService.getCorpIdByAmountId(id));
    }
    /**
     * 删除用途维护表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除外部定价限额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        guaExguaranteeAmountService.delete(id);
        return success();
    }
}
