package com.bytter.report.web.cms.controller;

import com.bytter.report.domain.cms.service.RatBkAmountCalculateService;
import com.bytter.report.domain.cms.vo.RatBkCalculateSearchVO;
import com.bytter.report.domain.cms.vo.RatBkCalculateVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.credit.api.IRatBkrationService;
import com.bytter.core.constant.RationConstant;
import com.bytter.credit.dto.BtBankDTO;
import com.bytter.system.api.dto.BtRateTypeDTO;
import com.bytter.system.api.IBtRateTypeService;
import com.bytter.system.api.IMultiLangService;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author luxy
 * @Title: RatBkAmountCalculateController
 * @ProjectName T3.4
 * @Description: 授信额度测算控制层
 * @date 2020/7/28 10:31
 */
@Controller
@RequestMapping("/cms/report/ratBkAmountCalculate")
public class RatBkAmountCalculateController extends BaseController {
    @Autowired
    private RatBkAmountCalculateService calculateService;
    @DubboReference
    private IRatBkrationService bkrationService;
    @DubboReference
    private IBtRateTypeService rateTypeService;
    @Resource
    private IMultiLangService multiLangServiceI;

    /**
     * 银行授信额度统计查询
     * @return
     */
    //@RequiresPermissions(ResourceConstant.CMS_RATION_AMOUNTCALCULATE_QUERY)
    @LogMessage(content = "授信额度测算报表查询",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/queryBkAmountCalData")
    public String queryBkAmountCalculate(RatBkCalculateSearchVO searchVO, Model model){
        //显示分配的银行信息
        List<BtBankDTO> bankDTOs = bkrationService.queryRationBank();
        if(null!=searchVO.getSearchFlag()&&searchVO.getSearchFlag().equals(RationConstant.RAT_COMMON_Y)){
            List<RatBkCalculateVO> ratBean = calculateService.getRatCalList(searchVO,getCurrentUser());
            model.addAttribute("ratBean", ratBean);
        }else{
            searchVO.setCalEndDate(DateUtils.format(DateUtils.getDate(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
        }

        model.addAttribute("searchVO",searchVO);
        model.addAttribute("bankDTOs", bankDTOs);
        model.addAttribute("curDate",DateUtils.getDate());
        return "cms/report/ratBankRation/listRatAmountCal";
    }

    @LogMessage(content = "导出银行授信额度统计表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/excel")
    public String excel(RatBkCalculateSearchVO searchVO, HttpServletResponse response) {
        List<RatBkCalculateVO> list = calculateService.getRatCalList(searchVO,getCurrentUser());
        if (null != list && list.size() > 0) {
            String filename = multiLangServiceI.getLang("table.rat.prediction");//报表名称：授信额度测算.xls
            // 声明一个工作薄
            HSSFWorkbook wb = new HSSFWorkbook();
            //声明一个单子并命名
            HSSFSheet sheet = wb.createSheet(filename);
            sheet.setColumnWidth(0, 50 * 256);//设置第一列列宽
            sheet.setColumnWidth(1, 20 * 256);//设置第一列列宽
            sheet.setColumnWidth(3, 25 * 256);//设置第四列列宽
            sheet.setColumnWidth(4, 25 * 256);//设置第五列列宽
            for (int i = 7; i < 12; i++) {
                sheet.setColumnWidth(i, 20 * 256);//设置第7-11列列宽
            }
            //创建表格样式
            calculateService.creatExcel(wb, sheet);
            //填充数据
            wb = calculateService.addDataToSheet(list, sheet, wb);
            try {
                //实现Excel以文件流的形式到浏览器中,浏览器左下角出现Excel的下载提示，
                OutputStream out = response.getOutputStream();
                response.reset();
                response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes(), "iso-8859-1") + ".xls");
                response.setContentType("application/x-download");
                wb.write(out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 检查币别汇率是否存在
     *
     * @return
     */
    @PostMapping("/checkExchangeRate")
    @ResponseBody
    public AjaxJson checkExchangeRate(String curIds, String convertCurId, String exRateType) {
        calculateService.checkExchangeRate(curIds,convertCurId,exRateType);
        return success();
    }

    /**
     * 获取有效的汇率类型
     * @return
     */
    @GetMapping("/getExchangeRate")
    @ResponseBody
    public List<BtRateTypeDTO> getAllRateType() {
        return rateTypeService.getAllRateType();
    }


}
