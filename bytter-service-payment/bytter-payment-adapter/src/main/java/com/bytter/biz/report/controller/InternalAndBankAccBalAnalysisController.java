package com.bytter.biz.report.controller;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.report.service.InternalAndBankAccBalAnalysisService;
import com.bytter.biz.report.vo.InternalAndBankAccBalVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcExcrateAdjustService;
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
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author zhoup
 * @Description 内部银行账户余额分析情况表
 * @date 2018.6.8
 */
@Controller
@RequestMapping("/report/internalAndBankAccBal")
public class InternalAndBankAccBalAnalysisController extends BaseController {
    @Autowired
	private InternalAndBankAccBalAnalysisService internalAndBankAccBalAnalysisService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    /**
     * @param start 第几页
     * @param len   一页多少条
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY)
    @LogMessage(content = "银行账户及内部账户资金情况查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model, InternalAndBankAccBalVO vo, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        DataTablesOutput output = null;
        if (null == start) {
            start = Integer.valueOf(1);
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        if (!"isNotFirst".equals(vo.getIsNotFirst())) {
            vo.setBalDate(DateUtils.getDate());
            model.addAttribute("currentDate", DateUtils.getDate());
        }
        queryParam(model, vo, input);
        output = internalAndBankAccBalAnalysisService.getInternalAndBankAccBal(input, ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY, getCurrentUser());
        Map<String, Object> result = new HashMap<String, Object>();
        long resultCount = output.getRecordsTotal();
        int pageSize = input.getLength();//每页多少条
        int pageCount = 0;
        if (resultCount % pageSize == 0) {
            //整除则直接取整相除
            pageCount = (int) (resultCount / pageSize); //总页数
        } else {
            //否则取整相除后加一
            pageCount = (int) ((resultCount / pageSize) + 1); //页数
        }
        result.put("balParam", output.getData());
        result.put("total", output.getRecordsTotal());//总条数
        result.put("curr", start);//第几页
        result.put("len", input.getLength());// 每页条数
        model.addAttribute("model", result);
        if (StringUtils.isNotEmpty(vo.getBalDate())) {
            model.addAttribute("balDate", DateUtils.formatDate(vo.getBalDate()));
        }
        model.addAttribute("accountAcc", vo.getAccountAcc());
        model.addAttribute("bankAcc", vo.getBankAcc());
        //获取人民币，开户币种默认人民币
        model.addAttribute("rmbCur", btCurrencyService.getRMBCurrency());
        return "report/InternalAndBankAccBal";
    }
    //////@RequiresPermissions(ResourceConstant.NETBANK_REPORT_NISALLOCATIONREQ_QUERY)
    public DataTablesInput queryParam(Model model, InternalAndBankAccBalVO vo, DataTablesInput input) {
        if (StringUtils.isNotEmpty(vo.getNetId())) {
            input.addFilter(MatchTypeEnum.IN, "netId", vo.getNetId());
            model.addAttribute("netId", vo.getNetId());
        }
        if (StringUtils.isNotEmpty(vo.getCorpId())) {
            input.addFilter(MatchTypeEnum.IN, "corpId", vo.getCorpId());
            model.addAttribute("corpId", vo.getCorpId());
        }
        if (StringUtils.isNotEmpty(vo.getBalDate())) {
            input.addFilter(MatchTypeEnum.EQ, "balDate", DateUtils.formatDate(vo.getBalDate()), Date.class);
            model.addAttribute("balDate", DateUtils.date2Str(vo.getBalDate(), DateUtils.date_sdf()));
        }
        if (StringUtils.isNotEmpty(vo.getAccountAcc())) {
            input.addFilter(MatchTypeEnum.LIKE, "accountAcc", vo.getAccountAcc());
            model.addAttribute("accountAcc", vo.getAccountAcc());
        }
        if (StringUtils.isNotEmpty(vo.getBankAcc())) {
            input.addFilter(MatchTypeEnum.LIKE, "bankAcc", vo.getBankAcc());
            model.addAttribute("bankAcc", vo.getBankAcc());
        }
        if (StringUtils.isNotEmpty(vo.getCurId())) {
            input.addFilter(MatchTypeEnum.EQ, "curId", vo.getCurId());
            model.addAttribute("curId", vo.getCurId());
        }
        if (StringUtils.isNotEmpty(vo.getBankType())) {
            input.addFilter(MatchTypeEnum.EQ, "bankType", vo.getBankType());
            model.addAttribute("bankType", vo.getBankType());
        }
        if(StringUtils.isNotEmpty(vo.getChangeCur())){
            input.addFilter(MatchTypeEnum.EQ, "changeCur", vo.getChangeCur());
            model.addAttribute("changeCur",vo.getChangeCur());
        }else{
            model.addAttribute("changeCur", CommonConstant.NO);
        }
        if(StringUtils.isNotEmpty(vo.getRateType())){
            input.addFilter(MatchTypeEnum.EQ, "rateType", vo.getRateType());
            model.addAttribute("rateType",vo.getRateType());
        }
        if(StringUtils.isNotEmpty(vo.getReducedCurId())){
            input.addFilter(MatchTypeEnum.EQ, "reducedCurId", vo.getReducedCurId());
            model.addAttribute("reducedCurId",vo.getReducedCurId());
        }
        if(StringUtils.isNotEmpty(vo.getRateDate())){
            input.addFilter(MatchTypeEnum.LE, "rateDate", DateUtils.formatDate(DateUtils.getIntegralEndTime(vo.getRateDate()),"yyyy-MM-dd HH:mm:ss"), Date.class);
            model.addAttribute("rateDate", DateUtils.date2Str(vo.getRateDate(),DateUtils.date_sdf()));
        }else{
            model.addAttribute("rateDate", DateUtils.date2Str(DateUtils.getDateYMD(),DateUtils.date_sdf()));
        }
        if (StringUtils.isNotEmpty(vo.getBankName())) {
            input.addFilter(MatchTypeEnum.LIKE, "bankName", vo.getBankName());
            model.addAttribute("bankName", vo.getBankName());
        }
        return input;
    }
    /**
     * 导出Excel
     *
     * @param request  请求体
     * @param response 响应体
     * @param searchVO 查询VO
     * @param model    视图
     * @throws Exception 输出流异常
     * @author qinl
     * @since 2019/08/29
     */
    @PostMapping("/doExportInterestBill")
    public void doExportInterestBill(Integer start, Integer len, HttpServletRequest request,
                                     HttpServletResponse response, InternalAndBankAccBalVO searchVO, Model model) throws Exception {
        //查询条件设置
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        input.addFilter(MatchTypeEnum.EQ, "balDate", DateUtils.formatDate(), Date.class);
        queryParam(model, searchVO, input);
        //设置文件名
        String filename = "账户余额统计信息.xls";
        if (isIE(request)) {//判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        //导出数据
        HSSFWorkbook workbook = internalAndBankAccBalAnalysisService.doExportInterestBillXls(input, this.getCurrentUser());
        //设置导出流
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
    /**
     * 判断是否是IE
     *
     * @param request 请求体
     * @return 是否IE
     * @author qinl
     * @since 2019/08/29
     */
    private boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }
}
