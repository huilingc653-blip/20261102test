package com.bytter.finance.web.lease.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.lease.dao.LeaseTypeDao;
import com.bytter.finance.domain.lease.entity.LeaseItemType;
import com.bytter.finance.domain.lease.service.LeaseBasedataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title
 * @Description 融资租赁基础数据controller
 * @Author wanggl
 * @Date 2022-4-2
 * @Version 1.0
 */
@Controller
@RequestMapping("/cms/lease/baseData")
public class LeaseBasedataController extends BaseController {
    @Autowired
    private LeaseBasedataService leaseBasedataService;
    @Autowired
    private LeaseTypeDao leaseTypeDao;
    /**
     * 列表跳转
     *
     * @return
     */
    @GetMapping("/leaseType")
    public String listCostType() {
        return "cms/lease/leaseType";
    }
    /**
     * 融资租赁基础数据
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, Model model) {
        return leaseBasedataService.listLeaseType(dataTablesInput);
    }
    /**
     * 新增跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String addType() {
        return "cms/lease/addLeaseType";
    }
    /**
     * 保存数据
     *
     * @param model
     * @return
     * @Param LeaseItemType
     */
    @LogMessage(content = "租赁类型保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(LeaseItemType entity, Model model) {
        entity.setStatus(CmsConstant.APPROVED);
        leaseBasedataService.saveLeaseType(entity);
        leaseBasedataService.saveEntity(entity);
        return success();
    }
    /**
     * 保存数据
     *
     * @param model
     * @return
     * @Param LeaseItemType
     */
    @LogMessage(content = "租赁类型更新", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/updatedate")
    @ResponseBody
    public AjaxJson update(LeaseItemType leaseItemType, Model model) {
        leaseBasedataService.verify(leaseItemType);
        String id = leaseItemType.getId();
        String code = leaseItemType.getCode();
        String name = leaseItemType.getName();
        String rmk = leaseItemType.getRmk();
        leaseBasedataService.updateLeaseType(id, code, name, rmk);
        return success();
    }
    /**
     * 数据校验
     *
     * @param id
     * @param model
     * @return
     */
    @PostMapping("/validate")
    @ResponseBody
    public AjaxJson validate(String id, String Code, Model model) {
        boolean result = leaseBasedataService.validateCode(id, Code);
        AjaxJson ajaxJson = new AjaxJson();
        if (result) {
            ajaxJson.setSuccess(true);
        } else {
            ajaxJson.setSuccess(false);
        }
        return ajaxJson;
    }
    @LogMessage(content = "删除租赁类型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String id, Model model) {
        LeaseItemType entity = leaseBasedataService.get(id);
        leaseBasedataService.deleteLeaseType(id);
        entity.setStatus(CmsConstant.DELETED);
        leaseBasedataService.updateEntity(entity);
        return success();
    }
    /**
     * 编辑跳转
     *
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "租赁类型编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/update")
    public String add(String id, Model model) {
        model.addAttribute("leaseItemTypeVO", leaseBasedataService.getLeaseTypeById(id));
        return "cms/lease/updateLeaseType";
    }
}
