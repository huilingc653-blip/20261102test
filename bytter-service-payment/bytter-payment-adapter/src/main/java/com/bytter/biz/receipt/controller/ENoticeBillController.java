package com.bytter.biz.receipt.controller;
import com.bytter.biz.finacial.api.dto.UserDTO;
import com.bytter.biz.finacial.api.service.IFcCarryService;
import com.bytter.biz.receipt.entity.ENoticeBill;
import com.bytter.biz.receipt.service.ENoticeBillService;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.ENoticeBillVO;
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
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 通知单电子回单表
 * @date 2017-07-04 15:30:08
 */
@Controller
@RequestMapping("/receipt/enoticebill")
public class ENoticeBillController extends BaseController {
    @Autowired
    private ENoticeBillService eNoticeBillService;
    @Autowired
    private EVisaTypeService eVisaTypeService;
    @DubboReference(check = false)
	private IFcCarryService fcCarryService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private ICorpService corpService;
    /**
     * 通知单电子回单表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询通知单电子回单表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "receipt/elecbill/listENoticeBill";
    }
    /**
     * 通知单电子回单表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return eNoticeBillService.listForUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 通知单电子回单表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(ENoticeBill eNoticeBill, Model model) {
        if (StringUtils.isNotEmpty(eNoticeBill.getId())) {
            eNoticeBill = eNoticeBillService.get(eNoticeBill.getId());
            model.addAttribute("eNoticeBillPage", eNoticeBill);
        }
        return "receipt/elecbill/addENoticeBill";
    }
    /**
     * 通知单电子回单表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(ENoticeBill eNoticeBill, Model model) {
        if (StringUtils.isNotEmpty(eNoticeBill.getId())) {
            eNoticeBill = eNoticeBillService.get(eNoticeBill.getId());
            model.addAttribute("eNoticeBillPage", eNoticeBill);
        }
        return "receipt/elecbill/updateENoticeBill";
    }
    /**
     * 通知单电子回单表详情页
     *
     * @return
     */
    @LogMessage(content = "查询通知单电子回单表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{opType}")
    public String view(@PathVariable("id") String id, @PathVariable("opType") String opType, Model model) {
        ENoticeBillVO eNoticeBill = eNoticeBillService.getVOByBillNo(null, id);
        CorpDTO corpDTO = corpService.get(eNoticeBill.getCorpId());
        String netId = corpDTO.getNetId();
        String billType = eNoticeBill.getBillType();
        if (StringUtils.isNotEmpty(netId) && StringUtils.isNotEmpty(billType)) {
            EVisaTypeVO eVisaType = eVisaTypeService.getVOByParamsAndValid(billType, netId);
            model.addAttribute("eVisaTypePage", eVisaType);
        }
        // 获取操作日结的用户
        UserDTO dayOverOperatorUser = fcCarryService.getDayOverOperatorUser(eNoticeBill.getNetId(), eNoticeBill.getTallyDate());
        if (dayOverOperatorUser != null) {
            eNoticeBill.setDayOverOperatorUsername(dayOverOperatorUser.getUsername());
        }
        if(StringUtils.isNotEmpty(eNoticeBill.getTallierCode()) ){
            com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(eNoticeBill.getTallierCode());
            eNoticeBill.setTallierName(userDTO.getUsername());
        }
        if(StringUtils.isNotEmpty(eNoticeBill.getCheckerCode()) ){
            com.bytter.system.api.dto.UserDTO userDTO= userService.getByUserId(eNoticeBill.getCheckerCode());
            eNoticeBill.setCheckerName(userDTO.getUsername());
        }
        //操作类型：0-打印预览 1-打印
        model.addAttribute("opType", opType);
        model.addAttribute("eNoticeBillPage", eNoticeBill);
        return "receipt/elecbill/viewENoticeBill";
    }
    /**
     * 添加通知单电子回单表
     *
     * @param eNoticeBill
     * @return
     */
    @LogMessage(content = "添加通知单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ENoticeBill eNoticeBill) {
        eNoticeBillService.save(eNoticeBill);
        return success();
    }
    /**
     * 修改通知单电子回单表
     *
     * @param eNoticeBill
     * @return
     */
    @LogMessage(content = "修改通知单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(ENoticeBill eNoticeBill) {
        ENoticeBill t = eNoticeBillService.get(eNoticeBill.getId());
        BeanUtils.copyNotNullProperties(eNoticeBill, t);
        eNoticeBillService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除通知单电子回单表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除通知单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ENoticeBill eNoticeBill = eNoticeBillService.get(id);
        eNoticeBillService.deleteEntity(eNoticeBill);
        return success();
    }
    /**
     * 批量删除通知单电子回单表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除通知单电子回单表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            ENoticeBill eNoticeBill = eNoticeBillService.get(id);
            eNoticeBillService.deleteEntity(eNoticeBill);
        }
        return success();
    }
}
