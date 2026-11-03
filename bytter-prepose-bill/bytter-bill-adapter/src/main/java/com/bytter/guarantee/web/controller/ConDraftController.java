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
import com.bytter.guarantee.domain.service.ConDraftService;
import com.bytter.guarantee.domain.service.ConInfoBusdtlService;
import com.bytter.guarantee.domain.vo.ConDraftVO;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import com.bytter.guarantee.domain.vo.ConInfoVO;
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
 * @Title: 合同起草Controller
 * @Description: 合同起草控制器类
 * @date 2019-11-04
 */
@Controller
@RequestMapping("/contract/condraft")
public class ConDraftController extends BaseController {
    @Autowired
    private ConDraftService conDraftService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @Autowired
    private ConCommonService conCommonService;
    /**
     * 合同起草列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "contract/listConDraft";
    }
    /**
     * 合同起草 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return conDraftService.getDataTablesUnion(input, this.getCurrentUser().getId());
    }
    /**
     * 删除起草信息
     *
     * @param rid 流水id
     * @return
     */
    @LogMessage(content = "删除起草信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson batchDelete(String rid) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(conDraftService.batchDelete(rid, this.getCurrentUser()));
        return success();
    }
    /**
     * 合同起草新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "contract/addConDraft";
    }
    /**
     * 暂存合同起草
     *
     * @param draftVo
     * @return
     */
    @LogMessage(content = "新增合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ConDraftVO draftVo, HttpServletRequest request) {
        conDraftService.saveDraftApply(draftVo, this.getCurrentUser());
        return success();
    }
    /**
     * 合同起草编辑页面跳转
     *
     * @return
     */
    @GetMapping("/edit/{rid}")
    public String edit(@PathVariable("rid") String rid, Model model) {
        ConDraftVO conDraftVO = conDraftService.getConDraftVOByRid(rid);
        model.addAttribute("conDraftVO", conDraftVO);
        return "contract/updateConDraft";
    }
    /**
     * 修改合同起草
     *
     * @param draftVo
     * @return
     */
    @LogMessage(content = "修改合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(ConDraftVO draftVo, HttpServletRequest request) {
        conDraftService.updateDraftApply(draftVo, this.getCurrentUser());
        return success();
    }
    /**
     * 提交合同起草
     *
     * @param draftVo
     * @return
     */
    @LogMessage(content = "提交合同起草", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(ConDraftVO draftVo, HttpServletRequest request) {
        conDraftService.submitDraftApply(draftVo, this.getCurrentUser());
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
        return "contract/approve/listConDraftApprove";
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
        return conDraftService.dataTableApprove(dataTablesInput);
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
            ConDraftVO conDraftVO = conDraftService.getConDraftVOByRid(businessId);
            List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService
                    .getConInfoBusDtl(new ConInfoBusdtlVO(conDraftVO.getMid()));
            if (ListUtils.isNotNullOrEmpty(conBusdtlList)) {
                List<ConInfoBusdtlVO> busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
                conDraftVO.setBusdtls(busdtlList);
            }
            model.addAttribute("conDraftVO", conDraftVO);
        }
        model.addAttribute("taskId", taskId);
        return "contract/approve/approveConDraft";
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
