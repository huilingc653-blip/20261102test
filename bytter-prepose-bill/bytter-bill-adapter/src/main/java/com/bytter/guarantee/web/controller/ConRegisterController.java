package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.domain.service.ConInfoBusdtlService;
import com.bytter.guarantee.domain.service.ConRegisterService;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.domain.vo.ConRegisterVO;
import com.bytter.guarantee.dto.ConInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同登记Controller
 * @Description: 合同登记控制器类
 * @date 2019-11-11
 */
@Controller
@RequestMapping("/contract/conregister")
public class ConRegisterController extends BaseController {
    @Autowired
    private ConRegisterService conRegisterService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @Autowired
    private ConCommonService conCommonService;
    /**
     * 合同登记列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "contract/listConRegister";
    }
    /**
     * 合同登记 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询合同登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return conRegisterService.getDataTablesUnion(input, this.getCurrentUser().getId());
    }
    /**
     * 删除登记信息
     *
     * @param rid 流水id
     * @return
     */
    @LogMessage(content = "删除登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson batchDelete(String rid) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(conRegisterService.batchDelete(rid, this.getCurrentUser()));
        return success();
    }
    /**
     * 合同登记新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "contract/addConRegister";
    }
    /**
     * 暂存合同登记
     *
     * @param registerVO
     * @return
     */
    @LogMessage(content = "新增合同登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ConRegisterVO registerVO, HttpServletRequest request) {
        conRegisterService.saveRegisterApply(registerVO, this.getCurrentUser());
        return success();
    }
    /**
     * 合同登记编辑页面跳转
     *
     * @return
     */
    @GetMapping("/edit/{rid}")
    public String edit(@PathVariable("rid") String rid, Model model) {
        ConRegisterVO registerVO = conRegisterService.getConRegisterVOByRid(rid);
        model.addAttribute("registerVO", registerVO);
        return "contract/updateConRegister";
    }
    /**
     * 修改合同登记
     *
     * @param registerVO
     * @return
     */
    @LogMessage(content = "修改合同登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(ConRegisterVO registerVO, HttpServletRequest request) {
        conRegisterService.updateRegisterApply(registerVO, this.getCurrentUser());
        return success();
    }
    /**
     * 提交合同登记
     *
     * @param registerVO
     * @return
     */
    @LogMessage(content = "提交合同登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(ConRegisterVO registerVO, HttpServletRequest request) {
        conRegisterService.submitRegisterApply(registerVO, this.getCurrentUser());
        return success();
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "contract/approve/listConRegisterApprove";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return conRegisterService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId 审批流水ID
     * @param taskId     流程ID
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            ConRegisterVO conRegisterVO = conRegisterService.getConRegisterVOByRid(businessId);
            List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService
                    .getConInfoBusDtl(new ConInfoBusdtlVO(conRegisterVO.getMid()));
            if (ListUtils.isNotNullOrEmpty(conBusdtlList)) {
                List<ConInfoBusdtlVO> busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
                conRegisterVO.setBusdtls(busdtlList);
            }
            model.addAttribute("conRegisterVO", conRegisterVO);
        }
        model.addAttribute("taskId", taskId);
        return "contract/approve/approveConRegister";
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            ConInfoVO params = new ConInfoVO();
            params.setRecordId(businessId);
            ConInfoDTO conInfo = conCommonService.getConInfo(params);
            model.addAttribute("conInfo", conInfo);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return "contract/mobile/appContractBasic";
    }
}
