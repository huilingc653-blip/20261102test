package com.bytter.biz.receipt.controller;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.receipt.entity.EPoundageBill;
import com.bytter.biz.receipt.service.EPoundageBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.EPoundageBillVO;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 手续费电子回单表
 * @date 2017-07-04 15:30:13
 */
@Controller
@RequestMapping("/receipt/epoundagebill")
public class EPoundageBillController extends BaseController {
    @Autowired
    private EPoundageBillService ePoundageBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @DubboReference(check = false)
	private IFcCarryService fcCarryService;
    /**
     * 手续费电子回单表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询手续费电子回单表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "receipt/elecbill/listEPoundageBill";
    }
    /**
     * 手续费电子回单表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return ePoundageBillService.listForUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 手续费电子回单表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(EPoundageBill ePoundageBill, Model model) {
        if (StringUtils.isNotEmpty(ePoundageBill.getId())) {
            ePoundageBill = ePoundageBillService.get(ePoundageBill.getId());
            model.addAttribute("ePoundageBillPage", ePoundageBill);
        }
        return "receipt/elecbill/addEPoundageBill";
    }
    /**
     * 手续费电子回单表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(EPoundageBill ePoundageBill, Model model) {
        if (StringUtils.isNotEmpty(ePoundageBill.getId())) {
            ePoundageBill = ePoundageBillService.get(ePoundageBill.getId());
            model.addAttribute("ePoundageBillPage", ePoundageBill);
        }
        return "receipt/elecbill/updateEPoundageBill";
    }
    /**
     * 手续费电子回单表详情页
     *
     * @return
     */
    @LogMessage(content = "查询手续费电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public String view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
        EPoundageBillVO ePoundageBill = ePoundageBillService.getVOByBillNo(null, id);
        String netId = ePoundageBill.getNetId();
        String billType = ePoundageBill.getBillType();
        if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
            EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
            model.addAttribute("eVisaTypePage", eVisaType);
        }
        // 获取操作日结的用户
        UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(ePoundageBill.getNetId(), ePoundageBill.getTallyDate());
        if (dayOverOperatorUser != null) {
            ePoundageBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
        }
        //操作类型：0-打印预览 1-打印
        model.addAttribute("opType", opType);
        model.addAttribute("ePoundageBillPage", ePoundageBill);
        return "receipt/elecbill/viewEPoundageBill";
    }
    /**
     * 添加手续费电子回单表
     *
     * @param ePoundageBill
     * @return
     */
    @LogMessage(content = "添加手续费电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(EPoundageBill ePoundageBill) {
        ePoundageBillService.save(ePoundageBill);
        return success();
    }
    /**
     * 修改手续费电子回单表
     *
     * @param ePoundageBill
     * @return
     */
    @LogMessage(content = "修改手续费电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(EPoundageBill ePoundageBill) {
        EPoundageBill t = ePoundageBillService.get(ePoundageBill.getId());
        BeanUtils.copyNotNullProperties(ePoundageBill, t);
        ePoundageBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除手续费电子回单表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除手续费电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        EPoundageBill ePoundageBill = ePoundageBillService.get(id);
        ePoundageBillService.deleteEntity(ePoundageBill);
        return success();
    }
    /**
     * 批量删除手续费电子回单表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除手续费电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            EPoundageBill ePoundageBill = ePoundageBillService.get(id);
            ePoundageBillService.deleteEntity(ePoundageBill);
        }
        return success();
    }
}
