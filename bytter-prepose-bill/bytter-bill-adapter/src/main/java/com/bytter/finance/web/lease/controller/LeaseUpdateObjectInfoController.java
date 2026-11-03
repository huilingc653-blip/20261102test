package com.bytter.finance.web.lease.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.lease.entity.LeaseObjectInfo;
import com.bytter.finance.domain.lease.service.LeaseObjectInfoService;
import com.bytter.finance.domain.lease.service.LeaseUpdateObjectInfoService;
import com.bytter.finance.domain.lease.vo.ContractLeaseObjectInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseObjectInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
@Controller
@RequestMapping("/cms/lease/UpdateObjectInfo")
public class LeaseUpdateObjectInfoController extends BaseController {
    @Autowired
    private LeaseUpdateObjectInfoService leaseUpdateObjectInfoService;
    @Autowired
    private LeaseObjectInfoService leaseObjectInfoService;
    /**
     * 跳转融资租赁标的物修改页面
     *
     * @return
     */
    @LogMessage(content = "跳转融资租赁标的物修改页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "cms/lease/listLeaseUpdateObjectInfo";
    }
    /**
     * 融资申请列表 - 数据
     */
    @PostMapping("/leaseDatatable")
    @ResponseBody
    public DataTablesOutput leaseDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return leaseUpdateObjectInfoService.getDataleaseDatatables(dataTablesInput, getCurrentUser());
    }
    /**
     * 跳转融资租赁标的物列表页面
     *
     * @return
     */
    @LogMessage(content = "跳转融资租赁标的物修改页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/toList/{id}")
    public String toList(@PathVariable("id") String id, Model model) {
        model.addAttribute("leaseId", id);
        return "cms/lease/listLeaseUpdateRegistration";
    }
    /*
     * 查看租赁物
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        LeaseObjectInfoVO leaseObjectInfoVO = leaseObjectInfoService.queryLeaseObjectInfo(id);
        if (EmptyUtils.isEmpty(leaseObjectInfoVO.getOldMoney())) {
            leaseObjectInfoVO.setOldMoney(BigDecimal.ZERO);
        }
        model.addAttribute("leaseObjectInfoVO", leaseObjectInfoVO);
        return "cms/lease/viewLeaseRegistration";
    }
    /*
     * 租赁物列表
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input, String leaseId, Model model) {
        model.addAttribute("leaseId", leaseId);
        return leaseUpdateObjectInfoService.queryLeaseObjectInfoList(input, getUserId(), leaseId);
    }
    /*
     * 删除租赁物
     */
    @PostMapping("/del/{id}")
    @ResponseBody
    public AjaxJson del(@PathVariable("id") String id) {
        LeaseObjectInfo leaseObjectInfo = leaseObjectInfoService.get(id);
        leaseObjectInfo.setStatus(CmsConstant.DELETED);
        leaseObjectInfoService.updateEntity(leaseObjectInfo);
        return success();
    }
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<ContractLeaseObjectInfoVO> list,
            @RequestParam("leaseId") String leaseId) {
        this.leaseUpdateObjectInfoService.saveImport(list, this.getCurrentUser(), leaseId);
        return success();
    }
    /*
     * 查看租赁物列表
     */
    @LogMessage(content = "跳转融资租赁标的物查看列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewList/{id}")
    public String viewList(@PathVariable("id") String id, Model model) {
        model.addAttribute("leaseId", id);
        return "cms/lease/viewListUpdateLeaseRegistration";
    }
    /*
     * 添加租赁物修改页面跳转
     */
    @GetMapping("/add/{leaseId}")
    public String add(@PathVariable("leaseId") String leaseId, Model model) {
        model.addAttribute("leaseId", leaseId);
        return "cms/lease/addLeaseUpdateRegistration";
    }
    /*
     * 保存租赁物
     */
    @LogMessage(content = "添加租的物", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(LeaseObjectInfoVO leaseObjectInfoVO) {
        if (leaseObjectInfoVO.getStatus() == 0) {// 暂存
            leaseObjectInfoVO.setStatus(CmsConstant.LEASE_TEMP);
        } else if (leaseObjectInfoVO.getStatus() == 1) {// 提交
            leaseObjectInfoVO.setStatus(CmsConstant.LEASE_COMMITAPPROVE);
        }
        leaseUpdateObjectInfoService.saveLeaseObjectInfo(leaseObjectInfoVO, getCurrentUser());
        return success();
    }
    /*
     * 判断租赁物编号是否已经存在
     */
    @PostMapping("/checkLeaseItemCode/{leaseItemCode}/{id}")
    @ResponseBody
    public String checkLeaseItemCode(@PathVariable("leaseItemCode") String leaseItemCode,
            @PathVariable("id") String id) {
        boolean b = leaseUpdateObjectInfoService.checkLeaseItemCode(leaseItemCode, id);
        if (b) { // 查询总数为0
            return "true";
        }
        return "false";// 查询总数不为0，系统已存在该租赁物编号
    }
    /*
     * 修改租赁物
     */
    @GetMapping("/update/{id}/{leaseId}")
    public String update(@PathVariable("id") String id, @PathVariable("leaseId") String leaseId, Model model) {
        LeaseObjectInfoVO leaseObjectInfoVO = leaseUpdateObjectInfoService.queryLeaseObjectInfo(id);
        model.addAttribute("leaseId", leaseId);
        model.addAttribute("leaseObjectInfoVO", leaseObjectInfoVO);
        return "cms/lease/updateLeaseUpdateRegistration";
    }
    /*
     * 修改租赁物
     */
    @LogMessage(content = "添加租的物", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(LeaseObjectInfoVO entity) {
        if (entity.getStatus() == 0) {// 暂存
            entity.setStatus(CmsConstant.LEASE_TEMP); // 修改暂存
        } else if (entity.getStatus() == 1) {// 提交
            entity.setStatus(CmsConstant.LEASE_COMMITAPPROVE);// 修改待审批
        }
        leaseUpdateObjectInfoService.modifyLeaseObjectInfo(entity, getCurrentUser());
        return success();
    }
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/listApproveLeaseUpdateObjectInfo";
    }
    /**
     * 列表查询
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/approveDataTable")
    public DataTablesOutput listInlInfoApprove(@RequestBody DataTablesInput input) {
        return leaseUpdateObjectInfoService.approveDataTable(input, getCurrentUser());
    }
}
