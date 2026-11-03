package com.bytter.finance.web.bond.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.bond.service.CmsBondExtendService;
import com.bytter.finance.domain.bond.service.CmsBondIssueInfoService;
import com.bytter.finance.domain.bond.vo.CmsBondExtendVO;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 债券展期
 *
 * @author wangf
 */
@Controller
@RequestMapping("/cms/bond/extend")
public class CmsBondExtendController extends BaseController {
    @Autowired
    private CmsBondExtendService cmsBondExtendService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    /**
     * 根据债券Id获取有效的债券发行编号
     *
     * @return 有效的债券类型集合
     */
    @RequestMapping("/listValidBondIssueNum/{registerId}")
    @ResponseBody
    public List<CmsBondIssueInfoVo> listValidBondIssueNum(@PathVariable("registerId") String registerId) {
        return cmsBondExtendService.listValidBondIssueNum(registerId);
    }
    /**
     * 债券展期列表主界面
     *
     * @return 页面路径
     */
    @GetMapping("/listBondExtendRegisterInfo")
    public String listTransferableRegisterInfo() {
        return "cms/bond/listBondExtendRegisterInfo";
    }
    /**
     * 查询债券展期注册列表
     *
     * @param input
     *            前端传参
     * @return 债券注册信息列表数据
     */
    @LogMessage(content = "查询债券展期注册列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/extendRegisterInfo/datatable")
    @ResponseBody
    public DataTablesOutput dataTableForTransferableRegisterInfo(@RequestBody DataTablesInput input) {
        return cmsBondExtendService.getTransferableRegisterDataTable(input, this.getCurrentUser());
    }
    /**
     * 获取债券展期新增页路径
     *
     * @return 债券展期新增页路径
     */
    @GetMapping("/add")
    public String add() {
        return "cms/bond/addBondExtend";
    }
    /**
     * 查询债券展期列表
     *
     * @return 页面路径
     */
    @LogMessage(content = "查询债券展期列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{registerId}/{isView}")
    public String list(@PathVariable String registerId, @PathVariable String isView, Model model) {
        model.addAttribute("registerId", registerId);
        model.addAttribute("isView", isView);
        return "cms/bond/listBondExtend";
    }
    /**
     * 债券展期列表数据
     *
     * @param input
     *            前端传参
     * @return 债券展期列表数据
     */
    @PostMapping("/datatable/{registerId}")
    @ResponseBody
    public DataTablesOutput dataTable(@PathVariable String registerId, @RequestBody DataTablesInput input) {
        return cmsBondExtendService.getDataTable(registerId, input);
    }
    /**
     * 债券展期编辑页面
     *
     * @param vo
     *            债券展期vo
     * @param model
     *            模型
     * @return页面路径
     */
    @GetMapping("/edit")
    public String editpage(CmsBondExtendVO vo, Model model) {
        model.addAttribute("registerId", vo.getRegisterId());
        if (EmptyUtils.isNotEmpty(vo.getId())) {
            vo = cmsBondExtendService.getCmsBondExtendById(vo.getId());
        }
        // 只有注册ID 先用注册ID拿到issueId
        if (StringUtils.isNotBlank(vo.getRegisterId())){
            Date lastExtendDate = null;
            CmsBondIssueInfoVo issueInfoVo = cmsBondIssueInfoService.getByRegisterId(vo.getRegisterId());
            if (StringUtils.isNotBlank(issueInfoVo.getId())){
                // 获取最新展期申请通过的数据
                CmsBondExtendVO lastExtend = cmsBondExtendService.getCmsBondExtendByIssueId(issueInfoVo.getId());
                if (lastExtend != null && lastExtend.getExtendEndDate() != null){
                    lastExtendDate = lastExtend.getExtendEndDate();//上次展期日
                }else {
                    lastExtendDate = issueInfoVo.getIssueEndDate();//发行结束日
                }
            }

            if (lastExtendDate != null) {
                model.addAttribute("lastExtendDate", DateUtils.formatDate(DateUtils.addDay(lastExtendDate, 1)));
            }
        }
        model.addAttribute("cmsBondExtendVO", vo);
        return "cms/bond/editBondExtend";
    }
    /**
     * 获取上一笔展期审批通过的到期日期
     *
     * @param issueId
     *            发行id
     * @param model
     *            模型
     * @return 上一笔展期审批通过的到期日期
     */
    @GetMapping("/getLastExtendDate/{issueId}")
    @ResponseBody
    public AjaxJson getLastExtendDate(@PathVariable("issueId") String issueId, Model model) {
        AjaxJson json = new AjaxJson();
        if (StringUtils.isNotEmpty(issueId)) {
            Map<String, Object> params = new HashMap<>(2);
            // 获取最新展期申请通过的数据
            CmsBondExtendVO lastExtend = cmsBondExtendService.getCmsBondExtendByIssueId(issueId);
            Date lastExtendDate = lastExtend.getExtendEndDate();
            if (lastExtend.getExtendEndDate() != null) {
                params.put("lastExtendDate", DateUtils.formatDate(lastExtendDate));
            }
            json.setSuccess(true);
            json.setAttributes(params);
        }
        return json;
    }
    /**
     * 新增或修改债券展期
     *
     * @param vo
     *            转债信息
     * @return 是否操作成功
     */
    @LogMessage(content = "新增或修改展期信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsBondExtendVO vo, String saveType) {
        cmsBondExtendService.save(vo, saveType, this.getCurrentUser());
        return success();
    }
    /**
     * 删除债券展期
     *
     * @param id
     *            债券展期id
     * @return 是否删除成功
     */
    @LogMessage(content = "删除展期信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        // 此id为债券注册id,需要查到债券发行id,再查出并删除最后一个展期
        cmsBondExtendService.delete(id, this.getCurrentUser());
        return success();
    }
    /**
     * @param id
     *            债券展期id
     * @param model
     *            模型
     * @return 债券展期信息
     */
    @LogMessage(content = "查看某个债券展期信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        CmsBondExtendVO vo = cmsBondExtendService.getCmsBondExtendById(id);
        model.addAttribute("cmsBondTransferVO", vo);
        return "cms/bond/viewBondExtend";
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "cms/bond/approve/listBondExtendApprove";
    }
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput input) {
        return cmsBondExtendService.dataTableApprove(input);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId
     *            id
     * @param taskId
     *            审批任务id
     * @param model
     *            模型
     * @return 前端页面路径
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        model.addAttribute("taskId", taskId);
        model.addAttribute("cmsBondExtendVO", cmsBondExtendService.getCmsBondExtendById(businessId));
        return "cms/bond/approve/viewBondExtendApprove";
    }
}
