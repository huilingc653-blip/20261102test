package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.domain.service.ConDraftService;
import com.bytter.guarantee.domain.service.ConInfoBusdtlService;
import com.bytter.guarantee.domain.service.ContractSignedService;
import com.bytter.guarantee.domain.vo.ConDraftVO;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.dto.ConInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @version V1.0
 * @Title: 合同签署Contorl.java
 * @Package com.bytter.biz.contract.controller
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-9 上午11:53:44
 */
@Controller
@RequestMapping("/contract/signed")
public class ContractSignedController extends BaseController {
    @Autowired
    private ContractSignedService contractSignedService;
    @Autowired
    private ConDraftService conDraftService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @Autowired
    private ConCommonService conCommonService;
    @GetMapping("/list")
    public String list(Model model) {
        return "contract/contractSignedList";
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/signApprove")
    public String listApprove(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "contract/approve/listConSignedApprove";
    }
    /**
     * @param @param
     *            dataTablesInput
     * @param @return
     *            参数
     * @return DataTablesOutput 返回类型
     * @throws @Title:
     *             dataTable
     * @Description: TODO(查询数据)
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return contractSignedService.getDataTablesUnion(dataTablesInput,this.getCurrentUser().getId());
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
        return contractSignedService.dataTableApprove(dataTablesInput);
    }
    /**
     * 批量操作
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "查询合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/batchOpt")
    @ResponseBody
    public AjaxJson batchOpt(String ids, String opt, String reason) {
        AjaxJson ajaxJson = success();
        try {
            contractSignedService.batchConSignedOptWith(ids, opt, reason, this.getCurrentUser());
            ajaxJson.setSuccess(true);
        } catch (RuntimeException e) {
            ajaxJson.setSuccess(false);
        }
        return ajaxJson;
    }
    /**
     * 某一行操作
     *
     * @param id
     * @return
     */
    @LogMessage(content = "查询合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/sigOpt")
    @ResponseBody
    public AjaxJson siginOpt(String id, String opt, String reason) {
        AjaxJson ajaxJson = success();
        try {
            contractSignedService.singleDispose(id, opt, this.getCurrentUser(), reason);
            ajaxJson.setSuccess(true);
            ajaxJson.setMsg("操作成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("操作失败:"+e.getMessage());
        }
        return ajaxJson;
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId
     *            审批流水ID
     * @param taskId
     *            流程ID
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            ConDraftVO conDraftVO = conDraftService.getConDraftVOByRid(businessId);
            List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService
                    .getConInfoBusDtl(new ConInfoBusdtlVO(conDraftVO.getMid()));
            if (null != conBusdtlList && conBusdtlList.size() > 0) {
                List<ConInfoBusdtlVO> busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
                conDraftVO.setBusdtls(busdtlList);
            }
            model.addAttribute("conDraftVO", conDraftVO);
        }
        model.addAttribute("taskId", taskId);
        return "contract/approve/approveConSigned";
    }
    /**
     * 单条拒绝理由
     *
     * @return
     */
    @GetMapping("/retreatPage/{id}")
    @LogMessage(content = "查询合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public String retreatPage(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("submit", "retreat");
        return "contract/contractSginCheckDtl";
    }
    @GetMapping("/mobile/approve")
    public String mobileSignApprove(String businessId, String flowStatus, Model model) {
        ConInfoVO params = new ConInfoVO();
        params.setRecordId(businessId);
        ConInfoDTO conInfo = conCommonService.getConInfo(params);
        model.addAttribute("conInfo", conInfo);
        model.addAttribute("flowStatus", flowStatus);
        return "contract/mobile/appContractBasic";
    }
}
