package com.bytter.biz.finacial.report.controller;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.report.service.SubjectAccBalanceQueryService;
import com.bytter.biz.finacial.report.vo.BalanceQueryVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.ExcelExportUtil;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.entity.vo.MapExcelConstants;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author zhengxn
 * @version 1.0
 * @Title:科目账户余额表
 * @Description: FC_ACC_BALANCE
 * @date 2020-4-23 14:27:04
 */
@Controller
@RequestMapping("finacial/report/subjectaccbalancequery")
public class SubjectAccBalanceQueryController extends BaseController {
    @Autowired
    private SubjectAccBalanceQueryService subjectAccBalanceQueryService;
    @DubboReference(check = false)
	private ICorpService corpService;
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_SUBJECTACCBALANCE_QUERY)
    @GetMapping("/getList")
    public String list(Model model, BalanceQueryVo search) {
        List<BalanceQueryVo> map = new ArrayList<>();
        if (search.getLedCloseDate() != null && search.getGedCloseDate() != null) {
            map = subjectAccBalanceQueryService.querySubjectAccResult(search, FinacialResourceConstant.FINACIAL_SUBJECTACCBALANCE_QUERY, getUserId());
        }
        CorpDTO net = null;
        if (!EmptyUtils.isEmpty(StringUtils.trimToEmpty(search.getNetId()))) {
            net = subjectAccBalanceQueryService.getNetId(search.getNetId());
            model.addAttribute("tableHead", net.getName());
        }
        if (null == search.getGedCloseDate() && null == search.getLedCloseDate()) {
            search.setGedCloseDate(new Date());
            search.setLedCloseDate(new Date());
        }
        model.addAttribute("search", search);
        model.addAttribute("balBeans", map);
        String netName = null;
        if (!CollectionUtils.isEmpty(map)) {
            netName = map.get(0).getCorpName();
        }
        if (net != null) {
            netName = net.getName();
        }
        if (netName == null) {
            CorpDTO corpDTO = corpService.getNetByCorpId(getCurrentUser().getCorpId());
            if (corpDTO != null) {
                netName = corpDTO.getName();
            }
        }
        model.addAttribute("netName", netName);
        model.addAttribute("sumType", search.getSumType());
        return "finacial/report/subjectAccbalanceQuery";
    }
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_SUBJECTACCBALANCE_QUERY)
    @PostMapping("/queryListAccount")
    @ResponseBody
    public List<FcAccDTO> queryListAccount(String netId, String corpId) {
        if ("undefined".equals(corpId)) {
            corpId = null;
        }
        return subjectAccBalanceQueryService.queryListAccount(netId, corpId);
    }
    /**
     * 导出
     **/
    @GetMapping("/excel")
    public void excel(ModelMap map, BalanceQueryVo vo,HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> res = subjectAccBalanceQueryService.excel(vo, FinacialResourceConstant.FINACIAL_SUBJECTACCBALANCE_QUERY, getUserId());
        List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
        if (!"5".equals(vo.getSumType()) && !"6".equals(vo.getSumType())) {
            if ("1".equals(vo.getSumType())) {
                entity.add(new ExcelExportEntity("单位名称", "corpName"));
            } else {
                entity.add(new ExcelExportEntity("科目", "subjectName"));
            }
            if ("1".equals(vo.getSumType())) {
                entity.add(new ExcelExportEntity("科目", "subjectName"));
            } else if ("2".equals(vo.getSumType())) {
                entity.add(new ExcelExportEntity("单位名称", "corpName"));
            } else {
                entity.add(new ExcelExportEntity("账号", "blnctrAcc"));
            }
            if ("4".equals(vo.getSumType())) {
                entity.add(new ExcelExportEntity("账号名称", "accName"));
            }
            entity.add(new ExcelExportEntity("币别", "curName"));
            entity.add(new ExcelExportEntity("借贷方向", "balanceName"));
            ExcelExportEntity yester = new ExcelExportEntity("期初余额", "yesterBalance");
            yester.setNumFormat("#,##0.00");
            entity.add(yester);
            ExcelExportEntity debitMoney = new ExcelExportEntity("借方发生额", "debitMoney");
            debitMoney.setNumFormat("#,##0.00");
            entity.add(debitMoney);
            ExcelExportEntity creditMoney = new ExcelExportEntity("贷方发生额", "creditMoney");
            creditMoney.setNumFormat("#,##0.00");
            entity.add(creditMoney);
            ExcelExportEntity balance = new ExcelExportEntity("期末余额", "balance");
            balance.setNumFormat("#,##0.00");
            entity.add(balance);
        }
        if ("5".equals(vo.getSumType()) || "6".equals(vo.getSumType())) {
            entity.add(new ExcelExportEntity("科目", "subjectName"));
            entity.add(new ExcelExportEntity("币别", "curName"));
            entity.add(new ExcelExportEntity("借贷方向", "balanceName"));
            ExcelExportEntity yester = new ExcelExportEntity("期初余额", "yesterBalance");
            yester.setNumFormat("#,##0.00");
            entity.add(yester);
            ExcelExportEntity debitMoney = new ExcelExportEntity("借方发生额", "debitMoney");
            debitMoney.setNumFormat("#,##0.00");
            entity.add(debitMoney);
            ExcelExportEntity creditMoney = new ExcelExportEntity("贷方发生额", "creditMoney");
            creditMoney.setNumFormat("#,##0.00");
            entity.add(creditMoney);
            ExcelExportEntity balance = new ExcelExportEntity("期末余额", "balance");
            balance.setNumFormat("#,##0.00");
            entity.add(balance);
        }
        map.put(MapExcelConstants.FILE_NAME, "科目账户余额表");
        map.put(MapExcelConstants.CLASS, BalanceQueryVo.class);
        map.put(MapExcelConstants.PARAMS, new ExportParams("科目账户余额表", "导出人:" + getCurrentUser().getUsername(), "科目账户余额表"));
        map.put(MapExcelConstants.ENTITY_LIST, entity);
        if (!EmptyUtils.isEmptyList(res)) {
            map.put(MapExcelConstants.MAP_LIST, res);
        } else {
            map.put(MapExcelConstants.MAP_LIST, new ArrayList<Map<String, Object>>());
        }
        renderMergedOutputModel(map,request,response);
    }

    public void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codedFileName = "临时文件";
        Workbook workbook = ExcelExportUtil.exportExcel((ExportParams) model.get("params"), (List) model.get("data"), (Collection) model.get("mapList"));
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
    }

    protected boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }


//    @Autowired
//	private FcAccbookDao fcAccbookDao;
//    
//    @GetMapping("/testList")
//    @ResponseBody
//    public String testList() {
//    	FcAccbook ddd = fcAccbookDao.get("77f113adf37345dda338e7836489a8f3");
//    	return ddd.getAttachmentId()+"|"+ddd.getBusRaceName()+"|"+ddd.getEmergencySign()+"|"+ddd.getVoucherName()+"|"+ddd.getRmk();
//    }
}
