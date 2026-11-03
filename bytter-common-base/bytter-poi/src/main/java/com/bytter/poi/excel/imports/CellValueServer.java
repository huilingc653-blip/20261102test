package com.bytter.poi.excel.imports;
import com.bytter.poi.excel.ExcelImportUtil;
import com.bytter.poi.excel.entity.params.ExcelImportEntity;
import com.bytter.poi.excel.entity.sax.SaxReadCellEntity;
import com.bytter.poi.excel.entity.vo.TemplateExcelConstants;
import com.bytter.poi.exception.excel.ExcelImportException;
import com.bytter.poi.exception.excel.enums.ExcelImportEnum;
import com.bytter.poi.handler.inter.IExcelDataHandler;
import com.bytter.poi.handler.inter.IExcelModel;
import com.bytter.poi.util.PoiPublicUtil;
import com.bytter.poi.util.StringPoiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Excel列值处理类
 *
 * @author zhouj
 * @since 2017/6/2.
 */
@Slf4j
public class CellValueServer {
    private List<String> handlerList = null;
    public CellValueServer() {
    }
    /**
     * 获取Excel列值
     *
     * @param xClass
     *            类型
     * @param cell
     *            列
     * @param entity
     *            导入配置
     * @return 值
     */
    private Object getCellValue(String xClass, Cell cell, ExcelImportEntity entity) {
        if (cell == null) {
            return "";
        } else {
            Object result = null;
            if ("class java.util.Date".equals(xClass) || "class java.sql.Date".equals(xClass)
                    || ("class java.sql.Time").equals(xClass) || ("class java.sql.Timestamp").equals(xClass)) {
                if (Cell.CELL_TYPE_NUMERIC == cell.getCellType() && DateUtil.isCellDateFormatted(cell)) {
                    result = DateUtil.getJavaDate(cell.getNumericCellValue());
                } else {
                    result = getDateData(entity, cell.getStringCellValue());
                }
                if (("class java.sql.Date").equals(xClass)) {
                    result = new java.sql.Date(((Date) result).getTime());
                }
                if (("class java.sql.Time").equals(xClass)) {
                    result = new Time(((Date) result).getTime());
                }
                if (("class java.sql.Timestamp").equals(xClass)) {
                    result = new Timestamp(((Date) result).getTime());
                }
            } else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType() && DateUtil.isCellDateFormatted(cell)) {
                result = DateUtil.getJavaDate(cell.getNumericCellValue());
            } else {
                switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getRichStringCellValue() == null ? "" : cell.getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        if ("class java.lang.String".equals(xClass)) {
                            result = formatDate(entity, cell.getDateCellValue());
                        }
                    } else {
                        result = readNumericCell(cell);
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        result = readNumericCell(cell);
                    } catch (Exception e1) {
                        try {
                            result = cell.getRichStringCellValue() == null ? ""
                                    : cell.getRichStringCellValue().getString();
                        } catch (Exception e2) {
                            throw new RuntimeException("获取公式类型的单元格失败", e2);
                        }
                    }
                    break;
                default:
                    break;
                }
            }
            return result;
        }
    }
    /**
     * 读取数值类型的单元格
     *
     * @param cell
     *            单元格
     * @return 值
     */
    private Object readNumericCell(Cell cell) {
        Object result = null;
        double value = cell.getNumericCellValue();
        if (((int) value) == value) {
            result = (int) value;
        } else {
            result = value;
        }
        return result;
    }
    /**
     * 根据配置将值转换为日期
     *
     * @param entity
     *            导入配置
     * @param value
     *            值
     * @return 日期
     */
    private Date getDateData(ExcelImportEntity entity, String value) {
        if (StringPoiUtils.isNotEmpty(entity.getFormat()) && StringPoiUtils.isNotEmpty(value)) {
            SimpleDateFormat format = new SimpleDateFormat(entity.getFormat());
            try {
                if (entity.isTryTransferDate()) {
                    value = ExcelImportUtil.parseFormat(value, entity.getFormat());
                }
                return format.parse(value);
            } catch (ParseException var5) {
                log.error("时间格式化失败,格式化:" + entity.getFormat() + ",值:" + value, var5);
                throw new ExcelImportException(ExcelImportEnum.FORMAT_VALUE_ERROR);
            }
        } else {
            boolean b = false;
            if(StringPoiUtils.isNotEmpty(value)){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return format.parse(value);
                } catch (ParseException var5) {
                    b = true;
                }
                if(b){
                    format = new SimpleDateFormat("yyyyMMdd");
                    try {
                        return format.parse(value);
                    } catch (ParseException var5) {
                    }
                }
                if(b){
                    format = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        return format.parse(value);
                    } catch (ParseException var5) {
                    }
                }
                if(b){
                    format = new SimpleDateFormat("yyyy:MM:dd");
                    try {
                        return format.parse(value);
                    } catch (ParseException var5) {
                    }
                }
                if(b){
                    format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        return format.parse(value);
                    } catch (ParseException var5) {
                        log.error("时间格式化失败,格式化:" + entity.getFormat() + ",值:" + value, var5);
                        throw new ExcelImportException(ExcelImportEnum.FORMAT_VALUE_ERROR);
                    }
                }
            }
            return null;
        }
    }
    /**
     * 格式化日期
     *
     * @param entity
     *            导入配置
     * @param value
     *            日期
     * @return 日期字符串
     */
    private String formatDate(ExcelImportEntity entity, Date value) {
        if (StringPoiUtils.isNotEmpty(entity.getFormat()) && value != null) {
            SimpleDateFormat format = new SimpleDateFormat(entity.getFormat());
            return format.format(value);
        }
        return null;
    }
    /**
     * 获取单元格的值
     *
     * @param dataHandler
     *            接口
     * @param object
     *            导入对象
     * @param cell
     *            单元格
     * @param excelParams
     *            Excel参数
     * @param titleString
     *            标题字段
     * @return 单元格的值
     * @throws Exception
     */
    public Object getValue(IExcelDataHandler dataHandler, Object object, Cell cell,
            Map<String, ExcelImportEntity> excelParams, String titleString) throws Exception {
        ExcelImportEntity entity = excelParams.get(titleString);
        String xclass = "class java.lang.Object";
        if (!(object instanceof Map)) {
            Method result = entity.getMethods() != null && entity.getMethods().size() > 0
                    ? (Method) entity.getMethods().get(entity.getMethods().size() - 1) : entity.getMethod();
            Type[] ts = result.getGenericParameterTypes();
            xclass = ts[0].toString();
        }
        try {
            Object result1 = this.getCellValue(xclass, cell, entity);
            if (entity != null) {
                result1 = this.handlerSuffix(entity.getSuffix(), result1);
                result1 = this.numFormatValue(entity.getNumFormat(), result1);
                result1 = this.replaceValue(entity.getReplace(), result1);
            }
            result1 = this.handlerValue(dataHandler, object, result1, titleString);
            return this.getValueByType(xclass, result1);
        } catch (ExcelImportException e1) {
            if (object instanceof IExcelModel) {
                IExcelModel model = (IExcelModel) object;
                if(model.getErrorMsg() != null && model.getErrorMsg().length() > 0){
                    model.setErrorMsg(model.getErrorMsg() + "，【" + titleString.replace("*", "") + "】" + e1.getMessage());
                }else {
                    model.setErrorMsg("【" + titleString.replace("*", "") + "】" + e1.getMessage());
                }
                //保留表格原值，用于替换，展示原值在页面
                try {
                    String nm = entity.getMethod().getName()+ TemplateExcelConstants.FORMATERRORSUFFIX;
                    Method method = object.getClass().getMethod(nm, String.class);
                    method.invoke(object,cell.getStringCellValue());
                }catch (Exception exception){}
            }
            return null;
        }
    }
    /**
     * 获取单元格的值
     *
     * @param dataHandler
     *            接口
     * @param object
     *            导入对象
     * @param cellEntity
     *            单元格
     * @param excelParams
     *            Excel参数
     * @param titleString
     *            标题字段
     * @return 单元格的值
     * @throws Exception
     */
    public Object getValue(IExcelDataHandler dataHandler, Object object, SaxReadCellEntity cellEntity,
            Map<String, ExcelImportEntity> excelParams, String titleString) {
        ExcelImportEntity entity = excelParams.get(titleString);
        Method setMethod = entity.getMethods() != null && entity.getMethods().size() > 0
                ? (Method) entity.getMethods().get(entity.getMethods().size() - 1) : entity.getMethod();
        Type[] ts = setMethod.getGenericParameterTypes();
        String xclass = ts[0].toString();
        Object result = cellEntity.getValue();
        try {
            result = this.handlerSuffix(entity.getSuffix(), result);
            result = this.numFormatValue(entity.getNumFormat(), result);
            result = this.replaceValue(entity.getReplace(), result);
            result = this.handlerValue(dataHandler, object, result, titleString);
            return this.getValueByType(xclass, result);
        } catch (ExcelImportException e1) {
            if (object instanceof IExcelModel) {
                IExcelModel model = (IExcelModel) object;
                model.setErrorMsg("【" + titleString.replace("*", "") + "】" + e1.getMessage());
            }
            return null;
        }
    }
    /**
     * 将对象去除后缀
     *
     * @param suffix
     *            后缀
     * @param obj
     *            对象
     * @return 处理后的对象
     */
    private Object handlerSuffix(String suffix, Object obj) {
        if (StringPoiUtils.isNotEmpty(suffix) && obj != null && obj.toString().endsWith(suffix)) {
            String temp = obj.toString();
            return temp.substring(0, temp.length() - suffix.length());
        } else {
            return obj;
        }
    }
    /**
     * 根据类型转换值
     *
     * @param xClass
     *            类型
     * @param result
     *            对象
     * @return 转换后的值
     */
    private Object getValueByType(String xClass, Object result) {
        try {
            if (result == null || StringPoiUtils.isBlank(result.toString())) {
                return null;
            }
            if ("class java.util.Date".equals(xClass)) {
                return result;
            }
            if ("class java.lang.Boolean".equals(xClass) || "boolean".equals(xClass)) {
                return Boolean.valueOf(String.valueOf(result));
            }
            if ("class java.lang.Double".equals(xClass) || "double".equals(xClass)) {
                return Double.valueOf(String.valueOf(result));
            }
            if ("class java.lang.Long".equals(xClass) || "long".equals(xClass)) {
                try {
                    return Long.valueOf(String.valueOf(result));
                } catch (Exception e) {
                    // 格式错误的时候,就用double,然后获取Int值
                    return Double.valueOf(String.valueOf(result)).longValue();
                }
            }
            if ("class java.lang.Float".equals(xClass) || "float".equals(xClass)) {
                return Float.valueOf(String.valueOf(result));
            }
            if ("class java.lang.Integer".equals(xClass) || "int".equals(xClass)) {
                try {
                    return Integer.valueOf(String.valueOf(result));
                } catch (Exception e) {
                    // 格式错误的时候,就用double,然后获取Int值
                    return Double.valueOf(String.valueOf(result)).intValue();
                }
            }
            if ("class java.math.BigDecimal".equals(xClass)) {
                return new BigDecimal(String.valueOf(result));
            }
            if ("class java.lang.String".equals(xClass)) {
                // 针对String 类型,但是Excel获取的数据却不是String,比如Double类型,防止科学计数法
                if (result instanceof String) {
                    return result;
                }
                // double类型防止科学计数法
                if (result instanceof Double) {
                    return PoiPublicUtil.doubleToString((Double) result);
                }
                return String.valueOf(result);
            }
            return result;
        } catch (Exception e) {
            // return null;2017年6月16日 16:51:57 zhouj修复数据格式异常返回null处理
            // throw new ExcelImportException(ExcelImportEnum.GET_VALUE_ERROR);
            throw new ExcelImportException(ExcelImportEnum.FORMAT_VALUE_ERROR);
        }
    }
    /**
     * 处理单元格的值
     *
     * @param dataHandler
     *            接口处理类
     * @param object
     *            导入对象
     * @param result
     *            单元格值
     * @param titleString
     *            标题字段
     * @return 处理后的单元格值
     */
    private Object handlerValue(IExcelDataHandler dataHandler, Object object, Object result, String titleString) {
        if (dataHandler != null && dataHandler.getNeedHandlerFields() != null
                && dataHandler.getNeedHandlerFields().length != 0) {
            if (this.handlerList == null) {
                this.handlerList = Arrays.asList(dataHandler.getNeedHandlerFields());
            }
            return this.handlerList.contains(titleString) ? dataHandler.importHandler(object, titleString, result)
                    : result;
        } else {
            return result;
        }
    }
    /**
     * 替换单元格值
     *
     * @param replace
     *            替换集合
     * @param result
     *            单元格值
     * @return 替换后的值
     */
    private Object replaceValue(String[] replace, Object result) {
        if (replace != null && replace.length > 0) {
            String temp = String.valueOf(result);
            for (int i = 0; i < replace.length; ++i) {
                String[] tempArr = replace[i].split("_");
                if (temp.equals(tempArr[0])) {
                    return tempArr[1];
                }
            }
        }
        return result;
    }
    /**
     * 格式化数值
     *
     * @param format
     *            格式
     * @param result
     *            单元格值
     * @return 格式化后的数值
     */
    private Object numFormatValue(String format, Object result) {
        if (result == null || StringPoiUtils.isBlank(String.valueOf(result))) {
            return null;
        }
        if (StringPoiUtils.isEmpty(format)) {
            return result;
        }
        String temp = String.valueOf(result);
        if (!NumberUtils.isNumber(temp)) {
            log.error("data want num format ,but is not num, value is:" + format);
            // return null;
            throw new ExcelImportException(ExcelImportEnum.FORMAT_VALUE_ERROR);
        }
        Double d = Double.parseDouble(temp);
        DecimalFormat df = new DecimalFormat(format);
        return df.format(d);
    }
}
