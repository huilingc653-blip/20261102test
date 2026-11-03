package com.bytter.biz.receipt.controller;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.receipt.entity.EAcceptBill;
import com.bytter.biz.receipt.service.EAcceptBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.EAcceptBillVO;
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
 * @Description: 承兑汇票电子回单表
 * @date 2017-07-04 15:30:24
 */
@Controller
@RequestMapping("/receipt/eacceptbill")
public class EAcceptBillController extends BaseController {
    @Autowired
    private EAcceptBillService eAcceptBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @DubboReference(check = false)
	private IFcCarryService fcCarryService;
    @DubboReference(check = false)
    private IUserService userService;
    /**
     * 承兑汇票电子回单表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询承兑汇票电子回单表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "receipt/elecbill/listEAcceptBill";
    }
    /**
     * 承兑汇票电子回单表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return eAcceptBillService.listForUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 承兑汇票电子回单表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(EAcceptBill eAcceptBill, Model model) {
        if (StringUtils.isNotEmpty(eAcceptBill.getId())) {
            eAcceptBill = eAcceptBillService.get(eAcceptBill.getId());
            model.addAttribute("eAcceptBillPage", eAcceptBill);
        }
        return "receipt/elecbill/addEAcceptBill";
    }
    /**
     * 承兑汇票电子回单表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(EAcceptBill eAcceptBill, Model model) {
        if (StringUtils.isNotEmpty(eAcceptBill.getId())) {
            eAcceptBill = eAcceptBillService.get(eAcceptBill.getId());
            model.addAttribute("eAcceptBillPage", eAcceptBill);
        }
        return "receipt/elecbill/updateEAcceptBill";
    }
    /**
     * 承兑汇票电子回单表详情页
     *
     * @return
     */
    @LogMessage(content = "查询承兑汇票电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public String view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
        EAcceptBillVO eAcceptBill = eAcceptBillService.getVOByBillNo(null, id);
        String netId = eAcceptBill.getNetId();
        String billType = eAcceptBill.getBillType();
        if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
            EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
            model.addAttribute("eVisaTypePage", eVisaType);
        }
        // 获取操作日结的用户
        UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(eAcceptBill.getNetId(), eAcceptBill.getTallyDate());
        if (dayOverOperatorUser != null) {
            eAcceptBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
        }
        if(StringUtils.isNotEmpty(eAcceptBill.getTallierCode()) ){
            com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(eAcceptBill.getTallierCode());
            eAcceptBill.setTallierName(userDTO.getUsername());
        }
        if(StringUtils.isNotEmpty(eAcceptBill.getCheckerCode()) ){
            com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(eAcceptBill.getCheckerCode());
            eAcceptBill.setCheckerName(userDTO.getUsername());
        }
        //操作类型：0-打印预览 1-打印
        model.addAttribute("opType", opType);
        model.addAttribute("eAcceptBillPage", eAcceptBill);
        return "receipt/elecbill/viewEAcceptBill";
    }
    /**
     * 添加承兑汇票电子回单表
     *
     * @param eAcceptBill
     * @return
     */
    @LogMessage(content = "添加承兑汇票电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(EAcceptBill eAcceptBill) {
        eAcceptBillService.save(eAcceptBill);
        return success();
    }
    /**
     * 修改承兑汇票电子回单表
     *
     * @param eAcceptBill
     * @return
     */
    @LogMessage(content = "修改承兑汇票电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(EAcceptBill eAcceptBill) {
        EAcceptBill t = eAcceptBillService.get(eAcceptBill.getId());
        BeanUtils.copyNotNullProperties(eAcceptBill, t);
        eAcceptBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除承兑汇票电子回单表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除承兑汇票电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        EAcceptBill eAcceptBill = eAcceptBillService.get(id);
        eAcceptBillService.deleteEntity(eAcceptBill);
        return success();
    }
    /**
     * 批量删除承兑汇票电子回单表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除承兑汇票电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            EAcceptBill eAcceptBill = eAcceptBillService.get(id);
            eAcceptBillService.deleteEntity(eAcceptBill);
        }
        return success();
    }
}
