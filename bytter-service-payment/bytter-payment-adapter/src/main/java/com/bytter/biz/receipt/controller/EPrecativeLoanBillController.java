package com.bytter.biz.receipt.controller;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.receipt.entity.EPrecativeLoanBill;
import com.bytter.biz.receipt.service.EPrecativeLoanBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.EPrecativeLoanBillVO;
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
import com.bytter.system.api.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托贷款电子回单表
 * @date 2017-07-04 15:30:40
 */
@Controller
@RequestMapping("/receipt/eprecativeloanbill")
public class EPrecativeLoanBillController extends BaseController {
    @Autowired
    private EPrecativeLoanBillService ePrecativeLoanBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @DubboReference(check = false)
	private IFcCarryService fcCarryService;
    @DubboReference(check = false)
    private IUserService userService;
    /**
     * 委托贷款电子回单表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询委托贷款电子回单表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "receipt/elecbill/listEPrecativeLoanBill";
    }
    /**
     * 委托贷款电子回单表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return ePrecativeLoanBillService.listForUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 委托贷款电子回单表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(EPrecativeLoanBill ePrecativeLoanBill, Model model) {
        if (StringUtils.isNotEmpty(ePrecativeLoanBill.getId())) {
            ePrecativeLoanBill = ePrecativeLoanBillService.get(ePrecativeLoanBill.getId());
            model.addAttribute("ePrecativeLoanBillPage", ePrecativeLoanBill);
        }
        return "receipt/elecbill/addEPrecativeLoanBill";
    }
    /**
     * 委托贷款电子回单表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(EPrecativeLoanBill ePrecativeLoanBill, Model model) {
        if (StringUtils.isNotEmpty(ePrecativeLoanBill.getId())) {
            ePrecativeLoanBill = ePrecativeLoanBillService.get(ePrecativeLoanBill.getId());
            model.addAttribute("ePrecativeLoanBillPage", ePrecativeLoanBill);
        }
        return "receipt/elecbill/updateEPrecativeLoanBill";
    }
    /**
     * 委托贷款电子回单表详情页
     *
     * @return
     */
    @LogMessage(content = "查询委托贷款电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public String view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
        EPrecativeLoanBillVO ePrecativeLoanBill = ePrecativeLoanBillService.getVOByBillNo(null, id);
        String netId = ePrecativeLoanBill.getNetId();
        String billType = ePrecativeLoanBill.getBillType();
        if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
            EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
            model.addAttribute("eVisaTypePage", eVisaType);
        }
        if(StringUtils.isNotEmpty(ePrecativeLoanBill.getTallyerCode()) ){
           com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(ePrecativeLoanBill.getTallyerCode());
           ePrecativeLoanBill.setTallyerCode(userDTO.getUsername());
        }
        if(StringUtils.isNotEmpty(ePrecativeLoanBill.getCheckerCode()) ){
            com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(ePrecativeLoanBill.getCheckerCode());
            ePrecativeLoanBill.setCheckerCode(userDTO.getUsername());
        }
        // 获取操作日结的用户
        UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(ePrecativeLoanBill.getNetId(), ePrecativeLoanBill.getTallyDate());
        if (dayOverOperatorUser != null) {
            ePrecativeLoanBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
        }
        //操作类型：0-打印预览 1-打印
        model.addAttribute("opType", opType);
        model.addAttribute("ePrecativeLoanBillPage", ePrecativeLoanBill);
        return "receipt/elecbill/viewEPrecativeLoanBill";
    }
    /**
     * 添加委托贷款电子回单表
     *
     * @param ePrecativeLoanBill
     * @return
     */
    @LogMessage(content = "添加委托贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(EPrecativeLoanBill ePrecativeLoanBill) {
        ePrecativeLoanBillService.save(ePrecativeLoanBill);
        return success();
    }
    /**
     * 修改委托贷款电子回单表
     *
     * @param ePrecativeLoanBill
     * @return
     */
    @LogMessage(content = "修改委托贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(EPrecativeLoanBill ePrecativeLoanBill) {
        EPrecativeLoanBill t = ePrecativeLoanBillService.get(ePrecativeLoanBill.getId());
        BeanUtils.copyNotNullProperties(ePrecativeLoanBill, t);
        ePrecativeLoanBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除委托贷款电子回单表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除委托贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        EPrecativeLoanBill ePrecativeLoanBill = ePrecativeLoanBillService.get(id);
        ePrecativeLoanBillService.deleteEntity(ePrecativeLoanBill);
        return success();
    }
    /**
     * 批量删除委托贷款电子回单表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除委托贷款电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            EPrecativeLoanBill ePrecativeLoanBill = ePrecativeLoanBillService.get(id);
            ePrecativeLoanBillService.deleteEntity(ePrecativeLoanBill);
        }
        return success();
    }
}
