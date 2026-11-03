package com.bytter.poi.excel.imports.base;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelCollection;
import com.bytter.poi.annotation.ExcelEntity;
import com.bytter.poi.annotation.ExcelVerify;
import com.bytter.poi.excel.entity.ImportParams;
import com.bytter.poi.excel.entity.params.ExcelCollectionParams;
import com.bytter.poi.excel.entity.params.ExcelImportEntity;
import com.bytter.poi.excel.entity.params.ExcelVerifyEntity;
import com.bytter.poi.util.PoiPublicUtil;
import com.bytter.poi.util.PoiReflectorUtil;
import com.bytter.poi.util.StringPoiUtils;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 导入基础和,普通方法和Sax共用 Created by zhouj on 2017/6/2.
 */
public class ImportBaseService {
    public ImportBaseService() {
    }
    public void addEntityToMap(String targetId, Field field, ExcelImportEntity excelEntity, Class<?> pojoClass,
            List<Method> getMethods, Map<String, ExcelImportEntity> temp) throws Exception {
        Excel excel = field.getAnnotation(Excel.class);
        excelEntity = new ExcelImportEntity();
        excelEntity.setType(excel.type());
        excelEntity.setSaveUrl(excel.savePath());
        excelEntity.setSaveType(excel.imageType());
        excelEntity.setReplace(excel.replace());
        excelEntity.setDatabaseFormat(excel.databaseFormat());
        excelEntity.setSuffix(excel.suffix());
        excelEntity.setNumFormat(excel.numFormat());// 2017年9月19日15:04:26周健添加导入数字格式化
        excelEntity.setImportField(
                Boolean.valueOf(PoiPublicUtil.getValueByTargetId(excel.isImportField(), targetId, "false")));
        excelEntity.setTryTransferDate(excel.isTryTransferDate());
        this.getExcelField(targetId, field, excelEntity, excel, pojoClass);
        if (getMethods != null) {
            List<Method> newMethods = new ArrayList<Method>();
            newMethods.addAll(getMethods);
            newMethods.add(excelEntity.getMethod());
            excelEntity.setMethods(newMethods);
        }
        temp.put(excelEntity.getName(), excelEntity);
    }
    public ExcelVerifyEntity getImportVerify(Field field) {
        ExcelVerify verify = (ExcelVerify) field.getAnnotation(ExcelVerify.class);
        if (verify != null) {
            ExcelVerifyEntity entity = new ExcelVerifyEntity();
            entity.setEmail(verify.isEmail());
            entity.setInterHandler(verify.interHandler());
            entity.setMaxLength(verify.maxLength());
            entity.setMinLength(verify.minLength());
            entity.setMobile(verify.isMobile());
            entity.setNotNull(verify.notNull());
            entity.setRegex(verify.regex());
            entity.setRegexTip(verify.regexTip());
            entity.setTel(verify.isTel());
            return entity;
        } else {
            return null;
        }
    }
    public void getAllExcelField(String targetId, Field[] fields, Map<String, ExcelImportEntity> excelParams,
            List<ExcelCollectionParams> excelCollection, Class<?> pojoClass, List<Method> getMethods) throws Exception {
        ExcelImportEntity excelEntity = null;
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            if (!PoiPublicUtil.isNotUserExcelUserThis(null, field, targetId)) {
                if (PoiPublicUtil.isCollection(field.getType())) {
                    ExcelCollection excel = field.getAnnotation(ExcelCollection.class);
                    ExcelCollectionParams newMethods = new ExcelCollectionParams();
                    newMethods.setName(field.getName());
                    Map<String, ExcelImportEntity> temp = new HashMap<String, ExcelImportEntity>();
                    ParameterizedType pt = (ParameterizedType) field.getGenericType();
                    Class clz = (Class) pt.getActualTypeArguments()[0];
                    newMethods.setType(clz);
                    this.getExcelFieldList(targetId, PoiPublicUtil.getClassFields(clz), clz, temp, (List) null);
                    newMethods.setExcelParams(temp);
                    // newMethods.setExcelName(((ExcelCollection)field.getAnnotation(ExcelCollection.class)).name());
                    newMethods.setExcelName(PoiPublicUtil
                            .getValueByTargetId(field.getAnnotation(ExcelCollection.class).name(), targetId, null));
                    this.additionalCollectionName(newMethods);
                    excelCollection.add(newMethods);
                } else if (PoiPublicUtil.isJavaClass(field)) {
                    this.addEntityToMap(targetId, field, excelEntity, pojoClass, getMethods, excelParams);
                } else {
                    List<Method> newMethods = new ArrayList<Method>();
                    if (getMethods != null) {
                        newMethods.addAll(getMethods);
                    }
                    newMethods.add(PoiReflectorUtil.fromCache(pojoClass).getGetMethod(field.getName()));
                    ExcelEntity excel = field.getAnnotation(ExcelEntity.class);
                    getAllExcelField(StringPoiUtils.isNotEmpty(excel.id()) ? excel.id() : targetId,
                            PoiPublicUtil.getClassFields(field.getType()), excelParams, excelCollection,
                            field.getType(), newMethods);
                    // newMethods.add(PoiPublicUtil.getMethod(field.getName(), pojoClass));
                    // this.getAllExcelField(targetId, PoiPublicUtil.getClassFields(field.getType()), excelParams,
                    // excelCollection, field.getType(), var14);
                }
            }
        }
    }
    private void additionalCollectionName(ExcelCollectionParams collection) {
        HashSet keys = new HashSet();
        keys.addAll(collection.getExcelParams().keySet());
        Iterator i$ = keys.iterator();
        while (i$.hasNext()) {
            String key = (String) i$.next();
            collection.getExcelParams().put(collection.getExcelName() + "_" + key,
                    collection.getExcelParams().get(key));
            collection.getExcelParams().remove(key);
        }
    }
    public void getExcelField(String targetId, Field field, ExcelImportEntity excelEntity, Excel excel,
            Class<?> pojoClass) throws Exception {
        // excelEntity.setName(this.getExcelName(excel.name(), targetId));
        excelEntity.setName(PoiPublicUtil.getValueByTargetId(excel.name(), targetId, null));
        String fieldname = field.getName();
        excelEntity.setMethod(PoiReflectorUtil.fromCache(pojoClass).getSetMethod(fieldname));
        // excelEntity.setMethod(PoiPublicUtil.getMethod(fieldname, pojoClass, field.getType()));
        if (StringPoiUtils.isNotEmpty(excel.importFormat())) {
            excelEntity.setFormat(excel.importFormat());
        } else {
            excelEntity.setFormat(excel.format());
        }
    }
    public void getExcelFieldList(String targetId, Field[] fields, Class<?> pojoClass,
            Map<String, ExcelImportEntity> temp, List<Method> getMethods) throws Exception {
        Object excelEntity = null;
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            if (!PoiPublicUtil.isNotUserExcelUserThis((List) null, field, targetId)) {
                if (PoiPublicUtil.isJavaClass(field)) {
                    this.addEntityToMap(targetId, field, (ExcelImportEntity) excelEntity, pojoClass, getMethods, temp);
                } else {
                    List<Method> newMethods = new ArrayList<Method>();
                    if (getMethods != null) {
                        newMethods.addAll(getMethods);
                    }
                    ExcelEntity excel = field.getAnnotation(ExcelEntity.class);
                    newMethods.add(PoiReflectorUtil.fromCache(pojoClass).getSetMethod(field.getName()));
                    getExcelFieldList(StringPoiUtils.isNotEmpty(excel.id()) ? excel.id() : targetId,
                            PoiPublicUtil.getClassFields(field.getType()), field.getType(), temp, newMethods);
                    // newMethods.add(PoiPublicUtil.getMethod(field.getName(), pojoClass, field.getType()));
                    // this.getExcelFieldList(targetId, PoiPublicUtil.getClassFields(field.getType()), field.getType(),
                    // temp, newMethods);
                }
            }
        }
    }
    public String getExcelName(String exportName, String targetId) {
        if (exportName.indexOf("_") < 0) {
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
        for (int i = 0; i < list.size() - 1; i++) {
            m = list.get(i);
            t = m.invoke(t, new Object[] {});
        }
        return t;
    }
    public void saveThisExcel(ImportParams params, Class<?> pojoClass, boolean isXSSFWorkbook, Workbook book)
            throws Exception {
        String path = PoiPublicUtil.getWebRootPath(this.getSaveExcelUrl(params, pojoClass));
        File savefile = new File(path);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyMMddHHmmss");
        FileOutputStream fos = new FileOutputStream(path + "/" + format.format(new Date()) + "_"
                + Math.round(Math.random() * 100000.0D) + (isXSSFWorkbook ? ".xlsx" : ".xls"));
        book.write(fos);
        fos.close();
    }
    public String getSaveExcelUrl(ImportParams params, Class<?> pojoClass) throws Exception {
        String url = "";
        if ("upload/excelUpload".equals(params.getSaveUrl())) {
            url = pojoClass.getName().split("\\.")[pojoClass.getName().split("\\.").length - 1];
            return params.getSaveUrl() + "/" + url;
        } else {
            return params.getSaveUrl();
        }
    }
    public void setFieldBySomeMethod(List<Method> setMethods, Object object, Object value) throws Exception {
        Object t = getFieldBySomeMethod(setMethods, object);
        setMethods.get(setMethods.size() - 1).invoke(t, value);
    }
    public void setValues(ExcelImportEntity entity, Object object, Object value) throws Exception {
        if (entity.getMethods() != null) {
            this.setFieldBySomeMethod(entity.getMethods(), object, value);
        } else {
            entity.getMethod().invoke(object, new Object[] { value });
        }
    }
}
