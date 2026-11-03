package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.vo.FexCorpExposureTemplateVO;
import com.bytter.biz.foreign.vo.FexExposureTemplateItemVO;
import com.bytter.biz.foreign.vo.FexExposureTemplateVO;
import com.bytter.biz.foreign.vo.FexExposureVO;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.ServletUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.IBtCurrencyService;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("fexExposureDownloadAndImportService")
@Transactional
public class FexExposureDownloadAndImportService<excelImportServiceImpl> {

    @Autowired
    private FexExposureService fexExposureService;

    @Autowired
    private FexCorpExposureTemplateService fexCorpExposureTemplateService;

    @Autowired
    private FexExposureTemplateService fexExposureTemplateService;

    @Autowired(required = false)
    private IBtCurrencyService currencyService;

    public File getFile(String filePath) throws Exception {
        File file = new File(filePath);
        File fileParent = file.getParentFile();
        // 推断文件夹或文件是否存在
        if (!fileParent.exists()) {
            // 不存在返回 false
            fileParent.mkdirs();
        } else {
            // 推断是否为文件
            if (fileParent.isFile()) {
                // 为文件时调用删除文件方法
                fileParent.delete();
            }
        }
        file.createNewFile();
        return file;
    }

    /**
     * 获取居中样式
     *
     * @throws Exception
     */
    public XSSFCellStyle getCenterCellStyle(XSSFWorkbook workbook) {
        return initStyle(workbook, HorizontalAlignment.CENTER);
    }

    /**
     * 获取靠右样式
     *
     * @throws Exception
     */
    public XSSFCellStyle getRightCellStyle(XSSFWorkbook workbook) {
        return initStyle(workbook, HorizontalAlignment.RIGHT);
    }

    /**
     * 获取靠左样式
     *
     * @throws Exception
     */
    public XSSFCellStyle getLeftCellStyle(XSSFWorkbook workbook) {
        return initStyle(workbook, HorizontalAlignment.LEFT);
    }

    /**
     * 获取靠右样式
     *
     * @throws Exception
     */
    public XSSFCellStyle getRightCellStyle(XSSFWorkbook workbook, Boolean locked) {
        return initStyle(workbook, HorizontalAlignment.RIGHT, locked);
    }

    /**
     * 获取靠左样式
     *
     * @throws Exception
     */
    public XSSFCellStyle getLeftCellStyle(XSSFWorkbook workbook, Boolean locked) {
        return initStyle(workbook, HorizontalAlignment.LEFT, locked);
    }

    /**
     * 获取一个初始的样式
     */
    public XSSFCellStyle initStyle(XSSFWorkbook workbook, HorizontalAlignment align, Boolean locked) {
        XSSFCellStyle style = workbook.createCellStyle();
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
     * 获取一个初始的样式
     */
    public XSSFCellStyle initStyle(XSSFWorkbook workbook, HorizontalAlignment align) {
        XSSFCellStyle style = workbook.createCellStyle();
        // 边框设置
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 字体居中
        style.setAlignment(align);
        style.setLocked(false);
        return style;
    }

    /**
     * 导出
     *
     * @param request
     * @param response
     * @param fileName
     * @param filePath
     */

    public void exports(HttpServletRequest request, HttpServletResponse response, String fileName, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                if (ServletUtils.isIE(request)) {
                    fileName = URLEncoder.encode(fileName, "UTF8");
                } else {
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                }
            } catch (UnsupportedEncodingException e) {
                fileName = fileName;
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download;");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int length;
                while ((length = bis.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } catch (Exception e) {
                throw new BusinessException(e);
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 下载敞口上报模板
     *
     * @param request
     * @param response
     * @param corpId
     * @param templateId
     * @param curId
     */
    public void downloadExposure(HttpServletRequest request, HttpServletResponse response, String corpId, String templateId, String curId) {
        String fileName = "敞口上报";
        String codedFileName = getCodedFileName(request, fileName);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/force-download;");
        response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            // 声明一个工作薄
            XSSFWorkbook workbook = new XSSFWorkbook();
            // 生成一个表格
            XSSFSheet sheet = workbook.createSheet(fileName);
            // 锁定表格
            sheet.protectSheet("edit");
            // 设置表格默认列宽度为20个字节
            sheet.setDefaultColumnWidth(20);
            // 样式
            XSSFCellStyle centerLockCellStyle = this.getCenterCellStyle(workbook);
            XSSFCellStyle leftLockCellStyle = this.getLeftCellStyle(workbook);
            XSSFCellStyle rightLockCellStyle = this.getRightCellStyle(workbook);

            
            //获取单位模板币种信息
            FexCorpExposureTemplateVO fexCorpExposureTemplateVO = fexCorpExposureTemplateService.getByCorpIdAndTemplateId(corpId, templateId);
            //获取模板详情
            FexExposureTemplateVO fexExposureTemplate = fexExposureTemplateService.getByIdUnion(templateId);
            if (CommonConstant.NO.equals(curId)) {
                curId = fexCorpExposureTemplateVO.getCurId();
            }
            String[] curIds = curId.split(CommonConstant.COMMA_SPLIT);
            List<FexExposureTemplateItemVO> listItem = fexExposureTemplate.getListItem();
            HashMap<BtCurrencyDTO, List<FexExposureTemplateItemVO>> dataMap = new HashMap<>();
            for (String cur:curIds) {
                BtCurrencyDTO currencyDTO = currencyService.getObjectById(cur);
                dataMap.put(currencyDTO, listItem);
            }
            LinkedMap<String, FexExposureVO> periodMap = fexExposureService.getExposurePeriod(fexExposureTemplate);

            int index = 0;
            for (BtCurrencyDTO curData:dataMap.keySet()) {
                // 币种
                XSSFRow rowCur = sheet.createRow(index);
                XSSFCell cellCur = rowCur.createCell(0);
                cellCur.setCellValue("币种："+curData.getEnglishCode());
                cellCur.setCellStyle(leftLockCellStyle);
                index = index + 1;

                // 组装表格标题头
                List<String> headList = new ArrayList<>();
                headList.add("序号");
                headList.add("科目编号");
                headList.add("科目名称");
                for (String period:periodMap.keySet()) {
                    headList.add(period);
                }
                // 标题
                XSSFRow rowHead = sheet.createRow(index);
                for (int i = 0; i < headList.size(); i++) {
                    XSSFCell cellHead = rowHead.createCell(i);
                    cellHead.setCellValue(headList.get(i));
                    cellHead.setCellStyle(centerLockCellStyle);
                }
                index = index + 1;
                int no = 1;
                for (FexExposureTemplateItemVO data:dataMap.get(curData)) {
                    int cellNo = 0;
                    XSSFRow row = sheet.createRow(index);
                    // 序号
                    XSSFCell cell = row.createCell(cellNo++);
                    cell.setCellStyle(centerLockCellStyle);
                    cell.setCellValue(no++);
                    // 科目编号
                    XSSFCell cell1 = row.createCell(cellNo++);
                    cell1.setCellStyle(leftLockCellStyle);
                    cell1.setCellValue(data.getItemCode());
                    // 科目名称
                    XSSFCell cell2 = row.createCell(cellNo++);
                    cell2.setCellStyle(leftLockCellStyle);
                    cell2.setCellValue(data.getItemName());
                    for (String period:periodMap.keySet()) {
                        XSSFCell cellPeriod = row.createCell(cellNo++);
                        cellPeriod.setCellStyle(rightLockCellStyle);
                        cellPeriod.setCellValue(NumberUtil.formatNumeric(BigDecimal.ZERO));
                    }
                    index++;
                }
            }
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件名
     *
     * @param request
     * @param fileName
     * @return
     */
    public String getCodedFileName(HttpServletRequest request, String fileName) {
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
}
