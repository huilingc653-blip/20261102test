package com.bytter.biz.budget.saas.controller.budget;

import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.service.FbsBudgetPeriodService;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.biz.budget.vo.BudgetProcessReportVO;
import com.bytter.biz.budget.vo.FbsBudgetPeriodVO;
import com.bytter.biz.report.service.FbsBudgetDownloadReportService;
import com.bytter.biz.report.service.FbsBudgetProcessReportService;
import com.bytter.biz.report.vo.FbsBudgetProcessReportVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金计划上报报表
 *
 * @author zuoy 2018-02-06
 */
@RestController("v1FbsBudgetProcessReportController")
@Api(value = "/v1-plan/budget/fbsBudgetProcessReport", tags = "资金计划上报报表")
@RequestMapping("/v1-plan/budget/fbsBudgetProcessReport")
public class FbsBudgetProcessReportController extends BaseController {
    @Autowired
    private FbsBudgetProcessReportService fbsBudgetProcessReportService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private FbsBudgetDownloadReportService fbsBudgetDownloadReportService;
    @Autowired
    private FbsBudgetService fbsBudgetService;
    @Autowired
    private FbsBudgetPeriodService fbsBudgetPeriodService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 查询
     *
     * @param timeType  预算类型
     * @param corpId    单位ID
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param isInfo    查看页面
     * @return
     */
    @GetMapping("/seach")
    public AjaxJson<BudgetProcessReportVO> seach(HttpServletRequest request, String timeType, String corpId, Date beginDate, Date endDate, boolean isInfo) {
        BudgetProcessReportVO model = new BudgetProcessReportVO();
        FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate, timeType);
        Date seachBeginDate = fbsBudgetPeriodVO.getBeginDate();
        Date seachEndDate = fbsBudgetPeriodVO.getEndDate();
        model.setTimeType(timeType);
        model.setCorpId(corpId);
        // 单个周期
        boolean onePeriod = beginDate.compareTo(seachBeginDate) != -1 && endDate.compareTo(seachEndDate) != 1;
        if (onePeriod || isInfo) {
            CorpDTO corpDTO = this.corpService.get(corpId);
            model.setCorpName(corpDTO.getName());
            model.setBeginDate(seachBeginDate);
            model.setEndDate(seachEndDate);
            //获取计划
            FbsBudget fbsBudget = fbsBudgetService.getCorpBudget(corpId, timeType, seachBeginDate, seachEndDate);
            model.setFbsBudget(  fbsBudget);
            List<FbsBudgetProcessReportVO> reportList = this.fbsBudgetProcessReportService.listFbsBudgetProcessReport(timeType, corpId, seachBeginDate, seachEndDate);
            model.setReportList( reportList);
            String curName = "";
            if (fbsBudget != null) {
                curName = btCurrencyService.getObjectById(fbsBudget.getCurId()).getCurName();
            }
            String title = corpDTO.getName() + "资金计划执行表" + " " + DateUtils.formatDate(seachBeginDate) + "~" + DateUtils.formatDate(seachEndDate) + " " + curName;
            String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "资金计划执行表.xls";
            this.fbsBudgetDownloadReportService.exporFbsBudgetProcess(request, reportList, filePath, title);
            model.setForwardUrl("report/reportFbsBudgetProcess");
            return AjaxJson.success(model);
        } else {
            model.setBeginDate(  beginDate);
            model.setEndDate(  endDate);
            model.setForwardUrl("report/listFbsBudgetProcessReport");
            return AjaxJson.success(model);
        }
    }




    /**
     * 查询
     *
     * @param request
     * @param timeType
     * @param corpId
     * @param beginDate
     * @return
     */
    @PostMapping("/seachCollect")
    public AjaxJson<BudgetProcessReportVO> seachCollect(HttpServletRequest request, String timeType, String corpId, Date beginDate, String itemCode) {
        BudgetProcessReportVO model=new  BudgetProcessReportVO();
        FbsBudgetPeriodVO fbsBudgetPeriodVO = fbsBudgetPeriodService.getCurBudgetPeriodByTimeType(beginDate, timeType);
        Date seachBeginDate = fbsBudgetPeriodVO.getBeginDate();
        Date seachEndDate = fbsBudgetPeriodVO.getEndDate();
        model.setTimeType(  timeType);
        model.setCorpId(  corpId);
        String corpName = "";
        String[] corpIdStr = corpId.split(",");
        String[] itemCodeStr = itemCode.split(",");
        for (int i = 0; i < corpIdStr.length; i++) {
            CorpDTO corpDTO = this.corpService.get(corpIdStr[i]);
            if (i == 0) {
                corpName = corpDTO.getName();
            } else {
                corpName += "," + corpDTO.getName();
            }
        }
        model.setCorpName(  corpName);
        model.setBeginDate( seachBeginDate);
        model.setEndDate(  seachEndDate);
        model.setItemCode( itemCode);
        List<FbsBudgetProcessReportVO> reportList = this.fbsBudgetProcessReportService.listFbsBudgetProcessReport(timeType, corpIdStr, seachBeginDate, seachEndDate, itemCodeStr);
        model.setReportList(  reportList);
        String title = "资金计划执行单位汇总表" + " " + DateUtils.formatDate(seachBeginDate) + "~" + DateUtils.formatDate(seachEndDate) + " ";
        String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + "资金计划执行单位汇总表.xls";
        this.fbsBudgetDownloadReportService.exporFbsBudgetProcess(request, reportList, filePath, title);
        return AjaxJson.success(model);
    }

    /**
     * 查询中间页面
     *
     * @param timeType  预算类型
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @GetMapping("/seachManyTemp")
    public AjaxJson<Map<String,Object>> seachManyTemp( String timeType, Date beginDate, Date endDate) {
        Map<String,Object> model=new HashMap<>();
        model.put("timeType", timeType);
        model.put("beginDate", beginDate);
        model.put("endDate", endDate);
        return AjaxJson.success(model);
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input= dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fbsBudgetProcessReportService.dataTable(input));
    }

    /**
     * 跳转预算执行明细页面
     *
     * @param itemCode      科目编码
     * @param corpId        单位ID
     * @param beginDate     开始日期
     * @param endDate       结束日期
     * @param statusFlag    状态	true-占用 false-在途
     * @param innerSignFlag 内转标志	true-内转 false-非内转
     * @return
     */
    @GetMapping("/openProcessInfo")
    public  AjaxJson<Map<String,Object>> openProcessInfo(String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag, boolean innerSignFlag, String curId, String payeeAccName) {
        Map<String,Object> model=new HashMap<>();
        model.put("itemCode", itemCode);
        model.put("corpId", corpId);
        CorpDTO corpDTO = this.corpService.get(corpId);
        model.put("corpName", corpDTO.getName());
        model.put("beginDate", beginDate);
        model.put("endDate", endDate);
        model.put("statusFlag", statusFlag);
        model.put("innerSign", innerSignFlag ? BudgetConstant.INNER_SIGN_1 : BudgetConstant.INNER_SIGN_0);
        model.put("curId", curId);
        model.put("payeeAccName", payeeAccName);
        return AjaxJson.success(model);
    }

    /**
     * 跳转预算执行明细页面中间页面
     *
     * @param itemCode      科目编码
     * @param beginDate     开始日期
     * @param endDate       结束日期
     * @param statusFlag    状态	true-占用 false-在途
     * @param innerSignFlag 内转标志	true-内转 false-非内转
     * @return
     */
    @GetMapping("/openProcessInfoTemp")
    public   AjaxJson<Map<String,Object>> openProcessInfoTemp(String itemCode, Date beginDate, Date endDate, boolean statusFlag, boolean innerSignFlag) {
        Map<String,Object> model=new HashMap<>();
        model.put("itemCode", itemCode);
        model.put("beginDate", beginDate);
        model.put("endDate", endDate);
        model.put("statusFlag", statusFlag);
        model.put("innerSign", innerSignFlag);
        return AjaxJson.success(model);
    }

    /**
     * 跳转预算执行明细页面
     *
     * @param itemCode      科目编码
     * @param corpId        单位ID
     * @param beginDate     开始日期
     * @param endDate       结束日期
     * @param statusFlag    状态	true-占用 false-在途
     * @param innerSignFlag 内转标志	true-内转 false-非内转
     * @return
     */
    @PostMapping("/openProcessInfoCollect")
    public AjaxJson<Map<String,Object>> openProcessInfoCollect(String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag, boolean innerSignFlag) {
        Map<String,Object> model=new HashMap<>();
        model.put("itemCode", itemCode);
        model.put("corpId", corpId);
        model.put("beginDate", beginDate);
        model.put("endDate", endDate);
        model.put("statusFlag", statusFlag);
        model.put("innerSign", innerSignFlag ? BudgetConstant.INNER_SIGN_1 : BudgetConstant.INNER_SIGN_0);
        return AjaxJson.success(model);
    }

    /**
     * 显示预算执行明细数据
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/dataTablesProcessInfo")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTablesProcessInfo(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input= dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fbsBudgetProcessReportService.dataTablesProcessInfo(input));
    }

    /**
     * 跳转预算追加明细页面
     *
     * @param itemCode   科目编码
     * @param corpId     单位ID
     * @param beginDate  开始日期
     * @param endDate    结束日期
     * @param statusFlag 状态	true-占用 false-在途
     * @return
     */
    @GetMapping("/openSuperaddInfo")
    public AjaxJson<Map<String,Object>> openSuperaddInfo( String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag, String timeType) {
        Map<String,Object> model=new HashMap<>();
        model.put("itemCode", itemCode);
        model.put("corpId", corpId);
        CorpDTO corpDTO = this.corpService.get(corpId);
        model.put("corpName", corpDTO.getName());
        model.put("beginDate", beginDate);
        model.put("endDate", endDate);
        model.put("statusFlag", statusFlag);
        model.put("timeType", timeType);
        return AjaxJson.success(model);
    }

    /**
     * 跳转预算追加明细页面
     *
     * @param itemCode   科目编码
     * @param beginDate  开始日期
     * @param endDate    结束日期
     * @param statusFlag 状态	true-占用 false-在途
     * @return
     */
    @GetMapping("/openSuperaddInfoTemp")
    public AjaxJson<Map<String,Object>> openSuperaddInfoTemp(String itemCode, Date beginDate, Date endDate, boolean statusFlag, String timeType) {
        Map<String,Object> model=new HashMap<>();
        model.put("itemCode", itemCode);
        model.put("beginDate", beginDate);
        model.put("endDate", endDate);
        model.put("statusFlag", statusFlag);
        model.put("timeType", timeType);
        return AjaxJson.success(model);
    }

    /**
     * 跳转预算追加明细页面
     *
     * @param itemCode   科目编码
     * @param corpId     单位ID
     * @param beginDate  开始日期
     * @param endDate    结束日期
     * @param statusFlag 状态	true-占用 false-在途
     * @return
     */
    @PostMapping("/openSuperaddInfoCollect")
    public AjaxJson<Map<String,Object>> openSuperaddInfoCollect(String itemCode, String corpId, Date beginDate, Date endDate, boolean statusFlag, String timeType) {
        Map<String,Object> model=new HashMap<>();
        model.put("itemCode", itemCode);
        model.put("corpId", corpId);
        model.put("beginDate", beginDate);
        model.put("endDate", endDate);
        model.put("statusFlag", statusFlag);
        model.put("timeType", timeType);
        return AjaxJson.success(model);
    }

    /**
     * 显示预算追加明细数据
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/dataTablesSuperaddInfo")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTablesSuperaddInfo(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input= dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fbsBudgetProcessReportService.dataTablesSuperaddInfo(input));
    }

    /**
     * 导出预算模板
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/exportsBudgetProcess")
    public void exportsBudgetProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = "资金计划执行表.xls";
        String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + fileName;
        this.fbsBudgetDownloadReportService.exports(request, response, fileName, filePath, BudgetConstant.BUDGET_EXPORTS_TYPE_BUDGETPROCESS);
    }

    /**
     * 导出预算模板
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/exportsBudgetProcessCollect")
    public void exportsBudgetProcessCollect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = "资金计划执行单位汇总表.xls";
        String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "budget" + File.separator + fileName;
        this.fbsBudgetDownloadReportService.exports(request, response, fileName, filePath, BudgetConstant.BUDGET_EXPORTS_TYPE_BUDGETPROCESS);
    }



    /**
     * 显示预算执行明细数据
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/dataTablesDetailProcessInfo")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTablesDetailProcessInfo(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fbsBudgetProcessReportService.dataTablesDetailProcessInfo(input));
    }
}
