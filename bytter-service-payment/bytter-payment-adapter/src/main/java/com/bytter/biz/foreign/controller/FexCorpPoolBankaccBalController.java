package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.service.FexCorpPoolBankaccBalService;
import com.bytter.biz.foreign.vo.FexCorpPoolBankAccVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IMultiLangService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: 资金池账户余额查询
 * @date 2020-02-25 09:31:56
 */
@Controller
@RequestMapping("/foreign/fexcorppoolbankaccbal")
public class FexCorpPoolBankaccBalController extends BaseController {

    @Autowired
    private FexCorpPoolBankaccBalService fexCorpPoolBankaccBalService;
    @DubboReference(check = false)
    private IMultiLangService iMultiLangService;
    /**
     * 资金池账户余额查询列表 页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = com.bytter.facade.foreign.constant.ResourceConstant.FOREIGN_FEXCORPPOOLBANKACCBAL_QUERY)
    @LogMessage(content = "资金池账户余额查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String list(FexCorpPoolBankAccVO fexCorpPoolBankAccVO, Model model) {
        setTreeForPool(fexCorpPoolBankAccVO, model);
        return "foreign/listFexCorpPoolBalQuery";
    }

    /**
     * 组装树结构 资金池
     *
     * @param fexCorpPoolBankAccVO
     * @param model
     */
    public void setTreeForPool(FexCorpPoolBankAccVO fexCorpPoolBankAccVO, Model model) {
        // 查询外汇资金池列表数据
        List<FexCorpPoolBankAccVO> treeList;
        try {
            treeList = fexCorpPoolBankaccBalService.setTreeForPool(fexCorpPoolBankAccVO);
        }catch (Exception e){
            treeList = new ArrayList<>();
        }
        model.addAttribute("treeList", treeList);
        model.addAttribute("treeListSize", treeList.size());
        if(!EmptyUtils.isEmptyList(treeList)){
            FexCorpPoolBankAccVO vo = treeList.get(0);
            //合计
            Map<String, BigDecimal> totalMap = fexCorpPoolBankaccBalService.toCurrMoneyTotal(vo.getSubCurList(),fexCorpPoolBankAccVO.getCurId(), fexCorpPoolBankAccVO.getRateType(), fexCorpPoolBankAccVO.getBalDate());
            model.addAttribute("total", totalMap);
            Map<String, String> curInfo = fexCorpPoolBankaccBalService.mergeCurInfo(vo.getSubCurList(),fexCorpPoolBankAccVO.getCurId(), fexCorpPoolBankAccVO.getRateType(), fexCorpPoolBankAccVO.getBalDate());
            model.addAttribute("curInfo", curInfo);
        }
        model.addAttribute("fexCorpPoolBankAccVOPage",fexCorpPoolBankAccVO);
    }

    /**
     * 导出
     * @param request
     * @param response
     * @param searchVO
     * @throws Exception
     */
    @LogMessage(content = "导出内部计价汇总数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportExcel")
    public void doExportInterestBill(HttpServletRequest request,
                                     HttpServletResponse response, FexCorpPoolBankAccVO searchVO) throws Exception{
        //String filename = iMultiLangService.getLang("downAllocationAnalysis.detailInformation") + ".xls";
        String filename = "资金池账户余额查询.xls";
        if(isIE(request)) {//判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        HSSFWorkbook workbook = fexCorpPoolBankaccBalService.createExcelContent(searchVO, ResourceConstant.NETBANK_REPORT_DOWNALLOCATIONANALYSIS_QUERY, getUserId());
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 判断是否是IE
     * @param request
     * @return
     */
    private boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
}
