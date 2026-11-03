package com.bytter.report.web.cms.controller;

import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.report.domain.cms.service.RatBkUseStatisticsSercive;
import com.bytter.report.domain.cms.vo.RatBkStatisticSearchVO;
import com.bytter.report.domain.cms.vo.RatBkUseCorpVO;
import com.bytter.report.domain.cms.vo.RatBkUseVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatBkrationService;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.IDictService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cms/report/ratbkusestatistics")
public class RatBkUseStatisticsController extends BaseController {

    @Autowired
    private IRatBkrationService bkrationService;

    @Autowired
    private RatBkUseStatisticsSercive ratBkUseStatisticsSercive;

    @Autowired(required = false)
    private IDictService iDictService;


    //@RequiresPermissions(ResourceConstant.CMS_REPORT_USE_QUERY)
    @LogMessage(content = "银行授信使用统计查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/queryBkUseRation")
    public String queryBkUseRation(RatBkStatisticSearchVO searchVO, Model model) {
        //显示分配的银行信息
        List<com.bytter.credit.dto.BtBankDTO> bankDTOs = bkrationService.queryRationBank();
        List<RatBkUseVO> ratBean = null;
        String optType = searchVO.getOptType();
        if (!EmptyUtils.isEmpty(optType)) {//查询
            ratBean = ratBkUseStatisticsSercive.getRatBkUseStatisticsList(searchVO, ResourceConstant.CMS_REPORT_USE_QUERY, getCurrentUser());
            model.addAttribute("reportDate", DateUtils.date2Str(DateUtils.getDate(), DateUtils.date_sdf()));//报表日期
            model.addAttribute("bankCode", searchVO.getBankCode());
            model.addAttribute("ratCode", searchVO.getRatCode());
            model.addAttribute("corpId", searchVO.getCorpId());//单位
            model.addAttribute("curCode", searchVO.getCurCode());//币别

            model.addAttribute("beginDate", DateUtils.format(searchVO.getBeginDate(), "yyyy-MM-dd"));//授信开始时间
            model.addAttribute("beginDate2", DateUtils.format(searchVO.getBeginDate2(), "yyyy-MM-dd"));//授信开始时间
            model.addAttribute("endDate", DateUtils.format(searchVO.getEndDate(), "yyyy-MM-dd"));//授信结束时间
            model.addAttribute("endDate2", DateUtils.format(searchVO.getEndDate2(), "yyyy-MM-dd"));//授信结束时间
        }
        model.addAttribute("isExpire",searchVO.getIsExpire());
        model.addAttribute("bankDTOs", bankDTOs);
        model.addAttribute("userName", getCurrentUser().getUsername());//制表人
        model.addAttribute("moneyType", searchVO.getMoneyType());//金额单位
        model.addAttribute("typesOfStatistics", searchVO.getTypesOfStatistics());//统计维度
        model.addAttribute("ratBean", ratBean);

        return "cms/report/ratBankRation/listBkUseStatics";
    }

    /**
     * 导出excel
     **/
    //@RequiresPermissions(ResourceConstant.CMS_REPORT_USE_QUERY)
    @LogMessage(content = "导出银行授信额度统计表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/excel")
    public String excel(RatBkStatisticSearchVO searchVO, HttpServletResponse response) {
        List<RatBkUseVO> list = null;
        list = ratBkUseStatisticsSercive.getRatBkUseStatisticsList(searchVO, ResourceConstant.CMS_REPORT_USE_QUERY, getCurrentUser());
        String filename = ratBkUseStatisticsSercive.getLangStr("RatBkUseStatistics.tableDetail") + ".xls";//报表名称：银行授信使用统计表.xls
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        //声明一个单子并命名
        HSSFSheet sheet = wb.createSheet(filename);
        sheet.setColumnWidth(0, 50 * 256);//设置第一列列宽
        sheet.setColumnWidth(1, 30 * 256);//设置第二列列宽
        sheet.setColumnWidth(3, 25 * 256);//设置第四列列宽
        sheet.setColumnWidth(5, 25 * 256);//设置第六列列宽
        for (int i = 10; i < 18; i++) {
            sheet.setColumnWidth(i, 20 * 256);//设置第11-18列列宽
        }
        //创建表格样式
        creatExcel(wb, sheet, searchVO.getTypesOfStatistics());
        //填充数据
        wb = this.addDataToSheet(list, sheet, wb, searchVO.getTypesOfStatistics());
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
        return null;
    }

    /**
     * 创建表格样式
     */
    public void creatExcel(HSSFWorkbook wb, HSSFSheet sheet, String typesOfStatistics) {
        String firstCol = "common.button.bank";//第一列表头：银行
        String secondCol = "table.fcloanrepay.corpName";//第二列表头：单位
        if (typesOfStatistics.equals(CmsConstant.SORT_TYPE_0)) {//统计维度--按单位统计，默认按银行统计
            firstCol = "table.fcloanrepay.corpName";//单位
            secondCol = "common.button.bank";//银行
        }
        //给单子名称一个长度
        sheet.setDefaultColumnWidth((short) 18);
        // 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();
        //创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);
        //设置表头字体
        HSSFFont headfont = wb.createFont();
        headfont.setFontName("宋体");
        headfont.setFontHeightInPoints((short) 14);// 字体大小
        headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        style.setFont(headfont);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        this.setBorder(style);
        //给表头第一行一次创建单元格
        String[] heads = new String[]{
                ratBkUseStatisticsSercive.getLangStr(firstCol),
                ratBkUseStatisticsSercive.getLangStr(secondCol),
                ratBkUseStatisticsSercive.getLangStr("table.fcBusinessRuleDefine.curCode"),//币别
                ratBkUseStatisticsSercive.getLangStr("table.bytter_test.create_time"),//时间
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDisDetail.ratMoneySum"),//授信总额
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDisDetail.useMoneySum"),//已使用额度合计
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDisDetail.leftMoneySum"),//剩余额度合计
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDis.distributeWay"),//分配方式
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDisDetail.remark"),//备注
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDetail.itemCode"),//授信品种
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDetail.creditType"),//授信方式
                ratBkUseStatisticsSercive.getLangStr("table.ratBkration.money"),//授信额度
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDisDetail.superProcessMoney"),//占用额度
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDisDetail.checkProcessMoney"),//在审占用额度
                ratBkUseStatisticsSercive.getLangStr("table.report.ratBondApply"),//保证金（已执行）
                ratBkUseStatisticsSercive.getLangStr("table.report.ratBondFrozen"),//保证金（在途）
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDisDetail.overPlusMoney"),//可用额度
                ratBkUseStatisticsSercive.getLangStr("table.ratBkrationDisDetail.leftDisMoney")};//剩余额度
        row.setHeightInPoints(20);
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            cell.setCellValue(heads[i]);
            cell.setCellStyle(style);
            //sheet.setColumnWidth(i, 256 * 18);
        }
    }

    /**
     * 填充数据
     */
    private HSSFWorkbook addDataToSheet(List<RatBkUseVO> list, HSSFSheet sheet, HSSFWorkbook wb, String typesOfStatistics) {
        //获取第2-9列的数据，存入map中
        int corpSize = 1;
        Map<String, RatBkUseCorpVO> map = new HashMap<String, RatBkUseCorpVO>();
        for (int i = 0; i < list.size(); i++) {
            List<RatBkUseCorpVO> corpVOList = list.get(i).getCorpDtls();
            if (null != corpVOList) {
                for (int j = 0; j < corpVOList.size(); j++) {
                    map.put(corpSize + "", corpVOList.get(j));
                    corpSize++;
                }
            }
        }

        //获取明细数据，放入detaillMap中
        int detailSize = 1;
        Map<String, Object> detaillMap = new HashMap<String, Object>();
        for (int j = 0; j < map.size(); j++) {
            RatBkUseCorpVO corpVO = map.get(j + 1 + "");
            List<RatUrationUseDtlVO> detailList = new ArrayList<RatUrationUseDtlVO>();
            if (!corpVO.getCorpId().equals("subTotal")) {//合计
                detailList = corpVO.getRationDlts();
                for (int z = 0; z < detailList.size(); z++) {
                    detaillMap.put(detailSize + "", detailList.get(z));
                    detailSize++;
                }
            } else {
                detaillMap.put(detailSize + "", corpVO);
                detailSize++;
            }
        }
        detaillMap.put(detailSize + "", list.get(list.size() - 1));

        //填充明细数据
        int rowIndex = 1;
        HSSFRow row = null;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        HSSFCellStyle centerCellStyle = getCenterCellStyle(wb);
        HSSFCellStyle rightCellStyle = getRightCellStyle(wb);

        Map<String, DictDTO> listMap = iDictService.listMap("rat.creditType");
        for (int i = 1; i < detaillMap.size(); i++) {
            String mapObject = detaillMap.get(i + "").getClass().getSimpleName();
            row = sheet.createRow(rowIndex);
            for (int k = 0; k <= 3; k++) {
                row.createCell(k);
                row.getCell((short) k).setCellStyle(centerCellStyle);
            }
            for (int k = 4; k <= 17; k++) {
                row.createCell(k);
                row.getCell((short) k).setCellStyle(rightCellStyle);
            }
            if (mapObject.equals("RatUrationUseDtlVO")) {
                RatUrationUseDtlVO dtlVO = (RatUrationUseDtlVO) detaillMap.get(i + "");
                row.getCell(9).setCellValue(dtlVO.getItemName());//授信品种
                String creditType = dtlVO.getCreditType();
                String creditTypeName = "";
                if (EmptyUtils.isNotEmpty(creditType)) {
                    DictDTO creditTypeDTO = listMap.get(creditType);
                    if (!EmptyUtils.isEmpty(creditTypeDTO)) {
                        creditTypeName = creditTypeDTO.getName();
                    }
                }
                row.getCell(10).setCellValue(creditTypeName);//授信方式
                row.getCell(11).setCellValue(df.format(dtlVO.getMoney()) + "");//授信额度
                row.getCell(12).setCellValue(df.format(dtlVO.getUseMoney()) + "");//占用额度
                row.getCell(13).setCellValue(df.format(dtlVO.getApproveMoney()) + "");//在审占用额度
                row.getCell(14).setCellValue(df.format(dtlVO.getBondApply()) + "");//保证金(已执行)
                row.getCell(15).setCellValue(df.format(dtlVO.getBondAway()) + "");//保证金（在途）
                row.getCell(16).setCellValue(df.format(dtlVO.getOverPlusMoney()) + "");//可用额度
                row.getCell(17).setCellValue(df.format(dtlVO.getLeftMoney()) + "");//剩余额度
				for (int k = 9; k <= 10; k++) {
					row.getCell((short) k).setCellStyle(centerCellStyle);
				}
            } else if (mapObject.equals("RatBkUseCorpVO")) {
                RatBkUseCorpVO corpVO = (RatBkUseCorpVO) detaillMap.get(i + "");
                row.getCell(11).setCellValue(df.format(corpVO.getMoney()) + "");
                row.getCell(12).setCellValue(df.format(corpVO.getUseMoney()) + "");
                row.getCell(13).setCellValue(df.format(corpVO.getApproveMoney()) + "");
                row.getCell(14).setCellValue(df.format(corpVO.getBondApply()) + "");//保证金(已执行)小计
                row.getCell(15).setCellValue(df.format(corpVO.getBondAway()) + "");//保证金(在途)小计
                row.getCell(16).setCellValue(df.format(corpVO.getOverPlusMoney()) + "");
                row.getCell(17).setCellValue(df.format(corpVO.getLeftMoney()) + "");
                CellRangeAddress region5 = new CellRangeAddress(rowIndex, rowIndex, (short) 1, (short) 10);//合并2-11列
                sheet.addMergedRegion(region5);
                row.createCell(1).setCellValue(corpVO.getCorpName());
                HSSFCell theCell = row.getCell((short) 1);//设置合并列格式
                HSSFCellStyle style = wb.createCellStyle();
                style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//居右
                this.setBorder(style);//设置所有边框
                theCell.setCellStyle(style);
                for (int m = 1; m < 11; m++) {
                    row.getCell((short) m).setCellStyle(style);
                }
            }
            rowIndex++;
        }

        //填充数据（2-9列）
        int corpSpanSize = 0;
        int firstRow = 1;
        for (int i = 1; i < map.size(); i++) {
            RatBkUseCorpVO corpVO = map.get(i + "");
            if (!corpVO.getCorpId().equals("subTotal")) {
                row = sheet.getRow(firstRow);
                //统计维度为单位时第二列显示银行，统计维度为银行时第二列显示单位
                row.getCell(1).setCellValue(typesOfStatistics.equals(CmsConstant.SORT_TYPE_0) ? corpVO.getBankName() : corpVO.getCorpName());
                row.getCell(2).setCellValue(corpVO.getCurName());
                row.getCell(3).setCellValue(corpVO.getRatDateStr());
                row.getCell(4).setCellValue(df.format(corpVO.getMoney()));
                row.getCell(5).setCellValue(df.format(corpVO.getUseMoney()));
                row.getCell(6).setCellValue(df.format(corpVO.getLeftMoney()));

                String disName = "";
                if (corpVO.getDisWay().equals("1")) {
                    disName = ratBkUseStatisticsSercive.getDict("RatBkrationDis.DistributeWay", "1");//自用
                    row.getCell(7).setCellValue(disName);
                } else if (corpVO.getDisWay().equals("2")) {
                    disName = ratBkUseStatisticsSercive.getDict("RatBkrationDis.DistributeWay", "2");//总额共享
                    row.getCell(7).setCellValue(disName);
                } else if (corpVO.getDisWay().equals("3")) {
                    disName = ratBkUseStatisticsSercive.getDict("RatBkrationDis.DistributeWay", "3");//金额共享
                    row.getCell(7).setCellValue(disName);
                } else {
                    row.getCell(7).setCellValue("");
                }
                row.getCell(8).setCellValue(corpVO.getRemark());
            }

            if (corpVO.getCorpId().equals("subTotal")) {//总计的数据单独占一行
                corpSpanSize = 1;
            } else {
                corpSpanSize = getOneCorpListSize(corpVO);//获取需要跨的行数
            }
            if (corpSpanSize > 1) {
                int lastRow = firstRow + corpSpanSize - 1;
                CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, (short) 1, (short) 1);
                sheet.addMergedRegion(region);
                CellRangeAddress region2 = new CellRangeAddress(firstRow, lastRow, (short) 2, (short) 2);
                sheet.addMergedRegion(region2);
                CellRangeAddress region3 = new CellRangeAddress(firstRow, lastRow, (short) 3, (short) 3);
                sheet.addMergedRegion(region3);
                CellRangeAddress region4 = new CellRangeAddress(firstRow, lastRow, (short) 4, (short) 4);
                sheet.addMergedRegion(region4);
                CellRangeAddress region5 = new CellRangeAddress(firstRow, lastRow, (short) 5, (short) 5);
                sheet.addMergedRegion(region5);
                CellRangeAddress region6 = new CellRangeAddress(firstRow, lastRow, (short) 6, (short) 6);
                sheet.addMergedRegion(region6);
                CellRangeAddress region7 = new CellRangeAddress(firstRow, lastRow, (short) 7, (short) 7);
                sheet.addMergedRegion(region7);
                CellRangeAddress region8 = new CellRangeAddress(firstRow, lastRow, (short) 8, (short) 8);
                sheet.addMergedRegion(region8);
            }

            firstRow += corpSpanSize;
        }


        //设置第一列的数据
        int index = 1;
        int rowspanSize = 0;

        for (int i = 0; i < list.size(); i++) {
            HSSFCell cell = null;
            if (StringUtils.isEmpty(list.get(i).getBankName())) {//币别合计显示
                row = sheet.createRow(index);
                row.createCell(0).setCellValue(list.get(i).getCorpName());
                cell = row.getCell((short) 0);
                row.createCell(1);
                row.createCell(2);
                row.createCell(3);
                row.createCell(4);
                row.createCell(5);
                row.createCell(6);
                row.createCell(7);
                row.createCell(8);
                row.createCell(9);
                row.createCell(10);
                row.createCell(11).setCellValue(df.format(list.get(i).getMoney()) + "");
                row.createCell(12).setCellValue(df.format(list.get(i).getUseMoney()) + "");
                row.createCell(13).setCellValue(df.format(list.get(i).getApproveMoney()) + "");
                row.createCell(14).setCellValue(df.format(list.get(i).getBondApply()) + "");//保证金(已执行)合计
                row.createCell(15).setCellValue(df.format(list.get(i).getBondAway()) + "");//保证金(在途)合计
                row.createCell(16).setCellValue(df.format(list.get(i).getOverPlusMoney()) + "");
                row.createCell(17).setCellValue(df.format(list.get(i).getLeftMoney()) + "");
                CellRangeAddress region = new CellRangeAddress(index, index, (short) 0, (short) 10);//合并2-10列
                sheet.addMergedRegion(region);
                HSSFCell sumCell = row.getCell((short) 1);//设置合并列格式
                HSSFCellStyle sumStyle = wb.createCellStyle();
                sumStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//居右
                this.setBorder(sumStyle);//设置所有边框
                sumCell.setCellStyle(sumStyle);
                for (int n = 1; n < 18; n++) {
                    row.getCell((short) n).setCellStyle(sumStyle);
                }
            } else {
                row = sheet.getRow(index);
                row.createCell(0).setCellValue(typesOfStatistics.equals(CmsConstant.SORT_TYPE_0) ? list.get(i).getCorpName() : list.get(i).getBankName());
                cell = row.getCell((short) 0);
            }
            HSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//水平居右
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
            cellStyle.setWrapText(true);//设置自动换行
            this.setBorder(cellStyle);//设置所有边框
            cell.setCellStyle(cellStyle);
            rowspanSize = getOneBkUseSize(list.get(i));//获取需要跨的行数
            if (rowspanSize > 1) {
                CellRangeAddress region = new CellRangeAddress(index, index + rowspanSize - 1, (short) 0, (short) 0);//参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
                sheet.addMergedRegion(region);
            }
            index += rowspanSize;
        }

        return wb;
    }

    /**
     * 获取银行或单位名称的高度（需要跨的行数）
     */
    public int getOneBkUseSize(RatBkUseVO ratBkUseVO) {
        int maxHeight = 1;
        List<RatBkUseCorpVO> banklist = ratBkUseVO.getCorpDtls();
        if (null != banklist) {
            for (int k = 0, paramSize = banklist.size(); k < paramSize - 1; k++) {
                if (banklist.get(k).getCorpId().equals("subTotal")) {//合计时加1
                    maxHeight += 1;
                } else {
                    List<RatUrationUseDtlVO> listDtl = banklist.get(k).getRationDlts();
                    if (listDtl != null || listDtl.size() > 0) {
                        maxHeight += listDtl.size();
                    }
                }
            }
        }
        return maxHeight;
    }

    /**
     * 获取单个对象的高度（需要跨的行数）,主要是处理一对多的情况
     */
    public int getOneCorpListSize(RatBkUseCorpVO ratBkUseCorpVO) {
        int maxHeight = 1;
        List<RatUrationUseDtlVO> listDtl = ratBkUseCorpVO.getRationDlts();
        maxHeight = listDtl.size();
        return maxHeight;
    }

    /**
     * 设置表格所有边框
     */
    public void setBorder(HSSFCellStyle style) {
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
    }


    /**
     * 获取一个初始的样式
     */
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

    /**
     * 获取居中样式
     *
     * @throws Exception
     */
    public HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook) {
        return initStyle(workbook, HorizontalAlignment.CENTER);
    }

    /**
     * 获取靠右样式
     *
     * @throws Exception
     */
    public HSSFCellStyle getRightCellStyle(HSSFWorkbook workbook) {
        return initStyle(workbook, HorizontalAlignment.RIGHT);
    }

    /**
     * 获取靠左样式
     *
     * @throws Exception
     */
    public HSSFCellStyle getLeftCellStyle(HSSFWorkbook workbook) {
        return initStyle(workbook, HorizontalAlignment.LEFT);
    }
}
