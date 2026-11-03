package com.bytter.report.web.financial.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.report.domain.financial.service.GeneralLedgerService;
import com.bytter.report.domain.financial.vo.GeneralLedgerSearchVO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
/**
 * 总账查询
 *
 * @author zuoy 2020-11-19
 */
@Controller
@RequestMapping("/bi/financial")
public class GeneralLedgerController extends BaseController {
    @Autowired
    public GeneralLedgerService generalLedgerService;
        @GetMapping("/listGeneralLedger")
    public String listGeneralLedger(Model model) {
        return "financial/listGeneralLedger";
    }
    /**
     * 总账查询列表页面
     *
     * @param dataTablesInput
     * @return
     */
        @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return this.generalLedgerService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    @GetMapping("/viewDetailLedger")
    public String viewDetailLedger(Model model, GeneralLedgerSearchVO generalLedgerSearchVO) {
        model.addAttribute("generalLedgerSearchVO", generalLedgerSearchVO);
        model.addAttribute("year", generalLedgerSearchVO.getEndDate().split("-")[0]);
        return "financial/viewDetailLedger";
    }
    /**
     * 总账查询详情页数据加载
     */
    @PostMapping("/getGeneralLedgerDetailList")
    @ResponseBody
    public Object getGeneralLedgerDetailList(GeneralLedgerSearchVO searchVO) {
        return generalLedgerService.getGeneralLedgerDetailList(searchVO);
    }
    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportCurrentPage")
    public void exportCurrentPage(HttpServletRequest request, HttpServletResponse response, String param) throws Exception {
        // 转换查询条件
        ObjectMapper mapper = new ObjectMapper();
        GeneralLedgerSearchVO searchVO = null;
        try {
            searchVO = mapper.readValue(URLDecoder.decode(param, "UTF-8"), GeneralLedgerSearchVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        generalLedgerService.exportCurrentPage(request, response, searchVO);
    }
    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletRequest request, HttpServletResponse response, String param, String subjectParam) throws Exception {
        // 转换查询条件
        ObjectMapper mapper = new ObjectMapper();
        GeneralLedgerSearchVO searchVO = null;
        try {
            searchVO = mapper.readValue(URLDecoder.decode(param, "UTF-8"), GeneralLedgerSearchVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<FcSubjectDTO> subjectList = null;
        try {
            subjectList = mapper.readValue(URLDecoder.decode(subjectParam, "UTF-8"), new TypeReference<List<FcSubjectDTO>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        generalLedgerService.exportAll(request, response, searchVO, subjectList);
    }
}
