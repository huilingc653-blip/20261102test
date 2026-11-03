package com.bytter.poi.excel.export.base;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelCollection;
import com.bytter.poi.annotation.ExcelEntity;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.params.ExcelExportEntity;
import com.bytter.poi.excel.entity.vo.PoiBaseConstants;
import com.bytter.poi.handler.inter.IExcelDataHandler;
import com.bytter.poi.util.PoiPublicUtil;
import com.bytter.poi.util.PoiReflectorUtil;
import com.bytter.poi.util.StringPoiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Created by zhouj on 2017/6/2.
 */
@Slf4j
public class ExportBase {
    protected IExcelDataHandler dataHanlder;
    protected List<String> needHanlderList;
    public ExportBase() {
    }
    private ExcelExportEntity createExcelExportEntity(Field field, String targetId, Class<?> pojoClass,
            List<Method> getMethods) throws Exception {
        Excel excel = field.getAnnotation(Excel.class);
        ExcelExportEntity excelEntity = new ExcelExportEntity();
        excelEntity.setType(excel.type());
        this.getExcelField(targetId, field, excelEntity, excel, pojoClass);
        if (getMethods != null) {
            ArrayList newMethods = new ArrayList();
            newMethods.addAll(getMethods);
            newMethods.add(excelEntity.getMethod());
            excelEntity.setMethods(newMethods);
        }
        return excelEntity;
    }
    private Object dateFormatValue(Object value, ExcelExportEntity entity) throws Exception {
        Date temp = null;
        if (value instanceof String) {
            SimpleDateFormat format = new SimpleDateFormat(entity.getDatabaseFormat());
            temp = format.parse(value.toString());
        } else if (value instanceof Date) {
            temp = (Date) value;
        } else if (value instanceof java.sql.Date) {
            temp = new Date(((java.sql.Date) value).getTime());
        } else if (value instanceof java.sql.Time) {
            temp = new Date(((java.sql.Time) value).getTime());
        } else if (value instanceof java.sql.Timestamp) {
            temp = new Date(((java.sql.Timestamp) value).getTime());
        }
        if (temp != null) {
            SimpleDateFormat format = new SimpleDateFormat(entity.getFormat());
            value = format.format(temp);
        }
        return value;
    }
    private Object numFormatValue(Object value, ExcelExportEntity entity) {
        if (value == null) {
            return null;
        }
        if (!NumberUtils.isNumber(value.toString())) {
            log.error("data want num format ,but is not num, value is:" + value);
            return null;
        }
        Double d = Double.parseDouble(value.toString());
        DecimalFormat df = new DecimalFormat(entity.getNumFormat());
        return df.format(d);
    }
    /*
     * private Object formatValue(Object value, ExcelExportEntity entity) throws Exception { Date temp = null;
     * SimpleDateFormat format; if(value instanceof String) { format = new SimpleDateFormat(entity.getDatabaseFormat());
     * temp = format.parse(value.toString()); } else if(value instanceof Date) { temp = (Date)value; } if(temp != null)
     * { format = new SimpleDateFormat(entity.getFormat()); value = format.format(temp); } return value; }
     */
    public void getAllExcelField(String[] exclusions, String targetId, Field[] fields,
            List<ExcelExportEntity> excelParams, Class<?> pojoClass, List<Method> getMethods) throws Exception {
        List exclusionsList = exclusions != null ? Arrays.asList(exclusions) : null;
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            if (!PoiPublicUtil.isNotUserExcelUserThis(exclusionsList, field, targetId)) {
                if (field.getAnnotation(Excel.class) != null) {
                    Excel excel = field.getAnnotation(Excel.class);
                    String name = PoiPublicUtil.getValueByTargetId(excel.name(), targetId, null);
                    if (StringPoiUtils.isNotBlank(name)) {
                        excelParams.add(createExcelExportEntity(field, targetId, pojoClass, getMethods));
                    }
                    // excelParams.add(this.createExcelExportEntity(field, targetId, pojoClass, getMethods));
                } else if (PoiPublicUtil.isCollection(field.getType())) {
                    ExcelCollection newMethods = field.getAnnotation(ExcelCollection.class);
                    ParameterizedType excel = (ParameterizedType) field.getGenericType();
                    Class clz = (Class) excel.getActualTypeArguments()[0];
                    ArrayList list = new ArrayList();
                    this.getAllExcelField(exclusions,
                            StringPoiUtils.isNotEmpty(newMethods.id()) ? newMethods.id() : targetId,
                            PoiPublicUtil.getClassFields(clz), list, clz, (List) null);
                    ExcelExportEntity excelEntity = new ExcelExportEntity();
                    // excelEntity.setName(this.getExcelName(newMethods.name(), targetId));
                    // excelEntity.setOrderNum(this.getCellOrder(newMethods.orderNum(), targetId));
                    // excelEntity.setMethod(PoiPublicUtil.getMethod(field.getName(), pojoClass));
                    excelEntity.setName(PoiPublicUtil.getValueByTargetId(newMethods.name(), targetId, null));
                    excelEntity.setOrderNum(
                            Integer.valueOf(PoiPublicUtil.getValueByTargetId(newMethods.orderNum(), targetId, "0")));
                    excelEntity.setMethod(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
                    excelEntity.setList(list);
                    excelParams.add(excelEntity);
                } else {
                    ArrayList var15 = new ArrayList();
                    if (getMethods != null) {
                        var15.addAll(getMethods);
                    }
                    // var15.add(PoiPublicUtil.getMethod(field.getName(), pojoClass));
                    var15.add(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
                    ExcelEntity var16 = field.getAnnotation(ExcelEntity.class);
                    this.getAllExcelField(exclusions, StringPoiUtils.isNotEmpty(var16.id()) ? var16.id() : targetId,
                            PoiPublicUtil.getClassFields(field.getType()), excelParams, field.getType(), var15);
                }
            }
        }
    }
    public int getCellOrder(String orderNum, String targetId) {
        if (!this.isInteger(orderNum) && targetId != null) {
            String[] arr = orderNum.split(",");
            String[] arr$ = arr;
            int len$ = arr.length;
            for (int i$ = 0; i$ < len$; ++i$) {
                String str = arr$[i$];
                String[] temp = str.split("_");
                if (targetId.equals(temp[1])) {
                    return Integer.valueOf(temp[0]).intValue();
                }
            }
            return 0;
        } else {
            return Integer.valueOf(orderNum).intValue();
        }
    }
    public Object getCellValue(ExcelExportEntity entity, Object obj) throws Exception {
        Object value;
        if (obj instanceof Map) {
            value = ((Map) obj).get(entity.getKey());
        } else {
            value = entity.getMethods() != null ? this.getFieldBySomeMethod(entity.getMethods(), obj)
                    : entity.getMethod().invoke(obj, new Object[0]);
        }
        if (StringPoiUtils.isNotEmpty(entity.getFormat())) {
            value = this.dateFormatValue(value, entity);
        }
        if (entity.getReplace() != null && entity.getReplace().length > 0) {
            value = this.replaceValue(entity.getReplace(), String.valueOf(value));
        }
        if (StringPoiUtils.isNotEmpty(entity.getNumFormat())) {
            value = numFormatValue(value, entity);
        }
        if (this.needHanlderList != null && this.needHanlderList.contains(entity.getName())) {
            value = this.dataHanlder.exportHandler(obj, entity.getName(), value);
        }
        if (StringPoiUtils.isNotEmpty(entity.getSuffix()) && value != null) {
            value = value + entity.getSuffix();
        }
        return value == null ? "" : value.toString();
    }
    public Collection<?> getListCellValue(ExcelExportEntity entity, Object obj) throws Exception {
        Object value;
        if (obj instanceof Map) {
            value = ((Map) obj).get(entity.getKey());
        } else {
            value = (Collection) entity.getMethod().invoke(obj, new Object[0]);
        }
        return (Collection) value;
    }
    private void getExcelField(String targetId, Field field, ExcelExportEntity excelEntity, Excel excel,
            Class<?> pojoClass) throws Exception {
        // excelEntity.setName(this.getExcelName(excel.name(), targetId));
        excelEntity.setName(PoiPublicUtil.getValueByTargetId(excel.name(), targetId, null));
        excelEntity.setWidth(excel.width());
        excelEntity.setHeight(excel.height());
        excelEntity.setNeedMerge(excel.needMerge());
        excelEntity.setMergeVertical(excel.mergeVertical());
        excelEntity.setMergeRely(excel.mergeRely());
        excelEntity.setReplace(excel.replace());
        // excelEntity.setOrderNum(this.getCellOrder(excel.orderNum(), targetId));
        excelEntity.setOrderNum(Integer.valueOf(PoiPublicUtil.getValueByTargetId(excel.orderNum(), targetId, "0")));
        excelEntity.setWrap(excel.isWrap());
        excelEntity.setExportImageType(excel.imageType());
        excelEntity.setSuffix(excel.suffix());
        excelEntity.setDatabaseFormat(excel.databaseFormat());
        excelEntity.setFormat(StringPoiUtils.isNotEmpty(excel.exportFormat()) ? excel.exportFormat() : excel.format());
        excelEntity.setStatistics(excel.isStatistics());
        excelEntity.setHyperlink(excel.isHyperlink());
        // String fieldname = field.getName();
        // excelEntity.setMethod(PoiPublicUtil.getMethod(fieldname, pojoClass));
        excelEntity.setMethod(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
        excelEntity.setNumFormat(excel.numFormat());
    }
    public String getExcelName(String exportName, String targetId) {
        if (exportName.indexOf(",") < 0) {
            return exportName;
        } else {
            String[] arr = exportName.split(",");
            String[] arr$ = arr;
            int len$ = arr.length;
            for (int i$ = 0; i$ < len$; ++i$) {
                String str = arr$[i$];
                if (str.indexOf(targetId) != -1) {
                    return str.split("_")[0];
                }
            }
            return null;
        }
    }
    public Object getFieldBySomeMethod(List<Method> list, Object t) throws Exception {
        Method m;
        for (Iterator i$ = list.iterator(); i$.hasNext(); t = m.invoke(t, new Object[0])) {
            m = (Method) i$.next();
            if (t == null) {
                t = "";
                break;
            }
        }
        return t;
    }
    public short getRowHeight(List<ExcelExportEntity> excelParams) {
        double maxHeight = 0.0D;
        for (int i = 0; i < excelParams.size(); ++i) {
            maxHeight = maxHeight > (excelParams.get(i)).getHeight() ? maxHeight : (excelParams.get(i)).getHeight();
            if ((excelParams.get(i)).getList() != null) {
                for (int j = 0; j < (excelParams.get(i)).getList().size(); ++j) {
                    maxHeight = maxHeight > ((excelParams.get(i)).getList().get(j)).getHeight() ? maxHeight
                            : ((excelParams.get(i)).getList().get(j)).getHeight();
                }
            }
        }
        return (short) ((int) (maxHeight * 50.0D));
    }
    public boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException var3) {
            return false;
        }
    }
    private Object replaceValue(String[] replace, String value) {
        String[] arr$ = replace;
        int len$ = replace.length;
        for (int i$ = 0; i$ < len$; ++i$) {
            String str = arr$[i$];
            String[] temp = str.split("_");
            if (value.equals(temp[1])) {
                value = temp[0];
                break;
            }
        }
        return value;
    }
    /**
     * 对字段根据用户设置排序
     */
    public void sortAllParams(List<ExcelExportEntity> excelParams) {
        Collections.sort(excelParams);
        Iterator i$ = excelParams.iterator();
        while (i$.hasNext()) {
            ExcelExportEntity entity = (ExcelExportEntity) i$.next();
            if (entity.getList() != null) {
                Collections.sort(entity.getList());
            }
        }
    }
    /**
     * 添加Index列
     * 
     * @param entity
     * @return
     */
    public ExcelExportEntity indexExcelEntity(ExportParams entity) {
        ExcelExportEntity exportEntity = new ExcelExportEntity();
        exportEntity.setOrderNum(0);
        exportEntity.setName(entity.getIndexName());
        exportEntity.setWidth(10);
        exportEntity.setFormat(PoiBaseConstants.IS_ADD_INDEX);
        return exportEntity;
    }
    /**
     * 获取导出报表的字段总长度
     *
     * @param excelParams
     * @return
     */
    public int getFieldLength(List<ExcelExportEntity> excelParams) {
        int length = -1;// 从0开始计算单元格的
        for (ExcelExportEntity entity : excelParams) {
            length += entity.getList() != null ? entity.getList().size() : 1;
        }
        return length;
    }
    /**
     * 判断表头是只有一行还是两行
     *
     * @param excelParams
     * @return
     */
    public int getRowNums(List<ExcelExportEntity> excelParams) {
        for (int i = 0; i < excelParams.size(); i++) {
            if (excelParams.get(i).getList() != null && StringPoiUtils.isNotBlank(excelParams.get(i).getName())) {
                return 2;
            }
        }
        return 1;
    }
}
