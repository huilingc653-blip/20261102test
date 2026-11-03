package com.bytter.finance.saas.controller.inl;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.inl.service.InlReportService;
import com.bytter.finance.domain.inl.vo.InlReportVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "内部拆借台账报表")
@RestController("v1InlReportController")
@RequestMapping("/v1-preposebill/inl/report")
public class InlReportController extends BaseController {
    @Autowired
    private InlReportService inlReportService;

    /**
     * 内部拆借台账报表列表
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/listReportData")
    public AjaxJson<DataTablesOutput> listReport(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        // Vue 清空还款状态后仍可能传空串，避免拼出 in ()
        Map<String, Object> formdata = input.getFormdata();
        if (formdata != null) {
            Object repayStatus = formdata.get("repayStatus");
            if (repayStatus != null && StringUtils.isBlank(repayStatus.toString())) {
                formdata.remove("repayStatus");
            }
        }
        return AjaxJson.success(inlReportService.listReport(input, getCurrentUser()));
    }

    /**
     * 内部拆借还款利息报表列表
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/listReportInterestData")
    public AjaxJson<DataTablesOutput> listReportInterestData(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlReportService.listReportInterest(input, getCurrentUser()));
    }

    /**
     * 导入数据
     *
     * @param list
     * @return
     */
    @LogMessage(content = "导入收款信息操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<InlReportVO> list) {
        inlReportService.saveImport(list, getCurrentUser());
        return success();
    }
}
