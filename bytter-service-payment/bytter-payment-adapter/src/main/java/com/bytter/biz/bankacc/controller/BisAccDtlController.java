package com.bytter.biz.bankacc.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.vo.BisAccDetailVO;
import com.bytter.biz.inneraccount.service.BtBankAccDtlService;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlExportDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.receipt.service.BisBankReceiptImportService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.enmus.ExcelType;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.entity.vo.MapExcelConstants;
import com.bytter.poi.excel.export.ExcelExportServer;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号明细表
 * @date 2017-07-05 11:20:30
 */
@Controller
@RequestMapping("/bankacc/bisaccdtl")
public class BisAccDtlController extends BaseController {

    @Autowired
    private BtBankAccDtlService btBankAccDtlService;

    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @Resource
    private IMultiLangService multiLangServiceI;

    @Autowired
    private BisBankReceiptImportService receiptImportService;


    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_RECEIPT_OP)
    @LogMessage(content = "流水回单上传", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping(params = "exportReceipt")
    @ResponseBody
    public AjaxJson exportReceipt(String attachmentId, String dtlId) {
        receiptImportService.dtlExportReceipt(attachmentId,dtlId,getCurrentUser());
        return success();
    }

    /**
     * 上传附件
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_RECEIPT_OP)
    @LogMessage(content = "上传附件", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/uploadReceipt/{id}")
    public String uploadReceipt(@PathVariable("id") String id, Model model) {
//        BisAccDtl bisAccDtl = bisAccDtlService.get(id);
//        model.addAttribute("id", id);
//        model.addAttribute("attachmentId", bisAccDtl.getAttachmentId());
        String[] ids = StringUtils.split(id, ",");
        BisAccDtlVO bisAccDtlVo = new BisAccDtlVO();
        BisAccDtlDTO byUnion = bisAccDtlService.getByUnion(ids[0]);
        BeanUtils.copyProperties(byUnion,bisAccDtlVo);
        model.addAttribute("bisAccDtlPage", bisAccDtlVo);
        if (ids.length > 1) {
            String rulesName = null;
            if (bisAccDtlService.getRules(ids[0]) != null) {
                rulesName = bisAccDtlService.getRules(ids[0]).getRulesName();
            }
            if (rulesName == null) {
                rulesName = "shoudong";
            }
            model.addAttribute("rules", rulesName);
        }
        return "bankacc/uploadReceipt";
    }

    /**
     * 银行账号明细表列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY)
    @LogMessage(content = "查询银行账号明细表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "bankacc/listBisAccDtl";
    }
    /**
     * 银行账号明细表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(value = {
//            ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY},
//            logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesUnion = bisAccDtlService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
        dataTablesUnion = bisAccDtlService.setDtlReceipt(dataTablesUnion);
        return dataTablesUnion;
    }
    /**
     * 银行账号明细表详情页
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY)
    @LogMessage(content = "查询银行账号明细表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        String[] ids = StringUtils.split(id, ",");
        BisAccDtlDTO bisAccDtlVo = bisAccDtlService.getByUnion(ids[0]);

        BtCurrencyDTO currencyDTO = iBtCurrencyService.getObjectById(bisAccDtlVo.getCurId());
        if(!EmptyUtils.isEmpty(currencyDTO)){
            bisAccDtlVo.setCurName(currencyDTO.getCurName());
        }

        BisBifInitDTO bisBifInitDTO= bisBifInitService.getObjectById(bisAccDtlVo.getBifCode());
        if(!EmptyUtils.isEmpty(bisBifInitDTO)){
            bisAccDtlVo.setBifName(bisBifInitDTO.getName());
        }

        //前面的bisAccDtlService.getByUnion()方法已经设置了CorpName
//        BtBankAccDTO bankAccInfo = bankAccService.getBankAccInfo(bisAccDtlVo.getBankAcc());
//        Map<String, String> nameMap = corpService.getNameByIds(Lists.newArrayList(bankAccInfo.getCorpId()));
//        bisAccDtlVo.setCorpName(nameMap.get(bankAccInfo.getCorpId()));

        model.addAttribute("bisAccDtlPage", bisAccDtlVo);
        /*if (ids.length > 1) {
            String rulesName = null;
            if (bisAccDtlService.getRules(ids[0]) != null) {
                rulesName = bisAccDtlService.getRules(ids[0]).getRulesName();
            }
            if (rulesName == null) {
                rulesName = "shoudong";
            }
            model.addAttribute("rules", rulesName);
        }*/
        return "bankacc/viewBisAccDtl";
    }
    /**
     * 银行流水补录页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTL_MAKEUPQUERY)
    @LogMessage(content = "银行流水补录页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listMakeup")
    public String listMakeup(Model model) {
        return "bankacc/listBisAccDtlMakeup";
    }
    /**
     * 查询银行流水可补录数据
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTL_MAKEUPQUERY)
    @PostMapping("/dataTableMakeup")
    @ResponseBody
    public DataTablesOutput dataTableMakeup(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput output = bisAccDtlService.getDataTablesMakeup(dataTablesInput, this.getCurrentUser());
        List<BisAccDtlVO> voList = output.getData();
        if (voList.size() > 0) {
            Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
            for (BisAccDtlVO vo : voList) {
                vo.setCurName(curMap.get(vo.getCurId()).getCurName());
            }
        }
        return output;
    }
    /**
     * 银行账号明细补录详情页
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTL_MAKEUPAPPLY)
    @GetMapping("/makeup")
    public String makeup(BisAccDtlDTO bisAccDtl, Model model) {
        if (StringUtils.isNotEmpty(bisAccDtl.getId())) {
            BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getByUnion(bisAccDtl.getId());

            BtCurrencyDTO currencyDTO = iBtCurrencyService.getObjectById(bisAccDtlDTO.getCurId());
            bisAccDtlDTO.setCurName(currencyDTO.getCurName());

            BisBifInitDTO bisBifInitDTO= bisBifInitService.getObjectById(bisAccDtlDTO.getBifCode());
            bisAccDtlDTO.setBifName(bisBifInitDTO.getName());

            model.addAttribute("btBankAccDtlPage", bisAccDtlDTO);
        }
        return "bankacc/updateBisAccDtlMakeup";
    }
    /**
     * 银行账号明细补录
     *
     * @param bisAccDtl
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTL_MAKEUPAPPLY)
    @LogMessage(content = "银行账号明细补录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modifyMakeup")
    @ResponseBody
    public AjaxJson modifyMakeup(BisAccDtlDTO bisAccDtl) {
        bisAccDtlService.saveOrUpdate(bisAccDtl); //bis_acc_dtl(银行账号余额表 )
        btBankAccDtlService.modifyMakeup(bisAccDtl); //BT_BANK_ACC_DTL(银行账号明细录入表)
        return success();
    }
    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                dataTablesInput.addColumnOrder("id", false);
                // 统计列名
                dataTablesInput.setTotalColumn("attributeName");
                // 统计数据列名
                dataTablesInput.addTotalList("amtIn","amtOut");
                List<BisAccDtlExportDTO> list = bisAccDtlService.listExport(dataTablesInput, user);
                // 统计收入金额
                BigDecimal incomeSum = list.stream().filter(item -> ObjectUtil.equal(item.getCdSign(), "0"))
                        .map(BisAccDtlExportDTO::getAmtIn)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                // 统计支出金额
                BigDecimal outAmtSum = list.stream().filter(item -> ObjectUtil.equal(item.getCdSign(), "1"))
                        .map(BisAccDtlExportDTO::getAmtOut)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BisAccDtlExportDTO bisAccDtl = new BisAccDtlExportDTO();
                bisAccDtl.setAttributeName("收入合计");
                bisAccDtl.setAmtIn(incomeSum);
                bisAccDtl.setAmtOut(BigDecimal.ZERO);
                list.add(bisAccDtl);
                bisAccDtl = new BisAccDtlExportDTO();
                bisAccDtl.setAttributeName("支出合计");
                bisAccDtl.setAmtOut(outAmtSum);
                bisAccDtl.setAmtIn(BigDecimal.ZERO);
                list.add(bisAccDtl);
                return list;
            }
        }, totalParam, fileNameParam,"银行账户流水查询", 5000, BisAccDtlExportDTO.class);
    }
    /**
     * 银行流水补录 导出全部
     *
     * @return
     */
    @RequestMapping("/makeupExportAll")
    public void makeupExportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput dataTablesOutput = bisAccDtlService.getDataTablesMakeup(dataTablesInput, user);
                List<BisAccDtlVO> list = dataTablesOutput.getData();
                BigDecimal sumAmt = BigDecimal.ZERO;
                BigDecimal sumBal = BigDecimal.ZERO;
                for(BisAccDtlVO vo:list){
                    if(!EmptyUtils.isEmpty(vo.getAmt())) {
                        sumAmt = sumAmt.add(vo.getAmt());
                    }
                    if(!EmptyUtils.isEmpty(vo.getBal())) {
                        sumBal = sumBal.add(vo.getBal());
                    }
                }
                BisAccDtlVO bisAccDtl = new BisAccDtlVO();
                bisAccDtl.setAccName("汇总");
                bisAccDtl.setAmt(sumAmt);
                bisAccDtl.setBal(sumBal);
                list.add(bisAccDtl);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "银行流水补录", 5000, BisAccDtlVO.class);
    }

    /**
     *
     * @param dataTablesInput 查询入参
     * @param modelMap
     * @param request
     * @param response
     * @return 返回列表数据信息
     */
    @PostMapping("/exportSelected")
    public void exportPayExcelByMap(String params, String ids, DataTablesInput dataTablesInput, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 转换前台传来的数据为JSON
        params= URLDecoder.decode(params,"utf-8").replaceAll("&quot;","\"");
        // 用于排除当出现多余的列
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        dataTablesInput = mapper.readValue(params, DataTablesInput.class);
        // 配置导出要素
        List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
        if(StringUtils.isNotBlank(ids)){
            dataTablesInput.getFormdata().put("ins_id",URLDecoder.decode(ids,"utf-8").replaceAll("&quot;","\""));
        }
        if(StringUtils.isBlank(dataTablesInput.getTitle())){
            dataTablesInput.setTitle("银行账户流水查询");
        }
        if(StringUtils.isBlank(dataTablesInput.getFilename())){
            dataTablesInput.setFilename("银行账户流水查询");
        }
        if(modelMap.get("dataTablesInput") == null){
            modelMap.put("dataTablesInput",dataTablesInput);
        }
        if(ListUtils.isNotNullOrEmpty(dataTablesInput.getOrder())
                &&ListUtils.isNotNullOrEmpty(dataTablesInput.getColumns())
                &&!"id".equals(dataTablesInput.getColumns().get(0).getName())){
            for(Order order:dataTablesInput.getOrder()){
                order.setColumn(order.getColumn()-1);
            }
        }
        DataTablesOutput dataTablesUnion = bisAccDtlService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());

        // 添加导出要素
        this.setDataColumn(dataTablesInput,entityList, modelMap, dataTablesUnion.getData());
        // 导出
        this.excelExport(dataTablesInput,modelMap,request,response);
    }

    public void setDataColumn(DataTablesInput dataTablesInput, List<ExcelExportEntity> entityList, ModelMap modelMap,
                              List list) {
        List<Column> columns = dataTablesInput.getColumns();
        List<Map<String,Object>> dataResult = new ArrayList<Map<String,Object>>();
        if(columns != null && columns.size()>0){
            // 配置导出数据和列头
            this.setColumnTitleAndData(columns, entityList, list,dataResult);
            modelMap.put(MapExcelConstants.PARAMS,new ExportParams(dataTablesInput.getTitle(),dataTablesInput.getTitle(), ExcelType.XSSF));
            modelMap.put(MapExcelConstants.FILE_NAME,dataTablesInput.getFilename());
            modelMap.put(MapExcelConstants.MAP_LIST,dataResult);
            modelMap.put(MapExcelConstants.ENTITY_LIST,entityList);
        }
    }

    public void setColumnTitleAndData(List<Column> columns, List entityList, List list,
                                      List<Map<String,Object>> dataResult) {
        // 将数据对象转成MAP存进集合
        for(Object bisAccDetailVO:list){
            Map<String, Object> dataMap = BeanUtils.beanToMap(bisAccDetailVO);
            if(bisAccDetailVO instanceof BisAccDetailVO){
                BisAccDetailVO info=(BisAccDetailVO)bisAccDetailVO;
                if(info.getTransTime()!=null){
                    dataMap.put("transTime", DateUtils.formatDate(info.getTransTime()));
                }
//                if(info.getEndDate()!=null){
//                    dataMap.put("endDate",DateUtils.formatDate(info.getEndDate()));
//                }
//                if(info.getEmitOpenDate()!=null){
//                    dataMap.put("emitOpenDate",DateUtils.formatDate(info.getEmitOpenDate()));
//                }
//                if(info.getPayDate()!=null){
//                    dataMap.put("payDate",DateUtils.formatDate(info.getPayDate()));
//                }
//                if(info.getReceDate()!=null){
//                    dataMap.put("receDate",DateUtils.formatDate(info.getReceDate()));
//                }
//                if(info.getBailPercent()!=null){
//                    if( info.getBailPercent().compareTo(BigDecimal.ZERO)!=0) {
//                        dataMap.put("bailPercent", NumberUtil.formatNumeric(info.getBailPercent().multiply(new BigDecimal("100")), 2) + "%");
//                    }else{
//                        dataMap.put("bailPercent","0");
//                    }
//                }
            }
            dataResult.add(dataMap);
        }
        // 遍历列头并将需要总计的列存进map
        Map<String, Object> map = new HashMap<String, Object>();
        for(Column column : columns){
            entityList.add(new ExcelExportEntity(column.getTitle(),column.getData()));
            if(column.getIsPagesum()){
                BigDecimal moneySum = BigDecimal.ZERO;
                map.put(column.getData(), moneySum);
            }
        }
        // 有总计数据时添加总计列
        if(map.size() > 0){
            // 计算导出数据中需要总计的数据和
            for(Map.Entry<String,Object> sumMoney: map.entrySet()){
                for(Map<String,Object> object:dataResult){
                    if(object.get(sumMoney.getKey()) != null){
                        BigDecimal money = BigDecimal.valueOf(NumberUtil.formatNumeric(object.get(sumMoney.getKey()).toString()));
                        sumMoney.setValue(money.add(NumberUtil.objToBigDecimal(sumMoney.getValue())));
                    }
                }
            }
            // 数据格式化
            for(Map.Entry<String,Object> sumMoney: map.entrySet()){
                BigDecimal money = (BigDecimal) sumMoney.getValue();
                sumMoney.setValue(NumberUtil.formatNumeric(money));
            }
            map.put(columns.get(0).getData(),multiLangServiceI.getLang("table.datatable.subtotal"));
            dataResult.add(map);
        }
    }

    public void excelExport(DataTablesInput dataTablesInput, ModelMap modelMap, HttpServletRequest request,
                            HttpServletResponse response)  throws IOException {
        String codedFileName = "临时文件";
        ExportParams entity = (ExportParams)modelMap.get("params");
        List entityList = (List)modelMap.get("data");
        Collection dataSet = (Collection)modelMap.get("mapList");
        try(
                // 生成工作簿
                Workbook workbook = new XSSFWorkbook();
                ServletOutputStream out = response.getOutputStream();
        ) {

            (new ExcelExportServer()).createSheetForMap(workbook, entity, entityList, dataSet);
            // 设置样式
            this.setStyle(dataTablesInput, workbook);
            // 设置文件名
            if(modelMap.containsKey("fileName")) {
                codedFileName = (String)modelMap.get("fileName");
            }
            codedFileName = codedFileName + ".xlsx";
//		if(request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0
//				|| request.getHeader("USER-AGENT").toLowerCase().indexOf("edge") > 0
//				|| request.getHeader("USER-AGENT")
//				.toLowerCase().indexOf("rv:11.0") > 0) {
            codedFileName = URLEncoder.encode(codedFileName, "UTF-8");
//		} else {
//			codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
//		}
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
            workbook.write(out);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("导出失败");
        }
    }

    public void setStyle(DataTablesInput dataTablesInput, Workbook workbook) {
        // 配置样式
        Sheet sheet = workbook.getSheet(dataTablesInput.getTitle());
        List<Column> columns = dataTablesInput.getColumns();
        // 表头样式
        Font font = workbook.createFont();
        CellStyle cs=workbook.createCellStyle();
        Row head = sheet.getRow(1);
        head.setHeightInPoints(16);
        font.setFontName("宋体"); //字体
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        // 设置样式
        cs.setFont(font);
        cs.setAlignment(HorizontalAlignment.CENTER);
        // 表体字体
        Font bodyFont = workbook.createFont();
        bodyFont.setFontName("宋体"); //字体
        bodyFont.setFontHeightInPoints((short) 11);
        for (int i = 0; i < columns.size(); i++) {
            // 表头样式
            Cell cell = head.getCell(i);
            cell.setCellStyle(cs);
            // 列头宽度
            int columnWidth = cell.getStringCellValue().getBytes().length;
            CellStyle cellStyle=workbook.createCellStyle();
            // 表体样式
            for(int r=2; r<sheet.getLastRowNum()+1; r++){
                Row row = sheet.getRow(r);
                row.setHeightInPoints(16);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //垂直居中对齐
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
                // 靠右数据
                if("right".equals(columns.get(i).getAlign())){
                    cellStyle.setAlignment(HorizontalAlignment.RIGHT);        //水平居右对齐
                }
                // 单元格样式
                cellStyle.setFont(bodyFont);
                Cell ce = row.getCell(i);
                int length = ce.getStringCellValue().getBytes().length;
                // 取每列最长宽度
                if(length > columnWidth){
                    columnWidth = length;
                }
                String columnName = columns.get(i).getName();
                //设置票据金额列为数值可统计计算
                if(columnName.equals("amtIn") || columnName.equals("amtOut") || columnName.equals("bal")){
                    if(StringUtils.isNotEmpty(ce.toString())){
                        ce.setCellValue( Double.parseDouble(ce.toString().replace(",","")));
                    }
                    cellStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
                    cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                }
                ce.setCellStyle(cellStyle);
            }
            //列样式
            sheet.setColumnWidth(i, columnWidth * 256 + 500);
        }
    }
}
