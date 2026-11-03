package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbBillDiscountApplyService;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillDiscountApplyVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description:贴现申请Controller
 * @author onlineGenerator
 * @date 2017-07-03 11:25:01
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abBillDiscountApply", tags = "贴现申请")
@RestController("v1AbBillDiscountApplyController")
@RequestMapping("/v1-preposebill/bill/accebill/abBillDiscountApply")
public class AbBillDiscountApplyController extends BaseController {
    @Autowired
    private AbBillDiscountApplyService abBillDiscountApplyService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    /**
     * 贴现申请页面初始化
     */
    @GetMapping("/list")
    @ApiOperation("贴现申请列表页面初始化")
    public AjaxJson<Map<String, Object>> list() {
        ConcurrentModel model = new ConcurrentModel();
        //不启用贴现申请功能直接贴现办理
        model.addAttribute("isNotDiscountApply", abVoucherinfoViceService.isNotDiscountApply());
        return AjaxJson.success(model.asMap());
    }

    /**
     * 贴现申请列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("贴现申请分页查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillDiscountApplyService.listAbBillDiscountApply(input);
        return AjaxJson.success(output);
    }

    /**
     * 贴现申请批量提交
     */
    @LogMessage(content = "批量提交应收票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchSubmit")
    @ApiOperation("批量提交应收票据贴现申请")
    public AjaxJson batchSubmitAbBillDiscountApply(@RequestBody AbBillDiscountApplyVO abBillDiscountApplyVO) {
        AjaxJson j = AjaxJson.success();
        boolean flag = abBillDiscountApplyService.batchSubmitAbBillDiscountApply(abBillDiscountApplyVO, this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }

    /**
     * 导入贴现申请
     */
    @LogMessage(content = "导入应收票据贴现申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveImport")
    @ApiOperation("导入应收票据贴现申请信息")
    public AjaxJson saveImport(@RequestBody List<AbBillDiscountApplyVO> list) {
        AjaxJson json = AjaxJson.success();
        boolean flag = abBillDiscountApplyService.saveImport(list, this.getCurrentUser());
        json.setSuccess(flag);
        return json;
    }

    /**
     * 贴现申请审核列表页面初始化
     */
    @GetMapping("/listApprove")
    @ApiOperation("贴现申请审核列表页面初始化")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        ConcurrentModel model = new ConcurrentModel();
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("贴现申请审核分页查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillDiscountApplyService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("贴现申请审核详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillDiscountApplyVO info = abBillDiscountApplyService.getAbBillDiscountApplyVOById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
            abBillAttachmentService.viewAttachment(businessId, model);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("贴现申请手机端审核详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        ConcurrentModel model = new ConcurrentModel();
        if (StringUtils.isNotEmpty(businessId)) {
            AbBillDiscountApplyVO info = abBillDiscountApplyService.getAbBillDiscountApplyVOById(businessId);
            model.addAttribute("info", info);
            model.addAttribute("isApprove", true);
        }
        model.addAttribute("taskId", taskId);
        return AjaxJson.success(model.asMap());
    }

    /**
     * 通过单位id获取单位信息
     */
    @PostMapping("/getSysCorpById")
    @ApiOperation("根据单位ID查询单位信息")
    public AjaxJson<CorpDTO> getSysCorpById(String id) {
        CorpDTO dto = abCorpService.get(id);
        return AjaxJson.success(dto);
    }

    /**
     * 通过单位id串获取单位信息集合
     */
    @PostMapping("/listSysCorp")
    @ApiOperation("根据单位ID/名称查询单位集合")
    public AjaxJson listSysCorp(String corpIds, String corpNames) {
        AjaxJson json = AjaxJson.success();
        List<CorpDTO> list = abBillDiscountApplyService.listSysCorp(corpIds, corpNames);
        if (list == null || list.isEmpty()) {
            json.setSuccess(false);
        } else {
            json.setSuccess(true);
            json.setObj(list);
        }
        return json;
    }
}