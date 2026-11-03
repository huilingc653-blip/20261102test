package com.bytter.biz.finacial.interest.controller;

import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.interest.service.FcPredrawDtlService;
import com.bytter.biz.finacial.interest.vo.FcPredrawDtlVo;
import com.bytter.biz.finacial.interest.vo.InterestFactSheetVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.vo.NormalExcelConstants;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author zhengxn
 * @version 1.0
 * @Title: 利息管理--预提明细查询表 /利息预提情况表
 * @Description: FC_PREDRAW_DTL
 * @date 2017-06-28 13:55:58
 */
@Controller
@RequestMapping("/finacial/interest/fcpredrawdtl")
public class FcPredrawDtlController extends BaseController {
    @Autowired
    private FcPredrawDtlService fcPredrawDtlService;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * 预提明细查询表 列表 页面跳转
     * <b>日期：</b>2017年6月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_FCPREDRAWDTL_DETAIL_QUERY)
    @LogMessage(content = "查询FC_PREDRAW_DTL列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "finacial/interest/listFcPredrawDtl";
    }
    /**
     * 预提明细查询表 列表
     * <b>日期：</b>2017年6月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_FCPREDRAWDTL_DETAIL_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, Model model) {
        return fcPredrawDtlService.listAll(dataTablesInput, FinacialResourceConstant.FINACIAL_FCPREDRAWDTL_DETAIL_QUERY, getUserId());
    }
    /**
     * 导出 预提明细查询表
     * <b>日期：</b>2017年6月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/excel")
    public String excel(ModelMap map, DataTablesInput dataTablesInput) {
        DataTablesOutput dto = fcPredrawDtlService.listAll(dataTablesInput, FinacialResourceConstant.FINACIAL_FCPREDRAWDTL_DETAIL_QUERY, getUserId());
        List list = dto.getData();
        map.put(NormalExcelConstants.FILE_NAME, "预提明细表");
        map.put(NormalExcelConstants.CLASS, FcPredrawDtlVo.class);
        map.put(NormalExcelConstants.PARAMS, new ExportParams("预提明细表", "", "预提明细表"));
        map.put(NormalExcelConstants.DATA_LIST, list);
        return NormalExcelConstants.EXCEL_VIEW;
    }
    /**
     * 利息预提情况表
     * <b>日期：</b>2017年10月10日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/factSheet")
    public String factSheet(Model model, FcPredrawDtlVo vo) {
        String curName = "";
        if (EmptyUtils.isNotEmpty(vo.getCurCode()) && !" ".equals(vo.getCurCode())) {
            curName = fcPredrawDtlService.getCurNameById(vo.getCurCode());
        }
        model.addAttribute("curName", curName);
        /*Integer pageNum = 1;
    	model.addAttribute("pageNum", pageNum);
    	Map<String, Object>  result = fcPredrawDtlService.queryFactSheet(vo);
    	model.addAttribute("size",result.get("size"));
    	model.addAttribute("res",result.get("res"));
    	model.addAttribute("search", vo);//查询条件
*/
        return "finacial/interest/listInterestFactSheet";
    }
    /**
     * 利息预提情况表 列表
     * <b>日期：</b>2017年6月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    ////@RequiresPermissions(FinacialResourceConstant.FINACIAL_FCPREDRAWDTL_FACTSHEET_QUERY)
    @PostMapping("/tablefactSheet")
    @ResponseBody
    public DataTablesOutput tableFactSheet(@RequestBody DataTablesInput dataTablesInput, Model model) {
        DataTablesOutput dto = fcPredrawDtlService.queryFactSheet(dataTablesInput, FinacialResourceConstant.FINACIAL_FCPREDRAWDTL_FACTSHEET_QUERY, getUserId());
        List<InterestFactSheetVo> res = dto.getData();
        InterestFactSheetVo fpd = new InterestFactSheetVo();
        BigDecimal sumInterest = new BigDecimal(0);
        for (InterestFactSheetVo interestFactSheetVo : res) {
            sumInterest = sumInterest.add(interestFactSheetVo.getInterest() == null ? new BigDecimal(0) : interestFactSheetVo.getInterest());
        }
        fpd.setInterest(sumInterest);
        String total = multiLangServiceI.getLang("table.datatable.total");
        fpd.setCorpName(total);
        res.add(fpd);
        return dto;
    }
    /**
     * 导出 利息预提情况表
     * <b>日期：</b>2017年10月11日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/excelFactSheet")
    public String excelFactSheet(ModelMap map, DataTablesInput dataTablesInput) {
        DataTablesOutput dto = fcPredrawDtlService.queryFactSheet(dataTablesInput, FinacialResourceConstant.FINACIAL_FCPREDRAWDTL_FACTSHEET_QUERY, getUserId());
        List<InterestFactSheetVo> list = dto.getData();
        InterestFactSheetVo fpd = new InterestFactSheetVo();
        BigDecimal sumInterest = new BigDecimal(0);
        for (InterestFactSheetVo interestFactSheetVo : list) {
            sumInterest = sumInterest.add(interestFactSheetVo.getInterest() == null ? new BigDecimal(0) : interestFactSheetVo.getInterest());
        }
        fpd.setInterest(sumInterest);
        fpd.setCorpName("合计");
        list.add(fpd);
        map.put(NormalExcelConstants.FILE_NAME, "利息预提情况表");
        map.put(NormalExcelConstants.CLASS, InterestFactSheetVo.class);
        map.put(NormalExcelConstants.PARAMS, new ExportParams("利息预提情况表", "", "利息预提情况表"));
        map.put(NormalExcelConstants.DATA_LIST, list);
        return NormalExcelConstants.EXCEL_VIEW;
    }
    @PostMapping("/accountlist")
    @ResponseBody
    public List accountList(String netId) {
        List<FcAccDTO> account;
        if (netId != null) {
            account = innerAccountService.listFcAccInFoByCorpIds(Arrays.asList(netId.split(",")), null);
        } else {
            account = new ArrayList<FcAccDTO>();
        }
        return account;
    }
    @PostMapping("/getCorpsByNetId")
    @ResponseBody
    public List<CorpDTO> getCorpsByNetId(String netId) {
        if (EmptyUtils.isEmpty(netId) || "null".equals(netId)) {
            return new ArrayList<CorpDTO>();
        }
        return corpService.listCorpByNetId(netId);
    }
}
