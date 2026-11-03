package com.bytter.poi.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.TableStyle;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.excel.dto.ExportExcelHeadDTO;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.exception.excel.ExcelImportException;
import com.bytter.poi.exception.excel.enums.ExcelImportEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import jodd.typeconverter.Convert;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
//todo 该工具类需要用3.0jar包重写
/**
 * @author zuoy
 */
public class ExportUtils {

    protected static final Logger logger = LoggerFactory.getLogger(ExportUtils.class);

    /**
     * 导出每列配置集合
     */
    public static final String CONFIG = "configList";
    /**
     * 导出每列标题集合
     */
    public static final String TITLE = "title";
    /**
     * 导出每列宽度集合
     */
    public static final String COLUMN_WIDTH = "columnWidth";

    public interface ExportData {
        List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user);
    }

    /**
     * 导出excel封装
     *
     * @param response                 相应头
     * @param dataTablesInputParam     数据表格参数
     * @param exportExcelHeadListParam 导出excel参数
     * @param exportData               获取导出数据接口
     * @param totalParam               总页数
     * @param fileNameParam            文件名称
     * @param fetchDataSize            每次查询多少笔
     * @param aClass                   转换对象
     * @throws Exception 异常信息
     * @author zhoul 2020-12-09
     * @since 3.4
     */
    public static void exportByStream(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                                      ExportData exportData, String totalParam, String fileNameParam, int fetchDataSize, Class<?> aClass) throws Exception {
        // 转换查询条件
        ObjectMapper mapper = new ObjectMapper();
        DataTablesInput input = mapper.readValue(URLDecoder.decode(dataTablesInputParam, "UTF-8"), DataTablesInput.class);
        //添加响应头信息
        response.setHeader("Content-disposition", "attachment; filename=" + fileNameParam + ".xlsx");
        //设置类型
        response.setContentType("application/msexcel;charset=UTF-8");
        //设置头
        response.setHeader("Pragma", "No-cache");
        //设置头
        response.setHeader("Cache-Control", "no-cache");
        //设置日期头
        response.setDateHeader("Expires", 0);

        List<ExportExcelHeadDTO> exportExcelHeadList = ExportUtils.getExportExcelHeadDTO(exportExcelHeadListParam);
        Map<String, Object> exportParam = ExportUtils.getExportParam(exportExcelHeadList, aClass);

        int currentSheet = 0;
        int sheetCount = 0;
        //每个sheet存放最大记录数
        int sheetSize = 800000;

        //一次查询多少条
        int total = Integer.parseInt(totalParam);
        int sheetNumber = Double.valueOf(Math.ceil(total / (double) sheetSize)).intValue();

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        // 每列宽度
        Map<Integer, Integer> sheetWidthMap = (Map<Integer, Integer>) exportParam.get(ExportUtils.COLUMN_WIDTH);
        //获取标题
        List<List<String>> titles = (List<List<String>>) exportParam.get(ExportUtils.TITLE);
        //先使用alibaba的工具类生成默认的头部
        CellStyle cellStyle = initHeaderStyle(workbook);
        for (int i = 1; i <= sheetNumber; i++) {
            SXSSFSheet sheet = workbook.createSheet("sheet" + i);
            //设置第一行表头格式
            Row headRow = sheet.createRow(0);
            for (Map.Entry<Integer, Integer> entry : sheetWidthMap.entrySet()) {
                sheet.setColumnWidth(entry.getKey(), entry.getValue());
            }
            for (int j = 0; j < titles.size(); j++) {
                //创建单元格
                Cell cell = headRow.createCell(j);
                //设置值
                cell.setCellValue(titles.get(j).get(0));
                //设置标题样式
                cell.setCellStyle(cellStyle);
            }
        }
        //需要查询的次数
        int times = Double.valueOf(Math.ceil(total / (double) fetchDataSize)).intValue();
        int start;
        SXSSFSheet sheet = workbook.getSheetAt(0);
        try (OutputStream outputStream = response.getOutputStream()){
            for (int i = 0; i < times; i++) {
                start = i * fetchDataSize;
                if (total - start < fetchDataSize) {
                    fetchDataSize = total - start;
                }
                input.setStart(start);
                input.setLength(fetchDataSize);
                input.setDraw(input.getDraw() + 1);
                List<?> list = exportData.getData(input, input.getCurrentUser());
                List<List<String>> dataList = ExportUtils.getDataList(list, exportParam);
                for (List<String> strings : dataList) {
                    //判断是否需要新起一个sheet
                    if (sheetCount % sheetSize == 0) {
                        sheet = workbook.getSheetAt(currentSheet++);
                        // workbook.writeBeginSheet(sheet);
                    }
                    //写入数据
                    int indexNum = sheetCount % sheetSize + 2;
                    SXSSFRow row = sheet.createRow(indexNum);
                    for (int cell = 0; cell < strings.size(); cell++) {
                        row.createCell(cell).setCellValue(strings.get(cell));
                    }
                    sheetCount++;
                }
                //清空集合，释放内存
                dataList.clear();
                list.clear();
            }
            // 写入Excel到响应
            workbook.write(outputStream);
        } finally {
            //这里出错的情形为用户点击取消，不将异常打印出来
            try {
                //关闭流
                workbook.dispose();
            } catch (Exception ignored) {
            }
        }
    }

    public static void exportByStreamCopy(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                                      ExportData exportData, String totalParam, String fileNameParam, int fetchDataSize, Class<?> aClass) throws Exception {
        // 转换查询条件
        ObjectMapper mapper = new ObjectMapper();
        DataTablesInput input = mapper.readValue(URLDecoder.decode(dataTablesInputParam, "UTF-8"), DataTablesInput.class);
        //添加响应头信息
        response.setHeader("Content-disposition", "attachment; filename=" + fileNameParam + ".xlsx");
        //设置类型
        response.setContentType("application/msexcel;charset=UTF-8");
        //设置头
        response.setHeader("Pragma", "No-cache");
        //设置头
        response.setHeader("Cache-Control", "no-cache");
        //设置日期头
        response.setDateHeader("Expires", 0);

        List<ExportExcelHeadDTO> exportExcelHeadList = ExportUtils.getExportExcelHeadDTO(exportExcelHeadListParam);
        Map<String, Object> exportParam = ExportUtils.getExportParam(exportExcelHeadList, aClass);

        int currentSheet = 0;
        int sheetCount = 0;
        //每个sheet存放最大记录数
        int sheetSize = 800000;

        //一次查询多少条
        int total = Integer.parseInt(totalParam);
        int sheetNumber = Double.valueOf(Math.ceil(total / (double) sheetSize)).intValue();

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        // 每列宽度
        Map<Integer, Integer> sheetWidthMap = (Map<Integer, Integer>) exportParam.get(ExportUtils.COLUMN_WIDTH);
        //获取标题
        List<List<String>> titles = (List<List<String>>) exportParam.get(ExportUtils.TITLE);
        //先使用alibaba的工具类生成默认的头部
        CellStyle cellStyle = initHeaderStyle(workbook);
        for (int i = 1; i <= sheetNumber; i++) {
            SXSSFSheet sheet = workbook.createSheet("sheet" + i);
            //设置第一行表头格式
            Row headRow = sheet.createRow(0);
            for (Map.Entry<Integer, Integer> entry : sheetWidthMap.entrySet()) {
                sheet.setColumnWidth(entry.getKey(), entry.getValue());
            }
            for (int j = 0; j < titles.size(); j++) {
                //创建单元格
                Cell cell = headRow.createCell(j);
                //设置值
                cell.setCellValue(titles.get(j).get(0));
                //设置标题样式
                cell.setCellStyle(cellStyle);
            }
        }
        //需要查询的次数
        int times = Double.valueOf(Math.ceil(total / (double) fetchDataSize)).intValue();
        int start;
        SXSSFSheet sheet = workbook.getSheetAt(0);
        try (OutputStream outputStream = response.getOutputStream()){
            for (int i = 0; i < times; i++) {
                start = i * fetchDataSize;
                if (total - start < fetchDataSize) {
                    fetchDataSize = total - start;
                }
                input.setStart(start);
                input.setLength(fetchDataSize);
                input.setDraw(input.getDraw() + 1);
                List<?> list = exportData.getData(input, input.getCurrentUser());
                List<List<String>> dataList = ExportUtils.getDataList(list, exportParam);
                for (List<String> strings : dataList) {
                    //判断是否需要新起一个sheet
                    if (sheetCount % sheetSize == 0) {
                        sheet = workbook.getSheetAt(currentSheet++);
                        // workbook.writeBeginSheet(sheet);
                    }
                    //写入数据
                    int indexNum = sheetCount % sheetSize + 1;
                    SXSSFRow row = sheet.createRow(indexNum);
                    for (int cell = 0; cell < strings.size(); cell++) {
                        row.createCell(cell).setCellValue(strings.get(cell));
                    }
                    sheetCount++;
                }
                //清空集合，释放内存
                dataList.clear();
                list.clear();
            }
            // 写入Excel到响应
            workbook.write(outputStream);
        } finally {
            //这里出错的情形为用户点击取消，不将异常打印出来
            try {
                //关闭流
                workbook.dispose();
            } catch (Exception ignored) {
            }
        }
    }

    @NotNull
    private static CellStyle initHeaderStyle(SXSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
        cellStyle.setWrapText(true);//设置自动换行
        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 导出excel封装
     *
     * @param response                 相应头
     * @param dataTablesInputParam     数据表格参数
     * @param exportExcelHeadListParam 导出excel参数
     * @param exportData               获取导出数据接口
     * @param totalParam               总页数
     * @param fileNameParam            文件名称
     * @param fileNameParam            标题
     * @param fetchDataSize            每次查询多少笔
     * @param aClass                   转换对象
     * @throws Exception 异常信息
     * @author zhoul 2020-12-09
     * @since 3.4
     */
    public static void exportByStream(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                                      ExportData exportData, String totalParam, String fileNameParam,String title, int fetchDataSize, Class<?> aClass) throws Exception{
        exportByStreamAll(response, dataTablesInputParam, exportExcelHeadListParam,null,exportData,totalParam,fileNameParam,title,fetchDataSize,aClass);
    }
    /**
     * 导出excel自定义表头封装
     *
     * @param response                 相应头
     * @param dataTablesInputParam     数据表格参数
     * @param exportExcelHeadListParam 导出excel参数
     * @param header                   自定义导出表头
     * @param exportData               获取导出数据接口
     * @param totalParam               总页数
     * @param fileNameParam            文件名称
     * @param fileNameParam            标题
     * @param fetchDataSize            每次查询多少笔
     * @param aClass                   转换对象
     * @throws Exception 异常信息
     * @author zhoul 2020-12-09
     * @since 3.4
     */
    public static void exportByStreamCustomizeHeader(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,ExcelHeader header,
                                      ExportData exportData, String totalParam, String fileNameParam,String title, int fetchDataSize, Class<?> aClass) throws Exception{
        exportByStreamAll(response, dataTablesInputParam, exportExcelHeadListParam,header,exportData,totalParam,fileNameParam,title,fetchDataSize,aClass);
    }
    /**
     * 导出excel封装
     *
     * @param response                 相应头
     * @param dataTablesInputParam     数据表格参数
     * @param exportExcelHeadListParam 导出excel参数
     * @param header                   自定义导出表头
     * @param exportData               获取导出数据接口
     * @param totalParam               总页数
     * @param fileNameParam            文件名称
     * @param fileNameParam            标题
     * @param fetchDataSize            每次查询多少笔
     * @param aClass                   转换对象
     * @throws Exception 异常信息
     * @author zhoul 2020-12-09
     * @since 3.4
     */
    public static void exportByStreamAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,ExcelHeader header,
                                      ExportData exportData, String totalParam, String fileNameParam,String title, int fetchDataSize, Class<?> aClass) throws Exception {
        // 转换查询条件
        ObjectMapper mapper = new ObjectMapper();
        DataTablesInput input = mapper.readValue(URLDecoder.decode(dataTablesInputParam, "UTF-8"), DataTablesInput.class);
        //添加响应头信息
        response.setHeader("Content-disposition", "attachment; filename=" + fileNameParam + ".xlsx");
        //设置类型
        response.setContentType("application/msexcel;charset=UTF-8");
        //设置头
        response.setHeader("Pragma", "No-cache");
        //设置头
        response.setHeader("Cache-Control", "no-cache");
        //设置日期头
        response.setDateHeader("Expires", 0);
        // 设置超时时间
        // 设置连接为长连接
        response.setHeader("Connection", "keep-alive");
        // 设置超时时间
        response.setHeader("Keep-Alive", "timeout=1800");// 30分钟
        // 获取当前请求上下文并设置超时
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 设置请求超时为1小时（根据需要调整）
            request.setAttribute("org.apache.tomcat.request.timeout", 1800000);
            request.setAttribute("org.apache.coyote.response.timeout", 1800000);
        }

        List<ExportExcelHeadDTO> exportExcelHeadList = ExportUtils.getExportExcelHeadDTO(exportExcelHeadListParam);
        Map<String, Object> exportParam = ExportUtils.getExportParam(exportExcelHeadList, aClass);

        int currentSheet = 0;
        int sheetCount = 0;
        //每个sheet存放最大记录数
        int sheetSize = 800000;

        //一次查询多少条
        int total = Integer.parseInt(totalParam);
        int sheetNumber = Double.valueOf(Math.ceil(total / (double) sheetSize)).intValue();

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        // 每列宽度
        Map<Integer, Integer> sheetWidthMap = (Map<Integer, Integer>) exportParam.get(ExportUtils.COLUMN_WIDTH);
        //获取标题
        List<List<String>> titles = (List<List<String>>) exportParam.get(ExportUtils.TITLE);
        //先使用alibaba的工具类生成默认的头部
        CellStyle cellStyle = initHeaderStyle(workbook);
        //金额样式
        CellStyle moneyStyle = initMoneyStyle(workbook);
        // 生成表头
        for (int i = 1; i <= sheetNumber; i++) {
            SXSSFSheet sheet = workbook.createSheet("sheet" + i);
            //设置第一行表头格式
            Row titleRow = sheet.createRow(0);
            Cell cell0 = titleRow.createCell(0);
            cell0.setCellValue(title);
            cell0.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titles.size()-1));

            // 第二行表头
            int rownum = 1;
            if (ObjectUtil.isEmpty(header)){
                Row headRow = sheet.createRow(rownum);
                for (int j = 0; j < titles.size(); j++) {
                    //创建单元格
                    Cell cell1 = headRow.createCell(j);
                    //设置值
                    cell1.setCellValue(titles.get(j).get(0));
                    //设置标题样式
                    cell1.setCellStyle(cellStyle);
                }
            }else{
                //定制表头,定制表头信息
                List<List<String>> headerTitles = header.getCellInfos();
                for(int j = 0; j < headerTitles.size(); j++){
                    Row headRow = sheet.createRow(rownum);
                    List<String> headerTitle = headerTitles.get(j);
                    for(int k=0; k<headerTitle.size();k++){
                        Cell cellk = headRow.createCell(k);
                        cellk.setCellValue(headerTitle.get(k));
                        cellk.setCellStyle(cellStyle);
                    }
                    rownum++;
                }

                //设置表格合并
                List<Integer[]> mergedRegion = header.getMergedRegion();
                if (ObjectUtil.isNotEmpty(mergedRegion)){
                    for(int z=0; z<mergedRegion.size(); z++){
                        Integer[] region = mergedRegion.get(z);
                        sheet.addMergedRegion(new CellRangeAddress(region[0], region[1], region[2], region[3]));
                    }
                }

            }
            for (Map.Entry<Integer, Integer> entry : sheetWidthMap.entrySet()) {
                sheet.setColumnWidth(entry.getKey(), entry.getValue());
            }
        }
        //需要查询的次数
        int times = Double.valueOf(Math.ceil(total / (double) fetchDataSize)).intValue();
        int start;
        SXSSFSheet sheet = workbook.getSheetAt(0);
        try (OutputStream outputStream = response.getOutputStream()) {
            List<?> totalList = new ArrayList<>();
            Map<String, Object> totalMap = new HashMap<>();
            for (int i = 0; i < times; i++) {
                start = i * fetchDataSize;
                if (total - start < fetchDataSize) {
                    fetchDataSize = total - start;
                }
                input.setStart(start);
                input.setLength(fetchDataSize);
                input.setDraw(input.getDraw() + 1);
                logger.error("=====start:" + start + ",fetchDataSize===" + fetchDataSize);
                List list = exportData.getData(input, input.getCurrentUser());
                // 如果做了统计，则将统计结果保留，但不直接录入系统
                List<String> totalColumn = input.getTotalList();
                // 处理统计数据
                if (fetchDataSize < list.size() && totalColumn.size() > 0) {
                    List subTotal = ListUtil.sub(list, fetchDataSize, list.size());
                    totalList.addAll(subTotal);
                    list = ListUtil.sub(list, 0, fetchDataSize);
                }
                // 最后一次获取时，加入输出队列
                if (i == times - 1 && totalList.size() > 0) {
                    initTotalList(input, totalList, totalMap, list, totalColumn);
                }
                logger.error("=====size:"+ list.size());
                List<List<Object>> dataList = ExportUtils.getObjDataList(list, exportParam);
                for (List<Object> strings : dataList) {
                    //判断是否需要新起一个sheet
                    if (sheetCount % sheetSize == 0) {
                        sheet = workbook.getSheetAt(currentSheet++);
                    }
                    //写入数据
                    int beginRow = 0;
                    if(header==null){
                        beginRow = sheetCount % sheetSize + 2;
                    }else {
                        int size = header.getCellInfos().size();
                        beginRow = sheetCount % sheetSize + 1 + size;
                    }
                    SXSSFRow row = sheet.createRow(beginRow);
                    for (int cell = 0; cell < strings.size(); cell++) {
                        Cell rowCell = row.createCell(cell);
                        Object obj = strings.get(cell);
                        if (obj instanceof BigDecimal) {
                            rowCell.setCellStyle(moneyStyle);
                            rowCell.setCellValue(new BigDecimal(obj.toString()).doubleValue());
                        } else {
                            rowCell.setCellValue(obj.toString());
                        }
                    }
                    sheetCount++;
                }
                // 清空集合，释放内存
                dataList.clear();
                list.clear();
            }
            // 写入Excel到响应
            workbook.write(outputStream);
            outputStream.close();
        } finally {
            //这里出错的情形为用户点击取消，不将异常打印出来
            try {
                //关闭流
                workbook.dispose();
            } catch (Exception ignored) {
            }
        }
    }

    private static void initTotalList(DataTablesInput input, List<?> totalList, Map<String, Object> totalMap, List list, List<String> totalColumn) {
        totalList.forEach(item->{
            String totalTitle = (String) getFieldValue(item, input.getTotalColumn());
            Object totalObj = totalMap.get(totalTitle);
            if (totalObj == null) {
                totalMap.put(totalTitle, item);
            } else {
                totalColumn.forEach(column -> {
                    BigDecimal addValue = Convert.toBigDecimal(getFieldValue(item, column), BigDecimal.ZERO);
                    BigDecimal totalValue = Convert.toBigDecimal(getFieldValue(totalObj, column), BigDecimal.ZERO);
                    setFieldValue(totalObj, column, totalValue.add(addValue));
                });
            }
        });
        for (Map.Entry<String, Object> entry : totalMap.entrySet()) {
            list.add(entry.getValue());
        }
    }

    /**
     * 辅助方法：获取对象指定名称字段值。
     *
     * @param data      数据对象。
     * @param fieldName 字段名。
     * @return 字段值。
     */
    private static Object getFieldValue(Object data, String fieldName) {
        try {
            Field declaredField = data.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return declaredField.get(data);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("当前字段【{}】不存在", fieldName);
            return null;
        }
    }

    /**
     * 辅助方法：根据字段名为对象字段赋值。
     *
     * @param data       数据对象。
     * @param fieldName  字段名。
     * @param fieldValue 字段值。
     */
    private static void setFieldValue(Object data, String fieldName, Object fieldValue) {
        try {
            Field declaredField = data.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            declaredField.set(data, fieldValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("当前字段【{}】不存在", fieldName);
        }
    }

    @NotNull
    private static CellStyle initMoneyStyle(SXSSFWorkbook workbook) {
        CellStyle moneyStyle = workbook.createCellStyle();
        // 获取数据格式对象
        DataFormat format = workbook.createDataFormat();
        // 保留两位小数
        moneyStyle.setDataFormat(format.getFormat("#,##0.00"));
        moneyStyle.setAlignment(HorizontalAlignment.RIGHT);
        return moneyStyle;
    }

    public static void export(HttpServletRequest request, HttpServletResponse response, String exportExcelHeadListParam, List dataList, String fileNameParam, Class aClass) throws Exception {
        List<ExportExcelHeadDTO> exportExcelHeadList = getExportExcelHeadDTO(exportExcelHeadListParam);
        ExcelWriter writer = null;
        try (OutputStream outputStream = response.getOutputStream()) {
            //添加响应头信息
            response.setHeader("Content-disposition", "attachment; filename=" + fileNameParam + ".xlsx");
            response.setContentType("application/msexcel;charset=UTF-8");//设置类型
            response.setHeader("Pragma", "No-cache");//设置头
            response.setHeader("Cache-Control", "no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
            writer = new ExcelWriter(outputStream, ExcelTypeEnum.XLSX);

            // 设置SHEET
            com.alibaba.excel.metadata.Sheet sheet = new com.alibaba.excel.metadata.Sheet(1, 0);
            sheet.setSheetName("Sheet1");
            sheet.setTableStyle(new TableStyle());

            // 获取导出参数
            Map<String, Object> exportParam = ExportUtils.getExportParam(exportExcelHeadList, aClass);
            // 每列宽度
            Map<Integer, Integer> columnWidthMap = (Map<Integer, Integer>) exportParam.get(ExportUtils.COLUMN_WIDTH);
            sheet.setColumnWidthMap(columnWidthMap);

            // 获取表格
            com.alibaba.excel.metadata.Table table = ExportUtils.getTable(exportParam);
            // 组装数据
            List<List<String>> userList = new ArrayList<>();
            userList.addAll(ExportUtils.getDataList(dataList, exportParam));
            // 写入
            writer.write0(userList, sheet, table);
            writer.finish();
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.getOutputStream().close();
        }
    }


    public static List<ExportExcelHeadDTO> getExportExcelHeadDTO(String exportExcelHeadListParam) {
        ObjectMapper mapper = new ObjectMapper();
        // mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<ExportExcelHeadDTO> exportExcelHeadList = null;
        try {
            exportExcelHeadList = mapper.readValue(URLDecoder.decode(exportExcelHeadListParam, "UTF-8"), new TypeReference<List<ExportExcelHeadDTO>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exportExcelHeadList;
    }

    /**
     * 将RequestAttributes对象设置为子线程共享
     */
    public static void setRequestAttributesByThread() {
        //将RequestAttributes对象设置为子线程共享
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(sra, true);
    }

    /**
     * 获取导出表格
     *
     * @param exportParam 导出参数
     * @return
     */
    public static com.alibaba.excel.metadata.Table getTable(Map<String, Object> exportParam) {
        com.alibaba.excel.metadata.Table table = new com.alibaba.excel.metadata.Table(1);
        List<List<String>> titles = (List<List<String>>) exportParam.get(TITLE);
        table.setHead(titles);
        return table;
    }

    /**
     * 获取导出参数
     *
     * @param exportExcelHeadList 导出标题集合
     * @param aClass              导出类
     * @return 导出参数
     */
    public static Map<String, Object> getExportParam(List<ExportExcelHeadDTO> exportExcelHeadList, Class aClass) {
        return getExportParam(exportExcelHeadList, aClass, null);
    }

    /**
     * 获取导出参数
     *
     * @param exportExcelHeadList 导出标题集合
     * @param aClass              导出类
     * @param scaleWidth          宽度比例，默认3.5
     * @return 导出参数
     */
    public static Map<String, Object> getExportParam(List<ExportExcelHeadDTO> exportExcelHeadList, Class aClass, Double scaleWidth) {
        scaleWidth = null == scaleWidth ? 3.5 : scaleWidth;
        Map<String, Object> map = Maps.newHashMap();
        // 每列配置
        List<ExcelExportEntity> configList = new ArrayList<>();
        // 每列标题
        List<List<String>> titleList = new ArrayList<>();
        // 每列宽度
        Map<Integer, Integer> columnWidthMap = Maps.newHashMap();

        for (int i = 0; i < exportExcelHeadList.size(); i++) {
            ExportExcelHeadDTO dto = exportExcelHeadList.get(i);
            // 是否显示
            if (dto.getVisible()) {
                // 宽度
                Integer width = new BigDecimal(dto.getWidth().replace("px", "")).intValue();
                width = new BigDecimal(width / scaleWidth).intValue();
                if (width > 255) {
                    width = 254;
                }
                ExcelExportEntity exportEntity = new ExcelExportEntity(dto.getTitle(), dto.getName(), width);
                // 自动换行
                exportEntity.setWrap(true);
                try {
                    // 设置金额转换格式
                    NumberFormat money = aClass.getDeclaredField(dto.getName()).getAnnotation(NumberFormat.class);
                    if (money != null && !EmptyUtils.isBlank(money.pattern())) {
                        exportEntity.setNumFormat(money.pattern());
                    }
                    // 设置日期转换格式
                    JsonFormat jsonFormat = aClass.getDeclaredField(dto.getName()).getAnnotation(JsonFormat.class);
                    if (jsonFormat != null) {
                        exportEntity.setFormat(jsonFormat.pattern());
                    }
                    // 获取get方法
                    String name = StringUtils.firstCharToUpperCase(dto.getName());
                    exportEntity.setMethod(aClass.getMethod("get" + name, new Class[]{}));
                } catch (NoSuchMethodException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                configList.add(exportEntity);
                titleList.add(Collections.singletonList(dto.getTitle()));
                // (实际列宽+列宽基数：0.72) * 256
                width = BigDecimal.valueOf((width + 0.72) * 256).intValue();
                columnWidthMap.put(i, width);
            }
        }
        map.put(CONFIG, configList);
        map.put(TITLE, titleList);
        map.put(COLUMN_WIDTH, columnWidthMap);
        return map;
    }

    /**
     * 根据配置将值转换为日期
     *
     * @param dataList    导出数据集合
     * @param exportParam 导出参数
     * @return 日期
     */
    public static List<List<String>> getDataList(List<?> dataList, Map<String, Object> exportParam) {
        // 配置信息
        List<ExcelExportEntity> configList = (List<ExcelExportEntity>) exportParam.get(CONFIG);
        // 组装数据
        List<List<String>> list = new ArrayList<>();
        try {
            List<String> values;
            Object obj;
            for (Object vo : dataList) {
                values = new ArrayList<>();
                for (ExcelExportEntity excelExportEntity : configList) {
                    obj = excelExportEntity.getMethod().invoke(vo, new Object[0]);
                    if (null != obj) {
                        // 处理日期
                        if (!EmptyUtils.isBlank(excelExportEntity.getFormat())) {
                            obj = dateFormatValue(excelExportEntity, obj.toString());
                        }
                        // 处理金额
                        if (!EmptyUtils.isBlank(excelExportEntity.getNumFormat())) {
                            obj = numFormatValue(excelExportEntity, obj);
                        }
                    }
                    values.add(null == obj ? "" : obj.toString());
                }
                list.add(values);
            }
            return list;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据配置将值转换为日期
     *
     * @param dataList    导出数据集合
     * @param exportParam 导出参数
     * @return 日期
     */
    public static List<List<Object>> getObjDataList(List<?> dataList, Map<String, Object> exportParam) {
        // 配置信息
        List<ExcelExportEntity> configList = (List<ExcelExportEntity>) exportParam.get(CONFIG);
        // 组装数据
        List<List<Object>> list = new ArrayList<>();
        try {
            List<Object> values;
            Object obj;
            for (Object vo : dataList) {
                values = new ArrayList<>();
                for (ExcelExportEntity excelExportEntity : configList) {
                    obj = excelExportEntity.getMethod().invoke(vo, new Object[0]);
                    if (null != obj) {
                        // 处理日期
                        if (!EmptyUtils.isBlank(excelExportEntity.getFormat())) {
                            obj = dateFormatValue(excelExportEntity, obj.toString());
                        }
                        // 处理金额
                        if (!EmptyUtils.isBlank(excelExportEntity.getNumFormat())) {
                            //obj = numFormatValue(excelExportEntity, obj);
                            //金额不做处理，方便判断设置样式
                            if (obj == null) {
                                obj = BigDecimal.ZERO;
                            }
                        }
                    }
                    if (obj instanceof BigDecimal) {
                        values.add(obj);
                    } else {
                        values.add(null == obj ? "" : obj.toString());
                    }
                }
                list.add(values);
            }
            return list;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据配置将值转换为日期
     *
     * @param entity 导入配置
     * @param value  值
     * @return 日期
     */
    public static Object dateFormatValue(ExcelExportEntity entity, String value) {
        if (StringUtils.isNotBlank(entity.getFormat()) && StringUtils.isNotBlank(value)) {
            Date temp = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat(entity.getFormat());
                temp = DateUtil.parse(value);
            } catch (DateException var5) {
                logger.error("value==>" + value);
                throw new ExcelImportException(ExcelImportEnum.FORMAT_VALUE_ERROR);
            }
            if (temp != null) {
                SimpleDateFormat format = new SimpleDateFormat(entity.getFormat());
                return format.format(temp);
            }
            return null;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        String date = "Sat Jul 27 11:29:12 CSsT 2024";
        System.out.println("DateUtil.parse(date) = " + DateUtil.parse(date));
    }

    /**
     * 格式化金额类型
     *
     * @param entity
     * @param value
     * @return
     */
    public static Object numFormatValue(ExcelExportEntity entity, Object value) {
        if (value == null) {
            return null;
        }
        if (!NumberUtils.isNumber(value.toString())) {
            return null;
        }
        BigDecimal d = new BigDecimal(value.toString());
        DecimalFormat df = new DecimalFormat(entity.getNumFormat());
        return df.format(d);
    }


    /**
     * 获取一个初始的样式
     */
    private static HSSFCellStyle initStyle(HSSFWorkbook workbook, HorizontalAlignment align, Boolean locked) {
        HSSFCellStyle style = workbook.createCellStyle();
        // 边框设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 字体居中
        style.setAlignment(align);
        style.setLocked(EmptyUtils.isEmpty(locked) || locked ? true : locked);
        return style;
    }

    /**
     * 获取居中样式
     *
     * @throws Exception
     */
    public static HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook, Boolean locked) {
        return initStyle(workbook, HorizontalAlignment.CENTER, locked);
    }

    /**
     * 获取靠右样式
     *
     * @throws Exception
     */
    public static HSSFCellStyle getRightCellStyle(HSSFWorkbook workbook, Boolean locked) {
        return initStyle(workbook, HorizontalAlignment.RIGHT, locked);
    }

    /**
     * 获取靠左样式
     *
     * @throws Exception
     */
    public static HSSFCellStyle getLeftCellStyle(HSSFWorkbook workbook, Boolean locked) {
        return initStyle(workbook, HorizontalAlignment.LEFT, locked);
    }

    /**
     * 获取靠左样式
     *
     * @throws Exception
     */
    public static HSSFCellStyle getHeaderCellStyle(HSSFWorkbook workbook, boolean b) {
        HSSFCellStyle hssfCellStyle = initStyle(workbook, HorizontalAlignment.CENTER, b);
        hssfCellStyle.setFont(initFont(workbook));
        return hssfCellStyle;
    }

    private static Font initFont(HSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        return font;
    }

    /**
     * 获取字体样式
     */
    public static HSSFFont getHeadFont(HSSFWorkbook workbook) {
        HSSFFont headFont = workbook.createFont();
        headFont.setFontName("宋体");
        //字体大小
        headFont.setFontHeightInPoints((short)14);
        headFont.setBold(true);
        return headFont;
    }

    /**
     * 获取字体样式
     */
    public static HSSFFont getHeadFont(HSSFWorkbook workbook, String fontName, short height) {
        HSSFFont headFont = workbook.createFont();
        headFont.setFontName(fontName);
        //字体大小
        headFont.setFontHeightInPoints(height);
        headFont.setBold(true);
        return headFont;
    }

    /**
     * 获取字体样式
     */
    public static HSSFFont getHeadFont(HSSFWorkbook workbook, String fontName, short height, short color) {
        HSSFFont headFont = workbook.createFont();
        headFont.setFontName(fontName);
        //字体大小
        headFont.setFontHeightInPoints(height);
        headFont.setBold(true);
        // 设置字体颜色
        headFont.setColor(color);
        return headFont;
    }

    /**
     * 获取居中锁定背景色样式
     *
     * @throws Exception
     */
    public static HSSFCellStyle getCenterLockCellStyle(HSSFWorkbook workbook) {
        // 标题样式
        HSSFCellStyle style = initStyle(workbook, HorizontalAlignment.CENTER, true);
        // 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.GOLD.index);
        style.setWrapText(true);
        return style;
    }


    /**
     * 获取靠右锁定背景色样式
     *
     * @throws Exception
     */
    public static HSSFCellStyle getRightLockCellStyle(HSSFWorkbook workbook) {
        // 标题样式
        HSSFCellStyle style = initStyle(workbook, HorizontalAlignment.RIGHT, true);
        // 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        return style;
    }

    /**
     * 导出文件名转码
     *
     * @param request
     * @param fileName
     * @return
     */
    public static String getFileName(HttpServletRequest request, String fileName) {
        String codedFileName = fileName;
        try {
            if (ServletUtils.isIE(request)) {
                codedFileName = URLEncoder.encode(codedFileName, "UTF8");
            } else {
                codedFileName = new String(codedFileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedFileName;
    }

    public static String getFileName(boolean isIE, String fileName) {
        String codedFileName = fileName;
        try {
            codedFileName = isIE ? URLEncoder.encode(codedFileName, "UTF8") :
                    new String(codedFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            logger.error("文件名异常:", e);
        }
        return codedFileName;
    }

    /**
     * 设置合并单元格的边框
     *
     * @param sheet
     * @param region
     * @param cs
     */
    public static void setRegionStyle(HSSFSheet sheet, CellRangeAddress region, HSSFCellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (region.getFirstColumn() != region.getLastColumn()) {
                for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                    HSSFCell cell = row.getCell((short) j);
                    cell.setCellStyle(cs);
                }
            }
        }
    }

    /**
     * excel生成
     * @param response
     * @param fileName 导出文件名
     * @param titles 标题头 按照标题顺序渲染表格
     * @param datas 数据
     */
    public static void export(boolean isIE,HttpServletResponse response, String fileName, LinkedMap<String,String> titles, List<Map<String, Object>> datas, int rowIndex,String mergeId) {
        try (OutputStream os = response.getOutputStream();HSSFWorkbook workbook = new HSSFWorkbook()){
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download;");
            response.setHeader("content-disposition", "attachment;filename=" + getFileName(isIE, fileName) + ".xls");
            HSSFSheet sheet = workbook.createSheet();
            //设置标题头
            setTitle(sheet, new ArrayList<>(titles.values()), rowIndex++);
            //设置数据
            setData(sheet, datas, new ArrayList<>(titles.keySet()), rowIndex,mergeId);
            workbook.write(os);
        } catch (Exception e) {
            logger.error("导出异常", e);
        }
    }

    /**
     * excel生成
     * @param response
     * @param fileName 导出文件名
     * @param titles 标题头 按照标题顺序渲染表格
     * @param datas 数据
     * @param mergeId 合并ID todo 合并目前还需要完善
     */
    public static void export(boolean isIE,HttpServletResponse response, String fileName, LinkedMap<String,String> titles, List<Map<String, Object>> datas,String mergeId) {
        export(isIE,response, fileName, titles, datas, 1, mergeId);
    }

    private static void setTitle(HSSFSheet sheet, List<String> titles, int rowIndex) {
        HSSFRow row = sheet.createRow(rowIndex);
        for (int i = 0; i < titles.size(); i++) {
            HSSFCell cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(titles.get(i));
        }
    }

    private static void setData(HSSFSheet sheet, List<Map<String, Object>> datas, List<String> titleKeys, int rowIndex,String mergeId) {
        int mergeStart = rowIndex;
        String lastMergeId = "";
        String currentId = "";
        for (Map<String, Object> data : datas) {
            if (StringUtils.isNotBlank(mergeId)) {
                currentId = (String) data.get(mergeId);
                if (!lastMergeId.equals(currentId)) {
                    //合并并且重新初始化   excel合并区间[,] 所以要-1
                    if (rowIndex - 1 > mergeStart) {
                        for (int i = 0; i < 8; i++) {
                            sheet.addMergedRegion(new CellRangeAddress(mergeStart, rowIndex - 1, i, i));
                        }
                    }
                    mergeStart = rowIndex;
                }
            }
            HSSFRow row = sheet.createRow(rowIndex++);
            int index = 0;
            for (String titleKey : titleKeys) {
                HSSFCell cell = row.createCell(index++, CellType.STRING);
                Object value = data.get(titleKey);
                if (value == null) {
                    cell.setCellValue("");
                    continue;
                }
                if (value instanceof Date) {
                    cell.setCellValue(DateUtils.formatDate((Date)value));
                }else {
                    cell.setCellValue(String.valueOf(value));
                }
            }
            lastMergeId = currentId;

        }


    }
}
