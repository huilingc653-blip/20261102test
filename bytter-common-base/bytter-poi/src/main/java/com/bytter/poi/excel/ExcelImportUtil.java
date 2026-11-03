package com.bytter.poi.excel;
import com.bytter.poi.excel.entity.ImportParams;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.excel.entity.result.ExcelImportResult;
import com.bytter.poi.excel.imports.ExcelImportServer;
import com.bytter.poi.excel.imports.TxtImportServer;
import com.bytter.poi.exception.excel.ExcelImportException;
import com.bytter.poi.exception.excel.enums.ExcelImportEnum;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
/**
 * Excel导入工具栏
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Slf4j
public final class ExcelImportUtil {
    private ExcelImportUtil() {
    }
    /**
     * 导入Excel
     *
     * @param file
     *            导入文件
     * @param pojoClass
     *            导入转换对象
     * @param params
     *            导入参数
     * @param <T>
     *            泛型
     * @return 导入集合
     */
    public static <T> List<T> importExcel(File file, Class<?> pojoClass, ImportParams params) {
        FileInputStream in = null;
        List result = null;
        try {
            in = new FileInputStream(file);
            result = (new ExcelImportServer()).importExcelByIs(in, pojoClass, params).getList();
        } catch (Exception var14) {
            log.error(var14.getMessage(), var14);
        } finally {
            try {
                in.close();
            } catch (IOException var13) {
                log.error(var13.getMessage(), var13);
            }
        }
        return result;
    }
    /**
     * 导入Excel
     *
     * @param inputStream
     *            导入流
     * @param pojoClass
     *            导入转换对象
     * @param params
     *            导入参数
     * @param <T>
     *            泛型
     * @return 导入集合
     * @throws Exception
     */
    public static <T> List<T> importExcel(InputStream inputStream, Class<?> pojoClass, ImportParams params)
            throws Exception {
        return (new ExcelImportServer()).importExcelByIs(new BufferedInputStream(inputStream), pojoClass, params)
                .getList();
    }
    /**
     * 导入Excel
     *
     * @param inputStream
     *            导入流
     * @param pojoClass
     *            导入转换对象
     * @param params
     *            导入参数
     * @param <T>
     *            泛型
     * @return {@link ExcelImportResult}
     * @throws Exception
     */
    public static <T> ExcelImportResult<T> importExcelVerify(InputStream inputStream, Class<?> pojoClass,
            ImportParams params) throws Exception {
        return (new ExcelImportServer()).importExcelByIs(new BufferedInputStream(inputStream), pojoClass, params);
    }
    /**
     * Excel column index begin 1
     *
     * @param colStr
     *            列
     * @param length
     *            长度
     * @return index
     */
    public static int excelColStrToNum(String colStr, int length) {
        int num = 0;
        int result = 0;
        for (int i = 0; i < length; i++) {
            char ch = colStr.charAt(length - i - 1);
            num = (int) (ch - 'A' + 1);
            num *= Math.pow(26, i);
            result += num;
        }
        return result;
    }
    /**
     * Excel column index begin 1
     *
     * @param columnIndex
     *            列索引
     * @return index
     */
    public static String excelColIndexToStr(int columnIndex) {
        if (columnIndex <= 0) {
            return null;
        }
        String columnStr = "";
        columnIndex--;
        do {
            if (columnStr.length() > 0) {
                columnIndex--;
            }
            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
        } while (columnIndex > 0);
        return columnStr;
    }
    /**
     * 读取文件的编码格式
     *
     * @param inputStream
     *            文件流
     * @return 编码格式
     * @throws Exception
     */
    public static String getCharsetName(InputStream inputStream) throws Exception {
        byte[] head = new byte[3];
        inputStream.read(head);
        // 或GBK
        String code = "GB2312";
        if (head[0] == -1 && head[1] == -2) {
            code = "UTF-16";
        } else if (head[0] == -2 && head[1] == -1) {
            code = "Unicode";
        } else if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
            code = "UTF-8";
        }
        inputStream.close();
        return code;
    }
    /**
     * 将Txt导入文件转换为Excel流
     *
     * @param inputStream
     *            导入流
     * @param importCustomRules
     *            自定义导入规则
     * @param params
     *            导入参数
     * @return Excel流
     * @throws Exception
     */
    public static InputStream txtInputStreamConvertExcelInputStream(InputStream inputStream,
            ImportCustomRules importCustomRules, ImportParams params) throws Exception {
        return new TxtImportServer().txtInputStreamConvertExcelInputStream(inputStream, importCustomRules, params);
    }
    /**
     * 格式转字符串
     *
     * @param str
     *            日期字符串
     * @param pattern
     *            格式
     * @return 转换后日期
     */
    public static String parseFormat(String str, String pattern) {
        SimpleDateFormat newSdf = new SimpleDateFormat(pattern);
        // 判断是否为时间戳
        if (str.length() > "yyyyMMdd".length() && isValidLong(str)) {
            try {
                long ts = Long.parseLong(str);
                Date date = new Date();
                date.setTime(ts);
                return newSdf.format(date);
            } catch (Exception e) {
                log.error("时间格式化失败,格式化:" + pattern + ",值:" + str);
                throw new ExcelImportException(ExcelImportEnum.FORMAT_VALUE_ERROR);
            }
        }
        try {
            String dateFormat = getDateFormat(str);
            // 格式转换
            SimpleDateFormat sdf = null;
            if (null != dateFormat && !"".equalsIgnoreCase(dateFormat)) {
                sdf = new SimpleDateFormat(dateFormat);
            }
            Date date = sdf.parse(str);
            return newSdf.format(date);
        } catch (ParseException e) {
            log.error("时间格式化失败,格式化:" + pattern + ",值:" + str);
            throw new ExcelImportException(ExcelImportEnum.FORMAT_VALUE_ERROR);
        } catch (NullPointerException e) {
            log.error("时间格式化失败,不支持" + str + "格式化");
            throw new ExcelImportException(ExcelImportEnum.FORMAT_VALUE_ERROR);
        }
    }
    /**
     * 字符串是否可以转换为Long类型
     *
     * @param str
     *            字符串
     * @return Boolean
     */
    public static boolean isValidLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * 常规自动日期格式识别
     *
     * @param str
     *            时间字符串
     * @return Date字符串
     */
    private static String getDateFormat(String str) {
        String dateFormat = "";
        if ("yyyyMMdd".length() == str.length() || "yyyyMdd".length() == str.length()) {
            dateFormat = "yyyyMMdd";
        } else if (str.indexOf("-") == 2 || str.indexOf("-") == 1) {
            dateFormat = "dd-MM-yyyy";
        } else if (str.indexOf("/") == 2 || str.indexOf("/") == 1) {
            dateFormat = "dd/MM/yyyy";
        }
        if (!"".equalsIgnoreCase(dateFormat)) {
            return dateFormat;
        }
        boolean year = false;
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if (pattern.matcher(str.substring(0, 4)).matches()) {
            year = true;
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        if (!year) {
            if (str.contains("月") || str.contains("-") || str.contains("/")) {
                if (Character.isDigit(str.charAt(0))) {
                    index = 1;
                }
            } else {
                index = 3;
            }
        }
        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            if (Character.isDigit(chr)) {
                if (index == 0) {
                    sb.append("y");
                }
                if (index == 1) {
                    sb.append("M");
                }
                if (index == 2) {
                    sb.append("d");
                }
                if (index == 3) {
                    sb.append("H");
                }
                if (index == 4) {
                    sb.append("m");
                }
                if (index == 5) {
                    sb.append("s");
                }
                if (index == 6) {
                    sb.append("S");
                }
            } else {
                if (i > 0) {
                    char lastChar = str.charAt(i - 1);
                    if (Character.isDigit(lastChar)) {
                        index++;
                    }
                }
                sb.append(chr);
            }
        }
        return sb.toString();
    }
}
