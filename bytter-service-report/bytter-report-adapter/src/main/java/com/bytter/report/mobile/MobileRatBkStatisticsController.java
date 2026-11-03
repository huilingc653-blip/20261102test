package com.bytter.report.mobile;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.RationConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.poi.enums.ResourceTemplateEnum;
import com.bytter.poi.excel.ExcelExportUtil;
import com.bytter.poi.excel.entity.TemplateExportParams;
import com.bytter.poi.excel.entity.vo.TemplateExcelConstants;
import com.bytter.report.domain.cms.service.RatBkStatisticsService;
import com.bytter.report.domain.cms.vo.RatBkStatisticSearchVO;
import com.bytter.report.domain.cms.vo.RatBkStatisticVO;
import com.bytter.report.domain.cms.vo.RatUrationUseBeanVO;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 本类是直接 拷贝 RatBkStatisticsController ，只是增加了 /mobile 前缀
 */
@Controller
@RequestMapping("/mobile/cms/report/ratbkstatistics")
public class MobileRatBkStatisticsController extends BaseController {

    @Autowired
    private RatBkStatisticsService ratBkStatisticsService;

    @DubboReference
    private IRatUrationService ratUrationService;

    @DubboReference
    private IDictService dictService;

    @DubboReference
    private IBtCurrencyService currencyService;

    @DubboReference
    private IBankTypeService bankTypeService;

    /**
     * 银行授信额度统计查询
     * @return
     */
    //@RequiresPermissions(ResourceConstant.CMS_REPORT_AMOUNT_QUERY)
    @LogMessage(content = "银行授信额度统计查询",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @RequestMapping("/queryBkAmountRation")
    public String queryBkAmountRation(RatBkStatisticSearchVO searchVO, Model model){
        String optType=searchVO.getOptType();
        List<RatBkStatisticVO> list=null;
        searchVO.setRatType(RationConstant.RAT_TYPE_0);//授信品种为银行授信
        if(!EmptyUtils.isEmpty(optType)){//查询
            list = ratBkStatisticsService.getRatBkAmountStatisticsList(searchVO, getCurrentUser());
            model.addAttribute("corpId", searchVO.getCorpId());//单位
            model.addAttribute("bankTypeId", searchVO.getBankTypeId());//银行
            model.addAttribute("ratDate", DateUtils.date2Str(searchVO.getRatDate(),DateUtils.date_sdf()));//日期
            model.addAttribute("curCode", searchVO.getCurCode());//币别
            model.addAttribute("ratMode", searchVO.getRatMode());//授信模式

            model.addAttribute("beginDate", DateUtils.format(searchVO.getBeginDate(), "yyyy-MM-dd"));//授信开始时间
            model.addAttribute("beginDate2", DateUtils.format(searchVO.getBeginDate2(), "yyyy-MM-dd"));//授信开始时间
            model.addAttribute("endDate", DateUtils.format(searchVO.getEndDate(), "yyyy-MM-dd"));//授信结束时间
            model.addAttribute("endDate2", DateUtils.format(searchVO.getEndDate2(), "yyyy-MM-dd"));//授信结束时间
        }
        model.addAttribute("reportDate", DateUtils.date2Str(DateUtils.getDate(),DateUtils.date_sdf()));//报表日期
        model.addAttribute("userName", getCurrentUser().getUsername());//制表人
        model.addAttribute("moneyType", searchVO.getMoneyType());//金额单位
        model.addAttribute("list", list);
        return "cms/report/ratBankRation/listAmountStatics";
    }


    @RequestMapping("/mobileBkAmountRation")
    public String mobileBkAmountRation(RatBkStatisticSearchVO searchVO,Model model){
        searchVO.setRatType(RationConstant.RAT_TYPE_0);//授信品种为银行授信
        searchVO.setCorpId(null);
        if (null == searchVO.getRatDate()) {
            searchVO.setRatDate(new Date());
        }
        if (null == searchVO.getMoneyType()) {
            searchVO.setMoneyType("10000");
        }
        List<RatBkStatisticVO> list = ratBkStatisticsService.getMobileData(searchVO, getCurrentUser());
        model.addAttribute("curList", currencyService.getBtCurrency());
        model.addAttribute("list", list);
        model.addAttribute("bankList", bankTypeService.listByValidSign(null));
        model.addAttribute("ratModes", dictService.list("RatBkration.RatMode"));
//		model.addAttribute("now", DateUtils.format(new Date(), "yyyy-MM-dd"));
        return "cms/report/ratBankRation/MobileAmountStatics";
    }

    /**
     *app报表后台数据交互
     *@Param searchVO
     *@Param model
     *@return
     */
    @RequestMapping("/getMobileData")
    @ResponseBody
    public AjaxJson getmobileData(RatBkStatisticSearchVO searchVO, Model model){
        searchVO.setRatType(RationConstant.RAT_TYPE_0);//授信品种为银行授信
        if (null == searchVO.getMoneyType()) {
            searchVO.setMoneyType("10000");
        }
        List<RatBkStatisticVO> list = ratBkStatisticsService.getMobileData(searchVO, getCurrentUser());
        AjaxJson json = new AjaxJson();
        json.setObj(list);
        json.setSuccess(true);
        return json;
    }

    /**
     * 查看授信占用情况
     *
     * @return
     */
    @LogMessage(content = "页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewRatioUse")
    public String viewRatioUse(RatBkStatisticSearchVO searchVO, Model model) {
        List<RatUrationUseBeanVO> rations = ratBkStatisticsService.queryCorpRationUseDtl(searchVO);
        model.addAttribute("rations", rations);
        model.addAttribute("queryVO", searchVO);
        return "cms/report/ratBankRation/rationQueryDetail";
    }

    /**
     * 【授信额度综合统计--点击详情】
     * @param paramDTO
     * @param model
     * @return
     */
    @RequestMapping("/viewMobileItemRatio")
    public String viewMobileItemRatio(UrationParamDTO paramDTO, Model model){
        paramDTO.setMoneyType(1);
        List<com.bytter.credit.vo.RatUrationUseVO> rations = null;
        if (EmptyUtils.isEmpty(paramDTO.getBankCode())) {
            rations = new ArrayList<RatUrationUseVO>();
        } else {
            paramDTO.setStatus(RationConstant.STATUS_CHECK.toString());
            rations = ratUrationService.queryRationUseDtl(paramDTO);
        }
        if (!EmptyUtils.isEmpty(rations)){
            com.bytter.credit.vo.RatUrationUseDtlVO totalVo = new com.bytter.credit.vo.RatUrationUseDtlVO();
            totalVo.setMoney(new BigDecimal(0));
            totalVo.setUseMoney(new BigDecimal(0));
            totalVo.setApproveMoney(new BigDecimal(0));
            totalVo.setOverPlusMoney(new BigDecimal(0));
            totalVo.setLeftMoney(new BigDecimal(0));
            for (com.bytter.credit.vo.RatUrationUseDtlVO rationDltItem : rations.get(0).getRationDlts()){
                totalVo.setMoney(totalVo.getMoney().add(rationDltItem.getMoney()));
                totalVo.setUseMoney(totalVo.getUseMoney().add(rationDltItem.getUseMoney()));
                totalVo.setApproveMoney(totalVo.getApproveMoney().add(rationDltItem.getApproveMoney()));
                totalVo.setOverPlusMoney(totalVo.getOverPlusMoney().add(rationDltItem.getOverPlusMoney()));
                totalVo.setLeftMoney(totalVo.getLeftMoney().add(rationDltItem.getLeftMoney()));
            }
            rations.get(0).getRationDlts().add(totalVo);
        }
        model.addAttribute("rations", rations);
        return "cms/report/ratBankRation/MobileRationItemDtl";
    }

    @GetMapping("/viewItemRatio")
    public String viewItemRatio(UrationParamDTO paramDTO, Model model){
        paramDTO.setMoneyType(1);
        List<RatUrationUseVO> rations = null;
        if (EmptyUtils.isEmpty(paramDTO.getBankCode())) {
            rations = new ArrayList<RatUrationUseVO>();
        } else {
            paramDTO.setStatus(RationConstant.STATUS_CHECK.toString());
            List<RatUrationUseVO> listvo = ratUrationService.queryRationUseDtl(paramDTO);
            if(CollectionUtils.isEmpty(listvo)) {
                rations = new ArrayList<RatUrationUseVO>();
            }else {
                rations = listvo;
            }

        }
        if (!EmptyUtils.isEmpty(rations)){
            RatUrationUseDtlVO totalVo = new RatUrationUseDtlVO();
            totalVo.setMoney(new BigDecimal(0));
            totalVo.setUseMoney(new BigDecimal(0));
            totalVo.setApproveMoney(new BigDecimal(0));
            totalVo.setOverPlusMoney(new BigDecimal(0));
            totalVo.setLeftMoney(new BigDecimal(0));
            for (RatUrationUseDtlVO rationDltItem : rations.get(0).getRationDlts()){
                totalVo.setMoney(totalVo.getMoney().add(rationDltItem.getMoney()));
                totalVo.setUseMoney(totalVo.getUseMoney().add(rationDltItem.getUseMoney()));
                totalVo.setApproveMoney(totalVo.getApproveMoney().add(rationDltItem.getApproveMoney()));
                totalVo.setOverPlusMoney(totalVo.getOverPlusMoney().add(rationDltItem.getOverPlusMoney()));
                totalVo.setLeftMoney(totalVo.getLeftMoney().add(rationDltItem.getLeftMoney()));
            }
            rations.get(0).getRationDlts().add(totalVo);
        }
        model.addAttribute("rations", rations);
        return "cms/report/ratBankRation/rationItemDetail";
    }

    /**导出excel**/
    //@RequiresPermissions(ResourceConstant.CMS_REPORT_AMOUNT_QUERY)
    @LogMessage(content = "导出银行授信额度统计表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/excel")
    public String excel(ModelMap model, RatBkStatisticSearchVO searchVO, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<RatBkStatisticVO> list = null;
        searchVO.setRatType(RationConstant.RAT_TYPE_0);//授信品种为银行授信
        list = ratBkStatisticsService.getRatBkAmountStatisticsList(searchVO, getCurrentUser());

        Map<String,Object> listmap=new HashMap<>();
        RatBkStatisticVO sumVo=list.get(list.size()-1);//最后一条为合计信息，单独设置
        RatBkStatisticVO lastVo=new RatBkStatisticVO();
        lastVo.setCorpName(sumVo.getCorpName());
        lastVo.setMoney(sumVo.getMoney());
        lastVo.setUseMoney(sumVo.getUseMoney());
        lastVo.setApproveMoney(sumVo.getApproveMoney());
        lastVo.setOverPlusMoney(sumVo.getOverPlusMoney());
        lastVo.setLeftMoney(sumVo.getLeftMoney());

        list.remove(list.size()-1);//将原来的最后一条数据删除
        list.add(lastVo);//放入新的数据
        String curName = dictService.get("bisGatherPolicy.unit", searchVO.getMoneyType());
        listmap.put("list", list);
        listmap.put("title","授信额度综合统计表");
        listmap.put("time", DateUtils.date2Str(DateUtils.getDate(), DateUtils.date_sdf()));
        listmap.put("curType",curName);
        listmap.put("createUser",getCurrentUser().getUsername());
        TemplateExportParams temmp = new TemplateExportParams(ResourceTemplateEnum.EXPORT_CMS_STATISCS_EXP_XLSX.getPath());
        model.put(TemplateExcelConstants.FILE_NAME,"授信额度综合统计表");
        model.put(TemplateExcelConstants.PARAMS,temmp);
        model.put(TemplateExcelConstants.MAP_DATA,listmap);
        String codedFileName = "临时文件";
        Workbook workbook = ExcelExportUtil.exportExcel((TemplateExportParams)model.get("params"), (Class)model.get("entity"), (List)model.get("list"), (Map)model.get("map"));
        if(model.containsKey("fileName")) {
            codedFileName = (String)model.get("fileName");
        }
        if(workbook instanceof HSSFWorkbook) {
            codedFileName = codedFileName + ".xls";
        } else {
            codedFileName = codedFileName + ".xlsx";
        }
        if(this.isIE(request)) {
            codedFileName = URLEncoder.encode(codedFileName, "UTF8");
        } else {
            codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        return null;
    }

    protected boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
}
