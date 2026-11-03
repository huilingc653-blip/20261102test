package com.bytter.biz.saas.controller.bond;

import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondBackService;
import com.bytter.guarantee.vo.FcBondApplyVO;
import com.bytter.guarantee.vo.FcBondBackVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fusb
 * @version 1.0
 * @Title FcBondBackController
 * @Description 保证金转回controller
 * @date 2018年9月17日
 */
@Api(value = "/v1-payment/bond/fcbondback", tags = "保证金转回")
@RestController("v1FcBondBackController")
@RequestMapping("/v1-payment/bond/fcbondback")
public class FcBondBackController extends BaseController {
    @DubboReference(check = false)
    IFcBondBackService fcBondBackService;

    @Autowired
    NisAllocationReqService nisAllocationReqService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @DubboReference
    private IDictService dictService;

    /**
     * 查询保证金转回列表
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("查询保证金转回列表")
    @LogMessage(content = "查询保证金转回列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcBondBackVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        NisAllocationReqDTO NisAllocationReqDTO = new NisAllocationReqDTO();
        NisAllocationReqDTO.setBillFrom("3");
        List<NisAllocationReqDTO> list = nisAllocationReqService.getNisAllocationReqBillFrom(NisAllocationReqDTO);
        if (list.size() > 0) {
            StringBuilder erpIds = new StringBuilder();
            for (NisAllocationReqDTO nisAll : list) {
                erpIds.append(nisAll.getErpId()).append(",");
            }
            erpIds = new StringBuilder(erpIds.substring(0, erpIds.length() - 1));
            input.getFormdata().put("erpIds", erpIds.toString());
        }
        return AjaxJson.success(fcBondBackService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 根据保证金id查询账户项目信息
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("根据保证金id查询账户项目信息")
    @LogMessage(content = "查询账户项目信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable1")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcBondBackVO>> dataTable1(@RequestBody DataTablesInput dataTablesInput) {
        return AjaxJson.success(fcBondBackService.getDataTablesUnion(dataTablesInput, this.getCurrentUser()));
    }

    /**
     * 根据保证金id查询转回历史记录
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("根据保证金id查询转回历史记录")
    @LogMessage(content = "查询保证金转回历史记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable2")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcBondBackVO>> dataTable2(@RequestBody DataTablesInput dataTablesInput) {
        return AjaxJson.success(fcBondBackService.getDataTablesUnion(dataTablesInput, this.getCurrentUser()));
    }

    /**
     * 保证金转回操作页面
     */
    @ApiOperation("查询保证金转回列表")
    @LogMessage(content = "保证金转回操作页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/backdetail/{bondId}")
    public AjaxJson<Map<String, Object>> backDetail(@PathVariable("bondId") String bondId) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(bondId)) {
            // 得到保证金账户信息
            FcBondBackVO fcBondBackVO = fcBondBackService.getByIdFcBondBackVO(bondId);
            // 得到账户项目信息
            List<FcBondApplyVO> listFcBondApply = fcBondBackService.getByIdFcBondApply(bondId);
            // 得到保证金转回历史记录
            List<FcBondBackVO> listFcBondBack = fcBondBackService.getByIdFcBondBack(bondId);
            model.put("listFcBondBack", listFcBondBack);
            model.put("fcBondBackVO", fcBondBackVO);
            model.put("listFcBondApply", listFcBondApply);
        }
        return AjaxJson.success(model);
    }

    /**
     * 保证金转回
     *
     * @param fcBondBackVO
     * @return
     */
    @ApiOperation("保证金转回")
    @LogMessage(content = "保证金转回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/backmoney")
    @ResponseBody
    public AjaxJson backMoney(@RequestBody FcBondBackVO fcBondBackVO) {
        fcBondBackService.backFcBondMoney(fcBondBackVO, this.getCurrentUser());
        return success();
    }

    /**
     * 保证金转回信息查看
     *
     * @param bondId
     * @return
     */
    @ApiOperation("保证金转回信息查看")
    @LogMessage(content = "保证金转回查看页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewbondback/{bondId}")
    public AjaxJson<Map<String, Object>> view(@PathVariable("bondId") String bondId) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(bondId)) {
            // 得到保证金账户信息
            FcBondBackVO fcBondBackVO = fcBondBackService.getByIdFcBondBackVO(bondId);
            // 得到保证金转回历史记录
            List<FcBondBackVO> listFcBondBack = fcBondBackService.getByIdFcBondBack(bondId);
            model.put("fcBondBackVO", fcBondBackVO);

            model.put("listFcBondBack", listFcBondBack);
            model.put("bondId", bondId);
        }
        return AjaxJson.success(model);
    }

    @PostMapping("/fcBondApplyDatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcBondApplyVO>> fcBondApplyDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(fcBondBackService.fcBondApplyDatatable(input, this.getCurrentUser()));
    }


    @ApiOperation("保证金转回查看")
    @LogMessage(content = "保证金转回查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/excelBondback")
    public void excelBondApplyDtl(String bondId, HttpServletResponse response) {
        if (StringUtils.isNotEmpty(bondId)) {
            // 得到保证金账户信息
            FcBondBackVO fcBondBackVO = fcBondBackService.getByIdFcBondBackVO(bondId);
            // 得到账户项目信息
            List<FcBondApplyVO> listFcBondApply = fcBondBackService.getByIdFcBondApply(bondId);
            // 得到保证金转回历史记录
            List<FcBondBackVO> listFcBondBack = fcBondBackService.getByIdFcBondBack(bondId);
            String filename = "保证金转回查看.xls";//报表名称：银行授信使用统计表.xls
            // 声明一个工作薄
            HSSFWorkbook wb = new HSSFWorkbook();
            //声明一个单子并命名
            HSSFSheet sheet = wb.createSheet(filename);
            //给单子名称一个长度
            sheet.setDefaultColumnWidth((short) 18);
            // 生成一个样式
            HSSFCellStyle style = wb.createCellStyle();
            //设置表头字体
            HSSFFont headfont = wb.createFont();
            headfont.setFontName("宋体");
            headfont.setFontHeightInPoints((short) 14);// 字体大小
            headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            style.setFont(headfont);
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            this.setBorder(style);
            HSSFCellStyle centerCellStyle = getCenterCellStyle(wb);
            HSSFCellStyle rightCellStyle = getRightCellStyle(wb);
            //填充明细数据
            DecimalFormat df = new DecimalFormat("#,##0.00");
            DecimalFormat df1 = new DecimalFormat("##0.000000");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            //创建第一行
            HSSFRow row0 = sheet.createRow(0);
            HSSFCell cell00 = row0.createCell(0);
            cell00.setCellStyle(style);
            cell00.setCellValue("保证金账户信息");
            row0.setHeightInPoints(20);
            // 合并单元格
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            HSSFRow row1 = sheet.createRow(1);
            HSSFCell cell10 = row1.createCell(0);
            cell10.setCellStyle(centerCellStyle);
            cell10.setCellValue("单位名称");
            HSSFCell cell11 = row1.createCell(1);
            cell11.setCellStyle(centerCellStyle);
            cell11.setCellValue(fcBondBackVO.getCorpName());
            HSSFCell cell12 = row1.createCell(2);
            cell12.setCellStyle(centerCellStyle);
            cell12.setCellValue("存入账号");
            HSSFCell cell13 = row1.createCell(3);
            cell13.setCellStyle(centerCellStyle);
            cell13.setCellValue(fcBondBackVO.getBankAcc());

            HSSFRow row2 = sheet.createRow(2);
            HSSFCell cell20 = row2.createCell(0);
            cell20.setCellStyle(centerCellStyle);
            cell20.setCellValue("账户名称");
            HSSFCell cell21 = row2.createCell(1);
            cell21.setCellStyle(centerCellStyle);
            cell21.setCellValue(fcBondBackVO.getAccName());
            HSSFCell cell22 = row2.createCell(2);
            cell22.setCellStyle(centerCellStyle);
            cell22.setCellValue("银行名称");
            HSSFCell cell23 = row2.createCell(3);
            cell23.setCellStyle(centerCellStyle);
            cell23.setCellValue(fcBondBackVO.getBankName());

            HSSFRow row3 = sheet.createRow(3);
            HSSFCell cell30 = row3.createCell(0);
            cell30.setCellStyle(centerCellStyle);
            cell30.setCellValue("当前账户余额");
            HSSFCell cell31 = row3.createCell(1);
            cell31.setCellStyle(rightCellStyle);
            String balMoney = "";
            if (!EmptyUtils.isEmpty(fcBondBackVO.getBalMoney())) {
                balMoney = df.format(fcBondBackVO.getBalMoney());
            }
            cell31.setCellValue(balMoney);
            HSSFCell cell32 = row3.createCell(2);
            cell32.setCellStyle(centerCellStyle);
            cell32.setCellValue("占用金额");
            HSSFCell cell33 = row3.createCell(3);
            cell33.setCellStyle(rightCellStyle);
            String applyMoney = "";
            if (!EmptyUtils.isEmpty(fcBondBackVO.getApplyMoney())) {
                applyMoney = df.format(fcBondBackVO.getApplyMoney());
            }
            cell33.setCellValue(applyMoney);

            HSSFRow row4 = sheet.createRow(4);
            HSSFCell cell40 = row4.createCell(0);
            cell40.setCellStyle(centerCellStyle);
            cell40.setCellValue("可用金额");
            HSSFCell cell41 = row4.createCell(1);
            cell41.setCellStyle(rightCellStyle);
            String availMoney = "";
            if (!EmptyUtils.isEmpty(fcBondBackVO.getAvailMoney())) {
                availMoney = df.format(fcBondBackVO.getAvailMoney());
            }
            cell41.setCellValue(availMoney);
            HSSFCell cell42 = row4.createCell(2);
            cell42.setCellStyle(centerCellStyle);
            cell42.setCellValue("币别");
            HSSFCell cell43 = row4.createCell(3);
            cell43.setCellStyle(centerCellStyle);
            cell43.setCellValue(fcBondBackVO.getCurName());

            HSSFRow row5 = sheet.createRow(5);
            HSSFCell cell50 = row5.createCell(0);
            cell50.setCellStyle(centerCellStyle);
            cell50.setCellValue("保证金类型");
            HSSFCell cell51 = row5.createCell(1);
            cell51.setCellStyle(centerCellStyle);
            cell51.setCellValue(fcBondBackVO.getBondTypeName());
            HSSFCell cell52 = row5.createCell(2);
            cell52.setCellStyle(centerCellStyle);
            cell52.setCellValue("存入日期");
            HSSFCell cell53 = row5.createCell(3);
            cell53.setCellStyle(centerCellStyle);
            String startDate = "";
            if (!EmptyUtils.isEmpty(fcBondBackVO.getStartDate())) {
                startDate = simpleDateFormat.format(fcBondBackVO.getStartDate());
            }
            cell53.setCellValue(startDate);

            HSSFRow row6 = sheet.createRow(6);
            HSSFCell cell60 = row6.createCell(0);
            cell60.setCellStyle(centerCellStyle);
            cell60.setCellValue("是否存入定期");
            HSSFCell cell61 = row6.createCell(1);
            cell61.setCellStyle(centerCellStyle);
            String isRegular = "";
            if (!EmptyUtils.isEmpty(fcBondBackVO.getIsRegular())) {
                isRegular = dictService.get("sf_yn", fcBondBackVO.getIsRegular());
            }
            cell61.setCellValue(isRegular);
            HSSFCell cell62 = row6.createCell(2);
            cell62.setCellStyle(centerCellStyle);
            cell62.setCellValue("利率");
            HSSFCell cell63 = row6.createCell(3);
            cell63.setCellStyle(rightCellStyle);
            String interestRate = "";
            if (!EmptyUtils.isEmpty(fcBondBackVO.getInterestRate())) {
                interestRate = df1.format(fcBondBackVO.getInterestRate());
            }
            cell63.setCellValue(interestRate);

            HSSFRow row7 = sheet.createRow(7);
            HSSFCell cell70 = row7.createCell(0);
            cell70.setCellStyle(centerCellStyle);
            cell70.setCellValue("转回保证金");
            HSSFCell cell71 = row7.createCell(1);
            cell71.setCellStyle(rightCellStyle);
            String backMoney = "";
            if (!EmptyUtils.isEmpty(fcBondBackVO.getBackMoney())) {
                backMoney = df.format(fcBondBackVO.getBackMoney());
            }
            cell71.setCellValue(backMoney);
            HSSFCell cell72 = row7.createCell(2);
            cell72.setCellStyle(centerCellStyle);
            cell72.setCellValue("利息收入");
            HSSFCell cell73 = row7.createCell(3);
            cell73.setCellStyle(rightCellStyle);
            String backInterest = "";
            if (!EmptyUtils.isEmpty(fcBondBackVO.getBackInterest())) {
                backInterest = df.format(fcBondBackVO.getBackInterest());
            }
            cell73.setCellValue(backInterest);

            //列表
            HSSFRow row8 = sheet.createRow(8);
            HSSFCell cell80 = row8.createCell(0);
            cell80.setCellStyle(style);
            cell80.setCellValue("账户项目信息");
            row8.setHeightInPoints(20);
            // 合并单元格
            sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 5));


            HSSFRow row9 = sheet.createRow(9);
            //给表头第一行一次创建单元格
            String[] heads = new String[]{
                    "序号", "项目单号", "项目类型", "项目开始日期", "项目到期日期", "项目占用金额"
            };
            row9.setHeightInPoints(20);
            for (int i = 0; i < heads.length; i++) {
                HSSFCell cell = row9.createCell((short) i);
                cell.setCellValue(heads[i]);
                cell.setCellStyle(style);
                sheet.setColumnWidth(i, 256 * 18);
            }

            for (int i = 0; i < listFcBondApply.size(); i++) {
                FcBondApplyVO fcBondApplyVO = listFcBondApply.get(i);
                HSSFRow rowTemp = sheet.createRow(i + 10);
                HSSFCell cell1 = rowTemp.createCell(0);
                cell1.setCellStyle(centerCellStyle);
                cell1.setCellValue(i + 1);

                HSSFCell cell2 = rowTemp.createCell(1);
                cell2.setCellStyle(centerCellStyle);
                cell2.setCellValue(fcBondApplyVO.getItemNo());

                HSSFCell cell3 = rowTemp.createCell(2);
                cell3.setCellStyle(centerCellStyle);
                String itemType = "";
                if (!EmptyUtils.isEmpty(fcBondApplyVO.getItemType())) {
                    itemType = dictService.get("fcBond.itemType", fcBondApplyVO.getItemType());
                }
                cell3.setCellValue(itemType);

                HSSFCell cell4 = rowTemp.createCell(3);
                cell4.setCellStyle(centerCellStyle);
                String startDate1 = "";
                if (!EmptyUtils.isEmpty(fcBondApplyVO.getStartDate())) {
                    startDate1 = simpleDateFormat.format(fcBondApplyVO.getStartDate());
                }
                cell4.setCellValue(startDate1);

                HSSFCell cell5 = rowTemp.createCell(4);
                cell5.setCellStyle(centerCellStyle);
                String endDate = "";
                if (!EmptyUtils.isEmpty(fcBondApplyVO.getEndDate())) {
                    endDate = simpleDateFormat.format(fcBondApplyVO.getEndDate());
                }
                cell5.setCellValue(endDate);

                HSSFCell cell6 = rowTemp.createCell(5);
                cell6.setCellStyle(rightCellStyle);
                String applyMoney1 = "";
                if (!EmptyUtils.isEmpty(fcBondApplyVO.getApplyMoney())) {
                    applyMoney1 = df.format(fcBondApplyVO.getApplyMoney());
                }
                cell6.setCellValue(applyMoney1);

            }
            int rowNum = 10 + listFcBondApply.size();
//列表
            HSSFRow rowTitle2 = sheet.createRow(rowNum + 1);
            HSSFCell cellrowTitle2 = rowTitle2.createCell(0);
            cellrowTitle2.setCellStyle(style);
            cellrowTitle2.setCellValue("转回历史记录");
            rowTitle2.setHeightInPoints(20);
            // 合并单元格
            sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 0, 6));

            HSSFRow rowTitle3 = sheet.createRow(rowNum + 2);
            //给表头第一行一次创建单元格
            String[] heads1 = new String[]{
                    "序号", "转回操作人", "转回时间", "转回金额", "利息收入", "转回账号", "用途"
            };
            rowTitle3.setHeightInPoints(20);
            for (int i = 0; i < heads1.length; i++) {
                HSSFCell cell = rowTitle3.createCell((short) i);
                cell.setCellValue(heads1[i]);
                cell.setCellStyle(style);
                sheet.setColumnWidth(i, 256 * 18);
            }

            for (int i = 0; i < listFcBondBack.size(); i++) {
                FcBondBackVO fcBondBackVO1 = listFcBondBack.get(i);
                HSSFRow rowTemp = sheet.createRow(i + rowNum + 3);
                HSSFCell cell1 = rowTemp.createCell(0);
                cell1.setCellStyle(centerCellStyle);
                cell1.setCellValue(i + 1);

                HSSFCell cell2 = rowTemp.createCell(1);
                cell2.setCellStyle(centerCellStyle);
                cell2.setCellValue(fcBondBackVO1.getCreateByName());

                HSSFCell cell3 = rowTemp.createCell(2);
                cell3.setCellStyle(centerCellStyle);
                String bankDate = "";
                if (!EmptyUtils.isEmpty(fcBondBackVO1.getBackDate())) {
                    bankDate = simpleDateFormat.format(fcBondBackVO1.getBackDate());
                }
                cell3.setCellValue(bankDate);

                HSSFCell cell4 = rowTemp.createCell(3);
                cell4.setCellStyle(rightCellStyle);
                String backMoney1 = "";
                if (!EmptyUtils.isEmpty(fcBondBackVO1.getBackMoney())) {
                    backMoney1 = df.format(fcBondBackVO1.getBackMoney());
                }
                cell4.setCellValue(backMoney1);

                HSSFCell cell5 = rowTemp.createCell(4);
                cell5.setCellStyle(rightCellStyle);
                String backInterest1 = "";
                if (!EmptyUtils.isEmpty(fcBondBackVO1.getBackInterest())) {
                    backInterest1 = df.format(fcBondBackVO1.getBackInterest());
                }
                cell5.setCellValue(backInterest1);

                HSSFCell cell6 = rowTemp.createCell(5);
                cell6.setCellStyle(centerCellStyle);
                cell6.setCellValue(fcBondBackVO1.getOutBankAcc());

                HSSFCell cell7 = rowTemp.createCell(6);
                cell7.setCellStyle(centerCellStyle);
                cell7.setCellValue(fcBondBackVO1.getPurpose());
            }

            try {
                //实现Excel以文件流的形式到浏览器中,浏览器左下角出现Excel的下载提示，
                OutputStream out = response.getOutputStream();
                response.reset();
                filename = URLEncoder.encode(filename, "UTF8");
                response.setHeader("Content-disposition", "attachment; filename=" + filename);
                response.setContentType("application/x-download");
                wb.write(out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setBorder(HSSFCellStyle style) {
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
    }

    public HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook) {
        return initStyle(workbook, HorizontalAlignment.CENTER);
    }

    public HSSFCellStyle initStyle(HSSFWorkbook workbook, HorizontalAlignment align) {
        HSSFCellStyle style = workbook.createCellStyle();
        // 边框设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 字体居中
        style.setAlignment(align);
        return style;
    }

    public HSSFCellStyle getRightCellStyle(HSSFWorkbook workbook) {
        return initStyle(workbook, HorizontalAlignment.RIGHT);
    }
}
