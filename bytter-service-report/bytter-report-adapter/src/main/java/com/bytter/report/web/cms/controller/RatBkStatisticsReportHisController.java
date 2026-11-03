package com.bytter.report.web.cms.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.poi.enums.ResourceTemplateEnum;
import com.bytter.poi.excel.ExcelExportUtil;
import com.bytter.poi.excel.entity.TemplateExportParams;
import com.bytter.poi.excel.entity.vo.TemplateExcelConstants;
import com.bytter.report.domain.cms.service.RatBkStatisticsReportHisService;
import com.bytter.report.domain.cms.vo.RatBkStatisticSearchVO;
import com.bytter.report.domain.cms.vo.RatBkStatisticsReportHisVo;
import com.bytter.system.api.IDictService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cms/report/ratBkStaticHis")
public class RatBkStatisticsReportHisController extends BaseController {

    @Autowired
    private RatBkStatisticsReportHisService ratBkStatisticsReportHisService;

    @Autowired
    private IDictService dictService;

    @GetMapping("/listTemp")
    public String listTemp(RatBkStatisticSearchVO searchVO, Model model) {
        model.addAttribute("reportDate", searchVO.getReportDate());//报表日期
        model.addAttribute("ratMode", searchVO.getRatMode());//授信模式
        model.addAttribute("curCode", searchVO.getCurCode());//币别
        model.addAttribute("moneyType", searchVO.getMoneyType());//金额单位
        model.addAttribute("status", searchVO.getStatus());//状态
        model.addAttribute("beginDate", DateUtils.format(searchVO.getBeginDate(), "yyyy-MM-dd"));//授信开始时间
        model.addAttribute("beginDate2", DateUtils.format(searchVO.getBeginDate2(), "yyyy-MM-dd"));//授信开始时间
        model.addAttribute("endDate", DateUtils.format(searchVO.getEndDate(), "yyyy-MM-dd"));//授信结束时间
        model.addAttribute("endDate2", DateUtils.format(searchVO.getEndDate2(), "yyyy-MM-dd"));//授信结束时间

        return "cms/report/ratBankRation/listAmountStaticsHisTemp";
    }

    /**
     * 银行授信额度统计查询
     */
    @PostMapping("/list")
    public String list(RatBkStatisticSearchVO searchVO, Model model) {
        searchVO.setRatType(RationConstant.RAT_TYPE_0);//授信品种为银行授信
        List<RatBkStatisticsReportHisVo> list = ratBkStatisticsReportHisService.getRatBkAmountStatisticsListHis(searchVO, getCurrentUser());
        model.addAttribute("list", list);

        model.addAttribute("corpId", searchVO.getCorpId());//单位
        model.addAttribute("bankTypeId", searchVO.getBankTypeId());//银行
        model.addAttribute("ratMode", searchVO.getRatMode());//授信模式
        model.addAttribute("curCode", searchVO.getCurCode());//币别
        model.addAttribute("moneyType", searchVO.getMoneyType());//金额单位
        model.addAttribute("status", searchVO.getStatus());//状态
        model.addAttribute("beginDate", DateUtils.format(searchVO.getBeginDate(), "yyyy-MM-dd"));//授信开始时间
        model.addAttribute("beginDate2", DateUtils.format(searchVO.getBeginDate2(), "yyyy-MM-dd"));//授信开始时间
        model.addAttribute("endDate", DateUtils.format(searchVO.getEndDate(), "yyyy-MM-dd"));//授信结束时间
        model.addAttribute("endDate2", DateUtils.format(searchVO.getEndDate2(), "yyyy-MM-dd"));//授信结束时间
        model.addAttribute("reportDate", searchVO.getReportDate());//报表日期
        model.addAttribute("userName", getCurrentUser().getUsername());//制表人

        return "cms/report/ratBankRation/listAmountStaticsHis";
    }

    /**
     * 导出excel
     **/
    @LogMessage(content = "导出授信额度综合统计历史表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/excel")
    public String excel(ModelMap model, RatBkStatisticSearchVO searchVO, HttpServletRequest request, HttpServletResponse response) throws IOException {
        searchVO.setRatType(RationConstant.RAT_TYPE_0);//授信品种为银行授信
        List<RatBkStatisticsReportHisVo> list = ratBkStatisticsReportHisService.getRatBkAmountStatisticsListHis(searchVO, getCurrentUser());

        Map<String, Object> listmap = new HashMap<>();
        RatBkStatisticsReportHisVo sumVo = list.get(list.size() - 1);//最后一条为合计信息，单独设置
        RatBkStatisticsReportHisVo lastVo = new RatBkStatisticsReportHisVo();
        lastVo.setCorpName(sumVo.getCorpName());
        lastVo.setMoney(sumVo.getMoney());
        lastVo.setUseMoney(sumVo.getUseMoney());
        lastVo.setApproveMoney(sumVo.getApproveMoney());
        lastVo.setOverPlusMoney(sumVo.getOverPlusMoney());
        lastVo.setLeftMoney(sumVo.getLeftMoney());

        list.remove(list.size() - 1);//将原来的最后一条数据删除
        list.add(lastVo);//放入新的数据
        String curName = dictService.get("bisGatherPolicy.unit", searchVO.getMoneyType());
        listmap.put("list", list);
        listmap.put("title", "授信额度综合统计历史表");
        listmap.put("time", searchVO.getReportDate());
        listmap.put("curType", curName);
        listmap.put("createUser", getCurrentUser().getUsername());
        TemplateExportParams temmp = new TemplateExportParams(ResourceTemplateEnum.EXPORT_CMS_STATISCS_EXP_HIS_XLSX.getPath());
        model.put(TemplateExcelConstants.FILE_NAME, "授信额度综合统计历史表");
        model.put(TemplateExcelConstants.PARAMS, temmp);
        model.put(TemplateExcelConstants.MAP_DATA, listmap);
        String codedFileName = "临时文件";
        Workbook workbook = ExcelExportUtil.exportExcel((TemplateExportParams) model.get("params"), (Class) model.get("entity"), (List) model.get("list"), (Map) model.get("map"));
        if (model.containsKey("fileName")) {
            codedFileName = (String) model.get("fileName");
        }
        if (workbook instanceof HSSFWorkbook) {
            codedFileName = codedFileName + ".xls";
        } else {
            codedFileName = codedFileName + ".xlsx";
        }
        if (this.isIE(request)) {
            codedFileName = URLEncoder.encode(codedFileName, "UTF8");
        } else {
            codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
        return null;
    }

    protected boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
}
